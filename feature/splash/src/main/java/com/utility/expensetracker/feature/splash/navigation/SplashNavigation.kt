package com.utility.expensetracker.feature.splash.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.utility.expensetracker.feature.splash.ui.SplashScreen

/**
 * Route definition for splash screen
 */
const val SPLASH_ROUTE = "splash"

/**
 * Extension function to add splash screen to navigation graph
 */
fun NavGraphBuilder.splashScreen(onSplashComplete: () -> Unit) {
    composable(route = SPLASH_ROUTE) {
        SplashScreen(
            onSplashComplete = onSplashComplete,
        )
    }
}

/**
 * Navigation extension to navigate to splash screen
 */
fun NavController.navigateToSplash() {
    navigate(SPLASH_ROUTE) {
        // Clear back stack to prevent going back to splash
        popUpTo(graph.startDestinationId) {
            inclusive = true
        }
    }
}
