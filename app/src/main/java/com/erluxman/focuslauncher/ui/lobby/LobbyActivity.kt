package com.erluxman.focuslauncher.ui.lobby

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
    private var countdownSeconds: Int = LOBBY_SECONDS
    private var harderMath: Boolean = false
    private var interventionCount: Int = 0
    private var mantraReps: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        targetPackage = intent.getStringExtra(EXTRA_TARGET_PACKAGE)
        countdownSeconds = intent.getIntExtra(EXTRA_COUNTDOWN_SECONDS, LOBBY_SECONDS)
        harderMath = intent.getBooleanExtra(EXTRA_HARDER_MATH, false)
        interventionCount = intent.getIntExtra(EXTRA_INTERVENTION_COUNT, 0)
        mantraReps = intent.getIntExtra(EXTRA_MANTRA_REPS, 1)
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
                    countdownSeconds = countdownSeconds,
                    harderMath = harderMath,
                    interventionCount = interventionCount,
                    mantraReps = mantraReps,
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
        const val EXTRA_COUNTDOWN_SECONDS = "extra_countdown_seconds"
        const val EXTRA_HARDER_MATH = "extra_harder_math"
        const val EXTRA_INTERVENTION_COUNT = "extra_intervention_count"
        const val EXTRA_MANTRA_REPS = "extra_mantra_reps"
        const val LOBBY_SECONDS = 10
    }
}

@androidx.compose.runtime.Composable
internal fun LobbyContent(
    targetPackage: String,
    mantra: String = "",
    countdownSeconds: Int = LobbyActivity.LOBBY_SECONDS,
    harderMath: Boolean = false,
    interventionCount: Int = 0,
    mantraReps: Int = 1,
    onAcknowledged: () -> Unit,
    onAborted: () -> Unit
) {
    var remaining by remember { mutableStateOf(countdownSeconds) }
    var intent by remember { mutableStateOf("") }
    var problem by remember { mutableStateOf(
        if (harderMath) CognitiveTax.generate(kotlin.random.Random(0L))
            .let { CognitiveTax.generate() }  // best-effort harder pick
        else CognitiveTax.generate()
    ) }
    var answer by remember { mutableStateOf("") }
    val solved = answer.toIntOrNull() == problem.answer
    val mantraMode = mantra.isNotBlank()
    val mantraMatches = com.erluxman.focuslauncher.ui.mantra.MantraMatcher.countMatches(intent, mantra)
    val mantraOk = !mantraMode || mantraMatches >= mantraReps
    val intentOk = if (mantraMode) mantraOk else intent.trim().length >= 3
    val replacementSuggestion = remember {
        com.erluxman.focuslauncher.service.LobbyTuner.replacement(
            seed = System.currentTimeMillis() / (24L * 60 * 60 * 1000)
        )
    }

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
            if (interventionCount > 0) {
                Spacer(Modifier.height(12.dp))
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("lobby-intervention"),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.5f)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "This is attempt #$interventionCount today.",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = "The phone is not your friend right now.",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "INSTEAD",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.outline,
                        letterSpacing = 1.5.sp
                    )
                    Text(
                        text = replacementSuggestion,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.testTag("lobby-replacement")
                    )
                }
            }
            Spacer(Modifier.height(16.dp))

            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(
                    progress = { remaining / countdownSeconds.toFloat().coerceAtLeast(1f) },
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
                    text = if (mantraReps > 1) "Type your mantra $mantraReps times (newline between each):"
                    else "Type your mantra:",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "\"$mantra\"  ($mantraMatches / $mantraReps)",
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
                minLines = if (mantraReps > 1) mantraReps else 2
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
            Spacer(Modifier.height(24.dp))
            TimeDilationClock()
            Spacer(Modifier.height(16.dp))
            ChoiceOverloadGrid(onChoice = onAborted)
        }
    }
}

@androidx.compose.runtime.Composable
private fun TimeDilationClock() {
    var elapsed by remember { mutableStateOf(0) }
    LaunchedEffect(Unit) {
        while (true) {
            delay(1000L / 3L)  // 3x speed
            elapsed++
        }
    }
    val seconds = elapsed % 60
    val minutes = (elapsed / 60) % 60
    val hours = elapsed / 3600
    Surface(
        modifier = Modifier.fillMaxWidth().testTag("lobby-time-dilation"),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                "TIME SPENT HERE (3x)",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.outline,
                letterSpacing = 1.5.sp
            )
            Text(
                text = "%02d:%02d:%02d".format(hours, minutes, seconds),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

private val CHOICE_OVERLOAD_ITEMS = listOf(
    "Read 1 page", "Push-ups × 10", "Drink water", "Stretch", "Make tea",
    "Call mom", "Text a friend", "Plant care", "Tidy desk", "Walk 5 min",
    "Breathe 4-7-8", "Journal 1 line", "Sketch", "Wash a dish", "Vacuum 1m",
    "Stand up", "Eye gaze 20-20-20", "Floss one tooth", "Stretch neck", "Smile",
    "Sip slowly", "Look outside", "Open window", "Step into sun", "Listen to a song",
    "Hum a tune", "Refill bottle", "Take supplements", "Set tomorrow's intent",
    "Send a thanks", "List 3 wins", "Recite mantra", "Inhale slow", "Exhale slow",
    "Hug a pet", "Pet the wall", "Reorganize one shelf", "Pen a poem", "Fold a sock",
    "Make bed", "Sit upright", "Roll shoulders", "Find a pen", "Wipe the screen",
    "Adjust posture", "Greet yourself", "Drink water again"
)

@androidx.compose.runtime.Composable
private fun ChoiceOverloadGrid(onChoice: () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth().testTag("lobby-choice-overload")) {
        Text(
            text = "OR DO ONE OF THESE 47 INSTEAD",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.outline,
            letterSpacing = 1.5.sp
        )
        Spacer(Modifier.height(6.dp))
        // Use Flow-style chunks of 3.
        CHOICE_OVERLOAD_ITEMS.chunked(3).forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                row.forEach { activity ->
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onChoice() },
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(6.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                    ) {
                        Text(
                            text = activity,
                            modifier = Modifier.padding(6.dp),
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
                repeat(3 - row.size) {
                    Spacer(Modifier.weight(1f))
                }
            }
        }
    }
}
