package com.example.codecup.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.codecup.data.CartRepository
import com.example.codecup.data.FavoritesRepository
import com.example.codecup.data.ProductRepository
import com.example.codecup.domain.PriceCalculator
import com.example.codecup.models.CartItem
import com.example.codecup.models.Product
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class FavoritesUiState(
    val favoriteProducts: List<Product> = emptyList(),
    val isLoading: Boolean = true
)

class FavoritesViewModel(
    private val favoritesRepository: FavoritesRepository,
    private val productRepository: ProductRepository,
    private val cartRepository: CartRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FavoritesUiState())
    val uiState: StateFlow<FavoritesUiState> = _uiState.asStateFlow()

    /** One-shot snackbar messages ("Added to cart"). */
    private val _events = MutableSharedFlow<String>()
    val events: SharedFlow<String> = _events.asSharedFlow()

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
            val isFav = _uiState.value.favoriteProducts.any { it.id == productId }
            if (isFav) {
                favoritesRepository.removeFavorite(productId)
            } else {
                favoritesRepository.addFavorite(productId)
            }
        }
    }

    /** Same default-configuration quick add as the Home grid. */
    fun quickAddToCart(product: Product) {
        viewModelScope.launch {
            cartRepository.addToCart(
                CartItem(
                    product = product,
                    quantity = 1,
                    size = PriceCalculator.SIZE_MEDIUM,
                    shots = PriceCalculator.SHOTS_DOUBLE,
                    iceLevel = PriceCalculator.ICE_REGULAR,
                    totalPrice = PriceCalculator.totalPrice(
                        product.price, PriceCalculator.SIZE_MEDIUM, PriceCalculator.SHOTS_DOUBLE, 1
                    )
                )
            )
            _events.emit("${product.name} added to cart")
        }
    }
}
