package com.erluxman.focuslauncher

import com.erluxman.focuslauncher.service.launcher.AutoReplyTemplates
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AutoReplyTemplatesTest {

    @Test
    fun pick_isStableForSameSeed() {
        assertEquals(AutoReplyTemplates.pick(seed = 0), AutoReplyTemplates.pick(seed = 0))
    }

    @Test
    fun pick_returnsNonBlank() {
        repeat(20) { assertTrue(AutoReplyTemplates.pick(seed = it.toLong()).isNotBlank()) }
    }

    @Test
    fun pick_rotatesAcrossSeeds() {
        val s = (0L..5L).map { AutoReplyTemplates.pick(it) }.toSet()
        assertTrue(s.size > 1)
        // Sanity: not just one constant.
        assertNotEquals(AutoReplyTemplates.pick(0L), AutoReplyTemplates.pick(1L))
    }
}
