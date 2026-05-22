package com.erluxman.focuslauncher
import com.erluxman.focuslauncher.service.lobby.UnlockIntervention

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class UnlockInterventionTest {

    @Test fun belowThreshold_noIntervention() {
        assertFalse(UnlockIntervention.shouldShow(13))
    }

    @Test fun atThreshold_showsIntervention() {
        assertTrue(UnlockIntervention.shouldShow(UnlockIntervention.THRESHOLD))
    }

    @Test fun multipleOfThreshold_showsIntervention() {
        assertTrue(UnlockIntervention.shouldShow(UnlockIntervention.THRESHOLD * 2))
    }

    @Test fun nonMultipleAboveThreshold_doesNotShow() {
        assertFalse(UnlockIntervention.shouldShow(UnlockIntervention.THRESHOLD + 1))
    }

    @Test fun parseCount_findsStoredEntry() {
        val store = setOf(
            "2026-05-21|com.instagram.android|7",
            "2026-05-21|com.twitter.android|3",
            "2026-05-20|com.instagram.android|99"
        )
        assertEquals(7, UnlockIntervention.parseCount(store, "2026-05-21", "com.instagram.android"))
        assertEquals(0, UnlockIntervention.parseCount(store, "2026-05-21", "com.reddit.frontpage"))
    }
}
