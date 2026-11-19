package com.example.data.repository

import android.content.Context
import com.example.data.model.PrayerSchedule
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone
import kotlin.math.*

object PrayerCalculator {

    const val KAABA_LAT = 21.4224779
    const val KAABA_LON = 39.8251832
    private const val PREFS_NAME = "prayer_calc_prefs"

    data class PrayerAdjustments(
        val fajrMinutes: Int = 0,
        val sunriseMinutes: Int = 0,
        val dhuhrMinutes: Int = 0,
        val asrMinutes: Int = 0,
        val maghribMinutes: Int = 0,
        val ishaMinutes: Int = 0
    ) {
        val hasAdjustments: Boolean
            get() = fajrMinutes != 0 || sunriseMinutes != 0 || dhuhrMinutes != 0 ||
                    asrMinutes != 0 || maghribMinutes != 0 || ishaMinutes != 0
    }

    data class CustomCalculationParams(
        val fajrAngle: Double = 18.0,
        val ishaAngle: Double = 17.0,
        val isIshaInterval: Boolean = false,
        val ishaIntervalMinutes: Int = 90
    )

    data class PrayerCalculationConfig(
        val method: CalculationMethod = CalculationMethod.KARACHI,
        val asrJuristic: AsrJuristic = AsrJuristic.HANAFI,
        val highLatitudeRule: HighLatitudeRule = HighLatitudeRule.ANGLE_BASED,
        val adjustments: PrayerAdjustments = PrayerAdjustments(),
        val customParams: CustomCalculationParams = CustomCalculationParams(),
        val isAutoMethodForLocation: Boolean = true
    )

    private val _calcConfig = MutableStateFlow(PrayerCalculationConfig())
    val calcConfig: StateFlow<PrayerCalculationConfig> = _calcConfig.asStateFlow()

    @Volatile
    private var isConfigLoaded = false

    fun resetForTest() {
        isConfigLoaded = false
        _calcConfig.value = PrayerCalculationConfig()
    }

    fun init(context: Context) {
        if (isConfigLoaded) return
        isConfigLoaded = true
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val methodName = prefs.getString("method", CalculationMethod.KARACHI.name) ?: CalculationMethod.KARACHI.name
        val method = runCatching { CalculationMethod.valueOf(methodName) }.getOrDefault(CalculationMethod.KARACHI)
        val asrName = prefs.getString("asr", AsrJuristic.HANAFI.name) ?: AsrJuristic.HANAFI.name
        val asr = runCatching { AsrJuristic.valueOf(asrName) }.getOrDefault(AsrJuristic.HANAFI)
        val hlName = prefs.getString("high_lat", HighLatitudeRule.ANGLE_BASED.name) ?: HighLatitudeRule.ANGLE_BASED.name
        val hl = runCatching { HighLatitudeRule.valueOf(hlName) }.getOrDefault(HighLatitudeRule.ANGLE_BASED)
        val adjustments = PrayerAdjustments(
            fajrMinutes = prefs.getInt("adj_fajr", 0),
            sunriseMinutes = prefs.getInt("adj_sunrise", 0),
            dhuhrMinutes = prefs.getInt("adj_dhuhr", 0),
            asrMinutes = prefs.getInt("adj_asr", 0),
            maghribMinutes = prefs.getInt("adj_maghrib", 0),
            ishaMinutes = prefs.getInt("adj_isha", 0)
        )
        val customParams = CustomCalculationParams(
            fajrAngle = prefs.getFloat("custom_fajr_angle", 18.0f).toDouble().coerceIn(10.0, 24.0),
            ishaAngle = prefs.getFloat("custom_isha_angle", 17.0f).toDouble().coerceIn(10.0, 24.0),
            isIshaInterval = prefs.getBoolean("custom_isha_interval", false),
            ishaIntervalMinutes = prefs.getInt("custom_isha_minutes", 90).coerceIn(30, 150)
        )
        val isAuto = prefs.getBoolean("is_auto_method", true)
        _calcConfig.value = PrayerCalculationConfig(
            method = method,
            asrJuristic = asr,
            highLatitudeRule = hl,
            adjustments = adjustments,
            customParams = customParams,
            isAutoMethodForLocation = isAuto
        )
    }

