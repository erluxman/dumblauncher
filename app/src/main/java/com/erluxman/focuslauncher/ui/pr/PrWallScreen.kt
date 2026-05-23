package com.erluxman.focuslauncher.ui.pr

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
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.erluxman.focuslauncher.data.prefs.UserPrefs
import com.erluxman.focuslauncher.data.prefs.addPersonalRecord
import com.erluxman.focuslauncher.data.prefs.prWall
import com.erluxman.focuslauncher.data.prefs.removePersonalRecord
import com.erluxman.focuslauncher.ui.home.minimal.MinimalTheme
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Strength / lift PR wall. Manual entry, "isoDate|label|value|unit".
 * Behind FlagKey.PR_WALL.
 */
@Composable
fun PrWallScreen(
    prefs: UserPrefs,
    onBack: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    var label by remember { mutableStateOf("") }
    var value by remember { mutableStateOf("") }
    var unit by remember { mutableStateOf("kg") }
    val raw by prefs.prWall.collectAsState(initial = emptySet())

    val rows = remember(raw) {
        raw.mapNotNull { entry ->
            val parts = entry.split("|", limit = 4)
            if (parts.size < 3) return@mapNotNull null
            val date = parts[0]
            val lbl = parts[1]
            val v = parts[2]
            val u = parts.getOrNull(3).orEmpty()
            Quadruple(entry, date, lbl, "$v $u".trim())
        }.sortedByDescending { it.date }
    }

    Surface(
        modifier = Modifier.fillMaxSize().testTag("pr-wall"),
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
                    .testTag("pr-back")
                    .clickable { onBack() }
                    .padding(8.dp),
            )

            Spacer(Modifier.height(24.dp))
            Text("pr wall.", style = displayStyle, color = MinimalTheme.fg)
            Spacer(Modifier.height(8.dp))
            Text(
                "personal records. lift, run, anything.",
                style = bodyStyle.copy(fontSize = 14.sp),
                color = MinimalTheme.outline,
            )

            Spacer(Modifier.height(24.dp))
            MinimalField("squat / bench / 5k …", label, { label = it.take(40) }, "pr-add-label", KeyboardType.Text)
            Spacer(Modifier.height(12.dp))
            Row {
                MinimalField("value", value, { value = it.take(10) }, "pr-add-value", KeyboardType.Decimal, Modifier.weight(1f))
                Spacer(Modifier.width(16.dp))
                MinimalField("unit", unit, { unit = it.take(8) }, "pr-add-unit", KeyboardType.Text, Modifier.width(96.dp))
            }
            Spacer(Modifier.height(12.dp))
            Text(
                "save",
                style = bodyStyle,
                color = if (label.isNotBlank() && value.isNotBlank()) MinimalTheme.accent else MinimalTheme.outline,
                modifier = Modifier
                    .testTag("pr-save")
                    .clickable {
                        if (label.isNotBlank() && value.isNotBlank()) {
                            val today = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
                            scope.launch { prefs.addPersonalRecord(today, label.trim(), value.trim(), unit.trim()) }
                            label = ""; value = ""
                        }
                    }
                    .padding(vertical = 12.dp),
            )

            Spacer(Modifier.height(24.dp))
            Text("records", style = captionStyle, color = MinimalTheme.outline)
            Spacer(Modifier.height(8.dp))
            if (rows.isEmpty()) {
                Text("none yet.", style = bodyStyle, color = MinimalTheme.outline)
            } else {
                rows.forEach { q ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp)
                            .testTag("pr-row"),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(q.label, style = bodyStyle, color = MinimalTheme.fg)
                            Text(q.date, style = captionStyle, color = MinimalTheme.outline)
                        }
                        Text(q.display, style = bodyStyle, color = MinimalTheme.fg)
                        Spacer(Modifier.width(16.dp))
                        Text(
                            "×",
                            style = bodyStyle,
                            color = MinimalTheme.outline,
                            modifier = Modifier
                                .clickable { scope.launch { prefs.removePersonalRecord(q.entry) } }
                                .padding(8.dp),
                        )
                    }
                }
            }
            Spacer(Modifier.height(48.dp))
        }
    }
}

private data class Quadruple(val entry: String, val date: String, val label: String, val display: String)

@Composable
private fun MinimalField(
    placeholder: String,
    value: String,
    onValueChange: (String) -> Unit,
    tag: String,
    keyboardType: KeyboardType,
    modifier: Modifier = Modifier.fillMaxWidth(),
) {
    Column(modifier = modifier) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            textStyle = bodyStyle.copy(color = MinimalTheme.fg),
            cursorBrush = SolidColor(MinimalTheme.accent),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
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
