package com.erluxman.focuslauncher

import com.erluxman.focuslauncher.service.FocusStory
import org.junit.Assert.assertTrue
import org.junit.Test

class FocusStoryTest {

    @Test
    fun cleanDay_celebrates() {
        val s = FocusStory.headline(focusSessionsToday = 3, todosCompletedToday = 2, distractionMinutesToday = 15)
        assertTrue("clean day phrasing: $s", s.contains("Shipped"))
    }

    @Test
    fun drowningDay_compassionate() {
        val s = FocusStory.headline(focusSessionsToday = 0, todosCompletedToday = 0, distractionMinutesToday = 360)
        assertTrue("drowning phrasing: $s", s.contains("Drowning"))
    }

    @Test
    fun fightingDay_mixedFraming() {
        val s = FocusStory.headline(focusSessionsToday = 1, todosCompletedToday = 1, distractionMinutesToday = 60)
        assertTrue("mixed phrasing: $s", s.contains("Mixed"))
    }

    @Test
    fun quietDay_acknowledges() {
        val s = FocusStory.headline(focusSessionsToday = 1, todosCompletedToday = 0, distractionMinutesToday = 200)
        assertTrue("non-empty: $s", s.isNotBlank())
    }

    @Test
    fun slowDay_isNotJudgemental() {
        val s = FocusStory.headline(0, 0, 200)
        assertTrue(s.contains("slow") || s.contains("Nothing wrong"))
    }
}
