package com.erluxman.focuslauncher.service

import kotlin.math.pow

/**
 * LIFE-010 Compounding Curve Visualizer.
 *
 * Given a daily improvement rate (e.g. 0.001 = +0.1%/day, the "1%/year"
 * curve), project the multiple of baseline output at every step over
 * [days] days.
 */
object CompoundCurve {

    /** Most users intuit "1% per day" but that overshoots; default to a calmer +0.1%/day (≈44% per year). */
    const val DEFAULT_DAILY_RATE = 0.001

    /** Returns y-values at each integer step from 0..days (inclusive). */
    fun series(days: Int, dailyRate: Double = DEFAULT_DAILY_RATE): DoubleArray {
        val n = days.coerceAtLeast(0)
        val out = DoubleArray(n + 1)
        for (i in 0..n) out[i] = (1.0 + dailyRate).pow(i)
        return out
    }

    /** Value at exactly [day] (no array allocation). */
    fun valueAt(day: Int, dailyRate: Double = DEFAULT_DAILY_RATE): Double {
        if (day <= 0) return 1.0
        return (1.0 + dailyRate).pow(day)
    }

    /** "X×" headline that the card surfaces above the curve. */
    fun multiplierAt(day: Int, dailyRate: Double = DEFAULT_DAILY_RATE): Double =
        valueAt(day, dailyRate)

    /** Days needed to reach [targetMultiplier]. Returns Int.MAX_VALUE if dailyRate <= 0. */
    fun daysToReach(targetMultiplier: Double, dailyRate: Double = DEFAULT_DAILY_RATE): Int {
        if (dailyRate <= 0.0 || targetMultiplier <= 1.0) return 0
        val d = kotlin.math.ln(targetMultiplier) / kotlin.math.ln(1.0 + dailyRate)
        return kotlin.math.ceil(d).toInt()
    }
}
