package com.example.data

import com.example.data.local.VideoUploadTaskDao
import com.example.data.local.toDomainModel
import com.example.data.local.toEntity
import com.example.model.AppLanguage
import com.example.model.FacebookPageAccount
import com.example.model.PlatformType
import com.example.model.ScheduledTask
import com.example.model.UploadStatus
import com.example.model.VideoPrivacy
import com.example.model.WhatsAppReportLog
import com.example.model.WhatsAppSettings
import com.example.model.YouTubeAccount
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.UUID

class AutoPosterRepository(
    private val scope: CoroutineScope,
    private val dao: VideoUploadTaskDao? = null
) {

    private val _tasks = MutableStateFlow<List<ScheduledTask>>(emptyList())
    val tasks: StateFlow<List<ScheduledTask>> = _tasks.asStateFlow()

    private val _youtubeAccount = MutableStateFlow(YouTubeAccount())
    val youtubeAccount: StateFlow<YouTubeAccount> = _youtubeAccount.asStateFlow()

    private val _facebookAccount = MutableStateFlow(FacebookPageAccount())
    val facebookAccount: StateFlow<FacebookPageAccount> = _facebookAccount.asStateFlow()

    private val _whatsappSettings = MutableStateFlow(WhatsAppSettings())
    val whatsappSettings: StateFlow<WhatsAppSettings> = _whatsappSettings.asStateFlow()

    private val _reportLogs = MutableStateFlow<List<WhatsAppReportLog>>(emptyList())
    val reportLogs: StateFlow<List<WhatsAppReportLog>> = _reportLogs.asStateFlow()

    private val _currentLanguage = MutableStateFlow(AppLanguage.URDU)
    val currentLanguage: StateFlow<AppLanguage> = _currentLanguage.asStateFlow()

    private val _isSchedulerRunning = MutableStateFlow(true)
    val isSchedulerRunning: StateFlow<Boolean> = _isSchedulerRunning.asStateFlow()

    private var schedulerLoopJob: Job? = null

    init {
        val now = System.currentTimeMillis()
        val sampleTasks = listOf(
            ScheduledTask(
                id = "task_01",
                title = "10 بہترین آن لائن کاروبار جو آپ 2026 میں شروع کر سکتے ہیں",
                description = "اس ویڈیو میں ہم جانیں گے کہ 2026 میں گھر بیٹھے پیسے کمانے کے سب سے منافع بخش طریقے کون سے ہیں اور آپ کیسے شروع کر سکتے ہیں۔",
                tags = listOf("OnlineBusiness", "UrduTech", "PassiveIncome", "BusinessIdeas"),
                category = "Education & Business",
                platform = PlatformType.BOTH,
                videoFileName = "online_business_guide_2026.mp4",
                videoFileSizeMb = 128.4,
                videoDuration = "12:40",
                privacy = VideoPrivacy.PUBLIC,
                scheduledTimeMillis = now + 15 * 60 * 1000L,
                status = UploadStatus.SCHEDULED,
                progressPercent = 0
            ),
            ScheduledTask(
                id = "task_02",
                title = "فیس بک ریلز اور یوٹیوب شارٹس کا الگورتھم کیسے کام کرتا ہے؟",
                description = "آسان الفاظ میں سمجھیں کہ ویڈیوز کو وائرل کیسے کیا جائے اور زیادہ آڈینس تک کیسے پہنچا جائے۔",
                tags = listOf("YouTubeShorts", "FacebookReels", "ViralTips", "Urdu"),
                category = "Technology",
                platform = PlatformType.BOTH,
                videoFileName = "reels_viral_secret.mp4",
                videoFileSizeMb = 42.0,
                videoDuration = "00:58",
                privacy = VideoPrivacy.PUBLIC,
                scheduledTimeMillis = now + 45 * 60 * 1000L,
                status = UploadStatus.SCHEDULED,
                progressPercent = 0
            ),
            ScheduledTask(
                id = "task_03",
                title = "تازہ ترین ٹیکنالوجی نیوز: پاکستان میں نیا ڈیٹا سینٹر قائم",
                description = "نئے انفراسٹرکچر پروجیکٹ کی تفصیلات اور اس کے فری لانسرز پر اثرات۔",
                tags = listOf("TechNews", "Pakistan", "ITExport"),
                category = "News & Politics",
                platform = PlatformType.YOUTUBE,
                videoFileName = "pakistan_datacenter_news.mp4",
                videoFileSizeMb = 85.0,
                videoDuration = "06:15",
                privacy = VideoPrivacy.PUBLIC,
                scheduledTimeMillis = now - 2 * 3600 * 1000L,
                status = UploadStatus.COMPLETED,
                progressPercent = 100,
                youtubeVideoUrl = "https://youtu.be/yt_techpak992",
                reportSentToWhatsApp = true,
                reportSentTimestamp = now - 2 * 3600 * 1000L + 5 * 60 * 1000L
            ),
            ScheduledTask(
                id = "task_04",
                title = "ڈیجیٹل مارکیٹنگ سے 1 لاکھ روپے ماہانہ کمانے کا روڈ میپ",
                description = "مرحلہ وار گائیڈ جو ہر مبتدی کو فالو کرنی چاہیے۔",
                tags = listOf("DigitalMarketing", "Freelancing", "SkillDevelopment"),
                category = "How-to & Style",
                platform = PlatformType.FACEBOOK,
                videoFileName = "digital_marketing_roadmap.mp4",
                videoFileSizeMb = 94.6,
                videoDuration = "15:20",
                privacy = VideoPrivacy.PUBLIC,
                scheduledTimeMillis = now - 6 * 3600 * 1000L,
                status = UploadStatus.COMPLETED,
                progressPercent = 100,
                facebookPostUrl = "https://facebook.com/watch/?v=fb_mktg_8849",
                reportSentToWhatsApp = true,
                reportSentTimestamp = now - 6 * 3600 * 1000L + 3 * 60 * 1000L
            ),
            ScheduledTask(
                id = "task_05",
                title = "انٹرنیٹ اسپیڈ اور 5G پاکستان میں: تفصیلی جائزہ",
                description = "مختلف شہروں میں 5G اور فائبر آپٹک کا لائیو اسپیڈ ٹیسٹ۔",
                tags = listOf("5GPakistan", "InternetSpeed", "UrduTech"),
                category = "Science & Technology",
                platform = PlatformType.BOTH,
                videoFileName = "pakistan_5g_speedtest.mp4",
                videoFileSizeMb = 112.0,
                videoDuration = "11:10",
                privacy = VideoPrivacy.PUBLIC,
                scheduledTimeMillis = now - 1 * 3600 * 1000L,
                status = UploadStatus.FAILED,
                progressPercent = 45,
                errorMessage = "YouTube API Quota exceeded or token refresh required (Error 403)"
            )
        )

        // Seed initial tasks in memory as fallback
        _tasks.value = sampleTasks

        // Initial WhatsApp Report logs
        _reportLogs.value = listOf(
            WhatsAppReportLog(
                id = "log_01",
                timestampMillis = now - 2 * 3600 * 1000L + 5 * 60 * 1000L,
                recipientPhone = _whatsappSettings.value.phoneNumber,
                formattedMessage = WhatsAppReportGenerator.generateReportMessage(sampleTasks[2], AppLanguage.URDU),
                videoTitle = sampleTasks[2].title,
                platformsSummary = "YouTube",
                status = "کامیابی سے ڈیلیور ہو گیا (Delivered)",
                isSuccess = true
            ),
            WhatsAppReportLog(
                id = "log_02",
                timestampMillis = now - 6 * 3600 * 1000L + 3 * 60 * 1000L,
                recipientPhone = _whatsappSettings.value.phoneNumber,
                formattedMessage = WhatsAppReportGenerator.generateReportMessage(sampleTasks[3], AppLanguage.URDU),
                videoTitle = sampleTasks[3].title,
                platformsSummary = "Facebook Page",
                status = "کامیابی سے ڈیلیور ہو گیا (Delivered)",
                isSuccess = true
            )
        )

        // If DAO is provided, observe the Room Flow reactively and seed if empty
        if (dao != null) {
            scope.launch(Dispatchers.IO) {
                if (dao.getTaskCount() == 0) {
                    dao.insertTasks(sampleTasks.map { it.toEntity() })
                }
                dao.getAllTasks().collectLatest { entities ->
                    if (entities.isNotEmpty()) {
                        _tasks.value = entities.map { it.toDomainModel() }
                    }
                }
            }
        }

        startSchedulerEngine()
    }

    fun toggleLanguage() {
        _currentLanguage.value = if (_currentLanguage.value == AppLanguage.URDU) AppLanguage.ENGLISH else AppLanguage.URDU
    }

    fun setLanguage(lang: AppLanguage) {
        _currentLanguage.value = lang
    }

    fun setSchedulerRunning(running: Boolean) {
        _isSchedulerRunning.value = running
        if (running && (schedulerLoopJob == null || schedulerLoopJob?.isActive == false)) {
            startSchedulerEngine()
        }
    }

    private fun startSchedulerEngine() {
        schedulerLoopJob?.cancel()
        schedulerLoopJob = scope.launch(Dispatchers.Default) {
            while (_isSchedulerRunning.value) {
                delay(3000)
                val now = System.currentTimeMillis()
                val currentTasks = _tasks.value
                val dueTask = currentTasks.firstOrNull { 
                    it.status == UploadStatus.SCHEDULED && it.scheduledTimeMillis <= now 
                }

                if (dueTask != null) {
                    processTaskUpload(dueTask.id)
                }
            }
        }
    }

    fun scheduleNewTask(task: ScheduledTask) {
        _tasks.value = listOf(task) + _tasks.value
        if (dao != null) {
            scope.launch(Dispatchers.IO) {
                dao.insertTask(task.toEntity())
            }
        }
    }

    fun deleteTask(taskId: String) {
        _tasks.value = _tasks.value.filter { it.id != taskId }
        if (dao != null) {
            scope.launch(Dispatchers.IO) {
                dao.deleteTaskById(taskId)
            }
        }
    }

    fun triggerImmediateUpload(taskId: String) {
        scope.launch(Dispatchers.Default) {
            processTaskUpload(taskId)
        }
    }

    private suspend fun processTaskUpload(taskId: String) {
        _tasks.value = _tasks.value.map { task ->
            if (task.id == taskId) {
                task.copy(status = UploadStatus.UPLOADING, progressPercent = 5)
            } else task
        }
        dao?.updateProgress(taskId, UploadStatus.UPLOADING.name, 5)

        val steps = listOf(15, 35, 60, 85, 100)
        for (p in steps) {
            delay(1000)
            _tasks.value = _tasks.value.map { task ->
                if (task.id == taskId) {
                    task.copy(progressPercent = p)
                } else task
            }
            dao?.updateProgress(taskId, UploadStatus.UPLOADING.name, p)
        }

        val ytUrl = "https://youtu.be/v_${taskId.take(8)}"
        val fbUrl = "https://facebook.com/watch/?v=fb_${taskId.take(8)}"
        val completedTime = System.currentTimeMillis()

        var finishedTask: ScheduledTask? = null
        _tasks.value = _tasks.value.map { task ->
            if (task.id == taskId) {
                val updated = task.copy(
                    status = UploadStatus.COMPLETED,
                    progressPercent = 100,
                    youtubeVideoUrl = ytUrl,
                    facebookPostUrl = fbUrl,
                    reportSentToWhatsApp = _whatsappSettings.value.autoSendAfterUpload,
                    reportSentTimestamp = completedTime
                )
                finishedTask = updated
                updated
            } else task
        }

        dao?.markCompleted(
            id = taskId,
            ytUrl = ytUrl,
            fbUrl = fbUrl,
            reportSent = _whatsappSettings.value.autoSendAfterUpload,
            reportTimestamp = completedTime
        )

        finishedTask?.let { task ->
            if (_whatsappSettings.value.autoSendAfterUpload) {
                val formattedMsg = WhatsAppReportGenerator.generateReportMessage(
                    task, 
                    if (_whatsappSettings.value.languageUrdu) AppLanguage.URDU else AppLanguage.ENGLISH
                )
                val logEntry = WhatsAppReportLog(
                    id = "log_${UUID.randomUUID()}",
                    timestampMillis = completedTime,
                    recipientPhone = _whatsappSettings.value.phoneNumber,
                    formattedMessage = formattedMsg,
                    videoTitle = task.title,
                    platformsSummary = task.platform.displayName,
                    status = "رپورٹ تیار اور روانہ (Dispatched to WhatsApp)",
                    isSuccess = true
                )
                _reportLogs.value = listOf(logEntry) + _reportLogs.value
            }
        }
    }

    fun updateWhatsAppSettings(newSettings: WhatsAppSettings) {
        _whatsappSettings.value = newSettings
    }

    fun updateYouTubeAccount(account: YouTubeAccount) {
        _youtubeAccount.value = account
    }

    fun updateFacebookAccount(account: FacebookPageAccount) {
        _facebookAccount.value = account
    }

    fun addManualReportLog(task: ScheduledTask, recipient: String, message: String) {
        val entry = WhatsAppReportLog(
            id = "log_${UUID.randomUUID()}",
            timestampMillis = System.currentTimeMillis(),
            recipientPhone = recipient,
            formattedMessage = message,
            videoTitle = task.title,
            platformsSummary = task.platform.displayName,
            status = "دستی بھیجی گئی رپورٹ (Direct Shared via WhatsApp)",
            isSuccess = true
        )
        _reportLogs.value = listOf(entry) + _reportLogs.value
        val now = System.currentTimeMillis()
        _tasks.value = _tasks.value.map {
            if (it.id == task.id) it.copy(reportSentToWhatsApp = true, reportSentTimestamp = now)
            else it
        }
        if (dao != null) {
            scope.launch(Dispatchers.IO) {
                dao.updateReportStatus(task.id, true, now)
            }
        }
    }
}
