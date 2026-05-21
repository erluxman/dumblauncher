package com.erluxman.focuslauncher.ui.home

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
import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.platform.testTag
import com.erluxman.focuslauncher.data.local.AppDatabase
import com.erluxman.focuslauncher.data.local.entity.ProjectEntity
import com.erluxman.focuslauncher.data.local.entity.TodoEntity
import com.erluxman.focuslauncher.data.prefs.UserPrefs
import com.erluxman.focuslauncher.data.repository.ProjectRepository
import com.erluxman.focuslauncher.data.repository.TodoRepository
import com.erluxman.focuslauncher.model.AppInfo
import com.erluxman.focuslauncher.receiver.FocusDeviceAdminReceiver
import com.erluxman.focuslauncher.ui.home.viewmodel.HomeViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HomeScreen(
    prefs: UserPrefs,
    onOpenTransparency: () -> Unit,
    onReplayOnboarding: () -> Unit
) {
    val context = LocalContext.current
    val database = remember { AppDatabase.getDatabase(context) }
    val todoRepository = remember { TodoRepository(database.todoDao()) }
    val projectRepository = remember { ProjectRepository(database.projectDao()) }

    val viewModel: HomeViewModel = viewModel(
        factory = HomeViewModel.provideFactory(
            todoRepository,
            projectRepository,
            context.packageManager,
            prefs,
            context.applicationContext
        )
    )

    val uiState by viewModel.uiState.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val obs = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) viewModel.refreshUsage()
        }
        lifecycleOwner.lifecycle.addObserver(obs)
        onDispose { lifecycleOwner.lifecycle.removeObserver(obs) }
    }

    var searchQuery by remember { mutableStateOf("") }
    var isSearching by remember { mutableStateOf(false) }
    var showSetupDialog by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxSize().testTag("home"),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp)
            ) {
                Spacer(modifier = Modifier.height(64.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (uiState.behaviorIndicatorEnabled) {
                        BehaviorIndicator(
                            state = uiState.behaviorState,
                            progress = uiState.behaviorProgress,
                            screenMinutes = uiState.screenMinutesToday,
                            targetMinutes = uiState.dailyTargetMin,
                            modifier = Modifier.weight(1f)
                        )
                    } else {
                        Spacer(Modifier.weight(1f))
                    }
                    IconButton(
                        onClick = { showSetupDialog = true },
                        modifier = Modifier.testTag("settings-button")
                    ) {
                        Icon(Icons.Default.Settings, contentDescription = "Setup", tint = MaterialTheme.colorScheme.outline)
                    }
                }

                if (uiState.whyHere.isNotBlank()) {
                    Spacer(Modifier.height(16.dp))
                    WhyHereCard(text = uiState.whyHere)
                }

                Spacer(modifier = Modifier.height(32.dp))

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(32.dp),
                    contentPadding = PaddingValues(bottom = 120.dp)
                ) {
                    item { ProjectSection(uiState.projects) }
                    item { TodoSection(uiState.todos, onToggle = viewModel::toggleTodo, onAdd = viewModel::addTodo, onDelete = viewModel::deleteTodo) }
                    item { WidgetSection() }
                }
            }

            // App Dock at the bottom
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
            ) {
                AppDock(
                    dockApps = uiState.dockApps,
                    onAppClick = { viewModel.launchApp(context, it) },
                    onSearchClick = { isSearching = true },
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            AnimatedVisibility(
                visible = isSearching,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                SearchOverlay(
                    query = searchQuery,
                    apps = uiState.apps,
                    onQueryChange = { searchQuery = it },
                    onAppClick = { 
                        viewModel.launchApp(context, it)
                        isSearching = false
                        searchQuery = ""
                    },
                    onDismiss = { 
                        isSearching = false
                        searchQuery = ""
                    }
                )
            }

            if (showSetupDialog) {
                SetupDialog(
                    onDismiss = { showSetupDialog = false },
                    onOpenTransparency = {
                        showSetupDialog = false
                        onOpenTransparency()
                    },
                    onReplayOnboarding = {
                        showSetupDialog = false
                        onReplayOnboarding()
                    }
                )
            }
        }
    }
}

