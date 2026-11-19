package com.example.data.salah

import android.content.Context
import com.example.data.model.AppLanguage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

data class MissedSalahTrackerState(
    val entries: List<MissedSalahEntryEntity> = emptyList(),
    val guidance: FiqhGuidancePerspective = FiqhGuidancePerspective.COMPARATIVE_OVERVIEW,
    val estimate: BacklogEstimateConfig = BacklogEstimateConfig(),
    val planner: QadaPlannerConfig = QadaPlannerConfig(),
    val todayDateKey: String = MissedSalahRepository.todayIsoDateKey(),
    val lastBackupFilePath: String? = null
) {
    val nonExemptEntries: List<MissedSalahEntryEntity>
        get() = entries.filter { it.statusEnum() != QadaEntryStatus.EXEMPT_NOT_REQUIRED }

    val exemptCount: Int
        get() = entries.count { it.statusEnum() == QadaEntryStatus.EXEMPT_NOT_REQUIRED }

    val totalConfirmedMissed: Int
        get() = nonExemptEntries.size

    val completedConfirmedCount: Int
        get() = nonExemptEntries.count { it.statusEnum() == QadaEntryStatus.COMPLETED }

    val pendingQadaEntries: List<MissedSalahEntryEntity>
        get() = nonExemptEntries.filter { entry ->
            if (entry.statusEnum() != QadaEntryStatus.PENDING) {
                false
            } else if (guidance == FiqhGuidancePerspective.UNINTENTIONAL_ONLY_VIEW &&
                entry.reasonEnum() == MissedReasonCategory.DELIBERATE_OR_NEGLIGENCE
            ) {
                false
            } else {
                true
            }
        }

    val pendingQadaCount: Int
        get() = pendingQadaEntries.size

    val deliberateSeparatePendingCount: Int
        get() = if (guidance == FiqhGuidancePerspective.UNINTENTIONAL_ONLY_VIEW) {
            nonExemptEntries.count {
                it.statusEnum() == QadaEntryStatus.PENDING &&
                    it.reasonEnum() == MissedReasonCategory.DELIBERATE_OR_NEGLIGENCE
            }
        } else {
            0
        }

    val completedTodayCount: Int
        get() = nonExemptEntries.count {
            it.statusEnum() == QadaEntryStatus.COMPLETED && it.completedDateKey == todayDateKey
        }

    val completedThisWeekCount: Int
        get() {
            val cutoff = MissedSalahRepository.offsetIsoDateKey(todayDateKey, -6)
            return nonExemptEntries.count { entry ->
                entry.statusEnum() == QadaEntryStatus.COMPLETED &&
                    entry.completedDateKey != null &&
                    entry.completedDateKey >= cutoff &&
                    entry.completedDateKey <= todayDateKey
            }
        }

    val confirmedProgressFraction: Float
        get() {
            val relevantTotal = completedConfirmedCount + pendingQadaCount
            return if (relevantTotal <= 0) 0f else completedConfirmedCount.toFloat() / relevantTotal.toFloat()
        }

    val prayerBreakdown: Map<SalahPrayerType, PrayerBreakdownCounts>
        get() = SalahPrayerType.entries.associateWith { prayer ->
            val ofPrayer = entries.filter { it.prayerEnum() == prayer }
            PrayerBreakdownCounts(
                totalRecorded = ofPrayer.count { it.statusEnum() != QadaEntryStatus.EXEMPT_NOT_REQUIRED },
                pendingQada = pendingQadaEntries.count { it.prayerEnum() == prayer },
                completed = ofPrayer.count { it.statusEnum() == QadaEntryStatus.COMPLETED },
                exemptCount = ofPrayer.count { it.statusEnum() == QadaEntryStatus.EXEMPT_NOT_REQUIRED }
            )
        }
}

object MissedSalahRepository {

    private const val PREFS_NAME = "missed_salah_private_prefs"
    private const val BACKUP_DIR_NAME = "missed_salah_backups"
    private const val KEY_GUIDANCE = "fiqh_guidance_mode"
    private const val KEY_ESTIMATE_JSON = "backlog_estimate_json"
    private const val KEY_PLANNER_JSON = "qada_planner_json"

