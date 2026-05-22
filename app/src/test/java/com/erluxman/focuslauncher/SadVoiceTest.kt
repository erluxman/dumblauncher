package com.erluxman.focuslauncher

import com.erluxman.focuslauncher.service.sad.SadSelfEngine
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SadVoiceTest {

    @Test
    fun differentVoices_produceDifferentMessages() {
        val stern = SadSelfEngine.pick("DROWNING", "Build", seed = 0, voice = SadSelfEngine.Voice.STERN)
        val drill = SadSelfEngine.pick("DROWNING", "Build", seed = 0, voice = SadSelfEngine.Voice.DRILL)
        assertNotEquals(stern, drill)
    }

    @Test
    fun compassionate_containsSoftCue() {
        val msg = SadSelfEngine.pick(
            "DROWNING",
            "Build",
            seed = 0,
            voice = SadSelfEngine.Voice.COMPASSIONATE
        )
        assertTrue(msg.contains("kind", ignoreCase = true) ||
            msg.contains("soft", ignoreCase = true) ||
            msg.contains("gentle", ignoreCase = true) ||
            msg.contains("Be ", ignoreCase = true) ||
            msg.contains("Breathe", ignoreCase = true) ||
            msg.contains("Hey", ignoreCase = true))
    }

    @Test
    fun drill_isPunchy() {
        val msg = SadSelfEngine.pick(
            "DROWNING",
            "",
            seed = 0,
            voice = SadSelfEngine.Voice.DRILL
        )
        assertTrue(msg.contains("UNACCEPTABLE") || msg.uppercase() == msg ||
            msg.contains("Move") || msg.contains("Go"))
    }
}
