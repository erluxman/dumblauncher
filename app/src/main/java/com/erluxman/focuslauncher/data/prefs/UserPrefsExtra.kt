package com.erluxman.focuslauncher.data.prefs

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
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
private val PHANTOM_CHECKS = stringSetPreferencesKey("phantom_checks")  // "yyyy-MM-dd|count"
private val ABSURD_KAZOO = booleanPreferencesKey("absurd_kazoo")
private val ABSURD_NARRATOR = booleanPreferencesKey("absurd_narrator")
private val ABSURD_VOICE_GATE = booleanPreferencesKey("absurd_voice_gate")
private val TAX_INCOMES = stringSetPreferencesKey("tax_incomes")  // "timestampMs|source|amount"

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

// PSYCH-011 phantom-vibration counter — per-day int.
val UserPrefs.phantomChecks: Flow<Set<String>>
    get() = store.data.map { it[PHANTOM_CHECKS] ?: emptySet() }

suspend fun UserPrefs.bumpPhantomCheck(isoDate: String, delta: Int = 1) {
    store.edit {
        val cur = it[PHANTOM_CHECKS] ?: emptySet()
        val sameDay = cur.firstOrNull { e -> e.substringBefore("|") == isoDate }
        val n = (sameDay?.substringAfter("|")?.toIntOrNull() ?: 0) + delta
        val pruned = if (sameDay != null) cur - sameDay else cur
        it[PHANTOM_CHECKS] = if (n > 0) pruned + "$isoDate|$n" else pruned
    }
}

// ABSURD-001/002/004 — three on-device toggles. False by default so the
// user has to choose to suffer.
val UserPrefs.absurdKazoo: Flow<Boolean>
    get() = store.data.map { it[ABSURD_KAZOO] ?: false }
val UserPrefs.absurdNarrator: Flow<Boolean>
    get() = store.data.map { it[ABSURD_NARRATOR] ?: false }
val UserPrefs.absurdVoiceGate: Flow<Boolean>
    get() = store.data.map { it[ABSURD_VOICE_GATE] ?: false }

suspend fun UserPrefs.setAbsurdKazoo(v: Boolean) { store.edit { it[ABSURD_KAZOO] = v } }
suspend fun UserPrefs.setAbsurdNarrator(v: Boolean) { store.edit { it[ABSURD_NARRATOR] = v } }
suspend fun UserPrefs.setAbsurdVoiceGate(v: Boolean) { store.edit { it[ABSURD_VOICE_GATE] = v } }

// FIN-010 tax-aware income tracker. "timestampMs|source|amount".
val UserPrefs.taxIncomes: Flow<Set<String>>
    get() = store.data.map { it[TAX_INCOMES] ?: emptySet() }

suspend fun UserPrefs.logTaxIncome(source: String, amountUsd: Double, nowMs: Long = System.currentTimeMillis()) {
    val safe = source.replace("|", " ").trim().ifBlank { return }
    if (amountUsd <= 0) return
    store.edit {
        val cur = it[TAX_INCOMES] ?: emptySet()
        it[TAX_INCOMES] = cur + "$nowMs|$safe|$amountUsd"
    }
}

suspend fun UserPrefs.removeTaxIncome(entry: String) {
    store.edit { it[TAX_INCOMES] = (it[TAX_INCOMES] ?: emptySet()) - entry }
}

// RESTRICT-022 — single active "life-state" mode (commute/vacation/sick/…)
private val LIFE_STATE_MODE = stringPreferencesKey("life_state_mode")

val UserPrefs.lifeStateMode: Flow<String>
    get() = store.data.map { it[LIFE_STATE_MODE].orEmpty() }

suspend fun UserPrefs.setLifeStateMode(mode: String) {
    store.edit { it[LIFE_STATE_MODE] = mode }
}

// RESTRICT-013 App roulette — pool of up to 3, one allowed per day.
private val ROULETTE_POOL = stringSetPreferencesKey("roulette_pool")
private val ROULETTE_PICK_DATE = stringPreferencesKey("roulette_pick_date")
private val ROULETTE_PICK_APP = stringPreferencesKey("roulette_pick_app")

val UserPrefs.roulettePool: Flow<Set<String>>
    get() = store.data.map { it[ROULETTE_POOL] ?: emptySet() }
val UserPrefs.roulettePickDate: Flow<String>
    get() = store.data.map { it[ROULETTE_PICK_DATE].orEmpty() }
val UserPrefs.roulettePickApp: Flow<String>
    get() = store.data.map { it[ROULETTE_PICK_APP].orEmpty() }

suspend fun UserPrefs.addRouletteApp(name: String) {
    val safe = name.trim().ifBlank { return }
    store.edit {
        val cur = it[ROULETTE_POOL] ?: emptySet()
        if (cur.size >= 3 || safe in cur) return@edit
        it[ROULETTE_POOL] = cur + safe
    }
}

suspend fun UserPrefs.removeRouletteApp(name: String) {
    store.edit { it[ROULETTE_POOL] = (it[ROULETTE_POOL] ?: emptySet()) - name }
}

suspend fun UserPrefs.spinRoulette(isoDate: String, seed: Long = System.currentTimeMillis()) {
    store.edit {
        val pool = (it[ROULETTE_POOL] ?: emptySet()).toList()
        if (pool.isEmpty()) return@edit
        val idx = ((seed.toInt() % pool.size) + pool.size) % pool.size
        it[ROULETTE_PICK_DATE] = isoDate
        it[ROULETTE_PICK_APP] = pool[idx]
    }
}

// SOCIAL-040 Public Phone Funeral — record which tombstones we've held a funeral for.
private val FUNERAL_LABELS = stringSetPreferencesKey("funeral_labels")

val UserPrefs.funeralLabels: Flow<Set<String>>
    get() = store.data.map { it[FUNERAL_LABELS] ?: emptySet() }

suspend fun UserPrefs.holdFuneralFor(label: String) {
    val safe = label.trim().ifBlank { return }
    store.edit {
        val cur = it[FUNERAL_LABELS] ?: emptySet()
        it[FUNERAL_LABELS] = cur + safe
    }
}
