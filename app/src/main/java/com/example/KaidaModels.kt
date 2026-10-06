package com.example

import androidx.compose.ui.graphics.Color

/**
 * Tajweed Rule category for color-coding Arabic text and interactive explanation
 */
enum class TajweedType(
  val titleBn: String,
  val titleEn: String,
  val ruleDescriptionBn: String,
  val ruleDescriptionEn: String,
  val color: Color,
  val hexColorCode: String
) {
  NONE(
    titleBn = "সাধারণ হরফ",
    titleEn = "Standard Letter",
    ruleDescriptionBn = "স্বাভাবিক গতি ও নিয়মে উচ্চারিত হরফ।",
    ruleDescriptionEn = "Normal pronunciation with regular duration.",
    color = Color(0xFF1E293B),
    hexColorCode = "#1E293B"
  ),
  MADD(
    titleBn = "মাদ্দ (টেনে পড়া)",
    titleEn = "Madd (Elongation)",
    ruleDescriptionBn = "১ থেকে ৩-৪ আলিফ পরিমাণ লম্বা বা টেনে পড়তে হবে। লাল রঙ দিয়ে চিহ্নিত।",
    ruleDescriptionEn = "Elongate pronunciation 1 to 4 counts. Highlighted in Red.",
    color = Color(0xFFEF4444), // Red
    hexColorCode = "#EF4444"
  ),
  GHUNNAH_IKHFA(
    titleBn = "ওয়াজিব গুন্নাহ ও ইখফা",
    titleEn = "Ghunnah & Ikhfa (Nasalization)",
    ruleDescriptionBn = "নাক দিয়ে গুণগুণ করে গুন্নাহ সহকারে উচ্চারণ করতে হয়। সবুজ রঙ দিয়ে চিহ্নিত।",
    ruleDescriptionEn = "Nasalized humming sound for 1 count. Highlighted in Green.",
    color = Color(0xFF10B981), // Green
    hexColorCode = "#10B981"
  ),
  QALQALAH(
    titleBn = "কলকলাহ (প্রতিধ্বনি)",
    titleEn = "Qalqalah (Echoing Bounce)",
    ruleDescriptionBn = "ق ط ب ج د হরফে সাকিন হলে ধাক্কা দিয়ে বা প্রতিধ্বনি করে পড়তে হয়। নীল রঙ দিয়ে চিহ্নিত।",
    ruleDescriptionEn = "Echoing or bouncing sound on Qaf, Taa, Baa, Jeem, Daal with Sukun. Blue.",
    color = Color(0xFF2563EB), // Blue
    hexColorCode = "#2563EB"
  ),
  IQLAB(
    titleBn = "ইক্বলাব (নূন সাকিন পরিবর্তন)",
    titleEn = "Iqlab (Nun Sakin to Meem)",
    ruleDescriptionBn = "নূন সাকিন বা তানবীনের পর 'বা' আসলে তা 'মীম' দ্বারা পরিবর্তিত হয়। কমলা রঙ দিয়ে চিহ্নিত।",
    ruleDescriptionEn = "Convert Nun Sakin / Tanween to Meem with Ghunnah. Highlighted in Orange.",
    color = Color(0xFFF97316), // Orange
    hexColorCode = "#F97316"
  ),
  SILENT(
    titleBn = "উচ্চারণহীন হরফ (সাইলেন্ট)",
    titleEn = "Silent Letters",
    ruleDescriptionBn = "লেখায় আসে কিন্তু উচ্চারিত হয় না (যেমন আল শামসিয়া)। ধূসর রঙ দিয়ে চিহ্নিত।",
    ruleDescriptionEn = "Written in script but not pronounced (e.g. solar lam). Gray color.",
    color = Color(0xFF94A3B8), // Gray
    hexColorCode = "#94A3B8"
  )
}

/**
 * A colored segment of an Arabic word for Tajweed visualization
 */
data class TajweedWordSegment(
  val text: String,
  val tajweedType: TajweedType = TajweedType.NONE
)

/**
 * An individual word card inside a Kaida Digital Book Page
 */
data class KaidaWordItem(
  val id: String,
  val arabicWord: String,
  val pronunciationBn: String,
  val pronunciationEn: String,
  val meaningBn: String = "",
  val meaningEn: String = "",
  val primaryTajweed: TajweedType = TajweedType.NONE,
  val tajweedNoteBn: String = "",
  val tajweedNoteEn: String = "",
  val segments: List<TajweedWordSegment> = emptyList(),
  val expectedPhoneme: String = ""
)

/**
 * A single page in the digital book containing 4 to 6 large clean Arabic words
 */
data class KaidaBookPage(
  val pageNumber: Int,
  val pageTitleBn: String,
  val pageTitleEn: String,
  val ruleFocusBn: String,
  val ruleFocusEn: String,
  val words: List<KaidaWordItem>
)

/**
 * Foundation Kaida Chapter / Module
 */
data class KaidaChapter(
  val id: Int,
  val chapterNumber: Int,
  val titleBn: String,
  val titleEn: String,
  val titleAr: String,
  val subtitleBn: String,
  val subtitleEn: String,
  val iconEmoji: String,
  val themeColor: Color,
  val primaryTajweedRule: TajweedType,
  val pages: List<KaidaBookPage>
) {
  val totalPages: Int get() = pages.size
  val totalWords: Int get() = pages.sumOf { it.words.size }
}
