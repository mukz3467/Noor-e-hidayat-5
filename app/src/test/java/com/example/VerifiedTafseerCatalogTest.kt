package com.example

import com.example.data.model.AppLanguage
import com.example.data.repository.VerifiedTafseerCatalog
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class VerifiedTafseerCatalogTest {

    @Test
    fun onlineEditionsCoverAllFourLanguagesAndAreStrictlySeparated() {
        val urdu = VerifiedTafseerCatalog.onlineEditionsFor(AppLanguage.URDU)
        val english = VerifiedTafseerCatalog.onlineEditionsFor(AppLanguage.ENGLISH)
        val hindi = VerifiedTafseerCatalog.onlineEditionsFor(AppLanguage.HINDI)
        val arabic = VerifiedTafseerCatalog.onlineEditionsFor(AppLanguage.ARABIC)

        assertEquals(
            listOf("ur_ibn_kathir", "ur_bayan_ul_quran", "ur_fi_zilal", "ur_saadi", "ur_tazkirul_quran"),
            urdu.map { it.id }
        )
        assertEquals(
            listOf("en_ibn_kathir", "en_maariful_quran", "en_jalalayn", "en_ibn_abbas", "en_tazkirul_quran"),
            english.map { it.id }
        )
        assertEquals(listOf("hi_al_mukhtasar"), hindi.map { it.id })
        assertEquals(
            listOf("ar_ibn_kathir", "ar_tabari", "ar_qurtubi", "ar_baghawi", "ar_saadi", "ar_wasit", "ar_jalalayn"),
            arabic.map { it.id }
        )

        VerifiedTafseerCatalog.onlineEditions.forEach { edition ->
            assertTrue("${edition.id} must declare an official sourceUrl", edition.sourceUrl.startsWith("https://"))
            assertTrue(
                "${edition.id} must configure Quran Foundation ID or spa5k slug",
                !edition.quranFoundationId.isNullOrBlank() || !edition.spa5kSlug.isNullOrBlank()
            )
        }
    }

    @Test
    fun resolveStrictOnlineEditionRefusesCrossLanguageMismatch() {
        assertNotNull(VerifiedTafseerCatalog.resolveStrictOnlineEdition("en_ibn_kathir", AppLanguage.ENGLISH))
        assertNotNull(VerifiedTafseerCatalog.resolveStrictOnlineEdition("ur_ibn_kathir", AppLanguage.URDU))
        assertNotNull(VerifiedTafseerCatalog.resolveStrictOnlineEdition("ar_ibn_kathir", AppLanguage.ARABIC))
        assertNotNull(VerifiedTafseerCatalog.resolveStrictOnlineEdition("hi_al_mukhtasar", AppLanguage.HINDI))

        assertNull(VerifiedTafseerCatalog.resolveStrictOnlineEdition("en_ibn_kathir", AppLanguage.URDU))
        assertNull(VerifiedTafseerCatalog.resolveStrictOnlineEdition("ur_ibn_kathir", AppLanguage.ENGLISH))
        assertNull(VerifiedTafseerCatalog.resolveStrictOnlineEdition("ar_qurtubi", AppLanguage.ENGLISH))
        assertNull(VerifiedTafseerCatalog.resolveStrictOnlineEdition("hi_al_mukhtasar", AppLanguage.ARABIC))
        assertNull(VerifiedTafseerCatalog.resolveStrictOnlineEdition("al_mukhtasar_en", AppLanguage.ENGLISH))
        assertNull(VerifiedTafseerCatalog.resolveStrictOnlineEdition("not-a-real-scholar", AppLanguage.ENGLISH))

        // Safe resolver always snaps back to active language's default online edition
        val snapped = VerifiedTafseerCatalog.resolveOnlineEdition("en_ibn_kathir", AppLanguage.URDU)
        assertEquals(AppLanguage.URDU, snapped.language)
        assertEquals("ur_ibn_kathir", snapped.id)
    }

    @Test
    fun toTafseerSourceEntitiesMarksAllSourcesOnlineOnlyWithOfficialUrls() {
        val entities = VerifiedTafseerCatalog.toTafseerSourceEntities()
        assertEquals(18, entities.size)
        entities.forEach { src ->
            assertFalse("${src.id} must NOT mark offlineAllowed=true", src.offlineAllowed)
            assertTrue("${src.id} must mark onlineAllowed=true", src.onlineAllowed)
            assertFalse("${src.id} must NOT mark isDownloaded=true", src.isDownloaded)
            assertEquals("ONLINE_LIVE_API", src.permissionStatus)
            assertTrue(src.sourceUrl.startsWith("https://"))
        }
    }

    @Test
    fun onlineErrorAyahNoticeAndLanguageDepthNotesAreLocalized() {
        AppLanguage.entries.forEach { language ->
            val err = VerifiedTafseerCatalog.onlineFetchErrorMessage(language, "Ibn Kathir")
            val notice = VerifiedTafseerCatalog.ayahNotIndexedNotice(language, "Ibn Kathir", 2, 255)
            assertFalse(err.isBlank())
            assertFalse(notice.isBlank())
            assertTrue(notice.contains("2:255"))
        }
        assertNotNull(VerifiedTafseerCatalog.languageDepthTransparencyNote(AppLanguage.HINDI))
        assertNull(VerifiedTafseerCatalog.languageDepthTransparencyNote(AppLanguage.URDU))
        assertNull(VerifiedTafseerCatalog.languageDepthTransparencyNote(AppLanguage.ENGLISH))
        assertNull(VerifiedTafseerCatalog.languageDepthTransparencyNote(AppLanguage.ARABIC))
    }
}
