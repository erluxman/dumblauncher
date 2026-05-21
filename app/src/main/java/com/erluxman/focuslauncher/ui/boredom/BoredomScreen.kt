package com.erluxman.focuslauncher.ui.boredom

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

const val BOREDOM_SECONDS = 120

@Composable
fun BoredomScreen(onBack: () -> Unit) {
    var remaining by remember { mutableStateOf(BOREDOM_SECONDS) }
    var done by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        while (remaining > 0) {
            delay(1000)
            remaining--
        }
        done = true
    }

    Surface(
        modifier = Modifier.fillMaxSize().testTag("boredom"),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "NOTHING",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.outline,
                letterSpacing = 4.sp
            )
            Spacer(Modifier.height(48.dp))
            if (!done) {
                Text(
                    text = formatTime(remaining),
                    style = MaterialTheme.typography.displayLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.testTag("boredom-timer")
                )
                Spacer(Modifier.height(24.dp))
                Text(
                    "Sit with it.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.outline
                )
            } else {
                Text(
                    text = "Done.",
                    style = MaterialTheme.typography.displayMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.testTag("boredom-done")
                )
                Spacer(Modifier.height(24.dp))
                Text(
                    "You sat with it. That counts.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.outline
                )
            }
            Spacer(Modifier.height(48.dp))
            if (done) {
                Button(
                    onClick = onBack,
                    modifier = Modifier.fillMaxWidth().testTag("boredom-back")
                ) { Text("Back home") }
            } else {
                OutlinedButton(
                    onClick = onBack,
                    modifier = Modifier.fillMaxWidth().testTag("boredom-quit")
                ) { Text("Quit (didn't make it)") }
            }
        }
    }
}

private fun formatTime(seconds: Int): String {
    val m = seconds / 60
    val s = seconds % 60
    return "%d:%02d".format(m, s)
}
