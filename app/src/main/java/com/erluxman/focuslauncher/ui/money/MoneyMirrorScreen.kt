package com.erluxman.focuslauncher.ui.money

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
import androidx.compose.runtime.LaunchedEffect
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
import com.erluxman.focuslauncher.data.prefs.moneyAssets
import com.erluxman.focuslauncher.data.prefs.moneyExpense
import com.erluxman.focuslauncher.data.prefs.moneyIncome
import com.erluxman.focuslauncher.data.prefs.moneyLiabilities
import com.erluxman.focuslauncher.data.prefs.setMoneyAssets
import com.erluxman.focuslauncher.data.prefs.setMoneyExpense
import com.erluxman.focuslauncher.data.prefs.setMoneyIncome
import com.erluxman.focuslauncher.data.prefs.setMoneyLiabilities
import com.erluxman.focuslauncher.ui.home.minimal.MinimalTheme
import kotlinx.coroutines.launch

/**
 * Manual net-worth snapshot. Four numbers in, derived savings rate + net
 * worth out. No bank integration — that's MONEY_MIRROR's external blocker.
 * Behind FlagKey.MONEY_MIRROR.
 */
@Composable
fun MoneyMirrorScreen(
    prefs: UserPrefs,
    onBack: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val income by prefs.moneyIncome.collectAsState(initial = 0)
    val expense by prefs.moneyExpense.collectAsState(initial = 0)
    val assets by prefs.moneyAssets.collectAsState(initial = 0)
    val liab by prefs.moneyLiabilities.collectAsState(initial = 0)

    var incomeTxt by remember { mutableStateOf("") }
    var expenseTxt by remember { mutableStateOf("") }
    var assetsTxt by remember { mutableStateOf("") }
    var liabTxt by remember { mutableStateOf("") }
    LaunchedEffect(income) { if (incomeTxt.isEmpty()) incomeTxt = if (income > 0) "$income" else "" }
    LaunchedEffect(expense) { if (expenseTxt.isEmpty()) expenseTxt = if (expense > 0) "$expense" else "" }
    LaunchedEffect(assets) { if (assetsTxt.isEmpty()) assetsTxt = if (assets > 0) "$assets" else "" }
    LaunchedEffect(liab) { if (liabTxt.isEmpty()) liabTxt = if (liab > 0) "$liab" else "" }

    val net = assets - liab
    val savingsRate = if (income > 0) ((income - expense).toDouble() / income * 100).coerceIn(-100.0, 100.0) else 0.0

    Surface(
        modifier = Modifier.fillMaxSize().testTag("money"),
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
                    .testTag("money-back")
                    .clickable { onBack() }
                    .padding(8.dp),
            )

            Spacer(Modifier.height(24.dp))
            Text("money.", style = displayStyle, color = MinimalTheme.fg)
            Spacer(Modifier.height(8.dp))
            Text(
                "manual snapshot. no bank connection (yet).",
                style = bodyStyle.copy(fontSize = 14.sp),
                color = MinimalTheme.outline,
            )

            Spacer(Modifier.height(32.dp))
            Text("net worth", style = captionStyle, color = MinimalTheme.outline)
            Spacer(Modifier.height(4.dp))
            Text(
                text = "$%,d".format(net),
                style = TextStyle(fontSize = 48.sp, fontWeight = FontWeight.Normal),
                color = if (net >= 0) MinimalTheme.accent else MinimalTheme.fg,
                modifier = Modifier.testTag("money-net"),
            )
            Spacer(Modifier.height(16.dp))
            Text("savings rate", style = captionStyle, color = MinimalTheme.outline)
            Spacer(Modifier.height(4.dp))
            Text(
                text = "%.0f%%".format(savingsRate),
                style = TextStyle(fontSize = 32.sp, fontWeight = FontWeight.Normal),
                color = if (savingsRate >= 20) MinimalTheme.accent else MinimalTheme.fg,
                modifier = Modifier.testTag("money-savings-rate"),
            )

            Spacer(Modifier.height(32.dp))

            MoneyField("monthly income", incomeTxt, { incomeTxt = it.filter { c -> c.isDigit() }.take(8) }, "money-income") {
                scope.launch { prefs.setMoneyIncome(incomeTxt.toIntOrNull() ?: 0) }
            }
            MoneyField("monthly expense", expenseTxt, { expenseTxt = it.filter { c -> c.isDigit() }.take(8) }, "money-expense") {
                scope.launch { prefs.setMoneyExpense(expenseTxt.toIntOrNull() ?: 0) }
            }
            MoneyField("assets", assetsTxt, { assetsTxt = it.filter { c -> c.isDigit() }.take(10) }, "money-assets") {
                scope.launch { prefs.setMoneyAssets(assetsTxt.toIntOrNull() ?: 0) }
            }
            MoneyField("liabilities", liabTxt, { liabTxt = it.filter { c -> c.isDigit() }.take(10) }, "money-liabilities") {
                scope.launch { prefs.setMoneyLiabilities(liabTxt.toIntOrNull() ?: 0) }
            }

            Spacer(Modifier.height(48.dp))
        }
    }
}

@Composable
private fun MoneyField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    tag: String,
    onCommit: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp)) {
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
            decorationBox = { inner ->
                if (value.isEmpty()) {
                    Text("$", style = bodyStyle.copy(fontSize = 24.sp), color = MinimalTheme.outline.copy(alpha = 0.5f))
                }
                inner()
            },
        )
        Spacer(Modifier.height(6.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(MinimalTheme.outline.copy(alpha = 0.4f))
        )
        Spacer(Modifier.height(8.dp))
        Text(
            "save",
            style = captionStyle,
            color = MinimalTheme.accent,
            modifier = Modifier
                .testTag("$tag-save")
                .clickable { onCommit() }
                .padding(4.dp),
        )
    }
}

private val displayStyle = TextStyle(fontSize = 32.sp, fontWeight = FontWeight.Normal, letterSpacing = 0.sp)
private val bodyStyle = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Normal, letterSpacing = 0.sp)
private val captionStyle = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Normal)
