package com.example.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.ArabicFontOption
import com.example.data.model.UiFontStyleOption

// Authentic Quranic Naskh / Uthmani Calligraphy Font Families
val QuranNaskhFontFamily = FontFamily(
    Font(R.font.amiri, FontWeight.Normal)
)

val NotoNaskhArabicFamily = FontFamily(
    Font(R.font.noto_naskh_arabic, FontWeight.Normal)
)

fun resolveArabicFontFamily(option: ArabicFontOption): FontFamily {
    return when (option) {
        ArabicFontOption.AMIRI -> QuranNaskhFontFamily
        ArabicFontOption.NOTO_NASKH -> NotoNaskhArabicFamily
        ArabicFontOption.SYSTEM -> FontFamily.Default
    }
}

fun buildAppTypography(
    scale: Float = 1.0f,
    fontStyle: UiFontStyleOption = UiFontStyleOption.DEFAULT
): Typography {
    val s = scale.coerceIn(0.85f, 1.35f)
    val family = when (fontStyle) {
        UiFontStyleOption.SERIF -> FontFamily.Serif
        UiFontStyleOption.SANS_CLEAN -> FontFamily.SansSerif
        UiFontStyleOption.DEFAULT -> FontFamily.Default
    }
    return Typography(
        headlineMedium = TextStyle(
            fontFamily = family,
            fontWeight = FontWeight.Bold,
            fontSize = (28f * s).sp,
            lineHeight = (36f * s).sp
        ),
        headlineSmall = TextStyle(
            fontFamily = family,
            fontWeight = FontWeight.Bold,
            fontSize = (24f * s).sp,
            lineHeight = (32f * s).sp
        ),
        titleLarge = TextStyle(
            fontFamily = family,
            fontWeight = FontWeight.Bold,
            fontSize = (20f * s).sp,
            lineHeight = (28f * s).sp
        ),
        titleMedium = TextStyle(
            fontFamily = family,
            fontWeight = FontWeight.SemiBold,
            fontSize = (16f * s).sp,
            lineHeight = (24f * s).sp
        ),
        titleSmall = TextStyle(
            fontFamily = family,
            fontWeight = FontWeight.Medium,
            fontSize = (14f * s).sp,
            lineHeight = (20f * s).sp
        ),
        bodyLarge = TextStyle(
            fontFamily = family,
            fontWeight = FontWeight.Normal,
            fontSize = (16f * s).sp,
            lineHeight = (24f * s).sp,
            letterSpacing = 0.25.sp
        ),
        bodyMedium = TextStyle(
            fontFamily = family,
            fontWeight = FontWeight.Normal,
            fontSize = (14f * s).sp,
            lineHeight = (20f * s).sp
        ),
        bodySmall = TextStyle(
            fontFamily = family,
            fontWeight = FontWeight.Normal,
            fontSize = (12f * s).sp,
            lineHeight = (16f * s).sp
        ),
        labelLarge = TextStyle(
            fontFamily = family,
            fontWeight = FontWeight.Medium,
            fontSize = (14f * s).sp,
            lineHeight = (20f * s).sp
        ),
        labelMedium = TextStyle(
            fontFamily = family,
            fontWeight = FontWeight.Medium,
            fontSize = (12f * s).sp,
            lineHeight = (16f * s).sp
        ),
        labelSmall = TextStyle(
            fontFamily = family,
            fontWeight = FontWeight.Medium,
            fontSize = (11f * s).sp,
            lineHeight = (16f * s).sp
        )
    )
}

// Default unscaled baseline typography for any direct legacy references
val Typography = buildAppTypography()
