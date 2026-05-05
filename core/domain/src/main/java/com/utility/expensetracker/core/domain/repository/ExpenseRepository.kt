package com.utility.expensetracker.core.domain.repository

import com.utility.expensetracker.core.domain.model.CategorySummary
import com.utility.expensetracker.core.domain.model.DatabaseError
import com.utility.expensetracker.core.domain.model.Expense
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal

/**
 * Repository interface for expense operations.
 *
 * Defines the contract for expense data operations. This interface is implemented
 * in the data layer and used by use cases in the domain layer.
 *
 * All operations return Result<T> for proper error handling, and queries return
 * Flow<T> for reactive UI updates.
 */
interface ExpenseRepository {

    /**
     * Get all expenses ordered by date (newest first).
     *
     * @return Flow of Result containing list of expenses
     */
    fun getAllExpenses(): Flow<Result<List<Expense>>>

    /**
     * Get a specific expense by ID.
     *
     * @param expenseId The expense ID to search for
     * @return Result containing the expense or error
     */
    suspend fun getExpenseById(expenseId: String): Result<Expense>

    /**
     * Get expenses filtered by category.
     *
     * @param categoryId The category ID to filter by
     * @return Flow of Result containing list of expenses for the category
     */
    fun getExpensesByCategory(categoryId: String): Flow<Result<List<Expense>>>

    /**
     * Get expenses within a date range.
     *
     * @param startDate Start of the date range (UTC timestamp)
     * @param endDate End of the date range (UTC timestamp)
     * @return Flow of Result containing list of expenses in the range
     */
    fun getExpensesByDateRange(startDate: Long, endDate: Long): Flow<Result<List<Expense>>>

    /**
     * Get expenses for a category within a date range.
     *
     * @param categoryId The category ID to filter by
     * @param startDate Start of the date range (UTC timestamp)
     * @param endDate End of the date range (UTC timestamp)
     * @return Flow of Result containing list of filtered expenses
     */
    fun getExpensesByCategoryAndDateRange(
        categoryId: String,
        startDate: Long,
        endDate: Long,
    ): Flow<Result<List<Expense>>>

    /**
     * Get recent expenses.
     *
     * @param limit Maximum number of expenses to return
     * @return Flow of Result containing list of recent expenses
     */
    fun getRecentExpenses(limit: Int): Flow<Result<List<Expense>>>

    /**
     * Create a new expense.
     *
     * Business rules applied:
     * - Amount must be greater than zero
     * - Category must exist
     * - Date must not be in the future (configurable)
     * - Amount must be within reasonable bounds
     *
     * @param amount The expense amount (must be > 0)
     * @param categoryId The category ID (must exist)
     * @param expenseDate The expense date (UTC timestamp)
     * @return Result containing the created expense or validation error
     */
    suspend fun createExpense(
        amount: BigDecimal,
        categoryId: String,
        expenseDate: Long,
    ): Result<Expense>

    /**
     * Update an existing expense.
     *
     * Business rules applied:
     * - Expense must exist
     * - Amount must be greater than zero (if provided)
     * - Category must exist (if provided)
     * - Date must not be in the future (if provided)
     *
     * @param expenseId The expense ID to update
     * @param amount The new amount (optional)
     * @param categoryId The new category ID (optional)
     * @param expenseDate The new expense date (optional)
     * @return Result containing the updated expense or validation error
     */
    suspend fun updateExpense(
        expenseId: String,
        amount: BigDecimal? = null,
        categoryId: String? = null,
        expenseDate: Long? = null,
    ): Result<Expense>

    /**
     * Delete an expense.
     *
     * @param expenseId The expense ID to delete
     * @return Result indicating success or error
     */
    suspend fun deleteExpense(expenseId: String): Result<Unit>

    /**
     * Get total amount spent for a category.
     *
     * @param categoryId The category ID to sum expenses for
     * @return Result containing the total amount or zero if no expenses
     */
    suspend fun getTotalAmountByCategory(categoryId: String): Result<BigDecimal>

    /**
     * Get total amount spent within a date range.
     *
     * @param startDate Start of the date range (UTC timestamp)
     * @param endDate End of the date range (UTC timestamp)
     * @return Result containing the total amount or zero if no expenses
     */
    suspend fun getTotalAmountByDateRange(startDate: Long, endDate: Long): Result<BigDecimal>

    /**
     * Get expense summaries grouped by category.
     *
     * @return Flow of Result containing list of category summaries
     */
    fun getCategorySummaries(): Flow<Result<List<CategorySummary>>>

    /**
     * Get expense summaries for a date range grouped by category.
     *
     * @param startDate Start of the date range (UTC timestamp)
     * @param endDate End of the date range (UTC timestamp)
     * @return Flow of Result containing list of category summaries for the period
     */
    fun getCategorySummariesByDateRange(startDate: Long, endDate: Long): Flow<Result<List<CategorySummary>>>

    /**
     * Get total count of all expenses.
     *
     * @return Flow of Result containing the total expense count
     */
    fun getTotalExpenseCount(): Flow<Result<Int>>

    /**
     * Get count of expenses for a date range.
     *
     * @param startDate Start of the date range (UTC timestamp)
     * @param endDate End of the date range (UTC timestamp)
     * @return Result containing the expense count for the range
     */
    suspend fun getExpenseCountByDateRange(startDate: Long, endDate: Long): Result<Int>
}