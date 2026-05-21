package com.erluxman.focuslauncher.service

/**
 * TRACK-004 Smart Baseline.
 *
 * Observe the user's screen-time minutes during the first [WINDOW_DAYS] days
 * after install. On completion, propose a target that is [REDUCTION] below
 * the average — i.e., a meaningful but achievable cut.
 */
object BaselineDetector {

    const val WINDOW_DAYS = 7
    const val REDUCTION = 0.20  // 20% off baseline

    /** True once we have a complete observation window. */
    fun isComplete(samples: List<Int>): Boolean = samples.size >= WINDOW_DAYS

    /** Returns the proposed daily target, or null if the window isn't complete yet. */
    fun proposedTarget(samples: List<Int>): Int? {
        if (!isComplete(samples)) return null
        val window = samples.takeLast(WINDOW_DAYS)
        val average = window.average()
        val target = (average * (1.0 - REDUCTION)).toInt()
        return target.coerceAtLeast(MIN_TARGET)
    }

    const val MIN_TARGET = 30
}
