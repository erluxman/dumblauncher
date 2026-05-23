package com.erluxman.focuslauncher.ui.readingthinking

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.erluxman.focuslauncher.data.prefs.UserPrefs
import com.erluxman.focuslauncher.data.prefs.highlights
import com.erluxman.focuslauncher.ui.home.minimal.MinimalTheme

/**
 * MIND-007 — Reading-as-thinking loop. Surface a random highlight + a
 * journal prompt to interrogate it. Pulls from existing READ-002
 * highlights set. Behind FlagKey.READING_THINKING.
 */
@Composable
fun ReadingThinkingScreen(
    prefs: UserPrefs,
    onBack: () -> Unit,
) {
    val items by prefs.highlights.collectAsState(initial = emptySet())
    var seed by remember { mutableIntStateOf(0) }
    val list = remember(items) { items.toList().sorted() }
    val chosen = remember(items, seed) {
        if (list.isEmpty()) null
        else list[((seed % list.size) + list.size) % list.size]
    }
    val prompts = listOf(
        "where in your life does this not yet apply?",
        "if this is true, what should you change today?",
        "what would the opposite belief cost you?",
        "explain it to a 10-year-old in one sentence.",
        "what's the smallest experiment you could run?",
    )
    val prompt = if (list.isEmpty()) prompts[0]
    else prompts[((seed % prompts.size) + prompts.size) % prompts.size]

    Surface(
        modifier = Modifier.fillMaxSize().testTag("reading-thinking"),
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
                    .testTag("reading-thinking-back")
                    .clickable { onBack() }
                    .padding(8.dp),
            )

            Spacer(Modifier.height(24.dp))
            Text("reading as thinking.", style = displayStyle, color = MinimalTheme.fg)
            Spacer(Modifier.height(8.dp))
            Text(
                "one of your highlights + a question to test it against.",
                style = bodyStyle.copy(fontSize = 14.sp),
                color = MinimalTheme.outline,
            )

            Spacer(Modifier.height(32.dp))
            if (chosen == null) {
                Text("no highlights yet. capture some in the highlights screen.",
                    style = bodyStyle, color = MinimalTheme.outline,
                    modifier = Modifier.testTag("reading-thinking-empty"))
            } else {
                Text("highlight", style = captionStyle, color = MinimalTheme.outline)
                Spacer(Modifier.height(4.dp))
                Text(chosen, style = bodyStyle, color = MinimalTheme.fg,
                    modifier = Modifier.testTag("reading-thinking-highlight"))

                Spacer(Modifier.height(24.dp))
                Text("prompt", style = captionStyle, color = MinimalTheme.outline)
                Spacer(Modifier.height(4.dp))
                Text(prompt, style = bodyStyle, color = MinimalTheme.accent,
                    modifier = Modifier.testTag("reading-thinking-prompt"))
            }

            Spacer(Modifier.height(32.dp))
            Text(
                "shuffle",
                style = bodyStyle,
                color = MinimalTheme.accent,
                modifier = Modifier
                    .testTag("reading-thinking-shuffle")
                    .clickable { seed += 1 }
                    .padding(vertical = 12.dp),
            )
            Spacer(Modifier.height(48.dp))
        }
    }
}

private val displayStyle = TextStyle(fontSize = 32.sp, fontWeight = FontWeight.Normal, letterSpacing = 0.sp)
private val bodyStyle = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Normal, letterSpacing = 0.sp)
private val captionStyle = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Normal)
