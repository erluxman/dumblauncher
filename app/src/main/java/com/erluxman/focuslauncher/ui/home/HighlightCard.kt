package com.erluxman.focuslauncher.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.erluxman.focuslauncher.service.habits.Highlights

@Composable
fun HighlightCard(
    todayIso: String,
    highlights: Set<String>,
    onAdd: (String) -> Unit,
    onRemove: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val today = Highlights.pickForToday(highlights, todayIso)
    val outline = MaterialTheme.colorScheme.outline
    var draft by remember { mutableStateOf("") }

    Column(modifier = modifier.testTag("highlight-card")) {
        Text(
            "TODAY'S HIGHLIGHT",
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
                if (today != null) {
                    Text(
                        text = "“$today”",
                        style = MaterialTheme.typography.bodyLarge,
                        fontStyle = FontStyle.Italic,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.testTag("highlight-today")
                    )
                    Spacer(Modifier.height(12.dp))
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    OutlinedTextField(
                        value = draft,
                        onValueChange = { draft = it.take(280) },
                        placeholder = { Text("Add a passage…") },
                        modifier = Modifier.weight(1f).testTag("highlight-input"),
                    )
                    IconButton(
                        onClick = {
                            onAdd(draft)
                            draft = ""
                        },
                        modifier = Modifier.testTag("highlight-add")
                    ) {
                        Icon(Icons.Filled.Add, contentDescription = "Add highlight")
                    }
                }
                if (highlights.isNotEmpty()) {
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "${highlights.size} saved",
                        style = MaterialTheme.typography.labelSmall,
                        color = outline,
                    )
                    highlights.sorted().take(6).forEachIndexed { i, h ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth().testTag("highlight-row-$i"),
                        ) {
                            Text(
                                text = h,
                                style = MaterialTheme.typography.bodySmall,
                                color = outline,
                                modifier = Modifier.weight(1f)
                            )
                            IconButton(
                                onClick = { onRemove(h) },
                                modifier = Modifier.testTag("highlight-remove-$i")
                            ) {
                                Icon(Icons.Filled.Close, contentDescription = "Remove highlight")
                            }
                        }
                    }
                }
            }
        }
    }
}
