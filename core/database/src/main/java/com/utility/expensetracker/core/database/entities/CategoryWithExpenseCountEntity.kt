package com.utility.expensetracker.core.database.entities

/**
 * Entity for returning category data with expense count.
 *
 * Used for queries that join categories with their expense count.
 * This is not a Room entity itself but a data class for query results.
 *
 * @property id Category unique identifier
 * @property name Category display name
 * @property createdAt Category creation timestamp
 * @property expenseCount Number of expenses in this category
 */
data class CategoryWithExpenseCountEntity(
    val id: String,
    val name: String,
    val createdAt: Long,
    val expenseCount: Int,
)