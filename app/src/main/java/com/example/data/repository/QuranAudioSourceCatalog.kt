package com.example.data.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.util.concurrent.ConcurrentHashMap

data class VerifiedAudioMetadata(
    val url: String,
    val contentLengthBytes: Long,
    val contentType: String?,
    val supportsByteRanges: Boolean,
    val verifiedAtMillis: Long = System.currentTimeMillis()
)

object QuranAudioSourceCatalog {

    const val MIN_AYAH_BYTES = 1_536L
    const val MIN_AZAN_BYTES = 10_240L
    const val ESTIMATED_AVERAGE_SURAH_BYTES = 4_800_000L // Fallback only when server omits Content-Length

    /**
     * Test hook to deterministically simulate Offline / Airplane Mode or Online state in JVM/Robolectric tests.
     */
    @Volatile
    var isOnlineOverrideForTest: Boolean? = null

    private val verifiedUrlCache = ConcurrentHashMap<String, VerifiedAudioMetadata>()

    fun clearVerifiedUrlCache() {
        verifiedUrlCache.clear()
    }

    fun getCachedVerifiedMetadata(url: String): VerifiedAudioMetadata? = verifiedUrlCache[url]

    /**
     * Proportional minimum MP3 byte threshold per Surah so a single-ayah stub (~15-30 KB)
     * can NEVER pass as a long Surah like Al-Baqarah (286 ayahs).
     */
    fun minExpectedSurahBytes(surahNumber: Int): Long {
        val ayahCount = QuranData.surahs.firstOrNull { it.number == surahNumber }?.ayahCount ?: 1
        return (ayahCount * 2_500L).coerceAtLeast(4_096L)
    }

    fun normalizeReciterId(reciterId: String?): String = when (reciterId?.lowercase()) {
        "abdulbasit", "abdul_basit" -> "abdulbasit"
        "husary", "alhusary" -> "husary"
        "ghamdi", "alghamdi" -> "ghamdi"
        else -> "mishary"
    }

    /**
     * Full-length Surah MP3 candidate URLs for Surahs 1..114.
     * Zero cross-reciter fallback and zero single-ayah (e.g. 001.mp3) substitution.
     */
    fun getFullSurahCandidateUrls(surahNumber: Int, reciterId: String): List<String> {
        if (surahNumber !in 1..114) return emptyList()
        val sPad = surahNumber.toString().padStart(3, '0')
        return when (normalizeReciterId(reciterId)) {
            "abdulbasit" -> listOf(
                "https://cdn.islamic.network/quran/audio-surah/128/ar.abdulbasitmurattal/$surahNumber.mp3",
                "https://download.quranicaudio.com/quran/abdul_basit_murattal/$sPad.mp3",
                "https://server7.mp3quran.net/basit/$sPad.mp3"
            )
            "husary" -> listOf(
                "https://download.quranicaudio.com/quran/mahmood_khaleel_al-husaree_iza3a/$sPad.mp3",
                "https://cdn.islamic.network/quran/audio-surah/128/ar.husary/$surahNumber.mp3",
                "https://server13.mp3quran.net/husr/$sPad.mp3"
            )
            "ghamdi" -> listOf(
                "https://download.quranicaudio.com/quran/sa3d_al-ghaamidi/complete/$sPad.mp3",
                "https://server7.mp3quran.net/s_gmd/$sPad.mp3"
            )
            else -> listOf(
                "https://download.quranicaudio.com/quran/mishaari_raashid_al_3afaasee/$sPad.mp3",
                "https://cdn.islamic.network/quran/audio-surah/128/ar.alafasy/$surahNumber.mp3",
                "https://server8.mp3quran.net/afs/$sPad.mp3"
            )
        }
    }

    /**
     * Computes 1-based global Ayah number (1..6236) for global CDN mirrors.
     */
    fun getGlobalAyahNumber(surahNumber: Int, ayahNumber: Int): Int? {
        if (surahNumber !in 1..114 || ayahNumber < 1) return null
        val targetSurah = QuranData.surahs.firstOrNull { it.number == surahNumber } ?: return null
        if (ayahNumber > targetSurah.ayahCount) return null
        var offset = 0
        for (s in QuranData.surahs) {
            if (s.number == surahNumber) {
                return offset + ayahNumber
            }
            offset += s.ayahCount
        }
        return null
    }

