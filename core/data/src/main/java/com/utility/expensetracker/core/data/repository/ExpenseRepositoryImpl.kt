package com.utility.expensetracker.core.data.repository

import com.utility.expensetracker.core.data.mapper.toBigDecimal
import com.utility.expensetracker.core.data.mapper.toCategorySummaryDomainModels
import com.utility.expensetracker.core.data.mapper.toCents
import com.utility.expensetracker.core.data.mapper.toDomainModel
import com.utility.expensetracker.core.data.mapper.toDomainModels
import com.utility.expensetracker.core.database.dao.CategoryDao
import com.utility.expensetracker.core.database.dao.ExpenseDao
import com.utility.expensetracker.core.database.entities.ExpenseEntity
import com.utility.expensetracker.core.domain.model.CategorySummary
import com.utility.expensetracker.core.domain.model.DatabaseError
import com.utility.expensetracker.core.domain.model.Expense
import com.utility.expensetracker.core.domain.repository.ExpenseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.math.BigDecimal
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of ExpenseRepository using Room database.
 *
 * This repository handles all expense-related data operations with proper
 * business rule validation and error handling. It converts between database
 * entities and domain models to maintain clean architecture boundaries.
 */
@Singleton
class ExpenseRepositoryImpl @Inject constructor(
    private val expenseDao: ExpenseDao,
    private val categoryDao: CategoryDao,
) : ExpenseRepository {

    companion object {
        private val MIN_AMOUNT = BigDecimal("0.01") // 1 cent
        private val MAX_AMOUNT = BigDecimal("999999.99") // Reasonable maximum
    }

    override fun getAllExpenses(): Flow<Result<List<Expense>>> {
        return expenseDao.getAllExpenses()
            .map<List<ExpenseEntity>, Result<List<Expense>>> { entities ->
                Result.success(entities.toDomainModels())
            }
            .catch { exception ->
                emit(Result.failure(DatabaseError.UnknownError("Failed to get expenses", exception)))
            }
    }

    override suspend fun getExpenseById(expenseId: String): Result<Expense> {
        return try {
            val entity = expenseDao.getExpenseById(expenseId)
            if (entity != null) {
                Result.success(entity.toDomainModel())
            } else {
                Result.failure(DatabaseError.ExpenseNotFound(expenseId))
            }
        } catch (exception: Exception) {
            Result.failure(DatabaseError.UnknownError("Failed to get expense", exception))
        }
    }

    override fun getExpensesByCategory(categoryId: String): Flow<Result<List<Expense>>> {
        return expenseDao.getExpensesByCategory(categoryId)
            .map<List<ExpenseEntity>, Result<List<Expense>>> { entities ->
                Result.success(entities.toDomainModels())
            }
            .catch { exception ->
                emit(Result.failure(DatabaseError.UnknownError("Failed to get expenses by category", exception)))
            }
    }

    override fun getExpensesByDateRange(startDate: Long, endDate: Long): Flow<Result<List<Expense>>> {
        return expenseDao.getExpensesByDateRange(startDate, endDate)
            .map<List<ExpenseEntity>, Result<List<Expense>>> { entities ->
                Result.success(entities.toDomainModels())
            }
            .catch { exception ->
                emit(Result.failure(DatabaseError.UnknownError("Failed to get expenses by date range", exception)))
            }
    }

    override fun getExpensesByCategoryAndDateRange(
        categoryId: String,
        startDate: Long,
        endDate: Long,
    ): Flow<Result<List<Expense>>> {
        return expenseDao.getExpensesByCategoryAndDateRange(categoryId, startDate, endDate)
            .map<List<ExpenseEntity>, Result<List<Expense>>> { entities ->
                Result.success(entities.toDomainModels())
            }
            .catch { exception ->
                emit(Result.failure(DatabaseError.UnknownError("Failed to get expenses by category and date range", exception)))
            }
    }

    override fun getRecentExpenses(limit: Int): Flow<Result<List<Expense>>> {
        return expenseDao.getRecentExpenses(limit)
            .map<List<ExpenseEntity>, Result<List<Expense>>> { entities ->
                Result.success(entities.toDomainModels())
            }
            .catch { exception ->
                emit(Result.failure(DatabaseError.UnknownError("Failed to get recent expenses", exception)))
            }
    }

    override suspend fun createExpense(
        amount: BigDecimal,
        categoryId: String,
        expenseDate: Long,
    ): Result<Expense> {
        // Validate amount
        val amountValidationResult = validateAmount(amount)
        if (amountValidationResult.isFailure) {
            return amountValidationResult
        }

        // Validate date
        val dateValidationResult = validateExpenseDate(expenseDate)
        if (dateValidationResult.isFailure) {
            return dateValidationResult
        }

        return try {
            // Check if category exists
            val category = categoryDao.getCategoryById(categoryId)
                ?: return Result.failure(DatabaseError.CategoryNotFound(categoryId))

            // Create new expense
            val now = System.currentTimeMillis()
            val expense = ExpenseEntity(
                id = UUID.randomUUID().toString(),
                amount = amount.toCents(),
                categoryId = categoryId,
                expenseDate = expenseDate,
                createdAt = now,
                updatedAt = now,
            )

            expenseDao.insertExpense(expense)
            Result.success(expense.toDomainModel())
        } catch (exception: Exception) {
            Result.failure(DatabaseError.UnknownError("Failed to create expense", exception))
        }
    }

    override suspend fun updateExpense(
        expenseId: String,
        amount: BigDecimal?,
        categoryId: String?,
        expenseDate: Long?,
    ): Result<Expense> {
        return try {
            // Check if expense exists
            val existingExpense = expenseDao.getExpenseById(expenseId)
                ?: return Result.failure(DatabaseError.ExpenseNotFound(expenseId))

            // Validate amount if provided
            amount?.let { newAmount ->
                val validationResult = validateAmount(newAmount)
                if (validationResult.isFailure) {
                    return validationResult
                }
            }

            // Validate date if provided
            expenseDate?.let { newDate ->
                val validationResult = validateExpenseDate(newDate)
                if (validationResult.isFailure) {
                    return validationResult
                }
            }

            // Validate category if provided
            categoryId?.let { newCategoryId ->
                val category = categoryDao.getCategoryById(newCategoryId)
                    ?: return Result.failure(DatabaseError.CategoryNotFound(newCategoryId))
            }

            // Update expense with provided values
            val updatedExpense = existingExpense.copy(
                amount = amount?.toCents() ?: existingExpense.amount,
                categoryId = categoryId ?: existingExpense.categoryId,
                expenseDate = expenseDate ?: existingExpense.expenseDate,
                updatedAt = System.currentTimeMillis(),
            )

            expenseDao.updateExpense(updatedExpense)
            Result.success(updatedExpense.toDomainModel())
        } catch (exception: Exception) {
            Result.failure(DatabaseError.UnknownError("Failed to update expense", exception))
        }
    }

    override suspend fun deleteExpense(expenseId: String): Result<Unit> {
        return try {
            // Check if expense exists
            val expense = expenseDao.getExpenseById(expenseId)
                ?: return Result.failure(DatabaseError.ExpenseNotFound(expenseId))

            expenseDao.deleteExpenseById(expenseId)
            Result.success(Unit)
        } catch (exception: Exception) {
            Result.failure(DatabaseError.UnknownError("Failed to delete expense", exception))
        }
    }

    override suspend fun getTotalAmountByCategory(categoryId: String): Result<BigDecimal> {
        return try {
            val totalCents = expenseDao.getTotalAmountByCategory(categoryId) ?: 0L
            Result.success(totalCents.toBigDecimal())
        } catch (exception: Exception) {
            Result.failure(DatabaseError.UnknownError("Failed to get total amount by category", exception))
        }
    }

    override suspend fun getTotalAmountByDateRange(startDate: Long, endDate: Long): Result<BigDecimal> {
        return try {
            val totalCents = expenseDao.getTotalAmountByDateRange(startDate, endDate) ?: 0L
            Result.success(totalCents.toBigDecimal())
        } catch (exception: Exception) {
            Result.failure(DatabaseError.UnknownError("Failed to get total amount by date range", exception))
        }
    }

    override fun getCategorySummaries(): Flow<Result<List<CategorySummary>>> {
        return expenseDao.getCategorySummaries()
            .map { entities ->
                Result.success(entities.toCategorySummaryDomainModels())
            }
            .catch { exception ->
                emit(Result.failure(DatabaseError.UnknownError("Failed to get category summaries", exception)))
            }
    }

    override fun getCategorySummariesByDateRange(startDate: Long, endDate: Long): Flow<Result<List<CategorySummary>>> {
        return expenseDao.getCategorySummariesByDateRange(startDate, endDate)
            .map { entities ->
                Result.success(entities.toCategorySummaryDomainModels())
            }
            .catch { exception ->
                emit(Result.failure(DatabaseError.UnknownError("Failed to get category summaries by date range", exception)))
            }
    }

    override fun getTotalExpenseCount(): Flow<Result<Int>> {
        return expenseDao.getTotalExpenseCount()
            .map<Int, Result<Int>> { count ->
                Result.success(count)
            }
            .catch { exception ->
                emit(Result.failure(DatabaseError.UnknownError("Failed to get total expense count", exception)))
            }
    }

    override suspend fun getExpenseCountByDateRange(startDate: Long, endDate: Long): Result<Int> {
        return try {
            val count = expenseDao.getExpenseCountByDateRange(startDate, endDate)
            Result.success(count)
        } catch (exception: Exception) {
            Result.failure(DatabaseError.UnknownError("Failed to get expense count by date range", exception))
        }
    }

    /**
     * Validates expense amount according to business rules.
     *
     * @param amount The amount to validate
     * @return Result indicating validation success or specific error
     */
    private fun validateAmount(amount: BigDecimal): Result<Expense> {
        return when {
            amount <= BigDecimal.ZERO -> {
                Result.failure(DatabaseError.InvalidAmount("Amount must be greater than zero"))
            }
            amount < MIN_AMOUNT -> {
                Result.failure(DatabaseError.InvalidAmount("Amount must be at least $MIN_AMOUNT"))
            }
            amount > MAX_AMOUNT -> {
                Result.failure(DatabaseError.InvalidAmount("Amount cannot exceed $MAX_AMOUNT"))
            }
            amount.scale() > 2 -> {
                Result.failure(DatabaseError.InvalidAmount("Amount cannot have more than 2 decimal places"))
            }
            else -> {
                // Validation passed - return dummy success
                Result.success(Expense("", amount, "", 0, 0, 0))
            }
        }
    }

    /**
     * Validates expense date according to business rules.
     *
     * @param expenseDate The expense date to validate (UTC timestamp)
     * @return Result indicating validation success or specific error
     */
    private fun validateExpenseDate(expenseDate: Long): Result<Expense> {
        val now = System.currentTimeMillis()
        val maxFutureDate = now + (24 * 60 * 60 * 1000) // Allow up to 24 hours in the future

        return when {
            expenseDate > maxFutureDate -> {
                Result.failure(DatabaseError.FutureDateNotAllowed(expenseDate))
            }
            expenseDate < 0 -> {
                Result.failure(DatabaseError.InvalidAmount("Invalid expense date"))
            }
            else -> {
                // Validation passed - return dummy success
                Result.success(Expense("", BigDecimal.ZERO, "", expenseDate, 0, 0))
            }
        }
    }
}