package com.erluxman.focuslauncher.ui.feed

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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.erluxman.focuslauncher.backend.BackendRepository
import com.erluxman.focuslauncher.ui.home.minimal.MinimalTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * SOCIAL-016 — chronological feed. No algorithm. Reverse-chronological
 * over Posts emitted by BackendRepository.feed. Filters expired Stories.
 *
 * Behind FlagKey.FEED.
 */
@Composable
fun FeedScreen(
    backend: BackendRepository,
    onBack: () -> Unit,
) {
    val feed by backend.feed.collectAsState(initial = emptyList())
    val now = remember { System.currentTimeMillis() }
    val visible = remember(feed, now) {
        feed.filter { it.expiresAtMs == null || it.expiresAtMs > now }
            .sortedByDescending { it.createdAtMs }
    }

    Surface(
        modifier = Modifier.fillMaxSize().testTag("feed"),
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
                    .testTag("feed-back")
                    .clickable { onBack() }
                    .padding(8.dp),
            )

            Spacer(Modifier.height(24.dp))
            Text("feed.", style = displayStyle, color = MinimalTheme.fg)
            Spacer(Modifier.height(8.dp))
            Text(
                "chronological. no algorithm. expires-at honored.",
                style = bodyStyle.copy(fontSize = 14.sp),
                color = MinimalTheme.outline,
            )

            Spacer(Modifier.height(24.dp))
            if (visible.isEmpty()) {
                Text(
                    "nothing here yet. post a pre-commit or a quiet brag.",
                    style = bodyStyle,
                    color = MinimalTheme.outline,
                    modifier = Modifier.testTag("feed-empty"),
                )
            } else {
                val fmt = SimpleDateFormat("MMM d, h:mm a", Locale.US)
                visible.forEach { p ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp)
                            .testTag("feed-row"),
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            Text(
                                p.kind.name.lowercase().replace("_", " "),
                                style = captionStyle,
                                color = MinimalTheme.accent,
                            )
                            Text("·", style = captionStyle, color = MinimalTheme.outline)
                            Text(fmt.format(Date(p.createdAtMs)), style = captionStyle, color = MinimalTheme.outline)
                            p.expiresAtMs?.let {
                                Text("· expires ${fmt.format(Date(it))}", style = captionStyle,
                                    color = MinimalTheme.outline.copy(alpha = 0.6f))
                            }
                        }
                        Spacer(Modifier.height(4.dp))
                        Text(p.text, style = bodyStyle, color = MinimalTheme.fg)
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
