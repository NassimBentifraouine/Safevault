package com.example.safevault.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = Mint600,
    onPrimary = Pearl50,
    primaryContainer = Mint200,
    onPrimaryContainer = Slate900,
    secondary = Slate600,
    onSecondary = Pearl50,
    secondaryContainer = Pearl100,
    onSecondaryContainer = Slate900,
    tertiary = Amber600,
    onTertiary = Pearl50,
    tertiaryContainer = Amber200,
    onTertiaryContainer = Slate900,
    error = Rose700,
    onError = Pearl50,
    errorContainer = Rose200,
    onErrorContainer = Slate900,
    background = Pearl50,
    onBackground = Slate900,
    surface = Color.White,
    onSurface = Slate900,
    surfaceVariant = Pearl100,
    onSurfaceVariant = Slate700,
    outline = Slate300,
)

private val DarkColorScheme = darkColorScheme(
    primary = Mint500,
    onPrimary = Slate900,
    primaryContainer = Slate700,
    onPrimaryContainer = Mint200,
    secondary = Slate300,
    onSecondary = Slate900,
    secondaryContainer = Slate700,
    onSecondaryContainer = Pearl100,
    tertiary = Amber200,
    onTertiary = Slate900,
    tertiaryContainer = Amber600,
    onTertiaryContainer = Pearl50,
    error = Rose200,
    onError = Slate900,
    errorContainer = Rose700,
    onErrorContainer = Pearl50,
    background = Slate900,
    onBackground = Pearl50,
    surface = Slate700,
    onSurface = Pearl50,
    surfaceVariant = Slate600,
    onSurfaceVariant = Pearl100,
    outline = Slate300,
)

@Composable
fun SafeVaultTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content,
    )
}
