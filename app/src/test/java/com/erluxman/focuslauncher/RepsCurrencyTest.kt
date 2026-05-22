package com.erluxman.focuslauncher

import com.erluxman.focuslauncher.service.fitness.RepsCurrency
import org.junit.Assert.assertEquals
import org.junit.Test

class RepsCurrencyTest {

    @Test
    fun pointsForMinutes_basicRatio() {
        assertEquals(0, RepsCurrency.pointsForMinutes(0))
        assertEquals(0, RepsCurrency.pointsForMinutes(4))
        assertEquals(1, RepsCurrency.pointsForMinutes(5))
        assertEquals(12, RepsCurrency.pointsForMinutes(60))
    }

    @Test
    fun pointsForMinutes_negativeIsZero() {
        assertEquals(0, RepsCurrency.pointsForMinutes(-30))
    }

    @Test
    fun pointsForMinutes_isCapped() {
        assertEquals(RepsCurrency.DAILY_CAP_POINTS, RepsCurrency.pointsForMinutes(10_000))
    }
}
