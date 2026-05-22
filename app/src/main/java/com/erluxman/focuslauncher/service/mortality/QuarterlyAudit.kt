package com.erluxman.focuslauncher.service.mortality

import com.erluxman.focuslauncher.service.MeditationLog
import com.erluxman.focuslauncher.service.ReadingLog
import com.erluxman.focuslauncher.service.fitness.WorkoutLog

/**
 * LIFE-004 Quarterly Self-Audit aggregator.
 *
 * Extends the weekly-review shape to a 90-day window with per-domain
 * "days covered" plus a simple A-F grade by coverage rate.
 */
object QuarterlyAudit {

    data class Audit(
        val meditationMin: Int,
        val readingMin: Int,
        val workoutMin: Int,
        val meditationDays: Int,
        val readingDays: Int,
        val workoutDays: Int,
        val windowDays: Int,
    ) {
        val coverageRate: Double get() {
            if (windowDays <= 0) return 0.0
            val avgDays = (meditationDays + readingDays + workoutDays) / 3.0
            return (avgDays / windowDays).coerceIn(0.0, 1.0)
        }

        val grade: Char get() = when {
            coverageRate >= 0.85 -> 'A'
            coverageRate >= 0.70 -> 'B'
            coverageRate >= 0.50 -> 'C'
            coverageRate >= 0.30 -> 'D'
            else -> 'F'
        }
    }

    fun compute(
        windowDaysIso: List<String>,
        meditation: List<MeditationLog.Session>,
        reading: List<ReadingLog.Session>,
        workout: List<WorkoutLog.Session>,
    ): Audit = Audit(
        meditationMin = MeditationLog.minutesIn(windowDaysIso, meditation),
        readingMin = ReadingLog.minutesIn(windowDaysIso, reading),
        workoutMin = WorkoutLog.minutesIn(windowDaysIso, workout),
        meditationDays = windowDaysIso.count { date -> meditation.any { it.isoDate == date } },
        readingDays = windowDaysIso.count { date -> reading.any { it.isoDate == date } },
        workoutDays = windowDaysIso.count { date -> workout.any { it.isoDate == date } },
        windowDays = windowDaysIso.size,
    )
}