@Composable
private fun WhyHereCard(text: String) {
    Surface(
        modifier = Modifier.fillMaxWidth().testTag("why-here-card"),
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "YOUR DECLARATION",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.outline,
                letterSpacing = 1.5.sp
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun SetupDialog(
    onDismiss: () -> Unit,
    onOpenTransparency: () -> Unit = {},
    onReplayOnboarding: () -> Unit = {}
) {
    val context = LocalContext.current
    val devicePolicyManager = context.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
    val componentName = ComponentName(context, FocusDeviceAdminReceiver::class.java)
    val lifecycleOwner = LocalLifecycleOwner.current
    
    var isAdminActive by remember { mutableStateOf(devicePolicyManager.isAdminActive(componentName)) }
    var isDefault by remember { mutableStateOf(isDefaultLauncher(context)) }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                isAdminActive = devicePolicyManager.isAdminActive(componentName)
                isDefault = isDefaultLauncher(context)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    val roleLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        isDefault = isDefaultLauncher(context)
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("System Setup") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text("Complete these steps to enable anti-uninstall features and lock your experience.")
                
                Button(
                    onClick = {
                        val intent = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN).apply {
                            putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName)
                            putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "Required for anti-uninstall protection.")
                        }
                        context.startActivity(intent)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (isAdminActive) {
                            Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(8.dp))
                        }
                        Text(if (isAdminActive) "Device Admin Active" else "Enable Device Admin")
                    }
                }

                Button(
                    onClick = {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            val roleManager = context.getSystemService(RoleManager::class.java)
                            if (roleManager != null) {
                                val intent = roleManager.createRequestRoleIntent(RoleManager.ROLE_HOME)
                                roleLauncher.launch(intent)
                            }
                        } else {
                            val intent = Intent(Settings.ACTION_HOME_SETTINGS)
                            context.startActivity(intent)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (isDefault) {
                            Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(8.dp))
                        }
                        Text(if (isDefault) "Default Launcher Active" else "Set as Default Launcher")
                    }
                }

                Button(
                    onClick = {
                        context.startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
                    },
                    modifier = Modifier.fillMaxWidth()
                ) { Text("Grant Usage Access") }

                Button(
                    onClick = {
                        context.startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
                    },
                    modifier = Modifier.fillMaxWidth()
                ) { Text("Enable Lobby (Accessibility)") }

                TextButton(
                    onClick = onOpenTransparency,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("open-transparency")
                ) { Text("Transparency / Techniques") }

                TextButton(
                    onClick = onReplayOnboarding,
                    modifier = Modifier.fillMaxWidth()
                ) { Text("Replay Onboarding") }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("Done") }
        }
    )
}

private fun isDefaultLauncher(context: Context): Boolean {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val roleManager = context.getSystemService(RoleManager::class.java)
        return roleManager?.isRoleHeld(RoleManager.ROLE_HOME) == true
    }

    val intent = Intent(Intent.ACTION_MAIN).apply {
        addCategory(Intent.CATEGORY_HOME)
    }
    val resolveInfo = context.packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY)
    val currentHomePackage = resolveInfo?.activityInfo?.packageName
    
    if (currentHomePackage == null || currentHomePackage == "android") return false
    
    return currentHomePackage == context.packageName
}

@Composable
fun BehaviorIndicator(
    state: String,
    progress: Float,
    screenMinutes: Int,
    targetMinutes: Int,
    modifier: Modifier = Modifier
) {
    val stateColor = when(state) {
        "THRIVING" -> MaterialTheme.colorScheme.primary
        "NEUTRAL" -> MaterialTheme.colorScheme.secondary
        "DRIFTING" -> Color(0xFFFBC02D)
        "SINKING" -> Color(0xFFF57C00)
        "DROWNING" -> MaterialTheme.colorScheme.error
        else -> MaterialTheme.colorScheme.outline
    }

    Column(modifier = modifier.testTag("behavior-indicator")) {
        Text(
            text = "CURRENT STATE",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.outline,
            letterSpacing = 2.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = state,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = (-1).sp
                ),
                color = stateColor,
                modifier = Modifier.testTag("behavior-state-text")
            )
            Text(
                text = "${screenMinutes}m / ${targetMinutes}m",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.secondary
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(CircleShape),
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
            color = stateColor
        )
    }
}

@Composable
fun ProjectSection(projects: List<ProjectEntity>) {
    Column {
        SectionHeader("ACTIVE PROJECTS")
        Spacer(modifier = Modifier.height(16.dp))
        if (projects.isEmpty()) {
            ProjectCard("No Active Projects", "Focus on your baseline first.", 0f)
        } else {
            projects.forEach { project ->
                ProjectCard(project.title, project.description, project.progress)
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun ProjectCard(title: String, subtitle: String, progress: Float) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(text = title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(text = subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
            Spacer(modifier = Modifier.height(16.dp))
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxWidth().height(4.dp).clip(CircleShape),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.background.copy(alpha = 0.5f)
            )
        }
    }
}

@Composable
fun TodoSection(
    todos: List<TodoEntity>,
    onToggle: (TodoEntity) -> Unit,
    onAdd: (String) -> Unit,
    onDelete: (TodoEntity) -> Unit
) {
    var newTodoText by remember { mutableStateOf("") }

    Column {
        SectionHeader("TODAY")
        Spacer(modifier = Modifier.height(12.dp))
        
        todos.forEach { todo ->
            TodoItem(todo, onToggle, onDelete)
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = newTodoText,
                onValueChange = { newTodoText = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Add task...") },
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent
                ),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {
                    if (newTodoText.isNotBlank()) {
                        onAdd(newTodoText)
                        newTodoText = ""
                    }
                })
            )
            IconButton(onClick = {
                if (newTodoText.isNotBlank()) {
                    onAdd(newTodoText)
                    newTodoText = ""
                }
            }) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    }
}

@Composable
fun TodoItem(todo: TodoEntity, onToggle: (TodoEntity) -> Unit, onDelete: (TodoEntity) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = todo.isCompleted,
            onCheckedChange = { onToggle(todo) },
            colors = CheckboxDefaults.colors(checkedColor = MaterialTheme.colorScheme.primary)
        )
        Text(
            text = todo.text,
            style = MaterialTheme.typography.bodyLarge,
            color = if (todo.isCompleted) MaterialTheme.colorScheme.outline else MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .weight(1f)
                .clickable { onToggle(todo) }
                .padding(start = 8.dp)
        )
        IconButton(onClick = { onDelete(todo) }, modifier = Modifier.size(24.dp)) {
            Icon(Icons.Default.Close, contentDescription = "Delete", tint = MaterialTheme.colorScheme.outline, modifier = Modifier.size(16.dp))
        }
    }
}

