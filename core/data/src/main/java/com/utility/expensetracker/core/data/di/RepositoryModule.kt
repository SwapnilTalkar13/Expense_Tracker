package com.utility.expensetracker.core.data.di

import com.utility.expensetracker.core.data.repository.CategoryRepositoryImpl
import com.utility.expensetracker.core.data.repository.ExpenseRepositoryImpl
import com.utility.expensetracker.core.domain.repository.CategoryRepository
import com.utility.expensetracker.core.domain.repository.ExpenseRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module providing repository implementations.
 *
 * This module binds repository interfaces to their concrete implementations.
 * Using @Binds is more efficient than @Provides for simple interface binding.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    /**
     * Binds CategoryRepository interface to CategoryRepositoryImpl.
     *
     * @param categoryRepositoryImpl The concrete implementation
     * @return CategoryRepository interface
     */
    @Binds
    @Singleton
    abstract fun bindCategoryRepository(
        categoryRepositoryImpl: CategoryRepositoryImpl,
    ): CategoryRepository

    /**
     * Binds ExpenseRepository interface to ExpenseRepositoryImpl.
     *
     * @param expenseRepositoryImpl The concrete implementation
     * @return ExpenseRepository interface
     */
    @Binds
    @Singleton
    abstract fun bindExpenseRepository(
        expenseRepositoryImpl: ExpenseRepositoryImpl,
    ): ExpenseRepository
}