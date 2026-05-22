package com.erluxman.focuslauncher

import com.erluxman.focuslauncher.service.BudgetProjector
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class BudgetProjectorTest {

    @Test
    fun projectAtAge_returnsBaseIfNoYearsLeft() {
        assertEquals(10_000L, BudgetProjector.projectAtAge(10_000, 500, 65, 65))
        assertEquals(10_000L, BudgetProjector.projectAtAge(10_000, 500, 65, 50))
    }

    @Test
    fun zeroRate_isPlainAddition() {
        // 10k principal + 100/mo * 120 months = 22_000 at 0% rate
        val fv = BudgetProjector.projectMonths(10_000, 100, 120, annualRate = 0.0)
        assertEquals(22_000L, fv)
    }

    @Test
    fun fivePercent_growsBalance() {
        val fv = BudgetProjector.projectMonths(10_000, 100, 120, annualRate = 0.05)
        // Compound effect should produce > 22_000.
        assertTrue("expected > 22000, got $fv", fv > 22_000L)
    }

    @Test
    fun thirtyYears_fivePercent_meaningfullyLarge() {
        val fv = BudgetProjector.projectAtAge(
            currentNetWorthUsd = 25_000,
            monthlyContributionUsd = 1_000,
            currentAge = 35,
            targetAge = 65,
            annualRate = 0.05,
        )
        // At 5% real return over 30 years, FV is roughly ~$0.95M
        assertTrue("expected >= 800_000, got $fv", fv >= 800_000L)
        assertTrue("expected <= 1_500_000, got $fv", fv <= 1_500_000L)
    }
}
