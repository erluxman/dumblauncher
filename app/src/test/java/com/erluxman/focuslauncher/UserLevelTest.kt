package com.erluxman.focuslauncher

import com.erluxman.focuslauncher.service.tracks.UserLevel
import org.junit.Assert.assertEquals
import org.junit.Test

class UserLevelTest {

    @Test
    fun level_zeroForFreshUser() {
        assertEquals(0, UserLevel.level(0))
    }

    @Test
    fun level_growsLinearly() {
        assertEquals(1, UserLevel.level(UserLevel.MINUTES_PER_LEVEL))
        assertEquals(5, UserLevel.level(UserLevel.MINUTES_PER_LEVEL * 5))
    }

    @Test
    fun level_clampsAtMax() {
        assertEquals(UserLevel.MAX_LEVEL, UserLevel.level(UserLevel.MINUTES_PER_LEVEL * 99))
    }

    @Test
    fun easedLobby_subtractsLevelFromBase() {
        assertEquals(10, UserLevel.easedLobbySeconds(baseSeconds = 10, level = 0))
        assertEquals(7, UserLevel.easedLobbySeconds(baseSeconds = 10, level = 3))
    }

    @Test
    fun easedLobby_clampsAtFloor() {
        assertEquals(UserLevel.MIN_FLOOR_S, UserLevel.easedLobbySeconds(baseSeconds = 3, level = 10))
    }
}
