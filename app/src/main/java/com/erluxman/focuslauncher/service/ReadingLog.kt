package com.erluxman.focuslauncher.service

/**
 * READ-001 Reading Log (manual entry).
 *
 * Mirrors MeditationLog but for reading minutes. We can revisit Kindle
 * / Audible / Readwise integration later (INTEG-009); for now this is a
 * deliberate, opinionated nudge to declare reading time.
 */
object ReadingLog {

    data class Session(val isoDate: String, val minutes: Int)

    fun parse(entries: Set<String>): List<Session> = entries.mapNotNull { e ->
        val parts = e.split("|", limit = 2)
        if (parts.size != 2) return@mapNotNull null
        val mins = parts[1].toIntOrNull() ?: return@mapNotNull null
        Session(parts[0], mins)
    }

    fun minutesOn(date: String, sessions: List<Session>): Int =
        sessions.filter { it.isoDate == date }.sumOf { it.minutes }

    fun minutesIn(daysIso: List<String>, sessions: List<Session>): Int =
        sessions.filter { it.isoDate in daysIso }.sumOf { it.minutes }

    /** "X books at 200 pages × ~2min/page" — for the weekly headline. */
    fun bookEquivalent(totalMinutes: Int, minutesPerPage: Double = 2.0, pagesPerBook: Int = 250): Double {
        if (totalMinutes <= 0) return 0.0
        val pages = totalMinutes / minutesPerPage
        return pages / pagesPerBook
    }

    val PRESET_MINUTES = listOf(10, 20, 30, 45, 60)
}
