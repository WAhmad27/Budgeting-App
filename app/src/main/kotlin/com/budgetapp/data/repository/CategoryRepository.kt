package com.budgetapp.data.repository

import com.budgetapp.data.local.dao.CategoryDao
import com.budgetapp.data.mapper.toDomain
import com.budgetapp.data.mapper.toEntity
import com.budgetapp.domain.model.Category
import com.budgetapp.domain.repository.ICategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CategoryRepository @Inject constructor(
    private val dao: CategoryDao
) : ICategoryRepository {

    override fun getAllCategories(): Flow<List<Category>> =
        dao.getAllCategories().map { list -> list.map { it.toDomain() } }

    override suspend fun getCategoryById(id: Long): Category? =
        dao.getCategoryById(id)?.toDomain()

    override suspend fun addCategory(category: Category): Long =
        dao.insertCategory(category.toEntity())

    override suspend fun updateCategory(category: Category) =
        dao.updateCategory(category.toEntity())

    override suspend fun deleteCategory(category: Category) =
        dao.deleteCategory(category.toEntity())
}
