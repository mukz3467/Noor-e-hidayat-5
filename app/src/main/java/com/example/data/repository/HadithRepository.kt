package com.example.data.repository

import android.content.Context
import com.example.data.model.Hadith
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import okhttp3.Dns
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.net.Inet4Address
import java.util.concurrent.TimeUnit

data class HadithCollectionProgress(
    val collection: String,
    val totalBooks: Int,
    val downloadedFullBooksCount: Int,
    val curatedSampleBooksCount: Int,
    val totalNarrationsAvailableOffline: Int,
    val canonicalTotalNarrations: Int,
    val totalDownloadedBytes: Long
) {
    val booksCompletionPercent: Int
        get() = if (totalBooks > 0) ((downloadedFullBooksCount * 100f) / totalBooks).toInt().coerceIn(0, 100) else 0

    val narrationsCompletionPercent: Int
        get() = if (canonicalTotalNarrations > 0) ((totalNarrationsAvailableOffline * 100f) / canonicalTotalNarrations).toInt().coerceIn(0, 100) else 0

    val isFullCollectionOffline: Boolean
        get() = downloadedFullBooksCount >= totalBooks && totalNarrationsAvailableOffline > (canonicalTotalNarrations / 2)
}

class HadithRepository private constructor(context: Context) {

    private val appContext = context.applicationContext
    private val storageDir = File(appContext.filesDir, "hadith_offline").apply { mkdirs() }

