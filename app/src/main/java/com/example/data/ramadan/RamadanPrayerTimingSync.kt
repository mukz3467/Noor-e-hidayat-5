package com.example.data.ramadan

import com.example.data.location.UserLocation
import com.example.data.repository.PrayerCalculator
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.TimeUnit

object RamadanPrayerTimingSync {

    private const val DEFAULT_BASE_URL = "https://api.aladhan.com/v1"

    @Volatile
    private var customBaseUrl: String? = null

    @Volatile
    private var customOkHttpClient: OkHttpClient? = null

    private val defaultClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(8, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .build()
    }

    fun overrideForTest(baseUrl: String?, client: OkHttpClient?) {
        customBaseUrl = baseUrl
        customOkHttpClient = client
    }

    fun resetTestOverride() {
        customBaseUrl = null
        customOkHttpClient = null
    }

    fun mapCalculationMethodToAlAdhanCode(method: PrayerCalculator.CalculationMethod): Int {
        return when (method) {
            PrayerCalculator.CalculationMethod.KARACHI -> 1
            PrayerCalculator.CalculationMethod.ISNA -> 2
            PrayerCalculator.CalculationMethod.MWL -> 3
            PrayerCalculator.CalculationMethod.UMM_AL_QURA -> 4
            PrayerCalculator.CalculationMethod.EGYPTIAN -> 5
            PrayerCalculator.CalculationMethod.TEHRAN -> 7
            PrayerCalculator.CalculationMethod.KUWAIT -> 9
            PrayerCalculator.CalculationMethod.QATAR -> 10
            PrayerCalculator.CalculationMethod.SINGAPORE -> 11
            PrayerCalculator.CalculationMethod.FRANCE_UOIF -> 12
            PrayerCalculator.CalculationMethod.TURKEY -> 13
            PrayerCalculator.CalculationMethod.RUSSIA -> 14
            PrayerCalculator.CalculationMethod.MOONSIGHTING_COMMITTEE -> 15
            PrayerCalculator.CalculationMethod.DUBAI -> 16
            PrayerCalculator.CalculationMethod.CUSTOM -> 99
        }
    }

    fun computeOfflineTiming(
        location: UserLocation,
        config: PrayerCalculator.PrayerCalculationConfig,
        targetCal: Calendar = Calendar.getInstance(),
        nowMillis: Long = System.currentTimeMillis()
    ): DailyRamadanTimingRecord {
        val tz = TimeZone.getTimeZone(location.timeZoneId)
        val localCal = (targetCal.clone() as Calendar).apply { timeZone = tz }
        val schedule = PrayerCalculator.calculatePrayerTimes(
            latitude = location.latitude,
            longitude = location.longitude,
            calendar = localCal,
            method = config.method,
            asrJuristic = config.asrJuristic,
            adjustments = config.adjustments,
            highLatitudeRule = config.highLatitudeRule,
            cityName = "${location.cityName}, ${location.countryName}",
            timeZoneId = location.timeZoneId,
            customParams = config.customParams
        )
        val fajrTrigger = PrayerCalculator.computePrayerTriggerTimeMillis(schedule.fajr, localCal, "fajr")
            ?: (localCal.timeInMillis)
        val maghribTrigger = PrayerCalculator.computePrayerTriggerTimeMillis(schedule.maghrib, localCal, "maghrib")
            ?: (localCal.timeInMillis)

        return DailyRamadanTimingRecord(
            dateKey = formatDateKey(localCal),
            timezoneId = location.timeZoneId,
            latitude = location.latitude,
            longitude = location.longitude,
            cityName = location.cityName,
            methodName = config.method.displayName,
            asrJuristic = config.asrJuristic.name,
            fajr24 = to24HourString(schedule.fajr),
            maghrib24 = to24HourString(schedule.maghrib),
            fajrDisplay = schedule.fajr,
            maghribDisplay = schedule.maghrib,
            fajrTriggerMillis = fajrTrigger,
            maghribTriggerMillis = maghribTrigger,
            source = TimingSource.OFFLINE_ASTRONOMICAL,
            lastUpdatedMillis = nowMillis,
            isEstimateFallback = true
        )
    }

