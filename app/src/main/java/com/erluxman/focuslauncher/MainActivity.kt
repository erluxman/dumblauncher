package com.erluxman.focuslauncher

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.erluxman.focuslauncher.ui.home.HomeScreen
import com.erluxman.focuslauncher.ui.theme.FocusLauncherTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FocusLauncherTheme {
                HomeScreen()
            }
        }
    }
}
