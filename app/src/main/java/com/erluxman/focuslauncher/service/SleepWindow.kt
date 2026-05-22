package com.erluxman.focuslauncher.service

/**
 * SLEEP-003 Sleep Window Guardrails.
 *
 * Pure check: given a cutoff and wake hour-of-day, return whether [hour]
 * falls inside the guardrail window. Window wraps midnight when
 * cutoff > wake (the common case).
 */
object SleepWindow {

    const val DEFAULT_CUTOFF_HOUR = 22
    const val DEFAULT_WAKE_HOUR = 5

    fun isInWindow(hour: Int, cutoffHour: Int = DEFAULT_CUTOFF_HOUR, wakeHour: Int = DEFAULT_WAKE_HOUR): Boolean {
        val c = cutoffHour.mod(24)
        val w = wakeHour.mod(24)
        val h = hour.mod(24)
        // No window when cutoff == wake.
        if (c == w) return false
        return if (c < w) {
            h in c until w
        } else {
            h >= c || h < w
        }
    }

    fun minutesUntilWake(hour: Int, minute: Int, wakeHour: Int = DEFAULT_WAKE_HOUR): Int {
        val now = hour.mod(24) * 60 + minute.mod(60)
        val wake = wakeHour.mod(24) * 60
        val diff = if (wake > now) wake - now else (24 * 60 - now) + wake
        return diff
    }

    /** Hours valid for cutoff/wake sliders, kept in 24h ints. */
    val VALID_HOURS: IntRange = 0..23
}
