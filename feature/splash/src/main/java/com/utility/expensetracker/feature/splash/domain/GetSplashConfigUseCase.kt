package com.utility.expensetracker.feature.splash.domain

import com.utility.expensetracker.feature.splash.model.SplashConfig
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Use case for getting splash screen configuration
 *
 * Follows clean architecture patterns and provides a single
 * entry point for splash configuration logic.
 */
@Singleton
class GetSplashConfigUseCase
    @Inject
    constructor(
        private val repository: SplashRepository,
    ) {
        /**
         * Get the current splash configuration
         *
         * @return SplashConfig with current settings
         */
        suspend operator fun invoke(): SplashConfig {
            return repository.getSplashConfig()
        }
    }
