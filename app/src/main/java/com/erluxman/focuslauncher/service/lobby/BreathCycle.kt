package com.erluxman.focuslauncher.service.lobby

/**
 * HARDWARE-002 Breath to Unlock — timing-only path.
 *
 * Implements the 4-7-8 pattern (4s inhale, 7s hold, 8s exhale) which
 * runs 19 seconds end-to-end, comfortably above the 16-second minimum
 * called for in the spec.
 *
 * Pure state machine: caller polls [phaseAt] / [progressAt] every
 * frame and renders the breathing circle accordingly.
 */
object BreathCycle {

    const val INHALE_MS = 4_000L
    const val HOLD_MS = 7_000L
    const val EXHALE_MS = 8_000L
    const val CYCLE_MS = INHALE_MS + HOLD_MS + EXHALE_MS

    enum class Phase { INHALE, HOLD, EXHALE, DONE }

    fun phaseAt(elapsedMs: Long): Phase = when {
        elapsedMs < 0L -> Phase.INHALE
        elapsedMs < INHALE_MS -> Phase.INHALE
        elapsedMs < INHALE_MS + HOLD_MS -> Phase.HOLD
        elapsedMs < CYCLE_MS -> Phase.EXHALE
        else -> Phase.DONE
    }

    /** 0..1 fraction within the current phase (clamped). */
    fun progressAt(elapsedMs: Long): Float = when (phaseAt(elapsedMs)) {
        Phase.INHALE -> (elapsedMs.coerceAtLeast(0L) / INHALE_MS.toFloat()).coerceIn(0f, 1f)
        Phase.HOLD -> ((elapsedMs - INHALE_MS) / HOLD_MS.toFloat()).coerceIn(0f, 1f)
        Phase.EXHALE -> ((elapsedMs - INHALE_MS - HOLD_MS) / EXHALE_MS.toFloat()).coerceIn(0f, 1f)
        Phase.DONE -> 1f
    }

    /** Visual circle radius fraction: 0.2 → 1.0 → 0.2 over the cycle. */
    fun circleScaleAt(elapsedMs: Long): Float {
        val p = progressAt(elapsedMs)
        return when (phaseAt(elapsedMs)) {
            Phase.INHALE -> 0.2f + p * 0.8f
            Phase.HOLD -> 1f
            Phase.EXHALE -> 1f - p * 0.8f
            Phase.DONE -> 0.2f
        }
    }
}
