package com.erluxman.focuslauncher

import com.erluxman.focuslauncher.service.Vocabulary
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class VocabularyTest {

    @Test
    fun unique_lowercased() {
        val s = Vocabulary.uniqueWords(listOf("Hello WORLD", "hello there"))
        assertTrue(s.contains("hello"))
        assertTrue(s.contains("world"))
        assertTrue(s.contains("there"))
        assertEquals(3, s.size)
    }

    @Test
    fun unique_dropsShortTokens() {
        val s = Vocabulary.uniqueWords(listOf("a be cat dog"))
        assertTrue("cat" in s && "dog" in s)
        assertTrue("a" !in s && "be" !in s)
    }

    @Test
    fun growth_isNetNew() {
        val before = listOf("the cat sat")
        val after = listOf("the cat ran across the road")
        val g = Vocabulary.growth(before, after)
        // new: ran, across, road → 3
        assertEquals(3, g)
    }

    @Test
    fun growth_zeroIfNoNew() {
        val before = listOf("the cat sat")
        val after = listOf("the cat sat the")
        assertEquals(0, Vocabulary.growth(before, after))
    }
}
