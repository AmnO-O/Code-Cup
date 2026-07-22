package com.example.codecup.data

import com.example.codecup.models.Order
import com.example.codecup.models.OrderStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class OrderRepository {
    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders: StateFlow<List<Order>> = _orders.asStateFlow()

    fun placeOrder(order: Order) {
        _orders.update { currentOrders ->
            listOf(order) + currentOrders
        }
    }

    fun updateOrderStatus(orderId: String, newStatus: OrderStatus) {
        _orders.update { currentOrders ->
            currentOrders.map { order ->
                if (order.id == orderId) {
                    order.copy(status = newStatus)
                } else {
                    order
                }
            }
        }
    }

    companion object {
        @Volatile
        private var instance: OrderRepository? = null

        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: OrderRepository().also { instance = it }
            }
    }
}
