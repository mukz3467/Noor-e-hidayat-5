package com.example

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.data.db.NoorDatabase
import com.example.data.db.NoorDatabaseSeeder
import com.example.data.db.TafseerDao
import com.example.data.db.TafseerPassageEntity
import com.example.data.db.TafseerSourceEntity
import com.example.data.db.TranslationVerseEntity
import com.example.data.model.AppLanguage
import com.example.data.network.QuranCatalogApiService
import com.example.data.repository.QuranData
import com.example.data.repository.QuranTafseerRepository
import com.example.data.repository.QuranTarjumaAssetsLoader
import com.example.data.repository.QuranTranslationTafseerRepository
import com.example.data.repository.VerifiedTafseerCatalog
import com.example.ui.screens.QuranReaderScreen
import com.example.ui.theme.NooreHidayatTheme
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class QuranTafseerIntegrationTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var context: Context
    private lateinit var db: NoorDatabase
    private lateinit var dao: TafseerDao

    @Before
    fun setUp() {
        QuranCatalogApiService.httpGetOverride = null
        context = ApplicationProvider.getApplicationContext()
        QuranTranslationTafseerRepository.getInstance(context).clearOnlineTafseerSessionCache()
        db = Room.inMemoryDatabaseBuilder(context, NoorDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = db.tafseerDao()
        runBlocking {
            NoorDatabaseSeeder.seedIfEmpty(dao)
        }
    }

    @After
    fun tearDown() {
        QuranCatalogApiService.httpGetOverride = null
        QuranTranslationTafseerRepository.getInstance(context).clearOnlineTafseerSessionCache()
        db.close()
    }

    @Test
    fun all114SurahsAnd6236AyahsLoadFromBundledAssetsCleanly() {
        var totalAyahs = 0
        for (surah in QuranData.surahs) {
            val ayahs = QuranData.getAyahsForSurah(context, surah.number)
            assertEquals(
                "Surah ${surah.number} (${surah.nameEnglish}) ayah count mismatch",
                surah.ayahCount,
                ayahs.size
            )
            ayahs.forEachIndexed { idx, ayah ->
                assertEquals(surah.number, ayah.surahNumber)
                assertEquals(idx + 1, ayah.ayahNumber)
                assertFalse("Arabic Uthmani must not contain UTF-8 BOM", ayah.textArabic.contains("\uFEFF"))
                assertTrue("Arabic text must not be blank at ${surah.number}:${ayah.ayahNumber}", ayah.textArabic.isNotBlank())
                assertEquals("Must carry all 4 language slots", 4, ayah.translations.size)
                assertTrue(ayah.translations.all { it.translationText.isNotBlank() })
            }
            totalAyahs += ayahs.size
        }
        assertEquals("Complete Quran must contain 6,236 ayahs", 6236, totalAyahs)
    }

    @Test
    fun roomSeedingPurgesLegacySourcesAndSeedsVerifiedOnlineTafseerCatalog() = runBlocking {
        // Simulate legacy offline/stale rows in Room prior to seeding
        dao.upsertTafseerSources(
            listOf(
                TafseerSourceEntity(
                    id = "tafseer_ibn_kathir",
                    displayOrder = 1,
                    scholarName = "Legacy",
                    translatorName = "",
                    reviewers = "",
                    bookTitle = "Legacy Tafseer",
                    originalLanguage = "Urdu",
                    publisher = "",
                    edition = "",
                    sourceUrl = "",
                    licenseEvidenceUrl = "",
                    permissionStatus = "LEGACY",
                    verificationStatus = "LEGACY",
                    offlineAllowed = true,
                    onlineAllowed = false
                ),
                TafseerSourceEntity(
                    id = "al_mukhtasar_en",
                    displayOrder = 2,
                    scholarName = "Old Bundled",
                    translatorName = "",
                    reviewers = "",
                    bookTitle = "Old Offline Al-Mukhtasar",
                    originalLanguage = "English",
                    publisher = "",
                    edition = "",
                    sourceUrl = "",
                    licenseEvidenceUrl = "",
                    permissionStatus = "LEGACY",
                    verificationStatus = "LEGACY",
                    offlineAllowed = true,
                    onlineAllowed = false
                )
            )
        )
        NoorDatabaseSeeder.seedIfEmpty(dao)

        val sources = dao.getTafseerSources()
        assertEquals(18, sources.size)
        assertNull("Legacy stale tafseer_ibn_kathir must be purged", dao.getTafseerSourceById("tafseer_ibn_kathir"))
        assertNull("Legacy bundled al_mukhtasar_en must be purged", dao.getTafseerSourceById("al_mukhtasar_en"))

        sources.forEach { source ->
            assertFalse("${source.bookTitle} must have offlineAllowed=false", source.offlineAllowed)
            assertTrue("${source.bookTitle} must have onlineAllowed=true", source.onlineAllowed)
            assertFalse("${source.bookTitle} must not mark isDownloaded=true", source.isDownloaded)
            assertTrue("${source.bookTitle} must carry official sourceUrl", source.sourceUrl.startsWith("https://"))
        }

        // Verify per-language online scholar filtering
        assertEquals(
            listOf("en_ibn_kathir", "en_maariful_quran", "en_jalalayn", "en_ibn_abbas", "en_tazkirul_quran"),
            QuranTafseerRepository.getTop5Scholars(AppLanguage.ENGLISH, context).map { it.key }
        )
        assertEquals(
            listOf("ur_ibn_kathir", "ur_bayan_ul_quran", "ur_fi_zilal", "ur_saadi", "ur_tazkirul_quran"),
            QuranTafseerRepository.getTop5Scholars(AppLanguage.URDU, context).map { it.key }
        )
        assertEquals(
            listOf("ar_ibn_kathir", "ar_tabari", "ar_qurtubi", "ar_baghawi", "ar_saadi", "ar_wasit", "ar_jalalayn"),
            QuranTafseerRepository.getTop5Scholars(AppLanguage.ARABIC, context).map { it.key }
        )
        assertEquals(
            listOf("hi_al_mukhtasar"),
            QuranTafseerRepository.getTop5Scholars(AppLanguage.HINDI, context).map { it.key }
        )
    }

    @Test
    fun liveOnlineTafseerApisReturnGenuineScholarlyCommentaryAcrossAllLanguagesAndSurahs() = runBlocking {
        QuranCatalogApiService.httpGetOverride = null
        val repo = QuranTranslationTafseerRepository.getInstance(context)
        repo.ensureSeeded()
        repo.clearOnlineTafseerSessionCache()

        // 1. URDU LIVE CHECK (Quran.com API v4 ID 160 Ibn Kathir across Surah 1, 2:255, and 114:6)
        val ur11 = repo.loadTafseerDetail(1, 1, "ur_ibn_kathir", AppLanguage.URDU, forceRefresh = true)
        assertNull("Unexpected online error for Urdu Ibn Kathir 1:1: ${ur11.onlineError}", ur11.onlineError)
        assertTrue("Urdu Ibn Kathir 1:1 must contain substantial multi-paragraph exegesis", (ur11.passageText?.length ?: 0) >= 800)

        val ur2255 = repo.loadTafseerDetail(2, 255, "ur_ibn_kathir", AppLanguage.URDU, forceRefresh = true)
        assertTrue("Urdu Ibn Kathir 2:255 must be detailed commentary", (ur2255.passageText?.length ?: 0) >= 800)

        val urBayan1146 = repo.loadTafseerDetail(114, 6, "ur_bayan_ul_quran", AppLanguage.URDU, forceRefresh = true)
        assertTrue("Urdu Bayan ul Quran 114:6 must return real online commentary", !urBayan1146.passageText.isNullOrBlank())

        // 2. ENGLISH LIVE CHECK (Ibn Kathir 169 + Ma'arif al-Qur'an 168 + Jalalayn spa5k; verify != Quran translation!)
        val en11 = repo.loadTafseerDetail(1, 1, "en_ibn_kathir", AppLanguage.ENGLISH, forceRefresh = true)
        val ayah11 = QuranData.getAyahsForSurah(context, 1).first { it.ayahNumber == 1 }
        val trans11 = repo.resolveAyahTranslation(ayah11, AppLanguage.ENGLISH)
        assertTrue("English Ibn Kathir 1:1 must contain extensive commentary", (en11.passageText?.length ?: 0) >= 1500)
        assertNotEquals("Tafseer must NEVER just be the verse translation", trans11.translatedText.trim(), en11.passageText?.trim())

        // Grouped span verification (Ibn Kathir 5:97 covers multiple ayahs)
        val enGrouped597 = repo.loadTafseerDetail(5, 97, "en_ibn_kathir", AppLanguage.ENGLISH, forceRefresh = true)
        assertTrue("Grouped commentary 5:97 must return full scholarly text", (enGrouped597.passageText?.length ?: 0) >= 500)
        assertTrue("Grouped commentary 5:97 should include ayah 97 in span", 97 in enGrouped597.startAyah..enGrouped597.endAyah)

        val enMaariful1146 = repo.loadTafseerDetail(114, 6, "en_maariful_quran", AppLanguage.ENGLISH, forceRefresh = true)
        assertTrue("Ma'arif al-Qur'an 114:6 must return detailed exegesis", (enMaariful1146.passageText?.length ?: 0) >= 800)

        val enJalalayn2255 = repo.loadTafseerDetail(2, 255, "en_jalalayn", AppLanguage.ENGLISH, forceRefresh = true)
        assertTrue("Al-Jalalayn (spa5k) 2:255 must load cleanly", !enJalalayn2255.passageText.isNullOrBlank())

        // 3. ARABIC LIVE CHECK (Ibn Kathir 14 + Qurtubi 90)
        val arIbn2255 = repo.loadTafseerDetail(2, 255, "ar_ibn_kathir", AppLanguage.ARABIC, forceRefresh = true)
        assertTrue("Arabic Ibn Kathir 2:255 must return rich commentary", (arIbn2255.passageText?.length ?: 0) >= 1000)

        val arQurtubi597 = repo.loadTafseerDetail(5, 97, "ar_qurtubi", AppLanguage.ARABIC, forceRefresh = true)
        assertTrue("Arabic Qurtubi 5:97 must return rich commentary", !arQurtubi597.passageText.isNullOrBlank())

        // 4. HINDI LIVE CHECK (verified Al-Mukhtasar Hindi edition `hindi-mokhtasar` across short & long Surahs + Surah/raw fallbacks)
        val hindiChecks = listOf(1 to 1, 2 to 255, 18 to 10, 36 to 83, 112 to 1, 114 to 6)
        hindiChecks.forEach { (surahNum, ayahNum) ->
            val hiDetail = repo.loadTafseerDetail(surahNum, ayahNum, "hi_al_mukhtasar", AppLanguage.HINDI, forceRefresh = true)
            assertNull("Hindi Tafseer $surahNum:$ayahNum unexpected error: ${hiDetail.onlineError}", hiDetail.onlineError)
            val text = hiDetail.passageText.orEmpty()
            assertTrue("Hindi online Tafseer $surahNum:$ayahNum must not be blank", text.isNotBlank())
            assertTrue(
                "Hindi Tafseer $surahNum:$ayahNum must contain Devanagari Hindi script",
                text.any { it in '\u0900'..'\u097F' }
            )
            assertFalse(
                "Hindi Tafseer $surahNum:$ayahNum must not substitute Urdu/Arabic prose for commentary",
                text.count { it in '\u0600'..'\u06FF' } > text.count { it in '\u0900'..'\u097F' }
            )
            val ayahObj = QuranData.getAyahsForSurah(context, surahNum).first { it.ayahNumber == ayahNum }
            val hiTrans = repo.resolveAyahTranslation(ayahObj, AppLanguage.HINDI)
            assertNotEquals(
                "Hindi Tafseer $surahNum:$ayahNum must NOT mirror Hindi Quran translation",
                hiTrans.translatedText.trim(),
                text.trim()
            )
            assertNotNull("Hindi must display honest upstream catalog scope note", hiDetail.languageAvailabilityNote)
        }
    }

    @Test
    fun onlineTafseerNetworkFailureReturnsHonestErrorStateWithoutTranslationFallback() = runBlocking {
        val repo = QuranTranslationTafseerRepository.getInstance(context)
        repo.ensureSeeded()
        repo.clearOnlineTafseerSessionCache()
        QuranCatalogApiService.httpGetOverride = { null }

        val ayah11 = QuranData.getAyahsForSurah(context, 1).first { it.ayahNumber == 1 }
        val checks = listOf(
            AppLanguage.ENGLISH to "en_ibn_kathir",
            AppLanguage.URDU to "ur_ibn_kathir",
            AppLanguage.ARABIC to "ar_ibn_kathir",
            AppLanguage.HINDI to "hi_al_mukhtasar"
        )

        checks.forEach { (lang, scholarId) ->
            val detail = repo.loadTafseerDetail(1, 1, scholarId, lang, forceRefresh = true)
            val translation = repo.resolveAyahTranslation(ayah11, lang)
            assertNull("Network error must NOT show fallback passageText for $lang", detail.passageText)
            assertFalse("Network error must populate onlineError for $lang", detail.onlineError.isNullOrBlank())
            assertFalse("Network error must NOT display Quran translation as Tafseer", detail.onlineError!!.contains(translation.translatedText))
        }
    }

    @Test
    fun onlineTafseerStrictLanguageSyncRefusesCrossLanguageIds() = runBlocking {
        assertNull(VerifiedTafseerCatalog.resolveStrictOnlineEdition("ur_ibn_kathir", AppLanguage.ENGLISH))
        assertNull(VerifiedTafseerCatalog.resolveStrictOnlineEdition("en_ibn_kathir", AppLanguage.URDU))
        val snapped = VerifiedTafseerCatalog.resolveOnlineEdition("ur_ibn_kathir", AppLanguage.ENGLISH)
        assertEquals(AppLanguage.ENGLISH, snapped.language)
        assertEquals("en_ibn_kathir", snapped.id)
    }

    @Test
    fun multiAyahPassageRangeMatchesAllIncludedAyahsWithoutSplitting() = runBlocking {
        val passage = TafseerPassageEntity(
            id = "test_ibn_kathir_2_1_5",
            sourceId = "en_ibn_kathir",
            surahNumber = 2,
            startAyah = 1,
            endAyah = 5,
            originalText = "Authorized multi-ayah commentary spanning 2:1-5.",
            volume = "Vol. 1",
            page = "p. 42",
            sourceReference = "Quran.com API v4 • 2:1-5",
            verified = true
        )
        dao.upsertPassages(listOf(passage))

        for (ayahNum in 1..5) {
            val found = dao.getPassageForAyah("en_ibn_kathir", 2, ayahNum)
            assertNotNull("Ayah $ayahNum must resolve multi-ayah passage 2:1-5", found)
            assertEquals(1, found!!.startAyah)
            assertEquals(5, found.endAyah)
        }

        val outside = dao.getPassageForAyah("en_ibn_kathir", 2, 6)
        assertNull("Ayah 6 must not falsely match 2:1-5", outside)
    }

    @Test
    fun asbabAlNuzulReturnsVerifiedNarrationsOrExplicitUnavailableMessage() = runBlocking {
        val qiblahAsbab = dao.getAsbabAlNuzulForAyah(2, 144)
        assertTrue("2:144 must return verified Asbab al-Nuzul", qiblahAsbab.isNotEmpty())
        assertTrue(qiblahAsbab.first().hadithReference.contains("Sahih al-Bukhari"))

        val safaAsbab = dao.getAsbabAlNuzulForAyah(2, 158)
        assertTrue("2:158 must return verified Asbab al-Nuzul", safaAsbab.isNotEmpty())
        assertTrue(safaAsbab.first().hadithReference.contains("1643"))

        val unmappedAsbab = dao.getAsbabAlNuzulForAyah(12, 4)
        assertTrue("Unmapped ayah must not return invented Asbab al-Nuzul", unmappedAsbab.isEmpty())
        assertEquals(
            "No verified specific occasion of revelation is available in the selected source.",
            QuranTafseerRepository.getUnavailableAsbabMessage(AppLanguage.ENGLISH)
        )
    }

    @Test
    fun revelationContextAcrossMultipleSurahsAndAyahsPopulatesAllFieldsAccurately() = runBlocking {
        val testAyahRefs = listOf(
            1 to 1,
            1 to 5,
            2 to 144,
            2 to 158,
            2 to 255,
            4 to 95,
            9 to 40,
            12 to 4,
            48 to 1,
            112 to 1
        )

        val repo = QuranTranslationTafseerRepository.getInstance(context)
        repo.ensureSeeded()

        testAyahRefs.forEach { (surahNum, ayahNum) ->
            val ayahs = QuranData.getAyahsForSurah(context, surahNum)
            val ayah = ayahs.first { it.ayahNumber == ayahNum }
            val resolvedTrans = repo.resolveAyahTranslation(ayah, AppLanguage.ENGLISH)
            val studyCtx = repo.loadTarjumaStudyContext(
                ayah = ayah,
                language = AppLanguage.ENGLISH,
                activeTranslation = resolvedTrans,
                fetchOnlineWisdom = false
            )

            assertTrue("sababNuzool must not be blank for $surahNum:$ayahNum", studyCtx.sababNuzool.isNotBlank())
            assertTrue("zamanaNuzool must not be blank for $surahNum:$ayahNum", studyCtx.zamanaNuzool.isNotBlank())
            assertTrue("maqsadHikmat must not be blank for $surahNum:$ayahNum", studyCtx.maqsadHikmat.isNotBlank())
            assertTrue("fazaailBenefits must not be blank for $surahNum:$ayahNum", studyCtx.fazaailBenefits.isNotBlank())
            assertTrue("authenticReferences must not be blank for $surahNum:$ayahNum", studyCtx.authenticReferences.isNotBlank())
        }

        val ctx2144 = QuranTafseerRepository.getRevelationContext(2, 144, AppLanguage.ENGLISH, context)
        val ctx124 = QuranTafseerRepository.getRevelationContext(12, 4, AppLanguage.ENGLISH, context)
        val ctx125 = QuranTafseerRepository.getRevelationContext(12, 5, AppLanguage.ENGLISH, context)
        assertTrue(ctx2144.sababNuzool.contains("Qiblah", ignoreCase = true))
        assertTrue(ctx124.sababNuzool.contains("Wahidi", ignoreCase = true))
        assertTrue(ctx125.sababNuzool.contains("No verified specific occasion of revelation"))

        val bundledEditions = listOf(
            "urdu_junagarhi",
            "english_rwwad",
            "hindi_omari",
            "arabic_mokhtasar",
            "english_mokhtasar",
            "hindi_mokhtasar"
        )
        bundledEditions.forEach { key ->
            val v1 = QuranTarjumaAssetsLoader.getBundledQuranEncVerse(context, key, 1, 1)
            val v255 = QuranTarjumaAssetsLoader.getBundledQuranEncVerse(context, key, 2, 255)
            val v114 = QuranTarjumaAssetsLoader.getBundledQuranEncVerse(context, key, 114, 6)
            assertNotNull("$key missing 1:1", v1)
            assertNotNull("$key missing 2:255", v255)
            assertNotNull("$key missing 114:6", v114)
        }
        assertNotNull(QuranTarjumaAssetsLoader.getAsbabAlNuzul(context, 93, 1))
        assertNotNull(QuranTarjumaAssetsLoader.getSurahHeaderArabic(context, 5))
        assertEquals(5, QuranTarjumaAssetsLoader.getSurahRevelationChronology(context, 1)?.revelationOrder)

        val ayah255 = QuranData.getAyahsForSurah(context, 2).first { it.ayahNumber == 255 }
        val resolvedUrJunagarhi = repo.resolveAyahTranslation(ayah255, AppLanguage.URDU, "quranenc_urdu_junagarhi")
        assertTrue(resolvedUrJunagarhi.isOfflineAvailable)
        assertTrue(resolvedUrJunagarhi.translatedText.isNotBlank())
        assertTrue(resolvedUrJunagarhi.footnotes.isNotBlank())
    }

    @Test
    fun translationCatalogPermissionRulesAndOfflineDeletionWorkProperly() = runBlocking {
        val catalog = dao.getTranslationCatalog()
        assertTrue(catalog.any { it.translationId == "tanzil_ar_uthmani" && it.isBundledOffline && it.offlineAllowed })
        assertTrue(catalog.any { it.translationId == "quranenc_urdu_junagarhi" && it.isBundledOffline && it.isDownloadedOffline && it.offlineAllowed })
        assertTrue(catalog.any { it.translationId == "quranenc_english_rwwad" && it.isBundledOffline && it.isDownloadedOffline && it.offlineAllowed })
        assertTrue(catalog.any { it.translationId == "quranenc_hindi_omari" && it.isBundledOffline && it.isDownloadedOffline && it.offlineAllowed })
        assertTrue(catalog.any { it.translationId == "quranenc_english_saheeh" && it.offlineAllowed && !it.isBundledOffline })

        val qfClearQuran = dao.getTranslationCatalogById("qf_en_clear_quran_131")
        assertNotNull(qfClearQuran)
        assertFalse("Quran Foundation translation must have offlineAllowed=false", qfClearQuran!!.offlineAllowed)
        assertTrue(qfClearQuran.onlineAllowed)

        dao.upsertTranslationVerses(
            listOf(
                TranslationVerseEntity(
                    id = "quranenc_english_saheeh_1_1",
                    translationId = "quranenc_english_saheeh",
                    surahNumber = 1,
                    ayahNumber = 1,
                    translatedText = "In the name of Allah, the Entirely Merciful, the Especially Merciful.",
                    footnotes = ""
                )
            )
        )
        val metaBefore = dao.getTranslationCatalogById("quranenc_english_saheeh")!!
        assertFalse("Incomplete verse storage must never auto-mark downloaded", metaBefore.isDownloadedOffline)

        dao.deleteTranslationVerses("quranenc_english_saheeh")
        dao.updateTranslationDownloadState("quranenc_english_saheeh", isDownloaded = false, ayahCount = 0, sizeBytes = 0L)
        assertEquals(0, dao.countDownloadedVerses("quranenc_english_saheeh"))
    }

    @Test
    fun tarjumaAndTafseerBottomSheetsMaintainStrictContentSeparation() {
        installDeterministicOnlineStub { false }

        composeTestRule.setContent {
            NooreHidayatTheme {
                QuranReaderScreen(
                    surahNumber = 1,
                    language = AppLanguage.ENGLISH,
                    onBack = {},
                    onNavigateToSurah = {}
                )
            }
        }
        composeTestRule.waitForIdle()

        // 1. Open Translation (Tarjuma) bottom sheet
        composeTestRule.waitUntil(timeoutMillis = 5_000L) {
            composeTestRule.onAllNodesWithTag("quran_reader_ayah_list").fetchSemanticsNodes().isNotEmpty()
        }
        composeTestRule.onNodeWithTag("quran_reader_ayah_list").performScrollToNode(hasTestTag("ayah_card_1"))
        composeTestRule.waitUntil(timeoutMillis = 5_000L) {
            composeTestRule.onAllNodesWithTag("ayah_1_translation_btn").fetchSemanticsNodes().isNotEmpty()
        }
        composeTestRule.onNodeWithTag("ayah_1_translation_btn").performClick()

        composeTestRule.waitUntil(timeoutMillis = 5_000L) {
            composeTestRule.onAllNodesWithTag("tarjuma_sheet_list").fetchSemanticsNodes().isNotEmpty() &&
                runCatching {
                    composeTestRule.onNodeWithTag("tarjuma_sheet_list").performScrollToNode(hasText("4. WHEN WAS THIS AYAH REVEALED?"))
                    composeTestRule.onAllNodesWithText("4. WHEN WAS THIS AYAH REVEALED?").fetchSemanticsNodes().isNotEmpty()
                }.getOrDefault(false)
        }

        // Translation sheet must retain existing structure & NO Tafseer scholar chips
        assertTrue(composeTestRule.onAllNodesWithTag("tarjuma_section_3").fetchSemanticsNodes().isEmpty())
        assertTrue(composeTestRule.onAllNodesWithTag("scholar_chip_en_ibn_kathir").fetchSemanticsNodes().isEmpty())
        composeTestRule.onNodeWithTag("tarjuma_sheet_list").performScrollToNode(hasText("2. Authentic Published Translation (English)"))
        composeTestRule.onNodeWithText("2. Authentic Published Translation (English)").assertIsDisplayed()
        composeTestRule.onNodeWithTag("tarjuma_sheet_list").performScrollToNode(hasText("5. HIDDEN WISDOM AND DEEP MEANING"))
        composeTestRule.onNodeWithText("5. HIDDEN WISDOM AND DEEP MEANING").assertIsDisplayed()

        // Dismiss Translation sheet, then open separate Tafseer bottom sheet
        composeTestRule.onNodeWithTag("tarjuma_sheet_list").performScrollToNode(hasText("1. Arabic Quranic Ayah"))
        composeTestRule.waitForIdle()
        // Close via semantic Close icon on Translation sheet header
        composeTestRule.onNodeWithTag("ayah_1_tafseer_btn")
    }

    @Test
    fun tafseerBottomSheetSyncsAll4LanguagesNavigatesAyahsAndHandlesOnlineErrorRetry() {
        var simulateNetworkError = false
        installDeterministicOnlineStub { simulateNetworkError }
        var activeLanguage by mutableStateOf(AppLanguage.ENGLISH)

        composeTestRule.setContent {
            NooreHidayatTheme {
                QuranReaderScreen(
                    surahNumber = 1,
                    language = activeLanguage,
                    onBack = {},
                    onNavigateToSurah = {}
                )
            }
        }
        composeTestRule.waitUntil(timeoutMillis = 5_000L) {
            composeTestRule.onAllNodesWithTag("quran_reader_ayah_list").fetchSemanticsNodes().isNotEmpty()
        }
        composeTestRule.onNodeWithTag("quran_reader_ayah_list").performScrollToNode(hasTestTag("ayah_card_1"))
        composeTestRule.waitUntil(timeoutMillis = 5_000L) {
            composeTestRule.onAllNodesWithTag("ayah_1_tafseer_btn").fetchSemanticsNodes().isNotEmpty()
        }

        // 1. Open Tafseer sheet in ENGLISH online -> loads Ibn Kathir & allows scholar switching
        composeTestRule.onNodeWithTag("ayah_1_tafseer_btn").performClick()
        composeTestRule.waitUntil(timeoutMillis = 5_000L) {
            composeTestRule.onAllNodesWithTag("tafseer_only_passage_card").fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onNodeWithTag("scholar_chip_en_ibn_kathir").assertIsDisplayed()
        composeTestRule.onNodeWithTag("scholar_chip_en_maariful_quran").assertIsDisplayed()
        assertEquals(0, composeTestRule.onAllNodesWithTag("scholar_chip_ur_ibn_kathir").fetchSemanticsNodes().size)
        assertEquals(0, composeTestRule.onAllNodesWithTag("scholar_chip_ar_ibn_kathir").fetchSemanticsNodes().size)
        composeTestRule.onNodeWithText("Exegesis of Bismillah according to Hafiz Ibn Kathir", substring = true).assertIsDisplayed()

        // Ensure Translation study sections are NOT inside Tafseer sheet
        assertEquals(0, composeTestRule.onAllNodesWithText("HIDDEN WISDOM AND DEEP MEANING", substring = true).fetchSemanticsNodes().size)

        // Verify Asbab & Historical cards inside Tafseer sheet
        composeTestRule.onNodeWithTag("tafseer_sheet_list").performScrollToNode(hasTestTag("tafseer_asbab_section"))
        composeTestRule.onNodeWithTag("tafseer_asbab_section").assertIsDisplayed()
        composeTestRule.onNodeWithTag("tafseer_sheet_list").performScrollToNode(hasTestTag("tafseer_historical_section"))
        composeTestRule.onNodeWithTag("tafseer_historical_section").assertIsDisplayed()
        composeTestRule.onNodeWithTag("tafseer_sheet_list").performScrollToNode(hasTestTag("tafseer_only_passage_card"))

        // Switch English scholar to Ma'arif al-Qur'an
        composeTestRule.onNodeWithTag("scholar_chip_en_maariful_quran").performClick()
        composeTestRule.waitUntil(timeoutMillis = 5_000L) {
            composeTestRule.onAllNodesWithText("Ma'arif al-Qur'an scholarly commentary on Ayah 1:1", substring = true).fetchSemanticsNodes().isNotEmpty()
        }

        // 2. Navigate to Next Ayah (1:2) inside Tafseer sheet
        composeTestRule.onNodeWithTag("tafseer_next_ayah_btn").performClick()
        composeTestRule.waitUntil(timeoutMillis = 5_000L) {
            composeTestRule.onAllNodesWithText("Scholarly Tafseer — Ayah 1:2", substring = true).fetchSemanticsNodes().isNotEmpty() &&
                composeTestRule.onAllNodesWithTag("tafseer_only_passage_card").fetchSemanticsNodes().isNotEmpty()
        }

        // 3. Switch language to URDU -> auto-synced to 5 Urdu scholars & RTL scholarly commentary
        composeTestRule.runOnUiThread { activeLanguage = AppLanguage.URDU }
        composeTestRule.waitUntil(timeoutMillis = 5_000L) {
            composeTestRule.onAllNodesWithTag("scholar_chip_ur_ibn_kathir").fetchSemanticsNodes().isNotEmpty() &&
                composeTestRule.onAllNodesWithText("تفسیر ابن کثیر اردو کا تفصیلی علمی بیان", substring = true).fetchSemanticsNodes().isNotEmpty()
        }
        assertEquals(0, composeTestRule.onAllNodesWithTag("scholar_chip_en_ibn_kathir").fetchSemanticsNodes().size)

        // 4. Switch language to ARABIC -> Arabic classical scholars load & switch cleanly
        composeTestRule.runOnUiThread { activeLanguage = AppLanguage.ARABIC }
        composeTestRule.waitUntil(timeoutMillis = 5_000L) {
            composeTestRule.onAllNodesWithTag("scholar_chip_ar_ibn_kathir").fetchSemanticsNodes().isNotEmpty()
        }
        composeTestRule.onNodeWithTag("scholar_chip_ar_qurtubi").performClick()
        composeTestRule.waitUntil(timeoutMillis = 5_000L) {
            composeTestRule.onAllNodesWithText("تفسير القرطبي الجامع لأحكام القرآن", substring = true).fetchSemanticsNodes().isNotEmpty()
        }

        // 5. Switch language to HINDI -> Hindi scholar + transparent scope note displayed
        composeTestRule.runOnUiThread { activeLanguage = AppLanguage.HINDI }
        composeTestRule.waitUntil(timeoutMillis = 5_000L) {
            composeTestRule.onAllNodesWithTag("scholar_chip_hi_al_mukhtasar").fetchSemanticsNodes().isNotEmpty() &&
                composeTestRule.onAllNodesWithTag("tafseer_language_scope_note").fetchSemanticsNodes().isNotEmpty()
        }

        // 6. Simulate offline / network failure -> verify tafseer_error_card + Retry recovery
        simulateNetworkError = true
        composeTestRule.onNodeWithTag("tafseer_prev_ayah_btn").performClick()
        composeTestRule.waitUntil(timeoutMillis = 5_000L) {
            composeTestRule.onAllNodesWithTag("tafseer_error_card").fetchSemanticsNodes().isNotEmpty()
        }
        composeTestRule.onNodeWithTag("tafseer_retry_btn").assertIsDisplayed()
        assertEquals(0, composeTestRule.onAllNodesWithTag("tafseer_only_passage_card").fetchSemanticsNodes().size)

        simulateNetworkError = false
        composeTestRule.onNodeWithTag("tafseer_retry_btn").performClick()
        composeTestRule.waitUntil(timeoutMillis = 5_000L) {
            composeTestRule.onAllNodesWithTag("tafseer_only_passage_card").fetchSemanticsNodes().isNotEmpty()
        }
        composeTestRule.onNodeWithText("हिन्दी ऑनलाइन तफ़सीर अल-मुख़्तसर", substring = true).assertIsDisplayed()
    }

    private fun installDeterministicOnlineStub(shouldFail: () -> Boolean) {
        QuranCatalogApiService.httpGetOverride = { url ->
            if (shouldFail()) {
                null
            } else {
                when {
                    url.contains("/tafsirs/169/by_ayah/") -> """
                        {"tafsir":{"resource_id":169,"resource_name":"Tafsir Ibn Kathir","translated_name":{"language_name":"english"},"text":"<p>Exegesis of Bismillah according to Hafiz Ibn Kathir: Allah commands His servants to begin righteous actions in His blessed Name.</p><p>Second scholarly paragraph detailing classical chains from Sahih al-Bukhari and Ibn Abbas regarding Al-Fatihah.</p>","verses":{"1:1":{"id":1}}}}
                    """.trimIndent()
                    url.contains("/tafsirs/168/by_ayah/") -> """
                        {"tafsir":{"resource_id":168,"resource_name":"Ma'arif al-Qur'an","translated_name":{"language_name":"english"},"text":"<p>Ma'arif al-Qur'an scholarly commentary on Ayah 1:1 explaining linguistic roots of Rahmah and Hanafi legal rulings.</p>","verses":{"1:1":{"id":1}}}}
                    """.trimIndent()
                    url.contains("/tafsirs/160/by_ayah/") -> """
                        {"tafsir":{"resource_id":160,"resource_name":"Tafsir Ibn Kathir Urdu","translated_name":{"language_name":"urdu"},"text":"<p>تفسیر ابن کثیر اردو کا تفصیلی علمی بیان: اللہ تعالیٰ کے بابرکت نام سے آغاز کی فضیلت اور صحابہ کرامؓ کے اقوال کی روشنی میں مکمل تشریح۔</p>","verses":{"1:2":{"id":2}}}}
                    """.trimIndent()
                    url.contains("/tafsirs/14/by_ayah/") -> """
                        {"tafsir":{"resource_id":14,"resource_name":"تفسير ابن كثير","translated_name":{"language_name":"arabic"},"text":"<p>تفسير الحافظ ابن كثير للآية الكريمة في أم الكتاب وما ورد في فضائل الفاتحة.</p>","verses":{"1:2":{"id":2}}}}
                    """.trimIndent()
                    url.contains("/tafsirs/90/by_ayah/") -> """
                        {"tafsir":{"resource_id":90,"resource_name":"تفسير القرطبي","translated_name":{"language_name":"arabic"},"text":"<p>تفسير القرطبي الجامع لأحكام القرآن الكريم وبيان المسائل الفقهية واللغوية.</p>","verses":{"1:2":{"id":2}}}}
                    """.trimIndent()
                    url.contains("/tafsir/hindi-mokhtasar/") -> """
                        {"ayah":1,"surah":1,"text":"हिन्दी ऑनलाइन तफ़सीर अल-मुख़्तसर: अल्लाह के नाम और उसकी अपार दया की व्याख्या।"}
                    """.trimIndent()
                    url.contains("/chapters/") -> """
                        {"chapter_info":{"chapter_id":1,"language_name":"en","short_text":"Surah Al-Fatihah is a Makki Surah revealed early in Makkah as the Opening of the Book.","source":"Sayyid Abul Ala Maududi","text":"Surah Al-Fatihah detailed historical context."}}
                    """.trimIndent()
                    url.contains("/tafsir/en-asbab-al-nuzul-by-al-wahidi/") -> """
                        {"ayah":1,"surah":1,"text":"Imam al-Wahidi narration regarding revelation context."}
                    """.trimIndent()
                    else -> """
                        {"tafsir":{"resource_id":1,"resource_name":"Scholarly Online Tafsir","translated_name":{"language_name":"english"},"text":"<p>Detailed online scholarly exegesis for $url.</p>","verses":{}}}
                    """.trimIndent()
                }
            }
        }
    }
}
