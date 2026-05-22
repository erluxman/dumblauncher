package com.erluxman.focuslauncher.service.insights

/**
 * Pure logic for GAMIFY-005 hourly productivity heatmap.
 *
 * Buckets a set of foreground sessions ([startMs, endMs]) into 24 hour-of-day
 * buckets relative to a given day start. Sessions spanning hour boundaries
 * are split across buckets.
 */
object HourlyHeatmap {

    fun bucketize(
        sessions: List<LongRange>,
        dayStartMs: Long,
        nowMs: Long = System.currentTimeMillis()
    ): IntArray {
        val buckets = IntArray(24)
        val dayEnd = dayStartMs + 24L * 60 * 60 * 1000
        for (s in sessions) {
            val start = maxOf(s.first, dayStartMs)
            val end = minOf(s.last, minOf(dayEnd, nowMs))
            if (end <= start) continue
            var cursor = start
            while (cursor < end) {
                val hourIdx = (((cursor - dayStartMs) / (60L * 60 * 1000)).toInt())
                    .coerceIn(0, 23)
                val hourEnd = dayStartMs + (hourIdx + 1L) * 60L * 60 * 1000
                val slice = minOf(hourEnd, end) - cursor
                buckets[hourIdx] += (slice / 60_000L).toInt()
                cursor += slice
            }
        }
        return buckets
    }

    /** Levels 0-4 like the existing HeatmapAggregator for color rendering. */
    fun level(minutes: Int): Int = when {
        minutes <= 0 -> 0
        minutes < 10 -> 1
        minutes < 20 -> 2
        minutes < 40 -> 3
        else -> 4
    }
}
