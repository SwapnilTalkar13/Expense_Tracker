package com.utility.expensetracker.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.utility.expensetracker.ui.theme.ExpenseTrackerTheme

/**
 * Preview component to showcase the enhanced animated bottom navigation
 */
@Preview(name = "Enhanced Bottom Navigation - Home Selected", showBackground = true)
@Composable
private fun EnhancedBottomNavigationHomePreview() {
    ExpenseTrackerTheme {
        Column {
            Text(
                "Enhanced Footer - Home Selected (🏠)",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(8.dp))
            PlayfulBottomNavigation(
                currentRoute = "home",
                onNavigate = { },
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "• Scale: 1.2x • 75% size • Background highlight • Smooth animations",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview(name = "Enhanced Bottom Navigation - Transaction Selected", showBackground = true)
@Composable
private fun EnhancedBottomNavigationTransactionPreview() {
    ExpenseTrackerTheme {
        Column {
            Text(
                "Enhanced Footer - Transaction Selected (💳)",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(8.dp))
            PlayfulBottomNavigation(
                currentRoute = "transaction",
                onNavigate = { },
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "• Coral background • 75% size • Ripple effects • Fast transitions",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview(name = "Enhanced Bottom Navigation - Category Selected", showBackground = true)
@Composable
private fun EnhancedBottomNavigationCategoryPreview() {
    ExpenseTrackerTheme {
        Column {
            Text(
                "Enhanced Footer - Category Selected (🏷️)",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(8.dp))
            PlayfulBottomNavigation(
                currentRoute = "category",
                onNavigate = { },
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "• Green highlight • 75% size • Enhanced contrast • Bouncy feedback",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview(name = "Enhanced Bottom Navigation - All States", showBackground = true, widthDp = 400)
@Composable 
private fun EnhancedBottomNavigationAllStatesPreview() {
    ExpenseTrackerTheme {
        Column {
            Text(
                "All Navigation States Comparison",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            
            listOf("home", "transaction", "category").forEach { route ->
                Text(
                    "${route.replaceFirstChar { it.uppercase() }} Selected:",
                    style = MaterialTheme.typography.labelMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                PlayfulBottomNavigation(
                    currentRoute = route,
                    onNavigate = { },
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}