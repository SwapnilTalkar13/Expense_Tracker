package com.utility.expensetracker.navigation

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.utility.expensetracker.ui.theme.PlayfulCoral
import com.utility.expensetracker.ui.theme.PlayfulGreen
import com.utility.expensetracker.ui.theme.PlayfulTeal
import com.utility.expensetracker.ui.theme.SoftWhite
import com.utility.expensetracker.ui.theme.WarmGray

/**
 * Bottom Navigation Items with emoji icons
 */
data class BottomNavItem(
    val route: String,
    val label: String,
    val emoji: String,
    val accentColor: Color,
)

/**
 * Navigation items for the bottom bar with emoji icons
 */
val bottomNavItems = listOf(
    BottomNavItem(
        route = "home",
        label = "Home",
        emoji = "🏠",
        accentColor = PlayfulTeal,
    ),
    BottomNavItem(
        route = "transaction",
        label = "Transaction",
        emoji = "💳",
        accentColor = PlayfulCoral,
    ),
    BottomNavItem(
        route = "category",
        label = "Category",
        emoji = "🏷️",
        accentColor = PlayfulGreen,
    ),
)

/**
 * Playful Bottom Navigation Bar with animated active states
 */
@Composable
fun PlayfulBottomNavigation(
    currentRoute: String,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    
    // Responsive padding and sizing
    val horizontalPadding = remember(screenWidth) {
        when {
            screenWidth < 360.dp -> 12.dp
            screenWidth < 600.dp -> 16.dp
            else -> 24.dp
        }
    }
    
    val verticalPadding = remember(screenWidth) {
        when {
            screenWidth < 360.dp -> 6.dp  // Reduced height
            screenWidth < 600.dp -> 8.dp  // Reduced height
            else -> 10.dp  // Reduced height
        }
    }

    Surface(
        modifier = modifier,
        shadowElevation = 16.dp,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        color = MaterialTheme.colorScheme.surface,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 600.dp) // Max width for tablets
                .padding(horizontal = horizontalPadding, vertical = verticalPadding),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            bottomNavItems.forEach { item ->
                PlayfulNavItem(
                    item = item,
                    isSelected = currentRoute == item.route,
                    onClick = { onNavigate(item.route) },
                    screenWidth = screenWidth,
                )
            }
        }
    }
}

/**
 * Individual navigation item with enhanced animations and visual feedback
 */
@Composable
private fun PlayfulNavItem(
    item: BottomNavItem,
    isSelected: Boolean,
    onClick: () -> Unit,
    screenWidth: androidx.compose.ui.unit.Dp,
    modifier: Modifier = Modifier,
) {
    // Enhanced scale animation with improved timing
    val animatedScale by animateFloatAsState(
        targetValue = if (isSelected) 1.2f else 1.0f, // Increased scale for better visibility
        animationSpec = tween(
            durationMillis = 250, // Smooth 250ms animation
            easing = androidx.compose.animation.core.FastOutSlowInEasing
        ),
        label = "scale",
    )

    // Smooth color animation
    val animatedColor by animateColorAsState(
        targetValue = if (isSelected) item.accentColor else WarmGray,
        animationSpec = tween(
            durationMillis = 200,
            easing = androidx.compose.animation.core.LinearOutSlowInEasing
        ),
        label = "color",
    )

    // Background highlight animation
    val backgroundAlpha by animateFloatAsState(
        targetValue = if (isSelected) 0.15f else 0.0f, // Subtle background highlight
        animationSpec = tween(
            durationMillis = 300,
            easing = androidx.compose.animation.core.FastOutSlowInEasing
        ),
        label = "backgroundAlpha",
    )

    // Responsive sizing - scaled to 75% of original size
    val containerSize = remember(screenWidth) {
        when {
            screenWidth < 360.dp -> 42.dp  // 56.dp * 0.75 = 42.dp
            screenWidth < 600.dp -> 45.dp  // 60.dp * 0.75 = 45.dp
            else -> 48.dp                  // 64.dp * 0.75 = 48.dp
        }
    }

    val fontSize = remember(screenWidth) {
        when {
            screenWidth < 360.dp -> 18.sp  // 24.sp * 0.75 = 18.sp
            screenWidth < 600.dp -> 21.sp  // 28.sp * 0.75 = 21.sp
            else -> 24.sp                  // 32.sp * 0.75 = 24.sp
        }
    }

    // Create interaction source for ripple effect
    val interactionSource = remember { MutableInteractionSource() }

    // Enhanced layout with background highlight
    Box(
        modifier = modifier
            .size(containerSize)
            .scale(animatedScale)
            .clip(RoundedCornerShape(containerSize / 2)) // Circular clip
            .background(
                color = item.accentColor.copy(alpha = backgroundAlpha) // Animated background
            )
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(
                    bounded = true,
                    radius = containerSize / 2,
                    color = item.accentColor
                ),
                onClick = onClick
            )
            .padding(
                horizontal = if (screenWidth < 360.dp) 4.dp else 6.dp,  // Reduced padding for smaller containers
                vertical = if (screenWidth < 360.dp) 3.dp else 4.dp,    // Reduced padding for smaller containers
            ),
        contentAlignment = Alignment.Center,
    ) {
        // Emoji text icon with enhanced styling
        Text(
            text = item.emoji,
            color = if (isSelected) {
                // Enhanced contrast when selected with background
                when {
                    backgroundAlpha > 0.1f -> SoftWhite
                    else -> animatedColor
                }
            } else {
                animatedColor
            },
            fontSize = fontSize,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PlayfulBottomNavigationPreview() {
    PlayfulBottomNavigation(
        currentRoute = "home",
        onNavigate = { },
    )
}