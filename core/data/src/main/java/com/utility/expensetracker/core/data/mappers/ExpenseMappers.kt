package com.utility.expensetracker.core.data.mappers

import com.utility.expensetracker.core.database.entities.ExpenseEntity
import com.utility.expensetracker.core.domain.model.Expense

/**
 * Mappers for Expense entity and domain model conversion
 */

fun ExpenseEntity.toDomain(): Expense {
    return Expense(
        id = id,
        amount = amount,
        categoryId = categoryId,
        expenseDate = expenseDate,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun Expense.toEntity(): ExpenseEntity {
    return ExpenseEntity(
        id = id,
        amount = amount,
        categoryId = categoryId,
        expenseDate = expenseDate,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}