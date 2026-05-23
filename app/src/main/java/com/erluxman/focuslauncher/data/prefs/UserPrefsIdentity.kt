package com.erluxman.focuslauncher.data.prefs

import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * IDENT-003 rejection counter, IDENT-004 risks-taken log, IDENT-005
 * things-made counter. All three are "log a moment with a one-line
 * note" — stored as `"timestampMs|text"` per entry.
 */

private val REJECTIONS = stringSetPreferencesKey("identity_rejections")
private val RISKS = stringSetPreferencesKey("identity_risks")
private val THINGS_MADE = stringSetPreferencesKey("identity_things_made")

val UserPrefs.rejections: Flow<Set<String>>
    get() = store.data.map { it[REJECTIONS] ?: emptySet() }

val UserPrefs.risks: Flow<Set<String>>
    get() = store.data.map { it[RISKS] ?: emptySet() }

val UserPrefs.thingsMade: Flow<Set<String>>
    get() = store.data.map { it[THINGS_MADE] ?: emptySet() }

suspend fun UserPrefs.logRejection(text: String, nowMs: Long = System.currentTimeMillis()) {
    val safe = text.replace("|", " ").trim().ifBlank { return }
    store.edit {
        val cur = it[REJECTIONS] ?: emptySet()
        it[REJECTIONS] = cur + "$nowMs|$safe"
    }
}

suspend fun UserPrefs.logRisk(text: String, nowMs: Long = System.currentTimeMillis()) {
    val safe = text.replace("|", " ").trim().ifBlank { return }
    store.edit {
        val cur = it[RISKS] ?: emptySet()
        it[RISKS] = cur + "$nowMs|$safe"
    }
}

suspend fun UserPrefs.logThingMade(text: String, nowMs: Long = System.currentTimeMillis()) {
    val safe = text.replace("|", " ").trim().ifBlank { return }
    store.edit {
        val cur = it[THINGS_MADE] ?: emptySet()
        it[THINGS_MADE] = cur + "$nowMs|$safe"
    }
}

suspend fun UserPrefs.removeIdentityEntry(category: IdentityCategory, entry: String) {
    val key = when (category) {
        IdentityCategory.REJECTION -> REJECTIONS
        IdentityCategory.RISK -> RISKS
        IdentityCategory.THING_MADE -> THINGS_MADE
    }
    store.edit { it[key] = (it[key] ?: emptySet()) - entry }
}

enum class IdentityCategory { REJECTION, RISK, THING_MADE }
