package com.erluxman.focuslauncher.service.tracks

/**
 * GAMIFY-007 Earned Pixels (V1 progress bar).
 *
 * The full spec is "home screen starts grayscale, focused hours earn
 * colour back" — a global colour-matrix on the launcher. This V1
 * exposes the math behind that as a 0..1 fraction so we can ship a
 * progress indicator and wire the full effect later.
 */
object EarnedPixels {

    const val DEFAULT_TARGET_POINTS = 100

    fun saturation(focusPoints: Int, target: Int = DEFAULT_TARGET_POINTS): Double {
        if (target <= 0) return 0.0
        return (focusPoints.coerceAtLeast(0).toDouble() / target).coerceIn(0.0, 1.0)
    }

    fun pctEarned(focusPoints: Int, target: Int = DEFAULT_TARGET_POINTS): Int =
        (saturation(focusPoints, target) * 100).toInt()
}
