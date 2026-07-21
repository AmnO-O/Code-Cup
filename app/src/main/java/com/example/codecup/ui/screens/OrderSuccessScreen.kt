package com.example.codecup.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.codecup.ui.components.PrimaryButton
import com.example.codecup.ui.components.SecondaryButton
import com.example.codecup.ui.theme.CoffeeBackground
import com.example.codecup.ui.theme.CoffeeOnSurface
import com.example.codecup.ui.theme.CoffeeOnSurfaceVariant
import com.example.codecup.ui.theme.CoffeePrimary
import com.example.codecup.ui.theme.CoffeeStampGreen

@Composable
fun OrderSuccessScreen(
    onTrackOrderClick: () -> Unit,
    onBackToHomeClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = CoffeeBackground
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                modifier = Modifier.size(96.dp),
                tint = CoffeeStampGreen
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Order placed!",
                style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Bold),
                color = CoffeeOnSurface,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Your daily ritual is being prepared.\nOrder #1042 · Ready in ~8 min",
                style = MaterialTheme.typography.bodyLarge,
                color = CoffeeOnSurfaceVariant,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(48.dp))
            
            PrimaryButton(onClick = onTrackOrderClick) {
                Text("Track My Order")
            }
            Spacer(modifier = Modifier.height(12.dp))
            TextButton(onClick = onBackToHomeClick) {
                Text(
                    text = "Back to Home",
                    style = MaterialTheme.typography.labelLarge,
                    color = CoffeePrimary
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OrderSuccessPreview() {
    com.example.codecup.ui.theme.CodeCupTheme {
        OrderSuccessScreen(onTrackOrderClick = {}, onBackToHomeClick = {})
    }
}
