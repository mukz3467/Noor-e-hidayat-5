package com.example.data.notification

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.AlarmManagerCompat
import androidx.core.app.NotificationCompat
import com.example.MainActivity
import com.example.R
import com.example.data.model.AppLanguage
import com.example.data.model.AppSettingsState
import com.example.data.repository.DuaData
import com.example.data.repository.HadithData
import com.example.data.repository.QuranLearningRepository
import com.example.data.repository.UserPreferencesRepository
import com.example.localization.AppLocalization
import com.example.localization.LocalizedContentResolver
import java.util.Calendar

enum class DailyReminderType(
    val id: String,
    val requestCode: Int,
    val defaultHour: Int,
    val defaultMinute: Int
) {
    QURAN("daily_quran", 981001, 7, 0),
    HADITH("daily_hadith", 981002, 10, 0),
    DUA("daily_dua", 981003, 13, 30),
    MORNING_ADHKAR("morning_adhkar", 981004, 6, 0),
    EVENING_ADHKAR("evening_adhkar", 981005, 17, 30),
    HIFZ_REVISION("hifz_revision", 981006, 20, 0)
}

object DailyReminderScheduler {

    const val CHANNEL_ID_REMINDERS = "noor_daily_reminders_v1"
    const val ACTION_DAILY_REMINDER = "com.example.ACTION_DAILY_REMINDER"
    const val EXTRA_REMINDER_TYPE = "extra_reminder_type"

