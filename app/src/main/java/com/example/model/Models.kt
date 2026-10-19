package com.example.model

enum class PlatformType(val displayName: String, val displayNameUrdu: String) {
    YOUTUBE("YouTube", "یوٹیوب"),
    FACEBOOK("Facebook Page", "فیس بک پیج"),
    BOTH("Both Platforms", "دونوں پلیٹ فارمز")
}

enum class UploadStatus(val label: String, val labelUrdu: String) {
    SCHEDULED("Scheduled", "شیڈول شدہ"),
    UPLOADING("Uploading...", "اپلوڈ جاری ہے..."),
    COMPLETED("Completed", "مکمل ہو گیا"),
    FAILED("Failed", "ناکام"),
    CANCELLED("Cancelled", "منسوخ")
}

enum class VideoPrivacy(val label: String, val labelUrdu: String) {
    PUBLIC("Public", "عوامی (پبلک)"),
    UNLISTED("Unlisted", "غیر فہرست شدہ"),
    PRIVATE("Private", "ذاتی (پرائیویٹ)")
}

data class ScheduledTask(
    val id: String,
    val title: String,
    val description: String,
    val tags: List<String> = emptyList(),
    val category: String = "Entertainment",
    val platform: PlatformType = PlatformType.BOTH,
    val videoFileName: String = "video_clip.mp4",
    val videoFileSizeMb: Double = 45.2,
    val videoDuration: String = "08:45",
    val privacy: VideoPrivacy = VideoPrivacy.PUBLIC,
    val scheduledTimeMillis: Long,
    val createdAtMillis: Long = System.currentTimeMillis(),
    val status: UploadStatus = UploadStatus.SCHEDULED,
    val progressPercent: Int = 0,
    val youtubeVideoUrl: String? = null,
    val facebookPostUrl: String? = null,
    val errorMessage: String? = null,
    val reportSentToWhatsApp: Boolean = false,
    val reportSentTimestamp: Long? = null
)

data class YouTubeAccount(
    val isConnected: Boolean = true,
    val channelName: String = "Tech & Info Official",
    val channelId: String = "UC_tech_info_official_01",
    val subscriberCount: String = "124K",
    val videoCount: Int = 342,
    val avatarUrl: String = "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?w=200",
    val apiKeyOrToken: String = "AIzaSyBxxxx...SAMPLE_KEY",
    val autoUploadEnabled: Boolean = true
)

data class FacebookPageAccount(
    val isConnected: Boolean = true,
    val pageName: String = "Daily Videos & Reels",
    val pageId: String = "104928502847592",
    val followersCount: String = "89.5K",
    val category: String = "Media & News Company",
    val pageAccessToken: String = "EAAGm0PXq...SAMPLE_FB_TOKEN",
    val autoUploadEnabled: Boolean = true
)

data class WhatsAppSettings(
    val phoneNumber: String = "+923001234567",
    val autoSendAfterUpload: Boolean = true,
    val includeDirectLinks: Boolean = true,
    val languageUrdu: Boolean = true,
    val alertOnFailure: Boolean = true,
    val customNotes: String = "Auto-generated report by AutoPoster Engine"
)

data class WhatsAppReportLog(
    val id: String,
    val timestampMillis: Long,
    val recipientPhone: String,
    val formattedMessage: String,
    val videoTitle: String,
    val platformsSummary: String,
    val status: String,
    val isSuccess: Boolean
)

enum class AppLanguage {
    URDU,
    ENGLISH
}
