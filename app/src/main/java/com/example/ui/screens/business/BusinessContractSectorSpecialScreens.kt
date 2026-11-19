package com.example.ui.screens.business

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.business.*
import com.example.data.model.AppLanguage
import com.example.localization.AppLocalization
import com.example.ui.components.NoorTopBar

@Composable
fun BusinessContractDetailScreen(
    contract: BusinessContractGuide,
    quranCitations: List<BusinessQuranCitation>,
    hadithCatalog: Map<String, BusinessHadithCitation>,
    isBookmarked: Boolean,
    isCompleted: Boolean,
    existingNote: String,
    existingQuizResult: BusinessQuizResultEntity?,
    language: AppLanguage,
    onToggleBookmark: () -> Unit,
    onToggleCompleted: () -> Unit,
    onSaveNote: (String) -> Unit,
    onSaveQuizScore: (Int, Int) -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            NoorTopBar(
                title = contract.name.get(language),
                language = language,
                onBackClick = onBack
            )
        }
    ) { innerPadding ->
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
                .testTag("contract_detail_${contract.id}")
        ) {
            item {
                Card(shape = RoundedCornerShape(16.dp)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = contract.arabicTerm,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = contract.name.get(language),
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        AssistChip(onClick = {}, label = { Text(contract.aaoifiStandard) })
                    }
                }
            }
            item {
                BusinessSectionCard(AppLocalization.get("ba_contract_definition", language), Icons.Default.Description) {
                    Text(contract.definition.get(language), style = MaterialTheme.typography.bodyMedium)
                }
            }
            item {
                BusinessSectionCard(AppLocalization.get("ba_contract_purpose", language), Icons.Default.Lightbulb) {
                    Text(contract.purpose.get(language), style = MaterialTheme.typography.bodyMedium)
                }
            }
            item {
                BusinessSectionCard(AppLocalization.get("ba_shariah_principles", language), Icons.Default.VerifiedUser) {
                    Text(contract.shariahPrinciples.get(language), style = MaterialTheme.typography.bodyMedium)
                }
            }
            item {
                BusinessSectionCard(AppLocalization.get("ba_contract_conditions", language), Icons.Default.Checklist) {
                    BulletListSection(contract.conditions.get(language), "✓")
                }
            }
            item {
                BusinessSectionCard(AppLocalization.get("ba_party_rights", language), Icons.Default.Groups) {
                    Text(contract.partyRights.get(language), style = MaterialTheme.typography.bodyMedium)
                }
            }
            item {
                BusinessSectionCard(AppLocalization.get("ba_ownership_possession", language), Icons.Default.Inventory2) {
                    Text(contract.ownershipAndPossession.get(language), style = MaterialTheme.typography.bodyMedium)
                }
            }
            item {
                BusinessSectionCard(AppLocalization.get("ba_profit_loss_rules", language), Icons.Default.PieChart) {
                    Text(contract.profitAndLossRules.get(language), style = MaterialTheme.typography.bodyMedium)
                }
            }
            item {
                BusinessSectionCard(
                    AppLocalization.get("ba_invalid_structures", language),
                    Icons.Default.ReportProblem,
                    containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.35f)
                ) {
                    BulletListSection(contract.commonInvalidStructures.get(language), "✕")
                }
            }
            item {
                BusinessSectionCard(AppLocalization.get("ba_modern_example", language), Icons.Default.Business) {
                    Text(contract.modernExamples.get(language), style = MaterialTheme.typography.bodyMedium)
                }
            }
            item {
                BusinessSectionCard(AppLocalization.get("ba_sbp_secp_guidance", language), Icons.Default.AccountBalance) {
                    EvidenceCategoryBadge(EvidenceCategory.REGULATORY_LEGAL, language)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(contract.sbpGuidance.get(language), style = MaterialTheme.typography.bodyMedium)
                }
            }
            item {
                BusinessSectionCard(AppLocalization.get("ba_madhhab_differences", language), Icons.Default.Balance) {
                    Text(contract.madhhabDifferences.get(language), style = MaterialTheme.typography.bodyMedium)
                }
            }
            item {
                BusinessSectionCard(AppLocalization.get("ba_mufti_questions", language), Icons.Default.SupportAgent) {
                    EvidenceCategoryBadge(EvidenceCategory.SCHOLARLY_REVIEW_REQUIRED, language)
                    Spacer(modifier = Modifier.height(8.dp))
                    BulletListSection(contract.muftiConsultationQuestions.get(language), "?")
                }
            }
            if (quranCitations.isNotEmpty()) {
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        quranCitations.forEach { QuranCitationCard(it, language) }
                    }
                }
            }
            val hadiths = contract.hadithIds.mapNotNull { hadithCatalog[it] }
            if (hadiths.isNotEmpty()) {
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        hadiths.forEach { HadithCitationCard(it, language) }
                    }
                }
            }
            item {
                InteractiveMcqSection(contract.id, contract.mcqs, existingQuizResult, language, onSaveQuizScore)
            }
            item {
                DetailActionAndNotesBar(
                    contract.id, isBookmarked, isCompleted, existingNote, language,
                    onToggleBookmark, onToggleCompleted, onSaveNote
                )
            }
        }
    }
}

