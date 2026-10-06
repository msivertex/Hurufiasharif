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
      titleEn = "Letter Link (Match Grid)",
      titleBn = "লেটার লিংক (ম্যাচ গ্রিড)",
      titleAr = "رابط الحروف (شبكة المطابقة)",
      subtitleEn = "Interactive grid matching identical, shape-variant, and phonetic letter pairs",
      subtitleBn = "ইন্টারেক্টিভ গ্রিডে একই হরফ, সংযুক্ত রূপভেদ ও সদৃশ মাখরাজ জোড়া মিলিয়ে লিংক করুন",
      subtitleAr = "شبكة تفاعلية لمطابقة الحروف المتماثلة والمتشابهة والأشكال المتصلة",
      badgeEn = "MATCH GRID",
      badgeBn = "ম্যাচ গ্রিড",
      badgeAr = "شبكة المطابقة",
      primaryColor = Color(0xFFB83280),
      secondaryColor = Color(0xFF702459),
      accentColor = Color(0xFFFED7E2),
      iconEmoji = "💎",
      levelRequired = 1,
      starsCount = 3
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

/**
 * 5-Step Progressive Arabic Cultural Praise Model (Scaled across 20 questions in 4-question intervals):
 * - Q 1..4: Marhaba (মারহাবা)
 * - Q 5..8: BarakAllah Fik (বারাকাল্লাহু ফিক)
 * - Q 9..12: MashaAllah (মাশাল্লাহ)
 * - Q 13..16: JazakAllah Khair (জাযাকাল্লাহু খাইরান)
 * - Q 17..20: Muntazun Jiddan (মুন্তাজুন জিদ্দান)
 */
data class ArabicCulturalPraise(
  val stepIndex: Int,
  val questionRange: IntRange,
  val arabicText: String,
  val bengaliText: String,
  val englishText: String,
  val meaningBn: String,
  val meaningEn: String,
  val iconEmoji: String
)

object ArabicPraiseEngine {
  val praises: List<ArabicCulturalPraise> = listOf(
    ArabicCulturalPraise(
      stepIndex = 1,
      questionRange = 1..4,
      arabicText = "مَرْحَبًا",
      bengaliText = "মারহাবা",
      englishText = "Marhaba",
      meaningBn = "অভিনন্দন ও শুভ সূচনা!",
      meaningEn = "Welcome & Great start!",
      iconEmoji = "🌱"
    ),
    ArabicCulturalPraise(
      stepIndex = 2,
      questionRange = 5..8,
      arabicText = "بَارَكَ اللَّهُ فِيكَ",
      bengaliText = "বারাকাল্লাহু ফিক",
      englishText = "BarakAllah Fik",
      meaningBn = "আল্লাহ আপনার মঙ্গল করুন!",
      meaningEn = "May Allah bless you!",
      iconEmoji = "✨"
    ),
    ArabicCulturalPraise(
      stepIndex = 3,
      questionRange = 9..12,
      arabicText = "مَا شَاءَ اللَّهُ",
      bengaliText = "মাশাল্লাহ",
      englishText = "MashaAllah",
      meaningBn = "আল্লাহ যা চেয়েছেন (চমৎকার)! ",
      meaningEn = "As Allah willed (Splendid)!",
      iconEmoji = "🌙"
    ),
    ArabicCulturalPraise(
      stepIndex = 4,
      questionRange = 13..16,
      arabicText = "جَزَاكَ اللَّهُ خَيْرًا",
      bengaliText = "জাযাকাল্লাহু খাইরান",
      englishText = "JazakAllah Khair",
      meaningBn = "আল্লাহ আপনাকে উত্তম প্রতিদান দিন!",
      meaningEn = "May Allah grant you best reward!",
      iconEmoji = "💎"
    ),
    ArabicCulturalPraise(
      stepIndex = 5,
      questionRange = 17..20,
      arabicText = "مُمْتَازٌ جِدًّا",
      bengaliText = "মুন্তাজুন জিদ্দান",
      englishText = "Muntazun Jiddan",
      meaningBn = "অসাধারণ ও নিখুঁত শ্রেষ্ঠত্ব!",
      meaningEn = "Outstanding & Supreme Excellence!",
      iconEmoji = "👑"
    )
  )

  fun getPraiseForQuestion(questionNumber: Int): ArabicCulturalPraise {
    val q = questionNumber.coerceIn(1, 20)
    return praises.firstOrNull { q in it.questionRange } ?: praises.last()
  }
}

/**
 * Level Completion Arabic Cultural Badges & Titles awarded based on score out of 20:
 * - 100% (20/20): 'হাফিজুল হুরুফ' (حَافِظُ الحُرُوف) - Master of Letters
 * - 90-99% (18-19/20): 'আল-মুতাকান' (المُتْقِن) - The Proficient
 * - 75-89% (15-17/20): 'আল-মুজতাহিদ' (المُجْتَهِد) - The Diligent
 * - Below 75% (<15/20): 'আল-মুতাআল্লিম' (المُتَعَلِّم) - The Learner
 */
enum class ArabicCulturalTitle(
  val titleBn: String,
  val titleAr: String,
  val titleEn: String,
  val subtitleBn: String,
  val subtitleEn: String,
  val subtitleAr: String,
  val primaryColor: Color,
  val secondaryColor: Color,
  val emoji: String,
  val starThreshold: Int
) {
  HAFIZ_UL_HURUF(
    titleBn = "হাফিজুল হুরুফ",
    titleAr = "حَافِظُ الحُرُوف",
    titleEn = "Hafiz-ul-Huruf (Master of Letters)",
    subtitleBn = "শতভাগ নিখুঁত ফলাফল! আপনি আরবি হরফের একজন প্রকৃত ওস্তাদ।",
    subtitleEn = "100% Perfect Score! You are a true Master of Arabic Letters.",
    subtitleAr = "نتيجة مثالية كاملة! أنت متقن وحافظ بارع لحروف القرآن الكريم.",
    primaryColor = Color(0xFF10B981),
    secondaryColor = Color(0xFF047857),
    emoji = "👑",
    starThreshold = 3
  ),
  AL_MUTQIN(
    titleBn = "আল-মুতাকান",
    titleAr = "المُتْقِن",
    titleEn = "Al-Mutqin (The Proficient)",
    subtitleBn = "অসাধারণ দক্ষতা! সর্বোচ্চ নির্ভুলতায় চমৎকার ফলাফল করেছেন।",
    subtitleEn = "Outstanding proficiency! High accuracy and sharp recognition.",
    subtitleAr = "إتقان متميز ودقة عالية في تمييز الحروف ومخارجها.",
    primaryColor = Color(0xFF0284C7),
    secondaryColor = Color(0xFF0369A1),
    emoji = "🌟",
    starThreshold = 3
  ),
  AL_MUJTAHID(
    titleBn = "আল-মুজতাহিদ",
    titleAr = "المُجْتَهِد",
    titleEn = "Al-Mujtahid (The Diligent)",
    subtitleBn = "প্রশংসনীয় অধ্যবসায়! নিয়মিত অনুশীলনে আপনি দ্রুত এগিয়ে যাচ্ছেন।",
    subtitleEn = "Commendable diligence! Advancing rapidly with great effort.",
    subtitleAr = "اجتهاد مبارك وسعي حثيث لتعلم لغة القرآن المجيد.",
    primaryColor = Color(0xFFF59E0B),
    secondaryColor = Color(0xFFD97706),
    emoji = "⚡",
    starThreshold = 2
  ),
  AL_MUTAALLIM(
    titleBn = "আল-মুতাআল্লিম",
    titleAr = "المُتَعَلِّم",
    titleEn = "Al-Muta'allim (The Learner)",
    subtitleBn = "শুভ সূচনা! পুনরায় খেলে ৩ তারকা ও উচ্চতর পদবি অর্জন করুন।",
    subtitleEn = "Good start! Replay to achieve higher stars and mastery.",
    subtitleAr = "بداية موفقة ومباركة في طريق العلم، واصل المحاولة للتميز.",
    primaryColor = Color(0xFF8B5CF6),
    secondaryColor = Color(0xFF6D28D9),
    emoji = "📖",
    starThreshold = 1
  );

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

  companion object {
    fun fromScore(score: Int, total: Int = 20): ArabicCulturalTitle {
      val percentage = (score * 100) / total
      return when {
        percentage == 100 -> HAFIZ_UL_HURUF
        percentage >= 90 -> AL_MUTQIN
        percentage >= 75 -> AL_MUJTAHID
        else -> AL_MUTAALLIM
      }
    }

    fun calculateStars(score: Int, total: Int = 20): Int {
      val percentage = (score * 100) / total
      return when {
        percentage >= 90 -> 3
        percentage >= 75 -> 2
        percentage >= 50 -> 1
        else -> 0
      }
    }
  }
}

