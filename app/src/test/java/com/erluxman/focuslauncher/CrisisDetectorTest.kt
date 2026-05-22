package com.erluxman.focuslauncher
import com.erluxman.focuslauncher.service.lobby.CrisisDetector

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class CrisisDetectorTest {

    @Test
    fun emptyOrSmallHistory_notCrisis() {
        assertFalse(CrisisDetector.isCrisis(emptyList()))
        assertFalse(CrisisDetector.isCrisis(listOf("DROWNING")))
        assertFalse(CrisisDetector.isCrisis(listOf("DROWNING", "DROWNING")))
    }

    @Test
    fun threeConsecutiveDrowning_isCrisis() {
        assertTrue(CrisisDetector.isCrisis(listOf("DROWNING", "DROWNING", "DROWNING")))
        assertTrue(CrisisDetector.isCrisis(listOf("DROWNING", "DROWNING", "DROWNING", "NEUTRAL")))
    }

    @Test
    fun gapInDrowning_notCrisis() {
        assertFalse(CrisisDetector.isCrisis(listOf("DROWNING", "NEUTRAL", "DROWNING", "DROWNING")))
        assertFalse(CrisisDetector.isCrisis(listOf("DROWNING", "DROWNING", "NEUTRAL", "DROWNING")))
    }

    @Test
    fun nonDrowningHead_notCrisis() {
        assertFalse(CrisisDetector.isCrisis(listOf("NEUTRAL", "DROWNING", "DROWNING", "DROWNING")))
    }
}
