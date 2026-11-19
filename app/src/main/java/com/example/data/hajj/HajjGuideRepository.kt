package com.example.data.hajj

import android.content.Context
import com.example.data.model.AppLanguage
import com.example.data.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONArray
import org.json.JSONObject
import java.util.UUID

object HajjGuideRepository {

    private const val KEY_HAJJ_STATE = "hajj_umrah_guide_state_v1"

    private val _state = MutableStateFlow(HajjGuideState())
    val state: StateFlow<HajjGuideState> = _state.asStateFlow()

    @Volatile
    private var isInitialized = false

    fun init(context: Context) {
        val prefs = context.applicationContext.getSharedPreferences(
            UserPreferencesRepository.PREFS_NAME,
            Context.MODE_PRIVATE
        )
        val raw = prefs.getString(KEY_HAJJ_STATE, null)
        _state.value = if (raw.isNullOrBlank()) HajjGuideState() else parseState(raw)
        isInitialized = true
    }

    fun reload(context: Context) {
        init(context)
    }

    private fun ensureLoaded(context: Context) {
        if (!isInitialized) init(context)
    }

    fun allStepsForMode(mode: PilgrimageMode): List<GuideStep> = when (mode) {
        PilgrimageMode.UMRAH -> HajjUmrahStepsData.steps
        PilgrimageMode.HAJJ -> HajjDaysStepsData.steps
    }

    fun setMode(context: Context, mode: PilgrimageMode) {
        ensureLoaded(context)
        updateAndSave(context, _state.value.copy(selectedMode = mode))
    }

    fun setSubType(context: Context, subType: HajjSubType) {
        ensureLoaded(context)
        updateAndSave(context, _state.value.copy(selectedSubType = subType))
    }

    fun setSchool(context: Context, school: FiqhSchool) {
        ensureLoaded(context)
        updateAndSave(context, _state.value.copy(selectedSchool = school))
    }

    fun toggleStepCompleted(context: Context, stepId: String) {
        ensureLoaded(context)
        val current = _state.value.completedStepIds.toMutableSet()
        if (current.contains(stepId)) current.remove(stepId) else current.add(stepId)
        updateAndSave(context, _state.value.copy(completedStepIds = current))
    }

    fun toggleChecklistItem(context: Context, itemId: String) {
        ensureLoaded(context)
        val current = _state.value.checkedChecklistIds.toMutableSet()
        if (current.contains(itemId)) current.remove(itemId) else current.add(itemId)
        updateAndSave(context, _state.value.copy(checkedChecklistIds = current))
    }

    fun addCustomChecklistItem(context: Context, category: ChecklistCategory, title: String) {
        val clean = title.trim()
        if (clean.isEmpty()) return
        ensureLoaded(context)
        val item = GuideChecklistItem(
            id = "custom_chk_${UUID.randomUUID()}",
            category = category,
            title = LocalizedText(clean, clean, clean, clean),
            isCustom = true
        )
        updateAndSave(
            context,
            _state.value.copy(customChecklistItems = _state.value.customChecklistItems + item)
        )
    }

    fun deleteCustomChecklistItem(context: Context, itemId: String) {
        ensureLoaded(context)
        updateAndSave(
            context,
            _state.value.copy(
                customChecklistItems = _state.value.customChecklistItems.filterNot { it.id == itemId },
                checkedChecklistIds = _state.value.checkedChecklistIds - itemId
            )
        )
    }

    fun savePersonalNote(context: Context, title: String, content: String, stepId: String? = null, noteId: String? = null) {
        val cleanTitle = title.trim()
        val cleanContent = content.trim()
        if (cleanTitle.isEmpty() && cleanContent.isEmpty()) return
        ensureLoaded(context)
        val finalTitle = cleanTitle.ifEmpty { "Hajj Note" }
        val now = System.currentTimeMillis()
        val currentNotes = _state.value.notes.toMutableList()
        if (!noteId.isNullOrBlank()) {
            val idx = currentNotes.indexOfFirst { it.id == noteId }
            if (idx >= 0) {
                currentNotes[idx] = currentNotes[idx].copy(
                    title = finalTitle,
                    content = cleanContent,
                    stepId = stepId,
                    updatedAt = now
                )
            } else {
                currentNotes.add(0, HajjPersonalNote(noteId, finalTitle, cleanContent, stepId, now))
            }
        } else {
            currentNotes.add(0, HajjPersonalNote("note_${UUID.randomUUID()}", finalTitle, cleanContent, stepId, now))
        }
        updateAndSave(context, _state.value.copy(notes = currentNotes))
    }

    fun deletePersonalNote(context: Context, noteId: String) {
        ensureLoaded(context)
        updateAndSave(
            context,
            _state.value.copy(notes = _state.value.notes.filterNot { it.id == noteId })
        )
    }

    fun allChecklistItems(): List<GuideChecklistItem> {
        return HajjDuasAndChecklistsData.defaultChecklists + _state.value.customChecklistItems
    }

