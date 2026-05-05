package com.utility.expensetracker.core.database.converters

import androidx.room.TypeConverter
import java.util.Date

/**
 * Room type converters for custom data types.
 *
 * These converters handle the transformation of complex data types
 * that Room doesn't natively support. Currently includes Date conversion
 * for potential future use cases.
 */
class Converters {

    /**
     * Convert Date to Long timestamp for database storage.
     *
     * @param date The Date to convert
     * @return Timestamp in milliseconds, or null if date is null
     */
    @TypeConverter
    fun fromDate(date: Date?): Long? {
        return date?.time
    }

    /**
     * Convert Long timestamp to Date for application use.
     *
     * @param timestamp The timestamp in milliseconds
     * @return Date object, or null if timestamp is null
     */
    @TypeConverter
    fun toDate(timestamp: Long?): Date? {
        return timestamp?.let { Date(it) }
    }
}