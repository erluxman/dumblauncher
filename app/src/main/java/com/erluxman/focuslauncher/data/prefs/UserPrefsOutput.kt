package com.erluxman.focuslauncher.data.prefs

import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/** INTEG-008 daily commit count. Entries: "iso|commitCount". */

val UserPrefs.commitLog: Flow<Set<String>>
    get() = store.data.map { it[PrefKeys.COMMIT_LOG] ?: emptySet() }

suspend fun UserPrefs.addCommits(isoDate: String, count: Int) {
    if (count <= 0) return
    store.edit {
        val current = it[PrefKeys.COMMIT_LOG] ?: emptySet()
        val existing = current.firstOrNull { e -> e.startsWith("$isoDate|") }
        val prev = existing?.substringAfter("|")?.toIntOrNull() ?: 0
        val without = current.filterNot { e -> e == existing }.toSet()
        it[PrefKeys.COMMIT_LOG] = without + "$isoDate|${prev + count}"
    }
}

suspend fun UserPrefs.clearCommitLog() {
    store.edit { it.remove(PrefKeys.COMMIT_LOG) }
}
