package com.erluxman.focuslauncher.ui.antibio

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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.erluxman.focuslauncher.data.prefs.UserPrefs
import com.erluxman.focuslauncher.data.prefs.antiBio
import com.erluxman.focuslauncher.data.prefs.setAntiBio
import com.erluxman.focuslauncher.ui.home.minimal.MinimalTheme
import kotlinx.coroutines.launch

/**
 * Anti-bio editor — the single sentence about who you are *not*. 280 chars.
 * Behind FlagKey.ANTI_BIO.
 */
@Composable
fun AntiBioScreen(
    prefs: UserPrefs,
    onBack: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val saved by prefs.antiBio.collectAsState(initial = "")
    var draft by remember { mutableStateOf("") }
    LaunchedEffect(saved) { if (draft.isEmpty()) draft = saved }

    val dirty = draft != saved && draft.isNotBlank()

    Surface(
        modifier = Modifier.fillMaxSize().testTag("anti-bio"),
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
                    .testTag("anti-bio-back")
                    .clickable { onBack() }
                    .padding(8.dp),
            )

            Spacer(Modifier.height(24.dp))
            Text("anti-bio.", style = displayStyle, color = MinimalTheme.fg)
            Spacer(Modifier.height(8.dp))
            Text(
                "the one thing you are not. surfaces at weak moments.",
                style = bodyStyle.copy(fontSize = 14.sp),
                color = MinimalTheme.outline,
            )

            Spacer(Modifier.height(32.dp))
            BasicTextField(
                value = draft,
                onValueChange = { draft = it.take(280) },
                textStyle = bodyStyle.copy(color = MinimalTheme.fg, fontSize = 22.sp),
                cursorBrush = SolidColor(MinimalTheme.accent),
                modifier = Modifier.fillMaxWidth().testTag("anti-bio-input"),
                decorationBox = { inner ->
                    Column {
                        if (draft.isEmpty()) {
                            Text(
                                "i am not the person who scrolls at 1am.",
                                style = bodyStyle.copy(fontSize = 18.sp),
                                color = MinimalTheme.outline.copy(alpha = 0.5f),
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
            Spacer(Modifier.height(8.dp))
            Text(
                "${draft.length} / 280",
                style = captionStyle,
                color = MinimalTheme.outline.copy(alpha = 0.6f),
            )

            Spacer(Modifier.height(16.dp))
            Text(
                "save",
                style = bodyStyle,
                color = if (dirty) MinimalTheme.accent else MinimalTheme.outline,
                modifier = Modifier
                    .testTag("anti-bio-save")
                    .clickable {
                        if (dirty) scope.launch { prefs.setAntiBio(draft.trim()) }
                    }
                    .padding(vertical = 12.dp),
            )

            if (saved.isNotBlank()) {
                Spacer(Modifier.height(32.dp))
                Text("current", style = captionStyle, color = MinimalTheme.outline)
                Spacer(Modifier.height(8.dp))
                Text(
                    text = saved,
                    style = bodyStyle,
                    color = MinimalTheme.fg,
                    modifier = Modifier.testTag("anti-bio-current"),
                )
            }
            Spacer(Modifier.height(48.dp))
        }
    }
}

private val displayStyle = TextStyle(fontSize = 32.sp, fontWeight = FontWeight.Normal, letterSpacing = 0.sp)
private val bodyStyle = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Normal, letterSpacing = 0.sp)
private val captionStyle = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Normal)
