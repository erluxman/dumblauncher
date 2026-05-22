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
import com.erluxman.focuslauncher.service.mortality.ActuarialMath

/**
 * LIFE-009 Beach Days Remaining + LIFE-012 Death Clock.
 *
 * Two adjacent stat tiles. Beach Saturdays is the friendlier framing; the
 * raw death-clock is shown smaller and the headline column lets the user
 * pick the frame that motivates them. Hidden unless the user opted in
 * (Transparency → "Mortality widgets") AND has set their age.
 */
@Composable
fun MortalityCard(
    userAge: Int,
    modifier: Modifier = Modifier,
) {
    if (userAge !in 1..120) return
    val days = ActuarialMath.daysRemaining(userAge)
    val beachSaturdays = ActuarialMath.beachSaturdaysRemaining(userAge)
    val nextSeason = ActuarialMath.nextBeachSeasonSaturdays()

    Column(modifier = modifier.testTag("mortality-card")) {
        SectionLabel("MORTALITY")
        Spacer(Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            MortalityTile(
                label = "BEACH SATURDAYS",
                value = "$beachSaturdays",
                sub = "left in your life",
                modifier = Modifier.weight(1f).testTag("mortality-beach-total")
            )
            MortalityTile(
                label = "NEXT SUMMER",
                value = "$nextSeason",
                sub = "Saturdays in Jun–Aug",
                modifier = Modifier.weight(1f).testTag("mortality-beach-next")
            )
        }
        Spacer(Modifier.height(8.dp))
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    "DAYS REMAINING (life expectancy 80)",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.outline,
                    letterSpacing = 1.5.sp
                )
                Text(
                    text = "%,d".format(days),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.testTag("mortality-days")
                )
            }
        }
    }
}

@Composable
private fun MortalityTile(
    label: String,
    value: String,
    sub: String,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.outline,
                letterSpacing = 1.5.sp
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = sub,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}

@Composable
private fun SectionLabel(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.outline,
        letterSpacing = 2.sp
    )
}
