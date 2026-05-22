package com.erluxman.focuslauncher.service

/**
 * INTEG-008 Code Output Integration — manual entry path.
 *
 * Entries: `"iso|commitCount"`. OAuth sync against GitHub is parked
 * until we have the backend story.
 */
object CommitLog {

    data class Entry(val isoDate: String, val commits: Int)

    fun parse(entries: Set<String>): List<Entry> = entries.mapNotNull { e ->
        val parts = e.split("|", limit = 2)
        if (parts.size != 2) return@mapNotNull null
        val n = parts[1].toIntOrNull() ?: return@mapNotNull null
        Entry(parts[0], n)
    }

    fun commitsOn(date: String, entries: List<Entry>): Int =
        entries.firstOrNull { it.isoDate == date }?.commits ?: 0

    fun commitsIn(daysIso: List<String>, entries: List<Entry>): Int =
        entries.filter { it.isoDate in daysIso }.sumOf { it.commits }

    val PRESET_INCREMENTS = listOf(1, 3, 5)
}
