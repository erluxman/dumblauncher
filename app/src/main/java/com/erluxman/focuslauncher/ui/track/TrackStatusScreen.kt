package com.erluxman.focuslauncher.ui.track

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.erluxman.focuslauncher.data.prefs.UserPrefs
import com.erluxman.focuslauncher.service.tracks.UserLevel
import com.erluxman.focuslauncher.ui.home.minimal.MinimalTheme

/**
 * Read-only track status: level, points, misses, recalibrated flag.
 * Behind FlagKey.TRACK_STATUS.
 */
@Composable
fun TrackStatusScreen(
    prefs: UserPrefs,
    onBack: () -> Unit,
) {
    val level by prefs.trackLevel.collectAsState(initial = 1)
    val points by prefs.trackPoints.collectAsState(initial = 0)
    val misses by prefs.trackMisses.collectAsState(initial = 0)
    val recal by prefs.trackRecalibrated.collectAsState(initial = false)

    val pctToNext = (points.toFloat() / 100f).coerceIn(0f, 1f)  // simple "100 points/level" surface

    Surface(
        modifier = Modifier.fillMaxSize().testTag("track-status"),
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
                    .testTag("track-back")
                    .clickable { onBack() }
                    .padding(8.dp),
            )

            Spacer(Modifier.height(24.dp))
            Text("track.", style = displayStyle, color = MinimalTheme.fg)
            Spacer(Modifier.height(8.dp))
            Text(
                "where you are on the 10-level ladder.",
                style = bodyStyle.copy(fontSize = 14.sp),
                color = MinimalTheme.outline,
            )

            Spacer(Modifier.height(40.dp))
            Text("level", style = captionStyle, color = MinimalTheme.outline)
            Spacer(Modifier.height(4.dp))
            Text(
                text = "$level",
                style = TextStyle(fontSize = 64.sp, fontWeight = FontWeight.Normal),
                color = MinimalTheme.accent,
                modifier = Modifier.testTag("track-level"),
            )

            if (recal) {
                Spacer(Modifier.height(8.dp))
                Text(
                    "recalibrated — be kind.",
                    style = bodyStyle,
                    color = MinimalTheme.outline,
                    modifier = Modifier.testTag("track-recalibrated"),
                )
            }

            Spacer(Modifier.height(32.dp))
            Text("points (this level)", style = captionStyle, color = MinimalTheme.outline)
            Spacer(Modifier.height(4.dp))
            Text(
                "$points",
                style = TextStyle(fontSize = 28.sp, fontWeight = FontWeight.Normal),
                color = MinimalTheme.fg,
                modifier = Modifier.testTag("track-points"),
            )
            Spacer(Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(MinimalTheme.outline.copy(alpha = 0.3f))
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth(pctToNext)
                    .height(2.dp)
                    .background(MinimalTheme.accent)
                    .testTag("track-progress")
            )

            Spacer(Modifier.height(32.dp))
            Text("misses", style = captionStyle, color = MinimalTheme.outline)
            Spacer(Modifier.height(4.dp))
            Text(
                "$misses",
                style = TextStyle(fontSize = 28.sp, fontWeight = FontWeight.Normal),
                color = if (misses > 0) MinimalTheme.fg else MinimalTheme.outline,
                modifier = Modifier.testTag("track-misses"),
            )

            Spacer(Modifier.height(40.dp))
            Text(
                "the lobby shaves 1s for every level above 1 (floor ${UserLevel.MIN_FLOOR_S}s).",
                style = captionStyle,
                color = MinimalTheme.outline.copy(alpha = 0.6f),
            )
            Spacer(Modifier.height(48.dp))
        }
    }
}

private val displayStyle = TextStyle(fontSize = 32.sp, fontWeight = FontWeight.Normal, letterSpacing = 0.sp)
private val bodyStyle = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Normal, letterSpacing = 0.sp)
private val captionStyle = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Normal)
