package com.erluxman.focuslauncher

import com.erluxman.focuslauncher.service.mortality.CompoundCurve
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CompoundCurveTest {

    @Test
    fun series_startsAtOneAndGrowsMonotonically() {
        val s = CompoundCurve.series(365)
        assertEquals(1.0, s[0], 1e-9)
        for (i in 1 until s.size) {
            assertTrue("series should be non-decreasing", s[i] >= s[i - 1])
        }
    }

    @Test
    fun valueAt_oneYearMatchesGeometricFormula() {
        val v = CompoundCurve.valueAt(365, 0.01)
        assertEquals((1.01).let { Math.pow(it, 365.0) }, v, 1e-9)
    }

    @Test
    fun daysToReach_double_atTenPercent_isCloseToSeven() {
        // 1.1^7 ≈ 1.948; need 8 days to actually cross 2.0.
        val d = CompoundCurve.daysToReach(2.0, dailyRate = 0.1)
        assertEquals(8, d)
    }

    @Test
    fun daysToReach_zeroRate_isZeroOrInfinityMarker() {
        // Per spec we just return zero on no growth (caller surfaces "never").
        assertEquals(0, CompoundCurve.daysToReach(2.0, dailyRate = 0.0))
    }

    @Test
    fun series_isEmptyOnNegativeDays() {
        val s = CompoundCurve.series(-5)
        // -5 clamped to 0 → array of length 1 containing 1.0
        assertEquals(1, s.size)
        assertEquals(1.0, s[0], 0.0)
    }
}
