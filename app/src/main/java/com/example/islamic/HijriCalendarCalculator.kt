package com.example.islamic

import java.util.Calendar
import java.util.Date
import kotlin.math.floor

/**
 * Hijri Date model
 */
data class HijriDate(
  val day: Int,
  val month: Int,
  val monthNameEn: String,
  val monthNameBn: String,
  val monthNameAr: String,
  val year: Int,
  val gregorianDate: Date,
  val moonPhaseEmoji: String,
  val isWhiteDay: Boolean = false, // Ayyam al-Beed (13, 14, 15)
  val keyEvent: IslamicEvent? = null
) {
  fun formatEn(): String = "$day $monthNameEn, $year AH"
  fun formatBn(): String = "$day $monthNameBn, $year হিজরি"
  fun formatAr(): String = "$day $monthNameAr $year هـ"

  fun format(lang: String): String = when (lang) {
    "EN" -> formatEn()
    "AR" -> formatAr()
    else -> formatBn()
  }
}

/**
 * Significant Islamic Event model
 */
data class IslamicEvent(
  val hijriMonth: Int,
  val hijriDay: Int,
  val titleEn: String,
  val titleBn: String,
  val titleAr: String,
  val descriptionEn: String,
  val descriptionBn: String,
  val badgeColorHex: Long = 0xFF0A5C36,
  val iconEmoji: String
)

