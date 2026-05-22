package com.erluxman.focuslauncher.service

/**
 * NUT-002 Fasting Window — pure-fn.
 *
 * Caller maintains a list of meal timestamps (epoch ms). We compute
 * the longest gap between consecutive meals plus the open gap from the
 * last meal to now.
 */
object FastingWindow {

    fun longestGapHours(timestampsMs: List<Long>): Double {
        if (timestampsMs.size < 2) return 0.0
        val sorted = timestampsMs.sorted()
        var longest = 0L
        for (i in 1 until sorted.size) {
            val gap = sorted[i] - sorted[i - 1]
            if (gap > longest) longest = gap
        }
        return longest / 3_600_000.0
    }

    /** Hours since the most recent meal, given `nowMs`. */
    fun openWindowHours(timestampsMs: List<Long>, nowMs: Long = System.currentTimeMillis()): Double {
        if (timestampsMs.isEmpty()) return 0.0
        val latest = timestampsMs.max()
        val ms = (nowMs - latest).coerceAtLeast(0L)
        return ms / 3_600_000.0
    }
}
