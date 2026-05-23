package com.erluxman.focuslauncher.ui.uninstallvote

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
import com.erluxman.focuslauncher.ui.home.minimal.MinimalTheme
import kotlinx.coroutines.launch

/**
 * SOCIAL-002 — Group uninstall vote. Request majority approval from a
 * group before you can uninstall. The actual uninstall block is the
 * existing UNINSTALL-001 device-admin chain; this UI just records the
 * request + vote. Behind FlagKey.UNINSTALL_VOTE.
 */
@Composable
fun UninstallVoteScreen(
    backend: BackendRepository,
    onBack: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val groups by backend.myGroups.collectAsState(initial = emptyList())
    var selectedGroup by remember { mutableStateOf<String?>(null) }
    var reason by remember { mutableStateOf("") }
    val vote by (selectedGroup?.let { backend.uninstallVote(it) } ?: kotlinx.coroutines.flow.flowOf(null))
        .collectAsState(initial = null)

    Surface(
        modifier = Modifier.fillMaxSize().testTag("uninstall-vote"),
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
                    .testTag("uninstall-vote-back")
                    .clickable { onBack() }
                    .padding(8.dp),
            )

            Spacer(Modifier.height(24.dp))
            Text("uninstall vote.", style = displayStyle, color = MinimalTheme.fg)
            Spacer(Modifier.height(8.dp))
            Text(
                "you can't quit alone. pick a group, give a reason, post the request.",
                style = bodyStyle.copy(fontSize = 14.sp),
                color = MinimalTheme.outline,
            )

            Spacer(Modifier.height(32.dp))
            if (groups.isEmpty()) {
                Text("(no groups — join or create one in the groups screen first.)",
                    style = bodyStyle, color = MinimalTheme.outline,
                    modifier = Modifier.testTag("uninstall-vote-no-groups"))
            } else {
                Text("group", style = captionStyle, color = MinimalTheme.outline)
                Spacer(Modifier.height(8.dp))
                groups.forEach { g ->
                    val sel = selectedGroup == g.id
                    Text(
                        text = (if (sel) "● " else "○ ") + g.name,
                        style = bodyStyle,
                        color = if (sel) MinimalTheme.accent else MinimalTheme.fg,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("uninstall-vote-group-${g.id}")
                            .clickable { selectedGroup = g.id }
                            .padding(vertical = 8.dp),
                    )
                }

                Spacer(Modifier.height(24.dp))
                Text("reason", style = captionStyle, color = MinimalTheme.outline)
                Spacer(Modifier.height(6.dp))
                BasicTextField(
                    value = reason,
                    onValueChange = { reason = it.take(280) },
                    textStyle = bodyStyle.copy(color = MinimalTheme.fg),
                    cursorBrush = SolidColor(MinimalTheme.accent),
                    modifier = Modifier.fillMaxWidth().testTag("uninstall-vote-reason"),
                    decorationBox = { inner ->
                        Column {
                            if (reason.isEmpty()) {
                                Text("why are you quitting?",
                                    style = bodyStyle.copy(fontSize = 16.sp),
                                    color = MinimalTheme.outline.copy(alpha = 0.6f))
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
                    "request vote",
                    style = bodyStyle,
                    color = if (selectedGroup != null && reason.isNotBlank()) MinimalTheme.accent else MinimalTheme.outline,
                    modifier = Modifier
                        .testTag("uninstall-vote-request")
                        .clickable {
                            val g = selectedGroup ?: return@clickable
                            if (reason.isBlank()) return@clickable
                            scope.launch { backend.requestUninstallVote(g, reason.trim()) }
                        }
                        .padding(vertical = 12.dp),
                )

                vote?.let { v ->
                    Spacer(Modifier.height(24.dp))
                    Text("current vote", style = captionStyle, color = MinimalTheme.outline)
                    Spacer(Modifier.height(4.dp))
                    Text("aye ${v.ayes} · nay ${v.nays}", style = bodyStyle, color = MinimalTheme.fg,
                        modifier = Modifier.testTag("uninstall-vote-tally"))
                    Text(v.reason, style = captionStyle, color = MinimalTheme.outline)
                }
            }
            Spacer(Modifier.height(48.dp))
        }
    }
}

private val displayStyle = TextStyle(fontSize = 32.sp, fontWeight = FontWeight.Normal, letterSpacing = 0.sp)
private val bodyStyle = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Normal, letterSpacing = 0.sp)
private val captionStyle = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Normal)
