package com.erluxman.focuslauncher.service.insights

/**
 * Pure logic for PSYCH-013 Anchoring Attack: surface a deliberately
 * unflattering comparison ("the most disciplined user did X minutes").
 *
 * No actual leaderboard — the number is hard-coded for now.
 */
object AnchorMath {

    /** The aspirational anchor: 12 minutes of distractions per day. */
    const val ANCHOR_MINUTES = 12

    fun delta(userMinutes: Int): Int = userMinutes - ANCHOR_MINUTES
}