object IslamicEventsMaster {
  val allEvents = listOf(
    IslamicEvent(
      hijriMonth = 1,
      hijriDay = 1,
      titleEn = "Islamic New Year",
      titleBn = "পবিত্র হিজরি নববর্ষ",
      titleAr = "رأس السنة الهجرية",
      descriptionEn = "1st of Muharram, commencement of the Islamic lunar calendar year.",
      descriptionBn = "১লা মহররম, হিজরি চান্দ্রবর্ষের সূচনা দিন।",
      badgeColorHex = 0xFF0A5C36,
      iconEmoji = "🌙"
    ),
    IslamicEvent(
      hijriMonth = 1,
      hijriDay = 10,
      titleEn = "Day of Ashura",
      titleBn = "পবিত্র আশুরা",
      titleAr = "يوم عاشوراء",
      descriptionEn = "Fasting day commemorating Prophet Musa's (AS) deliverance and Karbala remembrance.",
      descriptionBn = "১০ই মহররম, মুসা (আঃ)-এর মুক্তি ও ঐতিহাসিক কারবালার স্মৃতিবিজড়িত রোজা রাখার দিন।",
      badgeColorHex = 0xFF881337,
      iconEmoji = "🕌"
    ),
    IslamicEvent(
      hijriMonth = 3,
      hijriDay = 12,
      titleEn = "Mawlid an-Nabi (PBUH)",
      titleBn = "পবিত্র ঈদে মিলাদুন্নবী (সাঃ)",
      titleAr = "المولد النبوي الشريف",
      descriptionEn = "Commemorating the birth of the final Prophet Muhammad (PBUH).",
      descriptionBn = "১২ই রবিউল আউয়াল, বিশ্বনবী হযরত মুহাম্মদ (সাঃ)-এর পবিত্র জন্ম ও ওফাত দিবস।",
      badgeColorHex = 0xFF15803D,
      iconEmoji = "✨"
    ),
    IslamicEvent(
      hijriMonth = 7,
      hijriDay = 27,
      titleEn = "Shab-e-Meraj (Al-Isra wal-Mi'raj)",
      titleBn = "পবিত্র শবে মেরাজ",
      titleAr = "الإسراء والمعراج",
      descriptionEn = "The miraculous night journey of Prophet Muhammad (PBUH) through the heavens.",
      descriptionBn = "২৭শে রজব, প্রিয় নবী (সাঃ)-এর ঊর্ধ্বাকাশে মিরাজ গমনের বরকতময় রজনী।",
      badgeColorHex = 0xFF4338CA,
      iconEmoji = "🌌"
    ),
    IslamicEvent(
      hijriMonth = 8,
      hijriDay = 15,
      titleEn = "Shab-e-Barat (Laylat al-Bara'ah)",
      titleBn = "পবিত্র শবে বরাত",
      titleAr = "ليلة النصف من شعبان",
      descriptionEn = "The Night of Fortune and forgiveness on the 15th night of Sha'ban.",
      descriptionBn = "১৫ই শাবান, সৌভাগ্য ও পাপ মোচনের বরকতময় লাইলাতুল বরাত।",
      badgeColorHex = 0xFF0369A1,
      iconEmoji = "🌟"
    ),
    IslamicEvent(
      hijriMonth = 9,
      hijriDay = 1,
      titleEn = "Beginning of Holy Ramadan",
      titleBn = "পবিত্র রমজানুল মুবারক শুরু",
      titleAr = "بداية شهر رمضان المبارك",
      descriptionEn = "The first day of the sacred month of fasting, reflection and Quran recitation.",
      descriptionBn = "১লা রমজান, সিয়াম সাধনা ও রহমত-মাগফিরাতের পবিত্র মাসের শুভ সূচনা।",
      badgeColorHex = 0xFF047857,
      iconEmoji = "🌙"
    ),
    IslamicEvent(
      hijriMonth = 9,
      hijriDay = 27,
      titleEn = "Shab-e-Qadr (Laylat al-Qadr)",
      titleBn = "পবিত্র শবে কদর (লাইলাতুল কদর)",
      titleAr = "ليلة القدر",
      descriptionEn = "The Night of Power, better than a thousand months (Quran 97:3).",
      descriptionBn = "২৭শে রমজান, হাজার মাসের চেয়েও শ্রেষ্ঠ পরম বরকতময় কদরের রজনী।",
      badgeColorHex = 0xFFD97706,
      iconEmoji = "👑"
    ),
    IslamicEvent(
      hijriMonth = 10,
      hijriDay = 1,
      titleEn = "Eid-ul-Fitr",
      titleBn = "পবিত্র ঈদুল ফিতর",
      titleAr = "عيد الفطر المبارك",
      descriptionEn = "Celebration marking the conclusion of Ramadan fasting and charity (Sadaqatul Fitr).",
      descriptionBn = "১লা শাওয়াল, মাসব্যাপী সিয়াম সমাপ্তির মহিমান্বিত আনন্দের দিন।",
      badgeColorHex = 0xFF0D9488,
      iconEmoji = "🎉"
    ),
    IslamicEvent(
      hijriMonth = 12,
      hijriDay = 9,
      titleEn = "Day of Arafah",
      titleBn = "পবিত্র ইয়াওমে আরাফাহ (আরাফার দিন)",
      titleAr = "يوم عرفة",
      descriptionEn = "The peak day of Hajj pilgrimage, forgiving sins of past and coming year through fasting.",
      descriptionBn = "৯ই জিলহজ, হজের মূল দিন ও দুই বছরের গুনাহ মাফের বরকতময় নফল রোজার দিন।",
      badgeColorHex = 0xFFB45309,
      iconEmoji = "⛰️"
    ),
    IslamicEvent(
      hijriMonth = 12,
      hijriDay = 10,
      titleEn = "Eid-ul-Adha",
      titleBn = "পবিত্র ঈদুল আজহা (কোরবানি)",
      titleAr = "عيد الأضحى المبارك",
      descriptionEn = "Feast of Sacrifice honoring Prophet Ibrahim's (AS) devotion to Allah.",
      descriptionBn = "১০ই জিলহজ, হযরত ইব্রাহিম (আঃ)-এর আত্মত্যাগের স্মরণে ত্যাগের মহাপর্ব।",
      badgeColorHex = 0xFF0A5C36,
      iconEmoji = "🐑"
    ),
    IslamicEvent(
      hijriMonth = 12,
      hijriDay = 11,
      titleEn = "Days of Tashreeq",
      titleBn = "আইয়ামে তাশরীক শুরু",
      titleAr = "أيام التشريق",
      descriptionEn = "11th to 13th of Dhu al-Hijjah, days of eating, drinking and remembering Allah (Takbeer Tashreeq).",
      descriptionBn = "১১-১৩ই জিলহজ, তাকবীরে তাশরীক পাঠ ও ত্যাগের পবিত্র দিনসমূহ।",
      badgeColorHex = 0xFF475569,
      iconEmoji = "📿"
    )
  )
}

/**
 * Astronomical Hijri Converter with Moon Phase & Local Sighting Adjustment
 */
object HijriCalendarCalculator {

  val hijriMonthsEn = listOf(
    "Muharram", "Safar", "Rabi' al-Awwal", "Rabi' al-Thani",
    "Jumada al-Awwal", "Jumada al-Thani", "Rajab", "Sha'ban",
    "Ramadan", "Shawwal", "Dhu al-Qi'dah", "Dhu al-Hijjah"
  )

  val hijriMonthsBn = listOf(
    "মুহররম", "সফর", "রবিউল আউয়াল", "রবিউস সানি",
    "জমাদিউল আউয়াল", "জমাদিউস সানি", "রজব", "শাবান",
    "রমজান", "শাওয়াল", "জিলকদ", "জিলহজ"
  )

