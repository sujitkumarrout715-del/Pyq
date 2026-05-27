package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme =
  darkColorScheme(
    primary = ThemeDarkPrimary,
    onPrimary = ThemeDarkOnPrimary,
    primaryContainer = ThemeDarkPrimaryContainer,
    onPrimaryContainer = ThemeDarkOnPrimaryContainer,
    secondaryContainer = ThemeDarkSecondaryContainer,
    onSecondaryContainer = ThemeDarkOnSecondaryContainer,
    background = ThemeDarkBackground,
    onBackground = ThemeDarkOnBackground,
    surface = ThemeDarkSurface,
    onSurface = ThemeDarkOnSurface,
    surfaceVariant = ThemeDarkSurfaceVariant,
    onSurfaceVariant = ThemeDarkOnSurfaceVariant,
    outline = ThemeDarkOutline
  )

private val LightColorScheme =
  lightColorScheme(
    primary = ThemeLightPrimary,
    onPrimary = ThemeLightOnPrimary,
    primaryContainer = ThemeLightPrimaryContainer,
    onPrimaryContainer = ThemeLightOnPrimaryContainer,
    secondaryContainer = ThemeLightSecondaryContainer,
    onSecondaryContainer = ThemeLightOnSecondaryContainer,
    background = ThemeLightBackground,
    onBackground = ThemeLightOnBackground,
    surface = ThemeLightSurface,
    onSurface = ThemeLightOnSurface,
    surfaceVariant = ThemeLightSurfaceVariant,
    onSurfaceVariant = ThemeLightOnSurfaceVariant,
    outline = ThemeLightOutline
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  // Dynamic color is disabled by default to preserve the exact brand palette
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  val colorScheme =
    when {
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }

      darkTheme -> DarkColorScheme
      else -> LightColorScheme
    }

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
