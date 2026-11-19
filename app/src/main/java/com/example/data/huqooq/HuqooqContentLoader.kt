package com.example.data.huqooq

import android.content.Context
import com.example.data.model.AppLanguage
import com.example.data.repository.QuranData
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.util.concurrent.ConcurrentHashMap

object HuqooqContentLoader {

    @Volatile
    private var cachedReadyState: HuqooqAcademyUiState.Ready? = null

    private val quranCitationCache = ConcurrentHashMap<String, HuqooqQuranCitation>()

    fun loadAll(context: Context?): HuqooqAcademyUiState.Ready {
        cachedReadyState?.let { return it }
        synchronized(this) {
            cachedReadyState?.let { return it }

            val hadithAndSources = JSONObject(readAssetText(context, "huqooq_hadith_and_sources.json"))
            val hadithCatalog = parseHadithMap(hadithAndSources.optJSONArray("hadiths"))
            val officialSources = parseSources(hadithAndSources.optJSONArray("officialSources"))

            val lessons = mutableListOf<HuqooqLesson>()
            listOf(
                "huqooq_dept1_rights_1_15.json",
                "huqooq_dept1_rights_16_30.json",
                "huqooq_dept2_faraid.json",
                "huqooq_dept3_islah_part1.json",
                "huqooq_dept3_islah_part2.json",
                "huqooq_dept3_islah_part3.json",
                "huqooq_dept3_islah_part4.json",
                "huqooq_dept3_islah_part5.json",
                "huqooq_dept4_akhlaq.json"
            ).forEach { fileName ->
                val arr = JSONObject(readAssetText(context, fileName)).optJSONArray("lessons") ?: JSONArray()
                for (i in 0 until arr.length()) {
                    lessons.add(parseLesson(arr.getJSONObject(i)))
                }
            }
            lessons.sortWith(compareBy({ it.department.ordinal }, { it.order }))

            val ready = HuqooqAcademyUiState.Ready(
                lessons = lessons,
                hadithCatalog = hadithCatalog,
                officialSources = officialSources
            )
            cachedReadyState = ready
            return ready
        }
    }

    /**
     * Resolves Quranic verses verbatim from bundled surah_*.json via QuranData.
     * Never fabricates Arabic or translations.
     */
    fun resolveQuranCitations(
        context: Context?,
        specs: List<HuqooqQuranRefSpec>
    ): List<HuqooqQuranCitation> {
        if (specs.isEmpty()) return emptyList()
        return specs.map { spec ->
            val cacheKey = "${spec.surahNumber}:${spec.startAyah}-${spec.endAyah}"
            quranCitationCache[cacheKey]?.copy(contextExplanation = spec.contextExplanation)?.also {
                return@map it
            }

            val ayahs = QuranData.getAyahsForSurah(context, spec.surahNumber)
                .filter { it.ayahNumber in spec.startAyah..spec.endAyah }

            val arabicText = ayahs.joinToString(" ") { "(${it.ayahNumber}) ${it.textArabic}" }.trim()
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
            val citation = HuqooqQuranCitation(
                surahNumber = spec.surahNumber,
                startAyah = spec.startAyah,
                endAyah = spec.endAyah,
                arabicText = arabicText,
                urduTranslation = urText,
                englishTranslation = enText,
                hindiTranslation = hiText,
                contextExplanation = spec.contextExplanation,
                sourceUrl = "https://quran.com/${spec.surahNumber}/$rangeSuffix"
            )
            if (arabicText.isNotBlank()) {
                quranCitationCache[cacheKey] = citation
            }
            citation
        }
    }

    fun searchAll(
        ready: HuqooqAcademyUiState.Ready,
        query: String,
        language: AppLanguage
    ): List<HuqooqSearchHit> {
        val q = query.trim().lowercase()
        if (q.isEmpty()) return emptyList()

        return ready.lessons.mapNotNull { lesson ->
            val searchable = listOf(
                lesson.title.en,
                lesson.title.ur,
                lesson.title.ar,
                lesson.title.hi,
                lesson.groupLabel.en,
                lesson.groupLabel.ur,
                lesson.groupLabel.ar,
                lesson.groupLabel.hi,
                lesson.introduction.en,
                lesson.introduction.ur,
                lesson.introduction.ar,
                lesson.introduction.hi,
                lesson.explanation.en,
                lesson.explanation.ur,
                lesson.explanation.ar,
                lesson.explanation.hi
            ).joinToString(" ").lowercase()

            if (searchable.contains(q)) {
                HuqooqSearchHit(
                    id = lesson.id,
                    department = lesson.department,
                    title = lesson.title,
                    subtitle = lesson.groupLabel
                )
            } else {
                null
            }
        }
    }

