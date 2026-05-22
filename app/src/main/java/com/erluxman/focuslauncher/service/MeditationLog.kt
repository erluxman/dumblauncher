package com.erluxman.focuslauncher.service

/**
 * MIND-001 Meditation Session Log.
 *
 * Stores sessions as `"iso|minutes|technique"` strings to fit DataStore's
 * Set<String>. Pure helpers parse, aggregate, and bucket entries.
 */
object MeditationLog {

    data class Session(val isoDate: String, val minutes: Int, val technique: String)

    fun parse(entries: Set<String>): List<Session> = entries.mapNotNull { e ->
        val parts = e.split("|", limit = 3)
        if (parts.size != 3) return@mapNotNull null
        val mins = parts[1].toIntOrNull() ?: return@mapNotNull null
        Session(parts[0], mins, parts[2])
    }

    fun minutesOn(date: String, sessions: List<Session>): Int =
        sessions.filter { it.isoDate == date }.sumOf { it.minutes }

    fun minutesIn(daysIso: List<String>, sessions: List<Session>): Int =
        sessions.filter { it.isoDate in daysIso }.sumOf { it.minutes }

    /** Streak of consecutive past days (including [today]) with at least one minute. */
    fun consecutiveDayStreak(today: String, sessions: List<Session>): Int {
        if (sessions.isEmpty()) return 0
        val active = sessions.map { it.isoDate }.toSet()
        var n = 0
        var cursor = today
        while (cursor in active) {
            n++
            cursor = previousIso(cursor) ?: return n
        }
        return n
    }

    private fun previousIso(iso: String): String? {
        // yyyy-MM-dd parser; null on bad input.
        val parts = iso.split("-")
        if (parts.size != 3) return null
        val cal = java.util.Calendar.getInstance().apply {
            clear()
            set(
                parts[0].toIntOrNull() ?: return null,
                (parts[1].toIntOrNull() ?: return null) - 1,
                parts[2].toIntOrNull() ?: return null
            )
            add(java.util.Calendar.DAY_OF_MONTH, -1)
        }
        return java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.US).format(cal.time)
    }

    val PRESET_TECHNIQUES = listOf("Breath", "Body scan", "Loving-kindness", "Open monitoring", "Walking")
    val PRESET_MINUTES = listOf(5, 10, 15, 20, 30)
}
