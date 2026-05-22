package com.erluxman.focuslauncher

import com.erluxman.focuslauncher.service.insights.CognitiveVitals
import org.junit.Assert.assertEquals
import org.junit.Test

class CognitiveVitalsTest {

    @Test
    fun parse_dropsMalformedAndOutOfRange() {
        val e = CognitiveVitals.parse(setOf(
            "2026-05-22|nback|78",
            "bad",
            "2026-05-22|nback|999",
            "2026-05-21|nback|abc",
        ))
        assertEquals(1, e.size)
        assertEquals(78, e.first().score)
    }

    @Test
    fun rolling_averageOverWindow() {
        val e = CognitiveVitals.parse(setOf(
            "2026-05-22|nback|80",
            "2026-05-21|nback|60",
            "2026-05-20|nback|70",
            "2026-05-15|nback|10",
        ))
        val window = listOf("2026-05-22", "2026-05-21", "2026-05-20")
        assertEquals(70, CognitiveVitals.rollingAverage(window, "nback", e))
    }

    @Test
    fun best_picksHighest() {
        val e = CognitiveVitals.parse(setOf(
            "2026-05-22|nback|80",
            "2026-05-21|nback|90",
            "2026-05-20|stroop|99",
        ))
        assertEquals(90, CognitiveVitals.best("nback", e))
        assertEquals(99, CognitiveVitals.best("stroop", e))
        assertEquals(0, CognitiveVitals.best("missing", e))
    }

    @Test
    fun trend_isDeltaBetweenAverages() {
        val e = CognitiveVitals.parse(setOf(
            "2026-05-22|nback|80",
            "2026-05-21|nback|80",
            "2026-05-15|nback|60",
            "2026-05-14|nback|60",
        ))
        val newer = listOf("2026-05-22", "2026-05-21")
        val older = listOf("2026-05-15", "2026-05-14")
        assertEquals(20, CognitiveVitals.trend(newer, older, "nback", e))
    }
}
