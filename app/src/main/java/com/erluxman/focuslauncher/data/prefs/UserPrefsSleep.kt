package com.erluxman.focuslauncher.data.prefs

import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import com.erluxman.focuslauncher.service.lobby.SleepWindow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/** Per-day sleep minutes log, entries shaped as "yyyy-MM-dd|minutes". */
private val SLEEP_LOG = stringSetPreferencesKey("sleep_log")

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

val UserPrefs.sleepLog: Flow<Set<String>>
    get() = store.data.map { it[SLEEP_LOG] ?: emptySet() }

/** Idempotent: replaces any existing entry for [isoDate]. Minutes ≤ 0 is a no-op. */
suspend fun UserPrefs.logSleepMinutes(isoDate: String, minutes: Int) {
    if (minutes <= 0) return
    store.edit {
        val current = it[SLEEP_LOG] ?: emptySet()
        val others = current.filterNot { e -> e.substringBefore("|") == isoDate }.toSet()
        it[SLEEP_LOG] = others + "$isoDate|$minutes"
    }
}
