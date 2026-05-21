package com.erluxman.focuslauncher

import com.erluxman.focuslauncher.service.LegacyCounter
import org.junit.Assert.assertEquals
import org.junit.Test

class LegacyCounterTest {

    @Test
    fun zeros() {
        assertEquals(0, LegacyCounter.totalBuilderMinutes(0, 0))
    }

    @Test
    fun todosOnly() {
        assertEquals(45, LegacyCounter.totalBuilderMinutes(3, 0))
    }

    @Test
    fun focusOnly() {
        assertEquals(50, LegacyCounter.totalBuilderMinutes(0, 2))
    }

    @Test
    fun mix() {
        assertEquals(3 * 15 + 4 * 25, LegacyCounter.totalBuilderMinutes(3, 4))
    }

    @Test
    fun formatBelowOneHour() {
        assertEquals("45m", LegacyCounter.format(45))
    }

    @Test
    fun formatBelowOneDay() {
        assertEquals("2h 30m", LegacyCounter.format(150))
    }

    @Test
    fun formatOverOneDay() {
        assertEquals("1d 5h", LegacyCounter.format(24 * 60 + 5 * 60))
    }
}
