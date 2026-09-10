package com.example

import androidx.compose.ui.graphics.Color

/**
 * Anatomical categorization of Arabic Makharij (17 articulation points across 5 major regions).
 */
enum class MakhrajMainRegion(
  val nameBn: String,
  val nameEn: String,
  val nameAr: String,
  val color: Color
) {
  AL_JAWF(
    nameBn = "আল-জওফ (মুখ ও কণ্ঠের খালি জায়গা)",
    nameEn = "Al-Jawf (Oral & Throat Cavity)",
    nameAr = "الجوف (خلاء الفم والحلق)",
    color = Color(0xFF0284C7) // Sky Blue
  ),
  AL_HALQ(
    nameBn = "আল-হলক্ব (কণ্ঠনালী / গলা)",
    nameEn = "Al-Halq (The Throat)",
    nameAr = "الحلق",
    color = Color(0xFFD97706) // Amber / Coral
  ),
  AL_LISAN(
    nameBn = "আল-লিসান (জিহ্বা)",
    nameEn = "Al-Lisan (The Tongue)",
    nameAr = "اللسان",
    color = Color(0xFF059669) // Emerald Green
  ),
  ASH_SHAFATAYN(
    nameBn = "আশ-শাফাতাইন (দুই ওষ্ঠ / ঠোঁট)",
    nameEn = "Ash-Shafatayn (The Lips)",
    nameAr = "الشفتان",
    color = Color(0xFF8B5CF6) // Purple
  ),
  AL_KHAYSHUM(
    nameBn = "আল-খায়শূম (নাসিকা গহ্বর / নাকের বাঁশি)",
    nameEn = "Al-Khayshum (Nasal Cavity)",
    nameAr = "الخيشوم",
    color = Color(0xFFEC4899) // Rose
  )
}

/**
 * Specific organ articulation movement pattern.
 */
enum class ArticulationType {
  JAWF_OPEN_BREATH,             // Alif, Waw Madd, Ya Madd
  THROAT_DEEP_VOCAL_CORDS,       // Hamza, Ha
  THROAT_MID_EPIGLOTTIS,         // 'Ayn, Haa
  THROAT_UPPER_UVULA,            // Ghayn, Khaa
  TONGUE_ROOT_SOFT_PALATE,       // Qaf
  TONGUE_BACK_HARD_PALATE,       // Kaf
  TONGUE_CENTER_ROOF,            // Jim, Sheen, Yaa
  TONGUE_SIDE_MOLAR,             // Dad
  TONGUE_EDGE_GUMS,              // Lam
  TONGUE_TIP_UPPER_GUMS,         // Noon, Raa
  TONGUE_TIP_INCISORS_ROOT,      // Taa, Dal, Taa' (Heavy)
  TONGUE_TIP_INCISORS_EDGE,      // Thaa, Dhal, Zaa' (Heavy)
  TONGUE_TIP_LOWER_INCISORS,     // Seen, Zay, Saad
  LOWER_LIP_UPPER_TEETH,         // Faa
  BOTH_LIPS_COMPRESSED,          // Baa, Meem
  BOTH_LIPS_ROUNDED              // Waw
}

/**
 * Detailed articulation model for each Arabic letter.
 */
data class LetterMakhrajProfile(
  val letter: String,
  val letterNameBn: String,
  val letterNameEn: String,
  val letterNameAr: String,
  val makhrajNumber: Int,
  val mainRegion: MakhrajMainRegion,
  val articulationType: ArticulationType,
  // Normalized 2D/3D articulation contact point on the anatomical canvas (0.0f .. 1.0f)
  val focusX: Float,
  val focusY: Float,
  val hasQalqalah: Boolean = false,
  val hasGhunnah: Boolean = false,
  val isHeavySound: Boolean = false, // Mufakhkham / Isti'la
  val descriptionBn: String,
  val descriptionEn: String,
  val descriptionAr: String,
  val anatomicalRuleBn: String,
  val anatomicalRuleEn: String,
  val anatomicalRuleAr: String
)

