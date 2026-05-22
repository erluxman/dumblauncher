package com.erluxman.focuslauncher

import com.erluxman.focuslauncher.data.local.entity.TodoEntity
import com.erluxman.focuslauncher.service.social.PromiseRatio
import org.junit.Assert.assertEquals
import org.junit.Test

class PromiseRatioTest {

    private val now = 1_000_000_000L
    private val day = 86_400_000L

    @Test
    fun empty_isZero() {
        val s = PromiseRatio.compute(emptyList(), nowMs = now)
        assertEquals(0, s.kept)
        assertEquals(0, s.broken)
        assertEquals(0, s.pending)
        assertEquals(0, s.ratioPct)
    }

    @Test
    fun allCompletedIsHundredPct() {
        val s = PromiseRatio.compute(
            listOf(
                TodoEntity(text = "A", isCompleted = true, createdAt = now - 2 * day),
                TodoEntity(text = "B", isCompleted = true, createdAt = now - 1 * day),
            ),
            nowMs = now,
        )
        assertEquals(2, s.kept)
        assertEquals(0, s.broken)
        assertEquals(100, s.ratioPct)
    }

    @Test
    fun staleUncompletedCountsAsBroken() {
        val s = PromiseRatio.compute(
            listOf(
                TodoEntity(text = "Stale", isCompleted = false, createdAt = now - 30 * day),
                TodoEntity(text = "Done", isCompleted = true, createdAt = now - 1 * day),
            ),
            nowMs = now,
        )
        assertEquals(1, s.kept)
        assertEquals(1, s.broken)
        assertEquals(50, s.ratioPct)
    }

    @Test
    fun freshUncompletedIsPending() {
        val s = PromiseRatio.compute(
            listOf(TodoEntity(text = "Fresh", isCompleted = false, createdAt = now - 1 * day)),
            nowMs = now,
        )
        assertEquals(0, s.kept)
        assertEquals(0, s.broken)
        assertEquals(1, s.pending)
    }
}
