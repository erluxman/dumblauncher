package com.erluxman.focuslauncher.ui.lastcontact

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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.erluxman.focuslauncher.data.prefs.UserPrefs
import com.erluxman.focuslauncher.data.prefs.lastContactedLog
import com.erluxman.focuslauncher.data.prefs.logContacted
import com.erluxman.focuslauncher.data.prefs.removeContacted
import com.erluxman.focuslauncher.ui.home.minimal.MinimalTheme
import kotlinx.coroutines.launch

/**
 * PRM-002 — Last-contacted list. Track humans + days since you last spoke.
 * Behind FlagKey.LAST_CONTACTED.
 */
@Composable
fun LastContactedScreen(
    prefs: UserPrefs,
    onBack: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val raw by prefs.lastContactedLog.collectAsState(initial = emptySet())
    var name by remember { mutableStateOf("") }
    val now = System.currentTimeMillis()

    val parsed = remember(raw) {
        raw.mapNotNull { e ->
            val parts = e.split("|", limit = 2)
            val ts = parts.getOrNull(0)?.toLongOrNull() ?: return@mapNotNull null
            val n = parts.getOrNull(1)?.takeIf { it.isNotBlank() } ?: return@mapNotNull null
            Triple(e, ts, n)
        }.sortedBy { it.second }   // oldest first = most overdue first
    }

    Surface(
        modifier = Modifier.fillMaxSize().testTag("last-contacted"),
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
                    .testTag("last-contacted-back")
                    .clickable { onBack() }
                    .padding(8.dp),
            )

            Spacer(Modifier.height(24.dp))
            Text("last contacted.", style = displayStyle, color = MinimalTheme.fg)
            Spacer(Modifier.height(8.dp))
            Text(
                "log when you talked. the days-since column nudges you.",
                style = bodyStyle.copy(fontSize = 14.sp),
                color = MinimalTheme.outline,
            )

            Spacer(Modifier.height(24.dp))
            Field(name, { name = it.take(40) }, "name", "last-contacted-name")
            Spacer(Modifier.height(12.dp))
            Text(
                "logged it now",
                style = bodyStyle,
                color = if (name.isNotBlank()) MinimalTheme.accent else MinimalTheme.outline,
                modifier = Modifier
                    .testTag("last-contacted-log")
                    .clickable {
                        if (name.isNotBlank()) {
                            scope.launch { prefs.logContacted(name.trim()) }
                            name = ""
                        }
                    }
                    .padding(vertical = 12.dp),
            )

            Spacer(Modifier.height(24.dp))
            Text("by days-since", style = captionStyle, color = MinimalTheme.outline)
            Spacer(Modifier.height(8.dp))
            if (parsed.isEmpty()) {
                Text("none.", style = bodyStyle, color = MinimalTheme.outline)
            } else {
                parsed.forEach { (entry, ts, n) ->
                    val days = ((now - ts) / 86_400_000L).toInt()
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp).testTag("last-contacted-row"),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(n, style = bodyStyle, color = MinimalTheme.fg)
                            Text(
                                text = when {
                                    days == 0 -> "today"
                                    days == 1 -> "yesterday"
                                    days < 7 -> "$days days ago"
                                    days < 30 -> "$days days — overdue"
                                    else -> "$days days — call them"
                                },
                                style = captionStyle,
                                color = if (days >= 30) MinimalTheme.accent else MinimalTheme.outline,
                            )
                        }
                        Text("×", style = bodyStyle, color = MinimalTheme.outline,
                            modifier = Modifier
                                .clickable { scope.launch { prefs.removeContacted(entry) } }
                                .padding(8.dp))
                    }
                }
            }
            Spacer(Modifier.height(48.dp))
        }
    }
}

@Composable
private fun Field(value: String, onValueChange: (String) -> Unit, placeholder: String, tag: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            textStyle = bodyStyle.copy(color = MinimalTheme.fg),
            cursorBrush = SolidColor(MinimalTheme.accent),
            modifier = Modifier.fillMaxWidth().testTag(tag),
            decorationBox = { inner ->
                if (value.isEmpty()) {
                    Text(placeholder, style = bodyStyle, color = MinimalTheme.outline.copy(alpha = 0.6f))
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
}

private val displayStyle = TextStyle(fontSize = 32.sp, fontWeight = FontWeight.Normal, letterSpacing = 0.sp)
private val bodyStyle = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Normal, letterSpacing = 0.sp)
private val captionStyle = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Normal)
