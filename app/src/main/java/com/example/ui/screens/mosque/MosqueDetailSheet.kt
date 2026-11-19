package com.example.ui.screens.mosque

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Accessible
import androidx.compose.material.icons.filled.CenterFocusStrong
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material.icons.filled.DirectionsBike
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.EditLocationAlt
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Route
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Straighten
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.model.AppLanguage
import com.example.data.mosque.MosqueFinderRepository
import com.example.data.mosque.MosqueItem
import com.example.data.mosque.MosqueRouteState
import com.example.data.mosque.OSRMRouteProfile
import com.example.data.mosque.OsmVerificationLevel
import com.example.data.mosque.OsrmRoutingService
import com.example.localization.AppLocalization
import com.example.ui.components.noorSelectableChipColors
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MosqueDetailSheet(
    mosque: MosqueItem,
    originLat: Double,
    originLon: Double,
    hasValidOrigin: Boolean = (originLat != 0.0 || originLon != 0.0),
    routeState: MosqueRouteState = MosqueRouteState(),
    language: AppLanguage,
    onFocusOnMap: () -> Unit = {},
    onRequestRoute: (OSRMRouteProfile) -> Unit = {},
    onClearRoute: () -> Unit = {},
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val notProvidedText = AppLocalization.get("mosque_not_provided_osm", language)
    var selectedProfile by remember(mosque.id, routeState.profile) {
        mutableStateOf(routeState.profile)
    }

    val isRouteForThisMosque = routeState.targetMosqueId == mosque.id
    val activeRoute = if (isRouteForThisMosque) routeState.route else null

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
        modifier = Modifier.testTag("mosque_detail_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(bottom = 28.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.horizontalScroll(rememberScrollState())
            ) {
                AssistChip(
                    onClick = {},
                    label = {
                        Text(
                            text = mosque.category.label(language),
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                )
                AssistChip(
                    onClick = {},
                    label = {
                        Text(
                            text = mosque.verificationLevel.label(language),
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                )
            }

            if (mosque.verificationLevel == OsmVerificationLevel.BUILDING_MOSQUE_CANDIDATE) {
                Spacer(modifier = Modifier.height(6.dp))
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.tertiaryContainer,
                    modifier = Modifier.fillMaxWidth().testTag("mosque_candidate_warning")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onTertiaryContainer,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = AppLocalization.get("mosque_candidate_warning", language),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = mosque.name,
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${String.format(Locale.US, "%.5f", mosque.latitude)}, ${String.format(Locale.US, "%.5f", mosque.longitude)} • OSM ${mosque.id}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(10.dp))
            OutlinedButton(
                onClick = onFocusOnMap,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 48.dp)
                    .testTag("mosque_focus_map_btn")
            ) {
                Icon(Icons.Default.CenterFocusStrong, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = AppLocalization.get("mosque_focus_on_map", language),
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Straight-line distance card (clearly distinguishes crow-flies vs road route)
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.55f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Straighten,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "${AppLocalization.get("mosque_straight_line_distance", language)}: ${MosqueFinderRepository.formatStraightLineDistance(mosque.distanceMeters)}",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Real OSRM Road Routing Section
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f),
                modifier = Modifier.fillMaxWidth().testTag("mosque_osrm_route_section")
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Route,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = AppLocalization.get("mosque_route_section_title", language),
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OSRMRouteProfile.entries.forEach { prof ->
                            val icon = when (prof) {
                                OSRMRouteProfile.DRIVING -> Icons.Default.DirectionsCar
                                OSRMRouteProfile.WALKING -> Icons.Default.DirectionsWalk
                                OSRMRouteProfile.CYCLING -> Icons.Default.DirectionsBike
                            }
                            val labelKey = when (prof) {
                                OSRMRouteProfile.DRIVING -> "mosque_route_profile_driving"
                                OSRMRouteProfile.WALKING -> "mosque_route_profile_walking"
                                OSRMRouteProfile.CYCLING -> "mosque_route_profile_cycling"
                            }
                            FilterChip(
                                selected = selectedProfile == prof,
                                onClick = {
                                    selectedProfile = prof
                                    if (hasValidOrigin && activeRoute != null) {
                                        onRequestRoute(prof)
                                    }
                                },
                                leadingIcon = {
                                    Icon(icon, contentDescription = null, modifier = Modifier.size(16.dp))
                                },
                                label = { Text(AppLocalization.get(labelKey, language)) },
                                colors = noorSelectableChipColors(),
                                modifier = Modifier.testTag("mosque_route_profile_${prof.id}")
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = { onRequestRoute(selectedProfile) },
                            enabled = hasValidOrigin && !(isRouteForThisMosque && routeState.isLoading),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .weight(1f)
                                .defaultMinSize(minHeight = 48.dp)
                                .testTag("mosque_show_route_btn")
                        ) {
                            Icon(Icons.Default.Route, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = AppLocalization.get("mosque_route_show_on_map", language),
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        if (activeRoute != null) {
                            OutlinedButton(
                                onClick = onClearRoute,
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .defaultMinSize(minHeight = 48.dp)
                                    .testTag("mosque_route_clear_btn")
                            ) {
                                Icon(
                                    Icons.Default.Clear,
                                    contentDescription = AppLocalization.get("mosque_route_clear", language),
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }

                    if (!hasValidOrigin) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = AppLocalization.get("mosque_route_origin_needed", language),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    if (isRouteForThisMosque && routeState.isLoading) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.testTag("mosque_route_loading")
                        ) {
                            CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = AppLocalization.get("mosque_route_loading", language),
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }

                    if (isRouteForThisMosque && !routeState.errorMessage.isNullOrBlank()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.errorContainer,
                            modifier = Modifier.fillMaxWidth().testTag("mosque_route_error_box")
                        ) {
                            Text(
                                text = routeState.errorMessage,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onErrorContainer,
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                    }

                    if (activeRoute != null) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = MaterialTheme.colorScheme.surface,
                            modifier = Modifier.fillMaxWidth().testTag("mosque_route_summary_card")
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                val distText = MosqueFinderRepository.formatStraightLineDistance(activeRoute.distanceMeters)
                                val durText = activeRoute.durationSeconds?.let {
                                    OsrmRoutingService.formatDuration(it)
                                } ?: AppLocalization.get("mosque_route_duration_unavailable", language)

                                Text(
                                    text = "$distText • $durText",
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.primary
                                )
                                if (!activeRoute.roadSummary.isNullOrBlank()) {
                                    Text(
                                        text = "Via ${activeRoute.roadSummary}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                Text(
                                    text = "OSRM (${activeRoute.providerHost})",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                if (activeRoute.steps.isNotEmpty()) {
                                    Spacer(modifier = Modifier.height(6.dp))
                                    activeRoute.steps.take(4).forEach { step ->
                                        val stepDist = MosqueFinderRepository.formatStraightLineDistance(step.distanceMeters)
                                        Text(
                                            text = "• ${step.instruction} ($stepDist)",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = AppLocalization.get("mosque_route_demo_notice", language),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = AppLocalization.get("mosque_verified_osm_tags", language),
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))

            OsmMetadataRow(
                icon = Icons.Default.LocationOn,
                label = AppLocalization.get("mosque_address_label", language),
                value = mosque.address ?: notProvidedText,
                isMissing = mosque.address == null
            )
            OsmMetadataRow(
                icon = Icons.Default.Schedule,
                label = AppLocalization.get("mosque_hours_label", language),
                value = mosque.openingHours ?: notProvidedText,
                isMissing = mosque.openingHours == null
            )
            OsmMetadataRow(
                icon = Icons.Default.Phone,
                label = AppLocalization.get("mosque_phone_label", language),
                value = mosque.phone ?: notProvidedText,
                isMissing = mosque.phone == null
            )
            OsmMetadataRow(
                icon = Icons.Default.Language,
                label = AppLocalization.get("mosque_website_label", language),
                value = mosque.website ?: notProvidedText,
                isMissing = mosque.website == null
            )
            OsmMetadataRow(
                icon = Icons.Default.Accessible,
                label = AppLocalization.get("mosque_wheelchair_label", language),
                value = mosque.wheelchair ?: notProvidedText,
                isMissing = mosque.wheelchair == null
            )
            if (!mosque.denomination.isNullOrBlank()) {
                OsmMetadataRow(
                    icon = Icons.Default.Info,
                    label = AppLocalization.get("mosque_denomination_label", language),
                    value = mosque.denomination,
                    isMissing = false
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            Button(
                onClick = {
                    val geoUri = Uri.parse(
                        "geo:${mosque.latitude},${mosque.longitude}?q=${mosque.latitude},${mosque.longitude}(${Uri.encode(mosque.name)})"
                    )
                    val geoIntent = Intent(Intent.ACTION_VIEW, geoUri)
                    val launchedGeo = runCatching {
                        context.startActivity(geoIntent)
                        true
                    }.getOrDefault(false)
                    if (!launchedGeo) {
                        val fallbackUrl = if (hasValidOrigin) {
                            "https://www.openstreetmap.org/directions?from=$originLat,$originLon&to=${mosque.latitude},${mosque.longitude}"
                        } else {
                            mosque.osmUrl
                        }
                        runCatching { context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(fallbackUrl))) }
                    }
                },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 48.dp)
                    .testTag("mosque_directions_btn")
            ) {
                Icon(Icons.Default.Directions, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = AppLocalization.get("mosque_get_directions", language),
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedButton(
                onClick = {
                    runCatching {
                        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(mosque.osmUrl)))
                    }
                },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 48.dp)
                    .testTag("mosque_osm_edit_btn")
            ) {
                Icon(Icons.Default.EditLocationAlt, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = AppLocalization.get("mosque_report_edit_osm", language),
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "© OpenStreetMap contributors — openstreetmap.org/copyright",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun OsmMetadataRow(
    icon: ImageVector,
    label: String,
    value: String,
    isMissing: Boolean
) {
    Row(
        verticalAlignment = Alignment.Top,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(18.dp).padding(top = 2.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                color = if (isMissing) {
                    MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.78f)
                } else {
                    MaterialTheme.colorScheme.onSurface
                }
            )
        }
    }
}
