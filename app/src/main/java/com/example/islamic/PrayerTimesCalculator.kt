package com.example.islamic

import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import kotlin.math.abs
import kotlin.math.acos
import kotlin.math.asin
import kotlin.math.atan
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.floor
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.math.tan

/**
 * Calculation Methods for Prayer Times
 * With comprehensive Asian calculation methods:
 * - Karachi (Bangladesh, India, Pakistan, South Asia)
 * - Makkah (Umm al-Qura, Saudi Arabia & Middle East)
 * - MWL (Muslim World League)
 * - Egypt (Egyptian General Authority of Survey)
 * - MUIS (Singapore)
 * - JAKIM (Malaysia)
 * - KEMENAG (Indonesia)
 * - Dubai (UAE)
 * - ISNA (North America)
 */
enum class CalculationMethod(
  val titleEn: String,
  val titleBn: String,
  val region: String,
  val fajrAngle: Double,
  val ishaAngle: Double,
  val ishaIntervalMinutes: Int? = null
) {
  KARACHI(
    titleEn = "Univ. of Islamic Sciences, Karachi",
    titleBn = "করাচি বিশ্ববিদ্যালয় (বাংলাদেশ ও দক্ষিণ এশিয়া)",
    region = "South Asia",
    fajrAngle = 18.0,
    ishaAngle = 18.0
  ),
  MAKKAH(
    titleEn = "Umm al-Qura University, Makkah",
    titleBn = "উম্মুল কুরা বিশ্ববিদ্যালয়, মক্কা (মধ্যপ্রাচ্য)",
    region = "Middle East",
    fajrAngle = 18.5,
    ishaAngle = 0.0,
    ishaIntervalMinutes = 90
  ),
  MWL(
    titleEn = "Muslim World League (MWL)",
    titleBn = "মুসলিম ওয়ার্ল্ড লীগ (রাবেতা)",
    region = "Global",
    fajrAngle = 18.0,
    ishaAngle = 17.0
  ),
  EGYPT(
    titleEn = "Egyptian General Authority of Survey",
    titleBn = "মিশরীয় জেনেরাল সার্ভে",
    region = "Africa & Middle East",
    fajrAngle = 19.5,
    ishaAngle = 17.5
  ),
  MUIS(
    titleEn = "MUIS (Majlis Ugama Islam Singapura)",
    titleBn = "এমইউআইএস - সিঙ্গাপুর ইসলামিক কাউন্সিল",
    region = "Singapore",
    fajrAngle = 20.0,
    ishaAngle = 18.0
  ),
  JAKIM(
    titleEn = "JAKIM (Jabatan Kemajuan Islam Malaysia)",
    titleBn = "জাকিম - মালয়েশিয়া ইসলামিক উন্নয়ন বিভাগ",
    region = "Malaysia",
    fajrAngle = 20.0,
    ishaAngle = 18.0
  ),
  KEMENAG(
    titleEn = "KEMENAG (Kementerian Agama RI)",
    titleBn = "কেমেনাং - ইন্দোনেশিয়া ধর্মীয় মন্ত্রণালয়",
    region = "Indonesia",
    fajrAngle = 20.0,
    ishaAngle = 18.0
  ),
  DUBAI(
    titleEn = "Dubai / UAE Awqaf",
    titleBn = "দুবাই / সংযুক্ত আরব আমিরাত",
    region = "Gulf",
    fajrAngle = 18.2,
    ishaAngle = 18.2
  ),
  ISNA(
    titleEn = "Islamic Society of North America (ISNA)",
    titleBn = "ইসলামিক সোসাইটি অফ নর্থ আমেরিকা",
    region = "North America",
    fajrAngle = 15.0,
    ishaAngle = 15.0
  )
}

/**
 * Juristic Method for Asr prayer calculation
 */
enum class JuristicMethod(
  val titleEn: String,
  val titleBn: String,
  val shadowFactor: Double
) {
  HANAFI("Hanafi (Shadow factor 2)", "হানাফি (ছায়ার অনুপাত ২ - বাংলাদেশ/ভারত/পাকিস্তান)", 2.0),
  SHAFI("Shafi'i / Maliki / Hanbali (Shadow factor 1)", "শাফেয়ী / মালেকী / হাম্বলী (অনুপাত ১ - আরব/আসিয়ান)", 1.0)
}

/**
 * Single Prayer Entry
 */
data class PrayerEntry(
  val id: String,
  val nameEn: String,
  val nameBn: String,
  val nameAr: String,
  val iconEmoji: String,
  val timeDecimalHours: Double,
  val timeMillis: Long,
  val timeFormatted: String,
  val isCurrent: Boolean = false,
  val isNext: Boolean = false,
  val isOptional: Boolean = false
)

