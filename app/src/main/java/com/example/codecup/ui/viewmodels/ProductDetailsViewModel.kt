package com.example.codecup.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.codecup.data.CartRepository
import com.example.codecup.data.ProductRepository
import com.example.codecup.models.CartItem
import com.example.codecup.models.Product
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class ProductDetailsUiState(
    val product: Product? = null,
    val quantity: Int = 1,
    val selectedSize: String = "Medium (12oz)",
    val selectedShots: String = "Double",
    val selectedIce: String = "Regular Ice",
    val totalPrice: Double = 0.0,
    val cartItemsCount: Int = 0
)

class ProductDetailsViewModel(
    private val productId: Int,
    private val productRepository: ProductRepository,
    private val cartRepository: CartRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductDetailsUiState())
    val uiState: StateFlow<ProductDetailsUiState> = _uiState.asStateFlow()

    init {
        loadProduct()
        observeCart()
    }

    private fun loadProduct() {
        val product = productRepository.getProductById(productId)
        _uiState.update { it.copy(product = product) }
        calculatePrice()
    }

    private fun observeCart() {
        cartRepository.cartItems.onEach { items ->
            _uiState.update { it.copy(cartItemsCount = items.size) }
        }.launchIn(viewModelScope)
    }

    fun updateQuantity(newQuantity: Int) {
        if (newQuantity >= 1) {
            _uiState.update { it.copy(quantity = newQuantity) }
            calculatePrice()
        }
    }

    fun updateSize(size: String) {
        _uiState.update { it.copy(selectedSize = size) }
        calculatePrice()
    }

    fun updateShots(shots: String) {
        _uiState.update { it.copy(selectedShots = shots) }
        calculatePrice()
    }

    fun updateIce(ice: String) {
        _uiState.update { it.copy(selectedIce = ice) }
    }

    private fun calculatePrice() {
        val state = _uiState.value
        val product = state.product ?: return

        var basePrice = product.price
        
        // Add cost for size
        basePrice += when (state.selectedSize) {
            "Small (8oz)" -> 0.0
            "Medium (12oz)" -> 0.50
            "Large (16oz)" -> 1.00
            else -> 0.0
        }

        // Add cost for shots
        if (state.selectedShots.contains("Triple")) {
            basePrice += 0.80
        }

        val total = basePrice * state.quantity
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
}
