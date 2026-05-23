package com.erluxman.focuslauncher.ui.groups

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
import com.erluxman.focuslauncher.backend.BackendRepository
import com.erluxman.focuslauncher.backend.FirebaseInit
import com.erluxman.focuslauncher.ui.home.minimal.MinimalTheme
import kotlinx.coroutines.launch

/**
 * Groups — SOCIAL-001. The accountability primitive everything else builds
 * on. Currently backed by [com.erluxman.focuslauncher.backend.StubBackendRepository];
 * swap for the Firebase impl by flipping FIREBASE_BACKEND.
 *
 * Behind FlagKey.SOCIAL_GROUPS.
 */
@Composable
fun GroupsScreen(
    backend: BackendRepository,
    onBack: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val groups by backend.myGroups.collectAsState(initial = emptyList())
    var newName by remember { mutableStateOf("") }
    var inviteCode by remember { mutableStateOf("") }
    var lastError by remember { mutableStateOf<String?>(null) }

    Surface(
        modifier = Modifier.fillMaxSize().testTag("groups"),
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
                    .testTag("groups-back")
                    .clickable { onBack() }
                    .padding(8.dp),
            )

            Spacer(Modifier.height(24.dp))
            Text("groups.", style = displayStyle, color = MinimalTheme.fg)
            Spacer(Modifier.height(8.dp))
            Text(
                text = if (FirebaseInit.isAvailable) "synced (firebase)" else "local stub — firebase not initialized",
                style = bodyStyle.copy(fontSize = 14.sp),
                color = MinimalTheme.outline,
            )

            Spacer(Modifier.height(32.dp))
            Text("create", style = captionStyle, color = MinimalTheme.outline)
            Spacer(Modifier.height(6.dp))
            MinimalField(newName, { newName = it.take(40) }, "group name", "groups-create-name")
            Spacer(Modifier.height(12.dp))
            Text(
                "create",
                style = bodyStyle,
                color = if (newName.isNotBlank()) MinimalTheme.accent else MinimalTheme.outline,
                modifier = Modifier
                    .testTag("groups-create-save")
                    .clickable {
                        if (newName.isNotBlank()) {
                            scope.launch {
                                val r = backend.createGroup(newName.trim())
                                lastError = r.exceptionOrNull()?.message
                                if (r.isSuccess) newName = ""
                            }
                        }
                    }
                    .padding(vertical = 12.dp),
            )

            Spacer(Modifier.height(24.dp))
            Text("join", style = captionStyle, color = MinimalTheme.outline)
            Spacer(Modifier.height(6.dp))
            MinimalField(inviteCode, { inviteCode = it.take(20) }, "invite code", "groups-join-code")
            Spacer(Modifier.height(12.dp))
            Text(
                "join",
                style = bodyStyle,
                color = if (inviteCode.isNotBlank()) MinimalTheme.accent else MinimalTheme.outline,
                modifier = Modifier
                    .testTag("groups-join-save")
                    .clickable {
                        if (inviteCode.isNotBlank()) {
                            scope.launch {
                                val r = backend.joinGroup(inviteCode.trim())
                                lastError = r.exceptionOrNull()?.message
                                if (r.isSuccess) inviteCode = ""
                            }
                        }
                    }
                    .padding(vertical = 12.dp),
            )

            lastError?.let {
                Spacer(Modifier.height(12.dp))
                Text(it, style = captionStyle, color = MinimalTheme.accent,
                    modifier = Modifier.testTag("groups-error"))
            }

            Spacer(Modifier.height(32.dp))
            Text("your groups", style = captionStyle, color = MinimalTheme.outline)
            Spacer(Modifier.height(8.dp))
            if (groups.isEmpty()) {
                Text("none yet.", style = bodyStyle, color = MinimalTheme.outline)
            } else {
                groups.forEach { g ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp)
                            .testTag("groups-row"),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(g.name, style = bodyStyle, color = MinimalTheme.fg)
                            Text(
                                "${g.memberCount} member${if (g.memberCount == 1) "" else "s"} · " +
                                    "shared streak ${g.sharedStreak}d",
                                style = captionStyle,
                                color = MinimalTheme.outline,
                            )
                        }
                        Text(
                            "leave",
                            style = captionStyle,
                            color = MinimalTheme.outline,
                            modifier = Modifier
                                .clickable {
                                    scope.launch { backend.leaveGroup(g.id) }
                                }
                                .padding(8.dp),
                        )
                    }
                }
            }
            Spacer(Modifier.height(48.dp))
        }
    }
}

@Composable
private fun MinimalField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    tag: String,
) {
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
