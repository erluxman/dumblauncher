package com.erluxman.focuslauncher.service

/**
 * MIND-005 Cognitive Vitals — pure aggregator.
 *
 * Tracks daily 30-sec test results (n-back, reaction time, Stroop)
 * as ISO-keyed scores 0..100. We surface rolling 7-day average, best,
 * and trend (latest vs 7-day-prior).
 */
object CognitiveVitals {

    data class Entry(val isoDate: String, val kind: String, val score: Int)

    fun parse(entries: Set<String>): List<Entry> = entries.mapNotNull { e ->
        val parts = e.split("|", limit = 3)
        if (parts.size != 3) return@mapNotNull null
        val sc = parts[2].toIntOrNull() ?: return@mapNotNull null
        if (sc !in 0..100) return@mapNotNull null
        Entry(parts[0], parts[1], sc)
    }

    fun rollingAverage(daysIso: List<String>, kind: String, entries: List<Entry>): Int {
        val window = entries.filter { it.kind.equals(kind, ignoreCase = true) && it.isoDate in daysIso }
        if (window.isEmpty()) return 0
        return window.map { it.score }.average().toInt()
    }

    fun best(kind: String, entries: List<Entry>): Int =
        entries.filter { it.kind.equals(kind, ignoreCase = true) }
            .maxOfOrNull { it.score } ?: 0

    /** Positive = improving, negative = declining. */
    fun trend(daysIsoNewer: List<String>, daysIsoOlder: List<String>, kind: String, entries: List<Entry>): Int {
        val newer = rollingAverage(daysIsoNewer, kind, entries)
        val older = rollingAverage(daysIsoOlder, kind, entries)
        return newer - older
    }
}