/**
 * Complete Daily Prayer Times Result
 */
data class DailyPrayerSchedule(
  val fajr: PrayerEntry,
  val sunrise: PrayerEntry,
  val dhuhr: PrayerEntry,
  val asr: PrayerEntry,
  val maghrib: PrayerEntry,
  val isha: PrayerEntry,
  val tahajjud: PrayerEntry,
  val currentPrayer: PrayerEntry,
  val nextPrayer: PrayerEntry,
  val timeUntilNextMinutes: Long,
  val locationName: String,
  val latitude: Double,
  val longitude: Double,
  val timezoneOffsetHours: Double,
  val method: CalculationMethod,
  val juristicMethod: JuristicMethod
) {
  fun allPrayers(): List<PrayerEntry> = listOf(
    fajr, sunrise, dhuhr, asr, maghrib, isha, tahajjud
  )

  fun compulsoryPrayers(): List<PrayerEntry> = listOf(
    fajr, dhuhr, asr, maghrib, isha
  )
}

/**
 * Rock-Solid Astronomical Prayer Times Calculator
 * Accurately handles local solar time, Equation of Time, and timezones
 */
object PrayerTimesCalculator {

  private fun degToRad(deg: Double): Double = deg * Math.PI / 180.0
  private fun radToDeg(rad: Double): Double = rad * 180.0 / Math.PI

  private fun fixAngle(deg: Double): Double {
    var a = deg - 360.0 * floor(deg / 360.0)
    if (a < 0) a += 360.0
    return a
  }

  private fun fixHour(hour: Double): Double {
    var h = hour - 24.0 * floor(hour / 24.0)
    if (h < 0) h += 24.0
    return h
  }

  /**
   * Format decimal hours (e.g. 12.05 -> "12:03 PM", 4.75 -> "04:45 AM")
   * This completely bypasses device/emulator timezone bias!
   */
  fun formatDecimalHours(hour: Double): String {
    val totalMinutes = (fixHour(hour) * 60.0).roundToInt()
    var h = (totalMinutes / 60) % 24
    val m = totalMinutes % 60
    val amPm = if (h < 12) "AM" else "PM"
    val displayHour = when {
      h == 0 -> 12
      h > 12 -> h - 12
      else -> h
    }
    return String.format(Locale.US, "%02d:%02d %s", displayHour, m, amPm)
  }

  /**
   * Calculate Julian Day for given Gregorian year, month (1-12), day
   */
  private fun julianDay(year: Int, month: Int, day: Int): Double {
    var y = year
    var m = month
    if (m <= 2) {
      y -= 1
      m += 12
    }
    val a = floor(y / 100.0)
    val b = 2.0 - a + floor(a / 4.0)
    return floor(365.25 * (y + 4716)) + floor(30.6001 * (m + 1)) + day + b - 1524.5
  }

  /**
   * Accurate Solar Position:
   * Returns Pair(Declination in rad, Equation of Time in hours)
   * Note: Equation of Time is normalized within [-0.5, +0.5] hours (±20 minutes)
   */
  private fun sunPosition(jd: Double): Pair<Double, Double> {
    val d = jd - 2451545.0
    val g = fixAngle(357.529 + 0.98560028 * d)
    val q = fixAngle(280.459 + 0.98564736 * d)
    val l = fixAngle(q + 1.915 * sin(degToRad(g)) + 0.020 * sin(degToRad(2.0 * g)))

    val e = 23.439 - 0.00000036 * d
    val dRad = degToRad(d)

    val raRad = atan2(cos(degToRad(e)) * sin(degToRad(l)), cos(degToRad(l)))
    val raHours = fixHour(radToDeg(raRad) / 15.0)

    val declinationRad = asin(sin(degToRad(e)) * sin(degToRad(l)))

    // Equation of Time in hours
    var eqOfTime = fixHour(q / 15.0) - raHours
    if (eqOfTime > 12.0) eqOfTime -= 24.0
    if (eqOfTime < -12.0) eqOfTime += 24.0

    return Pair(declinationRad, eqOfTime)
  }

  /**
   * Compute hour angle for an altitude angle (in degrees)
   */
  private fun computeHourAngle(latRad: Double, decRad: Double, altitudeDeg: Double): Double {
    val altRad = degToRad(altitudeDeg)
    val cosHA = (sin(altRad) - sin(latRad) * sin(decRad)) / (cos(latRad) * cos(decRad))
    return if (cosHA > 1.0) 0.0 else if (cosHA < -1.0) 180.0 else radToDeg(acos(cosHA))
  }

