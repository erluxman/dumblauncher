package com.erluxman.focuslauncher.ui.mantra

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import kotlinx.coroutines.launch

object MantraMatcher {
    /** Case-insensitive, whitespace-collapsed equality. */
    fun matches(input: String, mantra: String): Boolean {
        if (mantra.isBlank()) return true
        return normalize(input) == normalize(mantra)
    }

    /** Counts how many newline-separated chunks of [input] match [mantra]. */
    fun countMatches(input: String, mantra: String): Int {
        if (mantra.isBlank()) return 0
        return input.split("\n")
            .map { it.trim() }
            .count { it.isNotEmpty() && normalize(it) == normalize(mantra) }
    }

    /**
     * MANTRA-002: required mantra repetitions scale with track level.
     * Level 1-3 → 1 repetition; 4-6 → 2; 7-9 → 3; 10 → 4.
     */
    fun requiredReps(trackLevel: Int): Int =
        (((trackLevel - 1) / 3) + 1).coerceIn(1, 4)

    fun normalize(s: String): String =
        s.trim().replace(Regex("\\s+"), " ").lowercase()
}

@Composable
fun MantraScreen(prefs: UserPrefs, onBack: () -> Unit) {
    val scope = rememberCoroutineScope()
    val current by prefs.mantraPhrase.collectAsState(initial = "")
    var draft by remember(current) { mutableStateOf(current) }

    Surface(
        modifier = Modifier.fillMaxSize().testTag("mantra"),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp)) {
            Spacer(Modifier.height(40.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack, modifier = Modifier.testTag("mantra-back")) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
                Text(
                    text = "MANTRA",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.outline,
                    letterSpacing = 2.sp
                )
            }
            Spacer(Modifier.height(16.dp))
            Text(
                text = "Set the phrase you'll type at the Lobby gate.",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Pick something you actually want to remember at your weakest moment. " +
                    "Comparison is case- and whitespace-insensitive. Leave blank to disable mantra mode.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.outline
            )
            Spacer(Modifier.height(16.dp))
            OutlinedTextField(
                value = draft,
                onValueChange = { draft = it },
                modifier = Modifier.fillMaxWidth().testTag("mantra-input"),
                placeholder = { Text("e.g. \"I am the builder, not the consumer.\"") },
                minLines = 2
            )
            Spacer(Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(
                    onClick = { scope.launch { prefs.setMantraPhrase(draft.trim()) } },
                    modifier = Modifier.weight(1f).testTag("mantra-save")
                ) { Text("Save mantra") }
                OutlinedButton(
                    onClick = {
                        draft = ""
                        scope.launch { prefs.setMantraPhrase("") }
                    },
                    modifier = Modifier.weight(1f).testTag("mantra-clear")
                ) { Text("Clear") }
            }
            if (current.isNotBlank()) {
                Spacer(Modifier.height(24.dp))
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "CURRENT",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.outline,
                            letterSpacing = 1.5.sp
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = current,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.testTag("mantra-current")
                        )
                    }
                }
            }
        }
    }
}
