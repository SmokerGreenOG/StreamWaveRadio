package com.streamwave.radio.data.repository

import com.streamwave.radio.data.database.dao.StationSubmissionDao
import com.streamwave.radio.data.database.entity.StationSubmissionEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SubmissionRepositoryImpl @Inject constructor(
    private val submissionDao: StationSubmissionDao
) : SubmissionRepository {

    override fun getAll(): Flow<List<StationSubmissionEntity>> =
        submissionDao.getAll()

    override fun getByStatus(status: String): Flow<List<StationSubmissionEntity>> =
        submissionDao.getByStatus(status)

    override fun getByUser(userId: String): Flow<List<StationSubmissionEntity>> =
        submissionDao.getByUser(userId)

    override suspend fun getById(id: Long): StationSubmissionEntity? =
        submissionDao.getById(id)

    override suspend fun submit(submission: StationSubmissionEntity): Long =
        submissionDao.insert(submission)

    override suspend fun update(submission: StationSubmissionEntity) =
        submissionDao.update(submission)

    override suspend fun updateStatus(id: Long, status: String, note: String) =
        submissionDao.updateStatus(id, status, note, System.currentTimeMillis())

    override suspend fun delete(id: Long) =
        submissionDao.delete(id)
}
