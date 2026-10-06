package com.example.islamic

import com.batoulapps.adhan.CalculationMethod as AdhanMethod
import com.batoulapps.adhan.CalculationParameters
import com.batoulapps.adhan.Coordinates
import com.batoulapps.adhan.Madhab
import com.batoulapps.adhan.PrayerTimes
import com.batoulapps.adhan.data.DateComponents
import java.text.SimpleDateFormat
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
  val ishaIntervalMinutes: Int? = null,
  val authorityNameEn: String = titleEn,
  val authorityNameBn: String = titleBn
) {
  KARACHI(
    titleEn = "Islamic Foundation BD / Univ. of Karachi",
    titleBn = "ইসলামিক ফাউন্ডেশন বাংলাদেশ / করাচি বিশ্ববিদ্যালয়",
    authorityNameEn = "Islamic Foundation Bangladesh",
    authorityNameBn = "ইসলামিক ফাউন্ডেশন (বাংলাদেশ)",
    region = "South Asia",
    fajrAngle = 18.0,
    ishaAngle = 18.0
  ),
  MAKKAH(
    titleEn = "Umm al-Qura University, Makkah",
    titleBn = "উম্মুল কুরা বিশ্ববিদ্যালয়, মক্কা (সৌদি ও মধ্যপ্রাচ্য)",
    authorityNameEn = "Umm al-Qura (Saudi & Gulf)",
    authorityNameBn = "উম্মুল কুরা (সৌদি ও মধ্যপ্রাচ্য)",
    region = "Middle East",
    fajrAngle = 18.5,
    ishaAngle = 0.0,
    ishaIntervalMinutes = 90
  ),
  MWL(
    titleEn = "Muslim World League (MWL)",
    titleBn = "মুসলিম ওয়ার্ল্ড লীগ (ইউরোপ ও বিশ্ব)",
    authorityNameEn = "Muslim World League (MWL)",
    authorityNameBn = "মুসলিম ওয়ার্ল্ড লীগ (ইউরোপ/রাবেতা)",
    region = "Global",
    fajrAngle = 18.0,
    ishaAngle = 17.0
  ),
  EGYPT(
    titleEn = "Egyptian General Authority of Survey",
    titleBn = "মিশরীয় জেনেরাল সার্ভে (আফ্রিকা)",
    authorityNameEn = "Egyptian General Authority",
    authorityNameBn = "মিশরীয় জেনারেল সার্ভে (আফ্রিকা)",
    region = "Africa & Middle East",
    fajrAngle = 19.5,
    ishaAngle = 17.5
  ),
  MUIS(
    titleEn = "MUIS (Majlis Ugama Islam Singapura)",
    titleBn = "এমইউআইএস - সিঙ্গাপুর ইসলামিক কাউন্সিল",
    authorityNameEn = "MUIS Singapore",
    authorityNameBn = "এমইউআইএস (সিঙ্গাপুর)",
    region = "Singapore",
    fajrAngle = 20.0,
    ishaAngle = 18.0
  ),
  JAKIM(
    titleEn = "JAKIM (Jabatan Kemajuan Islam Malaysia)",
    titleBn = "জাকিম - মালয়েশিয়া ইসলামিক উন্নয়ন বিভাগ",
    authorityNameEn = "JAKIM Malaysia",
    authorityNameBn = "জাকিম (মালয়েশিয়া)",
    region = "Malaysia",
    fajrAngle = 20.0,
    ishaAngle = 18.0
  ),
  KEMENAG(
    titleEn = "KEMENAG (Kementerian Agama RI)",
    titleBn = "কেমেনাং - ইন্দোনেশিয়া ধর্মীয় মন্ত্রণালয়",
    authorityNameEn = "KEMENAG Indonesia",
    authorityNameBn = "কেমেনাং (ইন্দোনেশিয়া)",
    region = "Indonesia",
    fajrAngle = 20.0,
    ishaAngle = 18.0
  ),
  DUBAI(
    titleEn = "Dubai / UAE Awqaf",
    titleBn = "দুবাই / সংযুক্ত আরব আমিরাত আওকাফ",
    authorityNameEn = "Dubai Awqaf (UAE)",
    authorityNameBn = "দুবাই আওকাফ (আমিরাত)",
    region = "Gulf",
    fajrAngle = 18.2,
    ishaAngle = 18.2
  ),
  ISNA(
    titleEn = "Islamic Society of North America (ISNA)",
    titleBn = "ইসলামিক সোসাইটি অফ নর্থ আমেরিকা",
    authorityNameEn = "ISNA (North America)",
    authorityNameBn = "আইএসএনএ (উত্তর আমেরিকা)",
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
  val juristicMethod: JuristicMethod,
  val currentPrayerStartMillis: Long = 0L,
  val nextPrayerStartMillis: Long = 0L,
  val timeUntilNextSeconds: Long = 0L,
  val sahriEndMillis: Long = 0L,
  val iftarMillis: Long = 0L,
  val isGpsLocated: Boolean = false,
  val nextAzanPrayer: PrayerEntry = nextPrayer,
  val nextAzanStartMillis: Long = nextPrayerStartMillis,
  val timeUntilNextAzanSeconds: Long = timeUntilNextSeconds,
  val todaySahriEndFormatted: String = fajr.timeFormatted,
  val todayIftarStartFormatted: String = maghrib.timeFormatted,
  val calculationAuthorityNameBn: String = method.authorityNameBn,
  val calculationAuthorityNameEn: String = method.authorityNameEn,
  val currentPrayerEndMillis: Long = nextPrayerStartMillis,
  val islamicMidnightMillis: Long = 0L,
  val islamicMidnightFormatted: String = "",
  val islamicMidnight: PrayerEntry = tahajjud
) {
  val calculationAuthorityBn: String get() = calculationAuthorityNameBn
  val calculationAuthorityEn: String get() = calculationAuthorityNameEn

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
   * Dynamically auto-detects the local prayer calculation authority and juristic school
   * based on coordinates, country code, and country/city name.
   */
  fun detectCalculationMethod(
    latitude: Double,
    longitude: Double,
    countryCode: String? = null,
    countryName: String? = null
  ): Pair<CalculationMethod, JuristicMethod> {
    val cc = countryCode?.trim()?.uppercase(Locale.US) ?: ""
    val cn = countryName?.trim()?.lowercase(Locale.US) ?: ""

    return when {
      // 1. Bangladesh -> Islamic Foundation Bangladesh (Univ. of Karachi parameters, Hanafi Asr)
      cc == "BD" || cn.contains("bangladesh") || (latitude in 20.5..26.8 && longitude in 88.0..92.8) -> {
        Pair(CalculationMethod.KARACHI, JuristicMethod.HANAFI)
      }

      // 2. Saudi Arabia, Qatar, Kuwait, Bahrain, Oman, Yemen -> Umm al-Qura University, Makkah
      cc in listOf("SA", "QA", "KW", "BH", "OM", "YE") ||
        cn.contains("saudi") || cn.contains("qatar") || cn.contains("kuwait") || cn.contains("bahrain") || cn.contains("oman") ||
        (latitude in 12.0..32.5 && longitude in 34.0..60.0 && !cn.contains("emirates")) -> {
        Pair(CalculationMethod.MAKKAH, JuristicMethod.SHAFI)
      }

      // 3. UAE / Dubai -> Dubai / UAE Awqaf
      cc == "AE" || cn.contains("emirates") || cn.contains("dubai") || (latitude in 22.5..26.5 && longitude in 51.5..56.5) -> {
        Pair(CalculationMethod.DUBAI, JuristicMethod.SHAFI)
      }

      // 4. North America (USA & Canada) -> ISNA
      cc in listOf("US", "CA") || cn.contains("united states") || cn.contains("canada") ||
        (latitude in 24.0..72.0 && longitude in -170.0..-52.0) -> {
        Pair(CalculationMethod.ISNA, JuristicMethod.HANAFI)
      }

      // 5. Malaysia -> JAKIM
      cc == "MY" || cn.contains("malaysia") || (latitude in 0.8..7.5 && longitude in 99.5..119.5) -> {
        Pair(CalculationMethod.JAKIM, JuristicMethod.SHAFI)
      }

      // 6. Singapore -> MUIS
      cc == "SG" || cn.contains("singapore") || (latitude in 1.15..1.5 && longitude in 103.5..104.1) -> {
        Pair(CalculationMethod.MUIS, JuristicMethod.SHAFI)
      }

      // 7. Indonesia -> KEMENAG
      cc == "ID" || cn.contains("indonesia") || (latitude in -11.0..6.0 && longitude in 95.0..141.0) -> {
        Pair(CalculationMethod.KEMENAG, JuristicMethod.SHAFI)
      }

      // 8. Egypt, Sudan, Libya -> Egyptian General Authority
      cc in listOf("EG", "SD", "LY") || cn.contains("egypt") || (latitude in 21.0..32.0 && longitude in 24.0..37.0) -> {
        Pair(CalculationMethod.EGYPT, JuristicMethod.SHAFI)
      }

      // 9. Europe & United Kingdom -> MWL (Muslim World League)
      cc in listOf("GB", "UK", "FR", "DE", "IT", "ES", "NL", "BE", "SE", "NO", "DK", "FI", "CH", "AT", "IE", "PL") ||
        cn.contains("kingdom") || cn.contains("germany") || cn.contains("france") || cn.contains("europe") ||
        (latitude in 35.0..71.0 && longitude in -11.0..40.0) -> {
        Pair(CalculationMethod.MWL, JuristicMethod.HANAFI)
      }

      // 10. Pakistan, India, Sri Lanka, Afghanistan -> Karachi, Hanafi
      cc in listOf("PK", "IN", "LK", "AF", "NP") || cn.contains("pakistan") || cn.contains("india") ||
        (latitude in 6.0..37.5 && longitude in 60.0..88.0) -> {
        Pair(CalculationMethod.KARACHI, JuristicMethod.HANAFI)
      }

      // Default global fallback -> MWL (Muslim World League)
      else -> {
        Pair(CalculationMethod.MWL, JuristicMethod.HANAFI)
      }
    }
  }

  /**
   * Calculate prayer times for a specific date, location and calculation method
   * Powered by the Adhan library with precision astronomical fallback.
   */
  fun calculate(
    calendar: Calendar = Calendar.getInstance(),
    latitude: Double,
    longitude: Double,
    timezoneOffset: Double? = null,
    locationName: String = "Current Location",
    method: CalculationMethod = CalculationMethod.KARACHI,
    juristicMethod: JuristicMethod = JuristicMethod.HANAFI,
    currentTimeMillis: Long = System.currentTimeMillis(),
    isGpsLocated: Boolean = false
  ): DailyPrayerSchedule {
    val systemTz = calendar.timeZone.rawOffset / 3600000.0 +
      if (calendar.timeZone.inDaylightTime(calendar.time)) 1.0 else 0.0

    val tzHours = timezoneOffset ?: resolveTimezone(longitude, systemTz)

    // Build the exact TimeZone for this location so prayer times are 100% immune to device/emulator bias
    val tzMinutes = (tzHours * 60.0).roundToInt()
    val tzSign = if (tzMinutes >= 0) "+" else "-"
    val tzAbsMinutes = kotlin.math.abs(tzMinutes)
    val tzHoursPart = tzAbsMinutes / 60
    val tzMinsPart = tzAbsMinutes % 60
    val tzId = String.format(Locale.US, "GMT%s%02d:%02d", tzSign, tzHoursPart, tzMinsPart)
    val locationTimeZone = TimeZone.getTimeZone(tzId)

    val localCalendar = Calendar.getInstance(locationTimeZone).apply {
      timeInMillis = calendar.timeInMillis
    }
    val year = localCalendar.get(Calendar.YEAR)
    val month = localCalendar.get(Calendar.MONTH) + 1
    val day = localCalendar.get(Calendar.DAY_OF_MONTH)

    // 1. Try calculation using the Adhan library
    val coordinates = Coordinates(latitude, longitude)
    val dateComponents = DateComponents(year, month, day)

    val adhanMethod = when (method) {
      CalculationMethod.KARACHI -> AdhanMethod.KARACHI
      CalculationMethod.MAKKAH -> AdhanMethod.UMM_AL_QURA
      CalculationMethod.MWL -> AdhanMethod.MUSLIM_WORLD_LEAGUE
      CalculationMethod.EGYPT -> AdhanMethod.EGYPTIAN
      CalculationMethod.DUBAI -> AdhanMethod.DUBAI
      CalculationMethod.MUIS, CalculationMethod.JAKIM, CalculationMethod.KEMENAG -> AdhanMethod.SINGAPORE
      CalculationMethod.ISNA -> AdhanMethod.NORTH_AMERICA
    }

    val params: CalculationParameters = adhanMethod.parameters.apply {
      madhab = if (juristicMethod == JuristicMethod.HANAFI) Madhab.HANAFI else Madhab.SHAFI
    }

    val adhanPrayerTimes: PrayerTimes? = try {
      PrayerTimes(coordinates, dateComponents, params)
    } catch (_: Exception) {
      null
    }

    val tomorrowCal = (localCalendar.clone() as Calendar).apply { add(Calendar.DAY_OF_YEAR, 1) }
    val tomorrowAdhan: PrayerTimes? = try {
      PrayerTimes(coordinates, DateComponents(tomorrowCal.get(Calendar.YEAR), tomorrowCal.get(Calendar.MONTH) + 1, tomorrowCal.get(Calendar.DAY_OF_MONTH)), params)
    } catch (_: Exception) {
      null
    }

    val yesterdayCal = (localCalendar.clone() as Calendar).apply { add(Calendar.DAY_OF_YEAR, -1) }
    val yesterdayAdhan: PrayerTimes? = try {
      PrayerTimes(coordinates, DateComponents(yesterdayCal.get(Calendar.YEAR), yesterdayCal.get(Calendar.MONTH) + 1, yesterdayCal.get(Calendar.DAY_OF_MONTH)), params)
    } catch (_: Exception) {
      null
    }

    // Astronomical fallback calculation
    val jd = julianDay(year, month, day)
    val (decRad, eqOfTime) = sunPosition(jd)
    val latRad = degToRad(latitude)
    val noonUtc = 12.0 - (longitude / 15.0) - eqOfTime
    val noonLocal = fixHour(noonUtc + tzHours + (2.0 / 60.0))
    val sunAngleSunRise = computeHourAngle(latRad, decRad, -0.833) / 15.0
    val astroSunriseHour = fixHour(noonLocal - sunAngleSunRise)
    val astroSunsetHour = fixHour(noonLocal + sunAngleSunRise)
    val fajrHA = computeHourAngle(latRad, decRad, -method.fajrAngle) / 15.0
    val astroFajrHour = fixHour(noonLocal - fajrHA)
    val asrHA = computeAsrHourAngle(latRad, decRad, juristicMethod.shadowFactor) / 15.0
    val astroAsrHour = fixHour(noonLocal + asrHA)
    val astroMaghribHour = astroSunsetHour
    val ishaHA = computeHourAngle(latRad, decRad, -method.ishaAngle) / 15.0
    val astroIshaHour = fixHour(noonLocal + ishaHA)

    fun toEpochMillis(localHour: Double): Long {
      val cal = Calendar.getInstance(locationTimeZone).apply {
        set(Calendar.YEAR, year)
        set(Calendar.MONTH, month - 1)
        set(Calendar.DAY_OF_MONTH, day)
        val totSec = (fixHour(localHour) * 3600.0).roundToInt()
        set(Calendar.HOUR_OF_DAY, totSec / 3600)
        set(Calendar.MINUTE, (totSec % 3600) / 60)
        set(Calendar.SECOND, totSec % 60)
        set(Calendar.MILLISECOND, 0)
      }
      return cal.timeInMillis
    }

    fun dateToDecimalHours(d: Date): Double {
      val c = Calendar.getInstance(locationTimeZone).apply { time = d }
      return c.get(Calendar.HOUR_OF_DAY) + (c.get(Calendar.MINUTE) / 60.0) + (c.get(Calendar.SECOND) / 3600.0)
    }

    val timeFormat = SimpleDateFormat("hh:mm a", Locale.US).apply {
      timeZone = locationTimeZone
    }

    val fajrMillis: Long
    val sunriseMillis: Long
    val dhuhrMillis: Long
    val asrMillis: Long
    val maghribMillis: Long
    val ishaMillis: Long
    val tomorrowFajrMillis: Long
    val yesterdayIshaMillis: Long

    val fajrDecHour: Double
    val sunriseDecHour: Double
    val dhuhrDecHour: Double
    val asrDecHour: Double
    val maghribDecHour: Double
    val ishaDecHour: Double

    val fajrFormatted: String
    val sunriseFormatted: String
    val dhuhrFormatted: String
    val asrFormatted: String
    val maghribFormatted: String
    val ishaFormatted: String

    if (adhanPrayerTimes != null && adhanPrayerTimes.fajr != null) {
      fajrMillis = adhanPrayerTimes.fajr.time
      sunriseMillis = adhanPrayerTimes.sunrise.time
      dhuhrMillis = adhanPrayerTimes.dhuhr.time
      asrMillis = adhanPrayerTimes.asr.time
      maghribMillis = adhanPrayerTimes.maghrib.time
      ishaMillis = adhanPrayerTimes.isha.time

      fajrDecHour = dateToDecimalHours(adhanPrayerTimes.fajr)
      sunriseDecHour = dateToDecimalHours(adhanPrayerTimes.sunrise)
      dhuhrDecHour = dateToDecimalHours(adhanPrayerTimes.dhuhr)
      asrDecHour = dateToDecimalHours(adhanPrayerTimes.asr)
      maghribDecHour = dateToDecimalHours(adhanPrayerTimes.maghrib)
      ishaDecHour = dateToDecimalHours(adhanPrayerTimes.isha)

      fajrFormatted = timeFormat.format(adhanPrayerTimes.fajr)
      sunriseFormatted = timeFormat.format(adhanPrayerTimes.sunrise)
      dhuhrFormatted = timeFormat.format(adhanPrayerTimes.dhuhr)
      asrFormatted = timeFormat.format(adhanPrayerTimes.asr)
      maghribFormatted = timeFormat.format(adhanPrayerTimes.maghrib)
      ishaFormatted = timeFormat.format(adhanPrayerTimes.isha)

      tomorrowFajrMillis = tomorrowAdhan?.fajr?.time ?: (fajrMillis + 24 * 3600 * 1000L)
      yesterdayIshaMillis = yesterdayAdhan?.isha?.time ?: (ishaMillis - 24 * 3600 * 1000L)
    } else {
      fajrMillis = toEpochMillis(astroFajrHour)
      sunriseMillis = toEpochMillis(astroSunriseHour)
      dhuhrMillis = toEpochMillis(noonLocal)
      asrMillis = toEpochMillis(astroAsrHour)
      maghribMillis = toEpochMillis(astroMaghribHour)
      ishaMillis = toEpochMillis(astroIshaHour)

      fajrDecHour = astroFajrHour
      sunriseDecHour = astroSunriseHour
      dhuhrDecHour = noonLocal
      asrDecHour = astroAsrHour
      maghribDecHour = astroMaghribHour
      ishaDecHour = astroIshaHour

      fajrFormatted = formatDecimalHours(astroFajrHour)
      sunriseFormatted = formatDecimalHours(astroSunriseHour)
      dhuhrFormatted = formatDecimalHours(noonLocal)
      asrFormatted = formatDecimalHours(astroAsrHour)
      maghribFormatted = formatDecimalHours(astroMaghribHour)
      ishaFormatted = formatDecimalHours(astroIshaHour)

      tomorrowFajrMillis = fajrMillis + 24 * 3600 * 1000L
      yesterdayIshaMillis = ishaMillis - 24 * 3600 * 1000L
    }

    // Dynamic Nisf al-Lail (Islamic Midnight) & Tahajjud calculation:
    // Islamic Midnight = Sunset (Maghrib) + ((Tomorrow's Fajr - Sunset) / 2)
    val tonightNightDuration = tomorrowFajrMillis - maghribMillis
    val tonightIslamicMidnightMillis = maghribMillis + (tonightNightDuration / 2L)
    val tonightTahajjudMillis = tonightIslamicMidnightMillis
    val tonightIslamicMidnightFormatted = timeFormat.format(Date(tonightIslamicMidnightMillis))
    val tonightIslamicMidnightDecHour = dateToDecimalHours(Date(tonightIslamicMidnightMillis))

    // Previous night calculations (for pre-dawn hours today: 00:00 to fajrMillis)
    val yesterdayMaghribMillis = yesterdayAdhan?.maghrib?.time ?: (maghribMillis - 24 * 3600 * 1000L)
    val yesterdayNightDuration = fajrMillis - yesterdayMaghribMillis
    val yesterdayIslamicMidnightMillis = yesterdayMaghribMillis + (yesterdayNightDuration / 2L)

    val fajrEntry = PrayerEntry(
      id = "fajr",
      nameEn = "Fajr",
      nameBn = "ফজর",
      nameAr = "الفجر",
      iconEmoji = "🌅",
      timeDecimalHours = fajrDecHour,
      timeMillis = fajrMillis,
      timeFormatted = fajrFormatted
    )

    val sunriseEntry = PrayerEntry(
      id = "sunrise",
      nameEn = "Sunrise / Ishraq",
      nameBn = "সূর্যোদয় / ইশরাক",
      nameAr = "الشروق",
      iconEmoji = "☀️",
      timeDecimalHours = sunriseDecHour,
      timeMillis = sunriseMillis,
      timeFormatted = sunriseFormatted,
      isOptional = true
    )

    val dhuhrEntry = PrayerEntry(
      id = "dhuhr",
      nameEn = "Dhuhr",
      nameBn = "যোহর",
      nameAr = "الظهر",
      iconEmoji = "☀️",
      timeDecimalHours = dhuhrDecHour,
      timeMillis = dhuhrMillis,
      timeFormatted = dhuhrFormatted
    )

    val asrEntry = PrayerEntry(
      id = "asr",
      nameEn = "Asr",
      nameBn = "আসর",
      nameAr = "العصر",
      iconEmoji = "🌤️",
      timeDecimalHours = asrDecHour,
      timeMillis = asrMillis,
      timeFormatted = asrFormatted
    )

    val maghribEntry = PrayerEntry(
      id = "maghrib",
      nameEn = "Maghrib",
      nameBn = "মাগরিব",
      nameAr = "المغرب",
      iconEmoji = "🌇",
      timeDecimalHours = maghribDecHour,
      timeMillis = maghribMillis,
      timeFormatted = maghribFormatted
    )

    val ishaEntry = PrayerEntry(
      id = "isha",
      nameEn = "Isha",
      nameBn = "ইশা",
      nameAr = "العشاء",
      iconEmoji = "🌙",
      timeDecimalHours = ishaDecHour,
      timeMillis = ishaMillis,
      timeFormatted = ishaFormatted
    )

    val tahajjudEntry = PrayerEntry(
      id = "tahajjud",
      nameEn = "Tahajjud / Night Prayer",
      nameBn = "তাহাজ্জুদ (শেষ তৃতীয়াংশ)",
      nameAr = "التهجّد",
      iconEmoji = "✨",
      timeDecimalHours = tonightIslamicMidnightDecHour,
      timeMillis = tonightTahajjudMillis,
      timeFormatted = tonightIslamicMidnightFormatted,
      isOptional = true
    )

    val islamicMidnightEntry = PrayerEntry(
      id = "midnight",
      nameEn = "Islamic Midnight (Nisf al-Lail)",
      nameBn = "নিসফুল লাইল (ইসলামিক মধ্যরাত)",
      nameAr = "نصف الليل",
      iconEmoji = "🌌",
      timeDecimalHours = tonightIslamicMidnightDecHour,
      timeMillis = tonightIslamicMidnightMillis,
      timeFormatted = tonightIslamicMidnightFormatted,
      isOptional = true
    )

    // Dynamic Current & Next prayer determination strictly based on live currentTimeMillis
    val now = currentTimeMillis
    val currentPrayerId: String
    val nextPrayerId: String
    val currentStartMillis: Long
    val currentEndMillis: Long
    val nextStartMillis: Long

    when {
      now < fajrMillis -> {
        // Late night / early morning before Fajr (e.g. 00:00 to Fajr)
        if (now >= yesterdayIslamicMidnightMillis) {
          // Crosses Islamic Midnight: transition active period to Tahajjud with countdown until Fajr!
          currentPrayerId = "tahajjud"
          nextPrayerId = "fajr"
          currentStartMillis = yesterdayIslamicMidnightMillis
          currentEndMillis = fajrMillis
          nextStartMillis = fajrMillis
        } else {
          // Before Islamic Midnight: preferred Isha duration
          currentPrayerId = "isha"
          nextPrayerId = "fajr"
          currentStartMillis = yesterdayIshaMillis
          currentEndMillis = yesterdayIslamicMidnightMillis
          nextStartMillis = fajrMillis
        }
      }
      now < sunriseMillis -> {
        // Fajr
        currentPrayerId = "fajr"
        nextPrayerId = "sunrise"
        currentStartMillis = fajrMillis
        currentEndMillis = sunriseMillis
        nextStartMillis = sunriseMillis
      }
      now < dhuhrMillis -> {
        // Morning (Ishraq / Chasht)
        currentPrayerId = "sunrise"
        nextPrayerId = "dhuhr"
        currentStartMillis = sunriseMillis
        currentEndMillis = dhuhrMillis
        nextStartMillis = dhuhrMillis
      }
      now < asrMillis -> {
        // Dhuhr
        currentPrayerId = "dhuhr"
        nextPrayerId = "asr"
        currentStartMillis = dhuhrMillis
        currentEndMillis = asrMillis
        nextStartMillis = asrMillis
      }
      now < maghribMillis -> {
        // Asr
        currentPrayerId = "asr"
        nextPrayerId = "maghrib"
        currentStartMillis = asrMillis
        currentEndMillis = maghribMillis
        nextStartMillis = maghribMillis
      }
      now < ishaMillis -> {
        // Maghrib
        currentPrayerId = "maghrib"
        nextPrayerId = "isha"
        currentStartMillis = maghribMillis
        currentEndMillis = ishaMillis
        nextStartMillis = ishaMillis
      }
      now < tonightIslamicMidnightMillis -> {
        // Isha evening before Islamic Midnight:
        // Preferred Isha duration ends dynamically at Islamic Midnight
        currentPrayerId = "isha"
        nextPrayerId = "fajr"
        currentStartMillis = ishaMillis
        currentEndMillis = tonightIslamicMidnightMillis
        nextStartMillis = tomorrowFajrMillis
      }
      else -> {
        // Crossing Islamic Midnight (tonight):
        // Automatically transition active/recommended time to Tahajjud with countdown until Fajr!
        currentPrayerId = "tahajjud"
        nextPrayerId = "fajr"
        currentStartMillis = tonightIslamicMidnightMillis
        currentEndMillis = tomorrowFajrMillis
        nextStartMillis = tomorrowFajrMillis
      }
    }

    val timeUntilNextSec = maxOf(0L, (nextStartMillis - now) / 1000L)
    val timeUntilNextMin = timeUntilNextSec / 60L

    val finalFajr = fajrEntry.copy(isCurrent = currentPrayerId == "fajr", isNext = nextPrayerId == "fajr")
    val finalSunrise = sunriseEntry.copy(isCurrent = currentPrayerId == "sunrise", isNext = nextPrayerId == "sunrise")
    val finalDhuhr = dhuhrEntry.copy(isCurrent = currentPrayerId == "dhuhr", isNext = nextPrayerId == "dhuhr")
    val finalAsr = asrEntry.copy(isCurrent = currentPrayerId == "asr", isNext = nextPrayerId == "asr")
    val finalMaghrib = maghribEntry.copy(isCurrent = currentPrayerId == "maghrib", isNext = nextPrayerId == "maghrib")
    val finalIsha = ishaEntry.copy(isCurrent = currentPrayerId == "isha", isNext = nextPrayerId == "isha")
    val finalTahajjud = tahajjudEntry.copy(isCurrent = currentPrayerId == "tahajjud", isNext = nextPrayerId == "tahajjud")
    val finalMidnight = islamicMidnightEntry.copy(isCurrent = false, isNext = false)

    val currentPrayerEntry = when (currentPrayerId) {
      "fajr" -> finalFajr
      "sunrise" -> finalSunrise
      "dhuhr" -> finalDhuhr
      "asr" -> finalAsr
      "maghrib" -> finalMaghrib
      "isha" -> finalIsha
      "tahajjud" -> finalTahajjud
      else -> finalIsha
    }

    val nextPrayerEntry = when (nextPrayerId) {
      "fajr" -> finalFajr.copy(timeMillis = nextStartMillis, timeFormatted = timeFormat.format(Date(nextStartMillis)))
      "sunrise" -> finalSunrise.copy(timeMillis = nextStartMillis, timeFormatted = timeFormat.format(Date(nextStartMillis)))
      "dhuhr" -> finalDhuhr.copy(timeMillis = nextStartMillis, timeFormatted = timeFormat.format(Date(nextStartMillis)))
      "asr" -> finalAsr.copy(timeMillis = nextStartMillis, timeFormatted = timeFormat.format(Date(nextStartMillis)))
      "maghrib" -> finalMaghrib.copy(timeMillis = nextStartMillis, timeFormatted = timeFormat.format(Date(nextStartMillis)))
      "isha" -> finalIsha.copy(timeMillis = nextStartMillis, timeFormatted = timeFormat.format(Date(nextStartMillis)))
      "tahajjud" -> finalTahajjud.copy(timeMillis = nextStartMillis, timeFormatted = timeFormat.format(Date(nextStartMillis)))
      else -> finalFajr.copy(timeMillis = nextStartMillis, timeFormatted = timeFormat.format(Date(nextStartMillis)))
    }

    val sahriEndMillis = if (now < fajrMillis) fajrMillis else tomorrowFajrMillis
    val iftarMillis = if (now < maghribMillis) maghribMillis else (maghribMillis + 24 * 3600 * 1000L)

    val nextAzanEntry: PrayerEntry
    val nextAzanStartMillis: Long

    when {
      now < fajrMillis -> {
        nextAzanEntry = finalFajr.copy(timeMillis = fajrMillis, timeFormatted = fajrFormatted)
        nextAzanStartMillis = fajrMillis
      }
      now < dhuhrMillis -> {
        nextAzanEntry = finalDhuhr.copy(timeMillis = dhuhrMillis, timeFormatted = dhuhrFormatted)
        nextAzanStartMillis = dhuhrMillis
      }
      now < asrMillis -> {
        nextAzanEntry = finalAsr.copy(timeMillis = asrMillis, timeFormatted = asrFormatted)
        nextAzanStartMillis = asrMillis
      }
      now < maghribMillis -> {
        nextAzanEntry = finalMaghrib.copy(timeMillis = maghribMillis, timeFormatted = maghribFormatted)
        nextAzanStartMillis = maghribMillis
      }
      now < ishaMillis -> {
        nextAzanEntry = finalIsha.copy(timeMillis = ishaMillis, timeFormatted = ishaFormatted)
        nextAzanStartMillis = ishaMillis
      }
      else -> {
        nextAzanEntry = finalFajr.copy(timeMillis = tomorrowFajrMillis, timeFormatted = timeFormat.format(Date(tomorrowFajrMillis)))
        nextAzanStartMillis = tomorrowFajrMillis
      }
    }

    val timeUntilNextAzanSec = maxOf(0L, (nextAzanStartMillis - now) / 1000L)

    return DailyPrayerSchedule(
      fajr = finalFajr,
      sunrise = finalSunrise,
      dhuhr = finalDhuhr,
      asr = finalAsr,
      maghrib = finalMaghrib,
      isha = finalIsha,
      tahajjud = finalTahajjud,
      currentPrayer = currentPrayerEntry,
      nextPrayer = nextPrayerEntry,
      timeUntilNextMinutes = timeUntilNextMin,
      locationName = locationName,
      latitude = latitude,
      longitude = longitude,
      timezoneOffsetHours = tzHours,
      method = method,
      juristicMethod = juristicMethod,
      currentPrayerStartMillis = currentStartMillis,
      nextPrayerStartMillis = nextStartMillis,
      timeUntilNextSeconds = timeUntilNextSec,
      sahriEndMillis = sahriEndMillis,
      iftarMillis = iftarMillis,
      isGpsLocated = isGpsLocated,
      nextAzanPrayer = nextAzanEntry,
      nextAzanStartMillis = nextAzanStartMillis,
      timeUntilNextAzanSeconds = timeUntilNextAzanSec,
      todaySahriEndFormatted = fajrFormatted,
      todayIftarStartFormatted = maghribFormatted,
      calculationAuthorityNameBn = method.authorityNameBn,
      calculationAuthorityNameEn = method.authorityNameEn,
      currentPrayerEndMillis = currentEndMillis,
      islamicMidnightMillis = tonightIslamicMidnightMillis,
      islamicMidnightFormatted = tonightIslamicMidnightFormatted,
      islamicMidnight = finalMidnight
    )
  }
}
