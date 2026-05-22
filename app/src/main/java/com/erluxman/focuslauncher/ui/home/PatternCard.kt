package com.erluxman.focuslauncher.ui.home

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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.erluxman.focuslauncher.service.PatternDetect

/**
 * PSYCH-012 Pattern Detection (today-scoped, simple weakest-hour).
 *
 * Surfaces the hour today with the highest distraction minutes. Hidden
 * until at least one hour has any data.
 */
@Composable
fun PatternCard(
    hourlyMinutes: IntArray,
    nowHour: Int,
    modifier: Modifier = Modifier,
) {
    val weakest = PatternDetect.weakestHourToday(hourlyMinutes, nowHour) ?: return
    val outline = MaterialTheme.colorScheme.outline

    Column(modifier = modifier.testTag("pattern-card")) {
        Text(
            "PATTERN",
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
            Row(modifier = Modifier.padding(16.dp).fillMaxWidth(), verticalAlignment = Alignment.Bottom) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("YOUR WEAKEST HOUR TODAY", style = MaterialTheme.typography.labelSmall, color = outline, letterSpacing = 1.5.sp)
                    Text(
                        text = PatternDetect.formatHour(weakest.hour),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.testTag("pattern-hour")
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("MINUTES", style = MaterialTheme.typography.labelSmall, color = outline, letterSpacing = 1.5.sp)
                    Text(
                        text = "${weakest.minutes}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.testTag("pattern-minutes")
                    )
                }
            }
        }
    }
}
