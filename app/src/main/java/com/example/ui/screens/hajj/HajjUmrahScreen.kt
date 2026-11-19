package com.example.ui.screens.hajj

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.hajj.GuideSectionTab
import com.example.data.hajj.HajjGuideRepository
import com.example.data.model.AppLanguage
import com.example.data.repository.DuaAudioPlayer
import com.example.localization.AppLocalization
import com.example.ui.components.NoorTopBar
import com.example.ui.components.noorSelectableChipColors

@Composable
fun HajjUmrahScreen(
    language: AppLanguage,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        HajjGuideRepository.init(context)
    }
    DisposableEffect(Unit) {
        onDispose { DuaAudioPlayer.stop() }
    }

    val state by HajjGuideRepository.state.collectAsStateWithLifecycle()
    var activeTab by rememberSaveable { mutableStateOf(GuideSectionTab.RITUALS) }

    val tabs = listOf(
        GuideSectionTab.INTRO to Triple("hajj_tab_intro", Icons.Default.MenuBook, "hajj_tab_intro"),
        GuideSectionTab.IHRAM to Triple("hajj_tab_ihram", Icons.Default.Checkroom, "hajj_tab_ihram"),
        GuideSectionTab.RITUALS to Triple("hajj_tab_rituals", Icons.Default.Map, "hajj_tab_rituals"),
        GuideSectionTab.DUAS to Triple("hajj_tab_duas", Icons.Default.VolunteerActivism, "hajj_tab_duas"),
        GuideSectionTab.PRACTICAL to Triple("hajj_tab_practical", Icons.Default.Checklist, "hajj_tab_practical")
    )

    Scaffold(
        topBar = {
            NoorTopBar(
                title = AppLocalization.get("hajj_umrah_guide", language),
                language = language,
                onBackClick = onBack
            )
        }
    ) { innerPadding ->
        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            Column(
                modifier = Modifier
                    .widthIn(max = 680.dp)
                    .fillMaxSize()
            ) {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(tabs) { (tab, meta) ->
                        val (titleKey, icon, tag) = meta
                        FilterChip(
                            selected = activeTab == tab,
                            onClick = { activeTab = tab },
                            leadingIcon = {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                            },
                            label = { Text(AppLocalization.get(titleKey, language)) },
                            colors = noorSelectableChipColors(),
                            modifier = Modifier
                                .heightIn(min = 48.dp)
                                .testTag(tag)
                        )
                    }
                }

                Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
                    when (activeTab) {
                        GuideSectionTab.INTRO -> HajjIntroTab(
                            language = language,
                            state = state,
                            onSelectSchool = { HajjGuideRepository.setSchool(context, it) }
                        )
                        GuideSectionTab.IHRAM -> HajjIhramTab(
                            language = language,
                            state = state
                        )
                        GuideSectionTab.RITUALS -> HajjRitualsTab(
                            language = language,
                            state = state,
                            onSelectMode = { HajjGuideRepository.setMode(context, it) },
                            onSelectSubType = { HajjGuideRepository.setSubType(context, it) },
                            onSelectSchool = { HajjGuideRepository.setSchool(context, it) },
                            onToggleStepComplete = { HajjGuideRepository.toggleStepCompleted(context, it) }
                        )
                        GuideSectionTab.DUAS -> HajjDuasTab(language = language)
                        GuideSectionTab.PRACTICAL -> HajjPracticalTab(
                            language = language,
                            state = state,
                            onToggleChecklist = { HajjGuideRepository.toggleChecklistItem(context, it) },
                            onAddCustomChecklist = { cat, title -> HajjGuideRepository.addCustomChecklistItem(context, cat, title) },
                            onDeleteCustomChecklist = { HajjGuideRepository.deleteCustomChecklistItem(context, it) },
                            onSaveNote = { title, content, noteId ->
                                HajjGuideRepository.savePersonalNote(context, title, content, null, noteId)
                            },
                            onDeleteNote = { HajjGuideRepository.deletePersonalNote(context, it) }
                        )
                    }
                }
            }
        }
    }
}
