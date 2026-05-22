package com.erluxman.focuslauncher.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.erluxman.focuslauncher.service.Deathbed

/**
 * PSYCH-009 Deathbed Simulator card.
 *
 * Lives next to MortalityCard so users who opted into mortality framings
 * see both. Hidden when opt-in is off or no age is set.
 */
@Composable
fun DeathbedCard(
    userAge: Int,
    dailyDistractionMinutes: Int,
    enabled: Boolean,
    modifier: Modifier = Modifier,
) {
    if (!enabled || userAge !in 1..120 || dailyDistractionMinutes <= 0) return
    val years = Deathbed.lifetimeYearsOfWaking(dailyDistractionMinutes, userAge)
    val analogy = Deathbed.analogyFor(years)
    val outline = MaterialTheme.colorScheme.outline

    Column(modifier = modifier.testTag("deathbed-card")) {
        Text(
            "DEATHBED",
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
            Column(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
                Text(
                    "AT TODAY'S RATE, BY 80",
                    style = MaterialTheme.typography.labelSmall,
                    color = outline,
                    letterSpacing = 1.5.sp
                )
                Text(
                    text = "%.1f waking years".format(years),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.testTag("deathbed-years")
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = analogy,
                    style = MaterialTheme.typography.bodySmall,
                    color = outline,
                    modifier = Modifier.testTag("deathbed-analogy")
                )
            }
        }
    }
}
