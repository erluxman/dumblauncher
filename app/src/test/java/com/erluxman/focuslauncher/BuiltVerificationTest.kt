package com.erluxman.focuslauncher

import com.erluxman.focuslauncher.service.BuiltVerification
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class BuiltVerificationTest {

    @Test
    fun ineligible_whenDaysShort() {
        val s = BuiltVerification.compute(daysInstalled = 30, projectsShipped = 5)
        assertFalse(s.isEligible)
        assertEquals(335, s.daysShort)
    }

    @Test
    fun ineligible_whenProjectsShort() {
        val s = BuiltVerification.compute(daysInstalled = 400, projectsShipped = 1)
        assertFalse(s.isEligible)
        assertEquals(2, s.projectsShort)
    }

    @Test
    fun eligible_whenBothMet() {
        val s = BuiltVerification.compute(daysInstalled = 500, projectsShipped = 3)
        assertTrue(s.isEligible)
        assertEquals(0, s.daysShort)
        assertEquals(0, s.projectsShort)
    }

    @Test
    fun nonNegativeInputs() {
        val s = BuiltVerification.compute(daysInstalled = -5, projectsShipped = -2)
        assertEquals(0, s.daysInstalled)
        assertEquals(0, s.projectsShipped)
    }
}
