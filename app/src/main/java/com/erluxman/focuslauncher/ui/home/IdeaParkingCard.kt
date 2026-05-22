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
import androidx.compose.material.icons.filled.Check
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
import com.erluxman.focuslauncher.service.ParkedIdea

/**
 * PROD-009 Idea Parking Lot.
 *
 * Caps at 12 visible entries so the card stays scrollable in the home
 * feed. "Convert" maps the parked text to a new todo via [onConvert].
 */
@Composable
fun IdeaParkingCard(
    items: List<ParkedIdea.Item>,
    onAdd: (String) -> Unit,
    onConvert: (ParkedIdea.Item) -> Unit,
    onDiscard: (ParkedIdea.Item) -> Unit,
    modifier: Modifier = Modifier,
) {
    val outline = MaterialTheme.colorScheme.outline
    var draft by remember { mutableStateOf("") }

    Column(modifier = modifier.testTag("idea-parking-card")) {
        Text(
            "PARKED",
            style = MaterialTheme.typography.labelLarge,
            color = outline,
            letterSpacing = 2.sp
        )
        Spacer(Modifier.height(8.dp))
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(
                        value = draft,
                        onValueChange = { draft = it.take(160) },
                        placeholder = { Text("Park an idea…") },
                        singleLine = true,
                        modifier = Modifier.weight(1f).testTag("idea-parking-input"),
                    )
                    IconButton(
                        onClick = {
                            onAdd(draft)
                            draft = ""
                        },
                        modifier = Modifier.testTag("idea-parking-add")
                    ) {
                        Icon(Icons.Filled.Add, contentDescription = "Park idea")
                    }
                }
                if (items.isEmpty()) {
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "Catch the thought; come back to it later.",
                        style = MaterialTheme.typography.bodySmall,
                        color = outline,
                    )
                } else {
                    Spacer(Modifier.height(12.dp))
                    items.take(12).forEachIndexed { i, item ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth().testTag("idea-parking-row-$i"),
                        ) {
                            Text(
                                text = item.text,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.weight(1f)
                            )
                            IconButton(
                                onClick = { onConvert(item) },
                                modifier = Modifier.testTag("idea-parking-convert-$i")
                            ) {
                                Icon(Icons.Filled.Check, contentDescription = "Convert to todo")
                            }
                            IconButton(
                                onClick = { onDiscard(item) },
                                modifier = Modifier.testTag("idea-parking-discard-$i")
                            ) {
                                Icon(Icons.Filled.Close, contentDescription = "Discard idea")
                            }
                        }
                    }
                }
            }
        }
    }
}
