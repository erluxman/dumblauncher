package com.erluxman.focuslauncher.ui.notes

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
import com.erluxman.focuslauncher.data.prefs.addParkedIdea
import com.erluxman.focuslauncher.data.prefs.ideaParking
import com.erluxman.focuslauncher.data.prefs.removeParkedIdea
import com.erluxman.focuslauncher.ui.home.minimal.MinimalTheme
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Capture-and-forget idea list. Type, save, free the working memory.
 * Behind FlagKey.IDEA_PARKING.
 */
@Composable
fun IdeaParkingScreen(
    prefs: UserPrefs,
    onBack: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    var text by remember { mutableStateOf("") }
    val ideas by prefs.ideaParking.collectAsState(initial = emptySet())

    val sorted = remember(ideas) {
        ideas.mapNotNull { entry ->
            val parts = entry.split("|", limit = 2)
            val ts = parts.getOrNull(0)?.toLongOrNull() ?: return@mapNotNull null
            val txt = parts.getOrNull(1)?.takeIf { it.isNotBlank() } ?: return@mapNotNull null
            Triple(entry, ts, txt)
        }.sortedByDescending { it.second }
    }

    Surface(
        modifier = Modifier.fillMaxSize().testTag("ideas"),
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
                    .testTag("ideas-back")
                    .clickable { onBack() }
                    .padding(8.dp),
            )

            Spacer(Modifier.height(24.dp))
            Text("park an idea.", style = displayStyle, color = MinimalTheme.fg)
            Spacer(Modifier.height(8.dp))
            Text(
                "type it. forget it. it'll be here when you come back.",
                style = bodyStyle.copy(fontSize = 14.sp),
                color = MinimalTheme.outline,
            )

            Spacer(Modifier.height(32.dp))
            BasicTextField(
                value = text,
                onValueChange = { text = it.take(200) },
                textStyle = bodyStyle.copy(color = MinimalTheme.fg),
                cursorBrush = SolidColor(MinimalTheme.accent),
                modifier = Modifier.fillMaxWidth().testTag("ideas-input"),
                decorationBox = { inner ->
                    Column {
                        if (text.isEmpty()) {
                            Text(
                                "an idea, a thought, a thing to do later",
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
                text = "park it",
                style = bodyStyle,
                color = if (text.isNotBlank()) MinimalTheme.accent else MinimalTheme.outline,
                modifier = Modifier
                    .testTag("ideas-save")
                    .clickable {
                        if (text.isNotBlank()) {
                            scope.launch { prefs.addParkedIdea(text.trim()) }
                            text = ""
                        }
                    }
                    .padding(vertical = 12.dp),
            )

            Spacer(Modifier.height(32.dp))
            Text("parked", style = captionStyle, color = MinimalTheme.outline)
            Spacer(Modifier.height(8.dp))
            if (sorted.isEmpty()) {
                Text("nothing parked.", style = bodyStyle, color = MinimalTheme.outline)
            } else {
                val fmt = SimpleDateFormat("MMM d", Locale.US)
                sorted.forEach { (entry, ts, txt) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp)
                            .testTag("ideas-row"),
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                fmt.format(Date(ts)),
                                style = captionStyle,
                                color = MinimalTheme.outline,
                            )
                            Spacer(Modifier.height(2.dp))
                            Text(txt, style = bodyStyle, color = MinimalTheme.fg)
                        }
                        Text(
                            "×",
                            style = bodyStyle,
                            color = MinimalTheme.outline,
                            modifier = Modifier
                                .clickable { scope.launch { prefs.removeParkedIdea(entry) } }
                                .padding(8.dp),
                        )
                    }
                }
            }
            Spacer(Modifier.height(48.dp))
        }
    }
}

private val displayStyle = TextStyle(fontSize = 32.sp, fontWeight = FontWeight.Normal, letterSpacing = 0.sp)
private val bodyStyle = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Normal, letterSpacing = 0.sp)
private val captionStyle = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Normal)
