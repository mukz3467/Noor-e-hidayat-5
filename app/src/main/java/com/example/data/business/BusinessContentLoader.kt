package com.example.data.business

import android.content.Context
import com.example.data.model.AppLanguage
import com.example.data.repository.QuranData
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.util.concurrent.ConcurrentHashMap

data class BusinessSearchHit(
    val id: String,
    val type: String, // "lesson", "contract", "sector", "specialized"
    val title: LocalizedText4,
    val subtitle: LocalizedText4
)

object BusinessContentLoader {

    @Volatile
    private var cachedReadyState: BusinessAcademyUiState.Ready? = null

    private val quranCitationCache = ConcurrentHashMap<String, BusinessQuranCitation>()

    fun loadAll(context: Context?): BusinessAcademyUiState.Ready {
        cachedReadyState?.let { return it }
        synchronized(this) {
            cachedReadyState?.let { return it }

            val hadithObj = JSONObject(readAssetText(context, "business_hadith_and_sources.json"))
            val hadithMap = parseHadithMap(hadithObj.optJSONArray("hadiths"))
            val officialSources = parseSources(hadithObj.optJSONArray("officialSources"))

            val lessons = mutableListOf<BusinessLesson>()
            listOf(
                "business_lessons_l1.json",
                "business_lessons_l2.json",
                "business_lessons_l4a.json",
                "business_lessons_l4b.json"
            ).forEach { fileName ->
                val arr = JSONObject(readAssetText(context, fileName)).optJSONArray("lessons") ?: JSONArray()
                for (i in 0 until arr.length()) {
                    lessons.add(parseLesson(arr.getJSONObject(i)))
                }
            }
            lessons.sortWith(compareBy({ it.level }, { it.moduleNumber }))

            val contracts = mutableListOf<BusinessContractGuide>()
            listOf("business_contracts_part1.json", "business_contracts_part2.json").forEach { fileName ->
                val arr = JSONObject(readAssetText(context, fileName)).optJSONArray("contracts") ?: JSONArray()
                for (i in 0 until arr.length()) {
                    contracts.add(parseContract(arr.getJSONObject(i)))
                }
            }
            contracts.sortBy { it.order }

            val sectors = mutableListOf<BusinessSectorGuide>()
            listOf(
                "business_sectors_1_6.json",
                "business_sectors_7_12.json",
                "business_sectors_13_18.json",
                "business_sectors_19_24.json",
                "business_sectors_25_30.json"
            ).forEach { fileName ->
                val arr = JSONObject(readAssetText(context, fileName)).optJSONArray("sectors") ?: JSONArray()
                for (i in 0 until arr.length()) {
                    sectors.add(parseSector(arr.getJSONObject(i)))
                }
            }
            sectors.sortBy { it.sectorNumber }

            val specArr = JSONObject(readAssetText(context, "business_specialized.json"))
                .optJSONArray("specializedTopics") ?: JSONArray()
            val specializedTopics = mutableListOf<SpecializedTopicGuide>()
            for (i in 0 until specArr.length()) {
                specializedTopics.add(parseSpecialized(specArr.getJSONObject(i)))
            }
            specializedTopics.sortBy { it.order }

            val ready = BusinessAcademyUiState.Ready(
                lessons = lessons,
                contracts = contracts,
                sectors = sectors,
                specializedTopics = specializedTopics,
                hadithCatalog = hadithMap,
                officialSources = officialSources
            )
            cachedReadyState = ready
            return ready
        }
    }

