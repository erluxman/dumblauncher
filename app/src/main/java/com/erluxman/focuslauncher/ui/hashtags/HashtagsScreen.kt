package com.erluxman.focuslauncher.ui.hashtags

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import com.erluxman.focuslauncher.backend.BackendRepository
import com.erluxman.focuslauncher.ui.home.minimal.MinimalTheme
import kotlinx.coroutines.launch

/**
 * SOCIAL-019 — Hashtag tracks. Public joinable challenges like #75hard.
 * Behind FlagKey.HASHTAGS.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HashtagsScreen(
    backend: BackendRepository,
    onBack: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val joined by backend.joinedHashtags.collectAsState(initial = emptySet())
    var custom by remember { mutableStateOf("") }
    val popular = listOf("75hard", "buildinpublic", "shipdaily", "100daysofcode", "mileaday")

    Surface(
        modifier = Modifier.fillMaxSize().testTag("hashtags"),
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
                    .testTag("hashtags-back")
                    .clickable { onBack() }
                    .padding(8.dp),
            )

            Spacer(Modifier.height(24.dp))
            Text("hashtag tracks.", style = displayStyle, color = MinimalTheme.fg)
            Spacer(Modifier.height(8.dp))
            Text(
                "public joinable challenges. show up in the same leaderboards.",
                style = bodyStyle.copy(fontSize = 14.sp),
                color = MinimalTheme.outline,
            )

            Spacer(Modifier.height(32.dp))
            Text("joined", style = captionStyle, color = MinimalTheme.outline)
            Spacer(Modifier.height(8.dp))
            if (joined.isEmpty()) {
                Text("none yet.", style = bodyStyle, color = MinimalTheme.outline)
            } else {
                joined.forEach { tag ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                            .testTag("hashtags-joined-row"),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text("#$tag", style = bodyStyle, color = MinimalTheme.accent)
                        Text("leave", style = captionStyle, color = MinimalTheme.outline,
                            modifier = Modifier
                                .clickable { scope.launch { backend.leaveHashtag(tag) } }
                                .padding(8.dp))
                    }
                }
            }

            Spacer(Modifier.height(24.dp))
            Text("popular", style = captionStyle, color = MinimalTheme.outline)
            Spacer(Modifier.height(8.dp))
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                popular.forEachIndexed { i, tag ->
                    val in_ = joined.contains(tag)
                    Text(
                        "#$tag" + if (in_) " ✓" else "",
                        style = bodyStyle.copy(fontSize = 16.sp),
                        color = if (in_) MinimalTheme.outline else MinimalTheme.accent,
                        modifier = Modifier
                            .testTag("hashtags-popular-$i")
                            .clickable {
                                scope.launch {
                                    if (in_) backend.leaveHashtag(tag) else backend.joinHashtag(tag)
                                }
                            }
                            .padding(8.dp),
                    )
                }
            }

            Spacer(Modifier.height(24.dp))
            Text("custom", style = captionStyle, color = MinimalTheme.outline)
            Spacer(Modifier.height(8.dp))
            BasicTextField(
                value = custom,
                onValueChange = { custom = it.take(30) },
                singleLine = true,
                textStyle = bodyStyle.copy(color = MinimalTheme.fg),
                cursorBrush = SolidColor(MinimalTheme.accent),
                modifier = Modifier.fillMaxWidth().testTag("hashtags-custom-input"),
                decorationBox = { inner ->
                    Column {
                        if (custom.isEmpty()) {
                            Text("yourtag", style = bodyStyle, color = MinimalTheme.outline.copy(alpha = 0.6f))
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
            Spacer(Modifier.height(12.dp))
            Text(
                "join",
                style = bodyStyle,
                color = if (custom.isNotBlank()) MinimalTheme.accent else MinimalTheme.outline,
                modifier = Modifier
                    .testTag("hashtags-custom-join")
                    .clickable {
                        if (custom.isNotBlank()) {
                            scope.launch { backend.joinHashtag(custom.trim()) }
                            custom = ""
                        }
                    }
                    .padding(vertical = 12.dp),
            )
            Spacer(Modifier.height(48.dp))
        }
    }
}

private val displayStyle = TextStyle(fontSize = 32.sp, fontWeight = FontWeight.Normal, letterSpacing = 0.sp)
private val bodyStyle = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Normal, letterSpacing = 0.sp)
private val captionStyle = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Normal)
