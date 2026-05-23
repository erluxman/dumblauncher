package com.erluxman.focuslauncher.ui.substance

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
import com.erluxman.focuslauncher.data.prefs.logSubstance
import com.erluxman.focuslauncher.data.prefs.removeSubstanceEntry
import com.erluxman.focuslauncher.data.prefs.substanceLog
import com.erluxman.focuslauncher.ui.home.minimal.MinimalTheme
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * SUB-002 — Substance log (private). Cannabis, mushrooms, MDMA, etc.
 * Stored locally only; never synced. Behind FlagKey.SUBSTANCE.
 *
 * Storage is plain DataStore today. Spec calls for PII-grade encryption
 * — a follow-up will move this set to EncryptedSharedPrefs.
 */
@Composable
fun SubstanceScreen(
    prefs: UserPrefs,
    onBack: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val raw by prefs.substanceLog.collectAsState(initial = emptySet())
    var subst by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    val fmt = SimpleDateFormat("MMM d, yyyy", Locale.US)

    val parsed = remember(raw) {
        raw.mapNotNull { e ->
            val parts = e.split("|", limit = 3)
            val ts = parts.getOrNull(0)?.toLongOrNull() ?: return@mapNotNull null
            val s = parts.getOrNull(1)?.takeIf { it.isNotBlank() } ?: return@mapNotNull null
            val a = parts.getOrNull(2).orEmpty()
            Quad(e, ts, s, a)
        }.sortedByDescending { it.ts }
    }

    Surface(
        modifier = Modifier.fillMaxSize().testTag("substance"),
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
                    .testTag("substance-back")
                    .clickable { onBack() }
                    .padding(8.dp),
            )

            Spacer(Modifier.height(24.dp))
            Text("substance log.", style = displayStyle, color = MinimalTheme.fg)
            Spacer(Modifier.height(8.dp))
            Text(
                "private. stored on device only. e2ee encryption is a follow-up.",
                style = bodyStyle.copy(fontSize = 14.sp),
                color = MinimalTheme.outline,
            )

            Spacer(Modifier.height(32.dp))
            Field(subst, { subst = it.take(40) }, "substance", "substance-name")
            Spacer(Modifier.height(12.dp))
            Field(amount, { amount = it.take(20) }, "amount / dose", "substance-amount")
            Spacer(Modifier.height(12.dp))
            Text(
                "log",
                style = bodyStyle,
                color = if (subst.isNotBlank()) MinimalTheme.accent else MinimalTheme.outline,
                modifier = Modifier
                    .testTag("substance-log")
                    .clickable {
                        if (subst.isNotBlank()) {
                            scope.launch { prefs.logSubstance(subst.trim(), amount.trim()) }
                            subst = ""; amount = ""
                        }
                    }
                    .padding(vertical = 12.dp),
            )

            Spacer(Modifier.height(24.dp))
            Text("history", style = captionStyle, color = MinimalTheme.outline)
            Spacer(Modifier.height(8.dp))
            if (parsed.isEmpty()) {
                Text("none.", style = bodyStyle, color = MinimalTheme.outline)
            } else {
                parsed.forEach { q ->
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp).testTag("substance-row"),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(q.substance, style = bodyStyle, color = MinimalTheme.fg)
                            Text("${q.amount.ifEmpty { "—" }} · ${fmt.format(Date(q.ts))}",
                                style = captionStyle, color = MinimalTheme.outline)
                        }
                        Text("×", style = bodyStyle, color = MinimalTheme.outline,
                            modifier = Modifier
                                .clickable { scope.launch { prefs.removeSubstanceEntry(q.entry) } }
                                .padding(8.dp))
                    }
                }
            }
            Spacer(Modifier.height(48.dp))
        }
    }
}

private data class Quad(val entry: String, val ts: Long, val substance: String, val amount: String)

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
