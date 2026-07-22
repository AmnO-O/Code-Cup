package com.example.codecup.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.codecup.data.OrderRepository
import com.example.codecup.models.Order
import com.example.codecup.models.OrderStatus
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class MyOrdersUiState(
    val ongoingOrders: List<Order> = emptyList(),
    val orderHistory: List<Order> = emptyList(),
    val isLoading: Boolean = false
)

class MyOrdersViewModel(
    private val orderRepository: OrderRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MyOrdersUiState())
    val uiState: StateFlow<MyOrdersUiState> = _uiState.asStateFlow()

    init {
        observeOrders()
    }

    private fun observeOrders() {
        orderRepository.orders.onEach { allOrders ->
            val ongoing = allOrders.filter { it.status != OrderStatus.PickedUp }
            val history = allOrders.filter { it.status == OrderStatus.PickedUp }
            _uiState.update { 
                it.copy(
                    ongoingOrders = ongoing,
                    orderHistory = history
                )
            }
        }.launchIn(viewModelScope)
    }

    fun markAsPickedUp(orderId: String) {
        viewModelScope.launch {
            orderRepository.updateOrderStatus(orderId, OrderStatus.PickedUp)
        }
    }
}