    private fun readAssetText(context: Context?, fileName: String): String {
        if (context != null) {
            try {
                context.assets.open("huqooq/$fileName").bufferedReader(Charsets.UTF_8).use {
                    return it.readText()
                }
            } catch (_: Exception) {
                // Fall through to local disk fallback for Robolectric/JVM tests
            }
        }
        val candidates = listOf(
            File("app/src/main/assets/huqooq/$fileName"),
            File("src/main/assets/huqooq/$fileName")
        )
        for (file in candidates) {
            if (file.exists()) {
                return file.readText(Charsets.UTF_8)
            }
        }
        throw IllegalStateException("Huqooq Academy asset not found: huqooq/$fileName")
    }

    private fun readStringOrJoinedArray(obj: JSONObject, key: String): String {
        val arr = obj.optJSONArray(key)
        if (arr != null) {
            return jsonArrayToStrings(arr).joinToString(" ")
        }
        return obj.optString(key, "")
    }

    private fun parseLocalizedText(obj: JSONObject?): HuqooqLocalized4 {
        if (obj == null) return HuqooqLocalized4("", "")
        val en = readStringOrJoinedArray(obj, "en")
        val ur = readStringOrJoinedArray(obj, "ur").ifBlank { en }
        val ar = readStringOrJoinedArray(obj, "ar").ifBlank { en }
        val hi = readStringOrJoinedArray(obj, "hi").ifBlank { en }
        return HuqooqLocalized4(en = en, ur = ur, ar = ar, hi = hi)
    }

    private fun parseOptionalLocalizedText(obj: JSONObject?): HuqooqLocalized4? {
        if (obj == null) return null
        val en = readStringOrJoinedArray(obj, "en")
        val ur = readStringOrJoinedArray(obj, "ur")
        if (en.isBlank() && ur.isBlank()) return null
        return HuqooqLocalized4(
            en = en,
            ur = ur.ifBlank { en },
            ar = readStringOrJoinedArray(obj, "ar").ifBlank { en },
            hi = readStringOrJoinedArray(obj, "hi").ifBlank { en }
        )
    }

    private fun readStringList(obj: JSONObject, key: String): List<String> {
        val arr = obj.optJSONArray(key)
        if (arr != null) return jsonArrayToStrings(arr)
        val single = obj.optString(key, "")
        return if (single.isNotBlank()) listOf(single) else emptyList()
    }

    private fun parseLocalizedList(obj: JSONObject?): HuqooqLocalizedList4 {
        if (obj == null) return HuqooqLocalizedList4(emptyList(), emptyList())
        val en = readStringList(obj, "en")
        val ur = readStringList(obj, "ur").ifEmpty { en }
        val ar = readStringList(obj, "ar").ifEmpty { en }
        val hi = readStringList(obj, "hi").ifEmpty { en }
        return HuqooqLocalizedList4(en = en, ur = ur, ar = ar, hi = hi)
    }

    private fun parseOptionalLocalizedList(obj: JSONObject?): HuqooqLocalizedList4? {
        if (obj == null) return null
        val list = parseLocalizedList(obj)
        return if (list.en.isEmpty() && list.ur.isEmpty()) null else list
    }

    private fun jsonArrayToStrings(arr: JSONArray?): List<String> {
        if (arr == null) return emptyList()
        val list = ArrayList<String>(arr.length())
        for (i in 0 until arr.length()) {
            val s = arr.optString(i, "")
            if (s.isNotBlank()) list.add(s)
        }
        return list
    }

    private fun parseHadithMap(arr: JSONArray?): Map<String, HuqooqHadithCitation> {
        if (arr == null) return emptyMap()
        val map = linkedMapOf<String, HuqooqHadithCitation>()
        for (i in 0 until arr.length()) {
            val o = arr.getJSONObject(i)
            val id = o.getString("id")
            map[id] = HuqooqHadithCitation(
                id = id,
                collection = o.optString("collection", ""),
                hadithNumber = o.optString("hadithNumber", ""),
                bookChapter = o.optString("bookChapter", ""),
                arabicText = o.optString("arabicText", ""),
                englishTranslation = o.optString("englishTranslation", ""),
                urduTranslation = o.optString("urduTranslation", ""),
                hindiTranslation = o.optString("hindiTranslation", ""),
                narrator = o.optString("narrator", ""),
                grading = o.optString("grading", ""),
                sourceNote = parseLocalizedText(o.optJSONObject("sourceNote")),
                sourceUrl = o.optString("sourceUrl", "")
            )
        }
        return map
    }

