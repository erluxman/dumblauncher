package com.erluxman.focuslauncher.ui.relapse

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
 * UNINSTALL-006 — Relapse prediction screen. Shows "94% of users who
 * uninstalled at day N reinstalled in M days." Numbers are a stub model
 * today (function of days-onboarded); real population stats land with
 * Firebase aggregation. Behind FlagKey.RELAPSE.
 */
@Composable
fun RelapsePredictionScreen(
    prefs: UserPrefs,
    onBack: () -> Unit,
) {
    val trackLevel by prefs.trackLevel.collectAsState(initial = 1)
    val onboardingMs by prefs.onboardingCompletedAt.collectAsState(initial = 0L)
    val stat = remember(trackLevel, onboardingMs) {
        GraduateState.compute(trackLevel, onboardingMs)
    }
    val days = stat.daysOnboarded

    // Stub model:
    //   <30 days → high reinstall rate
    //   30..180  → still high (regret peak)
    //   180+     → graduated, low risk both ways
    val reinstallPct = when {
        days < 7 -> 80
        days < 30 -> 92
        days < 90 -> 78
        days < 180 -> 55
        else -> 30
    }
    val daysToReinstall = when {
        days < 30 -> 9
        days < 90 -> 14
        days < 180 -> 22
        else -> 60
    }

    Surface(
        modifier = Modifier.fillMaxSize().testTag("relapse"),
        color = MinimalTheme.bg,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 32.dp),
        ) {
            Spacer(Modifier.height(40.dp))
            Text(
                "← back",
                style = captionStyle,
                color = MinimalTheme.outline,
                modifier = Modifier
                    .testTag("relapse-back")
                    .clickable { onBack() }
                    .padding(8.dp),
            )

            Spacer(Modifier.height(24.dp))
            Text("relapse forecast.", style = displayStyle, color = MinimalTheme.fg)
            Spacer(Modifier.height(8.dp))
            Text(
                "if you uninstall today, here's what the cohort tends to do.",
                style = bodyStyle.copy(fontSize = 14.sp),
                color = MinimalTheme.outline,
            )

            Spacer(Modifier.height(40.dp))
            Text(
                "$reinstallPct%",
                style = TextStyle(fontSize = 80.sp, fontWeight = FontWeight.Normal),
                color = MinimalTheme.accent,
                modifier = Modifier.testTag("relapse-pct"),
            )
            Text(
                "of users who uninstall at day $days reinstall within $daysToReinstall days.",
                style = bodyStyle,
                color = MinimalTheme.fg,
            )

            Spacer(Modifier.height(32.dp))
            Text(
                "model is stubbed until cohort data lands via firebase.",
                style = captionStyle,
                color = MinimalTheme.outline.copy(alpha = 0.6f),
            )
            Spacer(Modifier.height(48.dp))
        }
    }
}

private val displayStyle = TextStyle(fontSize = 32.sp, fontWeight = FontWeight.Normal, letterSpacing = 0.sp)
private val bodyStyle = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Normal, letterSpacing = 0.sp)
private val captionStyle = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Normal)
