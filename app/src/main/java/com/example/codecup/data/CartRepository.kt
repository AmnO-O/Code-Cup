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
            val existingItemIndex = currentItems.indexOfFirst {
                it.product.id == item.product.id &&
                        it.size == item.size &&
                        it.shots == item.shots &&
                        it.iceLevel == item.iceLevel
            }

            if (existingItemIndex != -1) {
                currentItems.mapIndexed { index, cartItem ->
                    if (index == existingItemIndex) {
                        val newQuantity = cartItem.quantity + item.quantity
                        cartItem.copy(
                            quantity = newQuantity,
                            totalPrice = (cartItem.totalPrice / cartItem.quantity) * newQuantity
                        )
                    } else {
                        cartItem
                    }
                }
            } else {
                currentItems + item
            }
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
