package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.model.ScheduledTask
import com.example.model.UploadStatus
import com.example.ui.AppTab
import com.example.ui.MainViewModel
import com.example.ui.components.CountdownCard
import com.example.ui.components.StringsDictionary
import com.example.ui.components.TaskCard

/**
 * Task List & Scheduler Screen
 * Features a modern, sticky search bar at the very top of the screen to filter
 * uploaded and scheduled tasks by title, description, or status, alongside quick filter chips.
 */
@Composable
fun SchedulerScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val currentLang by viewModel.currentLanguage.collectAsState()
    val isUrdu = currentLang == AppLanguage.URDU
    val tasks by viewModel.filteredTasks.collectAsState()
    val allTasks by viewModel.repository.tasks.collectAsState()
    val isSchedulerRunning by viewModel.isSchedulerRunning.collectAsState()
    val searchQuery by viewModel.taskSearchQuery.collectAsState()
    val currentFilter by viewModel.statusFilter.collectAsState()

    val scheduledCount = allTasks.count { it.status == UploadStatus.SCHEDULED }
    val uploadingCount = allTasks.count { it.status == UploadStatus.UPLOADING }
    val completedCount = allTasks.count { it.status == UploadStatus.COMPLETED }
    val failedCount = allTasks.count { it.status == UploadStatus.FAILED }

    val isFilterActive = searchQuery.isNotBlank() || currentFilter != null

    Box(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            // =========================================================================
            // TOP SEARCH BAR & FILTER SECTION (Pinned at the very top of the task list screen)
            // =========================================================================
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("top_search_bar_surface"),
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 3.dp,
                shadowElevation = 2.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    // Search Bar OutlinedTextField
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { viewModel.setTaskSearchQuery(it) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("search_tasks_input"),
                        placeholder = {
                            Text(
                                text = StringsDictionary.get("search_tasks_placeholder", currentLang),
                                fontSize = 13.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(
                                    onClick = { viewModel.setTaskSearchQuery("") },
                                    modifier = Modifier.testTag("clear_search_btn")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Clear,
                                        contentDescription = "Clear search",
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        },
                        shape = RoundedCornerShape(14.dp),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.7f),
                            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.25f),
                            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.15f)
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Quick Filter Chips Row (All, Scheduled, Uploaded/Completed, Uploading, Failed)
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.testTag("filter_chips_row")
                    ) {
                        item {
                            FilterChip(
                                selected = currentFilter == null,
                                text = "${StringsDictionary.get("filter_all", currentLang)} (${allTasks.size})",
                                onClick = { viewModel.setStatusFilter(null) }
                            )
                        }
                        item {
                            FilterChip(
                                selected = currentFilter == UploadStatus.SCHEDULED,
                                text = "${StringsDictionary.get("filter_scheduled", currentLang)} ($scheduledCount)",
                                onClick = { viewModel.setStatusFilter(UploadStatus.SCHEDULED) }
                            )
                        }
                        item {
                            FilterChip(
                                selected = currentFilter == UploadStatus.COMPLETED,
                                text = "${StringsDictionary.get("filter_uploaded", currentLang)} ($completedCount)",
                                onClick = { viewModel.setStatusFilter(UploadStatus.COMPLETED) }
                            )
                        }
                        if (uploadingCount > 0) {
                            item {
                                FilterChip(
                                    selected = currentFilter == UploadStatus.UPLOADING,
                                    text = "${StringsDictionary.get("filter_uploading", currentLang)} ($uploadingCount)",
                                    onClick = { viewModel.setStatusFilter(UploadStatus.UPLOADING) }
                                )
                            }
                        }
                        if (failedCount > 0) {
                            item {
                                FilterChip(
                                    selected = currentFilter == UploadStatus.FAILED,
                                    text = "${StringsDictionary.get("filter_failed", currentLang)} ($failedCount)",
                                    onClick = { viewModel.setStatusFilter(UploadStatus.FAILED) }
                                )
                            }
                        }
                    }

                    // Active Filter & Match Counter Banner
                    AnimatedVisibility(visible = isFilterActive) {
                        Column {
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .clip(CircleShape)
                                            .background(MaterialTheme.colorScheme.primary)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = if (isUrdu) {
                                            "${allTasks.size} میں سے ${tasks.size} ${StringsDictionary.get("tasks_found_count", currentLang)}"
                                        } else {
                                            "Showing ${tasks.size} of ${allTasks.size} tasks"
                                        },
                                        fontSize = 11.5.sp,
                                        color = MaterialTheme.colorScheme.primary,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }

                                Text(
                                    text = StringsDictionary.get("reset_search", currentLang),
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.error,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .clickable {
                                            viewModel.setTaskSearchQuery("")
                                            viewModel.setStatusFilter(null)
                                        }
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                        .testTag("reset_filter_btn")
                                )
                            }
                        }
                    }
                }
            }

            // =========================================================================
            // SCROLLABLE TASK LIST CONTENT
            // =========================================================================
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("scheduler_task_list"),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Countdown & Engine Card (only show full details if not actively searching, or as helpful overview)
                item {
                    CountdownCard(
                        tasks = allTasks,
                        isSchedulerRunning = isSchedulerRunning,
                        onToggleScheduler = { viewModel.toggleSchedulerEngine(it) },
                        currentLanguage = currentLang
                    )
                }

                // Quick Stats Row
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        StatMetricCard(
                            count = scheduledCount,
                            label = StringsDictionary.get("scheduled_count", currentLang),
                            color = Color(0xFFF59E0B),
                            icon = Icons.Default.Schedule,
                            modifier = Modifier.weight(1f)
                        )
                        StatMetricCard(
                            count = uploadingCount,
                            label = StringsDictionary.get("in_progress", currentLang),
                            color = Color(0xFF3B82F6),
                            icon = Icons.Default.CloudUpload,
                            modifier = Modifier.weight(1f)
                        )
                        StatMetricCard(
                            count = completedCount,
                            label = StringsDictionary.get("completed", currentLang),
                            color = Color(0xFF10B981),
                            icon = Icons.Default.CheckCircle,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                // Section Header
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isUrdu) "ویڈیو ٹاسکس کی فہرست" else "Video Task List & Queue",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${tasks.size} ${if (isUrdu) "ویڈیوز" else "videos"}",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Task Items or Empty/No-Results State
                if (tasks.isEmpty()) {
                    item {
                        if (isFilterActive) {
                            NoSearchResultsCard(
                                searchQuery = searchQuery,
                                isUrdu = isUrdu,
                                currentLang = currentLang,
                                onClearSearch = {
                                    viewModel.setTaskSearchQuery("")
                                    viewModel.setStatusFilter(null)
                                }
                            )
                        } else {
                            EmptyQueueCard(
                                isUrdu = isUrdu,
                                onScheduleClick = { viewModel.selectTab(AppTab.NEW_TASK) }
                            )
                        }
                    }
                } else {
                    items(tasks, key = { it.id }) { task ->
                        TaskCard(
                            task = task,
                            currentLanguage = currentLang,
                            onTriggerUpload = { taskId -> viewModel.triggerImmediateUpload(taskId) },
                            onSendWhatsAppReport = { t -> viewModel.sendWhatsAppReport(context, t) },
                            onDelete = { taskId -> viewModel.deleteScheduledTask(taskId) }
                        )
                    }
                }

                // Bottom padding to avoid FAB overlap
                item {
                    Spacer(modifier = Modifier.height(72.dp))
                }
            }
        }

        // Floating Action Button to Add New Task
        FloatingActionButton(
            onClick = { viewModel.selectTab(AppTab.NEW_TASK) },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp)
                .testTag("fab_new_task"),
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = Color.White
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (isUrdu) "نیا ٹاسک" else "New Task",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun StatMetricCard(
    count: Int,
    label: String,
    color: Color,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.09f))
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "$count",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = color
                )
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = label,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun FilterChip(
    selected: Boolean,
    text: String,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
        contentColor = if (selected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.clickable { onClick() }
    ) {
        Text(
            text = text,
            fontSize = 12.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
            modifier = Modifier.padding(horizontal = 13.dp, vertical = 7.dp)
        )
    }
}

