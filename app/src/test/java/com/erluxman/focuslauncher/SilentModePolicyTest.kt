package com.erluxman.focuslauncher

import com.erluxman.focuslauncher.service.SilentModePolicy
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SilentModePolicyTest {

    @Test
    fun anySignalTriggersEnforcement() {
        assertTrue(SilentModePolicy.shouldEnforce(true, false, false))
        assertTrue(SilentModePolicy.shouldEnforce(false, true, false))
        assertTrue(SilentModePolicy.shouldEnforce(false, false, true))
    }

    @Test
    fun noSignalSkipsEnforcement() {
        assertFalse(SilentModePolicy.shouldEnforce(false, false, false))
    }

    @Test
    fun multipleSignals_stillTrue() {
        assertTrue(SilentModePolicy.shouldEnforce(true, true, true))
        assertTrue(SilentModePolicy.shouldEnforce(true, true, false))
    }
}
