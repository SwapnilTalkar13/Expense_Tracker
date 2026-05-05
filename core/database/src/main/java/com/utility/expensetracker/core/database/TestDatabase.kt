package com.utility.expensetracker.core.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.utility.expensetracker.core.database.dao.SimpleCategoryDao
import com.utility.expensetracker.core.database.entities.CategoryEntity
import android.content.Context

/**
 * Minimal test database to isolate KSP compilation issues.
 */
@Database(
    entities = [CategoryEntity::class],
    version = 1,
    exportSchema = true,
)
abstract class TestDatabase : RoomDatabase() {
    abstract fun categoryDao(): SimpleCategoryDao

    companion object {
        const val DATABASE_NAME = "test_database"
    }
}