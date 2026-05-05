package com.utility.expensetracker.core.domain.model

import java.math.BigDecimal

/**
 * Domain model representing expense summaries by category.
 *
 * Used for generating spending reports and analytics.
 * Provides aggregated financial data per category.
 *
 * @property categoryId The category identifier
 * @property totalAmount Total amount spent in this category
 */
data class CategorySummary(
    val categoryId: String,
    val totalAmount: BigDecimal,
)