@Composable
fun NoSearchResultsCard(
    searchQuery: String,
    isUrdu: Boolean,
    currentLang: AppLanguage,
    onClearSearch: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("no_search_results_card"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.SearchOff,
                contentDescription = null,
                modifier = Modifier.size(46.dp),
                tint = MaterialTheme.colorScheme.error.copy(alpha = 0.7f)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = StringsDictionary.get("no_matching_tasks", currentLang),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = if (searchQuery.isNotBlank()) {
                    if (isUrdu) {
                        "عنوان '$searchQuery' سے کوئی شیڈولڈ یا اپلوڈ شدہ ویڈیو نہیں ملی۔"
                    } else {
                        "No scheduled or uploaded videos found matching \"$searchQuery\"."
                    }
                } else {
                    StringsDictionary.get("no_matching_tasks_desc", currentLang)
                },
                fontSize = 12.5.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onClearSearch,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(imageVector = Icons.Default.Clear, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = StringsDictionary.get("reset_search", currentLang),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun EmptyQueueCard(
    isUrdu: Boolean,
    onScheduleClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("empty_queue_card"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.VideoLibrary,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = if (isUrdu) "کوئی شیڈول ویڈیو موجود نہیں" else "No Scheduled Videos",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = if (isUrdu) {
                    "آپ 'نیا ٹاسک' پر کلک کر کے یوٹیوب یا فیس بک کیلئے ویڈیو شیڈول کر سکتے ہیں۔"
                } else {
                    "Schedule videos to auto-publish to YouTube & Facebook with automated WhatsApp updates."
                },
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}
