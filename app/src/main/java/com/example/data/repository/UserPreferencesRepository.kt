package com.example.data.repository

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.example.data.model.AppLanguage
import com.example.data.model.AppSettingsState
import com.example.data.model.AppTheme
import com.example.data.model.AppearanceMode
import com.example.data.model.ArabicFontOption
import com.example.data.model.AyahLayoutMode
import com.example.data.model.BookmarkItem
import com.example.data.model.BookmarkType
import com.example.data.model.CardAppearanceStyle
import com.example.data.model.DailyGoalLevel
import com.example.data.model.DateFormatOption
import com.example.data.model.NumberFormatOption
import com.example.data.model.UiFontStyleOption
import com.example.data.notification.DailyReminderScheduler
import com.example.data.notification.PrayerNotificationManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.security.MessageDigest
import java.security.SecureRandom

data class LastReadHadith(
    val collection: String,
    val bookNumber: Int,
    val hadithId: String,
    val hadithNumber: Int,
    val bookTitle: String,
    val timestamp: Long = System.currentTimeMillis()
)

data class StorageBreakdown(
    val cacheBytes: Long,
    val quranAudioBytes: Long,
    val podcastBytes: Long,
    val hadithBytes: Long,
    val databaseBytes: Long
) {
    val offlineContentTotalBytes: Long
        get() = quranAudioBytes + podcastBytes + hadithBytes
}

/**
 * Process-wide reactive holder so every screen and repository observes identical preference changes
 * without stale snapshots across different UserPreferencesRepository(context) instances.
 */
internal object CentralSettingsHub {
    private val lock = Any()
    private val _appSettings = MutableStateFlow(AppSettingsState())
    val appSettings: StateFlow<AppSettingsState> = _appSettings.asStateFlow()

    private val _language = MutableStateFlow(AppLanguage.ENGLISH)
    val language: StateFlow<AppLanguage> = _language.asStateFlow()

    private val _theme = MutableStateFlow(AppTheme.EMERALD_GOLD)
    val theme: StateFlow<AppTheme> = _theme.asStateFlow()

    private val _arabicFontSize = MutableStateFlow(24f)
    val arabicFontSize: StateFlow<Float> = _arabicFontSize.asStateFlow()

    private val _selectedReciterId = MutableStateFlow("mishary")
    val selectedReciterId: StateFlow<String> = _selectedReciterId.asStateFlow()

    private val _notificationsEnabled = MutableStateFlow(true)
    val notificationsEnabled: StateFlow<Boolean> = _notificationsEnabled.asStateFlow()

