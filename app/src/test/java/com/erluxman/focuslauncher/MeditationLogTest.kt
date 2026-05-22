package com.erluxman.focuslauncher

import com.erluxman.focuslauncher.service.MeditationLog
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class MeditationLogTest {

    @Test
    fun parse_skipsMalformedEntries() {
        val raw = setOf("2026-05-22|10|Breath", "incomplete", "2026-05-21|notnumber|Body")
        val s = MeditationLog.parse(raw)
        assertEquals(1, s.size)
        assertEquals(10, s.first().minutes)
        assertEquals("Breath", s.first().technique)
    }

    @Test
    fun minutesOn_sumsPerDay() {
        val raw = setOf("2026-05-22|10|Breath", "2026-05-22|5|Body", "2026-05-21|20|Breath")
        val s = MeditationLog.parse(raw)
        assertEquals(15, MeditationLog.minutesOn("2026-05-22", s))
        assertEquals(20, MeditationLog.minutesOn("2026-05-21", s))
        assertEquals(0, MeditationLog.minutesOn("2026-05-20", s))
    }

    @Test
    fun minutesIn_sumsAcrossDays() {
        val raw = setOf("2026-05-22|10|Breath", "2026-05-21|20|Breath", "2026-05-20|7|Walking")
        val s = MeditationLog.parse(raw)
        val total = MeditationLog.minutesIn(
            listOf("2026-05-22", "2026-05-21", "2026-05-20"),
            s
        )
        assertEquals(37, total)
    }

    @Test
    fun consecutiveDayStreak_isLengthOfRunUpToToday() {
        val raw = setOf("2026-05-22|10|Breath", "2026-05-21|20|Breath", "2026-05-20|7|Walking")
        val s = MeditationLog.parse(raw)
        assertEquals(3, MeditationLog.consecutiveDayStreak("2026-05-22", s))
    }

    @Test
    fun consecutiveDayStreak_zeroWhenGap() {
        val raw = setOf("2026-05-22|10|Breath", "2026-05-19|20|Breath")
        val s = MeditationLog.parse(raw)
        assertEquals(1, MeditationLog.consecutiveDayStreak("2026-05-22", s))
    }

    @Test
    fun presets_arePopulated() {
        assertTrue(MeditationLog.PRESET_TECHNIQUES.isNotEmpty())
        assertTrue(MeditationLog.PRESET_MINUTES.isNotEmpty())
    }
}