    val VERIFIED_REFERENCES: List<VerifiedSalahReference> = listOf(
        VerifiedSalahReference(
            id = "bukhari_597",
            citationTitle = "Sahih al-Bukhari 597",
            url = "https://sunnah.com/bukhari:597",
            arabicText = "مَنْ نَسِيَ صَلاَةً فَلْيُصَلِّ إِذَا ذَكَرَهَا، لاَ كَفَّارَةَ لَهَا إِلَّا ذَلِكَ",
            translations = mapOf(
                AppLanguage.ENGLISH to "Whoever forgets a prayer should pray it when he remembers it; there is no expiation for it except that. (And Allah said: 'Establish prayer for My remembrance.')",
                AppLanguage.URDU to "جو شخص نماز بھول جائے تو جب یاد آئے اسے پڑھ لے، اس کا کفارہ اس کے سوا کچھ نہیں۔",
                AppLanguage.ARABIC to "مَنْ نَسِيَ صَلَاةً فَلْيُصَلِّ إِذَا ذَكَرَهَا، لَا كَفَّارَةَ لَهَا إِلَّا ذَلِكَ.",
                AppLanguage.HINDI to "जो कोई नमाज़ भूल जाए तो याद आते ही उसे अदा करे; इसका कोई और कफ़्फ़ारा नहीं।"
            ),
            scopeNote = mapOf(
                AppLanguage.ENGLISH to "Concerns prayers missed by forgetting or sleeping; scholars discuss deliberately omitted prayers separately.",
                AppLanguage.URDU to "یہ حدیث بھول یا نیند سے چھوٹی نماز سے متعلق ہے؛ عمداً ترک کے حکم پر علماء میں علمی اختلاف موجود ہے۔",
                AppLanguage.ARABIC to "يختص الحديث بالناسي والنائم؛ أما تارك الصلاة عمداً فله بحث فقهي مستقل عند العلماء.",
                AppLanguage.HINDI to "यह हदीस भूल या नींद से छूटी नमाज़ के बारे में है; जानबूझकर छोड़ने पर विद्वानों में अलग चर्चा है।"
            )
        ),
        VerifiedSalahReference(
            id = "muslim_684a",
            citationTitle = "Sahih Muslim 684a",
            url = "https://sunnah.com/muslim:684a",
            arabicText = "مَنْ نَسِيَ صَلَاةً، أَوْ نَامَ عَنْهَا، فَكَفَّارَتُهَا أَنْ يُصَلِّيَهَا إِذَا ذَكَرَهَا",
            translations = mapOf(
                AppLanguage.ENGLISH to "Whoever forgets a prayer or sleeps through it, its expiation is to pray it when he remembers it.",
                AppLanguage.URDU to "جو نماز بھول جائے یا سویا رہ جائے تو اس کا کفارہ یہ ہے کہ جب یاد آئے اسے ادا کرے۔",
                AppLanguage.ARABIC to "مَنْ نَسِيَ صَلَاةً أَوْ نَامَ عَنْهَا فَكَفَّارَتُهَا أَنْ يُصَلِّيَهَا إِذَا ذَكَرَهَا.",
                AppLanguage.HINDI to "जो कोई नमाज़ भूल जाए या सोता रह जाए तो उसका कफ़्फ़ारा यह है कि याद आते ही उसे पढ़े।"
            ),
            scopeNote = mapOf(
                AppLanguage.ENGLISH to "Addresses unintentional sleep and forgetfulness directly; not a blanket verdict closing juristic differences on deliberate omission.",
                AppLanguage.URDU to "بھول اور نیند کی صورت بیان کی گئی ہے؛ عمداً ترک پر تمام فقہی مسائل اس ایک حدیث سے ختم نہیں ہوتے۔",
                AppLanguage.ARABIC to "يبين حكم النوم والنسيان صراحة، مع بقاء الخلاف الفقهي المعروف في الترك العمدي.",
                AppLanguage.HINDI to "अनिच्छा से नींद या भूल की स्थिति स्पष्ट करता है; जानबूझकर छोड़ने के सभी पहलुओं पर विद्वानों से मार्गदर्शन लें।"
            )
        ),
        VerifiedSalahReference(
            id = "muslim_684c",
            citationTitle = "Sahih Muslim 684c",
            url = "https://sunnah.com/muslim:684c",
            arabicText = "لَيْسَ فِي النَّوْمِ تَفْرِيطٌ، إِنَّمَا التَّفْرِيطُ فِي الْيَقَظَةِ",
            translations = mapOf(
                AppLanguage.ENGLISH to "There is no negligence in sleep; negligence is only while awake (narrated when the Companions overslept on travel and performed Fajr upon waking).",
                AppLanguage.URDU to "سونے میں کوئی کوتاہی نہیں، کوتاہی تو جاگتے ہوئے نماز ٹال دینے میں ہے (سفر میں فجر قضا ہو جانے پر بیدار ہوتے ہی ادا فرمائی)۔",
                AppLanguage.ARABIC to "ليس في النوم تفريط، إنما التفريط في اليقظة.",
                AppLanguage.HINDI to "सोने में कोई लापरवाही नहीं है; लापरवाही तो जागते हुए नमाज़ टालने में है।"
            ),
            scopeNote = mapOf(
                AppLanguage.ENGLISH to "Distinguishes unintentional sleep from wakeful negligence.",
                AppLanguage.URDU to "نیند کی مجبوری اور جاگتے ہوئے غفلت کے درمیان واضح فرق بیان فرماتی ہے۔",
                AppLanguage.ARABIC to "يفرق بوضوح بين العذر في النوم والتفريط في اليقظة.",
                AppLanguage.HINDI to "नींद और जागते हुए की लापरवाही के बीच स्पष्ट अंतर करता है।"
            )
        ),
        VerifiedSalahReference(
            id = "quran_20_14",
            citationTitle = "Quran 20:14 (Surah Taha)",
            url = "https://quran.com/20/14",
            arabicText = "إِنَّنِي أَنَا اللَّهُ لَا إِلَٰهَ إِلَّا أَنَا فَاعْبُدْنِي وَأَقِمِ الصَّلَاةَ لِذِكْرِي",
            translations = mapOf(
                AppLanguage.ENGLISH to "Indeed, I am Allah. There is no deity except Me, so worship Me and establish prayer for My remembrance. (Saheeh International)",
                AppLanguage.URDU to "کچھ شک نہیں کہ میں ہی خدا ہوں میرے سوا کوئی معبود نہیں تو میری ہی عبادت کر اور میری یاد کے لیے نماز پڑھا کر۔ (فتح محمد جالندھری)",
                AppLanguage.ARABIC to "إِنَّنِي أَنَا اللَّهُ لَا إِلَٰهَ إِلَّا أَنَا فَاعْبُدْنِي وَأَقِمِ الصَّلَاةَ لِذِكْرِي",
                AppLanguage.HINDI to "निःसंदेह मैं ही अल्लाह हूँ, मेरे अतिरिक्त कोई पूज्य-प्रभु नहीं, अतः तू मेरी बन्दगी कर और मेरी याद के लिए नमाज़ क़ायम कर। (फ़ारूक़ ख़ान)"
            ),
            scopeNote = mapOf(
                AppLanguage.ENGLISH to "Cited by the Prophet ﷺ in Sahih al-Bukhari 597 & Muslim 684a.",
                AppLanguage.URDU to "صحیح بخاری 597 اور مسلم 684 میں اس آیت کا حوالہ دیا گیا ہے۔",
                AppLanguage.ARABIC to "استشهد النبي ﷺ بهذه الآية الكريمة في حديث البخاري 597 ومسلم 684.",
                AppLanguage.HINDI to "सहीह बुख़ारी 597 व मुस्लिम 684 में इसी आयत का उल्लेख आया है।"
            )
        ),
        VerifiedSalahReference(
            id = "quran_4_103",
            citationTitle = "Quran 4:103 (Surah An-Nisa)",
            url = "https://quran.com/4/103",
            arabicText = "إِنَّ الصَّلَاةَ كَانَتْ عَلَى الْمُؤْمِنِينَ كِتَابًا مَّوْقُوتًا",
            translations = mapOf(
                AppLanguage.ENGLISH to "Indeed, prayer has been decreed upon the believers a decree of specified times. (Saheeh International)",
                AppLanguage.URDU to "بے شک نماز مومنوں پر مقررہ وقتوں میں فرض ہے۔ (فتح محمد جالندھری)",
                AppLanguage.ARABIC to "إِنَّ الصَّلَاةَ كَانَتْ عَلَى الْمُؤْمِنِينَ كِتَابًا مَّوْقُوتًا",
                AppLanguage.HINDI to "निश्चय ही नमाज़ ईमानवालों पर निर्धारित समय पर फ़र्ज़ की गई है। (फ़ारूक़ ख़ान)"
            ),
            scopeNote = mapOf(
                AppLanguage.ENGLISH to "Establishes the fixed-time obligation of every daily Salah.",
                AppLanguage.URDU to "ہر نماز کی مقررہ وقت پر فرضیت کو بیان فرماتی ہے۔",
                AppLanguage.ARABIC to "تثبت فرضية الصلوات الخمس في أوقاتها المحددة.",
                AppLanguage.HINDI to "प्रतिदिन की पाँच नमाज़ों के निर्धारित समय की अनिवार्यता दर्शाती है।"
            )
        )
    )

