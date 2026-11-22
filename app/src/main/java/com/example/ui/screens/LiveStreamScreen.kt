package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.data.live.HarameynLiveSources
import com.example.data.live.LiveConnectionPhase
import com.example.data.live.LivePlayerTransport
import com.example.data.live.LiveSourceAttemptStatus
import com.example.data.model.AppLanguage
import com.example.data.repository.HarameynLocation
import com.example.localization.AppLocalization
import com.example.ui.components.InAppHlsLivePlayer
import com.example.ui.components.InAppYouTubeLivePlayer
import com.example.ui.components.InAppYouTubePlayer
import com.example.ui.components.NoorTopBar
import com.example.ui.viewmodel.LiveStreamViewModel

@Composable
fun LiveStreamScreen(
    language: AppLanguage,
    onBack: () -> Unit,
    viewModel: LiveStreamViewModel = viewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    DisposableEffect(viewModel) {
        viewModel.onScreenAttached()
        onDispose { viewModel.onScreenDisposed() }
    }

    val selectedLocation = uiState.selectedLocation
    val selectedChannelIndex = if (selectedLocation == HarameynLocation.MAKKAH) 0 else 1
    val channelMeta = uiState.channel

    val channelTitle = if (selectedLocation == HarameynLocation.MAKKAH) {
        AppLocalization.get("live_makkah_title", language)
    } else {
        AppLocalization.get("live_madinah_title", language)
    }

    Scaffold(
        topBar = {
            NoorTopBar(
                title = AppLocalization.get("live_stream", language),
                language = language,
                onBackClick = onBack,
                actions = {
                    IconButton(
                        onClick = { viewModel.retryAllSources() },
                        modifier = Modifier
                            .sizeIn(minWidth = 48.dp, minHeight = 48.dp)
                            .testTag("reload_stream_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = AppLocalization.get("retry", language)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Makkah & Madinah Official Channel Tabs
            TabRow(
                selectedTabIndex = selectedChannelIndex,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clip(RoundedCornerShape(12.dp))
            ) {
                Tab(
                    selected = selectedChannelIndex == 0,
                    onClick = { viewModel.selectLocation(HarameynLocation.MAKKAH) },
                    selectedContentColor = MaterialTheme.colorScheme.primary,
                    unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    text = {
                        Text(
                            text = AppLocalization.get("live_tab_makkah", language),
                            fontWeight = if (selectedChannelIndex == 0) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    modifier = Modifier.testTag("tab_live_makkah")
                )
                Tab(
                    selected = selectedChannelIndex == 1,
                    onClick = { viewModel.selectLocation(HarameynLocation.MADINAH) },
                    selectedContentColor = MaterialTheme.colorScheme.primary,
                    unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    text = {
                        Text(
                            text = AppLocalization.get("live_tab_madinah", language),
                            fontWeight = if (selectedChannelIndex == 1) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    modifier = Modifier.testTag("tab_live_madinah")
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Single Active Player Slot (NEVER mounts two players simultaneously)
            val activeRecorded = uiState.selectedRecordedVideo
            val activeLiveSource = uiState.activeSource
            val activeToken = uiState.attemptToken

            if (activeRecorded != null) {
                // User explicitly chose a recorded upload -> clearly badged NOT LIVE
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("recorded_not_live_banner")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSecondaryContainer,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "${AppLocalization.get("live_recorded_badge", language)}: ${activeRecorded.title}",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f)
                        )
                        TextButton(
                            onClick = { viewModel.returnToLiveBroadcast() },
                            modifier = Modifier.testTag("return_to_live_btn")
                        ) {
                            Text(
                                text = AppLocalization.get("live_return_to_live", language),
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.Black),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp)
                        .testTag("embedded_recorded_harameyn_player")
                ) {
                    key(activeRecorded.id, uiState.sessionNonce) {
                        InAppYouTubePlayer(
                            videoId = activeRecorded.id,
                            modifier = Modifier.fillMaxSize(),
                            autoplay = true,
                            language = language
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            } else if (activeLiveSource != null) {
                val playerTag = if (uiState.phase == LiveConnectionPhase.LIVE) {
                    "embedded_live_player"
                } else {
                    "connecting_live_player"
                }
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.Black),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp)
                        .testTag(playerTag)
                ) {
                    key(
                        selectedLocation,
                        activeLiveSource.config.id,
                        activeToken,
                        uiState.sessionNonce
                    ) {
                        when (activeLiveSource.transport) {
                            LivePlayerTransport.HLS_NATIVE -> {
                                val hlsUrl = activeLiveSource.hlsStreamUrl.orEmpty()
                                InAppHlsLivePlayer(
                                    hlsUrl = hlsUrl,
                                    language = language,
                                    onLivePlaybackConfirmed = {
                                        viewModel.onPlayerConfirmedLive(activeToken)
                                    },
                                    onReconnecting = {
                                        viewModel.onPlayerReconnecting(activeToken)
                                    },
                                    onPlaybackFailed = { reason ->
                                        viewModel.onPlayerFailed(activeToken, reason)
                                    },
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                            LivePlayerTransport.YOUTUBE_VIDEO_EMBED,
                            LivePlayerTransport.YOUTUBE_CHANNEL_LIVE_EMBED -> {
                                InAppYouTubeLivePlayer(
                                    resolvedSource = activeLiveSource,
                                    language = language,
                                    onLivePlaybackConfirmed = {
                                        viewModel.onPlayerConfirmedLive(activeToken)
                                    },
                                    onReconnecting = {
                                        viewModel.onPlayerReconnecting(activeToken)
                                    },
                                    onPlaybackFailed = { reason ->
                                        viewModel.onPlayerFailed(activeToken, reason)
                                    },
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Honest Live Status & Automatic Fallback Card
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("live_status_card")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = channelTitle,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Verified,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "${channelMeta.localizedName(language)} (${channelMeta.handle})",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Honest Connection Phase Banner
                    val activeSourceLabel = uiState.activeSource?.config?.localizedLabel(language)
                        ?: uiState.sourceAttempts.getOrNull(uiState.activeSourceIndex)?.source?.localizedLabel(language)
                        ?: channelMeta.localizedName(language)

                    when (uiState.phase) {
                        LiveConnectionPhase.CONNECTING -> {
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("live_phase_connecting_banner")
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(12.dp)
                                ) {
                                    CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.5.dp)
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = AppLocalization.get("live_phase_connecting", language),
                                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        Text(
                                            text = activeSourceLabel,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                }
                            }
                        }
                        LiveConnectionPhase.RECONNECTING -> {
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = MaterialTheme.colorScheme.secondaryContainer,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("live_phase_reconnecting_banner")
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(12.dp)
                                ) {
                                    CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.5.dp)
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = AppLocalization.get("live_phase_reconnecting", language),
                                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                                            color = MaterialTheme.colorScheme.onSecondaryContainer
                                        )
                                        Text(
                                            text = activeSourceLabel,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                }
                            }
                        }
                        LiveConnectionPhase.LIVE -> {
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = MaterialTheme.colorScheme.primaryContainer,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("live_phase_confirmed_banner")
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Box(
                                            modifier = Modifier
                                                .size(9.dp)
                                                .clip(CircleShape)
                                                .background(Color(0xFFDC2626))
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "${AppLocalization.get("live_badge_confirmed", language)} • $activeSourceLabel",
                                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                                            maxLines = 2,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                    if (uiState.isFallbackActive) {
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = AppLocalization.get("live_fallback_relay_note", language),
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.85f)
                                        )
                                    }
                                }
                            }
                        }
                        LiveConnectionPhase.OFFLINE -> {
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = MaterialTheme.colorScheme.errorContainer,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("live_phase_offline_banner")
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(12.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CloudOff,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onErrorContainer
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        text = AppLocalization.get("live_status_offline", language),
                                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                                        color = MaterialTheme.colorScheme.onErrorContainer
                                    )
                                }
                            }
                        }
                        LiveConnectionPhase.SOURCE_UNAVAILABLE -> {
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = MaterialTheme.colorScheme.errorContainer,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("live_phase_unavailable_banner")
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.WarningAmber,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.onErrorContainer
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = AppLocalization.get("live_phase_source_unavailable", language),
                                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                            color = MaterialTheme.colorScheme.onErrorContainer
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = AppLocalization.get("live_source_unavailable_desc", language),
                                        style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp),
                                        color = MaterialTheme.colorScheme.onErrorContainer
                                    )
                                }
                            }
                        }
                    }

                    // Ordered 3-source fallback transparency rows
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = AppLocalization.get("live_sources_label", language),
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    uiState.sourceAttempts.forEach { attempt ->
                        val (icon, tint) = when (attempt.status) {
                            LiveSourceAttemptStatus.ACTIVE_LIVE ->
                                Icons.Default.CheckCircle to MaterialTheme.colorScheme.primary
                            LiveSourceAttemptStatus.TESTING ->
                                Icons.Default.Sync to MaterialTheme.colorScheme.tertiary
                            LiveSourceAttemptStatus.FAILED ->
                                Icons.Default.ErrorOutline to MaterialTheme.colorScheme.error
                            LiveSourceAttemptStatus.PENDING ->
                                Icons.Default.RadioButtonUnchecked to MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 3.dp)
                                .testTag("live_source_row_${attempt.source.id.name.lowercase()}")
                        ) {
                            Icon(
                                imageVector = icon,
                                contentDescription = null,
                                tint = tint,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "${attempt.source.priorityOrder}. ${attempt.source.localizedLabel(language)}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Primary Retry + Official YouTube Channel Actions
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            onClick = { viewModel.retryAllSources() },
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .weight(1f)
                                .sizeIn(minHeight = 48.dp)
                                .testTag("live_retry_btn")
                        ) {
                            Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(AppLocalization.get("retry", language))
                        }

                        OutlinedButton(
                            onClick = {
                                openExternalUrl(context, channelMeta.officialChannelUrl)
                            },
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .weight(1f)
                                .sizeIn(minHeight = 48.dp)
                                .testTag("live_open_official_channel_btn")
                        ) {
                            Icon(Icons.AutoMirrored.Filled.OpenInNew, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = AppLocalization.get("live_open_official_channel", language),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    // External Browser Fallback links when all embedded sources fail
                    if (uiState.phase == LiveConnectionPhase.SOURCE_UNAVAILABLE) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            OutlinedButton(
                                onClick = {
                                    openExternalUrl(context, HarameynLiveSources.QURAN_HAYAT_PAGE_URL)
                                },
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .sizeIn(minHeight = 48.dp)
                                    .testTag("live_open_quranhayat_btn")
                            ) {
                                Icon(Icons.AutoMirrored.Filled.OpenInNew, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = AppLocalization.get("live_open_quranhayat", language),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            OutlinedButton(
                                onClick = {
                                    openExternalUrl(context, HarameynLiveSources.HARAMAIN_VOICE_PAGE_URL)
                                },
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .sizeIn(minHeight = 48.dp)
                                    .testTag("live_open_haramainvoice_btn")
                            ) {
                                Icon(Icons.AutoMirrored.Filled.OpenInNew, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = AppLocalization.get("live_open_haramainvoice", language),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

                    Text(
                        text = AppLocalization.get("live_policy_footer", language),
                        style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Recent Recorded Uploads from Official Channel RSS (Explicitly labeled Recorded, NOT Live)
            if (uiState.recentRecordedUploads.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = AppLocalization.get("live_recorded_uploads_title", language),
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(8.dp))

                uiState.recentRecordedUploads.forEach { upload ->
                    val isSelected = uiState.selectedRecordedVideo?.id == upload.id
                    Card(
                        onClick = { viewModel.selectRecordedVideo(upload) },
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f) else MaterialTheme.colorScheme.surface
                        ),
                        border = if (isSelected) BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary) else null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp)
                            .testTag("live_recorded_card_${upload.id}")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(12.dp)
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .width(104.dp)
                                    .aspectRatio(16f / 9f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color.Black)
                            ) {
                                AsyncImage(
                                    model = upload.thumbnail,
                                    contentDescription = upload.title,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(Color.Black.copy(alpha = 0.65f))
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.PlayArrow,
                                        contentDescription = AppLocalization.get("live_watch_recorded", language),
                                        tint = Color.White,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = MaterialTheme.colorScheme.secondaryContainer
                                ) {
                                    Text(
                                        text = AppLocalization.get("live_recorded_badge", language),
                                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = upload.title,
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                                    color = MaterialTheme.colorScheme.onSurface,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                                if (upload.publishedDisplay.isNotBlank()) {
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = upload.publishedDisplay,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun openExternalUrl(context: android.content.Context, url: String) {
    runCatching {
        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }
}