    /**
     * Resolves Quranic citations directly from bundled surah_*.json via QuranData without inventing text.
     */
    fun resolveQuranCitations(
        context: Context?,
        specs: List<BusinessQuranRefSpec>
    ): List<BusinessQuranCitation> {
        if (specs.isEmpty()) return emptyList()
        return specs.map { spec ->
            val cacheKey = "${spec.surahNumber}:${spec.startAyah}-${spec.endAyah}"
            quranCitationCache[cacheKey]?.copy(
                contextNote = spec.contextNote,
                businessRelevance = spec.businessRelevance
            )?.also { return@map it }

            val ayahs = QuranData.getAyahsForSurah(context, spec.surahNumber)
                .filter { it.ayahNumber in spec.startAyah..spec.endAyah }

            val arabicText = if (ayahs.isNotEmpty()) {
                ayahs.joinToString(" ") { "(${it.ayahNumber}) ${it.textArabic}" }
            } else {
                ""
            }
            val urText = ayahs.joinToString(" ") { ayah ->
                val tr = ayah.translations.firstOrNull { it.language == AppLanguage.URDU }?.translationText.orEmpty()
                if (tr.isNotBlank()) "(${ayah.ayahNumber}) $tr" else ""
            }.trim()
            val enText = ayahs.joinToString(" ") { ayah ->
                val tr = ayah.translations.firstOrNull { it.language == AppLanguage.ENGLISH }?.translationText.orEmpty()
                if (tr.isNotBlank()) "(${ayah.ayahNumber}) $tr" else ""
            }.trim()
            val hiText = ayahs.joinToString(" ") { ayah ->
                val tr = ayah.translations.firstOrNull { it.language == AppLanguage.HINDI }?.translationText.orEmpty()
                if (tr.isNotBlank()) "(${ayah.ayahNumber}) $tr" else ""
            }.trim()

            val rangeSuffix = if (spec.endAyah > spec.startAyah) {
                "${spec.startAyah}-${spec.endAyah}"
            } else {
                "${spec.startAyah}"
            }
            val citation = BusinessQuranCitation(
                surahNumber = spec.surahNumber,
                startAyah = spec.startAyah,
                endAyah = spec.endAyah,
                arabicText = arabicText,
                urduTranslation = urText,
                englishTranslation = enText,
                hindiTranslation = hiText,
                contextNote = spec.contextNote,
                businessRelevance = spec.businessRelevance,
                sourceUrl = "https://quran.com/${spec.surahNumber}/$rangeSuffix"
            )
            if (arabicText.isNotBlank()) {
                quranCitationCache[cacheKey] = citation
            }
            citation
        }
    }

    fun searchAll(
        ready: BusinessAcademyUiState.Ready,
        query: String,
        language: AppLanguage
    ): List<BusinessSearchHit> {
        val q = query.trim().lowercase()
        if (q.isEmpty()) return emptyList()
        val hits = mutableListOf<BusinessSearchHit>()

        ready.lessons.forEach { l ->
            val searchable = listOf(
                l.title.en, l.title.ur, l.title.ar, l.title.hi,
                l.moduleTitle.en, l.moduleTitle.ur,
                l.explanationEn, l.explanationUr,
                l.introduction.en, l.introduction.ur
            ).joinToString(" ").lowercase()
            if (searchable.contains(q)) {
                hits.add(
                    BusinessSearchHit(
                        id = l.id,
                        type = "lesson",
                        title = l.title,
                        subtitle = l.moduleTitle
                    )
                )
            }
        }

        ready.contracts.forEach { c ->
            val searchable = listOf(
                c.name.en, c.name.ur, c.name.ar, c.name.hi,
                c.arabicTerm, c.definition.en, c.definition.ur, c.purpose.en, c.aaoifiStandard
            ).joinToString(" ").lowercase()
            if (searchable.contains(q)) {
                hits.add(
                    BusinessSearchHit(
                        id = c.id,
                        type = "contract",
                        title = c.name,
                        subtitle = LocalizedText4(
                            en = "${c.arabicTerm} • ${c.aaoifiStandard}",
                            ur = "${c.arabicTerm} • ${c.aaoifiStandard}",
                            ar = "${c.arabicTerm} • ${c.aaoifiStandard}",
                            hi = "${c.arabicTerm} • ${c.aaoifiStandard}"
                        )
                    )
                )
            }
        }

        ready.sectors.forEach { s ->
            val searchable = listOf(
                s.name.en, s.name.ur, s.name.ar, s.name.hi,
                s.introductionA.en, s.introductionA.ur,
                s.productsServicesH.en, s.realisticCaseStudyO.en
            ).joinToString(" ").lowercase()
            if (searchable.contains(q)) {
                hits.add(
                    BusinessSearchHit(
                        id = s.id,
                        type = "sector",
                        title = s.name,
                        subtitle = s.introductionA
                    )
                )
            }
        }

        ready.specializedTopics.forEach { t ->
            val searchable = listOf(
                t.title.en, t.title.ur, t.title.ar, t.title.hi,
                t.overview.en, t.overview.ur,
                t.classicalAndContemporaryPositions.en, t.classicalAndContemporaryPositions.ur
            ).joinToString(" ").lowercase()
            if (searchable.contains(q)) {
                hits.add(
                    BusinessSearchHit(
                        id = t.id,
                        type = "specialized",
                        title = t.title,
                        subtitle = t.overview
                    )
                )
            }
        }
        return hits
    }

