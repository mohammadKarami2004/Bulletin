package com.bulletin.news.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val BulletinDarkScheme = darkColorScheme(
    primary = EmberPrimaryDark,
    onPrimary = EmberOnPrimary,
    primaryContainer = EmberContainer,
    onPrimaryContainer = EmberOnContainer,
    secondary = InkSecondary,
    onSecondary = InkOnSecondary,
    secondaryContainer = InkContainer,
    onSecondaryContainer = InkOnContainer,
    tertiary = SageTertiary,
    onTertiary = SageOnTertiary,
    background = DarkBackground,
    onBackground = DarkOnBackground,
    surface = DarkSurface,
    onSurface = DarkOnBackground,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkOnSurfaceVariant,
    outline = DarkOutline,
    error = ErrorRed,
    onError = OnError
)

private val BulletinLightScheme = lightColorScheme(
    primary = EmberPrimary,
    onPrimary = EmberOnPrimary,
    primaryContainer = EmberContainer,
    onPrimaryContainer = EmberOnContainer,
    secondary = InkSecondary,
    onSecondary = InkOnSecondary,
    secondaryContainer = InkContainer,
    onSecondaryContainer = InkOnContainer,
    tertiary = SageTertiary,
    onTertiary = SageOnTertiary,
    background = LightBackground,
    onBackground = LightOnBackground,
    surface = LightSurface,
    onSurface = LightOnBackground,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = LightOnSurfaceVariant,
    outline = LightOutline,
    error = ErrorRedLight,
    onError = OnError
)

@Composable
fun BulletinTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> BulletinDarkScheme
        else -> BulletinLightScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = BulletinTypography,
        shapes = BulletinShapes,
        content = content
    )
}
