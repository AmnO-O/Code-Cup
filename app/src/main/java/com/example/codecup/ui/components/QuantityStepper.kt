package com.example.codecup.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.codecup.ui.theme.CoffeeOnSurface
import com.example.codecup.ui.theme.CoffeePrimary
import com.example.codecup.ui.theme.CoffeeSurfaceContainerLow

@Composable
fun QuantityStepper(
    quantity: Int,
    onQuantityChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.clip(RoundedCornerShape(999.dp)),
        color = CoffeeSurfaceContainerLow
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(4.dp)
        ) {
            IconButton(
                onClick = { if (quantity > 1) onQuantityChange(quantity - 1) },
                modifier = Modifier.size(32.dp),
                enabled = quantity > 1
            ) {
                Icon(
                    imageVector = Icons.Default.Remove,
                    contentDescription = "Decrease",
                    tint = if (quantity > 1) CoffeePrimary else CoffeePrimary.copy(alpha = 0.38f),
                    modifier = Modifier.size(18.dp)
                )
            }
            
            Text(
                text = quantity.toString(),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = CoffeeOnSurface,
                modifier = Modifier.padding(horizontal = 12.dp)
            )
            
            IconButton(
                onClick = { onQuantityChange(quantity + 1) },
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Increase",
                    tint = CoffeePrimary,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}
