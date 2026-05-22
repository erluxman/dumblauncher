package com.erluxman.focuslauncher

import com.erluxman.focuslauncher.service.mortality.DailyWins
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class DailyWinsTest {

    @Test
    fun summarize_clampsNegatives() {
        val s = DailyWins.summarize(-1, -1, -10, -10, -10, -10)
        assertEquals(0, s.total)
    }

    @Test
    fun summarize_combinesAcrossSignals() {
        val s = DailyWins.summarize(
            todosDoneToday = 3,
            focusSessionsToday = 2,
            meditationMinToday = 10,
            readingMinToday = 15,
            workoutMinToday = 30,
            timeBankAddedToday = 60,
        )
        // 3 + 2 + 1 + 1 + 2 + 2 = 11
        assertEquals(11, s.total)
    }

    @Test
    fun summarize_isAdditive() {
        val a = DailyWins.summarize(1, 0, 0, 0, 0, 0).total
        val b = DailyWins.summarize(0, 1, 0, 0, 0, 0).total
        val both = DailyWins.summarize(1, 1, 0, 0, 0, 0).total
        assertTrue(both >= a + b)
    }
}
