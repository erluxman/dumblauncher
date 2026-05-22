package com.erluxman.focuslauncher.service

/**
 * PSYCH-009 Deathbed Simulator.
 *
 * Projects today's distraction minutes across the user's remaining life.
 * Surfaces in years of waking life consumed, plus a single-line analogy.
 * Pure-fn, no formatting opinions.
 */
object Deathbed {

    const val WAKING_HOURS_PER_DAY = 16
    const val DEFAULT_END_AGE = 80

    fun lifetimeMinutes(dailyDistractionMinutes: Int, currentAge: Int, endAge: Int = DEFAULT_END_AGE): Long {
        if (dailyDistractionMinutes <= 0 || currentAge !in 1..120 || endAge <= currentAge) return 0L
        val daysLeft = (endAge - currentAge).toLong() * 365L
        return dailyDistractionMinutes.toLong() * daysLeft
    }

    fun lifetimeYearsOfWaking(dailyDistractionMinutes: Int, currentAge: Int, endAge: Int = DEFAULT_END_AGE): Double {
        val mins = lifetimeMinutes(dailyDistractionMinutes, currentAge, endAge).toDouble()
        if (mins <= 0.0) return 0.0
        val wakingMinutesPerYear = 365.0 * WAKING_HOURS_PER_DAY * 60.0
        return mins / wakingMinutesPerYear
    }

    /** Equivalents the card cycles through; caller picks one by today's index. */
    fun analogyFor(years: Double): String {
        val y = years.coerceAtLeast(0.0)
        return when {
            y < 0.5 -> "less than half a year of waking life."
            y < 1.0 -> "almost a full waking year."
            y < 2.0 -> "${"%.1f".format(y)} waking years — a master's degree, twice."
            y < 5.0 -> "${"%.1f".format(y)} waking years — a marriage's worth of evenings."
            y < 10.0 -> "${"%.1f".format(y)} waking years — childhood, replayed."
            else -> "${"%.1f".format(y)} waking years — a decade of weekdays."
        }
    }
}
