package com.erluxman.focuslauncher.service.money

/**
 * FIN-003 Subscription Hunter — manual.
 *
 * Each entry: "name|monthlyUsd". Aggregates monthly cost and renders an
 * annualised number ("paid you 12× this year"). Plaid-driven auto-
 * detection deferred to INTEG-002.
 */
object SubscriptionMath {

    data class Item(val name: String, val monthlyUsd: Double, val raw: String)

    fun parse(entries: Set<String>): List<Item> = entries.mapNotNull { e ->
        val parts = e.split("|", limit = 2)
        if (parts.size != 2) return@mapNotNull null
        val usd = parts[1].toDoubleOrNull() ?: return@mapNotNull null
        Item(parts[0], usd, e)
    }.sortedByDescending { it.monthlyUsd }

    fun totalMonthly(items: List<Item>): Double = items.sumOf { it.monthlyUsd }
    fun totalAnnual(items: List<Item>): Double = totalMonthly(items) * 12.0
}
