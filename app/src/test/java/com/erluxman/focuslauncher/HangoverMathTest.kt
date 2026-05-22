package com.erluxman.focuslauncher

import com.erluxman.focuslauncher.service.habits.HangoverMath
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class HangoverMathTest {

    @Test
    fun bacAt_singleDrink_decaysOverTime() {
        val now = 24L * 3_600_000L
        val takenMs = now - 0L  // just consumed
        val bac0 = HangoverMath.bacAt(listOf(HangoverMath.Drink(1.0, takenMs)), now)
        val bac6 = HangoverMath.bacAt(listOf(HangoverMath.Drink(1.0, now - 6L * 3_600_000L)), now)
        assertEquals(0.02, bac0, 1e-9)
        // After 6h: 0.02 - 6*0.015 = -0.07 → clamped 0
        assertEquals(0.0, bac6, 1e-9)
    }

    @Test
    fun bacAt_multipleDrinks_sumPositiveContributions() {
        val now = 24L * 3_600_000L
        val drinks = listOf(
            HangoverMath.Drink(1.0, now),
            HangoverMath.Drink(1.0, now)
        )
        assertEquals(0.04, HangoverMath.bacAt(drinks, now), 1e-9)
    }

    @Test
    fun hoursToSober_isProportionalToBac() {
        val now = 24L * 3_600_000L
        val one = HangoverMath.hoursToSober(listOf(HangoverMath.Drink(2.0, now)), now)
        val two = HangoverMath.hoursToSober(listOf(HangoverMath.Drink(4.0, now)), now)
        assertTrue(two > one)
        // 4 units → 0.08 BAC → 0.08 / 0.015 ≈ 5.33h
        assertEquals(5.33, two, 0.02)
    }

    @Test
    fun sleepDeficit_scalesWithUnits() {
        val now = 24L * 3_600_000L
        val drinks = (1..3).map { HangoverMath.Drink(1.0, now) }
        // 3 units * 12 min = 36 min
        assertEquals(36, HangoverMath.estimatedSleepDeficitMin(drinks))
    }

    @Test
    fun presets_areReasonable() {
        HangoverMath.PRESETS.forEach {
            assertTrue("${it.label} units > 0", it.units > 0.0)
            assertTrue("${it.label} units <= 3", it.units <= 3.0)
        }
    }
}
