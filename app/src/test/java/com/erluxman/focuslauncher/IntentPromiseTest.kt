package com.erluxman.focuslauncher

import com.erluxman.focuslauncher.service.IntentPromise
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class IntentPromiseTest {
    @Test fun withinBudget() {
        assertFalse(IntentPromise.didOverstay(elapsedMs = 60_000))
    }
    @Test fun overstayed() {
        assertTrue(IntentPromise.didOverstay(elapsedMs = IntentPromise.SESSION_BUDGET_MS + 1))
    }
}
