package com.erluxman.focuslauncher.service.insights

object ParkedIdea {
    data class Item(val timestampMs: Long, val text: String, val raw: String)

    fun parse(entries: Set<String>): List<Item> = entries.mapNotNull { e ->
        val parts = e.split("|", limit = 2)
        if (parts.size != 2) return@mapNotNull null
        val ts = parts[0].toLongOrNull() ?: return@mapNotNull null
        Item(ts, parts[1], e)
    }.sortedByDescending { it.timestampMs }
}
