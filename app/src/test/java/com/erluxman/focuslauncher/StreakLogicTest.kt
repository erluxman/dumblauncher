package com.erluxman.focuslauncher

import com.erluxman.focuslauncher.service.StreakLogic
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class StreakLogicTest {

    @Test
    fun sameDay_isNoop() {
        val r = StreakLogic.update("2026-05-21", "2026-05-21", 5, 7, "2026-05-20", true)
        assertEquals(5, r.days)
        assertEquals(7, r.best)
        assertFalse(r.persist)
    }

    @Test
    fun yesterdayHit_extendsStreak() {
        val r = StreakLogic.update("2026-05-21", "2026-05-20", 5, 7, "2026-05-20", true)
        assertEquals(6, r.days)
        assertEquals(7, r.best)
        assertTrue(r.persist)
    }

    @Test
    fun yesterdayMissed_breaksStreak() {
        val r = StreakLogic.update("2026-05-21", "2026-05-20", 5, 7, "2026-05-20", false)
        assertEquals(0, r.days)
        assertEquals(7, r.best)
        assertTrue(r.persist)
    }

    @Test
    fun gap_breaksStreak() {
        val r = StreakLogic.update("2026-05-21", "2026-05-15", 5, 7, "2026-05-20", true)
        assertEquals(0, r.days)
        assertEquals(7, r.best)
    }

    @Test
    fun newStreakAboveOldBest_updatesBest() {
        val r = StreakLogic.update("2026-05-21", "2026-05-20", 7, 7, "2026-05-20", true)
        assertEquals(8, r.days)
        assertEquals(8, r.best)
    }

    @Test
    fun firstEverCheck_seedsBasedOnYesterday() {
        val r = StreakLogic.update("2026-05-21", "", 0, 0, "2026-05-20", true)
        assertEquals(1, r.days)
        assertEquals(1, r.best)
        assertTrue(r.persist)
    }
}
