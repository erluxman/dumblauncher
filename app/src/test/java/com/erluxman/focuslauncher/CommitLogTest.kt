package com.erluxman.focuslauncher

import com.erluxman.focuslauncher.service.habits.CommitLog
import org.junit.Assert.assertEquals
import org.junit.Test

class CommitLogTest {

    @Test
    fun parse_dropsMalformed() {
        val c = CommitLog.parse(setOf("2026-05-22|5", "bad", "x|y"))
        assertEquals(1, c.size)
        assertEquals(5, c.first().commits)
    }

    @Test
    fun commitsOn_picksDate() {
        val c = CommitLog.parse(setOf("2026-05-22|5", "2026-05-21|2"))
        assertEquals(5, CommitLog.commitsOn("2026-05-22", c))
        assertEquals(0, CommitLog.commitsOn("2026-05-20", c))
    }

    @Test
    fun commitsIn_sumsWeek() {
        val c = CommitLog.parse(setOf("2026-05-22|5", "2026-05-21|3", "2026-05-20|2"))
        assertEquals(8, CommitLog.commitsIn(listOf("2026-05-22", "2026-05-21"), c))
        assertEquals(10, CommitLog.commitsIn(listOf("2026-05-22", "2026-05-21", "2026-05-20"), c))
    }
}
