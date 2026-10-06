package com.example.islamic

import android.content.Context
import android.content.SharedPreferences

/**
 * Offline Data Caching Repository for Islamic Prayer Times & Location.
 * Persists selected coordinates, city name, timezone, calculation method, juristic method,
 * and Hijri day adjustment so all Islamic features function 100% offline without internet.
 */
class IslamicOfflineRepository private constructor(context: Context) {
  private val prefs: SharedPreferences =
    context.applicationContext.getSharedPreferences("hurufia_islamic_prefs", Context.MODE_PRIVATE)

  var latitude: Double
    get() = java.lang.Double.longBitsToDouble(
      prefs.getLong("latitude_bits", java.lang.Double.doubleToLongBits(23.8103))
    )
    set(value) {
      prefs.edit().putLong("latitude_bits", java.lang.Double.doubleToLongBits(value)).apply()
    }

  var longitude: Double
    get() = java.lang.Double.longBitsToDouble(
      prefs.getLong("longitude_bits", java.lang.Double.doubleToLongBits(90.4125))
    )
    set(value) {
      prefs.edit().putLong("longitude_bits", java.lang.Double.doubleToLongBits(value)).apply()
    }

  var timezoneHours: Double
    get() = java.lang.Double.longBitsToDouble(
      prefs.getLong("timezone_bits", java.lang.Double.doubleToLongBits(6.0))
    )
    set(value) {
      prefs.edit().putLong("timezone_bits", java.lang.Double.doubleToLongBits(value)).apply()
    }

  var locationName: String
    get() = prefs.getString("location_name", "ঢাকা, বাংলাদেশ") ?: "ঢাকা, বাংলাদেশ"
    set(value) {
      prefs.edit().putString("location_name", value).apply()
    }

  var calculationMethod: CalculationMethod
    get() {
      val saved = prefs.getString("calc_method", CalculationMethod.KARACHI.name)
      return try {
        CalculationMethod.valueOf(saved ?: CalculationMethod.KARACHI.name)
      } catch (_: Exception) {
        CalculationMethod.KARACHI
      }
    }
    set(value) {
      prefs.edit().putString("calc_method", value.name).apply()
    }

  var juristicMethod: JuristicMethod
    get() {
      val saved = prefs.getString("juristic_method", JuristicMethod.HANAFI.name)
      return try {
        JuristicMethod.valueOf(saved ?: JuristicMethod.HANAFI.name)
      } catch (_: Exception) {
        JuristicMethod.HANAFI
      }
    }
    set(value) {
      prefs.edit().putString("juristic_method", value.name).apply()
    }

  var hijriDayAdjustment: Int
    get() = prefs.getInt("hijri_day_adjustment", 0)
    set(value) {
      prefs.edit().putInt("hijri_day_adjustment", value).apply()
    }

  var isAzanMuted: Boolean
    get() = prefs.getBoolean("is_azan_muted", false)
    set(value) {
      prefs.edit().putBoolean("is_azan_muted", value).apply()
    }

  var isGpsLocated: Boolean
    get() = prefs.getBoolean("is_gps_located", false)
    set(value) {
      prefs.edit().putBoolean("is_gps_located", value).apply()
    }

  var selectedCityName: String
    get() = prefs.getString("selected_city_name", "") ?: ""
    set(value) {
      prefs.edit().putString("selected_city_name", value).apply()
    }

  var locationMode: String
    get() = if (isGpsLocated) "GPS" else "MANUAL"
    set(value) {
      isGpsLocated = (value.equals("GPS", ignoreCase = true))
    }

  fun saveLocation(
    lat: Double,
    lng: Double,
    name: String,
    tz: Double,
    isGps: Boolean = false,
    autoDetectedMethod: CalculationMethod? = null,
    autoDetectedJuristic: JuristicMethod? = null,
    cityName: String = ""
  ) {
    latitude = lat
    longitude = lng
    locationName = name
    timezoneHours = tz
    isGpsLocated = isGps
    if (cityName.isNotBlank()) {
      selectedCityName = cityName
    }
    if (autoDetectedMethod != null) {
      calculationMethod = autoDetectedMethod
    }
    if (autoDetectedJuristic != null) {
      juristicMethod = autoDetectedJuristic
    }
  }

  companion object {
    @Volatile
    private var INSTANCE: IslamicOfflineRepository? = null

    fun getInstance(context: Context): IslamicOfflineRepository {
      return INSTANCE ?: synchronized(this) {
        INSTANCE ?: IslamicOfflineRepository(context).also { INSTANCE = it }
      }
    }
  }
}
