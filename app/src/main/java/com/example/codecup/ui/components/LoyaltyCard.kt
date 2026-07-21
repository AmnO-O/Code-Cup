package com.example.codecup.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Coffee
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.codecup.ui.theme.*

@Composable
fun LoyaltyCard(
    stampsEarned: Int,
    totalStamps: Int = 8,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CoffeePrimaryContainer),
        onClick = onClick
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Background Coffee Icon
            Icon(
                Icons.Default.Coffee,
                contentDescription = null,
                modifier = Modifier
                    .size(140.dp)
                    .align(Alignment.TopEnd)
                    .offset(x = 40.dp, y = (-40).dp),
                tint = Color.White.copy(alpha = 0.1f)
            )

            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column {
                        Text(
                            text = "Loyalty Rewards",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White
                        )
                        Text(
                            text = "${totalStamps - stampsEarned} stamps until a free drink",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                    Surface(
                        color = CoffeeSurface,
                        shape = CircleShape,
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                        ) {
                            Icon(
                                Icons.Default.Star,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = CoffeePrimary
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "$stampsEarned / $totalStamps",
                                style = MaterialTheme.typography.labelLarge,
                                color = CoffeePrimary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    repeat(totalStamps) { index ->
                        val filled = index < stampsEarned
                        StampCircle(filled = filled)
                    }
                }
            }
        }
    }
}

@Composable
fun StampCircle(filled: Boolean) {
    Box(
        modifier = Modifier
            .size(36.dp)
            .clip(CircleShape)
            .then(
                if (filled) Modifier.background(CoffeeStampGreen)
                else Modifier.border(1.dp, CoffeeOutlineVariant, CircleShape)
            ),
        contentAlignment = Alignment.Center
    ) {
        if (filled) {
            Icon(
                Icons.Default.LocalCafe,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = Color.White
            )
        }
    }
}
