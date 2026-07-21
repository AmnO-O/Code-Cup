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
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = CoffeePrimary,
    secondary = CoffeeSecondary,
    background = CoffeeOnBackground,
    surface = CoffeePrimaryContainer,
    onPrimary = CoffeeSurface,
    onSecondary = CoffeeSurface,
    onBackground = CoffeeSurface,
    onSurface = CoffeeSurface
)

private val LightColorScheme = lightColorScheme(
    primary = CoffeePrimary,
    primaryContainer = CoffeePrimaryContainer,
    onPrimaryContainer = CoffeeOnPrimaryContainer,
    secondary = CoffeeSecondary,
    secondaryContainer = CoffeeSecondaryContainer,
    onSecondaryContainer = CoffeeOnSecondaryContainer,
    background = CoffeeBackground,
    onBackground = CoffeeOnBackground,
    surface = CoffeeSurface,
    onSurface = CoffeeOnSurface,
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