    fun fetchTimingsWithFallback(
        location: UserLocation,
        config: PrayerCalculator.PrayerCalculationConfig,
        targetCal: Calendar = Calendar.getInstance(),
        nowMillis: Long = System.currentTimeMillis()
    ): DailyRamadanTimingRecord {
        val tz = TimeZone.getTimeZone(location.timeZoneId)
        val localCal = (targetCal.clone() as Calendar).apply { timeZone = tz }

        if (config.method == PrayerCalculator.CalculationMethod.CUSTOM) {
            return computeOfflineTiming(location, config, localCal, nowMillis)
        }

        val ddMmYyyy = String.format(
            Locale.US,
            "%02d-%02d-%04d",
            localCal.get(Calendar.DAY_OF_MONTH),
            localCal.get(Calendar.MONTH) + 1,
            localCal.get(Calendar.YEAR)
        )
        val methodCode = mapCalculationMethodToAlAdhanCode(config.method)
        val school = if (config.asrJuristic == PrayerCalculator.AsrJuristic.HANAFI) 1 else 0
        val tune = "0,${config.adjustments.fajrMinutes},${config.adjustments.sunriseMinutes},${config.adjustments.dhuhrMinutes},${config.adjustments.asrMinutes},${config.adjustments.maghribMinutes},0,${config.adjustments.ishaMinutes},0"
        val baseUrl = customBaseUrl?.trimEnd('/') ?: DEFAULT_BASE_URL
        val url = "$baseUrl/timings/$ddMmYyyy?latitude=${location.latitude}&longitude=${location.longitude}&method=$methodCode&school=$school&tune=$tune&timezonestring=${location.timeZoneId}"

        return try {
            val client = customOkHttpClient ?: defaultClient
            val request = Request.Builder()
                .url(url)
                .header("Accept", "application/json")
                .build()
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    return computeOfflineTiming(location, config, localCal, nowMillis)
                }
                val body = response.body?.string().orEmpty()
                parseAlAdhanResponse(
                    jsonString = body,
                    location = location,
                    config = config,
                    localCal = localCal,
                    nowMillis = nowMillis
                ) ?: computeOfflineTiming(location, config, localCal, nowMillis)
            }
        } catch (_: Exception) {
            computeOfflineTiming(location, config, localCal, nowMillis)
        }
    }

    internal fun parseAlAdhanResponse(
        jsonString: String,
        location: UserLocation,
        config: PrayerCalculator.PrayerCalculationConfig,
        localCal: Calendar,
        nowMillis: Long
    ): DailyRamadanTimingRecord? {
        return try {
            val root = JSONObject(jsonString)
            if (root.optInt("code", -1) != 200) return null
            val data = root.optJSONObject("data") ?: return null
            val timings = data.optJSONObject("timings") ?: return null
            val rawFajr = sanitizeApiTime(timings.optString("Fajr", ""))
            val rawMaghrib = sanitizeApiTime(timings.optString("Maghrib", ""))
            if (rawFajr.isBlank() || rawMaghrib.isBlank()) return null

            val fajrDisplay = to12HourString(rawFajr)
            val maghribDisplay = to12HourString(rawMaghrib)
            val fajrTrigger = PrayerCalculator.computePrayerTriggerTimeMillis(rawFajr, localCal, "fajr")
                ?: return null
            val maghribTrigger = PrayerCalculator.computePrayerTriggerTimeMillis(rawMaghrib, localCal, "maghrib")
                ?: return null

            DailyRamadanTimingRecord(
                dateKey = formatDateKey(localCal),
                timezoneId = location.timeZoneId,
                latitude = location.latitude,
                longitude = location.longitude,
                cityName = location.cityName,
                methodName = config.method.displayName,
                asrJuristic = config.asrJuristic.name,
                fajr24 = rawFajr,
                maghrib24 = rawMaghrib,
                fajrDisplay = fajrDisplay,
                maghribDisplay = maghribDisplay,
                fajrTriggerMillis = fajrTrigger,
                maghribTriggerMillis = maghribTrigger,
                source = TimingSource.ALADHAN_API,
                lastUpdatedMillis = nowMillis,
                isEstimateFallback = false
            )
        } catch (_: Exception) {
            null
        }
    }

    fun sanitizeApiTime(raw: String): String {
        val clean = raw.trim().substringBefore(" ").trim()
        val parts = clean.split(":")
        if (parts.size < 2) return ""
        val h = parts[0].toIntOrNull() ?: return ""
        val m = parts[1].toIntOrNull() ?: return ""
        if (h !in 0..23 || m !in 0..59) return ""
        return String.format(Locale.US, "%02d:%02d", h, m)
    }

    fun to24HourString(timeStr: String): String {
        val trimmed = timeStr.trim()
        if (trimmed.isBlank() || trimmed == "--:--") return ""
        val isPm = trimmed.endsWith("PM", ignoreCase = true)
        val isAm = trimmed.endsWith("AM", ignoreCase = true)
        val clean = trimmed.replace("AM", "").replace("PM", "").trim()
        val parts = clean.split(":")
        if (parts.size < 2) return ""
        var h = parts[0].toIntOrNull() ?: return ""
        val m = parts[1].toIntOrNull() ?: return ""
        if (isPm && h < 12) h += 12
        if (isAm && h == 12) h = 0
        return String.format(Locale.US, "%02d:%02d", h.coerceIn(0, 23), m.coerceIn(0, 59))
    }

    fun to12HourString(time24: String): String {
        val parts = time24.trim().split(":")
        if (parts.size < 2) return time24
        val h = parts[0].toIntOrNull() ?: return time24
        val m = parts[1].toIntOrNull() ?: return time24
        val h12 = if (h == 0) 12 else if (h > 12) h - 12 else h
        val amPm = if (h < 12) "AM" else "PM"
        return String.format(Locale.US, "%02d:%02d %s", h12, m, amPm)
    }
}
