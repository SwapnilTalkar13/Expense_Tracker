package com.utility.expensetracker.feature.splash.domain

import javax.inject.Inject
import javax.inject.Singleton

/**
 * Use case for tracking splash screen completion
 *
 * Handles analytics and logging for splash screen events.
 */
@Singleton
class TrackSplashCompletionUseCase
    @Inject
    constructor(
        private val repository: SplashRepository,
    ) {
        /**
         * Track splash screen completion
         *
         * @param durationMs How long the splash was displayed
         */
        suspend operator fun invoke(durationMs: Long) {
            repository.trackSplashCompletion(durationMs)
        }
    }
