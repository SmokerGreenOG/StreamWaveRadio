package com.streamwave.radio.data.repository

import com.streamwave.radio.data.database.entity.CategoryEntity
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    fun getAllCategories(): Flow<List<CategoryEntity>>
    suspend fun getCategoryById(id: Long): CategoryEntity?
    suspend fun insert(category: CategoryEntity): Long
    suspend fun insertAll(categories: List<CategoryEntity>)
    suspend fun update(category: CategoryEntity)
    suspend fun delete(id: Long)
}
