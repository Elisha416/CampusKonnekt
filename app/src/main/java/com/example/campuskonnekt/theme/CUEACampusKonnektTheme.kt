package com.example.campuskonnekt.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// CUEA Brand Colors - Catholic University theme
// Burgundy/Maroon (primary), Gold (secondary), White/Cream (background)
val CUEABurgundy = Color(0xFF800020) // Deep burgundy/maroon
val CUEAGold = Color(0xFFD4AF37) // Rich gold
val CUEALightGold = Color(0xFFF4E4C1) // Light gold/cream
val CUEADarkBurgundy = Color(0xFF5C0016) // Darker burgundy for dark mode
val CUEAAccentRed = Color(0xFFDC143C) // Crimson accent

// Light theme colors for CUEA
private val CUEALightColorScheme = lightColorScheme(
    primary = CUEABurgundy,
    onPrimary = Color.White,
    primaryContainer = CUEALightGold,
    onPrimaryContainer = CUEABurgundy,

    secondary = CUEAGold,
    onSecondary = CUEABurgundy,
    secondaryContainer = CUEALightGold,
    onSecondaryContainer = CUEABurgundy,

    tertiary = CUEAAccentRed,
    onTertiary = Color.White,

    background = Color(0xFFFFFBF5), // Warm off-white
    onBackground = Color(0xFF1C1B1F),

    surface = Color.White,
    onSurface = Color(0xFF1C1B1F),
    surfaceVariant = CUEALightGold,
    onSurfaceVariant = CUEABurgundy,

    error = Color(0xFFB3261E),
    onError = Color.White,

    outline = CUEAGold,
    outlineVariant = CUEALightGold
)

// Dark theme colors for CUEA
private val CUEADarkColorScheme = darkColorScheme(
    primary = CUEAGold,
    onPrimary = CUEADarkBurgundy,
    primaryContainer = CUEADarkBurgundy,
    onPrimaryContainer = CUEAGold,

    secondary = CUEALightGold,
    onSecondary = CUEADarkBurgundy,
    secondaryContainer = CUEABurgundy,
    onSecondaryContainer = CUEALightGold,

    tertiary = CUEAAccentRed,
    onTertiary = Color.White,

    background = Color(0xFF1C1B1F),
    onBackground = Color(0xFFE6E1E5),

    surface = Color(0xFF2B2930),
    onSurface = Color(0xFFE6E1E5),
    surfaceVariant = Color(0xFF49454F),
    onSurfaceVariant = CUEALightGold,

    error = Color(0xFFF2B8B5),
    onError = Color(0xFF601410),

    outline = CUEAGold,
    outlineVariant = Color(0xFF49454F)
)

// Custom Typography for CUEA (academic/professional feel)
val CUEATypography = Typography(
    displayLarge = TextStyle(
        fontFamily = FontFamily.Serif,
        fontWeight = FontWeight.Bold,
        fontSize = 57.sp,
        lineHeight = 64.sp,
        letterSpacing = (-0.25).sp,
    ),
    displayMedium = TextStyle(
        fontFamily = FontFamily.Serif,
        fontWeight = FontWeight.Bold,
        fontSize = 45.sp,
        lineHeight = 52.sp,
        letterSpacing = 0.sp,
    ),
    displaySmall = TextStyle(
        fontFamily = FontFamily.Serif,
        fontWeight = FontWeight.Bold,
        fontSize = 36.sp,
        lineHeight = 44.sp,
        letterSpacing = 0.sp,
    ),
    headlineLarge = TextStyle(
        fontFamily = FontFamily.Serif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = 0.sp,
    ),
    headlineMedium = TextStyle(
        fontFamily = FontFamily.Serif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 28.sp,
        lineHeight = 36.sp,
        letterSpacing = 0.sp,
    ),
    headlineSmall = TextStyle(
        fontFamily = FontFamily.Serif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp,
    ),
    titleLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp,
    ),
    titleMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp,
    ),
    titleSmall = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp,
    ),
    bodyLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp,
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp,
    ),
    bodySmall = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp,
    ),
    labelLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp,
    ),
    labelMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp,
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp,
    )
)

@Composable
fun CUEACampusConnectTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) CUEADarkColorScheme else CUEALightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = CUEATypography,
        content = content
    )
}