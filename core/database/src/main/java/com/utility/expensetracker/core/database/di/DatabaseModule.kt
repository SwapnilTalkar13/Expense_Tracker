package com.utility.expensetracker.core.database.di

import android.content.Context
import androidx.room.Room
import com.utility.expensetracker.core.database.ExpenseDatabase
import com.utility.expensetracker.core.database.dao.SimpleCategoryDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module providing database-related dependencies.
 *
 * This module provides the Room database instance and DAOs as singletons.
 * It also handles database creation callbacks for seeding default data.
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    /**
     * Provides the Room database instance.
     *
     * Configures the database with:
     * - Proper database name
     * - Schema export for migration testing
     * - Fallback strategy (none for production safety)
     *
     * @param context Application context for database creation
     * @return Configured ExpenseDatabase instance
     */
    @Provides
    @Singleton
    fun provideExpenseDatabase(
        @ApplicationContext context: Context,
    ): ExpenseDatabase {
        return Room.databaseBuilder(
            context = context,
            klass = ExpenseDatabase::class.java,
            name = ExpenseDatabase.DATABASE_NAME,
        ).build()
    }

    /**
     * Provides the SimpleCategoryDao for category-related database operations.
     *
     * @param database The ExpenseDatabase instance
     * @return SimpleCategoryDao instance
     */
    @Provides
    fun provideCategoryDao(database: ExpenseDatabase): SimpleCategoryDao {
        return database.categoryDao()
    }
}