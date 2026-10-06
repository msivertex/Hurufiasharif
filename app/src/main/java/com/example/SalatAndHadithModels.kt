package com.example

import com.example.islamic.DailyPrayerSchedule
import com.example.islamic.PrayerEntry

typealias PrayerSchedule = DailyPrayerSchedule

val DailyPrayerSchedule.sahriEndFormatted: String
  get() = fajr.timeFormatted

val DailyPrayerSchedule.iftarFormatted: String
  get() = maghrib.timeFormatted

val DailyPrayerSchedule.allEntries: List<PrayerEntry>
  get() = allPrayers()

data class SalatStep(
  val stepNumber: Int,
  val titleEn: String,
  val titleBn: String,
  val titleAr: String,
  val arabicPhrase: String,
  val pronunciationBn: String,
  val pronunciationEn: String,
  val meaningBn: String,
  val meaningEn: String,
  val instructionBn: String,
  val instructionEn: String,
  val iconEmoji: String
)

data class PrayerRakatInfo(
  val prayerNameEn: String,
  val prayerNameBn: String,
  val iconEmoji: String,
  val sunnahMuakkadah: Int,
  val farz: Int,
  val sunnahGhairMuakkadah: Int = 0,
  val nafl: Int = 0,
  val witr: Int = 0,
  val totalRakats: Int
)

data class HadithItem(
  val id: String,
  val categoryKey: String,
  val categoryNameBn: String,
  val categoryNameEn: String,
  val titleBn: String,
  val titleEn: String,
  val arabicText: String,
  val translationBn: String,
  val translationEn: String,
  val narrator: String,
  val sourceReference: String,
  val virtueBn: String
)

data class DailyDuaItem(
  val id: String,
  val titleBn: String,
  val titleEn: String,
  val occasion: String,
  val arabicText: String,
  val pronunciationBn: String,
  val translationBn: String,
  val translationEn: String,
  val reference: String,
  val virtue: String
)

data class VirtuousDeedItem(
  val id: String,
  val titleBn: String,
  val titleEn: String,
  val iconEmoji: String,
  val descriptionBn: String,
  val hadithProofBn: String,
  val rewardBn: String,
  val category: String
)

object SalatRepository {
  val rakatList: List<PrayerRakatInfo> = listOf(
    PrayerRakatInfo(
      prayerNameEn = "Fajr",
      prayerNameBn = "ফজর",
      iconEmoji = "🌅",
      sunnahMuakkadah = 2,
      farz = 2,
      totalRakats = 4
    ),
    PrayerRakatInfo(
      prayerNameEn = "Dhuhr",
      prayerNameBn = "যোহর",
      iconEmoji = "☀️",
      sunnahMuakkadah = 6, // 4 before + 2 after
      farz = 4,
      nafl = 2,
      totalRakats = 12
    ),
    PrayerRakatInfo(
      prayerNameEn = "Asr",
      prayerNameBn = "আসর",
      iconEmoji = "🌤️",
      sunnahMuakkadah = 0,
      sunnahGhairMuakkadah = 4,
      farz = 4,
      totalRakats = 8
    ),
    PrayerRakatInfo(
      prayerNameEn = "Maghrib",
      prayerNameBn = "মাগরিব",
      iconEmoji = "🌇",
      sunnahMuakkadah = 2,
      farz = 3,
      nafl = 2,
      totalRakats = 7
    ),
    PrayerRakatInfo(
      prayerNameEn = "Isha",
      prayerNameBn = "ইশা",
      iconEmoji = "🌙",
      sunnahMuakkadah = 2,
      sunnahGhairMuakkadah = 4,
      farz = 4,
      witr = 3,
      nafl = 4,
      totalRakats = 17
    ),
    PrayerRakatInfo(
      prayerNameEn = "Jummah",
      prayerNameBn = "জুমু'আ",
      iconEmoji = "🕌",
      sunnahMuakkadah = 8, // 4 kablal + 4 ba'dal
      farz = 2,
      nafl = 2,
      totalRakats = 12
    ),
    PrayerRakatInfo(
      prayerNameEn = "Tahajjud",
      prayerNameBn = "তাহাজ্জুদ",
      iconEmoji = "✨",
      sunnahMuakkadah = 0,
      farz = 0,
      nafl = 8,
      totalRakats = 8
    )
  )

