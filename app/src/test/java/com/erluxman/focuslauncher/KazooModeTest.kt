package com.erluxman.focuslauncher

import com.erluxman.focuslauncher.service.KazooMode
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class KazooModeTest {

    @Test
    fun belowThreshold_isQuiet() {
        assertNull(KazooMode.lineFor(0))
        assertNull(KazooMode.lineFor(29))
    }

    @Test
    fun atThreshold_emits() {
        assertNotNull(KazooMode.lineFor(30, seed = 0L))
    }

    @Test
    fun lineRotatesAcrossSeeds() {
        val seen = (0L..7L).map { KazooMode.lineFor(60, seed = it) }.toSet()
        assertTrue(seen.size > 1)
    }
}
