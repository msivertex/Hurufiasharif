package com.example.islamic

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import java.util.Locale

/**
 * City & District Preset for instant offline or manual selection
 */
data class CityPreset(
  val nameEn: String,
  val nameBn: String,
  val countryEn: String,
  val countryBn: String,
  val latitude: Double,
  val longitude: Double,
  val timezoneHours: Double,
  val defaultMethod: CalculationMethod = CalculationMethod.KARACHI,
  val defaultJuristic: JuristicMethod = JuristicMethod.HANAFI
)

object LocationPresets {
  val defaultCities: List<CityPreset> = listOf(
    // ==================== BANGLADESH (Districts & Major Cities) ====================
    CityPreset("Dhaka", "ঢাকা", "Bangladesh", "বাংলাদেশ", 23.8103, 90.4125, 6.0, CalculationMethod.KARACHI, JuristicMethod.HANAFI),
    CityPreset("Chittagong (Chattogram)", "চট্টগ্রাম", "Bangladesh", "বাংলাদেশ", 22.3569, 91.7832, 6.0, CalculationMethod.KARACHI, JuristicMethod.HANAFI),
    CityPreset("Sylhet", "সিলেট", "Bangladesh", "বাংলাদেশ", 24.8949, 91.8687, 6.0, CalculationMethod.KARACHI, JuristicMethod.HANAFI),
    CityPreset("Rajshahi", "রাজশাহী", "Bangladesh", "বাংলাদেশ", 24.3745, 88.6042, 6.0, CalculationMethod.KARACHI, JuristicMethod.HANAFI),
    CityPreset("Khulna", "খুলনা", "Bangladesh", "বাংলাদেশ", 22.8456, 89.5403, 6.0, CalculationMethod.KARACHI, JuristicMethod.HANAFI),
    CityPreset("Barisal (Barishal)", "বরিশাল", "Bangladesh", "বাংলাদেশ", 22.7010, 90.3535, 6.0, CalculationMethod.KARACHI, JuristicMethod.HANAFI),
    CityPreset("Rangpur", "রংপুর", "Bangladesh", "বাংলাদেশ", 25.7439, 89.2752, 6.0, CalculationMethod.KARACHI, JuristicMethod.HANAFI),
    CityPreset("Mymensingh", "ময়মনসিংহ", "Bangladesh", "বাংলাদেশ", 24.7471, 90.4203, 6.0, CalculationMethod.KARACHI, JuristicMethod.HANAFI),
    CityPreset("Comilla (Cumilla)", "কুমিল্লা", "Bangladesh", "বাংলাদেশ", 23.4682, 91.1788, 6.0, CalculationMethod.KARACHI, JuristicMethod.HANAFI),
    CityPreset("Cox's Bazar", "কক্সবাজার", "Bangladesh", "বাংলাদেশ", 21.4272, 92.0058, 6.0, CalculationMethod.KARACHI, JuristicMethod.HANAFI),
    CityPreset("Bogura (Bogra)", "বগুড়া", "Bangladesh", "বাংলাদেশ", 24.8465, 89.3777, 6.0, CalculationMethod.KARACHI, JuristicMethod.HANAFI),
    CityPreset("Gazipur", "গাজীপুর", "Bangladesh", "বাংলাদেশ", 23.9999, 90.4203, 6.0, CalculationMethod.KARACHI, JuristicMethod.HANAFI),
    CityPreset("Narayanganj", "নারায়ণগঞ্জ", "Bangladesh", "বাংলাদেশ", 23.6238, 90.5000, 6.0, CalculationMethod.KARACHI, JuristicMethod.HANAFI),
    CityPreset("Jessore (Jashore)", "যশোর", "Bangladesh", "বাংলাদেশ", 23.1664, 89.2081, 6.0, CalculationMethod.KARACHI, JuristicMethod.HANAFI),
    CityPreset("Dinajpur", "দিনাজপুর", "Bangladesh", "বাংলাদেশ", 25.6217, 88.6354, 6.0, CalculationMethod.KARACHI, JuristicMethod.HANAFI),
    CityPreset("Tangail", "টাঙ্গাইল", "Bangladesh", "বাংলাদেশ", 24.2513, 89.9167, 6.0, CalculationMethod.KARACHI, JuristicMethod.HANAFI),
    CityPreset("Feni", "ফেনী", "Bangladesh", "বাংলাদেশ", 23.0159, 91.3976, 6.0, CalculationMethod.KARACHI, JuristicMethod.HANAFI),
    CityPreset("Brahmanbaria", "ব্রাহ্মণবাড়িয়া", "Bangladesh", "বাংলাদেশ", 23.9571, 91.1119, 6.0, CalculationMethod.KARACHI, JuristicMethod.HANAFI),
    CityPreset("Pabna", "পাবনা", "Bangladesh", "বাংলাদেশ", 24.0064, 89.2372, 6.0, CalculationMethod.KARACHI, JuristicMethod.HANAFI),
    CityPreset("Kushtia", "কুষ্টিয়া", "Bangladesh", "বাংলাদেশ", 23.9013, 89.1204, 6.0, CalculationMethod.KARACHI, JuristicMethod.HANAFI),
    CityPreset("Noakhali", "নোয়াখালী", "Bangladesh", "বাংলাদেশ", 22.8696, 91.0994, 6.0, CalculationMethod.KARACHI, JuristicMethod.HANAFI),
    CityPreset("Faridpur", "ফরিদপুর", "Bangladesh", "বাংলাদেশ", 23.6071, 89.8429, 6.0, CalculationMethod.KARACHI, JuristicMethod.HANAFI),

    // ==================== SAUDI ARABIA & GULF (Middle East) ====================
    CityPreset("Makkah Al-Mukarramah", "মক্কা মুকাররমা", "Saudi Arabia", "সৌদি আরব", 21.4225, 39.8262, 3.0, CalculationMethod.MAKKAH, JuristicMethod.SHAFI),
    CityPreset("Madinah Al-Munawwarah", "মদিনা মুনাওয়ারা", "Saudi Arabia", "সৌদি আরব", 24.4672, 39.6111, 3.0, CalculationMethod.MAKKAH, JuristicMethod.SHAFI),
    CityPreset("Riyadh", "রিয়াদ", "Saudi Arabia", "সৌদি আরব", 24.7136, 46.6753, 3.0, CalculationMethod.MAKKAH, JuristicMethod.SHAFI),
    CityPreset("Jeddah", "জেদ্দা", "Saudi Arabia", "সৌদি আরব", 21.5433, 39.1728, 3.0, CalculationMethod.MAKKAH, JuristicMethod.SHAFI),
    CityPreset("Dammam", "দাম্মাম", "Saudi Arabia", "সৌদি আরব", 26.4207, 50.0888, 3.0, CalculationMethod.MAKKAH, JuristicMethod.SHAFI),
    CityPreset("Dubai", "দুবাই", "United Arab Emirates", "সংযুক্ত আরব আমিরাত", 25.2048, 55.2708, 4.0, CalculationMethod.DUBAI, JuristicMethod.SHAFI),
    CityPreset("Abu Dhabi", "আবুধাবি", "United Arab Emirates", "সংযুক্ত আরব আমিরাত", 24.4539, 54.3773, 4.0, CalculationMethod.DUBAI, JuristicMethod.SHAFI),
    CityPreset("Sharjah", "শারজাহ", "United Arab Emirates", "সংযুক্ত আরব আমিরাত", 25.3463, 55.4209, 4.0, CalculationMethod.DUBAI, JuristicMethod.SHAFI),
    CityPreset("Doha", "দোহা", "Qatar", "কাতার", 25.2854, 51.5310, 3.0, CalculationMethod.MAKKAH, JuristicMethod.SHAFI),
    CityPreset("Kuwait City", "কুয়েত সিটি", "Kuwait", "কুয়েত", 29.3759, 47.9774, 3.0, CalculationMethod.MAKKAH, JuristicMethod.SHAFI),
    CityPreset("Manama", "মানামা", "Bahrain", "বাহরাইন", 26.2285, 50.5860, 3.0, CalculationMethod.MAKKAH, JuristicMethod.SHAFI),
    CityPreset("Muscat", "মাসকাট", "Oman", "ওমান", 23.5859, 58.4059, 4.0, CalculationMethod.MWL, JuristicMethod.SHAFI),

    // ==================== PAKISTAN ====================
    CityPreset("Karachi", "করাচি", "Pakistan", "পাকিস্তান", 24.8607, 67.0011, 5.0, CalculationMethod.KARACHI, JuristicMethod.HANAFI),
    CityPreset("Lahore", "লাহোর", "Pakistan", "পাকিস্তান", 31.5204, 74.3587, 5.0, CalculationMethod.KARACHI, JuristicMethod.HANAFI),
    CityPreset("Islamabad", "ইসলামাবাদ", "Pakistan", "পাকিস্তান", 33.6844, 73.0479, 5.0, CalculationMethod.KARACHI, JuristicMethod.HANAFI),
    CityPreset("Rawalpindi", "রাওয়ালপিন্ডি", "Pakistan", "পাকিস্তান", 33.5651, 73.0169, 5.0, CalculationMethod.KARACHI, JuristicMethod.HANAFI),
    CityPreset("Peshawar", "পেশোয়ার", "Pakistan", "পাকিস্তান", 34.0151, 71.5249, 5.0, CalculationMethod.KARACHI, JuristicMethod.HANAFI),
    CityPreset("Quetta", "কোয়েটা", "Pakistan", "পাকিস্তান", 30.1798, 66.9750, 5.0, CalculationMethod.KARACHI, JuristicMethod.HANAFI),
    CityPreset("Multan", "মুলতান", "Pakistan", "পাকিস্তান", 30.1575, 71.5249, 5.0, CalculationMethod.KARACHI, JuristicMethod.HANAFI),

    // ==================== INDIA ====================
    CityPreset("New Delhi", "নয়াদিল্লি", "India", "ভারত", 28.6139, 77.2090, 5.5, CalculationMethod.KARACHI, JuristicMethod.HANAFI),
    CityPreset("Kolkata", "কলকাতা", "India", "ভারত", 22.5726, 88.3639, 5.5, CalculationMethod.KARACHI, JuristicMethod.HANAFI),
    CityPreset("Mumbai", "মুম্বাই", "India", "ভারত", 19.0760, 72.8777, 5.5, CalculationMethod.KARACHI, JuristicMethod.HANAFI),
    CityPreset("Hyderabad", "হায়দ্রাবাদ", "India", "ভারত", 17.3850, 78.4867, 5.5, CalculationMethod.KARACHI, JuristicMethod.HANAFI),
    CityPreset("Bengaluru", "বেঙ্গালুরু", "India", "ভারত", 12.9716, 77.5946, 5.5, CalculationMethod.KARACHI, JuristicMethod.HANAFI),
    CityPreset("Chennai", "চেন্নাই", "India", "ভারত", 13.0827, 80.2707, 5.5, CalculationMethod.KARACHI, JuristicMethod.SHAFI),
    CityPreset("Lucknow", "লখনউ", "India", "ভারত", 26.8467, 80.9462, 5.5, CalculationMethod.KARACHI, JuristicMethod.HANAFI),
    CityPreset("Ahmedabad", "আহমেদাবাদ", "India", "ভারত", 23.0225, 72.5714, 5.5, CalculationMethod.KARACHI, JuristicMethod.HANAFI),
    CityPreset("Srinagar", "শ্রীনগর (কাশ্মীর)", "India", "ভারত", 34.0837, 74.7973, 5.5, CalculationMethod.KARACHI, JuristicMethod.HANAFI),

    // ==================== MALAYSIA (JAKIM) ====================
    CityPreset("Kuala Lumpur", "কুয়ালালামপুর", "Malaysia", "মালয়েশিয়া", 3.1390, 101.6869, 8.0, CalculationMethod.JAKIM, JuristicMethod.SHAFI),
    CityPreset("George Town (Penang)", "জর্জ টাউন (পেনাং)", "Malaysia", "মালয়েশিয়া", 5.4164, 100.3327, 8.0, CalculationMethod.JAKIM, JuristicMethod.SHAFI),
    CityPreset("Johor Bahru", "জহুর বাহরু", "Malaysia", "মালয়েশিয়া", 1.4927, 103.7414, 8.0, CalculationMethod.JAKIM, JuristicMethod.SHAFI),
    CityPreset("Kota Kinabalu", "কোটা কিনাবালু", "Malaysia", "মালয়েশিয়া", 5.9804, 116.0735, 8.0, CalculationMethod.JAKIM, JuristicMethod.SHAFI),
    CityPreset("Kuching", "কুচিং", "Malaysia", "মালয়েশিয়া", 1.5533, 110.3592, 8.0, CalculationMethod.JAKIM, JuristicMethod.SHAFI),
    CityPreset("Malacca City", "মেলাকা", "Malaysia", "মালয়েশিয়া", 2.1896, 102.2501, 8.0, CalculationMethod.JAKIM, JuristicMethod.SHAFI),

    // ==================== SINGAPORE (MUIS) ====================
    CityPreset("Singapore City", "সিঙ্গাপুর সিটি (MUIS)", "Singapore", "সিঙ্গাপুর", 1.3521, 103.8198, 8.0, CalculationMethod.MUIS, JuristicMethod.SHAFI),

    // ==================== INDONESIA (KEMENAG) ====================
    CityPreset("Jakarta", "জাকার্তা", "Indonesia", "ইন্দোনেশিয়া", -6.2088, 106.8456, 7.0, CalculationMethod.KEMENAG, JuristicMethod.SHAFI),
    CityPreset("Surabaya", "সুরাবায়া", "Indonesia", "ইন্দোনেশিয়া", -7.2575, 112.7521, 7.0, CalculationMethod.KEMENAG, JuristicMethod.SHAFI),
    CityPreset("Bandung", "বান্দুং", "Indonesia", "ইন্দোনেশিয়া", -6.9175, 107.6191, 7.0, CalculationMethod.KEMENAG, JuristicMethod.SHAFI),
    CityPreset("Medan", "মেদান", "Indonesia", "ইন্দোনেশিয়া", 3.5952, 98.6722, 7.0, CalculationMethod.KEMENAG, JuristicMethod.SHAFI),
    CityPreset("Semarang", "সেমারাং", "Indonesia", "ইন্দোনেশিয়া", -6.9667, 110.4167, 7.0, CalculationMethod.KEMENAG, JuristicMethod.SHAFI),
    CityPreset("Makassar", "মাকাসসার", "Indonesia", "ইন্দোনেশিয়া", -5.1477, 119.4327, 8.0, CalculationMethod.KEMENAG, JuristicMethod.SHAFI),
    CityPreset("Banda Aceh", "বান্দা আচেহ", "Indonesia", "ইন্দোনেশিয়া", 5.5483, 95.3238, 7.0, CalculationMethod.KEMENAG, JuristicMethod.SHAFI),

    // ==================== TURKEY & MIDDLE EAST ====================
    CityPreset("Istanbul", "ইস্তাম্বুল", "Turkey", "তুরস্ক", 41.0082, 28.9784, 3.0, CalculationMethod.MWL, JuristicMethod.HANAFI),
    CityPreset("Ankara", "আঙ্কারা", "Turkey", "তুরস্ক", 39.9334, 32.8597, 3.0, CalculationMethod.MWL, JuristicMethod.HANAFI),
    CityPreset("Konya", "কোনিয়া", "Turkey", "তুরস্ক", 37.8714, 32.4846, 3.0, CalculationMethod.MWL, JuristicMethod.HANAFI),
    CityPreset("Cairo", "কায়রো", "Egypt", "মিশর", 30.0444, 31.2357, 2.0, CalculationMethod.EGYPT, JuristicMethod.SHAFI),
    CityPreset("Alexandria", "আলেকজান্দ্রিয়া", "Egypt", "মিশর", 31.2001, 29.9187, 2.0, CalculationMethod.EGYPT, JuristicMethod.SHAFI),
    CityPreset("Amman", "আম্মান", "Jordan", "জর্ডান", 31.9454, 35.9284, 3.0, CalculationMethod.MWL, JuristicMethod.SHAFI),
    CityPreset("Beirut", "বৈরুত", "Lebanon", "লেবানন", 33.8938, 35.5018, 3.0, CalculationMethod.MWL, JuristicMethod.SHAFI),
    CityPreset("Baghdad", "বাগদাদ", "Iraq", "ইরাক", 33.3152, 44.3661, 3.0, CalculationMethod.MWL, JuristicMethod.HANAFI),
    CityPreset("Tehran", "তেহরান", "Iran", "ইরান", 35.6892, 51.3890, 3.5, CalculationMethod.MWL, JuristicMethod.HANAFI),

    // ==================== CENTRAL & EAST ASIA ====================
    CityPreset("Tashkent", "তাসখন্দ", "Uzbekistan", "উজবেকিস্তান", 41.2995, 69.2401, 5.0, CalculationMethod.KARACHI, JuristicMethod.HANAFI),
    CityPreset("Samarkand", "সমরখন্দ", "Uzbekistan", "উজবেকিস্তান", 39.6270, 66.9750, 5.0, CalculationMethod.KARACHI, JuristicMethod.HANAFI),
    CityPreset("Almaty", "আলমাতি", "Kazakhstan", "কাজাখস্তান", 43.2220, 76.8512, 5.0, CalculationMethod.MWL, JuristicMethod.HANAFI),
    CityPreset("Bishkek", "বিশকেক", "Kyrgyzstan", "কিরগিজস্তান", 42.8746, 74.5698, 6.0, CalculationMethod.MWL, JuristicMethod.HANAFI),
    CityPreset("Dushanbe", "দুশানবে", "Tajikistan", "তাজিকিস্তান", 38.5598, 68.7870, 5.0, CalculationMethod.MWL, JuristicMethod.HANAFI),
    CityPreset("Kabul", "কাবুল", "Afghanistan", "আফগানিস্তান", 34.5553, 69.2075, 4.5, CalculationMethod.KARACHI, JuristicMethod.HANAFI),
    CityPreset("Male", "মালে", "Maldives", "মালদ্বীপ", 4.1755, 73.5093, 5.0, CalculationMethod.MWL, JuristicMethod.SHAFI),
    CityPreset("Colombo", "কলম্বো", "Sri Lanka", "শ্রীলঙ্কা", 6.9271, 79.8612, 5.5, CalculationMethod.KARACHI, JuristicMethod.SHAFI),
    CityPreset("Bangkok", "ব্যাংকক", "Thailand", "থাইল্যান্ড", 13.7563, 100.5018, 7.0, CalculationMethod.MWL, JuristicMethod.SHAFI),
    CityPreset("Tokyo", "টোকিও", "Japan", "জাপান", 35.6762, 139.6503, 9.0, CalculationMethod.MWL, JuristicMethod.HANAFI),
    CityPreset("Seoul", "সিউল", "South Korea", "দক্ষিণ কোরিয়া", 37.5665, 126.9780, 9.0, CalculationMethod.MWL, JuristicMethod.HANAFI),
    CityPreset("Beijing", "বেইজিং", "China", "চীন", 39.9042, 116.4074, 8.0, CalculationMethod.MWL, JuristicMethod.HANAFI),

    // ==================== WESTERN / GLOBAL MAJOR DIASPORA ====================
    CityPreset("London", "লন্ডন", "United Kingdom", "যুক্তরাজ্য", 51.5074, -0.1278, 0.0, CalculationMethod.MWL, JuristicMethod.HANAFI),
    CityPreset("New York", "নিউ ইয়র্ক", "United States", "যুক্তরাষ্ট্র", 40.7128, -74.0060, -5.0, CalculationMethod.ISNA, JuristicMethod.HANAFI),
    CityPreset("Toronto", "টরন্টো", "Canada", "কানাডা", 43.6532, -79.3832, -5.0, CalculationMethod.ISNA, JuristicMethod.HANAFI),
    CityPreset("Sydney", "সিডনি", "Australia", "অস্ট্রেলিয়া", -33.8688, 151.2093, 10.0, CalculationMethod.MWL, JuristicMethod.HANAFI)
  )
}

