package com.erluxman.focuslauncher.ui.uninstall

import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.erluxman.focuslauncher.data.prefs.UserPrefs
import com.erluxman.focuslauncher.receiver.FocusDeviceAdminReceiver
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun UninstallScreen(prefs: UserPrefs, onBack: () -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val startedAt by prefs.uninstallRequestedAt.collectAsState(initial = null)
    var nowMs by remember { mutableStateOf(System.currentTimeMillis()) }

    LaunchedEffect(startedAt) {
        while (true) {
            nowMs = System.currentTimeMillis()
            delay(1000)
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize().testTag("uninstall"),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp)) {
            Spacer(Modifier.height(40.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack, modifier = Modifier.testTag("uninstall-back")) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
                Text(
                    text = "LEAVE",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.outline,
                    letterSpacing = 2.sp
                )
            }
            Spacer(Modifier.height(16.dp))

            val start = startedAt
            when {
                start == null -> NotStartedState(
                    onStart = { scope.launch { prefs.startUninstallRequest() } }
                )
                CooldownMath.isElapsed(start, nowMs) -> ElapsedState(
                    onUninstall = { startUninstall(context) },
                    onCancel = { scope.launch { prefs.cancelUninstallRequest() } }
                )
                else -> InProgressState(
                    startedAt = start,
                    nowMs = nowMs,
                    onCancel = { scope.launch { prefs.cancelUninstallRequest() } }
                )
            }
        }
    }
}

@Composable
private fun NotStartedState(onStart: () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(
            text = "Thinking about leaving?",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Per UNINSTALL-001, the app does not block you from leaving — but it does require a 72-hour cooldown. " +
                "This exists for the moments you'd later wish you'd waited.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.outline
        )
        Text(
            text = "You can cancel any time during the cooldown. After 72 hours, the uninstall button unlocks.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.outline
        )
        Spacer(Modifier.height(8.dp))
        Button(
            onClick = onStart,
            modifier = Modifier.fillMaxWidth().testTag("uninstall-start")
        ) { Text("Start 72-hour cooldown") }
    }
}

@Composable
private fun InProgressState(startedAt: Long, nowMs: Long, onCancel: () -> Unit) {
    val remaining = CooldownMath.remainingMs(startedAt, nowMs)
    val progress = 1f - (remaining.toFloat() / CooldownMath.COOLDOWN_MS.toFloat())
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(
            text = "Cooldown in progress",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Surface(
            modifier = Modifier.fillMaxWidth().testTag("uninstall-countdown"),
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = CooldownMath.format(remaining),
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.fillMaxWidth().height(6.dp),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
        Text(
            text = "Time remaining until uninstall is unlocked. The clock keeps running whether the app is open or not.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.outline
        )
        OutlinedButton(
            onClick = onCancel,
            modifier = Modifier.fillMaxWidth().testTag("uninstall-cancel")
        ) { Text("Cancel cooldown — I'll stay") }
    }
}

@Composable
private fun ElapsedState(onUninstall: () -> Unit, onCancel: () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(
            text = "Cooldown complete.",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "You waited 72 hours. Uninstall is unlocked. If you changed your mind, cancel and stay.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.outline
        )
        Button(
            onClick = onUninstall,
            modifier = Modifier.fillMaxWidth().testTag("uninstall-confirm")
        ) { Text("Remove admin + uninstall") }
        OutlinedButton(
            onClick = onCancel,
            modifier = Modifier.fillMaxWidth()
        ) { Text("Cancel — keep using") }
    }
}

private fun startUninstall(context: Context) {
    val dpm = context.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
    val cn = ComponentName(context, FocusDeviceAdminReceiver::class.java)
    if (dpm.isAdminActive(cn)) dpm.removeActiveAdmin(cn)
    val intent = Intent(Intent.ACTION_DELETE).apply {
        data = Uri.parse("package:${context.packageName}")
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    context.startActivity(intent)
}