    private val httpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(45, TimeUnit.SECONDS)
            .dns(object : Dns {
                override fun lookup(hostname: String): List<java.net.InetAddress> {
                    return try {
                        Dns.SYSTEM.lookup(hostname).sortedBy { if (it is Inet4Address) 0 else 1 }
                    } catch (_: Exception) {
                        Dns.SYSTEM.lookup(hostname)
                    }
                }
            })
            .build()
    }

    private val _downloadedBookKeys = MutableStateFlow<Set<String>>(emptySet())
    val downloadedBookKeys: StateFlow<Set<String>> = _downloadedBookKeys.asStateFlow()

    private val _bookByteSizes = MutableStateFlow<Map<String, Long>>(emptyMap())
    val bookByteSizes: StateFlow<Map<String, Long>> = _bookByteSizes.asStateFlow()

    private val _downloadedBookCounts = MutableStateFlow<Map<String, Int>>(emptyMap())
    val downloadedBookCounts: StateFlow<Map<String, Int>> = _downloadedBookCounts.asStateFlow()

    private val _activeDownloadKey = MutableStateFlow<String?>(null)
    val activeDownloadKey: StateFlow<String?> = _activeDownloadKey.asStateFlow()

    private val _downloadStatusMessage = MutableStateFlow<String?>(null)
    val downloadStatusMessage: StateFlow<String?> = _downloadStatusMessage.asStateFlow()

    private val _batchProgress = MutableStateFlow(0f)
    val batchProgress: StateFlow<Float> = _batchProgress.asStateFlow()

    private val _lastError = MutableStateFlow<String?>(null)
    val lastError: StateFlow<String?> = _lastError.asStateFlow()

    // In-memory cache of parsed downloaded books
    private val bookCache = mutableMapOf<String, List<Hadith>>()

    init {
        refreshDownloadIndex()
    }

    fun keyFor(collection: String, bookNumber: Int): String {
        val prefix = if (collection == HadithCatalog.COLLECTION_MUSLIM) "muslim" else "bukhari"
        return "${prefix}_$bookNumber"
    }

    private fun fileFor(collection: String, bookNumber: Int): File {
        val sub = if (collection == HadithCatalog.COLLECTION_MUSLIM) "muslim" else "bukhari"
        val dir = File(storageDir, sub).apply { mkdirs() }
        return File(dir, "book_${bookNumber}.json")
    }

    fun refreshDownloadIndex() {
        val keys = mutableSetOf<String>()
        val sizes = mutableMapOf<String, Long>()
        val counts = mutableMapOf<String, Int>()

        listOf(
            HadithCatalog.COLLECTION_BUKHARI to "bukhari",
            HadithCatalog.COLLECTION_MUSLIM to "muslim"
        ).forEach { (col, folder) ->
            val dir = File(storageDir, folder)
            if (dir.exists() && dir.isDirectory) {
                dir.listFiles()?.forEach { file ->
                    if (file.name.startsWith("book_") && file.name.endsWith(".json") && file.length() > 32L) {
                        val num = file.name.removePrefix("book_").removeSuffix(".json").toIntOrNull()
                        if (num != null) {
                            val key = keyFor(col, num)
                            keys.add(key)
                            sizes[key] = file.length()
                            counts[key] = readCountQuick(file)
                        }
                    }
                }
            }
        }
        _downloadedBookKeys.value = keys
        _bookByteSizes.value = sizes
        _downloadedBookCounts.value = counts
    }

    private fun readCountQuick(file: File): Int {
        return try {
            val obj = JSONObject(file.readText())
            obj.optInt("count", obj.optJSONArray("hadiths")?.length() ?: 0)
        } catch (_: Exception) {
            0
        }
    }

    fun isBookDownloaded(collection: String, bookNumber: Int): Boolean {
        return _downloadedBookKeys.value.contains(keyFor(collection, bookNumber))
    }

    fun getCollectionProgress(collection: String): HadithCollectionProgress {
        val books = HadithCatalog.booksFor(collection)
        val manifest = HadithCatalog.getManifest(collection)
        val keys = _downloadedBookKeys.value
        val sizes = _bookByteSizes.value
        val counts = _downloadedBookCounts.value

        val prefix = if (collection == HadithCatalog.COLLECTION_MUSLIM) "muslim_" else "bukhari_"
        val downloadedBooks = keys.count { it.startsWith(prefix) }
        val bytes = sizes.filterKeys { it.startsWith(prefix) }.values.sum()

        val curatedForCol = HadithData.hadiths.filter { it.collection == collection }
        val curatedBooksSet = curatedForCol.map { it.bookNumber }.toSet()

        // Sum narrations from downloaded books + curated items from books NOT yet downloaded
        var totalNarrations = 0
        books.forEach { info ->
            val k = keyFor(collection, info.bookNumber)
            val dlCount = counts[k] ?: 0
            if (dlCount > 0) {
                totalNarrations += dlCount
            } else {
                totalNarrations += curatedForCol.count { it.bookNumber == info.bookNumber }
            }
        }

        return HadithCollectionProgress(
            collection = collection,
            totalBooks = books.size,
            downloadedFullBooksCount = downloadedBooks,
            curatedSampleBooksCount = curatedBooksSet.size,
            totalNarrationsAvailableOffline = totalNarrations,
            canonicalTotalNarrations = manifest.canonicalTotalNarrations,
            totalDownloadedBytes = bytes
        )
    }

    fun getTotalDownloadedBytes(): Long = _bookByteSizes.value.values.sum()

    fun getTotalDownloadedBooksCount(): Int = _downloadedBookKeys.value.size

    fun getHadithsForBook(collection: String, bookNumber: Int): List<Hadith> {
        val key = keyFor(collection, bookNumber)
        val curatedInBook = HadithData.hadiths.filter {
            it.collection == collection && it.bookNumber == bookNumber
        }

        val downloaded = loadDownloadedBookFromDisk(collection, bookNumber, key)
        if (downloaded.isNullOrEmpty()) {
            return curatedInBook
        }

        // Merge: if curated item matches hadithNumber in downloaded, enrich Hindi & chapter translations
        val curatedByNum = curatedInBook.associateBy { it.hadithNumber }
        return downloaded.map { dl ->
            val curated = curatedByNum[dl.hadithNumber]
            if (curated != null) {
                dl.copy(
                    textHindi = curated.textHindi,
                    chapterHindi = curated.chapterHindi,
                    chapterUrdu = curated.chapterUrdu,
                    isHindiAvailable = true
                )
            } else {
                dl
            }
        }
    }

    private fun loadDownloadedBookFromDisk(collection: String, bookNumber: Int, key: String): List<Hadith>? {
        bookCache[key]?.let { return it }
        val file = fileFor(collection, bookNumber)
        if (!file.exists()) return null
        return try {
            val root = JSONObject(file.readText())
            val arr = root.optJSONArray("hadiths") ?: return null
            val list = ArrayList<Hadith>(arr.length())
            for (i in 0 until arr.length()) {
                val o = arr.getJSONObject(i)
                list.add(
                    Hadith(
                        id = o.optString("id"),
                        collection = o.optString("collection", collection),
                        bookNumber = o.optInt("bookNumber", bookNumber),
                        bookName = o.optString("bookName"),
                        hadithNumber = o.optInt("hadithNumber"),
                        chapterArabic = o.optString("chapterArabic"),
                        chapterEnglish = o.optString("chapterEnglish"),
                        chapterUrdu = o.optString("chapterUrdu"),
                        chapterHindi = o.optString("chapterHindi"),
                        textArabic = o.optString("textArabic"),
                        textEnglish = o.optString("textEnglish"),
                        textUrdu = o.optString("textUrdu"),
                        textHindi = o.optString("textHindi"),
                        narrator = o.optString("narrator"),
                        grade = o.optString("grade", "Sahih"),
                        sourceAttribution = o.optString("sourceAttribution"),
                        volumeNumber = o.optInt("volumeNumber", 1),
                        chapterNumber = o.optInt("chapterNumber", 1),
                        sourceUrl = o.optString("sourceUrl"),
                        editionName = o.optString("editionName"),
                        translatorName = o.optString("translatorName"),
                        licenseName = o.optString("licenseName"),
                        isHindiAvailable = o.optBoolean("isHindiAvailable", false),
                        isBundled = false
                    )
                )
            }
            bookCache[key] = list
            list
        } catch (_: Exception) {
            null
        }
    }

    fun searchAvailableHadiths(collectionFilter: String?, query: String): List<Hadith> {
        val q = query.trim()
        if (q.isBlank()) return emptyList()

        val collections = if (collectionFilter.isNullOrBlank() || collectionFilter == "All") {
            listOf(HadithCatalog.COLLECTION_BUKHARI, HadithCatalog.COLLECTION_MUSLIM)
        } else {
            listOf(collectionFilter)
        }

        val combined = mutableListOf<Hadith>()
        val seenIds = mutableSetOf<String>()

        // Load downloaded books first for full narrations, then fill with curated
        collections.forEach { col ->
            val books = HadithCatalog.booksFor(col)
            books.forEach { info ->
                val key = keyFor(col, info.bookNumber)
                if (_downloadedBookKeys.value.contains(key)) {
                    loadDownloadedBookFromDisk(col, info.bookNumber, key)?.forEach { h ->
                        if (matchesHadithQuery(h, q) && seenIds.add(h.id)) {
                            combined.add(h)
                        }
                    }
                }
            }
        }

        HadithData.hadiths.forEach { h ->
            if ((collectionFilter == null || collectionFilter == "All" || h.collection == collectionFilter) &&
                matchesHadithQuery(h, q) && seenIds.add(h.id)
            ) {
                combined.add(h)
            }
        }
        return combined.take(200)
    }

    private fun matchesHadithQuery(h: Hadith, q: String): Boolean {
        return h.textEnglish.contains(q, ignoreCase = true) ||
            h.textUrdu.contains(q) ||
            h.textArabic.contains(q) ||
            (h.isHindiAvailable && h.textHindi.contains(q, ignoreCase = true)) ||
            h.narrator.contains(q, ignoreCase = true) ||
            h.bookName.contains(q, ignoreCase = true) ||
            h.chapterEnglish.contains(q, ignoreCase = true) ||
            h.chapterUrdu.contains(q) ||
            h.chapterArabic.contains(q) ||
            h.chapterHindi.contains(q, ignoreCase = true) ||
            h.hadithNumber.toString() == q ||
            h.hadithNumber.toString().contains(q)
    }

    suspend fun downloadBook(collection: String, bookInfo: HadithBookInfo): Result<Int> =
        withContext(Dispatchers.IO) {
            val key = keyFor(collection, bookInfo.bookNumber)
            _activeDownloadKey.value = key
            _lastError.value = null
            _downloadStatusMessage.value = "${bookInfo.titleEn} (#${bookInfo.bookNumber})"

            try {
                val manifest = HadithCatalog.getManifest(collection)
                val sec = bookInfo.apiSectionId

                val (araJson, engJson, urdJson) = coroutineScope {
                    val a = async { fetchSectionJson(manifest.arabicEditionId, sec) }
                    val e = async { fetchSectionJson(manifest.englishEditionId, sec) }
                    val u = async { fetchSectionJson(manifest.urduEditionId, sec) }
                    Triple(a.await(), e.await(), u.await())
                }

                val araArr = araJson?.optJSONArray("hadiths")
                    ?: return@withContext Result.failure(IllegalStateException("Arabic dataset unavailable for Book ${bookInfo.bookNumber}"))
                if (araArr.length() == 0) {
                    return@withContext Result.failure(IllegalStateException("No narrations returned for Book ${bookInfo.bookNumber}"))
                }

                val engByNum = mapHadithArrayByNumber(engJson?.optJSONArray("hadiths"))
                val urdByNum = mapHadithArrayByNumber(urdJson?.optJSONArray("hadiths"))
                val engArr = engJson?.optJSONArray("hadiths")
                val urdArr = urdJson?.optJSONArray("hadiths")

                val outArray = JSONArray()
                val baseBookTitle = "Book ${bookInfo.bookNumber}: ${bookInfo.titleEn} (${bookInfo.titleAr})"
                val slug = if (collection == HadithCatalog.COLLECTION_MUSLIM) "muslim" else "bukhari"

                for (i in 0 until araArr.length()) {
                    val araObj = araArr.getJSONObject(i)
                    val arabicNumRaw = araObj.optDouble("arabicnumber", araObj.optDouble("hadithnumber", (i + 1).toDouble()))
                    val hadithNum = arabicNumRaw.toInt().takeIf { it > 0 } ?: (i + 1)
                    val apiNumKey = formatNumberKey(araObj.optDouble("hadithnumber", hadithNum.toDouble()))

                    val textAr = araObj.optString("text", "").trim()
                    if (textAr.isBlank()) continue

                    val engObj = engByNum[apiNumKey]
                        ?: engByNum[hadithNum.toString()]
                        ?: if (engArr != null && i < engArr.length()) engArr.optJSONObject(i) else null
                    val urdObj = urdByNum[apiNumKey]
                        ?: urdByNum[hadithNum.toString()]
                        ?: if (urdArr != null && i < urdArr.length()) urdArr.optJSONObject(i) else null

                    val textEn = engObj?.optString("text", "")?.trim().orEmpty()
                    val textUr = urdObj?.optString("text", "")?.trim().orEmpty()
                    val refObj = araObj.optJSONObject("reference")
                    val inBookHadith = refObj?.optInt("hadith", i + 1) ?: (i + 1)

                    val gradeText = extractGrade(araObj) ?: extractGrade(engObj) ?: "Sahih ($collection)"
                    val narratorText = extractNarrator(textEn) ?: "Companions of the Prophet ﷺ"

                    val directUrl = if (collection == HadithCatalog.COLLECTION_MUSLIM && bookInfo.bookNumber == 0) {
                        "https://sunnah.com/muslim/introduction"
                    } else {
                        "https://sunnah.com/$slug:${hadithNum}"
                    }

                    // Notice we preserve repeated narrations with unique index suffix if duplicate hadithNum appears
                    val uniqueId = "${slug}_b${bookInfo.bookNumber}_${hadithNum}_$i"

                    val itemJson = JSONObject().apply {
                        put("id", uniqueId)
                        put("collection", collection)
                        put("bookNumber", bookInfo.bookNumber)
                        put("bookName", baseBookTitle)
                        put("hadithNumber", hadithNum)
                        put("chapterArabic", bookInfo.titleAr)
                        put("chapterEnglish", "${bookInfo.titleEn} (Hadith $inBookHadith)")
                        put("chapterUrdu", bookInfo.titleUr)
                        put("chapterHindi", bookInfo.titleHi)
                        put("textArabic", textAr)
                        put("textEnglish", textEn)
                        put("textUrdu", textUr)
                        put("textHindi", "")
                        put("narrator", narratorText)
                        put("grade", gradeText)
                        put("sourceAttribution", "$collection, Book ${bookInfo.bookNumber}, Hadith $hadithNum (fawazahmed0/hadith-api)")
                        put("volumeNumber", bookInfo.volumeNumber)
                        put("chapterNumber", inBookHadith)
                        put("sourceUrl", directUrl)
                        put("editionName", manifest.volumeEditionName)
                        put("translatorName", "EN: ${manifest.englishTranslator} • UR: ${manifest.urduTranslator}")
                        put("licenseName", manifest.openDatasetLicense)
                        put("isHindiAvailable", false)
                    }
                    outArray.put(itemJson)
                }

                if (outArray.length() == 0) {
                    return@withContext Result.failure(IllegalStateException("Downloaded dataset contained zero valid narrations"))
                }

                val envelope = JSONObject().apply {
                    put("collection", collection)
                    put("bookNumber", bookInfo.bookNumber)
                    put("volumeNumber", bookInfo.volumeNumber)
                    put("count", outArray.length())
                    put("downloadedAtMs", System.currentTimeMillis())
                    put("hadiths", outArray)
                }

                val targetFile = fileFor(collection, bookInfo.bookNumber)
                val tmpFile = File(targetFile.parentFile, "${targetFile.name}.tmp")
                tmpFile.writeText(envelope.toString())
                if (targetFile.exists()) targetFile.delete()
                tmpFile.renameTo(targetFile)

                bookCache.remove(key)
                refreshDownloadIndex()
                Result.success(outArray.length())
            } catch (e: Exception) {
                val msg = e.message ?: "Network error while downloading Book ${bookInfo.bookNumber}"
                _lastError.value = msg
                Result.failure(e)
            } finally {
                if (_activeDownloadKey.value == key) {
                    _activeDownloadKey.value = null
                    _downloadStatusMessage.value = null
                }
            }
        }

    suspend fun downloadBukhariVolume(volumeNumber: Int): Result<Int> {
        val books = HadithCatalog.booksInBukhariVolume(volumeNumber)
        if (books.isEmpty()) return Result.failure(IllegalArgumentException("Unknown volume $volumeNumber"))
        var totalImported = 0
        _batchProgress.value = 0f
        books.forEachIndexed { idx, book ->
            if (!isBookDownloaded(HadithCatalog.COLLECTION_BUKHARI, book.bookNumber)) {
                val res = downloadBook(HadithCatalog.COLLECTION_BUKHARI, book)
                res.onSuccess { count -> totalImported += count }
                    .onFailure { err ->
                        _batchProgress.value = 0f
                        return Result.failure(err)
                    }
            } else {
                totalImported += (_downloadedBookCounts.value[keyFor(HadithCatalog.COLLECTION_BUKHARI, book.bookNumber)] ?: 0)
            }
            _batchProgress.value = (idx + 1).toFloat() / books.size.toFloat()
        }
        _batchProgress.value = 0f
        return Result.success(totalImported)
    }

    fun deleteDownloadedBook(collection: String, bookNumber: Int): Boolean {
        val key = keyFor(collection, bookNumber)
        val file = fileFor(collection, bookNumber)
        val deleted = if (file.exists()) file.delete() else false
        bookCache.remove(key)
        refreshDownloadIndex()
        return deleted
    }

    fun deleteAllDownloads() {
        storageDir.listFiles()?.forEach { sub ->
            if (sub.isDirectory) {
                sub.listFiles()?.forEach { it.delete() }
            } else {
                sub.delete()
            }
        }
        bookCache.clear()
        refreshDownloadIndex()
    }

    private fun fetchSectionJson(edition: String, sectionId: Int): JSONObject? {
        val urls = listOf(
            "https://cdn.jsdelivr.net/gh/fawazahmed0/hadith-api@1/editions/$edition/sections/$sectionId.min.json",
            "https://cdn.jsdelivr.net/gh/fawazahmed0/hadith-api@1/editions/$edition/sections/$sectionId.json"
        )
        for (url in urls) {
            try {
                val req = Request.Builder()
                    .url(url)
                    .header("Accept", "application/json")
                    .build()
                httpClient.newCall(req).execute().use { resp ->
                    if (resp.isSuccessful) {
                        val body = resp.body?.string()
                        if (!body.isNullOrBlank()) {
                            return JSONObject(body)
                        }
                    }
                }
            } catch (_: Exception) {}
        }
        return null
    }

    private fun mapHadithArrayByNumber(arr: JSONArray?): Map<String, JSONObject> {
        if (arr == null) return emptyMap()
        val map = LinkedHashMap<String, JSONObject>(arr.length())
        for (i in 0 until arr.length()) {
            val o = arr.optJSONObject(i) ?: continue
            val key = formatNumberKey(o.optDouble("hadithnumber", (i + 1).toDouble()))
            map.putIfAbsent(key, o)
        }
        return map
    }

    private fun formatNumberKey(value: Double): String {
        return if (value % 1.0 == 0.0) value.toInt().toString() else value.toString()
    }

    private fun extractGrade(obj: JSONObject?): String? {
        val grades = obj?.optJSONArray("grades") ?: return null
        if (grades.length() == 0) return null
        val first = grades.optJSONObject(0) ?: return null
        val grade = first.optString("grade").trim()
        val name = first.optString("name").trim()
        return when {
            grade.isNotBlank() && name.isNotBlank() -> "$grade ($name)"
            grade.isNotBlank() -> grade
            else -> null
        }
    }

    private fun extractNarrator(englishText: String): String? {
        if (englishText.isBlank()) return null
        val prefix = englishText.take(140)
        val colonIdx = prefix.indexOf(':')
        if (colonIdx in 8..120) {
            val head = prefix.substring(0, colonIdx).trim()
            if (head.startsWith("Narrated", ignoreCase = true) ||
                head.contains("reported", ignoreCase = true) ||
                head.startsWith("It was narrated", ignoreCase = true)
            ) {
                return head
                    .removePrefix("Narrated ")
                    .removePrefix("narrated ")
                    .trim()
                    .take(80)
            }
        }
        return null
    }

    companion object {
        @Volatile
        private var INSTANCE: HadithRepository? = null

        fun getInstance(context: Context): HadithRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: HadithRepository(context.applicationContext).also { INSTANCE = it }
            }
        }
    }
}
