package com.example.data.ramadan

import android.content.Context
import com.example.data.location.AppLocationManager
import com.example.data.notification.PrayerNotificationManager
import com.example.data.repository.IslamicCalendarRepository
import com.example.data.repository.PrayerCalculator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.util.Calendar
import java.util.TimeZone
import java.util.UUID

object RamadanRepository {

    private const val PREFS_NAME = "ramadan_companion_prefs"
    private const val KEY_ALARM_CONFIG = "alarm_config_json"
    private const val KEY_TIMING_CACHE = "timing_cache_json"
    private const val KEY_CONFIRMED_START = "confirmed_start_date_key"
    private const val KEY_CONFIRMED_DAYS = "confirmed_total_days"
    private const val KEY_FASTING_ENTRIES = "fasting_entries_json"
    private const val KEY_QURAN_GOAL = "quran_goal_json"
    private const val KEY_QURAN_LOGS = "quran_logs_json"
    private const val KEY_CUSTOM_DEEDS = "custom_deeds_json"
    private const val KEY_COMPLETED_DEEDS = "completed_deeds_json"
    private const val KEY_SADAQAH_RECORDS = "sadaqah_records_json"
    private const val KEY_NIGHT_WORSHIP = "night_worship_json"
    private const val KEY_CHECKLIST_ITEMS = "checklist_items_json"
    private const val KEY_CHECKED_CHECKLIST = "checked_checklist_json"

    val DEFAULT_DEEDS: List<GoodDeedItem> = listOf(
        GoodDeedItem("deed_prayers_on_time", "Prayed all 5 daily Salah on time", "ram_deed_5_salah", false, DeedCategory.WORSHIP),
        GoodDeedItem("deed_taraweeh_qiyam", "Offered Taraweeh / Night Prayer", "ram_deed_taraweeh", false, DeedCategory.WORSHIP),
        GoodDeedItem("deed_quran_tilawat", "Read daily Quran portion", "ram_deed_quran", false, DeedCategory.WORSHIP),
        GoodDeedItem("deed_adhkar", "Morning & evening Adhkar / Istighfar", "ram_deed_adhkar", false, DeedCategory.WORSHIP),
        GoodDeedItem("deed_charity", "Gave charity / fed a fasting person", "ram_deed_charity", false, DeedCategory.CHARITY_SADAQAH),
        GoodDeedItem("deed_parents_family", "Called or helped parents & family", "ram_deed_family", false, DeedCategory.FAMILY_KINDNESS),
        GoodDeedItem("deed_patience", "Guarded tongue, patience & kindness", "ram_deed_patience", false, DeedCategory.CHARACTER)
    )

    val DEFAULT_CHECKLIST: List<IftarChecklistItem> = listOf(
        IftarChecklistItem("chk_dates", "Dates (Tamr) & Fresh Water", "ram_chk_dates", false),
        IftarChecklistItem("chk_dua_before_maghrib", "10 mins silent Dua before Maghrib", "ram_chk_dua", false),
        IftarChecklistItem("chk_share_iftar", "Share a meal with family or neighbors", "ram_chk_share", false),
        IftarChecklistItem("chk_maghrib_ready", "Prepare Wudu & prayer mat for Maghrib", "ram_chk_wudu", false)
    )

    private val _state = MutableStateFlow(RamadanCompanionState.initialPlaceholder())
    val state: StateFlow<RamadanCompanionState> = _state.asStateFlow()

    @Volatile
    private var isInitialized = false

    fun resetForTest() {
        isInitialized = false
        _state.value = RamadanCompanionState.initialPlaceholder()
    }

    fun init(context: Context, nowMillis: Long = System.currentTimeMillis()) {
        AppLocationManager.init(context)
        PrayerCalculator.init(context)
        IslamicCalendarRepository.init(context)
        PrayerNotificationManager.init(context)

        if (!isInitialized) {
            isInitialized = true
            loadAndBuildState(context, nowMillis)
        } else {
            refreshPermissionAndPeriodOnly(context, nowMillis)
        }
    }

