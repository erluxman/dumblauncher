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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
    val passphrase by prefs.nuclearPassphrase.collectAsState(initial = "")
    val futureLetter by prefs.futureSelfLetter.collectAsState(initial = "")
    var nowMs by remember { mutableStateOf(System.currentTimeMillis()) }
    var lastDayAcknowledged by remember { mutableStateOf(false) }
    var passphraseCleared by remember { mutableStateOf(false) }
    var letterShown by remember { mutableStateOf(false) }
    val showLastDayTest = startedAt == null && !lastDayAcknowledged
    val showPassphraseGate = startedAt == null && lastDayAcknowledged && !passphraseCleared

    LaunchedEffect(startedAt) {
        while (true) {
            nowMs = System.currentTimeMillis()
            delay(1000)
        }
    }

    if (showLastDayTest) {
        LastDayTest(onAcknowledged = { lastDayAcknowledged = true }, onBack = onBack)
        return
    }

    if (showPassphraseGate) {
        PassphraseGate(
            currentPassphrase = passphrase,
            currentLetter = futureLetter,
            onSet = { phrase, letter ->
                scope.launch {
                    prefs.setNuclearPassphrase(phrase)
                    prefs.setFutureSelfLetter(letter)
                    passphraseCleared = true
                }
            },
            onVerified = { passphraseCleared = true },
            onBack = onBack
        )
        return
    }

    Surface(
        modifier = Modifier.fillMaxSize().testTag("uninstall"),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp)) {
            Spacer(Modifier.height(40.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack, modifier = Modifier.testTag("uninstall-back")) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
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
                CooldownMath.isElapsed(start, nowMs) -> {
                    if (!letterShown && futureLetter.isNotBlank()) {
                        FutureSelfLetterPage(
                            letter = futureLetter,
                            onDone = { letterShown = true }
                        )
                    } else {
                        ElapsedState(
                            onUninstall = { startUninstall(context) },
                            onCancel = { scope.launch { prefs.cancelUninstallRequest() } }
                        )
                    }
                }
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

@Composable
private fun LastDayTest(onAcknowledged: () -> Unit, onBack: () -> Unit) {
    androidx.compose.material3.Surface(
        modifier = Modifier.fillMaxSize().testTag("last-day-test"),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
            Spacer(Modifier.height(40.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) {
                    androidx.compose.material3.Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
                Text(
                    text = "PAUSE",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.outline,
                    letterSpacing = 2.sp
                )
            }
            Spacer(Modifier.height(16.dp))
            Text(
                text = "If today was your last day…",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(16.dp))
            Text(
                text = "Would the thing you're about to do — uninstall this discipline launcher — " +
                    "be the thing you'd want to be remembered for?",
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(Modifier.height(16.dp))
            Text(
                text = "Take a breath. Re-read your declaration. Talk to a friend. Then come back here if you still want to.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.outline
            )
            Spacer(Modifier.height(24.dp))
            Button(
                onClick = onAcknowledged,
                modifier = Modifier.fillMaxWidth().testTag("last-day-acknowledge")
            ) { Text("I've reflected. Continue.") }
            Spacer(Modifier.height(8.dp))
            OutlinedButton(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth().testTag("last-day-go-back")
            ) { Text("Back — I'll stay") }
        }
    }
}

@Composable
private fun PassphraseGate(
    currentPassphrase: String,
    currentLetter: String,
    onSet: (String, String) -> Unit,
    onVerified: () -> Unit,
    onBack: () -> Unit
) {
    val setupMode = currentPassphrase.length < MIN_PASSPHRASE_LEN
    var newPhrase by remember { mutableStateOf("") }
    var newLetter by remember { mutableStateOf(currentLetter) }
    var typed by remember { mutableStateOf("") }
    val match = !setupMode && typed == currentPassphrase

    Surface(
        modifier = Modifier.fillMaxSize().testTag("passphrase-gate"),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
        ) {
            Spacer(Modifier.height(40.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
                Text(
                    text = "PASSPHRASE",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.outline,
                    letterSpacing = 2.sp
                )
            }
            Spacer(Modifier.height(16.dp))
            if (setupMode) {
                Text(
                    "Set a Nuclear Passphrase",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(12.dp))
                Text(
                    "20+ characters. Write it on paper. You will be asked to type it verbatim to start a cooldown later.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.outline
                )
                Spacer(Modifier.height(16.dp))
                OutlinedTextField(
                    value = newPhrase,
                    onValueChange = { newPhrase = it },
                    placeholder = { Text("Type your passphrase") },
                    modifier = Modifier.fillMaxWidth().testTag("passphrase-new"),
                    minLines = 2
                )
                Spacer(Modifier.height(16.dp))
                Text(
                    "Also write a letter to your future self about why you started this.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.outline
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = newLetter,
                    onValueChange = { newLetter = it.take(2000) },
                    placeholder = { Text("Dear future self...") },
                    modifier = Modifier.fillMaxWidth().testTag("passphrase-letter"),
                    minLines = 4
                )
                Spacer(Modifier.height(16.dp))
                Button(
                    onClick = { onSet(newPhrase, newLetter) },
                    enabled = newPhrase.length >= MIN_PASSPHRASE_LEN && newLetter.length >= 20,
                    modifier = Modifier.fillMaxWidth().testTag("passphrase-save")
                ) { Text("Save and continue") }
            } else {
                Text(
                    "Type your Nuclear Passphrase",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    "Exact match required. We will not show hints.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.outline
                )
                Spacer(Modifier.height(16.dp))
                OutlinedTextField(
                    value = typed,
                    onValueChange = { typed = it },
                    placeholder = { Text("Your passphrase") },
                    modifier = Modifier.fillMaxWidth().testTag("passphrase-verify"),
                    minLines = 2
                )
                Spacer(Modifier.height(16.dp))
                Button(
                    onClick = onVerified,
                    enabled = match,
                    modifier = Modifier.fillMaxWidth().testTag("passphrase-continue")
                ) { Text(if (match) "Continue" else "Doesn't match") }
            }
        }
    }
}

@Composable
private fun FutureSelfLetterPage(letter: String, onDone: () -> Unit) {
    val totalSeconds = 30
    var remaining by remember { mutableStateOf(totalSeconds) }
    LaunchedEffect(Unit) {
        while (remaining > 0) {
            delay(1000)
            remaining--
        }
    }
    Surface(
        modifier = Modifier.fillMaxSize().testTag("future-self-letter"),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
        ) {
            Spacer(Modifier.height(40.dp))
            Text(
                "A LETTER FROM YOU",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.outline,
                letterSpacing = 2.sp
            )
            Spacer(Modifier.height(16.dp))
            Text(
                text = letter,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.testTag("future-self-text")
            )
            Spacer(Modifier.height(32.dp))
            Button(
                onClick = onDone,
                enabled = remaining == 0,
                modifier = Modifier.fillMaxWidth().testTag("future-self-done")
            ) {
                Text(if (remaining > 0) "Read for ${remaining}s" else "I've read it. Proceed.")
            }
        }
    }
}

const val MIN_PASSPHRASE_LEN = 20

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
