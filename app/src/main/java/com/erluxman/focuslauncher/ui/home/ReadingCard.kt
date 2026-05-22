package com.erluxman.focuslauncher.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.erluxman.focuslauncher.service.habits.ReadingLog

@Composable
fun ReadingCard(
    todayIso: String,
    last7DaysIso: List<String>,
    sessions: List<ReadingLog.Session>,
    onLog: (Int) -> Unit,
    onClear: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val todayMin = ReadingLog.minutesOn(todayIso, sessions)
    val weekMin = ReadingLog.minutesIn(last7DaysIso, sessions)
    val books = ReadingLog.bookEquivalent(weekMin)
    val outline = MaterialTheme.colorScheme.outline

    Column(modifier = modifier.testTag("reading-card")) {
        Text(
            "READING",
            style = MaterialTheme.typography.labelLarge,
            color = outline,
            letterSpacing = 2.sp
        )
        Spacer(Modifier.height(8.dp))
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.Bottom) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("TODAY", style = MaterialTheme.typography.labelSmall, color = outline, letterSpacing = 1.5.sp)
                        Text(
                            text = "${todayMin}m",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.testTag("reading-today")
                        )
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text("LAST 7 DAYS", style = MaterialTheme.typography.labelSmall, color = outline, letterSpacing = 1.5.sp)
                        Text(
                            text = "${weekMin}m  ·  ${"%.1f".format(books)} books",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.testTag("reading-week")
                        )
                    }
                }
                Spacer(Modifier.height(12.dp))
                Text("LOG SESSION", style = MaterialTheme.typography.labelSmall, color = outline)
                Spacer(Modifier.height(4.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(ReadingLog.PRESET_MINUTES) { m ->
                        AssistChip(
                            onClick = { onLog(m) },
                            label = { Text("+${m}m") },
                            modifier = Modifier.testTag("reading-add-$m")
                        )
                    }
                }
                if (sessions.isNotEmpty()) {
                    Spacer(Modifier.height(4.dp))
                    TextButton(
                        onClick = onClear,
                        modifier = Modifier.testTag("reading-clear")
                    ) { Text("Clear log (${sessions.size})") }
                }
            }
        }
    }
}
