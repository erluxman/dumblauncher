package com.erluxman.focuslauncher

import com.erluxman.focuslauncher.service.UsageStatsHelper
import org.junit.Assert.assertEquals
import org.junit.Test

class BehaviorStateTest {

    @Test
    fun under50PctOfTargetIsThriving() {
        val r = UsageStatsHelper.deriveBehaviorState(screenMinutes = 30, targetMinutes = 180)
        assertEquals("THRIVING", r.state)
    }

    @Test
    fun between50And100PctIsNeutral() {
        val r = UsageStatsHelper.deriveBehaviorState(screenMinutes = 120, targetMinutes = 180)
        assertEquals("NEUTRAL", r.state)
    }

    @Test
    fun between100And150PctIsDrifting() {
        val r = UsageStatsHelper.deriveBehaviorState(screenMinutes = 200, targetMinutes = 180)
        assertEquals("DRIFTING", r.state)
    }

    @Test
    fun between150And200PctIsSinking() {
        val r = UsageStatsHelper.deriveBehaviorState(screenMinutes = 320, targetMinutes = 180)
        assertEquals("SINKING", r.state)
    }

    @Test
    fun over200PctIsDrowning() {
        val r = UsageStatsHelper.deriveBehaviorState(screenMinutes = 600, targetMinutes = 180)
        assertEquals("DROWNING", r.state)
    }

    @Test
    fun zeroTargetDefaultsToThriving() {
        val r = UsageStatsHelper.deriveBehaviorState(screenMinutes = 50, targetMinutes = 0)
        assertEquals("THRIVING", r.state)
    }
}
