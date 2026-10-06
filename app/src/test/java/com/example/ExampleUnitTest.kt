package com.example

import com.example.islamic.CalculationMethod
import com.example.islamic.JuristicMethod
import com.example.islamic.PrayerTimesCalculator
import org.junit.Assert.*
import org.junit.Test
import java.util.Calendar
import java.util.TimeZone

class ExampleUnitTest {
  @Test
  fun testDhakaPrayerTimes() {
    val cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+6")).apply {
      set(2026, Calendar.SEPTEMBER, 14, 12, 0, 0)
    }
    val schedule = PrayerTimesCalculator.calculate(
      calendar = cal,
      latitude = 23.8103,
      longitude = 90.4125,
      timezoneOffset = 6.0,
      locationName = "Dhaka, Bangladesh",
      method = CalculationMethod.KARACHI,
      juristicMethod = JuristicMethod.HANAFI
    )
    val tzHours = 6.0
    val tzMinutes = (tzHours * 60).toInt()
    val tzSign = if (tzMinutes >= 0) "+" else "-"
    val tzAbsMinutes = kotlin.math.abs(tzMinutes)
    val tzHoursPart = tzAbsMinutes / 60
    val tzMinsPart = tzAbsMinutes % 60
    val tzId = String.format(java.util.Locale.US, "GMT%s%02d:%02d", tzSign, tzHoursPart, tzMinsPart)
    val locationTimeZone = TimeZone.getTimeZone(tzId)

    val fmt = java.text.SimpleDateFormat("hh:mm a", java.util.Locale.US).apply {
      timeZone = locationTimeZone
    }
    println("Formatted Dhaka Fajr with location TZ: ${fmt.format(java.util.Date(schedule.fajr.timeMillis))}")
    println("Formatted Dhaka Dhuhr with location TZ: ${fmt.format(java.util.Date(schedule.dhuhr.timeMillis))}")
    println("Formatted Dhaka Asr with location TZ: ${fmt.format(java.util.Date(schedule.asr.timeMillis))}")
    println("Formatted Dhaka Maghrib with location TZ: ${fmt.format(java.util.Date(schedule.maghrib.timeMillis))}")
    println("Formatted Dhaka Isha with location TZ: ${fmt.format(java.util.Date(schedule.isha.timeMillis))}")

    assertNotNull(schedule.fajr.timeFormatted)
  }

  @Test
  fun testDualVoiceProfiles() {
    val male = com.example.audio.VoiceProfile.MALE_QARI
    val female = com.example.audio.VoiceProfile.FEMALE_QARIA

    // Male Qari checks
    assertTrue(male.targetTtsVoices.contains("ar-sa-hamedneural"))
    assertEquals("audio/male", male.assetFolder)
    assertTrue(male.pitch < 1.0f) // Baritone depth
    assertTrue(male.speechRate < 1.0f) // Deliberate Tajweed cadence

    // Female Qaria checks
    assertTrue(female.targetTtsVoices.contains("ar-sa-zariyahneural"))
    assertEquals("audio/female", female.assetFolder)
    assertTrue(female.pitch > 1.0f) // Soft, sweet pitch
    assertTrue(female.speechRate <= 1.0f) // Measured Tajweed learning tempo

    // Two-way mapping checks
    assertEquals(male, VoiceGender.MALE_QARI.toVoiceProfile())
    assertEquals(female, VoiceGender.FEMALE_QARIA.toVoiceProfile())
    assertEquals(VoiceGender.MALE_QARI, VoiceGender.fromVoiceProfile(male))
    assertEquals(VoiceGender.FEMALE_QARIA, VoiceGender.fromVoiceProfile(female))
  }
}