    fun ensureChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager ?: return
            val channel = NotificationChannel(
                CHANNEL_ID_REMINDERS,
                "Daily Quran, Hadith & Adhkar Reminders",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Scheduled daily Islamic reminders and Hifz revision alerts"
            }
            nm.createNotificationChannel(channel)
        }
    }

    fun isWithinDndWindow(hourOfDay: Int, settings: AppSettingsState): Boolean {
        if (!settings.dndEnabled) return false
        val start = settings.dndStartHour.coerceIn(0, 23)
        val end = settings.dndEndHour.coerceIn(0, 23)
        if (start == end) return false
        return if (start < end) {
            hourOfDay in start until end
        } else {
            hourOfDay >= start || hourOfDay < end
        }
    }

    fun rescheduleAll(context: Context, nowMillis: Long = System.currentTimeMillis()): Int {
        ensureChannel(context)
        val repo = UserPreferencesRepository(context)
        val settings = repo.appSettings.value
        val am = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
        var scheduledCount = 0

        val hifzEnabled = try {
            QuranLearningRepository.getProgress(context).hifzRevisionReminderEnabled
        } catch (_: Exception) {
            false
        }

        DailyReminderType.entries.forEach { type ->
            val enabled = settings.notificationsEnabled && when (type) {
                DailyReminderType.QURAN -> settings.dailyQuranReminderEnabled
                DailyReminderType.HADITH -> settings.dailyHadithReminderEnabled
                DailyReminderType.DUA -> settings.dailyDuaReminderEnabled
                DailyReminderType.MORNING_ADHKAR -> settings.morningAdhkarReminderEnabled
                DailyReminderType.EVENING_ADHKAR -> settings.eveningAdhkarReminderEnabled
                DailyReminderType.HIFZ_REVISION -> hifzEnabled
            }

            val intent = Intent(context, PrayerAlarmReceiver::class.java).apply {
                action = ACTION_DAILY_REMINDER
                putExtra(EXTRA_REMINDER_TYPE, type.id)
            }

            if (!enabled) {
                val existingPi = PendingIntent.getBroadcast(
                    context,
                    type.requestCode,
                    intent,
                    PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
                )
                if (existingPi != null) {
                    am?.cancel(existingPi)
                    runCatching { existingPi.cancel() }
                }
            } else {
                val hour = if (type == DailyReminderType.QURAN) settings.dailyQuranReminderHour else type.defaultHour
                val minute = if (type == DailyReminderType.QURAN) settings.dailyQuranReminderMinute else type.defaultMinute
                val triggerAt = nextTriggerMillis(hour, minute, nowMillis)
                val pi = PendingIntent.getBroadcast(
                    context,
                    type.requestCode,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
                if (am != null) {
                    AlarmManagerCompat.setAndAllowWhileIdle(am, AlarmManager.RTC_WAKEUP, triggerAt, pi)
                }
                scheduledCount++
            }
        }
        return scheduledCount
    }

    fun nextTriggerMillis(hour: Int, minute: Int, nowMillis: Long = System.currentTimeMillis()): Long {
        val cal = Calendar.getInstance().apply {
            timeInMillis = nowMillis
            set(Calendar.HOUR_OF_DAY, hour.coerceIn(0, 23))
            set(Calendar.MINUTE, minute.coerceIn(0, 59))
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        if (cal.timeInMillis <= nowMillis) {
            cal.add(Calendar.DAY_OF_MONTH, 1)
        }
        return cal.timeInMillis
    }

    fun handleReminderAlarm(context: Context, typeId: String?) {
        val type = DailyReminderType.entries.firstOrNull { it.id == typeId } ?: DailyReminderType.QURAN
        val repo = UserPreferencesRepository(context)
        val settings = repo.appSettings.value

        if (settings.notificationsEnabled && PrayerNotificationManager.hasNotificationPermission(context)) {
            val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
            val suppressedByDnd = isWithinDndWindow(currentHour, settings)
            if (!suppressedByDnd) {
                postReminderNotification(context, type, settings.language)
            }
        }

        // Schedule next day's reminder cleanly
        rescheduleAll(context)
    }

    fun postReminderNotification(
        context: Context,
        type: DailyReminderType,
        language: AppLanguage
    ) {
        ensureChannel(context)
        if (!PrayerNotificationManager.hasNotificationPermission(context)) return

        val (title, body) = buildReminderContent(type, language)
        val openAppIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val pi = PendingIntent.getActivity(
            context,
            type.requestCode,
            openAppIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID_REMINDERS)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(body)
            .setStyle(NotificationCompat.BigTextStyle().bigText(body))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pi)
            .setAutoCancel(true)
            .build()

        val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager ?: return
        nm.notify(type.requestCode, notification)
    }

    private fun buildReminderContent(type: DailyReminderType, lang: AppLanguage): Pair<String, String> {
        return when (type) {
            DailyReminderType.QURAN -> when (lang) {
                AppLanguage.URDU -> "روزانہ تلاوتِ قرآن کریم" to "آج کی تلاوت، ترجمہ اور تدبر کے لیے چند منٹ نکالیں۔"
                AppLanguage.ARABIC -> "ورد القرآن اليومي" to "خصص وقتاً لتلاوة القرآن الكريم وتدبر معانيه اليوم."
                AppLanguage.HINDI -> "दैनिक क़ुरआन तिलावत" to "आज के लिए क़ुरआन की तिलावत और तर्जुमा पढ़ने का समय निकालें।"
                AppLanguage.ENGLISH -> "Daily Quran Recitation" to "Take a peaceful moment today to recite and reflect on the Noble Quran."
            }
            DailyReminderType.HADITH -> {
                val sample = HadithData.hadiths.firstOrNull()
                val textSnippet = if (sample != null) {
                    when (lang) {
                        AppLanguage.ARABIC -> sample.textArabic.take(140)
                        else -> {
                            val resolved = LocalizedContentResolver.resolveHadithTranslation(sample, lang)
                            if (resolved.isAvailable && !resolved.text.isNullOrBlank()) {
                                resolved.text.take(140)
                            } else {
                                resolved.unavailableNotice ?: LocalizedContentResolver.unavailableHadithTranslationNotice(lang)
                            }
                        }
                    }
                } else {
                    when (lang) {
                        AppLanguage.URDU -> "نبی کریم ﷺ کی مستند حدیث کا مطالعہ کریں۔"
                        AppLanguage.ARABIC -> "اقرأ حديثاً شريفاً وتأمل في هدي النبي ﷺ."
                        AppLanguage.HINDI -> "आज की प्रामाणिक हदीस पढ़ें और अमल करें।"
                        AppLanguage.ENGLISH -> "Read today's authentic Hadith from Sahih al-Bukhari."
                    }
                }
                val title = AppLocalization.get("notif_daily_hadith_title", lang)
                title to textSnippet
            }
            DailyReminderType.DUA -> {
                val dua = DuaData.getDailyFeaturedDua()
                val duaTitle = LocalizedContentResolver.resolveDuaTitle(dua, lang)
                val title = "${AppLocalization.get("notif_daily_dua_title", lang)}: $duaTitle"
                val body = when (lang) {
                    AppLanguage.ARABIC -> dua.arabic
                    else -> {
                        val resolved = LocalizedContentResolver.resolveDuaTranslation(dua, lang)
                        if (resolved.isAvailable && !resolved.text.isNullOrBlank()) {
                            resolved.text
                        } else {
                            dua.arabic
                        }
                    }
                }
                title to body
            }
            DailyReminderType.MORNING_ADHKAR -> when (lang) {
                AppLanguage.URDU -> "صبح کے مسنون اذکار" to "حفاظت، برکت اور سکونِ قلب کے لیے صبح کے اذکار پڑھیں۔"
                AppLanguage.ARABIC -> "أذكار الصباح" to "ابدأ يومك بأذكار الصباح المأثورة لحفظ الله وبركته."
                AppLanguage.HINDI -> "सुबह के अज़कार" to "दिन की शुरुआत बरकत और हिफ़ाज़त के लिए सुबह के अज़कार से करें।"
                AppLanguage.ENGLISH -> "Morning Adhkar" to "Begin your morning with authentic Prophetic supplications and remembrance of Allah."
            }
            DailyReminderType.EVENING_ADHKAR -> when (lang) {
                AppLanguage.URDU -> "شام کے مسنون اذکار" to "دن کے اختتام پر شام کے مسنون اذکار اور استغفار کا اہتمام کریں۔"
                AppLanguage.ARABIC -> "أذكار المساء" to "اختم يومك بأذكار المساء والاستغفار والتحصين."
                AppLanguage.HINDI -> "शाम के अज़कार" to "शाम के मसनून अज़कार और इस्तिग़फ़ार का एहतिमाम करें।"
                AppLanguage.ENGLISH -> "Evening Adhkar" to "End your afternoon with authentic evening Adhkar and gratitude to Allah."
            }
            DailyReminderType.HIFZ_REVISION -> when (lang) {
                AppLanguage.URDU -> "حفظ اور سبق کی دہرائی" to "اپنے روزانہ قرآن لرننگ ہدف اور حفظ کی دہرائی مکمل کریں۔"
                AppLanguage.ARABIC -> "مراجعة الحفظ والتعلم" to "لا تنسَ مراجعة حفظك وإتمام وردك التعليمي اليوم."
                AppLanguage.HINDI -> "हिफ़्ज़ और पाठ दोहराने का समय" to "अपना दैनिक क़ुरआन लर्निंग और हिफ़्ज़ दोहराव पूरा करें।"
                AppLanguage.ENGLISH -> "Hifz & Academy Revision" to "Keep your streak alive — review your memorized Surahs and daily Academy lesson."
            }
        }
    }
}
