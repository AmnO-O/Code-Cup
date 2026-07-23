package com.example.codecup.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.codecup.data.FavoritesRepository
import com.example.codecup.data.ProductRepository
import com.example.codecup.models.Product
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class FavoritesUiState(
    val favoriteProducts: List<Product> = emptyList(),
    val isLoading: Boolean = true
)

class FavoritesViewModel(
    private val favoritesRepository: FavoritesRepository,
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FavoritesUiState())
    val uiState: StateFlow<FavoritesUiState> = _uiState.asStateFlow()

    init {
        loadFavorites()
    }

    private fun loadFavorites() {
        viewModelScope.launch {
            favoritesRepository.getAllFavorites().collect { favorites ->
                val products = favorites.mapNotNull { fav ->
                    productRepository.getProductById(fav.productId)
                }
                _uiState.update { it.copy(favoriteProducts = products, isLoading = false) }
            }
        }
    }

    fun toggleFavorite(productId: Int) {
        viewModelScope.launch {
            // In a real app, we'd check if it's already favorited
            // But for simplicity in the list view, usually we'd just have a "remove" or toggle.
            // Let's implement toggle.
            val isFav = _uiState.value.favoriteProducts.any { it.id == productId }
            if (isFav) {
                favoritesRepository.removeFavorite(productId)
            } else {
                favoritesRepository.addFavorite(productId)
            }
        }
    }
}