/**
 * Service to manage Location & GPS updates
 */
class IslamicLocationService(private val context: Context) {

  fun hasLocationPermission(): Boolean {
    val fine = ContextCompat.checkSelfPermission(
      context,
      Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED
    val coarse = ContextCompat.checkSelfPermission(
      context,
      Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED
    return fine || coarse
  }

  @SuppressLint("MissingPermission")
  fun fetchCurrentLocation(
    onSuccess: (latitude: Double, longitude: Double, locationName: String, timezoneHours: Double) -> Unit,
    onFailure: (String) -> Unit
  ) {
    if (!hasLocationPermission()) {
      onFailure("Location permission not granted")
      return
    }

    try {
      val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
      fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
        .addOnSuccessListener { loc: Location? ->
          if (loc != null) {
            val name = resolveAddress(loc.latitude, loc.longitude)
            val tz = PrayerTimesCalculator.resolveTimezone(loc.longitude, getSystemTimezoneHours())
            onSuccess(loc.latitude, loc.longitude, name, tz)
          } else {
            fallbackLocationManager(onSuccess, onFailure)
          }
        }
        .addOnFailureListener {
          fallbackLocationManager(onSuccess, onFailure)
        }
    } catch (e: Exception) {
      fallbackLocationManager(onSuccess, onFailure)
    }
  }

  fun getSystemTimezoneHours(): Double {
    val tz = java.util.TimeZone.getDefault()
    val now = java.util.Date()
    return tz.rawOffset / 3600000.0 + if (tz.inDaylightTime(now)) 1.0 else 0.0
  }

  @SuppressLint("MissingPermission")
  private fun fallbackLocationManager(
    onSuccess: (Double, Double, String, Double) -> Unit,
    onFailure: (String) -> Unit
  ) {
    try {
      val locManager = context.getSystemService(Context.LOCATION_SERVICE) as? LocationManager
      if (locManager == null) {
        onFailure("Location Manager unavailable")
        return
      }

      val gpsLoc = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
      val netLoc = locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
      val best = gpsLoc ?: netLoc

      if (best != null) {
        val name = resolveAddress(best.latitude, best.longitude)
        val tz = PrayerTimesCalculator.resolveTimezone(best.longitude, getSystemTimezoneHours())
        onSuccess(best.latitude, best.longitude, name, tz)
      } else {
        val listener = object : LocationListener {
          override fun onLocationChanged(loc: Location) {
            locManager.removeUpdates(this)
            val name = resolveAddress(loc.latitude, loc.longitude)
            val tz = PrayerTimesCalculator.resolveTimezone(loc.longitude, getSystemTimezoneHours())
            onSuccess(loc.latitude, loc.longitude, name, tz)
          }
          @Deprecated("Deprecated in Java")
          override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
          override fun onProviderEnabled(provider: String) {}
          override fun onProviderDisabled(provider: String) {}
        }
        locManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, listener, null)
      }
    } catch (e: Exception) {
      onFailure(e.localizedMessage ?: "Failed to get location")
    }
  }

  private fun resolveAddress(lat: Double, lng: Double): String {
    return try {
      val geocoder = Geocoder(context, Locale.getDefault())
      val addresses = geocoder.getFromLocation(lat, lng, 1)
      if (!addresses.isNullOrEmpty()) {
        val addr = addresses[0]
        val locality = addr.locality ?: addr.subAdminArea ?: addr.adminArea
        val country = addr.countryName
        if (!locality.isNullOrBlank() && !country.isNullOrBlank()) {
          "$locality, $country"
        } else {
          locality ?: country ?: String.format(Locale.US, "%.4f, %.4f", lat, lng)
        }
      } else {
        String.format(Locale.US, "%.4f, %.4f", lat, lng)
      }
    } catch (e: Exception) {
      String.format(Locale.US, "%.4f, %.4f", lat, lng)
    }
  }
}
