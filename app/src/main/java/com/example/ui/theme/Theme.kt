package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.luminance
import com.example.data.model.AppSettingsState
import com.example.data.model.AppTheme
import com.example.data.model.AppearanceMode
import kotlin.math.max
import kotlin.math.min

val LocalAppSettings = staticCompositionLocalOf { AppSettingsState() }

object ThemeContrastUtils {

    fun contrastRatio(foreground: Color, background: Color): Float {
        val l1 = foreground.luminance()
        val l2 = background.luminance()
        val lighter = max(l1, l2)
        val darker = min(l1, l2)
        return (lighter + 0.05f) / (darker + 0.05f)
    }

    fun bestOnColor(background: Color): Color {
        val whiteRatio = contrastRatio(Color.White, background)
        val darkInk = Color(0xFF111815)
        val darkRatio = contrastRatio(darkInk, background)
        return if (whiteRatio >= darkRatio) Color.White else darkInk
    }

    fun ensureReadableAccent(
        rawAccent: Color,
        surfaceBackground: Color,
        isDarkSurface: Boolean,
        minRatio: Float = 3.8f
    ): Color {
        if (contrastRatio(rawAccent, surfaceBackground) >= minRatio) {
            return rawAccent
        }
        val target = if (isDarkSurface) Color.White else Color(0xFF0A1410)
        for (step in 1..10) {
            val candidate = lerp(rawAccent, target, step * 0.1f)
            if (contrastRatio(candidate, surfaceBackground) >= minRatio) {
                return candidate
            }
        }
        return target
    }
}

