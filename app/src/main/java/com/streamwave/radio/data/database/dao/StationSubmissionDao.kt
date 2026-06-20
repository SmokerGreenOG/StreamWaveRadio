package com.streamwave.radio.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.streamwave.radio.data.database.entity.StationSubmissionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StationSubmissionDao {
    @Query("SELECT * FROM station_submissions ORDER BY submittedAt DESC")
    fun getAll(): Flow<List<StationSubmissionEntity>>

    @Query("SELECT * FROM station_submissions WHERE status = :status ORDER BY submittedAt DESC")
    fun getByStatus(status: String): Flow<List<StationSubmissionEntity>>

    @Query("SELECT * FROM station_submissions WHERE submittedBy = :userId ORDER BY submittedAt DESC")
    fun getByUser(userId: String): Flow<List<StationSubmissionEntity>>

    @Query("SELECT * FROM station_submissions WHERE id = :id")
    suspend fun getById(id: Long): StationSubmissionEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(submission: StationSubmissionEntity): Long

    @Update
    suspend fun update(submission: StationSubmissionEntity)

    @Query("UPDATE station_submissions SET status = :status, adminNote = :note, reviewedAt = :reviewedAt WHERE id = :id")
    suspend fun updateStatus(id: Long, status: String, note: String, reviewedAt: Long)

    @Query("DELETE FROM station_submissions WHERE id = :id")
    suspend fun delete(id: Long)
}
