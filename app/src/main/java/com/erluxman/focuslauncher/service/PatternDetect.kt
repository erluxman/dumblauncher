package com.erluxman.focuslauncher.service

/**
 * PSYCH-012 Pattern Detection (today-scoped V1).
 *
 * Cheap, transparent heuristics over the hourly-distraction array. The
 * deeper week-wide cross-correlation engine in the spec is parked until
 * we persist historical hour-level data.
 */
object PatternDetect {

    data class WeakestHour(val hour: Int, val minutes: Int)

    /** Returns the hour-of-day (0..23) with the most distraction so far today. */
    fun weakestHourToday(hourlyMinutes: IntArray, nowHour: Int = -1): WeakestHour? {
        if (hourlyMinutes.isEmpty()) return null
        val cap = if (nowHour in 0..23) nowHour + 1 else hourlyMinutes.size
        var bestHour = -1
        var bestMin = -1
        for (h in 0 until cap.coerceAtMost(hourlyMinutes.size)) {
            val m = hourlyMinutes[h]
            if (m > bestMin) { bestMin = m; bestHour = h }
        }
        if (bestHour < 0 || bestMin <= 0) return null
        return WeakestHour(hour = bestHour, minutes = bestMin)
    }

    /** "9 PM" / "12 AM" / "3 AM" — caller surfaces this in the headline. */
    fun formatHour(hour: Int): String {
        val h = hour.mod(24)
        val period = if (h < 12) "AM" else "PM"
        val display = when {
            h == 0 -> 12
            h <= 12 -> if (h == 12) 12 else h
            else -> h - 12
        }
        return "$display $period"
    }
}
