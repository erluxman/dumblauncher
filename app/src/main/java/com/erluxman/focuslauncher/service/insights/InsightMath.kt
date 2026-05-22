package com.erluxman.focuslauncher.service.insights

/**
 * Pure math helpers powering the V1 "insight" surfaces — opportunity cost,
 * deathbed simulator, input/output ratio, and compound time-bank growth.
 */
object InsightMath {

    /** Hourly rate * fraction-of-hour. */
    fun opportunityCost(distractionMinutes: Int, hourlyRateUsd: Double): Double =
        (distractionMinutes / 60.0) * hourlyRateUsd

    /**
     * Projection of lifetime hours that will be spent on screen if the user keeps
     * burning the same daily minutes from now until the assumed end age.
     */
    fun lifetimeHoursOnScreen(
        dailyMinutes: Int,
        currentAge: Int,
        endAge: Int = 80
    ): Int {
        val yearsLeft = (endAge - currentAge).coerceAtLeast(0)
        return ((dailyMinutes.toLong() * 365L * yearsLeft) / 60L).toInt()
    }

    /**
     * Input = consumed distraction minutes today.
     * Output = builder minutes today (todos completed * 15 + focus sessions * 25).
     * Returns the ratio output/input, capped at 99.0 for display. Zero input → infinity-equivalent: 99.0.
     */
    fun ioRatio(distractionMinutes: Int, todosCompleted: Int, focusSessions: Int): Double {
        val output = todosCompleted * 15 + focusSessions * 25
        if (distractionMinutes == 0) return if (output == 0) 0.0 else CAPPED_RATIO
        return (output.toDouble() / distractionMinutes).coerceAtMost(CAPPED_RATIO)
    }

    const val CAPPED_RATIO = 99.0

    /**
     * Compound the time-bank balance forward by [days] days using a simple
     * daily interest rate. Default 0.1%/day is ~44%/yr, a deliberately
     * eye-catching projection ("look what compounding does").
     */
    fun compoundedBalance(currentMin: Int, dailyRate: Double = DAILY_RATE, days: Int): Int {
        if (currentMin <= 0 || days <= 0) return currentMin
        var balance = currentMin.toDouble()
        repeat(days) { balance *= (1.0 + dailyRate) }
        return balance.toInt()
    }

    const val DAILY_RATE = 0.001
}
