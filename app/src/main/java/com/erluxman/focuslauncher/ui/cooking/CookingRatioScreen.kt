package com.erluxman.focuslauncher.ui.cooking

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.erluxman.focuslauncher.data.prefs.UserPrefs
import com.erluxman.focuslauncher.data.prefs.bumpCooked
import com.erluxman.focuslauncher.data.prefs.bumpEatenOut
import com.erluxman.focuslauncher.data.prefs.mealsCooked
import com.erluxman.focuslauncher.data.prefs.mealsEatenOut
import com.erluxman.focuslauncher.ui.home.minimal.MinimalTheme
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * NUT-004 — Cooking ratio. Two simple counters: meals cooked vs eaten out
 * per day. Behind FlagKey.COOKING.
 */
@Composable
fun CookingRatioScreen(
    prefs: UserPrefs,
    onBack: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val today = remember { SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date()) }
    val last7 = remember {
        val cal = Calendar.getInstance()
        val fmt = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        (0..6).map {
            val c = cal.clone() as Calendar
            c.add(Calendar.DAY_OF_MONTH, -it)
            fmt.format(c.time)
        }
    }
    val cookedRaw by prefs.mealsCooked.collectAsState(initial = emptySet())
    val outRaw by prefs.mealsEatenOut.collectAsState(initial = emptySet())

    fun count(set: Set<String>, dates: Collection<String>): Int =
        set.mapNotNull {
            val parts = it.split("|", limit = 2)
            if (parts.getOrNull(0) in dates) parts.getOrNull(1)?.toIntOrNull() else null
        }.sum()

    val cookedToday = count(cookedRaw, listOf(today))
    val outToday = count(outRaw, listOf(today))
    val cookedWeek = count(cookedRaw, last7)
    val outWeek = count(outRaw, last7)
    val weekTotal = cookedWeek + outWeek
    val weekPct = if (weekTotal > 0) cookedWeek * 100 / weekTotal else 0

    Surface(
        modifier = Modifier.fillMaxSize().testTag("cooking"),
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
                    .testTag("cooking-back")
                    .clickable { onBack() }
                    .padding(8.dp),
            )

            Spacer(Modifier.height(24.dp))
            Text("cooking ratio.", style = displayStyle, color = MinimalTheme.fg)
            Spacer(Modifier.height(8.dp))
            Text(
                "meals cooked vs eaten out.",
                style = bodyStyle.copy(fontSize = 14.sp),
                color = MinimalTheme.outline,
            )

            Spacer(Modifier.height(32.dp))
            Text("today", style = captionStyle, color = MinimalTheme.outline)
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                Counter("cooked", cookedToday, "cooking-cooked-today", onBump = {
                    scope.launch { prefs.bumpCooked(today, +1) }
                }, onMinus = { scope.launch { prefs.bumpCooked(today, -1) } })
                Counter("eaten out", outToday, "cooking-out-today", onBump = {
                    scope.launch { prefs.bumpEatenOut(today, +1) }
                }, onMinus = { scope.launch { prefs.bumpEatenOut(today, -1) } })
            }

            Spacer(Modifier.height(40.dp))
            Text("last 7 days", style = captionStyle, color = MinimalTheme.outline)
            Spacer(Modifier.height(4.dp))
            Text(
                "$cookedWeek cooked · $outWeek out · ${weekPct}% home",
                style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Normal),
                color = if (weekPct >= 70) MinimalTheme.accent else MinimalTheme.fg,
                modifier = Modifier.testTag("cooking-week"),
            )
            Spacer(Modifier.height(48.dp))
        }
    }
}

@Composable
private fun Counter(label: String, value: Int, tag: String, onBump: () -> Unit, onMinus: () -> Unit) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(label, style = captionStyle, color = MinimalTheme.outline)
        Spacer(Modifier.height(4.dp))
        Text(
            "$value",
            style = TextStyle(fontSize = 48.sp, fontWeight = FontWeight.Normal),
            color = MinimalTheme.accent,
            modifier = Modifier.testTag(tag),
        )
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Text("−", style = TextStyle(fontSize = 28.sp), color = MinimalTheme.outline,
                modifier = Modifier.clickable { onMinus() }.padding(8.dp))
            Text("+", style = TextStyle(fontSize = 28.sp), color = MinimalTheme.accent,
                modifier = Modifier.clickable { onBump() }.padding(8.dp))
        }
    }
}

private val displayStyle = TextStyle(fontSize = 32.sp, fontWeight = FontWeight.Normal, letterSpacing = 0.sp)
private val bodyStyle = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Normal, letterSpacing = 0.sp)
private val captionStyle = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Normal)
