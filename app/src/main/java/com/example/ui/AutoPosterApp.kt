package com.example.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.outlined.AddCircleOutline
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.Link
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.ui.components.GalaxySidebarContainer
import com.example.ui.components.StringsDictionary
import com.example.ui.screens.AboutScreen
import com.example.ui.screens.AccountsScreen
import com.example.ui.screens.AnalyticsScreen
import com.example.ui.screens.NewTaskScreen
import com.example.ui.screens.SchedulerScreen
import com.example.ui.screens.UploadHistoryScreen
import com.example.ui.screens.WhatsAppReportScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AutoPosterApp(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val selectedTab by viewModel.selectedTab.collectAsState()
    val currentLang by viewModel.currentLanguage.collectAsState()
    val isSchedulerRunning by viewModel.isSchedulerRunning.collectAsState()
    val isUrdu = currentLang == AppLanguage.URDU
    var isGalaxySidebarOpen by remember { mutableStateOf(false) }

    // Back handling: First close Galaxy Sidebar if open, then return to Queue if on sub-tab
    BackHandler(enabled = isGalaxySidebarOpen) {
        isGalaxySidebarOpen = false
    }

    BackHandler(enabled = !isGalaxySidebarOpen && selectedTab != AppTab.QUEUE) {
        viewModel.selectTab(AppTab.QUEUE)
    }

    // Wrap the entire app with Galaxy Sidebar Container (includes floating Galaxy Edge Handle & sliding drawer)
    GalaxySidebarContainer(
        viewModel = viewModel,
        isOpen = isGalaxySidebarOpen,
        onOpenChange = { isGalaxySidebarOpen = it },
        modifier = modifier
    ) {
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val isWideScreen = maxWidth >= 600.dp

            Scaffold(
                topBar = {
                    CenterAlignedTopAppBar(
                        navigationIcon = {
                            // Galaxy Sidebar Menu Button
                            IconButton(
                                onClick = { isGalaxySidebarOpen = true },
                                modifier = Modifier
                                    .padding(start = 8.dp)
                                    .size(40.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(
                                        Brush.linearGradient(
                                            listOf(Color(0xFF6366F1), Color(0xFFA855F7))
                                        )
                                    )
                                    .testTag("galaxy_menu_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Menu,
                                    contentDescription = "Galaxy Sidebar",
                                    tint = Color.White,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        },
                        title = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(34.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color(0xFFFF0033)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.PlayArrow,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = StringsDictionary.get("app_title", currentLang),
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = if (isSchedulerRunning) {
                                            if (isUrdu) "🟢 آٹو اپلوڈر فعال ہے" else "🟢 Auto-Upload Active"
                                        } else {
                                            if (isUrdu) "⚪ موقوف ہے" else "⚪ Paused"
                                        },
                                        fontSize = 11.sp,
                                        color = if (isSchedulerRunning) Color(0xFF10B981) else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        },
                        actions = {
                            // Prominent Bilingual Switcher (اردو / English)
                            Surface(
                                shape = RoundedCornerShape(24.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier
                                    .padding(end = 8.dp)
                                    .clip(RoundedCornerShape(24.dp))
                                    .testTag("language_toggle_btn")
                            ) {
                                Row(
                                    modifier = Modifier.padding(3.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // Urdu Option
                                    Surface(
                                        shape = RoundedCornerShape(20.dp),
                                        color = if (isUrdu) MaterialTheme.colorScheme.primary else Color.Transparent,
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(20.dp))
                                            .clickable { if (!isUrdu) viewModel.toggleLanguage() }
                                    ) {
                                        Text(
                                            text = "اردو",
                                            fontSize = 12.sp,
                                            fontWeight = if (isUrdu) FontWeight.Bold else FontWeight.Medium,
                                            color = if (isUrdu) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                                            modifier = Modifier.padding(horizontal = 9.dp, vertical = 5.dp)
                                        )
                                    }

                                    // English Option
                                    Surface(
                                        shape = RoundedCornerShape(20.dp),
                                        color = if (!isUrdu) MaterialTheme.colorScheme.primary else Color.Transparent,
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(20.dp))
                                            .clickable { if (isUrdu) viewModel.toggleLanguage() }
                                    ) {
                                        Text(
                                            text = "English",
                                            fontSize = 11.5.sp,
                                            fontWeight = if (!isUrdu) FontWeight.Bold else FontWeight.Medium,
                                            color = if (!isUrdu) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                                            modifier = Modifier.padding(horizontal = 9.dp, vertical = 5.dp)
                                        )
                                    }
                                }
                            }
                        },
                        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        )
                    )
                },
                bottomBar = {
                    if (!isWideScreen) {
                        NavigationBar(
                            modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars),
                            containerColor = MaterialTheme.colorScheme.surface,
                            tonalElevation = 8.dp
                        ) {
                            NavigationBarItem(
                                selected = selectedTab == AppTab.QUEUE,
                                onClick = { viewModel.selectTab(AppTab.QUEUE) },
                                icon = {
                                    Icon(
                                        if (selectedTab == AppTab.QUEUE) Icons.Filled.Schedule else Icons.Outlined.Schedule,
                                        contentDescription = "Queue"
                                    )
                                },
                                label = { Text(StringsDictionary.get("tab_queue", currentLang), fontSize = 9.5.sp) },
                                modifier = Modifier.testTag("nav_tab_queue")
                            )
                            NavigationBarItem(
                                selected = selectedTab == AppTab.HISTORY,
                                onClick = { viewModel.selectTab(AppTab.HISTORY) },
                                icon = {
                                    Icon(
                                        if (selectedTab == AppTab.HISTORY) Icons.Filled.History else Icons.Outlined.History,
                                        contentDescription = "History"
                                    )
                                },
                                label = { Text(StringsDictionary.get("tab_history", currentLang), fontSize = 9.5.sp) },
                                modifier = Modifier.testTag("nav_tab_history")
                            )
                            NavigationBarItem(
                                selected = selectedTab == AppTab.NEW_TASK,
                                onClick = { viewModel.selectTab(AppTab.NEW_TASK) },
                                icon = {
                                    Icon(
                                        if (selectedTab == AppTab.NEW_TASK) Icons.Filled.AddCircle else Icons.Outlined.AddCircleOutline,
                                        contentDescription = "New Task"
                                    )
                                },
                                label = { Text(StringsDictionary.get("tab_new_task", currentLang), fontSize = 9.5.sp) },
                                modifier = Modifier.testTag("nav_tab_new_task")
                            )
                            NavigationBarItem(
                                selected = selectedTab == AppTab.ACCOUNTS,
                                onClick = { viewModel.selectTab(AppTab.ACCOUNTS) },
                                icon = {
                                    Icon(
                                        if (selectedTab == AppTab.ACCOUNTS) Icons.Filled.Link else Icons.Outlined.Link,
                                        contentDescription = "Accounts"
                                    )
                                },
                                label = { Text(StringsDictionary.get("tab_accounts", currentLang), fontSize = 9.5.sp) },
                                modifier = Modifier.testTag("nav_tab_accounts")
                            )
                            NavigationBarItem(
                                selected = selectedTab == AppTab.WHATSAPP,
                                onClick = { viewModel.selectTab(AppTab.WHATSAPP) },
                                icon = {
                                    Icon(
                                        if (selectedTab == AppTab.WHATSAPP) Icons.Filled.Chat else Icons.Outlined.Chat,
                                        contentDescription = "WhatsApp",
                                        tint = if (selectedTab == AppTab.WHATSAPP) Color(0xFF25D366) else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                },
                                label = { Text(StringsDictionary.get("tab_whatsapp", currentLang), fontSize = 9.5.sp) },
                                modifier = Modifier.testTag("nav_tab_whatsapp")
                            )
                            NavigationBarItem(
                                selected = selectedTab == AppTab.ANALYTICS,
                                onClick = { viewModel.selectTab(AppTab.ANALYTICS) },
                                icon = {
                                    Icon(
                                        if (selectedTab == AppTab.ANALYTICS) Icons.Filled.Lightbulb else Icons.Outlined.Lightbulb,
                                        contentDescription = "Ideas"
                                    )
                                },
                                label = { Text(StringsDictionary.get("tab_analytics", currentLang), fontSize = 9.5.sp) },
                                modifier = Modifier.testTag("nav_tab_analytics")
                            )
                            NavigationBarItem(
                                selected = selectedTab == AppTab.ABOUT,
                                onClick = { viewModel.selectTab(AppTab.ABOUT) },
                                icon = {
                                    Icon(
                                        if (selectedTab == AppTab.ABOUT) Icons.Filled.Info else Icons.Outlined.Info,
                                        contentDescription = "About",
                                        tint = if (selectedTab == AppTab.ABOUT) Color(0xFF38BDF8) else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                },
                                label = { Text(if (isUrdu) "ڈویلپر" else "About", fontSize = 9.5.sp) },
                                modifier = Modifier.testTag("nav_tab_about")
                            )
                        }
                    }
                }
            ) { innerPadding ->
                Row(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
                    // Wide Screen Navigation Rail (System / Tablet layout)
                    if (isWideScreen) {
                        NavigationRail(
                            modifier = Modifier.fillMaxHeight(),
                            containerColor = MaterialTheme.colorScheme.surface
                        ) {
                            Spacer(modifier = Modifier.size(16.dp))
                            NavigationRailItem(
                                selected = selectedTab == AppTab.QUEUE,
                                onClick = { viewModel.selectTab(AppTab.QUEUE) },
                                icon = { Icon(Icons.Default.Schedule, contentDescription = "Queue") },
                                label = { Text(StringsDictionary.get("tab_queue", currentLang), fontSize = 11.sp) }
                            )
                            NavigationRailItem(
                                selected = selectedTab == AppTab.HISTORY,
                                onClick = { viewModel.selectTab(AppTab.HISTORY) },
                                icon = { Icon(Icons.Default.History, contentDescription = "History") },
                                label = { Text(StringsDictionary.get("tab_history", currentLang), fontSize = 11.sp) }
                            )
                            NavigationRailItem(
                                selected = selectedTab == AppTab.NEW_TASK,
                                onClick = { viewModel.selectTab(AppTab.NEW_TASK) },
                                icon = { Icon(Icons.Default.AddCircle, contentDescription = "New") },
                                label = { Text(StringsDictionary.get("tab_new_task", currentLang), fontSize = 11.sp) }
                            )
                            NavigationRailItem(
                                selected = selectedTab == AppTab.ACCOUNTS,
                                onClick = { viewModel.selectTab(AppTab.ACCOUNTS) },
                                icon = { Icon(Icons.Default.Link, contentDescription = "Accounts") },
                                label = { Text(StringsDictionary.get("tab_accounts", currentLang), fontSize = 11.sp) }
                            )
                            NavigationRailItem(
                                selected = selectedTab == AppTab.WHATSAPP,
                                onClick = { viewModel.selectTab(AppTab.WHATSAPP) },
                                icon = { Icon(Icons.Default.Chat, contentDescription = "WhatsApp", tint = Color(0xFF25D366)) },
                                label = { Text(StringsDictionary.get("tab_whatsapp", currentLang), fontSize = 11.sp) }
                            )
                            NavigationRailItem(
                                selected = selectedTab == AppTab.ANALYTICS,
                                onClick = { viewModel.selectTab(AppTab.ANALYTICS) },
                                icon = { Icon(Icons.Default.Lightbulb, contentDescription = "Ideas") },
                                label = { Text(StringsDictionary.get("tab_analytics", currentLang), fontSize = 11.sp) }
                            )
                            NavigationRailItem(
                                selected = selectedTab == AppTab.ABOUT,
                                onClick = { viewModel.selectTab(AppTab.ABOUT) },
                                icon = { Icon(Icons.Default.Info, contentDescription = "About", tint = Color(0xFF38BDF8)) },
                                label = { Text(if (isUrdu) "ڈویلپر" else "About", fontSize = 11.sp) }
                            )
                        }
                    }

                    // Main Content View
                    Box(modifier = Modifier.fillMaxSize()) {
                        when (selectedTab) {
                            AppTab.QUEUE -> SchedulerScreen(viewModel = viewModel)
                            AppTab.HISTORY -> UploadHistoryScreen(viewModel = viewModel)
                            AppTab.NEW_TASK -> NewTaskScreen(viewModel = viewModel)
                            AppTab.ACCOUNTS -> AccountsScreen(viewModel = viewModel)
                            AppTab.WHATSAPP -> WhatsAppReportScreen(viewModel = viewModel)
                            AppTab.ANALYTICS -> AnalyticsScreen(viewModel = viewModel)
                            AppTab.ABOUT -> AboutScreen(viewModel = viewModel)
                        }
                    }
                }
            }
        }
    }
}
