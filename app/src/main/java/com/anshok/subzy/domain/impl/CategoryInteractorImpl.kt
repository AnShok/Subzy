package com.anshok.subzy.domain.impl

import com.anshok.subzy.data.converters.DomainToEntityMapper
import com.anshok.subzy.data.converters.EntityToDomainMapper
import com.anshok.subzy.domain.api.CategoryInteractor
import com.anshok.subzy.domain.api.CategoryRepository
import com.anshok.subzy.domain.model.Category
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CategoryInteractorImpl(
    private val repository: CategoryRepository
) : CategoryInteractor {

    override suspend fun insertCategory(category: Category) {
        repository.insertCategory(DomainToEntityMapper.category(category))
    }

    override suspend fun updateCategory(category: Category) {
        repository.updateCategory(DomainToEntityMapper.category(category))
    }

    override suspend fun deleteCategory(category: Category) {
        repository.deleteCategory(DomainToEntityMapper.category(category))
    }

    override fun getAllCategories(): Flow<List<Category>> {
        return repository.getAllCategories().map { list ->
            list.map { EntityToDomainMapper.category(it) }
        }
    }

    override suspend fun getCategoryById(id: Long): Category? {
        return repository.getCategoryById(id)?.let { EntityToDomainMapper.category(it) }
    }
}
