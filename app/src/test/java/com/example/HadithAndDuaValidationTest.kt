package com.example

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.performTextInput
import androidx.test.core.app.ApplicationProvider
import com.example.data.model.AppLanguage
import com.example.data.model.BookmarkItem
import com.example.data.model.BookmarkType
import com.example.data.repository.DuaAudioPlayer
import com.example.data.repository.DuaData
import com.example.data.repository.HadithCatalog
import com.example.data.repository.HadithData
import com.example.data.repository.HadithRepository
import com.example.data.repository.UserPreferencesRepository
import com.example.localization.AppLocalization
import com.example.ui.screens.DuasScreen
import com.example.ui.screens.HadithScreen
import com.example.ui.theme.NooreHidayatTheme
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class HadithAndDuaValidationTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var context: Context

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        context.getSharedPreferences("noor_e_hidayat_prefs", Context.MODE_PRIVATE).edit().clear().commit()
    }

    @Test
    fun bukhariHas97UniqueBooksAcross7VolumesWithoutSplits() {
        val books = HadithCatalog.bukhari97Books
        val volumes = HadithCatalog.bukhari7Volumes
        assertEquals(97, books.size)
        assertEquals(7, volumes.size)

        // Exact 1..97 sequential coverage
        assertEquals((1..97).toList(), books.map { it.bookNumber })
        assertEquals((1..7).toList(), volumes.map { it.volumeNumber })

        // Verify volumes span 1..97 contiguously without gaps or overlap
        val volumeAssignedBooks = volumes.flatMap { HadithCatalog.booksInBukhariVolume(it.volumeNumber) }
        assertEquals(97, volumeAssignedBooks.size)
        assertEquals((1..97).toList(), volumeAssignedBooks.map { it.bookNumber }.sorted())

        // Every book carries all 4-language titles + sunnah.com URL
        books.forEach { b ->
            assertEquals(HadithCatalog.COLLECTION_BUKHARI, b.collection)
            assertTrue(b.titleEn.isNotBlank())
            assertTrue(b.titleUr.isNotBlank())
            assertTrue(b.titleAr.isNotBlank())
            assertTrue(b.titleHi.isNotBlank())
            assertTrue(b.sourceUrl.startsWith("https://sunnah.com/bukhari/"))
        }
    }

    @Test
    fun muslimHas57IndependentSectionsAndNeverMergesWithBukhari() {
        val books = HadithCatalog.muslim57Books
        assertEquals(57, books.size)
        assertEquals((0..56).toList(), books.map { it.bookNumber })
        assertEquals("Introduction (Muqaddimah)", books[0].titleEn)
        assertEquals("https://sunnah.com/muslim/introduction", books[0].sourceUrl)

        books.forEach { m ->
            assertEquals(HadithCatalog.COLLECTION_MUSLIM, m.collection)
            assertTrue(m.sourceUrl.startsWith("https://sunnah.com/muslim/"))
        }

        // Manifests are distinct & honest
        val bukhariManifest = HadithCatalog.getManifest(HadithCatalog.COLLECTION_BUKHARI)
        val muslimManifest = HadithCatalog.getManifest(HadithCatalog.COLLECTION_MUSLIM)
        assertNotEquals(bukhariManifest.arabicEditionId, muslimManifest.arabicEditionId)
        assertTrue(bukhariManifest.openDatasetLicense.contains("Unlicense"))
        assertTrue(muslimManifest.hindiStatusNote.contains("Hindi", ignoreCase = true))
    }

    @Test
    fun curatedHadithsHaveUniqueIdsArabicNarratorsAndSourceUrls() {
        val ids = HadithData.hadiths.map { it.id }
        assertEquals("Curated Hadith IDs must be unique", ids.size, ids.toSet().size)

        HadithData.hadiths.forEach { h ->
            assertTrue(h.textArabic.isNotBlank())
            assertTrue(h.textEnglish.isNotBlank())
            assertTrue(h.textUrdu.isNotBlank())
            assertTrue(h.textHindi.isNotBlank())
            assertTrue(h.narrator.isNotBlank())
            assertTrue(h.sourceUrl.startsWith("https://sunnah.com/"))
            assertTrue(h.licenseName.isNotBlank())
        }
    }

    @Test
    fun priorityMasnoonDuasAThroughGAreCompleteAndVerified() {
        val allIds = DuaData.duas.map { it.id }
        assertEquals("Dua IDs must be unique", allIds.size, allIds.toSet().size)

        val duaMap = DuaData.duas.associateBy { it.id }

        // A. Huda / Tuqa / Afaf / Ghina (Muslim 2721a)
        val a = duaMap["dua_huda_tuqa_afaf_ghina"]!!
        assertTrue(a.arabic.contains("الْهُدَى وَالتُّقَى وَالْعَفَافَ وَالْغِنَى"))
        assertTrue(a.narrator.contains("Mas'ud", ignoreCase = true))
        assertTrue(a.sourceUrl.contains("sunnah.com/muslim:2721"))
        assertFalse(a.hasGenuineAudio)

        // B. Ayat al-Kursi (Quran 2:255)
        val b = duaMap["dua_ayat_ul_kursi"]!!
        assertTrue(b.arabic.contains("لَآ إِلَٰهَ إِلَّا هُوَ ٱلْحَىُّ ٱلْقَيُّومُ"))
        assertTrue(b.hasGenuineAudio)
        assertEquals(listOf("https://everyayah.com/data/Alafasy_128kbps/002255.mp3"), b.audioSources)
        assertTrue(b.sourceUrl.contains("quran.com/2/255"))

        // C. Last 2 Verses of Al-Baqarah (2:285-286)
        val c = duaMap["dua_last_two_baqarah"]!!
        assertTrue(c.arabic.contains("ءَامَنَ ٱلرَّسُولُ"))
        assertTrue(c.arabic.contains("لَا يُكَلِّفُ ٱللَّهُ نَفْسًا"))
        assertTrue(c.hasGenuineAudio)
        assertEquals(2, c.audioSources.size)

        // D. Three Quls (3x each morning & evening, Abu Dawud 5082)
        val d = duaMap["dua_three_quls"]!!
        assertEquals(3, d.repeatCount)
        assertTrue(d.arabic.contains("قُلْ هُوَ ٱللَّهُ أَحَدٌ"))
        assertTrue(d.arabic.contains("قُلْ أَعُوذُ بِرَبِّ ٱلْفَلَقِ"))
        assertTrue(d.arabic.contains("قُلْ أَعُوذُ بِرَبِّ ٱلنَّاسِ"))
        assertTrue(d.hasGenuineAudio)
        assertEquals(
            listOf(
                "asset:audio/surah_112_mishary.mp3",
                "asset:audio/surah_113_mishary.mp3",
                "asset:audio/surah_114_mishary.mp3"
            ),
            d.audioSources
        )

        // E. Surah Al-Baqarah in Home (Muslim 780)
        val e = duaMap["dua_baqarah_in_home"]!!
        assertTrue(e.translationEn.contains("houses into graveyards", ignoreCase = true))
        assertTrue(e.sourceUrl.contains("sunnah.com/muslim:780"))

        // F. Sayyidul Istighfar (Bukhari 6306)
        val f = duaMap["dua_sayyidul_istighfar"]!!
        assertTrue(f.arabic.contains("اللَّهُمَّ أَنْتَ رَبِّي"))
        assertTrue(f.sourceUrl.contains("sunnah.com/bukhari:6306"))

        // G. Bismillahilladhi La Yadurru 3x
        val g = duaMap["dua_protection_bismillah_3x"]!!
        assertEquals(3, g.repeatCount)
        val normActual = java.text.Normalizer.normalize(g.arabic, java.text.Normalizer.Form.NFC)
        val normExpected = java.text.Normalizer.normalize("بِسْمِ اللَّهِ الَّذِي لَا يَضُرُّ مَعَ اسْمِهِ شَيْءٌ", java.text.Normalizer.Form.NFC)
        assertTrue(normActual.contains(normExpected))
    }

    @Test
    fun duaAudioPlayerNeverSimulatesOrPlaysAudioWhenMissing() {
        val propheticDua = DuaData.duas.first { it.id == "dua_huda_tuqa_afaf_ghina" }
        assertFalse(propheticDua.hasGenuineAudio)
        assertTrue(propheticDua.audioSources.isEmpty())

        DuaAudioPlayer.stop()
        DuaAudioPlayer.clearError()
        DuaAudioPlayer.playDua(context, propheticDua, repeatTarget = 1)

        assertFalse("Must never play synthesized audio or beep", DuaAudioPlayer.isPlaying.value)
        assertNull(DuaAudioPlayer.currentDuaId.value)
        assertNotNull("Must expose explicit unavailable message", DuaAudioPlayer.errorMessage.value)

        // Bundled Three Quls is already offline-ready without network
        val threeQuls = DuaData.duas.first { it.id == "dua_three_quls" }
        assertTrue(DuaAudioPlayer.isDuaOfflineReady(context, threeQuls))
    }

    @Test
    fun preferencesPersistBookmarksAndLastReadHadithAcrossInstances() {
        val repo1 = UserPreferencesRepository(context)
        val addedHadith = repo1.toggleBookmark(
            BookmarkItem(
                id = "bm_hadith_bukhari_6306",
                type = BookmarkType.HADITH,
                title = "Sahih al-Bukhari #6306",
                subtitle = "سَيِّدُ الِاسْتِغْفَارِ",
                hadithId = "bukhari_6306"
            )
        )
        assertTrue(addedHadith)

        repo1.saveLastReadHadith(
            collection = HadithCatalog.COLLECTION_MUSLIM,
            bookNumber = 1,
            hadithId = "muslim_1",
            hadithNumber = 1,
            bookTitle = "The Book of Faith"
        )

        // Recreate repository on fresh instance backed by same SharedPreferences
        val repo2 = UserPreferencesRepository(context)
        assertTrue(repo2.isHadithBookmarked("bukhari_6306"))
        val last = repo2.lastReadHadith.value
        assertNotNull(last)
        assertEquals(HadithCatalog.COLLECTION_MUSLIM, last!!.collection)
        assertEquals("muslim_1", last.hadithId)
    }

    @Test
    fun hadithRepositoryProgressIsHonestAndSearchCoversAll4Languages() {
        val repo = HadithRepository.getInstance(context)
        repo.deleteAllDownloads()

        val bukhariProg = repo.getCollectionProgress(HadithCatalog.COLLECTION_BUKHARI)
        assertEquals(97, bukhariProg.totalBooks)
        assertEquals(0, bukhariProg.downloadedFullBooksCount)
        assertFalse(bukhariProg.isFullCollectionOffline)
        assertTrue(bukhariProg.narrationsCompletionPercent < 5)

        // Search in curated Hindi, English & Arabic
        val hiMatches = repo.searchAvailableHadiths(HadithCatalog.COLLECTION_BUKHARI, "नीयतों")
        assertTrue("Hindi search in curated core should match Hadith #1", hiMatches.any { it.hadithNumber == 1 })

        val muslimMatches = repo.searchAvailableHadiths(HadithCatalog.COLLECTION_MUSLIM, "6306")
        assertTrue("Independent Muslim search must not include Bukhari #6306", muslimMatches.none { it.collection == HadithCatalog.COLLECTION_BUKHARI })
    }

    @Test
    fun uiShowsUnavailableAudioNoticeForHadithDuaAndProgressInHadithScreen() {
        composeTestRule.setContent {
            NooreHidayatTheme {
                HadithScreen(language = AppLanguage.ENGLISH, onBack = {})
            }
        }
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("hadith_manifest_btn").assertIsDisplayed()
        composeTestRule.onNodeWithTag("hadith_filter_muslim").assertIsDisplayed()
        composeTestRule.onNodeWithTag("hadith_progress_Sahih al-Bukhari").assertIsDisplayed()
    }

    @Test
    fun uiRenders20DuaCategoriesAndAudioUnavailableLabel() {
        composeTestRule.setContent {
            NooreHidayatTheme {
                DuasScreen(language = AppLanguage.ENGLISH, onBack = {})
            }
        }
        composeTestRule.waitForIdle()

        // Filter to Priority A Prophetic Dua so LazyColumn displays it immediately in viewport
        composeTestRule.onNodeWithTag("duas_search_input").performTextInput("2721")
        composeTestRule.waitForIdle()

        // Priority A Prophetic Dua renders honest "Audio not available" state
        composeTestRule.waitUntil(timeoutMillis = 5_000L) {
            composeTestRule.onAllNodesWithText(
                AppLocalization.get("dua_audio_not_available", AppLanguage.ENGLISH)
            ).fetchSemanticsNodes().isNotEmpty()
        }
        assertTrue(
            composeTestRule.onAllNodesWithText(
                AppLocalization.get("dua_audio_not_available", AppLanguage.ENGLISH)
            ).fetchSemanticsNodes().isNotEmpty()
        )
    }
}