    fun syncFromPrefs(prefs: SharedPreferences) {
        synchronized(lock) {
            val langCode = prefs.getString("app_language", "en")
            val resolvedLang = AppLanguage.entries.firstOrNull { it.code == langCode } ?: AppLanguage.ENGLISH
            val resolvedTheme = AppTheme.fromId(prefs.getString("app_theme", "emerald_gold"))
            val arabicSize = prefs.getFloat("arabic_font_size", 24f).coerceIn(18f, 36f)
            val reciter = prefs.getString("selected_reciter_id", "mishary") ?: "mishary"
            val notifEnabled = prefs.getBoolean("notifications_enabled", true)
            val customAccent = if (prefs.contains("custom_accent_hex")) {
                prefs.getLong("custom_accent_hex", 0L).takeIf { it != 0L }
            } else null

            val snapshot = AppSettingsState(
                language = resolvedLang,
                dateFormat = DateFormatOption.fromId(prefs.getString("date_format", DateFormatOption.DMY.id)),
                numberFormat = NumberFormatOption.fromId(prefs.getString("number_format", NumberFormatOption.WESTERN.id)),
                showTimezoneLabel = prefs.getBoolean("show_timezone_label", true),
                theme = resolvedTheme,
                appearanceMode = AppearanceMode.fromId(prefs.getString("appearance_mode", AppearanceMode.SYSTEM.id)),
                customAccentHex = customAccent,
                uiTextScale = prefs.getFloat("ui_text_scale", 1.0f).coerceIn(0.85f, 1.35f),
                uiFontStyle = UiFontStyleOption.fromId(prefs.getString("ui_font_style", UiFontStyleOption.DEFAULT.id)),
                cardStyle = CardAppearanceStyle.fromId(prefs.getString("card_appearance_style", CardAppearanceStyle.ELEVATED.id)),
                showHomePrayerCard = prefs.getBoolean("show_home_prayer_card", true),
                arabicFontSize = arabicSize,
                arabicFontOption = ArabicFontOption.fromId(prefs.getString("arabic_font_option", ArabicFontOption.AMIRI.id)),
                translationFontSize = prefs.getFloat("translation_font_size", 15f).coerceIn(12f, 22f),
                ayahLayoutMode = AyahLayoutMode.fromId(prefs.getString("ayah_layout_mode", AyahLayoutMode.COMFORTABLE.id)),
                showInlineTranslation = prefs.getBoolean("show_inline_translation", true),
                defaultTafseerScholarId = prefs.getString("default_tafseer_scholar_id", "") ?: "",
                selectedReciterId = reciter,
                audioPlaybackSpeed = prefs.getFloat("audio_playback_speed", 1.0f).coerceIn(0.75f, 2.0f),
                ayahRepeatCount = prefs.getInt("ayah_repeat_count", 1).coerceIn(1, 10),
                qiblaUseTrueNorth = prefs.getBoolean("qibla_use_true_north", true),
                notificationsEnabled = notifEnabled,
                dailyQuranReminderEnabled = prefs.getBoolean("daily_quran_reminder_enabled", false),
                dailyQuranReminderHour = prefs.getInt("daily_quran_reminder_hour", 7).coerceIn(0, 23),
                dailyQuranReminderMinute = prefs.getInt("daily_quran_reminder_minute", 0).coerceIn(0, 59),
                dailyHadithReminderEnabled = prefs.getBoolean("daily_hadith_reminder_enabled", false),
                dailyDuaReminderEnabled = prefs.getBoolean("daily_dua_reminder_enabled", false),
                morningAdhkarReminderEnabled = prefs.getBoolean("morning_adhkar_reminder_enabled", false),
                eveningAdhkarReminderEnabled = prefs.getBoolean("evening_adhkar_reminder_enabled", false),
                dndEnabled = prefs.getBoolean("dnd_enabled", false),
                dndStartHour = prefs.getInt("dnd_start_hour", 22).coerceIn(0, 23),
                dndEndHour = prefs.getInt("dnd_end_hour", 5).coerceIn(0, 23),
                dailyGoalLevel = DailyGoalLevel.fromId(prefs.getString("daily_learning_goal", DailyGoalLevel.STEADY_15M.id)),
                wifiOnlyDownloads = prefs.getBoolean("wifi_only_downloads", true),
                allowMobileDataStreaming = prefs.getBoolean("allow_mobile_data_streaming", true),
                isPinLockEnabled = !prefs.getString("app_pin_hash_v1", null).isNullOrBlank(),
                highContrastEnabled = prefs.getBoolean("high_contrast_enabled", false),
                reducedMotionEnabled = prefs.getBoolean("reduced_motion_enabled", false)
            )
            _language.value = resolvedLang
            _theme.value = resolvedTheme
            _arabicFontSize.value = arabicSize
            _selectedReciterId.value = reciter
            _notificationsEnabled.value = notifEnabled
            _appSettings.value = snapshot
        }
    }
}

class UserPreferencesRepository(context: Context) {

    private val appContext: Context = context.applicationContext ?: context
    private val prefs: SharedPreferences = appContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    init {
        CentralSettingsHub.syncFromPrefs(prefs)
    }

    val appSettings: StateFlow<AppSettingsState> = CentralSettingsHub.appSettings
    val language: StateFlow<AppLanguage> = CentralSettingsHub.language
    val theme: StateFlow<AppTheme> = CentralSettingsHub.theme
    val arabicFontSize: StateFlow<Float> = CentralSettingsHub.arabicFontSize
    val selectedReciterId: StateFlow<String> = CentralSettingsHub.selectedReciterId
    val notificationsEnabled: StateFlow<Boolean> = CentralSettingsHub.notificationsEnabled

    private val _tasbeehCount = MutableStateFlow(prefs.getInt("tasbeeh_count", 0))
    val tasbeehCount: StateFlow<Int> = _tasbeehCount.asStateFlow()

    private val _tasbeehTarget = MutableStateFlow(prefs.getInt("tasbeeh_target", 33))
    val tasbeehTarget: StateFlow<Int> = _tasbeehTarget.asStateFlow()

