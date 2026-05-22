package com.erluxman.focuslauncher

import com.erluxman.focuslauncher.service.CaffeineMath
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CaffeineMathTest {

    @Test
    fun remaining_oneHalfLife_isHalf() {
        assertEquals(50.0, CaffeineMath.remainingMg(100.0, hoursElapsed = 5.0), 0.001)
    }

    @Test
    fun remaining_twoHalfLives_isQuarter() {
        assertEquals(25.0, CaffeineMath.remainingMg(100.0, hoursElapsed = 10.0), 0.001)
    }

    @Test
    fun remaining_zeroOrNegative_inputsAreSafe() {
        assertEquals(0.0, CaffeineMath.remainingMg(0.0, 5.0), 0.0)
        assertEquals(0.0, CaffeineMath.remainingMg(100.0, -1.0), 0.0)
        assertEquals(0.0, CaffeineMath.remainingMg(100.0, 5.0, halfLifeHours = 0.0), 0.0)
    }

    @Test
    fun remainingAt_sumsAllDoses() {
        val now = 24L * 3_600_000L
        val drinkHoursAgo = 5.0
        val takenMs = now - (drinkHoursAgo * 3_600_000L).toLong()
        val one = CaffeineMath.remainingMgAt(
            listOf(CaffeineMath.Dose(100, takenMs)),
            nowMs = now
        )
        val two = CaffeineMath.remainingMgAt(
            listOf(
                CaffeineMath.Dose(100, takenMs),
                CaffeineMath.Dose(100, takenMs),
            ),
            nowMs = now
        )
        assertEquals(50.0, one, 0.001)
        assertEquals(100.0, two, 0.001)
    }

    @Test
    fun decayFraction_neverAboveOne_neverBelowZero() {
        assertEquals(1.0, CaffeineMath.decayFraction(0.0), 0.0)
        val late = CaffeineMath.decayFraction(48.0)
        assertTrue(late in 0.0..1.0)
        assertTrue(late < 0.01)
    }

    @Test
    fun presets_areReasonable() {
        // Sanity check: nothing absurd, nothing zero.
        CaffeineMath.PRESETS.forEach {
            assertTrue("${it.label} mg should be 1..500", it.mg in 1..500)
            assertTrue(it.label.isNotBlank())
        }
    }
}
