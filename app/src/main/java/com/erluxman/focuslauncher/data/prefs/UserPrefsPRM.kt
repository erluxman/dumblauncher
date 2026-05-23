package com.erluxman.focuslauncher.data.prefs

import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * PRM-002 last-contacted log. Entries: "timestampMs|name". Newest wins.
 * (NUT-004 cooking ratio also lives here as two small counters.)
 */

private val LAST_CONTACTED = stringSetPreferencesKey("last_contacted")
private val MEALS_COOKED = stringSetPreferencesKey("meals_cooked")  // "isoDate|count"
private val MEALS_EATEN_OUT = stringSetPreferencesKey("meals_eaten_out")  // "isoDate|count"

val UserPrefs.lastContactedLog: Flow<Set<String>>
    get() = store.data.map { it[LAST_CONTACTED] ?: emptySet() }

suspend fun UserPrefs.logContacted(name: String, nowMs: Long = System.currentTimeMillis()) {
    val safe = name.replace("|", " ").trim().ifBlank { return }
    store.edit {
        val cur = it[LAST_CONTACTED] ?: emptySet()
        // Keep at most one entry per name (newest wins).
        val others = cur.filterNot { it.substringAfter("|").equals(safe, ignoreCase = true) }.toSet()
        it[LAST_CONTACTED] = others + "$nowMs|$safe"
    }
}

suspend fun UserPrefs.removeContacted(entry: String) {
    store.edit { it[LAST_CONTACTED] = (it[LAST_CONTACTED] ?: emptySet()) - entry }
}

val UserPrefs.mealsCooked: Flow<Set<String>>
    get() = store.data.map { it[MEALS_COOKED] ?: emptySet() }

val UserPrefs.mealsEatenOut: Flow<Set<String>>
    get() = store.data.map { it[MEALS_EATEN_OUT] ?: emptySet() }

private suspend fun UserPrefs.bumpMealCounter(key: androidx.datastore.preferences.core.Preferences.Key<Set<String>>, isoDate: String, delta: Int) {
    store.edit {
        val cur = it[key] ?: emptySet()
        val sameDay = cur.firstOrNull { e -> e.substringBefore("|") == isoDate }
        val n = (sameDay?.substringAfter("|")?.toIntOrNull() ?: 0) + delta
        val pruned = if (sameDay != null) cur - sameDay else cur
        it[key] = if (n > 0) pruned + "$isoDate|$n" else pruned
    }
}

suspend fun UserPrefs.bumpCooked(isoDate: String, delta: Int = 1) = bumpMealCounter(MEALS_COOKED, isoDate, delta)
suspend fun UserPrefs.bumpEatenOut(isoDate: String, delta: Int = 1) = bumpMealCounter(MEALS_EATEN_OUT, isoDate, delta)
