package com.example.codecup.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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

@Composable
fun MyOrdersScreen(
    onNavigate: (String) -> Unit,
    viewModel: MyOrdersViewModel = viewModel(factory = ViewModelFactory())
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Ongoing", "History")

    Scaffold(
        topBar = {
            AppHeader(title = "My Orders")
        },
        bottomBar = {
            BottomNavBar(currentRoute = NavDestination.Orders.route, onNavigate = onNavigate)
        },
        containerColor = CoffeeBackground
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
                contentColor = CoffeePrimary,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = CoffeePrimary
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
                        selectedContentColor = CoffeePrimary,
                        unselectedContentColor = CoffeeOnSurfaceVariant
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            if (selectedTab == 0) {
                OngoingOrdersList(uiState.ongoingOrders, onMarkPickedUp = { viewModel.markAsPickedUp(it) })
            } else {
                OrdersHistoryList(uiState.orderHistory)
            }
        }
    }
}

@Composable
fun OngoingOrdersList(orders: List<Order>, onMarkPickedUp: (String) -> Unit) {
    if (orders.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No active orders", color = CoffeeOnSurfaceVariant)
        }
    } else {
        LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            items(orders) { order ->
                OngoingOrderCard(order, onMarkPickedUp)
            }
            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}

@Composable
fun OngoingOrderCard(order: Order, onMarkPickedUp: (String) -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = CoffeeSurface,
        border = androidx.compose.foundation.BorderStroke(1.dp, CoffeeOutline)
    ) {
        Column {
            // Top Status Indicator
            Box(modifier = Modifier.fillMaxWidth().height(4.dp).background(CoffeeSecondary))
            
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
                                color = CoffeeSecondary,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                        )
                        Text(
                            text = "Order #${order.id}",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = CoffeePrimary
                        )
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "$${"%.2f".format(order.totalPrice)}",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = CoffeePrimary
                        )
                        Text(
                            text = order.date,
                            style = MaterialTheme.typography.bodySmall,
                            color = CoffeeOnSurfaceVariant
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(color = CoffeeOutlineVariant)
                Spacer(modifier = Modifier.height(12.dp))
                
                // Items
                order.items.forEach { item ->
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "${item.quantity}x ${item.product.name}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = CoffeeOnSurface
                        )
                        Text(
                            text = "$${"%.2f".format(item.totalPrice)}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = CoffeeOnSurface
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Progress
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        StatusText("Received", isReached = true)
                        StatusText("Preparing", isReached = order.status == OrderStatus.Preparing || order.status == OrderStatus.Ready, isCurrent = order.status == OrderStatus.Preparing)
                        StatusText("Ready", isReached = order.status == OrderStatus.Ready, isCurrent = order.status == OrderStatus.Ready)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    LinearProgressIndicator(
                        progress = {
                            when(order.status) {
                                OrderStatus.Received -> 0.1f
                                OrderStatus.Preparing -> 0.5f
                                OrderStatus.Ready -> 1f
                                else -> 0f
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)),
                        color = CoffeeSecondary,
                        trackColor = CoffeeSurfaceContainerHigh
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                PrimaryButton(
                    onClick = { onMarkPickedUp(order.id) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Mark as Picked Up")
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
        color = if (isCurrent) CoffeeSecondary else if (isReached) CoffeeOnSurface else CoffeeOnSurfaceVariant,
        fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal
    )
}

@Composable
fun OrdersHistoryList(orders: List<Order>) {
    if (orders.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No order history", color = CoffeeOnSurfaceVariant)
        }
    } else {
        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(orders) { order ->
                HistoryOrderCard(order)
            }
            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}

@Composable
fun HistoryOrderCard(order: Order) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = CoffeeSurface,
        border = androidx.compose.foundation.BorderStroke(1.dp, CoffeeOutline)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(64.dp),
                shape = RoundedCornerShape(12.dp),
                color = CoffeeSurfaceContainerLow
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        Icons.Default.LocalCafe,
                        contentDescription = null,
                        modifier = Modifier.size(32.dp),
                        tint = CoffeeOnSurfaceVariant
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
                        color = CoffeePrimary
                    )
                    Text(
                        text = "$${"%.2f".format(order.totalPrice)}",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = CoffeePrimary
                    )
                }
                Text(
                    text = order.date,
                    style = MaterialTheme.typography.bodySmall,
                    color = CoffeeOnSurfaceVariant
                )
                Text(
                    text = order.itemsSummary,
                    style = MaterialTheme.typography.bodyMedium,
                    color = CoffeeOnSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            
            Spacer(modifier = Modifier.width(8.dp))
            
            IconButton(onClick = { /* Reorder logic */ }) {
                Icon(Icons.Default.Replay, contentDescription = "Reorder", tint = CoffeeSecondary)
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
