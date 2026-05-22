package com.erluxman.focuslauncher.service

import kotlin.math.pow

/**
 * FIN-007 Future-Self Budget Projector.
 *
 * Compound a current balance forward with regular monthly contributions
 * at an annual rate. Returns projected balance at a target age (or
 * year-from-now if age unset).
 *
 *   FV = P · (1 + r)^n + M · ((1 + r)^n − 1) / r
 *     P = principal
 *     M = monthly contribution
 *     r = monthly rate (= annualRate / 12)
 *     n = months
 */
object BudgetProjector {

    const val DEFAULT_ANNUAL_RATE = 0.05  // 5% nominal

    fun projectAtAge(
        currentNetWorthUsd: Int,
        monthlyContributionUsd: Int,
        currentAge: Int,
        targetAge: Int,
        annualRate: Double = DEFAULT_ANNUAL_RATE,
    ): Long {
        if (targetAge <= currentAge) return currentNetWorthUsd.toLong()
        val months = (targetAge - currentAge) * 12
        return projectMonths(currentNetWorthUsd, monthlyContributionUsd, months, annualRate)
    }

    fun projectMonths(
        currentNetWorthUsd: Int,
        monthlyContributionUsd: Int,
        months: Int,
        annualRate: Double = DEFAULT_ANNUAL_RATE,
    ): Long {
        if (months <= 0) return currentNetWorthUsd.toLong()
        val r = annualRate / 12.0
        val growth = (1.0 + r).pow(months)
        val principalFv = currentNetWorthUsd * growth
        val contribFv = if (r == 0.0) monthlyContributionUsd.toDouble() * months
        else monthlyContributionUsd * ((growth - 1.0) / r)
        return (principalFv + contribFv).toLong().coerceAtLeast(0L)
    }
}
