package com.utility.expensetracker.feature.splash.domain

import com.utility.expensetracker.feature.splash.model.SplashConfig

/**
 * Repository interface for splash screen data and configuration
 *
 * This interface allows for future extension with remote configuration,
 * analytics tracking, or other data sources while keeping the
 * current implementation simple.
 */
interface SplashRepository {
    /**
     * Get the current splash configuration
     *
     * @return SplashConfig with current settings
     */
    suspend fun getSplashConfig(): SplashConfig

    /**
     * Track splash screen completion (for analytics)
     *
     * @param durationMs How long the splash was displayed
     */
    suspend fun trackSplashCompletion(durationMs: Long)

    /**
     * Check if splash should be shown (for cases where it might be skipped)
     *
     * @return true if splash should be displayed
     */
    suspend fun shouldShowSplash(): Boolean
}
