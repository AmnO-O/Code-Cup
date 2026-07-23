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
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import com.example.codecup.data.CartRepository
import com.example.codecup.models.Product
import com.example.codecup.ui.components.*
import com.example.codecup.ui.theme.*
import com.example.codecup.ui.viewmodels.ProductDetailsViewModel
import com.example.codecup.ui.viewmodels.ViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailsScreen(
    productId: Int,
    onBackClick: () -> Unit,
    onAddToCartClick: () -> Unit,
    viewModel: ProductDetailsViewModel = viewModel(factory = ViewModelFactory(productId = productId))
) {
    val uiState by viewModel.uiState.collectAsState()
    val product = uiState.product ?: return // Handle null product if not found

    var showCartPreview by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    val cartItems by CartRepository.getInstance().cartItems.collectAsState()

    Scaffold(
        topBar = {
            AppHeader(
                title = "Details",
                onBackClick = onBackClick,
                actions = {
                    BadgedBox(
                        badge = {
                            if (uiState.cartItemsCount > 0) {
                                Badge { Text(uiState.cartItemsCount.toString()) }
                            }
                        }
                    ) {
                        IconButton(onClick = { showCartPreview = true }) {
                            Icon(Icons.Default.ShoppingCart, contentDescription = "Cart", tint = MaterialTheme.colorScheme.primary)
                        }
                    }
                    IconButton(onClick = { }) {
                        Icon(Icons.Default.FavoriteBorder, contentDescription = "Favorite", tint = MaterialTheme.colorScheme.primary)
                    }
                }
            )
        },
        bottomBar = {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets.navigationBars),
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
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
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "$${"%.2f".format(uiState.totalPrice)}",
                            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    PrimaryButton(
                        onClick = {
                            viewModel.addToCart()
                            onAddToCartClick()
                        },
                        modifier = Modifier.width(200.dp)
                    ) {
                        Text("Add to Cart")
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(Icons.Default.ShoppingCart, contentDescription = null, modifier = Modifier.size(20.dp))
                    }
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.background
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
                    .height(240.dp)
                    .clip(RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)),
                contentScale = ContentScale.Crop
            )

            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = product.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(24.dp))
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                Spacer(modifier = Modifier.height(24.dp))

                // Customization: Size
                CustomizationSection(
                    title = "Size",
                    options = listOf("Small (8oz)", "Medium (12oz)", "Large (16oz)"),
                    selectedOption = uiState.selectedSize,
                    onOptionSelected = { viewModel.updateSize(it) }
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Customization: Espresso Shots
                CustomizationSection(
                    title = "Espresso Shots",
                    options = listOf("Single", "Double", "Triple (+$0.80)"),
                    selectedOption = uiState.selectedShots,
                    onOptionSelected = { viewModel.updateShots(it) }
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Customization: Ice Level
                CustomizationSection(
                    title = "Ice Level",
                    options = listOf("No Ice", "Light Ice", "Regular Ice", "Extra Ice"),
                    selectedOption = uiState.selectedIce,
                    onOptionSelected = { viewModel.updateIce(it) }
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Quantity
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(12.dp))
                        .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(12.dp))
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Quantity",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    QuantityStepper(
                        quantity = uiState.quantity,
                        onQuantityChange = { viewModel.updateQuantity(it) }
                    )
                }
                
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }

    if (showCartPreview) {
        ModalBottomSheet(
            onDismissRequest = { showCartPreview = false },
            sheetState = sheetState,
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            CartPreviewContent(
                cartItems = cartItems,
                onClose = { showCartPreview = false }
            )
        }
    }
}

@Composable
fun CartPreviewContent(
    cartItems: List<com.example.codecup.models.CartItem>,
    onClose: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .padding(bottom = 32.dp)
    ) {
        Text(
            "Cart Preview",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(16.dp))
        
        if (cartItems.isEmpty()) {
            Text(
                "Your cart is empty.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f, fill = false),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(cartItems) { item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.background, RoundedCornerShape(8.dp))
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            model = item.product.imageUrl,
                            contentDescription = null,
                            modifier = Modifier.size(40.dp).clip(RoundedCornerShape(4.dp)),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(item.product.name, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold), color = MaterialTheme.colorScheme.onSurface)
                            Text(item.customizationSummary, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Text("$${"%.2f".format(item.totalPrice)}", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        PrimaryButton(onClick = onClose) {
            Text("Continue Browsing")
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
            color = MaterialTheme.colorScheme.onSurface
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
                    color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent,
                    contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant,
                    border = if (isSelected) null else BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
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
