package com.utility.expensetracker.core.domain.model

/**
 * Domain model representing a category with its expense count.
 *
 * Used in category management screens to show usage statistics.
 * Helps users understand which categories are actively used.
 *
 * @property category The category information
 * @property expenseCount Number of expenses in this category
 */
data class CategoryWithExpenseCount(
    val category: Category,
    val expenseCount: Int,
)