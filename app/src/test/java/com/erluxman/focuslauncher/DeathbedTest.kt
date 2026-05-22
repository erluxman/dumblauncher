package com.erluxman.focuslauncher

import com.erluxman.focuslauncher.service.Deathbed
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class DeathbedTest {

    @Test
    fun lifetimeMinutes_zeroWhenAnyInputIsInvalid() {
        assertEquals(0L, Deathbed.lifetimeMinutes(0, 30))
        assertEquals(0L, Deathbed.lifetimeMinutes(60, 0))
        assertEquals(0L, Deathbed.lifetimeMinutes(60, 80, endAge = 80))
        assertEquals(0L, Deathbed.lifetimeMinutes(60, 90, endAge = 80))
    }

    @Test
    fun lifetimeMinutes_simpleProduct() {
        // 60min/day * 40 years * 365 = 876_000 min
        assertEquals(876_000L, Deathbed.lifetimeMinutes(60, 40))
    }

    @Test
    fun lifetimeYears_atOneHourADayForFortyYears_isAboutTwoAndHalf() {
        // 876_000 min / (365 * 16 * 60) = 2.50 waking years
        val y = Deathbed.lifetimeYearsOfWaking(60, 40)
        assertEquals(2.50, y, 0.01)
    }

    @Test
    fun analogy_isStableAcrossRanges() {
        listOf(0.1, 0.7, 1.5, 3.0, 7.0, 12.5).forEach {
            val s = Deathbed.analogyFor(it)
            assertTrue("non-empty analogy for $it", s.isNotBlank())
        }
    }
}
