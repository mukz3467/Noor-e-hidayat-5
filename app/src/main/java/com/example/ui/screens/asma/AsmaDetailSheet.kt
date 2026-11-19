package com.example.ui.screens.asma

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.asma.AsmaName
import com.example.data.asma.AsmaPronunciationController
import com.example.data.asma.AsmaRepository
import com.example.data.asma.NameEvidenceCategory
import com.example.data.model.AppLanguage
import com.example.localization.AppLocalization
import com.example.ui.theme.QuranNaskhFontFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AsmaDetailSheet(
    name: AsmaName,
    language: AppLanguage,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val uriHandler = LocalUriHandler.current
    val ttsAvailability by AsmaPronunciationController.availability.collectAsStateWithLifecycle()
    val speakingNumber by AsmaPronunciationController.speakingNumber.collectAsStateWithLifecycle()
    val isSpeakingThis = speakingNumber == name.number

    LaunchedEffect(name.number) {
        AsmaPronunciationController.init(context)
    }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = {
            AsmaPronunciationController.stop()
            onDismiss()
        },
        containerColor = MaterialTheme.colorScheme.surface,
        modifier = Modifier.testTag("asma_detail_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 640.dp)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(bottom = 28.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                AssistChip(
                    onClick = {},
                    label = {
                        Text(
                            text = evidenceLabel(name.evidenceCategory, language),
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold)
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Verified,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                )
                Text(
                    text = "#${name.number} / 99",
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.55f))
                    .padding(vertical = 22.dp, horizontal = 16.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = name.arabic,
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontFamily = QuranNaskhFontFamily,
                            fontSize = 42.sp,
                            lineHeight = 56.sp
                        ),
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = name.transliteration,
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = name.localizedMeaning(language),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Honest Pronunciation Controls
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = AppLocalization.get("asma_pronunciation_title", language),
                                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = when (ttsAvailability) {
                                    AsmaPronunciationController.TtsAvailability.AVAILABLE_DEVICE_TTS ->
                                        AppLocalization.get("asma_pronunciation_tts_note", language)
                                    AsmaPronunciationController.TtsAvailability.UNAVAILABLE ->
                                        AppLocalization.get("asma_pronunciation_unavailable", language)
                                    AsmaPronunciationController.TtsAvailability.CHECKING ->
                                        AppLocalization.get("asma_pronunciation_checking", language)
                                },
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        FilledTonalButton(
                            onClick = { AsmaPronunciationController.toggleSpeak(context, name) },
                            enabled = ttsAvailability == AsmaPronunciationController.TtsAvailability.AVAILABLE_DEVICE_TTS,
                            modifier = Modifier
                                .height(48.dp)
                                .testTag("asma_detail_audio_btn")
                        ) {
                            Icon(
                                imageVector = if (isSpeakingThis) Icons.Default.Stop else Icons.Default.VolumeUp,
                                contentDescription = AppLocalization.get("asma_pronunciation_title", language)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isSpeakingThis) AppLocalization.get("stop", language)
                                else AppLocalization.get("play", language)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = AppLocalization.get("asma_meaning_context", language),
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = name.localizedExplanation(language),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

            if (language != AppLanguage.ARABIC) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = name.explanationAr,
                    style = MaterialTheme.typography.bodyMedium.copy(fontFamily = QuranNaskhFontFamily, fontSize = 18.sp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(14.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = AppLocalization.get("asma_sources_label", language),
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(6.dp))
            if (!name.quranReference.isNullOrBlank()) {
                Text(
                    text = "Quran: ${name.quranReference}",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            if (!name.hadithReference.isNullOrBlank()) {
                Text(
                    text = "Hadith / Reference: ${name.hadithReference}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            OutlinedButton(
                onClick = { runCatching { uriHandler.openUri(name.sourceUrl) } },
                modifier = Modifier
                    .height(48.dp)
                    .testTag("asma_detail_source_btn")
            ) {
                Icon(Icons.Default.OpenInNew, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = name.sourceUrl)
            }

            Spacer(modifier = Modifier.height(18.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = onToggleFavorite,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("asma_detail_fav_btn")
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isFavorite) AppLocalization.get("asma_saved_fav", language)
                        else AppLocalization.get("asma_add_fav", language)
                    )
                }

                OutlinedButton(
                    onClick = {
                        val shareIntent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, AsmaRepository.buildShareText(name, language))
                        }
                        runCatching {
                            context.startActivity(Intent.createChooser(shareIntent, name.transliteration))
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("asma_detail_share_btn")
                ) {
                    Icon(Icons.Default.Share, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = AppLocalization.get("share", language))
                }
            }
        }
    }
}

internal fun evidenceLabel(category: NameEvidenceCategory, language: AppLanguage): String = when (category) {
    NameEvidenceCategory.QURAN_EXPLICIT -> when (language) {
        AppLanguage.ENGLISH -> "Explicit in Quran"
        AppLanguage.URDU -> "قرآنِ مجید میں صریح"
        AppLanguage.ARABIC -> "ثابت في القرآن"
        AppLanguage.HINDI -> "क़ुरआन में स्पष्ट"
    }
    NameEvidenceCategory.AUTHENTIC_HADITH -> when (language) {
        AppLanguage.ENGLISH -> "Authentic Hadith"
        AppLanguage.URDU -> "صحیح حدیث سے ثابت"
        AppLanguage.ARABIC -> "ثابت في السنة الصحيحة"
        AppLanguage.HINDI -> "सहीह हदीस में वर्णित"
    }
    NameEvidenceCategory.SCHOLARLY_COMPILATION -> when (language) {
        AppLanguage.ENGLISH -> "Scholarly Compilation"
        AppLanguage.URDU -> "علمی فہرستِ اسماء"
        AppLanguage.ARABIC -> "قائمة علمية مأثورة"
        AppLanguage.HINDI -> "विद्वान संकलन"
    }
}
