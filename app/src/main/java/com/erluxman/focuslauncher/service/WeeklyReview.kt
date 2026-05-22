package com.erluxman.focuslauncher.service

/**
 * LIFE-003 Sunday Life Review — aggregator only.
 *
 * Sums the trailing-7-day signals into a small immutable summary so
 * the card stays a pure render. We deliberately don't pull from
 * remote/historical data here — that's a future expansion.
 */
object WeeklyReview {

    data class Summary(
        val meditationMin: Int,
        val readingMin: Int,
        val workoutMin: Int,
        val meditationDays: Int,
        val readingDays: Int,
        val workoutDays: Int,
    )

    fun summarize(
        last7DaysIso: List<String>,
        meditation: List<MeditationLog.Session>,
        reading: List<ReadingLog.Session>,
        workout: List<WorkoutLog.Session>,
    ): Summary = Summary(
        meditationMin = MeditationLog.minutesIn(last7DaysIso, meditation),
        readingMin = ReadingLog.minutesIn(last7DaysIso, reading),
        workoutMin = WorkoutLog.minutesIn(last7DaysIso, workout),
        meditationDays = last7DaysIso.count { date -> meditation.any { it.isoDate == date } },
        readingDays = last7DaysIso.count { date -> reading.any { it.isoDate == date } },
        workoutDays = last7DaysIso.count { date -> workout.any { it.isoDate == date } },
    )
}
