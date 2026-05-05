package com.utility.expensetracker.core.common.extensions

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

/**
 * Common Flow extensions for the application
 */

/**
 * Maps exceptions to a Result wrapper
 */
inline fun <T> Flow<T>.asResult(): Flow<Result<T>> {
    return this
        .map<T, Result<T>> { Result.success(it) }
        .catch { emit(Result.failure(it)) }
}