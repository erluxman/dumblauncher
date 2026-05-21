package com.erluxman.focuslauncher.ui.focus

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.erluxman.focuslauncher.data.prefs.UserPrefs
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class FocusPhase {
    WORK, BREAK
}

object FocusTimer {
    const val WORK_MS: Long = 25 * 60 * 1000L
    const val BREAK_MS: Long = 5 * 60 * 1000L

    fun durationFor(phase: FocusPhase): Long = when (phase) {
        FocusPhase.WORK -> WORK_MS
        FocusPhase.BREAK -> BREAK_MS
    }

    fun nextPhase(phase: FocusPhase): FocusPhase = when (phase) {
        FocusPhase.WORK -> FocusPhase.BREAK
        FocusPhase.BREAK -> FocusPhase.WORK
    }

    fun format(remainingMs: Long): String {
        val sec = remainingMs / 1000L
        val m = sec / 60L
        val s = sec % 60L
        return "%02d:%02d".format(m, s)
    }
}

@Composable
fun FocusTimerScreen(prefs: UserPrefs, onBack: () -> Unit) {
    val scope = rememberCoroutineScope()
    var phase by remember { mutableStateOf(FocusPhase.WORK) }
    var remaining by remember { mutableStateOf(FocusTimer.WORK_MS) }
    var running by remember { mutableStateOf(false) }

    LaunchedEffect(running, phase) {
        while (running && remaining > 0) {
            delay(1000)
            remaining -= 1000
        }
        if (running && remaining <= 0) {
            if (phase == FocusPhase.WORK) {
                val today = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
                prefs.bumpFocusSession(today)
            }
            phase = FocusTimer.nextPhase(phase)
            remaining = FocusTimer.durationFor(phase)
            running = false
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize().testTag("focus-timer"),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp)) {
            Spacer(Modifier.height(40.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack, modifier = Modifier.testTag("focus-back")) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
                Text(
                    text = "FOCUS SESSION",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.outline,
                    letterSpacing = 2.sp
                )
            }
            Spacer(Modifier.height(32.dp))

            Text(
                text = if (phase == FocusPhase.WORK) "Work" else "Break",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(32.dp))

            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(
                    progress = { remaining.toFloat() / FocusTimer.durationFor(phase).toFloat() },
                    modifier = Modifier.size(220.dp),
                    strokeWidth = 12.dp
                )
                Text(
                    text = FocusTimer.format(remaining),
                    style = MaterialTheme.typography.displayLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.testTag("focus-time-text")
                )
            }
            Spacer(Modifier.height(40.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(
                    onClick = { running = !running },
                    modifier = Modifier.weight(1f).testTag("focus-toggle")
                ) { Text(if (running) "Pause" else "Start") }
                OutlinedButton(
                    onClick = {
                        running = false
                        remaining = FocusTimer.durationFor(phase)
                    },
                    modifier = Modifier.weight(1f).testTag("focus-reset")
                ) { Text("Reset") }
            }
            Spacer(Modifier.height(12.dp))
            OutlinedButton(
                onClick = {
                    running = false
                    if (phase == FocusPhase.WORK) {
                        scope.launch {
                            val today = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
                            prefs.bumpFocusSession(today)
                        }
                    }
                    phase = FocusTimer.nextPhase(phase)
                    remaining = FocusTimer.durationFor(phase)
                },
                modifier = Modifier.fillMaxWidth().testTag("focus-skip")
            ) { Text("Skip to next phase") }
        }
    }
}