private fun lightSchemeForTheme(theme: AppTheme): ColorScheme {
    return when (theme) {
        AppTheme.EMERALD_GOLD -> lightColorScheme(
            primary = EmeraldPrimary,
            onPrimary = EmeraldOnPrimary,
            primaryContainer = EmeraldPrimaryContainer,
            onPrimaryContainer = EmeraldOnPrimaryContainer,
            secondary = EmeraldSecondary,
            onSecondary = Color.White,
            secondaryContainer = Color(0xFFFDF3D7),
            onSecondaryContainer = Color(0xFF5E4704),
            tertiary = GoldAccent,
            onTertiary = Color(0xFF1F1600),
            background = EmeraldBackground,
            surface = EmeraldSurface,
            onBackground = EmeraldOnSurface,
            onSurface = EmeraldOnSurface,
            surfaceVariant = Color(0xFFE7EFE8),
            onSurfaceVariant = Color(0xFF354A40),
            outline = Color(0xFFA9BAB0),
            outlineVariant = Color(0xFFD5E0D9)
        )
        AppTheme.WARM_CREAM -> lightColorScheme(
            primary = WarmCreamPrimary,
            onPrimary = WarmCreamOnPrimary,
            primaryContainer = WarmCreamPrimaryContainer,
            onPrimaryContainer = WarmCreamOnPrimaryContainer,
            secondary = WarmCreamSecondary,
            onSecondary = Color.White,
            secondaryContainer = Color(0xFFF2E2C9),
            onSecondaryContainer = Color(0xFF42382E),
            background = WarmCreamBackground,
            surface = WarmCreamSurface,
            onBackground = WarmCreamOnSurface,
            onSurface = WarmCreamOnSurface,
            surfaceVariant = Color(0xFFEFE4D2),
            onSurfaceVariant = Color(0xFF5A4C3E),
            outline = Color(0xFFB5A38C),
            outlineVariant = Color(0xFFD8CBB8)
        )
        AppTheme.MIDNIGHT_BLUE -> lightColorScheme(
            primary = Color(0xFF1E3A5F),
            onPrimary = Color.White,
            primaryContainer = Color(0xFFDCE6F7),
            onPrimaryContainer = Color(0xFF101C35),
            secondary = Color(0xFF9C7A33),
            onSecondary = Color.White,
            secondaryContainer = Color(0xFFF7EBD0),
            onSecondaryContainer = Color(0xFF4A380F),
            background = Color(0xFFF2F5FA),
            surface = Color(0xFFFFFFFF),
            onBackground = Color(0xFF101C35),
            onSurface = Color(0xFF101C35),
            surfaceVariant = Color(0xFFE4EBF5),
            onSurfaceVariant = Color(0xFF334766),
            outline = Color(0xFF9FB0C9),
            outlineVariant = Color(0xFFD2DCEB)
        )
        AppTheme.SAGE_GREEN -> lightColorScheme(
            primary = SagePrimary,
            onPrimary = SageOnPrimary,
            primaryContainer = SagePrimaryContainer,
            onPrimaryContainer = SageOnPrimaryContainer,
            secondary = SageSecondary,
            onSecondary = Color.White,
            secondaryContainer = Color(0xFFDDE7DA),
            onSecondaryContainer = Color(0xFF213129),
            background = SageBackground,
            surface = SageSurface,
            onBackground = SageOnSurface,
            onSurface = SageOnSurface,
            surfaceVariant = Color(0xFFE4EBE1),
            onSurfaceVariant = Color(0xFF3D5245),
            outline = Color(0xFFA0B3A5),
            outlineVariant = Color(0xFFCFDCD2)
        )
        AppTheme.OBSIDIAN_DARK -> lightColorScheme(
            primary = Color(0xFF6B5422),
            onPrimary = Color.White,
            primaryContainer = Color(0xFFF3E6C8),
            onPrimaryContainer = Color(0xFF2E230B),
            secondary = Color(0xFF4A4A4A),
            onSecondary = Color.White,
            secondaryContainer = Color(0xFFE7E4DC),
            onSecondaryContainer = Color(0xFF242424),
            background = Color(0xFFF6F5F1),
            surface = Color(0xFFFFFFFF),
            onBackground = Color(0xFF1E1E1E),
            onSurface = Color(0xFF1E1E1E),
            surfaceVariant = Color(0xFFEBE8E0),
            onSurfaceVariant = Color(0xFF4C4940),
            outline = Color(0xFFB3AEA2),
            outlineVariant = Color(0xFFDBD7CD)
        )
        AppTheme.ROSE_SAND -> lightColorScheme(
            primary = RoseSandPrimary,
            onPrimary = RoseSandOnPrimary,
            primaryContainer = RoseSandPrimaryContainer,
            onPrimaryContainer = RoseSandOnPrimaryContainer,
            secondary = Color(0xFF7D525F),
            onSecondary = Color.White,
            secondaryContainer = Color(0xFFEED9D8),
            onSecondaryContainer = Color(0xFF493B3D),
            background = RoseSandBackground,
            surface = RoseSandSurface,
            onBackground = RoseSandOnSurface,
            onSurface = RoseSandOnSurface,
            surfaceVariant = Color(0xFFE9DAD6),
            onSurfaceVariant = Color(0xFF614D50),
            outline = Color(0xFFBC9FA3),
            outlineVariant = Color(0xFFDECAC9)
        )
        AppTheme.ROYAL_TEAL -> lightColorScheme(
            primary = RoyalTealPrimary,
            onPrimary = RoyalTealOnPrimary,
            primaryContainer = RoyalTealPrimaryContainer,
            onPrimaryContainer = RoyalTealOnPrimaryContainer,
            secondary = RoyalTealSecondary,
            onSecondary = Color.White,
            secondaryContainer = Color(0xFFF7EBD0),
            onSecondaryContainer = Color(0xFF4A380F),
            background = RoyalTealBackground,
            surface = RoyalTealSurface,
            onBackground = RoyalTealOnSurface,
            onSurface = RoyalTealOnSurface,
            surfaceVariant = Color(0xFFE2ECE9),
            onSurfaceVariant = Color(0xFF335254),
            outline = Color(0xFF9DB5B7),
            outlineVariant = Color(0xFFCDE0E1)
        )
    }
}

