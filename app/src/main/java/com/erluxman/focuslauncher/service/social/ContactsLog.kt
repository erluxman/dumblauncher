package com.erluxman.focuslauncher.service.social

/**
 * PRM-002 Last-Contacted List — pure aggregator.
 *
 * Each touch entry: "iso|name". Caller is responsible for inserting
 * entries when a call/text happens. The bluetooth-proximity auto-
 * detect path (PRM-001) is parked behind permission consent.
 */
object ContactsLog {

    data class Touch(val isoDate: String, val name: String)

    fun parse(entries: Set<String>): List<Touch> = entries.mapNotNull { e ->
        val parts = e.split("|", limit = 2)
        if (parts.size != 2) return@mapNotNull null
        if (parts[1].isBlank()) return@mapNotNull null
        Touch(parts[0], parts[1].trim())
    }

    fun lastSeen(name: String, touches: List<Touch>): String? =
        touches.filter { it.name.equals(name, ignoreCase = true) }
            .maxByOrNull { it.isoDate }
            ?.isoDate

    /**
     * Returns names that haven't been touched in [staleDays] days,
     * sorted by longest-since-contact first.
     */
    fun staleContacts(touches: List<Touch>, todayIso: String, staleDays: Int = 30): List<String> {
        val cutoff = shiftIso(todayIso, -staleDays) ?: return emptyList()
        // Latest per name
        val latest = touches.groupBy { it.name.lowercase() }
            .mapValues { (_, list) -> list.maxBy { it.isoDate } }
        return latest.values.filter { it.isoDate < cutoff }
            .sortedBy { it.isoDate }
            .map { it.name }
    }

    private fun shiftIso(iso: String, deltaDays: Int): String? {
        val fmt = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.US)
        val d = fmt.parse(iso) ?: return null
        val cal = java.util.Calendar.getInstance().apply { time = d; add(java.util.Calendar.DAY_OF_MONTH, deltaDays) }
        return fmt.format(cal.time)
    }
}
