package com.erluxman.focuslauncher.service

/**
 * Pure formatter for the auto-generated Day Summary journal entry (PROD-004).
 */
object DaySummary {

    fun build(
        dateIso: String,
        screenMinutes: Int,
        distractionMinutes: Int,
        targetMinutes: Int,
        todosCompleted: Int,
        focusSessions: Int,
        oneThing: String
    ): String = buildString {
        append("Day summary for $dateIso\n")
        append("- Screen: ${screenMinutes}m (target ${targetMinutes}m)\n")
        append("- Distraction: ${distractionMinutes}m\n")
        append("- Todos completed: $todosCompleted\n")
        append("- Focus sessions: $focusSessions\n")
        if (oneThing.isNotBlank()) append("- One thing: $oneThing\n")
        val verdict = when {
            screenMinutes <= targetMinutes && todosCompleted > 0 -> "Clean day."
            screenMinutes <= targetMinutes -> "Under target but quiet output."
            todosCompleted > 0 -> "Over target, but you still shipped."
            else -> "Today was rough. Reset tomorrow."
        }
        append("- Verdict: $verdict")
    }
}
