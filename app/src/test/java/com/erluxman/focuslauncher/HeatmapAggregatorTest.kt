package com.erluxman.focuslauncher

import com.erluxman.focuslauncher.service.HeatmapAggregator
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Test

class HeatmapAggregatorTest {

    private val dayMs = 24L * 60 * 60 * 1000

    @Test
    fun levelBuckets() {
        assertEquals(0, HeatmapAggregator.level(0))
        assertEquals(1, HeatmapAggregator.level(1))
        assertEquals(2, HeatmapAggregator.level(2))
        assertEquals(3, HeatmapAggregator.level(3))
        assertEquals(3, HeatmapAggregator.level(4))
        assertEquals(4, HeatmapAggregator.level(5))
        assertEquals(4, HeatmapAggregator.level(50))
    }

    @Test
    fun emptyTimestamps_yieldsAllZeros() {
        val r = HeatmapAggregator.perDayCounts(emptyList(), nowMs = 0L, dayStartLocalMs = 0L, days = 7)
        assertArrayEquals(IntArray(7), r)
    }

    @Test
    fun todayCompletions_landInLastBucket() {
        val today = 100L * dayMs
        val r = HeatmapAggregator.perDayCounts(
            completionsMs = listOf(today + 10, today + 20),
            nowMs = today + 100,
            dayStartLocalMs = today,
            days = 7
        )
        assertEquals(2, r[6])
        for (i in 0 until 6) assertEquals(0, r[i])
    }

    @Test
    fun yesterdayCompletion_landsInSecondToLastBucket() {
        val today = 100L * dayMs
        val yest = today - 10  // just before today's start
        val r = HeatmapAggregator.perDayCounts(
            completionsMs = listOf(yest),
            nowMs = today + 100,
            dayStartLocalMs = today,
            days = 7
        )
        assertEquals(1, r[5])
    }

    @Test
    fun olderThanWindow_isIgnored() {
        val today = 100L * dayMs
        val tooOld = today - 8 * dayMs
        val r = HeatmapAggregator.perDayCounts(
            completionsMs = listOf(tooOld),
            nowMs = today + 100,
            dayStartLocalMs = today,
            days = 7
        )
        assertArrayEquals(IntArray(7), r)
    }
}
