package com.utility.expensetracker.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.utility.expensetracker.core.database.entities.CategoryEntity
import kotlinx.coroutines.flow.Flow

/**
 * Simplified CategoryDao for basic operations.
 * This version excludes complex queries that might cause KSP issues.
 */
@Dao
interface SimpleCategoryDao {

    @Query("SELECT * FROM categories ORDER BY name ASC")
    fun getAllCategories(): Flow<List<CategoryEntity>>

    @Query("SELECT * FROM categories WHERE id = :categoryId")
    suspend fun getCategoryById(categoryId: String): CategoryEntity?

    @Insert
    suspend fun insertCategory(category: CategoryEntity)

    @Query("SELECT COUNT(*) FROM categories WHERE name = :name")
    suspend fun countByName(name: String): Int
}