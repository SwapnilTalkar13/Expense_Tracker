package com.utility.expensetracker.core.database.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.utility.expensetracker.core.database.ExpenseDatabase
import com.utility.expensetracker.core.database.entities.CategoryEntity
import com.utility.expensetracker.core.database.entities.ExpenseEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

/**
 * Instrumented tests for CategoryDao.
 *
 * Tests all CRUD operations and business logic constraints using an in-memory database.
 * Follows the project's testing conventions with proper setup/teardown and coroutine testing.
 */
@RunWith(AndroidJUnit4::class)
class CategoryDaoTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: ExpenseDatabase
    private lateinit var categoryDao: CategoryDao
    private lateinit var expenseDao: ExpenseDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            ExpenseDatabase::class.java,
        ).allowMainThreadQueries().build()

        categoryDao = database.categoryDao()
        expenseDao = database.expenseDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertCategory_success() = runTest {
        // Given
        val category = createTestCategory(name = "Food")

        // When
        categoryDao.insertCategory(category)

        // Then
        val retrieved = categoryDao.getCategoryById(category.id)
        assertEquals(category, retrieved)
    }

    @Test
    fun getAllCategories_returnsOrderedByName() = runTest {
        // Given
        val categories = listOf(
            createTestCategory(id = "1", name = "Travel"),
            createTestCategory(id = "2", name = "Food"),
            createTestCategory(id = "3", name = "Shopping"),
        )

        categories.forEach { categoryDao.insertCategory(it) }

        // When
        val result = categoryDao.getAllCategories().first()

        // Then
        assertEquals(3, result.size)
        assertEquals("Food", result[0].name)
        assertEquals("Shopping", result[1].name)
        assertEquals("Travel", result[2].name)
    }

    @Test
    fun getCategoryById_existingCategory_returnsCategory() = runTest {
        // Given
        val category = createTestCategory(name = "Food")
        categoryDao.insertCategory(category)

        // When
        val result = categoryDao.getCategoryById(category.id)

        // Then
        assertEquals(category, result)
    }

    @Test
    fun getCategoryById_nonExistentCategory_returnsNull() = runTest {
        // When
        val result = categoryDao.getCategoryById("non-existent-id")

        // Then
        assertNull(result)
    }

    @Test
    fun countByName_existingName_returnsOne() = runTest {
        // Given
        val category = createTestCategory(name = "Food")
        categoryDao.insertCategory(category)

        // When
        val count = categoryDao.countByName("Food")

        // Then
        assertEquals(1, count)
    }

    @Test
    fun countByName_nonExistentName_returnsZero() = runTest {
        // When
        val count = categoryDao.countByName("NonExistent")

        // Then
        assertEquals(0, count)
    }

    @Test
    fun countByNameExcluding_excludesSpecifiedCategory() = runTest {
        // Given
        val category1 = createTestCategory(id = "1", name = "Food")
        val category2 = createTestCategory(id = "2", name = "Food")
        categoryDao.insertCategory(category1)
        categoryDao.insertCategory(category2)

        // When - excluding category1, should find category2
        val count = categoryDao.countByNameExcluding("Food", category1.id)

        // Then
        assertEquals(1, count)
    }

    @Test
    fun updateCategory_success() = runTest {
        // Given
        val category = createTestCategory(name = "Food")
        categoryDao.insertCategory(category)

        // When
        val updatedCategory = category.copy(name = "Food & Dining")
        categoryDao.updateCategory(updatedCategory)

        // Then
        val retrieved = categoryDao.getCategoryById(category.id)
        assertEquals("Food & Dining", retrieved?.name)
    }

    @Test
    fun deleteCategory_success() = runTest {
        // Given
        val category = createTestCategory(name = "Food")
        categoryDao.insertCategory(category)

        // When
        categoryDao.deleteCategory(category.id)

        // Then
        val retrieved = categoryDao.getCategoryById(category.id)
        assertNull(retrieved)
    }

    @Test
    fun getCategoriesWithExpenseCount_noExpenses_returnsZeroCount() = runTest {
        // Given
        val category = createTestCategory(name = "Food")
        categoryDao.insertCategory(category)

        // When
        val result = categoryDao.getCategoriesWithExpenseCount().first()

        // Then
        assertEquals(1, result.size)
        assertEquals(category.id, result[0].id)
        assertEquals("Food", result[0].name)
        assertEquals(0, result[0].expenseCount)
    }

    @Test
    fun getCategoriesWithExpenseCount_withExpenses_returnsCorrectCount() = runTest {
        // Given
        val category = createTestCategory(name = "Food")
        categoryDao.insertCategory(category)

        val expense1 = createTestExpense(categoryId = category.id)
        val expense2 = createTestExpense(categoryId = category.id)
        expenseDao.insertExpense(expense1)
        expenseDao.insertExpense(expense2)

        // When
        val result = categoryDao.getCategoriesWithExpenseCount().first()

        // Then
        assertEquals(1, result.size)
        assertEquals(category.id, result[0].id)
        assertEquals(2, result[0].expenseCount)
    }

    @Test
    fun getCategoriesWithoutExpenses_returnsOnlyUnusedCategories() = runTest {
        // Given
        val usedCategory = createTestCategory(id = "1", name = "Food")
        val unusedCategory = createTestCategory(id = "2", name = "Travel")
        categoryDao.insertCategory(usedCategory)
        categoryDao.insertCategory(unusedCategory)

        val expense = createTestExpense(categoryId = usedCategory.id)
        expenseDao.insertExpense(expense)

        // When
        val result = categoryDao.getCategoriesWithoutExpenses().first()

        // Then
        assertEquals(1, result.size)
        assertEquals(unusedCategory.id, result[0].id)
        assertEquals("Travel", result[0].name)
    }

    @Test
    fun getExpenseCountForCategory_withExpenses_returnsCorrectCount() = runTest {
        // Given
        val category = createTestCategory(name = "Food")
        categoryDao.insertCategory(category)

        val expense1 = createTestExpense(categoryId = category.id)
        val expense2 = createTestExpense(categoryId = category.id)
        expenseDao.insertExpense(expense1)
        expenseDao.insertExpense(expense2)

        // When
        val count = categoryDao.getExpenseCountForCategory(category.id)

        // Then
        assertEquals(2, count)
    }

    @Test
    fun getExpenseCountForCategory_noExpenses_returnsZero() = runTest {
        // Given
        val category = createTestCategory(name = "Food")
        categoryDao.insertCategory(category)

        // When
        val count = categoryDao.getExpenseCountForCategory(category.id)

        // Then
        assertEquals(0, count)
    }

    @Test
    fun uniqueConstraint_duplicateName_shouldFail() = runTest {
        // Given
        val category1 = createTestCategory(id = "1", name = "Food")
        val category2 = createTestCategory(id = "2", name = "Food") // Same name
        categoryDao.insertCategory(category1)

        // When & Then
        try {
            categoryDao.insertCategory(category2)
            // If we reach here, the test should fail
            assertTrue(false, "Expected unique constraint violation")
        } catch (e: Exception) {
            // Expected - unique constraint should prevent duplicate names
            assertTrue(true)
        }
    }

    // Helper functions

    private fun createTestCategory(
        id: String = "test-category-id",
        name: String = "Test Category",
        createdAt: Long = System.currentTimeMillis(),
    ): CategoryEntity {
        return CategoryEntity(
            id = id,
            name = name,
            createdAt = createdAt,
        )
    }

    private fun createTestExpense(
        id: String = "test-expense-id",
        amount: Long = 1000L, // $10.00 in cents
        categoryId: String,
        expenseDate: Long = System.currentTimeMillis(),
    ): ExpenseEntity {
        val now = System.currentTimeMillis()
        return ExpenseEntity(
            id = id,
            amount = amount,
            categoryId = categoryId,
            expenseDate = expenseDate,
            createdAt = now,
            updatedAt = now,
        )
    }
}