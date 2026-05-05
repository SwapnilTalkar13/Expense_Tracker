package com.utility.expensetracker.core.data.mapper

import com.utility.expensetracker.core.database.entities.CategoryEntity
import com.utility.expensetracker.core.database.entities.CategoryWithExpenseCountEntity
import com.utility.expensetracker.core.domain.model.Category
import com.utility.expensetracker.core.domain.model.CategoryWithExpenseCount

/**
 * Mapper functions for converting between Category entities and domain models.
 *
 * These extension functions handle the conversion between the database layer
 * entities and the domain layer models, maintaining clean architecture boundaries.
 */

/**
 * Convert CategoryEntity to Category domain model.
 */
fun CategoryEntity.toDomainModel(): Category {
    return Category(
        id = id,
        name = name,
        createdAt = createdAt,
    )
}

/**
 * Convert Category domain model to CategoryEntity.
 */
fun Category.toEntity(): CategoryEntity {
    return CategoryEntity(
        id = id,
        name = name,
        createdAt = createdAt,
    )
}

/**
 * Convert list of CategoryEntity to list of Category domain models.
 */
fun List<CategoryEntity>.toDomainModels(): List<Category> {
    return map { it.toDomainModel() }
}

/**
 * Convert CategoryWithExpenseCountEntity to CategoryWithExpenseCount domain model.
 */
fun CategoryWithExpenseCountEntity.toDomainModel(): CategoryWithExpenseCount {
    return CategoryWithExpenseCount(
        category = Category(
            id = id,
            name = name,
            createdAt = createdAt,
        ),
        expenseCount = expenseCount,
    )
}

/**
 * Convert list of CategoryWithExpenseCountEntity to list of CategoryWithExpenseCount domain models.
 */
fun List<CategoryWithExpenseCountEntity>.toCategoryWithExpenseCountDomainModels(): List<CategoryWithExpenseCount> {
    return map { it.toDomainModel() }
}