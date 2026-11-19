package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.location.AppLocationManager
import com.example.data.model.AppLanguage
import com.example.data.repository.IslamicCalendarRepository
import com.example.data.repository.PrayerCalculator
import com.example.localization.AppLocalization
import java.util.Calendar
import java.util.TimeZone

private data class HomeFeatureBoxItem(
    val title: String,
    val icon: ImageVector,
    val testTag: String,
    val isPrimaryAccent: Boolean = false,
    val onClick: () -> Unit
)

@Composable
fun HomeScreen(
    language: AppLanguage,
    onNavigateToQuran: () -> Unit,
    onNavigateToLive: () -> Unit,
    onNavigateToLectures: (String?) -> Unit,
    onNavigateToLearning: () -> Unit,
    onNavigateToMoreHub: () -> Unit,
    onNavigateToPrayerTimes: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToHuqooqAcademy: () -> Unit = {},
    onNavigateToBusinessAcademy: () -> Unit = {},
    onLanguageChanged: (AppLanguage) -> Unit = {}
) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        AppLocationManager.initializeAndRefreshIfPermitted(context)
        PrayerCalculator.init(context)
        IslamicCalendarRepository.init(context)
    }

    val userLocation by AppLocationManager.currentLocation.collectAsState()
    val calcConfig by PrayerCalculator.calcConfig.collectAsState()
    val convention by IslamicCalendarRepository.calendarConvention.collectAsState()
    val manualOffset by IslamicCalendarRepository.manualAdjustmentDays.collectAsState()
    val appSettings = com.example.ui.theme.LocalAppSettings.current

    val schedule = remember(userLocation, calcConfig) {
        PrayerCalculator.calculatePrayerTimes(
            latitude = userLocation.latitude,
            longitude = userLocation.longitude,
            method = calcConfig.method,
            asrJuristic = calcConfig.asrJuristic,
            adjustments = calcConfig.adjustments,
            highLatitudeRule = calcConfig.highLatitudeRule,
            cityName = userLocation.cityName,
            timeZoneId = userLocation.timeZoneId,
            customParams = calcConfig.customParams
        )
    }

    val hijriDate = remember(userLocation.timeZoneId, convention, manualOffset) {
        val localCal = Calendar.getInstance(TimeZone.getTimeZone(userLocation.timeZoneId))
        IslamicCalendarRepository.getHijriDate(localCal, manualOffset, convention)
    }

    // Exact 8 feature boxes arranged in 4 rows x 2 columns
    val featureRows = listOf(
        // Row 1: Al-Quran | Live Makkah & Madinah
        HomeFeatureBoxItem(
            title = AppLocalization.get("quran", language),
            icon = Icons.Default.MenuBook,
            testTag = "main_option_quran",
            isPrimaryAccent = true,
            onClick = onNavigateToQuran
        ) to HomeFeatureBoxItem(
            title = AppLocalization.get("live_stream", language),
            icon = Icons.Default.LiveTv,
            testTag = "main_option_live",
            onClick = onNavigateToLive
        ),
        // Row 2: Islamic Lectures | Quran Learning
        HomeFeatureBoxItem(
            title = AppLocalization.get("lectures", language),
            icon = Icons.Default.PlayCircleOutline,
            testTag = "main_option_lectures",
            onClick = { onNavigateToLectures(null) }
        ) to HomeFeatureBoxItem(
            title = AppLocalization.get("learning", language),
            icon = Icons.Default.School,
            testTag = "main_option_learning",
            onClick = onNavigateToLearning
        ),
        // Row 3: Islami Business Course | Huqooq-ul-Ibad Academy
        HomeFeatureBoxItem(
            title = AppLocalization.get("home_box_business_course", language),
            icon = Icons.Default.Storefront,
            testTag = "main_option_business_course",
            onClick = onNavigateToBusinessAcademy
        ) to HomeFeatureBoxItem(
            title = AppLocalization.get("huqooq_academy", language),
            icon = Icons.Default.Handshake,
            testTag = "main_option_huqooq_academy",
            onClick = onNavigateToHuqooqAcademy
        ),
        // Row 4: More | Settings
        HomeFeatureBoxItem(
            title = AppLocalization.get("home_box_more", language),
            icon = Icons.Default.Widgets,
            testTag = "more_options_card",
            onClick = onNavigateToMoreHub
        ) to HomeFeatureBoxItem(
            title = AppLocalization.get("home_box_settings", language),
            icon = Icons.Default.Settings,
            testTag = "main_option_settings",
            onClick = onNavigateToSettings
        )
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .navigationBarsPadding()
            .testTag("home_screen_list"),
        contentPadding = PaddingValues(bottom = 28.dp)
    ) {
        // 1. Existing Header (Settings shortcut removed — now Box 8 in the grid)
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
                    .clipToBounds()
                    .testTag("home_header")
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_kaaba_header),
                    contentDescription = "Holy Kaaba, Makkah",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .testTag("home_header_image")
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Black.copy(alpha = 0.20f),
                                    Color.Black.copy(alpha = 0.60f)
                                )
                            )
                        )
                )

                val cityDisplay = remember(userLocation, language) {
                    AppLocationManager.localizedCityDisplay(userLocation, language)
                }
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(20.dp)
                ) {
                    Text(
                        text = AppLocalization.get("app_name", language),
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 28.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${hijriDate.formatted(language)} • $cityDisplay",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color(0xFFFEF3C7)
                        )
                    )
                }
            }
        }

        // 2. Existing Prayer Times Card — UNCHANGED
        if (appSettings.showHomePrayerCard) {
            item {
                Card(
                    onClick = onNavigateToPrayerTimes,
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 14.dp)
                        .testTag("home_prayer_card")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f))
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AccessTime,
                                    contentDescription = AppLocalization.get("prayer_times", language),
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "${AppLocalization.get("dhuhr", language)}: ${schedule.dhuhr}",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "${AppLocalization.get("fajr", language)} ${schedule.fajr} • ${AppLocalization.get("asr", language)} ${schedule.asr} • ${AppLocalization.get("maghrib", language)} ${schedule.maghrib}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = AppLocalization.get("prayer_times", language),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // Section Tagline above the 8-box grid
        item {
            Text(
                text = AppLocalization.get("app_tagline", language),
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp)
            )
        }

        // 3. Two-Column Grid with Four Evenly Spaced Rows (8 Boxes)
        item {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .testTag("home_feature_grid")
            ) {
                featureRows.forEach { (leftBox, rightBox) ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(IntrinsicSize.Max)
                    ) {
                        HomeGridBox(
                            item = leftBox,
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                        )
                        HomeGridBox(
                            item = rightBox,
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun HomeGridBox(
    item: HomeFeatureBoxItem,
    modifier: Modifier = Modifier
) {
    val containerColor = if (item.isPrimaryAccent) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        MaterialTheme.colorScheme.surface
    }
    val contentColor = if (item.isPrimaryAccent) {
        MaterialTheme.colorScheme.onPrimaryContainer
    } else {
        MaterialTheme.colorScheme.onSurface
    }
    val iconBadgeColor = if (item.isPrimaryAccent) {
        MaterialTheme.colorScheme.primary.copy(alpha = 0.14f)
    } else {
        MaterialTheme.colorScheme.primary.copy(alpha = 0.11f)
    }
    val iconTint = if (item.isPrimaryAccent) {
        MaterialTheme.colorScheme.onPrimaryContainer
    } else {
        MaterialTheme.colorScheme.primary
    }

    Card(
        onClick = item.onClick,
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        border = BorderStroke(
            width = 1.dp,
            color = if (item.isPrimaryAccent) {
                MaterialTheme.colorScheme.primary.copy(alpha = 0.25f)
            } else {
                MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.65f)
            }
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
            .defaultMinSize(minHeight = 118.dp)
            .testTag(item.testTag)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp, vertical = 14.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(iconBadgeColor)
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = item.title,
                    tint = iconTint,
                    modifier = Modifier.size(26.dp)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = item.title,
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    lineHeight = 19.sp
                ),
                color = contentColor,
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