  val stepsList: List<SalatStep> = listOf(
    SalatStep(
      stepNumber = 1,
      titleEn = "Intention (Niyyah)",
      titleBn = "নিয়ত করা",
      titleAr = "النية",
      arabicPhrase = "نَوَيْتُ أَنْ أُصَلِّيَ لِلَّهِ تَعَالَى",
      pronunciationBn = "নাওয়াইতু আন উসাল্লিয়া লিল্লাহি তাআলা...",
      pronunciationEn = "Nawaytu an usalliya lillahi ta'ala...",
      meaningBn = "আমি আল্লাহর সন্তুষ্টির উদ্দেশ্যে নির্দিষ্ট ওয়াক্তের নামাজ আদায় করার সংকল্প করছি।",
      meaningEn = "I intend to pray for the sake of Allah the Almighty.",
      instructionBn = "মনে মনে নির্দিষ্ট ওয়াক্তের নামাজ পড়ার ইচ্ছা পোষণ করা ফরজ। মুখে উচ্চারণ করা মুস্তাহাব।",
      instructionEn = "Make the sincere intention in your heart for the specific prayer.",
      iconEmoji = "🤲"
    ),
    SalatStep(
      stepNumber = 2,
      titleEn = "Takbeer Tahrimah",
      titleBn = "তাকবীরে তাহরীমা",
      titleAr = "تكبيرة الإحرام",
      arabicPhrase = "اللهُ أَكْبَرُ",
      pronunciationBn = "আল্লাহু আকবার",
      pronunciationEn = "Allahu Akbar",
      meaningBn = "আল্লাহ সর্বশ্রেষ্ঠ।",
      meaningEn = "Allah is the Greatest.",
      instructionBn = "পুরুষরা কানের লতি পর্যন্ত এবং নারীরা কাঁধ পর্যন্ত দুই হাত উঠিয়ে 'আল্লাহু আকবার' বলে হাত বাঁধবেন।",
      instructionEn = "Raise both hands to ear lobes (men) or shoulders (women) saying Allahu Akbar, then fold hands.",
      iconEmoji = "🙌"
    ),
    SalatStep(
      stepNumber = 3,
      titleEn = "Sana & Ta'awwuz",
      titleBn = "ছানা পাঠ ও তাআউয়ুজ",
      titleAr = "دعاء الاستفتاح",
      arabicPhrase = "سُبْحَانَكَ اللَّهُمَّ وَبِحَمْدِكَ وَتَبَارَكَ اسْمُكَ وَتَعَالَى جَدُّكَ وَلَا إِلَهَ غَيْرُكَ",
      pronunciationBn = "সুবহানাকাল্লাহুম্মা ওয়া বিহামদিকা, ওয়া তাবারাকাসমুকা, ওয়া তাআলা জাদ্দুকা, ওয়া লা ইলাহা গাইরুক।",
      pronunciationEn = "Subhanaka Allahumma wa bihamdika wa tabarakasmuka wa ta'ala jadduka wa la ilaha ghairuk.",
      meaningBn = "হে আল্লাহ! আপনার প্রশংসার সাথে আপনার পবিত্রতা ঘোষণা করছি। আপনার নাম বরকতময়, আপনার মর্যাদা সুউচ্চ এবং আপনি ব্যতীত কোনো ইলাহ নেই।",
      meaningEn = "Glory and praise be to You, O Allah. Blessed is Your name, exalted is Your majesty, and there is no god but You.",
      instructionBn = "হাত বাঁধার পর প্রথম রাকাতে চুপে চুপে ছানা পাঠ করুন, অতঃপর আউযুবিল্লাহ ও বিসমিল্লাহ পড়ুন।",
      instructionEn = "Recite Sana quietly, followed by Ta'awwudh and Tasmiyah.",
      iconEmoji = "📖"
    ),
    SalatStep(
      stepNumber = 4,
      titleEn = "Surah Al-Fatihah & Qira'at",
      titleBn = "সুরা ফাতিহা ও কেরাত",
      titleAr = "قراءة الفاتحة والسورة",
      arabicPhrase = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ • الْحَمْدُ لِلَّهِ رَبِّ الْعَالَمِينَ...",
      pronunciationBn = "আলহামদু লিল্লাহি রাব্বিল আলামিন...",
      pronunciationEn = "Alhamdu lillahi Rabbil 'alamin...",
      meaningBn = "সকল প্রশংসা জগতসমূহের প্রতিপালক আল্লাহর জন্য...",
      meaningEn = "All praise is due to Allah, Lord of all the worlds...",
      instructionBn = "সুরা ফাতিহা পাঠ করা ওয়াজিব। এরপর চুপে 'আমিন' বলুন এবং যেকোনো সুরা বা অন্তত ৩টি ছোট আয়াত মিলান।",
      instructionEn = "Recite Surah Al-Fatihah, say Ameen, then recite at least three verses of the Quran.",
      iconEmoji = "📜"
    ),
    SalatStep(
      stepNumber = 5,
      titleEn = "Ruku (Bowing)",
      titleBn = "রুকু ও রুকুর তাসবীহ",
      titleAr = "الركوع",
      arabicPhrase = "سُبْحَانَ رَبِّيَ الْعَظِيمِ (৩ বার)",
      pronunciationBn = "সুবহানা রাব্বিয়াল আজীম (৩ বার)",
      pronunciationEn = "Subhana Rabbiyal 'Azeem (3 times)",
      meaningBn = "আমার মহান প্রতিপালকের পবিত্রতা ঘোষণা করছি।",
      meaningEn = "Glory to my Lord the Magnificent.",
      instructionBn = "'আল্লাহু আকবার' বলে রুকুতে যান। পিঠ সোজা রাখুন, হাত দিয়ে হাঁটু শক্তভাবে ধরুন এবং অন্তত ৩ বার তাসবীহ পড়ুন।",
      instructionEn = "Bow with straight back, gripping knees with fingers spread, and recite at least 3 times.",
      iconEmoji = "🙇‍♂️"
    ),
    SalatStep(
      stepNumber = 6,
      titleEn = "Qawmah (Standing Straight)",
      titleBn = "কওমা (রুকু থেকে সোজা হওয়া)",
      titleAr = "الاعتدال",
      arabicPhrase = "سَمِعَ اللَّهُ لِمَنْ حَمِدَهُ • رَبَّنَا لَكَ الْحَمْدُ",
      pronunciationBn = "সামিআল্লাহু লিমান হামিদাহ • রাব্বানা লাকাল হামদ",
      pronunciationEn = "Sami' Allahu liman hamidah • Rabbana lakal hamd",
      meaningBn = "আল্লাহ শুনেন যে তাঁর প্রশংসা করে • হে আমাদের প্রতিপালক! সমস্ত প্রশংসা আপনারই জন্য।",
      meaningEn = "Allah hears whoever praises Him • Our Lord, to You belongs all praise.",
      instructionBn = "রুকু থেকে সোজা হয়ে দাঁড়ান। শরীর স্থির হওয়া ওয়াজিব।",
      instructionEn = "Stand completely upright in full calmness before moving to prostration.",
      iconEmoji = "🧍"
    ),
    SalatStep(
      stepNumber = 7,
      titleEn = "Sujood (Prostration)",
      titleBn = "সিজদা ও সিজদার তাসবীহ",
      titleAr = "السجود",
      arabicPhrase = "سُبْحَانَ رَبِّيَ الْأَعْلَى (৩ বার)",
      pronunciationBn = "সুবহানা রাব্বিয়াল আ'লা (৩ বার)",
      pronunciationEn = "Subhana Rabbiyal A'la (3 times)",
      meaningBn = "আমার সর্বোচ্চ প্রতিপালকের পবিত্রতা ঘোষণা করছি।",
      meaningEn = "Glory to my Lord the Most High.",
      instructionBn = "'আল্লাহু আকবার' বলে হাঁটু, হাত, নাক ও কপাল মাটিতে রাখুন। ৭টি অঙ্গ মাটিতে স্পর্শ করানো ওয়াজিব। ৩ বার তাসবীহ পড়ুন।",
      instructionEn = "Prostrate with forehead, nose, palms, knees, and toes touching the ground. Recite 3 times.",
      iconEmoji = "🧎"
    ),
    SalatStep(
      stepNumber = 8,
      titleEn = "Jalsah (Sitting between Sujood)",
      titleBn = "জলসা (দুই সিজদার মধ্যবর্তী বৈঠক)",
      titleAr = "الجلسة بين السجدتين",
      arabicPhrase = "رَبِّ اغْفِرْ لِي وَارْحَمْنِي وَعَافِنِي وَارْزُقْنِي",
      pronunciationBn = "রাব্বিগফির লী, ওয়ারহামনী, ওয়া 'আফিনী, ওয়ারযুক্বনী",
      pronunciationEn = "Rabbighfir lee, warhamnee, wa 'aafinee, warzuqnee",
      meaningBn = "হে আমার রব! আমাকে ক্ষমা করুন, আমার ওপর দয়া করুন, আমাকে সুস্থতা ও রিজিক দান করুন।",
      meaningEn = "O my Lord! Forgive me, have mercy on me, grant me well-being and provide for me.",
      instructionBn = "প্রথম সিজদা শেষে সোজা হয়ে বসুন। অন্তত একটি তাসবীহ পরিমাণ স্থির থাকা ওয়াজিব। এরপর দ্বিতীয় সিজদা করুন।",
      instructionEn = "Sit up completely straight and pause calmly before making the second prostration.",
      iconEmoji = "🪑"
    ),
    SalatStep(
      stepNumber = 9,
      titleEn = "Tashahhud (At-Tahiyyat)",
      titleBn = "তাশাহহুদ (আত্তাহিয়্যাতু)",
      titleAr = "التشهد",
      arabicPhrase = "التَّحِيَّاتُ لِلَّهِ وَالصَّلَوَاتُ وَالطَّيِّبَاتُ، السَّلَامُ عَلَيْكَ أَيُّهَا النَّبِيُّ وَرَحْمَةُ اللَّهِ وَبَرَكَاتُهُ...",
      pronunciationBn = "আত্তাহিয়্যাতু লিল্লাহি ওয়াস সালাওয়াতু ওয়াত ত্বায়্যিবাতু, আসসালামু আলাইকা আইয়্যুহান নাবিয়্যু...",
      pronunciationEn = "At-tahiyyatu lillahi was-salawatu wat-tayyibatu, as-salamu 'alayka ayyuhan-Nabiyyu...",
      meaningBn = "সকল মৌখিক, শারীরিক ও আর্থিক ইবাদত একমাত্র আল্লাহর জন্য। হে নবী! আপনার প্রতি শান্তি, রহমত ও বরকত বর্ষিত হোক...",
      meaningEn = "All compliments, prayers and pure words are due to Allah. Peace be upon you, O Prophet...",
      instructionBn = "দ্বিতীয় বা শেষ রাকাতে বসে তাশাহহুদ পাঠ করুন। 'আশহাদু আল্লা ইলাহা' বলার সময় শাহাদাত আঙুল উঠান।",
      instructionEn = "Sit for Tashahhud. Raise the index finger upon bearing witness to the Oneness of Allah.",
      iconEmoji = "☝️"
    ),
    SalatStep(
      stepNumber = 10,
      titleEn = "Durood Ibrahim & Dua Masura",
      titleBn = "দরূদে ইবরাহিম ও দোয়া মাছুরা",
      titleAr = "الصلاة الإبراهيمية والدعاء",
      arabicPhrase = "اللَّهُمَّ صَلِّ عَلَى مُحَمَّدٍ وَعَلَى آلِ مُحَمَّدٍ كَمَا صَلَّيْتَ عَلَى إِبْرَاهِيمَ...",
      pronunciationBn = "আল্লাহুম্মা সাল্লি আলা মুহাম্মাদিউঁ ওয়া আলা আলি মুহাম্মাদ...",
      pronunciationEn = "Allahumma salli 'ala Muhammadin wa 'ala aali Muhammad...",
      meaningBn = "হে আল্লাহ! মুহাম্মদ (সা.) ও তাঁর বংশধরদের ওপর রহমত বর্ষণ করুন যেভাবে ইবরাহিম (আ.)-এর ওপর বর্ষণ করেছিলেন...",
      meaningEn = "O Allah! Send prayers upon Muhammad and upon the family of Muhammad as You sent prayers upon Ibrahim...",
      instructionBn = "শেষ বৈঠকে তাশাহহুদের পর দরূদে ইবরাহিম ও কুরআন-হাদিসের যেকোনো মাছুরা দোয়া পড়ুন।",
      instructionEn = "Recite Durood Ibrahim followed by Dua Masura in the final sitting.",
      iconEmoji = "💚"
    ),
    SalatStep(
      stepNumber = 11,
      titleEn = "Taslim (Ending with Salam)",
      titleBn = "সালাম ফিরিয়ে নামাজ সমাপ্তি",
      titleAr = "التسليم",
      arabicPhrase = "السَّلَامُ عَلَيْكُمْ وَرَحْمَةُ اللَّهِ",
      pronunciationBn = "আসসালামু আলাইকুম ওয়া রাহমাতুল্লাহ",
      pronunciationEn = "As-salamu 'alaykum wa rahmatullah",
      meaningBn = "আপনাদের ওপর আল্লাহর শান্তি ও রহমত বর্ষিত হোক।",
      meaningEn = "May peace and mercy of Allah be upon you.",
      instructionBn = "প্রথমে ডানে কাঁধের দিকে মুখ ফিরিয়ে সালাম বলুন, অতঃপর বাম দিকে কাঁধের দিকে মুখ ফিরিয়ে সালাম বলুন।",
      instructionEn = "Turn your face to the right shoulder saying Salam, then to the left shoulder.",
      iconEmoji = "🤝"
    )
  )
}

