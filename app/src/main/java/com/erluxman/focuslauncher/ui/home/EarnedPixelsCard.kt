package com.erluxman.focuslauncher.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.erluxman.focuslauncher.service.tracks.EarnedPixels

@Composable
fun EarnedPixelsCard(
    focusPoints: Int,
    modifier: Modifier = Modifier,
) {
    if (focusPoints <= 0) return
    val pct = EarnedPixels.pctEarned(focusPoints)
    val outline = MaterialTheme.colorScheme.outline

    Column(modifier = modifier.testTag("earned-pixels-card")) {
        Text(
            "EARNED PIXELS",
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
                        Text("COLOUR EARNED", style = MaterialTheme.typography.labelSmall, color = outline, letterSpacing = 1.5.sp)
                        Text(
                            text = "$pct%",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.testTag("earned-pct")
                        )
                    }
                    Text(
                        text = "$focusPoints / ${EarnedPixels.DEFAULT_TARGET_POINTS} pts",
                        style = MaterialTheme.typography.labelLarge,
                        color = outline,
                    )
                }
                Spacer(Modifier.height(8.dp))
                Box(modifier = Modifier.fillMaxWidth().height(6.dp).testTag("earned-bar")) {
                    Surface(
                        color = outline.copy(alpha = 0.2f),
                        modifier = Modifier.fillMaxWidth().height(6.dp),
                        shape = RoundedCornerShape(3.dp)
                    ) {}
                    Surface(
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .height(6.dp)
                            .fillMaxWidth(pct / 100f),
                        shape = RoundedCornerShape(3.dp)
                    ) {}
                }
            }
        }
    }
}
