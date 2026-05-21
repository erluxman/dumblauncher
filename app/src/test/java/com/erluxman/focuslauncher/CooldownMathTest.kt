package com.erluxman.focuslauncher

import com.erluxman.focuslauncher.ui.uninstall.CooldownMath
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class CooldownMathTest {

    private val hour = 60L * 60L * 1000L

    @Test
    fun freshStart_remainingEqualsFullCooldown() {
        val start = 1_000_000L
        val now = start
        assertEquals(CooldownMath.COOLDOWN_MS, CooldownMath.remainingMs(start, now))
        assertFalse(CooldownMath.isElapsed(start, now))
    }

    @Test
    fun halfwayThrough_remainingIsHalf() {
        val start = 0L
        val now = 36 * hour
        assertEquals(36 * hour, CooldownMath.remainingMs(start, now))
    }

    @Test
    fun exactlyAt72hr_isElapsed() {
        val start = 0L
        val now = CooldownMath.COOLDOWN_MS
        assertEquals(0L, CooldownMath.remainingMs(start, now))
        assertTrue(CooldownMath.isElapsed(start, now))
    }

    @Test
    fun past72hr_isElapsed() {
        val start = 0L
        val now = CooldownMath.COOLDOWN_MS + 5 * hour
        assertEquals(0L, CooldownMath.remainingMs(start, now))
        assertTrue(CooldownMath.isElapsed(start, now))
    }

    @Test
    fun format_showsHoursMinutesSeconds() {
        val ms = 25L * 3600L * 1000L + 17 * 60 * 1000L + 4 * 1000L
        assertEquals("25h 17m 4s", CooldownMath.format(ms))
    }
}