object HadithRepository {
  val hadithList: List<HadithItem> = listOf(
    HadithItem(
      id = "h1",
      categoryKey = "niyyah",
      categoryNameBn = "ঈমান ও নিয়ত",
      categoryNameEn = "Faith & Intention",
      titleBn = "সকল কাজের ভিত্তি নিয়ত",
      titleEn = "Actions are Judged by Intentions",
      arabicText = "إِنَّمَا الْأَعْمَالُ بِالنِّيَّاتِ، وَإِنَّمَا لِكُلِّ امْرِئٍ مَا نَوَى",
      translationBn = "নিশ্চয়ই সকল কাজের ফলাফল নিয়তের ওপর নির্ভরশীল। প্রত্যেক মানুষ তাই পাবে যা সে নিয়ত করে।",
      translationEn = "Actions are according to intentions, and every person will have what he has intended.",
      narrator = "উমর ইবনুল খাত্তাব (রা.)",
      sourceReference = "সহিহ বুখারি: ১, সহিহ মুসলিম: ১৯০৭",
      virtueBn = "যেকোনো নেক কাজের পূর্বে খাঁটি নিয়ত করলে আল্লাহর দরবারে সওয়াব নিশ্চিত হয়।"
    ),
    HadithItem(
      id = "h2",
      categoryKey = "quran",
      categoryNameBn = "কুরআন শিক্ষা",
      categoryNameEn = "Quran Learning",
      titleBn = "সর্বোত্তম ব্যক্তি যে কুরআন শেখে ও শেখায়",
      titleEn = "The Best Among You Learns the Quran",
      arabicText = "خَيْرُكُمْ مَنْ تَعَلَّمَ الْقُرْآنَ وَعَلَّمَهُ",
      translationBn = "তোমাদের মধ্যে সর্বোত্তম ব্যক্তি সেই, যে নিজে কুরআন শেখে এবং অন্যকে তা শিক্ষা দেয়।",
      translationEn = "The best among you are those who learn the Quran and teach it to others.",
      narrator = "উসমান ইবনে আফফান (রা.)",
      sourceReference = "সহিহ বুখারি: ৫০২৭",
      virtueBn = "হরুফিয়া শরিফ অ্যাপের মাধ্যমে হরফ ও কুরআন চর্চা এই হাদিসের মহৎ সওয়াবের অংশীদার করে।"
    ),
    HadithItem(
      id = "h3",
      categoryKey = "salat",
      categoryNameBn = "সালাত ও তাহারাত",
      categoryNameEn = "Prayer & Purity",
      titleBn = "সালাত মুমিনের চোখের শীতলতা",
      titleEn = "Prayer is the Pillar of Religion",
      arabicText = "الطُّهُورُ شَطْرُ الإِيمَانِ، وَالصَّلَاةُ نُورٌ",
      translationBn = "পবিত্রতা ঈমানের অর্ধেক এবং নামাজ হলো (জীবনের) জ্যোতি ও আলো।",
      translationEn = "Purity is half of faith, and prayer is a shining light.",
      narrator = "আবু মালেক আশআরি (রা.)",
      sourceReference = "সহিহ মুসলিম: ২২৩",
      virtueBn = "নিয়মিত পাঁচ ওয়াক্ত নামাজ আদায় অন্তরে নূর ও জীবনে বরকত এনে দেয়।"
    ),
    HadithItem(
      id = "h4",
      categoryKey = "akhlaq",
      categoryNameBn = "উত্তম চরিত্র",
      categoryNameEn = "Noble Character",
      titleBn = "ঈমানে সর্বাধিক পূর্ণাঙ্গ মুমিন",
      titleEn = "Perfection of Faith in Good Character",
      arabicText = "أَكْمَلُ الْمُؤْمِنِينَ إِيمَانًا أَحْسَنُهُمْ خُلُقًا",
      translationBn = "মুমিনদের মধ্যে ঈমানের দিক থেকে সবচেয়ে পূর্ণাঙ্গ সেই ব্যক্তি, যার চরিত্র সবচেয়ে সুন্দর।",
      translationEn = "The most complete of believers in faith are those with the best character.",
      narrator = "আবু হুরায়রা (রা.)",
      sourceReference = "জামে তিরমিযী: ১১৬২ (সহিহ)",
      virtueBn = "পিতা-মাতা, পরিবার ও সকল সৃষ্টির সাথে সদাচরণ ও বিনম্র ভাষা বজায় রাখা ঈমানের পূর্ণতা।"
    ),
    HadithItem(
      id = "h5",
      categoryKey = "charity",
      categoryNameBn = "দান ও সদকা",
      categoryNameEn = "Charity & Kindness",
      titleBn = "হাসিমুখে কথা বলাও সদকা",
      titleEn = "Smiling at Your Brother is Charity",
      arabicText = "تَبَسُّمُكَ فِي وَجْهِ أَخِيكَ لَكَ صَدَقَةٌ",
      translationBn = "তোমার ভাইয়ের মুখের দিকে তাকিয়ে তোমার মুচকি হাসাও একটি সদকা (পুণ্যের কাজ)।",
      translationEn = "Your smiling in the face of your brother is charity for you.",
      narrator = "আবু যর গিফারী (রা.)",
      sourceReference = "জামে তিরমিযী: ১৯৫৬ (হাসান সহিহ)",
      virtueBn = "ছোট ছোট সদয় আচরণও মিজানের পাল্লায় ভারী নেকি হিসেবে পরিগণিত হয়।"
    ),
    HadithItem(
      id = "h6",
      categoryKey = "dua",
      categoryNameBn = "যিকির ও দোয়া",
      categoryNameEn = "Remembrance & Dua",
      titleBn = "দোয়া হলো ইবাদতের মূলমজ্জা",
      titleEn = "Supplication is Worship",
      arabicText = "الدُّعَاءُ هُوَ الْعِبَادَةُ",
      translationBn = "দোয়াই হলো ইবাদতের মূল নির্যাস ও স্বরূপ।",
      translationEn = "Supplication (Dua) itself is worship.",
      narrator = "নুমান ইবনে বাশীর (রা.)",
      sourceReference = "সুনানে আবু দাউদ: ১৪৭৯, তিরমিযী: ২৯৬৯",
      virtueBn = "আল্লাহর কাছে সরাসরি যেকোনো বিপদে বা প্রয়োজনেই দোয়ার হাত তোলা সর্বোৎকৃষ্ট ইবাদত।"
    )
  )

