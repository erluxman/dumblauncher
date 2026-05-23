package com.erluxman.focuslauncher.ui.dualstreak

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.erluxman.focuslauncher.backend.BackendRepository
import com.erluxman.focuslauncher.ui.home.minimal.MinimalTheme

/**
 * SOCIAL-025 — Dual streak with a chosen partner. Both must hit the daily
 * goal for the shared 🔥 to grow. Reads from BackendRepository.dualStreak.
 *
 * Behind FlagKey.SOCIAL_DUAL_STREAKS.
 */
@Composable
fun DualStreakScreen(
    backend: BackendRepository,
    onBack: () -> Unit,
) {
    var partner by remember { mutableStateOf("") }
    val pairId = partner.trim().lowercase().ifEmpty { "<no partner>" }
    val streak by backend.dualStreak(pairId).collectAsState(
        initial = BackendRepository.DualStreak(0, false, false)
    )

    Surface(
        modifier = Modifier.fillMaxSize().testTag("dual-streak"),
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
                    .testTag("dual-streak-back")
                    .clickable { onBack() }
                    .padding(8.dp),
            )

            Spacer(Modifier.height(24.dp))
            Text("dual streak.", style = displayStyle, color = MinimalTheme.fg)
            Spacer(Modifier.height(8.dp))
            Text(
                "both hit your focus goal → the fire grows. one of you misses → it dies.",
                style = bodyStyle.copy(fontSize = 14.sp),
                color = MinimalTheme.outline,
            )

            Spacer(Modifier.height(32.dp))
            Text("partner", style = captionStyle, color = MinimalTheme.outline)
            Spacer(Modifier.height(6.dp))
            Column(modifier = Modifier.fillMaxWidth()) {
                BasicTextField(
                    value = partner,
                    onValueChange = { partner = it.take(40) },
                    singleLine = true,
                    textStyle = bodyStyle.copy(color = MinimalTheme.fg),
                    cursorBrush = SolidColor(MinimalTheme.accent),
                    modifier = Modifier.fillMaxWidth().testTag("dual-streak-partner-input"),
                    decorationBox = { inner ->
                        if (partner.isEmpty()) {
                            Text(
                                "their uid / handle",
                                style = bodyStyle.copy(fontSize = 16.sp),
                                color = MinimalTheme.outline.copy(alpha = 0.6f),
                            )
                        }
                        inner()
                    },
                )
                Spacer(Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(MinimalTheme.outline.copy(alpha = 0.4f))
                )
            }

            Spacer(Modifier.height(40.dp))
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = "🔥",
                    style = TextStyle(fontSize = 64.sp),
                )
                Spacer(Modifier.width(16.dp))
                Text(
                    text = "${streak.days}",
                    style = TextStyle(fontSize = 80.sp, fontWeight = FontWeight.Normal),
                    color = MinimalTheme.accent,
                    modifier = Modifier.testTag("dual-streak-days"),
                )
                Spacer(Modifier.width(8.dp))
                Text("days", style = bodyStyle, color = MinimalTheme.outline)
            }

            Spacer(Modifier.height(40.dp))
            Status("you, today", streak.youDoneToday, "dual-streak-you")
            Status("them, today", streak.otherDoneToday, "dual-streak-them")

            Spacer(Modifier.height(40.dp))
            Text(
                text = when {
                    streak.youDoneToday && streak.otherDoneToday -> "both done. 🔥 stays."
                    streak.youDoneToday -> "you did your part. waiting on them."
                    streak.otherDoneToday -> "they're done. your turn."
                    else -> "neither of you yet. the day's young."
                },
                style = bodyStyle,
                color = MinimalTheme.fg,
                modifier = Modifier.testTag("dual-streak-verdict"),
            )
            Spacer(Modifier.height(48.dp))
        }
    }
}

@Composable
private fun Status(label: String, done: Boolean, tag: String) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp)) {
        Text(label, style = captionStyle, color = MinimalTheme.outline)
        Spacer(Modifier.height(4.dp))
        Text(
            text = if (done) "● done" else "○ pending",
            style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Normal),
            color = if (done) MinimalTheme.accent else MinimalTheme.outline,
            modifier = Modifier.testTag(tag),
        )
    }
}

private val displayStyle = TextStyle(fontSize = 32.sp, fontWeight = FontWeight.Normal, letterSpacing = 0.sp)
private val bodyStyle = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Normal, letterSpacing = 0.sp)
private val captionStyle = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Normal)