    fun updateConfig(context: Context, newConfig: PrayerCalculationConfig) {
        isConfigLoaded = true
        _calcConfig.value = newConfig
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().apply {
            putString("method", newConfig.method.name)
            putString("asr", newConfig.asrJuristic.name)
            putString("high_lat", newConfig.highLatitudeRule.name)
            putInt("adj_fajr", newConfig.adjustments.fajrMinutes)
            putInt("adj_sunrise", newConfig.adjustments.sunriseMinutes)
            putInt("adj_dhuhr", newConfig.adjustments.dhuhrMinutes)
            putInt("adj_asr", newConfig.adjustments.asrMinutes)
            putInt("adj_maghrib", newConfig.adjustments.maghribMinutes)
            putInt("adj_isha", newConfig.adjustments.ishaMinutes)
            putFloat("custom_fajr_angle", newConfig.customParams.fajrAngle.toFloat())
            putFloat("custom_isha_angle", newConfig.customParams.ishaAngle.toFloat())
            putBoolean("custom_isha_interval", newConfig.customParams.isIshaInterval)
            putInt("custom_isha_minutes", newConfig.customParams.ishaIntervalMinutes)
            putBoolean("is_auto_method", newConfig.isAutoMethodForLocation)
            apply()
        }
        try {
            com.example.data.ramadan.RamadanRepository.rescheduleAlarmsOfflineFallback(
                context = context,
                reason = "calc_config_changed"
            )
        } catch (_: Throwable) {}
    }

    fun syncRecommendedForLocationIfAuto(context: Context, country: String, lat: Double, lon: Double) {
        init(context)
        val current = _calcConfig.value
        if (!current.isAutoMethodForLocation) return
        val recMethod = getRecommendedMethod(country, lat, lon)
        val recAsr = getRecommendedAsr(country)
        if (current.method != recMethod || current.asrJuristic != recAsr) {
            updateConfig(context, current.copy(method = recMethod, asrJuristic = recAsr))
        }
    }

    enum class CalculationMethod(
        val id: String,
        val displayName: String,
        val fajrAngle: Double,
        val ishaAngle: Double,
        val isIshaInterval: Boolean = false,
        val ishaIntervalMinutes: Int = 0,
        val defaultCountry: String = ""
    ) {
        KARACHI(
            id = "karachi",
            displayName = "University of Islamic Sciences, Karachi (Pakistan)",
            fajrAngle = 18.0,
            ishaAngle = 18.0,
            defaultCountry = "Pakistan"
        ),
        MWL(
            id = "mwl",
            displayName = "Muslim World League (MWL)",
            fajrAngle = 18.0,
            ishaAngle = 17.0,
            defaultCountry = "Europe"
        ),
        ISNA(
            id = "isna",
            displayName = "Islamic Society of North America (ISNA)",
            fajrAngle = 15.0,
            ishaAngle = 15.0,
            defaultCountry = "United States"
        ),
        EGYPTIAN(
            id = "egyptian",
            displayName = "Egyptian General Authority of Survey",
            fajrAngle = 19.5,
            ishaAngle = 17.5,
            defaultCountry = "Egypt"
        ),
        UMM_AL_QURA(
            id = "makkah",
            displayName = "Umm Al-Qura University, Makkah",
            fajrAngle = 18.5,
            ishaAngle = 0.0,
            isIshaInterval = true,
            ishaIntervalMinutes = 90,
            defaultCountry = "Saudi Arabia"
        ),
        DUBAI(
            id = "dubai",
            displayName = "Dubai / UAE General Authority of Islamic Affairs",
            fajrAngle = 18.2,
            ishaAngle = 18.2,
            defaultCountry = "UAE"
        ),
        QATAR(
            id = "qatar",
            displayName = "Qatar Ministry of Awqaf & Islamic Affairs",
            fajrAngle = 18.0,
            ishaAngle = 0.0,
            isIshaInterval = true,
            ishaIntervalMinutes = 90,
            defaultCountry = "Qatar"
        ),
        KUWAIT(
            id = "kuwait",
            displayName = "Kuwait Ministry of Awqaf",
            fajrAngle = 18.0,
            ishaAngle = 17.5,
            defaultCountry = "Kuwait"
        ),
        SINGAPORE(
            id = "singapore",
            displayName = "MUIS (Islamic Religious Council of Singapore)",
            fajrAngle = 20.0,
            ishaAngle = 18.0,
            defaultCountry = "Singapore"
        ),
        TURKEY(
            id = "turkey",
            displayName = "Diyanet İşleri Başkanlığı (Turkey)",
            fajrAngle = 18.0,
            ishaAngle = 17.0,
            defaultCountry = "Turkey"
        ),
        TEHRAN(
            id = "tehran",
            displayName = "Institute of Geophysics, University of Tehran",
            fajrAngle = 17.7,
            ishaAngle = 14.0,
            defaultCountry = "Iran"
        ),
        MOONSIGHTING_COMMITTEE(
            id = "moonsighting",
            displayName = "Moonsighting Committee Worldwide (Shafaq)",
            fajrAngle = 18.0,
            ishaAngle = 18.0,
            defaultCountry = "Global"
        ),
        FRANCE_UOIF(
            id = "uoif",
            displayName = "UOIF — Union of Islamic Organizations of France (12°/12°)",
            fajrAngle = 12.0,
            ishaAngle = 12.0,
            defaultCountry = "France"
        ),
        RUSSIA(
            id = "russia",
            displayName = "Spiritual Administration of Muslims of Russia",
            fajrAngle = 16.0,
            ishaAngle = 15.0,
            defaultCountry = "Russia"
        ),
        CUSTOM(
            id = "custom",
            displayName = "Custom Calculation Settings",
            fajrAngle = 18.0,
            ishaAngle = 17.0,
            defaultCountry = "Custom"
        )
    }

