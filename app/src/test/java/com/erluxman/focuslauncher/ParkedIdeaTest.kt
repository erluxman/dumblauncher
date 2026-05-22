package com.erluxman.focuslauncher

import com.erluxman.focuslauncher.service.insights.ParkedIdea
import org.junit.Assert.assertEquals
import org.junit.Test

class ParkedIdeaTest {

    @Test
    fun parse_dropsMalformed() {
        val items = ParkedIdea.parse(setOf("badentry", "1234|hi", "notnumber|nope"))
        assertEquals(1, items.size)
        assertEquals("hi", items.first().text)
    }

    @Test
    fun parse_sortsNewestFirst() {
        val items = ParkedIdea.parse(setOf("100|old", "300|new", "200|mid"))
        assertEquals(listOf("new", "mid", "old"), items.map { it.text })
    }

    @Test
    fun parse_emptyIsEmpty() {
        assertEquals(emptyList<ParkedIdea.Item>(), ParkedIdea.parse(emptySet()))
    }
}
