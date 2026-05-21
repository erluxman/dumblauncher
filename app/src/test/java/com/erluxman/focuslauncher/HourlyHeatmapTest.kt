package com.erluxman.focuslauncher

import com.erluxman.focuslauncher.service.HourlyHeatmap
import org.junit.Assert.assertEquals
import org.junit.Test

class HourlyHeatmapTest {

    private val day0 = 0L
    private val hour = 60L * 60 * 1000

    @Test
    fun fullSingleHour_goesInOneBucket() {
        val sessions = listOf((0L..hour))
        val out = HourlyHeatmap.bucketize(sessions, dayStartMs = day0, nowMs = day0 + 24 * hour)
        assertEquals(60, out[0])
        assertEquals(0, out[1])
    }

    @Test
    fun spansBoundary_splitsAcrossBuckets() {
        // 30 minutes in hour 0, 30 minutes in hour 1.
        val start = 30L * 60 * 1000
        val end = start + hour
        val out = HourlyHeatmap.bucketize(
            listOf(start..end),
            dayStartMs = day0,
            nowMs = day0 + 24 * hour
        )
        assertEquals(30, out[0])
        assertEquals(30, out[1])
    }

    @Test
    fun level_increasesWithMinutes() {
        assertEquals(0, HourlyHeatmap.level(0))
        assertEquals(1, HourlyHeatmap.level(5))
        assertEquals(2, HourlyHeatmap.level(15))
        assertEquals(3, HourlyHeatmap.level(30))
        assertEquals(4, HourlyHeatmap.level(60))
    }
}
