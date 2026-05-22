package com.erluxman.focuslauncher
import com.erluxman.focuslauncher.data.prefs.caffeineDoses
import com.erluxman.focuslauncher.data.prefs.logCaffeine
import com.erluxman.focuslauncher.data.prefs.clearCaffeineLog
import com.erluxman.focuslauncher.data.prefs.drinkLog
import com.erluxman.focuslauncher.data.prefs.logDrink
import com.erluxman.focuslauncher.data.prefs.clearDrinkLog
import com.erluxman.focuslauncher.data.prefs.meditationLog
import com.erluxman.focuslauncher.data.prefs.logMeditation
import com.erluxman.focuslauncher.data.prefs.clearMeditationLog
import com.erluxman.focuslauncher.data.prefs.ideaParking
import com.erluxman.focuslauncher.data.prefs.addParkedIdea
import com.erluxman.focuslauncher.data.prefs.removeParkedIdea
import com.erluxman.focuslauncher.data.prefs.clearParkedIdeas
import com.erluxman.focuslauncher.data.prefs.readingLog
import com.erluxman.focuslauncher.data.prefs.logReading
import com.erluxman.focuslauncher.data.prefs.clearReadingLog
import com.erluxman.focuslauncher.data.prefs.workoutLog
import com.erluxman.focuslauncher.data.prefs.logWorkout
import com.erluxman.focuslauncher.data.prefs.clearWorkoutLog
import com.erluxman.focuslauncher.data.prefs.commitLog
import com.erluxman.focuslauncher.data.prefs.addCommits
import com.erluxman.focuslauncher.data.prefs.clearCommitLog
import com.erluxman.focuslauncher.data.prefs.prWall
import com.erluxman.focuslauncher.data.prefs.addPersonalRecord
import com.erluxman.focuslauncher.data.prefs.removePersonalRecord
import com.erluxman.focuslauncher.data.prefs.travelAtlas
import com.erluxman.focuslauncher.data.prefs.addTravel
import com.erluxman.focuslauncher.data.prefs.removeTravel
import com.erluxman.focuslauncher.data.prefs.subscriptions
import com.erluxman.focuslauncher.data.prefs.addSubscription
import com.erluxman.focuslauncher.data.prefs.removeSubscription
import com.erluxman.focuslauncher.data.prefs.moneyIncome
import com.erluxman.focuslauncher.data.prefs.moneyExpense
import com.erluxman.focuslauncher.data.prefs.moneyAssets
import com.erluxman.focuslauncher.data.prefs.moneyLiabilities
import com.erluxman.focuslauncher.data.prefs.setMoneyIncome
import com.erluxman.focuslauncher.data.prefs.setMoneyExpense
import com.erluxman.focuslauncher.data.prefs.setMoneyAssets
import com.erluxman.focuslauncher.data.prefs.setMoneyLiabilities
import com.erluxman.focuslauncher.data.prefs.mortalityWidgetsOptIn
import com.erluxman.focuslauncher.data.prefs.setMortalityWidgetsOptIn
import com.erluxman.focuslauncher.data.prefs.sleepCutoffHour
import com.erluxman.focuslauncher.data.prefs.sleepWakeHour
import com.erluxman.focuslauncher.data.prefs.setSleepCutoffHour
import com.erluxman.focuslauncher.data.prefs.setSleepWakeHour
import com.erluxman.focuslauncher.data.prefs.antiBio
import com.erluxman.focuslauncher.data.prefs.setAntiBio
import com.erluxman.focuslauncher.service.launcher.FaceDownDetector

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.erluxman.focuslauncher.data.prefs.UserPrefs
import com.erluxman.focuslauncher.ui.home.HomeScreen
import com.erluxman.focuslauncher.ui.onboarding.OnboardingScreen
import com.erluxman.focuslauncher.ui.theme.FocusLauncherTheme
import com.erluxman.focuslauncher.ui.focus.FocusTimerScreen
import com.erluxman.focuslauncher.ui.mantra.MantraScreen
import com.erluxman.focuslauncher.ui.transparency.TransparencyScreen
import com.erluxman.focuslauncher.ui.uninstall.UninstallScreen
import com.erluxman.focuslauncher.ui.vip.VipContactsScreen
import com.erluxman.focuslauncher.ui.boredom.BoredomScreen

