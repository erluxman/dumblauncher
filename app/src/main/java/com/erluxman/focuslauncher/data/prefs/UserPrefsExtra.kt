package com.erluxman.focuslauncher.data.prefs

import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Catch-all for stage 3/4 single-purpose pref additions that don't yet
 * justify their own domain file.
 *
 * - LOC-005 outdoor minutes per ISO day
 * - SUB-002 substance log (private; "timestampMs|substance|amount")
 * - MIND-006 dream journal text entries
 */

private val OUTDOOR_LOG = stringSetPreferencesKey("outdoor_log")  // "yyyy-MM-dd|minutes"
private val SUBSTANCE_LOG = stringSetPreferencesKey("substance_log")  // "timestampMs|substance|amount"
private val DREAM_JOURNAL = stringSetPreferencesKey("dream_journal")  // "timestampMs|text"

val UserPrefs.outdoorLog: Flow<Set<String>>
    get() = store.data.map { it[OUTDOOR_LOG] ?: emptySet() }

suspend fun UserPrefs.logOutdoor(isoDate: String, minutes: Int) {
    if (minutes <= 0) return
    store.edit {
        val cur = it[OUTDOOR_LOG] ?: emptySet()
        val others = cur.filterNot { e -> e.substringBefore("|") == isoDate }.toSet()
        it[OUTDOOR_LOG] = others + "$isoDate|$minutes"
    }
}

val UserPrefs.substanceLog: Flow<Set<String>>
    get() = store.data.map { it[SUBSTANCE_LOG] ?: emptySet() }

suspend fun UserPrefs.logSubstance(substance: String, amount: String, nowMs: Long = System.currentTimeMillis()) {
    val s = substance.replace("|", " ").trim().ifBlank { return }
    val a = amount.replace("|", " ").trim()
    store.edit {
        val cur = it[SUBSTANCE_LOG] ?: emptySet()
        it[SUBSTANCE_LOG] = cur + "$nowMs|$s|$a"
    }
}

suspend fun UserPrefs.removeSubstanceEntry(entry: String) {
    store.edit { it[SUBSTANCE_LOG] = (it[SUBSTANCE_LOG] ?: emptySet()) - entry }
}

val UserPrefs.dreamJournal: Flow<Set<String>>
    get() = store.data.map { it[DREAM_JOURNAL] ?: emptySet() }

suspend fun UserPrefs.addDreamEntry(text: String, nowMs: Long = System.currentTimeMillis()) {
    val safe = text.replace("|", " ").trim().ifBlank { return }
    store.edit {
        val cur = it[DREAM_JOURNAL] ?: emptySet()
        it[DREAM_JOURNAL] = cur + "$nowMs|$safe"
    }
}

suspend fun UserPrefs.removeDreamEntry(entry: String) {
    store.edit { it[DREAM_JOURNAL] = (it[DREAM_JOURNAL] ?: emptySet()) - entry }
}