    private val _bookmarks = MutableStateFlow(loadPersistedBookmarks())
    val bookmarks: StateFlow<List<BookmarkItem>> = _bookmarks.asStateFlow()

    private val _lastReadHadith = MutableStateFlow(loadLastReadHadith())
    val lastReadHadith: StateFlow<LastReadHadith?> = _lastReadHadith.asStateFlow()

    // --- Section A: Language & Region ---

    fun setLanguage(newLanguage: AppLanguage) {
        prefs.edit().putString("app_language", newLanguage.code).apply()
        CentralSettingsHub.syncFromPrefs(prefs)
    }

    fun setDateFormat(format: DateFormatOption) {
        prefs.edit().putString("date_format", format.id).apply()
        CentralSettingsHub.syncFromPrefs(prefs)
    }

    fun setNumberFormat(format: NumberFormatOption) {
        prefs.edit().putString("number_format", format.id).apply()
        CentralSettingsHub.syncFromPrefs(prefs)
    }

    fun setShowTimezoneLabel(show: Boolean) {
        prefs.edit().putBoolean("show_timezone_label", show).apply()
        CentralSettingsHub.syncFromPrefs(prefs)
    }

    // --- Section B: Appearance & Personalization ---

    fun setTheme(newTheme: AppTheme) {
        prefs.edit().putString("app_theme", newTheme.id).apply()
        CentralSettingsHub.syncFromPrefs(prefs)
    }

    fun setAppearanceMode(mode: AppearanceMode) {
        prefs.edit().putString("appearance_mode", mode.id).apply()
        CentralSettingsHub.syncFromPrefs(prefs)
    }

    fun setCustomAccentHex(colorHex: Long?) {
        val editor = prefs.edit()
        if (colorHex == null || colorHex == 0L) {
            editor.remove("custom_accent_hex")
        } else {
            editor.putLong("custom_accent_hex", colorHex)
        }
        editor.apply()
        CentralSettingsHub.syncFromPrefs(prefs)
    }

    fun setUiTextScale(scale: Float) {
        prefs.edit().putFloat("ui_text_scale", scale.coerceIn(0.85f, 1.35f)).apply()
        CentralSettingsHub.syncFromPrefs(prefs)
    }

    fun setUiFontStyle(style: UiFontStyleOption) {
        prefs.edit().putString("ui_font_style", style.id).apply()
        CentralSettingsHub.syncFromPrefs(prefs)
    }

    fun setCardStyle(style: CardAppearanceStyle) {
        prefs.edit().putString("card_appearance_style", style.id).apply()
        CentralSettingsHub.syncFromPrefs(prefs)
    }

    fun setShowHomePrayerCard(show: Boolean) {
        prefs.edit().putBoolean("show_home_prayer_card", show).apply()
        CentralSettingsHub.syncFromPrefs(prefs)
    }

    fun resetThemeToDefault() {
        prefs.edit()
            .putString("app_theme", AppTheme.EMERALD_GOLD.id)
            .putString("appearance_mode", AppearanceMode.SYSTEM.id)
            .remove("custom_accent_hex")
            .putFloat("ui_text_scale", 1.0f)
            .putString("ui_font_style", UiFontStyleOption.DEFAULT.id)
            .putString("card_appearance_style", CardAppearanceStyle.ELEVATED.id)
            .apply()
        CentralSettingsHub.syncFromPrefs(prefs)
    }

    // --- Section C & D: Quran, Tafseer & Reading ---

    fun setArabicFontSize(size: Float) {
        prefs.edit().putFloat("arabic_font_size", size.coerceIn(18f, 36f)).apply()
        CentralSettingsHub.syncFromPrefs(prefs)
    }

    fun setArabicFontOption(option: ArabicFontOption) {
        prefs.edit().putString("arabic_font_option", option.id).apply()
        CentralSettingsHub.syncFromPrefs(prefs)
    }

    fun setTranslationFontSize(size: Float) {
        prefs.edit().putFloat("translation_font_size", size.coerceIn(12f, 22f)).apply()
        CentralSettingsHub.syncFromPrefs(prefs)
    }

    fun setAyahLayoutMode(mode: AyahLayoutMode) {
        prefs.edit().putString("ayah_layout_mode", mode.id).apply()
        CentralSettingsHub.syncFromPrefs(prefs)
    }

