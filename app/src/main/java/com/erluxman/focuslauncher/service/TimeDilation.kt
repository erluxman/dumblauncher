package com.erluxman.focuslauncher.service

/**
 * ABSURD-003 Time Dilation.
 *
 * We cannot draw overlays inside other apps reliably, so we surface the
 * effect on our own home: today's distraction minutes shown at the spec's
 * 3× multiplier, plus a "felt like" string for the headline.
 */
object TimeDilation {

    const val DEFAULT_MULTIPLIER = 3.0

    fun dilatedMinutes(realMinutes: Int, multiplier: Double = DEFAULT_MULTIPLIER): Int {
        if (realMinutes <= 0) return 0
        return (realMinutes * multiplier).toInt()
    }

    /** "1h 12m" / "23m" — never seconds, never "0h" prefix. */
    fun formatHm(minutes: Int): String {
        val safe = minutes.coerceAtLeast(0)
        val h = safe / 60
        val m = safe % 60
        return when {
            h == 0 -> "${m}m"
            m == 0 -> "${h}h"
            else -> "${h}h ${m}m"
        }
    }
}
