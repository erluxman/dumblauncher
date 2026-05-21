package com.erluxman.focuslauncher.service

import android.content.Context
import android.graphics.PixelFormat
import android.graphics.Color
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import kotlin.math.min

/**
 * Manages a system-overlay window that progressively dims the screen while a
 * flagged distraction app is in the foreground (RESTRICT-003).
 *
 * Alpha curve: starts at MIN_ALPHA on attach, rises to MAX_ALPHA over RAMP_MS.
 * On stop(), the view is removed and the ramp resets.
 */
object DimmingCurve {
    const val MIN_ALPHA = 0.05f
    const val MAX_ALPHA = 0.55f
    const val RAMP_MS = 5 * 60 * 1000L  // 5 minutes

    /** Pure function so we can unit-test the curve without Android deps. */
    fun alphaForElapsed(elapsedMs: Long, rampMs: Long = RAMP_MS): Float {
        val t = (elapsedMs.coerceAtLeast(0L)).toFloat() / rampMs.toFloat()
        val clamped = min(1f, t)
        return MIN_ALPHA + (MAX_ALPHA - MIN_ALPHA) * clamped
    }
}

object DimmingOverlay {

    private const val TICK_MS = 1000L

    private var view: View? = null
    private var attachedAtMs: Long = 0L
    private val handler: Handler by lazy { Handler(Looper.getMainLooper()) }
    private val ticker = object : Runnable {
        override fun run() {
            updateAlpha()
            if (view != null) handler.postDelayed(this, TICK_MS)
        }
    }

    fun canDraw(context: Context): Boolean =
        Settings.canDrawOverlays(context)

    fun start(context: Context) {
        if (view != null) return
        if (!canDraw(context)) return
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val v = View(context).apply {
            setBackgroundColor(Color.BLACK)
            alpha = DimmingCurve.MIN_ALPHA
        }
        val type = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        else
            @Suppress("DEPRECATION")
            WindowManager.LayoutParams.TYPE_SYSTEM_ALERT
        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            type,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE or
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
            PixelFormat.TRANSLUCENT
        ).apply { gravity = Gravity.TOP or Gravity.START }

        try {
            wm.addView(v, params)
            view = v
            attachedAtMs = System.currentTimeMillis()
            handler.removeCallbacks(ticker)
            handler.post(ticker)
        } catch (_: Exception) {
            // SecurityException or BadTokenException — silently no-op; user can grant later.
            view = null
        }
    }

    fun stop(context: Context) {
        val v = view ?: return
        view = null
        handler.removeCallbacks(ticker)
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        try { wm.removeView(v) } catch (_: Exception) {}
    }

    private fun updateAlpha() {
        val v = view ?: return
        val elapsed = System.currentTimeMillis() - attachedAtMs
        v.alpha = DimmingCurve.alphaForElapsed(elapsed)
    }
}
