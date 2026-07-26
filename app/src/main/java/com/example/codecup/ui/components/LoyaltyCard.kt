package com.example.codecup.ui.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
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
    val isFull = stampsEarned >= totalStamps

    // Full card breathes with a green glow so "ready to redeem" is unmissable (ui_design §3.6)
    val glowAlpha = if (isFull) {
        rememberInfiniteTransition(label = "fullCardGlow").animateFloat(
            initialValue = 0.35f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(tween(700), RepeatMode.Reverse),
            label = "glowAlpha"
        ).value
    } else {
        0f
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        border = if (isFull) BorderStroke(2.dp, CoffeeStampGreen.copy(alpha = glowAlpha)) else null,
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
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = if (isFull) {
                                "Tap to redeem your free drink!"
                            } else {
                                "${totalStamps - stampsEarned} stamps until a free drink"
                            },
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = if (isFull) FontWeight.Bold else FontWeight.Normal
                            ),
                            color = if (isFull) {
                                MaterialTheme.colorScheme.onPrimaryContainer
                            } else {
                                MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                            }
                        )
                    }
                    Surface(
                        color = MaterialTheme.colorScheme.surface,
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
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "$stampsEarned / $totalStamps",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.primary
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
    // No animation on first composition; a stamp earned while visible pops in
    // with a small bounce (ui_design §6)
    val scale by animateFloatAsState(
        targetValue = if (filled) 1f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMediumLow
        ),
        label = "stampPop"
    )

    Box(
        modifier = Modifier
            .size(36.dp)
            .clip(CircleShape)
            .then(
                if (!filled) Modifier.border(1.dp, MaterialTheme.colorScheme.outlineVariant, CircleShape)
                else Modifier
            ),
        contentAlignment = Alignment.Center
    ) {
        if (scale > 0.01f) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                    }
                    .background(CoffeeStampGreen, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.LocalCafe,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = Color.White
                )
            }
        }
    }
}
