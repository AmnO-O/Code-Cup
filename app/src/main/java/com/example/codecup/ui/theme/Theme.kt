package com.example.codecup.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = CoffeeOnPrimaryContainer, // Light tan for visibility on dark
    onPrimary = CoffeePrimary,
    primaryContainer = CoffeePrimaryContainer,
    onPrimaryContainer = Color.White,
    secondary = CoffeeSecondaryContainer, // Vibrant orange for dark mode
    onSecondary = CoffeeOnSecondaryContainer,
    secondaryContainer = CoffeeSecondaryContainer,
    onSecondaryContainer = CoffeeOnSecondaryContainer,
    tertiaryContainer = Color(0xFF5D4037),
    onTertiaryContainer = CoffeeAccentContainer,
    background = CoffeeOnBackground,
    onBackground = CoffeeBackground,
    surface = CoffeePrimaryContainer,
    onSurface = CoffeeBackground,
    surfaceVariant = Color(0xFF3D2D26),
    onSurfaceVariant = CoffeeOutlineVariant,
    outline = CoffeeOutline,
    outlineVariant = CoffeeOutlineVariant
)

private val LightColorScheme = lightColorScheme(
    primary = CoffeePrimary,
    primaryContainer = CoffeePrimaryContainer,
    onPrimaryContainer = Color.White,
    secondary = CoffeeSecondary,
    secondaryContainer = CoffeeSecondaryContainer,
    onSecondaryContainer = CoffeeOnSecondaryContainer,
    tertiaryContainer = CoffeeAccentContainer,
    onTertiaryContainer = CoffeeOnSecondaryContainer,
    background = CoffeeBackground,
    onBackground = CoffeeOnBackground,
    surface = CoffeeSurface,
    onSurface = CoffeeOnSurface,
    surfaceVariant = CoffeeSurfaceContainerLow,
    onSurfaceVariant = CoffeeOnSurfaceVariant,
    outline = CoffeeOutline,
    outlineVariant = CoffeeOutlineVariant
)

@Composable
fun CodeCupTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is disabled by default to use our custom coffee theme
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