  /**
   * Compute hour angle for Asr shadow factor
   */
  private fun computeAsrHourAngle(latRad: Double, decRad: Double, shadowFactor: Double): Double {
    val lat = radToDeg(latRad)
    val dec = radToDeg(decRad)
    val delta = abs(lat - dec)
    val altRad = atan(1.0 / (shadowFactor + tan(degToRad(delta))))
    val altDeg = radToDeg(altRad)
    return computeHourAngle(latRad, decRad, altDeg)
  }

  /**
   * Determine timezone from longitude or system timezone
   */
  fun resolveTimezone(longitude: Double, systemTzHours: Double? = null): Double {
    val geoTz = (longitude / 15.0).roundToInt().toDouble()
    if (systemTzHours != null) {
      // If system timezone is within 1.5 hours of longitude-based timezone, trust system timezone
      if (abs(systemTzHours - geoTz) <= 1.5) {
        return systemTzHours
      }
    }
    return geoTz
  }

  /**
   * Calculate prayer times for a specific date, location and calculation method
   */
  fun calculate(
    calendar: Calendar = Calendar.getInstance(),
    latitude: Double,
    longitude: Double,
    timezoneOffset: Double? = null,
    locationName: String = "Current Location",
    method: CalculationMethod = CalculationMethod.KARACHI,
    juristicMethod: JuristicMethod = JuristicMethod.HANAFI
  ): DailyPrayerSchedule {
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH) + 1
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    val systemTz = calendar.timeZone.rawOffset / 3600000.0 +
      if (calendar.timeZone.inDaylightTime(calendar.time)) 1.0 else 0.0

    val tzHours = timezoneOffset ?: resolveTimezone(longitude, systemTz)

    val jd = julianDay(year, month, day)
    val (decRad, eqOfTime) = sunPosition(jd)
    val latRad = degToRad(latitude)

    // Solar noon in UTC hours: 12 - (longitude / 15) - eqOfTime
    // In Local Time: 12 + timezone - (longitude / 15) - eqOfTime + 2 min buffer
    val noonUtc = 12.0 - (longitude / 15.0) - eqOfTime
    val noonLocal = fixHour(noonUtc + tzHours + (2.0 / 60.0))

    // Sunrise & Sunset: sun center is 50 arcminutes (-0.833°) below horizon
    val sunAngleSunRise = computeHourAngle(latRad, decRad, -0.833) / 15.0
    val sunriseHour = fixHour(noonLocal - sunAngleSunRise)
    val sunsetHour = fixHour(noonLocal + sunAngleSunRise)

    // Fajr
    val fajrHA = computeHourAngle(latRad, decRad, -method.fajrAngle) / 15.0
    val fajrHour = fixHour(noonLocal - fajrHA)

    // Asr
    val asrHA = computeAsrHourAngle(latRad, decRad, juristicMethod.shadowFactor) / 15.0
    val asrHour = fixHour(noonLocal + asrHA)

    // Maghrib is sunset
    val maghribHour = sunsetHour

    // Isha
    val ishaHour = if (method.ishaIntervalMinutes != null) {
      fixHour(maghribHour + (method.ishaIntervalMinutes / 60.0))
    } else {
      val ishaHA = computeHourAngle(latRad, decRad, -method.ishaAngle) / 15.0
      fixHour(noonLocal + ishaHA)
    }

    // Tahajjud: Middle of last third of night (between Maghrib and next Fajr)
    val nightDurationHours = fixHour(fajrHour + 24.0 - maghribHour)
    val tahajjudHour = fixHour(maghribHour + (nightDurationHours * 2.0 / 3.0))

