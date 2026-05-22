package com.erluxman.focuslauncher

import com.erluxman.focuslauncher.service.EstimationAccuracy
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class EstimationAccuracyTest {

    @Test
    fun compute_emptyReturnsZero() {
        val s = EstimationAccuracy.compute(emptyList())
        assertEquals(0, s.sample)
        assertEquals(0, s.accuracyPct)
        assertEquals(0, s.biasPct)
    }

    @Test
    fun compute_perfectEstimatesAreHundredPct() {
        val s = EstimationAccuracy.compute(listOf(30 to 30, 60 to 60, 15 to 15))
        assertEquals(3, s.sample)
        assertEquals(100, s.accuracyPct)
        assertEquals(0, s.biasPct)
        assertEquals(1.0, s.medianRatio, 1e-9)
    }

    @Test
    fun compute_underestimatesShowAsNegativeBias() {
        // Estimated less than actual → ratio < 1 → bias negative.
        val s = EstimationAccuracy.compute(listOf(10 to 30, 20 to 60, 15 to 45))
        assertTrue("bias should be negative: ${s.biasPct}", s.biasPct < 0)
    }

    @Test
    fun compute_overestimatesShowAsPositiveBias() {
        val s = EstimationAccuracy.compute(listOf(60 to 30, 90 to 45, 120 to 60))
        assertTrue("bias should be positive: ${s.biasPct}", s.biasPct > 0)
    }

    @Test
    fun compute_dropsInvalidEntries() {
        val s = EstimationAccuracy.compute(listOf(0 to 30, 30 to 0, -10 to -10, 30 to 30))
        assertEquals(1, s.sample)
        assertEquals(100, s.accuracyPct)
    }
}
