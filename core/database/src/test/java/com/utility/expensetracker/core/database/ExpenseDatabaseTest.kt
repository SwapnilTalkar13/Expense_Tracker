package com.utility.expensetracker.core.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Integration tests for ExpenseDatabase.
 *
 * Tests database creation, schema validation, and default data seeding.
 * Ensures the database is properly configured and initialized.
 */
@RunWith(AndroidJUnit4::class)
class ExpenseDatabaseTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: ExpenseDatabase
    private val testScope = TestScope()

    @Before
    fun setup() {
        // Create database with callback for seeding
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            ExpenseDatabase::class.java,
        )
            .allowMainThreadQueries()
            .addCallback(ExpenseDatabase.createCallback(testScope))
            .build()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun databaseCreation_success() {
        // Database should be created successfully
        assertTrue(database.isOpen)
    }

    @Test
    fun daoAccess_success() {
        // Should be able to access DAOs
        val categoryDao = database.categoryDao()
        val expenseDao = database.expenseDao()

        assertTrue(categoryDao != null)
        assertTrue(expenseDao != null)
    }

    @Test
    fun defaultCategories_seededCorrectly() = runTest {
        // Wait a bit for the seeding callback to complete
        kotlinx.coroutines.delay(100)

        // When
        val categories = database.categoryDao().getAllCategories().first()
        val defaultCategories = ExpenseDatabase.getDefaultCategories()

        // Then
        assertEquals(defaultCategories.size, categories.size)

        val expectedNames = defaultCategories.map { it.name }.toSet()
        val actualNames = categories.map { it.name }.toSet()
        assertEquals(expectedNames, actualNames)

        // Verify specific default categories exist
        assertTrue(categories.any { it.name == "Food & Dining" })
        assertTrue(categories.any { it.name == "Travel & Transport" })
        assertTrue(categories.any { it.name == "Shopping & Retail" })
        assertTrue(categories.any { it.name == "Bills & Utilities" })
        assertTrue(categories.any { it.name == "Health & Medical" })
        assertTrue(categories.any { it.name == "Entertainment" })
    }

    @Test
    fun foreignKeyConstraints_enforced() = runTest {
        // Wait for seeding to complete
        kotlinx.coroutines.delay(100)

        val categories = database.categoryDao().getAllCategories().first()
        val testCategory = categories.first()

        // Create an expense with valid category
        val expense = com.utility.expensetracker.core.database.entities.ExpenseEntity(
            id = "test-expense",
            amount = 1000L,
            categoryId = testCategory.id,
            expenseDate = System.currentTimeMillis(),
        )

        database.expenseDao().insertExpense(expense)

        // Try to delete the category - should fail due to foreign key constraint
        try {
            database.categoryDao().deleteCategory(testCategory.id)
            // If we reach here, the constraint is not working
            assertTrue(false, "Expected foreign key constraint violation")
        } catch (e: Exception) {
            // Expected - foreign key constraint should prevent deletion
            assertTrue(true)
        }
    }

    @Test
    fun uniqueConstraints_enforced() = runTest {
        // Wait for seeding to complete
        kotlinx.coroutines.delay(100)

        // Try to insert a category with a name that already exists
        val duplicateCategory = com.utility.expensetracker.core.database.entities.CategoryEntity(
            id = "duplicate-id",
            name = "Food & Dining", // This should already exist from seeding
        )

        try {
            database.categoryDao().insertCategory(duplicateCategory)
            // If we reach here, the constraint is not working
            assertTrue(false, "Expected unique constraint violation")
        } catch (e: Exception) {
            // Expected - unique constraint should prevent duplicate names
            assertTrue(true)
        }
    }

    @Test
    fun getDefaultCategories_returnsExpectedList() {
        // When
        val defaultCategories = ExpenseDatabase.getDefaultCategories()

        // Then
        assertEquals(6, defaultCategories.size)

        val expectedCategories = mapOf(
            "category_food" to "Food & Dining",
            "category_travel" to "Travel & Transport",
            "category_shopping" to "Shopping & Retail",
            "category_bills" to "Bills & Utilities",
            "category_health" to "Health & Medical",
            "category_entertainment" to "Entertainment",
        )

        defaultCategories.forEach { category ->
            val expectedName = expectedCategories[category.id]
            assertTrue(expectedName != null, "Unexpected category ID: ${category.id}")
            assertEquals(expectedName, category.name)
        }
    }
}