    enum class AsrJuristic(val displayName: String, val shadowFactor: Double) {
        HANAFI("Hanafi (Shadow factor = 2)", 2.0),
        STANDARD("Standard / Shafi'i, Maliki, Hanbali (Shadow factor = 1)", 1.0)
    }

    enum class HighLatitudeRule(val displayName: String) {
        ANGLE_BASED("Angle-based Rule (1/60th of night per degree)"),
        MIDNIGHT("Midnight Rule (middle of the night)"),
        ONE_SEVENTH("One-Seventh Rule (1/7th of the night)")
    }

    /**
     * Recommends the canonical regional calculation method for any country worldwide.
     * Pakistan recommends Karachi, while users can freely switch to any method.
     */
    fun getRecommendedMethod(country: String, lat: Double = 0.0, lon: Double = 0.0): CalculationMethod {
        val c = country.trim().lowercase(Locale.ROOT)
        return when {
            c.contains("pakistan") || c.contains("india") || c.contains("bangladesh") || c.contains("afghanistan") -> CalculationMethod.KARACHI
            c.contains("saudi") || c.contains("yemen") || c.contains("oman") -> CalculationMethod.UMM_AL_QURA
            c.contains("emirates") || c.contains("uae") || c.contains("dubai") -> CalculationMethod.DUBAI
            c.contains("qatar") -> CalculationMethod.QATAR
            c.contains("kuwait") -> CalculationMethod.KUWAIT
            c.contains("egypt") || c.contains("libya") || c.contains("sudan") || c.contains("morocco") ||
                    c.contains("algeria") || c.contains("tunisia") || c.contains("syria") || c.contains("jordan") ||
                    c.contains("lebanon") || c.contains("palestine") -> CalculationMethod.EGYPTIAN
            c.contains("turkey") || c.contains("türkiye") -> CalculationMethod.TURKEY
            c.contains("united states") || c.contains("usa") || c.contains("canada") || c.contains("america") -> CalculationMethod.ISNA
            c.contains("singapore") || c.contains("malaysia") || c.contains("indonesia") || c.contains("brunei") -> CalculationMethod.SINGAPORE
            c.contains("france") -> CalculationMethod.FRANCE_UOIF
            c.contains("russia") -> CalculationMethod.RUSSIA
            c.contains("iran") -> CalculationMethod.TEHRAN
            else -> CalculationMethod.MWL // Muslim World League as worldwide global standard for UK, Europe, Australia, etc.
        }
    }

