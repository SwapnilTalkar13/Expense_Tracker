package com.utility.expensetracker.feature.splash.presentation

import androidx.annotation.StringRes
import com.utility.expensetracker.feature.splash.R

/**
 * Represents different types of errors that can occur during splash screen
 */
sealed class SplashError(
    @StringRes val messageRes: Int,
) {
    /**
     * Generic error during splash loading
     */
    data object LoadingFailed : SplashError(R.string.splash_error_failed_to_load)

    /**
     * Network connection error
     */
    data object NetworkConnection : SplashError(R.string.splash_error_network_connection)

    /**
     * Animation loading error
     */
    data object AnimationLoad : SplashError(R.string.splash_error_animation_load)

    /**
     * Network timeout error
     */
    data object NetworkTimeout : SplashError(R.string.splash_error_network_timeout)

    /**
     * Generic unexpected error
     */
    data object Generic : SplashError(R.string.splash_error_generic)
}
