package com.erluxman.focuslauncher
import com.erluxman.focuslauncher.service.lobby.DimmingCurve

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class DimmingCurveTest {

    @Test
    fun startsAtMinAlpha() {
        val a = DimmingCurve.alphaForElapsed(0L)
        assertEquals(DimmingCurve.MIN_ALPHA, a, 0.0001f)
    }

    @Test
    fun reachesMaxAlphaAtRampEnd() {
        val a = DimmingCurve.alphaForElapsed(DimmingCurve.RAMP_MS)
        assertEquals(DimmingCurve.MAX_ALPHA, a, 0.0001f)
    }

    @Test
    fun clampsAtMaxAfterRamp() {
        val a = DimmingCurve.alphaForElapsed(DimmingCurve.RAMP_MS * 10)
        assertEquals(DimmingCurve.MAX_ALPHA, a, 0.0001f)
    }

    @Test
    fun midwayIsHalfBetween() {
        val a = DimmingCurve.alphaForElapsed(DimmingCurve.RAMP_MS / 2)
        val expected = (DimmingCurve.MIN_ALPHA + DimmingCurve.MAX_ALPHA) / 2f
        assertEquals(expected, a, 0.0001f)
    }

    @Test
    fun negativeElapsedTreatedAsZero() {
        val a = DimmingCurve.alphaForElapsed(-1000L)
        assertEquals(DimmingCurve.MIN_ALPHA, a, 0.0001f)
    }

    @Test
    fun alphaIsAlwaysInRange() {
        for (ms in listOf(0L, 1L, 100L, 60_000L, 5 * 60_000L, 10 * 60_000L)) {
            val a = DimmingCurve.alphaForElapsed(ms)
            assertTrue("$ms -> $a", a in DimmingCurve.MIN_ALPHA..DimmingCurve.MAX_ALPHA)
        }
    }
}
