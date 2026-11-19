package com.example.ui.screens.shifa

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppLanguage
import com.example.data.shifa.ResolvedShifaEntry
import com.example.data.shifa.ShifaItemType
import com.example.data.shifa.ShifaReferenceSource
import com.example.data.shifa.ShifaTibHadithData
import com.example.localization.AppLocalization
import com.example.ui.theme.LocalAppSettings
import com.example.ui.theme.resolveArabicFontFamily

@Composable
fun ShifaScopeNoticeBanner(
    language: AppLanguage,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.65f),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary.copy(alpha = 0.25f)),
        modifier = modifier
            .fillMaxWidth()
            .testTag("shifa_scope_notice_banner")
    ) {
        Row(
            verticalAlignment = Alignment.Top,
            modifier = Modifier.padding(12.dp)
        ) {
            Icon(
                imageVector = Icons.Default.VerifiedUser,
                contentDescription = AppLocalization.get("shifa_scope_title", language),
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier
                    .padding(top = 2.dp)
                    .size(20.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = AppLocalization.get("shifa_scope_title", language),
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = AppLocalization.get("shifa_scope_body", language),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }
    }
}

@Composable
fun ShifaUnmappedConditionBanner(
    language: AppLanguage,
    onOpenQuranicShifa: () -> Unit,
    onOpenRuqyah: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        modifier = modifier
            .fillMaxWidth()
            .testTag("shifa_unmapped_condition_banner")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = AppLocalization.get("shifa_unmapped_title", language),
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = AppLocalization.get("shifa_unmapped_title", language),
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            // Exact required guidance notice preserved faithfully
            Text(
                text = ShifaTibHadithData.EXACT_UNMAPPED_CONDITION_NOTICE,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            if (language != AppLanguage.ENGLISH) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = AppLocalization.get("shifa_unmapped_localized_note", language),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                FilledTonalButton(
                    onClick = onOpenQuranicShifa,
                    modifier = Modifier
                        .weight(1f)
                        .minimumInteractiveComponentSize()
                        .testTag("shifa_fallback_quranic_btn")
                ) {
                    Text(
                        text = AppLocalization.get("shifa_cat_quranic", language),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                FilledTonalButton(
                    onClick = onOpenRuqyah,
                    modifier = Modifier
                        .weight(1f)
                        .minimumInteractiveComponentSize()
                        .testTag("shifa_fallback_ruqyah_btn")
                ) {
                    Text(
                        text = AppLocalization.get("shifa_cat_ruqyah", language),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ShifaEntryCard(
    context: Context,
    entry: ResolvedShifaEntry,
    language: AppLanguage,
    isFavorite: Boolean,
    isBookmarked: Boolean,
    isPlayingAudio: Boolean,
    isBufferingAudio: Boolean,
    onToggleFavorite: () -> Unit,
    onToggleBookmark: () -> Unit,
    onToggleAudio: () -> Unit,
    onLoadOnlineTafseer: () -> Unit,
    modifier: Modifier = Modifier
) {
    val item = entry.item
    val uriHandler = LocalUriHandler.current
    val appSettings = LocalAppSettings.current
    val arabicFont = resolveArabicFontFamily(appSettings.arabicFontOption)

    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.65f)),
        modifier = modifier
            .fillMaxWidth()
            .testTag("shifa_card_${item.id}")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // 1. Topic badge + Bookmark / Favorite / Share action row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier.weight(1f, fill = false)
                ) {
                    Text(
                        text = item.topicTag.get(language),
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                IconButton(
                    onClick = onToggleFavorite,
                    modifier = Modifier
                        .minimumInteractiveComponentSize()
                        .testTag("shifa_fav_${item.id}")
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = AppLocalization.get("shifa_action_favorite", language),
                        tint = if (isFavorite) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                IconButton(
                    onClick = onToggleBookmark,
                    modifier = Modifier
                        .minimumInteractiveComponentSize()
                        .testTag("shifa_bm_${item.id}")
                ) {
                    Icon(
                        imageVector = if (isBookmarked) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                        contentDescription = AppLocalization.get("shifa_action_bookmark", language),
                        tint = if (isBookmarked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                IconButton(
                    onClick = { shareShifaEntry(context, entry, language) },
                    modifier = Modifier
                        .minimumInteractiveComponentSize()
                        .testTag("shifa_share_${item.id}")
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = AppLocalization.get("shifa_action_share", language),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Entry Title & Surah/Hadith reference subtitle
            Text(
                text = item.title.get(language),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
            val metaLine = buildString {
                entry.surahLabel?.let { append(it) }
                if (item.narrator.isNotBlank()) {
                    if (isNotEmpty()) append(" • ")
                    append(item.narrator)
                }
            }
            if (metaLine.isNotBlank()) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = metaLine,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            // 2. Original Arabic Script Block (always preserved in authentic RTL script)
            if (entry.arabicText.isNotBlank()) {
                Spacer(modifier = Modifier.height(12.dp))
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.28f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("shifa_arabic_${item.id}")
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = AppLocalization.get("shifa_section_arabic", language),
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                            Text(
                                text = entry.arabicText,
                                fontFamily = arabicFont,
                                fontSize = (appSettings.arabicFontSize.coerceIn(20f, 30f)).sp,
                                lineHeight = (appSettings.arabicFontSize.coerceIn(20f, 30f) * 1.65f).sp,
                                color = MaterialTheme.colorScheme.onSurface,
                                textAlign = TextAlign.Right,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }

            // Audio Playback Bar for verified Quranic verses
            if (item.supportsQuranAudio) {
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedButton(
                    onClick = onToggleAudio,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .minimumInteractiveComponentSize()
                        .testTag("shifa_audio_${item.id}")
                ) {
                    if (isBufferingAudio) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Icon(
                            imageVector = if (isPlayingAudio) Icons.Default.Stop else Icons.Default.VolumeUp,
                            contentDescription = AppLocalization.get("shifa_action_audio", language),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = when {
                            isBufferingAudio -> AppLocalization.get("shifa_audio_loading", language)
                            isPlayingAudio -> AppLocalization.get("shifa_audio_stop", language)
                            else -> AppLocalization.get("shifa_audio_play", language)
                        }
                    )
                }
            }

            // 3. Translation Block (distinct from original Arabic and Tafseer)
            if (entry.translationText.isNotBlank()) {
                Spacer(modifier = Modifier.height(12.dp))
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("shifa_translation_${item.id}")
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = AppLocalization.get("shifa_section_translation", language),
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        val transDir = if (language.isRtl) LayoutDirection.Rtl else LayoutDirection.Ltr
                        CompositionLocalProvider(LocalLayoutDirection provides transDir) {
                            Text(
                                text = entry.translationText,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                        if (entry.translationAttribution.isNotBlank()) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = entry.translationAttribution,
                                style = MaterialTheme.typography.labelSmall.copy(fontStyle = FontStyle.Italic),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            // 4. Authentic Tafseer Section (for Quran passages — never mixes Quran translation)
            if (item.type == ShifaItemType.QURAN_AYAH) {
                Spacer(modifier = Modifier.height(10.dp))
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.35f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("shifa_tafseer_${item.id}")
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = AppLocalization.get("shifa_section_tafseer", language),
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        val activeTafseerText = entry.onlineTafseerText ?: entry.offlineTafseerText
                        val activeTafseerAttr = entry.onlineTafseerAttribution ?: entry.offlineTafseerAttribution

                        if (!activeTafseerText.isNullOrBlank()) {
                            Text(
                                text = activeTafseerText,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            if (!activeTafseerAttr.isNullOrBlank()) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = activeTafseerAttr,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        } else {
                            Text(
                                text = AppLocalization.get("shifa_tafseer_offline_note", language),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        if (entry.onlineTafseerText == null) {
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedButton(
                                onClick = onLoadOnlineTafseer,
                                enabled = !entry.isLoadingOnlineTafseer,
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .minimumInteractiveComponentSize()
                                    .testTag("shifa_load_tafseer_${item.id}")
                            ) {
                                if (entry.isLoadingOnlineTafseer) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(16.dp),
                                        strokeWidth = 2.dp
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                }
                                Text(text = AppLocalization.get("shifa_load_online_tafseer", language))
                            }
                        }
                        if (!entry.onlineTafseerError.isNullOrBlank()) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = entry.onlineTafseerError,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            }

            // 5. Islamic Context
            Spacer(modifier = Modifier.height(10.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.30f))
                    .padding(12.dp)
            ) {
                Text(
                    text = AppLocalization.get("shifa_section_context", language),
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = item.islamicContext.get(language),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            // 6. Tib-e-Nabawi Strict Separation: Prophet's ﷺ Established Action vs Scholarly Explanation
            if (item.propheticAction != null || item.scholarlyExplanation != null) {
                item.propheticAction?.let { actionText ->
                    Spacer(modifier = Modifier.height(8.dp))
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.25f),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("shifa_prophetic_action_${item.id}")
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = AppLocalization.get("shifa_prophetic_action_heading", language),
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(3.dp))
                            Text(
                                text = actionText.get(language),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
                item.scholarlyExplanation?.let { scholarText ->
                    Spacer(modifier = Modifier.height(8.dp))
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.30f),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("shifa_scholar_explanation_${item.id}")
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = AppLocalization.get("shifa_scholar_explanation_heading", language),
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.secondary
                            )
                            Spacer(modifier = Modifier.height(3.dp))
                            Text(
                                text = scholarText.get(language),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }

            // 7. Sunnah Recitation Method & Verified Count (only when explicitly proven in Hadith)
            if (item.recitationMethod != null || item.establishedCountNote != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.35f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        item.recitationMethod?.let { method ->
                            Text(
                                text = AppLocalization.get("shifa_recitation_method_heading", language),
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.secondary
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = method.get(language),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        item.establishedCountNote?.let { count ->
                            if (item.recitationMethod != null) Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = count.get(language),
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }

            // 8. Verified Source Links (Clickable)
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = AppLocalization.get("shifa_verified_references", language),
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(6.dp))
            val refs = listOf(item.primaryReference) + item.additionalReferences
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                refs.forEach { ref ->
                    ShifaReferenceChip(
                        ref = ref,
                        onOpen = {
                            runCatching { uriHandler.openUri(ref.url) }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun ShifaReferenceChip(
    ref: ShifaReferenceSource,
    onOpen: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        modifier = Modifier
            .minimumInteractiveComponentSize()
            .clip(RoundedCornerShape(10.dp))
            .clickable(onClick = onOpen)
            .testTag("shifa_ref_${ref.bookAndNumber}")
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Verified,
                contentDescription = ref.authenticityGrade,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Column {
                Text(
                    text = ref.bookAndNumber,
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = ref.authenticityGrade,
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(modifier = Modifier.width(6.dp))
            Icon(
                imageVector = Icons.AutoMirrored.Filled.OpenInNew,
                contentDescription = ref.url,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(14.dp)
            )
        }
    }
}

private fun shareShifaEntry(
    context: Context,
    entry: ResolvedShifaEntry,
    language: AppLanguage
) {
    val shareText = buildString {
        appendLine(entry.item.title.get(language))
        entry.surahLabel?.let { appendLine(it) }
        if (entry.arabicText.isNotBlank()) {
            appendLine()
            appendLine(entry.arabicText)
        }
        if (entry.translationText.isNotBlank()) {
            appendLine()
            appendLine(entry.translationText)
        }
        appendLine()
        appendLine(entry.item.islamicContext.get(language))
        appendLine()
        append("Source: ${entry.item.primaryReference.bookAndNumber} (${entry.item.primaryReference.url})")
    }
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, entry.item.title.get(language))
        putExtra(Intent.EXTRA_TEXT, shareText)
    }
    runCatching {
        context.startActivity(Intent.createChooser(intent, entry.item.title.get(language)))
    }
}
