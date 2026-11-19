package com.example.ui.theme

import androidx.compose.ui.graphics.Color
import com.example.data.model.AppAccentPreset

// 1. Emerald & Gold (Default - Rich Sacred Green, Gold Accent, Warm Ivory Paper)
val EmeraldPrimary = Color(0xFF064E3B)
val EmeraldOnPrimary = Color(0xFFFFFFFF)
val EmeraldPrimaryContainer = Color(0xFFD1FAE5)
val EmeraldOnPrimaryContainer = Color(0xFF022C22)
val EmeraldSecondary = Color(0xFF0F766E)
val GoldAccent = Color(0xFFD4AF37)
val GoldAccentDark = Color(0xFFB8860B)
val EmeraldSurface = Color(0xFFFFFFFF)
val EmeraldBackground = Color(0xFFF8F5EA)
val EmeraldOnSurface = Color(0xFF20332B)

// 2. Warm Cream (Classical Paper - Parchment, Bronze Accent, Walnut Text)
val WarmCreamPrimary = Color(0xFF6E5034)
val WarmCreamOnPrimary = Color(0xFFFFFFFF)
val WarmCreamPrimaryContainer = Color(0xFFEDE0CE)
val WarmCreamOnPrimaryContainer = Color(0xFF3E2B18)
val WarmCreamSecondary = Color(0xFF9B7952)
val WarmCreamAccent = Color(0xFF9B7952)
val WarmCreamSurface = Color(0xFFE8DCC8)
val WarmCreamBackground = Color(0xFFF7F1E5)
val WarmCreamOnSurface = Color(0xFF42382E)

// 3. Midnight Blue (Deep Indigo Night, Metallic Gold Accent, Soft Silver Text)
val MidnightPrimary = Color(0xFFC6A45C)
val MidnightOnPrimary = Color(0xFF101C35)
val MidnightPrimaryContainer = Color(0xFF1E3A5F)
val MidnightOnPrimaryContainer = Color(0xFFE8EDF5)
val MidnightSecondary = Color(0xFF5B8DEF)
val MidnightAccent = Color(0xFFC6A45C)
val MidnightBackground = Color(0xFF101C35)
val MidnightSurface = Color(0xFF172748)
val MidnightOnSurface = Color(0xFFE8EDF5)

// 4. Sage Green (Calm Organic Forest, Pale Sage Mist, Muted Sage Tint)
val SagePrimary = Color(0xFF344E41)
val SageOnPrimary = Color(0xFFFFFFFF)
val SagePrimaryContainer = Color(0xFFC9D8C5)
val SageOnPrimaryContainer = Color(0xFF1A2A22)
val SageSecondary = Color(0xFF6B8F71)
val SageAccent = Color(0xFF52796F)
val SageBackground = Color(0xFFF3F5EE)
val SageSurface = Color(0xFFFFFFFF)
val SageOnSurface = Color(0xFF213129)

// 5. Obsidian Dark (AMOLED True Dark, Warm Slate Cards, Soft Champagne Accent)
val ObsidianPrimary = Color(0xFFB99A55)
val ObsidianOnPrimary = Color(0xFF121212)
val ObsidianPrimaryContainer = Color(0xFF342C1D)
val ObsidianOnPrimaryContainer = Color(0xFFF6E7C1)
val ObsidianSecondary = Color(0xFF93A89C)
val ObsidianAccent = Color(0xFFB99A55)
val ObsidianBackground = Color(0xFF121212)
val ObsidianSurface = Color(0xFF242424)
val ObsidianOnSurface = Color(0xFFEDE8DC)

// 6. Rose Sand (Soft Dusty Mauve, Clay Surface, Sand Mist, Espresso Text)
val RoseSandPrimary = Color(0xFF8B6570)
val RoseSandOnPrimary = Color(0xFFFFFFFF)
val RoseSandPrimaryContainer = Color(0xFFEBD6D5)
val RoseSandOnPrimaryContainer = Color(0xFF492933)
val RoseSandSecondary = Color(0xFFC9A6A5)
val RoseSandAccent = Color(0xFFA0737E)
val RoseSandBackground = Color(0xFFF1E5E1)
val RoseSandSurface = Color(0xFFFAF4F2)
val RoseSandOnSurface = Color(0xFF493B3D)

// 7. Royal Teal (Cerulean Ocean Depth, Brass Accent, Mint Cream Background)
val RoyalTealPrimary = Color(0xFF064B50)
val RoyalTealOnPrimary = Color(0xFFFFFFFF)
val RoyalTealPrimaryContainer = Color(0xFFCCE9EA)
val RoyalTealOnPrimaryContainer = Color(0xFF032D30)
val RoyalTealSecondary = Color(0xFF087E83)
val RoyalTealAccent = Color(0xFFD6B66B)
val RoyalTealBackground = Color(0xFFF1F6F3)
val RoyalTealSurface = Color(0xFFFFFFFF)
val RoyalTealOnSurface = Color(0xFF163235)

// Curated Custom Accent Presets (validated at runtime for WCAG contrast)
val AppAccentPresets = listOf(
    AppAccentPreset("theme_default", 0x00000000L, "Theme Default", "تھیم کا اصل رنگ", "لون المظهر الافتراضي", "थीम डिफ़ॉल्ट"),
    AppAccentPreset("sacred_emerald", 0xFF064E3BL, "Sacred Emerald", "زمرد سبز", "زمردي عميق", "पन्ना हरा"),
    AppAccentPreset("imperial_gold", 0xFFB8860BL, "Antique Gold", "سنہری", "ذهبي عتيق", "सुनहरा"),
    AppAccentPreset("royal_teal", 0xFF087E83L, "Royal Teal", "شاہی فیروزی", "فيروزي ملكي", "रॉयल टील"),
    AppAccentPreset("sapphire_night", 0xFF1E40AFL, "Sapphire Blue", "نیلم نیلا", "أزرق ياقوتي", "नीलम नीला"),
    AppAccentPreset("crimson_rose", 0xFF9F1239L, "Damask Rose", "دمشقی گلاب", "وردي دمشقي", "गुलाबी आभा"),
    AppAccentPreset("saffron_amber", 0xFFB45309L, "Warm Amber", "زعفرانی عنبر", "كهرماني دافئ", "गर्म अंबर"),
    AppAccentPreset("cypress_sage", 0xFF344E41L, "Cypress Sage", "سرو سبز", "أخضر السرو", "सरू हरा")
)
