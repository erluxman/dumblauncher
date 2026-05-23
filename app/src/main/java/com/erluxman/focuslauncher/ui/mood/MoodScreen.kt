package com.erluxman.focuslauncher.ui.mood

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
import com.erluxman.focuslauncher.ui.home.minimal.MinimalTheme
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Quick mood ping logger. Tap an emoji, optionally type a note, save.
 * Stores via addMoodPing as "yyyy-MM-dd HH:mm|emoji|note".
 * Behind FlagKey.MOOD_PINGS.
 */
@Composable
fun MoodScreen(
    prefs: UserPrefs,
    onBack: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    var selected by remember { mutableStateOf<String?>(null) }
    var note by remember { mutableStateOf("") }
    val pings by prefs.moodPings.collectAsState(initial = emptySet())

    val recent = remember(pings) {
        pings.sortedDescending().take(20)
    }

    Surface(
        modifier = Modifier.fillMaxSize().testTag("mood"),
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
                    .testTag("mood-back")
                    .clickable { onBack() }
                    .padding(8.dp),
            )

            Spacer(Modifier.height(24.dp))
            Text("mood.", style = displayStyle, color = MinimalTheme.fg)
            Spacer(Modifier.height(8.dp))
            Text(
                "tap. type if you want. saves with a timestamp.",
                style = bodyStyle.copy(fontSize = 14.sp),
                color = MinimalTheme.outline,
            )

            Spacer(Modifier.height(32.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                MOOD_EMOJI.forEach { e ->
                    Text(
                        text = e,
                        style = TextStyle(fontSize = 40.sp),
                        color = if (selected == e) MinimalTheme.accent else MinimalTheme.fg,
                        modifier = Modifier
                            .testTag("mood-emoji-$e")
                            .clickable { selected = if (selected == e) null else e }
                            .padding(8.dp),
                    )
                }
            }

            Spacer(Modifier.height(24.dp))
            BasicTextField(
                value = note,
                onValueChange = { note = it.take(120) },
                singleLine = false,
                textStyle = bodyStyle.copy(color = MinimalTheme.fg),
                cursorBrush = SolidColor(MinimalTheme.accent),
                modifier = Modifier.fillMaxWidth().testTag("mood-note"),
                decorationBox = { inner ->
                    Column {
                        if (note.isEmpty()) {
                            Text(
                                "optional note",
                                style = bodyStyle.copy(fontSize = 16.sp),
                                color = MinimalTheme.outline.copy(alpha = 0.6f),
                            )
                        }
                        inner()
                        Spacer(Modifier.height(4.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .background(MinimalTheme.outline.copy(alpha = 0.4f))
                        )
                    }
                },
            )

            Spacer(Modifier.height(16.dp))
            Text(
                text = "save",
                style = bodyStyle,
                color = if (selected != null) MinimalTheme.accent else MinimalTheme.outline,
                modifier = Modifier
                    .testTag("mood-save")
                    .clickable {
                        val emoji = selected ?: return@clickable
                        val ts = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US).format(Date())
                        scope.launch { prefs.addMoodPing(ts, emoji, note.trim()) }
                        note = ""
                        selected = null
                    }
                    .padding(vertical = 12.dp),
            )

            Spacer(Modifier.height(24.dp))
            Text("recent", style = captionStyle, color = MinimalTheme.outline)
            Spacer(Modifier.height(8.dp))
            if (recent.isEmpty()) {
                Text("nothing yet.", style = bodyStyle, color = MinimalTheme.outline)
            } else {
                recent.forEach { entry ->
                    val parts = entry.split("|", limit = 3)
                    val ts = parts.getOrNull(0) ?: return@forEach
                    val em = parts.getOrNull(1) ?: ""
                    val nt = parts.getOrNull(2).orEmpty()
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .testTag("mood-row"),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(em, style = TextStyle(fontSize = 24.sp))
                        Spacer(Modifier.padding(start = 12.dp))
                        Column {
                            Text(ts, style = captionStyle, color = MinimalTheme.outline)
                            if (nt.isNotBlank()) {
                                Text(nt, style = bodyStyle.copy(fontSize = 14.sp), color = MinimalTheme.fg)
                            }
                        }
                    }
                }
            }
            Spacer(Modifier.height(48.dp))
        }
    }
}

private val MOOD_EMOJI = listOf("😀", "🙂", "😐", "😞", "😩")
private val displayStyle = TextStyle(fontSize = 32.sp, fontWeight = FontWeight.Normal, letterSpacing = 0.sp)
private val bodyStyle = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Normal, letterSpacing = 0.sp)
private val captionStyle = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Normal)
