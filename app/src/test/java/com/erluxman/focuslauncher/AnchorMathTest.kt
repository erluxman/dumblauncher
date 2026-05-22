package com.erluxman.focuslauncher

import com.erluxman.focuslauncher.service.insights.AnchorMath
import org.junit.Assert.assertEquals
import org.junit.Test

class AnchorMathTest {

    @Test
    fun delta_aboveAnchor() {
        assertEquals(48, AnchorMath.delta(60))
    }

    @Test
    fun delta_atAnchor() {
        assertEquals(0, AnchorMath.delta(AnchorMath.ANCHOR_MINUTES))
    }

    @Test
    fun delta_belowAnchor() {
        assertEquals(-2, AnchorMath.delta(10))
    }
}
