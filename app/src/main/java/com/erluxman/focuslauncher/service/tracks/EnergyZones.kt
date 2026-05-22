package com.erluxman.focuslauncher.service.tracks

/**
 * Pure logic for PROD-014 Energy Zones.
 *
 * The day is split into 6 four-hour windows; each can hold a HIGH / MED / LOW
 * label set by the user. Given the current hour and the stored labels, return
 * the active zone and a suggested activity.
 */
object EnergyZones {

    val WINDOW_LABELS = listOf("0-4", "4-8", "8-12", "12-16", "16-20", "20-24")
    enum class Energy { HIGH, MED, LOW, UNSET }

    fun windowIndex(hour: Int): Int = (hour.coerceIn(0, 23)) / 4

    fun activeEnergy(hour: Int, stored: Set<String>): Energy {
        val key = WINDOW_LABELS[windowIndex(hour)]
        val entry = stored.firstOrNull { it.startsWith("$key|") } ?: return Energy.UNSET
        return runCatching { Energy.valueOf(entry.substringAfter("|")) }.getOrDefault(Energy.UNSET)
    }

    fun suggestion(energy: Energy): String = when (energy) {
        Energy.HIGH -> "Deep work. Hardest task first."
        Energy.MED -> "Admin or shallow work. Reply to messages."
        Energy.LOW -> "Rest. Stretch. Hydrate. Do not start anything heavy."
        Energy.UNSET -> "Tag this window — high, med, or low."
    }

    fun encode(window: String, energy: Energy): String = "$window|${energy.name}"
}
