package com.erluxman.focuslauncher

import com.erluxman.focuslauncher.service.TravelAtlas
import org.junit.Assert.assertEquals
import org.junit.Test

class TravelAtlasTest {

    @Test
    fun parse_dropsMalformed() {
        val v = TravelAtlas.parse(setOf("2024|Tokyo", "x|y", "abc"))
        assertEquals(1, v.size)
        assertEquals("Tokyo", v.first().location)
    }

    @Test
    fun parse_sortsNewestFirst() {
        val v = TravelAtlas.parse(setOf("2024|Tokyo", "2023|Berlin", "2025|Lisbon"))
        assertEquals(listOf("Lisbon", "Tokyo", "Berlin"), v.map { it.location })
    }

    @Test
    fun distinct_dedupesCaseInsensitive() {
        val v = TravelAtlas.parse(setOf("2024|Tokyo", "2023|tokyo", "2024|Berlin"))
        assertEquals(2, TravelAtlas.distinctLocations(v))
    }

    @Test
    fun yearsCovered_distinctYears() {
        val v = TravelAtlas.parse(setOf("2024|Tokyo", "2024|Berlin", "2023|Lisbon"))
        assertEquals(2, TravelAtlas.yearsCovered(v))
    }
}
