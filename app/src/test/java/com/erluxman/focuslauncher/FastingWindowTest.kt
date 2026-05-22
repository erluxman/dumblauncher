package com.erluxman.focuslauncher

import com.erluxman.focuslauncher.service.habits.FastingWindow
import org.junit.Assert.assertEquals
import org.junit.Test

class FastingWindowTest {

    private val hour = 3_600_000L

    @Test
    fun longest_emptyOrSingle_isZero() {
        assertEquals(0.0, FastingWindow.longestGapHours(emptyList()), 0.0)
        assertEquals(0.0, FastingWindow.longestGapHours(listOf(0L)), 0.0)
    }

    @Test
    fun longest_picksTheBiggestGap() {
        val now = 1_000_000L
        val meals = listOf(now - 24 * hour, now - 16 * hour, now - 8 * hour, now)
        assertEquals(8.0, FastingWindow.longestGapHours(meals), 1e-6)
    }

    @Test
    fun open_hoursSinceLatest() {
        val now = 100L * hour
        val meals = listOf(now - 15 * hour, now - 8 * hour)
        assertEquals(8.0, FastingWindow.openWindowHours(meals, now), 1e-6)
    }

    @Test
    fun open_emptyIsZero() {
        assertEquals(0.0, FastingWindow.openWindowHours(emptyList(), 100L), 0.0)
    }
}
