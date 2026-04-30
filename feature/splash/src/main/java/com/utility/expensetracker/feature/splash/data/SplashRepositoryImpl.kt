package com.utility.expensetracker.feature.splash.data

import com.utility.expensetracker.feature.splash.domain.SplashRepository
import com.utility.expensetracker.feature.splash.model.SplashConfig
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Default implementation of SplashRepository
 *
 * Currently provides static configuration but can be extended
 * to support remote config, shared preferences, or analytics.
 */
@Singleton
class SplashRepositoryImpl
    @Inject
    constructor(
        private val defaultConfig: SplashConfig,
    ) : SplashRepository {
        override suspend fun getSplashConfig(): SplashConfig {
            // For now, return the injected default config
            // Future: Could load from SharedPreferences, remote config, etc.
            return defaultConfig
        }

        override suspend fun trackSplashCompletion(durationMs: Long) {
            // Future: Track analytics event
            // Example: analyticsService.track("splash_completed", mapOf("duration_ms" to durationMs))
        }

        override suspend fun shouldShowSplash(): Boolean {
            // Future: Could check user preferences, onboarding state, etc.
            // For now, always show splash
            return true
        }
    }