    fun setShowInlineTranslation(show: Boolean) {
        prefs.edit().putBoolean("show_inline_translation", show).apply()
        CentralSettingsHub.syncFromPrefs(prefs)
    }

    fun setDefaultTafseerScholarId(scholarId: String) {
        prefs.edit().putString("default_tafseer_scholar_id", scholarId).apply()
        CentralSettingsHub.syncFromPrefs(prefs)
    }

    // --- Section E: Audio & Playback ---

    fun setReciter(reciterId: String) {
        prefs.edit().putString("selected_reciter_id", reciterId).apply()
        CentralSettingsHub.syncFromPrefs(prefs)
    }

    fun setAudioPlaybackSpeed(speed: Float) {
        val clamped = speed.coerceIn(0.75f, 2.0f)
        prefs.edit().putFloat("audio_playback_speed", clamped).apply()
        QuranAudioManager.playbackSpeed.floatValue = clamped
        CentralSettingsHub.syncFromPrefs(prefs)
    }

    fun setAyahRepeatCount(repeat: Int) {
        val clamped = repeat.coerceIn(1, 10)
        prefs.edit().putInt("ayah_repeat_count", clamped).apply()
        QuranAudioManager.targetLoopCount.intValue = clamped
        CentralSettingsHub.syncFromPrefs(prefs)
    }

    // --- Section F: Qibla Preferences ---

    fun setQiblaUseTrueNorth(useTrueNorth: Boolean) {
        prefs.edit().putBoolean("qibla_use_true_north", useTrueNorth).apply()
        CentralSettingsHub.syncFromPrefs(prefs)
    }

    // --- Section G: Notifications, Daily Reminders & Quiet Hours ---

    fun setNotificationsEnabled(enabled: Boolean) {
        prefs.edit().putBoolean("notifications_enabled", enabled).apply()
        CentralSettingsHub.syncFromPrefs(prefs)
        if (!enabled) {
            PrayerNotificationManager.cancelAllAlarms(appContext)
        } else {
            PrayerNotificationManager.rescheduleAllAlarms(appContext, reason = "master_notif_enabled")
        }
        DailyReminderScheduler.rescheduleAll(appContext)
    }

    fun setDailyQuranReminder(enabled: Boolean, hour: Int = appSettings.value.dailyQuranReminderHour, minute: Int = appSettings.value.dailyQuranReminderMinute) {
        prefs.edit()
            .putBoolean("daily_quran_reminder_enabled", enabled)
            .putInt("daily_quran_reminder_hour", hour.coerceIn(0, 23))
            .putInt("daily_quran_reminder_minute", minute.coerceIn(0, 59))
            .apply()
        CentralSettingsHub.syncFromPrefs(prefs)
        DailyReminderScheduler.rescheduleAll(appContext)
    }

    fun setDailyHadithReminder(enabled: Boolean) {
        prefs.edit().putBoolean("daily_hadith_reminder_enabled", enabled).apply()
        CentralSettingsHub.syncFromPrefs(prefs)
        DailyReminderScheduler.rescheduleAll(appContext)
    }

    fun setDailyDuaReminder(enabled: Boolean) {
        prefs.edit().putBoolean("daily_dua_reminder_enabled", enabled).apply()
        CentralSettingsHub.syncFromPrefs(prefs)
        DailyReminderScheduler.rescheduleAll(appContext)
    }

    fun setMorningAdhkarReminder(enabled: Boolean) {
        prefs.edit().putBoolean("morning_adhkar_reminder_enabled", enabled).apply()
        CentralSettingsHub.syncFromPrefs(prefs)
        DailyReminderScheduler.rescheduleAll(appContext)
    }

    fun setEveningAdhkarReminder(enabled: Boolean) {
        prefs.edit().putBoolean("evening_adhkar_reminder_enabled", enabled).apply()
        CentralSettingsHub.syncFromPrefs(prefs)
        DailyReminderScheduler.rescheduleAll(appContext)
    }

    fun setDndQuietHours(enabled: Boolean, startHour: Int = appSettings.value.dndStartHour, endHour: Int = appSettings.value.dndEndHour) {
        prefs.edit()
            .putBoolean("dnd_enabled", enabled)
            .putInt("dnd_start_hour", startHour.coerceIn(0, 23))
            .putInt("dnd_end_hour", endHour.coerceIn(0, 23))
            .apply()
        CentralSettingsHub.syncFromPrefs(prefs)
    }