    private fun readAssetText(context: Context?, fileName: String): String {
        if (context != null) {
            try {
                context.assets.open("business/$fileName").bufferedReader(Charsets.UTF_8).use {
                    return it.readText()
                }
            } catch (_: Exception) {
                // Fall through to local project-path fallback (useful in JVM unit tests)
            }
        }
        val candidates = listOf(
            File("app/src/main/assets/business/$fileName"),
            File("src/main/assets/business/$fileName")
        )
        for (file in candidates) {
            if (file.exists()) {
                return file.readText(Charsets.UTF_8)
            }
        }
        throw IllegalStateException("Business Academy asset not found: business/$fileName")
    }

    private fun parseLocalizedText(obj: JSONObject?): LocalizedText4 {
        if (obj == null) return LocalizedText4("", "")
        val en = obj.optString("en", "")
        val ur = obj.optString("ur", "").ifBlank { en }
        val ar = obj.optString("ar", "").ifBlank { en }
        val hi = obj.optString("hi", "").ifBlank { en }
        return LocalizedText4(en = en, ur = ur, ar = ar, hi = hi)
    }

    private fun parseLocalizedList(obj: JSONObject?): LocalizedList4 {
        if (obj == null) return LocalizedList4(emptyList(), emptyList())
        val en = jsonArrayToStrings(obj.optJSONArray("en"))
        val ur = jsonArrayToStrings(obj.optJSONArray("ur")).ifEmpty { en }
        val ar = jsonArrayToStrings(obj.optJSONArray("ar")).ifEmpty { en }
        val hi = jsonArrayToStrings(obj.optJSONArray("hi")).ifEmpty { en }
        return LocalizedList4(en = en, ur = ur, ar = ar, hi = hi)
    }

    private fun jsonArrayToStrings(arr: JSONArray?): List<String> {
        if (arr == null) return emptyList()
        val list = ArrayList<String>(arr.length())
        for (i in 0 until arr.length()) {
            val v = arr.optString(i, "")
            if (v.isNotBlank()) list.add(v)
        }
        return list
    }

    private fun parseQuranRefSpecs(arr: JSONArray?): List<BusinessQuranRefSpec> {
        if (arr == null) return emptyList()
        val list = mutableListOf<BusinessQuranRefSpec>()
        for (i in 0 until arr.length()) {
            val o = arr.getJSONObject(i)
            list.add(
                BusinessQuranRefSpec(
                    surahNumber = o.getInt("surahNumber"),
                    startAyah = o.getInt("startAyah"),
                    endAyah = o.optInt("endAyah", o.getInt("startAyah")),
                    contextNote = parseLocalizedText(o.optJSONObject("contextNote")),
                    businessRelevance = parseLocalizedText(o.optJSONObject("businessRelevance"))
                )
            )
        }
        return list
    }

    private fun parseSources(arr: JSONArray?): List<BusinessSourceRef> {
        if (arr == null) return emptyList()
        val list = mutableListOf<BusinessSourceRef>()
        for (i in 0 until arr.length()) {
            val o = arr.getJSONObject(i)
            list.add(
                BusinessSourceRef(
                    title = o.optString("title", ""),
                    category = EvidenceCategory.fromId(o.optString("category", "")),
                    attribution = o.optString("attribution", ""),
                    url = o.optString("url", "")
                )
            )
        }
        return list
    }

    private fun parseMcqs(arr: JSONArray?): List<BusinessMcq> {
        if (arr == null) return emptyList()
        val list = mutableListOf<BusinessMcq>()
        for (i in 0 until arr.length()) {
            val o = arr.getJSONObject(i)
            list.add(
                BusinessMcq(
                    id = o.optString("id", "mcq_$i"),
                    question = parseLocalizedText(o.optJSONObject("question")),
                    options = parseLocalizedList(o.optJSONObject("options")),
                    correctIndex = o.optInt("correctIndex", 0),
                    explanation = parseLocalizedText(o.optJSONObject("explanation"))
                )
            )
        }
        return list
    }

