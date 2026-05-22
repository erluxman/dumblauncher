package com.erluxman.focuslauncher.data.prefs

import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/** FIT-002 workout log. Entries: "iso|minutes|kind". */

val UserPrefs.workoutLog: Flow<Set<String>>
    get() = store.data.map { it[PrefKeys.WORKOUT_LOG] ?: emptySet() }

suspend fun UserPrefs.logWorkout(isoDate: String, minutes: Int, kind: String) {
    if (minutes <= 0) return
    val safe = kind.replace("|", " ").ifBlank { "Strength" }
    store.edit {
        val current = it[PrefKeys.WORKOUT_LOG] ?: emptySet()
        it[PrefKeys.WORKOUT_LOG] = current + "$isoDate|$minutes|$safe"
    }
}

suspend fun UserPrefs.clearWorkoutLog() {
    store.edit { it.remove(PrefKeys.WORKOUT_LOG) }
}
