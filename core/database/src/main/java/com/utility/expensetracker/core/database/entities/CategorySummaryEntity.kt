package com.utility.expensetracker.core.database.entities

/**
 * Entity for returning category expense summaries.
 *
 * Used for queries that aggregate expense totals by category.
 * This is not a Room entity itself but a data class for query results.
 *
 * @property categoryId Category unique identifier
 * @property totalAmount Total amount spent in this category (in cents)
 */
data class CategorySummaryEntity(
    val categoryId: String,
    val totalAmount: Long,
)