package com.erluxman.focuslauncher

import com.erluxman.focuslauncher.service.Highlights
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class HighlightsTest {

    @Test
    fun pick_empty_returnsNull() {
        assertNull(Highlights.pickForToday(emptySet(), "2026-05-22"))
    }

    @Test
    fun pick_stableSameDay() {
        val set = setOf("alpha", "beta", "gamma")
        val a = Highlights.pickForToday(set, "2026-05-22")
        val b = Highlights.pickForToday(set, "2026-05-22")
        assertEquals(a, b)
        assertNotNull(a)
    }

    @Test
    fun pick_changesWithDate() {
        val set = setOf("alpha", "beta", "gamma", "delta", "epsilon")
        val pickedDays = (1..30).map { d ->
            Highlights.pickForToday(set, "2026-05-${"%02d".format(d)}")
        }.toSet()
        // Across 30 days we should see at least 2 distinct picks.
        assert(pickedDays.size >= 2) { "Pick should vary across days, got $pickedDays" }
    }
}