    fun evaluatePeriodForCalendar(
        cal: Calendar,
        confirmedStartDateKey: String?,
        confirmedTotalDays: Int = 30
    ): RamadanPeriodStatus {
        val hijri = IslamicCalendarRepository.getHijriDate(cal)
        val projectedStart = IslamicCalendarRepository.findProjectedRamadanStart(cal)
        val projectedKey = formatDateKey(projectedStart)
        val safeTotal = confirmedTotalDays.coerceIn(29, 30)

        if (!confirmedStartDateKey.isNullOrBlank()) {
            val startCal = parseDateKeyToCalendar(confirmedStartDateKey, cal.timeZone.id)
            if (startCal != null) {
                val targetMidday = (cal.clone() as Calendar).apply {
                    set(Calendar.HOUR_OF_DAY, 12); set(Calendar.MINUTE, 0); set(Calendar.SECOND, 0); set(Calendar.MILLISECOND, 0)
                }
                val startMidday = (startCal.clone() as Calendar).apply {
                    set(Calendar.HOUR_OF_DAY, 12); set(Calendar.MINUTE, 0); set(Calendar.SECOND, 0); set(Calendar.MILLISECOND, 0)
                }
                val diffDays = ((targetMidday.timeInMillis - startMidday.timeInMillis) / (24L * 3600L * 1000L)).toInt()
                if (diffDays in 0 until safeTotal) {
                    val rDay = diffDays + 1
                    val adjustedHijri = hijri.copy(
                        day = rDay,
                        month = 9,
                        monthNameEn = "Ramadan",
                        monthNameUr = IslamicCalendarRepository.HIJRI_MONTHS_UR[8],
                        monthNameAr = IslamicCalendarRepository.HIJRI_MONTHS_AR[8],
                        monthNameHi = IslamicCalendarRepository.HIJRI_MONTHS_HI[8],
                        isConfirmedByRuetHilal = true,
                        isEstimate = false,
                        sourceDescription = "Confirmed Local Ramadan Start ($confirmedStartDateKey)"
                    )
                    return RamadanPeriodStatus(
                        isRamadanActive = true,
                        activeSource = PeriodConfirmationSource.LOCALLY_CONFIRMED_START,
                        currentRamadanDay = rDay,
                        totalRamadanDays = safeTotal,
                        daysUntilRamadan = null,
                        activeHijriDate = adjustedHijri,
                        confirmedStartDateKey = confirmedStartDateKey,
                        projectedStartDateKey = projectedKey
                    )
                } else if (diffDays < 0) {
                    return RamadanPeriodStatus(
                        isRamadanActive = false,
                        activeSource = PeriodConfirmationSource.LOCALLY_CONFIRMED_START,
                        currentRamadanDay = null,
                        totalRamadanDays = safeTotal,
                        daysUntilRamadan = -diffDays,
                        activeHijriDate = hijri,
                        confirmedStartDateKey = confirmedStartDateKey,
                        projectedStartDateKey = confirmedStartDateKey
                    )
                } else {
                    // Confirmed Ramadan has already ended
                    return RamadanPeriodStatus(
                        isRamadanActive = false,
                        activeSource = PeriodConfirmationSource.CALCULATED_HIJRI,
                        currentRamadanDay = null,
                        totalRamadanDays = safeTotal,
                        daysUntilRamadan = computeDaysBetween(cal, projectedStart).coerceAtLeast(1),
                        activeHijriDate = hijri,
                        confirmedStartDateKey = confirmedStartDateKey,
                        projectedStartDateKey = projectedKey
                    )
                }
            }
        }

        val isRamadan = hijri.month == 9
        val daysUntil = if (isRamadan) null else computeDaysBetween(cal, projectedStart).coerceAtLeast(0)
        return RamadanPeriodStatus(
            isRamadanActive = isRamadan,
            activeSource = PeriodConfirmationSource.CALCULATED_HIJRI,
            currentRamadanDay = if (isRamadan) hijri.day.coerceIn(1, 30) else null,
            totalRamadanDays = 30,
            daysUntilRamadan = daysUntil,
            activeHijriDate = hijri,
            confirmedStartDateKey = null,
            projectedStartDateKey = projectedKey
        )
    }

    private fun computeDaysBetween(fromCal: Calendar, toCal: Calendar): Int {
        val c1 = (fromCal.clone() as Calendar).apply {
            set(Calendar.HOUR_OF_DAY, 12); set(Calendar.MINUTE, 0); set(Calendar.SECOND, 0); set(Calendar.MILLISECOND, 0)
        }
        val c2 = (toCal.clone() as Calendar).apply {
            set(Calendar.HOUR_OF_DAY, 12); set(Calendar.MINUTE, 0); set(Calendar.SECOND, 0); set(Calendar.MILLISECOND, 0)
        }
        return ((c2.timeInMillis - c1.timeInMillis) / (24L * 3600L * 1000L)).toInt()
    }

    private fun loadAndBuildState(context: Context, nowMillis: Long) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val location = AppLocationManager.currentLocation.value
        val calcConfig = PrayerCalculator.calcConfig.value
        val tz = TimeZone.getTimeZone(location.timeZoneId)
        val nowCal = Calendar.getInstance(tz).apply { timeInMillis = nowMillis }

        val alarmConfig = decodeAlarmConfig(prefs.getString(KEY_ALARM_CONFIG, null))
        val nightWorship = decodeNightWorship(prefs.getString(KEY_NIGHT_WORSHIP, null))
        val confirmedStart = prefs.getString(KEY_CONFIRMED_START, null)
        val confirmedDays = prefs.getInt(KEY_CONFIRMED_DAYS, 30).coerceIn(29, 30)
        val periodStatus = evaluatePeriodForCalendar(nowCal, confirmedStart, confirmedDays)

        val cachedTiming = decodeTimingCache(prefs.getString(KEY_TIMING_CACHE, null))
        val todayKey = formatDateKey(nowCal)
        val effectiveTiming = if (
            cachedTiming != null &&
            cachedTiming.isForDateKey(todayKey) &&
            cachedTiming.timezoneId == location.timeZoneId &&
            kotlin.math.abs(cachedTiming.latitude - location.latitude) < 0.01 &&
            kotlin.math.abs(cachedTiming.longitude - location.longitude) < 0.01 &&
            cachedTiming.methodName == calcConfig.method.displayName
        ) {
            cachedTiming
        } else {
            val computed = RamadanPrayerTimingSync.computeOfflineTiming(location, calcConfig, nowCal, nowMillis)
            saveTimingCache(prefs, computed)
            computed
        }