    /**
     * Recommends Asr juristic calculation method based on country's predominant tradition.
     */
    fun getRecommendedAsr(country: String): AsrJuristic {
        val c = country.trim().lowercase(Locale.ROOT)
        return if (c.contains("pakistan") || c.contains("india") || c.contains("bangladesh") || c.contains("afghanistan") || c.contains("turkey") || c.contains("türkiye")) {
            AsrJuristic.HANAFI
        } else {
            AsrJuristic.STANDARD
        }
    }

    fun isValidCoordinates(latitude: Double, longitude: Double): Boolean {
        return latitude.isFinite() &&
            longitude.isFinite() &&
            latitude in -90.0..90.0 &&
            longitude in -180.0..180.0
    }

    // Great circle initial bearing to the Holy Kaaba (degrees clockwise from True North, [0.0, 360.0))
    fun calculateQiblaBearing(userLat: Double, userLon: Double): Double {
        if (!isValidCoordinates(userLat, userLon)) return 0.0

        // Handle at-Kaaba proximity cleanly
        if (abs(userLat - KAABA_LAT) < 1e-7 && abs(userLon - KAABA_LON) < 1e-7) {
            return 0.0
        }

        // Handle geographic polar singularities cleanly
        if (userLat >= 89.999999) return 180.0
        if (userLat <= -89.999999) return 0.0

        val phi1 = Math.toRadians(userLat)
        val phi2 = Math.toRadians(KAABA_LAT)
        // Shortest longitude difference in [-180, 180]
        val deltaLonDeg = ((KAABA_LON - userLon + 540.0) % 360.0) - 180.0
        val deltaLambda = Math.toRadians(deltaLonDeg)

        val y = sin(deltaLambda) * cos(phi2)
        val x = cos(phi1) * sin(phi2) - sin(phi1) * cos(phi2) * cos(deltaLambda)
        if (abs(x) < 1e-15 && abs(y) < 1e-15) return 0.0

        val rawBearing = Math.toDegrees(atan2(y, x))
        val normalized = (rawBearing % 360.0 + 360.0) % 360.0
        return if (normalized >= 360.0 || normalized == 0.0) 0.0 else normalized
    }

    fun calculateDistanceToKaabaKm(userLat: Double, userLon: Double): Double {
        if (!isValidCoordinates(userLat, userLon)) return 0.0
        val r = 6371.0 // Earth's mean radius in km
        val dLat = Math.toRadians(KAABA_LAT - userLat)
        val deltaLonDeg = ((KAABA_LON - userLon + 540.0) % 360.0) - 180.0
        val dLon = Math.toRadians(deltaLonDeg)
        val lat1 = Math.toRadians(userLat)
        val lat2 = Math.toRadians(KAABA_LAT)
        val a = (sin(dLat / 2).pow(2) + sin(dLon / 2).pow(2) * cos(lat1) * cos(lat2)).coerceIn(0.0, 1.0)
        val c = 2 * atan2(sqrt(a), sqrt((1.0 - a).coerceAtLeast(0.0)))
        return r * c
    }

