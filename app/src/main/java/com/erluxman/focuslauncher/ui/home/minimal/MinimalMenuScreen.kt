package com.erluxman.focuslauncher.ui.home.minimal

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.erluxman.focuslauncher.config.FeatureFlagsRepository
import com.erluxman.focuslauncher.config.FlagKey

/**
 * Minimal menu — the single fan-out to every feature in the app.
 *
 * Reached from MinimalHomeScreen via the ↓ hint tap, the inline "(open menu)" tap,
 * the settings-dock tap, or a swipe-down gesture. Text rows only, no cards.
 */
@Composable
fun MinimalMenuScreen(
    flagsRepo: FeatureFlagsRepository,
    onBack: () -> Unit,
    onOpenStats: () -> Unit,
    onOpenDashboard: () -> Unit,
    onOpenTransparency: () -> Unit,
    onOpenVip: () -> Unit,
    onOpenFocus: () -> Unit,
    onOpenMantra: () -> Unit,
    onOpenBoredom: () -> Unit,
    onOpenBreath: () -> Unit,
    onOpenFutureSelfVideo: () -> Unit,
    onReplayOnboarding: () -> Unit,
    onOpenUninstall: () -> Unit,
    onOpenFeatureFlags: () -> Unit,
    onOpenExport: () -> Unit,
    onOpenIdentity: () -> Unit,
    onOpenGraduate: () -> Unit,
    onOpenWrapped: () -> Unit,
    onOpenSubscriptions: () -> Unit,
    onOpenSleepCorrelator: () -> Unit,
    onOpenMood: () -> Unit,
    onOpenIdeas: () -> Unit,
    onOpenConsumption: () -> Unit,
    onOpenJournal: () -> Unit,
    onOpenPrWall: () -> Unit,
    onOpenTravel: () -> Unit,
    onOpenCompound: () -> Unit,
    onOpenMoney: () -> Unit,
    onOpenAntiBio: () -> Unit,
    onOpenSleepWindow: () -> Unit,
    onOpenDailyLogs: () -> Unit,
    onOpenHighlights: () -> Unit,
    onOpenTombstones: () -> Unit,
    onOpenFutureLetters: () -> Unit,
    onOpenWeeklyReview: () -> Unit,
    onOpenPromises: () -> Unit,
    onOpenLegacy: () -> Unit,
    onOpenDilation: () -> Unit,
    onOpenSadSelf: () -> Unit,
    onOpenTimeMoney: () -> Unit,
) {
    val flags by flagsRepo.effective.collectAsState(initial = flagsRepo.defaults)
    fun on(key: String): Boolean = flags[key] ?: true
    Surface(
        modifier = Modifier.fillMaxSize().testTag("minimal-menu"),
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
                style = TextStyle(fontSize = 14.sp),
                color = MinimalTheme.outline,
                modifier = Modifier
                    .testTag("menu-back")
                    .clickable { onBack() }
                    .padding(8.dp),
            )

            Spacer(Modifier.height(24.dp))
            Text(
                text = "everything.",
                style = displayStyle,
                color = MinimalTheme.fg,
            )
            Spacer(Modifier.height(24.dp))

            if (on(FlagKey.STATS_SHEET))
                MenuRow("stats", "today, this week, this year — in sentences", "menu-stats", onOpenStats)
            if (on(FlagKey.LEGACY_DASHBOARD))
                MenuRow("dashboard", "all your data, the legacy cards", "menu-dashboard", onOpenDashboard)
            if (on(FlagKey.TRANSPARENCY))
                MenuRow("transparency", "opt out of any technique", "menu-transparency", onOpenTransparency)
            if (on(FlagKey.VIP_CONTACTS))
                MenuRow("vip contacts", "who can still reach you", "menu-vip", onOpenVip)
            if (on(FlagKey.FOCUS_TIMER))
                MenuRow("focus timer", "25 · 5 pomodoro", "menu-focus", onOpenFocus)
            if (on(FlagKey.MANTRA))
                MenuRow("mantra", "phrase that unlocks apps", "menu-mantra", onOpenMantra)
            if (on(FlagKey.BOREDOM))
                MenuRow("boredom", "two minutes with nothing", "menu-boredom", onOpenBoredom)
            if (on(FlagKey.BREATH_UNLOCK))
                MenuRow("breath unlock", "4 · 7 · 8", "menu-breath", onOpenBreath)
            if (on(FlagKey.FUTURE_SELF_VIDEO))
                MenuRow("future self", "record a video to future you", "menu-future-self", onOpenFutureSelfVideo)
            if (on(FlagKey.ONBOARDING))
                MenuRow("replay onboarding", "redo the setup", "menu-onboarding", onReplayOnboarding)
            if (on(FlagKey.DATA_EXPORT))
                MenuRow("export data", "json or csv dump of everything", "menu-export", onOpenExport)
            if (on(FlagKey.IDENTITY_VOTING))
                MenuRow("identity", "builder vs consumer — vote with every action", "menu-identity", onOpenIdentity)
            if (on(FlagKey.GRADUATE_MODE))
                MenuRow("graduate", "your progress out of the friction system", "menu-graduate", onOpenGraduate)
            if (on(FlagKey.BUILT_WRAPPED))
                MenuRow("wrapped", "year-in-review — what you built", "menu-wrapped", onOpenWrapped)
            if (on(FlagKey.SUBSCRIPTIONS))
                MenuRow("subscriptions", "monthly burn — manual entries", "menu-subscriptions", onOpenSubscriptions)
            if (on(FlagKey.SLEEP_CORRELATOR))
                MenuRow("sleep ↔ output", "did last night's sleep predict tomorrow?", "menu-sleep-correlator", onOpenSleepCorrelator)
            if (on(FlagKey.MOOD_PINGS))
                MenuRow("mood", "tap an emoji, log the moment", "menu-mood", onOpenMood)
            if (on(FlagKey.IDEA_PARKING))
                MenuRow("park an idea", "capture and forget", "menu-ideas", onOpenIdeas)
            if (on(FlagKey.CONSUMPTION_LOG))
                MenuRow("consumption", "caffeine + drinks, last 24h", "menu-consumption", onOpenConsumption)
            if (on(FlagKey.JOURNAL))
                MenuRow("journal", "one entry, however short", "menu-journal", onOpenJournal)
            if (on(FlagKey.PR_WALL))
                MenuRow("pr wall", "your personal records", "menu-pr-wall", onOpenPrWall)
            if (on(FlagKey.TRAVEL_ATLAS))
                MenuRow("travel atlas", "where you've been", "menu-travel", onOpenTravel)
            if (on(FlagKey.COMPOUND_CURVE))
                MenuRow("compounding", "1%/day is exponential", "menu-compound", onOpenCompound)
            if (on(FlagKey.MONEY_MIRROR))
                MenuRow("money", "income / expense / net worth", "menu-money", onOpenMoney)
            if (on(FlagKey.ANTI_BIO))
                MenuRow("anti-bio", "the one thing you are not", "menu-anti-bio", onOpenAntiBio)
            if (on(FlagKey.SLEEP_WINDOW))
                MenuRow("sleep window", "cutoff + wake hours", "menu-sleep-window", onOpenSleepWindow)
            if (on(FlagKey.DAILY_LOGS))
                MenuRow("daily logs", "meditation, reading, workout, commits", "menu-daily-logs", onOpenDailyLogs)
            if (on(FlagKey.HIGHLIGHTS))
                MenuRow("highlights", "lines you want to keep", "menu-highlights", onOpenHighlights)
            if (on(FlagKey.TOMBSTONES))
                MenuRow("tombstones", "apps you killed", "menu-tombstones", onOpenTombstones)
            if (on(FlagKey.FUTURE_LETTERS))
                MenuRow("future letters", "write to a future you", "menu-future-letters", onOpenFutureLetters)
            if (on(FlagKey.WEEKLY_REVIEW))
                MenuRow("week review", "last 7 days at a glance", "menu-weekly-review", onOpenWeeklyReview)
            if (on(FlagKey.PROMISE_RATIO))
                MenuRow("promises", "kept vs broken todos", "menu-promises", onOpenPromises)
            if (on(FlagKey.LEGACY_COUNTER))
                MenuRow("legacy", "cumulative builder minutes", "menu-legacy", onOpenLegacy)
            if (on(FlagKey.TIME_DILATION))
                MenuRow("time dilation", "what 30 minutes really felt like", "menu-dilation", onOpenDilation)
            if (on(FlagKey.SAD_SELF))
                MenuRow("sad self", "pick the voice that nudges you", "menu-sad-self", onOpenSadSelf)
            if (on(FlagKey.TIME_MONEY))
                MenuRow("time = money", "opportunity cost calc", "menu-time-money", onOpenTimeMoney)
            if (on(FlagKey.FEATURE_FLAGS))
                MenuRow("feature flags", "toggle features on or off", "menu-feature-flags", onOpenFeatureFlags)

            if (on(FlagKey.UNINSTALL_FLOW)) {
                Spacer(Modifier.height(48.dp))
                Text(
                    text = "uninstall",
                    style = bodyStyle,
                    color = MinimalTheme.outline,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("menu-uninstall")
                        .clickable { onOpenUninstall() }
                        .padding(vertical = 12.dp),
                )
            }
            Spacer(Modifier.height(64.dp))
        }
    }
}

@Composable
private fun MenuRow(
    title: String,
    subtitle: String,
    testTag: String,
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .testTag(testTag)
            .clickable { onClick() }
            .padding(vertical = 12.dp),
    ) {
        Text(title, style = bodyStyle, color = MinimalTheme.fg)
        Spacer(Modifier.height(2.dp))
        Text(subtitle, style = TextStyle(fontSize = 14.sp), color = MinimalTheme.outline)
    }
}

private val displayStyle = TextStyle(fontSize = 32.sp, fontWeight = FontWeight.Normal, letterSpacing = 0.sp)
private val bodyStyle = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Normal, letterSpacing = 0.sp)
