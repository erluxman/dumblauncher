package com.erluxman.focuslauncher

import com.erluxman.focuslauncher.service.sad.CelebrationSelf
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class CelebrationSelfTest {

    @Test
    fun noMilestone_returnsNull() {
        assertNull(CelebrationSelf.milestone(streakDays = 6, levelUp = false))
    }

    @Test
    fun sevenDayMilestone_recognized() {
        assertEquals("7 days clean", CelebrationSelf.milestone(7, false))
    }

    @Test
    fun levelUp_alwaysWins() {
        assertEquals("level up", CelebrationSelf.milestone(streakDays = 100, levelUp = true))
    }

    @Test
    fun pick_isStableForSameSeed() {
        val a = CelebrationSelf.pick("7 days clean", seed = 3)
        val b = CelebrationSelf.pick("7 days clean", seed = 3)
        assertEquals(a, b)
    }

    @Test
    fun pick_substitutesMilestone() {
        val msg = CelebrationSelf.pick("30-day streak", seed = 0)
        assertNotNull(msg)
        assert(msg.contains("30-day streak"))
    }
}
