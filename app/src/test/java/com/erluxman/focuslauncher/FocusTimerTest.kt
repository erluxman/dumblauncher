package com.erluxman.focuslauncher

import com.erluxman.focuslauncher.ui.focus.FocusPhase
import com.erluxman.focuslauncher.ui.focus.FocusTimer
import org.junit.Assert.assertEquals
import org.junit.Test

class FocusTimerTest {

    @Test
    fun workDurationIs25Minutes() {
        assertEquals(25L * 60L * 1000L, FocusTimer.WORK_MS)
        assertEquals(FocusTimer.WORK_MS, FocusTimer.durationFor(FocusPhase.WORK))
    }

    @Test
    fun breakDurationIs5Minutes() {
        assertEquals(5L * 60L * 1000L, FocusTimer.BREAK_MS)
        assertEquals(FocusTimer.BREAK_MS, FocusTimer.durationFor(FocusPhase.BREAK))
    }

    @Test
    fun nextPhase_togglesBetweenWorkAndBreak() {
        assertEquals(FocusPhase.BREAK, FocusTimer.nextPhase(FocusPhase.WORK))
        assertEquals(FocusPhase.WORK, FocusTimer.nextPhase(FocusPhase.BREAK))
    }

    @Test
    fun format_zeroPadsBelowTen() {
        assertEquals("00:05", FocusTimer.format(5_000L))
        assertEquals("01:30", FocusTimer.format(90_000L))
        assertEquals("25:00", FocusTimer.format(FocusTimer.WORK_MS))
    }
}
