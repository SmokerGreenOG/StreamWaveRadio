package com.streamwave.radio.data.repository

import com.streamwave.radio.data.database.dao.CategoryDao
import com.streamwave.radio.data.database.entity.CategoryEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoryRepositoryImpl @Inject constructor(
    private val categoryDao: CategoryDao
) : CategoryRepository {

    override fun getAllCategories(): Flow<List<CategoryEntity>> =
        categoryDao.getAll()

    override suspend fun getCategoryById(id: Long): CategoryEntity? =
        categoryDao.getById(id)

    override suspend fun insert(category: CategoryEntity): Long =
        categoryDao.insert(category)

    override suspend fun insertAll(categories: List<CategoryEntity>) =
        categoryDao.insertAll(categories)

    override suspend fun update(category: CategoryEntity) =
        categoryDao.update(category)

    override suspend fun delete(id: Long) =
        categoryDao.delete(id)
}
