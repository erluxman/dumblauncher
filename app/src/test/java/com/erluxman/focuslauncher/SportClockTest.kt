package com.erluxman.focuslauncher

import com.erluxman.focuslauncher.service.fitness.SportClock
import com.erluxman.focuslauncher.service.fitness.WorkoutLog
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class SportClockTest {

    @Test
    fun daysSinceLast_nullIfNoneOfThatKind() {
        val s = listOf(WorkoutLog.Session("2026-05-15", 30, "Run"))
        assertNull(SportClock.daysSinceLast("Cycle", s, "2026-05-22"))
    }

    @Test
    fun daysSinceLast_basicDiff() {
        val s = listOf(
            WorkoutLog.Session("2026-05-22", 30, "Run"),
            WorkoutLog.Session("2026-05-15", 30, "Run"),
        )
        assertEquals(0, SportClock.daysSinceLast("Run", s, "2026-05-22"))
        assertEquals(2, SportClock.daysSinceLast("Run", s.drop(1), "2026-05-17"))
    }

    @Test
    fun daysSinceLast_ignoresCase() {
        val s = listOf(WorkoutLog.Session("2026-05-22", 30, "run"))
        assertEquals(0, SportClock.daysSinceLast("Run", s, "2026-05-22"))
    }

    @Test
    fun overtraining_trueWhenAllSevenDaysCovered() {
        val dates = (16..22).map { "2026-05-${"%02d".format(it)}" }
        val s = dates.map { WorkoutLog.Session(it, 30, "Run") }
        assertTrue(SportClock.overtraining(s, "2026-05-22"))
    }

    @Test
    fun overtraining_falseWithGap() {
        val s = listOf(
            WorkoutLog.Session("2026-05-16", 30, "Run"),
            WorkoutLog.Session("2026-05-17", 30, "Run"),
            WorkoutLog.Session("2026-05-22", 30, "Run"),
        )
        assertFalse(SportClock.overtraining(s, "2026-05-22"))
    }
}
