package com.erluxman.focuslauncher.ui.fasting

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.erluxman.focuslauncher.data.prefs.UserPrefs
import com.erluxman.focuslauncher.data.prefs.lastMealAtMs
import com.erluxman.focuslauncher.data.prefs.setLastMealNow
import com.erluxman.focuslauncher.data.prefs.clearLastMeal
import com.erluxman.focuslauncher.ui.home.minimal.MinimalTheme
import kotlinx.coroutines.launch

/**
 * NUT-002 — Fasting window auto-detect. Records "I just ate" timestamps;
 * derives the current fasting window as `now - lastMealAt`. Behind FASTING.
 */
@Composable
fun FastingScreen(
    prefs: UserPrefs,
    onBack: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val lastAt by prefs.lastMealAtMs.collectAsState(initial = 0L)
    val now = remember { System.currentTimeMillis() }
    val elapsedMs = if (lastAt > 0) (now - lastAt).coerceAtLeast(0L) else 0L
    val elapsedH = (elapsedMs / 3_600_000L).toInt()
    val elapsedM = ((elapsedMs % 3_600_000L) / 60_000L).toInt()

    Surface(
        modifier = Modifier.fillMaxSize().testTag("fasting"),
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
                    .testTag("fasting-back")
                    .clickable { onBack() }
                    .padding(8.dp),
            )

            Spacer(Modifier.height(24.dp))
            Text("fasting.", style = displayStyle, color = MinimalTheme.fg)
            Spacer(Modifier.height(8.dp))
            Text(
                "tap when you eat. the window auto-counts.",
                style = bodyStyle.copy(fontSize = 14.sp),
                color = MinimalTheme.outline,
            )

            Spacer(Modifier.height(32.dp))
            Text("current window", style = captionStyle, color = MinimalTheme.outline)
            Spacer(Modifier.height(4.dp))
            Text(
                text = if (lastAt == 0L) "—" else "${elapsedH}h ${elapsedM}m",
                style = TextStyle(fontSize = 64.sp, fontWeight = FontWeight.Normal),
                color = if (elapsedH >= 14) MinimalTheme.accent else MinimalTheme.fg,
                modifier = Modifier.testTag("fasting-elapsed"),
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = when {
                    lastAt == 0L -> "no last-meal logged yet."
                    elapsedH >= 16 -> "you're past 16h — that's IF territory."
                    elapsedH >= 12 -> "12h+ — solid overnight fast."
                    elapsedH >= 4 -> "between meals."
                    else -> "just ate."
                },
                style = captionStyle,
                color = MinimalTheme.outline,
            )

            Spacer(Modifier.height(32.dp))
            Text(
                "i just ate",
                style = bodyStyle,
                color = MinimalTheme.accent,
                modifier = Modifier
                    .testTag("fasting-just-ate")
                    .clickable { scope.launch { prefs.setLastMealNow() } }
                    .padding(vertical = 12.dp),
            )
            if (lastAt > 0L) {
                Text(
                    "clear",
                    style = captionStyle,
                    color = MinimalTheme.outline,
                    modifier = Modifier
                        .testTag("fasting-clear")
                        .clickable { scope.launch { prefs.clearLastMeal() } }
                        .padding(vertical = 8.dp),
                )
            }
            Spacer(Modifier.height(48.dp))
        }
    }
}

private val displayStyle = TextStyle(fontSize = 32.sp, fontWeight = FontWeight.Normal, letterSpacing = 0.sp)
private val bodyStyle = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Normal, letterSpacing = 0.sp)
private val captionStyle = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Normal)
