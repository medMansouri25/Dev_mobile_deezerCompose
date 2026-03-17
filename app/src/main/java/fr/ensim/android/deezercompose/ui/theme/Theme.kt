package fr.ensim.android.deezercompose.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LuxuryDarkScheme = darkColorScheme(
    primary = GoldPrimary,
    onPrimary = NightBlack,
    primaryContainer = GoldDark,
    onPrimaryContainer = GoldLight,
    secondary = GoldLight,
    onSecondary = NightBlack,
    background = NightBlack,
    onBackground = PureWhite,
    surface = DeepCharcoal,
    onSurface = SilverText,
    surfaceVariant = SoftCharcoal,
    onSurfaceVariant = MutedSilver,
    error = RubyRed,
    onError = Color.White,
    outline = FadedGrey,
    inverseSurface = ElevatedSurface
)

@Composable
fun DeezerComposeTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LuxuryDarkScheme,
        typography = AppTypography,
        content = content
    )
}