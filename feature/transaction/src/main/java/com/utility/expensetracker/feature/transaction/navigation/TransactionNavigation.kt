package com.utility.expensetracker.feature.transaction.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.utility.expensetracker.feature.transaction.ui.TransactionScreen

const val TRANSACTION_ROUTE = "transaction"

/**
 * Navigation extension function for Transaction screen
 */
fun NavGraphBuilder.transactionScreen() {
    composable(route = TRANSACTION_ROUTE) {
        TransactionScreen()
    }
}

/**
 * Navigation extension function to navigate to Transaction screen
 */
fun NavController.navigateToTransaction() {
    navigate(TRANSACTION_ROUTE) {
        // Clear the back stack to avoid multiple instances
        popUpTo(graph.startDestinationId) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}