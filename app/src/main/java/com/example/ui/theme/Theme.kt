package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val FutTradingColorScheme = darkColorScheme(
    primary = FutGold,
    onPrimary = Color.Black,
    primaryContainer = FutGoldContainer,
    onPrimaryContainer = FutGoldBright,
    secondary = FutGreenProfit,
    onSecondary = Color.Black,
    secondaryContainer = FutGreenDark,
    onSecondaryContainer = FutGreenProfit,
    tertiary = FutCyanAccent,
    onTertiary = Color.Black,
    tertiaryContainer = FutCyanDark,
    onTertiaryContainer = FutCyanAccent,
    background = FutDarkBackground,
    onBackground = FutTextPrimary,
    surface = FutSurface,
    onSurface = FutTextPrimary,
    surfaceVariant = FutSurfaceVariant,
    onSurfaceVariant = FutTextSecondary,
    outline = FutBorder,
    error = FutRedTax,
    onError = Color.White,
    errorContainer = FutRedDark,
    onErrorContainer = FutRedTax
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Use our handcrafted FUT trading dark theme for consistent esports aesthetic
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = FutTradingColorScheme,
        typography = Typography,
        content = content
    )
}
