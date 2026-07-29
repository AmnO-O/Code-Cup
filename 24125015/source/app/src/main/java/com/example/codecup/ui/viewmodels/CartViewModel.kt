package com.example.codecup.ui.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.codecup.data.CartRepository
import com.example.codecup.data.NotificationsRepository
import com.example.codecup.data.OrderRepository
import com.example.codecup.data.RewardsRepository
import com.example.codecup.models.CartItem
import com.example.codecup.models.Order
import com.example.codecup.models.OrderStatus
import com.example.codecup.workers.OrderStatusWorker
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlin.random.Random

data class CartUiState(
    val cartItems: List<CartItem> = emptyList(),
    val totalPrice: Double = 0.0,
    val lastPlacedOrderId: String? = null,
    val deliveryAddress: String = "123 Artisan Lane, Coffee City"
)

class CartViewModel(
    private val cartRepository: CartRepository,
    private val orderRepository: OrderRepository,
    private val rewardsRepository: RewardsRepository,
    private val notificationsRepository: NotificationsRepository,
    private val context: Context
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
        viewModelScope.launch {
            cartRepository.updateQuantity(itemId, newQuantity)
        }
    }

    fun removeItem(itemId: String) {
        viewModelScope.launch {
            cartRepository.removeFromCart(itemId)
        }
    }

    fun updateAddress(newAddress: String) {
        _uiState.update { it.copy(deliveryAddress = newAddress) }
    }

    /**
     * Commits the cart into a persisted Order and clears the cart. Stamps/points are
     * NOT awarded here — the rubric grants them when the order is completed (picked up).
     */
    fun checkout(onSuccess: (String) -> Unit) {
        val items = _uiState.value.cartItems
        if (items.isEmpty()) return

        val orderId = "AC-${Random.nextInt(10000, 99999)}"
        val order = Order(
            id = orderId,
            dateMillis = System.currentTimeMillis(),
            items = items,
            totalPrice = _uiState.value.totalPrice,
            status = OrderStatus.Received,
            deliveryAddress = _uiState.value.deliveryAddress
        )

        viewModelScope.launch {
            orderRepository.placeOrder(order)
            notificationsRepository.add(
                title = "Order placed",
                body = "Order #$orderId is being prepared. We'll tell you when it's ready!"
            )

            val workRequest = OneTimeWorkRequestBuilder<OrderStatusWorker>()
                .setInputData(workDataOf(OrderStatusWorker.KEY_ORDER_ID to orderId))
                .build()
            WorkManager.getInstance(context).enqueue(workRequest)

            cartRepository.clearCart()
            _uiState.update { it.copy(lastPlacedOrderId = orderId) }
            onSuccess(orderId)
        }
    }
}
