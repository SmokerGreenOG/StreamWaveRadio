package com.streamwave.radio.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.streamwave.radio.data.database.entity.PersonalStationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PersonalStationDao {
    @Query("SELECT * FROM personal_stations ORDER BY name ASC")
    fun getAll(): Flow<List<PersonalStationEntity>>

    @Query("SELECT * FROM personal_stations WHERE userId = :userId ORDER BY name ASC")
    fun getByUserId(userId: String): Flow<List<PersonalStationEntity>>

    @Query("SELECT * FROM personal_stations WHERE id = :id")
    suspend fun getById(id: Long): PersonalStationEntity?

    @Query("SELECT * FROM personal_stations WHERE isFavorite = 1 ORDER BY name ASC")
    fun getFavorites(): Flow<List<PersonalStationEntity>>

    @Query("SELECT * FROM personal_stations WHERE submissionStatus = :status ORDER BY createdAt DESC")
    fun getBySubmissionStatus(status: String): Flow<List<PersonalStationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(station: PersonalStationEntity): Long

    @Update
    suspend fun update(station: PersonalStationEntity)

    @Query("DELETE FROM personal_stations WHERE id = :id")
    suspend fun delete(id: Long)

    @Query("UPDATE personal_stations SET isFavorite = :favorite WHERE id = :id")
    suspend fun setFavorite(id: Long, favorite: Boolean)

    @Query("UPDATE personal_stations SET submissionStatus = :status WHERE id = :id")
    suspend fun setSubmissionStatus(id: Long, status: String)

    @Query("UPDATE personal_stations SET lastPlayedAt = :timestamp WHERE id = :id")
    suspend fun updateLastPlayed(id: Long, timestamp: Long)

    @Query("SELECT * FROM personal_stations WHERE name LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%' ORDER BY name ASC")
    fun search(query: String): Flow<List<PersonalStationEntity>>
}
