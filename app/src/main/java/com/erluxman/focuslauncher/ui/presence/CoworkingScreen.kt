package com.erluxman.focuslauncher.ui.presence

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.erluxman.focuslauncher.backend.BackendRepository
import com.erluxman.focuslauncher.ui.home.minimal.MinimalTheme

/**
 * SOCIAL-006 — Co-working presence. Show group members currently in focus
 * mode. Stub seeded with two sample names. Behind FlagKey.COWORKING.
 */
@Composable
fun CoworkingScreen(
    backend: BackendRepository,
    onBack: () -> Unit,
) {
    val presence by backend.coworkingPresence.collectAsState(initial = emptyList())
    val now = System.currentTimeMillis()

    Surface(
        modifier = Modifier.fillMaxSize().testTag("coworking"),
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
                    .testTag("coworking-back")
                    .clickable { onBack() }
                    .padding(8.dp),
            )

            Spacer(Modifier.height(24.dp))
            Text("co-working.", style = displayStyle, color = MinimalTheme.fg)
            Spacer(Modifier.height(8.dp))
            Text(
                "group members currently in focus mode. you're not alone.",
                style = bodyStyle.copy(fontSize = 14.sp),
                color = MinimalTheme.outline,
            )

            Spacer(Modifier.height(32.dp))
            Text("active now (${presence.size})", style = captionStyle, color = MinimalTheme.outline)
            Spacer(Modifier.height(8.dp))
            if (presence.isEmpty()) {
                Text("no one in focus right now.",
                    style = bodyStyle, color = MinimalTheme.outline,
                    modifier = Modifier.testTag("coworking-empty"))
            } else {
                presence.forEach { p ->
                    val mins = ((now - p.inFocusSinceMs) / 60_000L).toInt().coerceAtLeast(0)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp)
                            .testTag("coworking-row"),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Column {
                            Text("● ${p.name}", style = bodyStyle, color = MinimalTheme.fg)
                            Text(p.uid, style = captionStyle, color = MinimalTheme.outline.copy(alpha = 0.6f))
                        }
                        Text("$mins min", style = bodyStyle, color = MinimalTheme.accent)
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(MinimalTheme.outline.copy(alpha = 0.15f))
                    )
                }
            }
            Spacer(Modifier.height(48.dp))
        }
    }
}

private val displayStyle = TextStyle(fontSize = 32.sp, fontWeight = FontWeight.Normal, letterSpacing = 0.sp)
private val bodyStyle = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Normal, letterSpacing = 0.sp)
private val captionStyle = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Normal)
