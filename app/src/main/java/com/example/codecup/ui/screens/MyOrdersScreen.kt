package com.example.codecup.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.codecup.models.Order
import com.example.codecup.models.OrderStatus
import com.example.codecup.models.sampleOrders
import com.example.codecup.ui.components.AppHeader
import com.example.codecup.ui.components.BottomNavBar
import com.example.codecup.ui.components.NavDestination
import com.example.codecup.ui.components.SecondaryButton
import com.example.codecup.ui.theme.*

@Composable
fun MyOrdersScreen(
    onNavigate: (String) -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Ongoing", "History")
    
    val filteredOrders = if (selectedTab == 0) {
        sampleOrders.filter { it.status != OrderStatus.PickedUp }
    } else {
        sampleOrders.filter { it.status == OrderStatus.PickedUp }
    }

    Scaffold(
        topBar = {
            Column {
                AppHeader(title = "My Orders")
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = CoffeeBackground,
                    contentColor = CoffeePrimary,
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                            color = CoffeePrimary
                        )
                    },
                    divider = { HorizontalDivider(color = CoffeeOutlineVariant) }
                ) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            text = { 
                                Text(
                                    text = title,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal
                                ) 
                            }
                        )
                    }
                }
            }
        },
        bottomBar = {
            BottomNavBar(currentRoute = NavDestination.Orders.route, onNavigate = onNavigate)
        },
        containerColor = CoffeeBackground
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(filteredOrders) { order ->
                OrderCard(order)
            }
        }
    }
}

@Composable
fun OrderCard(order: Order) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = CoffeeSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, CoffeeOutline)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Order #${order.id}",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = CoffeeOnSurface
                    )
                    Text(
                        text = order.date,
                        style = MaterialTheme.typography.bodySmall,
                        color = CoffeeOnSurfaceVariant
                    )
                }
                StatusChip(status = order.status)
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = CoffeeOutlineVariant.copy(alpha = 0.5f))
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                text = order.itemsSummary,
                style = MaterialTheme.typography.bodyMedium,
                color = CoffeeOnSurfaceVariant
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "$${"%.2f".format(order.totalPrice)}",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = CoffeePrimary
                )
                
                if (order.status == OrderStatus.Ready) {
                    SecondaryButton(
                        onClick = { },
                        modifier = Modifier.width(160.dp)
                    ) {
                        Text("Mark as Picked Up", style = MaterialTheme.typography.labelSmall)
                    }
                }
            }
        }
    }
}

@Composable
fun StatusChip(status: OrderStatus) {
    val containerColor = when (status) {
        OrderStatus.Preparing -> CoffeeSecondaryContainer.copy(alpha = 0.2f)
        OrderStatus.Ready -> CoffeeStampGreen.copy(alpha = 0.2f)
        OrderStatus.PickedUp -> CoffeeSurfaceContainerLow
    }
    val contentColor = when (status) {
        OrderStatus.Preparing -> CoffeeSecondary
        OrderStatus.Ready -> CoffeeStampGreen
        OrderStatus.PickedUp -> CoffeeOnSurfaceVariant
    }
    
    Surface(
        color = containerColor,
        contentColor = contentColor,
        shape = RoundedCornerShape(999.dp)
    ) {
        Text(
            text = status.name,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MyOrdersPreview() {
    CodeCupTheme {
        MyOrdersScreen(onNavigate = {})
    }
}