  val hijriMonthsAr = listOf(
    "المحرّم", "صفر", "ربيع الأوّل", "ربيع الثاني",
    "جمادى الأولى", "جمادى الآخرة", "رجب", "شعبان",
    "رمضان", "شوّال", "ذو القعدة", "ذو الحجة"
  )

  /**
   * Convert Julian Day to Hijri Date
   * Uses the astronomical Kuwaiti Algorithm with adjustable day offset (-2 to +2)
   */
  fun calculateHijri(calendar: Calendar = Calendar.getInstance(), dayAdjustment: Int = 0): HijriDate {
    val cal = calendar.clone() as Calendar
    if (dayAdjustment != 0) {
      cal.add(Calendar.DAY_OF_YEAR, dayAdjustment)
    }

    val y = cal.get(Calendar.YEAR)
    val m = cal.get(Calendar.MONTH) + 1
    val d = cal.get(Calendar.DAY_OF_MONTH)

    // Julian day calculation
    var year = y
    var month = m
    if (month <= 2) {
      year -= 1
      month += 12
    }
    val a = floor(year / 100.0)
    val b = 2 - a + floor(a / 4.0)
    val jd = floor(365.25 * (year + 4716)) + floor(30.6001 * (month + 1)) + d + b - 1524.5

    // Hijri conversion algorithm
    val epoch = 1948439.5
    val z = jd - epoch
    val cyc = floor(z / 10631.0)
    val rem = z - 10631.0 * cyc
    val j = floor((rem - 0.5) / 354.36667)
    val rem2 = rem - floor(354.36667 * j + 0.5)

    var hijriMonth = floor((rem2 + 0.5) / 29.5).toInt() + 1
    var hijriDay = (rem2 + 0.5 - floor(29.5 * (hijriMonth - 1))).toInt()
    var hijriYear = (30 * cyc + j).toInt() + 1

    if (hijriDay <= 0) {
      hijriMonth -= 1
      if (hijriMonth <= 0) {
        hijriMonth = 12
        hijriYear -= 1
      }
      hijriDay += 29
    }

    if (hijriMonth > 12) {
      hijriMonth = 1
      hijriYear += 1
    }

    val monthIndex = (hijriMonth - 1).coerceIn(0, 11)
    val mEn = hijriMonthsEn[monthIndex]
    val mBn = hijriMonthsBn[monthIndex]
    val mAr = hijriMonthsAr[monthIndex]

    // Moon phase emoji based on lunar age
    val lunarAge = hijriDay.coerceIn(1, 30)
    val moonEmoji = when {
      lunarAge == 1 -> "🌑" // New moon
      lunarAge in 2..6 -> "🌒" // Waxing crescent
      lunarAge in 7..9 -> "🌓" // First quarter
      lunarAge in 10..13 -> "🌔" // Waxing gibbous
      lunarAge in 14..16 -> "🌕" // Full moon (Badr)
      lunarAge in 17..21 -> "🌖" // Waning gibbous
      lunarAge in 22..24 -> "🌗" // Last quarter
      lunarAge in 25..29 -> "🌘" // Waning crescent
      else -> "🌑"
    }

    val isWhiteDay = hijriDay in 13..15

    val keyEvent = IslamicEventsMaster.allEvents.firstOrNull {
      it.hijriMonth == hijriMonth && it.hijriDay == hijriDay
    }

    return HijriDate(
      day = hijriDay,
      month = hijriMonth,
      monthNameEn = mEn,
      monthNameBn = mBn,
      monthNameAr = mAr,
      year = hijriYear,
      gregorianDate = calendar.time,
      moonPhaseEmoji = moonEmoji,
      isWhiteDay = isWhiteDay,
      keyEvent = keyEvent
    )
  }

  /**
   * Find upcoming Islamic events from the given Hijri date
   */
  fun getUpcomingEvents(currentHijri: HijriDate): List<Pair<IslamicEvent, Int>> {
    val currentDayOfYear = (currentHijri.month - 1) * 30 + currentHijri.day

    return IslamicEventsMaster.allEvents.map { event ->
      val eventDayOfYear = (event.hijriMonth - 1) * 30 + event.hijriDay
      var diffDays = eventDayOfYear - currentDayOfYear
      if (diffDays < 0) {
        diffDays += 354 // Next Hijri year
      }
      Pair(event, diffDays)
    }.sortedBy { it.second }
  }
}
