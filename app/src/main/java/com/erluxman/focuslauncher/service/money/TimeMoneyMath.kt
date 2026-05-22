package com.erluxman.focuslauncher.service.money

/**
 * FIN-006 Time-Money Conversion.
 *
 * Pure conversions between purchase amounts and hours-of-life at the
 * user's after-tax hourly rate. We default the tax multiplier to 1.0
 * (the user's hourly rate is already net of tax in practice); callers
 * can override if they want a "gross" version.
 */
object TimeMoneyMath {

    fun hoursForPurchase(usd: Double, hourlyRate: Int, taxMultiplier: Double = 1.0): Double {
        if (hourlyRate <= 0) return 0.0
        return (usd / (hourlyRate * taxMultiplier)).coerceAtLeast(0.0)
    }

    fun usdForHours(hours: Double, hourlyRate: Int): Double {
        if (hourlyRate <= 0 || hours <= 0.0) return 0.0
        return hours * hourlyRate
    }

    fun formatHours(hours: Double): String {
        return when {
            hours < 1.0 / 60 -> "moments"
            hours < 1.0 -> "${(hours * 60).toInt()}m"
            hours < 24.0 -> "${hours.toInt()}h ${((hours - hours.toInt()) * 60).toInt()}m"
            else -> "${(hours / 24).toInt()}d ${(hours.toInt() % 24)}h"
        }
    }
}
