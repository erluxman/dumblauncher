package com.erluxman.focuslauncher.ui.courage

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
import com.erluxman.focuslauncher.data.prefs.IdentityCategory
import com.erluxman.focuslauncher.data.prefs.UserPrefs
import com.erluxman.focuslauncher.data.prefs.logRejection
import com.erluxman.focuslauncher.data.prefs.logRisk
import com.erluxman.focuslauncher.data.prefs.logThingMade
import com.erluxman.focuslauncher.data.prefs.rejections
import com.erluxman.focuslauncher.data.prefs.removeIdentityEntry
import com.erluxman.focuslauncher.data.prefs.risks
import com.erluxman.focuslauncher.data.prefs.thingsMade
import com.erluxman.focuslauncher.ui.home.minimal.MinimalTheme
import kotlinx.coroutines.launch

/**
 * Combined "courage" screen: IDENT-003 Rejection Counter, IDENT-004
 * Risks-Taken Log, IDENT-005 Things-Made Counter. One screen with
 * three log sections; each entry is "timestampMs|text".
 *
 * Behind FlagKey.COURAGE.
 */
@Composable
fun CourageScreen(
    prefs: UserPrefs,
    onBack: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val rej by prefs.rejections.collectAsState(initial = emptySet())
    val rsk by prefs.risks.collectAsState(initial = emptySet())
    val mad by prefs.thingsMade.collectAsState(initial = emptySet())

    var rejText by remember { mutableStateOf("") }
    var rskText by remember { mutableStateOf("") }
    var madText by remember { mutableStateOf("") }

    Surface(
        modifier = Modifier.fillMaxSize().testTag("courage"),
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
                    .testTag("courage-back")
                    .clickable { onBack() }
                    .padding(8.dp),
            )

            Spacer(Modifier.height(24.dp))
            Text("courage.", style = displayStyle, color = MinimalTheme.fg)
            Spacer(Modifier.height(8.dp))
            Text(
                "rejections asked, risks taken, things made. three counts that compound.",
                style = bodyStyle.copy(fontSize = 14.sp),
                color = MinimalTheme.outline,
            )

            Spacer(Modifier.height(32.dp))
            Section(
                title = "rejections",
                count = rej.size,
                entries = rej,
                inputValue = rejText,
                onInputChange = { rejText = it.take(200) },
                placeholder = "asked, got 'no' — describe",
                tagPrefix = "courage-rej",
                onLog = {
                    if (rejText.isNotBlank()) {
                        scope.launch { prefs.logRejection(rejText.trim()) }
                        rejText = ""
                    }
                },
                onRemove = { entry ->
                    scope.launch { prefs.removeIdentityEntry(IdentityCategory.REJECTION, entry) }
                },
            )

            Spacer(Modifier.height(24.dp))
            Section(
                title = "risks taken",
                count = rsk.size,
                entries = rsk,
                inputValue = rskText,
                onInputChange = { rskText = it.take(200) },
                placeholder = "asked for a raise / sent the email / had the talk",
                tagPrefix = "courage-rsk",
                onLog = {
                    if (rskText.isNotBlank()) {
                        scope.launch { prefs.logRisk(rskText.trim()) }
                        rskText = ""
                    }
                },
                onRemove = { entry ->
                    scope.launch { prefs.removeIdentityEntry(IdentityCategory.RISK, entry) }
                },
            )

            Spacer(Modifier.height(24.dp))
            Section(
                title = "things made",
                count = mad.size,
                entries = mad,
                inputValue = madText,
                onInputChange = { madText = it.take(200) },
                placeholder = "post, recipe, drawing, song, drawer-pull installed…",
                tagPrefix = "courage-mad",
                onLog = {
                    if (madText.isNotBlank()) {
                        scope.launch { prefs.logThingMade(madText.trim()) }
                        madText = ""
                    }
                },
                onRemove = { entry ->
                    scope.launch { prefs.removeIdentityEntry(IdentityCategory.THING_MADE, entry) }
                },
            )

            Spacer(Modifier.height(48.dp))
        }
    }
}

@Composable
private fun Section(
    title: String,
    count: Int,
    entries: Set<String>,
    inputValue: String,
    onInputChange: (String) -> Unit,
    placeholder: String,
    tagPrefix: String,
    onLog: () -> Unit,
    onRemove: (String) -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(title, style = captionStyle, color = MinimalTheme.outline)
            Text("$count", style = TextStyle(fontSize = 32.sp, fontWeight = FontWeight.Normal),
                color = MinimalTheme.accent, modifier = Modifier.testTag("$tagPrefix-count"))
        }
        Spacer(Modifier.height(8.dp))
        BasicTextField(
            value = inputValue,
            onValueChange = onInputChange,
            singleLine = false,
            textStyle = bodyStyle.copy(color = MinimalTheme.fg),
            cursorBrush = SolidColor(MinimalTheme.accent),
            modifier = Modifier.fillMaxWidth().testTag("$tagPrefix-input"),
            decorationBox = { inner ->
                Column {
                    if (inputValue.isEmpty()) {
                        Text(placeholder, style = bodyStyle.copy(fontSize = 14.sp),
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
        Spacer(Modifier.height(8.dp))
        Text(
            "+ log",
            style = bodyStyle,
            color = if (inputValue.isNotBlank()) MinimalTheme.accent else MinimalTheme.outline,
            modifier = Modifier
                .testTag("$tagPrefix-log")
                .clickable { onLog() }
                .padding(vertical = 8.dp),
        )
        if (entries.isNotEmpty()) {
            val recent = entries.sortedByDescending {
                it.substringBefore("|").toLongOrNull() ?: 0
            }.take(3)
            recent.forEach { e ->
                val text = e.substringAfter("|", "")
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).testTag("$tagPrefix-row"),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text("· $text", style = bodyStyle.copy(fontSize = 14.sp), color = MinimalTheme.fg,
                        modifier = Modifier.weight(1f))
                    Text("×", style = bodyStyle, color = MinimalTheme.outline,
                        modifier = Modifier.clickable { onRemove(e) }.padding(start = 8.dp))
                }
            }
        }
    }
}

private val displayStyle = TextStyle(fontSize = 32.sp, fontWeight = FontWeight.Normal, letterSpacing = 0.sp)
private val bodyStyle = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Normal, letterSpacing = 0.sp)
private val captionStyle = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Normal)
