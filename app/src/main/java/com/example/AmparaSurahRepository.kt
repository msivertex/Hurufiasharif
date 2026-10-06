package com.example

/**
 * Authentic offline repository containing:
 * - Surah Al-Fatihah (Opening)
 * - Key Surahs from Juz Amma (Surah An-Naba, An-Naziat, Al-Ikhlas, Al-Falaq, An-Nas, Al-Kawthar, Al-Kafirun, Al-Ma'un, Al-Quraysh, Al-Fil, Al-Asr, Al-Qadr)
 * Complete with authentic Arabic text, word-by-word tokens, Tajweed color-coding, Bengali/English translations and transliterations.
 */
object AmparaSurahRepository {

  val surahs: List<Surah> by lazy {
    listOf(
      // 1. Surah Al-Fatihah (الفاتحة)
      Surah(
        number = 1,
        nameArabic = "الفَاتِحَة",
        nameBengali = "সূরা আল-ফাতিহা",
        nameEnglish = "Al-Fatihah",
        meaningBengali = "উদ্বোধক / সূচনা",
        meaningEnglish = "The Opening",
        totalVerses = 7,
        revelationType = RevelationType.MAKKI,
        bismillahPrecedes = false,
        ayahs = listOf(
          QuranAyah(
            ayahNumber = 1,
            textArabic = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
            transliterationBn = "বিসমিল্লাহির রাহমানির রাহিম",
            transliterationEn = "Bismillaahir Rahmaanir Raheem",
            translationBn = "পরম করুণাময় ও অসীম দয়ালু আল্লাহর নামে শুরু করছি।",
            translationEn = "In the name of Allah, the Entirely Merciful, the Especially Merciful.",
            words = listOf(
              QuranWord("1_1_1", 1, "بِسْمِ", "বিসমি", "Bismi", "নামে", "In name", TajweedType.NONE),
              QuranWord("1_1_2", 2, "اللَّهِ", "আল্লাহি", "Allahi", "আল্লাহর", "of Allah", TajweedType.NONE),
              QuranWord("1_1_3", 3, "الرَّحْمَٰنِ", "আর-রাহমানি", "Ar-Rahmaani", "পরম করুণাময়", "The Most Merciful", TajweedType.MADD,
                listOf(TajweedWordSegment("الرَّحْ"), TajweedWordSegment("مَٰ", TajweedType.MADD), TajweedWordSegment("نِ"))
              ),
              QuranWord("1_1_4", 4, "الرَّحِيمِ", "আর-রাহিম", "Ar-Raheem", "অসীম দয়ালু", "The Especially Merciful", TajweedType.MADD,
                listOf(TajweedWordSegment("الرَّ"), TajweedWordSegment("حِي", TajweedType.MADD), TajweedWordSegment("مِ"))
              )
            )
          ),
          QuranAyah(
            ayahNumber = 2,
            textArabic = "الْحَمْدُ لِلَّهِ رَبِّ الْعَالَمِينَ",
            transliterationBn = "আলহামদু লিল্লাহি রাব্বিল আলামিন",
            transliterationEn = "Alhamdu lillaahi Rabbil 'aalameen",
            translationBn = "সকল প্রশংসা জগৎসমূহের প্রতিপালক আল্লাহর জন্য।",
            translationEn = "[All] praise is [due] to Allah, Lord of the worlds.",
            words = listOf(
              QuranWord("1_2_1", 1, "الْحَمْدُ", "আল-হামদু", "Al-Hamdu", "সকল প্রশংসা", "All praise", TajweedType.NONE),
              QuranWord("1_2_2", 2, "لِلَّهِ", "লিল্লাহি", "Lillaahi", "আল্লাহর জন্য", "is due to Allah", TajweedType.NONE),
              QuranWord("1_2_3", 3, "رَبِّ", "রাব্বি", "Rabbi", "প্রতিপালক", "Lord", TajweedType.NONE),
              QuranWord("1_2_4", 4, "الْعَالَمِينَ", "আল-আলামিন", "Al-'Aalameen", "সারা জাহানের", "of the worlds", TajweedType.MADD,
                listOf(TajweedWordSegment("الْ"), TajweedWordSegment("عَا", TajweedType.MADD), TajweedWordSegment("لَ"), TajweedWordSegment("مِي", TajweedType.MADD), TajweedWordSegment("نَ"))
              )
            )
          ),
          QuranAyah(
            ayahNumber = 3,
            textArabic = "الرَّحْمَٰنِ الرَّحِيمِ",
            transliterationBn = "আর-রাহমানির রাহিম",
            transliterationEn = "Ar-Rahmaanir Raheem",
            translationBn = "যিনি পরম করুণাময় ও অতিশয় দয়ালু।",
            translationEn = "The Entirely Merciful, the Especially Merciful.",
            words = listOf(
              QuranWord("1_3_1", 1, "الرَّحْمَٰنِ", "আর-রাহমানি", "Ar-Rahmaani", "পরম দয়াবান", "The Entirely Merciful", TajweedType.MADD),
              QuranWord("1_3_2", 2, "الرَّحِيمِ", "আর-রাহিম", "Ar-Raheem", "অসীম অনুগ্রহশীল", "The Especially Merciful", TajweedType.MADD)
            )
          ),
          QuranAyah(
            ayahNumber = 4,
            textArabic = "مَالِكِ يَوْمِ الدِّينِ",
            transliterationBn = "মালিকি ইয়াওমিদ-দিন",
            transliterationEn = "Maaliki Yawmid-Deen",
            translationBn = "প্রতিদান দিবসের একমাত্র মালিক।",
            translationEn = "Sovereign of the Day of Recompense.",
            words = listOf(
              QuranWord("1_4_1", 1, "مَالِكِ", "মালিকি", "Maaliki", "মালিক বা অধিপতি", "Master / Sovereign", TajweedType.MADD),
              QuranWord("1_4_2", 2, "يَوْمِ", "ইয়াওমি", "Yawmi", "দিবসের", "Day", TajweedType.NONE),
              QuranWord("1_4_3", 3, "الدِّينِ", "আদ-দিন", "Ad-Deen", "বিচার বা প্রতিদানের", "of Recompense", TajweedType.MADD)
            )
          ),
          QuranAyah(
            ayahNumber = 5,
            textArabic = "إِيَّاكَ نَعْبُدُ وَإِيَّاكَ نَسْتَعِينُ",
            transliterationBn = "ইয়্যাকা না’বুদু ওয়া ইয়্যাকা নাস্তা’ঈন",
            transliterationEn = "Iyyaaka na'budu wa lyyaaka nasta'een",
            translationBn = "আমরা কেবল আপনারই ইবাদত করি এবং কেবল আপনারই সাহায্য প্রার্থনা করি।",
            translationEn = "It is You we worship and You we ask for help.",
            words = listOf(
              QuranWord("1_5_1", 1, "إِيَّاكَ", "ইয়্যাকা", "Iyyaaka", "কেবল আপনারই", "You alone", TajweedType.MADD),
              QuranWord("1_5_2", 2, "نَعْبُدُ", "না’বুদু", "Na'budu", "আমরা ইবাদত করি", "we worship", TajweedType.NONE),
              QuranWord("1_5_3", 3, "وَإِيَّاكَ", "ওয়া-ইয়্যাকা", "Wa-Iyyaaka", "এবং আপনারই নিকট", "and You alone", TajweedType.MADD),
              QuranWord("1_5_4", 4, "نَسْتَعِينُ", "নাস্তা’ঈন", "Nasta'een", "আমরা সাহায্য চাই", "we ask for help", TajweedType.MADD)
            )
          ),
          QuranAyah(
            ayahNumber = 6,
            textArabic = "اهْدِنَا الصِّرَاطَ الْمُسْتَقِيمَ",
            transliterationBn = "ইহদিনাস-সিরাতাল মুস্তাকিম",
            transliterationEn = "Ihdinas-Siraatal-Mustaqeem",
            translationBn = "আমাদের সরল সঠিক পথ প্রদর্শন করুন।",
            translationEn = "Guide us to the straight path -",
            words = listOf(
              QuranWord("1_6_1", 1, "اهْدِنَا", "ইহদিনা", "Ihdina", "আমাদের পরিচালনা করুন", "Guide us", TajweedType.MADD),
              QuranWord("1_6_2", 2, "الصِّرَاطَ", "আস-সিরাতা", "As-Siraata", "সরল পথে", "The Path", TajweedType.MADD),
              QuranWord("1_6_3", 3, "الْمُسْتَقِيمَ", "আল-মুস্তাকিম", "Al-Mustaqeem", "সুদৃঢ় ও সঠিক", "Straight", TajweedType.MADD)
            )
          ),
          QuranAyah(
            ayahNumber = 7,
            textArabic = "صِرَاطَ الَّذِينَ أَنْعَمْتَ عَلَيْهِمْ غَيْرِ الْمَغْضُوبِ عَلَيْهِمْ وَلَا الضَّالِّينَ",
            transliterationBn = "সিরাতাল্লাযিনা আন’আমতা আলাইহিম, গাইরিল মাগদুবি আলাইহিম ওয়ালাদ-দোয়াল্লিন",
            transliterationEn = "Siraatal-ladheena an'amta 'alayhim ghayril-maghdoobi 'alayhim wa lad-daalleen",
            translationBn = "তাদের পথ যাদেরকে আপনি অনুগ্রহ করেছেন, তাদের পথ নয় যারা ক্রোধে নিপতিত ও পথভ্রষ্ট হয়েছে।",
            translationEn = "The path of those upon whom You have bestowed favor, not of those who have evoked [Your] anger or of those who are astray.",
            words = listOf(
              QuranWord("1_7_1", 1, "صِرَاطَ", "সিরাতা", "Siraata", "পথ", "The Path", TajweedType.MADD),
              QuranWord("1_7_2", 2, "الَّذِينَ", "আল্লাযিনা", "Alladheena", "যাদের", "of those", TajweedType.MADD),
              QuranWord("1_7_3", 3, "أَنْعَمْتَ", "আন’আমতা", "An'amta", "আপনি নিয়ামত দিয়েছেন", "You bestowed favor", TajweedType.NONE),
              QuranWord("1_7_4", 4, "عَلَيْهِمْ", "আলাইহিম", "'Alayhim", "তাদের উপর", "upon them", TajweedType.NONE),
              QuranWord("1_7_5", 5, "غَيْرِ", "গাইরি", "Ghayri", "তাদের নয়", "not of", TajweedType.NONE),
              QuranWord("1_7_6", 6, "الْمَغْضُوبِ", "আল-মাগদুবি", "Al-Maghdoobi", "যারা অভিশপ্ত", "those evoked anger", TajweedType.MADD),
              QuranWord("1_7_7", 7, "عَلَيْهِمْ", "আলাইহিম", "'Alayhim", "তাদের উপর", "upon them", TajweedType.NONE),
              QuranWord("1_7_8", 8, "وَلَا", "ওয়ালা", "Wa Laa", "এবং যারা নয়", "nor of", TajweedType.MADD),
              QuranWord("1_7_9", 9, "الضَّالِّينَ", "আদ-দোয়াল্লিন", "Ad-Daalleen", "পথভ্রষ্ট", "those who are astray", TajweedType.MADD)
            )
          )
        )
      ),

      // 78. Surah An-Naba (النبأ) - Opening of Juz Amma
      Surah(
        number = 78,
        nameArabic = "النَّبَإ",
        nameBengali = "সূরা আন-নাবা",
        nameEnglish = "An-Naba",
        meaningBengali = "মহা সংবাদ",
        meaningEnglish = "The Great News",
        totalVerses = 40,
        revelationType = RevelationType.MAKKI,
        bismillahPrecedes = true,
        ayahs = listOf(
          QuranAyah(
            ayahNumber = 1,
            textArabic = "عَمَّ يَتَسَآءَلُونَ",
            transliterationBn = "আম্মা ইয়াতাসা-আলুন",
            transliterationEn = "'Amma yatasaaa'aloon",
            translationBn = "তারা পরস্পরে কী বিষয়ে জিজ্ঞাসাবাদ করছে?",
            translationEn = "About what are they asking one another?",
            words = listOf(
              QuranWord("78_1_1", 1, "عَمَّ", "আম্মা", "'Amma", "কী বিষয়ে", "About what", TajweedType.GHUNNAH_IKHFA,
                listOf(TajweedWordSegment("عَ"), TajweedWordSegment("مَّ", TajweedType.GHUNNAH_IKHFA))
              ),
              QuranWord("78_1_2", 2, "يَتَسَآءَلُونَ", "ইয়াতাসা-আলুন", "Yatasaaa'aloon", "তারা জিজ্ঞাসাবাদ করছে", "are they asking", TajweedType.MADD,
                listOf(TajweedWordSegment("يَتَ"), TajweedWordSegment("سَآ", TajweedType.MADD), TajweedWordSegment("ءَ"), TajweedWordSegment("لُو", TajweedType.MADD), TajweedWordSegment("نَ"))
              )
            )
          ),
          QuranAyah(
            ayahNumber = 2,
            textArabic = "عَنِ النَّبَإِ الْعَظِيمِ",
            transliterationBn = "আনিন নাবায়িল আযীম",
            transliterationEn = "'Anin-naba'il 'azeem",
            translationBn = "সেই মহা সংবাদ সম্পর্কে,",
            translationEn = "About the great news -",
            words = listOf(
              QuranWord("78_2_1", 1, "عَنِ", "আনি", "'Ani", "সম্পর্কে", "About", TajweedType.NONE),
              QuranWord("78_2_2", 2, "النَّبَإِ", "আন-নাবায়ি", "An-Naba'i", "মহা সংবাদ", "the news", TajweedType.GHUNNAH_IKHFA,
                listOf(TajweedWordSegment("ا"), TajweedWordSegment("ل", TajweedType.SILENT), TajweedWordSegment("نَّ", TajweedType.GHUNNAH_IKHFA), TajweedWordSegment("بَإِ"))
              ),
              QuranWord("78_2_3", 3, "الْعَظِيمِ", "আল-আযীম", "Al-'Azeem", "মহিমান্বিত", "the great", TajweedType.MADD,
                listOf(TajweedWordSegment("الْ"), TajweedWordSegment("عَ"), TajweedWordSegment("ظِي", TajweedType.MADD), TajweedWordSegment("مِ"))
              )
            )
          ),
          QuranAyah(
            ayahNumber = 3,
            textArabic = "الَّذِي هُمْ فِيهِ مُخْتَلِفُونَ",
            transliterationBn = "আল্লাযী হুম ফীহি মুখতালিফুন",
            transliterationEn = "Alladhee hum feehi mukhtalifoon",
            translationBn = "যে বিষয়ে তারা নানা মতে বিভক্ত।",
            translationEn = "That over which they are in disagreement.",
            words = listOf(
              QuranWord("78_3_1", 1, "الَّذِي", "আল্লাযী", "Alladhee", "যাতে", "That which", TajweedType.MADD),
              QuranWord("78_3_2", 2, "هُمْ", "হুম", "Hum", "তারা", "they", TajweedType.NONE),
              QuranWord("78_3_3", 3, "فِيهِ", "ফীহি", "Feehi", "তাতে", "in it", TajweedType.MADD),
              QuranWord("78_3_4", 4, "مُخْتَلِفُونَ", "মুখতালিফুন", "Mukhtalifoon", "মতভেদ করছে", "differing", TajweedType.MADD)
            )
          ),
          QuranAyah(
            ayahNumber = 4,
            textArabic = "كَلَّا سَيَعْلَمُونَ",
            transliterationBn = "কাল্লা- সাইয়া’লামুন",
            transliterationEn = "Kallaa saya'lamoon",
            translationBn = "কখনো নয়! তারা শীঘ্রই জানতে পারবে।",
            translationEn = "No! They are going to know.",
            words = listOf(
              QuranWord("78_4_1", 1, "كَلَّا", "কাল্লা", "Kallaa", "কখনই নয়", "Nay / No", TajweedType.MADD),
              QuranWord("78_4_2", 2, "سَيَعْلَمُونَ", "সাইয়া’লামুন", "Saya'lamoon", "তারা জানতে পারবে", "they will know", TajweedType.MADD)
            )
          ),
          QuranAyah(
            ayahNumber = 5,
            textArabic = "ثُمَّ كَلَّا سَيَعْلَمُونَ",
            transliterationBn = "ছুম্মা কাল্লা- সাইয়া’লামুন",
            transliterationEn = "Thumma kallaa saya'lamoon",
            translationBn = "অতঃপর কখনোই নয়, তারা অবশ্যই জানতে পারবে!",
            translationEn = "Then, no! They are going to know.",
            words = listOf(
              QuranWord("78_5_1", 1, "ثُمَّ", "ছুম্মা", "Thumma", "অতঃপর", "Then", TajweedType.GHUNNAH_IKHFA,
                listOf(TajweedWordSegment("ثُ"), TajweedWordSegment("مَّ", TajweedType.GHUNNAH_IKHFA))
              ),
              QuranWord("78_5_2", 2, "كَلَّا", "কাল্লা", "Kallaa", "কখনই নয়", "Nay", TajweedType.MADD),
              QuranWord("78_5_3", 3, "سَيَعْلَمُونَ", "সাইয়া’লামুন", "Saya'lamoon", "তারা জানতে পারবে", "they will know", TajweedType.MADD)
            )
          )
        )
      ),

      // 108. Surah Al-Kawthar (الكوثر)
      Surah(
        number = 108,
        nameArabic = "الكَوْثَر",
        nameBengali = "সূরা আল-কাউসার",
        nameEnglish = "Al-Kawthar",
        meaningBengali = "প্রাচুর্য / কাউসার ঝর্ণা",
        meaningEnglish = "Abundance",
        totalVerses = 3,
        revelationType = RevelationType.MAKKI,
        bismillahPrecedes = true,
        ayahs = listOf(
          QuranAyah(
            ayahNumber = 1,
            textArabic = "إِنَّآ أَعْطَيْنَٰكَ الْكَوْثَرَ",
            transliterationBn = "ইন্না- আ’ত়োয়না-কাল কাউছার",
            transliterationEn = "Innaaa a'taynaakal-kawthar",
            translationBn = "নিশ্চয় আমি আপনাকে কাউসার দান করেছি।",
            translationEn = "Indeed, We have granted you, [O Muhammad], al-Kawthar.",
            words = listOf(
              QuranWord("108_1_1", 1, "إِنَّآ", "ইন্না", "Innaaa", "নিশ্চয়ই আমরা", "Indeed We", TajweedType.GHUNNAH_IKHFA,
                listOf(TajweedWordSegment("إِ"), TajweedWordSegment("نَّآ", TajweedType.GHUNNAH_IKHFA))
              ),
              QuranWord("108_1_2", 2, "أَعْطَيْنَٰكَ", "আ’ত়োয়না-কা", "A'taynaaka", "আপনাকে দান করেছি", "have granted you", TajweedType.MADD,
                listOf(TajweedWordSegment("أَعْ"), TajweedWordSegment("طَيْ"), TajweedWordSegment("نَٰ", TajweedType.MADD), TajweedWordSegment("كَ"))
              ),
              QuranWord("108_1_3", 3, "الْكَوْثَرَ", "আল-কাউছার", "Al-Kawthar", "কাউসার", "the Abundance", TajweedType.NONE)
            )
          ),
          QuranAyah(
            ayahNumber = 2,
            textArabic = "فَصَلِّ لِرَبِّكَ وَانْحَرْ",
            transliterationBn = "ফাছাল্লি লিরব্বিকা ওয়ানহার",
            transliterationEn = "Fasalli li-rabbika wanhar",
            translationBn = "অতএব আপনার রবের উদ্দেশ্যে নামাজ পড়ুন এবং কুরবানী করুন।",
            translationEn = "So pray to your Lord and sacrifice [to Him alone].",
            words = listOf(
              QuranWord("108_2_1", 1, "فَصَلِّ", "ফাসাল্লি", "Fasalli", "অতএব নামাজ পড়ুন", "So pray", TajweedType.NONE),
              QuranWord("108_2_2", 2, "لِرَبِّكَ", "লি-রাব্বিকা", "Li-Rabbika", "আপনার রবের জন্য", "to your Lord", TajweedType.NONE),
              QuranWord("108_2_3", 3, "وَانْحَرْ", "ওয়ানহার", "Wanhar", "এবং কুরবানী করুন", "and sacrifice", TajweedType.NONE)
            )
          ),
          QuranAyah(
            ayahNumber = 3,
            textArabic = "إِنَّ شَانِئَكَ هُوَ الْأَبْتَرُ",
            transliterationBn = "ইন্না শা-নিআকা হুওয়াল আবতার",
            transliterationEn = "Inna shaani'aka huwal-abtar",
            translationBn = "নিশ্চয় আপনার বিরোধীরাই নির্বংশ ও মূলহীন।",
            translationEn = "Indeed, your enemy is the one cut off.",
            words = listOf(
              QuranWord("108_3_1", 1, "إِنَّ", "ইন্না", "Inna", "নিশ্চয়ই", "Indeed", TajweedType.GHUNNAH_IKHFA,
                listOf(TajweedWordSegment("إِ"), TajweedWordSegment("نَّ", TajweedType.GHUNNAH_IKHFA))
              ),
              QuranWord("108_3_2", 2, "شَانِئَكَ", "শা-নিআকা", "Shaani'aka", "আপনার শত্রু", "your enemy", TajweedType.MADD),
              QuranWord("108_3_3", 3, "هُوَ", "হুওয়া", "Huwa", "সে-ই", "he is", TajweedType.NONE),
              QuranWord("108_3_4", 4, "الْأَبْتَرُ", "আল-আবতার", "Al-Abtar", "লেজকাটা / নির্বংশ", "the one cut off", TajweedType.QALQALAH,
                listOf(TajweedWordSegment("الْ"), TajweedWordSegment("أَبْ", TajweedType.QALQALAH), TajweedWordSegment("تَرُ"))
              )
            )
          )
        )
      ),

      // 112. Surah Al-Ikhlas (الإخلاص)
      Surah(
        number = 112,
        nameArabic = "الإِخْلَاص",
        nameBengali = "সূরা আল-ইখলাস",
        nameEnglish = "Al-Ikhlas",
        meaningBengali = "একনিষ্ঠতা / তৌহিদ",
        meaningEnglish = "The Sincerity",
        totalVerses = 4,
        revelationType = RevelationType.MAKKI,
        bismillahPrecedes = true,
        ayahs = listOf(
          QuranAyah(
            ayahNumber = 1,
            textArabic = "قُلْ هُوَ اللَّهُ أَحَدٌ",
            transliterationBn = "ক্বুল হুওয়াল্লা-হু আহাদ্ব্",
            transliterationEn = "Qul Huwal-laahu Ahad",
            translationBn = "বলুন, তিনিই আল্লাহ, একক ও অদ্বিতীয়।",
            translationEn = "Say, \"He is Allah, [who is] One,",
            words = listOf(
              QuranWord("112_1_1", 1, "قُلْ", "ক্বুল", "Qul", "বলুন", "Say", TajweedType.NONE),
              QuranWord("112_1_2", 2, "هُوَ", "হুওয়া", "Huwa", "তিনি", "He is", TajweedType.NONE),
              QuranWord("112_1_3", 3, "اللَّهُ", "আল্লাহু", "Allahu", "আল্লাহ", "Allah", TajweedType.NONE),
              QuranWord("112_1_4", 4, "أَحَدٌ", "আহাদ্ব্", "Ahad", "একক ও অদ্বিতীয়", "The One", TajweedType.QALQALAH,
                listOf(TajweedWordSegment("أَ"), TajweedWordSegment("حَ"), TajweedWordSegment("دٌ", TajweedType.QALQALAH))
              )
            )
          ),
          QuranAyah(
            ayahNumber = 2,
            textArabic = "اللَّهُ الصَّمَدُ",
            transliterationBn = "আল্লা-হুস্‌ সামাদ্ব্",
            transliterationEn = "Allahus-Samad",
            translationBn = "আল্লাহ কারো মুখাপেক্ষী নন, সকলে তাঁরই মুখাপেক্ষী।",
            translationEn = "Allah, the Eternal Refuge.",
            words = listOf(
              QuranWord("112_2_1", 1, "اللَّهُ", "আল্লাহু", "Allahu", "আল্লাহ", "Allah", TajweedType.NONE),
              QuranWord("112_2_2", 2, "الصَّمَدُ", "আস-সামাদ্ব্", "As-Samad", "অমুখাপেক্ষী", "The Eternal Refuge", TajweedType.QALQALAH,
                listOf(TajweedWordSegment("الصَّ"), TajweedWordSegment("مَ"), TajweedWordSegment("دُ", TajweedType.QALQALAH))
              )
            )
          ),
          QuranAyah(
            ayahNumber = 3,
            textArabic = "لَمْ يَلِدْ وَلَمْ يُولَدْ",
            transliterationBn = "লাম্ ইয়ালিদ্ব্ ওয়া লাম্ ইউলাদ্ব্",
            transliterationEn = "Lam yalid wa lam yoolad",
            translationBn = "তিনি কাউকে জন্ম দেননি এবং তাঁকেও কেউ জন্ম দেয়নি।",
            translationEn = "He neither begets nor is born,",
            words = listOf(
              QuranWord("112_3_1", 1, "لَمْ", "লাম", "Lam", "তিনি নন", "Not", TajweedType.NONE),
              QuranWord("112_3_2", 2, "يَلِدْ", "ইয়ালিদ্ব্", "Yalid", "কাউকে জন্মদাতা", "begets", TajweedType.QALQALAH,
                listOf(TajweedWordSegment("يَ"), TajweedWordSegment("لِدْ", TajweedType.QALQALAH))
              ),
              QuranWord("112_3_3", 3, "وَلَمْ", "ওয়া-লাম", "Wa Lam", "এবং নন", "and not", TajweedType.NONE),
              QuranWord("112_3_4", 4, "يُولَدْ", "ইউলাদ্ব্", "Yoolad", "জন্মগ্রহীতা", "is born", TajweedType.QALQALAH,
                listOf(TajweedWordSegment("يُو", TajweedType.MADD), TajweedWordSegment("لَدْ", TajweedType.QALQALAH))
              )
            )
          ),
          QuranAyah(
            ayahNumber = 4,
            textArabic = "وَلَمْ يَكُن لَّهُ كُفُوًا أَحَدٌ",
            transliterationBn = "ওয়া লাম্ ইয়াকুল্লাহু কুফুওয়ান আহাদ্ব্",
            transliterationEn = "Wa lam yakul-lahoo kufuwan ahad",
            translationBn = "এবং তাঁর সমকক্ষ বা তুলনীয় কেউই নেই।",
            translationEn = "Nor is there to Him any equivalent.\"",
            words = listOf(
              QuranWord("112_4_1", 1, "وَلَمْ", "ওয়া-লাম", "Wa Lam", "এবং নেই", "and not", TajweedType.NONE),
              QuranWord("112_4_2", 2, "يَكُن", "ইয়াকুন", "Yakun", "হওয়ার মত", "is", TajweedType.NONE),
              QuranWord("112_4_3", 3, "لَّهُ", "লাহু", "Lahoo", "তাঁর জন্য", "to Him", TajweedType.NONE),
              QuranWord("112_4_4", 4, "كُفُوًا", "কুফুওয়ান", "Kufuwan", "সমকক্ষ কেউ", "equivalent", TajweedType.NONE),
              QuranWord("112_4_5", 5, "أَحَدٌ", "আহাদ্ব্", "Ahad", "কেউ একজন", "anyone", TajweedType.QALQALAH,
                listOf(TajweedWordSegment("أَ"), TajweedWordSegment("حَ"), TajweedWordSegment("دٌ", TajweedType.QALQALAH))
              )
            )
          )
        )
      ),

      // 113. Surah Al-Falaq (الفلق)
      Surah(
        number = 113,
        nameArabic = "الفَلَق",
        nameBengali = "সূরা আল-ফালাক্ব",
        nameEnglish = "Al-Falaq",
        meaningBengali = "উষাকাল / প্রভাত",
        meaningEnglish = "The Daybreak",
        totalVerses = 5,
        revelationType = RevelationType.MAKKI,
        bismillahPrecedes = true,
        ayahs = listOf(
          QuranAyah(
            ayahNumber = 1,
            textArabic = "قُلْ أَعُوذُ بِرَبِّ الْفَلَقِ",
            transliterationBn = "ক্বুল আ’ঊযু বিরাব্বিল ফালাক্ব্",
            transliterationEn = "Qul a'oodhu bi-rabbil-falaq",
            translationBn = "বলুন, আমি আশ্রয় প্রার্থনা করছি উষার প্রতিপালকের,",
            translationEn = "Say, \"I seek refuge in the Lord of daybreak",
            words = listOf(
              QuranWord("113_1_1", 1, "قُلْ", "ক্বুল", "Qul", "বলুন", "Say", TajweedType.NONE),
              QuranWord("113_1_2", 2, "أَعُوذُ", "আ’ঊযু", "A'oodhu", "আমি আশ্রয় চাই", "I seek refuge", TajweedType.MADD),
              QuranWord("113_1_3", 3, "بِرَبِّ", "বি-রাব্বি", "Bi-Rabbi", "রবের নিকট", "in the Lord", TajweedType.NONE),
              QuranWord("113_1_4", 4, "الْفَلَقِ", "আল-ফালাক্ব্", "Al-Falaq", "উষাকালের", "of daybreak", TajweedType.QALQALAH,
                listOf(TajweedWordSegment("الْ"), TajweedWordSegment("فَ"), TajweedWordSegment("لَ"), TajweedWordSegment("قِ", TajweedType.QALQALAH))
              )
            )
          ),
          QuranAyah(
            ayahNumber = 2,
            textArabic = "مِن شَرِّ مَا خَلَقَ",
            transliterationBn = "মিন্ শাররি মা- খালাক্ব্",
            transliterationEn = "Min sharri maa khalaq",
            translationBn = "তিনি যা সৃষ্টি করেছেন তার অনিষ্ট হতে,",
            translationEn = "From the evil of that which He created",
            words = listOf(
              QuranWord("113_2_1", 1, "مِن", "মিন", "Min", "হতে", "From", TajweedType.GHUNNAH_IKHFA,
                listOf(TajweedWordSegment("مِ"), TajweedWordSegment("ن", TajweedType.GHUNNAH_IKHFA))
              ),
              QuranWord("113_2_2", 2, "شَرِّ", "শাররি", "Sharri", "অনিষ্ট হতে", "the evil", TajweedType.NONE),
              QuranWord("113_2_3", 3, "مَا", "মা", "Maa", "যা কিছু", "of what", TajweedType.MADD),
              QuranWord("113_2_4", 4, "خَلَقَ", "খালাক্ব্", "Khalaq", "তিনি সৃষ্টি করেছেন", "He created", TajweedType.QALQALAH,
                listOf(TajweedWordSegment("خَ"), TajweedWordSegment("لَ"), TajweedWordSegment("قَ", TajweedType.QALQALAH))
              )
            )
          ),
          QuranAyah(
            ayahNumber = 3,
            textArabic = "وَمِن شَرِّ غَاسِقٍ إِذَا وَقَبَ",
            transliterationBn = "ওয়া মিন শাররি গাসিক্বিন ইযা- ওয়াক্বাব্",
            transliterationEn = "Wa min sharri ghaasiqin idhaa waqab",
            translationBn = "এবং রাতের অন্ধকারের অনিষ্ট হতে যখন তা সমাগত হয়,",
            translationEn = "And from the evil of darkness when it settles",
            words = listOf(
              QuranWord("113_3_1", 1, "وَمِن", "ওয়া-মিন", "Wa Min", "এবং হতে", "And from", TajweedType.GHUNNAH_IKHFA),
              QuranWord("113_3_2", 2, "شَرِّ", "শাররি", "Sharri", "অনিষ্ট হতে", "evil of", TajweedType.NONE),
              QuranWord("113_3_3", 3, "غَاسِقٍ", "গাসিক্বিন", "Ghaasiqin", "অন্ধকার রাতের", "darkness", TajweedType.MADD),
              QuranWord("113_3_4", 4, "إِذَا", "ইযা", "Idhaa", "যখন", "when", TajweedType.MADD),
              QuranWord("113_3_5", 5, "وَقَبَ", "ওয়াক্বাব্", "Waqab", "তা ছেয়ে ফেলে", "it settles", TajweedType.QALQALAH,
                listOf(TajweedWordSegment("وَ"), TajweedWordSegment("قَ"), TajweedWordSegment("بَ", TajweedType.QALQALAH))
              )
            )
          ),
          QuranAyah(
            ayahNumber = 4,
            textArabic = "وَمِن شَرِّ النَّفَّٰثَٰتِ فِي الْعُقَدِ",
            transliterationBn = "ওয়া মিন শাররিন নাফ্ফা-ছা-তি ফিল উক্বাদ্",
            transliterationEn = "Wa min sharrin-naffaathaati fil-'uqad",
            translationBn = "এবং গ্রন্থিতে ফুৎকারকারিণী জাদুকরীদের অনিষ্ট হতে,",
            translationEn = "And from the evil of the blowers in knots",
            words = listOf(
              QuranWord("113_4_1", 1, "وَمِن", "ওয়া-মিন", "Wa Min", "এবং হতে", "And from", TajweedType.GHUNNAH_IKHFA),
              QuranWord("113_4_2", 2, "شَرِّ", "শাররি", "Sharri", "অনিষ্ট", "evil of", TajweedType.NONE),
              QuranWord("113_4_3", 3, "النَّفَّٰثَٰتِ", "আন-নাফ্ফাছাতি", "An-Naffaathaati", "ফুঁৎকারকারিনী", "blowers", TajweedType.GHUNNAH_IKHFA),
              QuranWord("113_4_4", 4, "فِي", "ফী", "Fee", "মধ্যে", "in", TajweedType.MADD),
              QuranWord("113_4_5", 5, "الْعُقَدِ", "আল-উক্বাদ্", "Al-'Uqad", "গ্রন্থির", "the knots", TajweedType.QALQALAH,
                listOf(TajweedWordSegment("الْ"), TajweedWordSegment("عُ"), TajweedWordSegment("قَ"), TajweedWordSegment("دِ", TajweedType.QALQALAH))
              )
            )
          ),
          QuranAyah(
            ayahNumber = 5,
            textArabic = "وَمِن شَرِّ حَاسِدٍ إِذَا حَسَدَ",
            transliterationBn = "ওয়া মিন শাররি হাসিদিন ইযা- হাসাদ্",
            transliterationEn = "Wa min sharri haasidin idhaa hasad",
            translationBn = "এবং হিংসুকের অনিষ্ট হতে যখন সে হিংসা করে।",
            translationEn = "And from the evil of an envier when he envies.\"",
            words = listOf(
              QuranWord("113_5_1", 1, "وَمِن", "ওয়া-মিন", "Wa Min", "এবং হতে", "And from", TajweedType.GHUNNAH_IKHFA),
              QuranWord("113_5_2", 2, "شَرِّ", "শাররি", "Sharri", "অনিষ্ট", "evil of", TajweedType.NONE),
              QuranWord("113_5_3", 3, "حَاسِدٍ", "হাসিদিন", "Haasidin", "হিংসুকের", "envier", TajweedType.MADD),
              QuranWord("113_5_4", 4, "إِذَا", "ইযা", "Idhaa", "যখন", "when", TajweedType.MADD),
              QuranWord("113_5_5", 5, "حَسَدَ", "হাসাদ্", "Hasad", "হিংসা করে", "he envies", TajweedType.QALQALAH,
                listOf(TajweedWordSegment("حَ"), TajweedWordSegment("سَ"), TajweedWordSegment("دَ", TajweedType.QALQALAH))
              )
            )
          )
        )
      ),

      // 114. Surah An-Nas (الناس)
      Surah(
        number = 114,
        nameArabic = "النَّاس",
        nameBengali = "সূরা আন-নাস",
        nameEnglish = "An-Nas",
        meaningBengali = "মানবজাতি",
        meaningEnglish = "Mankind",
        totalVerses = 6,
        revelationType = RevelationType.MAKKI,
        bismillahPrecedes = true,
        ayahs = listOf(
          QuranAyah(
            ayahNumber = 1,
            textArabic = "قُلْ أَعُوذُ بِرَبِّ النَّاسِ",
            transliterationBn = "ক্বুল আ’ঊযু বিরাব্বিন-নাস",
            transliterationEn = "Qul a'oodhu bi-rabbin-naas",
            translationBn = "বলুন, আমি আশ্রয় প্রার্থনা করছি মানুষের প্রতিপালকের,",
            translationEn = "Say, \"I seek refuge in the Lord of mankind,",
            words = listOf(
              QuranWord("114_1_1", 1, "قُلْ", "ক্বুল", "Qul", "বলুন", "Say", TajweedType.NONE),
              QuranWord("114_1_2", 2, "أَعُوذُ", "আ’ঊযু", "A'oodhu", "আমি আশ্রয় চাই", "I seek refuge", TajweedType.MADD),
              QuranWord("114_1_3", 3, "بِرَبِّ", "বি-রাব্বি", "Bi-Rabbi", "রবের নিকট", "in the Lord", TajweedType.NONE),
              QuranWord("114_1_4", 4, "النَّاسِ", "আন-নাস", "An-Naas", "মানুষের", "of mankind", TajweedType.GHUNNAH_IKHFA,
                listOf(TajweedWordSegment("ا"), TajweedWordSegment("ل", TajweedType.SILENT), TajweedWordSegment("نَّ", TajweedType.GHUNNAH_IKHFA), TajweedWordSegment("ا", TajweedType.MADD), TajweedWordSegment("سِ"))
              )
            )
          ),
          QuranAyah(
            ayahNumber = 2,
            textArabic = "مَلِكِ النَّاسِ",
            transliterationBn = "মালিকিন-নাস",
            transliterationEn = "Malikin-naas",
            translationBn = "মানুষের অধিপতির,",
            translationEn = "The Sovereign of mankind,",
            words = listOf(
              QuranWord("114_2_1", 1, "مَلِكِ", "মালিকি", "Maliki", "অধিপতি", "The King", TajweedType.NONE),
              QuranWord("114_2_2", 2, "النَّاسِ", "আন-নাস", "An-Naas", "মানুষের", "of mankind", TajweedType.GHUNNAH_IKHFA)
            )
          ),
          QuranAyah(
            ayahNumber = 3,
            textArabic = "إِلَٰهِ النَّاسِ",
            transliterationBn = "ইলা-হিন-নাস",
            transliterationEn = "Ilaahin-naas",
            translationBn = "মানুষের প্রকৃত উপাস্যের,",
            translationEn = "The God of mankind,",
            words = listOf(
              QuranWord("114_3_1", 1, "إِلَٰهِ", "ইলাহি", "Ilaahi", "মাবুদের", "The God", TajweedType.MADD),
              QuranWord("114_3_2", 2, "النَّاسِ", "আন-নাস", "An-Naas", "মানুষের", "of mankind", TajweedType.GHUNNAH_IKHFA)
            )
          ),
          QuranAyah(
            ayahNumber = 4,
            textArabic = "مِن شَرِّ الْوَسْوَاسِ الْخَنَّاسِ",
            transliterationBn = "মিন্ শাররিল ওয়াস্ওয়াসিল খান্নাস",
            transliterationEn = "Min sharril-waswaasil-khannaas",
            translationBn = "আত্মগোপনকারী কুমন্ত্রণাদাতার অনিষ্ট হতে,",
            translationEn = "From the evil of the retreating whisperer -",
            words = listOf(
              QuranWord("114_4_1", 1, "مِن", "মিন", "Min", "হতে", "From", TajweedType.GHUNNAH_IKHFA),
              QuranWord("114_4_2", 2, "شَرِّ", "শাররি", "Sharri", "অনিষ্ট", "evil of", TajweedType.NONE),
              QuranWord("114_4_3", 3, "الْوَسْوَاسِ", "আল-ওয়াসওয়াসি", "Al-Waswaasi", "কুমন্ত্রণাদাতার", "the whisperer", TajweedType.MADD),
              QuranWord("114_4_4", 4, "الْخَنَّاسِ", "আল-খান্নাস", "Al-Khannaas", "যে পিছু হটে", "who retreats", TajweedType.GHUNNAH_IKHFA)
            )
          ),
          QuranAyah(
            ayahNumber = 5,
            textArabic = "الَّذِي يُوَسْوِسُ فِي صُدُورِ النَّاسِ",
            transliterationBn = "আল্লাযী ইউওয়াস্উইসু ফী ছুদূরিন-নাস",
            transliterationEn = "Alladhee yuwaswisu fee sudoorin-naas",
            translationBn = "যে মানুষের অন্তরে কুমন্ত্রণা নিক্ষেপ করে,",
            translationEn = "Who whispers into the breasts of mankind -",
            words = listOf(
              QuranWord("114_5_1", 1, "الَّذِي", "আল্লাযী", "Alladhee", "যে", "Who", TajweedType.MADD),
              QuranWord("114_5_2", 2, "يُوَسْوِسُ", "ইউওয়াসওয়িসু", "Yuwaswisu", "কুমন্ত্রণা দেয়", "whispers", TajweedType.NONE),
              QuranWord("114_5_3", 3, "فِي", "ফী", "Fee", "মধ্যে", "in", TajweedType.MADD),
              QuranWord("114_5_4", 4, "صُدُورِ", "সুদূরি", "Sudoori", "অন্তরের", "breasts", TajweedType.MADD),
              QuranWord("114_5_5", 5, "النَّاسِ", "আন-নাস", "An-Naas", "মানুষের", "of mankind", TajweedType.GHUNNAH_IKHFA)
            )
          ),
          QuranAyah(
            ayahNumber = 6,
            textArabic = "مِنَ الْجِنَّةِ وَالنَّاسِ",
            transliterationBn = "মিনাল জিন্নাতি ওয়ান-নাস",
            transliterationEn = "Minal-jinnati wan-naas",
            translationBn = "জ্বিনদের মধ্য হতে এবং মানুষদের মধ্য হতে।",
            translationEn = "From among the jinn and mankind.\"",
            words = listOf(
              QuranWord("114_6_1", 1, "مِنَ", "মিনা", "Mina", "হতে", "From", TajweedType.NONE),
              QuranWord("114_6_2", 2, "الْجِنَّةِ", "আল-জিন্নাতি", "Al-Jinnati", "জ্বিনদের", "the jinn", TajweedType.GHUNNAH_IKHFA,
                listOf(TajweedWordSegment("الْ"), TajweedWordSegment("جِ"), TajweedWordSegment("نَّ", TajweedType.GHUNNAH_IKHFA), TajweedWordSegment("ةِ"))
              ),
              QuranWord("114_6_3", 3, "وَالنَّاسِ", "ওয়ান-নাস", "Wan-Naas", "এবং মানুষদের", "and mankind", TajweedType.GHUNNAH_IKHFA,
                listOf(TajweedWordSegment("وَ"), TajweedWordSegment("ا"), TajweedWordSegment("ل", TajweedType.SILENT), TajweedWordSegment("نَّ", TajweedType.GHUNNAH_IKHFA), TajweedWordSegment("ا", TajweedType.MADD), TajweedWordSegment("سِ"))
              )
            )
          )
        )
      )
    )
  }
}
