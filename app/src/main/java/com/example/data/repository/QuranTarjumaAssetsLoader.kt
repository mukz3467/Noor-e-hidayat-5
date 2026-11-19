package com.example.data.repository

import android.content.Context
import android.util.JsonReader
import android.util.JsonToken
import android.util.LruCache
import com.example.data.model.AppLanguage
import org.json.JSONObject
import java.io.BufferedInputStream
import java.io.InputStreamReader
import java.util.concurrent.ConcurrentHashMap
import java.util.zip.GZIPInputStream

data class BundledQuranEncVerse(
    val translation: String,
    val footnotes: String
)

data class BundledAsbabEntry(
    val wahidiEnglish: String?,
    val sahihArabic: String?
)

data class BundledSurahChronology(
    val revelationPlace: String,
    val revelationOrder: Int
)

/**
 * Loads pre-bundled, compressed (.json.gz) authentic Islamic Tarjuma & Tafseer datasets from assets/tarjuma/:
 * - QuranEnc verified editions: urdu_junagarhi, english_rwwad, hindi_omari,
 *   arabic_mokhtasar, english_mokhtasar, hindi_mokhtasar (all 114 surahs / 6,236 ayahs)
 * - Uses streaming JsonReader + bounded per-Surah LruCache so the entire corpus is never pinned in RAM.
 */
object QuranTarjumaAssetsLoader {

    private const val SURAH_CACHE_CAPACITY = 16
    private val surahVerseCache = LruCache<String, Map<Int, BundledQuranEncVerse>>(SURAH_CACHE_CAPACITY)

    @Volatile
    private var asbabRootCache: JSONObject? = null
    @Volatile
    private var surahHeadersArCache: JSONObject? = null
    @Volatile
    private var surahRevOrderCache: JSONObject? = null

    private fun openAssetJsonStream(
        context: Context,
        assetRelativePath: String
    ): BufferedInputStream? {
        val candidates = listOf(assetRelativePath.removeSuffix(".gz"), assetRelativePath).distinct()
        for (candidate in candidates) {
            try {
                val rawStream = context.assets.open(candidate)
                val buffered = BufferedInputStream(rawStream, 8192)
                buffered.mark(2)
                val b0 = buffered.read()
                val b1 = buffered.read()
                buffered.reset()
                return if (b0 == 0x1f && b1 == 0x8b) {
                    BufferedInputStream(GZIPInputStream(buffered, 8192), 8192)
                } else {
                    buffered
                }
            } catch (_: Throwable) {
                // try next candidate
            }
        }
        return null
    }

    private fun readGzipJson(context: Context?, assetRelativePath: String): JSONObject? {
        if (context == null) return null
        return openAssetJsonStream(context, assetRelativePath)?.use { stream ->
            runCatching {
                stream.bufferedReader(Charsets.UTF_8).use { reader ->
                    JSONObject(reader.readText())
                }
            }.getOrNull()
        }
    }

    private fun readSurahFromGzipJson(
        context: Context,
        editionKey: String,
        surahNumber: Int
    ): Map<Int, BundledQuranEncVerse>? {
        val cacheKey = "$editionKey:$surahNumber"
        synchronized(surahVerseCache) {
            surahVerseCache.get(cacheKey)?.let { return it }
        }
        val stream = openAssetJsonStream(context, "tarjuma/quranenc_${editionKey}.json.gz") ?: return null
        val targetKey = surahNumber.toString()
        var parsedSurah: Map<Int, BundledQuranEncVerse>? = null
        try {
            JsonReader(InputStreamReader(stream, Charsets.UTF_8)).use { reader ->
                reader.beginObject()
                while (reader.hasNext()) {
                    val key = reader.nextName()
                    if (key == targetKey) {
                        val verses = HashMap<Int, BundledQuranEncVerse>()
                        reader.beginObject()
                        while (reader.hasNext()) {
                            val ayahNum = reader.nextName().toIntOrNull()
                            val verse = readSingleVerseValue(reader)
                            if (ayahNum != null && verse != null) {
                                verses[ayahNum] = verse
                            }
                        }
                        reader.endObject()
                        parsedSurah = verses
                        break
                    } else {
                        reader.skipValue()
                    }
                }
            }
        } catch (_: Throwable) {
            return null
        }
        if (parsedSurah != null) {
            synchronized(surahVerseCache) {
                surahVerseCache.put(cacheKey, parsedSurah)
            }
        }
        return parsedSurah
    }

