package com.erluxman.focuslauncher.ui.timemoney

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
import com.erluxman.focuslauncher.service.insights.InsightMath
import com.erluxman.focuslauncher.ui.home.minimal.MinimalTheme

/**
 * Type today's distraction minutes + your hourly rate. See:
 *  - opportunity cost today
 *  - lifetime hours on screen if you keep this up to age 80
 * Behind FlagKey.TIME_MONEY.
 */
@Composable
fun TimeMoneyScreen(
    prefs: UserPrefs,
    onBack: () -> Unit,
) {
    val savedAge by prefs.userAge.collectAsState(initial = 0)

    var minutes by remember { mutableStateOf("") }
    var rate by remember { mutableStateOf("25") }

    val mins = minutes.toIntOrNull() ?: 0
    val rateD = rate.toDoubleOrNull() ?: 0.0
    val cost = InsightMath.opportunityCost(mins, rateD)
    val effectiveAge = if (savedAge in 1..120) savedAge else 30
    val lifetimeHours = InsightMath.lifetimeHoursOnScreen(mins, effectiveAge)

    Surface(
        modifier = Modifier.fillMaxSize().testTag("time-money"),
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
                    .testTag("time-money-back")
                    .clickable { onBack() }
                    .padding(8.dp),
            )

            Spacer(Modifier.height(24.dp))
            Text("time = money.", style = displayStyle, color = MinimalTheme.fg)
            Spacer(Modifier.height(8.dp))
            Text(
                "what those minutes cost. and what they cost over a lifetime.",
                style = bodyStyle.copy(fontSize = 14.sp),
                color = MinimalTheme.outline,
            )

            Spacer(Modifier.height(32.dp))
            MoneyInput("distraction minutes today", minutes,
                { minutes = it.filter { c -> c.isDigit() }.take(4) }, "time-money-mins")
            Spacer(Modifier.height(16.dp))
            MoneyInput("your hourly rate (usd)", rate,
                { rate = it.filter { c -> c.isDigit() || c == '.' }.take(8) }, "time-money-rate")

            Spacer(Modifier.height(40.dp))
            Text("cost today", style = captionStyle, color = MinimalTheme.outline)
            Spacer(Modifier.height(4.dp))
            Text(
                text = "$%.2f".format(cost),
                style = TextStyle(fontSize = 48.sp, fontWeight = FontWeight.Normal),
                color = MinimalTheme.accent,
                modifier = Modifier.testTag("time-money-cost"),
            )

            Spacer(Modifier.height(32.dp))
            Text("lifetime hours on screen", style = captionStyle, color = MinimalTheme.outline)
            Spacer(Modifier.height(4.dp))
            Text(
                text = "${"%,d".format(lifetimeHours)} h",
                style = TextStyle(fontSize = 32.sp, fontWeight = FontWeight.Normal),
                color = MinimalTheme.fg,
                modifier = Modifier.testTag("time-money-lifetime"),
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "from age $effectiveAge to 80, at today's pace.",
                style = captionStyle,
                color = MinimalTheme.outline.copy(alpha = 0.6f),
            )
            Spacer(Modifier.height(48.dp))
        }
    }
}

@Composable
private fun MoneyInput(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    tag: String,
) {
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
            decorationBox = { inner ->
                if (value.isEmpty()) {
                    Text("0", style = bodyStyle.copy(fontSize = 24.sp),
                        color = MinimalTheme.outline.copy(alpha = 0.5f))
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
