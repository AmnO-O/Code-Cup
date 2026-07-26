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

data class ProductDetailsUiState(
    val product: Product? = null,
    val quantity: Int = 1,
    val selectedSize: String = PriceCalculator.SIZE_MEDIUM,
    val selectedShots: String = PriceCalculator.SHOTS_DOUBLE,
    val selectedIce: String = PriceCalculator.ICE_REGULAR,
    val isFavorite: Boolean = false,
    val totalPrice: Double = 0.0,
    val cartItemsCount: Int = 0,
    val cartItems: List<CartItem> = emptyList()
)

class ProductDetailsViewModel(
    private val productId: Int,
    private val productRepository: ProductRepository,
    private val cartRepository: CartRepository,
    private val favoritesRepository: FavoritesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductDetailsUiState())
    val uiState: StateFlow<ProductDetailsUiState> = _uiState.asStateFlow()

    init {
        loadProduct()
        observeCart()
        observeFavorite()
    }

    private fun loadProduct() {
        viewModelScope.launch {
            val product = productRepository.getProductById(productId)
            _uiState.update { it.copy(product = product) }
            recalculatePrice()
        }
    }

    private fun observeCart() {
        cartRepository.cartItems.onEach { items ->
            _uiState.update { it.copy(cartItemsCount = items.size, cartItems = items) }
        }.launchIn(viewModelScope)
    }

    private fun observeFavorite() {
        favoritesRepository.isFavorite(productId).onEach { isFav ->
            _uiState.update { it.copy(isFavorite = isFav) }
        }.launchIn(viewModelScope)
    }

    fun updateQuantity(newQuantity: Int) {
        if (newQuantity >= 1) {
            _uiState.update { it.copy(quantity = newQuantity) }
            recalculatePrice()
        }
    }

    fun updateSize(size: String) {
        _uiState.update { it.copy(selectedSize = size) }
        recalculatePrice()
    }

    fun updateShots(shots: String) {
        _uiState.update { it.copy(selectedShots = shots) }
        recalculatePrice()
    }

    fun updateIce(ice: String) {
        _uiState.update { it.copy(selectedIce = ice) }
        recalculatePrice()
    }

    /** Live price recompute — runs on every customization or quantity change. */
    private fun recalculatePrice() {
        val state = _uiState.value
        val product = state.product ?: return
        val total = PriceCalculator.totalPrice(product.price, state.selectedSize, state.selectedShots, state.quantity)
        _uiState.update { it.copy(totalPrice = total) }
    }

    fun addToCart() {
        val state = _uiState.value
        val product = state.product ?: return

        val cartItem = CartItem(
            product = product,
            quantity = state.quantity,
            size = state.selectedSize,
            shots = state.selectedShots,
            iceLevel = state.selectedIce,
            totalPrice = state.totalPrice
        )

        viewModelScope.launch {
            cartRepository.addToCart(cartItem)
        }
    }

    fun toggleFavorite() {
        viewModelScope.launch {
            if (_uiState.value.isFavorite) {
                favoritesRepository.removeFavorite(productId)
            } else {
                favoritesRepository.addFavorite(productId)
            }
        }
    }
}
