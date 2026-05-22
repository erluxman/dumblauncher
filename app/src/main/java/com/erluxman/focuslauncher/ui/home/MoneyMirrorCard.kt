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
import com.erluxman.focuslauncher.service.money.MoneyMath

/**
 * Combined FIN-002 net worth + FIN-004 savings rate. Pure manual entry.
 * Hidden when nothing's been filled in (so it doesn't shout at first
 * launch).
 */
@Composable
fun MoneyMirrorCard(
    income: Int,
    expense: Int,
    assets: Int,
    liabilities: Int,
    onSet: (income: Int?, expense: Int?, assets: Int?, liabilities: Int?) -> Unit,
    modifier: Modifier = Modifier,
) {
    val allZero = income == 0 && expense == 0 && assets == 0 && liabilities == 0
    val rate = MoneyMath.savingsRatePct(income, expense)
    val netWorth = MoneyMath.netWorth(assets, liabilities)
    val outline = MaterialTheme.colorScheme.outline

    var incomeStr by remember(income) { mutableStateOf(if (income > 0) income.toString() else "") }
    var expenseStr by remember(expense) { mutableStateOf(if (expense > 0) expense.toString() else "") }
    var assetsStr by remember(assets) { mutableStateOf(if (assets > 0) assets.toString() else "") }
    var liabilitiesStr by remember(liabilities) { mutableStateOf(if (liabilities > 0) liabilities.toString() else "") }

    Column(modifier = modifier.testTag("money-mirror-card")) {
        Text(
            "MONEY MIRROR",
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
                if (!allZero) {
                    Row(verticalAlignment = Alignment.Bottom) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("SAVINGS RATE", style = MaterialTheme.typography.labelSmall, color = outline, letterSpacing = 1.5.sp)
                            Text(
                                text = "$rate%",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.testTag("money-rate")
                            )
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("NET WORTH", style = MaterialTheme.typography.labelSmall, color = outline, letterSpacing = 1.5.sp)
                            Text(
                                text = "$%,d".format(netWorth),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.testTag("money-net-worth")
                            )
                        }
                    }
                    Spacer(Modifier.height(12.dp))
                } else {
                    Text(
                        "Manual entry only. The numbers stay on this device.",
                        style = MaterialTheme.typography.bodySmall,
                        color = outline,
                    )
                    Spacer(Modifier.height(8.dp))
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    MoneyInput("Income/mo $", incomeStr, "money-income") { incomeStr = it; onSet(it.toIntOrNull(), null, null, null) }
                    MoneyInput("Expense/mo $", expenseStr, "money-expense") { expenseStr = it; onSet(null, it.toIntOrNull(), null, null) }
                }
                Spacer(Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    MoneyInput("Assets $", assetsStr, "money-assets") { assetsStr = it; onSet(null, null, it.toIntOrNull(), null) }
                    MoneyInput("Liabilities $", liabilitiesStr, "money-liabilities") { liabilitiesStr = it; onSet(null, null, null, it.toIntOrNull()) }
                }
            }
        }
    }
}

@Composable
private fun androidx.compose.foundation.layout.RowScope.MoneyInput(
    placeholder: String,
    value: String,
    tag: String,
    onChange: (String) -> Unit,
) {
    OutlinedTextField(
        value = value,
        onValueChange = { onChange(it.filter { c -> c.isDigit() }.take(9)) },
        placeholder = { Text(placeholder) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
        modifier = Modifier.weight(1f).testTag(tag),
    )
}
