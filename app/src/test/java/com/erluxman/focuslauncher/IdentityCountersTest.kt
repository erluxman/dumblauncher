package com.erluxman.focuslauncher

import com.erluxman.focuslauncher.service.social.IdentityCounters
import org.junit.Assert.assertEquals
import org.junit.Test

class IdentityCountersTest {

    @Test
    fun parse_dropsMalformed() {
        val e = IdentityCounters.parse(setOf("2026-05-22|accepted|Asked for raise", "bad"))
        assertEquals(1, e.size)
        assertEquals("accepted", e.first().outcome)
    }

    @Test
    fun count_bucketsByOutcome() {
        val e = IdentityCounters.parse(setOf(
            "2026-05-22|accepted|A",
            "2026-05-22|rejected|B",
            "2026-05-21|accepted|C",
        ))
        assertEquals(2, IdentityCounters.count("accepted", e))
        assertEquals(1, IdentityCounters.count("rejected", e))
    }

    @Test
    fun countOnDate_filters() {
        val e = IdentityCounters.parse(setOf(
            "2026-05-22|made|A",
            "2026-05-22|made|B",
            "2026-05-21|made|C",
        ))
        assertEquals(2, IdentityCounters.countOnDate("made", "2026-05-22", e))
        assertEquals(1, IdentityCounters.countOnDate("made", "2026-05-21", e))
    }

    @Test
    fun acceptance_rateMath() {
        val e = IdentityCounters.parse(setOf(
            "2026-05-22|accepted|A",
            "2026-05-22|rejected|B",
            "2026-05-21|accepted|C",
        ))
        assertEquals(66, IdentityCounters.acceptanceRate(e))
    }

    @Test
    fun acceptance_zeroIfNoAsks() {
        val e = IdentityCounters.parse(setOf("2026-05-22|made|A"))
        assertEquals(0, IdentityCounters.acceptanceRate(e))
    }
}
