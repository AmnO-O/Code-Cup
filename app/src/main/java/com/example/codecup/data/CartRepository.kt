package com.example.codecup.data

import com.example.codecup.models.CartItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CartRepository {
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    fun addToCart(item: CartItem) {
        _cartItems.update { currentItems ->
            currentItems + item
        }
    }

    fun removeFromCart(itemId: String) {
        _cartItems.update { currentItems ->
            currentItems.filterNot { it.id == itemId }
        }
    }

    fun clearCart() {
        _cartItems.value = emptyList()
    }

    companion object {
        @Volatile
        private var instance: CartRepository? = null

        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: CartRepository().also { instance = it }
            }
    }
}
