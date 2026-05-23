package com.erluxman.focuslauncher.ui.budgetfuture

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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.erluxman.focuslauncher.data.prefs.moneyAssets
import com.erluxman.focuslauncher.ui.home.minimal.MinimalTheme
import kotlin.math.pow

/**
 * FIN-007 — Future-self budget projector. Today's assets + monthly
 * contribution + APY + years → projected net worth. Slider-free; numeric
 * inputs. Behind FlagKey.BUDGET_FUTURE.
 */
@Composable
fun BudgetFutureScreen(
    prefs: UserPrefs,
    onBack: () -> Unit,
) {
    val savedAssets by prefs.moneyAssets.collectAsState(initial = 0)
    var contribMonthly by remember { mutableStateOf("200") }
    var apyPct by remember { mutableStateOf("7") }
    var years by remember { mutableStateOf("30") }

    val start = savedAssets.toDouble()
    val monthly = contribMonthly.toDoubleOrNull() ?: 0.0
    val apy = (apyPct.toDoubleOrNull() ?: 0.0) / 100.0
    val n = (years.toIntOrNull() ?: 0).coerceAtLeast(0)
    val monthlyRate = apy / 12.0
    val months = n * 12
    val projected = if (monthlyRate > 0)
        start * (1 + monthlyRate).pow(months.toDouble()) +
            monthly * (((1 + monthlyRate).pow(months.toDouble()) - 1) / monthlyRate)
    else
        start + monthly * months

    Surface(
        modifier = Modifier.fillMaxSize().testTag("budget-future"),
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
                    .testTag("budget-future-back")
                    .clickable { onBack() }
                    .padding(8.dp),
            )

            Spacer(Modifier.height(24.dp))
            Text("future-self budget.", style = displayStyle, color = MinimalTheme.fg)
            Spacer(Modifier.height(8.dp))
            Text(
                "today's assets ($%,d) + monthly contribution + apy + years.".format(savedAssets),
                style = bodyStyle.copy(fontSize = 14.sp),
                color = MinimalTheme.outline,
            )

            Spacer(Modifier.height(32.dp))
            Field("monthly contribution (usd)", contribMonthly,
                { contribMonthly = it.filter { c -> c.isDigit() || c == '.' }.take(10) },
                "budget-future-contrib")
            Spacer(Modifier.height(16.dp))
            Field("apy (%)", apyPct,
                { apyPct = it.filter { c -> c.isDigit() || c == '.' }.take(5) },
                "budget-future-apy")
            Spacer(Modifier.height(16.dp))
            Field("years", years,
                { years = it.filter { c -> c.isDigit() }.take(3) },
                "budget-future-years")

            Spacer(Modifier.height(40.dp))
            Text("projected", style = captionStyle, color = MinimalTheme.outline)
            Spacer(Modifier.height(4.dp))
            Text(
                "$%,.0f".format(projected),
                style = TextStyle(fontSize = 48.sp, fontWeight = FontWeight.Normal),
                color = MinimalTheme.accent,
                modifier = Modifier.testTag("budget-future-projected"),
            )
            Spacer(Modifier.height(8.dp))
            Text(
                "monthly contribution × ${months}: $%,.0f total in.".format(monthly * months),
                style = captionStyle,
                color = MinimalTheme.outline,
            )
            Spacer(Modifier.height(48.dp))
        }
    }
}

@Composable
private fun Field(label: String, value: String, onValueChange: (String) -> Unit, tag: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(label, style = captionStyle, color = MinimalTheme.outline)
        Spacer(Modifier.height(6.dp))
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            textStyle = bodyStyle.copy(color = MinimalTheme.fg, fontSize = 24.sp),
            cursorBrush = SolidColor(MinimalTheme.accent),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth().testTag(tag),
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
