package com.budgetapp.di

import com.budgetapp.data.repository.*
import com.budgetapp.domain.repository.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds @Singleton
    abstract fun bindIncomeRepo(impl: IncomeRepository): IIncomeRepository

    @Binds @Singleton
    abstract fun bindCategoryRepo(impl: CategoryRepository): ICategoryRepository

    @Binds @Singleton
    abstract fun bindExpenseRepo(impl: ExpenseRepository): IExpenseRepository

    @Binds @Singleton
    abstract fun bindSavingsGoalRepo(impl: SavingsGoalRepository): ISavingsGoalRepository
}