object MakhrajRepository {
  val profiles: Map<String, LetterMakhrajProfile> = mapOf(
    "ا" to LetterMakhrajProfile(
      letter = "ا",
      letterNameBn = "আলিফ",
      letterNameEn = "Alif",
      letterNameAr = "ألف",
      makhrajNumber = 1,
      mainRegion = MakhrajMainRegion.AL_JAWF,
      articulationType = ArticulationType.JAWF_OPEN_BREATH,
      focusX = 0.48f,
      focusY = 0.52f,
      descriptionBn = "মুখ ও কণ্ঠের খালি জায়গা থেকে উন্মুক্তভাবে বাতাস প্রবাহিত হয়ে উচ্চারিত হয়।",
      descriptionEn = "Originates from the open space of the mouth and throat (Al-Jawf) with unobstructed airflow.",
      descriptionAr = "يخرج من الجوف وهو خلاء الفم والحلق بامتداد الصوت دون تصادم.",
      anatomicalRuleBn = "কোন নির্দিষ্ট অঙ্গে আঘাত না করে ফুসফুসের বাতাস মুখগহ্বর দিয়ে মুক্তভাবে বের হয়।",
      anatomicalRuleEn = "Breath flows freely through oral and pharyngeal cavities without focal constriction.",
      anatomicalRuleAr = "يمر الهواء بحرية عبر التجويف الحلقي والفموي دون انحباس."
    ),
    "ب" to LetterMakhrajProfile(
      letter = "ب",
      letterNameBn = "বা",
      letterNameEn = "Ba",
      letterNameAr = "باء",
      makhrajNumber = 15,
      mainRegion = MakhrajMainRegion.ASH_SHAFATAYN,
      articulationType = ArticulationType.BOTH_LIPS_COMPRESSED,
      focusX = 0.22f,
      focusY = 0.48f,
      hasQalqalah = true,
      descriptionBn = "উভয় ঠোঁটের ভেজা অংশ পরস্পর শক্তভাবে মিলিত হয়ে হঠাৎ বিচ্ছুরণে উচ্চারিত হয়।",
      descriptionEn = "Produced by pressing the wet inner surfaces of both lips firmly, then releasing.",
      descriptionAr = "يخرج من بين الشفتين بانطباقهما معًا من الجزء الرطب الداخلي.",
      anatomicalRuleBn = "দুই ঠোঁট শক্তভাবে বন্ধ হয়, বাতাস সাময়িক বাধাগ্রস্ত হয় এবং তীব্র ধাক্কায় (কলকলাহ) মুক্ত হয়।",
      anatomicalRuleEn = "Complete bilabial occlusion followed by sudden explosive release (Qalqalah).",
      anatomicalRuleAr = "انطباق تام للشفتين يليه انفجار صوتي واضح عند السكون (القلقلة)."
    ),
    "ت" to LetterMakhrajProfile(
      letter = "ت",
      letterNameBn = "তা",
      letterNameEn = "Ta",
      letterNameAr = "تاء",
      makhrajNumber = 11,
      mainRegion = MakhrajMainRegion.AL_LISAN,
      articulationType = ArticulationType.TONGUE_TIP_INCISORS_ROOT,
      focusX = 0.29f,
      focusY = 0.46f,
      descriptionBn = "জিহ্বার ডগা উপরের সামনের দুই দাঁতের গোঁড়ায় লাগিয়ে উচ্চারিত হয় (হালকা ফিসফিসানি বাতাস সহ)।",
      descriptionEn = "Tip of the tongue firmly against the roots of the upper front teeth with light air whisper (Hams).",
      descriptionAr = "يخرج من طرف اللسان مع أصول الثنايا العليا مع جريان النفس (الهمس).",
      anatomicalRuleBn = "জিহ্বার অগ্রভাগ মাড়ির উঁচু অংশে চাপ দেয়, ছাড়ার মুহূর্তে হালকা বাতাস বের হয়।",
      anatomicalRuleEn = "Tongue tip touches alveolar ridge of upper incisors; released with whispering air.",
      anatomicalRuleAr = "تلامس طرف اللسان مع أصول الثنيتين العلييين يتبعه همس خفيف."
    ),
    "ث" to LetterMakhrajProfile(
      letter = "ث",
      letterNameBn = "ছা",
      letterNameEn = "Tha",
      letterNameAr = "ثاء",
      makhrajNumber = 13,
      mainRegion = MakhrajMainRegion.AL_LISAN,
      articulationType = ArticulationType.TONGUE_TIP_INCISORS_EDGE,
      focusX = 0.25f,
      focusY = 0.47f,
      descriptionBn = "জিহ্বার ডগা উপরের সামনের দুই দাঁতের কিনারার নিচে নরমভাবে স্পর্শ করে উচ্চারিত হয়।",
      descriptionEn = "Tip of the tongue touching the sharp edges of the upper two front teeth softly.",
      descriptionAr = "يخرج من طرف اللسان مع أطراف الثنايا العليا برقة ونعومة.",
      anatomicalRuleBn = "জিহ্বা সামান্য বের হয়ে দাঁতের ডগায় নরম ছোঁয়া লাগায় এবং একটানা শ্বাস চলতে থাকে।",
      anatomicalRuleEn = "Interdental placement where the tongue tip softly contacts the upper incisor edges.",
      anatomicalRuleAr = "وضع طرف اللسان بين الأسنان مع ملامسة حافة الثنايا العليا بلطف."
    ),
    "ج" to LetterMakhrajProfile(
      letter = "ج",
      letterNameBn = "জীম",
      letterNameEn = "Jim",
      letterNameAr = "جيم",
      makhrajNumber = 7,
      mainRegion = MakhrajMainRegion.AL_LISAN,
      articulationType = ArticulationType.TONGUE_CENTER_ROOF,
      focusX = 0.41f,
      focusY = 0.44f,
      hasQalqalah = true,
      descriptionBn = "জিহ্বার মধ্যভাগ সোজা উপরের শক্ত তালুর সাথে পূর্ণ সংলগ্ন হয়ে উচ্চারিত হয়।",
      descriptionEn = "Middle of the tongue pressed firmly against the opposite hard palate roof.",
      descriptionAr = "يخرج من وسط اللسان مع ما يحاذيه من الحنك الأعلى بانحباس الصوت التام.",
      anatomicalRuleBn = "জিহ্বার মাঝের অংশ উপরে উঠে তালুকে স্পর্শ করে বাতাস সম্পূর্ণ আটকে দেয়, এরপর কলকলাহ সহ খোলে।",
      anatomicalRuleEn = "Palatal closure: mid-dorsum elevates and presses against the hard palate ceiling.",
      anatomicalRuleAr = "ارتفاع وسط اللسان والتصاقه بالحنك الأعلى لمنع جريان الصوت قبل الانفراج."
    ),
    "ح" to LetterMakhrajProfile(
      letter = "ح",
      letterNameBn = "হা (বড় হা)",
      letterNameEn = "Ha",
      letterNameAr = "حاء",
      makhrajNumber = 3,
      mainRegion = MakhrajMainRegion.AL_HALQ,
      articulationType = ArticulationType.THROAT_MID_EPIGLOTTIS,
      focusX = 0.61f,
      focusY = 0.67f,
      descriptionBn = "হলকের (গলার) মধ্যভাগ থেকে সংকুচিত বাতাস প্রবাহিত করে পরিষ্কার মসৃণ স্বরে উচ্চারিত হয়।",
      descriptionEn = "Originates from the middle of the throat (pharynx/epiglottis) with smooth friction.",
      descriptionAr = "يخرج من وسط الحلق بانقباض عضلات البلعوم ورجوع لسان المزمار.",
      anatomicalRuleBn = "এপিগ্লটিস পেছনের দিকে সরে এসে গলার মধ্যভাগে মৃদু সংকীর্ণতা তৈরি করে এবং গরম বাতাস নির্গত হয়।",
      anatomicalRuleEn = "Epiglottis shifts toward the posterior pharyngeal wall, creating narrow friction.",
      anatomicalRuleAr = "تراجع لسان المزمار نحو الجدار الخلفي للبلعوم مع سريان نفس صافٍ."
    ),
    "خ" to LetterMakhrajProfile(
      letter = "خ",
      letterNameBn = "খ",
      letterNameEn = "Kha",
      letterNameAr = "خاء",
      makhrajNumber = 4,
      mainRegion = MakhrajMainRegion.AL_HALQ,
      articulationType = ArticulationType.THROAT_UPPER_UVULA,
      focusX = 0.54f,
      focusY = 0.58f,
      isHeavySound = true,
      descriptionBn = "হলকের শেষভাগ (মুখের নিকটবর্তী আলজিহ্বার সংলগ্ন স্থান) থেকে গম্ভীর ভারী স্বরে উচ্চারিত হয়।",
      descriptionEn = "Upper part of the throat near the uvula with rasping friction and heavy fullness.",
      descriptionAr = "يخرج من أدنى الحلق مما يلي الفم مع رخاوة واحتكاك واستعلاء.",
      anatomicalRuleBn = "জিহ্বার গোড়া নরম তালু ও আলজিহ্বার কাছে সংকুচিত হয়ে ঘর্ষণ সৃষ্টি করে এবং শব্দ ভারী হয়।",
      anatomicalRuleEn = "Velar/uvular narrowing in the highest throat zone, producing raspy turbulence.",
      anatomicalRuleAr = "تضيق أعلى الحلق قرب اللهاة مع رفع أقصى اللسان لتفخيم الصوت."
    ),
    "د" to LetterMakhrajProfile(
      letter = "د",
      letterNameBn = "দাল",
      letterNameEn = "Dal",
      letterNameAr = "دال",
      makhrajNumber = 11,
      mainRegion = MakhrajMainRegion.AL_LISAN,
      articulationType = ArticulationType.TONGUE_TIP_INCISORS_ROOT,
      focusX = 0.29f,
      focusY = 0.46f,
      hasQalqalah = true,
      descriptionBn = "জিহ্বার ডগা উপরের সামনের দুই দাঁতের গোঁড়ার মাড়িতে শক্তভাবে লাগিয়ে উচ্চারিত হয়।",
      descriptionEn = "Tip of the tongue firmly against the roots of upper front incisors with Qalqalah.",
      descriptionAr = "يخرج من طرف اللسان مع أصول الثنايا العليا بقوة وانحباس صوت.",
      anatomicalRuleBn = "জিহ্বার ডগা দাঁতের গোঁড়ায় বাতাস আটকে দেয়, সাকিন অবস্থায় তীব্র প্রতিধ্বনি (কলকলাহ) হয়।",
      anatomicalRuleEn = "Tongue tip seals the upper incisor base; air stops and bounces with resonance.",
      anatomicalRuleAr = "انحباس تام للهواء عند أصول الأسنان يعقبه ارتداد صوتي نقي."
    ),
    "ذ" to LetterMakhrajProfile(
      letter = "ذ",
      letterNameBn = "যাল",
      letterNameEn = "Dhal",
      letterNameAr = "ذال",
      makhrajNumber = 13,
      mainRegion = MakhrajMainRegion.AL_LISAN,
      articulationType = ArticulationType.TONGUE_TIP_INCISORS_EDGE,
      focusX = 0.25f,
      focusY = 0.47f,
      descriptionBn = "জিহ্বার ডগা উপরের সামনের দুই দাঁতের ধারালো কিনারায় নরমভাবে ছুঁয়ে উচ্চারিত হয়।",
      descriptionEn = "Tip of the tongue touching the tips of the upper front teeth with soft voiced tone.",
      descriptionAr = "يخرج من طرف اللسان مع أطراف الثنايا العليا برخاوة وجهر.",
      anatomicalRuleBn = "জিহ্বার ডগা দাঁতের কিনারায় আলতোভাবে রাখা হয়, আওয়াজ নরমভাবে চালু থাকে।",
      anatomicalRuleEn = "Voiced interdental friction between upper incisor tips and tongue apex.",
      anatomicalRuleAr = "ملامسة خفيفة لأطراف الأسنان مع استمرار تدفق الصوت برخاوة."
    ),
    "ر" to LetterMakhrajProfile(
      letter = "ر",
      letterNameBn = "র",
      letterNameEn = "Ra",
      letterNameAr = "راء",
      makhrajNumber = 10,
      mainRegion = MakhrajMainRegion.AL_LISAN,
      articulationType = ArticulationType.TONGUE_TIP_UPPER_GUMS,
      focusX = 0.32f,
      focusY = 0.45f,
      descriptionBn = "জিহ্বার ডগার উল্টো পিঠ উপরের সামনের দাঁতের মাড়ির সাথে কম্পন সৃষ্টি করে উচ্চারিত হয়।",
      descriptionEn = "Back of the tongue tip striking the upper gum ridge with slight controlled vibration (Takreer).",
      descriptionAr = "يخرج من طرف اللسان مائلاً لظهره مع لثة الثنايا العليا مع تكرير خفيف.",
      anatomicalRuleBn = "জিহ্বার অগ্রভাগের পিঠ তালুর মাড়িতে দ্রুত স্পর্শ করে মৃদু কম্পন তৈরি করে।",
      anatomicalRuleEn = "Tongue tip lightly taps the alveolar ridge; tap rebound produces melodic trill.",
      anatomicalRuleAr = "ارتطام لطيف لطرف اللسان باللثة مع ارتعاد منضبط دون مبالغة."
    ),
    "ز" to LetterMakhrajProfile(
      letter = "ز",
      letterNameBn = "ঝা",
      letterNameEn = "Zay",
      letterNameAr = "زاي",
      makhrajNumber = 12,
      mainRegion = MakhrajMainRegion.AL_LISAN,
      articulationType = ArticulationType.TONGUE_TIP_LOWER_INCISORS,
      focusX = 0.27f,
      focusY = 0.50f,
      descriptionBn = "জিহ্বার ডগা নিচের সামনের দুই দাঁতের ভেতরের দেয়ালে রেখে মৌমাছির মতো গুঞ্জন/সিস দিয়ে উচ্চারিত হয়।",
      descriptionEn = "Tip of the tongue positioned behind lower front teeth, producing a voiced buzzing whistle (Safeer).",
      descriptionAr = "يخرج من طرف اللسان مع ما بين الثنايا العليا والسفلى قريباً من السفلى بصفير وجهر.",
      anatomicalRuleBn = "বাতাস জিহ্বার ডগা ও দাঁতের সূক্ষ্ম ফাঁক দিয়ে তীব্র গতিতে বেরিয়ে শিষধ্বনি সৃষ্টি করে।",
      anatomicalRuleEn = "Air stream jets across the tongue groove through the incisal gap, creating audible buzz.",
      anatomicalRuleAr = "مرور الصوت عبر قناة ضيقة بين اللسان والأسنان محدثاً صوت الصفير المميز."
    ),
    "س" to LetterMakhrajProfile(
      letter = "س",
      letterNameBn = "সীন",
      letterNameEn = "Sin",
      letterNameAr = "سين",
      makhrajNumber = 12,
      mainRegion = MakhrajMainRegion.AL_LISAN,
      articulationType = ArticulationType.TONGUE_TIP_LOWER_INCISORS,
      focusX = 0.27f,
      focusY = 0.50f,
      descriptionBn = "জিহ্বার ডগা নিচের সামনের দুই দাঁতের গোঁড়ায় রেখে পরিষ্কার বাতাসের শিষ (হিসস্) দিয়ে উচ্চারিত হয়।",
      descriptionEn = "Tip of the tongue resting near lower incisors producing a sharp unvoiced hiss (Safeer & Hams).",
      descriptionAr = "يخرج من طرف اللسان مع ما فوق الثنايا السفلى بصفير وهمس نقي.",
      anatomicalRuleBn = "জিহ্বার মধ্যভাগ অবনমিত এবং ডগা দিয়ে বাতাসের দ্রুত প্রবাহের ফলে স্বচ্ছ শিষধ্বনি তৈরি হয়।",
      anatomicalRuleEn = "Tongue forms a central groove funneling unvoiced breath directly over lower teeth.",
      anatomicalRuleAr = "تدفق النفس بلا رنين صوتي عبر فتحة ضيقة يصدر صفير السين الصافي."
    ),
    "ش" to LetterMakhrajProfile(
      letter = "ش",
      letterNameBn = "শীন",
      letterNameEn = "Shin",
      letterNameAr = "شين",
      makhrajNumber = 7,
      mainRegion = MakhrajMainRegion.AL_LISAN,
      articulationType = ArticulationType.TONGUE_CENTER_ROOF,
      focusX = 0.40f,
      focusY = 0.45f,
      descriptionBn = "জিহ্বার মধ্যভাগ উপরের তালুর কাছে উঠে পুরো মুখগহ্বরে বাতাস ছড়িয়ে (তাফাশ্শী) দিয়ে উচ্চারিত হয়।",
      descriptionEn = "Middle of the tongue raises toward the hard palate, allowing wide air dispersion (Tafash-shi).",
      descriptionAr = "يخرج من وسط اللسان مع الحنك الأعلى مع انتشار الهواء في الفم (التفشي).",
      anatomicalRuleBn = "তালুর সাথে সম্পূর্ণ না লেগে বাতাস ছড়িয়ে পড়ে, ফলে মুখের অভ্যন্তরে বিস্তৃত বাতাস সঞ্চালিত হয়।",
      anatomicalRuleEn = "Tongue dorsum constricts against palate without full seal, spreading air outward.",
      anatomicalRuleAr = "ارتفاع وسط اللسان دون إغلاق كامل يتيح انتشار الهواء داخل الفم."
    ),
    "ص" to LetterMakhrajProfile(
      letter = "ص",
      letterNameBn = "সোয়াদ",
      letterNameEn = "Sad",
      letterNameAr = "صاد",
      makhrajNumber = 12,
      mainRegion = MakhrajMainRegion.AL_LISAN,
      articulationType = ArticulationType.TONGUE_TIP_LOWER_INCISORS,
      focusX = 0.27f,
      focusY = 0.50f,
      isHeavySound = true,
      descriptionBn = "জিহ্বার ডগা নিচের দাঁতের কাছে রেখে এবং জিহ্বার গোড়া তালুর দিকে উঁচু করে ভারী শিষ সহ উচ্চারিত হয়।",
      descriptionEn = "Tip of the tongue at lower teeth with deep velarization/elevation of back tongue (Itbaq & Safeer).",
      descriptionAr = "يخرج من طرف اللسان مع فوق الثنايا السفلى مع إطباق أقصى اللسان وتفخيمه.",
      anatomicalRuleBn = "জিহ্বার ডগা ও গোড়া উভয়ই ওপরে ওঠার ফলে মুখগহ্বর অনুনাদিত হয়ে গম্ভীর ভারী আওয়াজ তৈরি করে।",
      anatomicalRuleEn = "Simultaneous tongue apex placement and posterior dorsum elevation into the pharynx.",
      anatomicalRuleAr = "انحصار الصوت بين ظهر اللسان والحنك الأعلى يمنح الصاد فخامتها المميزة."
    ),
    "ض" to LetterMakhrajProfile(
      letter = "ض",
      letterNameBn = "দোয়াদ",
      letterNameEn = "Dad",
      letterNameAr = "ضاد",
      makhrajNumber = 8,
      mainRegion = MakhrajMainRegion.AL_LISAN,
      articulationType = ArticulationType.TONGUE_SIDE_MOLAR,
      focusX = 0.38f,
      focusY = 0.48f,
      isHeavySound = true,
      descriptionBn = "জিহ্বার পার্শ্বভাগ (বাম বা ডান কিনারা) উপরের মাড়ির দাঁতের গোঁড়ায় দীর্ঘ স্পর্শ বজায় রেখে উচ্চারিত হয়।",
      descriptionEn = "One or both lateral edges of the tongue pressing against the upper molars with elongation (Istitalah).",
      descriptionAr = "يخرج من إحدى حافتي اللسان أو كلتيهما مع ما يحاذيها من الأضراس العليا مع استطالة.",
      anatomicalRuleBn = "আরবি ভাষার অনন্য হরফ; জিহ্বার পাশ মাড়ির দাঁতে চেপে বাতাস ধীরে ধীরে প্রসারিত হয়।",
      anatomicalRuleEn = "Lateral tongue seal against maxillary molars with gradual forward acoustic extension.",
      anatomicalRuleAr = "امتداد حافة اللسان للأمام أثناء نطق الحرف مع تلامس الأضراس العليا."
    ),
    "ط" to LetterMakhrajProfile(
      letter = "ط",
      letterNameBn = "তোয়া",
      letterNameEn = "Ta' (Heavy)",
      letterNameAr = "طاء",
      makhrajNumber = 11,
      mainRegion = MakhrajMainRegion.AL_LISAN,
      articulationType = ArticulationType.TONGUE_TIP_INCISORS_ROOT,
      focusX = 0.29f,
      focusY = 0.46f,
      hasQalqalah = true,
      isHeavySound = true,
      descriptionBn = "জিহ্বার ডগা উপরের দাঁতের গোঁড়ায় লাগিয়ে জিহ্বার পেছনের অংশ পুরোপুরি উঁচু করে সর্বোচ্চ ভারী স্বরে উচ্চারিত হয়।",
      descriptionEn = "Tip of the tongue against upper teeth roots with complete velarization and strong Qalqalah bounce.",
      descriptionAr = "يخرج من طرف اللسان مع أصول الثنايا العليا مع أقوى درجات الإطباق والاستعلاء والقلقلة.",
      anatomicalRuleBn = "আরবি ভাষার সবচেয়ে শক্তিশালী হরফ; দাঁতের গোঁড়ায় বাতাস রুদ্ধ করে ভারী বিস্ফোরণে ছাড়ে।",
      anatomicalRuleEn = "Strongest Arabic consonant; full alveolar occlusion with maximal pharyngeal constriction.",
      anatomicalRuleAr = "أقوى الحروف صوتاً، يحبس النفس والصوت معاً ثم ينفجر بقلقلة مفخمة مدوية."
    ),
    "ظ" to LetterMakhrajProfile(
      letter = "ظ",
      letterNameBn = "যোয়া",
      letterNameEn = "Za' (Heavy)",
      letterNameAr = "ظاء",
      makhrajNumber = 13,
      mainRegion = MakhrajMainRegion.AL_LISAN,
      articulationType = ArticulationType.TONGUE_TIP_INCISORS_EDGE,
      focusX = 0.25f,
      focusY = 0.47f,
      isHeavySound = true,
      descriptionBn = "জিহ্বার ডগা উপরের দাঁতের কিনারায় ছুঁইয়ে জিহ্বা উঁচু করে মুখ ভর্তি গম্ভীর ভারী আওয়াজে উচ্চারিত হয়।",
      descriptionEn = "Tip of the tongue at edges of upper front incisors with heavy velarization (Itbaq).",
      descriptionAr = "يخرج من طرف اللسان مع أطراف الثنايا العليا مع تفخيم وإطباق تام.",
      anatomicalRuleBn = "জিহ্বার প্রান্ত দাঁতের ডগায় রেখে পিছনের অংশ ওপরে উঠিয়ে বাতাসকে গম্ভীর করা হয়।",
      anatomicalRuleEn = "Interdental tongue tip contact with elevated posterior tongue arch creating heavy timbre.",
      anatomicalRuleAr = "تلامس لطيف مع أطراف الثنايا مع ارتفاع تام لظهر اللسان."
    ),
    "ع" to LetterMakhrajProfile(
      letter = "ع",
      letterNameBn = "আইন",
      letterNameEn = "'Ayn",
      letterNameAr = "عين",
      makhrajNumber = 3,
      mainRegion = MakhrajMainRegion.AL_HALQ,
      articulationType = ArticulationType.THROAT_MID_EPIGLOTTIS,
      focusX = 0.61f,
      focusY = 0.67f,
      descriptionBn = "হলকের (গলার) ঠিক মাঝখান থেকে কণ্ঠনালী গভীরভাবে সংকুচিত করে স্পষ্ট ও সুমধুর স্বরে উচ্চারিত হয়।",
      descriptionEn = "Deep middle of the throat; muscular constriction of the pharynx and epiglottis.",
      descriptionAr = "يخرج من وسط الحلق برجوع لسان المزمار بقوة نحو جدار الحلق الخلفي.",
      anatomicalRuleBn = "এপিগ্লটিস শক্তভাবে পেছনের দিকে সরে গিয়ে স্বরযন্ত্রের মাঝামাঝি সুন্দর ধ্বনি তৈরি করে।",
      anatomicalRuleEn = "Pharyngeal constriction: epiglottis moves backwards, producing rich resonant vocalization.",
      anatomicalRuleAr = "انضغاط عضلي في وسط الحلق يولد هذا الحرف العربي الفصيح."
    ),
    "غ" to LetterMakhrajProfile(
      letter = "غ",
      letterNameBn = "গাইন",
      letterNameEn = "Ghayn",
      letterNameAr = "غين",
      makhrajNumber = 4,
      mainRegion = MakhrajMainRegion.AL_HALQ,
      articulationType = ArticulationType.THROAT_UPPER_UVULA,
      focusX = 0.54f,
      focusY = 0.58f,
      isHeavySound = true,
      descriptionBn = "হলকের শেষভাগ থেকে (আলজিহ্বার কাছাকাছি) হালকা ঘর্ষণযুক্ত ভারী কোমল আওয়াজে উচ্চারিত হয়।",
      descriptionEn = "Upper throat nearest to the mouth with smooth voiced friction and heavy resonance.",
      descriptionAr = "يخرج من أدنى الحلق مما يلي الفم مع جهر ورخاوة واستعلاء.",
      anatomicalRuleBn = "জিহ্বার গোড়া ও নরম তালুর কাছাকাছি স্থানে নরম স্পর্শ ও ঘর্ষণ তৈরি হয়।",
      anatomicalRuleEn = "Voiced velar/uvular fricative in the upper throat zone with open vocal resonance.",
      anatomicalRuleAr = "احتكاك رخو في أعلى الحلق مع استعلاء أقصى اللسان وتفخيم."
    ),
    "ف" to LetterMakhrajProfile(
      letter = "ف",
      letterNameBn = "ফা",
      letterNameEn = "Fa",
      letterNameAr = "فاء",
      makhrajNumber = 14,
      mainRegion = MakhrajMainRegion.ASH_SHAFATAYN,
      articulationType = ArticulationType.LOWER_LIP_UPPER_TEETH,
      focusX = 0.23f,
      focusY = 0.49f,
      descriptionBn = "উপরের সামনের দুই দাঁতের ধারালো প্রান্ত নিচের ঠোঁটের ভেতরের আর্দ্র অংশে আলতোভাবে লাগিয়ে উচ্চারিত হয়।",
      descriptionEn = "Tips of the upper front incisors resting gently on the wet inner surface of the lower lip.",
      descriptionAr = "يخرج من باطن الشفة السفلى مع أطراف الثنايا العليا بجريان نفس نقي.",
      anatomicalRuleBn = "দাঁত ও ঠোঁটের সংযোগস্থল দিয়ে বাতাস অবিরামভাবে মসৃণভাবে নিঃসৃত হয়।",
      anatomicalRuleEn = "Labiodental friction: upper incisors touch the lower lip's inner mucosa.",
      anatomicalRuleAr = "ملامسة أطراف الأسنان لبطن الشفة السفلى مع سريان مستمر للهواء."
    ),
    "ق" to LetterMakhrajProfile(
      letter = "ق",
      letterNameBn = "ক্বাফ",
      letterNameEn = "Qaf",
      letterNameAr = "قاف",
      makhrajNumber = 5,
      mainRegion = MakhrajMainRegion.AL_LISAN,
      articulationType = ArticulationType.TONGUE_ROOT_SOFT_PALATE,
      focusX = 0.49f,
      focusY = 0.48f,
      hasQalqalah = true,
      isHeavySound = true,
      descriptionBn = "জিহ্বার গোড়া তার সোজা উপরের নরম মাংসল তালুর সাথে সম্পূর্ণ আবদ্ধ হয়ে তীব্র প্রতিধ্বনির সাথে উচ্চারিত হয়।",
      descriptionEn = "Deep root/back of the tongue striking the soft palate (velum/uvula), followed by resonant Qalqalah.",
      descriptionAr = "يخرج من أقصى اللسان مع ما يحاذيه من الحنك اللحمي بقوة واستعلاء وقلقلة.",
      anatomicalRuleBn = "জিহ্বার মূল নরম তালুতে বাতাস সম্পূর্ণ অবরুদ্ধ করে এবং হঠাৎ ছেড়ে দিলে ভারী কলকলাহ প্রতিধ্বনিত হয়।",
      anatomicalRuleEn = "Complete uvular/velar stop: back of tongue seals soft palate, then bursts with acoustic resonance.",
      anatomicalRuleAr = "إطباق محكم لأقصى اللسان على الحنك الرخو يتبعه انفجار صوتي مدوٍّ."
    ),
    "ك" to LetterMakhrajProfile(
      letter = "ك",
      letterNameBn = "কাফ",
      letterNameEn = "Kaf",
      letterNameAr = "كاف",
      makhrajNumber = 6,
      mainRegion = MakhrajMainRegion.AL_LISAN,
      articulationType = ArticulationType.TONGUE_BACK_HARD_PALATE,
      focusX = 0.45f,
      focusY = 0.46f,
      descriptionBn = "জিহ্বার গোড়া ক্বাফের চেয়ে সামান্য সামনে শক্ত ও নরম তালুর সংযোগস্থলে স্পর্শ করে হালকা বাতাসে উচ্চারিত হয়।",
      descriptionEn = "Back of the tongue touching the hard palate junction, just forward of the Qaf position, with whispering Hams.",
      descriptionAr = "يخرج من أقصى اللسان أسفل من مخرج القاف قليلاً مع الحنك العظمي واللحمي بهمس.",
      anatomicalRuleBn = "জিহ্বা তালুর শক্ত অংশে স্পর্শ করে প্রথমে আওয়াজ আটকায় এবং ছাড়ার সময় ফিসফিসানি বাতাস দেয়।",
      anatomicalRuleEn = "Velar stop positioned slightly anterior to Qaf; releases with clean whispered airflow.",
      anatomicalRuleAr = "التصاق طرف أقصى اللسان بالحنك متبوعاً بسريان نفس خفيف."
    ),
    "ل" to LetterMakhrajProfile(
      letter = "ل",
      letterNameBn = "লাম",
      letterNameEn = "Lam",
      letterNameAr = "لام",
      makhrajNumber = 9,
      mainRegion = MakhrajMainRegion.AL_LISAN,
      articulationType = ArticulationType.TONGUE_EDGE_GUMS,
      focusX = 0.33f,
      focusY = 0.45f,
      descriptionBn = "জিহ্বার সামনের দিকের পার্শ্বভাগ থেকে ডগা পর্যন্ত উপরের সামনের দাঁতগুলোর মাড়ির সাথে লেগে উচ্চারিত হয়।",
      descriptionEn = "The front edges of the tongue extending to its tip against the gums of the upper front teeth.",
      descriptionAr = "يخرج من أدنى حافة اللسان إلى منتهى طرفه مع لثة الأسنان العليا.",
      anatomicalRuleBn = "জিহ্বার সামনের দুই পাশের কিনারা মাড়ির সাথে মিলিত হয় এবং দুই পাশ দিয়ে বাতাস গড়ায়।",
      anatomicalRuleEn = "Lateral tongue margin to apex makes broad contact with maxillary alveolar arch.",
      anatomicalRuleAr = "تلامس عريض لحافة اللسان الأمامية مع لثة الثنايا والرباعيات."
    ),
    "م" to LetterMakhrajProfile(
      letter = "م",
      letterNameBn = "মীম",
      letterNameEn = "Mim",
      letterNameAr = "ميم",
      makhrajNumber = 15,
      mainRegion = MakhrajMainRegion.ASH_SHAFATAYN,
      articulationType = ArticulationType.BOTH_LIPS_COMPRESSED,
      focusX = 0.22f,
      focusY = 0.48f,
      hasGhunnah = true,
      descriptionBn = "উভয় ঠোঁটের শুষ্ক অংশ আলতোভাবে মিলিত হয়ে এবং বাতাস নাকের বাঁশি (খায়শূম) দিয়ে গুন্নাহ করে উচ্চারিত হয়।",
      descriptionEn = "Gentle closure of both lips with vocal resonance diverted through the nasal cavity (Ghunnah).",
      descriptionAr = "يخرج من بين الشفتين بانطباقهما مع غنة لازمة تخرج من الخيشوم.",
      anatomicalRuleBn = "ঠোঁট বন্ধ থাকায় বাতাস মুখ দিয়ে বের হতে পারে না, নরম তালু নিচে নেমে বাতাস নাকে পরিচালিত করে।",
      anatomicalRuleEn = "Bilabial closure while soft palate drops, directing acoustic flow out through the nasal passage.",
      anatomicalRuleAr = "انطباق الشفتين مصحوباً بانخفاض الحنك اللحمي لتوجيه الصوت للخيشوم."
    ),
    "ن" to LetterMakhrajProfile(
      letter = "ن",
      letterNameBn = "নূন",
      letterNameEn = "Nun",
      letterNameAr = "نون",
      makhrajNumber = 10,
      mainRegion = MakhrajMainRegion.AL_LISAN,
      articulationType = ArticulationType.TONGUE_TIP_UPPER_GUMS,
      focusX = 0.31f,
      focusY = 0.45f,
      hasGhunnah = true,
      descriptionBn = "জিহ্বার ডগা উপরের দাঁতের মাড়িতে লেগে মুখ বন্ধ করে এবং নাকের বাঁশি দিয়ে আকর্ষণীয় গুন্নাহ সুর তৈরি হয়।",
      descriptionEn = "Tip of the tongue against upper gum ridge while resonance flows through the nasal cavity (Ghunnah).",
      descriptionAr = "يخرج من طرف اللسان مع لثة الثنايا العليا مصحوباً بغنة من الخيشوم.",
      anatomicalRuleBn = "জিহ্বার ডগা মাড়িতে বায়ুপ্রবাহ আটকায়, একই সাথে নাকের গহ্বরে সুরময় অনুরণন (গুন্নাহ) বাজে।",
      anatomicalRuleEn = "Tongue tip seals the anterior gums; nasal port remains open creating sweet nasal resonance.",
      anatomicalRuleAr = "انسداد مخرج الفم بطرف اللسان وانفتاح الخيشوم لسريان الغنة الرخيمة."
    ),
    "و" to LetterMakhrajProfile(
      letter = "و",
      letterNameBn = "ওয়াও",
      letterNameEn = "Waw",
      letterNameAr = "واو",
      makhrajNumber = 16,
      mainRegion = MakhrajMainRegion.ASH_SHAFATAYN,
      articulationType = ArticulationType.BOTH_LIPS_ROUNDED,
      focusX = 0.20f,
      focusY = 0.48f,
      descriptionBn = "উভয় ঠোঁট গোল করে সামান্য বৃত্তাকার ফাঁকা রেখে বাতাস প্রবাহিত করে উচ্চারিত হয়।",
      descriptionEn = "Rounding both lips forward forming a circular opening without complete closure.",
      descriptionAr = "يخرج بضم الشفتين إلى الأمام مع إبقاء فرجة يسيرة لجريان الصوت.",
      anatomicalRuleBn = "দুই ঠোঁট সামনে এগিয়ে এসে চক্রাকার রূপ ধারণ করে এবং মুক্তভাবে আওয়াজ গড়ায়।",
      anatomicalRuleEn = "Labial protrusion and sphincter-like rounding creating forward resonant aperture.",
      anatomicalRuleAr = "استدارة الشفتين وبروزهما للأمام مع عدم انطباقهما تماماً."
    ),
    "هـ" to LetterMakhrajProfile(
      letter = "هـ",
      letterNameBn = "হা (ছোট হা)",
      letterNameEn = "Ha (Small)",
      letterNameAr = "هاء",
      makhrajNumber = 2,
      mainRegion = MakhrajMainRegion.AL_HALQ,
      articulationType = ArticulationType.THROAT_DEEP_VOCAL_CORDS,
      focusX = 0.63f,
      focusY = 0.74f,
      descriptionBn = "হলকের একেবারে গভীর তলদেশ (বুকের নিকটবর্তী স্বরতন্ত্রী) থেকে স্বস্তিদায়ক প্রশ্বাসের মতো বের হয়।",
      descriptionEn = "Deepest part of the throat near the chest / vocal cords; gentle exhalation breath.",
      descriptionAr = "يخرج من أقصى الحلق مما يلي الصدر عند الأوتار الصوتية بهمس ورخاوة.",
      anatomicalRuleBn = "স্বরতন্ত্রী শিথিল রেখে ফুসফুসের বাতাস মুক্তভাবে স্বস্তির নিঃশ্বাসের ন্যায় গলার গভীর থেকে ওঠে।",
      anatomicalRuleEn = "Glottal friction: vocal cords remain relaxed, allowing pure breath to rise effortlessly.",
      anatomicalRuleAr = "انفراج الأوتار الصوتية ومرور الهواء بسهولة من أعمق نقطة في الحلق."
    ),
    "ء" to LetterMakhrajProfile(
      letter = "ء",
      letterNameBn = "হামযা",
      letterNameEn = "Hamza",
      letterNameAr = "همزة",
      makhrajNumber = 2,
      mainRegion = MakhrajMainRegion.AL_HALQ,
      articulationType = ArticulationType.THROAT_DEEP_VOCAL_CORDS,
      focusX = 0.63f,
      focusY = 0.74f,
      descriptionBn = "হলকের সর্বনিম্ন স্থান (স্বরতন্ত্রী) পূর্ণ সংকুচিত হয়ে হঠাৎ শব্দ কেটে স্পষ্ট দৃঢ় আওয়াজে উচ্চারিত হয়।",
      descriptionEn = "Deepest throat at the vocal cords with a firm momentary glottal stop closure.",
      descriptionAr = "يخرج من أقصى الحلق بانطباق الأوتار الصوتية انطباقاً تاماً ثم انفراجها.",
      anatomicalRuleBn = "ভোকাল কর্ড দুটি ক্ষণিকের জন্য শক্তভাবে বন্ধ হয়ে বাতাস আটকে দেয়, এরপর স্পষ্ট ধ্বনি সৃষ্টি হয়।",
      anatomicalRuleEn = "True glottal stop: vocal cords press tightly shut, halting breath momentarily.",
      anatomicalRuleAr = "إغلاق محكم للمزمار والأوتار الصوتية يقطع النفس قبل انفراجه بقوة."
    ),
    "ي" to LetterMakhrajProfile(
      letter = "ي",
      letterNameBn = "ইয়া",
      letterNameEn = "Ya",
      letterNameAr = "ياء",
      makhrajNumber = 7,
      mainRegion = MakhrajMainRegion.AL_LISAN,
      articulationType = ArticulationType.TONGUE_CENTER_ROOF,
      focusX = 0.41f,
      focusY = 0.44f,
      descriptionBn = "জিহ্বার মধ্যভাগ উপরের তালুর দিকে আলতোভাবে উঁচু করে মসৃণ সুরেলা স্বর দিয়ে উচ্চারিত হয়।",
      descriptionEn = "Middle of the tongue raising softly toward the hard palate with smooth melodious flow.",
      descriptionAr = "يخرج من وسط اللسان مع ما يحاذيه من الحنك الأعلى بلين ودون تعسف.",
      anatomicalRuleBn = "জিহ্বার মধ্যভাগ তালুর কাছে উঠে কিন্তু স্পর্শ করে না, ফলে মিষ্টি ও কোমল স্বর বজায় থাকে।",
      anatomicalRuleEn = "Palatal approximant: tongue body arches toward hard palate without contact friction.",
      anatomicalRuleAr = "ارتفاع وسط اللسان نحو الحنك دون التصاق لجريان صوت رخيم."
    )
  )

  fun getProfile(letterGlyph: String): LetterMakhrajProfile {
    // Normalization check
    val clean = when {
      letterGlyph.startsWith("ا") -> "ا"
      letterGlyph.startsWith("ه") -> "هـ"
      else -> letterGlyph.trim()
    }
    return profiles[clean] ?: profiles[letterGlyph] ?: profiles["ا"]!!
  }
}
