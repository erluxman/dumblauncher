package com.erluxman.focuslauncher.ui.precommit

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
import java.util.Calendar

/**
 * SOCIAL-033 — Public pre-commitments. Write tomorrow's intent; it posts
 * to your feed for friends to see; auto-grades at end of day (server-side,
 * future work). Behind FlagKey.PRE_COMMIT.
 */
@Composable
fun PreCommitScreen(
    backend: BackendRepository,
    onBack: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    var text by remember { mutableStateOf("") }
    var status by remember { mutableStateOf<String?>(null) }

    // Posts expire end of tomorrow.
    val expiresAt = remember {
        Calendar.getInstance().apply {
            add(Calendar.DAY_OF_MONTH, 1)
            set(Calendar.HOUR_OF_DAY, 23); set(Calendar.MINUTE, 59); set(Calendar.SECOND, 59); set(Calendar.MILLISECOND, 0)
        }.timeInMillis
    }

    Surface(
        modifier = Modifier.fillMaxSize().testTag("pre-commit"),
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
                    .testTag("pre-commit-back")
                    .clickable { onBack() }
                    .padding(8.dp),
            )

            Spacer(Modifier.height(24.dp))
            Text("tomorrow.", style = displayStyle, color = MinimalTheme.fg)
            Spacer(Modifier.height(8.dp))
            Text(
                "say it out loud (well, post it). auto-grades when tomorrow ends.",
                style = bodyStyle.copy(fontSize = 14.sp),
                color = MinimalTheme.outline,
            )

            Spacer(Modifier.height(24.dp))
            BasicTextField(
                value = text,
                onValueChange = { text = it.take(240) },
                textStyle = bodyStyle.copy(color = MinimalTheme.fg),
                cursorBrush = SolidColor(MinimalTheme.accent),
                modifier = Modifier.fillMaxWidth().testTag("pre-commit-input"),
                decorationBox = { inner ->
                    Column {
                        if (text.isEmpty()) {
                            Text(
                                "tomorrow i will…",
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
                "post",
                style = bodyStyle,
                color = if (text.isNotBlank()) MinimalTheme.accent else MinimalTheme.outline,
                modifier = Modifier
                    .testTag("pre-commit-post")
                    .clickable {
                        if (text.isNotBlank()) {
                            scope.launch {
                                val r = backend.addPost(text.trim(), BackendRepository.PostKind.PRE_COMMIT, expiresAt)
                                status = if (r.isSuccess) "posted to feed."
                                else "failed: ${r.exceptionOrNull()?.message}"
                                if (r.isSuccess) text = ""
                            }
                        }
                    }
                    .padding(vertical = 12.dp),
            )

            status?.let {
                Spacer(Modifier.height(8.dp))
                Text(it, style = captionStyle, color = MinimalTheme.outline,
                    modifier = Modifier.testTag("pre-commit-status"))
            }
            Spacer(Modifier.height(48.dp))
        }
    }
}

private val displayStyle = TextStyle(fontSize = 32.sp, fontWeight = FontWeight.Normal, letterSpacing = 0.sp)
private val bodyStyle = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Normal, letterSpacing = 0.sp)
private val captionStyle = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Normal)
