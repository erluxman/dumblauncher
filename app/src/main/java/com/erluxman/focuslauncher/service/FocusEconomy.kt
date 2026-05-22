package com.erluxman.focuslauncher.service

/**
 * GAMIFY-003 Focus Score Economy — pure exchange math.
 *
 * Points already accrue via prefs.addFocusPoints. This service is the
 * conversion table for the eventual "spend points to unlock an app"
 * flow: how many minutes does N points buy, and how many points does
 * a tier cost.
 */
object FocusEconomy {

    /** Default: 1 point = 1 minute. Tweakable per-tier later. */
    const val POINTS_PER_MINUTE = 1

    val TIERS = listOf(5, 15, 30, 60)

    fun costFor(minutes: Int): Int = (minutes.coerceAtLeast(0) * POINTS_PER_MINUTE)

    fun minutesFor(points: Int): Int = (points.coerceAtLeast(0) / POINTS_PER_MINUTE)

    fun canAfford(points: Int, minutes: Int): Boolean = points >= costFor(minutes)
}