    fun calculatePrayerTimes(
        latitude: Double,
        longitude: Double,
        calendar: Calendar = Calendar.getInstance(),
        method: CalculationMethod = CalculationMethod.KARACHI,
        asrJuristic: AsrJuristic = AsrJuristic.HANAFI,
        adjustments: PrayerAdjustments = PrayerAdjustments(),
        highLatitudeRule: HighLatitudeRule = HighLatitudeRule.ANGLE_BASED,
        cityName: String = "Karachi, Pakistan",
        timeZoneId: String? = null,
        customParams: CustomCalculationParams = _calcConfig.value.customParams
    ): PrayerSchedule {
        val tz = if (!timeZoneId.isNullOrBlank()) TimeZone.getTimeZone(timeZoneId) else calendar.timeZone
        val localCal = (calendar.clone() as Calendar).apply { timeZone = tz }

        val year = localCal.get(Calendar.YEAR)
        val month = localCal.get(Calendar.MONTH) + 1
        val day = localCal.get(Calendar.DAY_OF_MONTH)
        val timeZoneOffsetHours = tz.getOffset(localCal.timeInMillis) / 3600000.0

        val effectiveFajrAngle = if (method == CalculationMethod.CUSTOM) customParams.fajrAngle else method.fajrAngle
        val effectiveIshaAngle = if (method == CalculationMethod.CUSTOM) customParams.ishaAngle else method.ishaAngle
        val effectiveIsInterval = if (method == CalculationMethod.CUSTOM) customParams.isIshaInterval else method.isIshaInterval
        val effectiveIntervalMinutes = if (method == CalculationMethod.CUSTOM) customParams.ishaIntervalMinutes else method.ishaIntervalMinutes

        // Astronomical calculations
        val jd = computeJulianDay(year, month, day)
        val d = jd - 2451545.0
        val g = fixAngle(357.529 + 0.98560028 * d)
        val q = fixAngle(280.459 + 0.98564736 * d)
        val l = fixAngle(q + 1.915 * sin(Math.toRadians(g)) + 0.020 * sin(Math.toRadians(2 * g)))
        val e = 23.439 - 0.00000036 * d
        val declination = Math.toDegrees(asin(sin(Math.toRadians(e)) * sin(Math.toRadians(l))))
        val ra = Math.toDegrees(atan2(cos(Math.toRadians(e)) * sin(Math.toRadians(l)), cos(Math.toRadians(l)))) / 15.0
        val eqTime = q / 15.0 - fixHour(ra)

        // Solar Noon (Dhuhr)
        val solarNoon = fixHour(12.0 + timeZoneOffsetHours - longitude / 15.0 - eqTime)

        // Sunrise & Sunset (accounting for atmospheric refraction 0.833 degrees)
        val sunAngle = 0.833
        val sunHourAngle = computeHourAngle(latitude, declination, sunAngle)
        val rawSunrise = solarNoon - sunHourAngle
        val rawSunset = solarNoon + sunHourAngle

        // Night length for high latitude adjustments
        val nightDurationHours = fixHour(rawSunrise - rawSunset + 24.0)

        // Fajr
        val fajrHourAngle = computeHourAngle(latitude, declination, effectiveFajrAngle)
        var rawFajr = solarNoon - fajrHourAngle
        if (rawFajr.isNaN() || fajrHourAngle.isNaN()) {
            rawFajr = applyHighLatitudeFajr(rawSunrise, nightDurationHours, effectiveFajrAngle, highLatitudeRule)
        } else if (method == CalculationMethod.MOONSIGHTING_COMMITTEE && abs(latitude) > 48.0) {
            val seasonalRule = applyHighLatitudeFajr(rawSunrise, nightDurationHours, effectiveFajrAngle, HighLatitudeRule.ONE_SEVENTH)
            if (rawFajr < seasonalRule) rawFajr = seasonalRule
        }

        // Asr
        val asrFactor = asrJuristic.shadowFactor
        val asrAngle = -Math.toDegrees(atan(1.0 / (asrFactor + tan(Math.toRadians(abs(latitude - declination))))))
        val asrHourAngle = computeHourAngle(latitude, declination, asrAngle)
        val rawAsr = solarNoon + asrHourAngle

        // Maghrib
        val rawMaghrib = rawSunset + (2.0 / 60.0) // 2 minutes precautionary buffer

        // Isha
        var rawIsha: Double
        if (effectiveIsInterval) {
            val interval = effectiveIntervalMinutes / 60.0
            rawIsha = rawMaghrib + interval
        } else {
            val ishaHourAngle = computeHourAngle(latitude, declination, effectiveIshaAngle)
            rawIsha = solarNoon + ishaHourAngle
            if (rawIsha.isNaN() || ishaHourAngle.isNaN()) {
                rawIsha = applyHighLatitudeIsha(rawSunset, nightDurationHours, effectiveIshaAngle, highLatitudeRule)
            } else if (method == CalculationMethod.MOONSIGHTING_COMMITTEE && abs(latitude) > 48.0) {
                val seasonalIsha = applyHighLatitudeIsha(rawSunset, nightDurationHours, effectiveIshaAngle, HighLatitudeRule.ONE_SEVENTH)
                if (rawIsha > seasonalIsha) rawIsha = seasonalIsha
            }
        }

        // Apply minute adjustments
        val adjustedFajr = rawFajr + (adjustments.fajrMinutes / 60.0)
        val adjustedSunrise = rawSunrise + (adjustments.sunriseMinutes / 60.0)
        val adjustedDhuhr = solarNoon + (adjustments.dhuhrMinutes / 60.0)
        val adjustedAsr = rawAsr + (adjustments.asrMinutes / 60.0)
        val adjustedMaghrib = rawMaghrib + (adjustments.maghribMinutes / 60.0)
        val adjustedIsha = rawIsha + (adjustments.ishaMinutes / 60.0)

        val dateFormatted = String.format(Locale.US, "%02d/%02d/%d", day, month, year)
        val hijriDateStr = IslamicCalendarRepository.getFormattedHijriDate(localCal)

        return PrayerSchedule(
            dateStr = dateFormatted,
            hijriDate = hijriDateStr,
            fajr = formatPrayerTime("fajr", adjustedFajr),
            sunrise = formatPrayerTime("sunrise", adjustedSunrise),
            dhuhr = formatPrayerTime("dhuhr", adjustedDhuhr),
            asr = formatPrayerTime("asr", adjustedAsr),
            maghrib = formatPrayerTime("maghrib", adjustedMaghrib),
            isha = formatPrayerTime("isha", adjustedIsha),
            calculationMethod = method.displayName,
            cityName = cityName
        )
    }

