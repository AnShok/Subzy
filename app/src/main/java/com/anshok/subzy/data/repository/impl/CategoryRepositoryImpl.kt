package com.anshok.subzy.data.repository.impl

import com.anshok.subzy.data.local.LocalDataSource
import com.anshok.subzy.domain.api.CategoryRepository
import com.anshok.subzy.data.local.db.entities.CategoryEntity
import kotlinx.coroutines.flow.Flow

class CategoryRepositoryImpl(
    private val localDataSource: LocalDataSource
) : CategoryRepository {

    override suspend fun insertCategory(category: CategoryEntity) {
        localDataSource.insertCategory(category)
    }

    override suspend fun updateCategory(category: CategoryEntity) {
        localDataSource.updateCategory(category)
    }

    override suspend fun deleteCategory(category: CategoryEntity) {
        localDataSource.deleteCategory(category)
    }

    override fun getAllCategories(): Flow<List<CategoryEntity>> {
        return localDataSource.getAllCategories()
    }

    override suspend fun getCategoryById(id: Long): CategoryEntity? {
        return localDataSource.getCategoryById(id)
    }
}
