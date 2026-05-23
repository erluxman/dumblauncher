package com.erluxman.focuslauncher.ui.earnedpixels

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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.erluxman.focuslauncher.data.prefs.UserPrefs
import com.erluxman.focuslauncher.service.tracks.EarnedPixels
import com.erluxman.focuslauncher.ui.home.minimal.MinimalTheme

/**
 * GAMIFY-007 — Earned pixels. Saturation 0..1 from focus points. The
 * full spec is a launcher-wide color matrix; this screen is the
 * read-only progress + a saturated swatch preview.
 *
 * Behind FlagKey.EARNED_PIXELS.
 */
@Composable
fun EarnedPixelsScreen(
    prefs: UserPrefs,
    onBack: () -> Unit,
) {
    val trackPoints by prefs.trackPoints.collectAsState(initial = 0)
    val saturation = remember(trackPoints) { EarnedPixels.saturation(trackPoints).toFloat() }
    val pct = EarnedPixels.pctEarned(trackPoints)
    // Approximate "saturated amber" lerp from gray toward MinimalTheme.accent.
    val swatch = remember(saturation) {
        val a = MinimalTheme.accent
        val g = Color(0xFF6E6E78)
        Color(
            red = g.red + (a.red - g.red) * saturation,
            green = g.green + (a.green - g.green) * saturation,
            blue = g.blue + (a.blue - g.blue) * saturation,
            alpha = 1f,
        )
    }

    Surface(
        modifier = Modifier.fillMaxSize().testTag("earned-pixels"),
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
                    .testTag("earned-pixels-back")
                    .clickable { onBack() }
                    .padding(8.dp),
            )

            Spacer(Modifier.height(24.dp))
            Text("earned pixels.", style = displayStyle, color = MinimalTheme.fg)
            Spacer(Modifier.height(8.dp))
            Text(
                "color is earned. focus points → saturation. eventually this re-skins the whole launcher.",
                style = bodyStyle.copy(fontSize = 14.sp),
                color = MinimalTheme.outline,
            )

            Spacer(Modifier.height(32.dp))
            Text("$pct% earned", style = captionStyle, color = MinimalTheme.outline)
            Spacer(Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(MinimalTheme.outline.copy(alpha = 0.3f))
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth(saturation)
                    .height(3.dp)
                    .background(MinimalTheme.accent)
                    .testTag("earned-pixels-progress")
            )

            Spacer(Modifier.height(32.dp))
            Text("preview", style = captionStyle, color = MinimalTheme.outline)
            Spacer(Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(swatch)
                    .testTag("earned-pixels-swatch")
            )

            Spacer(Modifier.height(24.dp))
            Text(
                "earn ${(EarnedPixels.DEFAULT_TARGET_POINTS - trackPoints).coerceAtLeast(0)} more points for full color.",
                style = bodyStyle.copy(fontSize = 14.sp),
                color = MinimalTheme.outline,
            )
            Spacer(Modifier.height(48.dp))
        }
    }
}

private val displayStyle = TextStyle(fontSize = 32.sp, fontWeight = FontWeight.Normal, letterSpacing = 0.sp)
private val bodyStyle = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Normal, letterSpacing = 0.sp)
private val captionStyle = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Normal)
