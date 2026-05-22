package com.erluxman.focuslauncher.service.money

/**
 * FIN-008 Regret Receipts — pure aggregator.
 *
 * Each rated purchase is a triple "category|usd|rating1to5". Ratings ≤
 * 2 count as regretted; we compute regret-rate per category so the
 * Money Mirror can surface "Eating out: $X / 47% regretted".
 */
object RegretReceipts {

    data class Receipt(val category: String, val usd: Double, val rating: Int)

    fun parse(entries: Set<String>): List<Receipt> = entries.mapNotNull { e ->
        val parts = e.split("|", limit = 3)
        if (parts.size != 3) return@mapNotNull null
        val usd = parts[1].toDoubleOrNull() ?: return@mapNotNull null
        val r = parts[2].toIntOrNull() ?: return@mapNotNull null
        if (r !in 1..5) return@mapNotNull null
        Receipt(parts[0].trim(), usd, r)
    }

    fun regretPctFor(category: String, receipts: List<Receipt>): Int {
        val matching = receipts.filter { it.category.equals(category, ignoreCase = true) }
        if (matching.isEmpty()) return 0
        val regretted = matching.count { it.rating <= 2 }
        return (regretted.toDouble() / matching.size * 100.0).toInt()
    }

    fun totalUsd(category: String, receipts: List<Receipt>): Double =
        receipts.filter { it.category.equals(category, ignoreCase = true) }.sumOf { it.usd }
}
