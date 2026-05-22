package com.erluxman.focuslauncher.service.fitness

import com.erluxman.focuslauncher.service.habits.MeditationLog

/**
 * FIT-002 Workout Log (manual entry).
 *
 * Entries serialized as `"iso|minutes|kind"`. Same shape as the other
 * day-keyed logs so we get consistent aggregation behaviour.
 */
object WorkoutLog {

    data class Session(val isoDate: String, val minutes: Int, val kind: String)

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

    fun consecutiveDayStreak(today: String, sessions: List<Session>): Int =
        MeditationLog.consecutiveDayStreak(today, sessions.map {
            MeditationLog.Session(it.isoDate, it.minutes, it.kind)
        })

    val PRESET_KINDS = listOf("Strength", "Run", "Walk", "Cycle", "Yoga", "Climb")
    val PRESET_MINUTES = listOf(15, 30, 45, 60, 90)
}
