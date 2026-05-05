package com.utility.expensetracker.core.domain.repository

import com.utility.expensetracker.core.domain.model.Category
import com.utility.expensetracker.core.domain.model.CategoryWithExpenseCount
import com.utility.expensetracker.core.domain.model.DatabaseError
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for category operations.
 *
 * Defines the contract for category data operations. This interface is implemented
 * in the data layer and used by use cases in the domain layer.
 *
 * All operations return Result<T> for proper error handling, and queries return
 * Flow<T> for reactive UI updates.
 */
interface CategoryRepository {

    /**
     * Get all categories.
     *
     * @return Flow of Result containing list of categories
     */
    fun getAllCategories(): Flow<Result<List<Category>>>

    /**
     * Get a specific category by ID.
     *
     * @param categoryId The category ID to search for
     * @return Result containing the category or error
     */
    suspend fun getCategoryById(categoryId: String): Result<Category>

    /**
     * Get all categories with their expense count.
     *
     * @return Flow of Result containing list of categories with expense counts
     */
    fun getCategoriesWithExpenseCount(): Flow<Result<List<CategoryWithExpenseCount>>>

    /**
     * Create a new category.
     *
     * Business rules applied:
     * - Category name must not be empty
     * - Category name must be unique
     * - Category name must not exceed maximum length
     *
     * @param name The category name
     * @return Result containing the created category or validation error
     */
    suspend fun createCategory(name: String): Result<Category>

    /**
     * Update an existing category.
     *
     * Business rules applied:
     * - Category must exist
     * - New name must not be empty
     * - New name must be unique (excluding current category)
     * - New name must not exceed maximum length
     *
     * @param categoryId The category ID to update
     * @param newName The new category name
     * @return Result indicating success or error
     */
    suspend fun updateCategory(categoryId: String, newName: String): Result<Category>

    /**
     * Delete a category.
     *
     * Business rules applied:
     * - Category must exist
     * - Category must not have linked expenses (foreign key constraint)
     *
     * @param categoryId The category ID to delete
     * @return Result indicating success or error with linked expense count
     */
    suspend fun deleteCategory(categoryId: String): Result<Unit>

    /**
     * Check if a category name already exists.
     *
     * @param name The category name to check
     * @return Result containing true if name exists, false otherwise
     */
    suspend fun categoryNameExists(name: String): Result<Boolean>

    /**
     * Check if a category name exists excluding a specific category.
     * Useful for update operations.
     *
     * @param name The category name to check
     * @param excludeCategoryId The category ID to exclude from the check
     * @return Result containing true if name exists elsewhere, false otherwise
     */
    suspend fun categoryNameExistsExcluding(name: String, excludeCategoryId: String): Result<Boolean>

    /**
     * Get categories that have no expenses.
     * Useful for determining which categories can be safely deleted.
     *
     * @return Flow of Result containing list of unused categories
     */
    fun getUnusedCategories(): Flow<Result<List<Category>>>
}