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
import com.erluxman.focuslauncher.service.sad.Anchoring

/**
 * PSYCH-013 Anchoring Attack — surfaced honestly.
 *
 * The "MOST DISCIPLINED" number is computed from the user's own stats,
 * not real users; the copy on the Transparency page makes this clear,
 * and a single Switch turns the whole card off.
 */
@Composable
fun AnchorCard(
    userDistractionMinutes: Int,
    enabled: Boolean,
    modifier: Modifier = Modifier,
) {
    if (!enabled || userDistractionMinutes <= 0) return
    val anchor = Anchoring.anchorMinutes(userDistractionMinutes)
    val multiplier = Anchoring.multiplier(userDistractionMinutes, anchor)
    val outline = MaterialTheme.colorScheme.outline

    Column(modifier = modifier.testTag("anchor-card")) {
        Text(
            "ANCHOR",
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
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "MOST DISCIPLINED",
                        style = MaterialTheme.typography.labelSmall,
                        color = outline,
                        letterSpacing = 1.5.sp
                    )
                    Text(
                        text = "${anchor}m",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.testTag("anchor-target")
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        "YOU",
                        style = MaterialTheme.typography.labelSmall,
                        color = outline,
                        letterSpacing = 1.5.sp
                    )
                    Text(
                        text = "${userDistractionMinutes}m  (%.1fx)".format(multiplier),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.testTag("anchor-you")
                    )
                }
            }
        }
    }
}
