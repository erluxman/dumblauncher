package com.erluxman.focuslauncher.backend

import android.content.Context
import com.erluxman.focuslauncher.config.FeatureFlagsRepository
import com.erluxman.focuslauncher.config.FlagKey
import kotlinx.coroutines.flow.first

/**
 * Resolves the right [BackendRepository] impl at process start.
 *
 * - If Firebase initialized successfully AND the FIREBASE_BACKEND flag is
 *   on → return the real impl (TODO: not built yet; falls back to stub).
 * - Otherwise → return the [StubBackendRepository].
 *
 * Construct once per process in MainActivity and pass down.
 */
object BackendProvider {

    suspend fun resolve(
        context: Context,
        flags: FeatureFlagsRepository,
    ): BackendRepository {
        FirebaseInit.attemptInit(context)
        val flagOn = flags.effective.first()[FlagKey.FIREBASE_BACKEND] == true
        val firebaseReady = FirebaseInit.isAvailable && flagOn
        // Real Firebase impl is intentionally not built yet — return stub for now.
        // When the real impl lands, swap here:
        //   return if (firebaseReady) FirebaseBackendRepository(context) else StubBackendRepository(context)
        return StubBackendRepository(context)
    }
}
