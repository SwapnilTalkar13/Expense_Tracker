package com.utility.expensetracker.feature.splash.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 * Color scheme for splash screen module
 * These colors match the main app theme for consistency
 */
private val SplashDarkColorScheme =
    darkColorScheme(
        // Purple80
        primary = Color(0xFFD0BCFF),
        // PurpleGrey80
        secondary = Color(0xFFCCC2DC),
        // Pink80
        tertiary = Color(0xFFEFB8C8),
        background = Color(0xFF1C1B1F),
        surface = Color(0xFF1C1B1F),
        surfaceVariant = Color(0xFF49454F),
        onPrimary = Color(0xFF381E72),
        onSecondary = Color(0xFF4A4458),
        onTertiary = Color(0xFF633B48),
        onBackground = Color(0xFFE6E0E9),
        onSurface = Color(0xFFE6E0E9),
        onSurfaceVariant = Color(0xFFCAC4D0),
        error = Color(0xFFFFB4AB),
        onError = Color(0xFF690005),
        outline = Color(0xFF938F99),
        outlineVariant = Color(0xFF49454F),
    )

private val SplashLightColorScheme =
    lightColorScheme(
        // Purple40
        primary = Color(0xFF6650a4),
        // PurpleGrey40
        secondary = Color(0xFF625b71),
        // Pink40
        tertiary = Color(0xFF7D5260),
        background = Color(0xFFFFFBFE),
        surface = Color(0xFFFFFBFE),
        surfaceVariant = Color(0xFFE7E0EC),
        onPrimary = Color.White,
        onSecondary = Color.White,
        onTertiary = Color.White,
        onBackground = Color(0xFF1C1B1F),
        onSurface = Color(0xFF1C1B1F),
        onSurfaceVariant = Color(0xFF49454F),
        error = Color(0xFFBA1A1A),
        onError = Color.White,
        outline = Color(0xFF79747E),
        outlineVariant = Color(0xFFCAC4D0),
    )

/**
 * Theme composable for splash screen previews
 * Uses the same color scheme as the main app for consistency
 */
@Composable
internal fun SplashPreviewTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme =
        if (darkTheme) {
            SplashDarkColorScheme
        } else {
            SplashLightColorScheme
        }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content,
    )
}
