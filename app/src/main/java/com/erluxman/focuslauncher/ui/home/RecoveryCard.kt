package com.erluxman.focuslauncher.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.erluxman.focuslauncher.service.RecoveryScore

@Composable
fun RecoveryCard(
    sleepMinutes: Int,
    steps: Int,
    feel1to10: Int = RecoveryScore.DEFAULT_FEEL,
    modifier: Modifier = Modifier,
) {
    if (sleepMinutes <= 0 && steps <= 0) return
    val score = RecoveryScore.compute(sleepMinutes, steps, feel1to10)
    val outline = MaterialTheme.colorScheme.outline
    val accent = when (score.label) {
        "READY" -> Color(0xFF7BD389)
        "STEADY" -> Color(0xFFE3D26F)
        "DRAINED" -> Color(0xFFE0A458)
        else -> Color(0xFFE05858)
    }

    Column(modifier = modifier.testTag("recovery-card")) {
        Text(
            "RECOVERY",
            style = MaterialTheme.typography.labelLarge,
            color = outline,
            letterSpacing = 2.sp
        )
        Spacer(Modifier.height(8.dp))
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(score.label, style = MaterialTheme.typography.labelSmall, color = accent, letterSpacing = 1.5.sp, fontWeight = FontWeight.Bold)
                        Text(
                            text = "${score.total}",
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.testTag("recovery-total")
                        )
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        SubScoreLine("SLEEP", score.sleepSub, "recovery-sleep")
                        SubScoreLine("ACTIVITY", score.activitySub, "recovery-activity")
                        SubScoreLine("FEEL", score.feelSub, "recovery-feel")
                    }
                }
                Spacer(Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .testTag("recovery-bar")
                ) {
                    Surface(color = outline.copy(alpha = 0.2f), modifier = Modifier.fillMaxWidth().height(4.dp), shape = RoundedCornerShape(2.dp)) {}
                    Surface(
                        color = accent,
                        modifier = Modifier
                            .height(4.dp)
                            .fillMaxWidth(score.total / 100f),
                        shape = RoundedCornerShape(2.dp)
                    ) {}
                }
            }
        }
    }
}

@Composable
private fun SubScoreLine(label: String, value: Int, tag: String) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.outline,
            letterSpacing = 1.5.sp
        )
        Text(
            text = "$value",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.testTag(tag)
        )
    }
}
