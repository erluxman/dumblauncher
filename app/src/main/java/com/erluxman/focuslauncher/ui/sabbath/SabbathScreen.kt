package com.erluxman.focuslauncher.ui.sabbath

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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.erluxman.focuslauncher.backend.BackendRepository
import com.erluxman.focuslauncher.ui.home.minimal.MinimalTheme
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * COUPLES-002 — Phone Sabbath Together. Schedule a shared full-day
 * disconnect with the paired partner. Behind FlagKey.SABBATH.
 */
@Composable
fun SabbathScreen(
    backend: BackendRepository,
    onBack: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val partner by backend.couplesPartner.collectAsState(initial = null)
    val at by backend.phoneSabbathAt.collectAsState(initial = null)
    val fmt = SimpleDateFormat("EEE, MMM d — h a", Locale.US)

    val presets = remember {
        listOf(
            "this saturday 9am" to nextWeekday(Calendar.SATURDAY, 9),
            "this sunday 9am" to nextWeekday(Calendar.SUNDAY, 9),
            "next saturday 9am" to nextWeekday(Calendar.SATURDAY, 9) + 7 * 86_400_000L,
        )
    }

    Surface(
        modifier = Modifier.fillMaxSize().testTag("sabbath"),
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
                    .testTag("sabbath-back")
                    .clickable { onBack() }
                    .padding(8.dp),
            )

            Spacer(Modifier.height(24.dp))
            Text("phone sabbath.", style = displayStyle, color = MinimalTheme.fg)
            Spacer(Modifier.height(8.dp))
            Text(
                "a full day, both phones face down. schedule it together.",
                style = bodyStyle.copy(fontSize = 14.sp),
                color = MinimalTheme.outline,
            )

            Spacer(Modifier.height(32.dp))
            partner?.let {
                Text("with ${it.name}", style = bodyStyle, color = MinimalTheme.outline,
                    modifier = Modifier.testTag("sabbath-partner"))
                Spacer(Modifier.height(16.dp))
            } ?: run {
                Text("(no partner paired — sabbath without one is a solo retreat)",
                    style = captionStyle, color = MinimalTheme.outline)
                Spacer(Modifier.height(16.dp))
            }

            if (at != null) {
                Text("scheduled", style = captionStyle, color = MinimalTheme.outline)
                Spacer(Modifier.height(4.dp))
                Text(fmt.format(Date(at!!)),
                    style = TextStyle(fontSize = 28.sp, fontWeight = FontWeight.Normal),
                    color = MinimalTheme.accent, modifier = Modifier.testTag("sabbath-current"))
                Spacer(Modifier.height(16.dp))
                Text(
                    "cancel",
                    style = bodyStyle,
                    color = MinimalTheme.outline,
                    modifier = Modifier
                        .testTag("sabbath-cancel")
                        .clickable { scope.launch { backend.cancelSabbath() } }
                        .padding(vertical = 8.dp),
                )
                Spacer(Modifier.height(24.dp))
            }

            Text(if (at == null) "schedule" else "reschedule",
                style = captionStyle, color = MinimalTheme.outline)
            Spacer(Modifier.height(12.dp))
            presets.forEachIndexed { i, (label, ts) ->
                Text(
                    label,
                    style = bodyStyle,
                    color = MinimalTheme.accent,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("sabbath-preset-$i")
                        .clickable { scope.launch { backend.scheduleSabbath(ts) } }
                        .padding(vertical = 10.dp),
                )
            }
            Spacer(Modifier.height(48.dp))
        }
    }
}

private fun nextWeekday(weekday: Int, hour: Int): Long {
    val cal = Calendar.getInstance()
    val today = cal.get(Calendar.DAY_OF_WEEK)
    val daysAhead = ((weekday - today) + 7) % 7
    cal.add(Calendar.DAY_OF_MONTH, if (daysAhead == 0) 7 else daysAhead)
    cal.set(Calendar.HOUR_OF_DAY, hour)
    cal.set(Calendar.MINUTE, 0); cal.set(Calendar.SECOND, 0); cal.set(Calendar.MILLISECOND, 0)
    return cal.timeInMillis
}

private val displayStyle = TextStyle(fontSize = 32.sp, fontWeight = FontWeight.Normal, letterSpacing = 0.sp)
private val bodyStyle = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Normal, letterSpacing = 0.sp)
private val captionStyle = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Normal)
