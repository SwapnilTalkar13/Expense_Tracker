package com.utility.expensetracker.feature.splash.model

/**
 * Configuration data class for splash screen
 *
 * @param animationAsset Path to the Lottie animation asset
 * @param minDisplayTime Minimum time to display splash screen (milliseconds)
 * @param fadeOutDuration Duration of fade out animation (milliseconds)
 * @param onSplashComplete Callback invoked when splash is complete
 */
data class SplashConfig(
    val animationAsset: String = "splash_animation.json",
    val minDisplayTime: Long = 3000L,
    val fadeOutDuration: Long = 500L,
    val onSplashComplete: () -> Unit,
)
