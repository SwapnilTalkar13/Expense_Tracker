package com.utility.expensetracker.core.data.mappers

import com.utility.expensetracker.core.database.entities.CategoryEntity
import com.utility.expensetracker.core.domain.model.Category

/**
 * Mappers for Category entity and domain model conversion
 */

fun CategoryEntity.toDomain(): Category {
    return Category(
        id = id,
        name = name,
        createdAt = createdAt
    )
}

fun Category.toEntity(): CategoryEntity {
    return CategoryEntity(
        id = id,
        name = name,
        createdAt = createdAt
    )
}