package com.erluxman.focuslauncher.service

/**
 * Combines FIN-002 (net worth) + FIN-004 (savings rate %) on a single
 * tile. All inputs in whole USD, no cents — manual entry only.
 */
object MoneyMath {

    /** assets - liabilities, clamped to Int range we care about. */
    fun netWorth(assetsUsd: Int, liabilitiesUsd: Int): Int =
        (assetsUsd.toLong() - liabilitiesUsd.toLong()).coerceIn(Int.MIN_VALUE.toLong(), Int.MAX_VALUE.toLong()).toInt()

    /** 0..100 percentage; negative income returns 0. Zero income → 0. */
    fun savingsRatePct(monthlyIncomeUsd: Int, monthlyExpenseUsd: Int): Int {
        if (monthlyIncomeUsd <= 0) return 0
        val saved = (monthlyIncomeUsd - monthlyExpenseUsd).coerceAtLeast(0)
        return (saved.toDouble() / monthlyIncomeUsd * 100.0).toInt().coerceIn(0, 100)
    }
}