@Composable
fun WidgetSection() {
    Column {
        SectionHeader("WIDGETS")
        Spacer(modifier = Modifier.height(16.dp))
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { ClockWidget() }
            item { QuoteWidget() }
            item { ProgressWidget() }
            item { EmptyWidgetPlaceholder() }
            item { EmptyWidgetPlaceholder() }
        }
    }
}

@Composable
fun ClockWidget() {
    var currentTime by remember { mutableStateOf(System.currentTimeMillis()) }
    LaunchedEffect(Unit) {
        while(true) {
            currentTime = System.currentTimeMillis()
            kotlinx.coroutines.delay(1000)
        }
    }
    
    val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
    val dateSdf = SimpleDateFormat("EEE, MMM d", Locale.getDefault())

    WidgetContainer {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = sdf.format(Date(currentTime)), style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text(text = dateSdf.format(Date(currentTime)), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
        }
    }
}

@Composable
fun QuoteWidget() {
    WidgetContainer {
        Column {
            Text(
                text = "\"Discipline is the bridge between goals and accomplishment.\"",
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Light
            )
            Text(
                text = "— Jim Rohn",
                style = androidx.compose.ui.text.TextStyle(fontSize = 10.sp),
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.align(Alignment.End)
            )
        }
    }
}

@Composable
fun ProgressWidget() {
    WidgetContainer {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(
                progress = { 0.7f },
                modifier = Modifier.size(40.dp),
                strokeWidth = 4.dp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Focus", style = MaterialTheme.typography.labelSmall)
        }
    }
}

@Composable
fun EmptyWidgetPlaceholder() {
    Box(
        modifier = Modifier
            .size(width = 140.dp, height = 100.dp)
            .background(
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                RoundedCornerShape(16.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(Icons.Default.Add, contentDescription = null, tint = MaterialTheme.colorScheme.outline)
    }
}

@Composable
fun WidgetContainer(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .size(width = 140.dp, height = 100.dp)
            .background(
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                RoundedCornerShape(16.dp)
            )
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

@Composable
fun SectionHeader(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.outline,
        letterSpacing = 1.5.sp
    )
}

@Composable
fun AppDock(dockApps: List<AppInfo>, onAppClick: (String) -> Unit, onSearchClick: () -> Unit, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        tonalElevation = 4.dp
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            dockApps.forEach { app ->
                DockIcon(app, onClick = { onAppClick(app.packageName) })
            }
            
            repeat(5 - dockApps.size) {
                DockPlaceholder()
            }

            VerticalDivider(modifier = Modifier.height(32.dp).padding(horizontal = 4.dp))

            IconButton(
                onClick = onSearchClick,
                modifier = Modifier
                    .size(48.dp)
                    .background(MaterialTheme.colorScheme.primary, CircleShape)
            ) {
                Icon(Icons.Default.Search, contentDescription = "Search", tint = MaterialTheme.colorScheme.onPrimary)
            }
        }
    }
}

@Composable
fun DockIcon(app: AppInfo, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surface)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        app.icon?.let {
            Image(
                bitmap = it.toBitmap().asImageBitmap(),
                contentDescription = app.label,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

@Composable
fun DockPlaceholder() {
    Box(
        modifier = Modifier
            .size(48.dp)
            .background(Color.Gray.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
    )
}

@Composable
fun SearchOverlay(
    query: String,
    apps: List<AppInfo>,
    onQueryChange: (String) -> Unit,
    onAppClick: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val filteredApps = remember(query, apps) {
        if (query.isBlank()) emptyList()
        else apps.filter { it.label.contains(query, ignoreCase = true) }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background.copy(alpha = 0.98f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            TextField(
                value = query,
                onValueChange = onQueryChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Type app name...") },
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent
                ),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = {
                    if (filteredApps.isNotEmpty()) {
                        onAppClick(filteredApps.first().packageName)
                    }
                })
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(filteredApps) { app ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onAppClick(app.packageName) }
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        app.icon?.let {
                            Image(
                                bitmap = it.toBitmap().asImageBitmap(),
                                contentDescription = null,
                                modifier = Modifier.size(40.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(text = app.label, style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }

            Button(onClick = onDismiss, modifier = Modifier.align(Alignment.End)) {
                Text("Close")
            }
        }
    }
}
