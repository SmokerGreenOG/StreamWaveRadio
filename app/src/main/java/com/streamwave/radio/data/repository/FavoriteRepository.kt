package com.streamwave.radio.data.repository

import com.streamwave.radio.data.database.entity.FavoriteEntity
import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {
    fun getAll(): Flow<List<FavoriteEntity>>
    fun getByType(type: String): Flow<List<FavoriteEntity>>
    suspend fun isFavorite(type: String, stationId: Long): Boolean
    suspend fun toggle(type: String, stationId: Long): Boolean
    suspend fun remove(type: String, stationId: Long)
    suspend fun updateOrder(id: Long, order: Int)
}
