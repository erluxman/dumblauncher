package com.erluxman.focuslauncher

import com.erluxman.focuslauncher.service.money.TimeMoneyMath
import org.junit.Assert.assertEquals
import org.junit.Test

class TimeMoneyMathTest {

    @Test
    fun hours_zeroRateReturnsZero() {
        assertEquals(0.0, TimeMoneyMath.hoursForPurchase(100.0, 0), 0.0)
    }

    @Test
    fun hours_basic() {
        assertEquals(5.0, TimeMoneyMath.hoursForPurchase(100.0, 20), 1e-9)
    }

    @Test
    fun hours_negativeUsdClampedToZero() {
        assertEquals(0.0, TimeMoneyMath.hoursForPurchase(-100.0, 20), 0.0)
    }

    @Test
    fun usd_basic() {
        assertEquals(100.0, TimeMoneyMath.usdForHours(5.0, 20), 1e-9)
    }

    @Test
    fun formatHours_picksRightBucket() {
        assertEquals("moments", TimeMoneyMath.formatHours(0.0001))
        assertEquals("30m", TimeMoneyMath.formatHours(0.5))
        assertEquals("1h 30m", TimeMoneyMath.formatHours(1.5))
        assertEquals("1d 1h", TimeMoneyMath.formatHours(25.0))
    }
}
