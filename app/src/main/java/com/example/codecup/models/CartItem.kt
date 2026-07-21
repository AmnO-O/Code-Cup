package com.example.codecup.models

import java.util.UUID

data class CartItem(
    val id: String = UUID.randomUUID().toString(),
    val product: Product,
    val quantity: Int,
    val size: String,
    val shots: String,
    val iceLevel: String,
    val totalPrice: Double
) {
    val customizationSummary: String
        get() = "$size, $shots Shots, $iceLevel"
}

val sampleCartItems = listOf<CartItem>() // Empty for now to fix build
