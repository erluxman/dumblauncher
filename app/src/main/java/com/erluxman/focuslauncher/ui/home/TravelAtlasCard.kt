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
import com.erluxman.focuslauncher.service.TravelAtlas
import java.util.Calendar

@Composable
fun TravelAtlasCard(
    visits: List<TravelAtlas.Visit>,
    onAdd: (year: Int, location: String) -> Unit,
    onRemove: (entry: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val outline = MaterialTheme.colorScheme.outline
    val currentYear = remember { Calendar.getInstance().get(Calendar.YEAR) }
    var location by remember { mutableStateOf("") }
    var year by remember { mutableStateOf(currentYear.toString()) }

    Column(modifier = modifier.testTag("travel-atlas-card")) {
        Text(
            "TRAVEL ATLAS",
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
                        Text("PLACES", style = MaterialTheme.typography.labelSmall, color = outline, letterSpacing = 1.5.sp)
                        Text(
                            text = "${TravelAtlas.distinctLocations(visits)}",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.testTag("travel-distinct")
                        )
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text("YEARS", style = MaterialTheme.typography.labelSmall, color = outline, letterSpacing = 1.5.sp)
                        Text(
                            text = "${TravelAtlas.yearsCovered(visits)}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.testTag("travel-years")
                        )
                    }
                }
                Spacer(Modifier.height(12.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    OutlinedTextField(
                        value = year,
                        onValueChange = { year = it.filter { c -> c.isDigit() }.take(4) },
                        placeholder = { Text("YYYY") },
                        singleLine = true,
                        modifier = Modifier.width(96.dp).testTag("travel-year-input"),
                    )
                    OutlinedTextField(
                        value = location,
                        onValueChange = { location = it.take(48) },
                        placeholder = { Text("Place") },
                        singleLine = true,
                        modifier = Modifier.weight(1f).testTag("travel-location-input"),
                    )
                    IconButton(
                        onClick = {
                            val y = year.toIntOrNull() ?: return@IconButton
                            onAdd(y, location)
                            location = ""
                        },
                        modifier = Modifier.testTag("travel-add")
                    ) {
                        Icon(Icons.Filled.Add, contentDescription = "Add travel")
                    }
                }
                if (visits.isNotEmpty()) {
                    Spacer(Modifier.height(8.dp))
                    visits.take(12).forEachIndexed { i, v ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth().testTag("travel-row-$i"),
                        ) {
                            Text(
                                text = "${v.year}",
                                style = MaterialTheme.typography.labelLarge,
                                color = outline,
                                modifier = Modifier.width(48.dp)
                            )
                            Text(
                                text = v.location,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.weight(1f)
                            )
                            IconButton(
                                onClick = { onRemove(v.raw) },
                                modifier = Modifier.testTag("travel-remove-$i")
                            ) {
                                Icon(Icons.Filled.Close, contentDescription = "Remove travel")
                            }
                        }
                    }
                }
            }
        }
    }
}
