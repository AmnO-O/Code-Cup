package com.example.codecup.domain

import org.junit.Assert.assertEquals
import org.junit.Test

class PriceCalculatorTest {

    private val basePrice = 4.50

    @Test
    fun `small size single shot has no surcharge`() {
        assertEquals(
            basePrice,
            PriceCalculator.unitPrice(basePrice, PriceCalculator.SIZE_SMALL, PriceCalculator.SHOTS_SINGLE),
            0.001
        )
    }

    @Test
    fun `medium size adds fifty cents`() {
        assertEquals(
            basePrice + PriceCalculator.MEDIUM_SURCHARGE,
            PriceCalculator.unitPrice(basePrice, PriceCalculator.SIZE_MEDIUM, PriceCalculator.SHOTS_SINGLE),
            0.001
        )
    }

    @Test
    fun `large size adds one dollar`() {
        assertEquals(
            basePrice + PriceCalculator.LARGE_SURCHARGE,
            PriceCalculator.unitPrice(basePrice, PriceCalculator.SIZE_LARGE, PriceCalculator.SHOTS_SINGLE),
            0.001
        )
    }

    @Test
    fun `triple shot adds surcharge on top of size`() {
        assertEquals(
            basePrice + PriceCalculator.LARGE_SURCHARGE + PriceCalculator.TRIPLE_SHOT_SURCHARGE,
            PriceCalculator.unitPrice(basePrice, PriceCalculator.SIZE_LARGE, PriceCalculator.SHOTS_TRIPLE),
            0.001
        )
    }

    @Test
    fun `double shot has no surcharge`() {
        assertEquals(
            basePrice,
            PriceCalculator.unitPrice(basePrice, PriceCalculator.SIZE_SMALL, PriceCalculator.SHOTS_DOUBLE),
            0.001
        )
    }

    @Test
    fun `total price multiplies unit price by quantity`() {
        val expectedUnit = basePrice + PriceCalculator.MEDIUM_SURCHARGE
        assertEquals(
            expectedUnit * 3,
            PriceCalculator.totalPrice(basePrice, PriceCalculator.SIZE_MEDIUM, PriceCalculator.SHOTS_DOUBLE, 3),
            0.001
        )
    }
}
