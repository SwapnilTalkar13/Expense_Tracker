package com.utility.expensetracker.core.database.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

/**
 * Room entity for expense categories.
 *
 * Represents categories that expenses can be assigned to. Categories have unique names
 * to prevent duplicates and maintain data consistency.
 *
 * @property id Unique identifier for the category
 * @property name Display name of the category (must be unique)
 * @property createdAt Timestamp when the category was created (UTC milliseconds)
 */
@Entity(
    tableName = "categories",
    indices = [
        Index(
            value = ["name"],
            unique = true,
        ),
    ],
)
data class CategoryEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val createdAt: Long = System.currentTimeMillis(),
)