/**
 * 3-Tier Progressive Difficulty Engine across 20 Levels:
 * - Levels 1-5: Basic single-letter recognition and simple visual cues.
 * - Levels 6-10: Connected letter forms (Initial, Medial, Final) and multi-nukta identification.
 * - Levels 11-20: Advanced connected words, time-attack rapid challenges, and phonetically similar letter pairs.
 */
enum class GameDifficultyTier(
  val levelRange: IntRange,
  val titleBn: String,
  val titleEn: String,
  val titleAr: String,
  val descriptionBn: String,
  val descriptionEn: String,
  val descriptionAr: String,
  val badgeColor: Color,
  val iconEmoji: String
) {
  FOUNDATION(
    levelRange = 1..5,
    titleBn = "মৌলিক হরফ পরিচিতি",
    titleEn = "Letter Foundation",
    titleAr = "أساسيات الحروف",
    descriptionBn = "একক হরফ চেনা, সাধারণ ভিজ্যুয়াল কিউ এবং স্পষ্ট উচ্চারণ",
    descriptionEn = "Single-letter recognition, basic visual cues & clear pronunciation",
    descriptionAr = "التعرف على الحروف المفردة والعلامات البصرية الأولية",
    badgeColor = Color(0xFF10B981),
    iconEmoji = "🌱"
  ),
  CONNECTED_FORMS(
    levelRange = 6..10,
    titleBn = "সংযুক্ত রূপ ও নুকতা",
    titleEn = "Connected Forms & Dots",
    titleAr = "الأشكال المتصلة والنقاط",
    descriptionBn = "শুরুর, মধ্য ও শেষ রূপ এবং বহু-নুকতাযুক্ত হরফের গভীর পার্থক্য",
    descriptionEn = "Initial, Medial, Final forms and multi-nukta identification",
    descriptionAr = "أشكال الحروف (بداية، وسط، نهاية) ومواضع النقاط المتعددة",
    badgeColor = Color(0xFF0284C7),
    iconEmoji = "🔗"
  ),
  ADVANCED_MASTERY(
    levelRange = 11..20,
    titleBn = "উন্নত শব্দ ও টাইম-অ্যাটাক",
    titleEn = "Advanced Words & Speed",
    titleAr = "الكلمات المتقدمة وسرعة البديهة",
    descriptionBn = "সংযুক্ত শব্দগঠন, দ্রুত চ্যালেঞ্জ এবং সদৃশ ধ্বনিযুক্ত হরফ জোড়া",
    descriptionEn = "Connected words, time-attack challenges & phonetically similar pairs",
    descriptionAr = "تركيب الكلمات، تحديات الوقت السريعة، وتفريق الحروف المتشابهة صوتاً",
    badgeColor = Color(0xFF8B5CF6),
    iconEmoji = "⚡"
  );

  companion object {
    fun fromLevel(level: Int): GameDifficultyTier {
      return when (level) {
        in 1..5 -> FOUNDATION
        in 6..10 -> CONNECTED_FORMS
        else -> ADVANCED_MASTERY
      }
    }
  }
}

/**
 * Shape Master Path Specific 4-Tier Curriculum Model
 */
data class ShapeMasterLevelInfo(
  val levelNumber: Int,
  val nameBn: String,
  val nameEn: String,
  val nameAr: String,
  val lettersSubtitle: String
)

data class ShapeMasterTier(
  val tierNumber: Int,
  val levelRange: IntRange,
  val titleBn: String,
  val titleEn: String,
  val titleAr: String,
  val descriptionBn: String,
  val descriptionEn: String,
  val descriptionAr: String,
  val badgeColor: Color,
  val iconEmoji: String
)

object ShapeMasterCurriculum {
  val tierInitial = ShapeMasterTier(
    tierNumber = 1,
    levelRange = 1..5,
    titleBn = "প্রাথমিক রূপ (Initial Forms)",
    titleEn = "Initial Forms (بِدَايَة)",
    titleAr = "بداية الكلمة (أشكال أولية)",
    descriptionBn = "শব্দের শুরুতে হরফের পরিবর্তনের রূপ চিনুন (যেমন: 'ب' থেকে 'بـ')",
    descriptionEn = "Recognize single-letter transformations at word start (e.g., 'ب' to 'بـ')",
    descriptionAr = "التعرف على شكل الحرف في بداية الكلمة وتغيراته",
    badgeColor = Color(0xFF10B981),
    iconEmoji = "🌱"
  )

  val tierMedial = ShapeMasterTier(
    tierNumber = 2,
    levelRange = 6..10,
    titleBn = "মধ্য রূপ (Medial Forms)",
    titleEn = "Medial Forms (وَسَط)",
    titleAr = "وسط الكلمة (أشكال متوسطة)",
    descriptionBn = "শব্দের মাঝে উভয় পাশে সংযুক্ত রূপ শনাক্ত করুন (যেমন: 'ـبـ')",
    descriptionEn = "Identify connected middle forms joined from both sides (e.g., 'ـبـ')",
    descriptionAr = "تمييز الحرف المتصل من الجانبين في وسط الكلمة",
    badgeColor = Color(0xFF0284C7),
    iconEmoji = "🔗"
  )

  val tierFinal = ShapeMasterTier(
    tierNumber = 3,
    levelRange = 11..15,
    titleBn = "শেষ রূপ (Final Forms)",
    titleEn = "Final Forms (نِهَايَة)",
    titleAr = "نهاية الكلمة (أشكال نهائية)",
    descriptionBn = "শব্দের শেষে যুক্ত সমাপ্তি রূপ চিনুন (যেমন: 'ـب' বনাম বিচ্ছিন্ন 'ب')",
    descriptionEn = "Identify connected ending forms (e.g., 'ـب' vs Isolated 'ب')",
    descriptionAr = "التعرف على شكل الحرف المتصل في نهاية الكلمة",
    badgeColor = Color(0xFF8B5CF6),
    iconEmoji = "🎯"
  )

  val tierWordAnalysis = ShapeMasterTier(
    tierNumber = 4,
    levelRange = 16..20,
    titleBn = "মিশ্র রূপ ও শব্দ বিশ্লেষণ (Word Analysis)",
    titleEn = "Word Analysis (تحليل الكلمات)",
    titleAr = "تحليل الكلمات في المصحف",
    descriptionBn = "৩-অক্ষরের আরবি শব্দে (যেমন: 'كَتَبَ') নির্দিষ্ট হরফের সঠিক রূপ শনাক্ত করুন",
    descriptionEn = "Isolate and identify the exact shape of a letter inside 3-letter words",
    descriptionAr = "استخراج وتحديد موضع وشكل الحرف داخل كلمات القرآن الثلاثية",
    badgeColor = Color(0xFFF59E0B),
    iconEmoji = "⚡"
  )

  val allTiers = listOf(tierInitial, tierMedial, tierFinal, tierWordAnalysis)

  fun getTierForLevel(level: Int): ShapeMasterTier = when (level) {
    in 1..5 -> tierInitial
    in 6..10 -> tierMedial
    in 11..15 -> tierFinal
    else -> tierWordAnalysis
  }

