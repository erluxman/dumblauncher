package com.erluxman.focuslauncher.ui.profile

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
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.erluxman.focuslauncher.backend.BackendRepository
import com.erluxman.focuslauncher.data.local.AppDatabase
import com.erluxman.focuslauncher.data.prefs.UserPrefs
import com.erluxman.focuslauncher.data.prefs.antiBio
import com.erluxman.focuslauncher.data.repository.ProjectRepository
import com.erluxman.focuslauncher.service.tracks.GraduateState
import com.erluxman.focuslauncher.ui.home.minimal.MinimalTheme
import kotlinx.coroutines.flow.first

/**
 * SOCIAL-009 — Public Builder Profile preview. Read-only render of the
 * data that would appear on the user's public profile URL once the web
 * app (PLATFORM-002) ships. Includes SOCIAL-011 verification badge
 * eligibility check.
 *
 * Behind FlagKey.BUILDER_PROFILE.
 */
@Composable
fun BuilderProfileScreen(
    prefs: UserPrefs,
    backend: BackendRepository,
    onBack: () -> Unit,
) {
    val context = LocalContext.current
    val uid by backend.uid.collectAsState(initial = null)
    val antiBio by prefs.antiBio.collectAsState(initial = "")
    val streakDays by prefs.streakDays.collectAsState(initial = 0)
    val streakBest by prefs.streakBest.collectAsState(initial = 0)
    val trackLevel by prefs.trackLevel.collectAsState(initial = 1)
    val onboardingMs by prefs.onboardingCompletedAt.collectAsState(initial = 0L)
    val grad = remember(trackLevel, onboardingMs) {
        GraduateState.compute(trackLevel, onboardingMs)
    }

    val shippedProjects by produceState(initialValue = 0, key1 = Unit) {
        value = runCatching {
            val repo = ProjectRepository(AppDatabase.getDatabase(context).projectDao())
            repo.activeProjects.first().count { it.progress >= 1f }
        }.getOrDefault(0)
    }

    // SOCIAL-011 spec: 365 days + 3 projects shipped + 3 vouches.
    // Vouches aren't tracked yet; surface eligibility-so-far transparently.
    val eligibleByDays = grad.daysOnboarded >= 365
    val eligibleByProjects = shippedProjects >= 3
    val verifiedEligible = eligibleByDays && eligibleByProjects  // + vouches when wired

    Surface(
        modifier = Modifier.fillMaxSize().testTag("profile"),
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
                    .testTag("profile-back")
                    .clickable { onBack() }
                    .padding(8.dp),
            )

            Spacer(Modifier.height(24.dp))
            Text("builder profile.", style = displayStyle, color = MinimalTheme.fg)
            Spacer(Modifier.height(8.dp))
            Text(
                "what your public url would show. read-only preview.",
                style = bodyStyle.copy(fontSize = 14.sp),
                color = MinimalTheme.outline,
            )

            Spacer(Modifier.height(32.dp))
            Row("uid", uid ?: "—", "profile-uid")
            Row("track level", "$trackLevel", "profile-level")
            Row("today's streak", "$streakDays days", "profile-streak")
            Row("best streak", "$streakBest days", "profile-best")
            Row("days onboarded", "${grad.daysOnboarded}", "profile-days")
            Row("projects shipped", "$shippedProjects", "profile-shipped")

            Spacer(Modifier.height(24.dp))
            Text("anti-bio", style = captionStyle, color = MinimalTheme.outline)
            Spacer(Modifier.height(4.dp))
            Text(
                if (antiBio.isBlank()) "(none set yet)" else antiBio,
                style = bodyStyle,
                color = if (antiBio.isBlank()) MinimalTheme.outline else MinimalTheme.fg,
                modifier = Modifier.testTag("profile-anti-bio"),
            )

            Spacer(Modifier.height(32.dp))
            Text("verification (SOCIAL-011)", style = captionStyle, color = MinimalTheme.outline)
            Spacer(Modifier.height(8.dp))
            Check("365 days onboarded", eligibleByDays, "profile-check-days",
                detail = "${grad.daysOnboarded}/365")
            Check("3 projects shipped", eligibleByProjects, "profile-check-projects",
                detail = "$shippedProjects/3")
            Check("3 vouches", false, "profile-check-vouches",
                detail = "vouch system pending — needs backend")

            Spacer(Modifier.height(24.dp))
            Text(
                text = if (verifiedEligible) "all data-prereqs met. waiting on vouches."
                else "more to do before the badge unlocks.",
                style = bodyStyle,
                color = MinimalTheme.accent,
                modifier = Modifier.testTag("profile-verdict"),
            )
            Spacer(Modifier.height(48.dp))
        }
    }
}

@Composable
private fun Row(label: String, value: String, tag: String) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Text(label, style = captionStyle, color = MinimalTheme.outline)
        Spacer(Modifier.height(2.dp))
        Text(value, style = bodyStyle, color = MinimalTheme.fg, modifier = Modifier.testTag(tag))
    }
}

@Composable
private fun Check(label: String, ok: Boolean, tag: String, detail: String) {
    androidx.compose.foundation.layout.Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).testTag(tag),
    ) {
        Text(
            if (ok) "● " else "○ ",
            style = bodyStyle,
            color = if (ok) MinimalTheme.accent else MinimalTheme.outline,
        )
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(label, style = bodyStyle, color = if (ok) MinimalTheme.fg else MinimalTheme.outline)
            Text(detail, style = captionStyle, color = MinimalTheme.outline.copy(alpha = 0.6f))
        }
    }
}

private val displayStyle = TextStyle(fontSize = 32.sp, fontWeight = FontWeight.Normal, letterSpacing = 0.sp)
private val bodyStyle = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Normal, letterSpacing = 0.sp)
private val captionStyle = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Normal)