    private fun parseHadithMap(arr: JSONArray?): Map<String, BusinessHadithCitation> {
        if (arr == null) return emptyMap()
        val map = linkedMapOf<String, BusinessHadithCitation>()
        for (i in 0 until arr.length()) {
            val o = arr.getJSONObject(i)
            val id = o.getString("id")
            map[id] = BusinessHadithCitation(
                id = id,
                collection = o.optString("collection", ""),
                hadithNumber = o.optString("hadithNumber", ""),
                chapter = o.optString("chapter", ""),
                arabicText = o.optString("arabicText", ""),
                urduTranslation = o.optString("urduTranslation", ""),
                englishTranslation = o.optString("englishTranslation", ""),
                hindiTranslation = o.optString("hindiTranslation", ""),
                narrator = o.optString("narrator", ""),
                grading = o.optString("grading", ""),
                gradingSourceAndNotes = parseLocalizedText(o.optJSONObject("gradingSourceAndNotes")),
                sourceUrl = o.optString("sourceUrl", "")
            )
        }
        return map
    }

    private fun parseLesson(o: JSONObject): BusinessLesson {
        val hvpArr = o.optJSONArray("halalVsProhibited") ?: JSONArray()
        val hvpList = mutableListOf<HalalVsProhibitedRow>()
        for (i in 0 until hvpArr.length()) {
            val r = hvpArr.getJSONObject(i)
            hvpList.add(
                HalalVsProhibitedRow(
                    halalPractice = parseLocalizedText(r.optJSONObject("halalPractice")),
                    prohibitedPractice = parseLocalizedText(r.optJSONObject("prohibitedPractice"))
                )
            )
        }
        val enExp = o.optString("explanationEn", "")
        return BusinessLesson(
            id = o.getString("id"),
            level = o.getInt("level"),
            moduleNumber = o.getInt("moduleNumber"),
            moduleTitle = parseLocalizedText(o.optJSONObject("moduleTitle")),
            title = parseLocalizedText(o.optJSONObject("title")),
            primaryEvidenceCategory = EvidenceCategory.fromId(o.optString("primaryEvidenceCategory")),
            learningObjectives = parseLocalizedList(o.optJSONObject("learningObjectives")),
            introduction = parseLocalizedText(o.optJSONObject("introduction")),
            explanationUr = o.optString("explanationUr", "").ifBlank { enExp },
            explanationEn = enExp,
            explanationAr = o.optString("explanationAr", "").ifBlank { enExp },
            explanationHi = o.optString("explanationHi", "").ifBlank { enExp },
            quranRefSpecs = parseQuranRefSpecs(o.optJSONArray("quranRefSpecs")),
            hadithIds = jsonArrayToStrings(o.optJSONArray("hadithIds")),
            classicalFiqh = parseLocalizedText(o.optJSONObject("classicalFiqh")),
            contemporaryGuidance = parseLocalizedText(o.optJSONObject("contemporaryGuidance")),
            madhhabDifferences = parseLocalizedText(o.optJSONObject("madhhabDifferences")),
            modernExample = parseLocalizedText(o.optJSONObject("modernExample")),
            halalVsProhibited = hvpList,
            commonMistakes = parseLocalizedList(o.optJSONObject("commonMistakes")),
            complianceChecklist = parseLocalizedList(o.optJSONObject("complianceChecklist")),
            summary = parseLocalizedText(o.optJSONObject("summary")),
            mcqs = parseMcqs(o.optJSONArray("mcqs")),
            practicalAssignment = parseLocalizedText(o.optJSONObject("practicalAssignment")),
            sources = parseSources(o.optJSONArray("sources"))
        )
    }

    private fun parseContract(o: JSONObject): BusinessContractGuide {
        return BusinessContractGuide(
            id = o.getString("id"),
            order = o.getInt("order"),
            arabicTerm = o.optString("arabicTerm", ""),
            name = parseLocalizedText(o.optJSONObject("name")),
            definition = parseLocalizedText(o.optJSONObject("definition")),
            purpose = parseLocalizedText(o.optJSONObject("purpose")),
            shariahPrinciples = parseLocalizedText(o.optJSONObject("shariahPrinciples")),
            conditions = parseLocalizedList(o.optJSONObject("conditions")),
            partyRights = parseLocalizedText(o.optJSONObject("partyRights")),
            ownershipAndPossession = parseLocalizedText(o.optJSONObject("ownershipAndPossession")),
            profitAndLossRules = parseLocalizedText(o.optJSONObject("profitAndLossRules")),
            commonInvalidStructures = parseLocalizedList(o.optJSONObject("commonInvalidStructures")),
            modernExamples = parseLocalizedText(o.optJSONObject("modernExamples")),
            aaoifiStandard = o.optString("aaoifiStandard", ""),
            sbpGuidance = parseLocalizedText(o.optJSONObject("sbpGuidance")),
            madhhabDifferences = parseLocalizedText(o.optJSONObject("madhhabDifferences")),
            muftiConsultationQuestions = parseLocalizedList(o.optJSONObject("muftiConsultationQuestions")),
            quranRefSpecs = parseQuranRefSpecs(o.optJSONArray("quranRefSpecs")),
            hadithIds = jsonArrayToStrings(o.optJSONArray("hadithIds")),
            mcqs = parseMcqs(o.optJSONArray("mcqs"))
        )
    }

