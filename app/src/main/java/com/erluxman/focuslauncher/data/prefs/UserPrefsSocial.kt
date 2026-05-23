package com.erluxman.focuslauncher.data.prefs

import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/** PRM-003 contacts log. Entries: "yyyy-MM-dd|name|out|in". */
private val CONTACTS_LOG = stringSetPreferencesKey("contacts_log")

/**
 * SOCIAL-013 anti-bio + READ-002 highlights + FIT-003 personal records.
 *
 * Extension properties / functions on UserPrefs so the API stays
 * `prefs.highlights` / `prefs.addHighlight(...)` but the source lives
 * next to its domain.
 */

val UserPrefs.highlights: Flow<Set<String>>
    get() = store.data.map { it[PrefKeys.HIGHLIGHTS] ?: emptySet() }

suspend fun UserPrefs.addHighlight(text: String) {
    val safe = text.trim()
    if (safe.isEmpty()) return
    store.edit {
        val current = it[PrefKeys.HIGHLIGHTS] ?: emptySet()
        it[PrefKeys.HIGHLIGHTS] = current + safe
    }
}

suspend fun UserPrefs.removeHighlight(text: String) {
    store.edit {
        val current = it[PrefKeys.HIGHLIGHTS] ?: emptySet()
        it[PrefKeys.HIGHLIGHTS] = current - text
    }
}

val UserPrefs.antiBio: Flow<String>
    get() = store.data.map { it[PrefKeys.ANTI_BIO].orEmpty() }

suspend fun UserPrefs.setAntiBio(text: String) {
    store.edit { it[PrefKeys.ANTI_BIO] = text.take(280) }
}

val UserPrefs.prWall: Flow<Set<String>>
    get() = store.data.map { it[PrefKeys.PR_WALL] ?: emptySet() }

suspend fun UserPrefs.addPersonalRecord(isoDate: String, label: String, value: String, unit: String) {
    val safeLabel = label.replace("|", " ").trim().ifBlank { return }
    val safeValue = value.replace("|", " ").trim().ifBlank { return }
    val safeUnit = unit.replace("|", " ").trim()
    store.edit {
        val current = it[PrefKeys.PR_WALL] ?: emptySet()
        it[PrefKeys.PR_WALL] = current + "$isoDate|$safeLabel|$safeValue|$safeUnit"
    }
}

suspend fun UserPrefs.removePersonalRecord(entry: String) {
    store.edit {
        val current = it[PrefKeys.PR_WALL] ?: emptySet()
        it[PrefKeys.PR_WALL] = current - entry
    }
}

val UserPrefs.contactsLog: Flow<Set<String>>
    get() = store.data.map { it[CONTACTS_LOG] ?: emptySet() }

/** [direction] is "out" (you initiated) or "in" (they did). */
suspend fun UserPrefs.logContactTouch(isoDate: String, name: String, direction: String) {
    val safeName = name.replace("|", " ").trim().ifBlank { return }
    val d = when (direction.lowercase()) {
        "out", "outbound", "you" -> "out"
        "in", "inbound", "them" -> "in"
        else -> return
    }
    store.edit {
        val current = it[CONTACTS_LOG] ?: emptySet()
        it[CONTACTS_LOG] = current + "$isoDate|$safeName|$d"
    }
}

suspend fun UserPrefs.removeContactTouch(entry: String) {
    store.edit {
        val current = it[CONTACTS_LOG] ?: emptySet()
        it[CONTACTS_LOG] = current - entry
    }
}
