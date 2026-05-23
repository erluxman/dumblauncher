package com.erluxman.focuslauncher.ui.letters

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
import com.erluxman.focuslauncher.ui.home.minimal.MinimalTheme
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * Future letters — write something, schedule a delivery date, surface when
 * due. Behind FlagKey.FUTURE_LETTERS.
 */
@Composable
fun FutureLettersScreen(
    prefs: UserPrefs,
    onBack: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    var text by remember { mutableStateOf("") }
    var deliverDays by remember { mutableStateOf("30") }
    val raw by prefs.futureLetters.collectAsState(initial = emptySet())
    val today = remember { Calendar.getInstance().apply { set(Calendar.HOUR_OF_DAY, 0); set(Calendar.MINUTE, 0); set(Calendar.SECOND, 0); set(Calendar.MILLISECOND, 0) }.time }
    val isoFmt = remember { SimpleDateFormat("yyyy-MM-dd", Locale.US) }
    val niceFmt = remember { SimpleDateFormat("MMM d, yyyy", Locale.US) }

    val parsed = remember(raw) {
        raw.mapNotNull { entry ->
            val parts = entry.split("|", limit = 4)
            if (parts.size < 4) return@mapNotNull null
            val id = parts[0]
            val date = parts[1]
            val delivered = parts[2] == "1"
            val body = parts[3]
            Letter(entry, id, date, delivered, body)
        }
    }
    val due = parsed.filter { !it.delivered && it.deliverDate <= isoFmt.format(today) }
    val pending = parsed.filter { !it.delivered && it.deliverDate > isoFmt.format(today) }
        .sortedBy { it.deliverDate }
    val past = parsed.filter { it.delivered }.sortedByDescending { it.deliverDate }

    Surface(
        modifier = Modifier.fillMaxSize().testTag("letters"),
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
                    .testTag("letters-back")
                    .clickable { onBack() }
                    .padding(8.dp),
            )

            Spacer(Modifier.height(24.dp))
            Text("future letters.", style = displayStyle, color = MinimalTheme.fg)
            Spacer(Modifier.height(8.dp))
            Text(
                "write to a future you. it shows up when due.",
                style = bodyStyle.copy(fontSize = 14.sp),
                color = MinimalTheme.outline,
            )

            if (due.isNotEmpty()) {
                Spacer(Modifier.height(32.dp))
                Text("due now", style = captionStyle, color = MinimalTheme.accent)
                Spacer(Modifier.height(8.dp))
                due.forEach { l ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp)
                            .testTag("letters-due-row"),
                    ) {
                        Text("written for ${l.deliverDate}", style = captionStyle, color = MinimalTheme.outline)
                        Spacer(Modifier.height(4.dp))
                        Text(l.text, style = bodyStyle, color = MinimalTheme.fg)
                        Spacer(Modifier.height(8.dp))
                        Text(
                            "mark read",
                            style = captionStyle,
                            color = MinimalTheme.accent,
                            modifier = Modifier
                                .clickable { scope.launch { prefs.markLetterDelivered(l.entry) } }
                                .padding(4.dp),
                        )
                    }
                }
            }

            Spacer(Modifier.height(32.dp))
            Text("write one", style = captionStyle, color = MinimalTheme.outline)
            Spacer(Modifier.height(8.dp))
            BasicTextField(
                value = text,
                onValueChange = { text = it.take(1000) },
                textStyle = bodyStyle.copy(color = MinimalTheme.fg),
                cursorBrush = SolidColor(MinimalTheme.accent),
                modifier = Modifier.fillMaxWidth().testTag("letters-input"),
                decorationBox = { inner ->
                    Column {
                        if (text.isEmpty()) {
                            Text(
                                "dear future me…",
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
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("deliver in", style = captionStyle, color = MinimalTheme.outline)
                Spacer(Modifier.width(12.dp))
                BasicTextField(
                    value = deliverDays,
                    onValueChange = { deliverDays = it.filter { c -> c.isDigit() }.take(4) },
                    singleLine = true,
                    textStyle = bodyStyle.copy(color = MinimalTheme.fg),
                    cursorBrush = SolidColor(MinimalTheme.accent),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.width(60.dp).testTag("letters-days"),
                )
                Text(" days", style = bodyStyle, color = MinimalTheme.outline)
            }
            Spacer(Modifier.height(12.dp))
            Text(
                "schedule",
                style = bodyStyle,
                color = if (text.isNotBlank() && (deliverDays.toIntOrNull() ?: 0) > 0) MinimalTheme.accent else MinimalTheme.outline,
                modifier = Modifier
                    .testTag("letters-save")
                    .clickable {
                        val days = deliverDays.toIntOrNull() ?: return@clickable
                        if (text.isBlank() || days <= 0) return@clickable
                        val cal = Calendar.getInstance().apply { add(Calendar.DAY_OF_MONTH, days) }
                        scope.launch { prefs.addFutureLetter(isoFmt.format(cal.time), text.trim()) }
                        text = ""
                    }
                    .padding(vertical = 12.dp),
            )

            if (pending.isNotEmpty()) {
                Spacer(Modifier.height(32.dp))
                Text("pending", style = captionStyle, color = MinimalTheme.outline)
                Spacer(Modifier.height(8.dp))
                pending.forEach { l ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                "due ${niceFmt.format(isoFmt.parse(l.deliverDate) ?: Date())}",
                                style = captionStyle,
                                color = MinimalTheme.outline,
                            )
                            Spacer(Modifier.height(2.dp))
                            Text(
                                text = if (l.text.length > 60) l.text.take(60) + "…" else l.text,
                                style = bodyStyle.copy(fontSize = 16.sp),
                                color = MinimalTheme.outline,
                            )
                        }
                    }
                }
            }

            if (past.isNotEmpty()) {
                Spacer(Modifier.height(32.dp))
                Text("delivered", style = captionStyle, color = MinimalTheme.outline)
                Spacer(Modifier.height(8.dp))
                past.take(5).forEach { l ->
                    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                        Text(l.deliverDate, style = captionStyle, color = MinimalTheme.outline.copy(alpha = 0.6f))
                        Text(
                            text = if (l.text.length > 80) l.text.take(80) + "…" else l.text,
                            style = bodyStyle.copy(fontSize = 16.sp),
                            color = MinimalTheme.outline,
                        )
                    }
                }
            }
            Spacer(Modifier.height(48.dp))
        }
    }
}

private data class Letter(
    val entry: String,
    val id: String,
    val deliverDate: String,
    val delivered: Boolean,
    val text: String,
)

private val displayStyle = TextStyle(fontSize = 32.sp, fontWeight = FontWeight.Normal, letterSpacing = 0.sp)
private val bodyStyle = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Normal, letterSpacing = 0.sp)
private val captionStyle = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Normal)
