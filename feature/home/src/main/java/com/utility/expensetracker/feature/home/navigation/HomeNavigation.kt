package com.utility.expensetracker.feature.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.utility.expensetracker.feature.home.ui.HomeScreen

const val HOME_ROUTE = "home"

/**
 * Navigation extension function for Home screen
 */
fun NavGraphBuilder.homeScreen() {
    composable(route = HOME_ROUTE) {
        HomeScreen()
    }
}

/**
 * Navigation extension function to navigate to Home screen
 */
fun NavController.navigateToHome() {
    navigate(HOME_ROUTE) {
        // Clear the back stack to avoid multiple instances
        popUpTo(graph.startDestinationId) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}