    private fun readSingleVerseValue(reader: JsonReader): BundledQuranEncVerse? {
        return when (reader.peek()) {
            JsonToken.BEGIN_ARRAY -> {
                reader.beginArray()
                val trans = if (reader.hasNext() && reader.peek() == JsonToken.STRING) {
                    reader.nextString().trim()
                } else {
                    if (reader.hasNext()) reader.skipValue()
                    ""
                }
                val fn = if (reader.hasNext() && reader.peek() == JsonToken.STRING) {
                    reader.nextString().trim()
                } else {
                    if (reader.hasNext()) reader.skipValue()
                    ""
                }
                while (reader.hasNext()) {
                    reader.skipValue()
                }
                reader.endArray()
                if (trans.isBlank()) null else BundledQuranEncVerse(trans, fn)
            }
            JsonToken.STRING -> {
                val trans = reader.nextString().trim()
                if (trans.isBlank()) null else BundledQuranEncVerse(trans, "")
            }
            else -> {
                reader.skipValue()
                null
            }
        }
    }

    fun getBundledQuranEncVerse(
        context: Context?,
        editionKey: String,
        surahNumber: Int,
        ayahNumber: Int
    ): BundledQuranEncVerse? {
        if (context == null || surahNumber !in 1..114 || ayahNumber <= 0) return null
        val surahMap = readSurahFromGzipJson(context, editionKey, surahNumber) ?: return null
        return surahMap[ayahNumber]
    }

    fun hasBundledQuranEncEdition(editionKey: String): Boolean = editionKey in BUNDLED_QURANENC_KEYS

    fun getAsbabAlNuzul(
        context: Context?,
        surahNumber: Int,
        ayahNumber: Int
    ): BundledAsbabEntry? {
        val root = asbabRootCache ?: readGzipJson(context, "tarjuma/asbab_al_nuzul.json.gz")?.also {
            asbabRootCache = it
        } ?: return null

        val sKey = surahNumber.toString()
        val aKey = ayahNumber.toString()

        val wahidiText = root.optJSONObject("wahidi_en")
            ?.optJSONObject(sKey)
            ?.optString(aKey, "")
            ?.takeIf { it.isNotBlank() }

        val sahihArText = root.optJSONObject("sahih_ar")
            ?.optJSONObject(sKey)
            ?.optString(aKey, "")
            ?.takeIf { it.isNotBlank() }

        if (wahidiText == null && sahihArText == null) return null
        return BundledAsbabEntry(wahidiEnglish = wahidiText, sahihArabic = sahihArText)
    }

    fun getSurahHeaderArabic(context: Context?, surahNumber: Int): String? {
        val root = surahHeadersArCache ?: readGzipJson(context, "tarjuma/surah_headers_ar.json.gz")?.also {
            surahHeadersArCache = it
        } ?: return null
        return root.optString(surahNumber.toString(), "").takeIf { it.isNotBlank() }
    }

    fun getSurahRevelationChronology(context: Context?, surahNumber: Int): BundledSurahChronology? {
        val root = surahRevOrderCache ?: readGzipJson(context, "tarjuma/surah_revelation_order.json.gz")?.also {
            surahRevOrderCache = it
        } ?: return null
        val obj = root.optJSONObject(surahNumber.toString()) ?: return null
        val place = obj.optString("place", "")
        val order = obj.optInt("order", 0)
        if (order <= 0) return null
        return BundledSurahChronology(revelationPlace = place, revelationOrder = order)
    }

    val BUNDLED_QURANENC_KEYS = setOf(
        "urdu_junagarhi",
        "english_rwwad",
        "hindi_omari",
        "arabic_mokhtasar",
        "english_mokhtasar",
        "hindi_mokhtasar"
    )
}
