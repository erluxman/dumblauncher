package com.erluxman.focuslauncher.data.prefs

import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/** FIN-008 regret-receipts log. Entries: "timestampMs|amount|category|name|rating". */
private val REGRETS = stringSetPreferencesKey("regrets")

/**
 * FIN-002 net worth + FIN-003 subscriptions + FIN-004 savings-rate
 * snapshot. All whole USD.
 */

val UserPrefs.subscriptions: Flow<Set<String>>
    get() = store.data.map { it[PrefKeys.SUBSCRIPTIONS] ?: emptySet() }

suspend fun UserPrefs.addSubscription(name: String, monthlyUsd: Double) {
    val safe = name.replace("|", " ").trim().ifBlank { return }
    if (monthlyUsd <= 0) return
    store.edit {
        val current = it[PrefKeys.SUBSCRIPTIONS] ?: emptySet()
        // Replace existing by name.
        val others = current.filterNot { e -> e.substringBefore("|") == safe }.toSet()
        it[PrefKeys.SUBSCRIPTIONS] = others + "$safe|$monthlyUsd"
    }
}

suspend fun UserPrefs.removeSubscription(entry: String) {
    store.edit {
        val current = it[PrefKeys.SUBSCRIPTIONS] ?: emptySet()
        it[PrefKeys.SUBSCRIPTIONS] = current - entry
    }
}

val UserPrefs.moneyIncome: Flow<Int>
    get() = store.data.map { it[PrefKeys.MONEY_INCOME] ?: 0 }
val UserPrefs.moneyExpense: Flow<Int>
    get() = store.data.map { it[PrefKeys.MONEY_EXPENSE] ?: 0 }
val UserPrefs.moneyAssets: Flow<Int>
    get() = store.data.map { it[PrefKeys.MONEY_ASSETS] ?: 0 }
val UserPrefs.moneyLiabilities: Flow<Int>
    get() = store.data.map { it[PrefKeys.MONEY_LIABILITIES] ?: 0 }

suspend fun UserPrefs.setMoneyIncome(v: Int) { store.edit { it[PrefKeys.MONEY_INCOME] = v.coerceAtLeast(0) } }
suspend fun UserPrefs.setMoneyExpense(v: Int) { store.edit { it[PrefKeys.MONEY_EXPENSE] = v.coerceAtLeast(0) } }
suspend fun UserPrefs.setMoneyAssets(v: Int) { store.edit { it[PrefKeys.MONEY_ASSETS] = v.coerceAtLeast(0) } }
suspend fun UserPrefs.setMoneyLiabilities(v: Int) { store.edit { it[PrefKeys.MONEY_LIABILITIES] = v.coerceAtLeast(0) } }

val UserPrefs.regrets: Flow<Set<String>>
    get() = store.data.map { it[REGRETS] ?: emptySet() }

suspend fun UserPrefs.addRegret(
    name: String,
    amountUsd: Double,
    category: String,
    nowMs: Long = System.currentTimeMillis(),
) {
    val safeName = name.replace("|", " ").trim().ifBlank { return }
    if (amountUsd <= 0) return
    val safeCat = category.replace("|", " ").trim()
    store.edit {
        val cur = it[REGRETS] ?: emptySet()
        it[REGRETS] = cur + "$nowMs|$amountUsd|$safeCat|$safeName|?"
    }
}

/** [rating] must be "glad" or "regret". */
suspend fun UserPrefs.markRegretRating(entry: String, rating: String) {
    if (rating != "glad" && rating != "regret") return
    store.edit {
        val cur = it[REGRETS] ?: emptySet()
        val parts = entry.split("|", limit = 5)
        if (parts.size < 5) return@edit
        val updated = "${parts[0]}|${parts[1]}|${parts[2]}|${parts[3]}|$rating"
        it[REGRETS] = (cur - entry) + updated
    }
}
