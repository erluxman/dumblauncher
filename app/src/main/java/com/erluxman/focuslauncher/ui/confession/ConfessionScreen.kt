package com.erluxman.focuslauncher.ui.confession

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
import com.erluxman.focuslauncher.ui.home.minimal.MinimalTheme
import kotlinx.coroutines.launch

/**
 * SOCIAL-023 — Confession booth. Anonymous post of today's failure to the
 * community. Behind FlagKey.CONFESSION.
 */
@Composable
fun ConfessionScreen(
    backend: BackendRepository,
    onBack: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    var text by remember { mutableStateOf("") }
    var status by remember { mutableStateOf<String?>(null) }

    Surface(
        modifier = Modifier.fillMaxSize().testTag("confession"),
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
                    .testTag("confession-back")
                    .clickable { onBack() }
                    .padding(8.dp),
            )

            Spacer(Modifier.height(24.dp))
            Text("confession.", style = displayStyle, color = MinimalTheme.fg)
            Spacer(Modifier.height(8.dp))
            Text(
                "anonymous. posted to the community, no name attached. you'll get sticker reactions back.",
                style = bodyStyle.copy(fontSize = 14.sp),
                color = MinimalTheme.outline,
            )

            Spacer(Modifier.height(24.dp))
            BasicTextField(
                value = text,
                onValueChange = { text = it.take(500) },
                textStyle = bodyStyle.copy(color = MinimalTheme.fg),
                cursorBrush = SolidColor(MinimalTheme.accent),
                modifier = Modifier.fillMaxWidth().testTag("confession-input"),
                decorationBox = { inner ->
                    Column {
                        if (text.isEmpty()) {
                            Text(
                                "today i…",
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
                "post anonymously",
                style = bodyStyle,
                color = if (text.isNotBlank()) MinimalTheme.accent else MinimalTheme.outline,
                modifier = Modifier
                    .testTag("confession-post")
                    .clickable {
                        if (text.isNotBlank()) {
                            scope.launch {
                                // Posts as STORY with 24h expiry, no author identity surfaced.
                                val expiresAt = System.currentTimeMillis() + 86_400_000L
                                val r = backend.addPost(text.trim(), BackendRepository.PostKind.STORY, expiresAt)
                                status = if (r.isSuccess) "confessed. expires in 24h." else "failed: ${r.exceptionOrNull()?.message}"
                                if (r.isSuccess) text = ""
                            }
                        }
                    }
                    .padding(vertical = 12.dp),
            )

            status?.let {
                Spacer(Modifier.height(8.dp))
                Text(it, style = captionStyle, color = MinimalTheme.outline,
                    modifier = Modifier.testTag("confession-status"))
            }
            Spacer(Modifier.height(48.dp))
        }
    }
}

private val displayStyle = TextStyle(fontSize = 32.sp, fontWeight = FontWeight.Normal, letterSpacing = 0.sp)
private val bodyStyle = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Normal, letterSpacing = 0.sp)
private val captionStyle = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Normal)
