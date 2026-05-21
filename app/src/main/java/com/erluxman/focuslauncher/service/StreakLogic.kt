package com.erluxman.focuslauncher.service

object StreakLogic {

    /**
     * Pure decision: given today, last check date, current streak, current best, and whether
     * yesterday hit the target, return the new (days, best) and whether to persist.
     *
     * Rules:
     * - If lastCheck == today: no-op (return current state unchanged, persist=false).
     * - If lastCheck == yesterday: extend or break based on yesterdayHit.
     * - Otherwise: missed days break the chain; reset to 0.
     */
    fun update(
        todayIso: String,
        lastCheckIso: String,
        currentDays: Int,
        currentBest: Int,
        yesterdayIso: String,
        yesterdayHitTarget: Boolean
    ): StreakUpdate {
        if (lastCheckIso == todayIso) return StreakUpdate(currentDays, currentBest, persist = false)

        val newDays = when {
            lastCheckIso == yesterdayIso && yesterdayHitTarget -> currentDays + 1
            lastCheckIso == yesterdayIso && !yesterdayHitTarget -> 0
            lastCheckIso.isEmpty() && yesterdayHitTarget -> 1
            lastCheckIso.isEmpty() && !yesterdayHitTarget -> 0
            else -> 0  // gap: chain broken
        }
        val newBest = maxOf(currentBest, newDays)
        return StreakUpdate(newDays, newBest, persist = true)
    }
}

data class StreakUpdate(val days: Int, val best: Int, val persist: Boolean)
