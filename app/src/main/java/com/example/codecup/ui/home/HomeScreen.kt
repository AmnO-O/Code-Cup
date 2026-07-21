package com.example.codecup.ui.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.codecup.models.sampleProducts
import com.example.codecup.ui.components.*
import com.example.codecup.ui.theme.*

@Composable
fun HomeScreen(
    onProductClick: (Int) -> Unit,
    onNavigateToRewards: () -> Unit,
    onNavigateToCart: () -> Unit,
    onNavigate: (String) -> Unit
) {
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
                    CartIconWithBadge(count = 3, onClick = onNavigateToCart)
                }
            )
        },
        bottomBar = {
            BottomNavBar(currentRoute = NavDestination.Home.route, onNavigate = onNavigate)
        },
        containerColor = CoffeeBackground
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Spacer(modifier = Modifier.height(8.dp))
                // Greeting
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

                // Loyalty Card
                LoyaltyCard(stampsEarned = 5, onClick = onNavigateToRewards)

                Spacer(modifier = Modifier.height(24.dp))

                // Categories
                CategoryChips()

                Spacer(modifier = Modifier.height(16.dp))
            }

            // Product Grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(sampleProducts) { product ->
                    ProductCard(
                        product = product,
                        onProductClick = { onProductClick(product.id) },
                        onAddClick = { /* Handle add to cart */ }
                    )
                }
                item { Spacer(modifier = Modifier.height(16.dp)) }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppHeader(
    title: String,
    navigationIcon: @Composable () -> Unit,
    actions: @Composable RowScope.() -> Unit
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = CoffeePrimary
                )
            )
        },
        navigationIcon = navigationIcon,
        actions = actions,
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = CoffeeBackground
        )
    )
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
