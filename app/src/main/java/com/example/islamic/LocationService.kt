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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
  val defaultJuristic: JuristicMethod = JuristicMethod.HANAFI,
  val regionCategory: String = "ASIA",
  val stateOrDivision: String = ""
)

object LocationPresets {
  val defaultCities: List<CityPreset> = listOf(
    // ==================== BANGLADESH (ALL 64 DISTRICTS & DIVISIONS) ====================
    // Dhaka Division
    CityPreset("Dhaka", "ঢাকা", "Bangladesh", "বাংলাদেশ", 23.8103, 90.4125, 6.0, CalculationMethod.KARACHI, JuristicMethod.HANAFI, "BD", "ঢাকা বিভাগ"),
    CityPreset("Gazipur", "গাজীপুর", "Bangladesh", "বাংলাদেশ", 23.9999, 90.4203, 6.0, CalculationMethod.KARACHI, JuristicMethod.HANAFI, "BD", "ঢাকা বিভাগ"),
    CityPreset("Narayanganj", "নারায়ণগঞ্জ", "Bangladesh", "বাংলাদেশ", 23.6238, 90.5000, 6.0, CalculationMethod.KARACHI, JuristicMethod.HANAFI, "BD", "ঢাকা বিভাগ"),
    CityPreset("Tangail", "টাঙ্গাইল", "Bangladesh", "বাংলাদেশ", 24.2513, 89.9167, 6.0, CalculationMethod.KARACHI, JuristicMethod.HANAFI, "BD", "ঢাকা বিভাগ"),
    CityPreset("Faridpur", "ফরিদপুর", "Bangladesh", "বাংলাদেশ", 23.6071, 89.8429, 6.0, CalculationMethod.KARACHI, JuristicMethod.HANAFI, "BD", "ঢাকা বিভাগ"),
    CityPreset("Narsingdi", "নরসিংদী", "Bangladesh", "বাংলাদেশ", 23.9193, 90.7202, 6.0, CalculationMethod.KARACHI, JuristicMethod.HANAFI, "BD", "ঢাকা বিভাগ"),
    CityPreset("Manikganj", "মানিকগঞ্জ", "Bangladesh", "বাংলাদেশ", 23.8644, 90.0047, 6.0, CalculationMethod.KARACHI, JuristicMethod.HANAFI, "BD", "ঢাকা বিভাগ"),
    CityPreset("Munshiganj", "মুন্সীগঞ্জ", "Bangladesh", "বাংলাদেশ", 23.5422, 90.5305, 6.0, CalculationMethod.KARACHI, JuristicMethod.HANAFI, "BD", "ঢাকা বিভাগ"),
    CityPreset("Gopalganj", "গোপালগঞ্জ", "Bangladesh", "বাংলাদেশ", 23.0051, 89.8266, 6.0, CalculationMethod.KARACHI, JuristicMethod.HANAFI, "BD", "ঢাকা বিভাগ"),
    CityPreset("Madaripur", "মাদারীপুর", "Bangladesh", "বাংলাদেশ", 23.1641, 90.1897, 6.0, CalculationMethod.KARACHI, JuristicMethod.HANAFI, "BD", "ঢাকা বিভাগ"),
    CityPreset("Rajbari", "রাজবাড়ী", "Bangladesh", "বাংলাদেশ", 23.7574, 89.6445, 6.0, CalculationMethod.KARACHI, JuristicMethod.HANAFI, "BD", "ঢাকা বিভাগ"),
    CityPreset("Shariatpur", "শরীয়তপুর", "Bangladesh", "বাংলাদেশ", 23.2423, 90.4348, 6.0, CalculationMethod.KARACHI, JuristicMethod.HANAFI, "BD", "ঢাকা বিভাগ"),
    CityPreset("Kishoreganj", "কিশোরগঞ্জ", "Bangladesh", "বাংলাদেশ", 24.4449, 90.7766, 6.0, CalculationMethod.KARACHI, JuristicMethod.HANAFI, "BD", "ঢাকা বিভাগ"),

    // Chittagong Division
    CityPreset("Chittagong (Chattogram)", "চট্টগ্রাম", "Bangladesh", "বাংলাদেশ", 22.3569, 91.7832, 6.0, CalculationMethod.KARACHI, JuristicMethod.HANAFI, "BD", "চট্টগ্রাম বিভাগ"),
    CityPreset("Cox's Bazar", "কক্সবাজার", "Bangladesh", "বাংলাদেশ", 21.4272, 92.0058, 6.0, CalculationMethod.KARACHI, JuristicMethod.HANAFI, "BD", "চট্টগ্রাম বিভাগ"),
    CityPreset("Comilla (Cumilla)", "কুমিল্লা", "Bangladesh", "বাংলাদেশ", 23.4682, 91.1788, 6.0, CalculationMethod.KARACHI, JuristicMethod.HANAFI, "BD", "চট্টগ্রাম বিভাগ"),
    CityPreset("Feni", "ফেনী", "Bangladesh", "বাংলাদেশ", 23.0159, 91.3976, 6.0, CalculationMethod.KARACHI, JuristicMethod.HANAFI, "BD", "চট্টগ্রাম বিভাগ"),
    CityPreset("Brahmanbaria", "ব্রাহ্মণবাড়িয়া", "Bangladesh", "বাংলাদেশ", 23.9571, 91.1119, 6.0, CalculationMethod.KARACHI, JuristicMethod.HANAFI, "BD", "চট্টগ্রাম বিভাগ"),
    CityPreset("Noakhali", "নোয়াখালী", "Bangladesh", "বাংলাদেশ", 22.8696, 91.0994, 6.0, CalculationMethod.KARACHI, JuristicMethod.HANAFI, "BD", "চট্টগ্রাম বিভাগ"),
    CityPreset("Chandpur", "চাঁদপুর", "Bangladesh", "বাংলাদেশ", 23.2333, 90.6667, 6.0, CalculationMethod.KARACHI, JuristicMethod.HANAFI, "BD", "চট্টগ্রাম বিভাগ"),
    CityPreset("Lakshmipur", "লক্ষ্মীপুর", "Bangladesh", "বাংলাদেশ", 22.9425, 90.8412, 6.0, CalculationMethod.KARACHI, JuristicMethod.HANAFI, "BD", "চট্টগ্রাম বিভাগ"),
    CityPreset("Bandarban", "বান্দরবান", "Bangladesh", "বাংলাদেশ", 22.1953, 92.2184, 6.0, CalculationMethod.KARACHI, JuristicMethod.HANAFI, "BD", "চট্টগ্রাম বিভাগ"),
    CityPreset("Rangamati", "রাঙ্গামাটি", "Bangladesh", "বাংলাদেশ", 22.6533, 92.1753, 6.0, CalculationMethod.KARACHI, JuristicMethod.HANAFI, "BD", "চট্টগ্রাম বিভাগ"),
    CityPreset("Khagrachhari", "খাগড়াছড়ি", "Bangladesh", "বাংলাদেশ", 23.1193, 91.9847, 6.0, CalculationMethod.KARACHI, JuristicMethod.HANAFI, "BD", "চট্টগ্রাম বিভাগ"),

    // Sylhet Division
    CityPreset("Sylhet", "সিলেট", "Bangladesh", "বাংলাদেশ", 24.8949, 91.8687, 6.0, CalculationMethod.KARACHI, JuristicMethod.HANAFI, "BD", "সিলেট বিভাগ"),
    CityPreset("Moulvibazar", "মৌলভীবাজার", "Bangladesh", "বাংলাদেশ", 24.4829, 91.7774, 6.0, CalculationMethod.KARACHI, JuristicMethod.HANAFI, "BD", "সিলেট বিভাগ"),
    CityPreset("Habiganj", "হবিগঞ্জ", "Bangladesh", "বাংলাদেশ", 24.3750, 91.4167, 6.0, CalculationMethod.KARACHI, JuristicMethod.HANAFI, "BD", "সিলেট বিভাগ"),
    CityPreset("Sunamganj", "সুনামগঞ্জ", "Bangladesh", "বাংলাদেশ", 25.0658, 91.3950, 6.0, CalculationMethod.KARACHI, JuristicMethod.HANAFI, "BD", "সিলেট বিভাগ"),

    // Rajshahi Division
    CityPreset("Rajshahi", "রাজশাহী", "Bangladesh", "বাংলাদেশ", 24.3745, 88.6042, 6.0, CalculationMethod.KARACHI, JuristicMethod.HANAFI, "BD", "রাজশাহী বিভাগ"),
    CityPreset("Bogura (Bogra)", "বগুড়া", "Bangladesh", "বাংলাদেশ", 24.8465, 89.3777, 6.0, CalculationMethod.KARACHI, JuristicMethod.HANAFI, "BD", "রাজশাহী বিভাগ"),
    CityPreset("Pabna", "পাবনা", "Bangladesh", "বাংলাদেশ", 24.0064, 89.2372, 6.0, CalculationMethod.KARACHI, JuristicMethod.HANAFI, "BD", "রাজশাহী বিভাগ"),
    CityPreset("Sirajganj", "সিরাজগঞ্জ", "Bangladesh", "বাংলাদেশ", 24.4534, 89.7008, 6.0, CalculationMethod.KARACHI, JuristicMethod.HANAFI, "BD", "রাজশাহী বিভাগ"),
    CityPreset("Natore", "নাটোর", "Bangladesh", "বাংলাদেশ", 24.4206, 88.9324, 6.0, CalculationMethod.KARACHI, JuristicMethod.HANAFI, "BD", "রাজশাহী বিভাগ"),
    CityPreset("Naogaon", "নওগাঁ", "Bangladesh", "বাংলাদেশ", 24.7936, 88.9318, 6.0, CalculationMethod.KARACHI, JuristicMethod.HANAFI, "BD", "রাজশাহী বিভাগ"),
    CityPreset("Chapainawabganj", "চাঁপাইনবাবগঞ্জ", "Bangladesh", "বাংলাদেশ", 24.5965, 88.2776, 6.0, CalculationMethod.KARACHI, JuristicMethod.HANAFI, "BD", "রাজশাহী বিভাগ"),
    CityPreset("Joypurhat", "জয়পুরহাট", "Bangladesh", "বাংলাদেশ", 25.1015, 89.0277, 6.0, CalculationMethod.KARACHI, JuristicMethod.HANAFI, "BD", "রাজশাহী বিভাগ"),

    // Khulna Division
    CityPreset("Khulna", "খুলনা", "Bangladesh", "বাংলাদেশ", 22.8456, 89.5403, 6.0, CalculationMethod.KARACHI, JuristicMethod.HANAFI, "BD", "খুলনা বিভাগ"),
    CityPreset("Jessore (Jashore)", "যশোর", "Bangladesh", "বাংলাদেশ", 23.1664, 89.2081, 6.0, CalculationMethod.KARACHI, JuristicMethod.HANAFI, "BD", "খুলনা বিভাগ"),
    CityPreset("Kushtia", "কুষ্টিয়া", "Bangladesh", "বাংলাদেশ", 23.9013, 89.1204, 6.0, CalculationMethod.KARACHI, JuristicMethod.HANAFI, "BD", "খুলনা বিভাগ"),
    CityPreset("Jhenaidah", "ঝিনাইদহ", "Bangladesh", "বাংলাদেশ", 23.5448, 89.1539, 6.0, CalculationMethod.KARACHI, JuristicMethod.HANAFI, "BD", "খুলনা বিভাগ"),
    CityPreset("Magura", "মাগুরা", "Bangladesh", "বাংলাদেশ", 23.4873, 89.4198, 6.0, CalculationMethod.KARACHI, JuristicMethod.HANAFI, "BD", "খুলনা বিভাগ"),
    CityPreset("Narail", "নড়াইল", "Bangladesh", "বাংলাদেশ", 23.1725, 89.5127, 6.0, CalculationMethod.KARACHI, JuristicMethod.HANAFI, "BD", "খুলনা বিভাগ"),
    CityPreset("Satkhira", "সাতক্ষীরা", "Bangladesh", "বাংলাদেশ", 22.7185, 89.0705, 6.0, CalculationMethod.KARACHI, JuristicMethod.HANAFI, "BD", "খুলনা বিভাগ"),
    CityPreset("Bagerhat", "বাগেরহাট", "Bangladesh", "বাংলাদেশ", 22.6516, 89.7859, 6.0, CalculationMethod.KARACHI, JuristicMethod.HANAFI, "BD", "খুলনা বিভাগ"),
    CityPreset("Chuadanga", "চুয়াডাঙ্গা", "Bangladesh", "বাংলাদেশ", 23.6402, 88.8418, 6.0, CalculationMethod.KARACHI, JuristicMethod.HANAFI, "BD", "খুলনা বিভাগ"),
    CityPreset("Meherpur", "মেহেরপুর", "Bangladesh", "বাংলাদেশ", 23.7622, 88.6318, 6.0, CalculationMethod.KARACHI, JuristicMethod.HANAFI, "BD", "খুলনা বিভাগ"),

    // Barishal Division
    CityPreset("Barisal (Barishal)", "বরিশাল", "Bangladesh", "বাংলাদেশ", 22.7010, 90.3535, 6.0, CalculationMethod.KARACHI, JuristicMethod.HANAFI, "BD", "বরিশাল বিভাগ"),
    CityPreset("Patuakhali", "পটুয়াখালী", "Bangladesh", "বাংলাদেশ", 22.3596, 90.3299, 6.0, CalculationMethod.KARACHI, JuristicMethod.HANAFI, "BD", "বরিশাল বিভাগ"),
    CityPreset("Bhola", "ভোলা", "Bangladesh", "বাংলাদেশ", 22.6859, 90.6481, 6.0, CalculationMethod.KARACHI, JuristicMethod.HANAFI, "BD", "বরিশাল বিভাগ"),
    CityPreset("Pirojpur", "পিরোজপুর", "Bangladesh", "বাংলাদেশ", 22.5841, 89.9720, 6.0, CalculationMethod.KARACHI, JuristicMethod.HANAFI, "BD", "বরিশাল বিভাগ"),
    CityPreset("Barguna", "বরগুনা", "Bangladesh", "বাংলাদেশ", 22.0953, 90.1121, 6.0, CalculationMethod.KARACHI, JuristicMethod.HANAFI, "BD", "বরিশাল বিভাগ"),
    CityPreset("Jhalokati", "ঝালকাঠি", "Bangladesh", "বাংলাদেশ", 22.6406, 90.1987, 6.0, CalculationMethod.KARACHI, JuristicMethod.HANAFI, "BD", "বরিশাল বিভাগ"),

    // Rangpur Division
    CityPreset("Rangpur", "রংপুর", "Bangladesh", "বাংলাদেশ", 25.7439, 89.2752, 6.0, CalculationMethod.KARACHI, JuristicMethod.HANAFI, "BD", "রংপুর বিভাগ"),
    CityPreset("Dinajpur", "দিনাজপুর", "Bangladesh", "বাংলাদেশ", 25.6217, 88.6354, 6.0, CalculationMethod.KARACHI, JuristicMethod.HANAFI, "BD", "রংপুর বিভাগ"),
    CityPreset("Kurigram", "কুড়িগ্রাম", "Bangladesh", "বাংলাদেশ", 25.8054, 89.6362, 6.0, CalculationMethod.KARACHI, JuristicMethod.HANAFI, "BD", "রংপুর বিভাগ"),
    CityPreset("Gaibandha", "গাইবান্ধা", "Bangladesh", "বাংলাদেশ", 25.3288, 89.5408, 6.0, CalculationMethod.KARACHI, JuristicMethod.HANAFI, "BD", "রংপুর বিভাগ"),
    CityPreset("Nilphamari", "নীলফামারী", "Bangladesh", "বাংলাদেশ", 25.9318, 88.8560, 6.0, CalculationMethod.KARACHI, JuristicMethod.HANAFI, "BD", "রংপুর বিভাগ"),
    CityPreset("Lalmonirhat", "লালমনিরহাট", "Bangladesh", "বাংলাদেশ", 25.9923, 89.2847, 6.0, CalculationMethod.KARACHI, JuristicMethod.HANAFI, "BD", "রংপুর বিভাগ"),
    CityPreset("Thakurgaon", "ঠাকুরগাঁও", "Bangladesh", "বাংলাদেশ", 26.0337, 88.4617, 6.0, CalculationMethod.KARACHI, JuristicMethod.HANAFI, "BD", "রংপুর বিভাগ"),
    CityPreset("Panchagarh", "পঞ্চগড়", "Bangladesh", "বাংলাদেশ", 26.3411, 88.5542, 6.0, CalculationMethod.KARACHI, JuristicMethod.HANAFI, "BD", "রংপুর বিভাগ"),

    // Mymensingh Division
    CityPreset("Mymensingh", "ময়মনসিংহ", "Bangladesh", "বাংলাদেশ", 24.7471, 90.4203, 6.0, CalculationMethod.KARACHI, JuristicMethod.HANAFI, "BD", "ময়মনসিংহ বিভাগ"),
    CityPreset("Jamalpur", "জামালপুর", "Bangladesh", "বাংলাদেশ", 24.9375, 89.9378, 6.0, CalculationMethod.KARACHI, JuristicMethod.HANAFI, "BD", "ময়মনসিংহ বিভাগ"),
    CityPreset("Netrokona", "নেত্রকোণা", "Bangladesh", "বাংলাদেশ", 24.8709, 90.7279, 6.0, CalculationMethod.KARACHI, JuristicMethod.HANAFI, "BD", "ময়মনসিংহ বিভাগ"),
    CityPreset("Sherpur", "শেরপুর", "Bangladesh", "বাংলাদেশ", 25.0205, 90.0153, 6.0, CalculationMethod.KARACHI, JuristicMethod.HANAFI, "BD", "ময়মনসিংহ বিভাগ"),

    // ==================== SAUDI ARABIA & GULF (MIDDLE EAST) ====================
    CityPreset("Makkah Al-Mukarramah", "মক্কা মুকাররমা", "Saudi Arabia", "সৌদি আরব", 21.4225, 39.8262, 3.0, CalculationMethod.MAKKAH, JuristicMethod.SHAFI, "GULF", "Makkah"),
    CityPreset("Madinah Al-Munawwarah", "মদিনা মুনাওয়ারা", "Saudi Arabia", "সৌদি আরব", 24.4672, 39.6111, 3.0, CalculationMethod.MAKKAH, JuristicMethod.SHAFI, "GULF", "Al Madinah"),
    CityPreset("Riyadh", "রিয়াদ", "Saudi Arabia", "সৌদি আরব", 24.7136, 46.6753, 3.0, CalculationMethod.MAKKAH, JuristicMethod.SHAFI, "GULF", "Riyadh"),
    CityPreset("Jeddah", "জেদ্দা", "Saudi Arabia", "সৌদি আরব", 21.5433, 39.1728, 3.0, CalculationMethod.MAKKAH, JuristicMethod.SHAFI, "GULF", "Makkah"),
    CityPreset("Dammam", "দাম্মাম", "Saudi Arabia", "সৌদি আরব", 26.4207, 50.0888, 3.0, CalculationMethod.MAKKAH, JuristicMethod.SHAFI, "GULF", "Eastern Province"),
    CityPreset("Al Khobar", "আল খোবার", "Saudi Arabia", "সৌদি আরব", 26.2172, 50.1971, 3.0, CalculationMethod.MAKKAH, JuristicMethod.SHAFI, "GULF", "Eastern Province"),
    CityPreset("Taif", "তায়েফ", "Saudi Arabia", "সৌদি আরব", 21.2854, 40.4222, 3.0, CalculationMethod.MAKKAH, JuristicMethod.SHAFI, "GULF", "Makkah"),
    CityPreset("Tabuk", "তাবুক", "Saudi Arabia", "সৌদি আরব", 28.3835, 36.5662, 3.0, CalculationMethod.MAKKAH, JuristicMethod.SHAFI, "GULF", "Tabuk"),
    CityPreset("Buraydah", "বুরাইদাহ", "Saudi Arabia", "সৌদি আরব", 26.3592, 43.9818, 3.0, CalculationMethod.MAKKAH, JuristicMethod.SHAFI, "GULF", "Al Qassim"),
    CityPreset("Abha", "আভা", "Saudi Arabia", "সৌদি আরব", 18.2164, 42.5053, 3.0, CalculationMethod.MAKKAH, JuristicMethod.SHAFI, "GULF", "Asir"),
    CityPreset("Dubai", "দুবাই", "United Arab Emirates", "সংযুক্ত আরব আমিরাত", 25.2048, 55.2708, 4.0, CalculationMethod.DUBAI, JuristicMethod.SHAFI, "GULF", "Dubai"),
    CityPreset("Abu Dhabi", "আবুধাবি", "United Arab Emirates", "সংযুক্ত আরব আমিরাত", 24.4539, 54.3773, 4.0, CalculationMethod.DUBAI, JuristicMethod.SHAFI, "GULF", "Abu Dhabi"),
    CityPreset("Sharjah", "শারজাহ", "United Arab Emirates", "সংযুক্ত আরব আমিরাত", 25.3463, 55.4209, 4.0, CalculationMethod.DUBAI, JuristicMethod.SHAFI, "GULF", "Sharjah"),
    CityPreset("Ajman", "আজমান", "United Arab Emirates", "সংযুক্ত আরব আমিরাত", 25.4052, 55.5136, 4.0, CalculationMethod.DUBAI, JuristicMethod.SHAFI, "GULF", "Ajman"),
    CityPreset("Ras Al Khaimah", "রাস আল খাইমাহ", "United Arab Emirates", "সংযুক্ত আরব আমিরাত", 25.7895, 55.9432, 4.0, CalculationMethod.DUBAI, JuristicMethod.SHAFI, "GULF", "RAK"),
    CityPreset("Al Ain", "আল আইন", "United Arab Emirates", "সংযুক্ত আরব আমিরাত", 24.2075, 55.7447, 4.0, CalculationMethod.DUBAI, JuristicMethod.SHAFI, "GULF", "Abu Dhabi"),
    CityPreset("Doha", "দোহা", "Qatar", "কাতার", 25.2854, 51.5310, 3.0, CalculationMethod.MAKKAH, JuristicMethod.SHAFI, "GULF", "Ad Dawhah"),
    CityPreset("Al Wakrah", "আল ওয়াকরাহ", "Qatar", "কাতার", 25.1768, 51.6048, 3.0, CalculationMethod.MAKKAH, JuristicMethod.SHAFI, "GULF", "Al Wakrah"),
    CityPreset("Kuwait City", "কুয়েত সিটি", "Kuwait", "কুয়েত", 29.3759, 47.9774, 3.0, CalculationMethod.MAKKAH, JuristicMethod.SHAFI, "GULF", "Al Asimah"),
    CityPreset("Manama", "মানামা", "Bahrain", "বাহরাইন", 26.2285, 50.5860, 3.0, CalculationMethod.MAKKAH, JuristicMethod.SHAFI, "GULF", "Capital"),
    CityPreset("Muscat", "মাসকাট", "Oman", "ওমান", 23.5859, 58.4059, 4.0, CalculationMethod.MWL, JuristicMethod.SHAFI, "GULF", "Muscat"),
    CityPreset("Salalah", "সালালাহ", "Oman", "ওমান", 17.0151, 54.0924, 4.0, CalculationMethod.MWL, JuristicMethod.SHAFI, "GULF", "Dhofar"),

    // ==================== SOUTH ASIA ====================
    CityPreset("Karachi", "করাচি", "Pakistan", "পাকিস্তান", 24.8607, 67.0011, 5.0, CalculationMethod.KARACHI, JuristicMethod.HANAFI, "SOUTH_ASIA", "Sindh"),
    CityPreset("Lahore", "লাহোর", "Pakistan", "পাকিস্তান", 31.5204, 74.3587, 5.0, CalculationMethod.KARACHI, JuristicMethod.HANAFI, "SOUTH_ASIA", "Punjab"),
    CityPreset("Islamabad", "ইসলামাবাদ", "Pakistan", "পাকিস্তান", 33.6844, 73.0479, 5.0, CalculationMethod.KARACHI, JuristicMethod.HANAFI, "SOUTH_ASIA", "Capital"),
    CityPreset("Rawalpindi", "রাওয়ালপিন্ডি", "Pakistan", "পাকিস্তান", 33.5651, 73.0169, 5.0, CalculationMethod.KARACHI, JuristicMethod.HANAFI, "SOUTH_ASIA", "Punjab"),
    CityPreset("Faisalabad", "ফয়সালাবাদ", "Pakistan", "পাকিস্তান", 31.4504, 73.1350, 5.0, CalculationMethod.KARACHI, JuristicMethod.HANAFI, "SOUTH_ASIA", "Punjab"),
    CityPreset("Peshawar", "পেশোয়ার", "Pakistan", "পাকিস্তান", 34.0151, 71.5249, 5.0, CalculationMethod.KARACHI, JuristicMethod.HANAFI, "SOUTH_ASIA", "Khyber Pakhtunkhwa"),
    CityPreset("Quetta", "কোয়েটা", "Pakistan", "পাকিস্তান", 30.1798, 66.9750, 5.0, CalculationMethod.KARACHI, JuristicMethod.HANAFI, "SOUTH_ASIA", "Balochistan"),
    CityPreset("Multan", "মুলতান", "Pakistan", "পাকিস্তান", 30.1575, 71.5249, 5.0, CalculationMethod.KARACHI, JuristicMethod.HANAFI, "SOUTH_ASIA", "Punjab"),
    CityPreset("New Delhi", "নয়াদিল্লি", "India", "ভারত", 28.6139, 77.2090, 5.5, CalculationMethod.KARACHI, JuristicMethod.HANAFI, "SOUTH_ASIA", "Delhi"),
    CityPreset("Kolkata", "কলকাতা", "India", "ভারত", 22.5726, 88.3639, 5.5, CalculationMethod.KARACHI, JuristicMethod.HANAFI, "SOUTH_ASIA", "West Bengal"),
    CityPreset("Mumbai", "মুম্বাই", "India", "ভারত", 19.0760, 72.8777, 5.5, CalculationMethod.KARACHI, JuristicMethod.HANAFI, "SOUTH_ASIA", "Maharashtra"),
    CityPreset("Hyderabad", "হায়দ্রাবাদ", "India", "ভারত", 17.3850, 78.4867, 5.5, CalculationMethod.KARACHI, JuristicMethod.HANAFI, "SOUTH_ASIA", "Telangana"),
    CityPreset("Bengaluru", "বেঙ্গালুরু", "India", "ভারত", 12.9716, 77.5946, 5.5, CalculationMethod.KARACHI, JuristicMethod.HANAFI, "SOUTH_ASIA", "Karnataka"),
    CityPreset("Chennai", "চেন্নাই", "India", "ভারত", 13.0827, 80.2707, 5.5, CalculationMethod.KARACHI, JuristicMethod.SHAFI, "SOUTH_ASIA", "Tamil Nadu"),
    CityPreset("Lucknow", "লখনউ", "India", "ভারত", 26.8467, 80.9462, 5.5, CalculationMethod.KARACHI, JuristicMethod.HANAFI, "SOUTH_ASIA", "Uttar Pradesh"),
    CityPreset("Ahmedabad", "আহমেদাবাদ", "India", "ভারত", 23.0225, 72.5714, 5.5, CalculationMethod.KARACHI, JuristicMethod.HANAFI, "SOUTH_ASIA", "Gujarat"),
    CityPreset("Srinagar", "শ্রীনগর (কাশ্মীর)", "India", "ভারত", 34.0837, 74.7973, 5.5, CalculationMethod.KARACHI, JuristicMethod.HANAFI, "SOUTH_ASIA", "Jammu & Kashmir"),
    CityPreset("Patna", "পাটনা", "India", "ভারত", 25.5941, 85.1376, 5.5, CalculationMethod.KARACHI, JuristicMethod.HANAFI, "SOUTH_ASIA", "Bihar"),
    CityPreset("Kozhikode", "কালিকট (কেরালা)", "India", "ভারত", 11.2588, 75.7804, 5.5, CalculationMethod.KARACHI, JuristicMethod.SHAFI, "SOUTH_ASIA", "Kerala"),
    CityPreset("Male", "মালে", "Maldives", "মালদ্বীপ", 4.1755, 73.5093, 5.0, CalculationMethod.MWL, JuristicMethod.SHAFI, "SOUTH_ASIA", "Kaafu"),
    CityPreset("Colombo", "কলম্বো", "Sri Lanka", "শ্রীলঙ্কা", 6.9271, 79.8612, 5.5, CalculationMethod.KARACHI, JuristicMethod.SHAFI, "SOUTH_ASIA", "Western"),
    CityPreset("Kandy", "ক্যান্ডি", "Sri Lanka", "শ্রীলঙ্কা", 7.2906, 80.6337, 5.5, CalculationMethod.KARACHI, JuristicMethod.SHAFI, "SOUTH_ASIA", "Central"),
    CityPreset("Kathmandu", "কাঠমান্ডু", "Nepal", "নেপাল", 27.7172, 85.3240, 5.75, CalculationMethod.KARACHI, JuristicMethod.HANAFI, "SOUTH_ASIA", "Bagmati"),
    CityPreset("Thimphu", "থিম্পু", "Bhutan", "ভুটান", 27.4728, 89.6393, 6.0, CalculationMethod.KARACHI, JuristicMethod.HANAFI, "SOUTH_ASIA", "Thimphu"),
    CityPreset("Kabul", "কাবুল", "Afghanistan", "আফগানিস্তান", 34.5553, 69.2075, 4.5, CalculationMethod.KARACHI, JuristicMethod.HANAFI, "SOUTH_ASIA", "Kabul"),
    CityPreset("Herat", "হেরাত", "Afghanistan", "আফগানিস্তান", 34.3529, 62.2040, 4.5, CalculationMethod.KARACHI, JuristicMethod.HANAFI, "SOUTH_ASIA", "Herat"),

    // ==================== SOUTHEAST ASIA ====================
    CityPreset("Kuala Lumpur", "কুয়ালালামপুর", "Malaysia", "মালয়েশিয়া", 3.1390, 101.6869, 8.0, CalculationMethod.JAKIM, JuristicMethod.SHAFI, "SOUTHEAST_ASIA", "Federal Territory"),
    CityPreset("George Town (Penang)", "জর্জ টাউন (পেনাং)", "Malaysia", "মালয়েশিয়া", 5.4164, 100.3327, 8.0, CalculationMethod.JAKIM, JuristicMethod.SHAFI, "SOUTHEAST_ASIA", "Penang"),
    CityPreset("Johor Bahru", "জহুর বাহরু", "Malaysia", "মালয়েশিয়া", 1.4927, 103.7414, 8.0, CalculationMethod.JAKIM, JuristicMethod.SHAFI, "SOUTHEAST_ASIA", "Johor"),
    CityPreset("Kota Kinabalu", "কোটা কিনাবালু", "Malaysia", "মালয়েশিয়া", 5.9804, 116.0735, 8.0, CalculationMethod.JAKIM, JuristicMethod.SHAFI, "SOUTHEAST_ASIA", "Sabah"),
    CityPreset("Kuching", "কুচিং", "Malaysia", "মালয়েশিয়া", 1.5533, 110.3592, 8.0, CalculationMethod.JAKIM, JuristicMethod.SHAFI, "SOUTHEAST_ASIA", "Sarawak"),
    CityPreset("Malacca City", "মেলাকা", "Malaysia", "মালয়েশিয়া", 2.1896, 102.2501, 8.0, CalculationMethod.JAKIM, JuristicMethod.SHAFI, "SOUTHEAST_ASIA", "Malacca"),
    CityPreset("Singapore City", "সিঙ্গাপুর সিটি (MUIS)", "Singapore", "সিঙ্গাপুর", 1.3521, 103.8198, 8.0, CalculationMethod.MUIS, JuristicMethod.SHAFI, "SOUTHEAST_ASIA", "Singapore"),
    CityPreset("Jakarta", "জাকার্তা", "Indonesia", "ইন্দোনেশিয়া", -6.2088, 106.8456, 7.0, CalculationMethod.KEMENAG, JuristicMethod.SHAFI, "SOUTHEAST_ASIA", "DKI Jakarta"),
    CityPreset("Surabaya", "সুরাবায়া", "Indonesia", "ইন্দোনেশিয়া", -7.2575, 112.7521, 7.0, CalculationMethod.KEMENAG, JuristicMethod.SHAFI, "SOUTHEAST_ASIA", "East Java"),
    CityPreset("Bandung", "বান্দুং", "Indonesia", "ইন্দোনেশিয়া", -6.9175, 107.6191, 7.0, CalculationMethod.KEMENAG, JuristicMethod.SHAFI, "SOUTHEAST_ASIA", "West Java"),
    CityPreset("Medan", "মেদান", "Indonesia", "ইন্দোনেশিয়া", 3.5952, 98.6722, 7.0, CalculationMethod.KEMENAG, JuristicMethod.SHAFI, "SOUTHEAST_ASIA", "North Sumatra"),
    CityPreset("Semarang", "সেমারাং", "Indonesia", "ইন্দোনেশিয়া", -6.9667, 110.4167, 7.0, CalculationMethod.KEMENAG, JuristicMethod.SHAFI, "SOUTHEAST_ASIA", "Central Java"),
    CityPreset("Makassar", "মাকাসসার", "Indonesia", "ইন্দোনেশিয়া", -5.1477, 119.4327, 8.0, CalculationMethod.KEMENAG, JuristicMethod.SHAFI, "SOUTHEAST_ASIA", "South Sulawesi"),
    CityPreset("Banda Aceh", "বান্দা আচেহ", "Indonesia", "ইন্দোনেশিয়া", 5.5483, 95.3238, 7.0, CalculationMethod.KEMENAG, JuristicMethod.SHAFI, "SOUTHEAST_ASIA", "Aceh"),
    CityPreset("Bandar Seri Begawan", "বন্দর সেরি বেগাওয়ান", "Brunei", "ব্রুনাই", 4.9031, 114.9398, 8.0, CalculationMethod.JAKIM, JuristicMethod.SHAFI, "SOUTHEAST_ASIA", "Brunei-Muara"),
    CityPreset("Bangkok", "ব্যাংকক", "Thailand", "থাইল্যান্ড", 13.7563, 100.5018, 7.0, CalculationMethod.MWL, JuristicMethod.SHAFI, "SOUTHEAST_ASIA", "Central"),
    CityPreset("Phuket", "ফুকেট", "Thailand", "থাইল্যান্ড", 7.8804, 98.3923, 7.0, CalculationMethod.MWL, JuristicMethod.SHAFI, "SOUTHEAST_ASIA", "Southern"),
    CityPreset("Manila", "ম্যানিলা", "Philippines", "ফিলিপাইন", 14.5995, 120.9842, 8.0, CalculationMethod.MWL, JuristicMethod.SHAFI, "SOUTHEAST_ASIA", "NCR"),
    CityPreset("Hanoi", "হ্যানয়", "Vietnam", "ভিয়েতনাম", 21.0285, 105.8542, 7.0, CalculationMethod.MWL, JuristicMethod.SHAFI, "SOUTHEAST_ASIA", "Red River Delta"),
    CityPreset("Ho Chi Minh City", "হো চি মিন সিটি", "Vietnam", "ভিয়েতনাম", 10.8231, 106.6297, 7.0, CalculationMethod.MWL, JuristicMethod.SHAFI, "SOUTHEAST_ASIA", "Southeast"),
    CityPreset("Phnom Penh", "নম পেন", "Cambodia", "কম্বোডিয়া", 11.5564, 104.9282, 7.0, CalculationMethod.MWL, JuristicMethod.SHAFI, "SOUTHEAST_ASIA", "Phnom Penh"),
    CityPreset("Yangon", "ইয়াঙ্গুন", "Myanmar", "মিয়ানমার", 16.8661, 96.1951, 6.5, CalculationMethod.KARACHI, JuristicMethod.HANAFI, "SOUTHEAST_ASIA", "Yangon"),

    // ==================== EAST ASIA ====================
    CityPreset("Tokyo", "টোকিও", "Japan", "জাপান", 35.6762, 139.6503, 9.0, CalculationMethod.MWL, JuristicMethod.HANAFI, "EAST_ASIA", "Kanto"),
    CityPreset("Osaka", "ওসাকা", "Japan", "জাপান", 34.6937, 135.5023, 9.0, CalculationMethod.MWL, JuristicMethod.HANAFI, "EAST_ASIA", "Kansai"),
    CityPreset("Kyoto", "কিয়োটো", "Japan", "জাপান", 35.0116, 135.7681, 9.0, CalculationMethod.MWL, JuristicMethod.HANAFI, "EAST_ASIA", "Kansai"),
    CityPreset("Seoul", "সিউল", "South Korea", "দক্ষিণ কোরিয়া", 37.5665, 126.9780, 9.0, CalculationMethod.MWL, JuristicMethod.HANAFI, "EAST_ASIA", "Sudogwon"),
    CityPreset("Busan", "বুসান", "South Korea", "দক্ষিণ কোরিয়া", 35.1796, 129.0756, 9.0, CalculationMethod.MWL, JuristicMethod.HANAFI, "EAST_ASIA", "Yeongnam"),
    CityPreset("Beijing", "বেইজিং", "China", "চীন", 39.9042, 116.4074, 8.0, CalculationMethod.MWL, JuristicMethod.HANAFI, "EAST_ASIA", "Beijing"),
    CityPreset("Shanghai", "সাংহাই", "China", "চীন", 31.2304, 121.4737, 8.0, CalculationMethod.MWL, JuristicMethod.HANAFI, "EAST_ASIA", "Shanghai"),
    CityPreset("Guangzhou", "গুয়াংজু", "China", "চীন", 23.1291, 113.2644, 8.0, CalculationMethod.MWL, JuristicMethod.HANAFI, "EAST_ASIA", "Guangdong"),
    CityPreset("Shenzhen", "শেনঝেন", "China", "চীন", 22.5431, 114.0579, 8.0, CalculationMethod.MWL, JuristicMethod.HANAFI, "EAST_ASIA", "Guangdong"),
    CityPreset("Urumqi", "উরুমকি (শিনজিয়াং)", "China", "চীন", 43.8256, 87.6168, 8.0, CalculationMethod.MWL, JuristicMethod.HANAFI, "EAST_ASIA", "Xinjiang"),
    CityPreset("Hong Kong", "হংকং", "Hong Kong", "হংকং", 22.3193, 114.1694, 8.0, CalculationMethod.MWL, JuristicMethod.HANAFI, "EAST_ASIA", "Hong Kong"),
    CityPreset("Taipei", "তাইপে", "Taiwan", "তাইওয়ান", 25.0330, 121.5654, 8.0, CalculationMethod.MWL, JuristicMethod.HANAFI, "EAST_ASIA", "Taipei"),

    // ==================== CENTRAL & WEST ASIA ====================
    CityPreset("Istanbul", "ইস্তাম্বুল", "Turkey", "তুরস্ক", 41.0082, 28.9784, 3.0, CalculationMethod.MWL, JuristicMethod.HANAFI, "CENTRAL_WEST_ASIA", "Marmara"),
    CityPreset("Ankara", "আঙ্কারা", "Turkey", "তুরস্ক", 39.9334, 32.8597, 3.0, CalculationMethod.MWL, JuristicMethod.HANAFI, "CENTRAL_WEST_ASIA", "Central Anatolia"),
    CityPreset("Izmir", "ইজমির", "Turkey", "তুরস্ক", 38.4237, 27.1428, 3.0, CalculationMethod.MWL, JuristicMethod.HANAFI, "CENTRAL_WEST_ASIA", "Aegean"),
    CityPreset("Konya", "কোনিয়া", "Turkey", "তুরস্ক", 37.8714, 32.4846, 3.0, CalculationMethod.MWL, JuristicMethod.HANAFI, "CENTRAL_WEST_ASIA", "Central Anatolia"),
    CityPreset("Cairo", "কায়রো", "Egypt", "মিশর", 30.0444, 31.2357, 2.0, CalculationMethod.EGYPT, JuristicMethod.SHAFI, "CENTRAL_WEST_ASIA", "Cairo"),
    CityPreset("Alexandria", "আলেকজান্দ্রিয়া", "Egypt", "মিশর", 31.2001, 29.9187, 2.0, CalculationMethod.EGYPT, JuristicMethod.SHAFI, "CENTRAL_WEST_ASIA", "Alexandria"),
    CityPreset("Amman", "আম্মান", "Jordan", "জর্ডান", 31.9454, 35.9284, 3.0, CalculationMethod.MWL, JuristicMethod.SHAFI, "CENTRAL_WEST_ASIA", "Amman"),
    CityPreset("Beirut", "বৈরুত", "Lebanon", "লেবানন", 33.8938, 35.5018, 3.0, CalculationMethod.MWL, JuristicMethod.SHAFI, "CENTRAL_WEST_ASIA", "Beirut"),
    CityPreset("Baghdad", "বাগদাদ", "Iraq", "ইরাক", 33.3152, 44.3661, 3.0, CalculationMethod.MWL, JuristicMethod.HANAFI, "CENTRAL_WEST_ASIA", "Baghdad"),
    CityPreset("Basra", "বসরা", "Iraq", "ইরাক", 30.5085, 47.7804, 3.0, CalculationMethod.MWL, JuristicMethod.HANAFI, "CENTRAL_WEST_ASIA", "Basra"),
    CityPreset("Erbil", "ইরবিল", "Iraq", "ইরাক", 36.1911, 44.0092, 3.0, CalculationMethod.MWL, JuristicMethod.HANAFI, "CENTRAL_WEST_ASIA", "Kurdistan"),
    CityPreset("Damascus", "দামেস্ক", "Syria", "সিরিয়া", 33.5138, 36.2765, 3.0, CalculationMethod.MWL, JuristicMethod.SHAFI, "CENTRAL_WEST_ASIA", "Damascus"),
    CityPreset("Jerusalem (Al-Quds)", "আল-কুদস (জেরুসালেম)", "Palestine", "ফিলিস্তিন", 31.7683, 35.2137, 3.0, CalculationMethod.MWL, JuristicMethod.SHAFI, "CENTRAL_WEST_ASIA", "Al-Quds"),
    CityPreset("Gaza City", "গাজা", "Palestine", "ফিলিস্তিন", 31.5017, 34.4668, 3.0, CalculationMethod.EGYPT, JuristicMethod.SHAFI, "CENTRAL_WEST_ASIA", "Gaza Strip"),
    CityPreset("Sana'a", "সানা", "Yemen", "ইয়েমেন", 15.3694, 44.1910, 3.0, CalculationMethod.MWL, JuristicMethod.SHAFI, "CENTRAL_WEST_ASIA", "Sana'a"),
    CityPreset("Tehran", "তেহরান", "Iran", "ইরান", 35.6892, 51.3890, 3.5, CalculationMethod.MWL, JuristicMethod.HANAFI, "CENTRAL_WEST_ASIA", "Tehran"),
    CityPreset("Mashhad", "মাশহাদ", "Iran", "ইরান", 36.2972, 59.6067, 3.5, CalculationMethod.MWL, JuristicMethod.HANAFI, "CENTRAL_WEST_ASIA", "Razavi Khorasan"),
    CityPreset("Tashkent", "তাসখন্দ", "Uzbekistan", "উজবেকিস্তান", 41.2995, 69.2401, 5.0, CalculationMethod.KARACHI, JuristicMethod.HANAFI, "CENTRAL_WEST_ASIA", "Tashkent"),
    CityPreset("Samarkand", "সমরখন্দ", "Uzbekistan", "উজবেকিস্তান", 39.6270, 66.9750, 5.0, CalculationMethod.KARACHI, JuristicMethod.HANAFI, "CENTRAL_WEST_ASIA", "Samarkand"),
    CityPreset("Bukhara", "বুখারা", "Uzbekistan", "উজবেকিস্তান", 39.7681, 64.4556, 5.0, CalculationMethod.KARACHI, JuristicMethod.HANAFI, "CENTRAL_WEST_ASIA", "Bukhara"),
    CityPreset("Almaty", "আলমাতি", "Kazakhstan", "কাজাখস্তান", 43.2220, 76.8512, 5.0, CalculationMethod.MWL, JuristicMethod.HANAFI, "CENTRAL_WEST_ASIA", "Almaty"),
    CityPreset("Astana", "আস্তানা", "Kazakhstan", "কাজাখস্তান", 51.1694, 71.4491, 5.0, CalculationMethod.MWL, JuristicMethod.HANAFI, "CENTRAL_WEST_ASIA", "Astana"),
    CityPreset("Bishkek", "বিশকেক", "Kyrgyzstan", "কিরগিজস্তান", 42.8746, 74.5698, 6.0, CalculationMethod.MWL, JuristicMethod.HANAFI, "CENTRAL_WEST_ASIA", "Chuy"),
    CityPreset("Dushanbe", "দুশানবে", "Tajikistan", "তাজিকিস্তান", 38.5598, 68.7870, 5.0, CalculationMethod.MWL, JuristicMethod.HANAFI, "CENTRAL_WEST_ASIA", "Dushanbe"),
    CityPreset("Ashgabat", "আশগাবাত", "Turkmenistan", "তুর্কমেনিস্তান", 37.9601, 58.3261, 5.0, CalculationMethod.MWL, JuristicMethod.HANAFI, "CENTRAL_WEST_ASIA", "Ahal"),
    CityPreset("Baku", "বাকু", "Azerbaijan", "আজারবাইজান", 40.4093, 49.8671, 4.0, CalculationMethod.MWL, JuristicMethod.HANAFI, "CENTRAL_WEST_ASIA", "Absheron"),

    // ==================== WESTERN / GLOBAL MAJOR DIASPORA ====================
    CityPreset("London", "লন্ডন", "United Kingdom", "যুক্তরাজ্য", 51.5074, -0.1278, 0.0, CalculationMethod.MWL, JuristicMethod.HANAFI, "WEST", "England"),
    CityPreset("New York", "নিউ ইয়র্ক", "United States", "যুক্তরাষ্ট্র", 40.7128, -74.0060, -5.0, CalculationMethod.ISNA, JuristicMethod.HANAFI, "WEST", "New York"),
    CityPreset("Toronto", "টরন্টো", "Canada", "কানাডা", 43.6532, -79.3832, -5.0, CalculationMethod.ISNA, JuristicMethod.HANAFI, "WEST", "Ontario"),
    CityPreset("Sydney", "সিডনি", "Australia", "অস্ট্রেলিয়া", -33.8688, 151.2093, 10.0, CalculationMethod.MWL, JuristicMethod.HANAFI, "WEST", "NSW")
  )
}

