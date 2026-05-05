package com.utility.expensetracker.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.utility.expensetracker.core.database.entities.CategorySummaryEntity
import com.utility.expensetracker.core.database.entities.ExpenseEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for expense operations.
 *
 * Provides methods to interact with the expenses table using Room.
 * All query methods return Flow for reactive UI updates, and modification
 * methods are suspend functions for coroutine support.
 */
@Dao
interface ExpenseDao {

    /**
     * Get all expenses ordered by date (newest first).
     *
     * @return Flow of all expenses for reactive UI updates
     */
    @Query("SELECT * FROM expenses ORDER BY expenseDate DESC")
    fun getAllExpenses(): Flow<List<ExpenseEntity>>

    /**
     * Get a specific expense by its ID.
     *
     * @param expenseId The expense ID to search for
     * @return The expense entity or null if not found
     */
    @Query("SELECT * FROM expenses WHERE id = :expenseId")
    suspend fun getExpenseById(expenseId: String): ExpenseEntity?

    /**
     * Get expenses filtered by category, ordered by date (newest first).
     *
     * @param categoryId The category ID to filter by
     * @return Flow of expenses for the specified category
     */
    @Query("SELECT * FROM expenses WHERE categoryId = :categoryId ORDER BY expenseDate DESC")
    fun getExpensesByCategory(categoryId: String): Flow<List<ExpenseEntity>>

    /**
     * Get expenses within a date range, ordered by date (newest first).
     *
     * @param startDate Start of the date range (UTC timestamp)
     * @param endDate End of the date range (UTC timestamp)
     * @return Flow of expenses within the specified date range
     */
    @Query("SELECT * FROM expenses WHERE expenseDate BETWEEN :startDate AND :endDate ORDER BY expenseDate DESC")
    fun getExpensesByDateRange(startDate: Long, endDate: Long): Flow<List<ExpenseEntity>>

    /**
     * Get expenses for a specific category within a date range.
     *
     * @param categoryId The category ID to filter by
     * @param startDate Start of the date range (UTC timestamp)
     * @param endDate End of the date range (UTC timestamp)
     * @return Flow of expenses matching both category and date criteria
     */
    @Query("""
        SELECT * FROM expenses 
        WHERE categoryId = :categoryId 
        AND expenseDate BETWEEN :startDate AND :endDate 
        ORDER BY expenseDate DESC
    """)
    fun getExpensesByCategoryAndDateRange(
        categoryId: String,
        startDate: Long,
        endDate: Long,
    ): Flow<List<ExpenseEntity>>

    /**
     * Get recent expenses (last N expenses).
     *
     * @param limit Maximum number of expenses to return
     * @return Flow of recent expenses
     */
    @Query("SELECT * FROM expenses ORDER BY expenseDate DESC LIMIT :limit")
    fun getRecentExpenses(limit: Int): Flow<List<ExpenseEntity>>

    /**
     * Insert a new expense.
     *
     * @param expense The expense to insert
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpense(expense: ExpenseEntity)

    /**
     * Insert multiple expenses in a single transaction.
     *
     * @param expenses List of expenses to insert
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpenses(expenses: List<ExpenseEntity>)

    /**
     * Update an existing expense.
     *
     * @param expense The expense to update
     */
    @Update
    suspend fun updateExpense(expense: ExpenseEntity)

    /**
     * Delete an expense.
     *
     * @param expense The expense to delete
     */
    @Delete
    suspend fun deleteExpense(expense: ExpenseEntity)

    /**
     * Delete an expense by its ID.
     *
     * @param expenseId The ID of the expense to delete
     */
    @Query("DELETE FROM expenses WHERE id = :expenseId")
    suspend fun deleteExpenseById(expenseId: String)

    /**
     * Get total amount spent for a specific category.
     *
     * @param categoryId The category ID to sum expenses for
     * @return Total amount in cents, or null if no expenses exist for this category
     */
    @Query("SELECT SUM(amount) FROM expenses WHERE categoryId = :categoryId")
    suspend fun getTotalAmountByCategory(categoryId: String): Long?

    /**
     * Get total amount spent within a date range.
     *
     * @param startDate Start of the date range (UTC timestamp)
     * @param endDate End of the date range (UTC timestamp)
     * @return Total amount in cents, or null if no expenses exist in the range
     */
    @Query("SELECT SUM(amount) FROM expenses WHERE expenseDate BETWEEN :startDate AND :endDate")
    suspend fun getTotalAmountByDateRange(startDate: Long, endDate: Long): Long?

    /**
     * Get expense summaries grouped by category.
     * Useful for generating spending reports and charts.
     *
     * @return Flow of category summaries with total amounts
     */
    @Query("""
        SELECT categoryId, SUM(amount) as totalAmount 
        FROM expenses 
        GROUP BY categoryId
        ORDER BY totalAmount DESC
    """)
    fun getCategorySummaries(): Flow<List<CategorySummaryEntity>>

    /**
     * Get expense summaries for a date range grouped by category.
     *
     * @param startDate Start of the date range (UTC timestamp)
     * @param endDate End of the date range (UTC timestamp)
     * @return Flow of category summaries with total amounts for the specified period
     */
    @Query("""
        SELECT categoryId, SUM(amount) as totalAmount 
        FROM expenses 
        WHERE expenseDate BETWEEN :startDate AND :endDate
        GROUP BY categoryId
        ORDER BY totalAmount DESC
    """)
    fun getCategorySummariesByDateRange(startDate: Long, endDate: Long): Flow<List<CategorySummaryEntity>>

    /**
     * Get the count of expenses for each category.
     * Useful for understanding expense frequency by category.
     *
     * @return Flow of category IDs with their expense counts
     */
    @Query("""
        SELECT categoryId, COUNT(*) as totalAmount 
        FROM expenses 
        GROUP BY categoryId
        ORDER BY totalAmount DESC
    """)
    fun getExpenseCountsByCategory(): Flow<List<CategorySummaryEntity>>

    /**
     * Get total count of all expenses.
     *
     * @return Flow of the total expense count
     */
    @Query("SELECT COUNT(*) FROM expenses")
    fun getTotalExpenseCount(): Flow<Int>

    /**
     * Get count of expenses for a specific date range.
     *
     * @param startDate Start of the date range (UTC timestamp)
     * @param endDate End of the date range (UTC timestamp)
     * @return Count of expenses in the specified range
     */
    @Query("SELECT COUNT(*) FROM expenses WHERE expenseDate BETWEEN :startDate AND :endDate")
    suspend fun getExpenseCountByDateRange(startDate: Long, endDate: Long): Int

    /**
     * Delete all expenses for a specific category.
     * Useful when implementing category deletion with expense cleanup.
     *
     * @param categoryId The category ID to delete expenses for
     */
    @Query("DELETE FROM expenses WHERE categoryId = :categoryId")
    suspend fun deleteExpensesByCategory(categoryId: String)
}