package com.utility.expensetracker.core.data.repository

import com.utility.expensetracker.core.data.mapper.toCategoryWithExpenseCountDomainModels
import com.utility.expensetracker.core.data.mapper.toDomainModel
import com.utility.expensetracker.core.data.mapper.toDomainModels
import com.utility.expensetracker.core.database.dao.CategoryDao
import com.utility.expensetracker.core.database.entities.CategoryEntity
import com.utility.expensetracker.core.domain.model.Category
import com.utility.expensetracker.core.domain.model.CategoryWithExpenseCount
import com.utility.expensetracker.core.domain.model.DatabaseError
import com.utility.expensetracker.core.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of CategoryRepository using Room database.
 *
 * This repository handles all category-related data operations with proper
 * business rule validation and error handling. It converts between database
 * entities and domain models to maintain clean architecture boundaries.
 */
@Singleton
class CategoryRepositoryImpl @Inject constructor(
    private val categoryDao: CategoryDao,
) : CategoryRepository {

    companion object {
        private const val MAX_CATEGORY_NAME_LENGTH = 50
        private const val MIN_CATEGORY_NAME_LENGTH = 1
    }

    override fun getAllCategories(): Flow<Result<List<Category>>> {
        return categoryDao.getAllCategories()
            .map<List<CategoryEntity>, Result<List<Category>>> { entities ->
                Result.success(entities.toDomainModels())
            }
            .catch { exception ->
                emit(Result.failure(DatabaseError.UnknownError("Failed to get categories", exception)))
            }
    }

    override suspend fun getCategoryById(categoryId: String): Result<Category> {
        return try {
            val entity = categoryDao.getCategoryById(categoryId)
            if (entity != null) {
                Result.success(entity.toDomainModel())
            } else {
                Result.failure(DatabaseError.CategoryNotFound(categoryId))
            }
        } catch (exception: Exception) {
            Result.failure(DatabaseError.UnknownError("Failed to get category", exception))
        }
    }

    override fun getCategoriesWithExpenseCount(): Flow<Result<List<CategoryWithExpenseCount>>> {
        return categoryDao.getCategoriesWithExpenseCount()
            .map { entities ->
                Result.success(entities.toCategoryWithExpenseCountDomainModels())
            }
            .catch { exception ->
                emit(Result.failure(DatabaseError.UnknownError("Failed to get categories with expense count", exception)))
            }
    }

    override suspend fun createCategory(name: String): Result<Category> {
        // Validate category name
        val validationResult = validateCategoryName(name)
        if (validationResult.isFailure) {
            return validationResult
        }

        return try {
            // Check for duplicate name
            val existingCount = categoryDao.countByName(name.trim())
            if (existingCount > 0) {
                return Result.failure(DatabaseError.CategoryNameAlreadyExists)
            }

            // Create new category
            val category = CategoryEntity(
                id = UUID.randomUUID().toString(),
                name = name.trim(),
                createdAt = System.currentTimeMillis(),
            )

            categoryDao.insertCategory(category)
            Result.success(category.toDomainModel())
        } catch (exception: Exception) {
            Result.failure(DatabaseError.UnknownError("Failed to create category", exception))
        }
    }

    override suspend fun updateCategory(categoryId: String, newName: String): Result<Category> {
        // Validate new category name
        val validationResult = validateCategoryName(newName)
        if (validationResult.isFailure) {
            return validationResult
        }

        return try {
            // Check if category exists
            val existingCategory = categoryDao.getCategoryById(categoryId)
                ?: return Result.failure(DatabaseError.CategoryNotFound(categoryId))

            // Check for duplicate name (excluding current category)
            val duplicateCount = categoryDao.countByNameExcluding(newName.trim(), categoryId)
            if (duplicateCount > 0) {
                return Result.failure(DatabaseError.CategoryNameAlreadyExists)
            }

            // Update category
            val updatedCategory = existingCategory.copy(name = newName.trim())
            categoryDao.updateCategory(updatedCategory)
            Result.success(updatedCategory.toDomainModel())
        } catch (exception: Exception) {
            Result.failure(DatabaseError.UnknownError("Failed to update category", exception))
        }
    }

    override suspend fun deleteCategory(categoryId: String): Result<Unit> {
        return try {
            // Check if category exists
            val category = categoryDao.getCategoryById(categoryId)
                ?: return Result.failure(DatabaseError.CategoryNotFound(categoryId))

            // Check if category has linked expenses
            val expenseCount = categoryDao.getExpenseCountForCategory(categoryId)
            if (expenseCount > 0) {
                return Result.failure(DatabaseError.CategoryHasLinkedExpenses(expenseCount))
            }

            // Delete category
            categoryDao.deleteCategory(categoryId)
            Result.success(Unit)
        } catch (exception: Exception) {
            Result.failure(DatabaseError.UnknownError("Failed to delete category", exception))
        }
    }

    override suspend fun categoryNameExists(name: String): Result<Boolean> {
        return try {
            val count = categoryDao.countByName(name.trim())
            Result.success(count > 0)
        } catch (exception: Exception) {
            Result.failure(DatabaseError.UnknownError("Failed to check category name", exception))
        }
    }

    override suspend fun categoryNameExistsExcluding(name: String, excludeCategoryId: String): Result<Boolean> {
        return try {
            val count = categoryDao.countByNameExcluding(name.trim(), excludeCategoryId)
            Result.success(count > 0)
        } catch (exception: Exception) {
            Result.failure(DatabaseError.UnknownError("Failed to check category name", exception))
        }
    }

    override fun getUnusedCategories(): Flow<Result<List<Category>>> {
        return categoryDao.getCategoriesWithoutExpenses()
            .map<List<CategoryEntity>, Result<List<Category>>> { entities ->
                Result.success(entities.toDomainModels())
            }
            .catch { exception ->
                emit(Result.failure(DatabaseError.UnknownError("Failed to get unused categories", exception)))
            }
    }

    /**
     * Validates category name according to business rules.
     *
     * @param name The category name to validate
     * @return Result indicating validation success or specific error
     */
    private fun validateCategoryName(name: String): Result<Category> {
        val trimmedName = name.trim()

        return when {
            trimmedName.isBlank() -> {
                Result.failure(DatabaseError.InvalidAmount("Category name cannot be empty"))
            }
            trimmedName.length < MIN_CATEGORY_NAME_LENGTH -> {
                Result.failure(DatabaseError.InvalidAmount("Category name is too short"))
            }
            trimmedName.length > MAX_CATEGORY_NAME_LENGTH -> {
                Result.failure(DatabaseError.InvalidAmount("Category name is too long (max $MAX_CATEGORY_NAME_LENGTH characters)"))
            }
            else -> {
                // Validation passed - we return a dummy success, the actual category will be created later
                // This is a bit of a hack but keeps the validation logic clean
                Result.success(Category("", trimmedName, 0))
            }
        }
    }
}