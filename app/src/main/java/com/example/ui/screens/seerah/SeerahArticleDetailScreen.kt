package com.example.ui.screens.seerah

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppLanguage
import com.example.data.seerah.SeerahArticle
import com.example.data.seerah.SeerahCitation
import com.example.data.seerah.SeerahRepository
import com.example.data.seerah.SourceCredibilityTier
import com.example.localization.AppLocalization
import com.example.ui.components.NoorTopBar
import com.example.ui.theme.QuranNaskhFontFamily

@Composable
fun SeerahArticleDetailScreen(
    article: SeerahArticle,
    language: AppLanguage,
    isRead: Boolean,
    isBookmarked: Boolean,
    onToggleRead: () -> Unit,
    onToggleBookmark: () -> Unit,
    onNavigateToArticle: (String) -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val prevArticle = remember(article.id) { SeerahRepository.previousArticle(article.id) }
    val nextArticle = remember(article.id) { SeerahRepository.nextArticle(article.id) }

    val tier1Citations = remember(article.id) {
        article.citations.filter { it.tier == SourceCredibilityTier.TIER_1_QURAN_HADITH }
    }
    val secondaryCitations = remember(article.id) {
        article.citations.filter { it.tier != SourceCredibilityTier.TIER_1_QURAN_HADITH }
    }

    Scaffold(
        topBar = {
            NoorTopBar(
                title = "${article.orderIndex}. ${article.era.title(language)}",
                language = language,
                onBackClick = onBack,
                actions = {
                    IconButton(
                        onClick = onToggleRead,
                        modifier = Modifier
                            .minimumInteractiveComponentSize()
                            .testTag("seerah_detail_read_btn")
                    ) {
                        Icon(
                            imageVector = if (isRead) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                            contentDescription = AppLocalization.get("seerah_mark_read", language),
                            tint = if (isRead) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    IconButton(
                        onClick = onToggleBookmark,
                        modifier = Modifier
                            .minimumInteractiveComponentSize()
                            .testTag("seerah_detail_bookmark_btn")
                    ) {
                        Icon(
                            imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = AppLocalization.get("seerah_bookmark_action", language),
                            tint = if (isBookmarked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    IconButton(
                        onClick = {
                            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_SUBJECT, article.textFor(article.title, language))
                                putExtra(Intent.EXTRA_TEXT, SeerahRepository.buildShareText(article, language))
                            }
                            runCatching {
                                context.startActivity(Intent.createChooser(shareIntent, AppLocalization.get("seerah_share_title", language)))
                            }
                        },
                        modifier = Modifier
                            .minimumInteractiveComponentSize()
                            .testTag("seerah_detail_share_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = AppLocalization.get("seerah_share_title", language)
                        )
                    }
                }
            )
        },
        bottomBar = {
            Surface(
                tonalElevation = 3.dp,
                color = MaterialTheme.colorScheme.surface
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 10.dp)
                ) {
                    OutlinedButton(
                        onClick = { prevArticle?.let { onNavigateToArticle(it.id) } },
                        enabled = prevArticle != null,
                        modifier = Modifier
                            .defaultMinSize(minHeight = 48.dp)
                            .testTag("seerah_prev_btn")
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(AppLocalization.get("seerah_prev_article", language))
                    }
                    Button(
                        onClick = { nextArticle?.let { onNavigateToArticle(it.id) } },
                        enabled = nextArticle != null,
                        modifier = Modifier
                            .defaultMinSize(minHeight = 48.dp)
                            .testTag("seerah_next_btn")
                    ) {
                        Text(AppLocalization.get("seerah_next_article", language))
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(18.dp))
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            verticalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Section 1: Title & Era Badge
            Column(modifier = Modifier.testTag("seerah_detail_title")) {
                AssistChip(
                    onClick = {},
                    label = { Text("${article.era.orderNumber}. ${article.era.title(language)}") }
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = article.textFor(article.title, language),
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            // Section 2: Approximate Chronology & Date Note
            ReaderSectionCard(
                title = AppLocalization.get("seerah_sec_chronology", language),
                body = article.textFor(article.approxChronology, language),
                testTag = "seerah_detail_chronology"
            )

            // Section 3: Verified Historical Overview
            ReaderSectionCard(
                title = AppLocalization.get("seerah_sec_overview", language),
                body = article.textFor(article.overview, language),
                testTag = "seerah_detail_overview"
            )

            // Section 4: Key Context, People & Places
            ReaderSectionCard(
                title = AppLocalization.get("seerah_sec_context", language),
                body = article.textFor(article.contextPeoplePlaces, language),
                testTag = "seerah_detail_context"
            )

            // Section 5: Tier 1 Quran & Sahih Hadith Citations
            if (tier1Citations.isNotEmpty()) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.testTag("seerah_detail_tier1_citations")
                ) {
                    Text(
                        text = AppLocalization.get("seerah_sec_tier1", language),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary
                    )
                    tier1Citations.forEach { citation ->
                        CitationCard(citation = citation, language = language)
                    }
                }
            }

            // Section 6: Tier 2 / Tier 3 Historical Seerah Reports (with explicit reliability distinction)
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.testTag("seerah_detail_secondary_citations")
            ) {
                Text(
                    text = AppLocalization.get("seerah_sec_tier2_3", language),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.secondary
                )
                if (secondaryCitations.isEmpty()) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = AppLocalization.get("seerah_tier1_sufficient_note", language),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                } else {
                    secondaryCitations.forEach { citation ->
                        CitationCard(citation = citation, language = language)
                    }
                }
            }

            // Section 7: Differences of Historical Reports
            ReaderSectionCard(
                title = AppLocalization.get("seerah_sec_differences", language),
                body = article.textFor(article.historicalDifferences, language),
                testTag = "seerah_detail_differences"
            )

            // Section 8: Spiritual & Practical Lessons Today
            ReaderSectionCard(
                title = AppLocalization.get("seerah_sec_lessons", language),
                body = article.textFor(article.lessonsToday, language),
                highlight = true,
                testTag = "seerah_detail_lessons"
            )

            // Section 9: Next Chronological Milestone
            ReaderSectionCard(
                title = AppLocalization.get("seerah_sec_next_milestone", language),
                body = article.textFor(article.nextMilestoneNote, language),
                testTag = "seerah_detail_next_milestone"
            )

            // Section 10: Primary Source Attribution & External Verification Links
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("seerah_detail_links")
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = AppLocalization.get("seerah_sec_verification", language),
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    article.referenceLinks.forEach { (label, url) ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .defaultMinSize(minHeight = 48.dp)
                                .clickable {
                                    runCatching {
                                        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                                    }
                                }
                                .padding(vertical = 4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.OpenInNew,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = label,
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                                color = MaterialTheme.colorScheme.primary,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ReaderSectionCard(
    title: String,
    body: String,
    highlight: Boolean = false,
    testTag: String
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (highlight) {
                MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.65f)
            } else {
                MaterialTheme.colorScheme.surface
            }
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (highlight) 0.dp else 1.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag(testTag)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = body,
                style = MaterialTheme.typography.bodyMedium,
                color = if (highlight) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun CitationCard(
    citation: SeerahCitation,
    language: AppLanguage
) {
    val isTier1 = citation.tier == SourceCredibilityTier.TIER_1_QURAN_HADITH
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isTier1) {
                MaterialTheme.colorScheme.surface
            } else {
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.72f)
            }
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isTier1) 2.dp else 0.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = if (isTier1) {
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.14f)
                } else {
                    MaterialTheme.colorScheme.secondary.copy(alpha = 0.16f)
                }
            ) {
                Text(
                    text = citation.tier.shortBadge(language),
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = if (isTier1) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "${citation.sourceName} • ${citation.referenceCode}",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
            if (!citation.arabicQuote.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = citation.arabicQuote,
                    fontFamily = QuranNaskhFontFamily,
                    fontSize = 20.sp,
                    lineHeight = 34.sp,
                    textAlign = TextAlign.End,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = citation.quoteTranslation[language] ?: citation.quoteTranslation[AppLanguage.ENGLISH].orEmpty(),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = citation.reliabilityNote[language] ?: citation.reliabilityNote[AppLanguage.ENGLISH].orEmpty(),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
