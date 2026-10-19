package com.example

import android.app.Application
import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.data.WhatsAppReportGenerator
import com.example.data.local.AppDatabase
import com.example.data.local.VideoUploadTaskDao
import com.example.data.local.VideoUploadTaskEntity
import com.example.data.local.toDomainModel
import com.example.data.local.toEntity
import com.example.data.repository.VideoUploadTaskRepository
import com.example.model.AppLanguage
import com.example.model.PlatformType
import com.example.model.ScheduledTask
import com.example.model.UploadStatus
import com.example.model.VideoPrivacy
import com.example.ui.viewmodel.VideoTaskViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    private lateinit var db: AppDatabase
    private lateinit var dao: VideoUploadTaskDao
    private lateinit var repository: VideoUploadTaskRepository

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = db.videoUploadTaskDao()
        repository = VideoUploadTaskRepository(dao)
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun readStringFromContext() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("AutoPoster", appName)
    }

    @Test
    fun testRoomDatabaseSchemaAndRepositoryInsertAndFetch() = runBlocking {
        val now = System.currentTimeMillis()
        val task = ScheduledTask(
            id = "task_room_101",
            title = "یوٹیوب اور فیس بک آٹو شیڈول ویڈیو",
            description = "مکمل خودکار شیڈولنگ گائیڈ",
            tags = listOf("Automation", "SocialMedia", "Urdu"),
            category = "Technology",
            platform = PlatformType.BOTH,
            videoFileName = "/storage/emulated/0/DCIM/upload_ready.mp4",
            videoFileSizeMb = 95.5,
            videoDuration = "09:30",
            privacy = VideoPrivacy.PUBLIC,
            scheduledTimeMillis = now + 3600000L,
            status = UploadStatus.SCHEDULED,
            progressPercent = 0
        )

        // Test insertion
        repository.insertTask(task)

        // Test query by ID
        val retrieved = repository.getTaskById("task_room_101")
        assertNotNull(retrieved)
        assertEquals("یوٹیوب اور فیس بک آٹو شیڈول ویڈیو", retrieved?.title)
        assertEquals("/storage/emulated/0/DCIM/upload_ready.mp4", retrieved?.videoFileName)
        assertEquals(PlatformType.BOTH, retrieved?.platform)
        assertEquals(UploadStatus.SCHEDULED, retrieved?.status)

        // Test Flow emission
        val allList = repository.allTasks.first()
        assertEquals(1, allList.size)
        assertEquals("task_room_101", allList[0].id)

        // Test update progress
        repository.updateProgress("task_room_101", UploadStatus.UPLOADING, 65)
        val updated = repository.getTaskById("task_room_101")
        assertEquals(UploadStatus.UPLOADING, updated?.status)
        assertEquals(65, updated?.progressPercent)

        // Test mark completed with URLs
        val ytUrl = "https://youtu.be/sample_101"
        val fbUrl = "https://facebook.com/watch/?v=sample_101"
        repository.markCompleted("task_room_101", ytUrl, fbUrl, true, now + 5000L)

        val completed = repository.getTaskById("task_room_101")
        assertEquals(UploadStatus.COMPLETED, completed?.status)
        assertEquals(100, completed?.progressPercent)
        assertEquals(ytUrl, completed?.youtubeVideoUrl)
        assertEquals(fbUrl, completed?.facebookPostUrl)
        assertTrue(completed?.reportSentToWhatsApp == true)

        // Test delete
        repository.deleteTask("task_room_101")
        val afterDelete = repository.getTaskById("task_room_101")
        assertEquals(null, afterDelete)
    }

    @Test
    fun testVideoTaskViewModelIntegration() = runBlocking {
        val app = ApplicationProvider.getApplicationContext<Application>()
        val vm = VideoTaskViewModel(app)

        vm.saveScheduledTask(
            title = "Test ViewModel Video",
            description = "ViewModel description test",
            videoPath = "/sdcard/videos/my_video.mp4",
            targetPlatform = PlatformType.YOUTUBE,
            scheduledTimeMillis = System.currentTimeMillis() + 600000L
        )

        // Let flow collect
        val state = vm.uiState.value
        assertNotNull(state)
    }

    @Test
    fun testWhatsAppReportFormattingUrdu() {
        val task = ScheduledTask(
            id = "test_01",
            title = "اردو ٹیسٹ ویڈیو",
            description = "ٹیسٹ تفصیل",
            tags = listOf("Urdu", "Viral"),
            category = "Education",
            platform = PlatformType.BOTH,
            scheduledTimeMillis = System.currentTimeMillis(),
            status = UploadStatus.COMPLETED,
            youtubeVideoUrl = "https://youtu.be/sample_yt",
            facebookPostUrl = "https://facebook.com/watch/?v=sample_fb"
        )

        val reportUrdu = WhatsAppReportGenerator.generateReportMessage(task, AppLanguage.URDU)
        assertTrue(reportUrdu.contains("آٹو پوسٹر اپلوڈ رپورٹ"))
        assertTrue(reportUrdu.contains("اردو ٹیسٹ ویڈیو"))
        assertTrue(reportUrdu.contains("https://youtu.be/sample_yt"))
        assertTrue(reportUrdu.contains("https://facebook.com/watch/?v=sample_fb"))
    }

    @Test
    fun testWhatsAppReportFormattingEnglish() {
        val task = ScheduledTask(
            id = "test_02",
            title = "English Test Video",
            description = "Test Desc",
            tags = listOf("English", "Viral"),
            category = "Tech",
            platform = PlatformType.YOUTUBE,
            scheduledTimeMillis = System.currentTimeMillis(),
            status = UploadStatus.COMPLETED,
            youtubeVideoUrl = "https://youtu.be/test_yt"
        )

        val reportEn = WhatsAppReportGenerator.generateReportMessage(task, AppLanguage.ENGLISH)
        assertTrue(reportEn.contains("AutoPoster Execution Report"))
        assertTrue(reportEn.contains("English Test Video"))
        assertTrue(reportEn.contains("https://youtu.be/test_yt"))
    }

    @Test
    fun testFormSavesToRoomDatabase() = runBlocking {
        var savedTitle = ""
        var savedPath = ""
        var savedPlatform: PlatformType = PlatformType.YOUTUBE

        val onSave: (String, String, String, PlatformType, Long) -> Unit = { title, desc, path, platform, time ->
            savedTitle = title
            savedPath = path
            savedPlatform = platform
            runBlocking {
                repository.insertTask(
                    ScheduledTask(
                        id = "form_task_01",
                        title = title,
                        description = desc,
                        videoFileName = path,
                        platform = platform,
                        scheduledTimeMillis = time
                    )
                )
            }
        }

        onSave(
            "New Video from Compose Form",
            "Collected from clean Compose UI component",
            "/storage/emulated/0/DCIM/form_video.mp4",
            PlatformType.FACEBOOK,
            1730000000000L
        )

        assertEquals("New Video from Compose Form", savedTitle)
        assertEquals(PlatformType.FACEBOOK, savedPlatform)

        val fromDb = repository.getTaskById("form_task_01")
        assertNotNull(fromDb)
        assertEquals("New Video from Compose Form", fromDb?.title)
        assertEquals("/storage/emulated/0/DCIM/form_video.mp4", fromDb?.videoFileName)
        assertEquals(PlatformType.FACEBOOK, fromDb?.platform)
    }

    @Test
    fun testCompletedAndFailedUploadsQueryAndRetry() = runBlocking {
        val now = System.currentTimeMillis()

        val completedTask = ScheduledTask(
            id = "test_completed_1",
            title = "Completed Video 1",
            description = "Completed successfully",
            platform = PlatformType.YOUTUBE,
            scheduledTimeMillis = now - 50000L,
            status = UploadStatus.COMPLETED,
            progressPercent = 100,
            youtubeVideoUrl = "https://youtu.be/test_completed_1"
        )

        val failedTask = ScheduledTask(
            id = "test_failed_1",
            title = "Failed Video 1",
            description = "Failed due to quota",
            platform = PlatformType.FACEBOOK,
            scheduledTimeMillis = now - 20000L,
            status = UploadStatus.FAILED,
            progressPercent = 30,
            errorMessage = "Quota exceeded or token expired (Error 403)"
        )

        val scheduledTask = ScheduledTask(
            id = "test_scheduled_1",
            title = "Scheduled Video 1",
            description = "Still upcoming",
            platform = PlatformType.BOTH,
            scheduledTimeMillis = now + 100000L,
            status = UploadStatus.SCHEDULED,
            progressPercent = 0
        )

        repository.insertTask(completedTask)
        repository.insertTask(failedTask)
        repository.insertTask(scheduledTask)

        // Query completed and failed tasks list
        val finishedList = repository.completedAndFailedTasks.first()
        assertEquals(2, finishedList.size)
        assertTrue(finishedList.any { it.id == "test_completed_1" && it.status == UploadStatus.COMPLETED })
        assertTrue(finishedList.any { it.id == "test_failed_1" && it.status == UploadStatus.FAILED })

        // Test retry on the failed task
        repository.retryTask("test_failed_1")
        val retriedTask = repository.getTaskById("test_failed_1")
        assertNotNull(retriedTask)
        assertEquals(UploadStatus.SCHEDULED, retriedTask?.status)
        assertEquals(0, retriedTask?.progressPercent)
        assertEquals(null, retriedTask?.errorMessage)
    }
}
