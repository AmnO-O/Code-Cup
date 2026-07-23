package com.example.codecup.ui.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.codecup.data.CartRepository
import com.example.codecup.data.OrderRepository
import com.example.codecup.models.CartItem
import com.example.codecup.models.Order
import com.example.codecup.models.OrderStatus
import com.example.codecup.workers.OrderStatusWorker
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random

data class CartUiState(
    val cartItems: List<CartItem> = emptyList(),
    val totalPrice: Double = 0.0,
    val lastPlacedOrderId: String? = null
)

class CartViewModel(
    private val cartRepository: CartRepository,
    private val orderRepository: OrderRepository,
    private val context: Context? = null
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

    fun checkout(onSuccess: (String) -> Unit) {
        val items = _uiState.value.cartItems
        if (items.isEmpty()) return

        val orderId = "AC-${Random.nextInt(10000, 99999)}"
        val sdf = SimpleDateFormat("dd MMMM, HH:mm", Locale.getDefault())
        val date = sdf.format(Date())

        val order = Order(
            id = orderId,
            date = date,
            items = items,
            totalPrice = _uiState.value.totalPrice,
            status = OrderStatus.Received
        )

        viewModelScope.launch {
            orderRepository.placeOrder(order)
            
            // Trigger background simulation if context is available
            context?.let { ctx ->
                val workRequest = OneTimeWorkRequestBuilder<OrderStatusWorker>()
                    .setInputData(workDataOf("order_id" to orderId))
                    .build()
                WorkManager.getInstance(ctx).enqueue(workRequest)
            }

            cartRepository.clearCart()
            _uiState.update { it.copy(lastPlacedOrderId = orderId) }
            onSuccess(orderId)
        }
    }
}
