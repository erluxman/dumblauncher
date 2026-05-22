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
import com.erluxman.focuslauncher.service.insights.TimeDilation

/**
 * ABSURD-003 Time Dilation Clock — applied to our own surface.
 *
 * Headline shows today's distraction time multiplied by 3, framed as
 * "felt like" to keep the conceit. Hidden if there's nothing to dilate.
 */
@Composable
fun TimeDilationCard(distractionMinutes: Int, modifier: Modifier = Modifier) {
    if (distractionMinutes <= 0) return
    val dilated = TimeDilation.dilatedMinutes(distractionMinutes)
    val real = TimeDilation.formatHm(distractionMinutes)
    val felt = TimeDilation.formatHm(dilated)
    val outline = MaterialTheme.colorScheme.outline

    Column(modifier = modifier.testTag("time-dilation-card")) {
        Text(
            "TIME DILATION",
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
            Row(
                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "DISTRACTION (REAL)",
                        style = MaterialTheme.typography.labelSmall,
                        color = outline,
                        letterSpacing = 1.5.sp
                    )
                    Text(
                        text = real,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.testTag("dilation-real")
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        "FELT LIKE (3×)",
                        style = MaterialTheme.typography.labelSmall,
                        color = outline,
                        letterSpacing = 1.5.sp
                    )
                    Text(
                        text = felt,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.testTag("dilation-felt")
                    )
                }
            }
        }
    }
}
