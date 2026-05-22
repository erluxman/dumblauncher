package com.erluxman.focuslauncher.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.erluxman.focuslauncher.service.SubscriptionMath

@Composable
fun SubscriptionsCard(
    items: List<SubscriptionMath.Item>,
    onAdd: (name: String, monthlyUsd: Double) -> Unit,
    onRemove: (entry: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val outline = MaterialTheme.colorScheme.outline
    val monthly = SubscriptionMath.totalMonthly(items)
    val annual = SubscriptionMath.totalAnnual(items)
    var name by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }

    Column(modifier = modifier.testTag("subscriptions-card")) {
        Text(
            "SUBSCRIPTIONS",
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
                Row(verticalAlignment = Alignment.Bottom) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("MONTHLY", style = MaterialTheme.typography.labelSmall, color = outline, letterSpacing = 1.5.sp)
                        Text(
                            text = "$%,.2f".format(monthly),
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.testTag("subscriptions-monthly")
                        )
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text("ANNUAL", style = MaterialTheme.typography.labelSmall, color = outline, letterSpacing = 1.5.sp)
                        Text(
                            text = "$%,.0f".format(annual),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.testTag("subscriptions-annual")
                        )
                    }
                }
                Spacer(Modifier.height(12.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it.take(24) },
                        placeholder = { Text("Service") },
                        singleLine = true,
                        modifier = Modifier.weight(1.2f).testTag("sub-name-input"),
                    )
                    OutlinedTextField(
                        value = price,
                        onValueChange = { price = it.filter { c -> c.isDigit() || c == '.' }.take(7) },
                        placeholder = { Text("$/mo") },
                        singleLine = true,
                        modifier = Modifier.width(96.dp).testTag("sub-price-input"),
                    )
                    IconButton(
                        onClick = {
                            val p = price.toDoubleOrNull() ?: return@IconButton
                            onAdd(name, p)
                            name = ""; price = ""
                        },
                        modifier = Modifier.testTag("sub-add")
                    ) {
                        Icon(Icons.Filled.Add, contentDescription = "Add subscription")
                    }
                }
                if (items.isNotEmpty()) {
                    Spacer(Modifier.height(8.dp))
                    items.take(12).forEachIndexed { i, item ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth().testTag("sub-row-$i"),
                        ) {
                            Text(
                                text = item.name,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = "$%.2f/mo".format(item.monthlyUsd),
                                style = MaterialTheme.typography.labelLarge,
                                color = outline,
                            )
                            IconButton(
                                onClick = { onRemove(item.raw) },
                                modifier = Modifier.testTag("sub-remove-$i")
                            ) {
                                Icon(Icons.Filled.Close, contentDescription = "Remove subscription")
                            }
                        }
                    }
                }
            }
        }
    }
}
