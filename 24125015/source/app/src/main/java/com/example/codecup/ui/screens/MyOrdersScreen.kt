package com.example.codecup.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.codecup.models.Order
import com.example.codecup.models.OrderStatus
import com.example.codecup.ui.components.*
import com.example.codecup.ui.theme.*
import com.example.codecup.ui.viewmodels.MyOrdersViewModel
import com.example.codecup.ui.viewmodels.ViewModelFactory
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val HIGHLIGHT_HOLD_MS = 450L
private const val HIGHLIGHT_FADE_MS = 1400

@Composable
fun MyOrdersScreen(
    onNavigate: (String) -> Unit,
    highlightOrderId: String? = null,
    viewModel: MyOrdersViewModel = viewModel(factory = ViewModelFactory(context = LocalContext.current))
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedTab by androidx.compose.runtime.saveable.rememberSaveable { mutableStateOf(0) }
    val tabs = listOf("Ongoing", "History")
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    // One-shot events from the ViewModel ("+1 stamp earned!", "Order added to cart")
    LaunchedEffect(Unit) {
        viewModel.events.collect { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AppDrawer(
                currentRoute = "orders",
                onNavigate = onNavigate,
                onCloseDrawer = { scope.launch { drawerState.close() } }
            )
        }
    ) {
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            topBar = {
                AppHeader(
                    title = "My Orders",
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    }
                )
            },
            bottomBar = {
                BottomNavBar(currentRoute = NavDestination.Orders.route, onNavigate = onNavigate)
            },
            containerColor = MaterialTheme.colorScheme.background
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
            Spacer(modifier = Modifier.height(16.dp))
            
            // Tabs
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.primary,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                divider = {}
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                title,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        selectedContentColor = MaterialTheme.colorScheme.primary,
                        unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            if (selectedTab == 0) {
                OngoingOrdersList(
                    orders = uiState.ongoingOrders,
                    highlightOrderId = highlightOrderId,
                    onMarkPickedUp = { viewModel.markAsPickedUp(it) },
                    onCancelOrder = { viewModel.cancelOrder(it) },
                    onNotReadyClick = {
                        scope.launch {
                            snackbarHostState.showSnackbar("Please wait a moment, your drink is being prepared!")
                        }
                    },
                    onOrderNow = { onNavigate("home") }
                )
            } else {
                OrdersHistoryList(
                    orders = uiState.orderHistory,
                    onReorder = { viewModel.reorder(it) }
                )
            }
        }
    }
}
}