@Composable
fun BusinessSectorDetailScreen(
    sector: BusinessSectorGuide,
    quranCitations: List<BusinessQuranCitation>,
    hadithCatalog: Map<String, BusinessHadithCitation>,
    isBookmarked: Boolean,
    isCompleted: Boolean,
    existingNote: String,
    existingQuizResult: BusinessQuizResultEntity?,
    language: AppLanguage,
    onToggleBookmark: () -> Unit,
    onToggleCompleted: () -> Unit,
    onSaveNote: (String) -> Unit,
    onSaveQuizScore: (Int, Int) -> Unit,
    onBack: () -> Unit
) {
    val sections: List<Pair<String, String>> = listOf(
        "A. ${AppLocalization.get("ba_sec_intro", language)}" to sector.introductionA.get(language),
        "B. ${AppLocalization.get("ba_sec_operates", language)}" to sector.howItOperatesB.get(language),
        "C. ${AppLocalization.get("ba_sec_startup", language)}" to sector.startupRequirementsC.get(language),
        "D. ${AppLocalization.get("ba_sec_capital", language)}" to sector.capitalAndInvestmentD.get(language),
        "E. ${AppLocalization.get("ba_sec_suppliers", language)}" to sector.supplierSelectionE.get(language),
        "F. ${AppLocalization.get("ba_sec_procurement", language)}" to sector.purchasingProcurementF.get(language),
        "G. ${AppLocalization.get("ba_sec_ownership", language)}" to sector.ownershipPossessionG.get(language),
        "H. ${AppLocalization.get("ba_sec_products", language)}" to sector.productsServicesH.get(language),
        "I. ${AppLocalization.get("ba_sec_pricing", language)}" to sector.pricingProfitI.get(language),
        "J. ${AppLocalization.get("ba_sec_customers", language)}" to sector.customerRightsJ.get(language),
        "K. ${AppLocalization.get("ba_sec_employees", language)}" to sector.employeeRightsK.get(language)
    )

    Scaffold(
        topBar = {
            NoorTopBar(
                title = sector.name.get(language),
                language = language,
                onBackClick = onBack
            )
        }
    ) { innerPadding ->
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
                .testTag("sector_detail_${sector.id}")
        ) {
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.55f)),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.tertiary.copy(alpha = 0.4f))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = AppLocalization.get("ba_non_blanket_title", language),
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = sector.nonBlanketAssessmentNote.get(language),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    }
                }
            }
            sections.forEach { (heading, body) ->
                item {
                    BusinessSectionCard(heading) {
                        Text(body, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
            item {
                BusinessSectionCard("L. ${AppLocalization.get("ba_sec_contracts", language)}", Icons.Default.Handshake) {
                    BulletListSection(sector.relevantContractsL.get(language))
                }
            }
            item {
                BusinessSectionCard(
                    "M. ${AppLocalization.get("ba_sec_prohibited", language)}",
                    Icons.Default.Block,
                    containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f)
                ) {
                    BulletListSection(sector.potentialProhibitedM.get(language), "✕")
                }
            }
            item {
                BusinessSectionCard("N. ${AppLocalization.get("ba_compliance_checklist", language)}", Icons.Default.Verified) {
                    BulletListSection(sector.halalChecklistN.get(language), "✓")
                }
            }
            item {
                BusinessSectionCard("O. ${AppLocalization.get("ba_sec_case_study", language)}", Icons.Default.Cases) {
                    Text(sector.realisticCaseStudyO.get(language), style = MaterialTheme.typography.bodyMedium)
                }
            }
            item {
                BusinessSectionCard("P. ${AppLocalization.get("ba_common_mistakes", language)}", Icons.Default.WarningAmber) {
                    BulletListSection(sector.commonMistakesP.get(language), "!")
                }
            }
            item {
                BusinessSectionCard("Q. ${AppLocalization.get("ba_practical_assignment", language)}", Icons.Default.Assignment) {
                    Text(sector.practicalAssignmentQ.get(language), style = MaterialTheme.typography.bodyMedium)
                }
            }
            if (quranCitations.isNotEmpty()) {
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        quranCitations.forEach { QuranCitationCard(it, language) }
                    }
                }
            }
            val hadiths = sector.hadithIds.mapNotNull { hadithCatalog[it] }
            if (hadiths.isNotEmpty()) {
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        hadiths.forEach { HadithCitationCard(it, language) }
                    }
                }
            }
            item {
                InteractiveMcqSection(sector.id, sector.mcqs, existingQuizResult, language, onSaveQuizScore)
            }
            item {
                DetailActionAndNotesBar(
                    sector.id, isBookmarked, isCompleted, existingNote, language,
                    onToggleBookmark, onToggleCompleted, onSaveNote
                )
            }
            item {
                SourceRefsSection(sector.referencesR, language)
            }
        }
    }
}

