package com.utility.expensetracker.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.utility.expensetracker.core.database.entities.CategoryEntity
import com.utility.expensetracker.core.database.entities.CategoryWithExpenseCountEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for category operations.
 *
 * Provides methods to interact with the categories table using Room.
 * All query methods return Flow for reactive UI updates, and modification
 * methods are suspend functions for coroutine support.
 */
@Dao
interface CategoryDao {

    /**
     * Get all categories ordered by name alphabetically.
     *
     * @return Flow of all categories for reactive UI updates
     */
    @Query("SELECT * FROM categories ORDER BY name ASC")
    fun getAllCategories(): Flow<List<CategoryEntity>>

    /**
     * Get a specific category by its ID.
     *
     * @param categoryId The category ID to search for
     * @return The category entity or null if not found
     */
    @Query("SELECT * FROM categories WHERE id = :categoryId")
    suspend fun getCategoryById(categoryId: String): CategoryEntity?

    /**
     * Check if a category name already exists.
     * Used for duplicate prevention during insert/update operations.
     *
     * @param name The category name to check
     * @return Count of categories with this name (0 or 1 due to unique constraint)
     */
    @Query("SELECT COUNT(*) FROM categories WHERE name = :name")
    suspend fun countByName(name: String): Int

    /**
     * Check if a category name exists excluding a specific category ID.
     * Used for duplicate prevention during update operations.
     *
     * @param name The category name to check
     * @param excludeId The category ID to exclude from the check
     * @return Count of categories with this name excluding the specified ID
     */
    @Query("SELECT COUNT(*) FROM categories WHERE name = :name AND id != :excludeId")
    suspend fun countByNameExcluding(name: String, excludeId: String): Int

    /**
     * Insert a new category.
     *
     * @param category The category to insert
     */
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertCategory(category: CategoryEntity)

    /**
     * Update an existing category.
     *
     * @param category The category to update
     */
    @Update
    suspend fun updateCategory(category: CategoryEntity)

    /**
     * Delete a category by its ID.
     * Note: This will fail if there are expenses linked to this category
     * due to the foreign key constraint with RESTRICT policy.
     *
     * @param categoryId The ID of the category to delete
     */
    @Query("DELETE FROM categories WHERE id = :categoryId")
    suspend fun deleteCategory(categoryId: String)

    /**
     * Get all categories with their expense count.
     * Useful for category management screens showing usage statistics.
     *
     * @return Flow of categories with expense counts for reactive UI updates
     */
    @Query("""
        SELECT c.id, c.name, c.createdAt, COUNT(e.id) as expenseCount 
        FROM categories c 
        LEFT JOIN expenses e ON c.id = e.categoryId 
        GROUP BY c.id, c.name, c.createdAt 
        ORDER BY c.name ASC
    """)
    fun getCategoriesWithExpenseCount(): Flow<List<CategoryWithExpenseCountEntity>>

    /**
     * Get categories that have no expenses linked to them.
     * Useful for safe deletion operations.
     *
     * @return Flow of categories with zero expenses
     */
    @Query("""
        SELECT c.id, c.name, c.createdAt 
        FROM categories c 
        LEFT JOIN expenses e ON c.id = e.categoryId 
        WHERE e.id IS NULL 
        ORDER BY c.name ASC
    """)
    fun getCategoriesWithoutExpenses(): Flow<List<CategoryEntity>>

    /**
     * Check if a category has any linked expenses.
     * Used to determine if a category can be safely deleted.
     *
     * @param categoryId The category ID to check
     * @return Count of expenses linked to this category
     */
    @Query("SELECT COUNT(*) FROM expenses WHERE categoryId = :categoryId")
    suspend fun getExpenseCountForCategory(categoryId: String): Int
}