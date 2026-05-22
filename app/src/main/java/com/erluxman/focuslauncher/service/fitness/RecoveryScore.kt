package com.erluxman.focuslauncher.service.fitness

/**
 * FIT-005 Recovery Score (Synthesized).
 *
 * Composite 0..100 from last-night sleep minutes + yesterday's steps,
 * with an optional self-reported "feel" multiplier. Designed to be
 * understandable from the inputs alone — three subscores, equally
 * weighted, clamped, no surprise factors.
 */
object RecoveryScore {

    const val TARGET_SLEEP_MIN = 8 * 60
    const val TARGET_STEPS = 8_000
    const val DEFAULT_FEEL = 7  // 1..10 scale

    data class Score(
        val total: Int,
        val sleepSub: Int,
        val activitySub: Int,
        val feelSub: Int,
    ) {
        val label: String get() = when {
            total >= 80 -> "READY"
            total >= 55 -> "STEADY"
            total >= 30 -> "DRAINED"
            else -> "DEPLETED"
        }
    }

    fun compute(
        sleepMinutes: Int,
        steps: Int,
        feel1to10: Int = DEFAULT_FEEL,
    ): Score {
        val s = (sleepMinutes.coerceAtLeast(0).toDouble() / TARGET_SLEEP_MIN * 100).coerceAtMost(100.0)
        // Activity: too few = under-recovered, too many = drained; both penalised.
        val a = activitySubscore(steps)
        val f = (feel1to10.coerceIn(1, 10) - 1) * (100.0 / 9.0)
        val total = ((s + a + f) / 3.0).toInt()
        return Score(
            total = total.coerceIn(0, 100),
            sleepSub = s.toInt(),
            activitySub = a.toInt(),
            feelSub = f.toInt(),
        )
    }

    private fun activitySubscore(steps: Int): Double {
        if (steps <= 0) return 0.0
        val ratio = steps.toDouble() / TARGET_STEPS
        return when {
            ratio <= 1.0 -> ratio * 100.0
            ratio <= 2.0 -> (100.0 - (ratio - 1.0) * 25.0)  // mild penalty for overshoot
            else -> 50.0
        }.coerceIn(0.0, 100.0)
    }
}
