package com.erluxman.focuslauncher.service

object SessionReceipt {

    /**
     * Builds the toast text for a finished distraction session.
     * Suppresses anything under SUPPRESS_BELOW_MS so quick toggles don't spam.
     */
    fun format(elapsedMs: Long, appLabel: String): String? {
        if (elapsedMs < SUPPRESS_BELOW_MS) return null
        val totalSec = elapsedMs / 1000L
        val m = totalSec / 60L
        val s = totalSec % 60L
        return if (m == 0L) "Spent ${s}s in $appLabel" else "Spent ${m}m in $appLabel"
    }

    const val SUPPRESS_BELOW_MS = 10_000L
}
