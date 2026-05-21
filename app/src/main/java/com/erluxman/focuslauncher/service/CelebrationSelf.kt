package com.erluxman.focuslauncher.service

/**
 * SAD-003: opposite-of-sad-self. Picks a celebratory line on milestones.
 */
object CelebrationSelf {

    private val templates = listOf(
        "Look at you. {milestone}. Quietly remarkable.",
        "{milestone}. The version of you that started this is grinning.",
        "{milestone} — that's not luck, that's pattern.",
        "Receipt: {milestone}. Frame it.",
        "Past-you wanted this. Today-you delivered: {milestone}."
    )

    /** Returns the celebratory milestone text, or null if no milestone applies. */
    fun milestone(streakDays: Int, levelUp: Boolean): String? = when {
        levelUp -> "level up"
        streakDays == 7 -> "7 days clean"
        streakDays == 30 -> "30-day streak"
        streakDays == 100 -> "100-day streak"
        streakDays > 0 && streakDays % 365 == 0 -> "$streakDays-day streak"
        else -> null
    }

    fun pick(milestone: String, seed: Int): String {
        val tpl = templates[((seed % templates.size) + templates.size) % templates.size]
        return tpl.replace("{milestone}", milestone)
    }
}
