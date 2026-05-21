package com.erluxman.focuslauncher.ui.onboarding

import android.app.admin.DevicePolicyManager
import android.app.role.RoleManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.erluxman.focuslauncher.data.prefs.UserPrefs
import com.erluxman.focuslauncher.receiver.FocusDeviceAdminReceiver
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope

private const val TOTAL_STEPS = 4

@Composable
fun OnboardingScreen(
    prefs: UserPrefs,
    onFinished: () -> Unit
) {
    var step by remember { mutableStateOf(0) }
    var whyHere by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    Surface(
        modifier = Modifier.fillMaxSize().testTag("onboarding"),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
            Spacer(Modifier.height(40.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                if (step > 0) {
                    IconButton(onClick = { step-- }, modifier = Modifier.testTag("onboarding-back")) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
                Text(
                    text = "Step ${step + 1} of $TOTAL_STEPS",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.outline,
                    letterSpacing = 2.sp
                )
            }
            Spacer(Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { (step + 1) / TOTAL_STEPS.toFloat() },
                modifier = Modifier.fillMaxWidth().height(6.dp).clip(CircleShape),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )
            Spacer(Modifier.height(32.dp))

            AnimatedContent(
                targetState = step,
                modifier = Modifier.weight(1f),
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "onboarding-content"
            ) { current ->
                when (current) {
                    0 -> WelcomeStep()
                    1 -> WhyHereStep(value = whyHere, onChange = { whyHere = it })
                    2 -> PermissionsStep()
                    3 -> FinishStep()
                }
            }

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = {
                    if (step == 1) {
                        scope.launch { prefs.setWhyHere(whyHere.trim()) }
                    }
                    if (step < TOTAL_STEPS - 1) {
                        step++
                    } else {
                        scope.launch {
                            prefs.setOnboardingComplete(true)
                            onFinished()
                        }
                    }
                },
                enabled = step != 1 || whyHere.trim().length >= 5,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("onboarding-next")
            ) {
                Text(if (step == TOTAL_STEPS - 1) "Enter FocusLauncher" else "Continue")
            }
        }
    }
}

@Composable
private fun WelcomeStep() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(
            text = "Welcome.",
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "This launcher is built to make leaving your phone easier than picking it up.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = "We will ask for a few things. Each one is optional. Each one is explained.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.outline
        )
    }
}

@Composable
private fun WhyHereStep(value: String, onChange: (String) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(
            text = "Why are you here?",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Write it now, while you're rational. We'll surface this back at your weakest moments.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.outline
        )
        OutlinedTextField(
            value = value,
            onValueChange = onChange,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("why-here-input"),
            minLines = 4,
            placeholder = { Text("I'm tired of losing hours to scroll...") }
        )
        Text(
            text = "${value.trim().length} chars (minimum 5)",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.outline
        )
    }
}

@Composable
private fun PermissionsStep() {
    val context = LocalContext.current
    val devicePolicyManager = context.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
    val componentName = ComponentName(context, FocusDeviceAdminReceiver::class.java)
    val lifecycleOwner = LocalLifecycleOwner.current

    var isAdmin by remember { mutableStateOf(devicePolicyManager.isAdminActive(componentName)) }
    var isDefault by remember { mutableStateOf(isDefaultLauncher(context)) }
    var hasUsage by remember { mutableStateOf(hasUsageStatsPermission(context)) }

    DisposableEffect(lifecycleOwner) {
        val obs = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                isAdmin = devicePolicyManager.isAdminActive(componentName)
                isDefault = isDefaultLauncher(context)
                hasUsage = hasUsageStatsPermission(context)
            }
        }
        lifecycleOwner.lifecycle.addObserver(obs)
        onDispose { lifecycleOwner.lifecycle.removeObserver(obs) }
    }

    val roleLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        isDefault = isDefaultLauncher(context)
    }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = "Permissions",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Grant any of these now or later. You can finish onboarding without them.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.outline
        )

        PermissionRow(
            label = "Default launcher",
            granted = isDefault,
            onGrant = {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    val roleManager = context.getSystemService(RoleManager::class.java)
                    val intent = roleManager?.createRequestRoleIntent(RoleManager.ROLE_HOME)
                    if (intent != null) roleLauncher.launch(intent)
                } else {
                    context.startActivity(Intent(Settings.ACTION_HOME_SETTINGS))
                }
            }
        )

        PermissionRow(
            label = "Usage access",
            granted = hasUsage,
            onGrant = {
                context.startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
            }
        )

        PermissionRow(
            label = "Device admin (anti-uninstall)",
            granted = isAdmin,
            onGrant = {
                val intent = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN).apply {
                    putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName)
                    putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "Required for anti-uninstall protection.")
                }
                context.startActivity(intent)
            }
        )
    }
}

@Composable
private fun PermissionRow(label: String, granted: Boolean, onGrant: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .let {
                        if (granted) it
                        else it
                    },
                contentAlignment = Alignment.Center
            ) {
                if (granted) {
                    Icon(
                        Icons.Default.Check,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
            Text(
                text = label,
                modifier = Modifier.weight(1f).padding(start = 12.dp),
                style = MaterialTheme.typography.bodyLarge
            )
            TextButton(onClick = onGrant, enabled = !granted) {
                Text(if (granted) "Granted" else "Grant")
            }
        }
    }
}

@Composable
private fun FinishStep() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(
            text = "Ready.",
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Your home screen is a tool, not a toy. Whatever you build today, it counts.",
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = "You can revisit transparency, permissions, and your declaration any time from Settings.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.outline
        )
    }
}

private fun isDefaultLauncher(context: Context): Boolean {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val roleManager = context.getSystemService(RoleManager::class.java)
        return roleManager?.isRoleHeld(RoleManager.ROLE_HOME) == true
    }
    val intent = Intent(Intent.ACTION_MAIN).apply { addCategory(Intent.CATEGORY_HOME) }
    val resolveInfo = context.packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY)
    val currentHomePackage = resolveInfo?.activityInfo?.packageName
    if (currentHomePackage == null || currentHomePackage == "android") return false
    return currentHomePackage == context.packageName
}

private fun hasUsageStatsPermission(context: Context): Boolean {
    return try {
        val appOps = context.getSystemService(Context.APP_OPS_SERVICE) as android.app.AppOpsManager
        val mode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            appOps.unsafeCheckOpNoThrow(
                android.app.AppOpsManager.OPSTR_GET_USAGE_STATS,
                android.os.Process.myUid(),
                context.packageName
            )
        } else {
            @Suppress("DEPRECATION")
            appOps.checkOpNoThrow(
                android.app.AppOpsManager.OPSTR_GET_USAGE_STATS,
                android.os.Process.myUid(),
                context.packageName
            )
        }
        mode == android.app.AppOpsManager.MODE_ALLOWED
    } catch (_: Exception) {
        false
    }
}
