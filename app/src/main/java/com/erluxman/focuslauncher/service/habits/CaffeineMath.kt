package com.erluxman.focuslauncher.service.habits

import kotlin.math.pow

/**
 * SUB-003 Caffeine Half-Life Graph.
 *
 * Single-compartment exponential decay. The literature half-life ranges
 * 3–7 hours; we use 5h as the default. Caller may override (e.g. heavy
 * users metabolize faster).
 *
 * `remainingMgAt` is the total of all logged doses, each decayed by the
 * time since it was consumed.
 */
object CaffeineMath {

    const val DEFAULT_HALF_LIFE_HOURS = 5.0

    data class Dose(val mg: Int, val takenAtMs: Long)

    fun remainingMg(initialMg: Double, hoursElapsed: Double, halfLifeHours: Double = DEFAULT_HALF_LIFE_HOURS): Double {
        if (initialMg <= 0.0 || hoursElapsed < 0.0 || halfLifeHours <= 0.0) return 0.0
        return initialMg * 0.5.pow(hoursElapsed / halfLifeHours)
    }

    fun remainingMgAt(
        doses: List<Dose>,
        nowMs: Long,
        halfLifeHours: Double = DEFAULT_HALF_LIFE_HOURS,
    ): Double = doses.sumOf {
        val hours = (nowMs - it.takenAtMs).coerceAtLeast(0L) / 3_600_000.0
        remainingMg(it.mg.toDouble(), hours, halfLifeHours)
    }

    /** Decay multiplier for [hoursElapsed]; 1.0 = none yet, 0.5 = one half-life passed. */
    fun decayFraction(hoursElapsed: Double, halfLifeHours: Double = DEFAULT_HALF_LIFE_HOURS): Double {
        if (hoursElapsed <= 0.0) return 1.0
        return 0.5.pow(hoursElapsed / halfLifeHours)
    }

    /** Common pour sizes. Values are widely-cited approximations, not measurements. */
    val PRESETS = listOf(
        Preset("Espresso", 65),
        Preset("Drip coffee (8oz)", 95),
        Preset("Cold brew (12oz)", 200),
        Preset("Black tea", 47),
        Preset("Energy drink", 80),
    )

    data class Preset(val label: String, val mg: Int)
}
