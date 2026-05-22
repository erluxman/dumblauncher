package com.erluxman.focuslauncher

import com.erluxman.focuslauncher.service.VoiceTodo
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class VoiceTodoTest {

    @Test
    fun blank_returnsNull() {
        assertNull(VoiceTodo.parse(""))
        assertNull(VoiceTodo.parse("   "))
    }

    @Test
    fun plainText_noEstimate() {
        val p = VoiceTodo.parse("Write a blog post")
        assertEquals("Write a blog post", p?.text)
        assertNull(p?.estimateMin)
    }

    @Test
    fun forMinutesPattern_extracts() {
        val p = VoiceTodo.parse("Write a blog post for 30 minutes")
        assertEquals("Write a blog post", p?.text)
        assertEquals(30, p?.estimateMin)
    }

    @Test
    fun forShortMin_extracts() {
        val p = VoiceTodo.parse("Cardio for 15 min")
        assertEquals("Cardio", p?.text)
        assertEquals(15, p?.estimateMin)
    }

    @Test
    fun parenPattern_extracts() {
        val p = VoiceTodo.parse("Review PR (20 min)")
        assertEquals("Review PR", p?.text)
        assertEquals(20, p?.estimateMin)
    }

    @Test
    fun caseInsensitive() {
        val p = VoiceTodo.parse("study FOR 45 MINUTES")
        assertEquals("study", p?.text)
        assertEquals(45, p?.estimateMin)
    }
}