  val levelInfos = listOf(
    ShapeMasterLevelInfo(1, "নুকতা হরফ (শুরুতে)", "Boat Letters Initial", "الحروف القاربية بداية", "بـ, تـ, ثـ, نـ, يـ"),
    ShapeMasterLevelInfo(2, "গোল ও কণ্ঠ হরফ", "Curve Letters Initial", "الحروف الحلقية بداية", "جـ, حـ, خـ, عـ, غـ"),
    ShapeMasterLevelInfo(3, "দাঁত ও মুকুট হরফ", "Teeth Letters Initial", "حروف الأسنان بداية", "سـ, شـ, صـ, ضـ, طـ, ظـ"),
    ShapeMasterLevelInfo(4, "উঁচু ও স্বতন্ত্র হরফ", "Tall & Special Initial", "الحروف القائمة والمفردة", "فـ, قـ, كـ, لـ, مـ, هـ"),
    ShapeMasterLevelInfo(5, "প্রারম্ভিক রূপ মাস্টার", "Initial Forms Master", "اختبار بداية الكلمة", "২৮টি হরফের প্রারম্ভিক রূপ"),
    ShapeMasterLevelInfo(6, "নুকতা হরফ (মাঝে)", "Boat Letters Medial", "الحروف القاربية وسط", "ـبـ, ـتـ, ـثـ, ـنـ, ـيـ"),
    ShapeMasterLevelInfo(7, "গোল ও কণ্ঠ হরফ", "Curve Letters Medial", "الحروف الحلقية وسط", "ـجـ, ـحـ, ـخـ, ـعـ, ـغـ"),
    ShapeMasterLevelInfo(8, "দাঁত ও মুকুট হরফ", "Teeth Letters Medial", "حروف الأسنان وسط", "ـسـ, ـشـ, ـصـ, ـضـ, ـطـ, ـظـ"),
    ShapeMasterLevelInfo(9, "উঁচু ও প্রজাপতি হরফ", "Tall & Butterfly Medial", "الحروف القائمة والمفرشية", "ـفـ, ـقـ, ـكـ, ـلـ, ـمـ, ـهـ"),
    ShapeMasterLevelInfo(10, "মধ্য রূপ মাস্টার", "Medial Forms Master", "اختبار وسط الكلمة", "সকল সংযুক্ত মধ্য রূপ"),
    ShapeMasterLevelInfo(11, "নুকতা হরফ (শেষে)", "Boat Letters Final", "الحروف القاربية نهاية", "ـب, ـت, ـث, ـن, ـي"),
    ShapeMasterLevelInfo(12, "গোল ও পেটযুক্ত হরফ", "Curve Letters Final", "الحروف الحلقية نهاية", "ـج, ـح, ـخ, ـع, ـغ"),
    ShapeMasterLevelInfo(13, "দাঁত ও বাটি হরফ", "Teeth Letters Final", "حروف الأسنان نهاية", "ـس, ـش, ـص, ـض, ـط, ـظ"),
    ShapeMasterLevelInfo(14, "উঁচু ও বিশেষ শেষ রূপ", "Tall & Special Final", "الحروف القائمة نهاية", "ـف, ـق, ـك, ـل, ـم, ـه"),
    ShapeMasterLevelInfo(15, "শেষ রূপ মাস্টার ⚡", "Final Forms Master ⚡", "اختبار نهاية الكلمة", "যুক্ত শেষ রূপ বনাম বিচ্ছিন্ন"),
    ShapeMasterLevelInfo(16, "শব্দে শুরুর হরফ", "Word Analysis: Initial", "الحرف الأول في الكلمة", "كَتَبَ, قَلَم, نَصَرَ"),
    ShapeMasterLevelInfo(17, "শব্দে মধ্য হরফ", "Word Analysis: Medial", "الحرف الأوسط في الكلمة", "كَتَبَ, قَلَم, صَبَرَ"),
    ShapeMasterLevelInfo(18, "শব্দে শেষ হরফ", "Word Analysis: Final", "الحرف الأخير في الكلمة", "كَتَبَ, قَلَم, سَمِعَ"),
    ShapeMasterLevelInfo(19, "মিশ্র রূপ বিশ্লেষণ", "Mixed Word Analysis", "تحليل مقاطع الكلمات", "কুরআনিক শব্দে হরফের রূপ"),
    ShapeMasterLevelInfo(20, "গ্র্যান্ড ওয়ার্ড মাস্টার ⚡", "Grand Word Master ⚡", "سيد تفكيك الكلمات", "১০ সেকেন্ড টাইম-অ্যাটাক")
  )

  fun getLevelInfo(level: Int): ShapeMasterLevelInfo {
    return levelInfos.getOrNull(level - 1) ?: ShapeMasterLevelInfo(
      levelNumber = level,
      nameBn = "লেভেল $level",
      nameEn = "Level $level",
      nameAr = "مستوى $level",
      lettersSubtitle = "হরফের রূপ চর্চা"
    )
  }
}

data class NuktaMasterLevelInfo(
  val levelNumber: Int,
  val nameBn: String,
  val nameEn: String,
  val nameAr: String,
  val lettersSubtitle: String
)

data class NuktaMasterTier(
  val tierNumber: Int,
  val levelRange: IntRange,
  val titleBn: String,
  val titleEn: String,
  val titleAr: String,
  val descriptionBn: String,
  val descriptionEn: String,
  val descriptionAr: String,
  val badgeColor: Color,
  val iconEmoji: String
)

object NuktaMasterCurriculum {
  val tierSingleDot = NuktaMasterTier(
    tierNumber = 1,
    levelRange = 1..5,
    titleBn = "১টি নুকতা চ্যালেঞ্জ (Single Dot)",
    titleEn = "Single Dot Challenge (نقطة واحدة)",
    titleAr = "تحدي النقطة الواحدة",
    descriptionBn = "১টি নুকতাযুক্ত হরফের উপরে বা নিচের অবস্থান চিনুন (ب, ن, ج, خ, ذ, ز, ض, ظ, غ, ف)",
    descriptionEn = "Identify letters with 1 dot placed above or below/inside",
    descriptionAr = "التعرف على الحروف ذات النقطة الواحدة فوق الحرف أو تحته",
    badgeColor = Color(0xFFEA580C),
    iconEmoji = "📍"
  )

  val tierDoubleDots = NuktaMasterTier(
    tierNumber = 2,
    levelRange = 6..10,
    titleBn = "২টি নুকতা চ্যালেঞ্জ (Double Dots)",
    titleEn = "Double Dots Challenge (نقطتان)",
    titleAr = "تحدي النقطتين",
    descriptionBn = "২টি নুকতাযুক্ত হরফের উপরে (ت, ق) বনাম নিচে (ي) অবস্থান পার্থক্য করুন",
    descriptionEn = "Differentiate letters with 2 dots above (ت, ق) vs below (ي)",
    descriptionAr = "التمييز بين الحروف ذات النقطتين في الأعلى والأسفل",
    badgeColor = Color(0xFF0284C7),
    iconEmoji = "🎯"
  )

  val tierTripleDots = NuktaMasterTier(
    tierNumber = 3,
    levelRange = 11..15,
    titleBn = "৩টি নুকতা চ্যালেঞ্জ (Triple Dots)",
    titleEn = "Triple Dots Challenge (ثلاث نقاط)",
    titleAr = "تحدي النقاط الثلاث",
    descriptionBn = "৩টি নুকতাযুক্ত হরফ (ث, ش) চিনুন এবং ১ বা ২ নুকতা থেকে পার্থক্য করুন",
    descriptionEn = "Master 3-dot letters (ث, ش) vs 1 or 2 dot counterparts",
    descriptionAr = "إتقان الحروف ذات النقاط الثلاث وتمييزها عن غيرها",
    badgeColor = Color(0xFF8B5CF6),
    iconEmoji = "✨"
  )

