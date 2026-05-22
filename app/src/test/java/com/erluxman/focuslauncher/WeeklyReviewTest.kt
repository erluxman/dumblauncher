package com.erluxman.focuslauncher

import com.erluxman.focuslauncher.service.MeditationLog
import com.erluxman.focuslauncher.service.ReadingLog
import com.erluxman.focuslauncher.service.WeeklyReview
import com.erluxman.focuslauncher.service.WorkoutLog
import org.junit.Assert.assertEquals
import org.junit.Test

class WeeklyReviewTest {

    private val week = listOf(
        "2026-05-22", "2026-05-21", "2026-05-20", "2026-05-19",
        "2026-05-18", "2026-05-17", "2026-05-16",
    )

    @Test
    fun summarize_emptyHasZeros() {
        val s = WeeklyReview.summarize(week, emptyList(), emptyList(), emptyList())
        assertEquals(0, s.meditationMin)
        assertEquals(0, s.readingMin)
        assertEquals(0, s.workoutMin)
        assertEquals(0, s.meditationDays)
    }

    @Test
    fun summarize_countsTotalsAndDistinctDays() {
        val s = WeeklyReview.summarize(
            week,
            meditation = listOf(
                MeditationLog.Session("2026-05-22", 10, "Breath"),
                MeditationLog.Session("2026-05-22", 5, "Body"),
                MeditationLog.Session("2026-05-20", 20, "Breath"),
            ),
            reading = listOf(
                ReadingLog.Session("2026-05-21", 30),
                ReadingLog.Session("2026-05-21", 15),
            ),
            workout = listOf(
                WorkoutLog.Session("2026-05-19", 45, "Run"),
            )
        )
        assertEquals(35, s.meditationMin)
        assertEquals(2, s.meditationDays)
        assertEquals(45, s.readingMin)
        assertEquals(1, s.readingDays)
        assertEquals(45, s.workoutMin)
        assertEquals(1, s.workoutDays)
    }

    @Test
    fun summarize_ignoresOldEntries() {
        val s = WeeklyReview.summarize(
            week,
            meditation = listOf(MeditationLog.Session("2026-04-01", 999, "Breath")),
            reading = emptyList(),
            workout = emptyList(),
        )
        assertEquals(0, s.meditationMin)
    }
}
