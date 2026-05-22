package com.erluxman.focuslauncher

import com.erluxman.focuslauncher.service.sad.Narrator
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class NarratorTest {

    @Test
    fun belowThreshold_isQuiet() {
        assertNull(Narrator.lineFor("TikTok", 1))
        assertNull(Narrator.lineFor("TikTok", 2))
    }

    @Test
    fun thirdOpen_emits() {
        val s = Narrator.lineFor("TikTok", 3)
        assertNotNull(s)
        assertTrue("contains app name", s!!.contains("TikTok"))
    }

    @Test
    fun escalates_acrossBuckets() {
        val a = Narrator.lineFor("TikTok", 4)!!
        val b = Narrator.lineFor("TikTok", 11)!!
        val c = Narrator.lineFor("TikTok", 30)!!
        assertTrue(a != b)
        assertTrue(b != c)
    }

    @Test
    fun emptyAppLabel_returnsNull() {
        assertNull(Narrator.lineFor("", 10))
        assertNull(Narrator.lineFor("   ", 10))
    }
}
