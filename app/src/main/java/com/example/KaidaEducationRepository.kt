package com.example

import androidx.compose.ui.graphics.Color

/**
 * Foundation Repository containing comprehensive educational data for all 9 Kaida chapters:
 * 1. Harakat (হরকত)
 * 2. Tanween (তানবীন)
 * 3. Sukun / Jazm (সাকিন)
 * 4. Tashdeed (তাসদীদ)
 * 5. Madd Rules (মাদ্দ - টেনে পড়া)
 * 6. Ghunnah & Ikhfa (গুননাহ)
 * 7. Qalqalah & Iqlab (কলকলাহ)
 * 8. Word Building (শব্দ গঠন)
 * 9. Short Surahs Practice (আমপাড়া কায়দা পাঠ)
 */
object KaidaEducationRepository {

  val chapters: List<KaidaChapter> by lazy {
    listOf(
      // 1. Harakat (হরকত)
      KaidaChapter(
        id = 1,
        chapterNumber = 1,
        titleBn = "হরকত (যবর, যের, পেশ)",
        titleEn = "Harakat (Fatha, Kasra, Damma)",
        titleAr = "الحركات الثلاث",
        subtitleBn = "এক যবর, এক যের, এক পেশকে হরকত বলে। হরকতের উচ্চারণ তাড়াতাড়ি পড়তে হয়।",
        subtitleEn = "Short vowels: Fatha, Kasra, and Damma. Pronounce briskly without elongation.",
        iconEmoji = "🌱",
        themeColor = Color(0xFF0D9488), // Teal
        primaryTajweedRule = TajweedType.NONE,
        pages = listOf(
          KaidaBookPage(
            pageNumber = 1,
            pageTitleBn = "যবর (Fatha) অনুশীলন",
            pageTitleEn = "Fatha (Zabar) Practice",
            ruleFocusBn = "যবর হরফের উপরে থাকে এবং 'আ' কার ধ্বনি দেয়।",
            ruleFocusEn = "Fatha sits above the letter, producing an 'a' sound.",
            words = listOf(
              KaidaWordItem(
                id = "c1_p1_w1",
                arabicWord = "دَرَسَ",
                pronunciationBn = "দা-রা-সা",
                pronunciationEn = "Da-ra-sa",
                meaningBn = "সে পাঠ গ্রহণ করল",
                meaningEn = "He studied",
                primaryTajweed = TajweedType.NONE,
                tajweedNoteBn = "তিনটি হরফই তাড়াতাড়ি যবর দিয়ে পড়ুন",
                tajweedNoteEn = "Pronounce all three letters crisply with Fatha",
                segments = listOf(
                  TajweedWordSegment("دَ"),
                  TajweedWordSegment("رَ"),
                  TajweedWordSegment("سَ")
                )
              ),
              KaidaWordItem(
                id = "c1_p1_w2",
                arabicWord = "وَزَنَ",
                pronunciationBn = "ওয়া-যা-না",
                pronunciationEn = "Wa-za-na",
                meaningBn = "সে ওজন করল",
                meaningEn = "He weighed",
                primaryTajweed = TajweedType.NONE,
                tajweedNoteBn = "ওয়াও, যা এবং নূন দ্রুত উচ্চারণ করুন",
                tajweedNoteEn = "Pronounce Wa, Zay, and Nun briskly",
                segments = listOf(
                  TajweedWordSegment("وَ"),
                  TajweedWordSegment("زَ"),
                  TajweedWordSegment("نَ")
                )
              ),
              KaidaWordItem(
                id = "c1_p1_w3",
                arabicWord = "زَرَعَ",
                pronunciationBn = "যা-রা-আ’",
                pronunciationEn = "Za-ra-'a",
                meaningBn = "সে রোপণ করল",
                meaningEn = "He planted",
                primaryTajweed = TajweedType.NONE,
                tajweedNoteBn = "আইনের মাখরাজ হলকের মধ্যভাগ থেকে স্পষ্ট করুন",
                tajweedNoteEn = "Pronounce 'Ayn clearly from the mid-throat",
                segments = listOf(
                  TajweedWordSegment("زَ"),
                  TajweedWordSegment("رَ"),
                  TajweedWordSegment("عَ")
                )
              ),
              KaidaWordItem(
                id = "c1_p1_w4",
                arabicWord = "ذَهَبَ",
                pronunciationBn = "যা-হা-বা",
                pronunciationEn = "Dha-ha-ba",
                meaningBn = "সে চলে গেল",
                meaningEn = "He went",
                primaryTajweed = TajweedType.NONE,
                tajweedNoteBn = "যাল হরফে নরম ও পাতলা করে উচ্চারণ করুন",
                tajweedNoteEn = "Pronounce Dhal softly with tongue tip",
                segments = listOf(
                  TajweedWordSegment("ذَ"),
                  TajweedWordSegment("هَ"),
                  TajweedWordSegment("بَ")
                )
              )
            )
          ),
          KaidaBookPage(
            pageNumber = 2,
            pageTitleBn = "যের ও পেশ (Kasra & Damma) সংমিশ্রণ",
            pageTitleEn = "Kasra & Damma Combinations",
            ruleFocusBn = "যের নিচে 'ই' কার এবং পেশ উপরে 'উ' কার ধ্বনি প্রকাশ করে।",
            ruleFocusEn = "Kasra below produces 'i', Damma above produces 'u'.",
            words = listOf(
              KaidaWordItem(
                id = "c1_p2_w1",
                arabicWord = "رُسُلُ",
                pronunciationBn = "রু-সু-লু",
                pronunciationEn = "Ru-su-lu",
                meaningBn = "রাসূলগণ",
                meaningEn = "Messengers",
                primaryTajweed = TajweedType.NONE,
                tajweedNoteBn = "সবগুলো পেশ ঠোঁট গোল করে দ্রুত পড়ুন",
                tajweedNoteEn = "Round lips for each Damma quickly",
                segments = listOf(
                  TajweedWordSegment("رُ"),
                  TajweedWordSegment("سُ"),
                  TajweedWordSegment("لُ")
                )
              ),
              KaidaWordItem(
                id = "c1_p2_w2",
                arabicWord = "سَمِعَ",
                pronunciationBn = "সা-মি-আ’",
                pronunciationEn = "Sa-mi-'a",
                meaningBn = "সে শুনল",
                meaningEn = "He heard",
                primaryTajweed = TajweedType.NONE,
                tajweedNoteBn = "যবর, যের এবং যবরের চমৎকার তারতম্য",
                tajweedNoteEn = "Smooth transitions between Fatha, Kasra, and Fatha",
                segments = listOf(
                  TajweedWordSegment("سَ"),
                  TajweedWordSegment("مِ"),
                  TajweedWordSegment("عَ")
                )
              ),
              KaidaWordItem(
                id = "c1_p2_w3",
                arabicWord = "شَرِبَ",
                pronunciationBn = "শা-রি-বা",
                pronunciationEn = "Sha-ri-ba",
                meaningBn = "সে পান করল",
                meaningEn = "He drank",
                primaryTajweed = TajweedType.NONE,
                tajweedNoteBn = "শীনে তাফাশশী ও র-এ যের পাতলা",
                tajweedNoteEn = "Sheen with air spread, Raa thin with Kasra",
                segments = listOf(
                  TajweedWordSegment("شَ"),
                  TajweedWordSegment("رِ"),
                  TajweedWordSegment("بَ")
                )
              ),
              KaidaWordItem(
                id = "c1_p2_w4",
                arabicWord = "كُتِبَ",
                pronunciationBn = "কু-তি-বা",
                pronunciationEn = "Ku-ti-ba",
                meaningBn = "নির্ধারিত হলো",
                meaningEn = "It was ordained",
                primaryTajweed = TajweedType.NONE,
                tajweedNoteBn = "পেশ, যের ও যবর কোনোটিই যেন টেনে না যায়",
                tajweedNoteEn = "Do not stretch any short vowel",
                segments = listOf(
                  TajweedWordSegment("كُ"),
                  TajweedWordSegment("تِ"),
                  TajweedWordSegment("بَ")
                )
              )
            )
          )
        )
      ),

      // 2. Tanween (তানবীন)
      KaidaChapter(
        id = 2,
        chapterNumber = 2,
        titleBn = "তানবীন (দুই যবর, দুই যের, দুই পেশ)",
        titleEn = "Tanween (Double Vowels)",
        titleAr = "التنوين",
        subtitleBn = "দুই যবর, দুই যের, দুই পেশকে তানবীন বলে। তানবীনের ভিতরে একটি লুক্কায়িত নূন সাকিন থাকে।",
        subtitleEn = "Two Fathas, two Kasras, two Dammas. Hidden non-written Nun Sakin sound (-an, -in, -un).",
        iconEmoji = "✨",
        themeColor = Color(0xFF0284C7), // Sky Blue
        primaryTajweedRule = TajweedType.NONE,
        pages = listOf(
          KaidaBookPage(
            pageNumber = 1,
            pageTitleBn = "দুই যবর, দুই যের, দুই পেশ",
            pageTitleEn = "Double Vowels Practice",
            ruleFocusBn = "তানবীন পড়ার সময় স্পষ্ট নূনের ধ্বনি তাড়াতাড়ি ادا করুন।",
            ruleFocusEn = "Pronounce the nunation crisp and clear without stretching.",
            words = listOf(
              KaidaWordItem(
                id = "c2_p1_w1",
                arabicWord = "كِتَابٌ",
                pronunciationBn = "কি-তা-বুন",
                pronunciationEn = "Ki-taa-bun",
                meaningBn = "একটি কিতাব বা বই",
                meaningEn = "A book",
                primaryTajweed = TajweedType.NONE,
                tajweedNoteBn = "বা-এর দুই পেশে 'বুন' স্পষ্ট করে পড়ুন",
                tajweedNoteEn = "Pronounce 'bun' crisply at the end",
                segments = listOf(
                  TajweedWordSegment("كِ"),
                  TajweedWordSegment("تَا", TajweedType.MADD),
                  TajweedWordSegment("بٌ")
                )
              ),
              KaidaWordItem(
                id = "c2_p1_w2",
                arabicWord = "رَحِيمًا",
                pronunciationBn = "রা-হী-মান",
                pronunciationEn = "Ra-hee-man",
                meaningBn = "পরম দয়ালু হিসেবে",
                meaningEn = "Most Merciful",
                primaryTajweed = TajweedType.NONE,
                tajweedNoteBn = "মীমে দুই যবর সাথে আলিফ থাকে",
                tajweedNoteEn = "Double Fatha on Meem with trailing Alif",
                segments = listOf(
                  TajweedWordSegment("رَ"),
                  TajweedWordSegment("حِي", TajweedType.MADD),
                  TajweedWordSegment("مًا")
                )
              ),
              KaidaWordItem(
                id = "c2_p1_w3",
                arabicWord = "عَذَابٍ",
                pronunciationBn = "আ’-যা-বিন",
                pronunciationEn = "'A-dhaa-bin",
                meaningBn = "শাস্তির",
                meaningEn = "Of punishment",
                primaryTajweed = TajweedType.NONE,
                tajweedNoteBn = "বা-এর নিচে দুই যের স্পষ্ট 'বিন' হবে",
                tajweedNoteEn = "Double Kasra on Baa sounds 'bin'",
                segments = listOf(
                  TajweedWordSegment("عَ"),
                  TajweedWordSegment("ذَا", TajweedType.MADD),
                  TajweedWordSegment("بٍ")
                )
              ),
              KaidaWordItem(
                id = "c2_p1_w4",
                arabicWord = "عَفُوًّا",
                pronunciationBn = "আ’-ফুও-ওয়ান",
                pronunciationEn = "'A-fuw-wan",
                meaningBn = "পরম ক্ষমাশীল",
                meaningEn = "Ever Pardoning",
                primaryTajweed = TajweedType.NONE,
                tajweedNoteBn = "তাসদীদ ও তানবীনের সুন্দর সংমিশ্রণ",
                tajweedNoteEn = "Tashdeed combined with Tanween Fathatayn",
                segments = listOf(
                  TajweedWordSegment("عَ"),
                  TajweedWordSegment("فُ"),
                  TajweedWordSegment("وًّا")
                )
              )
            )
          )
        )
      ),

      // 3. Sukun / Jazm (সাকিন)
      KaidaChapter(
        id = 3,
        chapterNumber = 3,
        titleBn = "সাকিন বা জযম (জযমের নিয়ম)",
        titleEn = "Sukun / Jazm (Resting Sign)",
        titleAr = "السكون والجزم",
        subtitleBn = "জযমযুক্ত হরফ তার ডানদিকের হরকতের সাথে মিলিয়ে একবার উচ্চারিত হয়।",
        subtitleEn = "The resting vowelless marker. Connected to the preceding vowelled letter in one syllable.",
        iconEmoji = "🛑",
        themeColor = Color(0xFF6366F1), // Indigo
        primaryTajweedRule = TajweedType.NONE,
        pages = listOf(
          KaidaBookPage(
            pageNumber = 1,
            pageTitleBn = "জযমযুক্ত হরফ পাঠ",
            pageTitleEn = "Basic Sukun Articulation",
            ruleFocusBn = "সাকিনযুক্ত হরফে কোনো স্বরধ্বনি থাকে না, পূর্বের হরকতের সাথে যুক্ত হয়।",
            ruleFocusEn = "A letter with Sukun rests and attaches to the previous vowel.",
            words = listOf(
              KaidaWordItem(
                id = "c3_p1_w1",
                arabicWord = "قُمْ",
                pronunciationBn = "ক্বুম্",
                pronunciationEn = "Qum",
                meaningBn = "দাঁড়াও বা ওঠো",
                meaningEn = "Stand up",
                primaryTajweed = TajweedType.NONE,
                tajweedNoteBn = "ক্বাফ পেশ ক্বু, মীম সাকিন ক্বুম",
                tajweedNoteEn = "Qaf Damma 'Qu', Meem Sukun 'm' -> Qum",
                segments = listOf(
                  TajweedWordSegment("قُ"),
                  TajweedWordSegment("مْ")
                )
              ),
              KaidaWordItem(
                id = "c3_p1_w2",
                arabicWord = "قُلْ",
                pronunciationBn = "ক্বুল্",
                pronunciationEn = "Qul",
                meaningBn = "বলুন (হে নবী)",
                meaningEn = "Say",
                primaryTajweed = TajweedType.NONE,
                tajweedNoteBn = "লামের মাথায় সাকিন স্পষ্ট করে থামান",
                tajweedNoteEn = "Rest on Lam with clean Sukun",
                segments = listOf(
                  TajweedWordSegment("قُ"),
                  TajweedWordSegment("لْ")
                )
              ),
              KaidaWordItem(
                id = "c3_p1_w3",
                arabicWord = "نَعْبُدُ",
                pronunciationBn = "না’বুদু",
                pronunciationEn = "Na'budu",
                meaningBn = "আমরা ইবাদত করি",
                meaningEn = "We worship",
                primaryTajweed = TajweedType.NONE,
                tajweedNoteBn = "আইনের সাকিন হলকের মাঝখান থেকে আটকে পড়ুন",
                tajweedNoteEn = "Clean mid-throat contraction on Ayn Sukun",
                segments = listOf(
                  TajweedWordSegment("نَ"),
                  TajweedWordSegment("عْ"),
                  TajweedWordSegment("بُ"),
                  TajweedWordSegment("دُ")
                )
              ),
              KaidaWordItem(
                id = "c3_p1_w4",
                arabicWord = "أَنْعَمْتَ",
                pronunciationBn = "আন্’আম্‌তা",
                pronunciationEn = "An'amta",
                meaningBn = "আপনি অনুগ্রহ করেছেন",
                meaningEn = "You bestowed favor",
                primaryTajweed = TajweedType.NONE,
                tajweedNoteBn = "নূন সাকিন এবং মীম সাকিন পাশাপাশি স্পষ্ট",
                tajweedNoteEn = "Izhar on Nun Sakin and Meem Sakin",
                segments = listOf(
                  TajweedWordSegment("أَ"),
                  TajweedWordSegment("نْ"),
                  TajweedWordSegment("عَ"),
                  TajweedWordSegment("مْ"),
                  TajweedWordSegment("تَ")
                )
              )
            )
          )
        )
      ),

      // 4. Tashdeed (তাসদীদ)
      KaidaChapter(
        id = 4,
        chapterNumber = 4,
        titleBn = "তাসদীদ (হরফে জোর দিয়ে দ্বিত্ব উচ্চারণ)",
        titleEn = "Tashdeed (Shaddah / Doubled)",
        titleAr = "التشديد (الشدّة)",
        subtitleBn = "তাসদীদযুক্ত হরফ দুইবার উচ্চারিত হয়; প্রথমবার সাকিন দিয়ে এবং দ্বিতীয়বার হরকত দিয়ে।",
        subtitleEn = "Doubled consonant marker. The letter is pronounced twice: first silent/resting, then vocalized.",
        iconEmoji = "⚡",
        themeColor = Color(0xFFE11D48), // Rose
        primaryTajweedRule = TajweedType.NONE,
        pages = listOf(
          KaidaBookPage(
            pageNumber = 1,
            pageTitleBn = "তাসদীদ হরফের দ্বিত্ব ধ্বনি",
            pageTitleEn = "Doubled Letter Mastery",
            ruleFocusBn = "তাসদীদযুক্ত হরফে কিছুটা শক্তি দিয়ে ধরে রেখে ছাড়তে হয়।",
            ruleFocusEn = "Hold the consonant briefly before releasing with the vowel.",
            words = listOf(
              KaidaWordItem(
                id = "c4_p1_w1",
                arabicWord = "رَبِّ",
                pronunciationBn = "রব্‌বি",
                pronunciationEn = "Rab-bi",
                meaningBn = "আমার প্রতিপালক",
                meaningEn = "My Lord",
                primaryTajweed = TajweedType.NONE,
                tajweedNoteBn = "বা-তে তাসদীদ থাকায় দুইবার উচ্চারণ (রব্ + বি)",
                tajweedNoteEn = "Double Baa: Rab + bi with emphasis",
                segments = listOf(
                  TajweedWordSegment("رَ"),
                  TajweedWordSegment("بِّ")
                )
              ),
              KaidaWordItem(
                id = "c4_p1_w2",
                arabicWord = "حَقٌّ",
                pronunciationBn = "হাক্ব্‌ক্বুন",
                pronunciationEn = "Haq-qun",
                meaningBn = "চিরন্তন সত্য",
                meaningEn = "Truth",
                primaryTajweed = TajweedType.NONE,
                tajweedNoteBn = "ক্বাফে তাসদীদ দিয়ে মোটা করে শক্তভাবে পড়ুন",
                tajweedNoteEn = "Heavy Qaf doubled with full resonance",
                segments = listOf(
                  TajweedWordSegment("حَ"),
                  TajweedWordSegment("قٌّ")
                )
              ),
              KaidaWordItem(
                id = "c4_p1_w3",
                arabicWord = "عَلَّمَ",
                pronunciationBn = "আল্লামা",
                pronunciationEn = "'Al-la-ma",
                meaningBn = "তিনি শিক্ষা দিলেন",
                meaningEn = "He taught",
                primaryTajweed = TajweedType.NONE,
                tajweedNoteBn = "লামে তাসদীদ দ্বিত্ব করে সুন্দরভাবে পড়ুন",
                tajweedNoteEn = "Smooth doubling on Lam",
                segments = listOf(
                  TajweedWordSegment("عَ"),
                  TajweedWordSegment("لَّ"),
                  TajweedWordSegment("مَ")
                )
              ),
              KaidaWordItem(
                id = "c4_p1_w4",
                arabicWord = "ثُمَّ",
                pronunciationBn = "ছুম্মা (গুন্নাহ সহ)",
                pronunciationEn = "Thum-ma",
                meaningBn = "অতঃপর",
                meaningEn = "Then",
                primaryTajweed = TajweedType.GHUNNAH_IKHFA,
                tajweedNoteBn = "মীম মুশদ্দদে ওয়াজিব গুন্নাহ করতে হবে (সবুজ রঙ)",
                tajweedNoteEn = "Obligatory Ghunnah on doubled Meem (Green)",
                segments = listOf(
                  TajweedWordSegment("ثُ"),
                  TajweedWordSegment("مَّ", TajweedType.GHUNNAH_IKHFA)
                )
              )
            )
          )
        )
      ),

      // 5. Madd Rules (মাদ্দ - টেনে পড়া)
      KaidaChapter(
        id = 5,
        chapterNumber = 5,
        titleBn = "মাদ্দ এর নিয়ম (টেনে পড়া)",
        titleEn = "Madd Rules (Elongation)",
        titleAr = "أحكام المدود",
        subtitleBn = "মাদ্দের হরফ তিনটি: আলিফ, ওয়াও, ইয়া। ১ থেকে ৪ আলিফ পরিমাণ টেনে পড়তে হয়।",
        subtitleEn = "Letters of Madd (Alif, Waw, Yaa). Prolong vowel from 1 count (Tabee'i) up to 4 counts. Red highlighted.",
        iconEmoji = "🔴",
        themeColor = Color(0xFFDC2626), // Red
        primaryTajweedRule = TajweedType.MADD,
        pages = listOf(
          KaidaBookPage(
            pageNumber = 1,
            pageTitleBn = "মাদ্দে তবাই (১ আলিফ মাদ্দ)",
            pageTitleEn = "Madd Tabee'i (1 Count)",
            ruleFocusBn = "যবরের বামে খালি আলিফ, পেশের বামে জযমওয়ালা ওয়াও, যেরের বামে জযমওয়ালা ইয়া। লাল রঙে চিহ্নিত।",
            ruleFocusEn = "Natural elongation of 1 count. Highlighted in vibrant Red.",
            words = listOf(
              KaidaWordItem(
                id = "c5_p1_w1",
                arabicWord = "قَالَ",
                pronunciationBn = "ক্বা-লা",
                pronunciationEn = "Qaa-la",
                meaningBn = "সে বলল",
                meaningEn = "He said",
                primaryTajweed = TajweedType.MADD,
                tajweedNoteBn = "যবরের পর খালি আলিফ থাকায় ১ আলিফ টানুন (লাল রঙ)",
                tajweedNoteEn = "1 count elongation on Qaf due to Alif (Red)",
                segments = listOf(
                  TajweedWordSegment("قَا", TajweedType.MADD),
                  TajweedWordSegment("لَ")
                )
              ),
              KaidaWordItem(
                id = "c5_p1_w2",
                arabicWord = "يَقُولُ",
                pronunciationBn = "ইয়াক্বূ-লু",
                pronunciationEn = "Yaqoo-lu",
                meaningBn = "সে বলে",
                meaningEn = "He says",
                primaryTajweed = TajweedType.MADD,
                tajweedNoteBn = "পেশের পর জযমওয়ালা ওয়াও থাকায় ১ আলিফ টানুন",
                tajweedNoteEn = "1 count elongation on Qaf with Waw Sakin",
                segments = listOf(
                  TajweedWordSegment("يَ"),
                  TajweedWordSegment("قُو", TajweedType.MADD),
                  TajweedWordSegment("لُ")
                )
              ),
              KaidaWordItem(
                id = "c5_p1_w3",
                arabicWord = "قِيلَ",
                pronunciationBn = "ক্বী-লা",
                pronunciationEn = "Qee-la",
                meaningBn = "বলা হলো",
                meaningEn = "It was said",
                primaryTajweed = TajweedType.MADD,
                tajweedNoteBn = "যেরের পর জযমওয়ালা ইয়া থাকায় ১ আলিফ টানুন",
                tajweedNoteEn = "1 count elongation on Qaf with Yaa Sakin",
                segments = listOf(
                  TajweedWordSegment("قِي", TajweedType.MADD),
                  TajweedWordSegment("لَ")
                )
              ),
              KaidaWordItem(
                id = "c5_p1_w4",
                arabicWord = "نُوحِيهَا",
                pronunciationBn = "নূহীহা (সবগুলো মাদ্দ)",
                pronunciationEn = "Noo-hee-haa",
                meaningBn = "আমরা তা ওহী করি",
                meaningEn = "We reveal it",
                primaryTajweed = TajweedType.MADD,
                tajweedNoteBn = "তিনটি মাদ্দের হরফের বিরল একত্র সমাবেশ",
                tajweedNoteEn = "Contains all three letters of natural Madd!",
                segments = listOf(
                  TajweedWordSegment("نُو", TajweedType.MADD),
                  TajweedWordSegment("حِي", TajweedType.MADD),
                  TajweedWordSegment("هَا", TajweedType.MADD)
                )
              )
            )
          ),
          KaidaBookPage(
            pageNumber = 2,
            pageTitleBn = "মাদ্দে মুত্তাছিল ও মুনফাছিল (৩-৪ আলিফ)",
            pageTitleEn = "Madd Muttasil & Munfasil (3-4 Counts)",
            ruleFocusBn = "মাদ্দের হরফের পর একই শব্দে অথবা পরবর্তী শব্দে হামযাহ আসলে ৩ থেকে ৪ আলিফ লম্বা করুন।",
            ruleFocusEn = "When Hamza follows Madd letter, prolong 3 to 4 counts.",
            words = listOf(
              KaidaWordItem(
                id = "c5_p2_w1",
                arabicWord = "جَآءَ",
                pronunciationBn = "জ্বা-আ’ (৪ আলিফ)",
                pronunciationEn = "Jaaa-'a",
                meaningBn = "সে আসল",
                meaningEn = "He came",
                primaryTajweed = TajweedType.MADD,
                tajweedNoteBn = "মাদ্দে মুত্তাছিল: এক শব্দে হামযাহ আসায় ৪ আলিফ টানুন",
                tajweedNoteEn = "Madd Muttasil: 4 counts elongation with Hamza",
                segments = listOf(
                  TajweedWordSegment("جَآ", TajweedType.MADD),
                  TajweedWordSegment("ءَ")
                )
              ),
              KaidaWordItem(
                id = "c5_p2_w2",
                arabicWord = "سُوٓءَ",
                pronunciationBn = "সূ-আ’ (৪ আলিফ)",
                pronunciationEn = "Sooo-'a",
                meaningBn = "মন্দ বা অনিষ্ট",
                meaningEn = "Evil",
                primaryTajweed = TajweedType.MADD,
                tajweedNoteBn = "ওয়াও মাদ্দের পর হামযাহ আসায় দীর্ঘ ৪ আলিফ টান",
                tajweedNoteEn = "Long 4 counts Madd on Waw",
                segments = listOf(
                  TajweedWordSegment("سُوٓ", TajweedType.MADD),
                  TajweedWordSegment("ءَ")
                )
              ),
              KaidaWordItem(
                id = "c5_p2_w3",
                arabicWord = "إِذَا جَآءَ",
                pronunciationBn = "ইযা- জ্বা-আ’",
                pronunciationEn = "Idha Jaaa-'a",
                meaningBn = "যখন আসল",
                meaningEn = "When there came",
                primaryTajweed = TajweedType.MADD,
                tajweedNoteBn = "প্রথমটি ১ আলিফ মাদ্দ এবং দ্বিতীয়টি ৪ আলিফ মাদ্দ",
                tajweedNoteEn = "First 1 count, second 4 counts Madd",
                segments = listOf(
                  TajweedWordSegment("إِ"),
                  TajweedWordSegment("ذَا", TajweedType.MADD),
                  TajweedWordSegment(" "),
                  TajweedWordSegment("جَآ", TajweedType.MADD),
                  TajweedWordSegment("ءَ")
                )
              ),
              KaidaWordItem(
                id = "c5_p2_w4",
                arabicWord = "الصَّآخَّةُ",
                pronunciationBn = "আস্‌-সা-খখহ্ (মাদ্দে লাযিম ৬ হরকত)",
                pronunciationEn = "As-Saaakh-khah",
                meaningBn = "বিকট গর্জন (কিয়ামত)",
                meaningEn = "The Deafening Blast",
                primaryTajweed = TajweedType.MADD,
                tajweedNoteBn = "মাদ্দে লাযিম: মাদ্দের হরফের পর তাসদীদ আসায় ৬ হরকত পূর্ণ টান",
                tajweedNoteEn = "Madd Lazim: 6 counts maximum elongation",
                segments = listOf(
                  TajweedWordSegment("الصَّآ", TajweedType.MADD),
                  TajweedWordSegment("خَّ"),
                  TajweedWordSegment("ةُ")
                )
              )
            )
          )
        )
      ),

      // 6. Ghunnah & Ikhfa (গুননাহ)
      KaidaChapter(
        id = 6,
        chapterNumber = 6,
        titleBn = "ওয়াজিব গুন্নাহ ও ইখফা",
        titleEn = "Ghunnah & Ikhfa Rules",
        titleAr = "الغنة والإخفاء",
        subtitleBn = "নাক দিয়ে গুণগুণ করে এক আলিফ পরিমাণ সময় গুন্নাহ করা। সবুজ রঙ দিয়ে নির্দেশিত।",
        subtitleEn = "Nasalization. Wajib Ghunnah on doubled Nun/Meem, and Ikhfa when concealing Nun Sakin. Green highlighted.",
        iconEmoji = "🟢",
        themeColor = Color(0xFF10B981), // Emerald Green
        primaryTajweedRule = TajweedType.GHUNNAH_IKHFA,
        pages = listOf(
          KaidaBookPage(
            pageNumber = 1,
            pageTitleBn = "নূন ও মীম মুশদ্দদের ওয়াজিব গুন্নাহ",
            pageTitleEn = "Wajib Ghunnah (Doubled Nun & Meem)",
            ruleFocusBn = "নূন বা মীমে তাসদীদ থাকলে নাকের বাঁশিতে গুণগুণ করে ওয়াজিব গুন্নাহ করা আবশ্যক।",
            ruleFocusEn = "Mandatory 1 count nasal humming on Shaddah Nun or Meem. Green.",
            words = listOf(
              KaidaWordItem(
                id = "c6_p1_w1",
                arabicWord = "إِنَّ",
                pronunciationBn = "ইন্‌-না (গুন্নাহ সহ)",
                pronunciationEn = "In-na",
                meaningBn = "নিশ্চয়ই",
                meaningEn = "Indeed",
                primaryTajweed = TajweedType.GHUNNAH_IKHFA,
                tajweedNoteBn = "নূন মুশদ্দদে ১ আলিফ পরিমাণ ওয়াজিব গুন্নাহ করুন (সবুজ রঙ)",
                tajweedNoteEn = "Wajib Ghunnah 1 count on doubled Nun (Green)",
                segments = listOf(
                  TajweedWordSegment("إِ"),
                  TajweedWordSegment("نَّ", TajweedType.GHUNNAH_IKHFA)
                )
              ),
              KaidaWordItem(
                id = "c6_p1_w2",
                arabicWord = "النَّاسِ",
                pronunciationBn = "আন্-না-সি",
                pronunciationEn = "An-Naasi",
                meaningBn = "মানুষ জাতি",
                meaningEn = "Mankind",
                primaryTajweed = TajweedType.GHUNNAH_IKHFA,
                tajweedNoteBn = "সবুজ নূনে গুন্নাহ ও লাল আলিফে মাদ্দ একসাথে",
                tajweedNoteEn = "Ghunnah on Nun (Green) + Madd on Alif (Red)",
                segments = listOf(
                  TajweedWordSegment("ا"),
                  TajweedWordSegment("ل", TajweedType.SILENT),
                  TajweedWordSegment("نَّ", TajweedType.GHUNNAH_IKHFA),
                  TajweedWordSegment("ا", TajweedType.MADD),
                  TajweedWordSegment("سِ")
                )
              ),
              KaidaWordItem(
                id = "c6_p1_w3",
                arabicWord = "مِن جَبَلٍ",
                pronunciationBn = "মিন্ জ্বাবালিন (ইখফা)",
                pronunciationEn = "Min jabalin (Ikhfa)",
                meaningBn = "পাহাড় হতে",
                meaningEn = "From a mountain",
                primaryTajweed = TajweedType.GHUNNAH_IKHFA,
                tajweedNoteBn = "নূন সাকিনের পর জীম আসায় ইখফা (নাকের ভেতর লুকিয়ে গুন্নাহ)",
                tajweedNoteEn = "Ikhfa: Conceal Nun Sakin before Jeem with Ghunnah",
                segments = listOf(
                  TajweedWordSegment("مِ"),
                  TajweedWordSegment("ن", TajweedType.GHUNNAH_IKHFA),
                  TajweedWordSegment(" "),
                  TajweedWordSegment("جَ"),
                  TajweedWordSegment("بَ"),
                  TajweedWordSegment("لٍ")
                )
              ),
              KaidaWordItem(
                id = "c6_p1_w4",
                arabicWord = "كُنتُمْ",
                pronunciationBn = "কুন্-তুম্ (ইখফা)",
                pronunciationEn = "Kuntum (Ikhfa)",
                meaningBn = "তোমরা ছিলে",
                meaningEn = "You were",
                primaryTajweed = TajweedType.GHUNNAH_IKHFA,
                tajweedNoteBn = "তা-এর পূর্বে নূন সাকিনে সুন্দর কোমল ইখফা করুন",
                tajweedNoteEn = "Soft Ikhfa on Nun Sakin preceding Taa",
                segments = listOf(
                  TajweedWordSegment("كُ"),
                  TajweedWordSegment("ن", TajweedType.GHUNNAH_IKHFA),
                  TajweedWordSegment("تُ"),
                  TajweedWordSegment("مْ")
                )
              )
            )
          )
        )
      ),

      // 7. Qalqalah & Iqlab (কলকলাহ ও ইক্বলাব)
      KaidaChapter(
        id = 7,
        chapterNumber = 7,
        titleBn = "কলকলাহ ও ইক্বলাব (প্রতিধ্বনি ও রূপান্তর)",
        titleEn = "Qalqalah & Iqlab Rules",
        titleAr = "القلقلة والإقلاب",
        subtitleBn = "কলকলাহর ৫ হরফে সাকিন হলে প্রতিধ্বনি হয় (নীল)। নূন সাকিনের পর বা আসলে মীমে বদলে যায় (কমলা)।",
        subtitleEn = "Qalqalah letters (ق ط ب ج د) bounce with Sukun (Blue). Iqlab turns Nun to Meem before Baa (Orange).",
        iconEmoji = "🔵",
        themeColor = Color(0xFF2563EB), // Blue
        primaryTajweedRule = TajweedType.QALQALAH,
        pages = listOf(
          KaidaBookPage(
            pageNumber = 1,
            pageTitleBn = "কলকলাহ (ق ط ب ج د) ধাক্কা দিয়ে পড়া",
            pageTitleEn = "Qalqalah Echoing Letters",
            ruleFocusBn = "ক্বাফ, ত্বা, বা, জীম, দাল এ সাকিন হলে প্রতিধ্বনি বা ধাক্কা দিয়ে পড়তে হয় (নীল রঙ)।",
            ruleFocusEn = "Echo or bounce sound when stopping on Qaf, Taa, Baa, Jeem, Daal. Blue.",
            words = listOf(
              KaidaWordItem(
                id = "c7_p1_w1",
                arabicWord = "أَحَدٌ",
                pronunciationBn = "আহাদ্ব্ (কলকলাহ)",
                pronunciationEn = "Ahad (Qalqalah bounce)",
                meaningBn = "এক ও অদ্বিতীয়",
                meaningEn = "The One",
                primaryTajweed = TajweedType.QALQALAH,
                tajweedNoteBn = "দাল হরফে থামলে সুন্দরভাবে ধাক্কা দিয়ে কলকলাহ করুন (নীল)",
                tajweedNoteEn = "Bounce on Daal with echoing resonance (Blue)",
                segments = listOf(
                  TajweedWordSegment("أَ"),
                  TajweedWordSegment("حَ"),
                  TajweedWordSegment("دٌ", TajweedType.QALQALAH)
                )
              ),
              KaidaWordItem(
                id = "c7_p1_w2",
                arabicWord = "الفَلَقِ",
                pronunciationBn = "আল-ফালাক্ব্",
                pronunciationEn = "Al-Falaq (Qalqalah)",
                meaningBn = "প্রভাত বা ভোর",
                meaningEn = "The Daybreak",
                primaryTajweed = TajweedType.QALQALAH,
                tajweedNoteBn = "ক্বাফে সাকিন অবস্থায় শক্তিশালী কলকলাহ উচ্চারণ",
                tajweedNoteEn = "Strong Qalqalah bounce on Qaf",
                segments = listOf(
                  TajweedWordSegment("ا"),
                  TajweedWordSegment("ل"),
                  TajweedWordSegment("فَ"),
                  TajweedWordSegment("لَ"),
                  TajweedWordSegment("قِ", TajweedType.QALQALAH)
                )
              ),
              KaidaWordItem(
                id = "c7_p1_w3",
                arabicWord = "حَبْلٌ",
                pronunciationBn = "হাব্‌লুন",
                pronunciationEn = "Hab-lun",
                meaningBn = "একটি রশি",
                meaningEn = "A rope",
                primaryTajweed = TajweedType.QALQALAH,
                tajweedNoteBn = "বা-এর সাকিনে হালকা ধাক্কা বা কলকলাহ সুগরা",
                tajweedNoteEn = "Gentle Qalqalah Sughra in middle of word on Baa",
                segments = listOf(
                  TajweedWordSegment("حَ"),
                  TajweedWordSegment("بْ", TajweedType.QALQALAH),
                  TajweedWordSegment("لٌ")
                )
              ),
              KaidaWordItem(
                id = "c7_p1_w4",
                arabicWord = "مِنۢ بَعْدِ",
                pronunciationBn = "মিম্ বা’দি (ইক্বলাব)",
                pronunciationEn = "Mim ba'di (Iqlab)",
                meaningBn = "এর পরবর্তীতে",
                meaningEn = "After that",
                primaryTajweed = TajweedType.IQLAB,
                tajweedNoteBn = "ইক্বলাব: নূন সাকিন 'মীম' দ্বারা পরিবর্তিত এবং গুন্নাহ সহ (কমলা রঙ)",
                tajweedNoteEn = "Iqlab: Nun Sakin converts to Meem before Baa (Orange)",
                segments = listOf(
                  TajweedWordSegment("مِ"),
                  TajweedWordSegment("نۢ", TajweedType.IQLAB),
                  TajweedWordSegment(" "),
                  TajweedWordSegment("بَ"),
                  TajweedWordSegment("عْ"),
                  TajweedWordSegment("دِ")
                )
              )
            )
          )
        )
      ),

      // 8. Word Building (শব্দ গঠন)
      KaidaChapter(
        id = 8,
        chapterNumber = 8,
        titleBn = "শব্দ গঠন (হরফ সংযোগ ও পাঠ)",
        titleEn = "Word Building (Connecting Script)",
        titleAr = "تركيب الكلمات",
        subtitleBn = "হরফগুলো কীভাবে একে অপরের সাথে যুক্ত হয়ে পূর্ণ শব্দ গঠন করে তার বাস্তব অনুশীলন।",
        subtitleEn = "How letters fuse seamlessly in initial, medial, and final forms into complete words.",
        iconEmoji = "🧩",
        themeColor = Color(0xFF8B5CF6), // Purple
        primaryTajweedRule = TajweedType.NONE,
        pages = listOf(
          KaidaBookPage(
            pageNumber = 1,
            pageTitleBn = "৩ ও ৪ হরফের পূর্ণ শব্দ",
            pageTitleEn = "3 & 4 Letter Quranic Words",
            ruleFocusBn = "যুক্তাক্ষরের সঠিক চেহারা চিনে স্বচ্ছ উচ্চারণ আয়ত্ত করুন।",
            ruleFocusEn = "Recognize connected ligatures and read fluidly.",
            words = listOf(
              KaidaWordItem(
                id = "c8_p1_w1",
                arabicWord = "يَعْلَمُونَ",
                pronunciationBn = "ইয়া’লামূন",
                pronunciationEn = "Ya'lamoon",
                meaningBn = "তারা জানে",
                meaningEn = "They know",
                primaryTajweed = TajweedType.MADD,
                tajweedNoteBn = "ইয়া + আইন সাকিন + লাম + মীম + ওয়াও মাদ্দ + নূন",
                tajweedNoteEn = "Yaa + Ayn + Lam + Meem + Waw Madd + Nun",
                segments = listOf(
                  TajweedWordSegment("يَ"),
                  TajweedWordSegment("عْ"),
                  TajweedWordSegment("لَ"),
                  TajweedWordSegment("مُو", TajweedType.MADD),
                  TajweedWordSegment("نَ")
                )
              ),
              KaidaWordItem(
                id = "c8_p1_w2",
                arabicWord = "الْحَمْدُ",
                pronunciationBn = "আল-হামদু",
                pronunciationEn = "Al-Hamdu",
                meaningBn = "যাবতীয় প্রশংসা",
                meaningEn = "All praise",
                primaryTajweed = TajweedType.NONE,
                tajweedNoteBn = "সূরা ফাতিহার প্রথম মূল শব্দ",
                tajweedNoteEn = "First prime word of Surah Al-Fatihah",
                segments = listOf(
                  TajweedWordSegment("ا"),
                  TajweedWordSegment("لْ"),
                  TajweedWordSegment("حَ"),
                  TajweedWordSegment("مْ"),
                  TajweedWordSegment("دُ")
                )
              ),
              KaidaWordItem(
                id = "c8_p1_w3",
                arabicWord = "الْعَالَمِينَ",
                pronunciationBn = "আল-আ’লামীন",
                pronunciationEn = "Al-'Aalameen",
                meaningBn = "সকল জাহানের",
                meaningEn = "Lord of the worlds",
                primaryTajweed = TajweedType.MADD,
                tajweedNoteBn = "আইনের ওপর খাড়া যবর এবং ইয়া-এর ওপর মাদ্দ",
                tajweedNoteEn = "Madd on Ayn and Madd on Meem",
                segments = listOf(
                  TajweedWordSegment("ا"),
                  TajweedWordSegment("لْ"),
                  TajweedWordSegment("عَا", TajweedType.MADD),
                  TajweedWordSegment("لَ"),
                  TajweedWordSegment("مِي", TajweedType.MADD),
                  TajweedWordSegment("نَ")
                )
              ),
              KaidaWordItem(
                id = "c8_p1_w4",
                arabicWord = "الْمُسْتَقِيمَ",
                pronunciationBn = "আল-মুস্তাক্বীম",
                pronunciationEn = "Al-Mustaqeem",
                meaningBn = "সরল ও সঠিক পথ",
                meaningEn = "The Straight Path",
                primaryTajweed = TajweedType.MADD,
                tajweedNoteBn = "সীন সাকিন, তা এবং ক্বাফের মাদ্দের সুন্দর মিলন",
                tajweedNoteEn = "Smooth transitions with Madd on Qaf",
                segments = listOf(
                  TajweedWordSegment("ا"),
                  TajweedWordSegment("لْ"),
                  TajweedWordSegment("مُ"),
                  TajweedWordSegment("سْ"),
                  TajweedWordSegment("تَ"),
                  TajweedWordSegment("قِي", TajweedType.MADD),
                  TajweedWordSegment("مَ")
                )
              )
            )
          )
        )
      ),

      // 9. Short Surahs Practice (আমপাড়া কায়দা পাঠ)
      KaidaChapter(
        id = 9,
        chapterNumber = 9,
        titleBn = "আমপাড়া কায়দা পাঠ (ছোট সূরা অনুশীলন)",
        titleEn = "Short Surahs Practice (Ampara)",
        titleAr = "قراءة قصار السور",
        subtitleBn = "সূরা ইখলাস, ফালাক্ব ও নাস এর আয়াতভিত্তিক বিশুদ্ধ কায়দা ও তাজবীদ প্রয়োগ।",
        subtitleEn = "Practical application of all Kaida rules on beloved short Surahs (Ikhlas, Falaq, Naas).",
        iconEmoji = "📖",
        themeColor = Color(0xFFD97706), // Amber
        primaryTajweedRule = TajweedType.NONE,
        pages = listOf(
          KaidaBookPage(
            pageNumber = 1,
            pageTitleBn = "সূরা আল-ইখলাস (Surah Al-Ikhlas)",
            pageTitleEn = "Surah Al-Ikhlas Practice",
            ruleFocusBn = "তৌহিদের মূল সূরা; কলকলাহ ও গুন্নাহর প্রত্যক্ষ প্রয়োগ।",
            ruleFocusEn = "Practical recitation highlighting Qalqalah bounces and Harakat.",
            words = listOf(
              KaidaWordItem(
                id = "c9_p1_w1",
                arabicWord = "قُلْ هُوَ اللَّهُ أَحَدٌ",
                pronunciationBn = "ক্বুল হুওয়াল্লা-হু আহাদ্ব্",
                pronunciationEn = "Qul Huwal-laahu Ahad",
                meaningBn = "বলুন, তিনিই আল্লাহ, একক ও অদ্বিতীয়",
                meaningEn = "Say, He is Allah, [who is] One",
                primaryTajweed = TajweedType.QALQALAH,
                tajweedNoteBn = "শেষে আহাদ্ব্ শব্দে দালের কলকলাহ প্রতিধ্বনি করুন",
                tajweedNoteEn = "Pronounce Qalqalah on Daal in Ahad",
                segments = listOf(
                  TajweedWordSegment("قُلْ هُوَ اللَّهُ "),
                  TajweedWordSegment("أَحَدٌ", TajweedType.QALQALAH)
                )
              ),
              KaidaWordItem(
                id = "c9_p1_w2",
                arabicWord = "اللَّهُ الصَّمَدُ",
                pronunciationBn = "আল্লা-হুস্‌ সামাদ্ব্",
                pronunciationEn = "Allahus-Samad",
                meaningBn = "আল্লাহ কারো মুখাপেক্ষী নন",
                meaningEn = "Allah, the Eternal Refuge",
                primaryTajweed = TajweedType.QALQALAH,
                tajweedNoteBn = "সোয়াদে মোটা ও দালে কলকলাহ ধাক্কা",
                tajweedNoteEn = "Heavy Saad and Qalqalah bounce on Daal",
                segments = listOf(
                  TajweedWordSegment("اللَّهُ الصَّ"),
                  TajweedWordSegment("مَدُ", TajweedType.QALQALAH)
                )
              ),
              KaidaWordItem(
                id = "c9_p1_w3",
                arabicWord = "لَمْ يَلِدْ وَلَمْ يُولَدْ",
                pronunciationBn = "লাম্ ইয়ালিদ্ব্ ওয়া লাম্ ইউলাদ্ব্",
                pronunciationEn = "Lam yalid wa lam yoolad",
                meaningBn = "তিনি কাউকে জন্ম দেননি এবং জন্ম নেননি",
                meaningEn = "He neither begets nor is born",
                primaryTajweed = TajweedType.QALQALAH,
                tajweedNoteBn = "ইয়ালিদ্ব্ এবং ইউলাদ্ব্ উভয়েই স্পষ্ট কলকলাহ",
                tajweedNoteEn = "Double Qalqalah on both Daal letters",
                segments = listOf(
                  TajweedWordSegment("لَمْ يَ"),
                  TajweedWordSegment("لِدْ", TajweedType.QALQALAH),
                  TajweedWordSegment(" وَلَمْ يُو"),
                  TajweedWordSegment("لَدْ", TajweedType.QALQALAH)
                )
              ),
              KaidaWordItem(
                id = "c9_p1_w4",
                arabicWord = "وَلَمْ يَكُن لَّهُ كُفُوًا أَحَدٌ",
                pronunciationBn = "ওয়া লাম্ ইয়াকুল্লাহু কুফুওয়ান আহাদ্ব্",
                pronunciationEn = "Wa lam yakul-lahoo kufuwan ahad",
                meaningBn = "এবং তাঁর সমকক্ষ কেউ নেই",
                meaningEn = "Nor is there to Him any equivalent",
                primaryTajweed = TajweedType.QALQALAH,
                tajweedNoteBn = "ইদগামে বেলা গুন্নাহ এবং শেষে আহাদ্ব্ এ কলকলাহ",
                tajweedNoteEn = "Idgham without Ghunnah + Qalqalah ending",
                segments = listOf(
                  TajweedWordSegment("وَلَمْ يَكُن لَّهُ كُفُوًا "),
                  TajweedWordSegment("أَحَدٌ", TajweedType.QALQALAH)
                )
              )
            )
          ),
          KaidaBookPage(
            pageNumber = 2,
            pageTitleBn = "সূরা আল-কাউসার (Surah Al-Kawthar)",
            pageTitleEn = "Surah Al-Kawthar Practice",
            ruleFocusBn = "কুরআনের সংক্ষিপ্ততম সূরা; নূন মুশদ্দাহ ও মাদ্দ অনুশীলন।",
            ruleFocusEn = "Shortest Surah in the Quran with vibrant Ghunnah and Madd.",
            words = listOf(
              KaidaWordItem(
                id = "c9_p2_w1",
                arabicWord = "إِنَّآ أَعْطَيْنَٰكَ الْكَوْثَرَ",
                pronunciationBn = "ইন্না- আ’ত়োয়না-কাল কাউছার",
                pronunciationEn = "Innaaa a'taynaakal-kawthar",
                meaningBn = "নিশ্চয় আমি আপনাকে কাউসার দান করেছি",
                meaningEn = "Indeed, We have granted you the Kawthar",
                primaryTajweed = TajweedType.GHUNNAH_IKHFA,
                tajweedNoteBn = "ইন্না-তে ওয়াজিব গুন্নাহ ও ৪ আলিফ মাদ্দ",
                tajweedNoteEn = "Wajib Ghunnah (Green) + 4 count Madd on Inna",
                segments = listOf(
                  TajweedWordSegment("إِ"),
                  TajweedWordSegment("نَّآ", TajweedType.GHUNNAH_IKHFA),
                  TajweedWordSegment(" أَعْطَيْ"),
                  TajweedWordSegment("نَٰ", TajweedType.MADD),
                  TajweedWordSegment("كَ الْكَوْثَرَ")
                )
              ),
              KaidaWordItem(
                id = "c9_p2_w2",
                arabicWord = "فَصَلِّ لِرَبِّكَ وَانْحَرْ",
                pronunciationBn = "ফাছাল্লি লিরব্বিকা ওয়ান্হার",
                pronunciationEn = "Fasalli li-rabbika wanhar",
                meaningBn = "অতএব আপনার রবের জন্য নামাজ পড়ুন ও কুরবানী করুন",
                meaningEn = "So pray to your Lord and sacrifice",
                primaryTajweed = TajweedType.NONE,
                tajweedNoteBn = "তাসদীদ ও ইযহার (নাক ছাড়া পরিষ্কার পড়া)",
                tajweedNoteEn = "Clear Izhar on Wanhar without Ghunnah",
                segments = listOf(
                  TajweedWordSegment("فَصَلِّ لِرَبِّكَ وَانْحَرْ")
                )
              ),
              KaidaWordItem(
                id = "c9_p2_w3",
                arabicWord = "إِنَّ شَانِئَكَ هُوَ الْأَبْتَرُ",
                pronunciationBn = "ইন্না শা-নিআকা হুওয়াল আবতার",
                pronunciationEn = "Inna shaani'aka huwal-abtar",
                meaningBn = "নিশ্চয় আপনার শত্রুই নির্বংশ",
                meaningEn = "Indeed, your enemy is the one cut off",
                primaryTajweed = TajweedType.GHUNNAH_IKHFA,
                tajweedNoteBn = "ইন্না-তে ওয়াজিব গুন্নাহ এবং আবতার-এ কলকলাহ",
                tajweedNoteEn = "Ghunnah on Inna and Qalqalah on Abtar",
                segments = listOf(
                  TajweedWordSegment("إِ"),
                  TajweedWordSegment("نَّ", TajweedType.GHUNNAH_IKHFA),
                  TajweedWordSegment(" شَانِئَكَ هُوَ الْ"),
                  TajweedWordSegment("أَبْ", TajweedType.QALQALAH),
                  TajweedWordSegment("تَرُ")
                )
              )
            )
          )
        )
      )
    )
  }
}
