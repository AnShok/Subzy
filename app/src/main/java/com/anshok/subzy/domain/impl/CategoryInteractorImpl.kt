package com.anshok.subzy.domain.impl

import com.anshok.subzy.domain.api.CategoryInteractor
import com.anshok.subzy.domain.api.CategoryRepository
import com.anshok.subzy.data.local.db.entities.CategoryEntity
import kotlinx.coroutines.flow.Flow

class CategoryInteractorImpl(
    private val repository: CategoryRepository
) : CategoryInteractor {

    override suspend fun insertCategory(category: CategoryEntity) {
        repository.insertCategory(category)
    }

    override suspend fun updateCategory(category: CategoryEntity) {
        repository.updateCategory(category)
    }

    override suspend fun deleteCategory(category: CategoryEntity) {
        repository.deleteCategory(category)
    }

    override fun getAllCategories(): Flow<List<CategoryEntity>> {
        return repository.getAllCategories()
    }

    override suspend fun getCategoryById(id: Long): CategoryEntity? {
        return repository.getCategoryById(id)
    }
}