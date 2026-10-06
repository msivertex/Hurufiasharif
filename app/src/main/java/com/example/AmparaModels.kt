package com.example

import androidx.compose.ui.graphics.Color

/**
 * Revelational classification of Surah
 */
enum class RevelationType(val titleBn: String, val titleEn: String, val titleAr: String) {
  MAKKI(titleBn = "মাক্কী", titleEn = "Makki", titleAr = "مكية"),
  MADANI(titleBn = "মাদানী", titleEn = "Madani", titleAr = "مدنية")
}

/**
 * Individual Word token inside an Ayah with Tajweed color classification and individual vocabulary meaning
 */
data class QuranWord(
  val id: String,
  val position: Int,
  val textArabic: String,
  val transliterationBn: String,
  val transliterationEn: String,
  val meaningBn: String,
  val meaningEn: String,
  val tajweedType: TajweedType = TajweedType.NONE,
  val segments: List<TajweedWordSegment> = emptyList()
)

/**
 * Single Ayah / Verse entity
 */
data class QuranAyah(
  val ayahNumber: Int,
  val textArabic: String,
  val transliterationBn: String,
  val transliterationEn: String,
  val translationBn: String,
  val translationEn: String,
  val words: List<QuranWord>,
  val audioTimingMs: Long = 3200L
)

/**
 * Surah metadata and full Ayah collection
 */
data class Surah(
  val number: Int,
  val nameArabic: String,
  val nameBengali: String,
  val nameEnglish: String,
  val meaningBengali: String,
  val meaningEnglish: String,
  val totalVerses: Int,
  val revelationType: RevelationType,
  val bismillahPrecedes: Boolean = true,
  val ayahs: List<QuranAyah>
) {
  val revelationBadgeColor: Color
    get() = if (revelationType == RevelationType.MAKKI) Color(0xFFD97706) else Color(0xFF059669)
}
