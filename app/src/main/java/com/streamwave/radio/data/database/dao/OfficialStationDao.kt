package com.streamwave.radio.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.streamwave.radio.data.database.entity.OfficialStationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface OfficialStationDao {
    @Query("SELECT * FROM official_stations WHERE isActive = 1 ORDER BY sortOrder ASC, name ASC")
    fun getAllActive(): Flow<List<OfficialStationEntity>>

    @Query("SELECT * FROM official_stations ORDER BY sortOrder ASC, name ASC")
    fun getAll(): Flow<List<OfficialStationEntity>>

    @Query("SELECT * FROM official_stations WHERE id = :id")
    suspend fun getById(id: Long): OfficialStationEntity?

    @Query("SELECT * FROM official_stations WHERE isFeatured = 1 AND isActive = 1 ORDER BY sortOrder ASC")
    fun getFeatured(): Flow<List<OfficialStationEntity>>

    @Query("SELECT * FROM official_stations WHERE categoryId = :categoryId AND isActive = 1 ORDER BY sortOrder ASC, name ASC")
    fun getByCategory(categoryId: Long): Flow<List<OfficialStationEntity>>

    @Query("SELECT * FROM official_stations WHERE isActive = 1 AND (name LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%' OR country LIKE '%' || :query || '%' OR language LIKE '%' || :query || '%') ORDER BY sortOrder ASC, name ASC")
    fun search(query: String): Flow<List<OfficialStationEntity>>

    @Query("SELECT * FROM official_stations WHERE isActive = 1 AND country = :country ORDER BY sortOrder ASC, name ASC")
    fun getByCountry(country: String): Flow<List<OfficialStationEntity>>

    @Query("SELECT * FROM official_stations WHERE isActive = 1 AND language = :language ORDER BY sortOrder ASC, name ASC")
    fun getByLanguage(language: String): Flow<List<OfficialStationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(station: OfficialStationEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(stations: List<OfficialStationEntity>)

    @Update
    suspend fun update(station: OfficialStationEntity)

    @Query("DELETE FROM official_stations WHERE id = :id")
    suspend fun delete(id: Long)

    @Query("UPDATE official_stations SET isActive = :active WHERE id = :id")
    suspend fun setActive(id: Long, active: Boolean)

    @Query("UPDATE official_stations SET streamStatus = :status, lastCheckedAt = :checkedAt WHERE id = :id")
    suspend fun updateStreamStatus(id: Long, status: String, checkedAt: Long)
}
