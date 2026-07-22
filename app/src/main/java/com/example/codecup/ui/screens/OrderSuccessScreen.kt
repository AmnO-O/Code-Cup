package com.example.codecup.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.codecup.ui.components.ConfettiEffect
import com.example.codecup.ui.components.PrimaryButton
import com.example.codecup.ui.theme.*

@Composable
fun OrderSuccessScreen(
    orderId: String,
    onTrackOrderClick: () -> Unit,
    onBackToHomeClick: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = CoffeeBackground
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 48.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Success Icon Circle
                Surface(
                    modifier = Modifier
                        .size(96.dp)
                        .shadow(4.dp, CircleShape),
                    shape = CircleShape,
                    color = CoffeeSurface
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = CoffeeSecondary
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(32.dp))
                
                Text(
                    text = "Order Placed!",
                    style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Bold),
                    color = CoffeePrimary,
                    textAlign = TextAlign.Center
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                Text(
                    text = "Your artisan coffee is being crafted with care. We'll let you know when it's on the way.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = CoffeeOnSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
                
                Spacer(modifier = Modifier.height(32.dp))
                
                // Order Number Card
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = CoffeeSurface,
                    border = androidx.compose.foundation.BorderStroke(1.dp, CoffeeOutline)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "ORDER NUMBER",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Medium,
                                letterSpacing = 2.sp
                            ),
                            color = CoffeeOnSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "#$orderId",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 4.sp
                            ),
                            color = CoffeePrimary
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(48.dp))
                
                // Actions
                PrimaryButton(
                    onClick = onTrackOrderClick,
                    modifier = Modifier.fillMaxWidth().height(56.dp)
                ) {
                    Text("Track My Order")
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Button(
                    onClick = onBackToHomeClick,
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CoffeePrimaryContainer,
                        contentColor = CoffeeOnPrimaryContainer
                    )
                ) {
                    Text("Back to Home", fontWeight = FontWeight.Bold)
                }
            }
        }
        
        ConfettiEffect(modifier = Modifier.fillMaxSize())
    }
}

@Preview(showBackground = true)
@Composable
fun OrderSuccessPreview() {
    CodeCupTheme {
        OrderSuccessScreen(orderId = "AC-78294", onTrackOrderClick = {}, onBackToHomeClick = {})
    }
}