private fun darkSchemeForTheme(theme: AppTheme): ColorScheme {
    return when (theme) {
        AppTheme.EMERALD_GOLD -> darkColorScheme(
            primary = Color(0xFF4ADE80),
            onPrimary = Color(0xFF042E1B),
            primaryContainer = Color(0xFF064E3B),
            onPrimaryContainer = Color(0xFFD1FAE5),
            secondary = Color(0xFFD4AF37),
            onSecondary = Color(0xFF1E1600),
            secondaryContainer = Color(0xFF4D3E10),
            onSecondaryContainer = Color(0xFFFEF3C7),
            background = Color(0xFF0A1612),
            surface = Color(0xFF13241D),
            onBackground = Color(0xFFE8F3ED),
            onSurface = Color(0xFFE8F3ED),
            surfaceVariant = Color(0xFF1C332A),
            onSurfaceVariant = Color(0xFFB7CCC0),
            outline = Color(0xFF4E6B5E),
            outlineVariant = Color(0xFF284237)
        )
        AppTheme.WARM_CREAM -> darkColorScheme(
            primary = Color(0xFFD7B58C),
            onPrimary = Color(0xFF2B1D10),
            primaryContainer = Color(0xFF4E3824),
            onPrimaryContainer = Color(0xFFF7E9D7),
            secondary = Color(0xFFC29B6B),
            onSecondary = Color(0xFF281B0B),
            secondaryContainer = Color(0xFF473520),
            onSecondaryContainer = Color(0xFFF5E6D3),
            background = Color(0xFF1B1510),
            surface = Color(0xFF271E17),
            onBackground = Color(0xFFF2E9DC),
            onSurface = Color(0xFFF2E9DC),
            surfaceVariant = Color(0xFF362B22),
            onSurfaceVariant = Color(0xFFCBBBA9),
            outline = Color(0xFF6E5D4C),
            outlineVariant = Color(0xFF45382B)
        )
        AppTheme.MIDNIGHT_BLUE -> darkColorScheme(
            primary = MidnightPrimary,
            onPrimary = MidnightOnPrimary,
            primaryContainer = MidnightPrimaryContainer,
            onPrimaryContainer = MidnightOnPrimaryContainer,
            secondary = MidnightSecondary,
            onSecondary = Color(0xFF0A1429),
            secondaryContainer = Color(0xFF233F69),
            onSecondaryContainer = Color(0xFFE8EDF5),
            background = MidnightBackground,
            surface = MidnightSurface,
            onBackground = MidnightOnSurface,
            onSurface = MidnightOnSurface,
            surfaceVariant = Color(0xFF20355E),
            onSurfaceVariant = Color(0xFFB9C6DE),
            outline = Color(0xFF4D6491),
            outlineVariant = Color(0xFF2B416D)
        )
        AppTheme.SAGE_GREEN -> darkColorScheme(
            primary = Color(0xFF9AC2A0),
            onPrimary = Color(0xFF142419),
            primaryContainer = Color(0xFF2A3E33),
            onPrimaryContainer = Color(0xFFDCEADF),
            secondary = Color(0xFFA7C4AD),
            onSecondary = Color(0xFF142419),
            secondaryContainer = Color(0xFF32483B),
            onSecondaryContainer = Color(0xFFE1ECE3),
            background = Color(0xFF111813),
            surface = Color(0xFF1B251E),
            onBackground = Color(0xFFE5EDE6),
            onSurface = Color(0xFFE5EDE6),
            surfaceVariant = Color(0xFF27342B),
            onSurfaceVariant = Color(0xFFB8C7BA),
            outline = Color(0xFF536958),
            outlineVariant = Color(0xFF2E3D33)
        )
        AppTheme.OBSIDIAN_DARK -> darkColorScheme(
            primary = ObsidianPrimary,
            onPrimary = ObsidianOnPrimary,
            primaryContainer = ObsidianPrimaryContainer,
            onPrimaryContainer = ObsidianOnPrimaryContainer,
            secondary = Color(0xFFD0C4A8),
            onSecondary = Color(0xFF1A1813),
            secondaryContainer = Color(0xFF332F25),
            onSecondaryContainer = Color(0xFFEDE8DC),
            background = ObsidianBackground,
            surface = ObsidianSurface,
            onBackground = ObsidianOnSurface,
            onSurface = ObsidianOnSurface,
            surfaceVariant = Color(0xFF2E2E2E),
            onSurfaceVariant = Color(0xFFC7C1B5),
            outline = Color(0xFF666055),
            outlineVariant = Color(0xFF3B3832)
        )
        AppTheme.ROSE_SAND -> darkColorScheme(
            primary = Color(0xFFDBA9B5),
            onPrimary = Color(0xFF381922),
            primaryContainer = Color(0xFF593842),
            onPrimaryContainer = Color(0xFFF7E4E7),
            secondary = Color(0xFFC9A6A5),
            onSecondary = Color(0xFF332021),
            secondaryContainer = Color(0xFF4F3739),
            onSecondaryContainer = Color(0xFFF3E2E2),
            background = Color(0xFF1D1416),
            surface = Color(0xFF291D20),
            onBackground = Color(0xFFF3E7E8),
            onSurface = Color(0xFFF3E7E8),
            surfaceVariant = Color(0xFF38292D),
            onSurfaceVariant = Color(0xFFCDB9BD),
            outline = Color(0xFF6F565C),
            outlineVariant = Color(0xFF463438)
        )
        AppTheme.ROYAL_TEAL -> darkColorScheme(
            primary = Color(0xFF5EEAD4),
            onPrimary = Color(0xFF042B2E),
            primaryContainer = Color(0xFF084B50),
            onPrimaryContainer = Color(0xFFCCFBF1),
            secondary = Color(0xFFD6B66B),
            onSecondary = Color(0xFF241B05),
            secondaryContainer = Color(0xFF473A18),
            onSecondaryContainer = Color(0xFFFAEDCA),
            background = Color(0xFF091718),
            surface = Color(0xFF112628),
            onBackground = Color(0xFFE5F2F2),
            onSurface = Color(0xFFE5F2F2),
            surfaceVariant = Color(0xFF1B3638),
            onSurfaceVariant = Color(0xFFB3CCCE),
            outline = Color(0xFF496B6E),
            outlineVariant = Color(0xFF254245)
        )
    }
}

