package com.erluxman.focuslauncher

import com.erluxman.focuslauncher.service.tracks.GraceLogic
import com.erluxman.focuslauncher.service.tracks.GraceOutcome
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class GraceLogicTest {

    @Test
    fun graceDay_recognized() {
        assertTrue(GraceLogic.isGraceDay("2026-05-20", setOf("2026-05-20", "2026-05-25")))
        assertFalse(GraceLogic.isGraceDay("2026-05-19", setOf("2026-05-20")))
    }

    @Test
    fun graceUsed_perMonth() {
        val days = setOf("2026-05-10", "2026-05-22", "2026-04-15")
        assertEquals(2, GraceLogic.graceUsedInMonth("2026-05", days))
        assertEquals(1, GraceLogic.graceUsedInMonth("2026-04", days))
    }

    @Test
    fun canAddGrace_respectsMonthlyLimit() {
        val days = setOf("2026-05-10", "2026-05-22")
        assertFalse(GraceLogic.canAddGrace("2026-05", days))
        assertTrue(GraceLogic.canAddGrace("2026-06", days))
    }

    @Test
    fun earnedFreezes_growsEvery30Days() {
        assertEquals(0, GraceLogic.earnedFreezes(29))
        assertEquals(1, GraceLogic.earnedFreezes(30))
        assertEquals(3, GraceLogic.earnedFreezes(120))
        assertEquals(GraceLogic.MAX_FREEZES, GraceLogic.earnedFreezes(999))
    }

    @Test
    fun resolveBreak_graceDayWins() {
        assertEquals(
            GraceOutcome.GracedByDay,
            GraceLogic.resolveBreak("2026-05-20", setOf("2026-05-20"), 3)
        )
    }

    @Test
    fun resolveBreak_freezeFallback() {
        assertEquals(
            GraceOutcome.GracedByFreeze,
            GraceLogic.resolveBreak("2026-05-20", emptySet(), 1)
        )
    }

    @Test
    fun resolveBreak_otherwiseBroken() {
        assertEquals(
            GraceOutcome.Broken,
            GraceLogic.resolveBreak("2026-05-20", emptySet(), 0)
        )
    }
}
