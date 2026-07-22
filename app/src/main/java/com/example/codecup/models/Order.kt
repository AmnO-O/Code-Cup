package com.example.codecup.models

enum class OrderStatus(val label: String) {
    Received("Received"),
    Preparing("Preparing"),
    Ready("Ready"),
    PickedUp("Picked Up")
}

data class Order(
    val id: String,
    val date: String,
    val items: List<CartItem>,
    val totalPrice: Double,
    var status: OrderStatus
) {
    val itemsSummary: String
        get() = items.joinToString(", ") { "${it.quantity}x ${it.product.name}" }
}

val sampleOrders = listOf<Order>() // Empty now, will be filled by repository
