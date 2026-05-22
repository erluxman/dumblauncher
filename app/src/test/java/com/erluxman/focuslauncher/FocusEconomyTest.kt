package com.erluxman.focuslauncher

import com.erluxman.focuslauncher.service.tracks.FocusEconomy
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class FocusEconomyTest {

    @Test
    fun costMirrorsMinutes() {
        assertEquals(0, FocusEconomy.costFor(0))
        assertEquals(5, FocusEconomy.costFor(5))
        assertEquals(60, FocusEconomy.costFor(60))
        assertEquals(0, FocusEconomy.costFor(-5))
    }

    @Test
    fun minutesMirrorPoints() {
        assertEquals(5, FocusEconomy.minutesFor(5))
        assertEquals(0, FocusEconomy.minutesFor(-1))
    }

    @Test
    fun canAfford_checksBalance() {
        assertTrue(FocusEconomy.canAfford(points = 30, minutes = 15))
        assertFalse(FocusEconomy.canAfford(points = 5, minutes = 30))
        assertTrue(FocusEconomy.canAfford(points = 30, minutes = 30))
    }

    @Test
    fun tiers_arePositive() {
        FocusEconomy.TIERS.forEach { assertTrue(it > 0) }
    }
}
