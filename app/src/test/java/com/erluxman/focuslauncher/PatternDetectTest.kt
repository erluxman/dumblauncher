package com.erluxman.focuslauncher

import com.erluxman.focuslauncher.service.insights.PatternDetect
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class PatternDetectTest {

    @Test
    fun weakestHour_pickHighest() {
        val arr = IntArray(24).apply {
            this[9] = 5
            this[14] = 30
            this[20] = 20
        }
        val res = PatternDetect.weakestHourToday(arr)
        assertEquals(14, res?.hour)
        assertEquals(30, res?.minutes)
    }

    @Test
    fun weakestHour_capsAtNowHour() {
        val arr = IntArray(24).apply {
            this[14] = 30
            this[20] = 60
        }
        val res = PatternDetect.weakestHourToday(arr, nowHour = 15)
        assertEquals(14, res?.hour)
    }

    @Test
    fun weakestHour_zeroOrEmptyIsNull() {
        assertNull(PatternDetect.weakestHourToday(IntArray(0)))
        assertNull(PatternDetect.weakestHourToday(IntArray(24)))
    }

    @Test
    fun formatHour_humanReadable() {
        assertEquals("12 AM", PatternDetect.formatHour(0))
        assertEquals("9 AM", PatternDetect.formatHour(9))
        assertEquals("12 PM", PatternDetect.formatHour(12))
        assertEquals("3 PM", PatternDetect.formatHour(15))
        assertEquals("11 PM", PatternDetect.formatHour(23))
    }
}
