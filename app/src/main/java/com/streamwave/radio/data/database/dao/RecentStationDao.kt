package com.streamwave.radio.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.streamwave.radio.data.database.entity.RecentStationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecentStationDao {
    @Query("SELECT * FROM recent_stations GROUP BY stationType, stationId ORDER BY MAX(playedAt) DESC LIMIT :limit")
    fun getRecent(limit: Int = 20): Flow<List<RecentStationEntity>>

    @Query("SELECT * FROM recent_stations ORDER BY playedAt DESC LIMIT :limit")
    fun getAll(limit: Int = 50): Flow<List<RecentStationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(recent: RecentStationEntity): Long

    @Query("DELETE FROM recent_stations WHERE id = :id")
    suspend fun delete(id: Long)

    @Query("DELETE FROM recent_stations WHERE id IN (SELECT id FROM recent_stations ORDER BY playedAt ASC LIMIT :count)")
    suspend fun deleteOldest(count: Int)

    @Query("DELETE FROM recent_stations")
    suspend fun deleteAll()
}
