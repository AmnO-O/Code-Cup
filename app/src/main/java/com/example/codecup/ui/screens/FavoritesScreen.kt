package com.example.codecup.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.codecup.ui.components.*
import com.example.codecup.ui.viewmodels.FavoritesViewModel
import com.example.codecup.ui.viewmodels.ViewModelFactory
import kotlinx.coroutines.launch

@Composable
fun FavoritesScreen(
    onProductClick: (Int) -> Unit,
    onNavigate: (String) -> Unit,
    viewModel: FavoritesViewModel = viewModel(factory = ViewModelFactory(context = LocalContext.current))
) {
    val uiState by viewModel.uiState.collectAsState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AppDrawer(
                currentRoute = "favorites",
                onNavigate = onNavigate,
                onCloseDrawer = { scope.launch { drawerState.close() } }
            )
        }
    ) {
        Scaffold(
            topBar = {
                AppHeader(
                    title = "My Favorites",
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    }
                )
            },
            bottomBar = {
                BottomNavBar(currentRoute = null, onNavigate = onNavigate)
            }
        ) { innerPadding ->
            if (uiState.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else if (uiState.favoriteProducts.isEmpty()) {
                EmptyState(
                    title = "No Favorites Yet",
                    description = "Your favorite brews will appear here once you heart them.",
                    icon = Icons.Default.Favorite,
                    action = {
                        Button(onClick = { onNavigate("home") }) {
                            Text("Browse Coffee")
                        }
                    },
                    modifier = Modifier.padding(innerPadding)
                )
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.padding(innerPadding)
                ) {
                    items(uiState.favoriteProducts) { product ->
                        ProductCard(
                            product = product,
                            onProductClick = { onProductClick(product.id) },
                            onAddClick = { /* Quick add logic could be here too */ },
                            isFavorite = true,
                            onFavoriteClick = { viewModel.toggleFavorite(it.id) }
                        )
                    }
                }
            }
        }
    }
}
