package com.example.codecup.models

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class OrderStatus(val label: String) {
    Received("Received"),
    Preparing("Preparing"),
    Ready("Ready"),
    PickedUp("Picked Up")
}

data class Order(
    val id: String,
    val dateMillis: Long,
    val items: List<CartItem>,
    val totalPrice: Double,
    val status: OrderStatus,
    val deliveryAddress: String = "123 Artisan Lane, Coffee City"
) {
    val date: String
        get() = SimpleDateFormat("dd MMMM, HH:mm", Locale.getDefault()).format(Date(dateMillis))

    val itemsSummary: String
        get() = items.joinToString(", ") { "${it.quantity}x ${it.product.name}" }
}
