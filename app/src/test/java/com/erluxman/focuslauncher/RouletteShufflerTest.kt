package com.erluxman.focuslauncher
import com.erluxman.focuslauncher.service.lobby.RouletteShuffler

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class RouletteShufflerTest {

    @Test
    fun emptyAndSingleton_returnInput() {
        assertEquals(emptyList<String>(), RouletteShuffler.shuffle(emptyList<String>(), seed = 0L))
        assertEquals(listOf("only"), RouletteShuffler.shuffle(listOf("only"), seed = 0L))
    }

    @Test
    fun sameSeed_returnsSameOrder() {
        val pool = listOf("a", "b", "c", "d", "e")
        val a = RouletteShuffler.shuffle(pool, seed = 17L)
        val b = RouletteShuffler.shuffle(pool, seed = 17L)
        assertEquals(a, b)
    }

    @Test
    fun differentSeeds_canChangeOrder() {
        val pool = (1..20).map { it.toString() }
        val a = RouletteShuffler.shuffle(pool, seed = 1L)
        val b = RouletteShuffler.shuffle(pool, seed = 2L)
        assertNotEquals(a, b)
    }

    @Test
    fun preservesElements() {
        val pool = listOf("a", "b", "c", "d", "e")
        val shuffled = RouletteShuffler.shuffle(pool, seed = 7L)
        assertEquals(pool.toSet(), shuffled.toSet())
        assertEquals(pool.size, shuffled.size)
    }

    @Test
    fun multipleSeeds_distributeAcrossPositions() {
        val pool = listOf("a", "b", "c")
        val seenFirsts = mutableSetOf<String>()
        for (seed in 0L..50L) {
            seenFirsts += RouletteShuffler.shuffle(pool, seed).first()
        }
        assertTrue("Expected variety of first positions, got $seenFirsts", seenFirsts.size >= 2)
    }
}
