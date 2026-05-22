package com.erluxman.focuslauncher

import com.erluxman.focuslauncher.service.insights.SessionReceipt
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class SessionReceiptTest {

    @Test
    fun underTenSeconds_isSuppressed() {
        assertNull(SessionReceipt.format(0L, "Instagram"))
        assertNull(SessionReceipt.format(5_000L, "Instagram"))
        assertNull(SessionReceipt.format(SessionReceipt.SUPPRESS_BELOW_MS - 1, "Instagram"))
    }

    @Test
    fun atSuppressBoundary_returnsSecondsFormat() {
        assertEquals("Spent 10s in Instagram", SessionReceipt.format(10_000L, "Instagram"))
    }

    @Test
    fun underOneMinute_secondsFormat() {
        assertEquals("Spent 45s in Reddit", SessionReceipt.format(45_000L, "Reddit"))
    }

    @Test
    fun overOneMinute_minutesFormat() {
        assertEquals("Spent 5m in TikTok", SessionReceipt.format(5L * 60 * 1000, "TikTok"))
        assertEquals("Spent 13m in YouTube", SessionReceipt.format(13L * 60 * 1000 + 22_000L, "YouTube"))
    }
}
