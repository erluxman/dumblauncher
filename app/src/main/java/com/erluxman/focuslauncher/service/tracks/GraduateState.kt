package com.erluxman.focuslauncher.service.tracks

/**
 * LIFECYCLE-001 Graduate Mode (pre-graduate visibility).
 *
 * Graduates when the user has spent enough days at the top of the
 * Track system that they no longer need the friction. The spec calls
 * out 18+ months; this code exposes both the threshold and a "days to
 * graduate" countdown so the home can show progress before the user
 * actually qualifies.
 */
object GraduateState {

    const val LEVEL_THRESHOLD = 10
    const val DAYS_THRESHOLD = 540  // ~18 months

    data class Stat(
        val isGraduate: Boolean,
        val daysOnboarded: Int,
        val daysRemaining: Int,
        val atTopLevel: Boolean,
    )

    fun compute(
        trackLevel: Int,
        onboardingMs: Long,
        nowMs: Long = System.currentTimeMillis(),
    ): Stat {
        val days = if (onboardingMs <= 0L) 0
        else ((nowMs - onboardingMs).coerceAtLeast(0L) / 86_400_000L).toInt()
        val atTop = trackLevel >= LEVEL_THRESHOLD
        val graduate = atTop && days >= DAYS_THRESHOLD
        val remaining = (DAYS_THRESHOLD - days).coerceAtLeast(0)
        return Stat(
            isGraduate = graduate,
            daysOnboarded = days,
            daysRemaining = remaining,
            atTopLevel = atTop,
        )
    }
}