sealed class Screen {
    data object Home : Screen()
    data object Onboarding : Screen()
    data object Transparency : Screen()
    data object Uninstall : Screen()
    data object Vip : Screen()
    data object Focus : Screen()
    data object Mantra : Screen()
    data object Boredom : Screen()
    data object FutureSelfVideo : Screen()
    data object Breath : Screen()
}

class MainActivity : ComponentActivity() {

    private var faceDownDetector: com.erluxman.focuslauncher.service.launcher.FaceDownDetector? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        com.erluxman.focuslauncher.service.launcher.CheckInScheduler.scheduleAll(applicationContext)
        faceDownDetector = com.erluxman.focuslauncher.service.launcher.FaceDownDetector(
            applicationContext
        ) {
            val line = com.erluxman.focuslauncher.service.sad.Applause
                .maybeLine(elapsedMs = 0L, seed = System.currentTimeMillis())
                ?: "phone down. that's a win."
            android.widget.Toast.makeText(applicationContext, line, android.widget.Toast.LENGTH_SHORT).show()
        }
        setContent {
            FocusLauncherTheme {
                AppRoot()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        faceDownDetector?.start()
    }

    override fun onPause() {
        faceDownDetector?.stop()
        super.onPause()
    }
}

@Composable
private fun AppRoot() {
    val context = LocalContext.current
    val prefs = remember { UserPrefs(context.applicationContext) }
    val onboardingComplete by prefs.onboardingComplete.collectAsState(initial = null)

    var current: Screen by remember { mutableStateOf(Screen.Home) }

    when {
        onboardingComplete == null -> Unit // First composition; render nothing for a moment.
        onboardingComplete == false || current == Screen.Onboarding -> {
            OnboardingScreen(prefs = prefs, onFinished = { current = Screen.Home })
        }
        current == Screen.Transparency -> {
            TransparencyScreen(prefs = prefs, onBack = { current = Screen.Home })
        }
        current == Screen.Uninstall -> {
            UninstallScreen(prefs = prefs, onBack = { current = Screen.Home })
        }
        current == Screen.Vip -> {
            VipContactsScreen(prefs = prefs, onBack = { current = Screen.Home })
        }
        current == Screen.Focus -> {
            FocusTimerScreen(prefs = prefs, onBack = { current = Screen.Home })
        }
        current == Screen.Mantra -> {
            MantraScreen(prefs = prefs, onBack = { current = Screen.Home })
        }
        current == Screen.Boredom -> {
            BoredomScreen(onBack = { current = Screen.Home })
        }
        current == Screen.FutureSelfVideo -> {
            com.erluxman.focuslauncher.ui.futureself.FutureSelfVideoScreen(
                onBack = { current = Screen.Home }
            )
        }
        current == Screen.Breath -> {
            com.erluxman.focuslauncher.ui.breath.BreathScreen(
                onBack = { current = Screen.Home }
            )
        }
        else -> {
            HomeScreen(
                prefs = prefs,
                onOpenTransparency = { current = Screen.Transparency },
                onReplayOnboarding = { current = Screen.Onboarding },
                onOpenUninstall = { current = Screen.Uninstall },
                onOpenVip = { current = Screen.Vip },
                onOpenFocus = { current = Screen.Focus },
                onOpenMantra = { current = Screen.Mantra },
                onOpenBoredom = { current = Screen.Boredom },
                onOpenFutureSelfVideo = { current = Screen.FutureSelfVideo },
                onOpenBreath = { current = Screen.Breath }
            )
        }
    }
}
