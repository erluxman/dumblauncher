package com.erluxman.focuslauncher.ui.quietbrag

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
import com.erluxman.focuslauncher.backend.BackendRepository
import com.erluxman.focuslauncher.data.prefs.UserPrefs
import com.erluxman.focuslauncher.ui.home.minimal.MinimalTheme
import kotlinx.coroutines.launch

/**
 * SOCIAL-036 — Quiet brag. Finish your One Thing → post it. One-tap from
 * the prompt; auto-suggests the current oneThing text.
 *
 * Behind FlagKey.QUIET_BRAG.
 */
@Composable
fun QuietBragScreen(
    prefs: UserPrefs,
    backend: BackendRepository,
    onBack: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val oneThing by prefs.oneThingText.collectAsState(initial = "")
    var text by remember { mutableStateOf("") }
    var status by remember { mutableStateOf<String?>(null) }
    // Auto-suggest the One Thing if user hasn't typed anything yet.
    val effective = if (text.isBlank()) oneThing else text

    Surface(
        modifier = Modifier.fillMaxSize().testTag("quiet-brag"),
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
                    .testTag("quiet-brag-back")
                    .clickable { onBack() }
                    .padding(8.dp),
            )

            Spacer(Modifier.height(24.dp))
            Text("quiet brag.", style = displayStyle, color = MinimalTheme.fg)
            Spacer(Modifier.height(8.dp))
            Text(
                "you finished the One Thing. say so. 🙏 reactions only — no comments.",
                style = bodyStyle.copy(fontSize = 14.sp),
                color = MinimalTheme.outline,
            )

            if (oneThing.isNotBlank()) {
                Spacer(Modifier.height(16.dp))
                Text("today's one thing", style = captionStyle, color = MinimalTheme.outline)
                Spacer(Modifier.height(4.dp))
                Text(oneThing, style = bodyStyle, color = MinimalTheme.accent,
                    modifier = Modifier.testTag("quiet-brag-one-thing"))
            }

            Spacer(Modifier.height(24.dp))
            BasicTextField(
                value = text,
                onValueChange = { text = it.take(280) },
                textStyle = bodyStyle.copy(color = MinimalTheme.fg),
                cursorBrush = SolidColor(MinimalTheme.accent),
                modifier = Modifier.fillMaxWidth().testTag("quiet-brag-input"),
                decorationBox = { inner ->
                    Column {
                        if (text.isEmpty()) {
                            Text(
                                if (oneThing.isNotBlank()) "override (or leave blank to post the one thing)"
                                else "what did you ship?",
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
                "post 🙏",
                style = bodyStyle,
                color = if (effective.isNotBlank()) MinimalTheme.accent else MinimalTheme.outline,
                modifier = Modifier
                    .testTag("quiet-brag-post")
                    .clickable {
                        if (effective.isNotBlank()) {
                            scope.launch {
                                val r = backend.addPost(effective.trim(), BackendRepository.PostKind.QUIET_BRAG, null)
                                status = if (r.isSuccess) "posted." else "failed: ${r.exceptionOrNull()?.message}"
                                if (r.isSuccess) text = ""
                            }
                        }
                    }
                    .padding(vertical = 12.dp),
            )

            status?.let {
                Spacer(Modifier.height(8.dp))
                Text(it, style = captionStyle, color = MinimalTheme.outline,
                    modifier = Modifier.testTag("quiet-brag-status"))
            }
            Spacer(Modifier.height(48.dp))
        }
    }
}

private val displayStyle = TextStyle(fontSize = 32.sp, fontWeight = FontWeight.Normal, letterSpacing = 0.sp)
private val bodyStyle = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Normal, letterSpacing = 0.sp)
private val captionStyle = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Normal)