        val fasting = decodeFastingMap(prefs.getString(KEY_FASTING_ENTRIES, null))
        val quranGoal = decodeQuranGoal(prefs.getString(KEY_QURAN_GOAL, null))
        val quranLogs = decodeQuranLogs(prefs.getString(KEY_QURAN_LOGS, null))
        val customDeeds = decodeDeedList(prefs.getString(KEY_CUSTOM_DEEDS, null))
        val allDeeds = DEFAULT_DEEDS + customDeeds
        val completedDeeds = decodeStringSetMap(prefs.getString(KEY_COMPLETED_DEEDS, null))
        val sadaqah = decodeSadaqahList(prefs.getString(KEY_SADAQAH_RECORDS, null))
        val checklistItems = decodeChecklist(prefs.getString(KEY_CHECKLIST_ITEMS, null)) ?: DEFAULT_CHECKLIST
        val checkedChecklist = decodeStringSetMap(prefs.getString(KEY_CHECKED_CHECKLIST, null))

        val upcoming = RamadanAlarmScheduler.rescheduleAlarms(
            context = context,
            alarmConfig = alarmConfig,
            nightWorship = nightWorship,
            location = location,
            calcConfig = calcConfig,
            confirmedStartDateKey = confirmedStart,
            confirmedTotalDays = confirmedDays,
            todayTimingOverride = effectiveTiming,
            nowMillis = nowMillis
        )

