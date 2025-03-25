package com.anshok.subzy.domain.api

import com.anshok.subzy.domain.model.Category
import kotlinx.coroutines.flow.Flow

interface CategoryInteractor {
    suspend fun insertCategory(category: Category)
    suspend fun updateCategory(category: Category)
    suspend fun deleteCategory(category: Category)
    fun getAllCategories(): Flow<List<Category>>
    suspend fun getCategoryById(id: Long): Category?
}