  val tierDotlessSpeed = NuktaMasterTier(
    tierNumber = 4,
    levelRange = 16..20,
    titleBn = "নুকতাহীন ও মিশ্র স্পিড টেস্ট (Dotless & Speed)",
    titleEn = "Dotless & Speed Test (بدون نقاط والسرعة)",
    titleAr = "الحروف غير المنقوطة واختبار السرعة",
    descriptionBn = "নুকতাহীন হরফ (ح, س, ص, ط, ع) ও মিশ্র হরফ দ্রুত শনাক্ত করুন (টাইম-অ্যাটাক)",
    descriptionEn = "Rapid visual accuracy separating dotless letters from dotted counterparts under time pressure",
    descriptionAr = "السرعة والدقة في تمييز الحروف غير المنقوطة من المنقوطة تحت ضغط الوقت",
    badgeColor = Color(0xFFEF4444),
    iconEmoji = "⚡"
  )

  val allTiers = listOf(tierSingleDot, tierDoubleDots, tierTripleDots, tierDotlessSpeed)

  fun getTierForLevel(level: Int): NuktaMasterTier = when (level) {
    in 1..5 -> tierSingleDot
    in 6..10 -> tierDoubleDots
    in 11..15 -> tierTripleDots
    else -> tierDotlessSpeed
  }

  val levelInfos = listOf(
    NuktaMasterLevelInfo(1, "১ নুকতা: নিচে (বা ও জিম)", "1 Dot: Below (Ba & Jeem)", "نقطة بالأسفل (ب، ج)", "ب, ج (নিচে ১ নুকতা)"),
    NuktaMasterLevelInfo(2, "১ নুকতা: উপরে (দাঁত ও বাটি)", "1 Dot: Above (Teeth/Bowl)", "نقطة بالأعلى (أسنان)", "ن, ض, ظ (উপরে ১ নুকতা)"),
    NuktaMasterLevelInfo(3, "১ নুকতা: উপরে (কণ্ঠ ও ওষ্ঠ)", "1 Dot: Above (Throat/Lip)", "نقطة بالأعلى (حلق وشفا)", "خ, غ, ف (উপরে ১ নুকতা)"),
    NuktaMasterLevelInfo(4, "১ নুকতা: উপরে (স্বতন্ত্র)", "1 Dot: Above (Independent)", "نقطة بالأعلى (منفصلة)", "ذ, ز (উপরে ১ নুকতা)"),
    NuktaMasterLevelInfo(5, "১ নুকতা মাস্টার চ্যালেঞ্জ", "Single Dot Master Challenge", "تحدي النقطة الواحدة الشامل", "১০টি ১-নুকতা হরফের রূপ"),
    NuktaMasterLevelInfo(6, "২ নুকতা: উপরে (তা ও ক্বাফ)", "2 Dots: Above (Taa & Qaaf)", "نقطتان بالأعلى (ت، ق)", "ت, ق (উপরে ২ নুকতা)"),
    NuktaMasterLevelInfo(7, "২ নুকতা: নিচে (ইয়া)", "2 Dots: Below (Yaa)", "نقطتان بالأسفل (ي)", "ي, يـ, ـيـ (নিচে ২ নুকতা)"),
    NuktaMasterLevelInfo(8, "২ নুকতার অবস্থান তুলনা", "2 Dots Position Contrast", "مقارنة مواضع النقطتين", "ت, ق বনাম ي এর পার্থক্য"),
    NuktaMasterLevelInfo(9, "সংযুক্ত রূপে ২ নুকতা", "Connected Double Dots", "النقطتان في الكلمات", "تـ, ـتـ বনাম يـ, ـيـ"),
    NuktaMasterLevelInfo(10, "২ নুকতা মাস্টার চ্যালেঞ্জ", "Double Dots Master Challenge", "تحدي النقطتين الشامل", "২-নুকতা হরফের নিখুঁত রূপ"),
    NuktaMasterLevelInfo(11, "৩ নুকতা: সা (নৌকা হরফ)", "3 Dots: Thaa (Boat)", "ثلاث نقاط: الثاء", "ث বনাম ب, ت, ن"),
    NuktaMasterLevelInfo(12, "৩ নুকতা: শিন (দাঁত হরফ)", "3 Dots: Sheen (Teeth)", "ثلاث نقاط: الشين", "ش বনাম س"),
    NuktaMasterLevelInfo(13, "৩ নুকতা বনাম ১ ও ২ নুকতা", "3 Dots vs 1 & 2 Dots", "مقارنة ٣ نقاط مع ١ و ٢", "ث, ش বনাম ب, ت, س"),
    NuktaMasterLevelInfo(14, "সংযুক্ত রূপে ৩ নুকতা", "Connected Triple Dots", "النقاط الثلاث في الكلمات", "ثـ, ـثـ, شـ, ـشـ"),
    NuktaMasterLevelInfo(15, "৩ নুকতা মাস্টার ⚡", "Triple Dots Master ⚡", "تحدي النقاط الثلاث السريع", "১২ সেকেন্ড দ্রুত পরীক্ষা"),
    NuktaMasterLevelInfo(16, "নুকতাহীন হরফ পরিচিতি", "Dotless Letters Intro", "الحروف المهملة (بدون نقاط)", "ح, س, ص, ط, ع"),
    NuktaMasterLevelInfo(17, "নুকতাহীন বনাম ১ নুকতা", "Dotless vs 1 Dot Contrast", "مقارنة المهملة بالمعجمة", "ح/خ, س/ش, ص/ض, ط/ظ, ع/غ"),
    NuktaMasterLevelInfo(18, "সদৃশ জোড়ায় নুকতা যাচাই", "Similar Pair Dot Check", "تمييز الحروف المتشابهة", "দাঁত, পেট ও গলার হরফ"),
    NuktaMasterLevelInfo(19, "নুকতা গণনা ও দ্রুত বাছাই", "Dot Count Challenge", "عد وتحديد النقاط بدقة", "০, ১, ২, ৩ নুকতার সঠিক মিল"),
    NuktaMasterLevelInfo(20, "গ্র্যান্ড নুকতা মাস্টার ⚡", "Grand Nukta Master ⚡", "سيد النقاط الأكبر", "১০ সেকেন্ড টাইম-অ্যাটাক")
  )

  fun getLevelInfo(level: Int): NuktaMasterLevelInfo {
    return levelInfos.getOrNull(level - 1) ?: NuktaMasterLevelInfo(
      levelNumber = level,
      nameBn = "লেভেল $level",
      nameEn = "Level $level",
      nameAr = "مستوى $level",
      lettersSubtitle = "নুকতা অনুশীলন"
    )
  }
}

data class LetterLinkLevelInfo(
  val levelNumber: Int,
  val nameBn: String,
  val nameEn: String,
  val nameAr: String,
  val lettersSubtitle: String
)

data class LetterLinkTier(
  val tierNumber: Int,
  val levelRange: IntRange,
  val titleBn: String,
  val titleEn: String,
  val titleAr: String,
  val descriptionBn: String,
  val descriptionEn: String,
  val descriptionAr: String,
  val badgeColor: Color,
  val iconEmoji: String
)

object LetterLinkCurriculum {
  val tierBasicMatching = LetterLinkTier(
    tierNumber = 1,
    levelRange = 1..5,
    titleBn = "২x২ বেসিক ম্যাচ গ্রিড (Basic Matching)",
    titleEn = "2x2 Basic Matching Grid (المطابقة الأساسية)",
    titleAr = "شبكة المطابقة الأساسية ٢×٢",
    descriptionBn = "একই ও পরিচিত একক হরফের জোড়া মিলিয়ে লিঙ্ক তৈরি করুন (ا, ب, ت, ث...)",
    descriptionEn = "Match identical isolated letters on a 2x2 grid for visual recognition",
    descriptionAr = "مطابقة الحروف المفردة المتطابقة في شبكة ٢×٢ لتعزيز التمييز البصري",
    badgeColor = Color(0xFFB83280),
    iconEmoji = "💎"
  )

  val tierShapeVariant = LetterLinkTier(
    tierNumber = 2,
    levelRange = 6..10,
    titleBn = "৩x৩ শেপ-ভ্যারিয়েন্ট গ্রিড (Shape-Variant Grid)",
    titleEn = "3x3 Shape-Variant Grid (أشكال الحروف)",
    titleAr = "شبكة أشكال الحروف المتصلة ٣×٣",
    descriptionBn = "একক হরফের সাথে তার প্রারম্ভিক, মধ্য ও শেষ সংযুক্ত রূপের সঠিক মিল খুঁজুন",
    descriptionEn = "Match isolated letters to their connected initial and medial forms",
    descriptionAr = "مطابقة الحرف المفرد مع أشكاله المتصلة في أول الكلمة ووسطها",
    badgeColor = Color(0xFF0284C7),
    iconEmoji = "🧩"
  )