    /**
     * Same-reciter-only Ayah MP3 candidate URLs. Never silently switches reciter.
     */
    fun getAyahCandidateUrls(surahNumber: Int, ayahNumber: Int, reciterId: String): List<String> {
        if (surahNumber !in 1..114 || ayahNumber < 1) return emptyList()
        val sStr = surahNumber.toString().padStart(3, '0')
        val aStr = ayahNumber.toString().padStart(3, '0')
        val globalAyah = getGlobalAyahNumber(surahNumber, ayahNumber)
        val normalized = normalizeReciterId(reciterId)

        return when (normalized) {
            "abdulbasit" -> buildList {
                add("https://everyayah.com/data/Abdul_Basit_Murattal_192kbps/$sStr$aStr.mp3")
                if (globalAyah != null) {
                    add("https://cdn.islamic.network/quran/audio/128/ar.abdulbasitmurattal/$globalAyah.mp3")
                }
            }
            "husary" -> buildList {
                add("https://everyayah.com/data/Husary_128kbps/$sStr$aStr.mp3")
                if (globalAyah != null) {
                    add("https://cdn.islamic.network/quran/audio/128/ar.husary/$globalAyah.mp3")
                }
            }
            "ghamdi" -> listOf(
                "https://everyayah.com/data/Ghamadi_40kbps/$sStr$aStr.mp3"
            )
            else -> buildList {
                add("https://everyayah.com/data/Alafasy_128kbps/$sStr$aStr.mp3")
                if (globalAyah != null) {
                    add("https://cdn.islamic.network/quran/audio/128/ar.alafasy/$globalAyah.mp3")
                }
            }
        }
    }

    fun getAzanCandidateUrls(azanType: String): List<String> {
        return when (azanType.lowercase()) {
            "madina", "madinah" -> listOf(
                "https://www.islamcan.com/audio/adhan/azan2.mp3",
                "https://ia800300.us.archive.org/31/items/AdhanMadinah/AdhanMadinah.mp3"
            )
            "fajr" -> listOf(
                "https://www.islamcan.com/audio/adhan/azan3.mp3",
                "https://ia800208.us.archive.org/16/items/AdhanFajr/AdhanFajr.mp3"
            )
            else -> listOf(
                "https://www.islamcan.com/audio/adhan/azan1.mp3",
                "https://ia800201.us.archive.org/29/items/AdhanMakkah/AdhanMakkah.mp3"
            )
        }
    }

    fun isValidAudioContentType(contentType: String?): Boolean {
        if (contentType.isNullOrBlank()) return true // Some CDNs omit Content-Type on binary MP3 responses; header bytes still checked
        val normalized = contentType.substringBefore(';').trim().lowercase()
        if (normalized.startsWith("text/") ||
            normalized.contains("html") ||
            normalized.contains("json") ||
            normalized.contains("xml") ||
            normalized.contains("javascript")
        ) {
            return false
        }
        return normalized.startsWith("audio/") ||
            normalized == "application/octet-stream" ||
            normalized == "binary/octet-stream"
    }

    fun hasValidMp3Header(buffer: ByteArray, length: Int): Boolean {
        if (length < 4) return false
        val limit = length.coerceAtMost(buffer.size)
        val prefixAscii = String(buffer, 0, limit.coerceAtMost(128), Charsets.US_ASCII).trimStart().lowercase()
        if (prefixAscii.startsWith("<!doctype") ||
            prefixAscii.startsWith("<html") ||
            prefixAscii.startsWith("<?xml") ||
            prefixAscii.startsWith("{") ||
            prefixAscii.startsWith("[") ||
            prefixAscii.startsWith("404") ||
            prefixAscii.startsWith("error")
        ) {
            return false
        }

        // ID3v2 header
        if (buffer[0] == 'I'.code.toByte() && buffer[1] == 'D'.code.toByte() && buffer[2] == '3'.code.toByte()) {
            return true
        }

        // MPEG audio frame sync (11 set bits: 0xFF followed by 0xE0 mask, valid version & layer)
        for (i in 0 until limit - 1) {
            val b0 = buffer[i].toInt() and 0xFF
            val b1 = buffer[i + 1].toInt() and 0xFF
            if (b0 == 0xFF && (b1 and 0xE0) == 0xE0) {
                val versionBits = (b1 and 0x18) shr 3
                val layerBits = (b1 and 0x06) shr 1
                if (versionBits != 0x01 && layerBits != 0x00) {
                    return true
                }
            }
        }
        return false
    }