    // --- Section H: Learning & Daily Routine ---

    fun setDailyGoalLevel(level: DailyGoalLevel) {
        prefs.edit().putString("daily_learning_goal", level.id).apply()
        CentralSettingsHub.syncFromPrefs(prefs)
    }

    // --- Section I: Internet & Storage ---

    fun setWifiOnlyDownloads(enabled: Boolean) {
        prefs.edit().putBoolean("wifi_only_downloads", enabled).apply()
        CentralSettingsHub.syncFromPrefs(prefs)
    }

    fun setAllowMobileDataStreaming(enabled: Boolean) {
        prefs.edit().putBoolean("allow_mobile_data_streaming", enabled).apply()
        CentralSettingsHub.syncFromPrefs(prefs)
    }

    fun getStorageBreakdown(): StorageBreakdown {
        return StorageBreakdown(
            cacheBytes = dirSizeBytes(appContext.cacheDir),
            quranAudioBytes = dirSizeBytes(File(appContext.filesDir, "quran_offline_audio")),
            podcastBytes = dirSizeBytes(File(appContext.filesDir, "podcasts")),
            hadithBytes = dirSizeBytes(File(appContext.filesDir, "hadith_offline")),
            databaseBytes = dirSizeBytes(appContext.getDatabasePath("noor_database").parentFile)
        )
    }

    /**
     * Clears ONLY temporary files in Context.cacheDir while strictly preserving
     * persistent offline downloads (Quran audio, Podcasts, Hadith packs), Room DB, Bookmarks, and Learning Progress.
     */
    fun clearAppCacheSafe(): Long {
        val before = dirSizeBytes(appContext.cacheDir)
        deleteChildrenRecursively(appContext.cacheDir)
        val after = dirSizeBytes(appContext.cacheDir)
        return (before - after).coerceAtLeast(0L)
    }

    // --- Section J: Privacy, Security & Resets ---

    fun setAppPin(pin: String?) {
        val editor = prefs.edit()
        if (pin.isNullOrBlank()) {
            editor.remove("app_pin_salt_v1").remove("app_pin_hash_v1")
        } else {
            val salt = ByteArray(16).also { SecureRandom().nextBytes(it) }.joinToString("") { "%02x".format(it) }
            val hash = hashPin(pin, salt)
            editor.putString("app_pin_salt_v1", salt).putString("app_pin_hash_v1", hash)
        }
        editor.apply()
        CentralSettingsHub.syncFromPrefs(prefs)
    }

    fun verifyAppPin(pin: String): Boolean {
        val salt = prefs.getString("app_pin_salt_v1", null) ?: return true
        val storedHash = prefs.getString("app_pin_hash_v1", null) ?: return true
        return hashPin(pin, salt) == storedHash
    }

    fun clearReadingHistory() {
        _recentViewedDuaIds.value = emptyList()
        _recentPlayedDuaIds.value = emptyList()
        _lastReadHadith.value = null
        prefs.edit()
            .remove("recent_viewed_dua_ids")
            .remove("recent_played_dua_ids")
            .remove(KEY_LAST_READ_HADITH)
            .apply()
    }

    /**
     * Resets ONLY UI, theme, reading, audio, reminder, network, and accessibility preferences
     * back to defaults — strictly preserving Bookmarks, Favorite Duas, Tasbeeh counts,
     * downloaded offline audio/podcasts/hadith files, Room DB content, and Learning Academy progress.
     */
    fun resetPreferencesOnly() {
        prefs.edit()
            .remove("app_language")
            .remove("date_format")
            .remove("number_format")
            .remove("show_timezone_label")
            .remove("app_theme")
            .remove("appearance_mode")
            .remove("custom_accent_hex")
            .remove("ui_text_scale")
            .remove("ui_font_style")
            .remove("card_appearance_style")
            .remove("show_home_prayer_card")
            .remove("arabic_font_size")
            .remove("arabic_font_option")
            .remove("translation_font_size")
            .remove("ayah_layout_mode")
            .remove("show_inline_translation")
            .remove("default_tafseer_scholar_id")
            .remove("selected_reciter_id")
            .remove("audio_playback_speed")
            .remove("ayah_repeat_count")
            .remove("qibla_use_true_north")
            .remove("notifications_enabled")
            .remove("daily_quran_reminder_enabled")
            .remove("daily_quran_reminder_hour")
            .remove("daily_quran_reminder_minute")
            .remove("daily_hadith_reminder_enabled")
            .remove("daily_dua_reminder_enabled")
            .remove("morning_adhkar_reminder_enabled")
            .remove("evening_adhkar_reminder_enabled")
            .remove("dnd_enabled")
            .remove("dnd_start_hour")
            .remove("dnd_end_hour")
            .remove("daily_learning_goal")
            .remove("wifi_only_downloads")
            .remove("allow_mobile_data_streaming")
            .remove("high_contrast_enabled")
            .remove("reduced_motion_enabled")
            .apply()
        CentralSettingsHub.syncFromPrefs(prefs)
        PrayerNotificationManager.rescheduleAllAlarms(appContext, reason = "reset_prefs_only")
        DailyReminderScheduler.rescheduleAll(appContext)
    }

