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

@Composable
fun PrWallCard(
    records: List<String>,
    onAdd: (label: String, value: String, unit: String) -> Unit,
    onRemove: (entry: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val outline = MaterialTheme.colorScheme.outline
    var label by remember { mutableStateOf("") }
    var value by remember { mutableStateOf("") }
    var unit by remember { mutableStateOf("kg") }

    Column(modifier = modifier.testTag("pr-wall-card")) {
        Text(
            "PR WALL",
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
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    OutlinedTextField(
                        value = label,
                        onValueChange = { label = it.take(24) },
                        placeholder = { Text("Lift") },
                        singleLine = true,
                        modifier = Modifier.weight(1.2f).testTag("pr-label-input"),
                    )
                    OutlinedTextField(
                        value = value,
                        onValueChange = { value = it.take(8) },
                        placeholder = { Text("100") },
                        singleLine = true,
                        modifier = Modifier.weight(0.7f).testTag("pr-value-input"),
                    )
                    OutlinedTextField(
                        value = unit,
                        onValueChange = { unit = it.take(4) },
                        placeholder = { Text("kg") },
                        singleLine = true,
                        modifier = Modifier.width(64.dp).testTag("pr-unit-input"),
                    )
                    IconButton(
                        onClick = {
                            onAdd(label, value, unit)
                            label = ""
                            value = ""
                        },
                        modifier = Modifier.testTag("pr-add")
                    ) {
                        Icon(Icons.Filled.Add, contentDescription = "Add personal record")
                    }
                }
                if (records.isEmpty()) {
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "Lifetime records pinned here.",
                        style = MaterialTheme.typography.bodySmall,
                        color = outline,
                    )
                } else {
                    Spacer(Modifier.height(12.dp))
                    records.take(12).forEachIndexed { i, raw ->
                        val parts = raw.split("|", limit = 4)
                        if (parts.size < 4) return@forEachIndexed
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth().testTag("pr-row-$i"),
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = parts[1],
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                )
                                Text(
                                    text = "${parts[2]} ${parts[3]} · ${parts[0]}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = outline,
                                )
                            }
                            IconButton(
                                onClick = { onRemove(raw) },
                                modifier = Modifier.testTag("pr-remove-$i")
                            ) {
                                Icon(Icons.Filled.Close, contentDescription = "Remove personal record")
                            }
                        }
                    }
                }
            }
        }
    }
}
