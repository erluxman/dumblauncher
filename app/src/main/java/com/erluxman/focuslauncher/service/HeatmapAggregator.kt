package com.erluxman.focuslauncher.service

object HeatmapAggregator {

    /** Levels 0..4 from a completion count, capped at 5 for level 4. */
    fun level(completions: Int): Int = when {
        completions <= 0 -> 0
        completions == 1 -> 1
        completions <= 2 -> 2
        completions <= 4 -> 3
        else -> 4
    }

    /**
     * Buckets a sequence of completion timestamps (ms since epoch) into per-day counts for
     * the last `days` days (inclusive of today). Index 0 is the oldest day, last is today.
     * `dayStartLocalMs` provides the start-of-today in local ms; we work backwards from there.
     */
    fun perDayCounts(
        completionsMs: List<Long>,
        nowMs: Long,
        dayStartLocalMs: Long,
        days: Int = 7
    ): IntArray {
        require(days > 0)
        val out = IntArray(days)
        val dayMs = 24L * 60L * 60L * 1000L
        for (ts in completionsMs) {
            // Offset days from today: 0 = today, 1 = yesterday, ...
            val offset = ((dayStartLocalMs - ts + (dayMs - 1)) / dayMs).toInt()
            // ts is in [todayStart, now] → offset = 0
            // ts in [yesterdayStart, todayStart) → offset = 1
            val realOffset = when {
                ts >= dayStartLocalMs -> 0
                else -> ((dayStartLocalMs - ts + dayMs - 1) / dayMs).toInt()
            }
            if (realOffset in 0 until days) out[days - 1 - realOffset]++
        }
        return out
    }
}