@Composable
fun SpecializedTopicDetailScreen(
    topic: SpecializedTopicGuide,
    quranCitations: List<BusinessQuranCitation>,
    hadithCatalog: Map<String, BusinessHadithCitation>,
    isBookmarked: Boolean,
    isCompleted: Boolean,
    existingNote: String,
    existingQuizResult: BusinessQuizResultEntity?,
    language: AppLanguage,
    onToggleBookmark: () -> Unit,
    onToggleCompleted: () -> Unit,
    onSaveNote: (String) -> Unit,
    onSaveQuizScore: (Int, Int) -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            NoorTopBar(
                title = topic.title.get(language),
                language = language,
                onBackClick = onBack
            )
        }
    ) { innerPadding ->
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
                .testTag("specialized_detail_${topic.id}")
        ) {
            item {
                Card(shape = RoundedCornerShape(16.dp)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        EvidenceCategoryBadge(topic.evidenceCategory, language)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = topic.title.get(language),
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }
            if (topic.isDisputedContemporaryIssue) {
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.65f)),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.4f)),
                        modifier = Modifier.testTag("disputed_issue_banner_${topic.id}")
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = AppLocalization.get("ba_disputed_warning_title", language),
                                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onErrorContainer
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = AppLocalization.get("ba_disputed_warning_body", language),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onErrorContainer
                            )
                        }
                    }
                }
            }
            item {
                BusinessSectionCard(AppLocalization.get("ba_introduction", language), Icons.Default.Info) {
                    Text(topic.overview.get(language), style = MaterialTheme.typography.bodyMedium)
                }
            }
            item {
                BusinessSectionCard(AppLocalization.get("ba_quran_hadith_basis", language), Icons.Default.MenuBook) {
                    Text(topic.quranAndHadithBasis.get(language), style = MaterialTheme.typography.bodyMedium)
                }
            }
            item {
                BusinessSectionCard(AppLocalization.get("ba_classical_contemporary_positions", language), Icons.Default.Balance) {
                    Text(topic.classicalAndContemporaryPositions.get(language), style = MaterialTheme.typography.bodyMedium)
                }
            }
            item {
                BusinessSectionCard(AppLocalization.get("ba_practical_guidance", language), Icons.Default.FactCheck) {
                    Text(topic.practicalGuidance.get(language), style = MaterialTheme.typography.bodyMedium)
                }
            }
            item {
                BusinessSectionCard(AppLocalization.get("ba_scholarly_review_triggers", language), Icons.Default.SupportAgent) {
                    EvidenceCategoryBadge(EvidenceCategory.SCHOLARLY_REVIEW_REQUIRED, language)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(topic.scholarlyReviewTriggers.get(language), style = MaterialTheme.typography.bodyMedium)
                }
            }
            if (quranCitations.isNotEmpty()) {
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        quranCitations.forEach { QuranCitationCard(it, language) }
                    }
                }
            }
            val hadiths = topic.hadithIds.mapNotNull { hadithCatalog[it] }
            if (hadiths.isNotEmpty()) {
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        hadiths.forEach { HadithCitationCard(it, language) }
                    }
                }
            }
            item {
                InteractiveMcqSection(topic.id, topic.mcqs, existingQuizResult, language, onSaveQuizScore)
            }
            item {
                DetailActionAndNotesBar(
                    topic.id, isBookmarked, isCompleted, existingNote, language,
                    onToggleBookmark, onToggleCompleted, onSaveNote
                )
            }
            item {
                SourceRefsSection(topic.sources, language)
            }
        }
    }
}
