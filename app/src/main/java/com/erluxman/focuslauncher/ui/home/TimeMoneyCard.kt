package com.erluxman.focuslauncher.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.erluxman.focuslauncher.service.money.TimeMoneyMath

/**
 * FIN-006 Time-Money Conversion.
 *
 * Enter a $ amount; see it as hours-of-life at your hourly rate. Only
 * shows once a rate is set (the InsightsCard's rate input doubles as
 * the configuration surface).
 */
@Composable
fun TimeMoneyCard(
    hourlyRate: Int,
    modifier: Modifier = Modifier,
) {
    if (hourlyRate <= 0) return
    var raw by remember { mutableStateOf("") }
    val usd = raw.toDoubleOrNull() ?: 0.0
    val hours = TimeMoneyMath.hoursForPurchase(usd, hourlyRate)
    val outline = MaterialTheme.colorScheme.outline

    Column(modifier = modifier.testTag("time-money-card")) {
        Text(
            "TIME COST",
            style = MaterialTheme.typography.labelLarge,
            color = outline,
            letterSpacing = 2.sp
        )
        Spacer(Modifier.height(8.dp))
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    OutlinedTextField(
                        value = raw,
                        onValueChange = { raw = it.filter { c -> c.isDigit() || c == '.' }.take(8) },
                        placeholder = { Text("$ amount") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.weight(1f).testTag("time-money-input")
                    )
                    Column(horizontalAlignment = Alignment.End) {
                        Text("AT \$$hourlyRate/HR", style = MaterialTheme.typography.labelSmall, color = outline, letterSpacing = 1.5.sp)
                        Text(
                            text = TimeMoneyMath.formatHours(hours),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.testTag("time-money-hours")
                        )
                    }
                }
            }
        }
    }
}
