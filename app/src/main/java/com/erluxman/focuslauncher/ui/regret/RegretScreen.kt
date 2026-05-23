package com.erluxman.focuslauncher.ui.regret

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.erluxman.focuslauncher.data.prefs.UserPrefs
import com.erluxman.focuslauncher.data.prefs.addRegret
import com.erluxman.focuslauncher.data.prefs.markRegretRating
import com.erluxman.focuslauncher.data.prefs.regrets
import com.erluxman.focuslauncher.ui.home.minimal.MinimalTheme
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * FIN-008 — Regret receipts. 24h after a purchase, ask "still glad?";
 * build per-category regret rate. Behind FlagKey.REGRET.
 */
@Composable
fun RegretScreen(
    prefs: UserPrefs,
    onBack: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val raw by prefs.regrets.collectAsState(initial = emptySet())
    var item by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    val now = remember { System.currentTimeMillis() }
    val fmt = SimpleDateFormat("MMM d", Locale.US)

    val parsed = remember(raw) {
        raw.mapNotNull { entry ->
            val parts = entry.split("|", limit = 5)
            if (parts.size < 5) return@mapNotNull null
            val ts = parts[0].toLongOrNull() ?: return@mapNotNull null
            val amt = parts[1].toDoubleOrNull() ?: return@mapNotNull null
            val cat = parts[2]
            val name = parts[3]
            val rating = parts[4]  // "?" / "glad" / "regret"
            Quint(entry, ts, amt, cat, name, rating)
        }.sortedByDescending { it.ts }
    }
    val dueForRating = parsed.filter {
        it.rating == "?" && now - it.ts >= 86_400_000L
    }
    val byCategory = parsed.filter { it.rating != "?" }
        .groupBy { it.category.lowercase().ifBlank { "uncategorized" } }
        .mapValues { (_, list) ->
            val total = list.size
            val regretted = list.count { it.rating == "regret" }
            (regretted * 100 / total.coerceAtLeast(1)) to total
        }

    Surface(
        modifier = Modifier.fillMaxSize().testTag("regret"),
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
                    .testTag("regret-back")
                    .clickable { onBack() }
                    .padding(8.dp),
            )

            Spacer(Modifier.height(24.dp))
            Text("regret receipts.", style = displayStyle, color = MinimalTheme.fg)
            Spacer(Modifier.height(8.dp))
            Text(
                "log a purchase. 24h later, mark glad or regret. categories build a regret rate.",
                style = bodyStyle.copy(fontSize = 14.sp),
                color = MinimalTheme.outline,
            )

            Spacer(Modifier.height(24.dp))
            Text("log purchase", style = captionStyle, color = MinimalTheme.outline)
            Spacer(Modifier.height(8.dp))
            Field(item, { item = it.take(40) }, "item", "regret-item", KeyboardType.Text)
            Spacer(Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Box(modifier = Modifier.weight(1f)) {
                    Field(amount, { amount = it.filter { c -> c.isDigit() || c == '.' }.take(8) }, "$", "regret-amount", KeyboardType.Decimal)
                }
                Box(modifier = Modifier.weight(1f)) {
                    Field(category, { category = it.take(20) }, "category", "regret-category", KeyboardType.Text)
                }
            }
            Spacer(Modifier.height(12.dp))
            Text(
                "log",
                style = bodyStyle,
                color = if (item.isNotBlank() && amount.toDoubleOrNull() ?: 0.0 > 0) MinimalTheme.accent else MinimalTheme.outline,
                modifier = Modifier
                    .testTag("regret-log")
                    .clickable {
                        val amt = amount.toDoubleOrNull() ?: return@clickable
                        if (item.isBlank() || amt <= 0) return@clickable
                        scope.launch { prefs.addRegret(item.trim(), amt, category.trim()) }
                        item = ""; amount = ""; category = ""
                    }
                    .padding(vertical = 12.dp),
            )

            if (dueForRating.isNotEmpty()) {
                Spacer(Modifier.height(24.dp))
                Text("due for rating (>24h old)", style = captionStyle, color = MinimalTheme.accent)
                Spacer(Modifier.height(8.dp))
                dueForRating.forEach { q ->
                    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp).testTag("regret-due-row")) {
                        Text(
                            "${q.name} · $%.2f · ${fmt.format(Date(q.ts))}".format(q.amount),
                            style = bodyStyle, color = MinimalTheme.fg,
                        )
                        Spacer(Modifier.height(4.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                            Text("still glad", style = bodyStyle, color = MinimalTheme.accent,
                                modifier = Modifier.clickable {
                                    scope.launch { prefs.markRegretRating(q.entry, "glad") }
                                }.padding(4.dp))
                            Text("regret", style = bodyStyle, color = MinimalTheme.outline,
                                modifier = Modifier.clickable {
                                    scope.launch { prefs.markRegretRating(q.entry, "regret") }
                                }.padding(4.dp))
                        }
                    }
                }
            }

            if (byCategory.isNotEmpty()) {
                Spacer(Modifier.height(32.dp))
                Text("regret rate by category", style = captionStyle, color = MinimalTheme.outline)
                Spacer(Modifier.height(8.dp))
                byCategory.forEach { (cat, p) ->
                    val (pct, total) = p
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text("$cat ($total)", style = bodyStyle, color = MinimalTheme.fg)
                        Text("${pct}%", style = bodyStyle,
                            color = if (pct >= 50) MinimalTheme.accent else MinimalTheme.fg)
                    }
                }
            }
            Spacer(Modifier.height(48.dp))
        }
    }
}

private data class Quint(
    val entry: String, val ts: Long, val amount: Double, val category: String,
    val name: String, val rating: String,
)

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
