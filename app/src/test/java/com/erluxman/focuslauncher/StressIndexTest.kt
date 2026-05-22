package com.erluxman.focuslauncher

import com.erluxman.focuslauncher.service.StressIndex
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class StressIndexTest {

    @Test
    fun compute_quietDayIsClear() {
        val r = StressIndex.compute(unlocksToday = 20, sleepMinutesLastNight = 8 * 60)
        assertEquals("CLEAR", r.label)
    }

    @Test
    fun compute_manyUnlocksFlipsToStormy() {
        val r = StressIndex.compute(unlocksToday = 300, sleepMinutesLastNight = 8 * 60)
        assertTrue(r.unlocksSub >= 70)
        assertEquals("STORMY", r.label)
    }

    @Test
    fun compute_lowSleepIsAtLeastOvercast() {
        val r = StressIndex.compute(unlocksToday = 0, sleepMinutesLastNight = 4 * 60)
        // half target gives full 100
        assertEquals(100, r.sleepSub)
        assertEquals("STORMY", r.label)
    }

    @Test
    fun compute_unknownSleepIsCappedAtHundred() {
        val r = StressIndex.compute(unlocksToday = 0, sleepMinutesLastNight = 0)
        assertEquals(100, r.sleepSub)
    }

    @Test
    fun compute_maxOfTwoSignals() {
        val high = StressIndex.compute(200, 8 * 60).total
        val low = StressIndex.compute(0, 8 * 60).total
        assertTrue(high > low)
    }
}
