package com.erluxman.focuslauncher

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.erluxman.focuslauncher.service.mortality.CompoundCurve
import com.erluxman.focuslauncher.service.EarnedPixels
import com.erluxman.focuslauncher.service.GraduateState
import com.erluxman.focuslauncher.service.MeditationLog
import com.erluxman.focuslauncher.service.PatternDetect
import com.erluxman.focuslauncher.service.ReadingLog
import com.erluxman.focuslauncher.service.StressIndex
import com.erluxman.focuslauncher.service.WeeklyReview
import com.erluxman.focuslauncher.service.fitness.WorkoutLog
import com.erluxman.focuslauncher.ui.home.AnchorCard
import com.erluxman.focuslauncher.ui.home.CompoundCurveCard
import com.erluxman.focuslauncher.ui.home.DeathbedCard
import com.erluxman.focuslauncher.ui.home.EarnedPixelsCard
import com.erluxman.focuslauncher.ui.home.EstimationCard
import com.erluxman.focuslauncher.ui.home.GraduateBanner
import com.erluxman.focuslauncher.ui.home.HighlightCard
import com.erluxman.focuslauncher.ui.home.MeditationCard
import com.erluxman.focuslauncher.ui.home.PatternCard
import com.erluxman.focuslauncher.ui.home.ReadingCard
import com.erluxman.focuslauncher.ui.home.RecoveryCard
import com.erluxman.focuslauncher.ui.home.StressCard
import com.erluxman.focuslauncher.ui.home.TimeDilationCard
import com.erluxman.focuslauncher.ui.home.TimeMoneyCard
import com.erluxman.focuslauncher.ui.home.WeeklyReviewCard
import com.erluxman.focuslauncher.ui.home.WorkoutCard
import com.erluxman.focuslauncher.ui.theme.FocusLauncherTheme
import com.erluxman.focuslauncher.data.local.entity.TodoEntity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CardSweepTest {

    @get:Rule val rule = createComposeRule()

    @Test
    fun stressCard_renders() {
        rule.setContent { FocusLauncherTheme { StressCard(unlocksToday = 60, sleepMinutesLastNight = 6 * 60) } }
        rule.onNodeWithTag("stress-card").assertIsDisplayed()
        rule.onNodeWithTag("stress-total").assertIsDisplayed()
        rule.onNodeWithTag("stress-unlocks").assertIsDisplayed()
        rule.onNodeWithTag("stress-sleep").assertIsDisplayed()
    }

    @Test
    fun recoveryCard_renders() {
        rule.setContent { FocusLauncherTheme { RecoveryCard(sleepMinutes = 7 * 60, steps = 7_000) } }
        rule.onNodeWithTag("recovery-card").assertIsDisplayed()
        rule.onNodeWithTag("recovery-total").assertIsDisplayed()
    }

    @Test
    fun patternCard_rendersWhenAnyHour() {
        rule.setContent {
            FocusLauncherTheme {
                PatternCard(hourlyMinutes = IntArray(24).apply { this[14] = 20 }, nowHour = 15)
            }
        }
        rule.onNodeWithTag("pattern-card").assertIsDisplayed()
        rule.onNodeWithTag("pattern-hour").assertIsDisplayed()
    }

    @Test
    fun earnedPixels_renders() {
        rule.setContent { FocusLauncherTheme { EarnedPixelsCard(focusPoints = 35) } }
        rule.onNodeWithTag("earned-pixels-card").assertIsDisplayed()
        rule.onNodeWithTag("earned-pct").assertIsDisplayed()
    }

    @Test
    fun graduateBanner_visibleAtTopLevel() {
        rule.setContent {
            FocusLauncherTheme {
                GraduateBanner(state = GraduateState.compute(trackLevel = 10, onboardingMs = System.currentTimeMillis() - 90L * 86_400_000L))
            }
        }
        rule.onNodeWithTag("graduate-banner").assertIsDisplayed()
        rule.onNodeWithTag("graduate-headline").assertIsDisplayed()
    }

    @Test
    fun estimationCard_rendersFromTodos() {
        val todos = listOf(
            TodoEntity(text = "A", estimatedMinutes = 30, actualMinutes = 30, isCompleted = true),
            TodoEntity(text = "B", estimatedMinutes = 60, actualMinutes = 45, isCompleted = true)
        )
        rule.setContent { FocusLauncherTheme { EstimationCard(todos = todos) } }
        rule.onNodeWithTag("estimation-card").assertIsDisplayed()
        rule.onNodeWithTag("estimation-accuracy").assertIsDisplayed()
    }

    @Test
    fun anchorCard_visibleWhenEnabled() {
        rule.setContent { FocusLauncherTheme { AnchorCard(userDistractionMinutes = 90, enabled = true) } }
        rule.onNodeWithTag("anchor-card").assertIsDisplayed()
        rule.onNodeWithTag("anchor-you").assertIsDisplayed()
    }

    @Test
    fun deathbedCard_renders() {
        rule.setContent { FocusLauncherTheme { DeathbedCard(userAge = 30, dailyDistractionMinutes = 90, enabled = true) } }
        rule.onNodeWithTag("deathbed-card").assertIsDisplayed()
        rule.onNodeWithTag("deathbed-years").assertIsDisplayed()
    }

    @Test
    fun timeDilationCard_renders() {
        rule.setContent { FocusLauncherTheme { TimeDilationCard(distractionMinutes = 60) } }
        rule.onNodeWithTag("time-dilation-card").assertIsDisplayed()
        rule.onNodeWithTag("dilation-felt").assertIsDisplayed()
    }

    @Test
    fun timeMoneyCard_renders() {
        rule.setContent { FocusLauncherTheme { TimeMoneyCard(hourlyRate = 50) } }
        rule.onNodeWithTag("time-money-card").assertIsDisplayed()
    }

    @Test
    fun compoundCurveCard_renders() {
        rule.setContent { FocusLauncherTheme { CompoundCurveCard() } }
        rule.onNodeWithTag("compound-card").assertIsDisplayed()
        rule.onNodeWithTag("compound-multiplier").assertIsDisplayed()
    }

    @Test
    fun weeklyReviewCard_renders() {
        val summary = WeeklyReview.summarize(
            last7DaysIso = listOf("2026-05-22", "2026-05-21"),
            meditation = listOf(MeditationLog.Session("2026-05-22", 10, "Breath")),
            reading = listOf(ReadingLog.Session("2026-05-22", 15)),
            workout = listOf(WorkoutLog.Session("2026-05-21", 30, "Run")),
        )
        rule.setContent { FocusLauncherTheme { WeeklyReviewCard(summary = summary) } }
        rule.onNodeWithTag("weekly-review-card").assertIsDisplayed()
        rule.onNodeWithTag("review-meditation").assertIsDisplayed()
        rule.onNodeWithTag("review-reading").assertIsDisplayed()
        rule.onNodeWithTag("review-workout").assertIsDisplayed()
    }

    @Test
    fun highlightCard_renders() {
        rule.setContent { FocusLauncherTheme { HighlightCard(todayIso = "2026-05-22", highlights = setOf("Be present.", "Less is more."), onAdd = {}, onRemove = {}) } }
        rule.onNodeWithTag("highlight-card").assertIsDisplayed()
        rule.onNodeWithTag("highlight-today").assertIsDisplayed()
    }

    @Test
    fun readingCard_renders() {
        rule.setContent { FocusLauncherTheme { ReadingCard(todayIso = "2026-05-22", last7DaysIso = listOf("2026-05-22"), sessions = emptyList(), onLog = {}, onClear = {}) } }
        rule.onNodeWithTag("reading-card").assertIsDisplayed()
        rule.onNodeWithTag("reading-today").assertIsDisplayed()
    }

    @Test
    fun workoutCard_renders() {
        rule.setContent { FocusLauncherTheme { WorkoutCard(todayIso = "2026-05-22", last7DaysIso = listOf("2026-05-22"), sessions = emptyList(), onLog = { _, _ -> }, onClear = {}) } }
        rule.onNodeWithTag("workout-card").assertIsDisplayed()
        rule.onNodeWithTag("workout-today").assertIsDisplayed()
    }

    @Test
    fun meditationCard_renders() {
        rule.setContent { FocusLauncherTheme { MeditationCard(todayIso = "2026-05-22", last7DaysIso = listOf("2026-05-22"), sessions = emptyList(), onLog = { _, _ -> }, onClear = {}) } }
        rule.onNodeWithTag("meditation-card").assertIsDisplayed()
        rule.onNodeWithTag("meditation-today").assertIsDisplayed()
    }
}
