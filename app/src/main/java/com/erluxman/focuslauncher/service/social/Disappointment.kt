package com.erluxman.focuslauncher.service.social

/**
 * SOCIAL-005 Disappointment API — pure-fn that summarises the worst
 * stat for the week into a single line. The social wiring (sending
 * this to one trusted recipient) ships later.
 */
object Disappointment {

    data class WeekStats(
        val distractionHours: Int,
        val nightUnlocksAfterCutoff: Int,
        val streakBreaks: Int,
        val unfinishedTodos: Int,
    )

    fun headline(stats: WeekStats): String? {
        val worst = listOf(
            stats.distractionHours to "${stats.distractionHours}h on a feed this week",
            stats.nightUnlocksAfterCutoff to "${stats.nightUnlocksAfterCutoff} late-night unlocks this week",
            stats.streakBreaks to "${stats.streakBreaks} streak breaks this week",
            stats.unfinishedTodos to "${stats.unfinishedTodos} todos that never got done",
        ).maxByOrNull { it.first }
        return worst?.takeIf { it.first > 0 }?.second
    }
}
