package com.erluxman.focuslauncher

import com.erluxman.focuslauncher.service.Anchoring
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AnchoringTest {

    @Test
    fun anchor_neverBelowFloor() {
        assertEquals(Anchoring.ANCHOR_MIN_FLOOR, Anchoring.anchorMinutes(0))
        assertEquals(Anchoring.ANCHOR_MIN_FLOOR, Anchoring.anchorMinutes(48))
    }

    @Test
    fun anchor_neverAboveSixty() {
        assertEquals(60, Anchoring.anchorMinutes(10_000))
    }

    @Test
    fun anchor_quartersUserWhenAboveFloor() {
        assertEquals(50, Anchoring.anchorMinutes(200))
    }

    @Test
    fun multiplier_zeroAnchorIsSafe() {
        assertEquals(0.0, Anchoring.multiplier(100, 0), 0.0)
    }

    @Test
    fun multiplier_greaterThanOneWhenUserHigher() {
        assertTrue(Anchoring.multiplier(60, 15) > 1.0)
    }
}
