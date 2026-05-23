package com.erluxman.focuslauncher.ui.taxincome

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
import com.erluxman.focuslauncher.data.prefs.logTaxIncome
import com.erluxman.focuslauncher.data.prefs.removeTaxIncome
import com.erluxman.focuslauncher.data.prefs.taxIncomes
import com.erluxman.focuslauncher.ui.home.minimal.MinimalTheme
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * FIN-010 — Tax-aware income tracker. Every 1099 / gig / sale logged
 * with date + source + amount; rolls up per-quarter for the nag.
 * Behind FlagKey.TAX_INCOME.
 */
@Composable
fun TaxIncomeScreen(
    prefs: UserPrefs,
    onBack: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val raw by prefs.taxIncomes.collectAsState(initial = emptySet())
    var source by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    val now = System.currentTimeMillis()
    val fmt = SimpleDateFormat("MMM d, yyyy", Locale.US)

    val parsed = remember(raw) {
        raw.mapNotNull { e ->
            val parts = e.split("|", limit = 3)
            val ts = parts.getOrNull(0)?.toLongOrNull() ?: return@mapNotNull null
            val s = parts.getOrNull(1)?.takeIf { it.isNotBlank() } ?: return@mapNotNull null
            val a = parts.getOrNull(2)?.toDoubleOrNull() ?: return@mapNotNull null
            Quad(e, ts, s, a)
        }.sortedByDescending { it.ts }
    }
    val cal = remember { Calendar.getInstance() }
    val q = cal.get(Calendar.MONTH) / 3
    val qStart = remember {
        Calendar.getInstance().apply {
            set(Calendar.MONTH, q * 3)
            set(Calendar.DAY_OF_MONTH, 1)
            set(Calendar.HOUR_OF_DAY, 0); set(Calendar.MINUTE, 0); set(Calendar.SECOND, 0); set(Calendar.MILLISECOND, 0)
        }.timeInMillis
    }
    val thisQ = parsed.filter { it.ts >= qStart }.sumOf { it.amount }
    val ytdStart = remember {
        Calendar.getInstance().apply {
            set(Calendar.MONTH, 0); set(Calendar.DAY_OF_MONTH, 1)
            set(Calendar.HOUR_OF_DAY, 0); set(Calendar.MINUTE, 0); set(Calendar.SECOND, 0); set(Calendar.MILLISECOND, 0)
        }.timeInMillis
    }
    val ytd = parsed.filter { it.ts >= ytdStart }.sumOf { it.amount }

    Surface(
        modifier = Modifier.fillMaxSize().testTag("tax-income"),
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
                    .testTag("tax-income-back")
                    .clickable { onBack() }
                    .padding(8.dp),
            )

            Spacer(Modifier.height(24.dp))
            Text("tax income.", style = displayStyle, color = MinimalTheme.fg)
            Spacer(Modifier.height(8.dp))
            Text(
                "every 1099 / gig / sale. quarterly nag included.",
                style = bodyStyle.copy(fontSize = 14.sp),
                color = MinimalTheme.outline,
            )

            Spacer(Modifier.height(32.dp))
            Text("this quarter (Q${q + 1})", style = captionStyle, color = MinimalTheme.outline)
            Text("$%,.0f".format(thisQ),
                style = TextStyle(fontSize = 40.sp, fontWeight = FontWeight.Normal),
                color = MinimalTheme.accent, modifier = Modifier.testTag("tax-income-quarter"))
            Spacer(Modifier.height(8.dp))
            Text("year to date", style = captionStyle, color = MinimalTheme.outline)
            Text("$%,.0f".format(ytd),
                style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Normal),
                color = MinimalTheme.fg, modifier = Modifier.testTag("tax-income-ytd"))

            Spacer(Modifier.height(32.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.weight(1f)) {
                    Field(source, { source = it.take(40) }, "source", "tax-income-source", KeyboardType.Text)
                }
                Spacer(Modifier.width(16.dp))
                Box(modifier = Modifier.width(120.dp)) {
                    Field(amount, { amount = it.filter { c -> c.isDigit() || c == '.' }.take(10) }, "$ amount", "tax-income-amount", KeyboardType.Decimal)
                }
            }
            Spacer(Modifier.height(12.dp))
            Text(
                "log",
                style = bodyStyle,
                color = if (source.isNotBlank() && (amount.toDoubleOrNull() ?: 0.0) > 0) MinimalTheme.accent else MinimalTheme.outline,
                modifier = Modifier
                    .testTag("tax-income-log")
                    .clickable {
                        val amt = amount.toDoubleOrNull() ?: return@clickable
                        if (source.isBlank() || amt <= 0) return@clickable
                        scope.launch { prefs.logTaxIncome(source.trim(), amt) }
                        source = ""; amount = ""
                    }
                    .padding(vertical = 12.dp),
            )

            Spacer(Modifier.height(24.dp))
            Text("history", style = captionStyle, color = MinimalTheme.outline)
            Spacer(Modifier.height(8.dp))
            if (parsed.isEmpty()) {
                Text("nothing logged.", style = bodyStyle, color = MinimalTheme.outline)
            } else {
                parsed.take(30).forEach { q ->
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp).testTag("tax-income-row"),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(q.source, style = bodyStyle, color = MinimalTheme.fg)
                            Text(fmt.format(Date(q.ts)), style = captionStyle, color = MinimalTheme.outline)
                        }
                        Text("$%,.0f".format(q.amount), style = bodyStyle, color = MinimalTheme.fg)
                        Spacer(Modifier.width(8.dp))
                        Text("×", style = bodyStyle, color = MinimalTheme.outline,
                            modifier = Modifier.clickable { scope.launch { prefs.removeTaxIncome(q.entry) } }
                                .padding(8.dp))
                    }
                }
            }
            Spacer(Modifier.height(48.dp))
        }
    }
}

private data class Quad(val entry: String, val ts: Long, val source: String, val amount: Double)

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
