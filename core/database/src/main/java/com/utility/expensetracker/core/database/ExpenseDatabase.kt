package com.utility.expensetracker.core.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.utility.expensetracker.core.database.dao.SimpleCategoryDao
import com.utility.expensetracker.core.database.entities.CategoryEntity
import com.utility.expensetracker.core.database.entities.ExpenseEntity
import android.content.Context

/**
 * Room database for the ExpenseTracker application.
 *
 * This database contains all the expense and category data with proper relationships
 * and constraints. It includes default category seeding on first creation.
 *
 * Database features:
 * - Version 1 (initial release)
 * - Schema export enabled for migration testing
 * - Default category seeding via callback
 * - Foreign key constraints enabled
 */
@Database(
    entities = [
        CategoryEntity::class,
        ExpenseEntity::class,
    ],
    version = 1,
    exportSchema = true,
)
abstract class ExpenseDatabase : RoomDatabase() {

    /**
     * Provides access to category data operations.
     */
    abstract fun categoryDao(): SimpleCategoryDao

    companion object {
        const val DATABASE_NAME = "expense_database"

        /**
         * Default categories to be inserted on database creation.
         * These provide a good starting point for users.
         */
        private val defaultCategories = listOf(
            CategoryEntity(
                id = "category_food",
                name = "Food & Dining",
            ),
            CategoryEntity(
                id = "category_travel",
                name = "Travel & Transport",
            ),
            CategoryEntity(
                id = "category_shopping",
                name = "Shopping & Retail",
            ),
            CategoryEntity(
                id = "category_bills",
                name = "Bills & Utilities",
            ),
            CategoryEntity(
                id = "category_health",
                name = "Health & Medical",
            ),
            CategoryEntity(
                id = "category_entertainment",
                name = "Entertainment",
            ),
        )

        /**
         * Creates a database builder instance without callback for now.
         * Seeding will be handled by the repository layer.
         */
        fun createDatabaseBuilder(context: Context): RoomDatabase.Builder<ExpenseDatabase> {
            return Room.databaseBuilder(
                context = context,
                klass = ExpenseDatabase::class.java,
                name = DATABASE_NAME,
            )
        }

        /**
         * Gets the default categories list.
         * Useful for testing and reference.
         *
         * @return List of default category entities
         */
        fun getDefaultCategories(): List<CategoryEntity> = defaultCategories
    }
}