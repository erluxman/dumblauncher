package com.erluxman.focuslauncher.service.insights

object LegacyCounter {

    /**
     * Returns the cumulative "builder minutes" for the user. Each completed todo counts for
     * TODO_MIN minutes, each focus session for FOCUS_MIN. Pure so we can test it.
     */
    fun totalBuilderMinutes(completedTodos: Int, focusSessions: Int): Int =
        completedTodos * TODO_MIN + focusSessions * FOCUS_MIN

    fun format(minutes: Int): String = when {
        minutes < 60 -> "${minutes}m"
        minutes < 24 * 60 -> "${minutes / 60}h ${minutes % 60}m"
        else -> {
            val days = minutes / (24 * 60)
            val remainHours = (minutes % (24 * 60)) / 60
            "${days}d ${remainHours}h"
        }
    }

    const val TODO_MIN = 15
    const val FOCUS_MIN = 25
}
