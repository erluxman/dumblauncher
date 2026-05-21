package com.erluxman.focuslauncher.service

/**
 * Pure logic for Grace Day (GRACE-001) and Streak Freeze (GRACE-002).
 *
 * Grace days: declared in advance for a future date — count up to 2 per month.
 * On a declared grace day, missing the target does NOT break the streak.
 *
 * Streak freezes: earned automatically every 30 perfect days, max 3.
 * Auto-applied when the user would otherwise break their streak.
 */
object GraceLogic {

    const val MAX_GRACE_DAYS_PER_MONTH = 2
    const val FREEZE_EARN_INTERVAL = 30
    const val MAX_FREEZES = 3

    /** True if the given date (yyyy-MM-dd) is in the user's declared grace-day set. */
    fun isGraceDay(yesterdayIso: String, graceDays: Set<String>): Boolean =
        yesterdayIso in graceDays

    /** Count grace days used in the given month (yyyy-MM). */
    fun graceUsedInMonth(monthIso: String, graceDays: Set<String>): Int =
        graceDays.count { it.startsWith("$monthIso-") }

    /** Whether user may add another grace day in the given month. */
    fun canAddGrace(monthIso: String, graceDays: Set<String>): Boolean =
        graceUsedInMonth(monthIso, graceDays) < MAX_GRACE_DAYS_PER_MONTH

    /** How many freezes the user has earned for a given perfect-streak length. */
    fun earnedFreezes(currentDays: Int): Int =
        ((currentDays / FREEZE_EARN_INTERVAL)).coerceAtMost(MAX_FREEZES)

    /**
     * Decide what happens when a streak-break would occur.
     *
     * Returns:
     *   - GraceOutcome.GracedByDay if yesterday was a grace day (streak preserved, no freeze consumed)
     *   - GraceOutcome.GracedByFreeze if a freeze is available (streak preserved, one freeze consumed)
     *   - GraceOutcome.Broken otherwise (streak resets)
     */
    fun resolveBreak(
        yesterdayIso: String,
        graceDays: Set<String>,
        freezesAvailable: Int
    ): GraceOutcome = when {
        isGraceDay(yesterdayIso, graceDays) -> GraceOutcome.GracedByDay
        freezesAvailable > 0 -> GraceOutcome.GracedByFreeze
        else -> GraceOutcome.Broken
    }
}

enum class GraceOutcome { GracedByDay, GracedByFreeze, Broken }
