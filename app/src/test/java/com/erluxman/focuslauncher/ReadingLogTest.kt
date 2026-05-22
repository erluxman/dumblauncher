package com.erluxman.focuslauncher

import com.erluxman.focuslauncher.service.ReadingLog
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ReadingLogTest {

    @Test
    fun parse_dropsMalformed() {
        val s = ReadingLog.parse(setOf("2026-05-22|45", "bad", "x|y"))
        assertEquals(1, s.size)
        assertEquals(45, s.first().minutes)
    }

    @Test
    fun minutesOn_sumsPerDay() {
        val s = ReadingLog.parse(setOf("2026-05-22|10", "2026-05-22|20", "2026-05-21|30"))
        assertEquals(30, ReadingLog.minutesOn("2026-05-22", s))
        assertEquals(30, ReadingLog.minutesOn("2026-05-21", s))
    }

    @Test
    fun bookEquivalent_zeroWhenZero() {
        assertEquals(0.0, ReadingLog.bookEquivalent(0), 0.0)
    }

    @Test
    fun bookEquivalent_grows() {
        val a = ReadingLog.bookEquivalent(60)
        val b = ReadingLog.bookEquivalent(600)
        assertTrue(b > a)
    }
}