@Composable
fun OngoingOrdersList(
    orders: List<Order>,
    highlightOrderId: String?,
    onMarkPickedUp: (String) -> Unit,
    onCancelOrder: (String) -> Unit,
    onNotReadyClick: () -> Unit,
    onOrderNow: () -> Unit
) {
    if (orders.isEmpty()) {
        EmptyState(
            title = "No active orders",
            description = "Hungry for a brew? Your next order will show up here.",
            icon = Icons.Default.LocalCafe,
            action = {
                PrimaryButton(onClick = onOrderNow, modifier = Modifier.width(180.dp)) {
                    Text("Order Now")
                }
            }
        )
    } else {
        LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            items(orders, key = { it.id }) { order ->
                val currentOrder by rememberUpdatedState(order)
                val dismissState = rememberSwipeToDismissBoxState(
                    confirmValueChange = {
                        if (it == SwipeToDismissBoxValue.EndToStart && currentOrder.isCancellable) {
                            onCancelOrder(currentOrder.id)
                            true
                        } else {
                            false
                        }
                    }
                )

                SwipeToDismissBox(
                    state = dismissState,
                    modifier = Modifier.animateItem(),
                    backgroundContent = {
                        val color = if (dismissState.dismissDirection == SwipeToDismissBoxValue.EndToStart && currentOrder.isCancellable) {
                            MaterialTheme.colorScheme.error.copy(alpha = 0.8f)
                        } else {
                            Color.Transparent
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(color, RoundedCornerShape(16.dp))
                                .padding(horizontal = 20.dp),
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            if (currentOrder.isCancellable) {
                                Icon(
                                    Icons.Default.Delete,
                                    contentDescription = "Cancel Order",
                                    tint = Color.White
                                )
                            }
                        }
                    },
                    enableDismissFromStartToEnd = false
                ) {
                    OngoingOrderCard(
                        order = order,
                        isHighlighted = order.id == highlightOrderId,
                        onMarkPickedUp = onMarkPickedUp,
                        onNotReadyClick = onNotReadyClick
                    )
                }
            }
            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}

@Composable
fun OngoingOrderCard(
    order: Order,
    onMarkPickedUp: (String) -> Unit,
    onNotReadyClick: () -> Unit,
    isHighlighted: Boolean = false
) {
    var expanded by remember { mutableStateOf(false) }

    // One-time background pulse on the order the user just placed (ui_design §3.5)
    val highlightFraction = remember { Animatable(if (isHighlighted) 1f else 0f) }
    LaunchedEffect(Unit) {
        if (isHighlighted) {
            delay(HIGHLIGHT_HOLD_MS)
            highlightFraction.animateTo(0f, tween(HIGHLIGHT_FADE_MS))
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded },
        shape = RoundedCornerShape(16.dp),
        color = lerp(
            MaterialTheme.colorScheme.surface,
            MaterialTheme.colorScheme.tertiaryContainer,
            highlightFraction.value
        ),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
    ) {
        Column {
            // Top Status Indicator
            Box(modifier = Modifier.fillMaxWidth().height(4.dp).background(MaterialTheme.colorScheme.secondary))
            
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column {
                        Text(
                            text = order.status.name.uppercase(),
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = MaterialTheme.colorScheme.secondary,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                        )
                        Text(
                            text = "Order #${order.id}",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "$${"%.2f".format(order.totalPrice)}",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = order.date,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                
                AnimatedVisibility(
                    visible = expanded,
                    enter = expandVertically(),
                    exit = shrinkVertically()
                ) {
                    Column {
                        Spacer(modifier = Modifier.height(12.dp))
                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                        Spacer(modifier = Modifier.height(12.dp))

                        // Items
                        order.items.forEach { item ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "${item.quantity}x ${item.product.name}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "$${"%.2f".format(item.totalPrice)}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        DeliveryAddressSection(
                            address = order.deliveryAddress,
                            onEditClick = null,
                            titleStyle = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            ),
                            titleColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            iconColor = MaterialTheme.colorScheme.secondary
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Progress
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                StatusText("Received", isReached = true)
                                StatusText(
                                    "Preparing",
                                    isReached = order.status == OrderStatus.Preparing || order.status == OrderStatus.Ready,
                                    isCurrent = order.status == OrderStatus.Preparing
                                )
                                StatusText(
                                    "Ready",
                                    isReached = order.status == OrderStatus.Ready,
                                    isCurrent = order.status == OrderStatus.Ready
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            LinearProgressIndicator(
                                progress = {
                                    when (order.status) {
                                        OrderStatus.Received -> 0.1f
                                        OrderStatus.Preparing -> 0.5f
                                        OrderStatus.Ready -> 1f
                                        else -> 0f
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp)
                                    .clip(RoundedCornerShape(4.dp)),
                                color = MaterialTheme.colorScheme.secondary,
                                trackColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        val isReady = order.status == OrderStatus.Ready
                        PrimaryButton(
                            onClick = {
                                if (isReady) {
                                    onMarkPickedUp(order.id)
                                } else {
                                    onNotReadyClick()
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            containerColor = if (isReady) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                            contentColor = if (isReady) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                        ) {
                            if (isReady) {
                                Icon(
                                    Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Mark as Picked Up")
                            } else {
                                Text("Preparing...")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StatusText(text: String, isReached: Boolean, isCurrent: Boolean = false) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelSmall,
        color = if (isCurrent) MaterialTheme.colorScheme.secondary else if (isReached) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant,
        fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal
    )
}

@Composable
fun OrdersHistoryList(orders: List<Order>, onReorder: (Order) -> Unit) {
    if (orders.isEmpty()) {
        EmptyState(
            title = "No past orders yet",
            description = "Completed orders move here from the Ongoing tab.",
            icon = Icons.AutoMirrored.Filled.ReceiptLong
        )
    } else {
        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(orders) { order ->
                HistoryOrderCard(order = order, onReorder = onReorder)
            }
            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}

@Composable
fun HistoryOrderCard(order: Order, onReorder: (Order) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded },
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    modifier = Modifier.size(64.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            Icons.Default.LocalCafe,
                            contentDescription = null,
                            modifier = Modifier.size(32.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Order #${order.id}",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "$${"%.2f".format(order.totalPrice)}",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Text(
                        text = order.date,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                Column {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = order.itemsSummary,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    SecondaryButton(
                        onClick = { onReorder(order) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            Icons.Default.Replay,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Reorder Items")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MyOrdersPreview() {
    CodeCupTheme {
        MyOrdersScreen(onNavigate = {})
    }
}
