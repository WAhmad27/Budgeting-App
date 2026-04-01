package com.budgetapp.di

import android.content.Context
import androidx.room.Room
import androidx.sqlite.db.SupportSQLiteDatabase
import com.budgetapp.data.local.BudgetDatabase
import com.budgetapp.data.local.dao.*
import com.budgetapp.data.local.entity.CategoryEntity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext ctx: Context): BudgetDatabase {
        lateinit var db: BudgetDatabase
        db = Room.databaseBuilder(ctx, BudgetDatabase::class.java, BudgetDatabase.DATABASE_NAME)
            .addCallback(object : androidx.room.RoomDatabase.Callback() {
                override fun onCreate(sqLiteDatabase: SupportSQLiteDatabase) {
                    super.onCreate(sqLiteDatabase)
                    CoroutineScope(Dispatchers.IO).launch {
                        db.categoryDao().insertCategories(defaultCategories())
                    }
                }
            })
            .build()
        return db
    }

    @Provides fun provideIncomeDao(db: BudgetDatabase): IncomeDao           = db.incomeDao()
    @Provides fun provideExpenseDao(db: BudgetDatabase): ExpenseDao         = db.expenseDao()
    @Provides fun provideCategoryDao(db: BudgetDatabase): CategoryDao       = db.categoryDao()
    @Provides fun provideSavingsGoalDao(db: BudgetDatabase): SavingsGoalDao = db.savingsGoalDao()

    private fun defaultCategories() = listOf(
        CategoryEntity(name = "Food & Dining",   iconName = "restaurant",   colorHex = "#F44336", isDefault = true,  monthlyBudgetLimit = null),
        CategoryEntity(name = "Transport",        iconName = "directions_car",colorHex = "#2196F3", isDefault = true,  monthlyBudgetLimit = null),
        CategoryEntity(name = "Housing",          iconName = "home",         colorHex = "#4CAF50", isDefault = true,  monthlyBudgetLimit = null),
        CategoryEntity(name = "Healthcare",       iconName = "local_hospital",colorHex = "#FF5722", isDefault = true,  monthlyBudgetLimit = null),
        CategoryEntity(name = "Entertainment",    iconName = "movie",        colorHex = "#9C27B0", isDefault = true,  monthlyBudgetLimit = null),
        CategoryEntity(name = "Shopping",         iconName = "shopping_bag", colorHex = "#FF9800", isDefault = true,  monthlyBudgetLimit = null),
        CategoryEntity(name = "Utilities",        iconName = "bolt",         colorHex = "#009688", isDefault = true,  monthlyBudgetLimit = null),
        CategoryEntity(name = "Education",        iconName = "school",       colorHex = "#3F51B5", isDefault = true,  monthlyBudgetLimit = null),
        CategoryEntity(name = "Personal Care",    iconName = "spa",          colorHex = "#E91E63", isDefault = true,  monthlyBudgetLimit = null),
        CategoryEntity(name = "Other",            iconName = "more_horiz",   colorHex = "#607D8B", isDefault = true,  monthlyBudgetLimit = null)
    )
}
