package com.utility.expensetracker.core.domain.model

/**
 * Domain model representing an expense category.
 *
 * This is the clean domain representation used throughout the business logic.
 * It's separate from database entities to maintain clean architecture boundaries.
 *
 * @property id Unique identifier for the category
 * @property name Display name of the category
 * @property createdAt Timestamp when the category was created (UTC milliseconds)
 */
data class Category(
    val id: String,
    val name: String,
    val createdAt: Long,
)