fun resolveColorScheme(
    settings: AppSettingsState,
    systemDark: Boolean
): Pair<ColorScheme, Boolean> {
    val useDark = when (settings.appearanceMode) {
        AppearanceMode.LIGHT -> false
        AppearanceMode.DARK -> true
        AppearanceMode.SYSTEM -> if (settings.theme.isDarkDefault) true else systemDark
    }

    var scheme = if (useDark) darkSchemeForTheme(settings.theme) else lightSchemeForTheme(settings.theme)

    val accentLong = settings.customAccentHex
    if (accentLong != null && accentLong != 0L) {
        val rawAccent = Color(accentLong)
        val minContrast = if (settings.highContrastEnabled) 4.5f else 3.8f
        val safePrimary = ThemeContrastUtils.ensureReadableAccent(
            rawAccent = rawAccent,
            surfaceBackground = scheme.surface,
            isDarkSurface = useDark,
            minRatio = minContrast
        )
        val safeOnPrimary = ThemeContrastUtils.bestOnColor(safePrimary)
        val containerMix = if (useDark) 0.25f else 0.16f
        val safeContainer = lerp(scheme.surface, safePrimary, containerMix)
        val safeOnContainer = ThemeContrastUtils.bestOnColor(safeContainer)
        scheme = scheme.copy(
            primary = safePrimary,
            onPrimary = safeOnPrimary,
            primaryContainer = safeContainer,
            onPrimaryContainer = safeOnContainer
        )
    }

    if (settings.highContrastEnabled) {
        val pureOnBg = if (useDark) Color.White else Color.Black
        val pureOnSurface = if (useDark) Color.White else Color.Black
        val crispSecondaryText = if (useDark) Color(0xFFE2E8F0) else Color(0xFF1F2937)
        val boostedPrimary = ThemeContrastUtils.ensureReadableAccent(
            rawAccent = scheme.primary,
            surfaceBackground = scheme.surface,
            isDarkSurface = useDark,
            minRatio = 5.0f
        )
        scheme = scheme.copy(
            primary = boostedPrimary,
            onPrimary = ThemeContrastUtils.bestOnColor(boostedPrimary),
            onBackground = pureOnBg,
            onSurface = pureOnSurface,
            onSurfaceVariant = crispSecondaryText,
            outline = if (useDark) Color(0xFFD1D5DB) else Color(0xFF374151)
        )
    }

    return scheme to useDark
}

@Composable
fun NooreHidayatTheme(
    selectedTheme: AppTheme = AppTheme.EMERALD_GOLD,
    settings: AppSettingsState? = null,
    content: @Composable () -> Unit
) {
    val systemDark = isSystemInDarkTheme()
    val effectiveSettings = (settings ?: AppSettingsState(theme = selectedTheme))
    val (colorScheme, _) = resolveColorScheme(effectiveSettings, systemDark)
    val typography = buildAppTypography(
        scale = effectiveSettings.uiTextScale,
        fontStyle = effectiveSettings.uiFontStyle
    )

    CompositionLocalProvider(LocalAppSettings provides effectiveSettings) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = typography,
            content = content
        )
    }
}
