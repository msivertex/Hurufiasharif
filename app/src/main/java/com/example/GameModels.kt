package com.example

import androidx.compose.ui.graphics.Color

enum class GameType {
  SHAPE_MASTER_PATH,
  DOT_MASTER,
  LETTER_LINK,
  FORM_FUSER,
  MIX_AND_MATCH
}

data class GameMode(
  val type: GameType,
  val titleEn: String,
  val titleBn: String,
  val titleAr: String,
  val subtitleEn: String,
  val subtitleBn: String,
  val subtitleAr: String,
  val badgeEn: String,
  val badgeBn: String,
  val badgeAr: String,
  val primaryColor: Color,
  val secondaryColor: Color,
  val accentColor: Color,
  val iconEmoji: String,
  val levelRequired: Int = 1,
  val starsCount: Int = 3
) {
  fun getTitle(lang: String): String = when (lang) {
    "EN" -> titleEn
    "AR" -> titleAr
    else -> titleBn
  }

  fun getSubtitle(lang: String): String = when (lang) {
    "EN" -> subtitleEn
    "AR" -> subtitleAr
    else -> subtitleBn
  }

  fun getBadge(lang: String): String = when (lang) {
    "EN" -> badgeEn
    "AR" -> badgeAr
    else -> badgeBn
  }
}

object GameRepository {
  val gameModes: List<GameMode> = listOf(
    GameMode(
      type = GameType.SHAPE_MASTER_PATH,
      titleEn = "Shape Master Path",
      titleBn = "শেপ মাস্টার পাথ",
      titleAr = "مسار إتقان الأشكال",
      subtitleEn = "Duolingo-style roadmap to master all Arabic letter forms step-by-step",
      subtitleBn = "রোডম্যাপ অনুযায়ী ধাপে ধাপে আরবি হরফের প্রতিটি রূপ আয়ত্ত করুন",
      subtitleAr = "طريق تعليمي متدرج لإتقان كافة أشكال الحروف العربية",
      badgeEn = "ROADMAP",
      badgeBn = "রোডম্যাপ",
      badgeAr = "المسار",
      primaryColor = Color(0xFF0A5C36),
      secondaryColor = Color(0xFF137A4B),
      accentColor = Color(0xFFF6E05E),
      iconEmoji = "🗺️",
      levelRequired = 1,
      starsCount = 3
    ),
    GameMode(
      type = GameType.DOT_MASTER,
      titleEn = "Dot Master",
      titleBn = "নুকতা মাস্টার (ডট পজিশনিং)",
      titleAr = "إتقان النقاط (مواضع النقط)",
      subtitleEn = "Identify and place dots (Nukta) above, below, or inside letters",
      subtitleBn = "হরফের উপরে, নিচে বা পেটে ১টি, ২টি বা ৩টি নুকতার সঠিক অবস্থান চিহ্নিত করুন",
      subtitleAr = "أتقن مواضع وعدد النقاط فوق وتحت وداخل الحروف المتشابهة",
      badgeEn = "DOTS / NUKTA",
      badgeBn = "নুকতা মাস্টার",
      badgeAr = "نقاط الحروف",
      primaryColor = Color(0xFFEA580C),
      secondaryColor = Color(0xFFC2410C),
      accentColor = Color(0xFFFED7AA),
      iconEmoji = "🎯",
      levelRequired = 1,
      starsCount = 3
    ),
    GameMode(
      type = GameType.LETTER_LINK,
      titleEn = "Letter Link",
      titleBn = "লেটার লিংক",
      titleAr = "رابط الحروف",
      subtitleEn = "Match-3 candy puzzle connecting identical letter sounds and gems",
      subtitleBn = "ক্যান্ডি ক্রাশ স্টাইল ম্যাচ-৩ গেম: একই হরফের রত্ন মিলিয়ে স্কোর বাড়ান",
      subtitleAr = "لعبة مطابقة الحروف الثلاثية الشيقة لجمع النقاط",
      badgeEn = "MATCH-3",
      badgeBn = "ম্যাচ-৩",
      badgeAr = "مطابقة",
      primaryColor = Color(0xFFB83280),
      secondaryColor = Color(0xFF702459),
      accentColor = Color(0xFFFED7E2),
      iconEmoji = "💎",
      levelRequired = 1,
      starsCount = 2
    ),
    GameMode(
      type = GameType.FORM_FUSER,
      titleEn = "Form Fuser",
      titleBn = "ফর্ম ফিউজার",
      titleAr = "صانع الأشكال",
      subtitleEn = "Puzzle joining: fuse Isolated, Initial, Medial & Final into words",
      subtitleBn = "পাজেল স্টাইল: বিচ্ছিন্ন, প্রারম্ভিক, মধ্য ও শেষ রূপ জোড়া দিয়ে শব্দ গঠন",
      subtitleAr = "لغز تركيب الأشكال ودمج أشكال الحروف لتكوين الكلمات",
      badgeEn = "PUZZLE",
      badgeBn = "পাজেল",
      badgeAr = "أحجية",
      primaryColor = Color(0xFF2B6CB0),
      secondaryColor = Color(0xFF1A365D),
      accentColor = Color(0xFF90CDF4),
      iconEmoji = "🧩",
      levelRequired = 1,
      starsCount = 1
    ),
    GameMode(
      type = GameType.MIX_AND_MATCH,
      titleEn = "Mix & Match",
      titleBn = "মিক্স অ্যান্ড ম্যাচ",
      titleAr = "مزج ومطابقة",
      subtitleEn = "Memory flip card challenge testing your recognition and speed",
      subtitleBn = "স্মৃতিশক্তি পরীক্ষা: কার্ড ফ্লিপ করে সঠিক হরফ ও উচ্চারণ জোড়া মেলান",
      subtitleAr = "تحدي بطاقات الذاكرة السريعة لمطابقة الحروف المتطابقة",
      badgeEn = "IQ FLIP",
      badgeBn = "ব্রেইন গেম",
      badgeAr = "ذاكرة",
      primaryColor = Color(0xFF6B46C1),
      secondaryColor = Color(0xFF44337A),
      accentColor = Color(0xFFD6BCFA),
      iconEmoji = "🃏",
      levelRequired = 1,
      starsCount = 2
    )
  )
}
