package com.example.codecup.domain

/**
 * Pure pricing logic for drink customization, shared by the Details screen and the
 * Home quick-add path, and unit-tested in isolation (no Android dependencies).
 */
object PriceCalculator {

    const val SIZE_SMALL = "Small (8oz)"
    const val SIZE_MEDIUM = "Medium (12oz)"
    const val SIZE_LARGE = "Large (16oz)"

    const val SHOTS_SINGLE = "Single"
    const val SHOTS_DOUBLE = "Double"
    const val SHOTS_TRIPLE = "Triple (+$0.80)"

    const val ICE_NONE = "No Ice"
    const val ICE_LIGHT = "Light Ice"
    const val ICE_REGULAR = "Regular Ice"
    const val ICE_EXTRA = "Extra Ice"

    val SIZE_OPTIONS = listOf(SIZE_SMALL, SIZE_MEDIUM, SIZE_LARGE)
    val SHOT_OPTIONS = listOf(SHOTS_SINGLE, SHOTS_DOUBLE, SHOTS_TRIPLE)
    val ICE_OPTIONS = listOf(ICE_NONE, ICE_LIGHT, ICE_REGULAR, ICE_EXTRA)

    const val MEDIUM_SURCHARGE = 0.50
    const val LARGE_SURCHARGE = 1.00
    const val TRIPLE_SHOT_SURCHARGE = 0.80

    fun unitPrice(basePrice: Double, size: String, shots: String): Double {
        var price = basePrice
        price += when (size) {
            SIZE_MEDIUM -> MEDIUM_SURCHARGE
            SIZE_LARGE -> LARGE_SURCHARGE
            else -> 0.0
        }
        if (shots == SHOTS_TRIPLE) {
            price += TRIPLE_SHOT_SURCHARGE
        }
        return price
    }

    fun totalPrice(basePrice: Double, size: String, shots: String, quantity: Int): Double =
        unitPrice(basePrice, size, shots) * quantity
}
