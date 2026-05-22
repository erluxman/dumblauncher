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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.erluxman.focuslauncher.service.WeeklyReview

@Composable
fun WeeklyReviewCard(
    summary: WeeklyReview.Summary,
    modifier: Modifier = Modifier,
) {
    val allZero = summary.meditationMin == 0 && summary.readingMin == 0 && summary.workoutMin == 0
    if (allZero) return
    val outline = MaterialTheme.colorScheme.outline

    Column(modifier = modifier.testTag("weekly-review-card")) {
        Text(
            "WEEK IN REVIEW",
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
                ReviewRow("MEDITATION", "${summary.meditationMin}m  ·  ${summary.meditationDays}d", "review-meditation")
                Spacer(Modifier.height(6.dp))
                ReviewRow("READING", "${summary.readingMin}m  ·  ${summary.readingDays}d", "review-reading")
                Spacer(Modifier.height(6.dp))
                ReviewRow("WORKOUT", "${summary.workoutMin}m  ·  ${summary.workoutDays}d", "review-workout")
            }
        }
    }
}

@Composable
private fun ReviewRow(label: String, value: String, tag: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.outline,
            letterSpacing = 1.5.sp
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.testTag(tag)
        )
    }
}