    // --- Section K: Accessibility ---

    fun setHighContrastEnabled(enabled: Boolean) {
        prefs.edit().putBoolean("high_contrast_enabled", enabled).apply()
        CentralSettingsHub.syncFromPrefs(prefs)
    }

    fun setReducedMotionEnabled(enabled: Boolean) {
        prefs.edit().putBoolean("reduced_motion_enabled", enabled).apply()
        CentralSettingsHub.syncFromPrefs(prefs)
    }

    // --- Existing Tasbeeh / Bookmarks / Hadiths / Duas Features ---

    fun incrementTasbeeh() {
        val next = _tasbeehCount.value + 1
        _tasbeehCount.value = next
        prefs.edit().putInt("tasbeeh_count", next).apply()
    }

    fun resetTasbeeh() {
        _tasbeehCount.value = 0
        prefs.edit().putInt("tasbeeh_count", 0).apply()
    }

    fun setTasbeehTarget(target: Int) {
        _tasbeehTarget.value = target
        prefs.edit().putInt("tasbeeh_target", target).apply()
    }

    private fun isSameBookmark(a: BookmarkItem, b: BookmarkItem): Boolean {
        if (a.id == b.id) return true
        if (a.surahNumber != null && b.surahNumber != null &&
            a.surahNumber == b.surahNumber && a.ayahNumber == b.ayahNumber
        ) return true
        if (!a.hadithId.isNullOrBlank() && a.hadithId == b.hadithId) return true
        if (!a.duaId.isNullOrBlank() && a.duaId == b.duaId) return true
        return false
    }

    fun toggleBookmark(item: BookmarkItem): Boolean {
        val current = _bookmarks.value.toMutableList()
        val exists = current.any { isSameBookmark(it, item) }
        val added: Boolean
        if (exists) {
            current.removeAll { isSameBookmark(it, item) }
            added = false
        } else {
            current.add(0, item)
            added = true
        }
        _bookmarks.value = current
        savePersistedBookmarks(current)
        return added
    }

    fun removeBookmarkById(id: String) {
        val current = _bookmarks.value.filterNot { it.id == id }
        _bookmarks.value = current
        savePersistedBookmarks(current)
    }

    fun reloadBookmarksFromPrefs() {
        _bookmarks.value = loadPersistedBookmarks()
    }

    fun isBookmarked(surahNumber: Int, ayahNumber: Int): Boolean {
        return _bookmarks.value.any { it.surahNumber == surahNumber && it.ayahNumber == ayahNumber }
    }

    fun isHadithBookmarked(hadithId: String): Boolean {
        return _bookmarks.value.any { it.hadithId == hadithId }
    }

    fun isDuaBookmarked(duaId: String): Boolean {
        return _bookmarks.value.any { it.duaId == duaId }
    }

