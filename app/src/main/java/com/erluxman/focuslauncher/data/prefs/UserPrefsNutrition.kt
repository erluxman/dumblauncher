package com.erluxman.focuslauncher.data.prefs

import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * NUT-002 fasting window — single timestamp of the user's last meal.
 * "Auto-detect" in the spec is aspirational (would need plate-photo +
 * vision); this is the bare-minimum manual entry that the engine still
 * benefits from.
 */

private val LAST_MEAL_AT_MS = longPreferencesKey("nut_last_meal_at_ms")

val UserPrefs.lastMealAtMs: Flow<Long>
    get() = store.data.map { it[LAST_MEAL_AT_MS] ?: 0L }

suspend fun UserPrefs.setLastMealNow(nowMs: Long = System.currentTimeMillis()) {
    store.edit { it[LAST_MEAL_AT_MS] = nowMs }
}

suspend fun UserPrefs.clearLastMeal() {
    store.edit { it.remove(LAST_MEAL_AT_MS) }
}
