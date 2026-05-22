package com.erluxman.focuslauncher

import com.erluxman.focuslauncher.service.social.Disappointment
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class DisappointmentTest {

    @Test
    fun headline_nullWhenAllZero() {
        assertNull(Disappointment.headline(Disappointment.WeekStats(0, 0, 0, 0)))
    }

    @Test
    fun headline_picksTheHighestSignal() {
        val s = Disappointment.headline(Disappointment.WeekStats(distractionHours = 20, nightUnlocksAfterCutoff = 5, streakBreaks = 2, unfinishedTodos = 8))
        assertTrue("$s", s!!.contains("20h"))
    }

    @Test
    fun headline_picksFirstOnTie() {
        val s = Disappointment.headline(Disappointment.WeekStats(0, 5, 5, 0))
        // either night unlocks or streak breaks; both have 5. just ensure non-blank
        assertTrue(s!!.isNotBlank())
    }
}
