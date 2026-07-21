package com.example.codecup.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Coffee
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.codecup.ui.theme.CoffeeBackground
import com.example.codecup.ui.theme.CoffeeOnSurfaceVariant
import com.example.codecup.ui.theme.CoffeePrimary
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onTimeout: () -> Unit) {
    LaunchedEffect(Unit) {
        delay(1500) // Minimum 1.5s splash
        onTimeout()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CoffeeBackground),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.Coffee,
                contentDescription = null,
                modifier = Modifier.size(96.dp),
                tint = CoffeePrimary
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Artisan Coffee",
                style = MaterialTheme.typography.displayMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = CoffeePrimary
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Your daily ritual, ready in seconds",
                style = MaterialTheme.typography.bodyMedium,
                color = CoffeeOnSurfaceVariant
            )
        }

        CircularProgressIndicator(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 48.dp)
                .size(32.dp),
            color = CoffeePrimary,
            strokeWidth = 3.dp
        )
    }
}
