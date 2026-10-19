package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.repository.VideoUploadTaskRepository
import com.example.model.PlatformType
import com.example.model.ScheduledTask
import com.example.model.UploadStatus
import com.example.model.VideoPrivacy
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

data class VideoTaskUiState(
    val tasks: List<ScheduledTask> = emptyList(),
    val totalCount: Int = 0,
    val scheduledCount: Int = 0,
    val uploadingCount: Int = 0,
    val completedCount: Int = 0,
    val failedCount: Int = 0,
    val searchQuery: String = "",
    val selectedStatus: UploadStatus? = null,
    val isLoading: Boolean = false,
    val userMessage: String? = null
)

class VideoTaskViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getInstance(application)
    private val repository = VideoUploadTaskRepository(database.videoUploadTaskDao())

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _statusFilter = MutableStateFlow<UploadStatus?>(null)
    val statusFilter: StateFlow<UploadStatus?> = _statusFilter.asStateFlow()

    private val _userMessage = MutableStateFlow<String?>(null)
    val userMessage: StateFlow<String?> = _userMessage.asStateFlow()

    // Form states for creating or editing tasks
    val formTitle = MutableStateFlow("")
    val formDescription = MutableStateFlow("")
    val formVideoPath = MutableStateFlow("storage/emulated/0/DCIM/Camera/video_clip.mp4")
    val formTargetPlatform = MutableStateFlow(PlatformType.BOTH)
    val formScheduledTimeMillis = MutableStateFlow(System.currentTimeMillis() + 15 * 60 * 1000L)
    val formTags = MutableStateFlow("Video, Viral, Trending")
    val formCategory = MutableStateFlow("Entertainment")
    val formPrivacy = MutableStateFlow(VideoPrivacy.PUBLIC)
    val formDuration = MutableStateFlow("04:30")
    val formFileSizeMb = MutableStateFlow(48.5)

    // Reactive UI State combining Room Flow with filter and search queries
    val uiState: StateFlow<VideoTaskUiState> = combine(
        repository.allTasks,
        _searchQuery,
        _statusFilter,
        _userMessage
    ) { allTasks, query, filter, message ->
        val filtered = allTasks.filter { task ->
            val matchesQuery = query.isBlank() ||
                task.title.contains(query, ignoreCase = true) ||
                task.description.contains(query, ignoreCase = true) ||
                task.videoFileName.contains(query, ignoreCase = true) ||
                task.tags.any { it.contains(query, ignoreCase = true) }
            val matchesFilter = filter == null || task.status == filter
            matchesQuery && matchesFilter
        }

        VideoTaskUiState(
            tasks = filtered,
            totalCount = allTasks.size,
            scheduledCount = allTasks.count { it.status == UploadStatus.SCHEDULED },
            uploadingCount = allTasks.count { it.status == UploadStatus.UPLOADING },
            completedCount = allTasks.count { it.status == UploadStatus.COMPLETED },
            failedCount = allTasks.count { it.status == UploadStatus.FAILED },
            searchQuery = query,
            selectedStatus = filter,
            isLoading = false,
            userMessage = message
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = VideoTaskUiState(isLoading = true)
    )

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setStatusFilter(status: UploadStatus?) {
        _statusFilter.value = status
    }

    fun clearUserMessage() {
        _userMessage.value = null
    }

    /**
     * Stores a new scheduled video upload task into the Room Database.
     */
    fun saveScheduledTask(
        title: String = formTitle.value,
        description: String = formDescription.value,
        videoPath: String = formVideoPath.value,
        targetPlatform: PlatformType = formTargetPlatform.value,
        scheduledTimeMillis: Long = formScheduledTimeMillis.value,
        tags: List<String> = formTags.value.split(",").map { it.trim() }.filter { it.isNotEmpty() },
        category: String = formCategory.value,
        privacy: VideoPrivacy = formPrivacy.value,
        duration: String = formDuration.value,
        fileSizeMb: Double = formFileSizeMb.value
    ) {
        val cleanTitle = title.trim().ifEmpty { "New Video Upload Task" }
        val task = ScheduledTask(
            id = "task_${UUID.randomUUID().toString().take(8)}",
            title = cleanTitle,
            description = description.trim(),
            tags = if (tags.isEmpty()) listOf("Upload", "SocialMedia") else tags,
            category = category,
            platform = targetPlatform,
            videoFileName = videoPath.trim().ifEmpty { "video.mp4" },
            videoFileSizeMb = fileSizeMb,
            videoDuration = duration,
            privacy = privacy,
            scheduledTimeMillis = scheduledTimeMillis,
            status = UploadStatus.SCHEDULED,
            progressPercent = 0
        )

        viewModelScope.launch {
            repository.insertTask(task)
            _userMessage.value = "Task successfully scheduled in database!"
            resetForm()
        }
    }

    /**
     * Updates an existing video upload task.
     */
    fun updateTask(task: ScheduledTask) {
        viewModelScope.launch {
            repository.updateTask(task)
        }
    }

    /**
     * Deletes a video upload task from Room database by ID.
     */
    fun deleteTask(taskId: String) {
        viewModelScope.launch {
            repository.deleteTask(taskId)
            _userMessage.value = "Task deleted"
        }
    }

    /**
     * Updates upload progress in the Room database.
     */
    fun updateUploadProgress(taskId: String, status: UploadStatus, progress: Int) {
        viewModelScope.launch {
            repository.updateProgress(taskId, status, progress)
        }
    }

    /**
     * Marks a task as completed with publication URLs and WhatsApp report state.
     */
    fun markTaskCompleted(
        taskId: String,
        youtubeUrl: String? = "https://youtu.be/v_${taskId.take(8)}",
        facebookUrl: String? = "https://facebook.com/watch/?v=fb_${taskId.take(8)}",
        reportSentToWhatsApp: Boolean = true
    ) {
        viewModelScope.launch {
            val now = System.currentTimeMillis()
            repository.markCompleted(
                id = taskId,
                ytUrl = youtubeUrl,
                fbUrl = facebookUrl,
                reportSent = reportSentToWhatsApp,
                reportTimestamp = if (reportSentToWhatsApp) now else null
            )
        }
    }

    private fun resetForm() {
        formTitle.value = ""
        formDescription.value = ""
        formScheduledTimeMillis.value = System.currentTimeMillis() + 15 * 60 * 1000L
    }
}
