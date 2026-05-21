package com.erluxman.focuslauncher.service

/**
 * Pure logic for PSYCH-008: a periodic "are you sure?" full-screen intervention
 * triggered every Nth same-day opening of a distraction app.
 */
object UnlockIntervention {

    /** N — show intervention every Nth open (and at N itself). */
    const val THRESHOLD = 14

    /** Show intervention when this open is the Nth, or any multiple of N after. */
    fun shouldShow(count: Int): Boolean = count >= THRESHOLD && count % THRESHOLD == 0

    fun parseCount(stored: Set<String>, todayIso: String, pkg: String): Int {
        val prefix = "$todayIso|$pkg|"
        return stored.firstOrNull { it.startsWith(prefix) }
            ?.substringAfterLast("|")
            ?.toIntOrNull()
            ?: 0
    }
}