    private fun computeJulianDay(year: Int, month: Int, day: Int): Double {
        var y = year
        var m = month
        if (m <= 2) {
            y -= 1
            m += 12
        }
        val a = floor(y / 100.0)
        val b = 2 - a + floor(a / 4.0)
        return floor(365.25 * (y + 4716)) + floor(30.6001 * (m + 1)) + day + b - 1524.5
    }

    private fun computeHourAngle(lat: Double, decl: Double, angle: Double): Double {
        val cosHA = (-sin(Math.toRadians(angle)) - sin(Math.toRadians(lat)) * sin(Math.toRadians(decl))) /
                (cos(Math.toRadians(lat)) * cos(Math.toRadians(decl)))
        if (cosHA < -1.0 || cosHA > 1.0) {
            return Double.NaN
        }
        return Math.toDegrees(acos(cosHA)) / 15.0
    }

    private fun applyHighLatitudeFajr(
        sunrise: Double,
        nightHours: Double,
        fajrAngle: Double,
        rule: HighLatitudeRule
    ): Double {
        val portion = when (rule) {
            HighLatitudeRule.ANGLE_BASED -> (fajrAngle / 60.0) * nightHours
            HighLatitudeRule.MIDNIGHT -> 0.5 * nightHours
            HighLatitudeRule.ONE_SEVENTH -> (1.0 / 7.0) * nightHours
        }
        return sunrise - portion
    }

    private fun applyHighLatitudeIsha(
        sunset: Double,
        nightHours: Double,
        ishaAngle: Double,
        rule: HighLatitudeRule
    ): Double {
        val portion = when (rule) {
            HighLatitudeRule.ANGLE_BASED -> (ishaAngle / 60.0) * nightHours
            HighLatitudeRule.MIDNIGHT -> 0.5 * nightHours
            HighLatitudeRule.ONE_SEVENTH -> (1.0 / 7.0) * nightHours
        }
        return sunset + portion
    }

    fun fixHour(h: Double): Double = (h % 24.0 + 24.0) % 24.0
    fun fixAngle(a: Double): Double = (a % 360.0 + 360.0) % 360.0

    /**
     * Canonical formatter for Islamic prayer times.
     * Guarantees Fajr/Sunrise are AM, Asr/Maghrib are PM, and allows post-midnight Isha (00:00..04:00 AM)
     * in high-latitude midsummer without flipping it to noon.
     */
    fun formatPrayerTime(prayerKey: String, timeHours: Double, is24Hour: Boolean = false): String {
        if (timeHours.isNaN()) return "--:--"
        var fixed = fixHour(timeHours)

        if (prayerKey == "fajr" || prayerKey == "sunrise") {
            if (fixed >= 12.0) {
                fixed -= 12.0
            }
        } else if (prayerKey == "asr" || prayerKey == "maghrib") {
            if (fixed < 12.0) {
                fixed += 12.0
            }
        } else if (prayerKey == "isha") {
            // Only wrap daytime hours [4.0..12.0) to PM; hours [0.0..4.0) represent past-midnight night Isha
            if (fixed in 4.0..11.9999) {
                fixed += 12.0
            }
        }

        val hours = fixed.toInt()
        val totalMinutes = ((fixed - hours) * 60).roundToInt()
        val finalHours = (hours + totalMinutes / 60) % 24
        val finalMinutes = totalMinutes % 60

        return if (is24Hour) {
            String.format(Locale.US, "%02d:%02d", finalHours, finalMinutes)
        } else {
            val h12 = if (finalHours == 0) 12 else if (finalHours > 12) finalHours - 12 else finalHours
            val amPm = if (finalHours < 12) "AM" else "PM"
            String.format(Locale.US, "%02d:%02d %s", h12, finalMinutes, amPm)
        }
    }

