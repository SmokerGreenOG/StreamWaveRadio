package com.streamwave.radio.data.repository

import com.streamwave.radio.data.database.entity.StationSubmissionEntity
import kotlinx.coroutines.flow.Flow

interface SubmissionRepository {
    fun getAll(): Flow<List<StationSubmissionEntity>>
    fun getByStatus(status: String): Flow<List<StationSubmissionEntity>>
    fun getByUser(userId: String): Flow<List<StationSubmissionEntity>>
    suspend fun getById(id: Long): StationSubmissionEntity?
    suspend fun submit(submission: StationSubmissionEntity): Long
    suspend fun update(submission: StationSubmissionEntity)
    suspend fun updateStatus(id: Long, status: String, note: String)
    suspend fun delete(id: Long)
}
