package com.example.codecup.data

import com.example.codecup.data.database.OrderDao
import com.example.codecup.data.database.OrderEntity
import com.example.codecup.data.database.OrderItemEntity
import com.example.codecup.data.database.OrderWithItems
import com.example.codecup.models.CartItem
import com.example.codecup.models.Order
import com.example.codecup.models.OrderStatus
import com.example.codecup.models.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class OrderRepository(private val orderDao: OrderDao) {

    val orders: Flow<List<Order>> = orderDao.getOrders()
        .map { rows -> rows.map(OrderWithItems::toDomain) }

    val orderCount: Flow<Int> = orderDao.orderCount()

    suspend fun placeOrder(order: Order) {
        orderDao.placeOrder(
            OrderEntity(
                id = order.id,
                dateMillis = order.dateMillis,
                totalPrice = order.totalPrice,
                status = order.status.name
            ),
            order.items.map { item ->
                OrderItemEntity(
                    orderId = order.id,
                    productId = item.product.id,
                    productName = item.product.name,
                    imageUrl = item.product.imageUrl,
                    quantity = item.quantity,
                    size = item.size,
                    shots = item.shots,
                    iceLevel = item.iceLevel,
                    linePrice = item.totalPrice
                )
            }
        )
    }

    suspend fun updateOrderStatus(orderId: String, newStatus: OrderStatus) {
        orderDao.updateStatus(orderId, newStatus.name)
    }
}

private fun OrderWithItems.toDomain() = Order(
    id = order.id,
    dateMillis = order.dateMillis,
    totalPrice = order.totalPrice,
    status = runCatching { OrderStatus.valueOf(order.status) }.getOrDefault(OrderStatus.Received),
    items = items.map { row ->
        CartItem(
            id = row.itemId.toString(),
            product = Product(
                id = row.productId,
                name = row.productName,
                description = "",
                price = if (row.quantity > 0) row.linePrice / row.quantity else row.linePrice,
                imageUrl = row.imageUrl,
                category = ""
            ),
            quantity = row.quantity,
            size = row.size,
            shots = row.shots,
            iceLevel = row.iceLevel,
            totalPrice = row.linePrice
        )
    }
)
