package com.erluxman.focuslauncher.ui.graduate

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.erluxman.focuslauncher.data.prefs.UserPrefs
import com.erluxman.focuslauncher.service.tracks.GraduateState
import com.erluxman.focuslauncher.ui.home.minimal.MinimalTheme

/**
 * Graduate Mode progress surface. Shows where the user is on the path to
 * graduating from the friction system, and what's left. Behind
 * FlagKey.GRADUATE_MODE.
 */
@Composable
fun GraduateScreen(
    prefs: UserPrefs,
    onBack: () -> Unit,
) {
    val trackLevel by prefs.trackLevel.collectAsState(initial = 1)
    val onboardingMs by prefs.onboardingCompletedAt.collectAsState(initial = 0L)
    val stat = remember(trackLevel, onboardingMs) {
        GraduateState.compute(trackLevel, onboardingMs)
    }
    val pctDays = (stat.daysOnboarded.toFloat() / GraduateState.DAYS_THRESHOLD).coerceIn(0f, 1f)

    Surface(
        modifier = Modifier.fillMaxSize().testTag("graduate"),
        color = MinimalTheme.bg,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
        ) {
            Spacer(Modifier.height(40.dp))
            Text(
                "← back",
                style = captionStyle,
                color = MinimalTheme.outline,
                modifier = Modifier
                    .testTag("graduate-back")
                    .clickable { onBack() }
                    .padding(8.dp),
            )

            Spacer(Modifier.height(40.dp))
            Text(
                text = if (stat.isGraduate) "graduate." else "graduating.",
                style = displayStyle,
                color = if (stat.isGraduate) MinimalTheme.accent else MinimalTheme.fg,
                modifier = Modifier.testTag("graduate-headline"),
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = if (stat.isGraduate)
                    "you've earned your way out of the friction. it's all on you now."
                else
                    "graduating happens when you've held level ${GraduateState.LEVEL_THRESHOLD} for ${GraduateState.DAYS_THRESHOLD} days.",
                style = bodyStyle.copy(fontSize = 14.sp),
                color = MinimalTheme.outline,
            )

            Spacer(Modifier.height(40.dp))

            Section(
                label = "track level",
                value = "$trackLevel of ${GraduateState.LEVEL_THRESHOLD}",
                tag = "graduate-level",
                emphasis = stat.atTopLevel,
            )

            Spacer(Modifier.height(24.dp))

            Section(
                label = "days onboarded",
                value = "${stat.daysOnboarded} of ${GraduateState.DAYS_THRESHOLD}",
                tag = "graduate-days",
                emphasis = false,
            )

            Spacer(Modifier.height(16.dp))
            // Single-bar progress, no Material progress widget — just a line.
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(MinimalTheme.outline.copy(alpha = 0.3f))
            )
            Spacer(Modifier.height(2.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth(pctDays)
                    .height(2.dp)
                    .background(MinimalTheme.accent)
                    .testTag("graduate-progress-fill")
            )

            Spacer(Modifier.height(40.dp))

            Section(
                label = "days remaining",
                value = "${stat.daysRemaining}",
                tag = "graduate-remaining",
                emphasis = false,
            )

            Spacer(Modifier.height(40.dp))

            if (!stat.atTopLevel) {
                Text(
                    "you're not at the top track yet. promotion happens automatically as your behavior earns it.",
                    style = bodyStyle.copy(fontSize = 14.sp),
                    color = MinimalTheme.outline,
                )
            } else if (!stat.isGraduate) {
                Text(
                    "you're at the top. keep it. the timer is ticking.",
                    style = bodyStyle.copy(fontSize = 14.sp),
                    color = MinimalTheme.outline,
                )
            }
        }
    }
}

@Composable
private fun Section(label: String, value: String, tag: String, emphasis: Boolean) {
    Column {
        Text(label, style = captionStyle, color = MinimalTheme.outline)
        Spacer(Modifier.height(4.dp))
        Text(
            text = value,
            style = TextStyle(fontSize = 28.sp, fontWeight = FontWeight.Normal),
            color = if (emphasis) MinimalTheme.accent else MinimalTheme.fg,
            modifier = Modifier.testTag(tag),
        )
    }
}

private val displayStyle = TextStyle(fontSize = 32.sp, fontWeight = FontWeight.Normal, letterSpacing = 0.sp)
private val bodyStyle = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Normal, letterSpacing = 0.sp)
private val captionStyle = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Normal)
