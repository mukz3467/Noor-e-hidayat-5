package com.example.ui.screens.settings

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.data.model.AppLanguage
import com.example.data.model.CardAppearanceStyle
import com.example.ui.theme.LocalAppSettings
import java.util.Locale

internal fun loc(
    lang: AppLanguage,
    en: String,
    ur: String,
    ar: String,
    hi: String
): String = when (lang) {
    AppLanguage.URDU -> ur
    AppLanguage.ARABIC -> ar
    AppLanguage.HINDI -> hi
    AppLanguage.ENGLISH -> en
}

internal fun formatBytesReadable(bytes: Long): String {
    if (bytes <= 0L) return "0 KB"
    val kb = bytes / 1024.0
    if (kb < 1024.0) return String.format(Locale.US, "%.0f KB", kb.coerceAtLeast(1.0))
    val mb = kb / 1024.0
    return if (mb < 1024.0) {
        String.format(Locale.US, "%.2f MB", mb)
    } else {
        String.format(Locale.US, "%.2f GB", mb / 1024.0)
    }
}

@Composable
internal fun styledCardColors(): CardColors =
    CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)

@Composable
internal fun styledCardElevation(): CardElevation = when (LocalAppSettings.current.cardStyle) {
    CardAppearanceStyle.ELEVATED -> CardDefaults.cardElevation(defaultElevation = 2.dp)
    CardAppearanceStyle.OUTLINED -> CardDefaults.cardElevation(defaultElevation = 0.dp)
    CardAppearanceStyle.FLAT -> CardDefaults.cardElevation(defaultElevation = 0.dp)
}

@Composable
internal fun styledCardBorder(): BorderStroke? = when (LocalAppSettings.current.cardStyle) {
    CardAppearanceStyle.OUTLINED -> BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    CardAppearanceStyle.ELEVATED, CardAppearanceStyle.FLAT -> null
}

@Composable
internal fun SettingsSectionCard(
    sectionId: String,
    title: String,
    summary: String,
    icon: ImageVector,
    expanded: Boolean,
    onToggleExpand: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    val reducedMotion = LocalAppSettings.current.reducedMotionEnabled
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = styledCardColors(),
        elevation = styledCardElevation(),
        border = styledCardBorder(),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("settings_section_$sectionId")
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .minimumInteractiveComponentSize()
                    .clickable(onClick = onToggleExpand)
                    .padding(horizontal = 16.dp, vertical = 14.dp)
                    .semantics { heading() }
                    .testTag("settings_header_$sectionId")
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    if (summary.isNotBlank()) {
                        Text(
                            text = summary,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
                Icon(
                    imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = if (expanded) "Collapse" else "Expand",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (reducedMotion) {
                if (expanded) {
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        content = content
                    )
                }
            } else {
                AnimatedVisibility(visible = expanded) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            content = content
                        )
                    }
                }
            }
        }
    }
}

@Composable
internal fun SettingsSwitchRow(
    title: String,
    subtitle: String? = null,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    switchTestTag: String,
    enabled: Boolean = true
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .minimumInteractiveComponentSize()
            .padding(vertical = 6.dp)
    ) {
        Column(modifier = Modifier.weight(1f).padding(end = 12.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                color = if (enabled) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
            if (!subtitle.isNullOrBlank()) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            enabled = enabled,
            modifier = Modifier.testTag(switchTestTag)
        )
    }
}

@Composable
internal fun SettingsActionRow(
    title: String,
    subtitle: String,
    icon: ImageVector,
    tint: Color = MaterialTheme.colorScheme.primary,
    onClick: () -> Unit,
    testTag: String
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .minimumInteractiveComponentSize()
            .testTag(testTag)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = tint,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                    color = tint
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
internal fun SettingsFieldLabel(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(top = 8.dp, bottom = 6.dp)
    )
}
