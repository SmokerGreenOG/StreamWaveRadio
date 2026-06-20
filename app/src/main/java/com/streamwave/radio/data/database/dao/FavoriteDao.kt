package com.streamwave.radio.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.streamwave.radio.data.database.entity.FavoriteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM favorites ORDER BY sortOrder ASC, addedAt DESC")
    fun getAll(): Flow<List<FavoriteEntity>>

    @Query("SELECT * FROM favorites WHERE stationType = :type ORDER BY sortOrder ASC")
    fun getByType(type: String): Flow<List<FavoriteEntity>>

    @Query("SELECT * FROM favorites WHERE stationType = :type AND stationId = :stationId LIMIT 1")
    suspend fun getByStation(type: String, stationId: Long): FavoriteEntity?

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE stationType = :type AND stationId = :stationId)")
    suspend fun isFavorite(type: String, stationId: Long): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(favorite: FavoriteEntity): Long

    @Query("DELETE FROM favorites WHERE stationType = :type AND stationId = :stationId")
    suspend fun delete(type: String, stationId: Long)

    @Query("UPDATE favorites SET sortOrder = :order WHERE id = :id")
    suspend fun updateOrder(id: Long, order: Int)

    @Query("DELETE FROM favorites")
    suspend fun deleteAll()
}
