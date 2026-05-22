package com.erluxman.focuslauncher

import com.erluxman.focuslauncher.service.mortality.BuiltWrapped
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class BuiltWrappedTest {

    @Test
    fun headline_emptyYear_quiet() {
        val s = BuiltWrapped.headline(BuiltWrapped.Year(0, 0, 0, 0, 0))
        assertEquals("A quiet year.", s)
    }

    @Test
    fun headline_includesNonZeroPieces() {
        val year = BuiltWrapped.Year(
            totalFocusMinutes = 600,
            totalDistractionMinutes = 1200,
            projectsShipped = 3,
            streakBest = 45,
            bookCount = 12,
        )
        val s = BuiltWrapped.headline(year)
        assertTrue(s, s.contains("10 focused hours"))
        assertTrue(s, s.contains("45-day streak"))
        assertTrue(s, s.contains("3 projects"))
        assertTrue(s, s.contains("12 books"))
        assertTrue(s, s.contains("20h on a feed"))
    }

    @Test
    fun score_growsWithGoodSignals() {
        val a = BuiltWrapped.score(BuiltWrapped.Year(60, 0, 0, 0, 0))
        val b = BuiltWrapped.score(BuiltWrapped.Year(600, 0, 0, 0, 0))
        assertTrue(b > a)
    }

    @Test
    fun score_isPenalisedByDistraction() {
        val a = BuiltWrapped.score(BuiltWrapped.Year(600, 0, 0, 0, 0))
        val b = BuiltWrapped.score(BuiltWrapped.Year(600, 2400, 0, 0, 0))
        assertTrue(a > b)
    }

    @Test
    fun score_neverNegative() {
        val s = BuiltWrapped.score(BuiltWrapped.Year(0, 10000, 0, 0, 0))
        assertTrue(s >= 0)
    }
}
