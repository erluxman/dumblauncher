package com.erluxman.focuslauncher

import com.erluxman.focuslauncher.service.MeditationLog
import com.erluxman.focuslauncher.service.mortality.QuarterlyAudit
import com.erluxman.focuslauncher.service.ReadingLog
import com.erluxman.focuslauncher.service.WorkoutLog
import org.junit.Assert.assertEquals
import org.junit.Test

class QuarterlyAuditTest {

    @Test
    fun empty_isFGrade() {
        val window = (1..30).map { "2026-04-${"%02d".format(it)}" }
        val a = QuarterlyAudit.compute(window, emptyList(), emptyList(), emptyList())
        assertEquals('F', a.grade)
        assertEquals(0.0, a.coverageRate, 0.0)
    }

    @Test
    fun fullCoverage_isAGrade() {
        val window = (1..10).map { "2026-04-${"%02d".format(it)}" }
        val meds = window.map { MeditationLog.Session(it, 10, "Breath") }
        val reads = window.map { ReadingLog.Session(it, 15) }
        val workouts = window.map { WorkoutLog.Session(it, 30, "Run") }
        val a = QuarterlyAudit.compute(window, meds, reads, workouts)
        assertEquals('A', a.grade)
        assertEquals(1.0, a.coverageRate, 1e-9)
    }

    @Test
    fun gradeBoundaries() {
        val window = (1..10).map { "2026-04-${"%02d".format(it)}" }
        // Cover ~55% across domains → C
        val partial = window.take(5).map { MeditationLog.Session(it, 10, "Breath") } +
            window.take(6).map { MeditationLog.Session(it, 10, "Body") }
        val reads = window.take(5).map { ReadingLog.Session(it, 15) }
        val workouts = window.take(6).map { WorkoutLog.Session(it, 30, "Run") }
        val a = QuarterlyAudit.compute(window, partial, reads, workouts)
        // 5/10 + 5/10 + 6/10 → 5.33/10 → 0.53 → C
        assertEquals('C', a.grade)
    }
}
