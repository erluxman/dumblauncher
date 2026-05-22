package com.erluxman.focuslauncher

import com.erluxman.focuslauncher.service.SubscriptionMath
import org.junit.Assert.assertEquals
import org.junit.Test

class SubscriptionMathTest {

    @Test
    fun parse_dropsMalformed() {
        val s = SubscriptionMath.parse(setOf("Spotify|10.99", "Bad", "x|y"))
        assertEquals(1, s.size)
        assertEquals("Spotify", s.first().name)
    }

    @Test
    fun totals_sumAndAnnualise() {
        val s = SubscriptionMath.parse(setOf("Spotify|10.99", "Netflix|15.99", "Gym|29.0"))
        assertEquals(55.98, SubscriptionMath.totalMonthly(s), 1e-9)
        assertEquals(671.76, SubscriptionMath.totalAnnual(s), 1e-9)
    }

    @Test
    fun parse_sortsExpensiveFirst() {
        val s = SubscriptionMath.parse(setOf("A|5", "B|20", "C|10"))
        assertEquals(listOf("B", "C", "A"), s.map { it.name })
    }
}
