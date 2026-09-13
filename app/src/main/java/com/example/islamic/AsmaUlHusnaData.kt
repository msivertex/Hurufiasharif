package com.example.islamic

/**
 * Model for each of the 99 Blessed Names of Allah
 */
data class AllahName(
  val number: Int,
  val nameAr: String,
  val nameBn: String,
  val nameEn: String,
  val meaningBn: String,
  val meaningEn: String,
  val explanationBn: String,
  val category: NameCategory,
  val quranVerse: String
)

enum class NameCategory(val titleBn: String, val titleEn: String) {
  ALL("সকল নাম", "All Names"),
  MERCY("রহমত ও দয়া", "Mercy & Grace"),
  POWER("শক্তি ও মহিমা", "Power & Majesty"),
  KNOWLEDGE("জ্ঞান ও প্রজ্ঞা", "Knowledge & Wisdom"),
  CREATION("সৃষ্টি ও লালন", "Creation & Sustenance"),
  FORGIVENESS("ক্ষমা ও অনুকম্পা", "Forgiveness & Pardon"),
  PEACE("শান্তি ও পবিত্রতা", "Peace & Holiness")
}

object AsmaUlHusnaMaster {
  val all99Names: List<AllahName> = listOf(
    AllahName(
      1, "الرَّحْمَٰنُ", "আর-রহমান", "Ar-Rahman",
      "পরম করুণাময়", "The Most Gracious",
      "যিনি সকল সৃষ্টির প্রতি অসীম দয়ালু ও অনুগ্রহশীল।",
      NameCategory.MERCY, "সূরা আল-ফাতিহা ১:৩"
    ),
    AllahName(
      2, "الرَّحِيمُ", "আর-রাহীম", "Ar-Raheem",
      "অতি দয়ালু", "The Most Merciful",
      "যিনি মুমিনদের প্রতি বিশেষভাবে চিরকাল দয়াবান।",
      NameCategory.MERCY, "সূরা আল-ফাতিহা ১:৩"
    ),
    AllahName(
      3, "الْمَلِكُ", "আল-মালিক", "Al-Malik",
      "সার্বভৌম অধিপতি", "The King / The Sovereign",
      "সমগ্র মহাবিশ্বের একমাত্র প্রকৃত মালিক ও শাসক।",
      NameCategory.POWER, "সূরা আল-হাশর ৫৯:২৩"
    ),
    AllahName(
      4, "الْقُدُّوسُ", "আল-কুদ্দুস", "Al-Quddoos",
      "নিখুঁত মহাপবিত্র", "The Most Sacred",
      "সকল দোষত্রুটি ও অপূর্ণতা থেকে সম্পূর্ণ মুক্ত।",
      NameCategory.PEACE, "সূরা আল-হাশর ৫৯:২৩"
    ),
    AllahName(
      5, "السَّلَامُ", "আস-সালাম", "As-Salaam",
      "শান্তি প্রদানকারী", "The Giver of Peace",
      "যিনি সকল ত্রুটিমুক্ত এবং সৃষ্টিকে শান্তি ও নিরাপত্তা দানকারী।",
      NameCategory.PEACE, "সূরা আল-হাশর ৫৯:২৩"
    ),
    AllahName(
      6, "الْمُؤْمِنُ", "আল-মুমিন", "Al-Mu'min",
      "নিরাপত্তা ও ঈমান দাতা", "The Granter of Security",
      "যিনি বান্দাকে ভয়ভীতি থেকে নিরাপত্তা ও নির্ভরতা দেন।",
      NameCategory.PEACE, "সূরা আল-হাশর ৫৯:২৩"
    ),
    AllahName(
      7, "الْمُهَيْمِنُ", "আল-মুহাইমিন", "Al-Muhaymin",
      "রক্ষক ও পর্যবেক্ষণকারী", "The Guardian",
      "যিনি সবকিছু সংরক্ষণ, রক্ষা ও তত্ত্বাবধান করেন।",
      NameCategory.POWER, "সূরা আল-হাশর ৫৯:২৩"
    ),
    AllahName(
      8, "الْعَزِيزُ", "আল-আজিজ", "Al-Azeez",
      "পরাক্রমশালী ও অপ্রতিরোধ্য", "The Almighty",
      "যাঁকে কেউ পরাজিত করতে পারে না, অজেয় ও পরাক্রান্ত।",
      NameCategory.POWER, "সূরা আল-হাশর ৫৯:২৩"
    ),
    AllahName(
      9, "الْجَبَّارُ", "আল-জাব্বার", "Al-Jabbaar",
      "মহাপ্রতাপশালী / সংশোধনকারী", "The Compeller / Restorer",
      "যিনি ভাঙা হৃদয়ে শান্তি আনেন এবং অপ্রতিহত ক্ষমতার অধিকারী।",
      NameCategory.POWER, "সূরা আল-হাশর ৫৯:২৩"
    ),
    AllahName(
      10, "الْمُتَكَبِّرُ", "আল-মুতাকাব্বির", "Al-Mutakabbir",
      "সর্বশ্রেষ্ঠ ও অহংকারের প্রকৃত অধিকারী", "The Supreme",
      "সকল শ্রেষ্ঠত্ব ও মহিমা যাঁর প্রাপ্য, তিনিই একমাত্র গর্বিত।",
      NameCategory.POWER, "সূরা আল-হাশর ৫৯:২৩"
    ),
    AllahName(
      11, "الْخَالِقُ", "আল-খালিক", "Al-Khaaliq",
      "সৃষ্টিকর্তা", "The Creator",
      "যিনি কোনো পূর্ব নমুনা ছাড়া অনস্তিত্ব থেকে অস্তিত্ব দান করেছেন।",
      NameCategory.CREATION, "সূরা আল-হাশর ৫৯:২৪"
    ),
    AllahName(
      12, "الْبَارِئُ", "আল-বারী", "Al-Baari'",
      "উদ্ভাবক ও রূপদাতা", "The Evolver",
      "যিনি কোনো ত্রুটি ছাড়া নিখুঁতভাবে সবকিছু অস্তিত্বে আনেন।",
      NameCategory.CREATION, "সূরা আল-হাশর ৫৯:২৪"
    ),
    AllahName(
      13, "الْمُصَوِّرُ", "আল-মুসাওয়ির", "Al-Musawwir",
      "আকৃতি দানকারী", "The Fashioner",
      "যিনি প্রতিটি সৃষ্টিকে সুন্দরতম আকৃতি ও রূপ দিয়েছেন।",
      NameCategory.CREATION, "সূরা আল-হাশর ৫৯:২৪"
    ),
    AllahName(
      14, "الْغَفَّارُ", "আল-গাফফার", "Al-Ghaffaar",
      "বারবার ক্ষমাকারী", "The All-Forgiving",
      "যিনি বান্দার বারবার পাপ সত্ত্বেও বারবার ক্ষমা করে দেন।",
      NameCategory.FORGIVENESS, "সূরা নূহ ৭১:১০"
    ),
    AllahName(
      15, "الْقَهَّارُ", "আল-কাহ্হার", "Al-Qahhaar",
      "মহাদমনকারী", "The Subduer",
      "সমগ্র বিশ্ব যাঁর নির্দেশের সামনে অবনত ও বশীভূত।",
      NameCategory.POWER, "সূরা আর-রাদ ১৩:১৬"
    ),
    AllahName(
      16, "الْوَهَّابُ", "আল-ওয়াহ্হাব", "Al-Wahhaab",
      "মহাদানশীল", "The Bestower",
      "যিনি কোনো প্রতিদান বা শর্ত ছাড়াই অকাতরে অনুগ্রহ দান করেন।",
      NameCategory.MERCY, "সূরা আল-ইমরান ৩:৮"
    ),
    AllahName(
      17, "الرَّزَّاقُ", "আর-রাজ্জাক", "Ar-Razzaaq",
      "রিজিকদাতা", "The Provider",
      "যিনি সমস্ত জীবের খাদ্য, জীবনোপকরণ ও আত্মিক রিযিক যোগান।",
      NameCategory.CREATION, "সূরা আয-যারিয়াত ৫১:৫৮"
    ),
    AllahName(
      18, "الْفَتَّاحُ", "আল-ফাত্তাহ", "Al-Fattaah",
      "উন্মোচনকারী / বিজয়দাতা", "The Opener",
      "যিনি রহমতের দুয়ার, সাফল্যের পথ ও সত্যের দরজা খুলে দেন।",
      NameCategory.MERCY, "সূরা সাবা ৩৪:২৬"
    ),
    AllahName(
      19, "الْعَلِيمُ", "আল-আলীম", "Al-'Aleem",
      "সর্বজ্ঞ", "The All-Knowing",
      "যাঁর কাছে প্রকাশ্য ও গোপন, অতীত ও ভবিষ্যৎ কিছুই লুকায়িত নয়।",
      NameCategory.KNOWLEDGE, "সূরা আল-বাকারা ২:২৯"
    ),
    AllahName(
      20, "الْقَابِضُ", "আল-কাবিদ", "Al-Qaabid",
      "সংকোচনকারী", "The Withholder",
      "যিনি নিজ প্রজ্ঞায় রিযিক ও জীবনকে সংকুচিত বা সীমিত করেন।",
      NameCategory.POWER, "সূরা আল-বাকারা ২:২৪৫"
    ),
    AllahName(
      21, "الْبَاسِطُ", "আল-বাসিত", "Al-Baasit",
      "সম্প্রসারণকারী", "The Expander",
      "যিনি অসীম উদারতায় রিযিক ও হৃদয়কে প্রশস্ত করে দেন।",
      NameCategory.MERCY, "সূরা আল-বাকারা ২:২৪৫"
    ),
    AllahName(
      22, "الْخَافِضُ", "আল-খাফিদ", "Al-Khaafid",
      "অবনমনকারী", "The Abaser",
      "যিনি অহংকারী ও উদ্ধতদের মর্যাদা ধূলিসাৎ করে দেন।",
      NameCategory.POWER, "হাদীস শরিফ"
    ),
    AllahName(
      23, "الرَّافِعُ", "আর-রাফি", "Ar-Raafi'",
      "উন্নীতকারী", "The Exalter",
      "যিনি বিনীত ও বিশ্বাসীদের মর্যাদা উচ্চতায় সমুন্নত করেন।",
      NameCategory.POWER, "সূরা আল-মুজাদালাহ ৫৮:১১"
    ),
    AllahName(
      24, "الْمُعِزُّ", "আল-মুইজ", "Al-Mu'izz",
      "সম্মানদাতা", "The Bestower of Honor",
      "যিনি যাকে ইচ্ছা সম্মান ও মর্যাদা দান করেন।",
      NameCategory.POWER, "সূরা আল-ইমরান ৩:২৬"
    ),
    AllahName(
      25, "الْمُذِلُّ", "আল-মুজিল্ল", "Al-Muzill",
      "অপমানকারী", "The Humiliator",
      "যিনি অবাধ্য ও অহংকারীদের অপদস্থ ও লাঞ্ছিত করেন।",
      NameCategory.POWER, "সূরা আল-ইমরান ৩:২৬"
    ),
    AllahName(
      26, "السَّمِيعُ", "আস-সামী", "As-Samee'",
      "সর্বশ্রোতা", "The All-Hearing",
      "যিনি হৃদয়ের গোপনতম ফিসফিসানি ও নিস্তব্ধ দোয়াও শোনেন।",
      NameCategory.KNOWLEDGE, "সূরা আল-বাকারা ২:১২৭"
    ),
    AllahName(
      27, "الْبَصِيرُ", "আল-বাসির", "Al-Baseer",
      "সর্বদ্রষ্টা", "The All-Seeing",
      "যিনি অন্ধকারের কালো পাথরের উপর চলা কালো পিঁপড়াকেও দেখেন।",
      NameCategory.KNOWLEDGE, "সূরা আশ-শূরা ৪২:১১"
    ),
    AllahName(
      28, "الْحَكَمُ", "আল-হাকাম", "Al-Hakam",
      "চূড়ান্ত বিচারক", "The Impartial Judge",
      "যাঁর ফয়সালা অবিসংবাদিত এবং ন্যায়বিচারে কোনো ত্রুটি নেই।",
      NameCategory.KNOWLEDGE, "সূরা আল-আনআম ৬:১১৪"
    ),
    AllahName(
      29, "الْعَدْلُ", "আল-আদল", "Al-'Adl",
      "পরম ন্যায়পরায়ণ", "The Utterly Just",
      "যিনি কারও প্রতি অণুমাত্র অন্যায় বা অবিচার করেন না।",
      NameCategory.PEACE, "সূরা আল-আনআম ৬:১১৫"
    ),
    AllahName(
      30, "اللَّطِيفُ", "আল-লাতীফ", "Al-Lateef",
      "সূক্ষ্মদর্শী ও স্নেহময়", "The Subtle & Kind",
      "যিনি অসীম স্নেহে অলক্ষ্যে বান্দার কল্যাণসাধন করেন।",
      NameCategory.MERCY, "সূরা আল-মুলক ৬৭:১৪"
    ),
    AllahName(
      31, "الْخَبِيرُ", "আল-খাবীর", "Al-Khabeer",
      "সর্বজ্ঞাত ও সর্বসন্ধানী", "The All-Aware",
      "যিনি সমস্ত কিছুর অভ্যন্তরীণ সত্য ও গুহ্য তথ্য জানেন।",
      NameCategory.KNOWLEDGE, "সূরা আল-হুজুরাত ৪৯:১৩"
    ),
    AllahName(
      32, "الْحَلِيمُ", "আল-হালীম", "Al-Haleem",
      "ধৈর্যশীল ও পরমতসহিষ্ণু", "The Most Forbearing",
      "যিনি পাপ দেখেও সঙ্গে সঙ্গে শাস্তি দেন না, তাওবার সুযোগ দেন।",
      NameCategory.FORGIVENESS, "সূরা আল-বাকারা ২:২২৫"
    ),
    AllahName(
      33, "الْعَظِيمُ", "আল-আজীম", "Al-'Azeem",
      "মহিমান্বিত", "The Magnificent",
      "যাঁর মহিমা ও বিশালতার কোনো সীমা বা কল্পনা নেই।",
      NameCategory.POWER, "সূরা আল-বাকারা ২:২৫৫"
    ),
    AllahName(
      34, "الْغَفُورُ", "আল-গাফুর", "Al-Ghafoor",
      "পরম ক্ষমাশীল", "The Great Forgiver",
      "যিনি পাপ ক্ষমা করেন এবং তা অন্যদের দৃষ্টি থেকেও ঢেকে রাখেন।",
      NameCategory.FORGIVENESS, "সূরা আল-বাকারা ২:১৭৩"
    ),
    AllahName(
      35, "الشَّكُورُ", "আশ-শাকূর", "Ash-Shakoor",
      "কৃতজ্ঞতা সমাদরকারী", "The Most Appreciative",
      "যিনি বান্দার সামান্য নেক আমলের বিপুল ও অসীম সওয়াব দেন।",
      NameCategory.MERCY, "সূরা ফাতির ৩৫:৩০"
    ),
    AllahName(
      36, "الْعَلِيُّ", "আল-আলী", "Al-'Aliyy",
      "সর্বোচ্চ", "The Most High",
      "যাঁর অবস্থান ও মর্যাদা সবকিছুর ঊর্ধ্বে।",
      NameCategory.POWER, "সূরা আল-বাকারা ২:২৫৫"
    ),
    AllahName(
      37, "الْكَبِيرُ", "আল-কবীর", "Al-Kabeer",
      "সর্বশ্রেষ্ঠ", "The Most Great",
      "যাঁর চেয়ে বড় কোনো কিছুই অস্তিত্বে থাকতে পারে না।",
      NameCategory.POWER, "সূরা আর-রাদ ১৩:৯"
    ),
    AllahName(
      38, "الْحَفِيظُ", "আল-হাফীজ", "Al-Hafeez",
      "মহারক্ষক", "The Preserver",
      "যিনি গোটা সৃষ্টিজগত ও আমলসমূহকে ধ্বংস ও বিস্মৃতি থেকে রক্ষা করেন।",
      NameCategory.PEACE, "সূরা হুদ ১১:৫৭"
    ),
    AllahName(
      39, "الْمُقِيتُ", "আল-মুকীত", "Al-Muqeet",
      "পুষ্টিদাতা ও সামর্থ্যদাতা", "The Sustainer",
      "যিনি সমস্ত সৃষ্টিকে টিকে থাকার শক্তি ও শক্তি যোগান।",
      NameCategory.CREATION, "সূরা আন-নিসা ৪:৮৫"
    ),
    AllahName(
      40, "الْحَسِيبُ", "আল-হাসীব", "Al-Haseeb",
      "হিসাব গ্রহণকারী / যথেষ্টকারী", "The Reckoner",
      "যিনি বান্দার সকল আমলের হিসাব রাখেন এবং একাই বান্দার জন্য যথেষ্ট।",
      NameCategory.KNOWLEDGE, "সূরা আন-নিসা ৪:৬"
    ),
    AllahName(
      41, "الْجَلِيلُ", "আল-জালীল", "Al-Jaleel",
      "মহিমান্বিত ও প্রভাবশালী", "The Majestic",
      "যিনি অতুলনীয় প্রতাপ ও মহত্বের অধিকারী।",
      NameCategory.POWER, "সূরা আর-রহমান ৫৫:২৭"
    ),
    AllahName(
      42, "الْكَرِيمُ", "আল-কারীম", "Al-Kareem",
      "পরম দাতা ও মহানুভব", "The Most Generous",
      "যিনি অবারিত দান করেন এবং অপরাধীদের উদারভাবে ক্ষমা করেন।",
      NameCategory.MERCY, "সূরা আল-ইনফিতার ৮২:৬"
    ),
    AllahName(
      43, "الرَّقِيبُ", "আর-রাকীব", "Ar-Raqeeb",
      "সতর্ক দৃষ্টিদানকারী", "The Watchful",
      "যিনি সবসময় প্রতিটি মুহূর্ত ও অনুভূতি পর্যবেক্ষণ করেন।",
      NameCategory.KNOWLEDGE, "সূরা আন-নিসা ৪:১"
    ),
    AllahName(
      44, "الْمُجِيبُ", "আল-মুজিব", "Al-Mujeeb",
      "দোয়া কবুলকারী", "The Responsive",
      "যিনি ডাকলে বান্দার আহ্বানে সাড়া দেন ও ফরিয়াদ শোনেন।",
      NameCategory.MERCY, "সূরা হুদ ১১:৬১"
    ),
    AllahName(
      45, "الْوَاسِعُ", "আল-ওয়াসি", "Al-Waasi'",
      "সর্বব্যাপী ও অসীম", "The All-Encompassing",
      "যাঁর রিযিক, ক্ষমা ও রহমত সমগ্র সৃষ্টিকে পরিবেষ্টন করে আছে।",
      NameCategory.MERCY, "সূরা আল-বাকারা ২:১১৫"
    ),
    AllahName(
      46, "الْحَكِيمُ", "আল-হাকীম", "Al-Hakeem",
      "পরম প্রজ্ঞাময়", "The All-Wise",
      "যাঁর প্রতিটি সিদ্ধান্তে রয়েছে গভীরতম জ্ঞান ও কল্যাণ।",
      NameCategory.KNOWLEDGE, "সূরা আল-বাকারা ২:১২৯"
    ),
    AllahName(
      47, "الْوَدُودُ", "আল-ওয়াদূদ", "Al-Wadood",
      "প্রেমময় ও ভালোবাসার আধার", "The Most Loving",
      "যিনি নিজ বান্দাদের ভালোবাসেন এবং বান্দারাও তাঁকে ভালোবাসে।",
      NameCategory.MERCY, "সূরা আল-বুরুজ ৮৫:১৪"
    ),
    AllahName(
      48, "الْمَجِيدُ", "আল-মাজীদ", "Al-Majeed",
      "মহাগৌরবময়", "The Glorious",
      "যাঁর মহিমা ও সম্মান শাশ্বত ও সর্বোত্তম।",
      NameCategory.POWER, "সূরা হুদ ১১:৭৩"
    ),
    AllahName(
      49, "الْبَاعِثُ", "আল-বাইস", "Al-Baa'ith",
      "পুনরুজ্জীবনকারী", "The Resurrector",
      "যিনি মৃত্যুর পর কিয়ামতের দিন সকলকে পুনরায় জীবিত করবেন।",
      NameCategory.POWER, "সূরা আল-হাজ্জ ২২:৭"
    ),
    AllahName(
      50, "الشَّهِيدُ", "আশ-শাহীদ", "Ash-Shaheed",
      "প্রত্যক্ষ সাক্ষী", "The All-Witnessing",
      "যাঁর সামনে পৃথিবীর সবকিছু স্পষ্ট সাক্ষ্যরূপে বিদ্যমান।",
      NameCategory.KNOWLEDGE, "সূরা আল-মায়েদা ৫:১১৭"
    ),
    AllahName(
      51, "الْحَقُّ", "আল-হাক্ক", "Al-Haqq",
      "পরম সত্য", "The Absolute Truth",
      "যাঁর অস্তিত্ব চিরন্তন ও অবিসংবাদিত বাস্তব।",
      NameCategory.PEACE, "সূরা আল-হাজ্জ ২২:৬"
    ),
    AllahName(
      52, "الْوَكِيلُ", "আল-ওয়াকীল", "Al-Wakeel",
      "পরম কর্মবিধায়ক", "The Trustee",
      "যাঁর ওপর ভরসা করলে কোনো উদ্বেগের কারণ থাকে না।",
      NameCategory.PEACE, "সূরা আল-ইমরান ৩:১৭৩"
    ),
    AllahName(
      53, "الْقَوِيُّ", "আল-কাউই", "Al-Qawiyy",
      "মহাশক্তিধর", "The All-Strong",
      "যাঁর শক্তিতে কখনও ক্লান্তি বা দুর্বলতা আসে না।",
      NameCategory.POWER, "সূরা আল-হাজ্জ ২২:৪০"
    ),
    AllahName(
      54, "الْمَتِينُ", "আল-মাতীন", "Al-Mateen",
      "দৃঢ় ও অটল", "The Firm",
      "যাঁর কর্তৃত্ব ও শক্তিতে কোনো রদবদল অসম্ভব।",
      NameCategory.POWER, "সূরা আয-যারিয়াত ৫১:৫৮"
    ),
    AllahName(
      55, "الْوَلِيُّ", "আল-ওয়ালী", "Al-Waliyy",
      "পরম বন্ধু ও অভিভাবক", "The Protecting Friend",
      "যিনি বিশ্বাসীদের পথ দেখান এবং অন্ধকার থেকে আলোতে আনেন।",
      NameCategory.MERCY, "সূরা আল-বাকারা ২:২৫৭"
    ),
    AllahName(
      56, "الْحَمِيدُ", "আল-হামীদ", "Al-Hameed",
      "চিরপ্রশংসিত", "The Praiseworthy",
      "যিনি সব প্রশংসা ও গুণকীর্তনের একমাত্র যোগ্য আধার।",
      NameCategory.PEACE, "সূরা ইব্রাহিম ১৪:১"
    ),
    AllahName(
      57, "الْمُحْصِي", "আল-মুহসী", "Al-Muhsee",
      "গণনাকারী", "The Appraiser",
      "যিনি প্রতিটি ধূলিকণা ও সেকেন্ডের নির্ভুল হিসাব জানেন।",
      NameCategory.KNOWLEDGE, "সূরা মারিয়াম ১৯:৯৪"
    ),
    AllahName(
      58, "الْمُبْدِئُ", "আল-মুবদি", "Al-Mubdi'",
      "আদি সৃষ্টিকর্তা", "The Originator",
      "যিনি শূন্য থেকে সর্বপ্রথম সৃষ্টির সূচনা করেছেন।",
      NameCategory.CREATION, "সূরা আল-বুরুজ ৮৫:১৩"
    ),
    AllahName(
      59, "الْمُعِيدُ", "আল-মুঈদ", "Al-Mu'eed",
      "পুনরাবৃত্তিকারী", "The Restorer",
      "যিনি ধ্বংসের পর পুনরায় সৃষ্টিকে ফিরিয়ে আনতে সক্ষম।",
      NameCategory.CREATION, "সূরা আল-বুরুজ ৮৫:১৩"
    ),
    AllahName(
      60, "الْمُحْيِي", "আল-মুহয়ী", "Al-Muhyee",
      "জীবনদাতা", "The Giver of Life",
      "যিনি নির্জীব দেহে আত্মা ফুঁকে জীবন দান করেন।",
      NameCategory.CREATION, "সূরা আর-রুম ৩০:৫০"
    ),
    AllahName(
      61, "الْمُمِيتُ", "আল-মুমীত", "Al-Mumeet",
      "মৃত্যুদাতা", "The Creator of Death",
      "যাঁর হুকুমে প্রতিটি প্রাণীকে মৃত্যুর স্বাদ গ্রহণ করতে হয়।",
      NameCategory.POWER, "সূরা আল-ইমরান ৩:১৫৬"
    ),
    AllahName(
      62, "الْحَيُّ", "আল-হাইয়্য", "Al-Hayy",
      "চিরঞ্জীব", "The Ever-Living",
      "যাঁর কোনো শুরু নেই এবং যাঁর কোনো মৃত্যু বা সমাপ্তি নেই।",
      NameCategory.PEACE, "সূরা আল-বাকারা ২:২৫৫"
    ),
    AllahName(
      63, "الْقَيُّومُ", "আল-কাইয়ূম", "Al-Qayyoom",
      "স্বয়ংসম্পূর্ণ ও সর্বধারক", "The Self-Sustaining",
      "যিনি নিজে স্বনির্ভর এবং সমগ্র মহাবিশ্বকে ধারণ করে আছেন।",
      NameCategory.POWER, "সূরা আল-বাকারা ২:২৫৫"
    ),
    AllahName(
      64, "الْوَاجِدُ", "আল-ওয়াজিদ", "Al-Waajid",
      "অভাবমুক্ত ও সর্বপ্রাপ্ত", "The Finder",
      "যাঁর কোনো কিছুরই অভাব নেই এবং যা চান তা-ই পান।",
      NameCategory.POWER, "হাদীস শরিফ"
    ),
    AllahName(
      65, "الْمَاجِدُ", "আল-মাজিদ", "Al-Maajid",
      "মহান ও মহাসম্মানিত", "The Noble",
      "যাঁর মর্যাদা ও বদান্যতা অতুলনীয়।",
      NameCategory.POWER, "হাদীস শরিফ"
    ),
    AllahName(
      66, "الْوَاحِدُ", "আল-ওয়াহিদ", "Al-Waahid",
      "এক ও অদ্বিতীয়", "The Unique One",
      "যাঁর সত্ত্বা ও গুণে কোনো সমকক্ষ বা অংশীদার নেই।",
      NameCategory.PEACE, "সূরা আল-ইখলাস ১১২:১"
    ),
    AllahName(
      67, "الْأَحَدُ", "আল-আহাদ", "Al-Ahad",
      "একক সত্তা", "The Indivisible",
      "যিনি অবিভাজ্য ও একক চিরন্তন প্রভু।",
      NameCategory.PEACE, "সূরা আল-ইখলাস ১১২:১"
    ),
    AllahName(
      68, "الصَّمَدُ", "আস-সামাদ", "As-Samad",
      "অমুখাপেক্ষী", "The Eternal Refuge",
      "সকলেই যাঁর মুখাপেক্ষী কিন্তু তিনি কারও মুখাপেক্ষী নন।",
      NameCategory.PEACE, "সূরা আল-ইখলাস ১১২:২"
    ),
    AllahName(
      69, "الْقَادِرُ", "আল-কাদির", "Al-Qaadir",
      "সর্বশক্তিমান", "The Capable",
      "যিনি যা ইচ্ছা তা করতে পুরোপুরি সক্ষম।",
      NameCategory.POWER, "সূরা আল-আনআম ৬:৬৫"
    ),
    AllahName(
      70, "الْمُقْتَدِرُ", "আল-মুকতাদির", "Al-Muqtadir",
      "প্রবল প্রভাবশালী ও নিয়ন্ত্রক", "The Omnipotent",
      "যাঁর কুদরতের সামনে কোনো প্রতিবন্ধকতা দাঁড়াতে পারে না।",
      NameCategory.POWER, "সূরা আল-ক্বামার ৫৪:৪২"
    ),
    AllahName(
      71, "الْمُقَدِّمُ", "আল-মুকাদ্দিম", "Al-Muqaddim",
      "অগ্রবর্তীকারী", "The Expediter",
      "যিনি প্রজ্ঞার সাথে যাকে ইচ্ছা এগিয়ে দেন।",
      NameCategory.KNOWLEDGE, "সূরা কাফ ৫০:২৮"
    ),
    AllahName(
      72, "الْمُؤَخِّرُ", "আল-মুআখ্খির", "Al-Mu'akhkhir",
      "পশ্চাদ্বর্তীকারী", "The Delayer",
      "যিনি নিজ ইচ্ছায় যাকে ইচ্ছা পিছিয়ে দেন বা অবকাশ দেন।",
      NameCategory.KNOWLEDGE, "সূরা ইব্রাহিম ১৪:৪২"
    ),
    AllahName(
      73, "الْأَوَّلُ", "আল-আউয়াল", "Al-Awwal",
      "অনাদি", "The First",
      "যার পূর্বে কোনো কিছুরই অস্তিত্ব ছিল না।",
      NameCategory.PEACE, "সূরা আল-হাদীদ ৫৭:৩"
    ),
    AllahName(
      74, "الْآخِرُ", "আল-আখির", "Al-Aakhir",
      "অনন্ত", "The Last",
      "সবকিছু ধ্বংসের পরও যিনি চিরকাল থাকবেন।",
      NameCategory.PEACE, "সূরা আল-হাদীদ ৫৭:৩"
    ),
    AllahName(
      75, "الظَّاهِرُ", "আজ-জাহির", "Az-Zaahir",
      "সুপ্রকাশিত", "The Manifest",
      "যাঁর সৃষ্টির নিদর্শনাবলি সর্বত্র স্পষ্ট ও দৃশ্যমান।",
      NameCategory.KNOWLEDGE, "সূরা আল-হাদীদ ৫৭:৩"
    ),
    AllahName(
      76, "الْبَاطِنُ", "আল-বাতিন", "Al-Baatin",
      "গুপ্ত ও গুহ্য", "The Hidden",
      "যিনি মানবচক্ষুর অন্তরালে তবে আত্মায় অনুভূত।",
      NameCategory.KNOWLEDGE, "সূরা আল-হাদীদ ৫৭:৩"
    ),
    AllahName(
      77, "الْوَالِي", "আল-ওয়ালী", "Al-Waali",
      "সার্বিক তত্ত্বাবধায়ক", "The Governor",
      "যিনি সমস্ত জগতের পরিচালক ও অধিপতি।",
      NameCategory.POWER, "সূরা আর-রাদ ১৩:১১"
    ),
    AllahName(
      78, "الْمُتَعَالِي", "আল-মুতাআলী", "Al-Muta'aali",
      "সুউচ্চ ও শ্রেষ্ঠতম", "The Supreme Exalted",
      "সৃষ্টির সমস্ত কল্পনার চেয়েও যিনি বহুগুণ উচ্চতায়।",
      NameCategory.POWER, "সূরা আর-রাদ ১৩:৯"
    ),
    AllahName(
      79, "الْبَرُّ", "আল-বার্", "Al-Barr",
      "পরম কল্যাণময় ও উপকারী", "The Source of Goodness",
      "যিনি সৃষ্টির প্রতি সর্বদা অনুগ্রহশীল ও মঙ্গলময়।",
      NameCategory.MERCY, "সূরা আত-তূর ৫২:২৮"
    ),
    AllahName(
      80, "التَّوَّابُ", "আত-তাওয়াব", "At-Tawwaab",
      "তাওবা কবুলকারী", "The Acceptor of Repentance",
      "যিনি বারে বারে বান্দার অনুশোচনা গ্রহণ করেন।",
      NameCategory.FORGIVENESS, "সূরা আন-নূর ২৪:১০"
    ),
    AllahName(
      81, "الْمُنْتَقِمُ", "আল-মুনতাকিম", "Al-Muntaqim",
      "প্রতিশোধ গ্রহণকারী", "The Avenger",
      "যিনি জালিম ও নিপীড়কদের উপযুক্ত শাস্তি দেন।",
      NameCategory.POWER, "সূরা আস-সাজদাহ ৩২:২২"
    ),
    AllahName(
      82, "الْعَفُوُّ", "আল-আফুউ", "Al-'Afuww",
      "পরম মার্জনাশীল", "The Pardoner",
      "যিনি গুনাহ সম্পূর্ণ মুছে দিয়ে রেকর্ডহীন করে দেন।",
      NameCategory.FORGIVENESS, "সূরা আন-নিসা ৪:৯৯"
    ),
    AllahName(
      83, "الرَّءُوفُ", "আর-রাউফ", "Ar-Ra'oof",
      "স্নেহপরায়ণ ও সদয়", "The Most Kind",
      "যাঁর অন্তরে বান্দার জন্য রয়েছে সুগভীর কোমলতা।",
      NameCategory.MERCY, "সূরা আল-বাকারা ২:১৪৩"
    ),
    AllahName(
      84, "مَالِكُ الْمُلْكِ", "মালিকুল মুলক", "Maalik-ul-Mulk",
      "রাজাধিরাজ", "Owner of all Sovereignty",
      "সমগ্র বিশ্বব্রহ্মাণ্ডের সার্বভৌম রাজত্ব যাঁর হাতে।",
      NameCategory.POWER, "সূরা আল-ইমরান ৩:২৬"
    ),
    AllahName(
      85, "ذُو الْجَلَالِ وَالْإِكْرَامِ", "যুল-জালালি ওয়াল-ইকরাম", "Zul-Jalaali wal-Ikraam",
      "মহিমা ও সম্মানের অধিকারী", "Lord of Glory & Honor",
      "যিনি সকল মর্যাদা, শ্রেষ্ঠত্ব ও সম্মানের মালিক।",
      NameCategory.POWER, "সূরা আর-রহমান ৫৫:৭৮"
    ),
    AllahName(
      86, "الْمُقْسِطُ", "আল-মুকসিত", "Al-Muqsit",
      "ন্যায়বিচারক", "The Equitable",
      "যিনি মজলুমের অধিকার জালেমের কাছ থেকে উদ্ধার করেন।",
      NameCategory.PEACE, "সূরা আল-ইমরান ৩:১৮"
    ),
    AllahName(
      87, "الْجَامِعُ", "আল-জামি", "Al-Jaami'",
      "সমাবেশকারী", "The Gatherer",
      "যিনি কিয়ামতের মাঠে সমগ্র মানবজাতিকে একত্র করবেন।",
      NameCategory.POWER, "সূরা আল-ইমরান ৩:৯"
    ),
    AllahName(
      88, "الْغَنِيُّ", "আল-গানী", "Al-Ghaniyy",
      "স্বয়ংসম্পূর্ণ ধনাঢ্য", "The Self-Sufficient",
      "যাঁর কোনো কিছুর প্রয়োজন নেই, সবাই যাঁর ভিখারী।",
      NameCategory.PEACE, "সূরা আল-বাকারা ২:২৬৭"
    ),
    AllahName(
      89, "الْمُغْنِي", "আল-মুগনী", "Al-Mughnee",
      "ধনীকারী", "The Enricher",
      "যিনি যাকে ইচ্ছা বিপুল সম্পদ ও আত্মিক তৃপ্তি দান করেন।",
      NameCategory.MERCY, "সূরা আন-নাজম ৫৩:৪৮"
    ),
    AllahName(
      90, "الْمَانِعُ", "আল-মানি", "Al-Maani'",
      "প্রতিরোধকারী", "The Preventer",
      "যিনি অনিষ্ট হতে বান্দাকে রক্ষা করেন ও ক্ষতি রোধ করেন।",
      NameCategory.POWER, "হাদীস শরিফ"
    ),
    AllahName(
      91, "الضَّارُّ", "আদ-দার্", "Ad-Daarr",
      "ক্ষতি সাধনকারী", "The Distressor",
      "যাঁর অনুমতি ছাড়া কারও কোনো ক্ষতি করার সাধ্য নেই।",
      NameCategory.POWER, "সূরা আল-আনআম ৬:১৭"
    ),
    AllahName(
      92, "النَّافِعُ", "আন-নাফি", "An-Naafi'",
      "কল্যাণকারী", "The Propitious",
      "সকল উপকার ও অনুগ্রহ একমাত্র যাঁর পক্ষ থেকেই আসে।",
      NameCategory.MERCY, "সূরা আল-আনআম ৬:১৭"
    ),
    AllahName(
      93, "النُّورُ", "আন-নূর", "An-Noor",
      "জ্যোতির্ময়", "The Light",
      "যিনি আকাশ ও পৃথিবীর জ্যোতি এবং হেদায়েতের আলো।",
      NameCategory.PEACE, "সূরা আন-নূর ২৪:৩৫"
    ),
    AllahName(
      94, "الْهَادِي", "আল-হাদী", "Al-Haadi",
      "পথপ্রদর্শক", "The Guide",
      "যিনি পথভোলা মানুষকে সরল সঠিক পথে পরিচালনা করেন।",
      NameCategory.MERCY, "সূরা আল-হাজ্জ ২২:৫৪"
    ),
    AllahName(
      95, "الْبَدِيعُ", "আল-বাদী", "Al-Badee'",
      "অনুপম উদ্ভাবক", "The Incomparable Originator",
      "যিনি কোনো দৃষ্টান্ত ছাড়াই অপূর্ব সুন্দর সৃষ্টি উদ্ভাবন করেছেন।",
      NameCategory.CREATION, "সূরা আল-বাকারা ২:১১৭"
    ),
    AllahName(
      96, "الْبَاقِي", "আল-বাকী", "Al-Baaqee",
      "চিরস্থায়ী", "The Everlasting",
      "সবকিছু লয়প্রাপ্ত হলেও যিনি অনন্তকাল টিকে থাকবেন।",
      NameCategory.PEACE, "সূরা আর-রহমান ৫৫:২৭"
    ),
    AllahName(
      97, "الْوَارِثُ", "আল-ওয়ারিস", "Al-Waarith",
      "পরম উত্তরাধিকারী", "The Ultimate Inheritor",
      "মহাবিশ্বের বিলুপ্তির পর সমস্ত কিছুর একমাত্র অধিকারী।",
      NameCategory.POWER, "সূরা আল-হিজর ১৫:২৩"
    ),
    AllahName(
      98, "الرَّشِيدُ", "আর-রাশীদ", "Ar-Rasheed",
      "সঠিক পথনির্দেশক ও প্রজ্ঞাময়", "The Righteous Teacher",
      "যাঁর সমস্ত ব্যবস্থাপনা সঠিক ও কল্যাণপ্রসূত।",
      NameCategory.KNOWLEDGE, "সূরা আল-হুদ ১১:৮৭"
    ),
    AllahName(
      99, "الصَّبُورُ", "আস-সবূর", "As-Saboor",
      "পরম ধৈর্যশীল", "The Most Patient",
      "যিনি সীমাহীন ধৈর্য্যের সাথে সৃষ্টিকে পরিচালনা করেন।",
      NameCategory.PEACE, "হাদীস শরিফ"
    )
  )
}
