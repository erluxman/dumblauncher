package com.erluxman.focuslauncher.service.sad

/**
 * ABSURD-001 Kazoo Mode — line generator.
 *
 * We can't reliably play audio over other apps (challenges.md
 * §IMPOSSIBLE-016), so the V1 surface is a sticky line a future
 * notification or overlay could carry. Lines are short and absurd by
 * design.
 */
object KazooMode {

    private val LINES = listOf(
        "🎺 Honk. You're scrolling.",
        "🎺 Honk. Still scrolling.",
        "🎺 Honk. Still scrolling. Embarrassing, isn't it?",
        "🎺 Honk-honk. Phone is winning.",
    )

    fun lineFor(secondsInApp: Int, seed: Long = System.currentTimeMillis()): String? {
        if (secondsInApp < 30) return null
        val idx = (((seed % LINES.size) + LINES.size) % LINES.size).toInt()
        return LINES[idx]
    }
}
