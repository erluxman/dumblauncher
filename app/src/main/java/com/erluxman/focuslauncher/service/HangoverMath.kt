package com.erluxman.focuslauncher.service

/**
 * SUB-001 Hangover Calculus.
 *
 * Single-compartment alcohol pharmacokinetics, rough enough to be useful
 * but explicitly approximate. BAC is in % (0.08 ≈ legal limit).
 *
 * Inputs/outputs deliberately simple: we don't ask for body weight or
 * sex here — V1 just demonstrates the curve and the next-day hit.
 */
object HangoverMath {

    /** Alcohol elimination, percentage-points per hour (Widmark constant for an average adult). */
    const val ELIMINATION_RATE_PER_HOUR = 0.015

    /** Headline rule of thumb: each standard drink ≈ 0.02% BAC. */
    const val BAC_PER_STANDARD_DRINK = 0.02

    /** Estimated REM-sleep hit per standard drink consumed within 4 hours of bed. */
    const val SLEEP_DEFICIT_MIN_PER_DRINK = 12

    data class Drink(val units: Double, val takenAtMs: Long)

    fun bacAt(drinks: List<Drink>, nowMs: Long): Double {
        if (drinks.isEmpty()) return 0.0
        var bac = 0.0
        for (d in drinks) {
            val hours = ((nowMs - d.takenAtMs).coerceAtLeast(0L) / 3_600_000.0)
            val raw = d.units * BAC_PER_STANDARD_DRINK - ELIMINATION_RATE_PER_HOUR * hours
            if (raw > 0.0) bac += raw
        }
        return bac.coerceAtLeast(0.0)
    }

    fun totalUnits(drinks: List<Drink>): Double = drinks.sumOf { it.units }

    fun estimatedSleepDeficitMin(drinks: List<Drink>): Int =
        (totalUnits(drinks) * SLEEP_DEFICIT_MIN_PER_DRINK).toInt()

    /** Hours until BAC returns to zero. */
    fun hoursToSober(drinks: List<Drink>, nowMs: Long): Double {
        val bac = bacAt(drinks, nowMs)
        return if (bac <= 0.0) 0.0 else bac / ELIMINATION_RATE_PER_HOUR
    }

    val PRESETS = listOf(
        Preset("Beer (12oz)", 1.0),
        Preset("Wine (5oz)", 1.0),
        Preset("Cocktail (1.5oz)", 1.0),
        Preset("Double pour", 2.0),
    )

    data class Preset(val label: String, val units: Double)
}