    private fun parseSector(o: JSONObject): BusinessSectorGuide {
        return BusinessSectorGuide(
            id = o.getString("id"),
            sectorNumber = o.getInt("sectorNumber"),
            name = parseLocalizedText(o.optJSONObject("name")),
            nonBlanketAssessmentNote = parseLocalizedText(o.optJSONObject("nonBlanketAssessmentNote")),
            introductionA = parseLocalizedText(o.optJSONObject("introductionA")),
            howItOperatesB = parseLocalizedText(o.optJSONObject("howItOperatesB")),
            startupRequirementsC = parseLocalizedText(o.optJSONObject("startupRequirementsC")),
            capitalAndInvestmentD = parseLocalizedText(o.optJSONObject("capitalAndInvestmentD")),
            supplierSelectionE = parseLocalizedText(o.optJSONObject("supplierSelectionE")),
            purchasingProcurementF = parseLocalizedText(o.optJSONObject("purchasingProcurementF")),
            ownershipPossessionG = parseLocalizedText(o.optJSONObject("ownershipPossessionG")),
            productsServicesH = parseLocalizedText(o.optJSONObject("productsServicesH")),
            pricingProfitI = parseLocalizedText(o.optJSONObject("pricingProfitI")),
            customerRightsJ = parseLocalizedText(o.optJSONObject("customerRightsJ")),
            employeeRightsK = parseLocalizedText(o.optJSONObject("employeeRightsK")),
            relevantContractsL = parseLocalizedList(o.optJSONObject("relevantContractsL")),
            potentialProhibitedM = parseLocalizedList(o.optJSONObject("potentialProhibitedM")),
            halalChecklistN = parseLocalizedList(o.optJSONObject("halalChecklistN")),
            realisticCaseStudyO = parseLocalizedText(o.optJSONObject("realisticCaseStudyO")),
            commonMistakesP = parseLocalizedList(o.optJSONObject("commonMistakesP")),
            practicalAssignmentQ = parseLocalizedText(o.optJSONObject("practicalAssignmentQ")),
            referencesR = parseSources(o.optJSONArray("referencesR")),
            quranRefSpecs = parseQuranRefSpecs(o.optJSONArray("quranRefSpecs")),
            hadithIds = jsonArrayToStrings(o.optJSONArray("hadithIds")),
            mcqs = parseMcqs(o.optJSONArray("mcqs"))
        )
    }

    private fun parseSpecialized(o: JSONObject): SpecializedTopicGuide {
        return SpecializedTopicGuide(
            id = o.getString("id"),
            order = o.getInt("order"),
            title = parseLocalizedText(o.optJSONObject("title")),
            evidenceCategory = EvidenceCategory.fromId(o.optString("evidenceCategory")),
            isDisputedContemporaryIssue = o.optBoolean("isDisputedContemporaryIssue", false),
            overview = parseLocalizedText(o.optJSONObject("overview")),
            quranAndHadithBasis = parseLocalizedText(o.optJSONObject("quranAndHadithBasis")),
            classicalAndContemporaryPositions = parseLocalizedText(o.optJSONObject("classicalAndContemporaryPositions")),
            practicalGuidance = parseLocalizedText(o.optJSONObject("practicalGuidance")),
            scholarlyReviewTriggers = parseLocalizedText(o.optJSONObject("scholarlyReviewTriggers")),
            quranRefSpecs = parseQuranRefSpecs(o.optJSONArray("quranRefSpecs")),
            hadithIds = jsonArrayToStrings(o.optJSONArray("hadithIds")),
            sources = parseSources(o.optJSONArray("sources")),
            mcqs = parseMcqs(o.optJSONArray("mcqs"))
        )
    }
}