    fun formatTime(timeHours: Double, is24Hour: Boolean = false): String {
        if (timeHours.isNaN()) return "--:--"
        val fixed = fixHour(timeHours)
        val hours = fixed.toInt()
        val totalMinutes = ((fixed - hours) * 60).roundToInt()
        val finalHours = (hours + totalMinutes / 60) % 24
        val finalMinutes = totalMinutes % 60

        return if (is24Hour) {
            String.format(Locale.US, "%02d:%02d", finalHours, finalMinutes)
        } else {
            val h12 = if (finalHours == 0) 12 else if (finalHours > 12) finalHours - 12 else finalHours
            val amPm = if (finalHours < 12) "AM" else "PM"
            String.format(Locale.US, "%02d:%02d %s", h12, finalMinutes, amPm)
        }
    }

    data class LiveNextPrayerInfo(
        val prayerKey: String, // "fajr", "dhuhr", "asr", "maghrib", "isha", "sunrise"
        val prayerNameEnglish: String,
        val prayerNameUrdu: String,
        val prayerTimeFormatted: String,
        val remainingMillis: Long,
        val remainingHours: Int,
        val remainingMinutes: Int,
        val remainingSeconds: Int,
        val progressPercent: Float
    )

    fun calculateLiveNextPrayer(
        schedule: PrayerSchedule,
        timeZoneId: String? = null,
        nowCalendar: Calendar = Calendar.getInstance()
    ): LiveNextPrayerInfo {
        val tz = if (!timeZoneId.isNullOrBlank()) TimeZone.getTimeZone(timeZoneId) else nowCalendar.timeZone
        val localCal = (nowCalendar.clone() as Calendar).apply { timeZone = tz }
        val nowMillis = localCal.timeInMillis

        // Parse each prayer time for today
        val prayers = listOf(
            Triple("fajr", "Fajr", parsePrayerTimeToMillis(schedule.fajr, localCal)),
            Triple("sunrise", "Sunrise", parsePrayerTimeToMillis(schedule.sunrise, localCal)),
            Triple("dhuhr", "Dhuhr", parsePrayerTimeToMillis(schedule.dhuhr, localCal)),
            Triple("asr", "Asr", parsePrayerTimeToMillis(schedule.asr, localCal)),
            Triple("maghrib", "Maghrib", parsePrayerTimeToMillis(schedule.maghrib, localCal)),
            Triple("isha", "Isha", parsePrayerTimeToMillis(schedule.isha, localCal))
        )

        // Find the next upcoming prayer today
        for (i in prayers.indices) {
            val (key, name, timeMillis) = prayers[i]
            if (timeMillis > nowMillis) {
                val remaining = timeMillis - nowMillis
                val totalSeconds = (remaining / 1000).toInt()
                val hrs = totalSeconds / 3600
                val mins = (totalSeconds % 3600) / 60
                val secs = totalSeconds % 60

                val prevMillis = if (i > 0) prayers[i - 1].third else timeMillis - (4 * 3600 * 1000)
                val totalWindow = (timeMillis - prevMillis).toFloat().coerceAtLeast(1f)
                val elapsed = (nowMillis - prevMillis).toFloat().coerceAtLeast(0f)
                val progress = (elapsed / totalWindow).coerceIn(0f, 1f)

                val urduName = when (key) {
                    "fajr" -> "فجر"
                    "sunrise" -> "طلوعِ آفتاب"
                    "dhuhr" -> "ظہر"
                    "asr" -> "عصر"
                    "maghrib" -> "مغرب"
                    else -> "عشاء"
                }

                val timeStr = when (key) {
                    "fajr" -> schedule.fajr
                    "sunrise" -> schedule.sunrise
                    "dhuhr" -> schedule.dhuhr
                    "asr" -> schedule.asr
                    "maghrib" -> schedule.maghrib
                    else -> schedule.isha
                }

                return LiveNextPrayerInfo(
                    prayerKey = key,
                    prayerNameEnglish = name,
                    prayerNameUrdu = urduName,
                    prayerTimeFormatted = timeStr,
                    remainingMillis = remaining,
                    remainingHours = hrs,
                    remainingMinutes = mins,
                    remainingSeconds = secs,
                    progressPercent = progress
                )
            }
        }

        // If all prayers today have passed, next prayer is Tomorrow's Fajr
        val tomorrowFajrMillis = prayers[0].third + 24 * 3600 * 1000
        val remaining = (tomorrowFajrMillis - nowMillis).coerceAtLeast(0L)
        val totalSeconds = (remaining / 1000).toInt()
        val hrs = totalSeconds / 3600
        val mins = (totalSeconds % 3600) / 60
        val secs = totalSeconds % 60

        return LiveNextPrayerInfo(
            prayerKey = "fajr",
            prayerNameEnglish = "Fajr (Tomorrow)",
            prayerNameUrdu = "فجر (کل)",
            prayerTimeFormatted = schedule.fajr,
            remainingMillis = remaining,
            remainingHours = hrs,
            remainingMinutes = mins,
            remainingSeconds = secs,
            progressPercent = 0.95f
        )
    }

