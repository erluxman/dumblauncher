package com.erluxman.focuslauncher.data.prefs

import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/** LIFE-009 + LIFE-012 mortality widgets opt-in (default off). */

val UserPrefs.mortalityWidgetsOptIn: Flow<Boolean>
    get() = store.data.map { it[PrefKeys.MORTALITY_WIDGETS_OPT_IN] ?: false }

suspend fun UserPrefs.setMortalityWidgetsOptIn(value: Boolean) {
    store.edit { it[PrefKeys.MORTALITY_WIDGETS_OPT_IN] = value }
}
