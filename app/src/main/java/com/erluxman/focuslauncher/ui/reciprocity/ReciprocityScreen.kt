package com.erluxman.focuslauncher.ui.reciprocity

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
import com.erluxman.focuslauncher.data.prefs.UserPrefs
import com.erluxman.focuslauncher.data.prefs.contactsLog
import com.erluxman.focuslauncher.data.prefs.logContactTouch
import com.erluxman.focuslauncher.service.social.Reciprocity
import com.erluxman.focuslauncher.ui.home.minimal.MinimalTheme
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Reciprocity — log outbound/inbound contacts; flag lopsided relationships
 * where you're carrying or being carried. Behind FlagKey.RECIPROCITY.
 */
@Composable
fun ReciprocityScreen(
    prefs: UserPrefs,
    onBack: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val today = remember { SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date()) }
    val raw by prefs.contactsLog.collectAsState(initial = emptySet())
    val touches = remember(raw) { Reciprocity.parse(raw) }
    val byName = remember(touches) {
        touches.groupBy { it.name.lowercase() }
            .map { (lcName, list) ->
                val displayName = list.first().name
                val outPct = Reciprocity.outboundPct(displayName, list)
                Triple(displayName, list.size, outPct)
            }.sortedByDescending { it.second }
    }
    val lopsided = remember(touches) { Reciprocity.lopsidedRelationships(touches) }

    var name by remember { mutableStateOf("") }

    Surface(
        modifier = Modifier.fillMaxSize().testTag("reciprocity"),
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
                    .testTag("reciprocity-back")
                    .clickable { onBack() }
                    .padding(8.dp),
            )

            Spacer(Modifier.height(24.dp))
            Text("reciprocity.", style = displayStyle, color = MinimalTheme.fg)
            Spacer(Modifier.height(8.dp))
            Text(
                "log who reached out to whom. patterns surface.",
                style = bodyStyle.copy(fontSize = 14.sp),
                color = MinimalTheme.outline,
            )

            Spacer(Modifier.height(32.dp))
            Text("log a touch", style = captionStyle, color = MinimalTheme.outline)
            Spacer(Modifier.height(6.dp))
            BasicTextField(
                value = name,
                onValueChange = { name = it.take(40) },
                singleLine = true,
                textStyle = bodyStyle.copy(color = MinimalTheme.fg),
                cursorBrush = SolidColor(MinimalTheme.accent),
                modifier = Modifier.fillMaxWidth().testTag("reciprocity-name"),
                decorationBox = { inner ->
                    Column {
                        if (name.isEmpty()) {
                            Text("name", style = bodyStyle.copy(fontSize = 16.sp),
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
            Spacer(Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Text(
                    "i reached out",
                    style = bodyStyle,
                    color = if (name.isNotBlank()) MinimalTheme.accent else MinimalTheme.outline,
                    modifier = Modifier
                        .testTag("reciprocity-log-out")
                        .clickable {
                            if (name.isNotBlank()) {
                                scope.launch { prefs.logContactTouch(today, name.trim(), "out") }
                                name = ""
                            }
                        }
                        .padding(vertical = 8.dp),
                )
                Text(
                    "they reached out",
                    style = bodyStyle,
                    color = if (name.isNotBlank()) MinimalTheme.accent else MinimalTheme.outline,
                    modifier = Modifier
                        .testTag("reciprocity-log-in")
                        .clickable {
                            if (name.isNotBlank()) {
                                scope.launch { prefs.logContactTouch(today, name.trim(), "in") }
                                name = ""
                            }
                        }
                        .padding(vertical = 8.dp),
                )
            }

            if (lopsided.isNotEmpty()) {
                Spacer(Modifier.height(32.dp))
                Text("lopsided", style = captionStyle, color = MinimalTheme.accent)
                Spacer(Modifier.height(8.dp))
                lopsided.forEach { n ->
                    Text("· $n", style = bodyStyle, color = MinimalTheme.fg,
                        modifier = Modifier.testTag("reciprocity-lopsided-row").padding(vertical = 4.dp))
                }
            }

            Spacer(Modifier.height(32.dp))
            Text("by person", style = captionStyle, color = MinimalTheme.outline)
            Spacer(Modifier.height(8.dp))
            if (byName.isEmpty()) {
                Text("no touches logged.", style = bodyStyle, color = MinimalTheme.outline)
            } else {
                byName.forEach { (name, count, outPct) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .testTag("reciprocity-person-row"),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(name, style = bodyStyle, color = MinimalTheme.fg)
                            Text("$count touches", style = captionStyle, color = MinimalTheme.outline)
                        }
                        Text(
                            text = "${outPct}% you",
                            style = bodyStyle,
                            color = when {
                                outPct >= 65 -> MinimalTheme.accent
                                outPct <= 35 -> MinimalTheme.outline.copy(alpha = 0.7f)
                                else -> MinimalTheme.fg
                            },
                        )
                    }
                }
            }
            Spacer(Modifier.height(48.dp))
        }
    }
}

private val displayStyle = TextStyle(fontSize = 32.sp, fontWeight = FontWeight.Normal, letterSpacing = 0.sp)
private val bodyStyle = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Normal, letterSpacing = 0.sp)
private val captionStyle = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Normal)
