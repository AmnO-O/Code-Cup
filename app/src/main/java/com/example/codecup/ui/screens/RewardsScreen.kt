package com.example.codecup.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.codecup.ui.components.*
import com.example.codecup.ui.theme.*

@Composable
fun RewardsScreen(
    onNavigate: (String) -> Unit,
    onRedeemClick: () -> Unit
) {
    Scaffold(
        topBar = {
            AppHeader(
                title = "Rewards",
                onBackClick = { onNavigate("home") }
            )
        },
        bottomBar = {
            BottomNavBar(currentRoute = NavDestination.Rewards.route, onNavigate = onNavigate)
        },
        containerColor = CoffeeBackground
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Loyalty Card
            item {
                LoyaltyCard(stampsEarned = 5)
            }
            
            // Points Banner
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = CoffeeAccentContainer)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = Color.White,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.size(48.dp)
                        ) {
                            Icon(
                                Icons.Default.Stars,
                                contentDescription = null,
                                modifier = Modifier.padding(8.dp),
                                tint = CoffeeSecondary
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Total Points",
                                style = MaterialTheme.typography.labelSmall,
                                color = CoffeeOnSurfaceVariant
                            )
                            Text(
                                text = "1,240 pts",
                                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                                color = CoffeePrimary
                            )
                        }
                        TextButton(onClick = onRedeemClick) {
                            Text("Redeem", color = CoffeeSecondary, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
            
            // Points History
            item {
                Text(
                    text = "Points History",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = CoffeeOnSurface
                )
            }
            
            items(samplePointsHistory) { history ->
                PointsHistoryRow(history)
            }
        }
    }
}

data class PointsHistory(
    val title: String,
    val date: String,
    val points: String
)

val samplePointsHistory = listOf(
    PointsHistory("Order #1042", "21 July, 21:15", "+8 pts"),
    PointsHistory("Order #1041", "20 July, 09:30", "+5 pts"),
    PointsHistory("Redeemed Caramel Macchiato", "18 July, 14:20", "-150 pts"),
    PointsHistory("Order #1039", "18 July, 10:15", "+12 pts")
)

@Composable
fun PointsHistoryRow(history: PointsHistory) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = history.title,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                color = CoffeeOnSurface
            )
            Text(
                text = history.date,
                style = MaterialTheme.typography.bodySmall,
                color = CoffeeOnSurfaceVariant
            )
        }
        Text(
            text = history.points,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = if (history.points.startsWith("+")) CoffeeStampGreen else CoffeeSecondary
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RewardsPreview() {
    CodeCupTheme {
        RewardsScreen(onNavigate = {}, onRedeemClick = {})
    }
}
