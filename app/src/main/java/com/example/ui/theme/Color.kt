package com.example.ui.theme

import androidx.compose.ui.graphics.Color

// Sleek Interface Design Theme Palette
val CanvasDark = Color(0xFF070913)          // bg-[#070913]
val SlateCard = Color(0xFF131D31)           // bg-[#131D31]
val SlateCardGradientEnd = Color(0xFF0A1220)// to-[#0A1220]
val SlateCardVariant = Color(0xFF1A233A)    // Elevated slate container
val SlateSubCard = Color(0x80070913)        // bg-[#070913]/50
val SlateBorder = Color(0xFF1E293B)         // border-slate-800
val SlateBorderLight = Color(0xFF334155)    // border-slate-700
val SlateTrack = Color(0xFF1E293B)          // bg-slate-800 track

val NeonMint = Color(0xFF00F5A0)            // #00F5A0 primary accent
val NeonMintDark = Color(0xFF00D98B)        // #00D98B gradient accent
val CyberGold = Color(0xFFFFB800)           // #FFB800 gold highlight
val ElectricCyan = Color(0xFF00D2FF)        // Electric telemetry cyan
val CoralRed = Color(0xFFFF4D6D)            // Negative / debit red

val TextPrimary = Color(0xFFF1F5F9)         // text-slate-100
val TextSecondary = Color(0xFF94A3B8)       // text-slate-400
val TextMuted = Color(0xFF64748B)           // text-slate-500
val ButtonWhite = Color(0xFFFFFFFF)         // bg-white sleek button

// Material 3 mappings
val md_theme_dark_primary = NeonMint
val md_theme_dark_onPrimary = Color(0xFF070913)
val md_theme_dark_primaryContainer = Color(0xFF003822)
val md_theme_dark_onPrimaryContainer = Color(0xFF6FFFC5)

val md_theme_dark_secondary = CyberGold
val md_theme_dark_onSecondary = Color(0xFF412D00)
val md_theme_dark_secondaryContainer = Color(0xFF5E4200)
val md_theme_dark_onSecondaryContainer = Color(0xFFFFDEA8)

val md_theme_dark_tertiary = ElectricCyan
val md_theme_dark_onTertiary = Color(0xFF003642)
val md_theme_dark_tertiaryContainer = Color(0xFF004E5F)
val md_theme_dark_onTertiaryContainer = Color(0xFFA5EEFF)

val md_theme_dark_error = CoralRed
val md_theme_dark_onError = Color(0xFF69001D)

val md_theme_dark_background = CanvasDark
val md_theme_dark_onBackground = TextPrimary

val md_theme_dark_surface = SlateCard
val md_theme_dark_onSurface = TextPrimary
val md_theme_dark_surfaceVariant = SlateCardVariant
val md_theme_dark_onSurfaceVariant = TextSecondary
val md_theme_dark_outline = SlateBorder
