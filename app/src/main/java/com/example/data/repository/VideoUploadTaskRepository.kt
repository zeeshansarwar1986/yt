package com.example.data.repository

import com.example.data.local.VideoUploadTaskDao
import com.example.data.local.toDomainModel
import com.example.data.local.toEntity
import com.example.model.ScheduledTask
import com.example.model.UploadStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class VideoUploadTaskRepository(private val dao: VideoUploadTaskDao) {

    val allTasks: Flow<List<ScheduledTask>> = dao.getAllTasks().map { entities ->
        entities.map { it.toDomainModel() }
    }

    val completedAndFailedTasks: Flow<List<ScheduledTask>> = dao.getCompletedAndFailedTasks().map { entities ->
        entities.map { it.toDomainModel() }
    }

    suspend fun retryTask(id: String) {
        dao.resetTaskForRetry(id)
    }

    fun getTasksByStatus(status: UploadStatus): Flow<List<ScheduledTask>> {
        return dao.getTasksByStatus(status.name).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    suspend fun getTaskById(id: String): ScheduledTask? {
        return dao.getTaskById(id)?.toDomainModel()
    }

    suspend fun insertTask(task: ScheduledTask) {
        dao.insertTask(task.toEntity())
    }

    suspend fun updateTask(task: ScheduledTask) {
        dao.updateTask(task.toEntity())
    }

    suspend fun deleteTask(id: String) {
        dao.deleteTaskById(id)
    }

    suspend fun updateProgress(id: String, status: UploadStatus, progress: Int) {
        dao.updateProgress(id, status.name, progress)
    }

    suspend fun markCompleted(
        id: String,
        ytUrl: String?,
        fbUrl: String?,
        reportSent: Boolean,
        reportTimestamp: Long?
    ) {
        dao.markCompleted(id, ytUrl, fbUrl, reportSent, reportTimestamp)
    }

    suspend fun updateReportSent(id: String, reportSent: Boolean, timestamp: Long) {
        dao.updateReportStatus(id, reportSent, timestamp)
    }
}
