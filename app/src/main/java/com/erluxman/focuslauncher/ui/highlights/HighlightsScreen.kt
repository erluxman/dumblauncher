package com.erluxman.focuslauncher.ui.highlights

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
import com.erluxman.focuslauncher.data.prefs.addHighlight
import com.erluxman.focuslauncher.data.prefs.highlights
import com.erluxman.focuslauncher.data.prefs.removeHighlight
import com.erluxman.focuslauncher.ui.home.minimal.MinimalTheme
import kotlinx.coroutines.launch

/**
 * Free-form quote / highlight capture. Behind FlagKey.HIGHLIGHTS.
 */
@Composable
fun HighlightsScreen(
    prefs: UserPrefs,
    onBack: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    var text by remember { mutableStateOf("") }
    val items by prefs.highlights.collectAsState(initial = emptySet())

    val sorted = remember(items) { items.toList() }

    Surface(
        modifier = Modifier.fillMaxSize().testTag("highlights"),
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
                    .testTag("highlights-back")
                    .clickable { onBack() }
                    .padding(8.dp),
            )

            Spacer(Modifier.height(24.dp))
            Text("highlights.", style = displayStyle, color = MinimalTheme.fg)
            Spacer(Modifier.height(8.dp))
            Text(
                "lines you want to keep.",
                style = bodyStyle.copy(fontSize = 14.sp),
                color = MinimalTheme.outline,
            )

            Spacer(Modifier.height(24.dp))
            BasicTextField(
                value = text,
                onValueChange = { text = it.take(500) },
                textStyle = bodyStyle.copy(color = MinimalTheme.fg),
                cursorBrush = SolidColor(MinimalTheme.accent),
                modifier = Modifier.fillMaxWidth().testTag("highlights-input"),
                decorationBox = { inner ->
                    Column {
                        if (text.isEmpty()) {
                            Text(
                                "paste a quote, a line, a thought",
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
                "keep",
                style = bodyStyle,
                color = if (text.isNotBlank()) MinimalTheme.accent else MinimalTheme.outline,
                modifier = Modifier
                    .testTag("highlights-save")
                    .clickable {
                        if (text.isNotBlank()) {
                            scope.launch { prefs.addHighlight(text.trim()) }
                            text = ""
                        }
                    }
                    .padding(vertical = 12.dp),
            )

            Spacer(Modifier.height(24.dp))
            if (sorted.isEmpty()) {
                Text("nothing kept.", style = bodyStyle, color = MinimalTheme.outline)
            } else {
                sorted.forEach { txt ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp)
                            .testTag("highlights-row"),
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(
                            txt,
                            style = bodyStyle,
                            color = MinimalTheme.fg,
                            modifier = Modifier.weight(1f),
                        )
                        Text(
                            "×",
                            style = bodyStyle,
                            color = MinimalTheme.outline,
                            modifier = Modifier
                                .clickable { scope.launch { prefs.removeHighlight(txt) } }
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
