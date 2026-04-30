package com.utility.expensetracker.feature.splash.presentation

/**
 * Represents the different states of the splash screen.
 */
sealed class SplashUiState {
    /**
     * Initial loading state when splash screen is displayed
     */
    data object Loading : SplashUiState()

    /**
     * Animation complete state, ready to navigate
     */
    data object Complete : SplashUiState()

    /**
     * Error state if something goes wrong during splash
     * @param error The specific error type with localized message resource
     * @param details Optional additional error details for logging/debugging
     */
    data class Error(
        val error: SplashError,
        val details: String? = null,
    ) : SplashUiState()
}
