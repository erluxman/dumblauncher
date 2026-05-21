package com.erluxman.focuslauncher.ui.lobby

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        targetPackage = intent.getStringExtra(EXTRA_TARGET_PACKAGE)

        setContent {
            FocusLauncherTheme {
                LobbyContent(
                    targetPackage = targetPackage ?: "this app",
                    onAcknowledged = ::finish,
                    onAborted = {
                        // Send user back home rather than into the distraction app.
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

    override fun onBackPressed() {
        // Block back button — must declare or abort.
    }

    companion object {
        const val EXTRA_TARGET_PACKAGE = "extra_target_package"
        const val LOBBY_SECONDS = 10
    }
}

@androidx.compose.runtime.Composable
internal fun LobbyContent(
    targetPackage: String,
    onAcknowledged: () -> Unit,
    onAborted: () -> Unit
) {
    var remaining by remember { mutableStateOf(LobbyActivity.LOBBY_SECONDS) }
    var intent by remember { mutableStateOf("") }
    var problem by remember { mutableStateOf(CognitiveTax.generate()) }
    var answer by remember { mutableStateOf("") }
    val solved = answer.toIntOrNull() == problem.answer

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
            Text(
                text = "What are you opening this for?",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = intent,
                onValueChange = { intent = it },
                modifier = Modifier.fillMaxWidth().testTag("lobby-intent-input"),
                placeholder = { Text("e.g. message Sam back") },
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
                enabled = remaining == 0 && intent.trim().length >= 3 && solved,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("lobby-continue")
            ) {
                Text(
                    when {
                        remaining > 0 -> "Wait ${remaining}s"
                        !solved -> "Solve the math"
                        intent.trim().length < 3 -> "Declare your intent"
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
