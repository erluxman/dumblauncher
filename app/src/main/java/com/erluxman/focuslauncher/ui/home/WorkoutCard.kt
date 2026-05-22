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
import com.erluxman.focuslauncher.service.fitness.WorkoutLog

@Composable
fun WorkoutCard(
    todayIso: String,
    last7DaysIso: List<String>,
    sessions: List<WorkoutLog.Session>,
    onLog: (minutes: Int, kind: String) -> Unit,
    onClear: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val todayMin = WorkoutLog.minutesOn(todayIso, sessions)
    val weekMin = WorkoutLog.minutesIn(last7DaysIso, sessions)
    val streak = WorkoutLog.consecutiveDayStreak(todayIso, sessions)
    val outline = MaterialTheme.colorScheme.outline

    var kind by remember { mutableStateOf(WorkoutLog.PRESET_KINDS.first()) }

    Column(modifier = modifier.testTag("workout-card")) {
        Text(
            "WORKOUT",
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
                            modifier = Modifier.testTag("workout-today")
                        )
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text("LAST 7 DAYS", style = MaterialTheme.typography.labelSmall, color = outline, letterSpacing = 1.5.sp)
                        Text(
                            text = "${weekMin}m  ·  ${streak}🔥",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.testTag("workout-week")
                        )
                    }
                }
                Spacer(Modifier.height(12.dp))
                Text("KIND", style = MaterialTheme.typography.labelSmall, color = outline)
                Spacer(Modifier.height(4.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(WorkoutLog.PRESET_KINDS) { k ->
                        AssistChip(
                            onClick = { kind = k },
                            label = {
                                Text(
                                    k,
                                    fontWeight = if (k == kind) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            modifier = Modifier.testTag(
                                "workout-kind-${k.lowercase().replace(Regex("[^a-z0-9]"), "_")}"
                            )
                        )
                    }
                }
                Spacer(Modifier.height(8.dp))
                Text("LOG SESSION", style = MaterialTheme.typography.labelSmall, color = outline)
                Spacer(Modifier.height(4.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(WorkoutLog.PRESET_MINUTES) { m ->
                        AssistChip(
                            onClick = { onLog(m, kind) },
                            label = { Text("+${m}m") },
                            modifier = Modifier.testTag("workout-add-$m")
                        )
                    }
                }
                if (sessions.isNotEmpty()) {
                    Spacer(Modifier.height(4.dp))
                    TextButton(
                        onClick = onClear,
                        modifier = Modifier.testTag("workout-clear")
                    ) { Text("Clear log (${sessions.size})") }
                }
            }
        }
    }
}
