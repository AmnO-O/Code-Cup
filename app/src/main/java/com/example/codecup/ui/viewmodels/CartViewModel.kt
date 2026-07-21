package com.example.codecup.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.codecup.data.CartRepository
import com.example.codecup.models.CartItem
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class CartUiState(
    val cartItems: List<CartItem> = emptyList(),
    val totalPrice: Double = 0.0
)

class CartViewModel(
    private val cartRepository: CartRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CartUiState())
    val uiState: StateFlow<CartUiState> = _uiState.asStateFlow()

    init {
        observeCart()
    }

    private fun observeCart() {
        cartRepository.cartItems.onEach { items ->
            val total = items.sumOf { it.totalPrice }
            _uiState.update { it.copy(cartItems = items, totalPrice = total) }
        }.launchIn(viewModelScope)
    }

    fun updateQuantity(itemId: String, newQuantity: Int) {
        // In a real app, repository would handle this update.
        // For now, we'll re-add with new quantity if repository is simple.
        // But our CartRepository only has addToCart and removeFromCart.
        // Let's keep it simple for now as it's an in-memory mock.
    }

    fun removeItem(itemId: String) {
        viewModelScope.launch {
            cartRepository.removeFromCart(itemId)
        }
    }
}
