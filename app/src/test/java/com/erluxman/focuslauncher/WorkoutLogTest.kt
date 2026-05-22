package com.erluxman.focuslauncher

import com.erluxman.focuslauncher.service.fitness.WorkoutLog
import org.junit.Assert.assertEquals
import org.junit.Test

class WorkoutLogTest {

    @Test
    fun parse_skipsMalformed() {
        val s = WorkoutLog.parse(setOf("2026-05-22|45|Run", "x", "2026-05-21|notnum|Walk"))
        assertEquals(1, s.size)
        assertEquals("Run", s.first().kind)
    }

    @Test
    fun minutesOn_aggregatesPerDay() {
        val s = WorkoutLog.parse(setOf("2026-05-22|30|Run", "2026-05-22|45|Strength", "2026-05-21|60|Run"))
        assertEquals(75, WorkoutLog.minutesOn("2026-05-22", s))
        assertEquals(60, WorkoutLog.minutesOn("2026-05-21", s))
    }

    @Test
    fun consecutiveStreak_isLengthOfRun() {
        val s = WorkoutLog.parse(setOf("2026-05-22|30|Run", "2026-05-21|30|Run", "2026-05-19|30|Run"))
        assertEquals(2, WorkoutLog.consecutiveDayStreak("2026-05-22", s))
    }
}
