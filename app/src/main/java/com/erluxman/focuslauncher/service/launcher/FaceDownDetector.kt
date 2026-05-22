package com.erluxman.focuslauncher.service.launcher

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager

/**
 * PROD-006 Face-Down Detection.
 *
 * Listens to the accelerometer while the host is active. When the z-axis
 * dips below [FACE_DOWN_Z] (i.e., screen is pointed downward) for at least
 * [DEBOUNCE_MS], invokes [onFaceDown]. Useful as a positive-reinforcement
 * trigger (paired with Applause) when the user puts the phone down.
 *
 * Lifecycle: call [start] from `onResume` and [stop] from `onPause`.
 */
class FaceDownDetector(
    context: Context,
    private val onFaceDown: () -> Unit
) : SensorEventListener {

    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val accel: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    private var faceDownSince: Long = 0L
    private var lastFireMs: Long = 0L

    fun start() {
        accel?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    fun stop() {
        sensorManager.unregisterListener(this)
        faceDownSince = 0L
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null || event.values.size < 3) return
        val z = event.values[2]
        val now = System.currentTimeMillis()
        if (z < FACE_DOWN_Z) {
            if (faceDownSince == 0L) faceDownSince = now
            if (now - faceDownSince >= DEBOUNCE_MS && now - lastFireMs > REFIRE_COOLDOWN_MS) {
                lastFireMs = now
                onFaceDown()
            }
        } else {
            faceDownSince = 0L
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit

    companion object {
        const val FACE_DOWN_Z = -8f         // m/s²; -9.81 is perfectly face-down
        const val DEBOUNCE_MS = 3_000L      // 3 seconds of stillness
        const val REFIRE_COOLDOWN_MS = 60_000L
    }
}
