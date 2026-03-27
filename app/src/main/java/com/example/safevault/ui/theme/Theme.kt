package com.example.safevault.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = RoyalBlue600,
    onPrimary = White,
    primaryContainer = RoyalBlue100,
    onPrimaryContainer = Ink950,
    secondary = Ink700,
    onSecondary = White,
    secondaryContainer = Cloud100,
    onSecondaryContainer = Ink800,
    tertiary = MintAccent500,
    onTertiary = Ink950,
    tertiaryContainer = MintAccent100,
    onTertiaryContainer = Ink950,
    error = Rose700,
    onError = White,
    errorContainer = Rose100,
    onErrorContainer = Ink950,
    background = Cloud50,
    onBackground = Ink950,
    surface = White,
    onSurface = Ink950,
    surfaceVariant = Cloud100,
    onSurfaceVariant = Ink700,
    outline = Ink500,
)

private val DarkColorScheme = darkColorScheme(
    primary = RoyalBlue600,
    onPrimary = White,
    primaryContainer = RoyalBlue700,
    onPrimaryContainer = Cloud50,
    secondary = RoyalBlue100,
    onSecondary = Ink950,
    secondaryContainer = Ink800,
    onSecondaryContainer = Cloud50,
    tertiary = MintAccent500,
    onTertiary = Ink950,
    tertiaryContainer = Ink700,
    onTertiaryContainer = Cloud50,
    error = Rose100,
    onError = Ink950,
    errorContainer = Rose700,
    onErrorContainer = White,
    background = Ink950,
    onBackground = Cloud50,
    surface = Ink800,
    onSurface = Cloud50,
    surfaceVariant = Ink700,
    onSurfaceVariant = Cloud100,
    outline = Ink500,
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
