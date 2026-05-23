package com.erluxman.focuslauncher.data.prefs

import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * LOC-003 third-place tracker. Each entry is "timestampMs|placeName|minutes".
 * Lives in its own file so `UserPrefsPlaces.kt` keeps the lifetime travel
 * atlas intent unambiguous.
 */

private val THIRD_PLACES = stringSetPreferencesKey("third_places")

val UserPrefs.thirdPlaces: Flow<Set<String>>
    get() = store.data.map { it[THIRD_PLACES] ?: emptySet() }

suspend fun UserPrefs.logThirdPlace(name: String, minutes: Int, nowMs: Long = System.currentTimeMillis()) {
    val safe = name.replace("|", " ").trim().ifBlank { return }
    if (minutes <= 0) return
    store.edit {
        val cur = it[THIRD_PLACES] ?: emptySet()
        it[THIRD_PLACES] = cur + "$nowMs|$safe|$minutes"
    }
}

suspend fun UserPrefs.removeThirdPlace(entry: String) {
    store.edit { it[THIRD_PLACES] = (it[THIRD_PLACES] ?: emptySet()) - entry }
}
