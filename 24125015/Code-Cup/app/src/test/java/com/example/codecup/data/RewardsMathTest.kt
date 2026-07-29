package com.example.codecup.data

import org.junit.Assert.assertEquals
import org.junit.Test

class RewardsMathTest {

    @Test
    fun `redeem cost is twenty five points per dollar, floored`() {
        assertEquals(112, RewardsRepository.redeemCostFor(4.50))
        assertEquals(125, RewardsRepository.redeemCostFor(5.00))
        assertEquals(0, RewardsRepository.redeemCostFor(0.0))
    }

    @Test
    fun `earn rate constant matches five points per dollar`() {
        val orderTotal = 10.40
        val earned = (orderTotal * RewardsRepository.POINTS_PER_DOLLAR).toInt()
        assertEquals(52, earned)
    }
}