    private val repoScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var collectorJob: Job? = null

    private val _state = MutableStateFlow(MissedSalahTrackerState())
    val state: StateFlow<MissedSalahTrackerState> = _state.asStateFlow()

    @Volatile
    private var initialized = false

    fun init(context: Context) {
        val appCtx = context.applicationContext
        if (initialized) return
        initialized = true
        loadConfigPrefs(appCtx)

        val dao = MissedSalahDatabase.getInstance(appCtx).missedSalahDao()
        collectorJob?.cancel()
        collectorJob = repoScope.launch {
            dao.observeAllEntries().collect { list ->
                _state.value = _state.value.copy(
                    entries = list,
                    todayDateKey = todayIsoDateKey()
                )
            }
        }
    }

    internal suspend fun resetForTest(context: Context) {
        initialized = false
        collectorJob?.cancel()
        collectorJob = null
        val appCtx = context.applicationContext
        MissedSalahDatabase.getInstance(appCtx).missedSalahDao().deleteAllEntries()
        appCtx.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit().clear().commit()
        File(appCtx.filesDir, BACKUP_DIR_NAME).deleteRecursively()
        _state.value = MissedSalahTrackerState(todayDateKey = todayIsoDateKey())
    }

    fun todayIsoDateKey(): String =
        SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())

    fun offsetIsoDateKey(baseIsoKey: String, deltaDays: Int): String {
        return try {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            val parsed = sdf.parse(baseIsoKey) ?: return baseIsoKey
            val cal = Calendar.getInstance(Locale.US).apply {
                time = parsed
                add(Calendar.DAY_OF_YEAR, deltaDays)
            }
            sdf.format(cal.time)
        } catch (_: Exception) {
            baseIsoKey
        }
    }

    suspend fun addEntry(
        context: Context,
        prayer: SalahPrayerType,
        missedDateKey: String,
        reasonCategory: MissedReasonCategory,
        optionalNote: String,
        markCompletedNow: Boolean = false,
        completedDateKey: String? = null
    ) {
        init(context)
        val status = when {
            reasonCategory == MissedReasonCategory.JURISPRUDENCE_EXEMPT -> QadaEntryStatus.EXEMPT_NOT_REQUIRED
            markCompletedNow -> QadaEntryStatus.COMPLETED
            else -> QadaEntryStatus.PENDING
        }
        val cleanCompletedDate = if (status == QadaEntryStatus.COMPLETED) {
            completedDateKey?.takeIf { it.isNotBlank() } ?: todayIsoDateKey()
        } else {
            null
        }
        val now = System.currentTimeMillis()
        val entity = MissedSalahEntryEntity(
            prayerType = prayer.name,
            missedDateKey = missedDateKey.ifBlank { todayIsoDateKey() },
            reasonCategory = reasonCategory.name,
            optionalNote = optionalNote.trim(),
            status = status.name,
            completedDateKey = cleanCompletedDate,
            createdAtMs = now,
            updatedAtMs = now
        )
        MissedSalahDatabase.getInstance(context.applicationContext).missedSalahDao().insertEntry(entity)
    }

    suspend fun updateEntry(context: Context, updated: MissedSalahEntryEntity) {
        init(context)
        val coercedStatus = if (updated.reasonEnum() == MissedReasonCategory.JURISPRUDENCE_EXEMPT) {
            QadaEntryStatus.EXEMPT_NOT_REQUIRED
        } else {
            updated.statusEnum()
        }
        val cleanCompletedDate = if (coercedStatus == QadaEntryStatus.COMPLETED) {
            updated.completedDateKey?.takeIf { it.isNotBlank() } ?: todayIsoDateKey()
        } else {
            null
        }
        MissedSalahDatabase.getInstance(context.applicationContext).missedSalahDao().updateEntry(
            updated.copy(
                status = coercedStatus.name,
                completedDateKey = cleanCompletedDate,
                updatedAtMs = System.currentTimeMillis()
            )
        )
    }

    suspend fun toggleCompleted(context: Context, entry: MissedSalahEntryEntity) {
        if (entry.statusEnum() == QadaEntryStatus.EXEMPT_NOT_REQUIRED) return
        val nowDone = entry.statusEnum() != QadaEntryStatus.COMPLETED
        updateEntry(
            context,
            entry.copy(
                status = if (nowDone) QadaEntryStatus.COMPLETED.name else QadaEntryStatus.PENDING.name,
                completedDateKey = if (nowDone) todayIsoDateKey() else null
            )
        )
    }

    suspend fun deleteEntry(context: Context, entryId: Long) {
        init(context)
        MissedSalahDatabase.getInstance(context.applicationContext).missedSalahDao().deleteById(entryId)
    }

    fun setGuidancePerspective(context: Context, perspective: FiqhGuidancePerspective) {
        init(context)
        _state.value = _state.value.copy(guidance = perspective)
        context.applicationContext
            .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_GUIDANCE, perspective.name)
            .apply()
    }

    fun saveBacklogEstimate(context: Context, config: BacklogEstimateConfig) {
        init(context)
        _state.value = _state.value.copy(estimate = config)
        val obj = JSONObject().apply {
            put("enabled", config.enabled)
            put("startDateKey", config.startDateKey)
            put("endDateKey", config.endDateKey)
            put("durationNote", config.durationNote)
            val estObj = JSONObject()
            val compObj = JSONObject()
            SalahPrayerType.entries.forEach { p ->
                estObj.put(p.name, (config.estimatedByPrayer[p] ?: 0).coerceAtLeast(0))
                compObj.put(p.name, (config.completedByPrayer[p] ?: 0).coerceAtLeast(0))
            }
            put("estimated", estObj)
            put("completed", compObj)
        }
        context.applicationContext
            .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_ESTIMATE_JSON, obj.toString())
            .apply()
    }

    fun logCompletedAgainstEstimate(context: Context, prayer: SalahPrayerType, delta: Int) {
        init(context)
        val cur = _state.value.estimate
        val estMax = cur.estimatedByPrayer[prayer] ?: 0
        val currentDone = cur.completedByPrayer[prayer] ?: 0
        val updatedDone = (currentDone + delta).coerceIn(0, estMax)
        val nextCompleted = cur.completedByPrayer.toMutableMap().apply { put(prayer, updatedDone) }
        saveBacklogEstimate(context, cur.copy(completedByPrayer = nextCompleted))
    }

    fun savePlannerConfig(context: Context, planner: QadaPlannerConfig) {
        init(context)
        val safePlanner = planner.copy(dailyTarget = planner.dailyTarget.coerceIn(1, 20))
        _state.value = _state.value.copy(planner = safePlanner)
        val obj = JSONObject().apply {
            put("isActive", safePlanner.isActive)
            put("isPaused", safePlanner.isPaused)
            put("dailyTarget", safePlanner.dailyTarget)
        }
        context.applicationContext
            .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_PLANNER_JSON, obj.toString())
            .apply()
    }

    suspend fun exportLocalBackupJson(context: Context): Result<File> {
        return try {
            init(context)
            val appCtx = context.applicationContext
            val all = MissedSalahDatabase.getInstance(appCtx).missedSalahDao().getAllEntriesOnce()
            val cur = _state.value
            val root = JSONObject().apply {
                put("exportedAtMs", System.currentTimeMillis())
                put("guidance", cur.guidance.name)
                put("dailyTarget", cur.planner.dailyTarget)
                put("plannerActive", cur.planner.isActive)
                put("plannerPaused", cur.planner.isPaused)
                val arr = JSONArray()
                all.forEach { e ->
                    arr.put(
                        JSONObject().apply {
                            put("id", e.id)
                            put("prayerType", e.prayerType)
                            put("missedDateKey", e.missedDateKey)
                            put("reasonCategory", e.reasonCategory)
                            put("optionalNote", e.optionalNote)
                            put("status", e.status)
                            e.completedDateKey?.let { put("completedDateKey", it) }
                            put("createdAtMs", e.createdAtMs)
                            put("updatedAtMs", e.updatedAtMs)
                        }
                    )
                }
                put("entries", arr)
            }
            val dir = File(appCtx.filesDir, BACKUP_DIR_NAME).apply { mkdirs() }
            val file = File(dir, "salah_backup_${System.currentTimeMillis()}.json")
            file.writeText(root.toString(2))
            _state.value = _state.value.copy(lastBackupFilePath = file.absolutePath)
            Result.success(file)
        } catch (t: Throwable) {
            Result.failure(t)
        }
    }

    suspend fun restoreFromBackupFile(context: Context, file: File): Result<Int> {
        return try {
            if (!file.exists()) return Result.failure(IllegalStateException("Backup file not found"))
            val root = JSONObject(file.readText())
            val arr = root.optJSONArray("entries") ?: JSONArray()
            val list = mutableListOf<MissedSalahEntryEntity>()
            for (i in 0 until arr.length()) {
                val o = arr.optJSONObject(i) ?: continue
                list.add(
                    MissedSalahEntryEntity(
                        id = o.optLong("id", 0L),
                        prayerType = o.optString("prayerType", SalahPrayerType.FAJR.name),
                        missedDateKey = o.optString("missedDateKey", todayIsoDateKey()),
                        reasonCategory = o.optString("reasonCategory", MissedReasonCategory.SLEEP_OR_FORGETFULNESS.name),
                        optionalNote = o.optString("optionalNote", ""),
                        status = o.optString("status", QadaEntryStatus.PENDING.name),
                        completedDateKey = o.optString("completedDateKey").takeIf { it.isNotBlank() },
                        createdAtMs = o.optLong("createdAtMs", System.currentTimeMillis()),
                        updatedAtMs = o.optLong("updatedAtMs", System.currentTimeMillis())
                    )
                )
            }
            val dao = MissedSalahDatabase.getInstance(context.applicationContext).missedSalahDao()
            dao.insertAll(list)
            Result.success(list.size)
        } catch (t: Throwable) {
            Result.failure(t)
        }
    }

    suspend fun deleteAllPersonalData(context: Context) {
        val appCtx = context.applicationContext
        MissedSalahDatabase.getInstance(appCtx).missedSalahDao().deleteAllEntries()
        appCtx.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit().clear().apply()
        File(appCtx.filesDir, BACKUP_DIR_NAME).deleteRecursively()
        _state.value = MissedSalahTrackerState(todayDateKey = todayIsoDateKey())
    }

    private fun loadConfigPrefs(appCtx: Context) {
        val prefs = appCtx.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val guidanceStr = prefs.getString(KEY_GUIDANCE, null)
        val guidance = runCatching { guidanceStr?.let { FiqhGuidancePerspective.valueOf(it) } }
            .getOrNull() ?: FiqhGuidancePerspective.COMPARATIVE_OVERVIEW

        val estimate = parseEstimateJson(prefs.getString(KEY_ESTIMATE_JSON, null))
        val planner = parsePlannerJson(prefs.getString(KEY_PLANNER_JSON, null))
        val dir = File(appCtx.filesDir, BACKUP_DIR_NAME)
        val latestBackup = dir.listFiles()?.maxByOrNull { it.lastModified() }?.absolutePath

        _state.value = _state.value.copy(
            guidance = guidance,
            estimate = estimate,
            planner = planner,
            lastBackupFilePath = latestBackup,
            todayDateKey = todayIsoDateKey()
        )
    }

    private fun parseEstimateJson(raw: String?): BacklogEstimateConfig {
        if (raw.isNullOrBlank()) return BacklogEstimateConfig()
        return try {
            val o = JSONObject(raw)
            val estObj = o.optJSONObject("estimated") ?: JSONObject()
            val compObj = o.optJSONObject("completed") ?: JSONObject()
            val estMap = SalahPrayerType.entries.associateWith { p -> estObj.optInt(p.name, 0).coerceAtLeast(0) }
            val compMap = SalahPrayerType.entries.associateWith { p -> compObj.optInt(p.name, 0).coerceAtLeast(0) }
            BacklogEstimateConfig(
                enabled = o.optBoolean("enabled", false),
                startDateKey = o.optString("startDateKey", ""),
                endDateKey = o.optString("endDateKey", ""),
                durationNote = o.optString("durationNote", ""),
                estimatedByPrayer = estMap,
                completedByPrayer = compMap
            )
        } catch (_: Exception) {
            BacklogEstimateConfig()
        }
    }

    private fun parsePlannerJson(raw: String?): QadaPlannerConfig {
        if (raw.isNullOrBlank()) return QadaPlannerConfig()
        return try {
            val o = JSONObject(raw)
            QadaPlannerConfig(
                isActive = o.optBoolean("isActive", false),
                isPaused = o.optBoolean("isPaused", false),
                dailyTarget = o.optInt("dailyTarget", 2).coerceIn(1, 20)
            )
        } catch (_: Exception) {
            QadaPlannerConfig()
        }
    }
}