  val dailyDuas: List<DailyDuaItem> = listOf(
    DailyDuaItem(
      id = "d1",
      titleBn = "পিতা-মাতার জন্য সর্বোত্তম দোয়া",
      titleEn = "Dua for Parents",
      occasion = "প্রতি নামাজের পর ও প্রতিদিন",
      arabicText = "رَّبِّ ارْحَمْهُمَا كَمَا رَبَّيَانِي صَغِيرًا",
      pronunciationBn = "রাব্বির হামহুমা কামা রাব্বায়ানী সাগীরা",
      translationBn = "হে আমার প্রতিপালক! তাঁদের উভয়ের প্রতি রহম করুন, যেমন তাঁরা শৈশবে আমাকে লালন-পালন করেছেন।",
      translationEn = "My Lord, have mercy upon them as they brought me up when I was small.",
      reference = "সুরা আল-ইসরা: ২৪",
      virtue = "পিতা-মাতার জন্য এই দোয়া পাঠ করলে সন্তানের প্রতি পিতামাতার হক ও আল্লাহর রহমত অর্জিত হয়।"
    ),
    DailyDuaItem(
      id = "d2",
      titleBn = "সাইয়্যিদুল ইস্তিগফার (শ্রেষ্ঠ ক্ষমা প্রার্থনা)",
      titleEn = "Sayyidul Istighfar",
      occasion = "সকাল ও সন্ধ্যায় পাঠ্য",
      arabicText = "اللَّهُمَّ أَنْتَ رَبِّي لَا إِلَهَ إِلَّا أَنْتَ، خَلَقْتَنِي وَأَنَا عَبْدُكَ...",
      pronunciationBn = "আল্লাহুম্মা আনতা রাব্বী লা ইলাহা ইল্লা আনতা, খালাকতানী ওয়া আনা আবদুকা...",
      translationBn = "হে আল্লাহ! আপনি আমার রব, আপনি ছাড়া কোনো ইলাহ নেই। আপনি আমাকে সৃষ্টি করেছেন এবং আমি আপনার বান্দা...",
      translationEn = "O Allah, You are my Lord, there is no god but You. You created me and I am Your servant...",
      reference = "সহিহ বুখারি: ৬৩০৬",
      virtue = "যে ব্যক্তি সকাল বা সন্ধ্যায় দৃঢ় বিশ্বাসের সাথে এটি পড়বে এবং মারা যাবে, সে জান্নাতে প্রবেশ করবে।"
    ),
    DailyDuaItem(
      id = "d3",
      titleBn = "ইলম ও জ্ঞানের বৃদ্ধির দোয়া",
      titleEn = "Dua for Knowledge",
      occasion = "পড়ালেখা ও কুরআন শিক্ষার শুরুতে",
      arabicText = "رَبِّ زِدْنِي عِلْمًا",
      pronunciationBn = "রাব্বি যিদনী ইলমা",
      translationBn = "হে আমার প্রতিপালক! আমার জ্ঞান বাড়িয়ে দিন।",
      translationEn = "My Lord! Increase me in knowledge.",
      reference = "সুরা ত্ব-হা: ১১৪",
      virtue = "নবী করিম (সা.)-কে আল্লাহ তায়ালা এই দোয়ার নির্দেশ দিয়েছিলেন মেধা ও প্রজ্ঞার বরকতের জন্য।"
    )
  )

