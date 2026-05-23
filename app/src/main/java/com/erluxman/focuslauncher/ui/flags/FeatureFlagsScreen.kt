package com.erluxman.focuslauncher.ui.flags

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
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.erluxman.focuslauncher.config.FeatureFlag
import com.erluxman.focuslauncher.config.FeatureFlagsRepository
import com.erluxman.focuslauncher.ui.home.minimal.MinimalTheme
import kotlinx.coroutines.launch

/**
 * In-app settings screen for build-time feature flags. Definitions come from
 * `assets/featureflags.json`; toggling a row writes a user override to
 * DataStore via [FeatureFlagsRepository]. Required flags render locked.
 *
 * Same monochrome aesthetic as the rest of the minimal surface.
 */
@Composable
fun FeatureFlagsScreen(
    repo: FeatureFlagsRepository,
    onBack: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val effective by repo.effective.collectAsState(initial = repo.defaults)
    val grouped = remember(repo.definitions) { repo.definitions.groupBy { it.stage } }

    Surface(
        modifier = Modifier.fillMaxSize().testTag("feature-flags"),
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
                text = "← back",
                style = captionStyle,
                color = MinimalTheme.outline,
                modifier = Modifier
                    .testTag("flags-back")
                    .clickable { onBack() }
                    .padding(8.dp),
            )

            Spacer(Modifier.height(24.dp))
            Text("feature flags.", style = displayStyle, color = MinimalTheme.fg)
            Spacer(Modifier.height(8.dp))
            Text(
                "toggle a feature on or off. required ones can't be disabled.",
                style = bodyStyle.copy(fontSize = 14.sp),
                color = MinimalTheme.outline,
            )

            Spacer(Modifier.height(24.dp))

            grouped.toSortedMap().forEach { (stage, flags) ->
                Spacer(Modifier.height(16.dp))
                Text(
                    "stage $stage",
                    style = captionStyle,
                    color = MinimalTheme.outline,
                    modifier = Modifier.testTag("flags-stage-$stage"),
                )
                Spacer(Modifier.height(12.dp))
                flags.forEach { flag ->
                    FlagRow(
                        flag = flag,
                        enabled = effective[flag.key] ?: flag.default,
                        onToggle = { newValue ->
                            scope.launch { repo.setOverride(flag.key, newValue) }
                        },
                    )
                }
            }

            Spacer(Modifier.height(40.dp))
            Text(
                text = "reset all overrides",
                style = bodyStyle,
                color = MinimalTheme.accent,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("flags-reset-all")
                    .clickable { scope.launch { repo.resetAll() } }
                    .padding(vertical = 12.dp),
            )
            Spacer(Modifier.height(48.dp))
        }
    }
}

@Composable
private fun FlagRow(
    flag: FeatureFlag,
    enabled: Boolean,
    onToggle: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("flag-${flag.key}")
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(flag.label, style = bodyStyle, color = MinimalTheme.fg)
            Spacer(Modifier.height(2.dp))
            Text(
                text = flag.description + if (flag.required) "  ·  required" else "",
                style = TextStyle(fontSize = 13.sp),
                color = MinimalTheme.outline,
            )
        }
        Switch(
            checked = enabled,
            enabled = !flag.required,
            onCheckedChange = { onToggle(it) },
            modifier = Modifier.testTag("flag-switch-${flag.key}"),
            colors = SwitchDefaults.colors(
                checkedThumbColor = MinimalTheme.accent,
                checkedTrackColor = MinimalTheme.accent.copy(alpha = 0.4f),
                uncheckedThumbColor = MinimalTheme.outline,
                uncheckedTrackColor = Color.Transparent,
                uncheckedBorderColor = MinimalTheme.outline,
                disabledCheckedThumbColor = MinimalTheme.outline,
                disabledCheckedTrackColor = MinimalTheme.outline.copy(alpha = 0.25f),
            ),
        )
    }
}

private val displayStyle = TextStyle(fontSize = 32.sp, fontWeight = FontWeight.Normal, letterSpacing = 0.sp)
private val bodyStyle = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Normal, letterSpacing = 0.sp)
private val captionStyle = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Normal)
