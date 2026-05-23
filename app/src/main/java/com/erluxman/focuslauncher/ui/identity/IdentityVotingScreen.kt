package com.erluxman.focuslauncher.ui.identity

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.erluxman.focuslauncher.data.prefs.UserPrefs
import com.erluxman.focuslauncher.ui.home.minimal.MinimalTheme
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Identity voting — one tap declares the kind of person you're being today.
 * Resets daily. Builder vs Consumer running tally for the current day.
 * Behind FlagKey.IDENTITY_VOTING.
 */
@Composable
fun IdentityVotingScreen(
    prefs: UserPrefs,
    onBack: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val todayIso = remember { SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date()) }

    val builderRaw by prefs.identityBuilderCount.collectAsState(initial = 0)
    val consumerRaw by prefs.identityConsumerCount.collectAsState(initial = 0)
    val date by prefs.identityDate.collectAsState(initial = "")
    val sameDay = date == todayIso
    val builder = if (sameDay) builderRaw else 0
    val consumer = if (sameDay) consumerRaw else 0
    val total = builder + consumer

    Surface(
        modifier = Modifier.fillMaxSize().testTag("identity"),
        color = MinimalTheme.bg,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
        ) {
            Spacer(Modifier.height(40.dp))
            Text(
                "← back",
                style = captionStyle,
                color = MinimalTheme.outline,
                modifier = Modifier
                    .testTag("identity-back")
                    .clickable { onBack() }
                    .padding(8.dp),
            )

            Spacer(Modifier.height(40.dp))
            Text("identity.", style = displayStyle, color = MinimalTheme.fg)
            Spacer(Modifier.height(8.dp))
            Text(
                if (total == 0) "no votes yet today. every action is a vote."
                else "$total votes today. each one shapes who you are.",
                style = bodyStyle.copy(fontSize = 14.sp),
                color = MinimalTheme.outline,
            )

            Spacer(Modifier.height(48.dp))

            Text("builder", style = bodyStyle, color = MinimalTheme.outline)
            Spacer(Modifier.height(4.dp))
            Text(
                text = "$builder",
                style = TextStyle(fontSize = 48.sp, fontWeight = FontWeight.Normal),
                color = if (builder >= consumer) MinimalTheme.accent else MinimalTheme.fg,
                modifier = Modifier.testTag("identity-builder-count"),
            )

            Spacer(Modifier.height(32.dp))

            Text("consumer", style = bodyStyle, color = MinimalTheme.outline)
            Spacer(Modifier.height(4.dp))
            Text(
                text = "$consumer",
                style = TextStyle(fontSize = 48.sp, fontWeight = FontWeight.Normal),
                color = if (consumer > builder) MinimalTheme.accent else MinimalTheme.fg,
                modifier = Modifier.testTag("identity-consumer-count"),
            )

            Spacer(Modifier.weight(1f))

            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 48.dp),
                horizontalArrangement = Arrangement.spacedBy(32.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                VoteButton(
                    label = "vote builder",
                    tag = "identity-vote-builder",
                    onClick = {
                        scope.launch { prefs.voteIdentity(isBuilder = true, todayIso = todayIso) }
                    },
                )
                VoteButton(
                    label = "vote consumer",
                    tag = "identity-vote-consumer",
                    onClick = {
                        scope.launch { prefs.voteIdentity(isBuilder = false, todayIso = todayIso) }
                    },
                )
            }
        }
    }
}

@Composable
private fun VoteButton(label: String, tag: String, onClick: () -> Unit) {
    Text(
        text = label,
        style = bodyStyle,
        color = MinimalTheme.accent,
        modifier = Modifier
            .testTag(tag)
            .clickable { onClick() }
            .padding(vertical = 14.dp, horizontal = 8.dp),
    )
}

private val displayStyle = TextStyle(fontSize = 32.sp, fontWeight = FontWeight.Normal, letterSpacing = 0.sp)
private val bodyStyle = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Normal, letterSpacing = 0.sp)
private val captionStyle = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Normal)
