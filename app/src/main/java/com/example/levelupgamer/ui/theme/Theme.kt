package com.example.levelupgamer.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColors = darkColorScheme(
    primary = BrandYellow,
    onPrimary = Color.Black,
    secondary = BrandYellowDark,
    onSecondary = Color.Black,
    background = Bg,
    onBackground = OnDark,
    surface = SurfaceDark,
    onSurface = OnDark
)

private val LightColors = lightColorScheme(
    primary = BrandYellow,
    onPrimary = Color.Black,
    secondary = BrandYellowDark,
    onSecondary = Color.Black,
    // tonos claros que combinen
    background = Color(0xFFF7F7F7),
    onBackground = Color(0xFF111111),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF222222)
)

@Composable
fun LevelUpGamerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),

    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme =
        if (dynamicColor && Build.VERSION.SDK_INT >= 31) {
            val ctx = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(ctx) else dynamicLightColorScheme(ctx)
        } else {
            if (darkTheme) DarkColors else LightColors
        }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}