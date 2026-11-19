package com.example.data.asma

import android.content.Context
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import com.example.data.model.AppLanguage
import com.example.data.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONArray
import java.time.LocalDate
import java.util.Locale

object AsmaRepository {

    private const val KEY_FAVORITES = "asma_favorite_numbers_v1"
    private const val KEY_EXPLORED = "asma_explored_numbers_v1"

    private val _state = MutableStateFlow(AsmaState())
    val state: StateFlow<AsmaState> = _state.asStateFlow()

    @Volatile
    private var isInitialized = false

    fun init(context: Context) {
        val prefs = context.applicationContext.getSharedPreferences(
            UserPreferencesRepository.PREFS_NAME,
            Context.MODE_PRIVATE
        )
        val favs = loadIntSet(prefs.getString(KEY_FAVORITES, null))
        val explored = loadIntSet(prefs.getString(KEY_EXPLORED, null))
        _state.value = AsmaState(
            favoriteNumbers = favs,
            exploredNumbers = explored
        )
        isInitialized = true
    }

    fun reload(context: Context) {
        init(context)
    }

    private fun ensureLoaded(context: Context) {
        if (!isInitialized) init(context)
    }

    fun toggleFavorite(context: Context, number: Int): Boolean {
        ensureLoaded(context)
        val current = _state.value.favoriteNumbers.toMutableSet()
        val added = if (current.contains(number)) {
            current.remove(number)
            false
        } else {
            current.add(number)
            true
        }
        _state.value = _state.value.copy(favoriteNumbers = current)
        persistIntSet(context, KEY_FAVORITES, current)
        return added
    }

    fun markExplored(context: Context, number: Int) {
        ensureLoaded(context)
        if (_state.value.exploredNumbers.contains(number)) return
        val updated = _state.value.exploredNumbers + number
        _state.value = _state.value.copy(exploredNumbers = updated)
        persistIntSet(context, KEY_EXPLORED, updated)
    }

    fun dailyName(date: LocalDate = LocalDate.now()): AsmaName {
        val names = AsmaData.allNames
        val index = ((date.dayOfYear - 1).coerceAtLeast(0)) % names.size
        return names[index]
    }

    fun buildShareText(name: AsmaName, language: AppLanguage): String {
        val refParts = listOfNotNull(
            name.quranReference?.let { "Quran: $it" },
            name.hadithReference?.let { "Ref: $it" }
        ).joinToString(" | ")
        return buildString {
            appendLine("${name.number}. ${name.arabic} (${name.transliteration})")
            appendLine(name.localizedMeaning(language))
            appendLine(name.localizedExplanation(language))
            if (refParts.isNotBlank()) {
                appendLine(refParts)
            }
            append(name.sourceUrl)
        }
    }

    private fun persistIntSet(context: Context, key: String, values: Set<Int>) {
        val prefs = context.applicationContext.getSharedPreferences(
            UserPreferencesRepository.PREFS_NAME,
            Context.MODE_PRIVATE
        )
        val arr = JSONArray()
        values.sorted().forEach { arr.put(it) }
        prefs.edit().putString(key, arr.toString()).apply()
    }

    private fun loadIntSet(raw: String?): Set<Int> {
        if (raw.isNullOrBlank()) return emptySet()
        return try {
            val arr = JSONArray(raw)
            val set = LinkedHashSet<Int>(arr.length())
            for (i in 0 until arr.length()) {
                val n = arr.optInt(i, -1)
                if (n in 1..99) set.add(n)
            }
            set
        } catch (_: Exception) {
            emptySet()
        }
    }
}

/**
 * Honest device-TTS pronunciation helper for Asma-ul-Husna.
 * Pre-recorded Qari studio MP3 files are NOT bundled for the 99 Names, so this controller
 * explicitly reports device Arabic TTS availability and never fakes studio Qari recordings.
 */
object AsmaPronunciationController {

    enum class TtsAvailability {
        CHECKING,
        AVAILABLE_DEVICE_TTS,
        UNAVAILABLE
    }

    private var tts: TextToSpeech? = null
    private val _availability = MutableStateFlow(TtsAvailability.CHECKING)
    val availability: StateFlow<TtsAvailability> = _availability.asStateFlow()

    private val _speakingNumber = MutableStateFlow<Int?>(null)
    val speakingNumber: StateFlow<Int?> = _speakingNumber.asStateFlow()

    fun init(context: Context) {
        if (tts != null) return
        try {
            tts = TextToSpeech(context.applicationContext) { status ->
                if (status == TextToSpeech.SUCCESS) {
                    val res = tts?.setLanguage(Locale("ar"))
                    val supported = res != TextToSpeech.LANG_MISSING_DATA && res != TextToSpeech.LANG_NOT_SUPPORTED
                    _availability.value = if (supported) {
                        TtsAvailability.AVAILABLE_DEVICE_TTS
                    } else {
                        TtsAvailability.UNAVAILABLE
                    }
                    tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                        override fun onStart(utteranceId: String?) {}
                        override fun onDone(utteranceId: String?) {
                            _speakingNumber.value = null
                        }
                        @Deprecated("Deprecated in Java")
                        override fun onError(utteranceId: String?) {
                            _speakingNumber.value = null
                        }
                    })
                } else {
                    _availability.value = TtsAvailability.UNAVAILABLE
                }
            }
        } catch (_: Exception) {
            _availability.value = TtsAvailability.UNAVAILABLE
        }
    }

    fun toggleSpeak(context: Context, name: AsmaName) {
        init(context)
        if (_speakingNumber.value == name.number) {
            stop()
            return
        }
        if (_availability.value != TtsAvailability.AVAILABLE_DEVICE_TTS) {
            return
        }
        try {
            _speakingNumber.value = name.number
            val params = Bundle()
            params.putFloat(TextToSpeech.Engine.KEY_PARAM_VOLUME, 1.0f)
            tts?.speak(name.arabic, TextToSpeech.QUEUE_FLUSH, params, "asma_${name.number}")
        } catch (_: Exception) {
            _speakingNumber.value = null
        }
    }

    fun stop() {
        try {
            tts?.stop()
        } catch (_: Exception) {}
        _speakingNumber.value = null
    }
}
