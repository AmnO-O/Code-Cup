package com.example.codecup.models

data class CartItem(
    val id: Int,
    val product: Product,
    val quantity: Int,
    val customization: String
)

val sampleCartItems = listOf(
    CartItem(1, sampleProducts[0], 1, "Double shot · Medium · Light ice"),
    CartItem(2, sampleProducts[3], 2, "Baked fresh")
)
