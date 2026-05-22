package com.erluxman.focuslauncher.service

import kotlin.math.abs

/**
 * PROD-010 Effort Estimation Training.
 *
 * Aggregates `(estimatedMinutes, actualMinutes)` pairs from completed
 * todos and produces:
 *   - accuracy: average ratio of est/actual, capped at 1.0 (1 = perfect)
 *   - bias: positive = over-estimate, negative = under-estimate, as a %
 *   - median ratio: est/actual at the middle (defends against outliers)
 *
 * Pure-fn. Caller filters which todos to include.
 */
object EstimationAccuracy {

    data class Stats(
        val sample: Int,
        val accuracyPct: Int,
        val biasPct: Int,
        val medianRatio: Double,
    )

    fun compute(pairs: List<Pair<Int, Int>>): Stats {
        val valid = pairs.filter { (e, a) -> e > 0 && a > 0 }
        if (valid.isEmpty()) return Stats(0, 0, 0, 0.0)
        val ratios = valid.map { (e, a) -> e.toDouble() / a }
        val accuracy = ratios.map { 1.0 - abs(1.0 - it).coerceAtMost(1.0) }.average()
        val bias = ratios.average() - 1.0
        val median = ratios.sorted().let { sorted ->
            val mid = sorted.size / 2
            if (sorted.size % 2 == 1) sorted[mid] else (sorted[mid - 1] + sorted[mid]) / 2.0
        }
        return Stats(
            sample = valid.size,
            accuracyPct = (accuracy * 100).toInt(),
            biasPct = (bias * 100).toInt(),
            medianRatio = median,
        )
    }
}
