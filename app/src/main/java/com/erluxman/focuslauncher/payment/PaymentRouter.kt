package com.erluxman.focuslauncher.payment

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import com.erluxman.focuslauncher.backend.BackendRepository
import com.erluxman.focuslauncher.config.FeatureFlagsRepository
import com.erluxman.focuslauncher.config.FlagKey
import kotlinx.coroutines.flow.first

/**
 * Routes "Buy" taps to either Play Billing (native) or web checkout
 * (custom tab) based on the Firebase-driven [BackendRepository.PaymentConfig]
 * with a local-flag offline fallback.
 *
 * See `documentation/payment-architecture.md` for the contract.
 *
 * Covers PAY-003. The actual Billing client (PAY-001) and the web app
 * (PAY-002 / PLATFORM-002) are ship-later — this router is the boundary.
 */
class PaymentRouter(
    private val context: Context,
    private val backend: BackendRepository,
    private val flags: FeatureFlagsRepository,
) {

    sealed interface Outcome {
        data object LaunchedNative : Outcome
        data class LaunchedWeb(val url: String) : Outcome
        data class Disabled(val reason: String) : Outcome
        data class Failed(val message: String) : Outcome
    }

    /** Resolve the active channel without performing a purchase. */
    suspend fun activeChannel(): Channel {
        val nativeAllowed = flags.effective.first()[FlagKey.PAYMENTS_NATIVE] == true
        val webAllowed = flags.effective.first()[FlagKey.PAYMENTS_WEB] == true
        if (!nativeAllowed && !webAllowed) return Channel.NONE

        val cfg = backend.paymentConfig.first()
        val offlineFallback = flags.effective.first()[FlagKey.PAYMENTS_NATIVE_ON_DEVICE] == true
        val nativeOnDevice = cfg.nativeOnDevice xor false  // remote wins; cfg already merges defaults
        val preferNative = nativeOnDevice || offlineFallback

        return when {
            preferNative && nativeAllowed -> Channel.NATIVE
            webAllowed -> Channel.WEB
            nativeAllowed -> Channel.NATIVE
            else -> Channel.NONE
        }
    }

    suspend fun checkout(productId: String): Outcome {
        return when (activeChannel()) {
            Channel.NATIVE -> launchNative(productId)
            Channel.WEB -> launchWeb(productId)
            Channel.NONE -> Outcome.Disabled("no payment channel enabled — check PAYMENTS_NATIVE / PAYMENTS_WEB flags")
        }
    }

    private fun launchNative(productId: String): Outcome {
        // TODO PAY-001 — wire BillingClient. For now: surface that we'd go native.
        return Outcome.Disabled("native channel selected but BillingClient (PAY-001) not yet wired — set PAYMENTS_NATIVE_ON_DEVICE=false to use web")
    }

    private suspend fun launchWeb(productId: String): Outcome {
        val cfg = backend.paymentConfig.first()
        val uid = backend.uid.first().orEmpty()
        val url = buildString {
            append(cfg.webFallbackUrl)
            append("?p=").append(Uri.encode(productId))
            if (uid.isNotEmpty()) append("&uid=").append(Uri.encode(uid))
        }
        return try {
            val intent = CustomTabsIntent.Builder().build().intent.apply {
                data = Uri.parse(url)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
            Outcome.LaunchedWeb(url)
        } catch (t: Throwable) {
            Outcome.Failed(t.message ?: "custom tab launch failed")
        }
    }

    enum class Channel { NATIVE, WEB, NONE }
}
