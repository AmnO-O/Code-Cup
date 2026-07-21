package com.example.codecup.models

enum class OrderStatus {
    Preparing, Ready, PickedUp
}

data class Order(
    val id: String,
    val date: String,
    val itemsSummary: String,
    val totalPrice: Double,
    val status: OrderStatus
)

val sampleOrders = listOf(
    Order("1042", "21 July, 21:15", "Artisan Cappuccino, Butter Croissant", 8.00, OrderStatus.Preparing),
    Order("1041", "20 July, 09:30", "Nitro Cold Brew", 5.00, OrderStatus.PickedUp),
    Order("1039", "19 July, 10:15", "Caramel Macchiato", 5.50, OrderStatus.PickedUp)
)
