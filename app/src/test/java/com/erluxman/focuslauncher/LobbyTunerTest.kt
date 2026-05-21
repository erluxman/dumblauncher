package com.erluxman.focuslauncher

import com.erluxman.focuslauncher.service.LobbyTuner
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class LobbyTunerTest {

    @Test
    fun base_whenNothingActive() {
        assertEquals(10, LobbyTuner.countdownSeconds(visitOrdinal = 0, escalating = false, variableRatio = false, randomRoll = 0.5))
    }

    @Test
    fun escalation_growsWithVisitOrdinal() {
        assertEquals(10, LobbyTuner.countdownSeconds(visitOrdinal = 0, escalating = true, variableRatio = false, randomRoll = 0.99))
        assertEquals(15, LobbyTuner.countdownSeconds(visitOrdinal = 1, escalating = true, variableRatio = false, randomRoll = 0.99))
        assertEquals(25, LobbyTuner.countdownSeconds(visitOrdinal = 3, escalating = true, variableRatio = false, randomRoll = 0.99))
    }

    @Test
    fun escalationDisabled_noGrowth() {
        assertEquals(10, LobbyTuner.countdownSeconds(visitOrdinal = 5, escalating = false, variableRatio = false, randomRoll = 0.99))
    }

    @Test
    fun variableRatio_addsPenaltyOnLowRoll() {
        assertEquals(25, LobbyTuner.countdownSeconds(visitOrdinal = 0, escalating = false, variableRatio = true, randomRoll = 0.0))
        assertEquals(10, LobbyTuner.countdownSeconds(visitOrdinal = 0, escalating = false, variableRatio = true, randomRoll = 0.99))
    }

    @Test
    fun harderMath_followsVariableRatioGate() {
        assertEquals(true, LobbyTuner.isHarderMath(variableRatio = true, randomRoll = 0.0))
        assertEquals(false, LobbyTuner.isHarderMath(variableRatio = true, randomRoll = 0.99))
        assertEquals(false, LobbyTuner.isHarderMath(variableRatio = false, randomRoll = 0.0))
    }

    @Test
    fun userLevel_easesBase() {
        assertEquals(7, LobbyTuner.countdownSeconds(
            visitOrdinal = 0,
            escalating = false,
            variableRatio = false,
            randomRoll = 0.99,
            userLevel = 3
        ))
    }

    @Test
    fun replacement_isStableAndInPool() {
        val a = LobbyTuner.replacement(42L)
        val b = LobbyTuner.replacement(42L)
        assertEquals(a, b)
        assertTrue(a in LobbyTuner.REPLACEMENTS)
    }
}
