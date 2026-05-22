package com.erluxman.focuslauncher.data.prefs

import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Daily-habit logs: caffeine (SUB-003), alcohol (SUB-001), meditation
 * (MIND-001), reading (READ-001), idea parking lot (PROD-009).
 *
 * All entries share an "iso|value..." shape so aggregation in the
 * service layer is symmetric across domains.
 */

val UserPrefs.caffeineDoses: Flow<Set<String>>
    get() = store.data.map { it[PrefKeys.CAFFEINE_DOSES] ?: emptySet() }

/** Append a dose and prune anything older than 24h. */
suspend fun UserPrefs.logCaffeine(mg: Int, nowMs: Long = System.currentTimeMillis()) {
    if (mg <= 0) return
    store.edit {
        val cutoff = nowMs - 24L * 3_600_000L
        val current = it[PrefKeys.CAFFEINE_DOSES] ?: emptySet()
        val pruned = current.filter { entry ->
            entry.substringBefore("|").toLongOrNull()?.let { ts -> ts >= cutoff } ?: false
        }.toSet()
        it[PrefKeys.CAFFEINE_DOSES] = pruned + "$nowMs|$mg"
    }
}

suspend fun UserPrefs.clearCaffeineLog() {
    store.edit { it.remove(PrefKeys.CAFFEINE_DOSES) }
}

val UserPrefs.drinkLog: Flow<Set<String>>
    get() = store.data.map { it[PrefKeys.DRINK_LOG] ?: emptySet() }

suspend fun UserPrefs.logDrink(units: Double, nowMs: Long = System.currentTimeMillis()) {
    if (units <= 0.0) return
    store.edit {
        val cutoff = nowMs - 24L * 3_600_000L
        val pruned = (it[PrefKeys.DRINK_LOG] ?: emptySet()).filter { entry ->
            entry.substringBefore("|").toLongOrNull()?.let { ts -> ts >= cutoff } ?: false
        }.toSet()
        it[PrefKeys.DRINK_LOG] = pruned + "$nowMs|$units"
    }
}

suspend fun UserPrefs.clearDrinkLog() {
    store.edit { it.remove(PrefKeys.DRINK_LOG) }
}

val UserPrefs.meditationLog: Flow<Set<String>>
    get() = store.data.map { it[PrefKeys.MEDITATION_LOG] ?: emptySet() }

suspend fun UserPrefs.logMeditation(isoDate: String, minutes: Int, technique: String) {
    if (minutes <= 0) return
    val safeTech = technique.replace("|", " ").ifBlank { "Breath" }
    store.edit {
        val current = it[PrefKeys.MEDITATION_LOG] ?: emptySet()
        it[PrefKeys.MEDITATION_LOG] = current + "$isoDate|$minutes|$safeTech"
    }
}

suspend fun UserPrefs.clearMeditationLog() {
    store.edit { it.remove(PrefKeys.MEDITATION_LOG) }
}

val UserPrefs.ideaParking: Flow<Set<String>>
    get() = store.data.map { it[PrefKeys.IDEA_PARKING] ?: emptySet() }

suspend fun UserPrefs.addParkedIdea(text: String, nowMs: Long = System.currentTimeMillis()) {
    val safe = text.trim().replace("|", " ")
    if (safe.isEmpty()) return
    store.edit {
        val current = it[PrefKeys.IDEA_PARKING] ?: emptySet()
        it[PrefKeys.IDEA_PARKING] = current + "$nowMs|$safe"
    }
}

suspend fun UserPrefs.removeParkedIdea(entry: String) {
    store.edit {
        val current = it[PrefKeys.IDEA_PARKING] ?: emptySet()
        it[PrefKeys.IDEA_PARKING] = current - entry
    }
}

suspend fun UserPrefs.clearParkedIdeas() {
    store.edit { it.remove(PrefKeys.IDEA_PARKING) }
}

val UserPrefs.readingLog: Flow<Set<String>>
    get() = store.data.map { it[PrefKeys.READING_LOG] ?: emptySet() }

suspend fun UserPrefs.logReading(isoDate: String, minutes: Int) {
    if (minutes <= 0) return
    store.edit {
        val current = it[PrefKeys.READING_LOG] ?: emptySet()
        it[PrefKeys.READING_LOG] = current + "$isoDate|$minutes"
    }
}

suspend fun UserPrefs.clearReadingLog() {
    store.edit { it.remove(PrefKeys.READING_LOG) }
}
