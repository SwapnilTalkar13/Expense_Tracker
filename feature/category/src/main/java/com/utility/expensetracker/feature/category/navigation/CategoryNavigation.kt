package com.utility.expensetracker.feature.category.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.utility.expensetracker.feature.category.ui.CategoryScreen

const val CATEGORY_ROUTE = "category"

/**
 * Navigation extension function for Category screen
 */
fun NavGraphBuilder.categoryScreen() {
    composable(route = CATEGORY_ROUTE) {
        CategoryScreen()
    }
}

/**
 * Navigation extension function to navigate to Category screen
 */
fun NavController.navigateToCategory() {
    navigate(CATEGORY_ROUTE) {
        // Clear the back stack to avoid multiple instances
        popUpTo(graph.startDestinationId) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}