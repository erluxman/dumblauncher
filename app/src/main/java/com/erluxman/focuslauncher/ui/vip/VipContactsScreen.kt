package com.erluxman.focuslauncher.ui.vip

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.erluxman.focuslauncher.data.prefs.UserPrefs
import kotlinx.coroutines.launch

private const val MAX_VIPS = 10

@Composable
fun VipContactsScreen(prefs: UserPrefs, onBack: () -> Unit) {
    val scope = rememberCoroutineScope()
    val contacts by prefs.vipContacts.collectAsState(initial = emptySet())
    var newEntry by remember { mutableStateOf("") }

    Surface(
        modifier = Modifier.fillMaxSize().testTag("vip"),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp)) {
            Spacer(Modifier.height(40.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack, modifier = Modifier.testTag("vip-back")) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
                Text(
                    text = "VIP CONTACTS",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.outline,
                    letterSpacing = 2.sp
                )
            }
            Spacer(Modifier.height(16.dp))
            Text(
                text = "Calls/SMS from these numbers can break through silent-mode locks.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.outline
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "${contacts.size} / $MAX_VIPS used",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.outline
            )
            Spacer(Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = newEntry,
                    onValueChange = { newEntry = it.filter { c -> c.isDigit() || c == '+' || c == ' ' || c == '-' } },
                    modifier = Modifier.weight(1f).testTag("vip-input"),
                    placeholder = { Text("Phone number") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                )
                Spacer(Modifier.height(8.dp).then(Modifier))
                Button(
                    onClick = {
                        val normalized = newEntry.replace(Regex("[\\s-]"), "")
                        if (normalized.length >= 5 && contacts.size < MAX_VIPS && normalized !in contacts) {
                            scope.launch { prefs.setVipContacts(contacts + normalized) }
                            newEntry = ""
                        }
                    },
                    enabled = newEntry.replace(Regex("[\\s-]"), "").length >= 5 && contacts.size < MAX_VIPS,
                    modifier = Modifier.padding(start = 8.dp).testTag("vip-add")
                ) { Text("Add") }
            }

            Spacer(Modifier.height(12.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(contacts.toList(), key = { it }) { number ->
                    VipRow(number = number, onDelete = {
                        scope.launch { prefs.setVipContacts(contacts - number) }
                    })
                }
            }
        }
    }
}

@Composable
private fun VipRow(number: String, onDelete: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth().testTag("vip-row-$number"),
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = number,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
            IconButton(onClick = onDelete, modifier = Modifier.testTag("vip-delete-$number")) {
                Icon(Icons.Default.Close, contentDescription = "Remove")
            }
        }
    }
}
