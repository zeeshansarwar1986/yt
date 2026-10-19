package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [VideoUploadTaskEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun videoUploadTaskDao(): VideoUploadTaskDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "video_upload_scheduler.db"
                )
                .addCallback(DatabasePrepopulationCallback(context))
                .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabasePrepopulationCallback(
            private val context: Context
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                // Prepopulate database with initial sample tasks on background thread
                CoroutineScope(Dispatchers.IO).launch {
                    val dao = getInstance(context).videoUploadTaskDao()
                    val now = System.currentTimeMillis()
                    val initialTasks = listOf(
                        VideoUploadTaskEntity(
                            id = "task_01",
                            title = "10 بہترین آن لائن کاروبار جو آپ 2026 میں شروع کر سکتے ہیں",
                            description = "اس ویڈیو میں ہم جانیں گے کہ 2026 میں گھر بیٹھے پیسے کمانے کے سب سے منافع بخش طریقے کون سے ہیں اور آپ کیسے شروع کر سکتے ہیں۔",
                            videoPath = "online_business_guide_2026.mp4",
                            targetPlatform = "BOTH",
                            tags = "OnlineBusiness,UrduTech,PassiveIncome,BusinessIdeas",
                            category = "Education & Business",
                            privacy = "PUBLIC",
                            scheduledTimeEpochMillis = now + 15 * 60 * 1000L,
                            status = "SCHEDULED",
                            progressPercent = 0,
                            videoDuration = "12:40",
                            videoFileSizeMb = 128.4
                        ),
                        VideoUploadTaskEntity(
                            id = "task_02",
                            title = "فیس بک ریلز اور یوٹیوب شارٹس کا الگورتھم کیسے کام کرتا ہے؟",
                            description = "آسان الفاظ میں سمجھیں کہ ویڈیوز کو وائرل کیسے کیا جائے اور زیادہ آڈینس تک کیسے پہنچا جائے۔",
                            videoPath = "reels_viral_secret.mp4",
                            targetPlatform = "BOTH",
                            tags = "YouTubeShorts,FacebookReels,ViralTips,Urdu",
                            category = "Technology",
                            privacy = "PUBLIC",
                            scheduledTimeEpochMillis = now + 45 * 60 * 1000L,
                            status = "SCHEDULED",
                            progressPercent = 0,
                            videoDuration = "00:58",
                            videoFileSizeMb = 42.0
                        ),
                        VideoUploadTaskEntity(
                            id = "task_03",
                            title = "تازہ ترین ٹیکنالوجی نیوز: پاکستان میں نیا ڈیٹا سینٹر قائم",
                            description = "نئے انفراسٹرکچر پروجیکٹ کی تفصیلات اور اس کے فری لانسرز پر اثرات۔",
                            videoPath = "pakistan_datacenter_news.mp4",
                            targetPlatform = "YOUTUBE",
                            tags = "TechNews,Pakistan,ITExport",
                            category = "News & Politics",
                            privacy = "PUBLIC",
                            scheduledTimeEpochMillis = now - 2 * 3600 * 1000L,
                            status = "COMPLETED",
                            progressPercent = 100,
                            videoDuration = "06:15",
                            videoFileSizeMb = 85.0,
                            youtubeVideoUrl = "https://youtu.be/yt_techpak992",
                            reportSentToWhatsApp = true,
                            whatsappReportTimestamp = now - 2 * 3600 * 1000L + 5 * 60 * 1000L
                        ),
                        VideoUploadTaskEntity(
                            id = "task_04",
                            title = "ڈیجیٹل مارکیٹنگ سے 1 لاکھ روپے ماہانہ کمانے کا روڈ میپ",
                            description = "مرحلہ وار گائیڈ جو ہر مبتدی کو فالو کرنی چاہیے۔",
                            videoPath = "digital_marketing_roadmap.mp4",
                            targetPlatform = "FACEBOOK",
                            tags = "DigitalMarketing,Freelancing,SkillDevelopment",
                            category = "How-to & Style",
                            privacy = "PUBLIC",
                            scheduledTimeEpochMillis = now - 6 * 3600 * 1000L,
                            status = "COMPLETED",
                            progressPercent = 100,
                            videoDuration = "15:20",
                            videoFileSizeMb = 94.6,
                            facebookPostUrl = "https://facebook.com/watch/?v=fb_mktg_8849",
                            reportSentToWhatsApp = true,
                            whatsappReportTimestamp = now - 6 * 3600 * 1000L + 3 * 60 * 1000L
                        ),
                        VideoUploadTaskEntity(
                            id = "task_05",
                            title = "انٹرنیٹ اسپیڈ اور 5G پاکستان میں: تفصیلی جائزہ",
                            description = "مختلف شہروں میں 5G اور فائبر آپٹک کا لائیو اسپیڈ ٹیسٹ۔",
                            videoPath = "pakistan_5g_speedtest.mp4",
                            targetPlatform = "BOTH",
                            tags = "5GPakistan,InternetSpeed,UrduTech",
                            category = "Science & Technology",
                            privacy = "PUBLIC",
                            scheduledTimeEpochMillis = now - 1 * 3600 * 1000L,
                            status = "FAILED",
                            progressPercent = 45,
                            videoDuration = "11:10",
                            videoFileSizeMb = 112.0,
                            errorMessage = "YouTube API Quota exceeded or token refresh required (Error 403)"
                        )
                    )
                    dao.insertTasks(initialTasks)
                }
            }
        }
    }
}
