package com.erluxman.focuslauncher.ui.sponsor

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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * SOCIAL-008 — Sponsor system (AA-style). Designate one person notified
 * on uninstall attempts; can call you. Behind FlagKey.SPONSOR.
 */
@Composable
fun SponsorScreen(
    backend: BackendRepository,
    onBack: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val sponsor by backend.sponsor.collectAsState(initial = null)
    var uid by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    val ready = uid.isNotBlank() && name.isNotBlank()
    val fmt = SimpleDateFormat("MMM d, yyyy", Locale.US)

    Surface(
        modifier = Modifier.fillMaxSize().testTag("sponsor"),
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
                    .testTag("sponsor-back")
                    .clickable { onBack() }
                    .padding(8.dp),
            )

            Spacer(Modifier.height(24.dp))
            Text("sponsor.", style = displayStyle, color = MinimalTheme.fg)
            Spacer(Modifier.height(8.dp))
            Text(
                "one person, notified on uninstall attempts. they can call you.",
                style = bodyStyle.copy(fontSize = 14.sp),
                color = MinimalTheme.outline,
            )

            Spacer(Modifier.height(32.dp))
            sponsor?.let { s ->
                Text("current", style = captionStyle, color = MinimalTheme.outline)
                Spacer(Modifier.height(4.dp))
                Text(s.name, style = TextStyle(fontSize = 32.sp, fontWeight = FontWeight.Normal),
                    color = MinimalTheme.accent, modifier = Modifier.testTag("sponsor-current-name"))
                Text("uid ${s.uid}", style = captionStyle, color = MinimalTheme.outline)
                Text("since ${fmt.format(Date(s.sinceMs))}", style = captionStyle, color = MinimalTheme.outline)
                Spacer(Modifier.height(12.dp))
                Text(
                    "remove",
                    style = bodyStyle,
                    color = MinimalTheme.outline,
                    modifier = Modifier
                        .testTag("sponsor-remove")
                        .clickable { scope.launch { backend.clearSponsor() } }
                        .padding(vertical = 8.dp),
                )
                Spacer(Modifier.height(24.dp))
            }

            Text(if (sponsor == null) "choose" else "change", style = captionStyle, color = MinimalTheme.outline)
            Spacer(Modifier.height(8.dp))
            Field(uid, { uid = it.take(40) }, "uid", "sponsor-uid")
            Spacer(Modifier.height(12.dp))
            Field(name, { name = it.take(40) }, "name", "sponsor-name")
            Spacer(Modifier.height(16.dp))
            Text(
                "save",
                style = bodyStyle,
                color = if (ready) MinimalTheme.accent else MinimalTheme.outline,
                modifier = Modifier
                    .testTag("sponsor-save")
                    .clickable {
                        if (ready) {
                            scope.launch { backend.setSponsor(uid.trim(), name.trim()) }
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