  val tierPhoneticPair = LetterLinkTier(
    tierNumber = 3,
    levelRange = 11..15,
    titleBn = "৪x৪ ফনেটিক পেয়ার গ্রিড (Phonetic Pair Grid)",
    titleEn = "4x4 Phonetic Pair Grid (الأزواج الصوتية)",
    titleAr = "شبكة الأزواج الصوتية المتشابهة ٤×٤",
    descriptionBn = "উচ্চারণ ও মাখরাজে সদৃশ কিন্তু ভিন্ন বৈশিষ্ট্যের হরফ জোড়া মিলিয়ে লিংক করুন",
    descriptionEn = "Match phonetically similar letter pairs based on articulation and sound",
    descriptionAr = "مطابقة الحروف المتقاربة في المخرج والصوت لتمييز مخارج الحروف",
    badgeColor = Color(0xFF7C3AED),
    iconEmoji = "🎧"
  )

  val tierTimeAttack = LetterLinkTier(
    tierNumber = 4,
    levelRange = 16..20,
    titleBn = "র‌্যাপিড টাইম-অ্যাটাক গ্রিড (Time-Attack Grid)",
    titleEn = "Rapid Time-Attack Grid (سباق السرعة)",
    titleAr = "شبكة سباق السرعة والذاكرة ٤×٤",
    descriptionBn = "দ্রুততম সময়ে মেমোরি রিকল ও ভিজ্যুয়াল লিংকিং সম্পন্ন করুন (টাইম-অ্যাটাক)",
    descriptionEn = "High-speed memory recall and instant letter recognition under ticking clock",
    descriptionAr = "سرعة الاسترجاع الذهني والربط الفوري تحت ضغط العداد التنازلي",
    badgeColor = Color(0xFFEF4444),
    iconEmoji = "⚡"
  )

  val allTiers = listOf(tierBasicMatching, tierShapeVariant, tierPhoneticPair, tierTimeAttack)

  fun getTierForLevel(level: Int): LetterLinkTier = when (level) {
    in 1..5 -> tierBasicMatching
    in 6..10 -> tierShapeVariant
    in 11..15 -> tierPhoneticPair
    else -> tierTimeAttack
  }

  val levelInfos = listOf(
    LetterLinkLevelInfo(1, "প্রাথমিক হরফ লিংক (১-৭)", "Early Letters Link (1-7)", "ربط الحروف الأولى (١-٧)", "ا, ب, ت, ث, ج, ح, خ"),
    LetterLinkLevelInfo(2, "মধ্যবর্তী হরফ লিংক (৮-১৪)", "Mid Letters Link (8-14)", "ربط الحروف المتوسطة", "د, ذ, ر, ز, س, ش, ص"),
    LetterLinkLevelInfo(3, "ভারী হরফ লিংক (১৫-২১)", "Heavy Letters Link (15-21)", "ربط الحروف المفخمة", "ض, ط, ظ, ع, غ, ف, ق"),
    LetterLinkLevelInfo(4, "শেষবর্তী হরফ লিংক (২২-২৮)", "Final Letters Link (22-28)", "ربط الحروف الأخيرة", "ك, ل, م, ن, و, هـ, ي"),
    LetterLinkLevelInfo(5, "২x২ মাস্টার লিংক চ্যালেঞ্জ", "2x2 Master Link Challenge", "تحدي شبكة ٢×٢ الشامل", "২৮টি হরফের সম্পূর্ণ জোড়া মিল"),
    LetterLinkLevelInfo(6, "শুরুর রূপ লিংক (Initial Forms)", "Initial Form Match", "ربط بداية الكلمة", "بـ, تـ, ثـ, نـ, يـ এর রূপ মিল"),
    LetterLinkLevelInfo(7, "পেট ও গলার রূপ লিংক", "Curved & Throat Form Match", "ربط أشكال الحلق والبطن", "جـ, حـ, خـ, عـ, غـ এর রূপ মিল"),
    LetterLinkLevelInfo(8, "দাঁত ও লম্বা রূপ লিংক", "Teeth & Tall Form Match", "ربط أشكال الأسنان", "سـ, شـ, كـ, لـ এর রূপ মিল"),
    LetterLinkLevelInfo(9, "মধ্য ও শেষ রূপ মিশ্রণ", "Medial & Final Mix Match", "أشكال وسط ونهاية الكلمة", "ـبـ, ـجـ, ـعـ, ـم এর জোড়া মিল"),
    LetterLinkLevelInfo(10, "৩x৩ রূপ মাস্টার চ্যালেঞ্জ", "3x3 Shape-Variant Master", "تحدي شبكة الأشكال ٣×٣", "সকল সংযুক্ত রূপের দ্রুত মিল"),
    LetterLinkLevelInfo(11, "তা বনাম ত্বা (ت / ط) লিংক", "Taa vs Twaa Link (ت / ط)", "ربط التاء والطاء", "নরম তা বনাম ভারী ত্বা"),
    LetterLinkLevelInfo(12, "যাল, যা ও জোয়া (ذ / ز / ظ)", "Z-Sound Triad Link", "ربط الذال والزاي والظاء", "তিন ভিন্ন মাখরাজের ঝ-ধ্বনি"),
    LetterLinkLevelInfo(13, "সিন, সদ ও সা (س / ص / ث)", "S & Th Sounds Link", "ربط السين والصاد والثاء", "শিস ধ্বনি বনাম নরম ধ্বনি"),
    LetterLinkLevelInfo(14, "হা, হা ও আইন (ح / هـ / ع)", "Throat H & Ayn Link", "ربط الحاء والهاء والعين", "গলার ৩ স্তরের ধ্বনি মিল"),
    LetterLinkLevelInfo(15, "৪x৪ ফনেটিক মাস্টার ⚡", "4x4 Phonetic Master ⚡", "تحدي الأزواج الصوتية السريع", "১২ সেকেন্ড দ্রুত ফনেটিক মিল"),
    LetterLinkLevelInfo(16, "র‌্যাপিড মেমোরি রিকল ১ ⚡", "Rapid Memory Recall I ⚡", "استرجاع الذاكرة السريع ١", "১২ সেকেন্ড টাইম-অ্যাটাক"),
    LetterLinkLevelInfo(17, "র‌্যাপিড মেমোরি রিকল ২ ⚡", "Rapid Memory Recall II ⚡", "استرجاع الذاكرة السريع ٢", "১১ সেকেন্ড টাইম-অ্যাটাক"),
    LetterLinkLevelInfo(18, "হাই-স্পিড রূপ ও সাউন্ড লিংক ⚡", "High-Speed Form & Sound ⚡", "ربط الصوت والشكل فائق السرعة", "১০ সেকেন্ড টাইম-অ্যাটাক"),
    LetterLinkLevelInfo(19, "৪x৪ স্পিড ব্লিৎজ ⚡", "4x4 Speed Blitz ⚡", "تحدي السرعة الخاطفة ٤×٤", "৯ সেকেন্ড চরম পরীক্ষা"),
    LetterLinkLevelInfo(20, "গ্র্যান্ড লেটার লিংক চ্যাম্পিয়ন ⚡", "Grand Letter Link Champion ⚡", "بطل شبكة المطابقة الأكبر", "৮ সেকেন্ড আল্টিমেট চ্যালেঞ্জ")
  )

  fun getLevelInfo(level: Int): LetterLinkLevelInfo {
    return levelInfos.getOrNull(level - 1) ?: LetterLinkLevelInfo(
      levelNumber = level,
      nameBn = "লেভেল $level",
      nameEn = "Level $level",
      nameAr = "مستوى $level",
      lettersSubtitle = "ম্যাচ গ্রিড অনুশীলন"
    )
  }
}

