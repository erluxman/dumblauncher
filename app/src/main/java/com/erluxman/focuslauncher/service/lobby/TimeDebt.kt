package com.erluxman.focuslauncher.service.lobby

/**
 * Pure logic for the Time Debt restriction (V1 RESTRICT-009).
 *
 * Going over yesterday's effective target adds to a running debt that *halves*
 * today's spare budget. Days under target slowly drain the debt back to zero.
 *
 * The pure surface is two functions:
 *   - [effectiveTarget] computes what today's target should be given a base + current debt.
 *   - [nextDebt] computes the new debt given the previous-day screen minutes
 *     and the effective target that applied to that day.
 */
object TimeDebt {

    /** Each minute of debt removes 2 minutes from today's effective target. */
    const val DEBT_MULTIPLIER = 2

    /** Effective target floor — never punish below this many minutes. */
    const val FLOOR_MIN = 30

    /** Cap total debt so a single bad week can't permanently lock the budget. */
    const val MAX_DEBT_MIN = 120

    fun effectiveTarget(baseTargetMin: Int, debtMin: Int): Int {
        val penalty = debtMin.coerceAtLeast(0) * DEBT_MULTIPLIER
        return (baseTargetMin - penalty).coerceAtLeast(FLOOR_MIN)
    }

    /**
     * Given the debt that was active yesterday and yesterday's actual screen
     * minutes, compute today's starting debt.
     *
     * over = max(0, yesterdayScreen - yesterdayEffectiveTarget)  → adds to debt
     * under = max(0, yesterdayEffectiveTarget - yesterdayScreen) → drains debt
     */
    fun nextDebt(
        currentDebtMin: Int,
        yesterdayScreenMin: Int,
        yesterdayEffectiveTargetMin: Int
    ): Int {
        val over = (yesterdayScreenMin - yesterdayEffectiveTargetMin).coerceAtLeast(0)
        val under = (yesterdayEffectiveTargetMin - yesterdayScreenMin).coerceAtLeast(0)
        return (currentDebtMin + over - under).coerceIn(0, MAX_DEBT_MIN)
    }
}
