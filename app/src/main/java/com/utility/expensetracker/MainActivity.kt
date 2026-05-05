package com.utility.expensetracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.utility.expensetracker.feature.splash.navigation.SPLASH_ROUTE
import com.utility.expensetracker.feature.splash.navigation.splashScreen
import com.utility.expensetracker.feature.home.navigation.HOME_ROUTE
import com.utility.expensetracker.feature.home.navigation.homeScreen
import com.utility.expensetracker.feature.home.navigation.navigateToHome
import com.utility.expensetracker.feature.transaction.navigation.TRANSACTION_ROUTE
import com.utility.expensetracker.feature.transaction.navigation.transactionScreen
import com.utility.expensetracker.feature.transaction.navigation.navigateToTransaction
import com.utility.expensetracker.feature.category.navigation.CATEGORY_ROUTE
import com.utility.expensetracker.feature.category.navigation.categoryScreen
import com.utility.expensetracker.feature.category.navigation.navigateToCategory
import com.utility.expensetracker.navigation.PlayfulBottomNavigation
import com.utility.expensetracker.ui.theme.ExpenseTrackerTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * Main activity for ExpenseTracker application
 *
 * Sets up the navigation graph with splash screen as the initial destination.
 * Annotated with @AndroidEntryPoint to enable Hilt dependency injection.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Enable edge-to-edge display
        enableEdgeToEdge()

        // Ensure proper system bar handling for splash screen
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            ExpenseTrackerTheme {
                ExpenseTrackerApp()
            }
        }
    }
}

/**
 * Main composable for the ExpenseTracker app
 *
 * Sets up navigation with splash screen as the start destination.
 */
@Composable
private fun ExpenseTrackerApp() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = SPLASH_ROUTE,
        modifier = Modifier.fillMaxSize(),
    ) {
        // Splash screen - initial destination
        splashScreen(
            onSplashComplete = {
                // Navigate to main app content
                navController.navigate("main") {
                    // Remove splash from back stack
                    popUpTo(SPLASH_ROUTE) {
                        inclusive = true
                    }
                }
            },
        )

        // Main app content with bottom navigation
        composable("main") {
            MainAppContent()
        }
    }
}

/**
 * Main app content with bottom navigation and feature screens
 */
@Composable
private fun MainAppContent() {
    val mainNavController = rememberNavController()
    val currentBackStackEntry by mainNavController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route ?: HOME_ROUTE

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            PlayfulBottomNavigation(
                currentRoute = currentRoute,
                onNavigate = { route ->
                    when (route) {
                        "home" -> mainNavController.navigateToHome()
                        "transaction" -> mainNavController.navigateToTransaction()
                        "category" -> mainNavController.navigateToCategory()
                    }
                },
            )
        },
    ) { innerPadding ->
        NavHost(
            navController = mainNavController,
            startDestination = HOME_ROUTE,
            modifier = Modifier.padding(innerPadding),
        ) {
            // Feature screens
            homeScreen()
            transactionScreen()
            categoryScreen()
        }
    }
}
