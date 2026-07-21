package com.example.codecup.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.codecup.models.Product
import com.example.codecup.models.sampleProducts
import com.example.codecup.ui.components.*
import com.example.codecup.ui.theme.*

@Composable
fun ProductDetailsScreen(
    productId: Int,
    onBackClick: () -> Unit,
    onAddToCartClick: () -> Unit
) {
    val product = sampleProducts.find { it.id == productId } ?: sampleProducts[0]
    var quantity by remember { mutableStateOf(1) }
    var selectedSize by remember { mutableStateOf("Medium (12oz)") }
    var selectedShots by remember { mutableStateOf("Double") }
    var selectedIce by remember { mutableStateOf("Regular Ice") }

    Scaffold(
        topBar = {
            AppHeader(
                title = "Details",
                onBackClick = onBackClick,
                actions = {
                    IconButton(onClick = { }) {
                        Icon(Icons.Default.FavoriteBorder, contentDescription = "Favorite", tint = CoffeePrimary)
                    }
                }
            )
        },
        bottomBar = {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets.navigationBars),
                color = CoffeeSurface,
                tonalElevation = 8.dp,
                border = BorderStroke(1.dp, CoffeeOutlineVariant.copy(alpha = 0.5f))
            ) {
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Total Price",
                            style = MaterialTheme.typography.labelSmall,
                            color = CoffeeOnSurfaceVariant
                        )
                        Text(
                            text = "$${"%.2f".format(product.price * quantity)}",
                            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                            color = CoffeeOnSurface
                        )
                    }
                    PrimaryButton(
                        onClick = onAddToCartClick,
                        modifier = Modifier.width(200.dp)
                    ) {
                        Text("Add to Cart")
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(Icons.Default.ShoppingCart, contentDescription = null, modifier = Modifier.size(20.dp))
                    }
                }
            }
        },
        containerColor = CoffeeBackground
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Hero Image
            AsyncImage(
                model = product.imageUrl,
                contentDescription = product.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .clip(RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)),
                contentScale = ContentScale.Crop
            )

            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Bold),
                    color = CoffeeOnSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = product.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = CoffeeOnSurfaceVariant
                )

                Spacer(modifier = Modifier.height(24.dp))
                HorizontalDivider(color = CoffeeOutlineVariant)
                Spacer(modifier = Modifier.height(24.dp))

                // Customization: Size
                CustomizationSection(
                    title = "Size",
                    options = listOf("Small (8oz)", "Medium (12oz)", "Large (16oz)"),
                    selectedOption = selectedSize,
                    onOptionSelected = { selectedSize = it }
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Customization: Espresso Shots
                CustomizationSection(
                    title = "Espresso Shots",
                    options = listOf("Single", "Double", "Triple (+$0.80)"),
                    selectedOption = selectedShots,
                    onOptionSelected = { selectedShots = it }
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Customization: Ice Level
                CustomizationSection(
                    title = "Ice Level",
                    options = listOf("No Ice", "Light Ice", "Regular Ice", "Extra Ice"),
                    selectedOption = selectedIce,
                    onOptionSelected = { selectedIce = it }
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Quantity
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(CoffeeSurface, RoundedCornerShape(12.dp))
                        .border(1.dp, CoffeeOutline, RoundedCornerShape(12.dp))
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Quantity",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = CoffeeOnSurface
                    )
                    QuantityStepper(
                        quantity = quantity,
                        onQuantityChange = { quantity = it }
                    )
                }
                
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun CustomizationSection(
    title: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = CoffeeOnSurface
        )
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            options.forEach { option ->
                val isSelected = option == selectedOption
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .clickable { onOptionSelected(option) },
                    shape = RoundedCornerShape(999.dp),
                    color = if (isSelected) CoffeePrimaryContainer else Color.Transparent,
                    contentColor = if (isSelected) Color.White else CoffeeOnSurfaceVariant,
                    border = if (isSelected) null else BorderStroke(1.dp, CoffeeOutline)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = option,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProductDetailsPreview() {
    CodeCupTheme {
        ProductDetailsScreen(productId = 1, onBackClick = {}, onAddToCartClick = {})
    }
}
