package com.utility.expensetracker.core.domain.model

/**
 * Sealed class representing database operation errors.
 *
 * Provides type-safe error handling for database operations with
 * specific error types for different business rule violations.
 */
sealed class DatabaseError {
    /**
     * Category name already exists error.
     * Thrown when trying to create/update a category with a duplicate name.
     */
    data object CategoryNameAlreadyExists : DatabaseError()

    /**
     * Category has linked expenses error.
     * Thrown when trying to delete a category that still has expenses.
     */
    data class CategoryHasLinkedExpenses(val expenseCount: Int) : DatabaseError()

    /**
     * Invalid amount error.
     * Thrown when trying to create/update an expense with invalid amount.
     */
    data class InvalidAmount(val reason: String) : DatabaseError()

    /**
     * Category not found error.
     * Thrown when referencing a non-existent category.
     */
    data class CategoryNotFound(val categoryId: String) : DatabaseError()

    /**
     * Expense not found error.
     * Thrown when referencing a non-existent expense.
     */
    data class ExpenseNotFound(val expenseId: String) : DatabaseError()

    /**
     * Future date not allowed error.
     * Thrown when trying to create an expense with a future date.
     */
    data class FutureDateNotAllowed(val date: Long) : DatabaseError()

    /**
     * Generic database error.
     * Used for unexpected database exceptions.
     */
    data class UnknownError(val message: String, val cause: Throwable? = null) : DatabaseError()
}