/**
 * Form Fuser (ফর্ম ফিউজার - শব্দ গঠনের পাজল) Dedicated 20-Level Curriculum
 */
data class FormFuserTier(
  val tierNumber: Int,
  val titleBn: String,
  val titleEn: String,
  val titleAr: String,
  val descriptionBn: String,
  val descriptionEn: String,
  val descriptionAr: String,
  val badgeColor: Color,
  val iconEmoji: String,
  val levelRange: IntRange
)

data class FormFuserLevelInfo(
  val levelNumber: Int,
  val nameBn: String,
  val nameEn: String,
  val nameAr: String,
  val lettersSubtitle: String
)

object FormFuserCurriculum {
  val tierTwoLetter = FormFuserTier(
    tierNumber = 1,
    titleBn = "২-বর্ণের সহজ শব্দ",
    titleEn = "2-Letter Simple Words",
    titleAr = "كلمات ثنائية بسيطة",
    descriptionBn = "মৌলিক ২-বর্ণের হরফ ও অব্যয় জোড়া দিয়ে শব্দ গঠন (مِنْ, عَنْ, قُلْ, هَلْ)",
    descriptionEn = "Fuse 2-letter fundamental Arabic particles and commands",
    descriptionAr = "دمج الحروف الثنائية البسيطة والكلمات الأساسية",
    badgeColor = Color(0xFF2B6CB0),
    iconEmoji = "🧩",
    levelRange = 1..5
  )

  val tierThreeLetter = FormFuserTier(
    tierNumber = 2,
    titleBn = "৩-বর্ণের মৌলিক শব্দ",
    titleEn = "3-Letter Basic Words",
    titleAr = "كلمات ثلاثية أساسية",
    descriptionBn = "হারাকাতযুক্ত ৩-বর্ণের মৌলিক ক্রিয়াপদ ও বিশেষ্য গঠন (كَتَبَ, قَرَأَ, دَرَسَ)",
    descriptionEn = "Combine 3-letter verbs and nouns with clear Harakat",
    descriptionAr = "تركيب الأفعال والأسماء الثلاثية مع الحركات",
    badgeColor = Color(0xFF319795),
    iconEmoji = "✨",
    levelRange = 6..10
  )

  val tierFourLetter = FormFuserTier(
    tierNumber = 3,
    titleBn = "৪-বর্ণের যুক্তশব্দ",
    titleEn = "4-Letter Compound Words",
    titleAr = "كلمات رباعية مركبة",
    descriptionBn = "শুরু, মধ্য ও শেষ রূপের মসৃণ রূপান্তরসহ ৪-বর্ণের শব্দ জোড়া (مَسْجِد, مَكْتَب)",
    descriptionEn = "Master 4-letter words with initial, medial, and final transitions",
    descriptionAr = "تركيب الكلمات الرباعية المركبة مع الانتقال السلس للأشكال",
    badgeColor = Color(0xFFD69E2E),
    iconEmoji = "🏛️",
    levelRange = 11..15
  )

  val tierComplexSpeed = FormFuserTier(
    tierNumber = 4,
    titleBn = "জটিল শব্দ গঠন ও স্পিড ফিউশন ⚡",
    titleEn = "Complex Words & Speed Fusion ⚡",
    titleAr = "تركيب الكلمات المعقدة والسرعة ⚡",
    descriptionBn = "অসংযোগ হরফ (ا, د, ذ, ر, ز, و) যুক্ত শব্দ ও দ্রুত টাইম-অ্যাটাক পাজল",
    descriptionEn = "Puzzles featuring non-connecting letters and rapid assembly",
    descriptionAr = "ألغاز الحروف غير المتصلة والتحدي الزمني السريع",
    badgeColor = Color(0xFFE53E3E),
    iconEmoji = "⚡",
    levelRange = 16..20
  )

  val allTiers = listOf(tierTwoLetter, tierThreeLetter, tierFourLetter, tierComplexSpeed)

  fun getTierForLevel(level: Int): FormFuserTier = when (level) {
    in 1..5 -> tierTwoLetter
    in 6..10 -> tierThreeLetter
    in 11..15 -> tierFourLetter
    else -> tierComplexSpeed
  }

  val levelInfos = listOf(
    FormFuserLevelInfo(1, "মৌলিক অব্যয় ও হরফ (مِنْ, عَنْ)", "Basic Particles (min, 'an)", "حروف الجر الثنائية", "م, ن ও ع, ن হরফ ফিউশন"),
    FormFuserLevelInfo(2, "নির্দেশ ও প্রশ্নবোধক (قُلْ, هَلْ)", "Commands & Question (qul, hal)", "الاستفهام والأمر", "ق, ل ও هـ, ل শব্দ গঠন"),
    FormFuserLevelInfo(3, "সংক্ষিপ্ত ক্রিয়া ও আদেশ (كُنْ, قُمْ)", "Short Verbs (kun, qum)", "أفعال الأمر الثنائية", "ك, ن ও ق, م শব্দ ফিউশন"),
    FormFuserLevelInfo(4, "অস্বীকৃতি ও নিশ্চয়তা (لَمْ, لَنْ, قَدْ)", "Negation & Emphasis (lam, lan)", "النفي والتحقيق", "ل, ম ও ل, ن ও ق, د হরফ জোড়া"),
    FormFuserLevelInfo(5, "২-বর্ণের মাস্টার ফিউশন", "2-Letter Master Fusion", "إتقان الكلمات الثنائية", "২-বর্ণের সকল শব্দের সমন্বিত পাজল"),
    FormFuserLevelInfo(6, "৩-বর্ণের মৌলিক ক্রিয়া ১ (كَتَبَ, قَرَأَ)", "3-Letter Verbs I (kataba, qara'a)", "الأفعال الثلاثية ١", "ك, ت, ب ও ق, ر, أ শব্দ জোড়া"),
    FormFuserLevelInfo(7, "৩-বর্ণের মৌলিক ক্রিয়া ২ (دَرَسَ, جَلَسَ)", "3-Letter Verbs II (darasa, jalasa)", "الأفعال الثلاثية ٢", "দাল, সিন, জিম রূপান্তর"),
    FormFuserLevelInfo(8, "কুরআনিক ক্রিয়াপদ ৩ (خَلَقَ, نَصَرَ)", "Quranic Verbs III (khalaqa, nasara)", "أفعال قرآنية ٣", "খলাক্বা ও নাসারা শব্দ গঠন"),
    FormFuserLevelInfo(9, "জ্ঞান ও প্রশংসা ক্রিয়া (عَلِمَ, حَمِدَ)", "Knowledge & Praise (alima, hamida)", "أفعال العلم والحمد", "আইন, লাম, মিম রূপান্তর"),
    FormFuserLevelInfo(10, "৩-বর্ণের মাস্টার ওয়ার্ড বিল্ডার", "3-Letter Master Word Builder", "أستاذ بناء الكلمات الثلاثية", "৩-বর্ণের সমস্ত শব্দের চরম পরীক্ষা"),
    FormFuserLevelInfo(11, "৪-বর্ণের স্থানবাচক শব্দ (مَسْجِد, مَكْتَب)", "4-Letter Places (masjid, maktab)", "أسماء المكان الرباعية", "৪-বর্ণের শব্দের রূপান্তর"),
    FormFuserLevelInfo(12, "৪-বর্ণের বস্তু ও পাঠ (مُصْحَف, دَفْتَر)", "4-Letter Objects (mushaf, daftar)", "أسماء الأدوات والكتب", "মুসহাফ ও দফতর গঠন"),
    FormFuserLevelInfo(13, "৪-বর্ণের কুরআনিক বিশেষ্য (كِتَاب, قُرْآن)", "4-Letter Quranic Nouns (kitaab, quran)", "الأسماء القرآنية", "কিতাব ও কুরআন শব্দ গঠন"),
    FormFuserLevelInfo(14, "৪-বর্ণের জিকির শব্দ (سُبْحَان, رَحْمَن)", "4-Letter Dhikr (subhan, rahman)", "كلمات الأذكار والتسبيح", "সুবহান ও রহমান রূপান্তর"),
    FormFuserLevelInfo(15, "৪-বর্ণের স্পিড ফিউশন ⚡", "4-Letter Speed Fusion ⚡", "تحدي السرعة الرباعي", "১২ সেকেন্ড দ্রুত ফিউশন"),
    FormFuserLevelInfo(16, "অসংযোগ হরফ ১: د, ذ, ر, ز", "Non-Connecting I: d, dh, r, z", "حروف الانفصال ١", "দাল, যাল, র, যা এর বিচ্ছিন্নতা"),
    FormFuserLevelInfo(17, "অসংযোগ হরফ ২: ا, و (نُور, بَاب)", "Non-Connecting II: a, w", "حروف الانفصال ٢", "আলিফ ও ওয়াও এর শব্দ গঠন"),
    FormFuserLevelInfo(18, "জটিল মিশ্র সংযোগ ও বিচ্ছিন্নতা ⚡", "Complex Mixed Connectivity ⚡", "الاتصال والانفصال المزدوج", "১১ সেকেন্ড স্পিড চ্যালেঞ্জ"),
    FormFuserLevelInfo(19, "৫-বর্ণের কুরআন শব্দ ও স্পিড ফিউশন ⚡", "5-Letter Quranic Speed Fusion ⚡", "الكلمات الخماسية السريعة", "১০ সেকেন্ড দ্রুত ফিউশন"),
    FormFuserLevelInfo(20, "গ্র্যান্ড ফর্ম ফিউজার চ্যাম্পিয়ন ⚡", "Grand Form Fuser Champion ⚡", "بطل تركيب الكلمات الأكبر", "৮ সেকেন্ড আল্টিমেট পাজল")
  )

