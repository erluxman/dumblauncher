package com.erluxman.focuslauncher.service.sad

/**
 * GAMIFY-001 The Applause.
 *
 * Pavlov-style positive reinforcement: when the user puts the phone down
 * (exits a distraction app) in under a minute, surface a congratulatory
 * line instead of (or alongside) the regular session receipt.
 */
object Applause {

    const val APPLAUSE_THRESHOLD_MS = 60_000L

    private val lines = listOf(
        "applause noises 👏",
        "phone down. that's a win.",
        "fast exit. respect.",
        "you blinked at the screen and walked away.",
        "60s ceiling held. nice."
    )

    fun maybeLine(elapsedMs: Long, seed: Long = System.currentTimeMillis()): String? {
        if (elapsedMs >= APPLAUSE_THRESHOLD_MS) return null
        val idx = ((seed % lines.size) + lines.size).toInt() % lines.size
        return lines[idx]
    }
}
