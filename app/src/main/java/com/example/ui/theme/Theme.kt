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
    primary = Emerald80,
    onPrimary = Emerald20,
    primaryContainer = Emerald20,
    onPrimaryContainer = Emerald80,
    secondary = GoldWarm,
    onSecondary = Emerald20,
    tertiary = Gold80,
    background = DarkPineSurface,
    onBackground = WhiteText,
    surface = DarkPineCard,
    onSurface = WhiteText,
    surfaceVariant = DarkPineSurface,
    onSurfaceVariant = GoldLightText
  )

private val LightColorScheme =
  lightColorScheme(
    primary = Emerald40,
    onPrimary = SandIvory,
    primaryContainer = SandSurface,
    onPrimaryContainer = Emerald20,
    secondary = Gold40,
    onSecondary = SandIvory,
    tertiary = GoldWarm,
    background = SandIvory,
    onBackground = CharcoalText,
    surface = SandIvory,
    onSurface = CharcoalText,
    surfaceVariant = SandSurface,
    onSurfaceVariant = CharcoalMuted
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  // Use intentional brand colors
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
