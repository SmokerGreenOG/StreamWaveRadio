package com.streamwave.radio.data.repository

import com.streamwave.radio.data.database.dao.FavoriteDao
import com.streamwave.radio.data.database.entity.FavoriteEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoriteRepositoryImpl @Inject constructor(
    private val favoriteDao: FavoriteDao
) : FavoriteRepository {

    override fun getAll(): Flow<List<FavoriteEntity>> =
        favoriteDao.getAll()

    override fun getByType(type: String): Flow<List<FavoriteEntity>> =
        favoriteDao.getByType(type)

    override suspend fun isFavorite(type: String, stationId: Long): Boolean =
        favoriteDao.isFavorite(type, stationId)

    override suspend fun toggle(type: String, stationId: Long): Boolean {
        return if (favoriteDao.isFavorite(type, stationId)) {
            favoriteDao.delete(type, stationId)
            false  // nu verwijderd
        } else {
            favoriteDao.insert(
                FavoriteEntity(stationType = type, stationId = stationId, sortOrder = 0)
            )
            true  // nu favoriet
        }
    }

    override suspend fun remove(type: String, stationId: Long) =
        favoriteDao.delete(type, stationId)

    override suspend fun updateOrder(id: Long, order: Int) =
        favoriteDao.updateOrder(id, order)
}
