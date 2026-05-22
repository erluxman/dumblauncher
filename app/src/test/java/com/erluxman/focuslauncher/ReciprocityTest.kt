package com.erluxman.focuslauncher

import com.erluxman.focuslauncher.service.Reciprocity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ReciprocityTest {

    @Test
    fun parse_dropsMalformed() {
        val t = Reciprocity.parse(setOf(
            "2026-05-22|Sarah|out",
            "noPipe",
            "2026-05-22|Sarah|maybe",
            "2026-05-22||out",
        ))
        assertEquals(1, t.size)
        assertEquals(true, t.first().outbound)
    }

    @Test
    fun outboundPct_simpleMath() {
        val t = Reciprocity.parse(setOf(
            "2026-05-22|Sarah|out",
            "2026-05-21|Sarah|out",
            "2026-05-20|Sarah|in",
            "2026-05-19|Sarah|out",
        ))
        assertEquals(75, Reciprocity.outboundPct("Sarah", t))
    }

    @Test
    fun lopsided_threshold_picksOneSidedRelations() {
        val t = Reciprocity.parse(setOf(
            "2026-05-22|Sarah|out",
            "2026-05-21|Sarah|out",
            "2026-05-20|Sarah|out",
            "2026-05-19|Sarah|out",
            "2026-05-18|Sarah|out",
            // Tied other:
            "2026-05-22|Mike|out",
            "2026-05-21|Mike|in",
            "2026-05-20|Mike|out",
            "2026-05-19|Mike|in",
            "2026-05-18|Mike|out",
        ))
        val l = Reciprocity.lopsidedRelationships(t)
        assertTrue("Sarah is lopsided: $l", l.contains("Sarah"))
        assertTrue("Mike is balanced: $l", !l.contains("Mike"))
    }
}