data class ResolvedLocationInfo(
  val formattedName: String,
  val locality: String? = null,
  val countryCode: String? = null,
  val countryName: String? = null,
  val autoDetectedMethod: CalculationMethod = CalculationMethod.KARACHI,
  val autoDetectedJuristic: JuristicMethod = JuristicMethod.HANAFI
)

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

  fun getFallbackPreset(): CityPreset {
    val tzId = java.util.TimeZone.getDefault().id
    return when {
      tzId.contains("Dhaka", ignoreCase = true) || tzId.contains("Bangladesh", ignoreCase = true) ->
        LocationPresets.defaultCities.first { it.nameEn == "Dhaka" }
      tzId.contains("Kolkata", ignoreCase = true) || tzId.contains("Calcutta", ignoreCase = true) || tzId.contains("India", ignoreCase = true) ->
        LocationPresets.defaultCities.first { it.nameEn == "Kolkata" }
      tzId.contains("Karachi", ignoreCase = true) || tzId.contains("Pakistan", ignoreCase = true) ->
        LocationPresets.defaultCities.first { it.nameEn == "Karachi" }
      tzId.contains("Riyadh", ignoreCase = true) || tzId.contains("Saudi", ignoreCase = true) ->
        LocationPresets.defaultCities.first { it.nameEn.contains("Riyadh") }
      tzId.contains("Dubai", ignoreCase = true) || tzId.contains("Emirates", ignoreCase = true) ->
        LocationPresets.defaultCities.first { it.nameEn == "Dubai" }
      tzId.contains("Kuala_Lumpur", ignoreCase = true) || tzId.contains("Malaysia", ignoreCase = true) ->
        LocationPresets.defaultCities.first { it.nameEn == "Kuala Lumpur" }
      tzId.contains("Singapore", ignoreCase = true) ->
        LocationPresets.defaultCities.first { it.nameEn.contains("Singapore") }
      tzId.contains("Jakarta", ignoreCase = true) || tzId.contains("Indonesia", ignoreCase = true) ->
        LocationPresets.defaultCities.first { it.nameEn == "Jakarta" }
      tzId.contains("London", ignoreCase = true) || tzId.contains("Europe/London", ignoreCase = true) ->
        LocationPresets.defaultCities.first { it.nameEn == "London" }
      tzId.contains("New_York", ignoreCase = true) ->
        LocationPresets.defaultCities.first { it.nameEn == "New York" }
      else -> {
        val systemHours = getSystemTimezoneHours()
        LocationPresets.defaultCities.minByOrNull { kotlin.math.abs(it.timezoneHours - systemHours) }
          ?: LocationPresets.defaultCities.first()
      }
    }
  }

  @SuppressLint("MissingPermission")
  fun fetchCurrentLocation(
    onSuccess: (
      latitude: Double,
      longitude: Double,
      locationName: String,
      timezoneHours: Double,
      isGps: Boolean,
      autoMethod: CalculationMethod,
      autoJuristic: JuristicMethod
    ) -> Unit,
    onFailure: (String) -> Unit
  ) {
    if (!hasLocationPermission()) {
      val fallback = getFallbackPreset()
      val (fMethod, fJuristic) = PrayerTimesCalculator.detectCalculationMethod(
        fallback.latitude,
        fallback.longitude,
        countryName = fallback.countryEn
      )
      onSuccess(
        fallback.latitude,
        fallback.longitude,
        "${fallback.nameBn}, ${fallback.countryBn}",
        fallback.timezoneHours,
        false,
        fMethod,
        fJuristic
      )
      return
    }

    try {
      val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
      fusedLocationClient.lastLocation.addOnSuccessListener { lastLoc: Location? ->
        if (lastLoc != null) {
          CoroutineScope(Dispatchers.IO).launch {
            val info = resolveLocationInfo(lastLoc.latitude, lastLoc.longitude)
            val tz = PrayerTimesCalculator.resolveTimezone(lastLoc.longitude, getSystemTimezoneHours())
            withContext(Dispatchers.Main) {
              onSuccess(
                lastLoc.latitude,
                lastLoc.longitude,
                info.formattedName,
                tz,
                true,
                info.autoDetectedMethod,
                info.autoDetectedJuristic
              )
            }
          }
        } else {
          fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
            .addOnSuccessListener { freshLoc: Location? ->
              if (freshLoc != null) {
                CoroutineScope(Dispatchers.IO).launch {
                  val info = resolveLocationInfo(freshLoc.latitude, freshLoc.longitude)
                  val tz = PrayerTimesCalculator.resolveTimezone(freshLoc.longitude, getSystemTimezoneHours())
                  withContext(Dispatchers.Main) {
                    onSuccess(
                      freshLoc.latitude,
                      freshLoc.longitude,
                      info.formattedName,
                      tz,
                      true,
                      info.autoDetectedMethod,
                      info.autoDetectedJuristic
                    )
                  }
                }
              } else {
                fallbackLocationManager(
                  onSuccess = { lat, lng, name, tz, isGps ->
                    CoroutineScope(Dispatchers.IO).launch {
                      val info = resolveLocationInfo(lat, lng)
                      withContext(Dispatchers.Main) {
                        onSuccess(lat, lng, name, tz, isGps, info.autoDetectedMethod, info.autoDetectedJuristic)
                      }
                    }
                  },
                  onFailure = onFailure
                )
              }
            }
            .addOnFailureListener {
              fallbackLocationManager(
                onSuccess = { lat, lng, name, tz, isGps ->
                  CoroutineScope(Dispatchers.IO).launch {
                    val info = resolveLocationInfo(lat, lng)
                    withContext(Dispatchers.Main) {
                      onSuccess(lat, lng, name, tz, isGps, info.autoDetectedMethod, info.autoDetectedJuristic)
                    }
                  }
                },
                onFailure = onFailure
              )
            }
        }
      }.addOnFailureListener {
        fallbackLocationManager(
          onSuccess = { lat, lng, name, tz, isGps ->
            CoroutineScope(Dispatchers.IO).launch {
              val info = resolveLocationInfo(lat, lng)
              withContext(Dispatchers.Main) {
                onSuccess(lat, lng, name, tz, isGps, info.autoDetectedMethod, info.autoDetectedJuristic)
              }
            }
          },
          onFailure = onFailure
        )
      }
    } catch (_: Exception) {
      fallbackLocationManager(
        onSuccess = { lat, lng, name, tz, isGps ->
          CoroutineScope(Dispatchers.IO).launch {
            val info = resolveLocationInfo(lat, lng)
            withContext(Dispatchers.Main) {
              onSuccess(lat, lng, name, tz, isGps, info.autoDetectedMethod, info.autoDetectedJuristic)
            }
          }
        },
        onFailure = onFailure
      )
    }
  }

  @SuppressLint("MissingPermission")
  fun fetchCurrentLocation(
    onSuccess: (latitude: Double, longitude: Double, locationName: String, timezoneHours: Double, isGps: Boolean) -> Unit,
    onFailure: (String) -> Unit
  ) {
    fetchCurrentLocation(
      onSuccess = { lat, lng, name, tz, isGps, _, _ ->
        onSuccess(lat, lng, name, tz, isGps)
      },
      onFailure = onFailure
    )
  }

  @SuppressLint("MissingPermission")
  fun fetchCurrentLocation(
    onSuccess: (latitude: Double, longitude: Double, locationName: String, timezoneHours: Double) -> Unit,
    onFailure: (String) -> Unit
  ) {
    fetchCurrentLocation(
      onSuccess = { lat, lng, name, tz, _ -> onSuccess(lat, lng, name, tz) },
      onFailure = onFailure
    )
  }

  fun getSystemTimezoneHours(): Double {
    val tz = java.util.TimeZone.getDefault()
    val now = java.util.Date()
    return tz.rawOffset / 3600000.0 + if (tz.inDaylightTime(now)) 1.0 else 0.0
  }

  @SuppressLint("MissingPermission")
  private fun fallbackLocationManager(
    onSuccess: (Double, Double, String, Double, Boolean) -> Unit,
    onFailure: (String) -> Unit
  ) {
    try {
      val locManager = context.getSystemService(Context.LOCATION_SERVICE) as? LocationManager
      if (locManager == null) {
        val fallback = getFallbackPreset()
        onSuccess(fallback.latitude, fallback.longitude, "${fallback.nameBn}, ${fallback.countryBn}", fallback.timezoneHours, false)
        return
      }

      val gpsLoc = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
      val netLoc = locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
      val best = gpsLoc ?: netLoc

      if (best != null) {
        val name = resolveAddress(best.latitude, best.longitude)
        val tz = PrayerTimesCalculator.resolveTimezone(best.longitude, getSystemTimezoneHours())
        onSuccess(best.latitude, best.longitude, name, tz, true)
      } else {
        val listener = object : LocationListener {
          override fun onLocationChanged(loc: Location) {
            locManager.removeUpdates(this)
            val name = resolveAddress(loc.latitude, loc.longitude)
            val tz = PrayerTimesCalculator.resolveTimezone(loc.longitude, getSystemTimezoneHours())
            onSuccess(loc.latitude, loc.longitude, name, tz, true)
          }
          @Deprecated("Deprecated in Java")
          override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
          override fun onProviderEnabled(provider: String) {}
          override fun onProviderDisabled(provider: String) {}
        }
        locManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, listener, null)
      }
    } catch (e: Exception) {
      val fallback = getFallbackPreset()
      onSuccess(fallback.latitude, fallback.longitude, "${fallback.nameBn}, ${fallback.countryBn}", fallback.timezoneHours, false)
    }
  }

  fun resolveLocationInfo(lat: Double, lng: Double): ResolvedLocationInfo {
    var locality: String? = null
    var countryCode: String? = null
    var countryName: String? = null
    var formatted = ""

    try {
      val geocoder = Geocoder(context, Locale.getDefault())
      val addresses = geocoder.getFromLocation(lat, lng, 1)
      if (!addresses.isNullOrEmpty()) {
        val addr = addresses[0]
        locality = addr.locality ?: addr.subLocality ?: addr.subAdminArea ?: addr.adminArea ?: addr.featureName
        countryName = addr.countryName
        countryCode = addr.countryCode
        formatted = if (!locality.isNullOrBlank() && !countryName.isNullOrBlank()) {
          "$locality, $countryName"
        } else if (!locality.isNullOrBlank()) {
          locality
        } else {
          countryName ?: String.format(Locale.US, "%.4f° N, %.4f° E", lat, lng)
        }
      }
    } catch (_: Exception) {}

    if (formatted.isBlank()) {
      val nearest = LocationPresets.defaultCities.minByOrNull {
        val dLat = it.latitude - lat
        val dLng = it.longitude - lng
        dLat * dLat + dLng * dLng
      }
      if (nearest != null) {
        val distSq = (nearest.latitude - lat).let { it * it } + (nearest.longitude - lng).let { it * it }
        if (distSq < 1.2) {
          formatted = "${nearest.nameBn}, ${nearest.countryBn}"
          countryName = nearest.countryEn
          locality = nearest.nameEn
        }
      }
    }

    if (formatted.isBlank()) {
      formatted = String.format(Locale.US, "%.4f° N, %.4f° E", lat, lng)
    }

    val (method, juristic) = PrayerTimesCalculator.detectCalculationMethod(
      latitude = lat,
      longitude = lng,
      countryCode = countryCode,
      countryName = countryName
    )

    return ResolvedLocationInfo(
      formattedName = formatted,
      locality = locality,
      countryCode = countryCode,
      countryName = countryName,
      autoDetectedMethod = method,
      autoDetectedJuristic = juristic
    )
  }

  fun resolveAddress(lat: Double, lng: Double): String {
    return resolveLocationInfo(lat, lng).formattedName
  }

  fun searchCustomLocation(query: String, onResult: (CityPreset?) -> Unit) {
    kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.IO).launch {
      try {
        val geocoder = Geocoder(context, Locale.getDefault())
        val results = geocoder.getFromLocationName(query, 1)
        if (!results.isNullOrEmpty()) {
          val addr = results[0]
          val lat = addr.latitude
          val lng = addr.longitude
          val cityName = addr.locality ?: addr.subLocality ?: addr.subAdminArea ?: addr.adminArea ?: query
          val country = addr.countryName ?: "Asia"
          val tzHours = PrayerTimesCalculator.resolveTimezone(lng, getSystemTimezoneHours())
          val (method, juristic) = PrayerTimesCalculator.detectCalculationMethod(
            latitude = lat,
            longitude = lng,
            countryCode = addr.countryCode,
            countryName = country
          )
          val preset = CityPreset(
            nameEn = cityName,
            nameBn = cityName,
            countryEn = country,
            countryBn = country,
            latitude = lat,
            longitude = lng,
            timezoneHours = tzHours,
            defaultMethod = method,
            defaultJuristic = juristic,
            regionCategory = "ASIA",
            stateOrDivision = addr.adminArea ?: ""
          )
          kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.Main) {
            onResult(preset)
          }
          return@launch
        }
      } catch (_: Exception) {}
      kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.Main) {
        onResult(null)
      }
    }
  }
}
