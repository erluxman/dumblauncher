package com.erluxman.focuslauncher.ui.lobby

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.erluxman.focuslauncher.ui.theme.FocusLauncherTheme
import kotlinx.coroutines.delay

class LobbyActivity : ComponentActivity() {

    private var targetPackage: String? = null
    private var mantra: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        targetPackage = intent.getStringExtra(EXTRA_TARGET_PACKAGE)
        // Read mantra synchronously via runBlocking for activity startup; trivial size.
        mantra = runBlocking {
            com.erluxman.focuslauncher.data.prefs.UserPrefs(applicationContext)
                .mantraPhrase
                .first()
        }

        onBackPressedDispatcher.addCallback(this, object :
            androidx.activity.OnBackPressedCallback(true) {
            override fun handleOnBackPressed() = Unit
        })

        setContent {
            FocusLauncherTheme {
                LobbyContent(
                    targetPackage = targetPackage ?: "this app",
                    mantra = mantra,
                    onAcknowledged = ::finish,
                    onAborted = {
                        val home = android.content.Intent(android.content.Intent.ACTION_MAIN).apply {
                            addCategory(android.content.Intent.CATEGORY_HOME)
                            addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK)
                        }
                        startActivity(home)
                        finish()
                    }
                )
            }
        }
    }

    companion object {
        const val EXTRA_TARGET_PACKAGE = "extra_target_package"
        const val LOBBY_SECONDS = 10
    }
}

@androidx.compose.runtime.Composable
internal fun LobbyContent(
    targetPackage: String,
    mantra: String = "",
    onAcknowledged: () -> Unit,
    onAborted: () -> Unit
) {
    var remaining by remember { mutableStateOf(LobbyActivity.LOBBY_SECONDS) }
    var intent by remember { mutableStateOf("") }
    var problem by remember { mutableStateOf(CognitiveTax.generate()) }
    var answer by remember { mutableStateOf("") }
    val solved = answer.toIntOrNull() == problem.answer
    val mantraMode = mantra.isNotBlank()
    val mantraOk = !mantraMode || com.erluxman.focuslauncher.ui.mantra.MantraMatcher.matches(intent, mantra)
    val intentOk = if (mantraMode) mantraOk else intent.trim().length >= 3

    LaunchedEffect(Unit) {
        while (remaining > 0) {
            delay(1000)
            remaining--
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize().testTag("lobby"),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "THE LOBBY",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.outline,
                letterSpacing = 4.sp
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "About to open\n$targetPackage",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(32.dp))

            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(
                    progress = { remaining / LobbyActivity.LOBBY_SECONDS.toFloat() },
                    modifier = Modifier.size(120.dp),
                    strokeWidth = 8.dp
                )
                Text(
                    text = remaining.toString(),
                    style = MaterialTheme.typography.displayMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(Modifier.height(32.dp))
            if (mantraMode) {
                Text(
                    text = "Type your mantra:",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "\"$mantra\"",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.outline
                )
            } else {
                Text(
                    text = "What are you opening this for?",
                    style = MaterialTheme.typography.titleMedium
                )
            }
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = intent,
                onValueChange = { intent = it },
                modifier = Modifier.fillMaxWidth().testTag("lobby-intent-input"),
                placeholder = {
                    Text(if (mantraMode) "Type the mantra exactly" else "e.g. message Sam back")
                },
                minLines = 2
            )

            Spacer(Modifier.height(16.dp))

            Text(
                text = "Cognitive tax: ${problem.prompt} = ?",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = answer,
                onValueChange = { answer = it.filter { c -> c.isDigit() || c == '-' } },
                modifier = Modifier.fillMaxWidth().testTag("lobby-tax-input"),
                placeholder = { Text("Type the answer") },
                singleLine = true,
                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                    keyboardType = androidx.compose.ui.text.input.KeyboardType.NumberPassword
                )
            )

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = onAcknowledged,
                enabled = remaining == 0 && intentOk && solved,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("lobby-continue")
            ) {
                Text(
                    when {
                        remaining > 0 -> "Wait ${remaining}s"
                        !solved -> "Solve the math"
                        !intentOk && mantraMode -> "Type the mantra"
                        !intentOk -> "Declare your intent"
                        else -> "Continue"
                    }
                )
            }
            Spacer(Modifier.height(8.dp))
            TextButton(
                onClick = onAborted,
                modifier = Modifier.fillMaxWidth().testTag("lobby-abort")
            ) {
                Text("Actually, no — go home")
            }
        }
    }
}
