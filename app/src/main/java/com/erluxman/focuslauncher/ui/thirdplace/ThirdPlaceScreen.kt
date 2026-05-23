package com.erluxman.focuslauncher.ui.thirdplace

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
import com.erluxman.focuslauncher.data.prefs.logThirdPlace
import com.erluxman.focuslauncher.data.prefs.removeThirdPlace
import com.erluxman.focuslauncher.data.prefs.thirdPlaces
import com.erluxman.focuslauncher.ui.home.minimal.MinimalTheme
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * LOC-003 — Third-place tracker. Distinct non-home/work places visited
 * ≥30 min/week. Loneliness indicator. Behind FlagKey.THIRD_PLACE.
 */
@Composable
fun ThirdPlaceScreen(
    prefs: UserPrefs,
    onBack: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val raw by prefs.thirdPlaces.collectAsState(initial = emptySet())
    var place by remember { mutableStateOf("") }
    var minsTxt by remember { mutableStateOf("") }
    val mins = minsTxt.toIntOrNull() ?: 0
    val fmt = SimpleDateFormat("MMM d", Locale.US)

    val parsed = remember(raw) {
        raw.mapNotNull { e ->
            val parts = e.split("|", limit = 3)
            val ts = parts.getOrNull(0)?.toLongOrNull() ?: return@mapNotNull null
            val name = parts.getOrNull(1)?.takeIf { it.isNotBlank() } ?: return@mapNotNull null
            val m = parts.getOrNull(2)?.toIntOrNull() ?: return@mapNotNull null
            Quad(e, ts, name, m)
        }.sortedByDescending { it.ts }
    }
    val weekCutoff = System.currentTimeMillis() - 7 * 86_400_000L
    val byPlaceThisWeek = parsed.filter { it.ts >= weekCutoff }
        .groupBy { it.name.lowercase() }
        .mapValues { (_, list) -> list.sumOf { it.minutes } to list.first().name }

    Surface(
        modifier = Modifier.fillMaxSize().testTag("third-place"),
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
                    .testTag("third-place-back")
                    .clickable { onBack() }
                    .padding(8.dp),
            )

            Spacer(Modifier.height(24.dp))
            Text("third place.", style = displayStyle, color = MinimalTheme.fg)
            Spacer(Modifier.height(8.dp))
            Text(
                "places that aren't home or work, where you spent ≥30 min in the last week.",
                style = bodyStyle.copy(fontSize = 14.sp),
                color = MinimalTheme.outline,
            )

            Spacer(Modifier.height(24.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.weight(1f)) {
                    Field(place, { place = it.take(40) }, "place", "third-place-name", KeyboardType.Text)
                }
                Spacer(Modifier.width(16.dp))
                Box(modifier = Modifier.width(96.dp)) {
                    Field(minsTxt, { minsTxt = it.filter { c -> c.isDigit() }.take(4) }, "min", "third-place-mins", KeyboardType.Number)
                }
            }
            Spacer(Modifier.height(12.dp))
            Text(
                "log",
                style = bodyStyle,
                color = if (place.isNotBlank() && mins > 0) MinimalTheme.accent else MinimalTheme.outline,
                modifier = Modifier
                    .testTag("third-place-log")
                    .clickable {
                        if (place.isNotBlank() && mins > 0) {
                            scope.launch { prefs.logThirdPlace(place.trim(), mins) }
                            place = ""; minsTxt = ""
                        }
                    }
                    .padding(vertical = 12.dp),
            )

            Spacer(Modifier.height(24.dp))
            Text("this week (≥30 min only)", style = captionStyle, color = MinimalTheme.outline)
            Spacer(Modifier.height(8.dp))
            val qualifying = byPlaceThisWeek.filter { it.value.first >= 30 }
            if (qualifying.isEmpty()) {
                Text("0 third places. that's data too.",
                    style = bodyStyle, color = MinimalTheme.outline,
                    modifier = Modifier.testTag("third-place-empty"))
            } else {
                qualifying.values.sortedByDescending { it.first }.forEach { (m, n) ->
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp).testTag("third-place-weekly-row"),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(n, style = bodyStyle, color = MinimalTheme.fg)
                        Text("$m min", style = bodyStyle, color = MinimalTheme.fg)
                    }
                }
            }

            Spacer(Modifier.height(32.dp))
            Text("all logs", style = captionStyle, color = MinimalTheme.outline)
            Spacer(Modifier.height(8.dp))
            if (parsed.isEmpty()) {
                Text("none.", style = bodyStyle, color = MinimalTheme.outline)
            } else {
                parsed.take(20).forEach { q ->
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp).testTag("third-place-row"),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(q.name, style = bodyStyle, color = MinimalTheme.fg)
                            Text("${fmt.format(Date(q.ts))} · ${q.minutes} min", style = captionStyle, color = MinimalTheme.outline)
                        }
                        Text("×", style = bodyStyle, color = MinimalTheme.outline,
                            modifier = Modifier
                                .clickable { scope.launch { prefs.removeThirdPlace(q.entry) } }
                                .padding(8.dp))
                    }
                }
            }
            Spacer(Modifier.height(48.dp))
        }
    }
}

private data class Quad(val entry: String, val ts: Long, val name: String, val minutes: Int)

@Composable
private fun Field(value: String, onValueChange: (String) -> Unit, placeholder: String, tag: String, keyboardType: KeyboardType) {
    Column(modifier = Modifier.fillMaxWidth()) {
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