    private fun loadPersistedBookmarks(): List<BookmarkItem> {
        val raw = prefs.getString(KEY_BOOKMARKS_JSON, null)
        if (raw == null) {
            val defaults = listOf(
                BookmarkItem(
                    id = "bm_1",
                    type = BookmarkType.QURAN,
                    title = "Surah Al-Fatihah, Ayah 1",
                    subtitle = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
                    surahNumber = 1,
                    ayahNumber = 1
                ),
                BookmarkItem(
                    id = "bm_2",
                    type = BookmarkType.HADITH,
                    title = "Sahih al-Bukhari, Hadith 1",
                    subtitle = "إِنَّمَا الأَعْمَالُ بِالنِّيَّاتِ",
                    hadithId = "bukhari_1"
                ),
                BookmarkItem(
                    id = "bm_3",
                    type = BookmarkType.DUA,
                    title = "Sayyid al-Istighfar",
                    subtitle = "اللَّهُمَّ أَنْتَ رَبِّي لَا إِلَهَ إِلَّا أَنْتَ",
                    duaId = "dua_sayyidul_istighfar"
                )
            )
            savePersistedBookmarks(defaults)
            return defaults
        }

        return try {
            val arr = JSONArray(raw)
            val list = ArrayList<BookmarkItem>(arr.length())
            for (i in 0 until arr.length()) {
                val o = arr.optJSONObject(i) ?: continue
                val typeStr = o.optString("type", BookmarkType.QURAN.name)
                val bmType = BookmarkType.entries.firstOrNull { it.name == typeStr } ?: BookmarkType.QURAN
                list.add(
                    BookmarkItem(
                        id = o.optString("id"),
                        type = bmType,
                        title = o.optString("title"),
                        subtitle = o.optString("subtitle"),
                        surahNumber = if (o.has("surahNumber") && !o.isNull("surahNumber")) o.optInt("surahNumber") else null,
                        ayahNumber = if (o.has("ayahNumber") && !o.isNull("ayahNumber")) o.optInt("ayahNumber") else null,
                        hadithId = o.optString("hadithId").ifBlank { null },
                        duaId = o.optString("duaId").ifBlank { null },
                        timestamp = o.optLong("timestamp", System.currentTimeMillis())
                    )
                )
            }
            list
        } catch (_: Exception) {
            emptyList()
        }
    }

    private fun savePersistedBookmarks(items: List<BookmarkItem>) {
        val arr = JSONArray()
        items.forEach { item ->
            val o = JSONObject().apply {
                put("id", item.id)
                put("type", item.type.name)
                put("title", item.title)
                put("subtitle", item.subtitle)
                if (item.surahNumber != null) put("surahNumber", item.surahNumber)
                if (item.ayahNumber != null) put("ayahNumber", item.ayahNumber)
                if (!item.hadithId.isNullOrBlank()) put("hadithId", item.hadithId)
                if (!item.duaId.isNullOrBlank()) put("duaId", item.duaId)
                put("timestamp", item.timestamp)
            }
            arr.put(o)
        }
        prefs.edit().putString(KEY_BOOKMARKS_JSON, arr.toString()).apply()
    }

    fun saveLastReadHadith(
        collection: String,
        bookNumber: Int,
        hadithId: String,
        hadithNumber: Int,
        bookTitle: String
    ) {
        val entry = LastReadHadith(
            collection = collection,
            bookNumber = bookNumber,
            hadithId = hadithId,
            hadithNumber = hadithNumber,
            bookTitle = bookTitle,
            timestamp = System.currentTimeMillis()
        )
        _lastReadHadith.value = entry
        val json = JSONObject().apply {
            put("collection", collection)
            put("bookNumber", bookNumber)
            put("hadithId", hadithId)
            put("hadithNumber", hadithNumber)
            put("bookTitle", bookTitle)
            put("timestamp", entry.timestamp)
        }.toString()
        prefs.edit().putString(KEY_LAST_READ_HADITH, json).apply()
    }

    private fun loadLastReadHadith(): LastReadHadith? {
        val raw = prefs.getString(KEY_LAST_READ_HADITH, null) ?: return null
        return try {
            val o = JSONObject(raw)
            LastReadHadith(
                collection = o.optString("collection"),
                bookNumber = o.optInt("bookNumber", 1),
                hadithId = o.optString("hadithId"),
                hadithNumber = o.optInt("hadithNumber", 1),
                bookTitle = o.optString("bookTitle"),
                timestamp = o.optLong("timestamp", 0L)
            )
        } catch (_: Exception) {
            null
        }
    }

    private val _favoriteDuaIds = MutableStateFlow<Set<String>>(
        prefs.getStringSet("favorite_dua_ids", setOf("dua_sayyidul_istighfar", "dua_rabbana_atina_dunya"))
            ?: setOf("dua_sayyidul_istighfar", "dua_rabbana_atina_dunya")
    )
    val favoriteDuaIds: StateFlow<Set<String>> = _favoriteDuaIds.asStateFlow()