    fun computePrayerTriggerTimeMillis(
        formattedTime: String,
        baseCal: Calendar,
        prayerKey: String = ""
    ): Long? {
        return try {
            val trimmed = formattedTime.trim()
            if (trimmed == "--:--" || trimmed.isBlank()) return null
            val isPm = trimmed.endsWith("PM", ignoreCase = true)
            val isAm = trimmed.endsWith("AM", ignoreCase = true)
            val clean = trimmed.replace("AM", "").replace("PM", "").trim()
            val parts = clean.split(":")
            var hour = parts[0].toInt()
            val minute = parts[1].toInt()

            if (isPm && hour < 12) hour += 12
            if (isAm && hour == 12) hour = 0

            val cal = (baseCal.clone() as Calendar).apply {
                set(Calendar.HOUR_OF_DAY, hour)
                set(Calendar.MINUTE, minute)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
                // If Isha crosses midnight (00:00..04:00 AM), it occurs the early morning after baseCal's day
                if (prayerKey == "isha" && hour < 4) {
                    add(Calendar.DAY_OF_MONTH, 1)
                }
            }
            cal.timeInMillis
        } catch (_: Exception) {
            null
        }
    }

    private fun parsePrayerTimeToMillis(formattedTime: String, baseCal: Calendar): Long {
        return computePrayerTriggerTimeMillis(formattedTime, baseCal) ?: baseCal.timeInMillis
    }

    fun generateMonthlyTimetable(
        year: Int,
        month: Int, // 1-12
        latitude: Double,
        longitude: Double,
        timeZoneId: String,
        method: CalculationMethod = CalculationMethod.KARACHI,
        asrJuristic: AsrJuristic = AsrJuristic.HANAFI,
        adjustments: PrayerAdjustments = PrayerAdjustments(),
        highLatitudeRule: HighLatitudeRule = HighLatitudeRule.ANGLE_BASED,
        customParams: CustomCalculationParams = _calcConfig.value.customParams
    ): List<PrayerSchedule> {
        val list = mutableListOf<PrayerSchedule>()
        val cal = Calendar.getInstance(TimeZone.getTimeZone(timeZoneId))
        cal.set(Calendar.YEAR, year)
        cal.set(Calendar.MONTH, month - 1)
        cal.set(Calendar.DAY_OF_MONTH, 1)

        val maxDays = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
        for (day in 1..maxDays) {
            cal.set(Calendar.DAY_OF_MONTH, day)
            val schedule = calculatePrayerTimes(
                latitude = latitude,
                longitude = longitude,
                calendar = cal,
                method = method,
                asrJuristic = asrJuristic,
                adjustments = adjustments,
                highLatitudeRule = highLatitudeRule,
                timeZoneId = timeZoneId,
                customParams = customParams
            )
            list.add(schedule)
        }
        return list
    }
}
