package com.erluxman.focuslauncher.ui.stake

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
import com.erluxman.focuslauncher.backend.BackendRepository
import com.erluxman.focuslauncher.ui.home.minimal.MinimalTheme
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * FINANCE-001 — Money stake (escrow). User commits N days; if they fail,
 * money goes to a cause they hate. The stub records the commitment;
 * real escrow needs the web-checkout path (PAY-002).
 *
 * Behind FlagKey.STAKE.
 */
@Composable
fun MoneyStakeScreen(
    backend: BackendRepository,
    onBack: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val stake by backend.moneyStake.collectAsState(initial = null)
    var amount by remember { mutableStateOf("50") }
    var days by remember { mutableStateOf("30") }
    var charity by remember { mutableStateOf("") }
    var status by remember { mutableStateOf<String?>(null) }
    val ready = (amount.toIntOrNull() ?: 0) > 0 && (days.toIntOrNull() ?: 0) > 0 && charity.isNotBlank()
    val fmt = SimpleDateFormat("MMM d, yyyy", Locale.US)

    Surface(
        modifier = Modifier.fillMaxSize().testTag("stake"),
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
                    .testTag("stake-back")
                    .clickable { onBack() }
                    .padding(8.dp),
            )

            Spacer(Modifier.height(24.dp))
            Text("money stake.", style = displayStyle, color = MinimalTheme.fg)
            Spacer(Modifier.height(8.dp))
            Text(
                "commit n days. if you break it, the money goes to a cause you hate. that's the point.",
                style = bodyStyle.copy(fontSize = 14.sp),
                color = MinimalTheme.outline,
            )

            Spacer(Modifier.height(32.dp))
            stake?.let { s ->
                Text("active stake", style = captionStyle, color = MinimalTheme.outline)
                Spacer(Modifier.height(4.dp))
                Text("$${s.amountUsd} for ${s.daysCommitted} days",
                    style = TextStyle(fontSize = 32.sp, fontWeight = FontWeight.Normal),
                    color = MinimalTheme.accent, modifier = Modifier.testTag("stake-active"))
                Text("forfeit to: ${s.charity}", style = captionStyle, color = MinimalTheme.outline)
                Text("started ${fmt.format(Date(s.startedAtMs))}",
                    style = captionStyle, color = MinimalTheme.outline)
                Spacer(Modifier.height(16.dp))
                Text(
                    "cancel",
                    style = bodyStyle,
                    color = MinimalTheme.outline,
                    modifier = Modifier
                        .testTag("stake-cancel")
                        .clickable { scope.launch { backend.cancelMoneyStake() } }
                        .padding(vertical = 8.dp),
                )
                Spacer(Modifier.height(24.dp))
                return@Column
            }

            Text("set stake", style = captionStyle, color = MinimalTheme.outline)
            Spacer(Modifier.height(12.dp))
            Field("amount (usd)", amount,
                { amount = it.filter { c -> c.isDigit() }.take(5) }, "stake-amount", KeyboardType.Number)
            Spacer(Modifier.height(16.dp))
            Field("days committed", days,
                { days = it.filter { c -> c.isDigit() }.take(3) }, "stake-days", KeyboardType.Number)
            Spacer(Modifier.height(16.dp))
            Field("forfeit charity (cause you hate)", charity,
                { charity = it.take(80) }, "stake-charity", KeyboardType.Text)

            Spacer(Modifier.height(16.dp))
            Text(
                "commit (real escrow ships with PAY-002)",
                style = bodyStyle,
                color = if (ready) MinimalTheme.accent else MinimalTheme.outline,
                modifier = Modifier
                    .testTag("stake-commit")
                    .clickable {
                        if (ready) {
                            scope.launch {
                                val r = backend.createMoneyStake(amount.toInt(), days.toInt(), charity)
                                status = if (r.isSuccess) "staked." else "failed: ${r.exceptionOrNull()?.message}"
                            }
                        }
                    }
                    .padding(vertical = 12.dp),
            )
            status?.let {
                Spacer(Modifier.height(8.dp))
                Text(it, style = captionStyle, color = MinimalTheme.outline,
                    modifier = Modifier.testTag("stake-status"))
            }
            Spacer(Modifier.height(48.dp))
        }
    }
}

@Composable
private fun Field(label: String, value: String, onValueChange: (String) -> Unit, tag: String, keyboardType: KeyboardType) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(label, style = captionStyle, color = MinimalTheme.outline)
        Spacer(Modifier.height(6.dp))
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            textStyle = bodyStyle.copy(color = MinimalTheme.fg, fontSize = 24.sp),
            cursorBrush = SolidColor(MinimalTheme.accent),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
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
