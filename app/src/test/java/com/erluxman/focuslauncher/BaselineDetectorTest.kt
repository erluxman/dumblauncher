package com.erluxman.focuslauncher

import com.erluxman.focuslauncher.service.tracks.BaselineDetector
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class BaselineDetectorTest {

    @Test
    fun proposed_isNullUntilWindowComplete() {
        assertNull(BaselineDetector.proposedTarget(listOf(180, 200, 220)))
    }

    @Test
    fun proposed_isAverageMinusReduction() {
        val samples = List(BaselineDetector.WINDOW_DAYS) { 250 }
        val target = BaselineDetector.proposedTarget(samples)
        assertEquals(200, target)  // 250 * 0.8
    }

    @Test
    fun proposed_clampsAtMinFloor() {
        val samples = List(BaselineDetector.WINDOW_DAYS) { 10 }
        assertEquals(BaselineDetector.MIN_TARGET, BaselineDetector.proposedTarget(samples))
    }
}
