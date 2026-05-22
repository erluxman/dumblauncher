package com.erluxman.focuslauncher

import com.erluxman.focuslauncher.service.sad.SadSelfEngine
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SadSelfEngineTest {

    @Test
    fun picksFromWhyPool_whenWhyProvided() {
        val msg = SadSelfEngine.pick(state = "DROWNING", why = "Build, don't scroll.", seed = 0)
        assertTrue(msg.contains("Build, don't scroll.") || msg.contains("Right now"))
    }

    @Test
    fun picksFromNoWhyPool_whenWhyBlank() {
        val msg = SadSelfEngine.pick(state = "SINKING", why = "", seed = 0)
        assertFalse(msg.contains("{why}"))
        assertTrue(msg.contains("SINKING") || msg.isNotBlank())
    }

    @Test
    fun substitutesStatePlaceholder() {
        repeat(20) { seed ->
            val msg = SadSelfEngine.pick(state = "DROWNING", why = "", seed = seed)
            assertFalse(msg.contains("{state}"))
        }
    }

    @Test
    fun stableForSameSeed() {
        val a = SadSelfEngine.pick("DROWNING", "x", seed = 7)
        val b = SadSelfEngine.pick("DROWNING", "x", seed = 7)
        assertEquals(a, b)
    }

    @Test
    fun negativeSeedStillReturnsValidString() {
        val msg = SadSelfEngine.pick("DROWNING", "", seed = -3)
        assertTrue(msg.isNotBlank())
    }

    @Test
    fun differentSeedsCanProduceDifferentText() {
        val seen = mutableSetOf<String>()
        repeat(10) { seed -> seen += SadSelfEngine.pick("DROWNING", "", seed = seed) }
        assertTrue("Expected at least 2 distinct messages across 10 seeds", seen.size >= 2)
    }
}
