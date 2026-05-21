package com.erluxman.focuslauncher

import com.erluxman.focuslauncher.service.InsightMath
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class InsightMathTest {

    @Test
    fun opportunityCost_proRatesAcrossHour() {
        assertEquals(10.0, InsightMath.opportunityCost(60, 10.0), 0.001)
        assertEquals(5.0, InsightMath.opportunityCost(30, 10.0), 0.001)
        assertEquals(0.0, InsightMath.opportunityCost(0, 50.0), 0.001)
    }

    @Test
    fun lifetimeHours_zeroWhenAlreadyAtEnd() {
        assertEquals(0, InsightMath.lifetimeHoursOnScreen(120, currentAge = 80, endAge = 80))
    }

    @Test
    fun lifetimeHours_isMonotonicInDailyMinutes() {
        val low = InsightMath.lifetimeHoursOnScreen(30, currentAge = 30, endAge = 80)
        val high = InsightMath.lifetimeHoursOnScreen(120, currentAge = 30, endAge = 80)
        assertTrue(high > low)
    }

    @Test
    fun ioRatio_balancedDay() {
        assertEquals(1.0, InsightMath.ioRatio(distractionMinutes = 40, todosCompleted = 1, focusSessions = 1), 0.01)
    }

    @Test
    fun ioRatio_zeroInputZeroOutput() {
        assertEquals(0.0, InsightMath.ioRatio(distractionMinutes = 0, todosCompleted = 0, focusSessions = 0), 0.01)
    }

    @Test
    fun ioRatio_zeroInputWithOutput_isCapped() {
        assertEquals(
            InsightMath.CAPPED_RATIO,
            InsightMath.ioRatio(distractionMinutes = 0, todosCompleted = 5, focusSessions = 0),
            0.01
        )
    }

    @Test
    fun compoundedBalance_growsWithDays() {
        val now = 100
        val oneYear = InsightMath.compoundedBalance(now, days = 365)
        assertTrue(oneYear > now)
    }

    @Test
    fun compoundedBalance_zeroInputStaysZero() {
        assertEquals(0, InsightMath.compoundedBalance(0, days = 30))
    }
}
