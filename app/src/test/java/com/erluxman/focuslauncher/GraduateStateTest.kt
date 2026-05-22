package com.erluxman.focuslauncher

import com.erluxman.focuslauncher.service.tracks.GraduateState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class GraduateStateTest {

    private val day = 86_400_000L

    @Test
    fun graduate_requiresBothLevelAndDays() {
        val now = 1000 * day
        // Just level, not days.
        val s1 = GraduateState.compute(10, onboardingMs = now - 5 * day, nowMs = now)
        assertFalse(s1.isGraduate)
        // Just days, not level.
        val s2 = GraduateState.compute(5, onboardingMs = now - 600 * day, nowMs = now)
        assertFalse(s2.isGraduate)
        // Both.
        val s3 = GraduateState.compute(10, onboardingMs = now - 600 * day, nowMs = now)
        assertTrue(s3.isGraduate)
    }

    @Test
    fun daysOnboarded_isFloor() {
        val now = 1000 * day
        val s = GraduateState.compute(1, onboardingMs = now - 5 * day - day / 2, nowMs = now)
        assertEquals(5, s.daysOnboarded)
    }

    @Test
    fun daysRemaining_zeroPastThreshold() {
        val now = 2000 * day
        // 600 days ago, beyond the 540-day threshold.
        val s = GraduateState.compute(10, onboardingMs = now - 600 * day, nowMs = now)
        assertEquals(0, s.daysRemaining)
    }

    @Test
    fun zeroOnboarding_isSafe() {
        val s = GraduateState.compute(10, onboardingMs = 0L, nowMs = 0L)
        assertEquals(0, s.daysOnboarded)
        assertFalse(s.isGraduate)
    }
}
