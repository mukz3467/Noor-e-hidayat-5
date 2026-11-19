package com.example.data.ramadan

import com.example.data.repository.HijriDate
import com.example.data.repository.IslamicCalendarRepository
import java.util.Calendar
import java.util.Locale

enum class TimingSource(val id: String) {
    ALADHAN_API("aladhan_api"),
    OFFLINE_ASTRONOMICAL("offline_astronomical")
}

enum class PeriodConfirmationSource(val id: String) {
    CALCULATED_HIJRI("calculated_hijri"),
    LOCALLY_CONFIRMED_START("locally_confirmed_start"),
    MANUAL_FORCE_PREVIEW("manual_force_preview")
}

enum class AlarmSoundType(val id: String, val adhanAudioKey: String?) {
    ADHAN_MAKKAH("adhan_makkah", "makkah"),
    ADHAN_MADINA("adhan_madina", "madina"),
    ADHAN_FAJR("adhan_fajr", "fajr"),
    NOTIFICATION_TONE("notification_tone", null),
    VIBRATE_ONLY("vibrate_only", null),
    SILENT("silent", null);

    companion object {
        fun fromId(id: String?): AlarmSoundType =
            entries.firstOrNull { it.id == id } ?: ADHAN_MAKKAH
    }
}

data class RamadanAlarmConfig(
    val alarmsMasterEnabled: Boolean = true,
    val sehriPreAlarmEnabled: Boolean = true,
    val sehriOffsetMinutes: Int = 30,
    val sehriFajrEndAlarmEnabled: Boolean = true,
    val iftarPreReminderEnabled: Boolean = true,
    val iftarOffsetMinutes: Int = 10,
    val iftarMainAlarmEnabled: Boolean = true,
    val alarmSoundType: AlarmSoundType = AlarmSoundType.ADHAN_MAKKAH,
    val vibrationEnabled: Boolean = true,
    val snoozeMinutes: Int = 5,
    val activeOnlyDuringRamadan: Boolean = true
)

data class DailyRamadanTimingRecord(
    val dateKey: String,
    val timezoneId: String,
    val latitude: Double,
    val longitude: Double,
    val cityName: String,
    val methodName: String,
    val asrJuristic: String,
    val fajr24: String,
    val maghrib24: String,
    val fajrDisplay: String,
    val maghribDisplay: String,
    val fajrTriggerMillis: Long,
    val maghribTriggerMillis: Long,
    val source: TimingSource,
    val lastUpdatedMillis: Long,
    val isEstimateFallback: Boolean = false
) {
    fun isStale(nowMillis: Long = System.currentTimeMillis(), maxAgeMillis: Long = 36L * 3600L * 1000L): Boolean {
        val ageExceeded = lastUpdatedMillis <= 0L || (nowMillis - lastUpdatedMillis) > maxAgeMillis
        return ageExceeded
    }

    fun isForDateKey(expectedDateKey: String): Boolean = dateKey == expectedDateKey
}

data class UpcomingRamadanAlarmInfo(
    val alarmKey: String,
    val title: String,
    val triggerAtMillis: Long,
    val formattedTime: String,
    val isExact: Boolean,
    val requestCode: Int
)

data class RamadanPeriodStatus(
    val isRamadanActive: Boolean,
    val activeSource: PeriodConfirmationSource,
    val currentRamadanDay: Int?,
    val totalRamadanDays: Int,
    val daysUntilRamadan: Int?,
    val activeHijriDate: HijriDate,
    val confirmedStartDateKey: String?,
    val projectedStartDateKey: String
)

enum class FastingStatus(val id: String) {
    NOT_RECORDED("not_recorded"),
    COMPLETED("completed"),
    MISSED("missed"),
    EXCUSED("excused");

    companion object {
        fun fromId(id: String?): FastingStatus =
            entries.firstOrNull { it.id == id } ?: NOT_RECORDED
    }
}

data class FastingDayEntry(
    val dateKey: String,
    val ramadanDay: Int? = null,
    val isVoluntary: Boolean = false,
    val status: FastingStatus = FastingStatus.NOT_RECORDED,
    val note: String = "",
    val updatedAtMillis: Long = 0L
)

enum class QuranTargetMode(val id: String) {
    PAGES("pages"),
    AYAAT("ayaat"),
    SURAHS("surahs");

    companion object {
        fun fromId(id: String?): QuranTargetMode =
            entries.firstOrNull { it.id == id } ?: PAGES
    }
}

data class QuranGoalConfig(
    val khatamGoalEnabled: Boolean = true,
    val targetDays: Int = 30,
    val dailyTargetMode: QuranTargetMode = QuranTargetMode.PAGES,
    val dailyTargetValue: Int = 20,
    val totalMushafPages: Int = 604
)

data class QuranLogEntry(
    val id: String,
    val dateKey: String,
    val pagesRead: Int = 0,
    val ayaatRead: Int = 0,
    val surahNumber: Int? = null,
    val note: String = "",
    val timestampMillis: Long
)

