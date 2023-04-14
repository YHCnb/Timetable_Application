package com.example.timetable_application.ui.theme

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.ViewCompat

private val DarkColorScheme = darkColorScheme(
    primary = Green45,
    onPrimary = Color.White,
    primaryContainer = Green70,
    onPrimaryContainer = Green30,
    inversePrimary = Green80,
    secondary = DarkGreen35,
    onSecondary = Color.White,
    secondaryContainer = DarkGreen70,
    onSecondaryContainer = DarkGreen25,
    tertiary = Orange40,
    onTertiary = Color.White,
    tertiaryContainer = Orange80,
    onTertiaryContainer = Orange30,
    error = Red40,
    onError = Color.White,
    errorContainer = Red90,
    onErrorContainer = Red20,
    background = Brown90,
    onBackground = Brown30,
    surface = Gray70,
    onSurface = GreenGray30,
//    inverseSurface = Grey20,
//    inverseOnSurface = Grey95,
    surfaceVariant = GreenGray70,
    onSurfaceVariant = GreenGray30,
    outline = GreenGray50
)

private val LightColorScheme = lightColorScheme(
    primary = Green45,
    onPrimary = Color.White,
    primaryContainer = Green70,
    onPrimaryContainer = Green30,
    inversePrimary = Green80,
    secondary = DarkGreen35,
    onSecondary = Color.White,
    secondaryContainer = DarkGreen70,
    onSecondaryContainer = DarkGreen25,
    tertiary = Orange40,
    onTertiary = Color.White,
    tertiaryContainer = Orange80,
    onTertiaryContainer = Orange30,
    error = Red40,
    onError = Color.White,
    errorContainer = Red90,
    onErrorContainer = Red20,
    background = Brown90,
    onBackground = Brown30,
    surface = Gray70,
    onSurface = GreenGray30,
//    inverseSurface = Grey20,
//    inverseOnSurface = Grey95,
    surfaceVariant = GreenGray70,
    onSurfaceVariant = GreenGray30,
    outline = GreenGray40
)

@Composable
fun Timetable_ApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
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
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            (view.context as Activity).window.statusBarColor = colorScheme.primary.toArgb()
            ViewCompat.getWindowInsetsController(view)?.isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}