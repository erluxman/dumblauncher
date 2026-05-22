package com.erluxman.focuslauncher.service

/**
 * LIFE-006 Built Wrapped — pure aggregator + headline.
 *
 * Annual recap data is computed here so the (eventual) share card can
 * be a thin render. No social wiring yet.
 */
object BuiltWrapped {

    data class Year(
        val totalFocusMinutes: Int,
        val totalDistractionMinutes: Int,
        val projectsShipped: Int,
        val streakBest: Int,
        val bookCount: Int,
    )

    fun headline(year: Year): String {
        val focusHours = year.totalFocusMinutes / 60
        val distractionHours = year.totalDistractionMinutes / 60
        val pieces = mutableListOf<String>()
        if (focusHours > 0) pieces += "$focusHours focused hours"
        if (year.streakBest > 0) pieces += "${year.streakBest}-day streak"
        if (year.projectsShipped > 0) pieces += "${year.projectsShipped} projects shipped"
        if (year.bookCount > 0) pieces += "${year.bookCount} books"
        if (distractionHours > 0) pieces += "${distractionHours}h on a feed"
        return if (pieces.isEmpty()) "A quiet year." else pieces.joinToString(" · ")
    }

    /** Single number people can share as a 'score'. */
    fun score(year: Year): Int {
        val focus = year.totalFocusMinutes / 60
        val distractionPenalty = year.totalDistractionMinutes / 120 // half-weight
        val books = year.bookCount * 30
        val projects = year.projectsShipped * 100
        val streak = year.streakBest
        return (focus + books + projects + streak - distractionPenalty).coerceAtLeast(0)
    }
}
