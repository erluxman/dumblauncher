package com.erluxman.focuslauncher.ui.bestfriends

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
import androidx.compose.foundation.layout.width
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
 * SOCIAL-039 — Best friends list (max 8). Behind FlagKey.BEST_FRIENDS.
 */
@Composable
fun BestFriendsScreen(
    backend: BackendRepository,
    onBack: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val friends by backend.bestFriends.collectAsState(initial = emptyList())
    var uid by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }
    val full = friends.size >= 8

    Surface(
        modifier = Modifier.fillMaxSize().testTag("best-friends"),
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
                    .testTag("best-friends-back")
                    .clickable { onBack() }
                    .padding(8.dp),
            )

            Spacer(Modifier.height(24.dp))
            Text("best friends.", style = displayStyle, color = MinimalTheme.fg)
            Spacer(Modifier.height(8.dp))
            Text(
                "top 8 supportive humans. shown on your profile.",
                style = bodyStyle.copy(fontSize = 14.sp),
                color = MinimalTheme.outline,
            )

            Spacer(Modifier.height(32.dp))
            Text("${friends.size} / 8", style = captionStyle, color = MinimalTheme.outline)
            Spacer(Modifier.height(12.dp))
            if (friends.isEmpty()) {
                Text("none yet.", style = bodyStyle, color = MinimalTheme.outline)
            } else {
                friends.forEach { f ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .testTag("best-friends-row"),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(f.name, style = bodyStyle, color = MinimalTheme.fg)
                            Text(f.uid, style = captionStyle, color = MinimalTheme.outline)
                        }
                        Text("×", style = bodyStyle, color = MinimalTheme.outline,
                            modifier = Modifier
                                .clickable { scope.launch { backend.removeBestFriend(f.uid) } }
                                .padding(8.dp))
                    }
                }
            }

            Spacer(Modifier.height(24.dp))
            if (!full) {
                Text("add", style = captionStyle, color = MinimalTheme.outline)
                Spacer(Modifier.height(8.dp))
                Field(uid, { uid = it.take(40) }, "uid", "best-friends-uid")
                Spacer(Modifier.height(12.dp))
                Field(name, { name = it.take(40) }, "name", "best-friends-name")
                Spacer(Modifier.height(12.dp))
                Text(
                    "add",
                    style = bodyStyle,
                    color = if (uid.isNotBlank() && name.isNotBlank()) MinimalTheme.accent else MinimalTheme.outline,
                    modifier = Modifier
                        .testTag("best-friends-add")
                        .clickable {
                            if (uid.isNotBlank() && name.isNotBlank()) {
                                scope.launch {
                                    val r = backend.addBestFriend(uid.trim(), name.trim())
                                    error = r.exceptionOrNull()?.message
                                    if (r.isSuccess) { uid = ""; name = "" }
                                }
                            }
                        }
                        .padding(vertical = 12.dp),
                )
                error?.let {
                    Spacer(Modifier.height(8.dp))
                    Text(it, style = captionStyle, color = MinimalTheme.accent,
                        modifier = Modifier.testTag("best-friends-error"))
                }
            } else {
                Text("at the cap. remove one to add another.",
                    style = captionStyle, color = MinimalTheme.outline)
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
