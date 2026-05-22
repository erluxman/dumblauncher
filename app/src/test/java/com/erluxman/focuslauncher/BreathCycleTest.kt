package com.erluxman.focuslauncher
import com.erluxman.focuslauncher.service.lobby.BreathCycle

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class BreathCycleTest {

    @Test
    fun phase_transitionsAreCorrect() {
        assertEquals(BreathCycle.Phase.INHALE, BreathCycle.phaseAt(0))
        assertEquals(BreathCycle.Phase.INHALE, BreathCycle.phaseAt(3_999))
        assertEquals(BreathCycle.Phase.HOLD, BreathCycle.phaseAt(4_000))
        assertEquals(BreathCycle.Phase.HOLD, BreathCycle.phaseAt(10_999))
        assertEquals(BreathCycle.Phase.EXHALE, BreathCycle.phaseAt(11_000))
        assertEquals(BreathCycle.Phase.EXHALE, BreathCycle.phaseAt(18_999))
        assertEquals(BreathCycle.Phase.DONE, BreathCycle.phaseAt(BreathCycle.CYCLE_MS))
        assertEquals(BreathCycle.Phase.DONE, BreathCycle.phaseAt(BreathCycle.CYCLE_MS + 1))
    }

    @Test
    fun cycleLength_exceeds16Seconds() {
        assertTrue("4-7-8 must be ≥16s", BreathCycle.CYCLE_MS >= 16_000L)
    }

    @Test
    fun progress_isAlwaysBetweenZeroAndOne() {
        for (t in -1000L..BreathCycle.CYCLE_MS + 1000L step 250L) {
            val p = BreathCycle.progressAt(t)
            assertTrue("$t produced $p", p in 0f..1f)
        }
    }

    @Test
    fun circleScale_growsThenHoldsThenShrinks() {
        val start = BreathCycle.circleScaleAt(0)
        val peak = BreathCycle.circleScaleAt(BreathCycle.INHALE_MS)
        val hold = BreathCycle.circleScaleAt(BreathCycle.INHALE_MS + BreathCycle.HOLD_MS / 2)
        val end = BreathCycle.circleScaleAt(BreathCycle.CYCLE_MS - 1)
        assertTrue("starts small", start < 0.3f)
        assertEquals(1f, peak, 0.01f)
        assertEquals(1f, hold, 0.01f)
        assertTrue("returns to small", end < 0.3f)
    }
}
