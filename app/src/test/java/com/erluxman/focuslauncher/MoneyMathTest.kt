package com.erluxman.focuslauncher

import com.erluxman.focuslauncher.service.MoneyMath
import org.junit.Assert.assertEquals
import org.junit.Test

class MoneyMathTest {

    @Test
    fun netWorth_subtracts() {
        assertEquals(40_000, MoneyMath.netWorth(50_000, 10_000))
        assertEquals(-5_000, MoneyMath.netWorth(0, 5_000))
    }

    @Test
    fun savingsRate_basic() {
        assertEquals(25, MoneyMath.savingsRatePct(4_000, 3_000))
        assertEquals(0, MoneyMath.savingsRatePct(3_000, 3_000))
        assertEquals(0, MoneyMath.savingsRatePct(3_000, 4_000))
        assertEquals(100, MoneyMath.savingsRatePct(1_000, 0))
    }

    @Test
    fun savingsRate_zeroIncomeIsZero() {
        assertEquals(0, MoneyMath.savingsRatePct(0, 100))
    }
}
