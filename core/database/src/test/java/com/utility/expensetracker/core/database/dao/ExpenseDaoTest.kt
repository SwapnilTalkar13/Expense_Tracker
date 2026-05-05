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

/**
 * Instrumented tests for ExpenseDao.
 *
 * Tests all CRUD operations, date filtering, category relationships, and aggregation queries
 * using an in-memory database. Follows the project's testing conventions.
 */
@RunWith(AndroidJUnit4::class)
class ExpenseDaoTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: ExpenseDatabase
    private lateinit var expenseDao: ExpenseDao
    private lateinit var categoryDao: CategoryDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            ExpenseDatabase::class.java,
        ).allowMainThreadQueries().build()

        expenseDao = database.expenseDao()
        categoryDao = database.categoryDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertExpense_success() = runTest {
        // Given
        val category = createTestCategory()
        categoryDao.insertCategory(category)

        val expense = createTestExpense(categoryId = category.id)

        // When
        expenseDao.insertExpense(expense)

        // Then
        val retrieved = expenseDao.getExpenseById(expense.id)
        assertEquals(expense, retrieved)
    }

    @Test
    fun getAllExpenses_returnsOrderedByDateDesc() = runTest {
        // Given
        val category = createTestCategory()
        categoryDao.insertCategory(category)

        val now = System.currentTimeMillis()
        val expenses = listOf(
            createTestExpense(id = "1", categoryId = category.id, expenseDate = now - 2000),
            createTestExpense(id = "2", categoryId = category.id, expenseDate = now), // Most recent
            createTestExpense(id = "3", categoryId = category.id, expenseDate = now - 1000),
        )

        expenses.forEach { expenseDao.insertExpense(it) }

        // When
        val result = expenseDao.getAllExpenses().first()

        // Then
        assertEquals(3, result.size)
        assertEquals("2", result[0].id) // Most recent first
        assertEquals("3", result[1].id)
        assertEquals("1", result[2].id) // Oldest last
    }

    @Test
    fun getExpenseById_existingExpense_returnsExpense() = runTest {
        // Given
        val category = createTestCategory()
        categoryDao.insertCategory(category)

        val expense = createTestExpense(categoryId = category.id)
        expenseDao.insertExpense(expense)

        // When
        val result = expenseDao.getExpenseById(expense.id)

        // Then
        assertEquals(expense, result)
    }

    @Test
    fun getExpenseById_nonExistentExpense_returnsNull() = runTest {
        // When
        val result = expenseDao.getExpenseById("non-existent-id")

        // Then
        assertNull(result)
    }

    @Test
    fun getExpensesByCategory_returnsOnlyMatchingCategory() = runTest {
        // Given
        val category1 = createTestCategory(id = "cat1", name = "Food")
        val category2 = createTestCategory(id = "cat2", name = "Travel")
        categoryDao.insertCategory(category1)
        categoryDao.insertCategory(category2)

        val expense1 = createTestExpense(id = "exp1", categoryId = category1.id)
        val expense2 = createTestExpense(id = "exp2", categoryId = category2.id)
        val expense3 = createTestExpense(id = "exp3", categoryId = category1.id)
        expenseDao.insertExpense(expense1)
        expenseDao.insertExpense(expense2)
        expenseDao.insertExpense(expense3)

        // When
        val result = expenseDao.getExpensesByCategory(category1.id).first()

        // Then
        assertEquals(2, result.size)
        assertEquals(setOf("exp1", "exp3"), result.map { it.id }.toSet())
    }

    @Test
    fun getExpensesByDateRange_returnsOnlyExpensesInRange() = runTest {
        // Given
        val category = createTestCategory()
        categoryDao.insertCategory(category)

        val baseTime = System.currentTimeMillis()
        val startDate = baseTime
        val endDate = baseTime + 10000

        val expenses = listOf(
            createTestExpense(id = "1", categoryId = category.id, expenseDate = baseTime - 1000), // Before range
            createTestExpense(id = "2", categoryId = category.id, expenseDate = baseTime + 5000), // In range
            createTestExpense(id = "3", categoryId = category.id, expenseDate = baseTime + 15000), // After range
            createTestExpense(id = "4", categoryId = category.id, expenseDate = startDate), // At start
            createTestExpense(id = "5", categoryId = category.id, expenseDate = endDate), // At end
        )

        expenses.forEach { expenseDao.insertExpense(it) }

        // When
        val result = expenseDao.getExpensesByDateRange(startDate, endDate).first()

        // Then
        assertEquals(3, result.size)
        assertEquals(setOf("2", "4", "5"), result.map { it.id }.toSet())
    }

    @Test
    fun getExpensesByCategoryAndDateRange_returnsFilteredResults() = runTest {
        // Given
        val category1 = createTestCategory(id = "cat1", name = "Food")
        val category2 = createTestCategory(id = "cat2", name = "Travel")
        categoryDao.insertCategory(category1)
        categoryDao.insertCategory(category2)

        val baseTime = System.currentTimeMillis()
        val startDate = baseTime
        val endDate = baseTime + 10000

        val expenses = listOf(
            createTestExpense(id = "1", categoryId = category1.id, expenseDate = baseTime + 5000), // Match both
            createTestExpense(id = "2", categoryId = category2.id, expenseDate = baseTime + 5000), // Wrong category
            createTestExpense(id = "3", categoryId = category1.id, expenseDate = baseTime - 1000), // Wrong date
            createTestExpense(id = "4", categoryId = category1.id, expenseDate = baseTime + 3000), // Match both
        )

        expenses.forEach { expenseDao.insertExpense(it) }

        // When
        val result = expenseDao.getExpensesByCategoryAndDateRange(category1.id, startDate, endDate).first()

        // Then
        assertEquals(2, result.size)
        assertEquals(setOf("1", "4"), result.map { it.id }.toSet())
    }

    @Test
    fun getRecentExpenses_returnsLimitedResults() = runTest {
        // Given
        val category = createTestCategory()
        categoryDao.insertCategory(category)

        val now = System.currentTimeMillis()
        val expenses = (1..5).map { i ->
            createTestExpense(id = i.toString(), categoryId = category.id, expenseDate = now - i * 1000)
        }

        expenses.forEach { expenseDao.insertExpense(it) }

        // When
        val result = expenseDao.getRecentExpenses(3).first()

        // Then
        assertEquals(3, result.size)
        assertEquals(listOf("1", "2", "3"), result.map { it.id })
    }

    @Test
    fun updateExpense_success() = runTest {
        // Given
        val category = createTestCategory()
        categoryDao.insertCategory(category)

        val expense = createTestExpense(categoryId = category.id, amount = 1000)
        expenseDao.insertExpense(expense)

        // When
        val updatedExpense = expense.copy(amount = 2000, updatedAt = System.currentTimeMillis())
        expenseDao.updateExpense(updatedExpense)

        // Then
        val retrieved = expenseDao.getExpenseById(expense.id)
        assertEquals(2000, retrieved?.amount)
    }

    @Test
    fun deleteExpense_success() = runTest {
        // Given
        val category = createTestCategory()
        categoryDao.insertCategory(category)

        val expense = createTestExpense(categoryId = category.id)
        expenseDao.insertExpense(expense)

        // When
        expenseDao.deleteExpense(expense)

        // Then
        val retrieved = expenseDao.getExpenseById(expense.id)
        assertNull(retrieved)
    }

    @Test
    fun deleteExpenseById_success() = runTest {
        // Given
        val category = createTestCategory()
        categoryDao.insertCategory(category)

        val expense = createTestExpense(categoryId = category.id)
        expenseDao.insertExpense(expense)

        // When
        expenseDao.deleteExpenseById(expense.id)

        // Then
        val retrieved = expenseDao.getExpenseById(expense.id)
        assertNull(retrieved)
    }

    @Test
    fun getTotalAmountByCategory_returnsCorrectSum() = runTest {
        // Given
        val category1 = createTestCategory(id = "cat1", name = "Food")
        val category2 = createTestCategory(id = "cat2", name = "Travel")
        categoryDao.insertCategory(category1)
        categoryDao.insertCategory(category2)

        val expenses = listOf(
            createTestExpense(categoryId = category1.id, amount = 1000),
            createTestExpense(categoryId = category1.id, amount = 2000),
            createTestExpense(categoryId = category2.id, amount = 5000), // Different category
        )

        expenses.forEach { expenseDao.insertExpense(it) }

        // When
        val total = expenseDao.getTotalAmountByCategory(category1.id)

        // Then
        assertEquals(3000L, total)
    }

    @Test
    fun getTotalAmountByCategory_noExpenses_returnsNull() = runTest {
        // Given
        val category = createTestCategory()
        categoryDao.insertCategory(category)

        // When
        val total = expenseDao.getTotalAmountByCategory(category.id)

        // Then
        assertNull(total)
    }

    @Test
    fun getTotalAmountByDateRange_returnsCorrectSum() = runTest {
        // Given
        val category = createTestCategory()
        categoryDao.insertCategory(category)

        val baseTime = System.currentTimeMillis()
        val startDate = baseTime
        val endDate = baseTime + 10000

        val expenses = listOf(
            createTestExpense(categoryId = category.id, amount = 1000, expenseDate = baseTime + 5000), // In range
            createTestExpense(categoryId = category.id, amount = 2000, expenseDate = baseTime + 3000), // In range
            createTestExpense(categoryId = category.id, amount = 5000, expenseDate = baseTime - 1000), // Out of range
        )

        expenses.forEach { expenseDao.insertExpense(it) }

        // When
        val total = expenseDao.getTotalAmountByDateRange(startDate, endDate)

        // Then
        assertEquals(3000L, total)
    }

    @Test
    fun getCategorySummaries_returnsOrderedByAmount() = runTest {
        // Given
        val category1 = createTestCategory(id = "cat1", name = "Food")
        val category2 = createTestCategory(id = "cat2", name = "Travel")
        categoryDao.insertCategory(category1)
        categoryDao.insertCategory(category2)

        val expenses = listOf(
            createTestExpense(categoryId = category1.id, amount = 1000),
            createTestExpense(categoryId = category1.id, amount = 2000), // Total: 3000
            createTestExpense(categoryId = category2.id, amount = 5000), // Total: 5000
        )

        expenses.forEach { expenseDao.insertExpense(it) }

        // When
        val summaries = expenseDao.getCategorySummaries().first()

        // Then
        assertEquals(2, summaries.size)
        assertEquals(category2.id, summaries[0].categoryId) // Highest amount first
        assertEquals(5000L, summaries[0].totalAmount)
        assertEquals(category1.id, summaries[1].categoryId)
        assertEquals(3000L, summaries[1].totalAmount)
    }

    @Test
    fun getTotalExpenseCount_returnsCorrectCount() = runTest {
        // Given
        val category = createTestCategory()
        categoryDao.insertCategory(category)

        val expenses = (1..3).map { i ->
            createTestExpense(id = i.toString(), categoryId = category.id)
        }

        expenses.forEach { expenseDao.insertExpense(it) }

        // When
        val count = expenseDao.getTotalExpenseCount().first()

        // Then
        assertEquals(3, count)
    }

    @Test
    fun getExpenseCountByDateRange_returnsCorrectCount() = runTest {
        // Given
        val category = createTestCategory()
        categoryDao.insertCategory(category)

        val baseTime = System.currentTimeMillis()
        val startDate = baseTime
        val endDate = baseTime + 10000

        val expenses = listOf(
            createTestExpense(id = "1", categoryId = category.id, expenseDate = baseTime + 5000), // In range
            createTestExpense(id = "2", categoryId = category.id, expenseDate = baseTime + 3000), // In range
            createTestExpense(id = "3", categoryId = category.id, expenseDate = baseTime - 1000), // Out of range
        )

        expenses.forEach { expenseDao.insertExpense(it) }

        // When
        val count = expenseDao.getExpenseCountByDateRange(startDate, endDate)

        // Then
        assertEquals(2, count)
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