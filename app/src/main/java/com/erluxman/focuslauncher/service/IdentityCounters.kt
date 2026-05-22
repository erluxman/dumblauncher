package com.erluxman.focuslauncher.service

/**
 * Three small identity counters that share a parse/count shape:
 *
 *   IDENT-003 Rejection Counter — asks made, accepted, rejected.
 *   IDENT-004 Risks-Taken Log — uncomfortable actions tagged end-of-day.
 *   IDENT-005 Things-Made Counter — photos/posts/recipes/songs made.
 *
 * Each entry: "iso|outcome|note". `outcome` is free text per counter
 * type ("accepted" / "rejected" for rejections, "risk" for risks,
 * "made" for things-made). Aggregator just buckets by outcome.
 */
object IdentityCounters {

    data class Entry(val isoDate: String, val outcome: String, val note: String, val raw: String)

    fun parse(entries: Set<String>): List<Entry> = entries.mapNotNull { e ->
        val parts = e.split("|", limit = 3)
        if (parts.size != 3) return@mapNotNull null
        Entry(parts[0], parts[1].lowercase(), parts[2], e)
    }

    fun count(outcome: String, entries: List<Entry>): Int =
        entries.count { it.outcome.equals(outcome, ignoreCase = true) }

    fun countOnDate(outcome: String, dateIso: String, entries: List<Entry>): Int =
        entries.count { it.isoDate == dateIso && it.outcome.equals(outcome, ignoreCase = true) }

    fun acceptanceRate(rejections: List<Entry>): Int {
        val asks = rejections.count { it.outcome.equals("accepted", true) || it.outcome.equals("rejected", true) }
        if (asks == 0) return 0
        val accepted = rejections.count { it.outcome.equals("accepted", true) }
        return (accepted.toDouble() / asks * 100.0).toInt()
    }
}
