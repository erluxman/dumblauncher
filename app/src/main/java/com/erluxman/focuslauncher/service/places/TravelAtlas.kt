package com.erluxman.focuslauncher.service.places

/**
 * LOC-006 Travel Atlas — manual entries.
 *
 * Each stored as "year|location" (year is 4-digit; location free text).
 * GPS-driven location heatmap (LOC-001) is parked behind background
 * location permission consent flow.
 */
object TravelAtlas {

    data class Visit(val year: Int, val location: String, val raw: String)

    fun parse(entries: Set<String>): List<Visit> = entries.mapNotNull { e ->
        val parts = e.split("|", limit = 2)
        if (parts.size != 2) return@mapNotNull null
        val y = parts[0].toIntOrNull() ?: return@mapNotNull null
        Visit(y, parts[1], e)
    }.sortedWith(compareByDescending<Visit> { it.year }.thenBy { it.location })

    fun distinctLocations(visits: List<Visit>): Int =
        visits.map { it.location.trim().lowercase() }.toSet().size

    fun yearsCovered(visits: List<Visit>): Int =
        visits.map { it.year }.toSet().size
}
