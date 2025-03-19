package com.anshok.subzy.domain.api

import com.anshok.subzy.data.local.db.entities.CategoryEntity
import kotlinx.coroutines.flow.Flow

interface CategoryInteractor {
    suspend fun insertCategory(category: CategoryEntity)
    suspend fun updateCategory(category: CategoryEntity)
    suspend fun deleteCategory(category: CategoryEntity)
    fun getAllCategories(): Flow<List<CategoryEntity>>
    suspend fun getCategoryById(id: Long): CategoryEntity?
}