enum class DeedCategory(val id: String) {
    WORSHIP("worship"),
    CHARITY_SADAQAH("charity_sadaqah"),
    FAMILY_KINDNESS("family_kindness"),
    CHARACTER("character");

    companion object {
        fun fromId(id: String?): DeedCategory =
            entries.firstOrNull { it.id == id } ?: WORSHIP
    }
}

data class GoodDeedItem(
    val id: String,
    val title: String,
    val titleKey: String? = null,
    val isCustom: Boolean = false,
    val category: DeedCategory = DeedCategory.WORSHIP
)

data class SadaqahRecord(
    val id: String,
    val dateKey: String,
    val amountText: String,
    val currencyCode: String,
    val recipientOrCause: String,
    val timestampMillis: Long
)

data class NightWorshipConfig(
    val taraweehReminderEnabled: Boolean = true,
    val taraweehHour: Int = 20,
    val taraweehMinute: Int = 30,
    val tahajjudReminderEnabled: Boolean = true,
    val tahajjudOffsetBeforeFajrMinutes: Int = 60,
    val tahajjudUseCustomFixedTime: Boolean = false,
    val tahajjudFixedHour: Int = 3,
    val tahajjudFixedMinute: Int = 30,
    val lastTenNightsReminderEnabled: Boolean = true,
    val lastTenNightsHour: Int = 21,
    val lastTenNightsMinute: Int = 0
)

data class IftarChecklistItem(
    val id: String,
    val title: String,
    val titleKey: String? = null,
    val isCustom: Boolean = false
)

data class RamadanCompanionState(
    val periodStatus: RamadanPeriodStatus,
    val todayTiming: DailyRamadanTimingRecord?,
    val alarmConfig: RamadanAlarmConfig = RamadanAlarmConfig(),
    val upcomingAlarms: List<UpcomingRamadanAlarmInfo> = emptyList(),
    val hasNotificationPermission: Boolean = true,
    val canScheduleExactAlarms: Boolean = true,
    val fastingEntries: Map<String, FastingDayEntry> = emptyMap(),
    val quranGoal: QuranGoalConfig = QuranGoalConfig(),
    val quranLogs: List<QuranLogEntry> = emptyList(),
    val deeds: List<GoodDeedItem> = emptyList(),
    val completedDeedIdsByDate: Map<String, Set<String>> = emptyMap(),
    val sadaqahRecords: List<SadaqahRecord> = emptyList(),
    val nightWorship: NightWorshipConfig = NightWorshipConfig(),
    val checklistItems: List<IftarChecklistItem> = emptyList(),
    val checkedChecklistIdsByDate: Map<String, Set<String>> = emptyMap(),
    val isSyncingTimings: Boolean = false,
    val syncStatusMessage: String? = null
) {
    companion object {
        fun initialPlaceholder(): RamadanCompanionState {
            val nowCal = Calendar.getInstance()
            val todayHijri = IslamicCalendarRepository.getHijriDate(nowCal)
            val projectedStart = IslamicCalendarRepository.findProjectedRamadanStart(nowCal)
            val projectedKey = formatDateKey(projectedStart)
            return RamadanCompanionState(
                periodStatus = RamadanPeriodStatus(
                    isRamadanActive = todayHijri.month == 9,
                    activeSource = PeriodConfirmationSource.CALCULATED_HIJRI,
                    currentRamadanDay = if (todayHijri.month == 9) todayHijri.day else null,
                    totalRamadanDays = 30,
                    daysUntilRamadan = null,
                    activeHijriDate = todayHijri,
                    confirmedStartDateKey = null,
                    projectedStartDateKey = projectedKey
                ),
                todayTiming = null
            )
        }
    }
}

fun formatDateKey(cal: Calendar): String {
    return String.format(
        Locale.US,
        "%04d-%02d-%02d",
        cal.get(Calendar.YEAR),
        cal.get(Calendar.MONTH) + 1,
        cal.get(Calendar.DAY_OF_MONTH)
    )
}

fun parseDateKeyToCalendar(dateKey: String, timeZoneId: String? = null): Calendar? {
    val parts = dateKey.trim().split("-")
    if (parts.size != 3) return null
    val y = parts[0].toIntOrNull() ?: return null
    val m = parts[1].toIntOrNull() ?: return null
    val d = parts[2].toIntOrNull() ?: return null
    if (m !in 1..12 || d !in 1..31) return null
    return Calendar.getInstance().apply {
        if (!timeZoneId.isNullOrBlank()) {
            timeZone = java.util.TimeZone.getTimeZone(timeZoneId)
        }
        set(Calendar.YEAR, y)
        set(Calendar.MONTH, m - 1)
        set(Calendar.DAY_OF_MONTH, d)
        set(Calendar.HOUR_OF_DAY, 12)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }
}
