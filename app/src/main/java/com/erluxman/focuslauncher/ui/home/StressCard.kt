package com.erluxman.focuslauncher.ui.home

import androidx.compose.foundation.layout.Arrangement
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
import com.erluxman.focuslauncher.service.insights.StressIndex

@Composable
fun StressCard(
    unlocksToday: Int,
    sleepMinutesLastNight: Int,
    modifier: Modifier = Modifier,
) {
    if (unlocksToday <= 0 && sleepMinutesLastNight <= 0) return
    val r = StressIndex.compute(unlocksToday, sleepMinutesLastNight)
    val outline = MaterialTheme.colorScheme.outline
    val accent = when (r.label) {
        "CLEAR" -> Color(0xFF7BD389)
        "PARTLY CLOUDY" -> Color(0xFFE3D26F)
        "OVERCAST" -> Color(0xFFE0A458)
        else -> Color(0xFFE05858)
    }

    Column(modifier = modifier.testTag("stress-card")) {
        Text(
            "STRESS WEATHER",
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
            Row(
                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom,
            ) {
                Column {
                    Text(r.label, style = MaterialTheme.typography.labelSmall, color = accent, letterSpacing = 1.5.sp, fontWeight = FontWeight.Bold)
                    Text(
                        text = "${r.total}",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.testTag("stress-total")
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    SubScoreLine("UNLOCKS", r.unlocksSub, "stress-unlocks")
                    SubScoreLine("SLEEP", r.sleepSub, "stress-sleep")
                }
            }
        }
    }
}

@Composable
private fun SubScoreLine(label: String, value: Int, tag: String) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline, letterSpacing = 1.5.sp)
        Text(
            text = "$value",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.testTag(tag)
        )
    }
}
