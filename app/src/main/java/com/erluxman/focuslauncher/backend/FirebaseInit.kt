package com.erluxman.focuslauncher.backend

import android.content.Context
import android.util.Log

/**
 * Best-effort Firebase initialization. We don't depend on the
 * google-services Gradle plugin at compile time (it's gated on the
 * presence of `app/google-services.json`), so at runtime we attempt
 * `FirebaseApp.initializeApp` reflectively and report success/failure.
 *
 * Call `FirebaseInit.attemptInit(context)` once at process start and read
 * [isAvailable] elsewhere to gate Firebase-dependent code.
 */
object FirebaseInit {

    @Volatile
    var isAvailable: Boolean = false
        private set

    private var lastError: String? = null

    fun attemptInit(context: Context) {
        if (isAvailable) return
        try {
            val cls = Class.forName("com.google.firebase.FirebaseApp")
            val initializeApp = cls.getMethod("initializeApp", Context::class.java)
            val app = initializeApp.invoke(null, context.applicationContext)
            isAvailable = app != null
            if (!isAvailable) lastError = "FirebaseApp.initializeApp returned null"
            else Log.i(TAG, "Firebase initialized")
        } catch (t: Throwable) {
            isAvailable = false
            lastError = t.message ?: t.javaClass.simpleName
            Log.i(TAG, "Firebase not initialized: $lastError")
        }
    }

    fun lastErrorOrNull(): String? = lastError

    private const val TAG = "FirebaseInit"
}