    private fun parseSources(arr: JSONArray?): List<HuqooqSourceLink> {
        if (arr == null) return emptyList()
        val list = mutableListOf<HuqooqSourceLink>()
        for (i in 0 until arr.length()) {
            val o = arr.getJSONObject(i)
            list.add(
                HuqooqSourceLink(
                    title = o.optString("title", ""),
                    attribution = o.optString("attribution", ""),
                    url = o.optString("url", "")
                )
            )
        }
        return list
    }

    private fun parseQuranSpecs(arr: JSONArray?): List<HuqooqQuranRefSpec> {
        if (arr == null) return emptyList()
        val list = mutableListOf<HuqooqQuranRefSpec>()
        for (i in 0 until arr.length()) {
            val o = arr.getJSONObject(i)
            val start = o.getInt("startAyah")
            list.add(
                HuqooqQuranRefSpec(
                    surahNumber = o.getInt("surahNumber"),
                    startAyah = start,
                    endAyah = o.optInt("endAyah", start),
                    contextExplanation = parseLocalizedText(o.optJSONObject("contextExplanation"))
                )
            )
        }
        return list
    }

    private fun parseMcqs(arr: JSONArray?): List<HuqooqMcq> {
        if (arr == null) return emptyList()
        val list = mutableListOf<HuqooqMcq>()
        for (i in 0 until arr.length()) {
            val o = arr.getJSONObject(i)
            list.add(
                HuqooqMcq(
                    id = o.optString("id", "hmcq_$i"),
                    question = parseLocalizedText(o.optJSONObject("question")),
                    options = parseLocalizedList(o.optJSONObject("options")),
                    correctIndex = o.optInt("correctIndex", 0),
                    explanation = parseLocalizedText(o.optJSONObject("explanation"))
                )
            )
        }
        return list
    }

    private fun parseLesson(o: JSONObject): HuqooqLesson {
        val dept = HuqooqDepartment.fromId(o.optString("departmentId"))
        val badgeId = o.optString("evidenceBadge", "quran")
        val badge = HuqooqEvidenceType.entries.firstOrNull { it.id == badgeId } ?: HuqooqEvidenceType.QURAN
        return HuqooqLesson(
            id = o.getString("id"),
            department = dept,
            order = o.getInt("order"),
            groupCode = o.optString("groupCode", "GENERAL"),
            groupLabel = parseLocalizedText(o.optJSONObject("groupLabel")),
            title = parseLocalizedText(o.optJSONObject("title")),
            introduction = parseLocalizedText(o.optJSONObject("introduction")),
            explanation = parseLocalizedText(o.optJSONObject("explanation")),
            dutyBearer = parseOptionalLocalizedText(o.optJSONObject("dutyBearer")),
            mutualBoundaries = parseOptionalLocalizedText(o.optJSONObject("mutualBoundaries")),
            rightsViolatedAction = parseOptionalLocalizedText(o.optJSONObject("rightsViolatedAction")),
            rootCauses = parseOptionalLocalizedList(o.optJSONObject("rootCauses")),
            individualSolution = parseOptionalLocalizedList(o.optJSONObject("individualSolution")),
            familySocietySolution = parseOptionalLocalizedList(o.optJSONObject("familySocietySolution")),
            safetyAndLegalSupport = parseOptionalLocalizedText(o.optJSONObject("safetyAndLegalSupport")),
            dailyExercise = parseOptionalLocalizedText(o.optJSONObject("dailyExercise")),
            practicalWays = parseLocalizedList(o.optJSONObject("practicalWays")),
            realLifeExamples = parseLocalizedList(o.optJSONObject("realLifeExamples")),
            commonMistakes = parseLocalizedList(o.optJSONObject("commonMistakes")),
            actionSteps = parseLocalizedList(o.optJSONObject("actionSteps")),
            scholarlyNuance = parseOptionalLocalizedText(o.optJSONObject("scholarlyNuance")),
            isDisputedOrComplex = o.optBoolean("isDisputedOrComplex", false),
            evidenceBadge = badge,
            quranRefSpecs = parseQuranSpecs(o.optJSONArray("quranRefSpecs")),
            hadithIds = jsonArrayToStrings(o.optJSONArray("hadithIds")),
            sourceLinks = parseSources(o.optJSONArray("sourceLinks")),
            mcqs = parseMcqs(o.optJSONArray("mcqs"))
        )
    }
}
