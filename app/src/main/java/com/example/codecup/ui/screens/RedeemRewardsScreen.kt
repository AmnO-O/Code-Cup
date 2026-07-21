package com.example.codecup.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.codecup.models.Product
import com.example.codecup.models.sampleProducts
import com.example.codecup.ui.components.AppHeader
import com.example.codecup.ui.components.PrimaryButton
import com.example.codecup.ui.theme.*

@Composable
fun RedeemRewardsScreen(
    onBackClick: () -> Unit
) {
    val pointsBalance = 1240
    
    Scaffold(
        topBar = {
            AppHeader(title = "Redeem Rewards", onBackClick = onBackClick)
        },
        containerColor = CoffeeBackground
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Balance Banner
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    color = CoffeeAccentContainer
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "You have ",
                            style = MaterialTheme.typography.bodyLarge,
                            color = CoffeeOnSurface
                        )
                        Text(
                            text = "$pointsBalance points",
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                            color = CoffeeSecondary
                        )
                    }
                }
            }
            
            items(sampleProducts) { product ->
                val pointCost = (product.price * 25).toInt() // Example: $1 = 25 pts
                val canRedeem = pointsBalance >= pointCost
                
                RedeemItemRow(
                    product = product,
                    pointCost = pointCost,
                    canRedeem = canRedeem
                )
            }
        }
    }
}

@Composable
fun RedeemItemRow(
    product: Product,
    pointCost: Int,
    canRedeem: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = CoffeeSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, CoffeeOutline)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = product.imageUrl,
                contentDescription = product.name,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = CoffeeOnSurface
                )
                Text(
                    text = "$pointCost pts",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = CoffeeSecondary
                )
                if (!canRedeem) {
                    Text(
                        text = "Need ${pointCost - 1240} more pts",
                        style = MaterialTheme.typography.labelSmall,
                        color = CoffeeSecondary.copy(alpha = 0.6f)
                    )
                }
            }
            PrimaryButton(
                onClick = { },
                modifier = Modifier.width(100.dp),
                enabled = canRedeem
            ) {
                Text("Redeem", style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RedeemRewardsPreview() {
    CodeCupTheme {
        RedeemRewardsScreen(onBackClick = {})
    }
}
