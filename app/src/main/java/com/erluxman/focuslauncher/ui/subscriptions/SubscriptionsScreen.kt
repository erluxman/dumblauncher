package com.erluxman.focuslauncher.ui.subscriptions

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
import com.erluxman.focuslauncher.data.prefs.addSubscription
import com.erluxman.focuslauncher.data.prefs.removeSubscription
import com.erluxman.focuslauncher.data.prefs.subscriptions
import com.erluxman.focuslauncher.ui.home.minimal.MinimalTheme
import kotlinx.coroutines.launch

/**
 * Manual subscriptions tracker. Each row is `name|monthlyUsd` in DataStore;
 * total = sum of monthlyUsd. Behind FlagKey.SUBSCRIPTIONS.
 */
@Composable
fun SubscriptionsScreen(
    prefs: UserPrefs,
    onBack: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val subs by prefs.subscriptions.collectAsState(initial = emptySet())
    var name by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }

    val parsed = remember(subs) {
        subs.mapNotNull { entry ->
            val parts = entry.split("|", limit = 2)
            val n = parts.getOrNull(0)?.takeIf { it.isNotBlank() } ?: return@mapNotNull null
            val a = parts.getOrNull(1)?.toDoubleOrNull() ?: return@mapNotNull null
            n to a
        }.sortedByDescending { it.second }
    }
    val total = parsed.sumOf { it.second }

    Surface(
        modifier = Modifier.fillMaxSize().testTag("subscriptions"),
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
                    .testTag("subs-back")
                    .clickable { onBack() }
                    .padding(8.dp),
            )

            Spacer(Modifier.height(24.dp))
            Text("subscriptions.", style = displayStyle, color = MinimalTheme.fg)
            Spacer(Modifier.height(8.dp))
            Text(
                "monthly burn, manually tracked. decays in 30 days unless you keep it honest.",
                style = bodyStyle.copy(fontSize = 14.sp),
                color = MinimalTheme.outline,
            )

            Spacer(Modifier.height(24.dp))
            Text("total / month", style = captionStyle, color = MinimalTheme.outline)
            Spacer(Modifier.height(4.dp))
            Text(
                text = "$%.2f".format(total),
                style = TextStyle(fontSize = 48.sp, fontWeight = FontWeight.Normal),
                color = MinimalTheme.accent,
                modifier = Modifier.testTag("subs-total"),
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "yearly ≈ $%.0f".format(total * 12),
                style = captionStyle,
                color = MinimalTheme.outline,
                modifier = Modifier.testTag("subs-yearly"),
            )

            Spacer(Modifier.height(32.dp))
            Text("add", style = captionStyle, color = MinimalTheme.outline)
            Spacer(Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                MinimalField(
                    value = name,
                    onValueChange = { name = it.take(40) },
                    placeholder = "name",
                    modifier = Modifier.weight(1f).testTag("subs-add-name"),
                    keyboardType = KeyboardType.Text,
                )
                Spacer(Modifier.width(16.dp))
                MinimalField(
                    value = amount,
                    onValueChange = { v -> amount = v.filter { it.isDigit() || it == '.' }.take(8) },
                    placeholder = "usd/mo",
                    modifier = Modifier.width(120.dp).testTag("subs-add-amount"),
                    keyboardType = KeyboardType.Decimal,
                )
            }
            Spacer(Modifier.height(12.dp))
            Text(
                text = "add",
                style = bodyStyle,
                color = MinimalTheme.accent,
                modifier = Modifier
                    .testTag("subs-add-save")
                    .clickable {
                        val amt = amount.toDoubleOrNull() ?: return@clickable
                        if (name.isBlank() || amt <= 0) return@clickable
                        scope.launch { prefs.addSubscription(name.trim(), amt) }
                        name = ""
                        amount = ""
                    }
                    .padding(vertical = 8.dp),
            )

            Spacer(Modifier.height(24.dp))
            if (parsed.isEmpty()) {
                Text("nothing yet.", style = bodyStyle, color = MinimalTheme.outline)
            } else {
                parsed.forEach { (n, a) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp)
                            .testTag("subs-row-$n"),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(n, style = bodyStyle, color = MinimalTheme.fg)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("$%.2f".format(a), style = bodyStyle, color = MinimalTheme.fg)
                            Spacer(Modifier.width(16.dp))
                            Text(
                                "×",
                                style = bodyStyle,
                                color = MinimalTheme.outline,
                                modifier = Modifier
                                    .testTag("subs-remove-$n")
                                    .clickable {
                                        scope.launch { prefs.removeSubscription("$n|$a") }
                                    }
                                    .padding(8.dp),
                            )
                        }
                    }
                }
            }
            Spacer(Modifier.height(48.dp))
        }
    }
}

@Composable
private fun MinimalField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
) {
    Column(modifier = modifier) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            textStyle = bodyStyle.copy(color = MinimalTheme.fg),
            cursorBrush = SolidColor(MinimalTheme.accent),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
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
