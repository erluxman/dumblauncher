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
import com.erluxman.focuslauncher.service.tracks.GraduateState

@Composable
fun GraduateBanner(
    state: GraduateState.Stat,
    modifier: Modifier = Modifier,
) {
    // Show only at top level or when actually graduated.
    if (!state.atTopLevel) return
    val outline = MaterialTheme.colorScheme.outline

    Column(modifier = modifier.testTag("graduate-banner")) {
        Text(
            "GRADUATE TRACK",
            style = MaterialTheme.typography.labelLarge,
            color = outline,
            letterSpacing = 2.sp
        )
        Spacer(Modifier.height(8.dp))
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.18f),
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = if (state.isGraduate) "Graduate." else "${state.daysRemaining}d to graduate",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.testTag("graduate-headline")
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = if (state.isGraduate) "Restrictions will quietly fade. You don't need the friction anymore."
                    else "Stay at Level ${com.erluxman.focuslauncher.service.tracks.GraduateState.LEVEL_THRESHOLD} until ${com.erluxman.focuslauncher.service.tracks.GraduateState.DAYS_THRESHOLD} days from onboarding.",
                    style = MaterialTheme.typography.bodySmall,
                    color = outline,
                )
            }
        }
    }
}
