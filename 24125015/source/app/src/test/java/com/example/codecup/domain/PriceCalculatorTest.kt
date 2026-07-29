package com.example.codecup.domain

import org.junit.Assert.assertEquals
import org.junit.Test

class PriceCalculatorTest {

    private val basePrice = 4.50

    @Test
    fun `small size single shot drink has no surcharge`() {
        assertEquals(
            basePrice,
            PriceCalculator.unitPrice(basePrice, PriceCalculator.SIZE_SMALL, PriceCalculator.SHOTS_SINGLE, "Espresso"),
            0.001
        )
    }

    @Test
    fun `medium size drink adds fifty cents`() {
        assertEquals(
            basePrice + PriceCalculator.MEDIUM_SURCHARGE,
            PriceCalculator.unitPrice(basePrice, PriceCalculator.SIZE_MEDIUM, PriceCalculator.SHOTS_SINGLE, "Espresso"),
            0.001
        )
    }

    @Test
    fun `large size drink adds one dollar`() {
        assertEquals(
            basePrice + PriceCalculator.LARGE_SURCHARGE,
            PriceCalculator.unitPrice(basePrice, PriceCalculator.SIZE_LARGE, PriceCalculator.SHOTS_SINGLE, "Espresso"),
            0.001
        )
    }

    @Test
    fun `triple shot drink adds surcharge on top of size`() {
        assertEquals(
            basePrice + PriceCalculator.LARGE_SURCHARGE + PriceCalculator.TRIPLE_SHOT_SURCHARGE,
            PriceCalculator.unitPrice(basePrice, PriceCalculator.SIZE_LARGE, PriceCalculator.SHOTS_TRIPLE, "Espresso"),
            0.001
        )
    }

    @Test
    fun `whole cake adds multiplier`() {
        assertEquals(
            basePrice * PriceCalculator.WHOLE_CAKE_MULTIPLIER,
            PriceCalculator.unitPrice(basePrice, PriceCalculator.SIZE_WHOLE, PriceCalculator.SHOTS_SINGLE, "Cakes"),
            0.001
        )
    }

    @Test
    fun `single slice cake has no surcharge`() {
        assertEquals(
            basePrice,
            PriceCalculator.unitPrice(basePrice, PriceCalculator.SIZE_SLICE, PriceCalculator.SHOTS_SINGLE, "Cakes"),
            0.001
        )
    }

    @Test
    fun `pastries have no surcharge regardless of size or shots`() {
        assertEquals(
            basePrice,
            PriceCalculator.unitPrice(basePrice, PriceCalculator.SIZE_LARGE, PriceCalculator.SHOTS_TRIPLE, "Pastries"),
            0.001
        )
    }

    @Test
    fun `total price multiplies unit price by quantity`() {
        val expectedUnit = basePrice + PriceCalculator.MEDIUM_SURCHARGE
        assertEquals(
            expectedUnit * 3,
            PriceCalculator.totalPrice(basePrice, PriceCalculator.SIZE_MEDIUM, PriceCalculator.SHOTS_DOUBLE, 3, "Espresso"),
            0.001
        )
    }
}
