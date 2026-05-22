package com.erluxman.focuslauncher

import com.erluxman.focuslauncher.service.ContactsLog
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ContactsLogTest {

    @Test
    fun parse_dropsMalformed() {
        val t = ContactsLog.parse(setOf("2026-05-22|Sarah", "noPipe", "2026-05-21|   "))
        assertEquals(1, t.size)
        assertEquals("Sarah", t.first().name)
    }

    @Test
    fun lastSeen_picksMax() {
        val t = ContactsLog.parse(setOf("2026-05-22|Sarah", "2026-05-20|Sarah"))
        assertEquals("2026-05-22", ContactsLog.lastSeen("Sarah", t))
        assertNull(ContactsLog.lastSeen("Other", t))
    }

    @Test
    fun stale_surfacesUnseenInWindow() {
        val t = ContactsLog.parse(setOf(
            "2026-04-01|Old",
            "2026-05-22|Fresh",
            "2026-04-20|Older",
        ))
        val stale = ContactsLog.staleContacts(t, todayIso = "2026-05-22", staleDays = 30)
        assertTrue("Fresh shouldn't be stale", "Fresh" !in stale)
        assertTrue(stale.contains("Old"))
        assertTrue(stale.contains("Older"))
        // Oldest first
        assertEquals(listOf("Old", "Older"), stale)
    }
}