    fun isValidMp3File(file: File, minBytes: Long): Boolean {
        if (!file.exists() || !file.isFile) return false
        if (file.name.endsWith(".tmp", ignoreCase = true)) return false
        if (file.length() < minBytes) return false
        return try {
            val header = ByteArray(4096)
            val read = file.inputStream().use { it.read(header) }
            hasValidMp3Header(header, read)
        } catch (_: Exception) {
            false
        }
    }

    fun isBundledAssetPresent(context: Context, assetFileName: String): Boolean {
        return try {
            context.assets.openFd("audio/$assetFileName").use { afd ->
                afd.length > 0 || afd.declaredLength > 0
            }
        } catch (_: Exception) {
            try {
                context.assets.list("audio")?.contains(assetFileName) == true
            } catch (_: Exception) {
                false
            }
        }
    }

    fun isNetworkAvailable(context: Context): Boolean {
        isOnlineOverrideForTest?.let { return it }
        return try {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
                ?: return false
            val network = cm.activeNetwork ?: return false
            val caps = cm.getNetworkCapabilities(network) ?: return false
            caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        } catch (_: Exception) {
            false
        }
    }

    /**
     * Verifies a candidate audio URL via HTTP HEAD (with byte-range GET fallback) before registering it as valid.
     */
    fun verifyUrlOnline(
        client: OkHttpClient,
        url: String,
        minBytes: Long
    ): VerifiedAudioMetadata? {
        verifiedUrlCache[url]?.let { cached ->
            if (cached.contentLengthBytes <= 0L || cached.contentLengthBytes >= minBytes) {
                return cached
            }
        }

        try {
            val headReq = Request.Builder()
                .url(url)
                .head()
                .header("User-Agent", "Noor-e-Hidayat/3.0 (Android)")
                .build()
            client.newCall(headReq).execute().use { response ->
                val contentType = response.header("Content-Type")
                val contentLength = response.header("Content-Length")?.toLongOrNull() ?: -1L
                val acceptRanges = response.header("Accept-Ranges")?.equals("bytes", ignoreCase = true) == true
                if (response.isSuccessful && isValidAudioContentType(contentType)) {
                    if (contentLength >= minBytes) {
                        val meta = VerifiedAudioMetadata(url, contentLength, contentType, acceptRanges)
                        verifiedUrlCache[url] = meta
                        return meta
                    }
                    if (contentLength in 0 until minBytes) {
                        return null
                    }
                }
            }
        } catch (_: Exception) {
            // Fall through to small byte-range GET verification
        }

        return try {
            val rangeReq = Request.Builder()
                .url(url)
                .get()
                .header("Range", "bytes=0-2047")
                .header("User-Agent", "Noor-e-Hidayat/3.0 (Android)")
                .build()
            client.newCall(rangeReq).execute().use { response ->
                if (!response.isSuccessful) return null
                val contentType = response.header("Content-Type")
                if (!isValidAudioContentType(contentType)) return null

                val totalFromRange = response.header("Content-Range")
                    ?.substringAfter('/')
                    ?.toLongOrNull()
                val headerLength = response.header("Content-Length")?.toLongOrNull() ?: -1L
                val totalLength = totalFromRange ?: if (response.code == 200) headerLength else -1L
                if (totalLength in 0 until minBytes) return null

                val body = response.body ?: return null
                val prefix = ByteArray(2048)
                val read = body.byteStream().use { input ->
                    var totalRead = 0
                    while (totalRead < prefix.size) {
                        val n = input.read(prefix, totalRead, prefix.size - totalRead)
                        if (n <= 0) break
                        totalRead += n
                    }
                    totalRead
                }
                if (!hasValidMp3Header(prefix, read)) return null

                val resolvedLength = if (totalLength > 0L) totalLength else read.toLong()
                val supportsRanges = response.code == 206 ||
                    response.header("Accept-Ranges")?.equals("bytes", ignoreCase = true) == true
                val meta = VerifiedAudioMetadata(url, resolvedLength, contentType, supportsRanges)
                verifiedUrlCache[url] = meta
                meta
            }
        } catch (_: Exception) {
            null
        }
    }
}
