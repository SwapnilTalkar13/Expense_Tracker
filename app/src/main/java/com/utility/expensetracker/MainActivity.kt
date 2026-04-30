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
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.utility.expensetracker.feature.splash.navigation.SPLASH_ROUTE
import com.utility.expensetracker.feature.splash.navigation.splashScreen
import com.utility.expensetracker.ui.theme.ExpenseTrackerTheme

/**
 * Main activity for ExpenseTracker application
 *
 * Sets up the navigation graph with splash screen as the initial destination.
 */
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

        // Main app content - placeholder for now
        composable("main") {
            MainAppContent()
        }
    }
}

/**
 * Placeholder main app content
 *
 * This will be replaced with the actual main navigation graph
 * once other features are implemented.
 */
@Composable
private fun MainAppContent() {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Text(
            text = "Welcome to Expense Tracker!",
            modifier = Modifier.padding(innerPadding),
        )
    }
}
