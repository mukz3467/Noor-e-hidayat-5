package com.example.ui.components

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.data.model.AppLanguage
import com.example.localization.AppLocalization

data class MethodologyReference(
    val id: String,
    val titleKey: String,
    val descKey: String,
    val url: String
)

object MethodologyReferences {
    val HIJRI_CALENDAR_REFS = listOf(
        MethodologyReference(
            id = "pk_mora",
            titleKey = "src_pk_ruet_title",
            descKey = "src_pk_ruet_desc",
            url = "https://www.mora.gov.pk/Detail/ZTNhOGU5YjEtZTBmMy00OWQ2LWJlNmMtNWUxNzBkNGJmY2Nl"
        ),
        MethodologyReference(
            id = "umm_alqura",
            titleKey = "src_umm_alqura_title",
            descKey = "src_umm_alqura_desc",
            url = "https://www.ummulqura.org.sa/en"
        )
    )

    val PRAYER_AND_ALARM_REFS = listOf(
        MethodologyReference(
            id = "aladhan_methods",
            titleKey = "src_aladhan_methods_title",
            descKey = "src_aladhan_methods_desc",
            url = "https://aladhan.com/calculation-methods"
        ),
        MethodologyReference(
            id = "aladhan_api",
            titleKey = "src_aladhan_api_title",
            descKey = "src_aladhan_api_desc",
            url = "https://aladhan.com/prayer-times-api"
        ),
        MethodologyReference(
            id = "praytimes",
            titleKey = "src_praytimes_title",
            descKey = "src_praytimes_desc",
            url = "https://praytimes.org/docs/methods"
        ),
        MethodologyReference(
            id = "iana_tz",
            titleKey = "src_iana_title",
            descKey = "src_iana_desc",
            url = "https://www.iana.org/time-zones"
        ),
        MethodologyReference(
            id = "android_alarms",
            titleKey = "src_android_alarms_title",
            descKey = "src_android_alarms_desc",
            url = "https://developer.android.com/develop/background-work/services/alarms"
        ),
        MethodologyReference(
            id = "android_notif_perm",
            titleKey = "src_android_notif_title",
            descKey = "src_android_notif_desc",
            url = "https://developer.android.com/develop/ui/compose/notifications/notification-permission"
        )
    )

    val ALL_REFS = HIJRI_CALENDAR_REFS + PRAYER_AND_ALARM_REFS
}

@Composable
fun SourcesAndMethodologyCard(
    language: AppLanguage,
    references: List<MethodologyReference> = MethodologyReferences.ALL_REFS,
    modifier: Modifier = Modifier
) {
    val uriHandler = LocalUriHandler.current
    val context = LocalContext.current

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = modifier
            .fillMaxWidth()
            .testTag("sources_methodology_card")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.MenuBook,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = AppLocalization.get("sources_section_title", language),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = AppLocalization.get("sources_section_subtitle", language),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(10.dp))

            references.forEachIndexed { index, ref ->
                if (index > 0) {
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.25f))
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .minimumInteractiveComponentSize()
                        .clickable {
                            try {
                                uriHandler.openUri(ref.url)
                            } catch (_: Exception) {
                                Toast.makeText(context, ref.url, Toast.LENGTH_SHORT).show()
                            }
                        }
                        .padding(vertical = 8.dp)
                        .testTag("source_link_${ref.id}")
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = AppLocalization.get(ref.titleKey, language),
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = AppLocalization.get(ref.descKey, language),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = ref.url,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.tertiary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.OpenInNew,
                        contentDescription = ref.url,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}
