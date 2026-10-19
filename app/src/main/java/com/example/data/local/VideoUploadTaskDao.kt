package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface VideoUploadTaskDao {

    @Query("SELECT * FROM video_upload_tasks ORDER BY scheduledTimeEpochMillis ASC")
    fun getAllTasks(): Flow<List<VideoUploadTaskEntity>>

    @Query("SELECT * FROM video_upload_tasks WHERE status = :status ORDER BY scheduledTimeEpochMillis ASC")
    fun getTasksByStatus(status: String): Flow<List<VideoUploadTaskEntity>>

    @Query("SELECT * FROM video_upload_tasks WHERE status IN ('COMPLETED', 'FAILED') ORDER BY scheduledTimeEpochMillis DESC")
    fun getCompletedAndFailedTasks(): Flow<List<VideoUploadTaskEntity>>

    @Query("UPDATE video_upload_tasks SET status = 'SCHEDULED', progressPercent = 0, errorMessage = null WHERE id = :id")
    suspend fun resetTaskForRetry(id: String)

    @Query("SELECT * FROM video_upload_tasks WHERE id = :id")
    suspend fun getTaskById(id: String): VideoUploadTaskEntity?

    @Query("SELECT COUNT(*) FROM video_upload_tasks")
    suspend fun getTaskCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: VideoUploadTaskEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTasks(tasks: List<VideoUploadTaskEntity>)

    @Update
    suspend fun updateTask(task: VideoUploadTaskEntity)

    @Query("UPDATE video_upload_tasks SET status = :status, progressPercent = :progress WHERE id = :id")
    suspend fun updateProgress(id: String, status: String, progress: Int)

    @Query("UPDATE video_upload_tasks SET status = 'COMPLETED', progressPercent = 100, youtubeVideoUrl = :ytUrl, facebookPostUrl = :fbUrl, reportSentToWhatsApp = :reportSent, whatsappReportTimestamp = :reportTimestamp WHERE id = :id")
    suspend fun markCompleted(id: String, ytUrl: String?, fbUrl: String?, reportSent: Boolean, reportTimestamp: Long?)

    @Query("UPDATE video_upload_tasks SET reportSentToWhatsApp = :reportSent, whatsappReportTimestamp = :reportTimestamp WHERE id = :id")
    suspend fun updateReportStatus(id: String, reportSent: Boolean, reportTimestamp: Long)

    @Query("DELETE FROM video_upload_tasks WHERE id = :id")
    suspend fun deleteTaskById(id: String)

    @Query("DELETE FROM video_upload_tasks")
    suspend fun clearAll()
}
