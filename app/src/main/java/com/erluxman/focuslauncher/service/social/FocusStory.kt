package com.erluxman.focuslauncher.service.social

/**
 * SOCIAL-021 Focus Story — headline generator.
 *
 * Pure-fn that picks one of a few templates based on today's shape.
 * The social tier ships later; in the meantime the same string powers
 * Wrapped previews and any "share today" buttons.
 */
object FocusStory {

    fun headline(
        focusSessionsToday: Int,
        todosCompletedToday: Int,
        distractionMinutesToday: Int,
    ): String {
        val isClean = distractionMinutesToday <= 30 && (focusSessionsToday + todosCompletedToday) >= 3
        val isFighting = distractionMinutesToday in 31..120
        val isDrowning = distractionMinutesToday > 240
        return when {
            isClean -> "Shipped: $todosCompletedToday todos, $focusSessionsToday focus blocks. Distractions held under 30m."
            isDrowning -> "Drowning day: ${distractionMinutesToday}m on screen. Tomorrow: one small win."
            isFighting -> "Mixed bag: ${distractionMinutesToday}m on screen, $todosCompletedToday todos done."
            todosCompletedToday > 0 || focusSessionsToday > 0 ->
                "Quiet day: $todosCompletedToday todos, $focusSessionsToday focus blocks, ${distractionMinutesToday}m off-task."
            else -> "A slow day. ${distractionMinutesToday}m off-task. Nothing wrong with that."
        }
    }
}
