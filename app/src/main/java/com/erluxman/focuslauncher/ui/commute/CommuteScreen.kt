package com.erluxman.focuslauncher.ui.commute

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
import com.erluxman.focuslauncher.ui.home.minimal.MinimalTheme

/**
 * LOC-004 — Commute Tax. Daily commute minutes → yearly hours + USD cost +
 * book-equivalents + gym-equivalents. Behind FlagKey.COMMUTE.
 */
@Composable
fun CommuteScreen(onBack: () -> Unit) {
    var minsPerDay by remember { mutableStateOf("60") }
    var daysPerWeek by remember { mutableStateOf("5") }
    var rate by remember { mutableStateOf("25") }

    val mins = minsPerDay.toIntOrNull() ?: 0
    val days = daysPerWeek.toIntOrNull() ?: 0
    val hr = rate.toDoubleOrNull() ?: 0.0

    val weeklyMin = mins * days
    val yearlyHr = weeklyMin * 52 / 60
    val yearlyCost = yearlyHr * hr
    val booksUnread = yearlyHr / 5     // ~5h per book
    val gymSkipped = yearlyHr / 1      // ~1h per session

    Surface(
        modifier = Modifier.fillMaxSize().testTag("commute"),
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
                    .testTag("commute-back")
                    .clickable { onBack() }
                    .padding(8.dp),
            )

            Spacer(Modifier.height(24.dp))
            Text("commute tax.", style = displayStyle, color = MinimalTheme.fg)
            Spacer(Modifier.height(8.dp))
            Text(
                "hours/year × what you could have done.",
                style = bodyStyle.copy(fontSize = 14.sp),
                color = MinimalTheme.outline,
            )

            Spacer(Modifier.height(32.dp))
            Field("minutes per day", minsPerDay, { minsPerDay = it.filter { c -> c.isDigit() }.take(3) }, "commute-mins")
            Spacer(Modifier.height(16.dp))
            Field("days per week", daysPerWeek, { daysPerWeek = it.filter { c -> c.isDigit() }.take(1) }, "commute-days")
            Spacer(Modifier.height(16.dp))
            Field("hourly rate (usd)", rate, { rate = it.filter { c -> c.isDigit() || c == '.' }.take(6) }, "commute-rate")

            Spacer(Modifier.height(40.dp))
            Stat("hours / year", "$yearlyHr", "commute-hours")
            Stat("dollars / year", "$%,.0f".format(yearlyCost), "commute-cost")
            Stat("books unread / year", "$booksUnread", "commute-books")
            Stat("gym sessions / year", "$gymSkipped", "commute-gym")
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
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
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

@Composable
private fun Stat(label: String, value: String, tag: String) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp)) {
        Text(label, style = captionStyle, color = MinimalTheme.outline)
        Spacer(Modifier.height(4.dp))
        Text(value, style = TextStyle(fontSize = 28.sp, fontWeight = FontWeight.Normal),
            color = MinimalTheme.accent, modifier = Modifier.testTag(tag))
    }
}

private val displayStyle = TextStyle(fontSize = 32.sp, fontWeight = FontWeight.Normal, letterSpacing = 0.sp)
private val bodyStyle = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Normal, letterSpacing = 0.sp)
private val captionStyle = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Normal)
