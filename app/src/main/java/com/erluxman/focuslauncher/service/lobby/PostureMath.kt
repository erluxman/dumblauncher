package com.erluxman.focuslauncher.service.lobby

import kotlin.math.abs

/**
 * HARDWARE-004 Posture Lock — pure classifier.
 *
 * Given an accelerometer reading (m/s²), classify the device pose.
 * The full Posture-Lock feature ("block entertainment when lying down")
 * needs an Android sensor wire and a debounce loop; this is the pure
 * math the controller will call.
 *
 * Coordinate convention: Android SensorEvent.values, gravity at rest
 *   - x ≈ 0, y ≈ 9.8 when phone is upright (portrait, screen facing user)
 *   - z ≈ ±9.8 when phone is flat on a table (sign depends on face)
 */
object PostureMath {

    enum class Pose { UPRIGHT, TILTED, FLAT_FACEUP, FLAT_FACEDOWN }

    /** Threshold for "mostly along that axis" in m/s². */
    const val DOMINANT_THRESHOLD = 7.0

    fun classify(x: Float, y: Float, z: Float): Pose {
        val ax = abs(x)
        val ay = abs(y)
        val az = abs(z)
        return when {
            az >= DOMINANT_THRESHOLD && az > ax && az > ay ->
                if (z > 0) Pose.FLAT_FACEUP else Pose.FLAT_FACEDOWN
            ay >= DOMINANT_THRESHOLD && ay > ax -> Pose.UPRIGHT
            else -> Pose.TILTED
        }
    }

    /** Spec: block entertainment when lying down → faceup-on-table proxies bed. */
    fun isInBed(pose: Pose): Boolean = pose == Pose.FLAT_FACEUP
}
