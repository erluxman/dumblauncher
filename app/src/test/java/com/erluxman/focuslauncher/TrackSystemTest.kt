package com.erluxman.focuslauncher

import com.erluxman.focuslauncher.service.TrackSystem
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class TrackSystemTest {

    @Test
    fun perfectDay_bumpsPointsTowardNext() {
        val s = TrackSystem.Snapshot(level = 1, pointsToward = 0, missStreak = 0)
        val next = TrackSystem.applyDay(s, hitTarget = true)
        assertEquals(1, next.pointsToward)
        assertEquals(1, next.level)
    }

    @Test
    fun sevenPerfectDays_promoteToNextLevel() {
        var s = TrackSystem.Snapshot(level = 1, pointsToward = 0, missStreak = 0)
        repeat(TrackSystem.POINTS_PER_LEVEL) {
            s = TrackSystem.applyDay(s, hitTarget = true)
        }
        assertEquals(2, s.level)
        assertEquals(0, s.pointsToward)
        assertFalse(s.recalibrated)
    }

    @Test
    fun threeConsecutiveMisses_demoteWithRecalibrationFlag() {
        var s = TrackSystem.Snapshot(level = 3, pointsToward = 4, missStreak = 0)
        repeat(TrackSystem.DEMOTION_MISS_STREAK) {
            s = TrackSystem.applyDay(s, hitTarget = false)
        }
        assertEquals(2, s.level)
        assertEquals(0, s.pointsToward)
        assertTrue(s.recalibrated)
    }

    @Test
    fun missAtLevelOne_doesNotGoBelowFloor() {
        var s = TrackSystem.Snapshot(level = 1, pointsToward = 0, missStreak = 0)
        repeat(10) { s = TrackSystem.applyDay(s, hitTarget = false) }
        assertEquals(1, s.level)
    }

    @Test
    fun perfectAtMaxLevel_doesNotOverflow() {
        val s = TrackSystem.Snapshot(level = TrackSystem.MAX_LEVEL, pointsToward = 6, missStreak = 0)
        val next = TrackSystem.applyDay(s, hitTarget = true)
        assertEquals(TrackSystem.MAX_LEVEL, next.level)
    }

    @Test
    fun easedBase_clampsAtFloor() {
        assertEquals(TrackSystem.LEVEL_LOBBY_FLOOR_S, TrackSystem.easedBaseSeconds(5, 10))
    }
}
