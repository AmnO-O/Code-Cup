package com.example.codecup.domain

/**
 * Pure pricing logic for drink customization, shared by the Details screen and the
 * Home quick-add path, and unit-tested in isolation (no Android dependencies).
 */
object PriceCalculator {

    const val SIZE_SMALL = "Small (8oz)"
    const val SIZE_MEDIUM = "Medium (12oz)"
    const val SIZE_LARGE = "Large (16oz)"

    const val SIZE_SLICE = "Single Slice"
    const val SIZE_WHOLE = "Whole Cake"

    const val SHOTS_SINGLE = "Single"
    const val SHOTS_DOUBLE = "Double"
    const val SHOTS_TRIPLE = "Triple (+$0.80)"

    const val ICE_NONE = "No Ice"
    const val ICE_LIGHT = "Light Ice"
    const val ICE_REGULAR = "Regular Ice"
    const val ICE_EXTRA = "Extra Ice"

    val DRINK_SIZE_OPTIONS = listOf(SIZE_SMALL, SIZE_MEDIUM, SIZE_LARGE)
    val CAKE_SIZE_OPTIONS = listOf(SIZE_SLICE, SIZE_WHOLE)
    val SHOT_OPTIONS = listOf(SHOTS_SINGLE, SHOTS_DOUBLE, SHOTS_TRIPLE)
    val ICE_OPTIONS = listOf(ICE_NONE, ICE_LIGHT, ICE_REGULAR, ICE_EXTRA)

    const val MEDIUM_SURCHARGE = 0.50
    const val LARGE_SURCHARGE = 1.00
    const val TRIPLE_SHOT_SURCHARGE = 0.80
    const val WHOLE_CAKE_MULTIPLIER = 8.0

    fun getOptionsForCategory(category: String): List<String> {
        return when (category) {
            "Cakes" -> CAKE_SIZE_OPTIONS
            "Pastries" -> emptyList()
            else -> DRINK_SIZE_OPTIONS // Espresso, Cold Brew, Latte
        }
    }

    fun isDrink(category: String): Boolean = category != "Cakes" && category != "Pastries"

    fun unitPrice(basePrice: Double, size: String, shots: String, category: String): Double {
        if (category == "Cakes") {
            return if (size == SIZE_WHOLE) basePrice * WHOLE_CAKE_MULTIPLIER else basePrice
        }
        
        if (category == "Pastries") return basePrice

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

    fun totalPrice(basePrice: Double, size: String, shots: String, quantity: Int, category: String): Double =
        unitPrice(basePrice, size, shots, category) * quantity
}