  val dailyDeeds: List<VirtuousDeedItem> = listOf(
    VirtuousDeedItem(
      id = "deed1",
      titleBn = "ফরজ নামাজের পর আয়াতুল কুরসি পাঠ",
      titleEn = "Reciting Ayatul Kursi After Prayer",
      iconEmoji = "👑",
      descriptionBn = "প্রতিটি ফরজ নামাজান্তে সালাম ফিরিয়ে একবার আয়াতুল কুরসি তিলাওয়াত করুন।",
      hadithProofBn = "রাসুলুল্লাহ (সা.) বলেছেন: 'যে ব্যক্তি প্রত্যেক ফরজ নামাজের পর আয়াতুল কুরসি পড়বে, তার জান্নাতে প্রবেশের পথে একমাত্র মৃত্যু ছাড়া আর কোনো বাধা থাকবে না।' (নাসাঈ কুবরা)",
      rewardBn = "মৃত্যুর সাথে সাথেই জান্নাত লাভের খোশখবর।",
      category = "সালাত পরবর্তী আমল"
    ),
    VirtuousDeedItem(
      id = "deed2",
      titleBn = "তাসবীহে ফাতেমী (৩৩-৩৩-৩৪)",
      titleEn = "Tasbeeh Fatimi",
      iconEmoji = "📿",
      descriptionBn = "নামাজ শেষে ৩৩ বার সুবহানাল্লাহ, ৩৩ বার আলহামদুলিল্লাহ এবং ৩৪ বার আল্লাহু আকবার পাঠ করা।",
      hadithProofBn = "রাসুলুল্লাহ (সা.) প্রিয় কন্যা ফাতেমা (রা.) ও সাহাবিদের এই আমলের তাগিদ দিয়েছিলেন ক্লান্তি দূরীকরণ ও পাপ মোচনে। (বুখারি ও মুসলিম)",
      rewardBn = "সমুদ্রের ফেনা পরিমাণ গুনাহ হলেও ক্ষমা করা হয় এবং আত্মিক শক্তি লাভ হয়।",
      category = "দৈনিক যিকির"
    ),
    VirtuousDeedItem(
      id = "deed3",
      titleBn = "রাত্রে সুরা মুলক তিলাওয়াত",
      titleEn = "Surah Al-Mulk at Night",
      iconEmoji = "🌌",
      descriptionBn = "প্রতি রাতে ঘুমানোর আগে ৩০ আয়াতের সুরা আল-মুলক তিলাওয়াত করা।",
      hadithProofBn = "নবীজি (সা.) বলেছেন: 'কুরআনে ৩০ আয়াতের একটি সুরা আছে যা পাঠকারীর জন্য শাফায়াত করে যতক্ষণ না তাকে ক্ষমা করে দেওয়া হয়।' (তিরমিযী)",
      rewardBn = "কবরের আজাব থেকে সুরক্ষা ও ক্ষমা প্রাপ্তি।",
      category = "রাত্রিকালীন আমল"
    )
  )
}
