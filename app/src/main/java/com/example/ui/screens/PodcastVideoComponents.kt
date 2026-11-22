package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.HistoryEdu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.AppLanguage
import com.example.data.podcast.PodcastFeedStatus
import com.example.data.podcast.PodcastProfileType
import com.example.data.podcast.PodcastScholarProfile
import com.example.data.podcast.PodcastSourceKind
import com.example.data.podcast.VerifiedPodcastVideo
import com.example.data.repository.YouTubeRepository
import com.example.data.repository.YouTubeVideoItem
import com.example.localization.AppLocalization
import com.example.ui.components.InAppYouTubePlayer

@Composable
internal fun ActivePodcastPlayerSection(
    videoId: String,
    title: String,
    channelName: String,
    videoUrl: String,
    languageBadge: String,
    sourceBadge: String?,
    language: AppLanguage,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    onDurationDetected: (Int) -> Unit
) {
    val context = LocalContext.current

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = Color.Black),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(230.dp)
                .testTag("in_app_podcast_player")
        ) {
            key(videoId) {
                InAppYouTubePlayer(
                    videoId = videoId,
                    language = language,
                    modifier = Modifier.fillMaxSize(),
                    autoplay = true,
                    onDurationDetected = onDurationDetected
                )
            }
        }

        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Verified,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = channelName,
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.weight(1f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    PodcastMetaPill(
                        text = languageBadge,
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                    if (!sourceBadge.isNullOrBlank()) {
                        PodcastMetaPill(
                            text = sourceBadge,
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedButton(
                        onClick = onToggleFavorite,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .weight(1f)
                            .sizeIn(minHeight = 48.dp)
                            .testTag("active_podcast_fav_btn")
                    ) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = AppLocalization.get(
                                if (isFavorite) "lectures_bookmark_remove" else "lectures_bookmark_add",
                                language
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    OutlinedButton(
                        onClick = {
                            try {
                                context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl)))
                            } catch (_: Exception) {
                            }
                        },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .weight(1f)
                            .sizeIn(minHeight = 48.dp)
                            .testTag("active_podcast_youtube_btn")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.OpenInNew,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = AppLocalization.get("lectures_watch_on_youtube", language),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}

@Composable
internal fun PodcastScholarDirectoryCard(
    scholar: PodcastScholarProfile,
    cachedVideoCount: Int,
    language: AppLanguage,
    onClick: () -> Unit
) {
    val isChannel = scholar.profileType == PodcastProfileType.CHANNEL
    val sourceKind = scholar.primarySourceKind
    val badgeBg = when (sourceKind) {
        PodcastSourceKind.OFFICIAL_CHANNEL -> MaterialTheme.colorScheme.primaryContainer
        PodcastSourceKind.HOSTED_INTERVIEW -> MaterialTheme.colorScheme.secondaryContainer
        PodcastSourceKind.UNAVAILABLE -> MaterialTheme.colorScheme.surfaceVariant
    }
    val badgeFg = when (sourceKind) {
        PodcastSourceKind.OFFICIAL_CHANNEL -> MaterialTheme.colorScheme.onPrimaryContainer
        PodcastSourceKind.HOSTED_INTERVIEW -> MaterialTheme.colorScheme.onSecondaryContainer
        PodcastSourceKind.UNAVAILABLE -> MaterialTheme.colorScheme.onSurfaceVariant
    }

    Card(
        onClick = onClick,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = if (isChannel) BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.45f)) else null,
        elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp),
        modifier = Modifier
            .fillMaxWidth()
            .sizeIn(minHeight = 64.dp)
            .testTag("podcast_scholar_${scholar.id}")
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(
                        if (scholar.isAvailable) MaterialTheme.colorScheme.primaryContainer
                        else MaterialTheme.colorScheme.surfaceVariant
                    )
            ) {
                Icon(
                    imageVector = if (isChannel) Icons.Default.HistoryEdu else Icons.Default.Person,
                    contentDescription = null,
                    tint = if (scholar.isAvailable) MaterialTheme.colorScheme.onPrimaryContainer
                    else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(22.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = scholar.displayNameNative,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (!scholar.displayNameEn.equals(scholar.displayNameNative, ignoreCase = true)) {
                    Text(
                        text = scholar.displayNameEn,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Spacer(modifier = Modifier.height(5.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    PodcastMetaPill(
                        text = AppLocalization.get(sourceKind.labelKey, language),
                        containerColor = badgeBg,
                        contentColor = badgeFg
                    )
                    if (cachedVideoCount > 0) {
                        PodcastMetaPill(
                            text = "$cachedVideoCount",
                            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                            contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    }
                }
            }
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = scholar.displayNameEn,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun VerifiedPodcastVideoCard(
    video: VerifiedPodcastVideo,
    language: AppLanguage,
    isPlayingNow: Boolean,
    isFavorite: Boolean,
    onPlay: () -> Unit,
    onToggleFavorite: () -> Unit
) {
    Card(
        onClick = onPlay,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isPlayingNow) {
                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f)
            } else {
                MaterialTheme.colorScheme.surface
            }
        ),
        border = if (isPlayingNow) BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary) else null,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("podcast_card_${video.videoId}")
    ) {
        Row(
            verticalAlignment = Alignment.Top,
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .width(120.dp)
                    .aspectRatio(16f / 9f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.Black)
            ) {
                AsyncImage(
                    model = video.thumbnailUrl,
                    contentDescription = video.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.65f))
                        .border(1.dp, MaterialTheme.colorScheme.primary, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = AppLocalization.get("lectures_watch_in_app", language),
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
                if (video.durationDisplay.isNotBlank()) {
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = Color.Black.copy(alpha = 0.82f),
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(4.dp)
                    ) {
                        Text(
                            text = video.durationDisplay,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold
                            ),
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = video.title,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.5.sp,
                        lineHeight = 18.sp
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Verified,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (video.scholarName.isNotBlank() &&
                            !video.scholarName.equals(video.channelName, ignoreCase = true)
                        ) {
                            "${video.scholarName} • ${video.channelName}"
                        } else {
                            video.channelName
                        },
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    PodcastMetaPill(
                        text = video.language.localizedBadge(language),
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                    PodcastMetaPill(
                        text = AppLocalization.get(video.contentType.labelKey, language),
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                        contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                    PodcastMetaPill(
                        text = AppLocalization.get(video.sourceKind.labelKey, language),
                        containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f),
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }

                if (video.publishedDisplay.isNotBlank() || video.durationDisplay.isNotBlank()) {
                    Spacer(modifier = Modifier.height(5.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        if (video.publishedDisplay.isNotBlank()) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.CalendarToday,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(11.dp)
                                )
                                Spacer(modifier = Modifier.width(3.dp))
                                Text(
                                    text = video.publishedDisplay,
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        if (video.durationDisplay.isNotBlank()) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Schedule,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(11.dp)
                                )
                                Spacer(modifier = Modifier.width(3.dp))
                                Text(
                                    text = video.durationDisplay,
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }

            IconButton(
                onClick = onToggleFavorite,
                modifier = Modifier
                    .sizeIn(minWidth = 48.dp, minHeight = 48.dp)
                    .testTag("podcast_fav_${video.videoId}")
            ) {
                Icon(
                    imageVector = if (isFavorite) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                    contentDescription = AppLocalization.get(
                        if (isFavorite) "lectures_bookmark_remove" else "lectures_bookmark_add",
                        language
                    ),
                    tint = if (isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
internal fun LegacySavedPodcastCard(
    episode: YouTubeVideoItem,
    language: AppLanguage,
    isPlayingNow: Boolean,
    isFavorite: Boolean,
    onPlay: () -> Unit,
    onToggleFavorite: () -> Unit
) {
    Card(
        onClick = onPlay,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isPlayingNow) {
                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f)
            } else {
                MaterialTheme.colorScheme.surface
            }
        ),
        border = if (isPlayingNow) BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary) else null,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("podcast_card_${episode.id}")
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .width(116.dp)
                    .aspectRatio(16f / 9f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.Black)
            ) {
                AsyncImage(
                    model = episode.thumbnail,
                    contentDescription = episode.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.65f))
                        .border(1.dp, MaterialTheme.colorScheme.primary, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = AppLocalization.get("lectures_watch_in_app", language),
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = episode.title,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, fontSize = 13.5.sp),
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = episode.channel,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            IconButton(
                onClick = onToggleFavorite,
                modifier = Modifier
                    .sizeIn(minWidth = 48.dp, minHeight = 48.dp)
                    .testTag("podcast_fav_${episode.id}")
            ) {
                Icon(
                    imageVector = if (isFavorite) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                    contentDescription = AppLocalization.get(
                        if (isFavorite) "lectures_bookmark_remove" else "lectures_bookmark_add",
                        language
                    ),
                    tint = if (isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
internal fun PodcastStatusBanner(
    status: PodcastFeedStatus,
    lastUpdatedMs: Long,
    language: AppLanguage
) {
    when (status) {
        PodcastFeedStatus.OFFLINE_CACHE -> {
            Surface(
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("podcast_offline_banner")
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(12.dp)
                ) {
                    Icon(Icons.Default.CloudOff, contentDescription = null, tint = MaterialTheme.colorScheme.onSecondaryContainer)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = AppLocalization.get("pod_offline_banner", language),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }
        }
        PodcastFeedStatus.QUOTA_EXCEEDED_CACHE -> {
            Surface(
                color = MaterialTheme.colorScheme.tertiaryContainer,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("podcast_quota_banner")
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(12.dp)
                ) {
                    Icon(Icons.Default.WarningAmber, contentDescription = null, tint = MaterialTheme.colorScheme.onTertiaryContainer)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = AppLocalization.get("pod_quota_banner", language),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                }
            }
        }
        else -> {
            if (lastUpdatedMs > 0L) {
                val updatedText = YouTubeRepository.formatLastUpdatedDisplay(lastUpdatedMs)
                Text(
                    text = String.format(AppLocalization.get("pod_last_updated", language), updatedText),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
            }
        }
    }
}

@Composable
internal fun PodcastMetaPill(
    text: String,
    containerColor: Color,
    contentColor: Color
) {
    Surface(
        shape = RoundedCornerShape(6.dp),
        color = containerColor
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
            color = contentColor,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
        )
    }
}
