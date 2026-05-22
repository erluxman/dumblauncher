package com.erluxman.focuslauncher
import com.erluxman.focuslauncher.service.lobby.SleepWindow

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SleepWindowTest {

    @Test
    fun isInWindow_defaultEveningToMorning() {
        assertTrue(SleepWindow.isInWindow(22))
        assertTrue(SleepWindow.isInWindow(23))
        assertTrue(SleepWindow.isInWindow(0))
        assertTrue(SleepWindow.isInWindow(4))
        assertFalse(SleepWindow.isInWindow(5))
        assertFalse(SleepWindow.isInWindow(12))
        assertFalse(SleepWindow.isInWindow(21))
    }

    @Test
    fun isInWindow_sameDayRange_doesNotWrap() {
        // Daytime nap: 13 to 16
        assertTrue(SleepWindow.isInWindow(13, cutoffHour = 13, wakeHour = 16))
        assertTrue(SleepWindow.isInWindow(15, cutoffHour = 13, wakeHour = 16))
        assertFalse(SleepWindow.isInWindow(16, cutoffHour = 13, wakeHour = 16))
        assertFalse(SleepWindow.isInWindow(12, cutoffHour = 13, wakeHour = 16))
    }

    @Test
    fun isInWindow_cutoffEqualsWake_isNever() {
        assertFalse(SleepWindow.isInWindow(0, 8, 8))
        assertFalse(SleepWindow.isInWindow(8, 8, 8))
        assertFalse(SleepWindow.isInWindow(23, 8, 8))
    }

    @Test
    fun isInWindow_handlesNegativeAndOversize() {
        assertTrue(SleepWindow.isInWindow(26, cutoffHour = 0, wakeHour = 4))  // 26 mod 24 = 2
        assertFalse(SleepWindow.isInWindow(-1, cutoffHour = 0, wakeHour = 4)) // -1 mod 24 = 23
    }

    @Test
    fun minutesUntilWake_simpleAndCrossingMidnight() {
        // 23:30 -> wake 5  => 5*60 = 300, since 0..now=23*60+30=1410: diff = (24*60 - 1410) + 300 = 330
        assertEquals(330, SleepWindow.minutesUntilWake(23, 30, wakeHour = 5))
        // 03:00 -> wake 5 => 5*60 - 3*60 = 120
        assertEquals(120, SleepWindow.minutesUntilWake(3, 0, wakeHour = 5))
    }
}
