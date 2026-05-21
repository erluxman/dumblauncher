package com.erluxman.focuslauncher

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
import com.erluxman.focuslauncher.ui.transparency.TransparencyScreen
import com.erluxman.focuslauncher.ui.uninstall.UninstallScreen
import com.erluxman.focuslauncher.ui.vip.VipContactsScreen

sealed class Screen {
    data object Home : Screen()
    data object Onboarding : Screen()
    data object Transparency : Screen()
    data object Uninstall : Screen()
    data object Vip : Screen()
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FocusLauncherTheme {
                AppRoot()
            }
        }
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
        else -> {
            HomeScreen(
                prefs = prefs,
                onOpenTransparency = { current = Screen.Transparency },
                onReplayOnboarding = { current = Screen.Onboarding },
                onOpenUninstall = { current = Screen.Uninstall },
                onOpenVip = { current = Screen.Vip }
            )
        }
    }
}
