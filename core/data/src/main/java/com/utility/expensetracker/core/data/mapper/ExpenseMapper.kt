package com.utility.expensetracker.core.data.mapper

import com.utility.expensetracker.core.database.entities.CategorySummaryEntity
import com.utility.expensetracker.core.database.entities.ExpenseEntity
import com.utility.expensetracker.core.domain.model.CategorySummary
import com.utility.expensetracker.core.domain.model.Expense
import java.math.BigDecimal

/**
 * Mapper functions for converting between Expense entities and domain models.
 *
 * These extension functions handle the conversion between the database layer
 * entities and the domain layer models, including amount conversion between
 * Long (cents) and BigDecimal for precise financial calculations.
 */

/**
 * Convert ExpenseEntity to Expense domain model.
 * Converts amount from cents (Long) to BigDecimal.
 */
fun ExpenseEntity.toDomainModel(): Expense {
    return Expense(
        id = id,
        amount = BigDecimal(amount).divide(BigDecimal(100)), // Convert cents to decimal
        categoryId = categoryId,
        expenseDate = expenseDate,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )
}

/**
 * Convert Expense domain model to ExpenseEntity.
 * Converts amount from BigDecimal to cents (Long).
 */
fun Expense.toEntity(): ExpenseEntity {
    return ExpenseEntity(
        id = id,
        amount = (amount * BigDecimal(100)).toLong(), // Convert decimal to cents
        categoryId = categoryId,
        expenseDate = expenseDate,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )
}

/**
 * Convert list of ExpenseEntity to list of Expense domain models.
 */
fun List<ExpenseEntity>.toDomainModels(): List<Expense> {
    return map { it.toDomainModel() }
}

/**
 * Convert CategorySummaryEntity to CategorySummary domain model.
 * Converts totalAmount from cents (Long) to BigDecimal.
 */
fun CategorySummaryEntity.toDomainModel(): CategorySummary {
    return CategorySummary(
        categoryId = categoryId,
        totalAmount = BigDecimal(totalAmount).divide(BigDecimal(100)), // Convert cents to decimal
    )
}

/**
 * Convert list of CategorySummaryEntity to list of CategorySummary domain models.
 */
fun List<CategorySummaryEntity>.toCategorySummaryDomainModels(): List<CategorySummary> {
    return map { it.toDomainModel() }
}

/**
 * Convert BigDecimal amount to cents (Long) for database storage.
 */
fun BigDecimal.toCents(): Long {
    return (this * BigDecimal(100)).toLong()
}

/**
 * Convert cents (Long) to BigDecimal amount for domain use.
 */
fun Long.toBigDecimal(): BigDecimal {
    return BigDecimal(this).divide(BigDecimal(100))
}