  fun getLevelInfo(level: Int): FormFuserLevelInfo {
    return levelInfos.getOrNull(level - 1) ?: FormFuserLevelInfo(
      levelNumber = level,
      nameBn = "লেভেল $level",
      nameEn = "Level $level",
      nameAr = "مستوى $level",
      lettersSubtitle = "শব্দ গঠন অনুশীলন"
    )
  }
}

/**
 * Memory Card Model for Mix & Match Game Mode
 */
data class MemoryFlipCard(
  val id: String,
  val pairId: String,
  val displayArabic: String,
  val subtitle: String = "",
  val audioLetter: String = displayArabic,
  val cardTypeLabel: String = ""
)

/**
 * Mix & Match (মিক্স অ্যান্ড ম্যাচ - মেমোরি কার্ড ফ্লিপ) Dedicated 20-Level Curriculum
 */
data class MixAndMatchTier(
  val tierNumber: Int,
  val titleBn: String,
  val titleEn: String,
  val titleAr: String,
  val descriptionBn: String,
  val descriptionEn: String,
  val descriptionAr: String,
  val badgeColor: Color,
  val iconEmoji: String,
  val levelRange: IntRange
)

data class MixAndMatchLevelInfo(
  val levelNumber: Int,
  val nameBn: String,
  val nameEn: String,
  val nameAr: String,
  val cardsSubtitle: String
)

object MixAndMatchCurriculum {
  val tierIntroSixCard = MixAndMatchTier(
    tierNumber = 1,
    titleBn = "ইন্ট্রো ৬-কার্ড মেমোরি গ্রিড (৩ জোড়া)",
    titleEn = "Intro 6-Card Grid (3 Pairs / 2x3 Layout)",
    titleAr = "المستوى التمهيدي: شبكة ٦ بطاقات (٣ أزواج)",
    descriptionBn = "২x৩ গ্রিডে মুখবন্ধ ৬টি কার্ড ফ্লিপ করে ৩ জোড়া হরফ, নাম বা রূপের মিল করুন",
    descriptionEn = "Flip 6 face-down cards in a 2x3 grid to match 3 pairs of identical Arabic letters",
    descriptionAr = "اقلب ٦ بطاقات في شبكة ٢×٣ لمطابقة ٣ أزواج من الحروف العربية المتطابقة",
    badgeColor = Color(0xFF805AD5),
    iconEmoji = "🃏",
    levelRange = 1..5
  )

  val tierIntermediateEightCard = MixAndMatchTier(
    tierNumber = 2,
    titleBn = "ইন্টারমিডিয়েট ৮-কার্ড গ্রিড (৪ জোড়া)",
    titleEn = "Intermediate 8-Card Grid (4 Pairs / 2x4 Layout)",
    titleAr = "المستوى المتوسط: شبكة ٨ بطاقات (٤ أزواج)",
    descriptionBn = "২x৪ গ্রিডে ৮টি কার্ড ফ্লিপ করে ৪ জোড়া হরফ, পূর্ণ নাম, মাখরাজ ও যুক্তরূপ মেলান",
    descriptionEn = "Flip 8 face-down cards in a 2x4 grid to match 4 pairs of letters, names, and connected forms",
    descriptionAr = "اقلب ٨ بطاقات في شبكة ٢×٤ لمطابقة ٤ أزواج من الحروف والأسماء والأشكال المتصلة",
    badgeColor = Color(0xFF6B46C1),
    iconEmoji = "🎴",
    levelRange = 6..12
  )

  val tierMasterTenTwelveCard = MixAndMatchTier(
    tierNumber = 3,
    titleBn = "মাস্টার ১০-১২ কার্ড গ্রিড ও স্পিড ফ্লিপ ⚡ (৫-৬ জোড়া)",
    titleEn = "Master 10-12 Card Grid & Speed Flip ⚡ (5-6 Pairs)",
    titleAr = "مستوى الإتقان: شبكة ١٠-١٢ بطاقة وتحدي السرعة ⚡",
    descriptionBn = "৩x৪ গ্রিডে ১০ থেকে ১২টি কার্ডের দ্রুত মেমোরি রিকল ও স্পিড টাইম-অ্যাটাক চ্যালেঞ্জ",
    descriptionEn = "Rapid visual memory recall across 10 to 12 cards (3x4 grid) under time-attack pressure",
    descriptionAr = "تحدي الذاكرة الفائقة في شبكة ٣×٤ لـ ١٠-١٢ بطاقة تحت ضغط عداد السرعة",
    badgeColor = Color(0xFFD53F8C),
    iconEmoji = "⚡",
    levelRange = 13..20
  )

  val allTiers = listOf(tierIntroSixCard, tierIntermediateEightCard, tierMasterTenTwelveCard)

  fun getTierForLevel(level: Int): MixAndMatchTier = when (level) {
    in 1..5 -> tierIntroSixCard
    in 6..12 -> tierIntermediateEightCard
    else -> tierMasterTenTwelveCard
  }

