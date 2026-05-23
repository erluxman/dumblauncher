package com.erluxman.focuslauncher.ui.funeral

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.erluxman.focuslauncher.data.prefs.UserPrefs
import com.erluxman.focuslauncher.data.prefs.funeralLabels
import com.erluxman.focuslauncher.data.prefs.holdFuneralFor
import com.erluxman.focuslauncher.ui.home.minimal.MinimalTheme
import kotlinx.coroutines.launch

/**
 * SOCIAL-040 — Public Phone Funeral. 30 days zero opens of an app → hold a
 * ceremony. Reuses the existing tombstones list; marks labels as
 * "funeral held" via a separate funeralLabels set.
 *
 * Behind FlagKey.FUNERAL.
 */
@Composable
fun PhoneFuneralScreen(
    prefs: UserPrefs,
    onBack: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val tombstones by prefs.appTombstones.collectAsState(initial = emptySet())
    val funerals by prefs.funeralLabels.collectAsState(initial = emptySet())

    val tombList = tombstones.mapNotNull { e ->
        val parts = e.split("|", limit = 2)
        parts.getOrNull(0)?.takeIf { it.isNotBlank() }
    }

    Surface(
        modifier = Modifier.fillMaxSize().testTag("funeral"),
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
                    .testTag("funeral-back")
                    .clickable { onBack() }
                    .padding(8.dp),
            )

            Spacer(Modifier.height(24.dp))
            Text("phone funerals.", style = displayStyle, color = MinimalTheme.fg)
            Spacer(Modifier.height(8.dp))
            Text(
                "30 days off an app earns a community ritual. friends witness; gravestone added.",
                style = bodyStyle.copy(fontSize = 14.sp),
                color = MinimalTheme.outline,
            )

            Spacer(Modifier.height(32.dp))
            if (tombList.isEmpty()) {
                Text("no tombstones yet. bury one in the tombstones screen first.",
                    style = bodyStyle, color = MinimalTheme.outline,
                    modifier = Modifier.testTag("funeral-empty"))
            } else {
                Text("eligible apps", style = captionStyle, color = MinimalTheme.outline)
                Spacer(Modifier.height(8.dp))
                tombList.forEach { label ->
                    val held = label in funerals
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp)
                            .testTag("funeral-row"),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = (if (held) "🪦 " else "💀 ") + label,
                            style = bodyStyle,
                            color = if (held) MinimalTheme.accent else MinimalTheme.fg,
                        )
                        if (!held) {
                            Text(
                                "hold ceremony",
                                style = captionStyle,
                                color = MinimalTheme.accent,
                                modifier = Modifier
                                    .clickable { scope.launch { prefs.holdFuneralFor(label) } }
                                    .padding(8.dp),
                            )
                        } else {
                            Text("rest in peace", style = captionStyle, color = MinimalTheme.outline)
                        }
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(MinimalTheme.outline.copy(alpha = 0.15f))
                    )
                }
            }
            Spacer(Modifier.height(48.dp))
        }
    }
}

private val displayStyle = TextStyle(fontSize = 32.sp, fontWeight = FontWeight.Normal, letterSpacing = 0.sp)
private val bodyStyle = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Normal, letterSpacing = 0.sp)
private val captionStyle = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Normal)
