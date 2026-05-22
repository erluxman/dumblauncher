package com.erluxman.focuslauncher
import com.erluxman.focuslauncher.service.launcher.CalendarReader

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class CalendarReaderTest {

    @Test
    fun activeEvent_picksCurrent() {
        val now = 1_000_000L
        val events = listOf(
            CalendarReader.Event("Past", now - 5000, now - 1000),
            CalendarReader.Event("Now", now - 500, now + 500),
            CalendarReader.Event("Future", now + 1000, now + 5000)
        )
        assertEquals("Now", CalendarReader.activeEvent(events, now)?.title)
    }

    @Test
    fun noActiveEvent_returnsNull() {
        val events = listOf(CalendarReader.Event("Later", 2000, 3000))
        assertNull(CalendarReader.activeEvent(events, nowMs = 100))
    }

    @Test
    fun isFocusBlock_detectsKeywordsCaseInsensitive() {
        val e = CalendarReader.Event("Deep Work block", 0, 1)
        assertTrue(CalendarReader.isFocusBlock(e))
        assertFalse(CalendarReader.isFocusBlock(CalendarReader.Event("Lunch", 0, 1)))
        assertFalse(CalendarReader.isFocusBlock(null))
    }
}
