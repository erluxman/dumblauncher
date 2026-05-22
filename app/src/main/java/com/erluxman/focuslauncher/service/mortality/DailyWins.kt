package com.erluxman.focuslauncher.service.mortality

/**
 * Pure aggregator that tallies today's wins across modules into a
 * single summary the UI can render or share. Each field is opt-in:
 * callers pass 0 / empty for signals they don't track.
 */
object DailyWins {

    data class Summary(
        val todosDone: Int,
        val focusSessions: Int,
        val meditationMin: Int,
        val readingMin: Int,
        val workoutMin: Int,
        val timeBankMin: Int,
    ) {
        val total: Int get() = todosDone + focusSessions + (meditationMin / 10) +
            (readingMin / 15) + (workoutMin / 15) + (timeBankMin / 30)
    }

    fun summarize(
        todosDoneToday: Int,
        focusSessionsToday: Int,
        meditationMinToday: Int,
        readingMinToday: Int,
        workoutMinToday: Int,
        timeBankAddedToday: Int,
    ): Summary = Summary(
        todosDone = todosDoneToday.coerceAtLeast(0),
        focusSessions = focusSessionsToday.coerceAtLeast(0),
        meditationMin = meditationMinToday.coerceAtLeast(0),
        readingMin = readingMinToday.coerceAtLeast(0),
        workoutMin = workoutMinToday.coerceAtLeast(0),
        timeBankMin = timeBankAddedToday.coerceAtLeast(0),
    )
}
