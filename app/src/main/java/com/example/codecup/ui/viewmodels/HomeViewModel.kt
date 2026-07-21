package com.example.codecup.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.codecup.data.CartRepository
import com.example.codecup.data.ProductRepository
import com.example.codecup.models.CartItem
import com.example.codecup.models.Product
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class HomeUiState(
    val products: List<Product> = emptyList(),
    val cartItemsCount: Int = 0,
    val isLoading: Boolean = false
)

class HomeViewModel(
    private val productRepository: ProductRepository,
    private val cartRepository: CartRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadProducts()
        observeCart()
    }

    private fun loadProducts() {
        productRepository.getProducts()
            .onStart { _uiState.update { it.copy(isLoading = true) } }
            .onEach { products ->
                _uiState.update { it.copy(products = products, isLoading = false) }
            }
            .launchIn(viewModelScope)
    }

    private fun observeCart() {
        cartRepository.cartItems.onEach { items ->
            _uiState.update { it.copy(cartItemsCount = items.size) }
        }.launchIn(viewModelScope)
    }

    fun quickAddToCart(product: Product) {
        val cartItem = CartItem(
            product = product,
            quantity = 1,
            size = "Medium (12oz)",
            shots = "Double",
            iceLevel = "Regular Ice",
            totalPrice = product.price + 0.50 // Default medium price
        )
        viewModelScope.launch {
            cartRepository.addToCart(cartItem)
        }
    }
}
