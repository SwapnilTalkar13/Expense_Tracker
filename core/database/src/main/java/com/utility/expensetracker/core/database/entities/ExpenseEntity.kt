package com.utility.expensetracker.core.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

/**
 * Room entity for expenses.
 *
 * Represents individual expense transactions with category relationships.
 * Amounts are stored in cents (Long) to avoid floating-point precision issues.
 * Foreign key constraint prevents deletion of categories that have linked expenses.
 *
 * @property id Unique identifier for the expense
 * @property amount Expense amount in cents (Long to avoid Double precision issues)
 * @property categoryId Foreign key reference to CategoryEntity
 * @property expenseDate Date of the expense (UTC timestamp)
 * @property createdAt Timestamp when the expense was created (UTC milliseconds)
 * @property updatedAt Timestamp when the expense was last updated (UTC milliseconds)
 */
@Entity(
    tableName = "expenses",
    foreignKeys = [
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.RESTRICT, // Prevent category deletion with linked expenses
        ),
    ],
    indices = [
        Index(value = ["categoryId"]), // Optimize category-based queries
        Index(value = ["expenseDate"]), // Optimize date range queries
        Index(value = ["expenseDate", "categoryId"]), // Composite index for combined queries
    ],
)
data class ExpenseEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val amount: Long, // Amount in cents to avoid Double precision issues
    val categoryId: String,
    val expenseDate: Long, // UTC timestamp
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
)