    fun rulingLabel(level: RulingLevel, language: AppLanguage): String = when (level) {
        RulingLevel.RUKN_PILLAR -> when (language) {
            AppLanguage.ENGLISH -> "Rukn (Pillar)"
            AppLanguage.URDU -> "رکن (فرض)"
            AppLanguage.ARABIC -> "ركن"
            AppLanguage.HINDI -> "रुकन (अनिवार्य स्तंभ)"
        }
        RulingLevel.WAJIB_NECESSARY -> when (language) {
            AppLanguage.ENGLISH -> "Wajib (Necessary)"
            AppLanguage.URDU -> "واجب"
            AppLanguage.ARABIC -> "واجب"
            AppLanguage.HINDI -> "वाजिब (आवश्यक)"
        }
        RulingLevel.SUNNAH_RECOMMENDED -> when (language) {
            AppLanguage.ENGLISH -> "Sunnah / Recommended"
            AppLanguage.URDU -> "سنت / مستحب"
            AppLanguage.ARABIC -> "سنة / مستحب"
            AppLanguage.HINDI -> "सुन्नत / मुस्तहब"
        }
        RulingLevel.CONDITION_SHART -> when (language) {
            AppLanguage.ENGLISH -> "Shart (Condition)"
            AppLanguage.URDU -> "شرط"
            AppLanguage.ARABIC -> "شرط"
            AppLanguage.HINDI -> "शर्त"
        }
        RulingLevel.MUBAH_OPTIONAL -> when (language) {
            AppLanguage.ENGLISH -> "Mubah (Optional)"
            AppLanguage.URDU -> "مباح / اختیاری"
            AppLanguage.ARABIC -> "مباح"
            AppLanguage.HINDI -> "मुबाह (वैकल्पिक)"
        }
    }

    private fun updateAndSave(context: Context, next: HajjGuideState) {
        _state.value = next
        val prefs = context.applicationContext.getSharedPreferences(
            UserPreferencesRepository.PREFS_NAME,
            Context.MODE_PRIVATE
        )
        prefs.edit().putString(KEY_HAJJ_STATE, serializeState(next)).apply()
    }

    private fun serializeState(s: HajjGuideState): String {
        return JSONObject().apply {
            put("mode", s.selectedMode.name)
            put("subType", s.selectedSubType.name)
            put("school", s.selectedSchool.name)
            put("completedSteps", JSONArray().apply { s.completedStepIds.sorted().forEach { put(it) } })
            put("checkedChecklists", JSONArray().apply { s.checkedChecklistIds.sorted().forEach { put(it) } })
            put("customChecklists", JSONArray().apply {
                s.customChecklistItems.forEach { item ->
                    put(JSONObject().apply {
                        put("id", item.id)
                        put("category", item.category.name)
                        put("title", item.title.en)
                    })
                }
            })
            put("notes", JSONArray().apply {
                s.notes.forEach { n ->
                    put(JSONObject().apply {
                        put("id", n.id)
                        put("title", n.title)
                        put("content", n.content)
                        if (!n.stepId.isNullOrBlank()) put("stepId", n.stepId)
                        put("updatedAt", n.updatedAt)
                    })
                }
            })
        }.toString()
    }

    private fun parseState(raw: String): HajjGuideState {
        return try {
            val o = JSONObject(raw)
            val mode = PilgrimageMode.entries.firstOrNull { it.name == o.optString("mode") } ?: PilgrimageMode.UMRAH
            val subType = HajjSubType.entries.firstOrNull { it.name == o.optString("subType") } ?: HajjSubType.TAMATTU
            val school = FiqhSchool.entries.firstOrNull { it.name == o.optString("school") } ?: FiqhSchool.COMPARATIVE

            val completed = LinkedHashSet<String>()
            o.optJSONArray("completedSteps")?.let { arr ->
                for (i in 0 until arr.length()) arr.optString(i).takeIf { it.isNotBlank() }?.let { completed.add(it) }
            }

            val checked = LinkedHashSet<String>()
            o.optJSONArray("checkedChecklists")?.let { arr ->
                for (i in 0 until arr.length()) arr.optString(i).takeIf { it.isNotBlank() }?.let { checked.add(it) }
            }

            val customItems = mutableListOf<GuideChecklistItem>()
            o.optJSONArray("customChecklists")?.let { arr ->
                for (i in 0 until arr.length()) {
                    val obj = arr.optJSONObject(i) ?: continue
                    val id = obj.optString("id")
                    val title = obj.optString("title")
                    val cat = ChecklistCategory.entries.firstOrNull { it.name == obj.optString("category") }
                        ?: ChecklistCategory.PACKING
                    if (id.isNotBlank() && title.isNotBlank()) {
                        customItems.add(
                            GuideChecklistItem(
                                id = id,
                                category = cat,
                                title = LocalizedText(title, title, title, title),
                                isCustom = true
                            )
                        )
                    }
                }
            }

            val notes = mutableListOf<HajjPersonalNote>()
            o.optJSONArray("notes")?.let { arr ->
                for (i in 0 until arr.length()) {
                    val obj = arr.optJSONObject(i) ?: continue
                    val id = obj.optString("id")
                    if (id.isNotBlank()) {
                        notes.add(
                            HajjPersonalNote(
                                id = id,
                                title = obj.optString("title"),
                                content = obj.optString("content"),
                                stepId = obj.optString("stepId").ifBlank { null },
                                updatedAt = obj.optLong("updatedAt", 0L)
                            )
                        )
                    }
                }
            }

            HajjGuideState(
                selectedMode = mode,
                selectedSubType = subType,
                selectedSchool = school,
                completedStepIds = completed,
                checkedChecklistIds = checked,
                customChecklistItems = customItems,
                notes = notes
            )
        } catch (_: Exception) {
            HajjGuideState()
        }
    }
}
