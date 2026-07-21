package com.example.codecup.ui.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.codecup.ui.components.*
import com.example.codecup.ui.theme.*
import com.example.codecup.ui.viewmodels.HomeViewModel
import com.example.codecup.ui.viewmodels.ViewModelFactory

@Composable
fun HomeScreen(
    onProductClick: (Int) -> Unit,
    onNavigateToRewards: () -> Unit,
    onNavigateToCart: () -> Unit,
    onNavigate: (String) -> Unit,
    viewModel: HomeViewModel = viewModel(factory = ViewModelFactory())
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            AppHeader(
                title = "Artisan Coffee",
                navigationIcon = {
                    IconButton(onClick = { }) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu", tint = CoffeeOnSurfaceVariant)
                    }
                },
                actions = {
                    CartIconWithBadge(count = uiState.cartItemsCount, onClick = onNavigateToCart)
                }
            )
        },
        bottomBar = {
            BottomNavBar(currentRoute = NavDestination.Home.route, onNavigate = onNavigate)
        },
        containerColor = CoffeeBackground
    ) { innerPadding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 16.dp),
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            // Header Content
            item(span = { GridItemSpan(2) }) {
                Column {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Good Morning, Alex",
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                        color = CoffeeOnSurface
                    )
                    Text(
                        text = "Ready for your daily brew?",
                        style = MaterialTheme.typography.bodyMedium,
                        color = CoffeeOnSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    LoyaltyCard(stampsEarned = 5, onClick = onNavigateToRewards)
                    Spacer(modifier = Modifier.height(24.dp))
                    CategoryChips()
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            // Product Grid
            items(uiState.products) { product ->
                ProductCard(
                    product = product,
                    onProductClick = { onProductClick(product.id) },
                    onAddClick = { viewModel.quickAddToCart(it) }
                )
            }
            
            // Footer spacer
            item(span = { GridItemSpan(2) }) {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun CategoryChips() {
    val categories = listOf("All Coffee", "Espresso", "Cold Brew", "Pastries", "Beans")
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(end = 16.dp)
    ) {
        items(categories) { category ->
            val isSelected = category == "All Coffee"
            Surface(
                color = if (isSelected) CoffeePrimaryContainer else Color.Transparent,
                contentColor = if (isSelected) Color.White else CoffeeOnSurface,
                shape = CircleShape,
                border = if (isSelected) null else BorderStroke(1.dp, CoffeeOutline),
                modifier = Modifier.height(40.dp)
            ) {
                Box(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = category, style = MaterialTheme.typography.labelLarge)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    CodeCupTheme {
        HomeScreen(onProductClick = {}, onNavigateToRewards = {}, onNavigateToCart = {}, onNavigate = {})
    }
}
