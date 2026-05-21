package com.erluxman.focuslauncher

import com.erluxman.focuslauncher.service.DaySummary
import org.junit.Assert.assertTrue
import org.junit.Test

class DaySummaryTest {

    @Test
    fun cleanDay_returnsCleanVerdict() {
        val s = DaySummary.build(
            dateIso = "2026-05-21",
            screenMinutes = 90,
            distractionMinutes = 20,
            targetMinutes = 180,
            todosCompleted = 3,
            focusSessions = 2,
            oneThing = "ship the export feature"
        )
        assertTrue(s.contains("Clean day."))
        assertTrue(s.contains("ship the export feature"))
    }

    @Test
    fun roughDay_returnsRoughVerdict() {
        val s = DaySummary.build(
            dateIso = "2026-05-22",
            screenMinutes = 400,
            distractionMinutes = 200,
            targetMinutes = 180,
            todosCompleted = 0,
            focusSessions = 0,
            oneThing = ""
        )
        assertTrue(s.contains("rough"))
    }
}