  val levelInfos = listOf(
    MixAndMatchLevelInfo(1, "আলিফ থেকে সা (ا, ب, ت, ث, ج)", "Alif to Thaa (ا to ث)", "الألف إلى الثاء", "৬ কার্ড • ৩ জোড়া একই হরফ (২x৩ গ্রিড)"),
    MixAndMatchLevelInfo(2, "হা থেকে দাল (ح, خ, د, ذ, ر)", "Haa to Dhal (ح to د)", "الحاء إلى الذال", "৬ কার্ড • ৩ জোড়া একই হরফ (২x৩ গ্রিড)"),
    MixAndMatchLevelInfo(3, "ঝা থেকে সাদ (ز, س, ش, ص, ض)", "Zaa to Saad (ز to ص)", "الزاي إلى الصاد", "৬ কার্ড • ৩ জোড়া হরফ ও রূপ (২x৩ গ্রিড)"),
    MixAndMatchLevelInfo(4, "তোয়া থেকে কাফ (ط, ظ, ع, غ, ف, ق, ك)", "Taa to Kaaf (ط to ك)", "الطاء إلى الكاف", "৬ কার্ড • ৩ জোড়া হরফ ও রূপ (২x৩ গ্রিড)"),
    MixAndMatchLevelInfo(5, "লাম থেকে ইয়া ও রিভিশন", "Laam to Yaa & Review", "اللام إلى الياء والمراجعة", "৬ কার্ড • ৩ জোড়া রিভিশন ফ্লিপ (২x৩ গ্রিড)"),
    MixAndMatchLevelInfo(6, "৪ জোড়া হরফ ও আরবি নাম ১ (ا, ب, ت, ث)", "4 Pairs Letter & Name I", "الحرف واسمه ١", "৮ কার্ড • ৪ জোড়া হরফ ও নাম (২x৪ গ্রিড)"),
    MixAndMatchLevelInfo(7, "৪ জোড়া হরফ ও আরবি নাম ২ (ج, ح, خ, د)", "4 Pairs Letter & Name II", "الحرف واسمه ٢", "৮ কার্ড • ৪ জোড়া হরফ ও নাম (২x৪ গ্রিড)"),
    MixAndMatchLevelInfo(8, "৪ জোড়া হরফ ও আরবি নাম ৩ (ذ, ر, ز, س)", "4 Pairs Letter & Name III", "الحرف واسمه ٣", "৮ কার্ড • ৪ জোড়া হরফ ও নাম (২x৪ গ্রিড)"),
    MixAndMatchLevelInfo(9, "৪ জোড়া হরফ ও আরবি নাম ৪ (ش, ص, ض, ط)", "4 Pairs Letter & Name IV", "الحرف واسمه ٤", "৮ কার্ড • ৪ জোড়া হরফ ও নাম (২x৪ গ্রিড)"),
    MixAndMatchLevelInfo(10, "৪ জোড়া প্রারম্ভিক রূপান্তর (بـ, تـ, ثـ, نـ, يـ)", "4 Pairs Initial Forms", "الأشكال الأولية", "৮ কার্ড • ৪ জোড়া বিচ্ছিন্ন ও সংযুক্ত (২x৪ গ্রিড)"),
    MixAndMatchLevelInfo(11, "৪ জোড়া মধ্যবর্তী রূপান্তর (ـسـ, ـشـ, ـصـ, ـضـ)", "4 Pairs Medial Forms", "الأشكال المتوسطة", "৮ কার্ড • ৪ জোড়া মধ্যবর্তী রূপ (২x৪ গ্রিড)"),
    MixAndMatchLevelInfo(12, "৪ জোড়া বিশিষ্ট রূপভেদ ও ধ্বনি (ـعـ, ـغـ, ـفـ, ـقـ)", "4 Pairs Special Forms & Sounds", "الأشكال الخاصة والأصوات", "৮ কার্ড • ৪ জোড়া রূপ ও ধ্বনি (২x৪ গ্রিড)"),
    MixAndMatchLevelInfo(13, "১০ কার্ড সদৃশ ধ্বনি ও মাখরাজ ফ্লিপ (৫ জোড়া)", "10 Cards Phonetic Pairs (5 Pairs)", "أزواج الأصوات المتشابهة", "১০ কার্ড • ৫ জোড়া মাখরাজ বৈচিত্র্য (৩x৪ গ্রিড)"),
    MixAndMatchLevelInfo(14, "১০ কার্ড হরকত ও উচ্চারণ ফ্লিপ (৫ জোড়া)", "10 Cards Harakat Pairs (5 Pairs)", "الحركات القصيرة", "১০ কার্ড • ৫ জোড়া যবর, যের, পেশ উচ্চারণ (৩x৪ গ্রিড)"),
    MixAndMatchLevelInfo(15, "১০ কার্ড মিশ্র হরফ ও রূপভেদ (৫ জোড়া)", "10 Cards Mixed Letters & Forms", "الحروف والأشكال المتنوعة", "১০ কার্ড • ৫ জোড়া মিশ্র ফ্লিপ (৩x৪ গ্রিড)"),
    MixAndMatchLevelInfo(16, "১০ কার্ড স্পিড ফ্লিপ টাইম-অ্যাটাক ⚡ (৫ জোড়া)", "10 Cards Speed Flip Blitz ⚡", "سباق السرعة ٥ أزواج", "১০ কার্ড • ৫ জোড়া ১২ সেকেন্ড চ্যালেঞ্জ ⚡ (৩x৪ গ্রিড)"),
    MixAndMatchLevelInfo(17, "১২ কার্ড পূর্ণ বর্ণমালা মেমোরি রিকল (৬ জোড়া)", "12 Cards Alphabet Recall (6 Pairs)", "استرجاع الحروف الكاملة", "১২ কার্ড • ৬ জোড়া (৩x৪ গ্রিড)"),
    MixAndMatchLevelInfo(18, "১২ কার্ড রূপান্তর ও মাখরাজ চ্যাম্পিয়ন (৬ জোড়া)", "12 Cards Form & Makhraj Champion", "بطل الأشكال والمخارج", "১২ কার্ড • ৬ জোড়া রূপ ও মাখরাজ (৩x৪ গ্রিড)"),
    MixAndMatchLevelInfo(19, "১২ কার্ড সুপার মেমোরি ব্লিৎজ ⚡ (৬ জোড়া)", "12 Cards Super Memory Blitz ⚡", "تحدي الذاكرة الفائقة ٦ أزواج", "১২ কার্ড • ৬ জোড়া ১০ সেকেন্ড স্পিড চ্যালেঞ্জ ⚡ (৩x৪ গ্রিড)"),
    MixAndMatchLevelInfo(20, "১২ কার্ড গ্র্যান্ড মেমোরি চ্যাম্পিয়ন ⚡ (৬ জোড়া)", "12 Cards Grand Flip Champion ⚡", "البطل الأكبر لبطاقات الذاكرة", "১২ কার্ড • ৬ জোড়া ৮ সেকেন্ড আল্টিমেট গ্র্যান্ড ফিনালে ⚡ (৩x৪ গ্রিড)")
  )

  fun getLevelInfo(level: Int): MixAndMatchLevelInfo {
    return levelInfos.getOrNull(level - 1) ?: MixAndMatchLevelInfo(
      levelNumber = level,
      nameBn = "লেভেল $level",
      nameEn = "Level $level",
      nameAr = "مستوى $level",
      cardsSubtitle = "মেমোরি কার্ড ফ্লিপ অনুশীলন"
    )
  }
}

/**
 * Question Model for 20-Question Level Engine
 */
data class GameOption(
  val id: String,
  val arabicDisplay: String = "",
  val primaryText: String,
  val secondaryText: String = "",
  val iconEmoji: String = ""
)

data class GameQuestion(
  val questionNumber: Int, // 1..20
  val gameType: GameType,
  val levelNumber: Int,
  val promptBn: String,
  val promptEn: String,
  val promptAr: String,
  val displayArabic: String,
  val targetLetter: ArabicLetter,
  val options: List<GameOption>,
  val correctOptionId: String,
  val explanationBn: String,
  val explanationEn: String,
  val isTimeAttack: Boolean = false,
  val timeLimitSeconds: Int = 12,
  val puzzleLetters: List<String> = emptyList(),
  val targetWordMeaningBn: String = "",
  val targetWordMeaningEn: String = "",
  val targetWordPhonetic: String = "",
  val memoryCards: List<MemoryFlipCard> = emptyList(),
  val requiredPairsCount: Int = 2
) {
  fun getPrompt(lang: String): String = when (lang) {
    "EN" -> promptEn
    "AR" -> promptAr
    else -> promptBn
  }
}

data class LevelProgressState(
  val levelNumber: Int,
  val isUnlocked: Boolean,
  val starsEarned: Int, // 0..3
  val tier: GameDifficultyTier = GameDifficultyTier.fromLevel(levelNumber)
)

fun toBnDigits(number: Number): String {
  val bnDigits = charArrayOf('০', '১', '২', '৩', '৪', '৫', '৬', '৭', '৮', '৯')
  return number.toString().map { if (it in '0'..'9') bnDigits[it - '0'] else it }.joinToString("")
}

fun toArDigits(number: Number): String {
  val arDigits = charArrayOf('٠', '١', '٢', '٣', '٤', '٥', '٦', '٧', '٨', '٩')
  return number.toString().map { if (it in '0'..'9') arDigits[it - '0'] else it }.joinToString("")
}


