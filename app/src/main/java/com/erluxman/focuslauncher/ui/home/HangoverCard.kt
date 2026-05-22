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
import com.erluxman.focuslauncher.service.HangoverMath

/**
 * SUB-001 Hangover Calculus card.
 *
 * Headline mirrors the caffeine card: current BAC + hours-to-sober +
 * estimated REM-sleep deficit tonight. Preset chips (drinks) add to the
 * log. Hidden when nothing has been logged today.
 */
@Composable
fun HangoverCard(
    drinks: List<HangoverMath.Drink>,
    nowMs: Long,
    onLog: (Double) -> Unit,
    onClear: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val bac = HangoverMath.bacAt(drinks, nowMs)
    val hoursToSober = HangoverMath.hoursToSober(drinks, nowMs)
    val deficit = HangoverMath.estimatedSleepDeficitMin(drinks)
    val outline = MaterialTheme.colorScheme.outline

    Column(modifier = modifier.testTag("hangover-card")) {
        Text(
            "HANGOVER",
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
                        Text(
                            "ESTIMATED BAC",
                            style = MaterialTheme.typography.labelSmall,
                            color = outline,
                            letterSpacing = 1.5.sp
                        )
                        Text(
                            text = "%.3f%%".format(bac * 100),
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.testTag("hangover-bac")
                        )
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            "HOURS TO SOBER",
                            style = MaterialTheme.typography.labelSmall,
                            color = outline,
                            letterSpacing = 1.5.sp
                        )
                        Text(
                            text = "%.1fh".format(hoursToSober),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.testTag("hangover-sober")
                        )
                    }
                }
                Spacer(Modifier.height(8.dp))
                Text(
                    text = if (drinks.isEmpty()) "Tap a preset below to log a drink."
                    else "Estimated REM-sleep deficit tonight: ${deficit}m.",
                    style = MaterialTheme.typography.bodySmall,
                    color = outline,
                    modifier = Modifier.testTag("hangover-deficit-line")
                )
                Spacer(Modifier.height(12.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(HangoverMath.PRESETS) { preset ->
                        AssistChip(
                            onClick = { onLog(preset.units) },
                            label = { Text("${preset.label} +${preset.units}u") },
                            modifier = Modifier.testTag(
                                "hangover-preset-${preset.label.lowercase().replace(Regex("[^a-z0-9]"), "_")}"
                            )
                        )
                    }
                }
                if (drinks.isNotEmpty()) {
                    Spacer(Modifier.height(4.dp))
                    TextButton(
                        onClick = onClear,
                        modifier = Modifier.testTag("hangover-clear")
                    ) { Text("Clear today (${drinks.size})") }
                }
            }
        }
    }
}
