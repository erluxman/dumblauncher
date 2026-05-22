package com.erluxman.focuslauncher.service

/**
 * MIND-002 Stress Weather Report (V1 lite).
 *
 * No HRV/voice/typing yet — just two cheap, available signals:
 *  - unlocks today (higher = more anxious checking)
 *  - sleep deficit vs target (lower sleep = higher stress proxy)
 *
 * Each maps to a 0..100 subscore; the headline is the max of the two
 * (worst signal wins) so a single bad column flips the weather.
 */
object StressIndex {

    const val UNLOCKS_TARGET = 60     // 60 unlocks/day ≈ "normal"
    const val SLEEP_TARGET_MIN = 8 * 60

    data class Report(val total: Int, val unlocksSub: Int, val sleepSub: Int) {
        val label: String get() = when {
            total >= 70 -> "STORMY"
            total >= 45 -> "OVERCAST"
            total >= 20 -> "PARTLY CLOUDY"
            else -> "CLEAR"
        }
    }

    fun compute(unlocksToday: Int, sleepMinutesLastNight: Int): Report {
        val u = unlockSubscore(unlocksToday)
        val s = sleepSubscore(sleepMinutesLastNight)
        val total = maxOf(u, s)
        return Report(total = total, unlocksSub = u, sleepSub = s)
    }

    private fun unlockSubscore(unlocks: Int): Int {
        // Linear from 0 unlocks → 0 to 4× target → 100.
        if (unlocks <= 0) return 0
        val raw = unlocks.toDouble() / (UNLOCKS_TARGET * 4) * 100.0
        return raw.toInt().coerceIn(0, 100)
    }

    private fun sleepSubscore(sleepMin: Int): Int {
        // Linear from on-target sleep → 0 to half-target → 100.
        if (sleepMin <= 0) return 100
        if (sleepMin >= SLEEP_TARGET_MIN) return 0
        val deficit = (SLEEP_TARGET_MIN - sleepMin).toDouble()
        return (deficit / (SLEEP_TARGET_MIN / 2.0) * 100.0).toInt().coerceIn(0, 100)
    }
}
