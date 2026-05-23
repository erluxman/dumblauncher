package com.erluxman.focuslauncher.ui.backend

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.erluxman.focuslauncher.backend.BackendRepository
import com.erluxman.focuslauncher.backend.FirebaseInit
import com.erluxman.focuslauncher.payment.PaymentRouter
import com.erluxman.focuslauncher.ui.home.minimal.MinimalTheme
import kotlinx.coroutines.launch

/**
 * Debug-style read-only snapshot of the backend + payment-router state.
 * Surfaces whether Firebase initialized, the current uid, the active
 * payment channel, and the cached payment config. Useful while the
 * Firebase project + Play Console products are being provisioned.
 *
 * Behind FlagKey.FIREBASE_BACKEND.
 */
@Composable
fun BackendStatusScreen(
    backend: BackendRepository,
    paymentRouter: PaymentRouter,
    onBack: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val uid by backend.uid.collectAsState(initial = null)
    val cfg by backend.paymentConfig.collectAsState(
        initial = BackendRepository.PaymentConfig(false, "")
    )
    val channel by produceState(initialValue = PaymentRouter.Channel.NONE, key1 = Unit) {
        value = paymentRouter.activeChannel()
    }
    var lastOutcome by remember { mutableStateOf<String?>(null) }

    Surface(
        modifier = Modifier.fillMaxSize().testTag("backend"),
        color = MinimalTheme.bg,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 32.dp),
        ) {
            Spacer(Modifier.height(40.dp))
            Text(
                "← back",
                style = captionStyle,
                color = MinimalTheme.outline,
                modifier = Modifier
                    .testTag("backend-back")
                    .clickable { onBack() }
                    .padding(8.dp),
            )

            Spacer(Modifier.height(24.dp))
            Text("backend.", style = displayStyle, color = MinimalTheme.fg)
            Spacer(Modifier.height(8.dp))
            Text(
                "firebase + payment router status. read-only.",
                style = bodyStyle.copy(fontSize = 14.sp),
                color = MinimalTheme.outline,
            )

            Spacer(Modifier.height(32.dp))
            Row("firebase", if (FirebaseInit.isAvailable) "initialized" else "not initialized",
                FirebaseInit.isAvailable, "backend-firebase-status")
            FirebaseInit.lastErrorOrNull()?.let {
                Spacer(Modifier.height(4.dp))
                Text("    $it", style = captionStyle, color = MinimalTheme.outline.copy(alpha = 0.6f))
            }

            Row("uid", uid ?: "—", uid != null, "backend-uid")

            Row("payment channel", channel.name.lowercase(),
                channel != PaymentRouter.Channel.NONE, "backend-channel")
            Row("native on device (remote)", cfg.nativeOnDevice.toString(),
                cfg.nativeOnDevice, "backend-native-on-device")
            Row("web fallback url", cfg.webFallbackUrl,
                cfg.webFallbackUrl.isNotEmpty(), "backend-web-url")

            Spacer(Modifier.height(32.dp))
            Text(
                "test checkout (productId=test_subscription_001)",
                style = bodyStyle,
                color = MinimalTheme.accent,
                modifier = Modifier
                    .testTag("backend-test-checkout")
                    .clickable {
                        scope.launch {
                            val out = paymentRouter.checkout("test_subscription_001")
                            lastOutcome = out.toString()
                        }
                    }
                    .padding(vertical = 12.dp),
            )
            lastOutcome?.let {
                Spacer(Modifier.height(12.dp))
                Text("outcome:", style = captionStyle, color = MinimalTheme.outline)
                Spacer(Modifier.height(4.dp))
                Text(it, style = bodyStyle.copy(fontSize = 14.sp), color = MinimalTheme.fg,
                    modifier = Modifier.testTag("backend-outcome"))
            }
            Spacer(Modifier.height(48.dp))
        }
    }
}

@Composable
private fun Row(label: String, value: String, ok: Boolean, tag: String) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp)) {
        Text(label, style = captionStyle, color = MinimalTheme.outline)
        Spacer(Modifier.height(4.dp))
        Text(
            value,
            style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Normal),
            color = if (ok) MinimalTheme.fg else MinimalTheme.outline,
            modifier = Modifier.testTag(tag),
        )
    }
}

private val displayStyle = TextStyle(fontSize = 32.sp, fontWeight = FontWeight.Normal, letterSpacing = 0.sp)
private val bodyStyle = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Normal, letterSpacing = 0.sp)
private val captionStyle = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Normal)
