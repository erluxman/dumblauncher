package com.erluxman.focuslauncher

import com.erluxman.focuslauncher.service.EarnedPixels
import org.junit.Assert.assertEquals
import org.junit.Test

class EarnedPixelsTest {

    @Test
    fun saturation_zeroAndFull() {
        assertEquals(0.0, EarnedPixels.saturation(0), 0.0)
        assertEquals(1.0, EarnedPixels.saturation(100), 0.0)
        assertEquals(1.0, EarnedPixels.saturation(200), 0.0)
    }

    @Test
    fun saturation_negativeIsZero() {
        assertEquals(0.0, EarnedPixels.saturation(-5), 0.0)
    }

    @Test
    fun saturation_targetZeroSafe() {
        assertEquals(0.0, EarnedPixels.saturation(100, target = 0), 0.0)
    }

    @Test
    fun pctEarned_clamps() {
        assertEquals(0, EarnedPixels.pctEarned(0))
        assertEquals(50, EarnedPixels.pctEarned(50))
        assertEquals(100, EarnedPixels.pctEarned(500))
    }
}
