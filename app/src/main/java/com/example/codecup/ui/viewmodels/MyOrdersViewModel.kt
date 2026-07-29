package com.example.codecup.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkManager
import android.content.Context
import com.example.codecup.data.CartRepository
import com.example.codecup.data.NotificationsRepository
import com.example.codecup.data.OrderRepository
import com.example.codecup.data.RewardsRepository
import com.example.codecup.models.Order
import com.example.codecup.models.OrderStatus
import java.util.UUID
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class MyOrdersUiState(
    val ongoingOrders: List<Order> = emptyList(),
    val orderHistory: List<Order> = emptyList(),
    val isLoading: Boolean = false
)

class MyOrdersViewModel(
    private val orderRepository: OrderRepository,
    private val rewardsRepository: RewardsRepository,
    private val cartRepository: CartRepository,
    private val notificationsRepository: NotificationsRepository,
    private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(MyOrdersUiState())
    val uiState: StateFlow<MyOrdersUiState> = _uiState.asStateFlow()

    /** One-shot snackbar messages ("+1 stamp earned!", "Added to cart"). */
    private val _events = MutableSharedFlow<String>()
    val events: SharedFlow<String> = _events.asSharedFlow()

    init {
        observeOrders()
    }

    private fun observeOrders() {
        orderRepository.orders.onEach { allOrders ->
            _uiState.update {
                it.copy(
                    ongoingOrders = allOrders.filter { order -> order.status != OrderStatus.PickedUp },
                    orderHistory = allOrders.filter { order -> order.status == OrderStatus.PickedUp }
                )
            }
        }.launchIn(viewModelScope)
    }

    /**
     * The Ongoing -> History transition. This is the single place loyalty rewards are
     * granted (rubric: one stamp per COMPLETED order, points from the order total).
     */
    fun markAsPickedUp(orderId: String) {
        val order = _uiState.value.ongoingOrders.find { it.id == orderId } ?: return
        viewModelScope.launch {
            orderRepository.updateOrderStatus(orderId, OrderStatus.PickedUp)
            val earnedPoints = rewardsRepository.awardForCompletedOrder(order)
            notificationsRepository.add(
                title = "Stamp earned",
                body = "Order #$orderId completed — +1 stamp" +
                    if (earnedPoints > 0) ", +$earnedPoints points!" else "!"
            )
            _events.emit(
                if (earnedPoints > 0) "+1 stamp earned! +$earnedPoints pts" else "+1 stamp earned!"
            )
        }
    }

    /** Copies a past order's items back into the cart. */
    fun reorder(order: Order) {
        viewModelScope.launch {
            order.items.forEach { item ->
                cartRepository.addToCart(item.copy(id = UUID.randomUUID().toString()))
            }
            _events.emit("Order added to cart")
        }
    }

    fun cancelOrder(orderId: String) {
        viewModelScope.launch {
            orderRepository.deleteOrder(orderId)
            WorkManager.getInstance(context).cancelAllWorkByTag("order_$orderId")
            _events.emit("Order #$orderId cancelled")
        }
    }
}