    private val _recentViewedDuaIds = MutableStateFlow<List<String>>(
        prefs.getString("recent_viewed_dua_ids", "dua_sayyidul_istighfar,dua_rabbana_atina_dunya")
            ?.split(",")?.filter { it.isNotBlank() } ?: emptyList()
    )
    val recentViewedDuaIds: StateFlow<List<String>> = _recentViewedDuaIds.asStateFlow()

    private val _recentPlayedDuaIds = MutableStateFlow<List<String>>(
        prefs.getString("recent_played_dua_ids", "")
            ?.split(",")?.filter { it.isNotBlank() } ?: emptyList()
    )
    val recentPlayedDuaIds: StateFlow<List<String>> = _recentPlayedDuaIds.asStateFlow()

    fun toggleFavoriteDua(id: String): Boolean {
        val current = _favoriteDuaIds.value.toMutableSet()
        val isAdded: Boolean
        if (current.contains(id)) {
            current.remove(id)
            isAdded = false
        } else {
            current.add(id)
            isAdded = true
        }
        _favoriteDuaIds.value = current
        prefs.edit().putStringSet("favorite_dua_ids", current).apply()
        return isAdded
    }

    fun isFavoriteDua(id: String): Boolean {
        return _favoriteDuaIds.value.contains(id)
    }

    fun recordViewedDua(id: String) {
        val list = _recentViewedDuaIds.value.toMutableList()
        list.remove(id)
        list.add(0, id)
        val trimmed = list.take(20)
        _recentViewedDuaIds.value = trimmed
        prefs.edit().putString("recent_viewed_dua_ids", trimmed.joinToString(",")).apply()
    }

    fun recordPlayedDua(id: String) {
        val list = _recentPlayedDuaIds.value.toMutableList()
        list.remove(id)
        list.add(0, id)
        val trimmed = list.take(20)
        _recentPlayedDuaIds.value = trimmed
        prefs.edit().putString("recent_played_dua_ids", trimmed.joinToString(",")).apply()
    }

    private fun hashPin(pin: String, salt: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
            .digest("$salt:$pin:noor_pin_v1".toByteArray(Charsets.UTF_8))
        return digest.joinToString("") { "%02x".format(it) }
    }

    private fun dirSizeBytes(file: File?): Long {
        if (file == null || !file.exists()) return 0L
        if (file.isFile) return file.length()
        return file.listFiles()?.sumOf { dirSizeBytes(it) } ?: 0L
    }

    private fun deleteChildrenRecursively(dir: File?) {
        if (dir == null || !dir.exists()) return
        dir.listFiles()?.forEach { child ->
            if (child.isDirectory) {
                deleteChildrenRecursively(child)
            }
            runCatching { child.delete() }
        }
    }

    companion object {
        const val PREFS_NAME = "noor_e_hidayat_prefs"
        private const val KEY_BOOKMARKS_JSON = "bookmarks_json_v1"
        private const val KEY_LAST_READ_HADITH = "last_read_hadith_v1"

        @Volatile
        var isCellularOnlyOverrideForTest: Boolean? = null

        fun isActiveNetworkCellularOnly(context: Context): Boolean {
            isCellularOnlyOverrideForTest?.let { return it }
            if (Build.FINGERPRINT.equals("robolectric", ignoreCase = true)) {
                return false
            }
            return try {
                val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager ?: return false
                val network = cm.activeNetwork ?: return false
                val caps = cm.getNetworkCapabilities(network) ?: return false
                caps.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) &&
                    !caps.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) &&
                    !caps.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
            } catch (_: Exception) {
                false
            }
        }

        fun isDownloadBlockedByWifiOnly(context: Context): Boolean {
            val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            val wifiOnly = prefs.getBoolean("wifi_only_downloads", true)
            if (!wifiOnly) return false
            return isActiveNetworkCellularOnly(context)
        }

        fun getSavedAudioSpeed(context: Context): Float {
            val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            return prefs.getFloat("audio_playback_speed", 1.0f).coerceIn(0.75f, 2.0f)
        }

        fun getSavedAyahRepeatCount(context: Context): Int {
            val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            return prefs.getInt("ayah_repeat_count", 1).coerceIn(1, 10)
        }
    }
}
