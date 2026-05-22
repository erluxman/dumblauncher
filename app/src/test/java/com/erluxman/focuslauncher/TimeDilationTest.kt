package com.erluxman.focuslauncher

import com.erluxman.focuslauncher.service.insights.TimeDilation
import org.junit.Assert.assertEquals
import org.junit.Test

class TimeDilationTest {

    @Test
    fun dilation_default3x() {
        assertEquals(30, TimeDilation.dilatedMinutes(10))
        assertEquals(180, TimeDilation.dilatedMinutes(60))
    }

    @Test
    fun dilation_customMultiplier() {
        assertEquals(25, TimeDilation.dilatedMinutes(10, 2.5))
        assertEquals(0, TimeDilation.dilatedMinutes(0, 5.0))
    }

    @Test
    fun dilation_negativeInputIsZero() {
        assertEquals(0, TimeDilation.dilatedMinutes(-1))
    }

    @Test
    fun formatHm_picksRightUnits() {
        assertEquals("0m", TimeDilation.formatHm(0))
        assertEquals("23m", TimeDilation.formatHm(23))
        assertEquals("1h", TimeDilation.formatHm(60))
        assertEquals("1h 12m", TimeDilation.formatHm(72))
        assertEquals("0m", TimeDilation.formatHm(-5))
    }
}
