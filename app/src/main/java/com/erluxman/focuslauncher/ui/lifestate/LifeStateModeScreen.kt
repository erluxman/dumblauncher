package com.erluxman.focuslauncher.ui.lifestate

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.erluxman.focuslauncher.data.prefs.UserPrefs
import com.erluxman.focuslauncher.data.prefs.lifeStateMode
import com.erluxman.focuslauncher.data.prefs.setLifeStateMode
import com.erluxman.focuslauncher.ui.home.minimal.MinimalTheme
import kotlinx.coroutines.launch

/**
 * RESTRICT-022 — Life-state mode set. Single active mode at a time
 * (commute / travel / sick / vacation / weekend). Lobby/Restrictions
 * adapt by mode in follow-ups. Behind FlagKey.LIFE_STATE.
 */
@Composable
fun LifeStateModeScreen(
    prefs: UserPrefs,
    onBack: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val current by prefs.lifeStateMode.collectAsState(initial = "")
    val modes = listOf(
        "normal" to "default rules apply",
        "commute" to "music + audiobooks only",
        "travel" to "maps + comms loose, social off",
        "sick" to "all rules eased, dream stays on",
        "vacation" to "social tier silenced, no lockouts",
        "weekend" to "stricter mornings, looser evenings",
    )

    Surface(
        modifier = Modifier.fillMaxSize().testTag("life-state"),
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
                    .testTag("life-state-back")
                    .clickable { onBack() }
                    .padding(8.dp),
            )

            Spacer(Modifier.height(24.dp))
            Text("life state.", style = displayStyle, color = MinimalTheme.fg)
            Spacer(Modifier.height(8.dp))
            Text(
                "tell the launcher what kind of day this is. lobby + restrictions adapt.",
                style = bodyStyle.copy(fontSize = 14.sp),
                color = MinimalTheme.outline,
            )

            Spacer(Modifier.height(32.dp))
            Text("active", style = captionStyle, color = MinimalTheme.outline)
            Text(
                if (current.isBlank()) "normal" else current,
                style = TextStyle(fontSize = 28.sp, fontWeight = FontWeight.Normal),
                color = MinimalTheme.accent,
                modifier = Modifier.testTag("life-state-current"),
            )

            Spacer(Modifier.height(24.dp))
            modes.forEach { (name, desc) ->
                val sel = current.equals(name, ignoreCase = true) || (current.isBlank() && name == "normal")
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp)
                        .testTag("life-state-mode-$name")
                        .clickable { scope.launch { prefs.setLifeStateMode(if (name == "normal") "" else name) } },
                ) {
                    Text(
                        text = (if (sel) "● " else "○ ") + name,
                        style = bodyStyle,
                        color = if (sel) MinimalTheme.accent else MinimalTheme.fg,
                    )
                    Text(desc, style = captionStyle, color = MinimalTheme.outline)
                }
            }
            Spacer(Modifier.height(48.dp))
        }
    }
}

private val displayStyle = TextStyle(fontSize = 32.sp, fontWeight = FontWeight.Normal, letterSpacing = 0.sp)
private val bodyStyle = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Normal, letterSpacing = 0.sp)
private val captionStyle = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Normal)
