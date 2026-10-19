package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.model.PlatformType
import com.example.model.ScheduledTask
import com.example.model.UploadStatus
import com.example.model.VideoPrivacy

@Entity(tableName = "video_upload_tasks")
data class VideoUploadTaskEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val description: String,
    val videoPath: String,
    val targetPlatform: String, // "YOUTUBE", "FACEBOOK", "BOTH"
    val tags: String = "", // Comma-separated tags
    val category: String = "Entertainment",
    val privacy: String = "PUBLIC", // "PUBLIC", "UNLISTED", "PRIVATE"
    val scheduledTimeEpochMillis: Long,
    val createdAtEpochMillis: Long = System.currentTimeMillis(),
    val status: String = "SCHEDULED", // "SCHEDULED", "UPLOADING", "COMPLETED", "FAILED", "CANCELLED"
    val progressPercent: Int = 0,
    val videoDuration: String = "05:00",
    val videoFileSizeMb: Double = 50.0,
    val youtubeVideoUrl: String? = null,
    val facebookPostUrl: String? = null,
    val errorMessage: String? = null,
    val reportSentToWhatsApp: Boolean = false,
    val whatsappReportTimestamp: Long? = null
)

fun VideoUploadTaskEntity.toDomainModel(): ScheduledTask {
    val platformEnum = try {
        PlatformType.valueOf(targetPlatform)
    } catch (e: Exception) {
        PlatformType.BOTH
    }

    val statusEnum = try {
        UploadStatus.valueOf(status)
    } catch (e: Exception) {
        UploadStatus.SCHEDULED
    }

    val privacyEnum = try {
        VideoPrivacy.valueOf(privacy)
    } catch (e: Exception) {
        VideoPrivacy.PUBLIC
    }

    val tagList = if (tags.isBlank()) emptyList() else tags.split(",").map { it.trim() }.filter { it.isNotEmpty() }

    return ScheduledTask(
        id = id,
        title = title,
        description = description,
        tags = tagList,
        category = category,
        platform = platformEnum,
        videoFileName = videoPath,
        videoFileSizeMb = videoFileSizeMb,
        videoDuration = videoDuration,
        privacy = privacyEnum,
        scheduledTimeMillis = scheduledTimeEpochMillis,
        createdAtMillis = createdAtEpochMillis,
        status = statusEnum,
        progressPercent = progressPercent,
        youtubeVideoUrl = youtubeVideoUrl,
        facebookPostUrl = facebookPostUrl,
        errorMessage = errorMessage,
        reportSentToWhatsApp = reportSentToWhatsApp,
        reportSentTimestamp = whatsappReportTimestamp
    )
}

fun ScheduledTask.toEntity(): VideoUploadTaskEntity {
    return VideoUploadTaskEntity(
        id = id,
        title = title,
        description = description,
        videoPath = videoFileName,
        targetPlatform = platform.name,
        tags = tags.joinToString(","),
        category = category,
        privacy = privacy.name,
        scheduledTimeEpochMillis = scheduledTimeMillis,
        createdAtEpochMillis = createdAtMillis,
        status = status.name,
        progressPercent = progressPercent,
        videoDuration = videoDuration,
        videoFileSizeMb = videoFileSizeMb,
        youtubeVideoUrl = youtubeVideoUrl,
        facebookPostUrl = facebookPostUrl,
        errorMessage = errorMessage,
        reportSentToWhatsApp = reportSentToWhatsApp,
        whatsappReportTimestamp = reportSentTimestamp
    )
}
