package com.utility.expensetracker.core.domain.model

import java.math.BigDecimal

/**
 * Domain model representing an expense.
 *
 * This is the clean domain representation used throughout the business logic.
 * It's separate from database entities to maintain clean architecture boundaries.
 * 
 * Amount is represented as BigDecimal for precise financial calculations in the domain layer,
 * while the database stores it as Long (cents) for consistency and performance.
 *
 * @property id Unique identifier for the expense
 * @property amount Expense amount as BigDecimal for precise calculations
 * @property categoryId Foreign key reference to the category
 * @property expenseDate Date of the expense (UTC timestamp)
 * @property createdAt Timestamp when the expense was created (UTC milliseconds)
 * @property updatedAt Timestamp when the expense was last updated (UTC milliseconds)
 */
data class Expense(
    val id: String,
    val amount: BigDecimal,
    val categoryId: String,
    val expenseDate: Long,
    val createdAt: Long,
    val updatedAt: Long,
)