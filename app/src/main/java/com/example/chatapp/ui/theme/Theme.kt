package com.example.chatapp.ui.theme

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
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat

// Custom Colors


// Chat App Custom Colors
val PrimaryBlue = Color(0xFF2A6FDB)
val SecondaryTeal = Color(0xFF03DAC5)
val ErrorRed = Color(0xFFE53935)
val SuccessGreen = Color(0xFF43A047)
val WarningAmber = Color(0xFFFFA000)
val InfoCyan = Color(0xFF00ACC1)

val SurfaceDark = Color(0xFF121212)
val SurfaceLight = Color(0xFFFAFAFA)
val BackgroundDark = Color(0xFF000000)
val BackgroundLight = Color(0xFFFFFFFF)

val MessageBubbleSent = Color(0xFFE3F2FD)
val MessageBubbleReceived = Color(0xFFFFFFFF)
val MessageBubbleSentDark = Color(0xFF0D47A1)
val MessageBubbleReceivedDark = Color(0xFF1E1E1E)

// Dark Theme Color Scheme
private val DarkColorScheme = darkColorScheme(
    primary = PrimaryBlue,
    secondary = SecondaryTeal,
    tertiary = Pink80,
    error = ErrorRed,
    background = BackgroundDark,
    surface = SurfaceDark,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onTertiary = Color.Black,
    onBackground = Color.White,
    onSurface = Color.White,
    onError = Color.White,
    surfaceVariant = Color(0xFF1E1E1E),
    onSurfaceVariant = Color.White.copy(alpha = 0.8f)
)

// Light Theme Color Scheme
private val LightColorScheme = lightColorScheme(
    primary = PrimaryBlue,
    secondary = SecondaryTeal,
    tertiary = Pink40,
    error = ErrorRed,
    background = BackgroundLight,
    surface = SurfaceLight,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onTertiary = Color.White,
    onBackground = Color.Black,
    onSurface = Color.Black,
    onError = Color.White,
    surfaceVariant = Color(0xFFE0E0E0),
    onSurfaceVariant = Color.Black.copy(alpha = 0.8f)
)

// Custom Chat App Typography
val ChatAppTypography = androidx.compose.material3.Typography(
    displayLarge = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 57.sp,
        lineHeight = 64.sp,
        letterSpacing = (-0.25).sp
    ),
    displayMedium = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 45.sp,
        lineHeight = 52.sp,
        letterSpacing = 0.sp
    ),
    displaySmall = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 36.sp,
        lineHeight = 44.sp,
        letterSpacing = 0.sp
    ),
    headlineLarge = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = 0.sp
    ),
    headlineMedium = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 28.sp,
        lineHeight = 36.sp,
        letterSpacing = 0.sp
    ),
    headlineSmall = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp
    ),
    titleLarge = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    titleMedium = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp
    ),
    titleSmall = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),
    bodyLarge = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp
    ),
    bodyMedium = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp
    ),
    bodySmall = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp
    ),
    labelLarge = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),
    labelMedium = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    ),
    labelSmall = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
)

// Custom Shapes
val ChatAppShapes = androidx.compose.material3.Shapes(
    small = androidx.compose.foundation.shape.RoundedCornerShape(4.dp),
    medium = androidx.compose.foundation.shape.RoundedCornerShape(8.dp),
    large = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
)

// Custom Chat App Theme Data Class
data class ChatAppTheme(
    val isDark: Boolean,
    val shapes: androidx.compose.material3.Shapes = ChatAppShapes,
    val typography: androidx.compose.material3.Typography = ChatAppTypography,
    val colors: ChatAppColors
)

// Custom Colors specific to chat app functionality
data class ChatAppColors(
    val messageBubbleSent: Color,
    val messageBubbleReceived: Color,
    val typingIndicator: Color,
    val onlineStatus: Color,
    val offlineStatus: Color,
    val divider: Color
)

// CompositionLocal to provide theme values
val LocalChatAppTheme = compositionLocalOf {
    ChatAppTheme(
        isDark = false,
        colors = ChatAppColors(
            messageBubbleSent = MessageBubbleSent,
            messageBubbleReceived = MessageBubbleReceived,
            typingIndicator = PrimaryBlue,
            onlineStatus = SuccessGreen,
            offlineStatus = Color.Gray,
            divider = Color.LightGray
        )
    )
}

@Composable
fun ChatAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
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

    val chatAppColors = if (darkTheme) {
        ChatAppColors(
            messageBubbleSent = MessageBubbleSentDark,
            messageBubbleReceived = MessageBubbleReceivedDark,
            typingIndicator = PrimaryBlue,
            onlineStatus = SuccessGreen,
            offlineStatus = Color.Gray,
            divider = Color.DarkGray
        )
    } else {
        ChatAppColors(
            messageBubbleSent = MessageBubbleSent,
            messageBubbleReceived = MessageBubbleReceived,
            typingIndicator = PrimaryBlue,
            onlineStatus = SuccessGreen,
            offlineStatus = Color.Gray,
            divider = Color.LightGray
        )
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    androidx.compose.runtime.CompositionLocalProvider(
        LocalChatAppTheme provides ChatAppTheme(
            isDark = darkTheme,
            colors = chatAppColors
        )
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = ChatAppTypography,
            shapes = ChatAppShapes,
            content = content
        )
    }
}