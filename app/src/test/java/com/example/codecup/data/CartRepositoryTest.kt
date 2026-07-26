package com.example.codecup.data

import com.example.codecup.data.database.CartDao
import com.example.codecup.data.database.CartItemEntity
import com.example.codecup.data.database.CartItemWithProduct
import com.example.codecup.domain.PriceCalculator
import com.example.codecup.models.CartItem
import com.example.codecup.models.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

/** In-memory CartDao double backed by a StateFlow, mirroring Room's reactive behavior. */
private class FakeCartDao(private val products: Map<Int, Product>) : CartDao {
    private val rows = MutableStateFlow<Map<String, CartItemEntity>>(emptyMap())

    override fun getCartItems(): Flow<List<CartItemWithProduct>> =
        rows.map { map -> map.values.map { CartItemWithProduct(it, products.getValue(it.productId)) } }

    override suspend fun findMatching(productId: Int, size: String, shots: String, iceLevel: String): CartItemEntity? =
        rows.value.values.find {
            it.productId == productId && it.size == size && it.shots == shots && it.iceLevel == iceLevel
        }

    override suspend fun getById(id: String): CartItemEntity? = rows.value[id]

    override suspend fun insert(item: CartItemEntity) {
        rows.value = rows.value + (item.id to item)
    }

    override suspend fun update(item: CartItemEntity) = insert(item)

    override suspend fun delete(id: String) {
        rows.value = rows.value - id
    }

    override suspend fun clear() {
        rows.value = emptyMap()
    }
}

class CartRepositoryTest {

    private val latte = Product(1, "Latte", "desc", 4.00, "url", "Latte")
    private val dao = FakeCartDao(mapOf(1 to latte))
    private val repository = CartRepository(dao)

    private fun cartItem(size: String = PriceCalculator.SIZE_MEDIUM, quantity: Int = 1) = CartItem(
        product = latte,
        quantity = quantity,
        size = size,
        shots = PriceCalculator.SHOTS_DOUBLE,
        iceLevel = PriceCalculator.ICE_REGULAR,
        totalPrice = PriceCalculator.totalPrice(latte.price, size, PriceCalculator.SHOTS_DOUBLE, quantity)
    )

    @Test
    fun `adding same customization twice merges into one row with summed quantity`() = runTest {
        repository.addToCart(cartItem())
        repository.addToCart(cartItem())

        val items = repository.cartItems.first()
        assertEquals(1, items.size)
        assertEquals(2, items[0].quantity)
        assertEquals(9.00, items[0].totalPrice, 0.001) // (4.00 + 0.50) * 2
    }

    @Test
    fun `different customization creates a separate row`() = runTest {
        repository.addToCart(cartItem(size = PriceCalculator.SIZE_MEDIUM))
        repository.addToCart(cartItem(size = PriceCalculator.SIZE_LARGE))

        assertEquals(2, repository.cartItems.first().size)
    }

    @Test
    fun `updating quantity re-derives total from unit price`() = runTest {
        val item = cartItem()
        repository.addToCart(item)

        repository.updateQuantity(item.id, 3)

        val row = repository.cartItems.first().single()
        assertEquals(3, row.quantity)
        assertEquals(13.50, row.totalPrice, 0.001) // 4.50 * 3
    }

    @Test
    fun `quantity below one is ignored`() = runTest {
        val item = cartItem()
        repository.addToCart(item)

        repository.updateQuantity(item.id, 0)

        assertEquals(1, repository.cartItems.first().single().quantity)
    }

    @Test
    fun `removing an item deletes its row`() = runTest {
        val item = cartItem()
        repository.addToCart(item)

        repository.removeFromCart(item.id)

        assertEquals(0, repository.cartItems.first().size)
    }
}
