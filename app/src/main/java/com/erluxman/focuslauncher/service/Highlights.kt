package com.erluxman.focuslauncher.service

/**
 * READ-002 Daily Highlight Resurfacing.
 *
 * Stable per-day selection so the same highlight is shown all day,
 * and rotates predictably across days. No backend; the highlights set
 * is whatever the user pasted in.
 */
object Highlights {

    /** Deterministically pick a highlight for the given ISO date. */
    fun pickForToday(highlights: Set<String>, todayIso: String): String? {
        if (highlights.isEmpty()) return null
        val sorted = highlights.sorted()
        val seed = todayIso.hashCode()
        val idx = ((seed % sorted.size) + sorted.size) % sorted.size
        return sorted[idx]
    }
}
