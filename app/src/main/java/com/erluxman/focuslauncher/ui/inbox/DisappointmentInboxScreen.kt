package com.erluxman.focuslauncher.ui.inbox

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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.erluxman.focuslauncher.backend.BackendRepository
import com.erluxman.focuslauncher.ui.home.minimal.MinimalTheme
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * SOCIAL-020 — Disappointment inbox. Receives pings sent via SOCIAL-005.
 * Behind FlagKey.DISAPPOINTMENT_INBOX.
 */
@Composable
fun DisappointmentInboxScreen(
    backend: BackendRepository,
    onBack: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val items by backend.disappointmentInbox.collectAsState(initial = emptyList())
    val fmt = SimpleDateFormat("MMM d, h:mm a", Locale.US)

    Surface(
        modifier = Modifier.fillMaxSize().testTag("disappointment-inbox"),
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
                    .testTag("inbox-back")
                    .clickable { onBack() }
                    .padding(8.dp),
            )

            Spacer(Modifier.height(24.dp))
            Text("inbox.", style = displayStyle, color = MinimalTheme.fg)
            Spacer(Modifier.height(8.dp))
            Text(
                "incoming disappointment + sad-self pings. read them and sit with it.",
                style = bodyStyle.copy(fontSize = 14.sp),
                color = MinimalTheme.outline,
            )

            Spacer(Modifier.height(24.dp))
            if (items.isEmpty()) {
                Text("nothing.", style = bodyStyle, color = MinimalTheme.outline,
                    modifier = Modifier.testTag("inbox-empty"))
            } else {
                items.sortedByDescending { it.createdAtMs }.forEach { d ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp)
                            .testTag("inbox-row"),
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                if (d.read) "○" else "●",
                                style = bodyStyle,
                                color = if (d.read) MinimalTheme.outline else MinimalTheme.accent,
                            )
                            Text("from ${d.fromName}", style = captionStyle, color = MinimalTheme.outline)
                            Text("·", style = captionStyle, color = MinimalTheme.outline)
                            Text(fmt.format(Date(d.createdAtMs)), style = captionStyle, color = MinimalTheme.outline)
                        }
                        Spacer(Modifier.height(4.dp))
                        Text(d.worstStat, style = bodyStyle, color = MinimalTheme.fg)
                        if (!d.read) {
                            Spacer(Modifier.height(4.dp))
                            Text(
                                "mark read",
                                style = captionStyle,
                                color = MinimalTheme.accent,
                                modifier = Modifier
                                    .clickable { scope.launch { backend.markDisappointmentRead(d.id) } }
                                    .padding(4.dp),
                            )
                        }
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
