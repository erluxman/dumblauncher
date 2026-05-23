package com.erluxman.focuslauncher.ui.family

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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * FAMILY-001 — Parent / Child pair. Parent app sees child stats and can
 * approve/deny apps. Behind FlagKey.FAMILY.
 */
@Composable
fun FamilyScreen(
    backend: BackendRepository,
    onBack: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val pair by backend.familyPair.collectAsState(initial = null)
    var uid by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var role by remember { mutableStateOf(BackendRepository.FamilyRole.PARENT) }
    val fmt = SimpleDateFormat("MMM d, yyyy", Locale.US)
    val ready = uid.isNotBlank() && name.isNotBlank()

    Surface(
        modifier = Modifier.fillMaxSize().testTag("family"),
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
                    .testTag("family-back")
                    .clickable { onBack() }
                    .padding(8.dp),
            )

            Spacer(Modifier.height(24.dp))
            Text("family.", style = displayStyle, color = MinimalTheme.fg)
            Spacer(Modifier.height(8.dp))
            Text(
                "pair one parent with one child. parent sees stats + approves apps.",
                style = bodyStyle.copy(fontSize = 14.sp),
                color = MinimalTheme.outline,
            )

            Spacer(Modifier.height(32.dp))
            pair?.let { p ->
                Text(
                    text = if (p.role == BackendRepository.FamilyRole.PARENT) "you are parent to" else "you are child of",
                    style = captionStyle, color = MinimalTheme.outline,
                )
                Spacer(Modifier.height(4.dp))
                Text(p.name,
                    style = TextStyle(fontSize = 32.sp, fontWeight = FontWeight.Normal),
                    color = MinimalTheme.accent, modifier = Modifier.testTag("family-name"))
                Text("uid ${p.uid}", style = captionStyle, color = MinimalTheme.outline)
                Text("since ${fmt.format(Date(p.sinceMs))}", style = captionStyle, color = MinimalTheme.outline)
                Spacer(Modifier.height(16.dp))
                Text(
                    "unpair",
                    style = bodyStyle,
                    color = MinimalTheme.outline,
                    modifier = Modifier
                        .testTag("family-unpair")
                        .clickable { scope.launch { backend.unpairFamily() } }
                        .padding(vertical = 8.dp),
                )
                Spacer(Modifier.height(24.dp))
                return@Column
            }

            Text("pair", style = captionStyle, color = MinimalTheme.outline)
            Spacer(Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                BackendRepository.FamilyRole.entries.forEach { r ->
                    Text(
                        r.name.lowercase(),
                        style = bodyStyle,
                        color = if (role == r) MinimalTheme.accent else MinimalTheme.outline,
                        modifier = Modifier
                            .testTag("family-role-${r.name}")
                            .clickable { role = r }
                            .padding(vertical = 6.dp, horizontal = 10.dp),
                    )
                }
            }
            Spacer(Modifier.height(12.dp))
            Field(uid, { uid = it.take(40) }, "their uid", "family-uid")
            Spacer(Modifier.height(12.dp))
            Field(name, { name = it.take(40) }, "their name", "family-name-input")
            Spacer(Modifier.height(16.dp))
            Text(
                "pair",
                style = bodyStyle,
                color = if (ready) MinimalTheme.accent else MinimalTheme.outline,
                modifier = Modifier
                    .testTag("family-pair")
                    .clickable {
                        if (ready) {
                            scope.launch { backend.pairFamily(uid.trim(), name.trim(), role) }
                            uid = ""; name = ""
                        }
                    }
                    .padding(vertical = 12.dp),
            )
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
