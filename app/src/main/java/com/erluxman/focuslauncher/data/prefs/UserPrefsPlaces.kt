package com.erluxman.focuslauncher.data.prefs

import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/** LOC-006 manual travel atlas. Entries: "year|location". */

val UserPrefs.travelAtlas: Flow<Set<String>>
    get() = store.data.map { it[PrefKeys.TRAVEL_ATLAS] ?: emptySet() }

suspend fun UserPrefs.addTravel(year: Int, location: String) {
    val safeLoc = location.replace("|", " ").trim().ifBlank { return }
    if (year !in 1900..2100) return
    store.edit {
        val current = it[PrefKeys.TRAVEL_ATLAS] ?: emptySet()
        it[PrefKeys.TRAVEL_ATLAS] = current + "$year|$safeLoc"
    }
}

suspend fun UserPrefs.removeTravel(entry: String) {
    store.edit {
        val current = it[PrefKeys.TRAVEL_ATLAS] ?: emptySet()
        it[PrefKeys.TRAVEL_ATLAS] = current - entry
    }
}