        _state.value = RamadanCompanionState(
            periodStatus = periodStatus,
            todayTiming = effectiveTiming,
            alarmConfig = alarmConfig,
            upcomingAlarms = upcoming,
            hasNotificationPermission = PrayerNotificationManager.hasNotificationPermission(context),
            canScheduleExactAlarms = PrayerNotificationManager.canScheduleExactAlarms(context),
            fastingEntries = fasting,
            quranGoal = quranGoal,
            quranLogs = quranLogs,
            deeds = allDeeds,
            completedDeedIdsByDate = completedDeeds,
            sadaqahRecords = sadaqah,
            nightWorship = nightWorship,
            checklistItems = checklistItems,
            checkedChecklistIdsByDate = checkedChecklist,
            isSyncingTimings = false
        )
    }

    fun refreshPermissionAndPeriodOnly(context: Context, nowMillis: Long = System.currentTimeMillis()) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val location = AppLocationManager.currentLocation.value
        val tz = TimeZone.getTimeZone(location.timeZoneId)
        val nowCal = Calendar.getInstance(tz).apply { timeInMillis = nowMillis }
        val confirmedStart = prefs.getString(KEY_CONFIRMED_START, null)
        val confirmedDays = prefs.getInt(KEY_CONFIRMED_DAYS, 30).coerceIn(29, 30)
        val periodStatus = evaluatePeriodForCalendar(nowCal, confirmedStart, confirmedDays)
        _state.value = _state.value.copy(
            periodStatus = periodStatus,
            hasNotificationPermission = PrayerNotificationManager.hasNotificationPermission(context),
            canScheduleExactAlarms = PrayerNotificationManager.canScheduleExactAlarms(context)
        )
    }

    suspend fun refreshTodayTimingsAndReschedule(
        context: Context,
        preferNetwork: Boolean = true,
        reason: String = "manual_refresh",
        nowMillis: Long = System.currentTimeMillis()
    ) {
        init(context, nowMillis)
        _state.value = _state.value.copy(isSyncingTimings = true)
        val location = AppLocationManager.currentLocation.value
        val calcConfig = PrayerCalculator.calcConfig.value
        val tz = TimeZone.getTimeZone(location.timeZoneId)
        val nowCal = Calendar.getInstance(tz).apply { timeInMillis = nowMillis }

        val updatedTiming = withContext(Dispatchers.IO) {
            if (preferNetwork) {
                RamadanPrayerTimingSync.fetchTimingsWithFallback(location, calcConfig, nowCal, nowMillis)
            } else {
                RamadanPrayerTimingSync.computeOfflineTiming(location, calcConfig, nowCal, nowMillis)
            }
        }

        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        saveTimingCache(prefs, updatedTiming)
        val confirmedStart = prefs.getString(KEY_CONFIRMED_START, null)
        val confirmedDays = prefs.getInt(KEY_CONFIRMED_DAYS, 30).coerceIn(29, 30)
        val periodStatus = evaluatePeriodForCalendar(nowCal, confirmedStart, confirmedDays)
        val current = _state.value
        val upcoming = RamadanAlarmScheduler.rescheduleAlarms(
            context = context,
            alarmConfig = current.alarmConfig,
            nightWorship = current.nightWorship,
            location = location,
            calcConfig = calcConfig,
            confirmedStartDateKey = confirmedStart,
            confirmedTotalDays = confirmedDays,
            todayTimingOverride = updatedTiming,
            nowMillis = nowMillis
        )

        _state.value = current.copy(
            periodStatus = periodStatus,
            todayTiming = updatedTiming,
            upcomingAlarms = upcoming,
            hasNotificationPermission = PrayerNotificationManager.hasNotificationPermission(context),
            canScheduleExactAlarms = PrayerNotificationManager.canScheduleExactAlarms(context),
            isSyncingTimings = false,
            syncStatusMessage = "${updatedTiming.source.id}:$reason"
        )
    }

    fun rescheduleAlarmsOfflineFallback(
        context: Context,
        reason: String = "offline_reschedule",
        nowMillis: Long = System.currentTimeMillis()
    ) {
        init(context, nowMillis)
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val location = AppLocationManager.currentLocation.value
        val calcConfig = PrayerCalculator.calcConfig.value
        val tz = TimeZone.getTimeZone(location.timeZoneId)
        val nowCal = Calendar.getInstance(tz).apply { timeInMillis = nowMillis }
        val confirmedStart = prefs.getString(KEY_CONFIRMED_START, null)
        val confirmedDays = prefs.getInt(KEY_CONFIRMED_DAYS, 30).coerceIn(29, 30)
        val periodStatus = evaluatePeriodForCalendar(nowCal, confirmedStart, confirmedDays)
        val timing = RamadanPrayerTimingSync.computeOfflineTiming(location, calcConfig, nowCal, nowMillis)
        saveTimingCache(prefs, timing)
        val current = _state.value
        val upcoming = RamadanAlarmScheduler.rescheduleAlarms(
            context = context,
            alarmConfig = current.alarmConfig,
            nightWorship = current.nightWorship,
            location = location,
            calcConfig = calcConfig,
            confirmedStartDateKey = confirmedStart,
            confirmedTotalDays = confirmedDays,
            todayTimingOverride = timing,
            nowMillis = nowMillis
        )
        _state.value = current.copy(
            periodStatus = periodStatus,
            todayTiming = timing,
            upcomingAlarms = upcoming,
            hasNotificationPermission = PrayerNotificationManager.hasNotificationPermission(context),
            canScheduleExactAlarms = PrayerNotificationManager.canScheduleExactAlarms(context),
            syncStatusMessage = reason
        )
    }

    fun updateAlarmConfig(context: Context, newConfig: RamadanAlarmConfig, nowMillis: Long = System.currentTimeMillis()) {
        init(context, nowMillis)
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_ALARM_CONFIG, encodeAlarmConfig(newConfig).toString()).apply()
        val current = _state.value
        _state.value = current.copy(alarmConfig = newConfig)
        rescheduleAlarmsWithCurrentTiming(context, nowMillis)
    }

    fun updateNightWorshipConfig(context: Context, newNight: NightWorshipConfig, nowMillis: Long = System.currentTimeMillis()) {
        init(context, nowMillis)
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_NIGHT_WORSHIP, encodeNightWorship(newNight).toString()).apply()
        _state.value = _state.value.copy(nightWorship = newNight)
        rescheduleAlarmsWithCurrentTiming(context, nowMillis)
    }

    fun setConfirmedRamadanStart(
        context: Context,
        startDateKey: String?,
        totalDays: Int = 30,
        nowMillis: Long = System.currentTimeMillis()
    ) {
        init(context, nowMillis)
        val safeTotal = totalDays.coerceIn(29, 30)
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().apply {
            if (startDateKey.isNullOrBlank()) remove(KEY_CONFIRMED_START)
            else putString(KEY_CONFIRMED_START, startDateKey.trim())
            putInt(KEY_CONFIRMED_DAYS, safeTotal)
            apply()
        }
        val location = AppLocationManager.currentLocation.value
        val tz = TimeZone.getTimeZone(location.timeZoneId)
        val nowCal = Calendar.getInstance(tz).apply { timeInMillis = nowMillis }
        val period = evaluatePeriodForCalendar(nowCal, startDateKey?.ifBlank { null }, safeTotal)
        _state.value = _state.value.copy(periodStatus = period)
        rescheduleAlarmsWithCurrentTiming(context, nowMillis)
    }

    fun setFastingStatus(
        context: Context,
        dateKey: String,
        status: FastingStatus,
        isVoluntary: Boolean = false,
        ramadanDay: Int? = null,
        note: String = "",
        nowMillis: Long = System.currentTimeMillis()
    ) {
        init(context, nowMillis)
        val updated = _state.value.fastingEntries.toMutableMap()
        if (status == FastingStatus.NOT_RECORDED && note.isBlank() && isVoluntary) {
            updated.remove(dateKey)
        } else {
            updated[dateKey] = FastingDayEntry(
                dateKey = dateKey,
                ramadanDay = ramadanDay,
                isVoluntary = isVoluntary,
                status = status,
                note = note.trim(),
                updatedAtMillis = nowMillis
            )
        }
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_FASTING_ENTRIES, encodeFastingMap(updated).toString()).apply()
        _state.value = _state.value.copy(fastingEntries = updated)
    }

    fun updateQuranGoal(context: Context, goal: QuranGoalConfig) {
        init(context)
        val sanitized = goal.copy(
            targetDays = goal.targetDays.coerceIn(1, 365),
            dailyTargetValue = goal.dailyTargetValue.coerceIn(1, 604)
        )
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_QURAN_GOAL, encodeQuranGoal(sanitized).toString()).apply()
        _state.value = _state.value.copy(quranGoal = sanitized)
    }

    fun logQuranReading(
        context: Context,
        pagesRead: Int,
        ayaatRead: Int = 0,
        surahNumber: Int? = null,
        note: String = "",
        dateKey: String = formatDateKey(Calendar.getInstance())
    ) {
        init(context)
        if (pagesRead <= 0 && ayaatRead <= 0) return
        val entry = QuranLogEntry(
            id = UUID.randomUUID().toString(),
            dateKey = dateKey,
            pagesRead = pagesRead.coerceAtLeast(0),
            ayaatRead = ayaatRead.coerceAtLeast(0),
            surahNumber = surahNumber,
            note = note.trim(),
            timestampMillis = System.currentTimeMillis()
        )
        val updated = listOf(entry) + _state.value.quranLogs
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_QURAN_LOGS, encodeQuranLogs(updated).toString()).apply()
        _state.value = _state.value.copy(quranLogs = updated)
    }

    fun deleteQuranLog(context: Context, id: String) {
        init(context)
        val updated = _state.value.quranLogs.filterNot { it.id == id }
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_QURAN_LOGS, encodeQuranLogs(updated).toString()).apply()
        _state.value = _state.value.copy(quranLogs = updated)
    }

    fun toggleDeedForDate(
        context: Context,
        deedId: String,
        dateKey: String = formatDateKey(Calendar.getInstance())
    ) {
        init(context)
        val map = _state.value.completedDeedIdsByDate.toMutableMap()
        val set = map[dateKey]?.toMutableSet() ?: mutableSetOf()
        if (!set.add(deedId)) {
            set.remove(deedId)
        }
        map[dateKey] = set
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_COMPLETED_DEEDS, encodeStringSetMap(map).toString()).apply()
        _state.value = _state.value.copy(completedDeedIdsByDate = map)
    }

    fun addCustomDeed(context: Context, title: String, category: DeedCategory = DeedCategory.WORSHIP) {
        init(context)
        val clean = title.trim()
        if (clean.isBlank()) return
        val newItem = GoodDeedItem(
            id = "deed_custom_${UUID.randomUUID()}",
            title = clean,
            titleKey = null,
            isCustom = true,
            category = category
        )
        val customOnly = _state.value.deeds.filter { it.isCustom } + newItem
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_CUSTOM_DEEDS, encodeDeedList(customOnly).toString()).apply()
        _state.value = _state.value.copy(deeds = DEFAULT_DEEDS + customOnly)
    }

    fun updateCustomDeed(context: Context, id: String, newTitle: String, category: DeedCategory) {
        init(context)
        val clean = newTitle.trim()
        if (clean.isBlank()) return
        val customOnly = _state.value.deeds.filter { it.isCustom }.map {
            if (it.id == id) it.copy(title = clean, category = category) else it
        }
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_CUSTOM_DEEDS, encodeDeedList(customOnly).toString()).apply()
        _state.value = _state.value.copy(deeds = DEFAULT_DEEDS + customOnly)
    }

    fun deleteCustomDeed(context: Context, id: String) {
        init(context)
        val customOnly = _state.value.deeds.filter { it.isCustom && it.id != id }
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_CUSTOM_DEEDS, encodeDeedList(customOnly).toString()).apply()
        _state.value = _state.value.copy(deeds = DEFAULT_DEEDS + customOnly)
    }

    fun addSadaqahRecord(
        context: Context,
        amountText: String,
        currencyCode: String,
        recipientOrCause: String,
        dateKey: String = formatDateKey(Calendar.getInstance())
    ) {
        init(context)
        val cleanAmt = amountText.trim()
        val cleanCause = recipientOrCause.trim()
        if (cleanAmt.isBlank() && cleanCause.isBlank()) return
        val record = SadaqahRecord(
            id = UUID.randomUUID().toString(),
            dateKey = dateKey,
            amountText = cleanAmt.ifBlank { "—" },
            currencyCode = currencyCode.ifBlank { "USD" },
            recipientOrCause = cleanCause.ifBlank { "Charity" },
            timestampMillis = System.currentTimeMillis()
        )
        val updated = listOf(record) + _state.value.sadaqahRecords
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_SADAQAH_RECORDS, encodeSadaqahList(updated).toString()).apply()
        _state.value = _state.value.copy(sadaqahRecords = updated)
    }

    fun deleteSadaqahRecord(context: Context, id: String) {
        init(context)
        val updated = _state.value.sadaqahRecords.filterNot { it.id == id }
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_SADAQAH_RECORDS, encodeSadaqahList(updated).toString()).apply()
        _state.value = _state.value.copy(sadaqahRecords = updated)
    }

    fun toggleChecklistItem(
        context: Context,
        itemId: String,
        dateKey: String = formatDateKey(Calendar.getInstance())
    ) {
        init(context)
        val map = _state.value.checkedChecklistIdsByDate.toMutableMap()
        val set = map[dateKey]?.toMutableSet() ?: mutableSetOf()
        if (!set.add(itemId)) {
            set.remove(itemId)
        }
        map[dateKey] = set
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_CHECKED_CHECKLIST, encodeStringSetMap(map).toString()).apply()
        _state.value = _state.value.copy(checkedChecklistIdsByDate = map)
    }

    fun addChecklistItem(context: Context, title: String) {
        init(context)
        val clean = title.trim()
        if (clean.isBlank()) return
        val updated = _state.value.checklistItems + IftarChecklistItem(
            id = "chk_custom_${UUID.randomUUID()}",
            title = clean,
            titleKey = null,
            isCustom = true
        )
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_CHECKLIST_ITEMS, encodeChecklist(updated).toString()).apply()
        _state.value = _state.value.copy(checklistItems = updated)
    }

    fun updateChecklistItem(context: Context, id: String, newTitle: String) {
        init(context)
        val clean = newTitle.trim()
        if (clean.isBlank()) return
        val updated = _state.value.checklistItems.map {
            if (it.id == id) it.copy(title = clean, titleKey = null, isCustom = true) else it
        }
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_CHECKLIST_ITEMS, encodeChecklist(updated).toString()).apply()
        _state.value = _state.value.copy(checklistItems = updated)
    }

    fun deleteChecklistItem(context: Context, id: String) {
        init(context)
        val updated = _state.value.checklistItems.filterNot { it.id == id }
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_CHECKLIST_ITEMS, encodeChecklist(updated).toString()).apply()
        _state.value = _state.value.copy(checklistItems = updated)
    }

    private fun rescheduleAlarmsWithCurrentTiming(context: Context, nowMillis: Long) {
        val current = _state.value
        val location = AppLocationManager.currentLocation.value
        val calcConfig = PrayerCalculator.calcConfig.value
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val confirmedStart = prefs.getString(KEY_CONFIRMED_START, null)
        val confirmedDays = prefs.getInt(KEY_CONFIRMED_DAYS, 30).coerceIn(29, 30)
        val upcoming = RamadanAlarmScheduler.rescheduleAlarms(
            context = context,
            alarmConfig = current.alarmConfig,
            nightWorship = current.nightWorship,
            location = location,
            calcConfig = calcConfig,
            confirmedStartDateKey = confirmedStart,
            confirmedTotalDays = confirmedDays,
            todayTimingOverride = current.todayTiming,
            nowMillis = nowMillis
        )
        _state.value = _state.value.copy(
            upcomingAlarms = upcoming,
            hasNotificationPermission = PrayerNotificationManager.hasNotificationPermission(context),
            canScheduleExactAlarms = PrayerNotificationManager.canScheduleExactAlarms(context)
        )
    }

    private fun saveTimingCache(prefs: android.content.SharedPreferences, timing: DailyRamadanTimingRecord) {
        prefs.edit().putString(KEY_TIMING_CACHE, encodeTimingCache(timing).toString()).apply()
    }

    private fun encodeAlarmConfig(c: RamadanAlarmConfig): JSONObject = JSONObject().apply {
        put("master", c.alarmsMasterEnabled)
        put("sehriPre", c.sehriPreAlarmEnabled)
        put("sehriOff", c.sehriOffsetMinutes)
        put("sehriFajr", c.sehriFajrEndAlarmEnabled)
        put("iftarPre", c.iftarPreReminderEnabled)
        put("iftarOff", c.iftarOffsetMinutes)
        put("iftarMain", c.iftarMainAlarmEnabled)
        put("sound", c.alarmSoundType.id)
        put("vib", c.vibrationEnabled)
        put("snooze", c.snoozeMinutes)
        put("onlyRamadan", c.activeOnlyDuringRamadan)
    }

    private fun decodeAlarmConfig(raw: String?): RamadanAlarmConfig {
        if (raw.isNullOrBlank()) return RamadanAlarmConfig()
        return try {
            val o = JSONObject(raw)
            RamadanAlarmConfig(
                alarmsMasterEnabled = o.optBoolean("master", true),
                sehriPreAlarmEnabled = o.optBoolean("sehriPre", true),
                sehriOffsetMinutes = o.optInt("sehriOff", 30).coerceIn(1, 180),
                sehriFajrEndAlarmEnabled = o.optBoolean("sehriFajr", true),
                iftarPreReminderEnabled = o.optBoolean("iftarPre", true),
                iftarOffsetMinutes = o.optInt("iftarOff", 10).coerceIn(1, 120),
                iftarMainAlarmEnabled = o.optBoolean("iftarMain", true),
                alarmSoundType = AlarmSoundType.fromId(o.optString("sound", "adhan_makkah")),
                vibrationEnabled = o.optBoolean("vib", true),
                snoozeMinutes = o.optInt("snooze", 5).coerceIn(1, 30),
                activeOnlyDuringRamadan = o.optBoolean("onlyRamadan", true)
            )
        } catch (_: Exception) {
            RamadanAlarmConfig()
        }
    }

    private fun encodeTimingCache(t: DailyRamadanTimingRecord): JSONObject = JSONObject().apply {
        put("dateKey", t.dateKey)
        put("tz", t.timezoneId)
        put("lat", t.latitude)
        put("lon", t.longitude)
        put("city", t.cityName)
        put("method", t.methodName)
        put("asr", t.asrJuristic)
        put("fajr24", t.fajr24)
        put("maghrib24", t.maghrib24)
        put("fajrDisp", t.fajrDisplay)
        put("maghribDisp", t.maghribDisplay)
        put("fajrMs", t.fajrTriggerMillis)
        put("maghribMs", t.maghribTriggerMillis)
        put("source", t.source.id)
        put("updated", t.lastUpdatedMillis)
        put("fallback", t.isEstimateFallback)
    }

    private fun decodeTimingCache(raw: String?): DailyRamadanTimingRecord? {
        if (raw.isNullOrBlank()) return null
        return try {
            val o = JSONObject(raw)
            DailyRamadanTimingRecord(
                dateKey = o.optString("dateKey", ""),
                timezoneId = o.optString("tz", ""),
                latitude = o.optDouble("lat", 0.0),
                longitude = o.optDouble("lon", 0.0),
                cityName = o.optString("city", ""),
                methodName = o.optString("method", ""),
                asrJuristic = o.optString("asr", "HANAFI"),
                fajr24 = o.optString("fajr24", ""),
                maghrib24 = o.optString("maghrib24", ""),
                fajrDisplay = o.optString("fajrDisp", ""),
                maghribDisplay = o.optString("maghribDisp", ""),
                fajrTriggerMillis = o.optLong("fajrMs", 0L),
                maghribTriggerMillis = o.optLong("maghribMs", 0L),
                source = if (o.optString("source") == TimingSource.ALADHAN_API.id) TimingSource.ALADHAN_API else TimingSource.OFFLINE_ASTRONOMICAL,
                lastUpdatedMillis = o.optLong("updated", 0L),
                isEstimateFallback = o.optBoolean("fallback", true)
            )
        } catch (_: Exception) {
            null
        }
    }

    private fun encodeFastingMap(map: Map<String, FastingDayEntry>): JSONArray = JSONArray().apply {
        map.values.forEach { e ->
            put(JSONObject().apply {
                put("dateKey", e.dateKey)
                if (e.ramadanDay != null) put("ramadanDay", e.ramadanDay)
                put("vol", e.isVoluntary)
                put("status", e.status.id)
                put("note", e.note)
                put("updated", e.updatedAtMillis)
            })
        }
    }

    private fun decodeFastingMap(raw: String?): Map<String, FastingDayEntry> {
        if (raw.isNullOrBlank()) return emptyMap()
        return try {
            val arr = JSONArray(raw)
            val result = mutableMapOf<String, FastingDayEntry>()
            for (i in 0 until arr.length()) {
                val o = arr.getJSONObject(i)
                val key = o.optString("dateKey", "")
                if (key.isNotBlank()) {
                    result[key] = FastingDayEntry(
                        dateKey = key,
                        ramadanDay = if (o.has("ramadanDay") && !o.isNull("ramadanDay")) o.optInt("ramadanDay") else null,
                        isVoluntary = o.optBoolean("vol", false),
                        status = FastingStatus.fromId(o.optString("status")),
                        note = o.optString("note", ""),
                        updatedAtMillis = o.optLong("updated", 0L)
                    )
                }
            }
            result
        } catch (_: Exception) {
            emptyMap()
        }
    }

    private fun encodeQuranGoal(g: QuranGoalConfig): JSONObject = JSONObject().apply {
        put("enabled", g.khatamGoalEnabled)
        put("days", g.targetDays)
        put("mode", g.dailyTargetMode.id)
        put("val", g.dailyTargetValue)
        put("totalPages", g.totalMushafPages)
    }

    private fun decodeQuranGoal(raw: String?): QuranGoalConfig {
        if (raw.isNullOrBlank()) return QuranGoalConfig()
        return try {
            val o = JSONObject(raw)
            QuranGoalConfig(
                khatamGoalEnabled = o.optBoolean("enabled", true),
                targetDays = o.optInt("days", 30).coerceIn(1, 365),
                dailyTargetMode = QuranTargetMode.fromId(o.optString("mode")),
                dailyTargetValue = o.optInt("val", 20).coerceIn(1, 604),
                totalMushafPages = o.optInt("totalPages", 604).coerceIn(100, 900)
            )
        } catch (_: Exception) {
            QuranGoalConfig()
        }
    }

    private fun encodeQuranLogs(list: List<QuranLogEntry>): JSONArray = JSONArray().apply {
        list.forEach { e ->
            put(JSONObject().apply {
                put("id", e.id)
                put("dateKey", e.dateKey)
                put("pages", e.pagesRead)
                put("ayaat", e.ayaatRead)
                if (e.surahNumber != null) put("surah", e.surahNumber)
                put("note", e.note)
                put("ts", e.timestampMillis)
            })
        }
    }

    private fun decodeQuranLogs(raw: String?): List<QuranLogEntry> {
        if (raw.isNullOrBlank()) return emptyList()
        return try {
            val arr = JSONArray(raw)
            (0 until arr.length()).map { i ->
                val o = arr.getJSONObject(i)
                QuranLogEntry(
                    id = o.optString("id", UUID.randomUUID().toString()),
                    dateKey = o.optString("dateKey", ""),
                    pagesRead = o.optInt("pages", 0),
                    ayaatRead = o.optInt("ayaat", 0),
                    surahNumber = if (o.has("surah") && !o.isNull("surah")) o.optInt("surah") else null,
                    note = o.optString("note", ""),
                    timestampMillis = o.optLong("ts", 0L)
                )
            }
        } catch (_: Exception) {
            emptyList()
        }
    }

    private fun encodeDeedList(list: List<GoodDeedItem>): JSONArray = JSONArray().apply {
        list.forEach { d ->
            put(JSONObject().apply {
                put("id", d.id)
                put("title", d.title)
                if (d.titleKey != null) put("titleKey", d.titleKey)
                put("custom", d.isCustom)
                put("cat", d.category.id)
            })
        }
    }

    private fun decodeDeedList(raw: String?): List<GoodDeedItem> {
        if (raw.isNullOrBlank()) return emptyList()
        return try {
            val arr = JSONArray(raw)
            (0 until arr.length()).map { i ->
                val o = arr.getJSONObject(i)
                GoodDeedItem(
                    id = o.optString("id", UUID.randomUUID().toString()),
                    title = o.optString("title", ""),
                    titleKey = if (o.has("titleKey") && !o.isNull("titleKey")) o.optString("titleKey") else null,
                    isCustom = o.optBoolean("custom", true),
                    category = DeedCategory.fromId(o.optString("cat"))
                )
            }
        } catch (_: Exception) {
            emptyList()
        }
    }

    private fun encodeStringSetMap(map: Map<String, Set<String>>): JSONObject = JSONObject().apply {
        map.forEach { (k, v) ->
            put(k, JSONArray(v.toList()))
        }
    }

    private fun decodeStringSetMap(raw: String?): Map<String, Set<String>> {
        if (raw.isNullOrBlank()) return emptyMap()
        return try {
            val o = JSONObject(raw)
            val result = mutableMapOf<String, Set<String>>()
            o.keys().forEach { k ->
                val arr = o.optJSONArray(k) ?: JSONArray()
                val set = mutableSetOf<String>()
                for (i in 0 until arr.length()) {
                    set.add(arr.optString(i))
                }
                result[k] = set
            }
            result
        } catch (_: Exception) {
            emptyMap()
        }
    }

    private fun encodeSadaqahList(list: List<SadaqahRecord>): JSONArray = JSONArray().apply {
        list.forEach { r ->
            put(JSONObject().apply {
                put("id", r.id)
                put("dateKey", r.dateKey)
                put("amt", r.amountText)
                put("cur", r.currencyCode)
                put("cause", r.recipientOrCause)
                put("ts", r.timestampMillis)
            })
        }
    }

    private fun decodeSadaqahList(raw: String?): List<SadaqahRecord> {
        if (raw.isNullOrBlank()) return emptyList()
        return try {
            val arr = JSONArray(raw)
            (0 until arr.length()).map { i ->
                val o = arr.getJSONObject(i)
                SadaqahRecord(
                    id = o.optString("id", UUID.randomUUID().toString()),
                    dateKey = o.optString("dateKey", ""),
                    amountText = o.optString("amt", "0"),
                    currencyCode = o.optString("cur", "USD"),
                    recipientOrCause = o.optString("cause", ""),
                    timestampMillis = o.optLong("ts", 0L)
                )
            }
        } catch (_: Exception) {
            emptyList()
        }
    }

    private fun encodeNightWorship(n: NightWorshipConfig): JSONObject = JSONObject().apply {
        put("tarEn", n.taraweehReminderEnabled)
        put("tarH", n.taraweehHour)
        put("tarM", n.taraweehMinute)
        put("tahEn", n.tahajjudReminderEnabled)
        put("tahOff", n.tahajjudOffsetBeforeFajrMinutes)
        put("tahFix", n.tahajjudUseCustomFixedTime)
        put("tahH", n.tahajjudFixedHour)
        put("tahM", n.tahajjudFixedMinute)
        put("l10En", n.lastTenNightsReminderEnabled)
        put("l10H", n.lastTenNightsHour)
        put("l10M", n.lastTenNightsMinute)
    }

    private fun decodeNightWorship(raw: String?): NightWorshipConfig {
        if (raw.isNullOrBlank()) return NightWorshipConfig()
        return try {
            val o = JSONObject(raw)
            NightWorshipConfig(
                taraweehReminderEnabled = o.optBoolean("tarEn", true),
                taraweehHour = o.optInt("tarH", 20).coerceIn(0, 23),
                taraweehMinute = o.optInt("tarM", 30).coerceIn(0, 59),
                tahajjudReminderEnabled = o.optBoolean("tahEn", true),
                tahajjudOffsetBeforeFajrMinutes = o.optInt("tahOff", 60).coerceIn(15, 180),
                tahajjudUseCustomFixedTime = o.optBoolean("tahFix", false),
                tahajjudFixedHour = o.optInt("tahH", 3).coerceIn(0, 23),
                tahajjudFixedMinute = o.optInt("tahM", 30).coerceIn(0, 59),
                lastTenNightsReminderEnabled = o.optBoolean("l10En", true),
                lastTenNightsHour = o.optInt("l10H", 21).coerceIn(0, 23),
                lastTenNightsMinute = o.optInt("l10M", 0).coerceIn(0, 59)
            )
        } catch (_: Exception) {
            NightWorshipConfig()
        }
    }

    private fun encodeChecklist(list: List<IftarChecklistItem>): JSONArray = JSONArray().apply {
        list.forEach { item ->
            put(JSONObject().apply {
                put("id", item.id)
                put("title", item.title)
                if (item.titleKey != null) put("titleKey", item.titleKey)
                put("custom", item.isCustom)
            })
        }
    }

    private fun decodeChecklist(raw: String?): List<IftarChecklistItem>? {
        if (raw.isNullOrBlank()) return null
        return try {
            val arr = JSONArray(raw)
            (0 until arr.length()).map { i ->
                val o = arr.getJSONObject(i)
                IftarChecklistItem(
                    id = o.optString("id", UUID.randomUUID().toString()),
                    title = o.optString("title", ""),
                    titleKey = if (o.has("titleKey") && !o.isNull("titleKey")) o.optString("titleKey") else null,
                    isCustom = o.optBoolean("custom", false)
                )
            }
        } catch (_: Exception) {
            null
        }
    }
}
