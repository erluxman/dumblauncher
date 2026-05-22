package com.erluxman.focuslauncher.service.tracks

/**
 * Pure logic for graduated freedom (V1 UNINSTALL-007).
 *
 * As the user accumulates builder minutes, they "level up" and the restrictions
 * relax — Level 0 is max friction, Level 10 is near-invisible.
 */
object UserLevel {

    const val MAX_LEVEL = 10

    /** Minutes of builder work to reach the next level. Linear for simplicity. */
    const val MINUTES_PER_LEVEL = 600  // ~10 hours per level

    fun level(builderMinutes: Int): Int =
        (builderMinutes / MINUTES_PER_LEVEL).coerceIn(0, MAX_LEVEL)

    /** Each level shaves a small amount off the base Lobby countdown. */
    fun easedLobbySeconds(baseSeconds: Int, level: Int): Int =
        (baseSeconds - level).coerceAtLeast(MIN_FLOOR_S)

    const val MIN_FLOOR_S = 2
}
