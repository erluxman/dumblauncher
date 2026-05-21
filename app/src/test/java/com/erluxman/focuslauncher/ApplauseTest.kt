package com.erluxman.focuslauncher

import com.erluxman.focuslauncher.service.Applause
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class ApplauseTest {

    @Test
    fun shortSession_getsApplause() {
        assertNotNull(Applause.maybeLine(elapsedMs = 30_000L, seed = 0L))
    }

    @Test
    fun longSession_noApplause() {
        assertNull(Applause.maybeLine(elapsedMs = Applause.APPLAUSE_THRESHOLD_MS, seed = 0L))
    }
}
