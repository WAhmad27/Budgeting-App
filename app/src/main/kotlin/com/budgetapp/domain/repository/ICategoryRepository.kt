package com.budgetapp.domain.repository

import com.budgetapp.domain.model.Category
import kotlinx.coroutines.flow.Flow

interface ICategoryRepository {
    fun getAllCategories(): Flow<List<Category>>
    suspend fun getCategoryById(id: Long): Category?
    suspend fun addCategory(category: Category): Long
    suspend fun updateCategory(category: Category)
    suspend fun deleteCategory(category: Category)
}
