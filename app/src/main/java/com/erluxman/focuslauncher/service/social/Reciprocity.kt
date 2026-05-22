package com.erluxman.focuslauncher.service.social

/**
 * PRM-003 Reciprocity Score — pure aggregator.
 *
 * Each touch: "iso|name|direction" where direction is "out" (you
 * initiated) or "in" (they initiated). We compute, per name, the
 * percentage you initiated. > 60 means you're carrying the relation;
 * < 40 means they are.
 */
object Reciprocity {

    data class Touch(val isoDate: String, val name: String, val outbound: Boolean)

    fun parse(entries: Set<String>): List<Touch> = entries.mapNotNull { e ->
        val parts = e.split("|", limit = 3)
        if (parts.size != 3) return@mapNotNull null
        val outbound = when (parts[2].lowercase()) {
            "out", "outbound", "you" -> true
            "in", "inbound", "them" -> false
            else -> return@mapNotNull null
        }
        if (parts[1].isBlank()) return@mapNotNull null
        Touch(parts[0], parts[1].trim(), outbound)
    }

    fun outboundPct(name: String, touches: List<Touch>): Int {
        val matching = touches.filter { it.name.equals(name, ignoreCase = true) }
        if (matching.isEmpty()) return 0
        val out = matching.count { it.outbound }
        return (out.toDouble() / matching.size * 100.0).toInt()
    }

    fun lopsidedRelationships(touches: List<Touch>, threshold: Int = 65): List<String> {
        val byName = touches.groupBy { it.name.lowercase() }
        return byName.values.mapNotNull { list ->
            val pct = outboundPct(list.first().name, list)
            if (list.size >= 5 && (pct >= threshold || pct <= (100 - threshold))) list.first().name else null
        }
    }
}
