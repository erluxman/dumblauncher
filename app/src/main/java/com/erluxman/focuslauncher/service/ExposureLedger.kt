package com.erluxman.focuslauncher.service

/**
 * LOC-008 Air-Quality + UV Exposure Ledger.
 *
 * Caller logs samples: "isoMin|pm25|uv". We accumulate per-day +
 * lifetime totals. Pure-fn so the eventual sensor controller stays
 * thin.
 */
object ExposureLedger {

    data class Sample(val isoDate: String, val pm25: Double, val uv: Double)

    fun parse(entries: Set<String>): List<Sample> = entries.mapNotNull { e ->
        val parts = e.split("|", limit = 3)
        if (parts.size != 3) return@mapNotNull null
        val pm = parts[1].toDoubleOrNull() ?: return@mapNotNull null
        val uv = parts[2].toDoubleOrNull() ?: return@mapNotNull null
        if (pm < 0 || uv < 0) return@mapNotNull null
        Sample(parts[0], pm, uv)
    }

    fun pm25OnDate(dateIso: String, samples: List<Sample>): Double =
        samples.filter { it.isoDate == dateIso }.sumOf { it.pm25 }

    fun lifetimePm25(samples: List<Sample>): Double = samples.sumOf { it.pm25 }

    fun lifetimeUv(samples: List<Sample>): Double = samples.sumOf { it.uv }
}
