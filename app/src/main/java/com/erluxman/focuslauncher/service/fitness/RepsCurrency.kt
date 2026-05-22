package com.erluxman.focuslauncher.service.fitness

/**
 * FIT-006 Reps-as-Currency.
 *
 * Pure conversion: workout minutes → focus points credited. Default is
 * 1 focus point per 5 minutes of workout (matches the existing economy
 * scale where 1 point ≈ 1 minute of unlock time). Caller is responsible
 * for actually applying the credit (prefs.addFocusPoints).
 */
object RepsCurrency {

    const val MIN_PER_POINT = 5
    const val DAILY_CAP_POINTS = 60   // 5 hours of credit per day max

    fun pointsForMinutes(workoutMinutes: Int): Int {
        if (workoutMinutes <= 0) return 0
        return (workoutMinutes / MIN_PER_POINT).coerceAtMost(DAILY_CAP_POINTS)
    }
}
