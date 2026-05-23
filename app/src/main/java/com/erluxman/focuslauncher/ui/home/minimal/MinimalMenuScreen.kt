package com.erluxman.focuslauncher.ui.home.minimal

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Minimal menu — the single fan-out to every feature in the app.
 *
 * Reached from MinimalHomeScreen via the ↓ hint tap, the inline "(open menu)" tap,
 * the settings-dock tap, or a swipe-down gesture. Text rows only, no cards.
 */
@Composable
fun MinimalMenuScreen(
    onBack: () -> Unit,
    onOpenDashboard: () -> Unit,
    onOpenTransparency: () -> Unit,
    onOpenVip: () -> Unit,
    onOpenFocus: () -> Unit,
    onOpenMantra: () -> Unit,
    onOpenBoredom: () -> Unit,
    onOpenBreath: () -> Unit,
    onOpenFutureSelfVideo: () -> Unit,
    onReplayOnboarding: () -> Unit,
    onOpenUninstall: () -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxSize().testTag("minimal-menu"),
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
                text = "← back",
                style = TextStyle(fontSize = 14.sp),
                color = MinimalTheme.outline,
                modifier = Modifier
                    .testTag("menu-back")
                    .clickable { onBack() }
                    .padding(8.dp),
            )

            Spacer(Modifier.height(24.dp))
            Text(
                text = "everything.",
                style = displayStyle,
                color = MinimalTheme.fg,
            )
            Spacer(Modifier.height(24.dp))

            MenuRow("dashboard", "all your data, the legacy cards", "menu-dashboard", onOpenDashboard)
            MenuRow("transparency", "opt out of any technique", "menu-transparency", onOpenTransparency)
            MenuRow("vip contacts", "who can still reach you", "menu-vip", onOpenVip)
            MenuRow("focus timer", "25 · 5 pomodoro", "menu-focus", onOpenFocus)
            MenuRow("mantra", "phrase that unlocks apps", "menu-mantra", onOpenMantra)
            MenuRow("boredom", "two minutes with nothing", "menu-boredom", onOpenBoredom)
            MenuRow("breath unlock", "4 · 7 · 8", "menu-breath", onOpenBreath)
            MenuRow("future self", "record a video to future you", "menu-future-self", onOpenFutureSelfVideo)
            MenuRow("replay onboarding", "redo the setup", "menu-onboarding", onReplayOnboarding)

            Spacer(Modifier.height(48.dp))
            Text(
                text = "uninstall",
                style = bodyStyle,
                color = MinimalTheme.outline,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("menu-uninstall")
                    .clickable { onOpenUninstall() }
                    .padding(vertical = 12.dp),
            )
            Spacer(Modifier.height(64.dp))
        }
    }
}

@Composable
private fun MenuRow(
    title: String,
    subtitle: String,
    testTag: String,
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .testTag(testTag)
            .clickable { onClick() }
            .padding(vertical = 12.dp),
    ) {
        Text(title, style = bodyStyle, color = MinimalTheme.fg)
        Spacer(Modifier.height(2.dp))
        Text(subtitle, style = TextStyle(fontSize = 14.sp), color = MinimalTheme.outline)
    }
}

private val displayStyle = TextStyle(fontSize = 32.sp, fontWeight = FontWeight.Normal, letterSpacing = 0.sp)
private val bodyStyle = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Normal, letterSpacing = 0.sp)