    // Calculate epoch millis for accurate countdown
    fun toEpochMillis(localHour: Double): Long {
      val cal = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
        set(Calendar.YEAR, year)
        set(Calendar.MONTH, month - 1)
        set(Calendar.DAY_OF_MONTH, day)
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
      }
      val startOfDayUtc = cal.timeInMillis
      val hourInUtc = fixHour(localHour - tzHours)
      return startOfDayUtc + (hourInUtc * 3600 * 1000L).toLong()
    }

    val fajrMillis = toEpochMillis(fajrHour)
    val sunriseMillis = toEpochMillis(sunriseHour)
    val dhuhrMillis = toEpochMillis(noonLocal)
    val asrMillis = toEpochMillis(asrHour)
    val maghribMillis = toEpochMillis(maghribHour)
    val ishaMillis = toEpochMillis(ishaHour)
    val tahajjudMillis = toEpochMillis(tahajjudHour)

    val nowMillis = System.currentTimeMillis()

    val fajrEntry = PrayerEntry(
      id = "fajr",
      nameEn = "Fajr",
      nameBn = "ফজর",
      nameAr = "الفجر",
      iconEmoji = "🌅",
      timeDecimalHours = fajrHour,
      timeMillis = fajrMillis,
      timeFormatted = formatDecimalHours(fajrHour)
    )

    val sunriseEntry = PrayerEntry(
      id = "sunrise",
      nameEn = "Sunrise / Ishraq",
      nameBn = "সূর্যোদয় / ইশরাক",
      nameAr = "الشروق",
      iconEmoji = "☀️",
      timeDecimalHours = sunriseHour,
      timeMillis = sunriseMillis,
      timeFormatted = formatDecimalHours(sunriseHour),
      isOptional = true
    )

    val dhuhrEntry = PrayerEntry(
      id = "dhuhr",
      nameEn = "Dhuhr",
      nameBn = "যোহর",
      nameAr = "الظهر",
      iconEmoji = "☀️",
      timeDecimalHours = noonLocal,
      timeMillis = dhuhrMillis,
      timeFormatted = formatDecimalHours(noonLocal)
    )

    val asrEntry = PrayerEntry(
      id = "asr",
      nameEn = "Asr",
      nameBn = "আসর",
      nameAr = "العصر",
      iconEmoji = "🌤️",
      timeDecimalHours = asrHour,
      timeMillis = asrMillis,
      timeFormatted = formatDecimalHours(asrHour)
    )

    val maghribEntry = PrayerEntry(
      id = "maghrib",
      nameEn = "Maghrib",
      nameBn = "মাগরিব",
      nameAr = "المغرب",
      iconEmoji = "🌇",
      timeDecimalHours = maghribHour,
      timeMillis = maghribMillis,
      timeFormatted = formatDecimalHours(maghribHour)
    )

    val ishaEntry = PrayerEntry(
      id = "isha",
      nameEn = "Isha",
      nameBn = "ইশা",
      nameAr = "العشاء",
      iconEmoji = "🌙",
      timeDecimalHours = ishaHour,
      timeMillis = ishaMillis,
      timeFormatted = formatDecimalHours(ishaHour)
    )

    val tahajjudEntry = PrayerEntry(
      id = "tahajjud",
      nameEn = "Tahajjud / Night Prayer",
      nameBn = "তাহাজ্জুদ (শেষ তৃতীয়াংশ)",
      nameAr = "التهجّد",
      iconEmoji = "✨",
      timeDecimalHours = tahajjudHour,
      timeMillis = tahajjudMillis,
      timeFormatted = formatDecimalHours(tahajjudHour),
      isOptional = true
    )

    // Current & Next determination
    val sequence = listOf(fajrEntry, sunriseEntry, dhuhrEntry, asrEntry, maghribEntry, ishaEntry)
    var currentPrayer = ishaEntry
    var nextPrayer = fajrEntry
    var nextMillis = fajrMillis + 24 * 3600 * 1000L

    for (i in sequence.indices) {
      val prayer = sequence[i]
      if (nowMillis < prayer.timeMillis) {
        nextPrayer = prayer
        nextMillis = prayer.timeMillis
        currentPrayer = if (i > 0) sequence[i - 1] else ishaEntry
        break
      }
    }

    val minutesRemaining = maxOf(0L, (nextMillis - nowMillis) / (60 * 1000L))

    return DailyPrayerSchedule(
      fajr = fajrEntry.copy(isCurrent = currentPrayer.id == "fajr", isNext = nextPrayer.id == "fajr"),
      sunrise = sunriseEntry.copy(isCurrent = currentPrayer.id == "sunrise", isNext = nextPrayer.id == "sunrise"),
      dhuhr = dhuhrEntry.copy(isCurrent = currentPrayer.id == "dhuhr", isNext = nextPrayer.id == "dhuhr"),
      asr = asrEntry.copy(isCurrent = currentPrayer.id == "asr", isNext = nextPrayer.id == "asr"),
      maghrib = maghribEntry.copy(isCurrent = currentPrayer.id == "maghrib", isNext = nextPrayer.id == "maghrib"),
      isha = ishaEntry.copy(isCurrent = currentPrayer.id == "isha", isNext = nextPrayer.id == "isha"),
      tahajjud = tahajjudEntry,
      currentPrayer = currentPrayer,
      nextPrayer = nextPrayer,
      timeUntilNextMinutes = minutesRemaining,
      locationName = locationName,
      latitude = latitude,
      longitude = longitude,
      timezoneOffsetHours = tzHours,
      method = method,
      juristicMethod = juristicMethod
    )
  }
}
