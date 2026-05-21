package com.erluxman.focuslauncher.service

/**
 * Pure logic for the V1 Track System (TRACK-001 + TRACK-002 + GRACE-003).
 *
 * - A "track" is a 1-10 level the user moves through.
 * - One [POINTS_PER_LEVEL] perfect day earns 1 point; reaching that many points
 *   promotes one level (capped at [MAX_LEVEL]).
 * - Three consecutive misses demote one level (floored at [MIN_LEVEL]) and
 *   refund the user's points-toward-next-level.
 * - Each level shaves [LEVEL_LOBBY_RELIEF_S] seconds off the Lobby base, with
 *   a floor enforced by [easedBaseSeconds].
 */
object TrackSystem {

    const val MIN_LEVEL = 1
    const val MAX_LEVEL = 10
    const val POINTS_PER_LEVEL = 7  // 1 week of perfect days promotes one level
    const val DEMOTION_MISS_STREAK = 3
    const val LEVEL_LOBBY_RELIEF_S = 1
    const val LEVEL_LOBBY_FLOOR_S = 3

    data class Snapshot(
        val level: Int,
        val pointsToward: Int,
        val missStreak: Int,
        val recalibrated: Boolean = false
    )

    /** Apply a single day's result and return the new track snapshot. */
    fun applyDay(
        snapshot: Snapshot,
        hitTarget: Boolean
    ): Snapshot {
        if (hitTarget) {
            val newMiss = 0
            val newPoints = snapshot.pointsToward + 1
            if (newPoints >= POINTS_PER_LEVEL && snapshot.level < MAX_LEVEL) {
                return Snapshot(snapshot.level + 1, 0, newMiss, recalibrated = false)
            }
            return Snapshot(snapshot.level, newPoints, newMiss, recalibrated = false)
        }

        // Missed day: bump miss streak, possibly demote.
        val nextMiss = snapshot.missStreak + 1
        if (nextMiss >= DEMOTION_MISS_STREAK && snapshot.level > MIN_LEVEL) {
            return Snapshot(snapshot.level - 1, 0, 0, recalibrated = true)
        }
        return Snapshot(snapshot.level, snapshot.pointsToward, nextMiss, recalibrated = false)
    }

    /** Lobby seconds adjusted for the user's level (higher level = lighter friction). */
    fun easedBaseSeconds(baseSeconds: Int, level: Int): Int =
        (baseSeconds - (level.coerceIn(MIN_LEVEL, MAX_LEVEL) - 1) * LEVEL_LOBBY_RELIEF_S)
            .coerceAtLeast(LEVEL_LOBBY_FLOOR_S)
}
