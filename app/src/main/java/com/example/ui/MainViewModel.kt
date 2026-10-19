package com.example.ui

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AutoPosterRepository
import com.example.data.local.AppDatabase
import com.example.data.WhatsAppReportGenerator
import com.example.model.AppLanguage
import com.example.model.FacebookPageAccount
import com.example.model.PlatformType
import com.example.model.ScheduledTask
import com.example.model.UploadStatus
import com.example.model.VideoPrivacy
import com.example.model.WhatsAppReportLog
import com.example.model.WhatsAppSettings
import com.example.model.YouTubeAccount
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import java.util.UUID

enum class AppTab {
    QUEUE,
    HISTORY,
    NEW_TASK,
    ACCOUNTS,
    WHATSAPP,
    ANALYTICS,
    ABOUT
}

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getInstance(application)
    val repository = AutoPosterRepository(viewModelScope, database.videoUploadTaskDao())

    val currentLanguage: StateFlow<AppLanguage> = repository.currentLanguage
    val isSchedulerRunning: StateFlow<Boolean> = repository.isSchedulerRunning
    val youtubeAccount: StateFlow<YouTubeAccount> = repository.youtubeAccount
    val facebookAccount: StateFlow<FacebookPageAccount> = repository.facebookAccount
    val whatsappSettings: StateFlow<WhatsAppSettings> = repository.whatsappSettings
    val reportLogs: StateFlow<List<WhatsAppReportLog>> = repository.reportLogs
    val allTasks: StateFlow<List<ScheduledTask>> = repository.tasks

    private val _selectedTab = MutableStateFlow(AppTab.QUEUE)
    val selectedTab: StateFlow<AppTab> = _selectedTab.asStateFlow()

    private val _taskSearchQuery = MutableStateFlow("")
    val taskSearchQuery: StateFlow<String> = _taskSearchQuery.asStateFlow()

    private val _statusFilter = MutableStateFlow<UploadStatus?>(null)
    val statusFilter: StateFlow<UploadStatus?> = _statusFilter.asStateFlow()

    // Filtered tasks flow
    val filteredTasks: StateFlow<List<ScheduledTask>> = combine(
        repository.tasks,
        _taskSearchQuery,
        _statusFilter
    ) { tasks, query, filter ->
        val cleanQuery = query.trim()
        tasks.filter { task ->
            val matchesQuery = cleanQuery.isBlank() || 
                task.title.contains(cleanQuery, ignoreCase = true) ||
                task.description.contains(cleanQuery, ignoreCase = true) ||
                task.videoFileName.contains(cleanQuery, ignoreCase = true) ||
                task.tags.any { it.contains(cleanQuery, ignoreCase = true) } ||
                task.status.label.contains(cleanQuery, ignoreCase = true) ||
                task.status.labelUrdu.contains(cleanQuery, ignoreCase = true)
            val matchesStatus = filter == null || task.status == filter
            matchesQuery && matchesStatus
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // New Task Form State
    var newTaskTitle = MutableStateFlow("")
    var newTaskDescription = MutableStateFlow("")
    var newTaskTags = MutableStateFlow("Video, Viral, Trending, Urdu")
    var newTaskCategory = MutableStateFlow("Entertainment")
    var newTaskPlatform = MutableStateFlow(PlatformType.BOTH)
    var newTaskPrivacy = MutableStateFlow(VideoPrivacy.PUBLIC)
    var newTaskVideoName = MutableStateFlow("my_edited_video_1080p.mp4")
    var newTaskDuration = MutableStateFlow("04:30")
    var newTaskFileSizeMb = MutableStateFlow(55.0)
    var newTaskScheduledTimeMillis = MutableStateFlow(System.currentTimeMillis() + 10 * 60 * 1000L) // in 10 mins

    fun selectTab(tab: AppTab) {
        _selectedTab.value = tab
    }

    fun setTaskSearchQuery(query: String) {
        _taskSearchQuery.value = query
    }

    fun setStatusFilter(filter: UploadStatus?) {
        _statusFilter.value = filter
    }

    fun toggleLanguage() {
        repository.toggleLanguage()
    }

    fun toggleSchedulerEngine(running: Boolean) {
        repository.setSchedulerRunning(running)
    }

    fun toggleScheduler() {
        repository.setSchedulerRunning(!isSchedulerRunning.value)
    }

    fun submitNewTask(isImmediate: Boolean = false) {
        val title = newTaskTitle.value.trim().ifEmpty { 
            if (currentLanguage.value == AppLanguage.URDU) "نیا شیڈول شدہ ویڈیو ٹاسک" else "New Scheduled Video Task"
        }
        val tagsList = newTaskTags.value.split(",")
            .map { it.trim().replace("#", "") }
            .filter { it.isNotEmpty() }

        val scheduleTime = if (isImmediate) System.currentTimeMillis() else newTaskScheduledTimeMillis.value

        val task = ScheduledTask(
            id = "task_${UUID.randomUUID().toString().take(8)}",
            title = title,
            description = newTaskDescription.value.trim().ifEmpty { "Auto-uploaded video via AutoPoster Hub." },
            tags = if (tagsList.isEmpty()) listOf("AutoPoster", "NewVideo") else tagsList,
            category = newTaskCategory.value,
            platform = newTaskPlatform.value,
            videoFileName = newTaskVideoName.value,
            videoFileSizeMb = newTaskFileSizeMb.value,
            videoDuration = newTaskDuration.value,
            privacy = newTaskPrivacy.value,
            scheduledTimeMillis = scheduleTime,
            status = UploadStatus.SCHEDULED,
            progressPercent = 0
        )

        repository.scheduleNewTask(task)

        if (isImmediate) {
            repository.triggerImmediateUpload(task.id)
        }

        // Reset form
        newTaskTitle.value = ""
        newTaskDescription.value = ""
        _selectedTab.value = AppTab.QUEUE
    }

    fun deleteScheduledTask(taskId: String) {
        repository.deleteTask(taskId)
    }

    fun retryFailedTask(taskId: String) {
        repository.triggerImmediateUpload(taskId)
    }

    fun retryAllFailed() {
        val failed = repository.tasks.value.filter { it.status == UploadStatus.FAILED }
        failed.forEach {
            repository.triggerImmediateUpload(it.id)
        }
    }

    fun triggerImmediateUpload(taskId: String) {
        repository.triggerImmediateUpload(taskId)
    }

    fun sendWhatsAppReport(context: Context, task: ScheduledTask) {
        val currentLang = currentLanguage.value
        val message = WhatsAppReportGenerator.generateReportMessage(task, currentLang)
        val success = WhatsAppReportGenerator.openWhatsApp(context, whatsappSettings.value.phoneNumber, message)
        if (success) {
            repository.addManualReportLog(task, whatsappSettings.value.phoneNumber, message)
        }
    }

    fun sendDirectTestReport(context: Context) {
        val tasks = repository.tasks.value
        val taskToReport = tasks.firstOrNull { it.status == UploadStatus.COMPLETED } ?: tasks.firstOrNull()
        if (taskToReport != null) {
            sendWhatsAppReport(context, taskToReport)
        }
    }

    fun updateWhatsAppConfig(phone: String, autoSend: Boolean, langUrdu: Boolean, alertFailure: Boolean) {
        repository.updateWhatsAppSettings(
            whatsappSettings.value.copy(
                phoneNumber = phone,
                autoSendAfterUpload = autoSend,
                languageUrdu = langUrdu,
                alertOnFailure = alertFailure
            )
        )
    }

    fun updateYouTubeAccount(name: String, channelId: String, apiKey: String, autoUpload: Boolean) {
        repository.updateYouTubeAccount(
            youtubeAccount.value.copy(
                channelName = name,
                channelId = channelId,
                apiKeyOrToken = apiKey,
                autoUploadEnabled = autoUpload
            )
        )
    }

    fun updateFacebookAccount(pageName: String, pageId: String, token: String, autoUpload: Boolean) {
        repository.updateFacebookAccount(
            facebookAccount.value.copy(
                pageName = pageName,
                pageId = pageId,
                pageAccessToken = token,
                autoUploadEnabled = autoUpload
            )
        )
    }

    // AI Smart Generator for Titles & Hashtags (More Ideas requirement)
    fun generateAiMetadata(topic: String = "") {
        val isUrdu = currentLanguage.value == AppLanguage.URDU
        val seed = if (topic.isNotBlank()) topic else "ٹیکنالوجی اور سوشل میڈیا ارننگ"

        if (isUrdu) {
            val suggestions = listOf(
                Pair(
                    "🔥 2026 میں گھر بیٹھے یوٹیوب اور فیس بک سے ڈالرز کمانے کا مکمل طریقہ!",
                    "اس تفصیلی گائیڈ میں ہم بتائیں گے کہ کس طرح آپ اپنے فیس بک پیج اور یوٹیوب کو مونیٹائز کر سکتے ہیں۔ ویڈیو کو پورا دیکھیں اور دوستوں سے شیئر کریں۔ #Earning #YouTubeUrdu #FacebookReels #OnlineMoney"
                ),
                Pair(
                    "🚀 فیس بک ریلز کو 24 گھنٹے میں وائرل کرنے کے 5 خفیہ طریقے",
                    "اگر آپ کی ریلز پر ویوز نہیں آ رہے تو یہ ٹرکس استعمال کریں۔ فالورز اور ریچ فوری بڑھے گی! #ViralVideo #ReelsTips #GrowthHacks"
                ),
                Pair(
                    "💡 AI ٹولز سے ویڈیو بنا کر بغیر چہرہ دکھائے لاکھوں کمائیں",
                    "فیس لیس یوٹیوب چینل شروع کرنے کا سب سے آسان طریقہ۔ اسکرپٹ، وائس اوور اور ایڈیٹنگ سب کچھ آٹومیٹک! #FacelessChannel #AIContent #DigitalPak"
                )
            )
            val picked = suggestions.random()
            newTaskTitle.value = picked.first
            newTaskDescription.value = picked.second
            newTaskTags.value = "YouTube, FacebookReels, ViralContent, OnlineEarning, UrduTutorial"
        } else {
            val suggestions = listOf(
                Pair(
                    "🔥 How to Automate YouTube & Facebook Uploads in 2026 (Full Guide)",
                    "Learn step-by-step how to schedule, cross-publish, and automatically receive upload reports directly to your WhatsApp! #Automation #ContentCreator #SocialMediaStrategy"
                ),
                Pair(
                    "🚀 5 Proven Secrets to Make Your Facebook Reels & YouTube Shorts Go Viral",
                    "Are your videos stuck at low views? Use these algorithmic hooks and posting schedule optimizations to boost watch time. #ViralShorts #FacebookGrowth #ContentHacks"
                ),
                Pair(
                    "💡 How to Build an Automated Video Publishing Empire with AI",
                    "Automate your video workflow from script to auto-upload and instant WhatsApp delivery confirmation. #VideoAutomation #Productivity #CreatorEconomy"
                )
            )
            val picked = suggestions.random()
            newTaskTitle.value = picked.first
            newTaskDescription.value = picked.second
            newTaskTags.value = "Automation, VideoMarketing, SocialMedia, ContentCreator, Growth"
        }
    }
}
