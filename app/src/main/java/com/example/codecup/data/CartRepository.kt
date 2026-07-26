package com.example.codecup.data

import com.example.codecup.data.database.CartDao
import com.example.codecup.data.database.CartItemEntity
import com.example.codecup.data.database.CartItemWithProduct
import com.example.codecup.models.CartItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CartRepository(private val cartDao: CartDao) {

    val cartItems: Flow<List<CartItem>> = cartDao.getCartItems()
        .map { rows -> rows.map(CartItemWithProduct::toDomain) }

    /**
     * Inserts the item, or — when an identical product+customization row already
     * exists — merges into it by bumping the quantity and re-deriving the price
     * from the existing row's unit price.
     */
    suspend fun addToCart(item: CartItem) {
        val existing = cartDao.findMatching(item.product.id, item.size, item.shots, item.iceLevel)
        if (existing != null) {
            val unitPrice = existing.totalPrice / existing.quantity
            val newQuantity = existing.quantity + item.quantity
            cartDao.update(existing.copy(quantity = newQuantity, totalPrice = unitPrice * newQuantity))
        } else {
            cartDao.insert(
                CartItemEntity(
                    id = item.id,
                    productId = item.product.id,
                    quantity = item.quantity,
                    size = item.size,
                    shots = item.shots,
                    iceLevel = item.iceLevel,
                    totalPrice = item.totalPrice
                )
            )
        }
    }

    suspend fun updateQuantity(itemId: String, newQuantity: Int) {
        if (newQuantity < 1) return
        val row = cartDao.getById(itemId) ?: return
        val unitPrice = row.totalPrice / row.quantity
        cartDao.update(row.copy(quantity = newQuantity, totalPrice = unitPrice * newQuantity))
    }

    suspend fun removeFromCart(itemId: String) = cartDao.delete(itemId)

    suspend fun clearCart() = cartDao.clear()
}

private fun CartItemWithProduct.toDomain() = CartItem(
    id = item.id,
    product = product,
    quantity = item.quantity,
    size = item.size,
    shots = item.shots,
    iceLevel = item.iceLevel,
    totalPrice = item.totalPrice
)
