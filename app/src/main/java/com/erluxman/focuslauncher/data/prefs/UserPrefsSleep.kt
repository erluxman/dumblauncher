package com.erluxman.focuslauncher.data.prefs

import androidx.datastore.preferences.core.edit
import com.erluxman.focuslauncher.service.lobby.SleepWindow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/** SLEEP-003 sleep window guardrails. Hours stored as 0..23 ints. */

val UserPrefs.sleepCutoffHour: Flow<Int>
    get() = store.data.map {
        (it[PrefKeys.SLEEP_CUTOFF_HOUR] ?: SleepWindow.DEFAULT_CUTOFF_HOUR).coerceIn(0, 23)
    }

val UserPrefs.sleepWakeHour: Flow<Int>
    get() = store.data.map {
        (it[PrefKeys.SLEEP_WAKE_HOUR] ?: SleepWindow.DEFAULT_WAKE_HOUR).coerceIn(0, 23)
    }

suspend fun UserPrefs.setSleepCutoffHour(h: Int) {
    store.edit { it[PrefKeys.SLEEP_CUTOFF_HOUR] = h.coerceIn(0, 23) }
}

suspend fun UserPrefs.setSleepWakeHour(h: Int) {
    store.edit { it[PrefKeys.SLEEP_WAKE_HOUR] = h.coerceIn(0, 23) }
}
