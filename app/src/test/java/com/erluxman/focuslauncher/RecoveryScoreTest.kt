package com.erluxman.focuslauncher

import com.erluxman.focuslauncher.service.fitness.RecoveryScore
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class RecoveryScoreTest {

    @Test
    fun compute_zeroInputsScoreLow() {
        val s = RecoveryScore.compute(sleepMinutes = 0, steps = 0, feel1to10 = 1)
        assertEquals(0, s.total)
        assertEquals("DEPLETED", s.label)
    }

    @Test
    fun compute_perfectInputsScoreHigh() {
        val s = RecoveryScore.compute(sleepMinutes = 8 * 60, steps = 8_000, feel1to10 = 10)
        assertEquals(100, s.total)
        assertEquals("READY", s.label)
    }

    @Test
    fun compute_clampsLabels() {
        assertEquals("STEADY", RecoveryScore.compute(7 * 60, 6_000, 6).label)
        assertEquals("DRAINED", RecoveryScore.compute(5 * 60, 3_000, 4).label)
    }

    @Test
    fun activity_subscore_penalisesOvershoot() {
        val under = RecoveryScore.compute(8 * 60, 8_000, 10)
        val over = RecoveryScore.compute(8 * 60, 16_000, 10)
        assertTrue("overshoot is penalised", over.activitySub < under.activitySub)
    }

    @Test
    fun feel_subscoreScalesLinearly() {
        val low = RecoveryScore.compute(8 * 60, 8_000, 1)
        val high = RecoveryScore.compute(8 * 60, 8_000, 10)
        assertTrue(low.feelSub < high.feelSub)
        assertEquals(100, high.feelSub)
    }
}
