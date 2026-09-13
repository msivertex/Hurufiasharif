package com.example

import androidx.compose.ui.graphics.Color

object ShapeMasterRoadmapRepository {

  val levels: List<RoadmapLevel> by lazy {
    val alif = ArabicAlphabetRepository.letters[0]  // 1. Alif
    val baa = ArabicAlphabetRepository.letters[1]   // 2. Baa
    val taa = ArabicAlphabetRepository.letters[2]   // 3. Taa
    val thaa = ArabicAlphabetRepository.letters[3]  // 4. Thaa
    val jeem = ArabicAlphabetRepository.letters[4]  // 5. Jeem
    val haa = ArabicAlphabetRepository.letters[5]   // 6. Haa
    val khaa = ArabicAlphabetRepository.letters[6]  // 7. Khaa
    val daal = ArabicAlphabetRepository.letters[7]  // 8. Daal
    val dhaal = ArabicAlphabetRepository.letters[8] // 9. Dhaal
    val raa = ArabicAlphabetRepository.letters[9]   // 10. Raa
    val zaay = ArabicAlphabetRepository.letters[10] // 11. Zaay
    val seen = ArabicAlphabetRepository.letters[11] // 12. Seen
    val sheen = ArabicAlphabetRepository.letters[12]// 13. Sheen
    val saad = ArabicAlphabetRepository.letters[13] // 14. Saad
    val daad = ArabicAlphabetRepository.letters[14] // 15. Daad

    listOf(
      // Level 1: Alif & Baa
      RoadmapLevel(
        id = 1,
        titleEn = "Level 1: Alif & Baa",
        titleBn = "লেভেল ১: আলিফ ও বা",
        titleAr = "المستوى ١: ألف وباء",
        subtitleEn = "Isolated & Initial shapes with baseline stroke",
        subtitleBn = "আলিফ ও বা এর বিচ্ছিন্ন এবং প্রারম্ভিক রূপ",
        subtitleAr = "الأشكال المنفصلة وبداية الكلمة",
        iconEmoji = "🌱",
        nodeColor = Color(0xFF0A5C36), // Royal Emerald
        isCheckpoint = false,
        questions = listOf(
          ShapeQuizQuestion(
            id = "q1_1",
            type = QuizQuestionType.SHAPE_IDENTIFICATION,
            targetLetter = baa,
            targetFormType = FormType.INITIAL,
            promptEn = "Which one is the Initial (beginning) form of Baa (ب)?",
            promptBn = "'বা' (ب) হরফের শুরুতে বসার প্রারম্ভিক রূপ কোনটি?",
            promptAr = "ما هو شكل حرف الباء (ب) في بداية الكلمة؟",
            options = listOf(
              ShapeOption("بـ", FormType.INITIAL, "Baa (Initial)", true),
              ShapeOption("ـبـ", FormType.MEDIAL, "Baa (Medial)", false),
              ShapeOption("ـب", FormType.FINAL, "Baa (Final)", false),
              ShapeOption("ب", FormType.ISOLATED, "Baa (Isolated)", false)
            ),
            explanationEn = "'بـ' is the Initial form. It keeps the tooth and dot below, with a connecting arm to the left.",
            explanationBn = "'بـ' হলো বা-এর প্রারম্ভিক রূপ। এর নিচে একটি নুকতা থাকে এবং বামে যুক্ত হওয়ার বাহু থাকে।",
            explanationAr = "الحرف 'بـ' هو شكل الباء في أول الكلمة، ويتصل بالحرف الذي يليه من اليسار."
          ),
          ShapeQuizQuestion(
            id = "q1_2",
            type = QuizQuestionType.SHAPE_IDENTIFICATION,
            targetLetter = alif,
            targetFormType = FormType.ISOLATED,
            promptEn = "Identify the Isolated form of Alif (ا):",
            promptBn = "'আলিফ' (ا) হরফের মূল বিচ্ছিন্ন রূপটি শনাক্ত করুন:",
            promptAr = "حدد الشكل المنفصل لحرف الألف (ا):",
            options = listOf(
              ShapeOption("ا", FormType.ISOLATED, "Alif (Isolated)", true),
              ShapeOption("ـا", FormType.FINAL, "Alif (Final)", false),
              ShapeOption("بـ", FormType.INITIAL, "Baa (Initial)", false),
              ShapeOption("ـب", FormType.FINAL, "Baa (Final)", false)
            ),
            explanationEn = "'ا' is the simple upright vertical stroke that does not connect to any following letter.",
            explanationBn = "'ا' হলো সোজা খাড়া দণ্ড যা কোনো পরবর্তী হরফের সাথে বামে জোড়া লাগে না।",
            explanationAr = "الألف 'ا' حرف عمودي قائم لا يتصل بما بعده."
          ),
          ShapeQuizQuestion(
            id = "q1_3",
            type = QuizQuestionType.MATCHING_PAIRS,
            targetLetter = baa,
            targetFormType = FormType.INITIAL,
            promptEn = "Match each isolated letter to its connecting shape:",
            promptBn = "বিচ্ছিন্ন হরফগুলোর সাথে এদের যুক্ত হওয়ার রূপ জোড়া লাগান:",
            promptAr = "طابق كل حرف منفصل مع شكله المتصل المناسب:",
            pairs = listOf(
              MatchingPairItem("p1", "ب", "بـ", FormType.INITIAL, "Baa"),
              MatchingPairItem("p2", "ا", "ـا", FormType.FINAL, "Alif"),
              MatchingPairItem("p3", "ب", "ـبـ", FormType.MEDIAL, "Baa")
            ),
            explanationEn = "Notice how Baa extends a stroke while Alif only connects from the right.",
            explanationBn = "লক্ষ্য করুন বা-এর বাহু প্রসারিত হয়, কিন্তু আলিফ কেবল ডান পাশ থেকে যুক্ত হতে পারে।",
            explanationAr = "لاحظ أن الباء تمتد لوصل الحروف بينما الألف تتصل فقط من اليمين."
          )
        )
      ),

      // Level 2: Taa & Thaa
      RoadmapLevel(
        id = 2,
        titleEn = "Level 2: Taa & Thaa",
        titleBn = "লেভেল ২: তা ও ছা",
        titleAr = "المستوى ٢: تاء وثاء",
        subtitleEn = "Two dots (ت) and three dots (ث) positions",
        subtitleBn = "দুই নুকতা ও তিন নুকতার অবস্থান ও রূপভেদ",
        subtitleAr = "مواضع النقطتين والثلاث نقاط",
        iconEmoji = "🎯",
        nodeColor = Color(0xFF1E40AF), // Deep Blue
        isCheckpoint = false,
        questions = listOf(
          ShapeQuizQuestion(
            id = "q2_1",
            type = QuizQuestionType.SHAPE_IDENTIFICATION,
            targetLetter = taa,
            targetFormType = FormType.MEDIAL,
            promptEn = "Which one is the Medial (middle) form of Taa (ت)?",
            promptBn = "'তা' (ت) হরফের মাঝে বসার মধ্যবর্তী রূপ কোনটি?",
            promptAr = "ما هو شكل حرف التاء (ت) في وسط الكلمة؟",
            options = listOf(
              ShapeOption("ـتـ", FormType.MEDIAL, "Taa (Medial)", true),
              ShapeOption("تـ", FormType.INITIAL, "Taa (Initial)", false),
              ShapeOption("ـت", FormType.FINAL, "Taa (Final)", false),
              ShapeOption("ت", FormType.ISOLATED, "Taa (Isolated)", false)
            ),
            explanationEn = "'ـتـ' connects from both sides (right and left) and bears two dots on top.",
            explanationBn = "'ـتـ' দুই দিক থেকেই যুক্ত হয় এবং উপরে দুটি নুকতা থাকে।",
            explanationAr = "حرف 'ـتـ' في الوسط يتصل من الجهتين وله نقطتان في الأعلى."
          ),
          ShapeQuizQuestion(
            id = "q2_2",
            type = QuizQuestionType.SHAPE_IDENTIFICATION,
            targetLetter = thaa,
            targetFormType = FormType.FINAL,
            promptEn = "Select the Final (end) form of Thaa (ث):",
            promptBn = "'ছা' (ث) হরফের শেষে বসার প্রান্তিক রূপ নির্বাচন করুন:",
            promptAr = "اختر شكل حرف الثاء (ث) في نهاية الكلمة:",
            options = listOf(
              ShapeOption("ـث", FormType.FINAL, "Thaa (Final)", true),
              ShapeOption("ثـ", FormType.INITIAL, "Thaa (Initial)", false),
              ShapeOption("ـثـ", FormType.MEDIAL, "Thaa (Medial)", false),
              ShapeOption("ث", FormType.ISOLATED, "Thaa (Isolated)", false)
            ),
            explanationEn = "'ـث' closes with a full boat dish and holds three dots on top.",
            explanationBn = "'ـث' শেষে পুরো থালার রূপ ধারণ করে এবং উপরে তিনটি নুকতা থাকে।",
            explanationAr = "الحرف 'ـث' يغلق الطبق في نهاية الكلمة مع ثلاث نقاط بالأعلى."
          ),
          ShapeQuizQuestion(
            id = "q2_3",
            type = QuizQuestionType.MATCHING_PAIRS,
            targetLetter = taa,
            targetFormType = FormType.INITIAL,
            promptEn = "Match Taa and Thaa forms correctly:",
            promptBn = "তা ও ছা এর বিভিন্ন রূপ জোড়া মেলান:",
            promptAr = "طابق أشكال التاء والثاء بدقة:",
            pairs = listOf(
              MatchingPairItem("p2_1", "ت", "تـ", FormType.INITIAL, "Taa"),
              MatchingPairItem("p2_2", "ث", "ثـ", FormType.INITIAL, "Thaa"),
              MatchingPairItem("p2_3", "ت", "ـتـ", FormType.MEDIAL, "Taa")
            ),
            explanationEn = "Two dots = Taa; Three dots = Thaa. Both share the boat family family!",
            explanationBn = "দুই নুকতা মানে তা, তিন নুকতা মানে ছা। দুটোই একই আকৃতির পরিবার!",
            explanationAr = "نقطتان تعني تاء، وثلاث نقاط تعني ثاء، وكلاهما من نفس العائلة."
          )
        )
      ),

      // Level 3: Jeem, Haa, Khaa (Checkpoint Chest)
      RoadmapLevel(
        id = 3,
        titleEn = "Level 3: Jeem, Haa & Khaa",
        titleBn = "লেভেল ৩: জিম, হা ও খা",
        titleAr = "المستوى ٣: جيم وحاء وخاء",
        subtitleEn = "Crown checkpoint with reward chest",
        subtitleBn = "গলার হরফ ও ক্রাউন চেকপয়েন্ট চেস্ট",
        subtitleAr = "محطة التتويج وصندوق المكافآت",
        iconEmoji = "🏰",
        nodeColor = Color(0xFFD97706), // Amber Gold
        isCheckpoint = true,
        rewardStars = 50,
        rewardXp = 120,
        questions = listOf(
          ShapeQuizQuestion(
            id = "q3_1",
            type = QuizQuestionType.SHAPE_IDENTIFICATION,
            targetLetter = jeem,
            targetFormType = FormType.INITIAL,
            promptEn = "Identify the Initial form of Jeem (ج):",
            promptBn = "'জিম' (ج) হরফের শুরুতে বসার রূপ কোনটি?",
            promptAr = "ما هو شكل حرف الجيم (ج) في بداية الكلمة؟",
            options = listOf(
              ShapeOption("جـ", FormType.INITIAL, "Jeem (Initial)", true),
              ShapeOption("ـجـ", FormType.MEDIAL, "Jeem (Medial)", false),
              ShapeOption("ـج", FormType.FINAL, "Jeem (Final)", false),
              ShapeOption("ج", FormType.ISOLATED, "Jeem (Isolated)", false)
            ),
            explanationEn = "In the initial form, the big tail disappears, leaving the head and the dot below.",
            explanationBn = "শুরুতে জিম-এর পেটের গোল অংশ বাদ গিয়ে শুধু মাথা ও নিচের নুকতাটি থাকে।",
            explanationAr = "في بداية الكلمة يختفي بطن الجيم ويبقى الرأس مع النقطة بالأسفل."
          ),
          ShapeQuizQuestion(
            id = "q3_2",
            type = QuizQuestionType.SHAPE_IDENTIFICATION,
            targetLetter = haa,
            targetFormType = FormType.MEDIAL,
            promptEn = "Find the Medial form of dotless Haa (ح):",
            promptBn = "নুকতাবিহীন 'হা' (ح) হরফের মধ্যবর্তী রূপ নির্বাচন করুন:",
            promptAr = "اختر شكل حرف الحاء (ح) الخالي من النقط في وسط الكلمة:",
            options = listOf(
              ShapeOption("ـحـ", FormType.MEDIAL, "Haa (Medial)", true),
              ShapeOption("حـ", FormType.INITIAL, "Haa (Initial)", false),
              ShapeOption("ـح", FormType.FINAL, "Haa (Final)", false),
              ShapeOption("ـخـ", FormType.MEDIAL, "Khaa (Medial)", false)
            ),
            explanationEn = "Haa (ح) has no dots at all. In the middle it connects from both sides: 'ـحـ'.",
            explanationBn = "হা (ح)-এর কোনো নুকতা নেই। মাঝে বসার সময় দুই দিক থেকেই যুক্ত হয়।",
            explanationAr = "الحاء حرف لا نقط له، ويتصل في الوسط من الجانبين: 'ـحـ'."
          ),
          ShapeQuizQuestion(
            id = "q3_3",
            type = QuizQuestionType.MATCHING_PAIRS,
            targetLetter = khaa,
            targetFormType = FormType.INITIAL,
            promptEn = "Match the Jeem family forms:",
            promptBn = "জিম পরিবারের হরফগুলোর রূপ জোড়া মেলান:",
            promptAr = "طابق أشكال عائلة الجيم والحاء والخاء:",
            pairs = listOf(
              MatchingPairItem("p3_1", "ج", "جـ", FormType.INITIAL, "Jeem"),
              MatchingPairItem("p3_2", "ح", "ـحـ", FormType.MEDIAL, "Haa"),
              MatchingPairItem("p3_3", "خ", "ـخ", FormType.FINAL, "Khaa")
            ),
            explanationEn = "Jeem has a dot below, Haa is clean, and Khaa has a dot above!",
            explanationBn = "জিমের নুকতা নিচে, হা নুকতামুক্ত, এবং খা-এর নুকতা মাথার উপরে!",
            explanationAr = "الجيم نقطتها بالأسفل، والحاء بلا نقط، والخاء نقطتها بالأعلى!"
          )
        )
      ),

      // Level 4: Daal & Dhaal (Non-connectors)
      RoadmapLevel(
        id = 4,
        titleEn = "Level 4: Daal & Dhaal",
        titleBn = "লেভেল ৪: দাল ও যাল",
        titleAr = "المستوى ٤: دال وذال",
        subtitleEn = "Friendly letters that don't connect left",
        subtitleBn = "অসংযোগকারী হরফ: বামে কখনো যুক্ত হয় না",
        subtitleAr = "حروف الانفصال التي لا تتصل بما بعدها",
        iconEmoji = "🛡️",
        nodeColor = Color(0xFF059669), // Teal Emerald
        isCheckpoint = false,
        questions = listOf(
          ShapeQuizQuestion(
            id = "q4_1",
            type = QuizQuestionType.SHAPE_IDENTIFICATION,
            targetLetter = daal,
            targetFormType = FormType.FINAL,
            promptEn = "Which one is the Final form of Daal (د)?",
            promptBn = "'দাল' (د) হরফের প্রান্তিক (শেষে) রূপ কোনটি?",
            promptAr = "ما هو شكل حرف الدال (د) في نهاية الكلمة؟",
            options = listOf(
              ShapeOption("ـد", FormType.FINAL, "Daal (Final)", true),
              ShapeOption("د", FormType.ISOLATED, "Daal (Isolated)", false),
              ShapeOption("ـذ", FormType.FINAL, "Dhaal (Final)", false),
              ShapeOption("دـ", FormType.INITIAL, "Invalid Form", false)
            ),
            explanationEn = "Daal connects only from the right ('ـد'). It never extends an arm to the left!",
            explanationBn = "দাল শুধু ডান দিক থেকে যুক্ত হতে পারে ('ـد')। বামে কখনো যুক্ত হয় না।",
            explanationAr = "الدال تتصل فقط من اليمين ولا تمد يدها لليسار أبداً."
          ),
          ShapeQuizQuestion(
            id = "q4_2",
            type = QuizQuestionType.MATCHING_PAIRS,
            targetLetter = dhaal,
            targetFormType = FormType.ISOLATED,
            promptEn = "Match Daal and Dhaal pairs:",
            promptBn = "দাল ও যাল হরফের রূপ জোড়া লাগান:",
            promptAr = "طابق أزواج الدال والذال:",
            pairs = listOf(
              MatchingPairItem("p4_1", "د", "ـد", FormType.FINAL, "Daal"),
              MatchingPairItem("p4_2", "ذ", "ـذ", FormType.FINAL, "Dhaal"),
              MatchingPairItem("p4_3", "ذ", "ذ", FormType.ISOLATED, "Dhaal")
            ),
            explanationEn = "Daal and Dhaal keep their recognizable upright curve in all positions.",
            explanationBn = "দাল ও যাল তাদের চিরচেনা কোণাকুণি বাঁক ধরে রাখে।",
            explanationAr = "الدال والذال تحافظان على انحنائهما المميز في كل المواضع."
          )
        )
      ),

      // Level 5: Raa & Zaay
      RoadmapLevel(
        id = 5,
        titleEn = "Level 5: Raa & Zaay",
        titleBn = "লেভেল ৫: রা ও যা",
        titleAr = "المستوى ٥: راء وزاي",
        subtitleEn = "Curved sliding strokes below the line",
        subtitleBn = "রেখার নিচে নেমে যাওয়া স্লাইডার রূপ",
        subtitleAr = "الحروف المنزلقة تحت السطر",
        iconEmoji = "⚡",
        nodeColor = Color(0xFF7C3AED), // Violet
        isCheckpoint = false,
        questions = listOf(
          ShapeQuizQuestion(
            id = "q5_1",
            type = QuizQuestionType.SHAPE_IDENTIFICATION,
            targetLetter = raa,
            targetFormType = FormType.FINAL,
            promptEn = "Choose the Final connecting form of Raa (ر):",
            promptBn = "'রা' (ر) হরফের শেষে যুক্ত হওয়ার রূপটি বেছে নিন:",
            promptAr = "اختر شكل حرف الراء (ر) المتصل في النهاية:",
            options = listOf(
              ShapeOption("ـر", FormType.FINAL, "Raa (Final)", true),
              ShapeOption("ر", FormType.ISOLATED, "Raa (Isolated)", false),
              ShapeOption("ـز", FormType.FINAL, "Zaay (Final)", false),
              ShapeOption("ز", FormType.ISOLATED, "Zaay (Isolated)", false)
            ),
            explanationEn = "'ـر' receives a connection from the right and curves smoothly downward.",
            explanationBn = "'ـر' ডান পাশ থেকে সংযোগ নেয় এবং নিচে বাঁক নেয়।",
            explanationAr = "الراء المتصلة 'ـر' تتصل من اليمين وتهبط تحت السطر بانسيابية."
          ),
          ShapeQuizQuestion(
            id = "q5_2",
            type = QuizQuestionType.MATCHING_PAIRS,
            targetLetter = zaay,
            targetFormType = FormType.ISOLATED,
            promptEn = "Match Raa and Zaay with their connecting shapes:",
            promptBn = "রা ও যা হরফ দুটির সঠিক রূপ জোড়া লাগান:",
            promptAr = "طابق الراء والزاي مع أشكالهما المتصلة:",
            pairs = listOf(
              MatchingPairItem("p5_1", "ر", "ـر", FormType.FINAL, "Raa"),
              MatchingPairItem("p5_2", "ز", "ـز", FormType.FINAL, "Zaay"),
              MatchingPairItem("p5_3", "ز", "ز", FormType.ISOLATED, "Zaay")
            ),
            explanationEn = "Zaay (ز) is simply Raa (ر) adorned with a single glowing diamond dot above.",
            explanationBn = "যা (ز) হলো রা (ر)-এর মাথার উপর একটি উজ্জ্বল নুকতা।",
            explanationAr = "الزاي هي راء تعلوها نقطة واحدة بالأعلى."
          )
        )
      ),

      // Level 6: Seen & Sheen (Checkpoint Chest)
      RoadmapLevel(
        id = 6,
        titleEn = "Level 6: Seen & Sheen",
        titleBn = "লেভেল ৬: সিন ও শিন",
        titleAr = "المستوى ٦: سين وشين",
        subtitleEn = "The three-toothed letters and treasure chest",
        subtitleBn = "তিন দাঁতওয়ালা হরফ ও রত্নভাণ্ডার চেস্ট",
        subtitleAr = "حروف الأسنان الثلاثة وصندوق الكنز",
        iconEmoji = "💎",
        nodeColor = Color(0xFFE11D48), // Rose Red
        isCheckpoint = true,
        rewardStars = 60,
        rewardXp = 150,
        questions = listOf(
          ShapeQuizQuestion(
            id = "q6_1",
            type = QuizQuestionType.SHAPE_IDENTIFICATION,
            targetLetter = seen,
            targetFormType = FormType.MEDIAL,
            promptEn = "Which is the Medial (middle) form of Seen (س)?",
            promptBn = "'সিন' (س) হরফের মাঝে বসার মধ্যবর্তী রূপ কোনটি?",
            promptAr = "ما هو شكل حرف السين (س) في وسط الكلمة؟",
            options = listOf(
              ShapeOption("ـسـ", FormType.MEDIAL, "Seen (Medial)", true),
              ShapeOption("سـ", FormType.INITIAL, "Seen (Initial)", false),
              ShapeOption("ـس", FormType.FINAL, "Seen (Final)", false),
              ShapeOption("س", FormType.ISOLATED, "Seen (Isolated)", false)
            ),
            explanationEn = "'ـسـ' connects left and right, maintaining its 3 sharp teeth on the baseline.",
            explanationBn = "'ـسـ' এর ৩টি দাঁত সারিবদ্ধভাবে থাকে এবং উভয় পাশে বাহু প্রসারিত করে।",
            explanationAr = "السين 'ـسـ' في الوسط تتصل من الطرفين مع بقاء أسنانها الثلاثة."
          ),
          ShapeQuizQuestion(
            id = "q6_2",
            type = QuizQuestionType.SHAPE_IDENTIFICATION,
            targetLetter = sheen,
            targetFormType = FormType.INITIAL,
            promptEn = "Identify the Initial form of Sheen (ش):",
            promptBn = "'শিন' (ش) হরফের শুরুতে বসার প্রারম্ভিক রূপ কোনটি?",
            promptAr = "حدد شكل حرف الشين (ش) في بداية الكلمة:",
            options = listOf(
              ShapeOption("شـ", FormType.INITIAL, "Sheen (Initial)", true),
              ShapeOption("ـشـ", FormType.MEDIAL, "Sheen (Medial)", false),
              ShapeOption("ـش", FormType.FINAL, "Sheen (Final)", false),
              ShapeOption("ش", FormType.ISOLATED, "Sheen (Isolated)", false)
            ),
            explanationEn = "Sheen (شـ) begins with three teeth topped by three dots in a pyramid.",
            explanationBn = "শিন (شـ) তিনটি দাঁত এবং উপরে পিরামিড আকারের ৩টি নুকতা নিয়ে শুরু হয়।",
            explanationAr = "الشين في البداية 'شـ' لها ثلاثة أسنان تعلوها ثلاث نقاط هرمية."
          ),
          ShapeQuizQuestion(
            id = "q6_3",
            type = QuizQuestionType.MATCHING_PAIRS,
            targetLetter = sheen,
            targetFormType = FormType.FINAL,
            promptEn = "Match Seen and Sheen shapes:",
            promptBn = "সিন ও শিনের রূপগুলো জোড়া মেলান:",
            promptAr = "طابق أشكال السين والشين:",
            pairs = listOf(
              MatchingPairItem("p6_1", "س", "سـ", FormType.INITIAL, "Seen"),
              MatchingPairItem("p6_2", "ش", "شـ", FormType.INITIAL, "Sheen"),
              MatchingPairItem("p6_3", "ش", "ـش", FormType.FINAL, "Sheen")
            ),
            explanationEn = "At the end of words, the deep sweeping tail of Seen and Sheen returns.",
            explanationBn = "শব্দের শেষে সিন ও শিনের গভীর ঝুলন্ত পেটের অংশটি আবার ফিরে আসে।",
            explanationAr = "في نهاية الكلمة يعود طبق السين والشين العميق."
          )
        )
      ),

      // Level 7: Saad & Daad
      RoadmapLevel(
        id = 7,
        titleEn = "Level 7: Saad & Daad",
        titleBn = "লেভেল ৭: সোয়াদ ও দোয়াদ",
        titleAr = "المستوى ٧: صاد وضاد",
        subtitleEn = "Loop head with tooth and bowl",
        subtitleBn = "লুপের মাথা এবং দাঁতসহ গভীর থালা",
        subtitleAr = "الرأس المستدير والسن مع الصحن",
        iconEmoji = "🌊",
        nodeColor = Color(0xFF0D9488), // Teal
        isCheckpoint = false,
        questions = listOf(
          ShapeQuizQuestion(
            id = "q7_1",
            type = QuizQuestionType.SHAPE_IDENTIFICATION,
            targetLetter = saad,
            targetFormType = FormType.INITIAL,
            promptEn = "Select the Initial form of Saad (ص):",
            promptBn = "'সোয়াদ' (ص) হরফের শুরুতে বসার রূপটি নির্বাচন করুন:",
            promptAr = "اختر شكل حرف الصاد (ص) في بداية الكلمة:",
            options = listOf(
              ShapeOption("صـ", FormType.INITIAL, "Saad (Initial)", true),
              ShapeOption("ـصـ", FormType.MEDIAL, "Saad (Medial)", false),
              ShapeOption("ـص", FormType.FINAL, "Saad (Final)", false),
              ShapeOption("ضـ", FormType.INITIAL, "Daad (Initial)", false)
            ),
            explanationEn = "Saad (صـ) has an elongated loop followed immediately by a single distinct tooth.",
            explanationBn = "সোয়াদ (صـ) এর একটি লম্বাটে লুপ থাকে এবং এর ঠিক পরেই একটি ছোট দাঁত থাকে।",
            explanationAr = "الصاد 'صـ' تتكون من حلقة ممتدة يتبعها مباشرة سن مميز."
          ),
          ShapeQuizQuestion(
            id = "q7_2",
            type = QuizQuestionType.MATCHING_PAIRS,
            targetLetter = daad,
            targetFormType = FormType.MEDIAL,
            promptEn = "Match Saad and Daad forms:",
            promptBn = "সোয়াদ ও দোয়াদ হরফের রূপ জোড়া মেলান:",
            promptAr = "طابق أشكال الصاد والضاد:",
            pairs = listOf(
              MatchingPairItem("p7_1", "ص", "صـ", FormType.INITIAL, "Saad"),
              MatchingPairItem("p7_2", "ض", "ـضـ", FormType.MEDIAL, "Daad"),
              MatchingPairItem("p7_3", "ض", "ـض", FormType.FINAL, "Daad")
            ),
            explanationEn = "Daad has the unique dot that defines the language of the Quran!",
            explanationBn = "দোয়াদ হরফটির উপর একটি অনন্য নুকতা থাকে।",
            explanationAr = "حرف الضاد يتميز بالنقطة التي تشتهر بها لغة الضاد."
          )
        )
      ),

      // Level 8: Taa & Zaa
      RoadmapLevel(
        id = 8,
        titleEn = "Level 8: Taa & Zaa",
        titleBn = "লেভেল ৮: ত্বোয়া ও জোয়া",
        titleAr = "المستوى ٨: طاء وظاء",
        subtitleEn = "The tall masts that connect both ways",
        subtitleBn = "খাড়া মাস্তুলযুক্ত হরফ যা উভয় দিকে জোড়া লাগে",
        subtitleAr = "حروف الألف القائمة المتصلة",
        iconEmoji = "🏛️",
        nodeColor = Color(0xFF4F46E5), // Indigo
        isCheckpoint = false,
        questions = listOf(
          ShapeQuizQuestion(
            id = "q8_1",
            type = QuizQuestionType.SHAPE_IDENTIFICATION,
            targetLetter = ArabicAlphabetRepository.letters[15], // 16. Taa
            targetFormType = FormType.MEDIAL,
            promptEn = "Which is the Medial form of Taa (ط)?",
            promptBn = "'ত্বোয়া' (ط) হরফের মধ্যবর্তী রূপ কোনটি?",
            promptAr = "ما هو شكل حرف الطاء (ط) في وسط الكلمة؟",
            options = listOf(
              ShapeOption("ـطـ", FormType.MEDIAL, "Taa (Medial)", true),
              ShapeOption("طـ", FormType.INITIAL, "Taa (Initial)", false),
              ShapeOption("ـط", FormType.FINAL, "Taa (Final)", false),
              ShapeOption("ـظـ", FormType.MEDIAL, "Zaa (Medial)", false)
            ),
            explanationEn = "'ـطـ' connects from both sides while maintaining its tall vertical mast.",
            explanationBn = "'ـطـ' উভয় পাশ থেকে যুক্ত হয় এবং এর খাড়া মাস্তুল অটুট থাকে।",
            explanationAr = "الطاء في الوسط 'ـطـ' تتصل من الجهتين مع الاحتفاظ بعصاها القائمة."
          ),
          ShapeQuizQuestion(
            id = "q8_2",
            type = QuizQuestionType.MATCHING_PAIRS,
            targetLetter = ArabicAlphabetRepository.letters[16], // 17. Zaa
            targetFormType = FormType.ISOLATED,
            promptEn = "Match Taa and Zaa forms:",
            promptBn = "ত্বোয়া ও জোয়া হরফের রূপগুলো মেলাও:",
            promptAr = "طابق أشكال الطاء والظاء:",
            pairs = listOf(
              MatchingPairItem("p8_1", "ط", "طـ", FormType.INITIAL, "Taa"),
              MatchingPairItem("p8_2", "ظ", "ظـ", FormType.INITIAL, "Zaa"),
              MatchingPairItem("p8_3", "ظ", "ـظ", FormType.FINAL, "Zaa")
            ),
            explanationEn = "Taa and Zaa never lose their basic body structure in any position!",
            explanationBn = "ত্বোয়া ও জোয়া যেকোনো অবস্থানে তাদের মূল কাঠামো বজায় রাখে।",
            explanationAr = "الطاء والظاء لا تفقدان شكلهما الأساسي في أي موضع."
          )
        )
      ),

      // Level 9: Ayn & Ghayn
      RoadmapLevel(
        id = 9,
        titleEn = "Level 9: Ayn & Ghayn",
        titleBn = "লেভেল ৯: আইন ও গাইন",
        titleAr = "المستوى ٩: عين وغين",
        subtitleEn = "Open head in front, closed triangle inside",
        subtitleBn = "সামনে খোলা মুখ, কিন্তু মাঝে ত্রিকোণাকার বন্ধ রূপ",
        subtitleAr = "الرأس المفتوح في البداية والمغلق في الوسط",
        iconEmoji = "👁️",
        nodeColor = Color(0xFFC026D3), // Fuchsia
        isCheckpoint = false,
        questions = listOf(
          ShapeQuizQuestion(
            id = "q9_1",
            type = QuizQuestionType.SHAPE_IDENTIFICATION,
            targetLetter = ArabicAlphabetRepository.letters[17], // 18. Ayn
            targetFormType = FormType.MEDIAL,
            promptEn = "Notice the transformation! Which is Medial Ayn (ع)?",
            promptBn = "রূপবদল লক্ষ্য করুন! 'আইন' (ع) এর মধ্যবর্তী রূপ কোনটি?",
            promptAr = "لاحظ التغير! ما هو شكل حرف العين (ع) في وسط الكلمة؟",
            options = listOf(
              ShapeOption("ـعـ", FormType.MEDIAL, "Ayn (Medial)", true),
              ShapeOption("عـ", FormType.INITIAL, "Ayn (Initial)", false),
              ShapeOption("ـع", FormType.FINAL, "Ayn (Final)", false),
              ShapeOption("ع", FormType.ISOLATED, "Ayn (Isolated)", false)
            ),
            explanationEn = "In medial form ('ـعـ'), Ayn transforms into an inverted closed triangle!",
            explanationBn = "শব্দের মাঝে ('ـعـ') আইন একটি উল্টানো বন্ধ ত্রিকোণে রূপ নেয়!",
            explanationAr = "في وسط الكلمة 'ـعـ' تنقلب العين إلى مثلث مغلق مميز!"
          ),
          ShapeQuizQuestion(
            id = "q9_2",
            type = QuizQuestionType.MATCHING_PAIRS,
            targetLetter = ArabicAlphabetRepository.letters[18], // 19. Ghayn
            targetFormType = FormType.INITIAL,
            promptEn = "Match Ayn and Ghayn forms:",
            promptBn = "আইন ও গাইনের বিভিন্ন রূপ জোড়া মেলান:",
            promptAr = "طابق أشكال العين والغين:",
            pairs = listOf(
              MatchingPairItem("p9_1", "ع", "عـ", FormType.INITIAL, "Ayn"),
              MatchingPairItem("p9_2", "غ", "غـ", FormType.INITIAL, "Ghayn"),
              MatchingPairItem("p9_3", "غ", "ـغـ", FormType.MEDIAL, "Ghayn")
            ),
            explanationEn = "Open mouth at the start, closed triangle in the middle and end!",
            explanationBn = "শুরুতে খোলা মুখ, কিন্তু মাঝে এবং শেষে বন্ধ রূপ!",
            explanationAr = "مفتوحة في البداية، ومغلقة في الوسط والنهاية!"
          )
        )
      ),

      // Level 10: Grand Master Crown (Final Checkpoint)
      RoadmapLevel(
        id = 10,
        titleEn = "Level 10: Grand Master Crown",
        titleBn = "লেভেল ১০: গ্র্যান্ড মাস্টার ক্রাউন",
        titleAr = "المستوى ١٠: تاج البطولة الأكبر",
        subtitleEn = "Ultimate mastery test of all Arabic letter forms",
        subtitleBn = "সকল আরবি হরফের চূড়ান্ত রূপভেদ মাস্টার পরীক্ষা",
        subtitleAr = "الاختبار الشامل لكافة أشكال الحروف العربية",
        iconEmoji = "👑",
        nodeColor = Color(0xFFD97706), // Royal Gold
        isCheckpoint = true,
        rewardStars = 100,
        rewardXp = 250,
        questions = listOf(
          ShapeQuizQuestion(
            id = "q10_1",
            type = QuizQuestionType.SHAPE_IDENTIFICATION,
            targetLetter = ArabicAlphabetRepository.letters[23], // 24. Meem
            targetFormType = FormType.MEDIAL,
            promptEn = "Master Test: Which is the Medial form of Meem (م)?",
            promptBn = "মাস্টার টেস্ট: 'মিম' (م) হরফের মধ্যবর্তী রূপ কোনটি?",
            promptAr = "اختبار الأبطال: ما هو شكل حرف الميم (م) في وسط الكلمة؟",
            options = listOf(
              ShapeOption("ـمـ", FormType.MEDIAL, "Meem (Medial)", true),
              ShapeOption("مـ", FormType.INITIAL, "Meem (Initial)", false),
              ShapeOption("ـم", FormType.FINAL, "Meem (Final)", false),
              ShapeOption("م", FormType.ISOLATED, "Meem (Isolated)", false)
            ),
            explanationEn = "Meem dips smoothly below or stays looped on the baseline: 'ـمـ'.",
            explanationBn = "'ـمـ' হলো মিমের মধ্যবর্তী রূপ যা মাঝখানে গোল লুপের মতো থাকে।",
            explanationAr = "الميم 'ـمـ' في الوسط تستدير بانسيابية متصلة من الجانبين."
          ),
          ShapeQuizQuestion(
            id = "q10_2",
            type = QuizQuestionType.SHAPE_IDENTIFICATION,
            targetLetter = ArabicAlphabetRepository.letters[24], // 25. Noon
            targetFormType = FormType.INITIAL,
            promptEn = "Select the Initial form of Noon (ن):",
            promptBn = "'নূন' (ن) হরফের প্রারম্ভিক রূপ নির্বাচন করুন:",
            promptAr = "اختر شكل حرف النون (ن) في بداية الكلمة:",
            options = listOf(
              ShapeOption("نـ", FormType.INITIAL, "Noon (Initial)", true),
              ShapeOption("ـنـ", FormType.MEDIAL, "Noon (Medial)", false),
              ShapeOption("ـن", FormType.FINAL, "Noon (Final)", false),
              ShapeOption("ن", FormType.ISOLATED, "Noon (Isolated)", false)
            ),
            explanationEn = "In the beginning, Noon becomes a single tooth with one dot on top: 'نـ'.",
            explanationBn = "শুরুতে নূন কেবল একটি দাঁত এবং উপরে একটি নুকতা নিয়ে বসে: 'نـ'।",
            explanationAr = "في البداية تصبح النون سناً واحداً تعلوه نقطة واحدة: 'نـ'."
          ),
          ShapeQuizQuestion(
            id = "q10_3",
            type = QuizQuestionType.MATCHING_PAIRS,
            targetLetter = ArabicAlphabetRepository.letters[27], // 28. Yaa
            targetFormType = FormType.INITIAL,
            promptEn = "Final Master Challenge: Match these letters to their initial shapes:",
            promptBn = "চূড়ান্ত চ্যালেঞ্জ: হরফগুলোকে তাদের প্রারম্ভিক রূপের সাথে মেলান:",
            promptAr = "التحدي الأخير: طابق الحروف مع أشكالها الابتدائية:",
            pairs = listOf(
              MatchingPairItem("p10_1", "ن", "نـ", FormType.INITIAL, "Noon"),
              MatchingPairItem("p10_2", "ي", "يـ", FormType.INITIAL, "Yaa"),
              MatchingPairItem("p10_3", "م", "مـ", FormType.INITIAL, "Meem")
            ),
            explanationEn = "Congratulations! You have mastered the foundational connections of Arabic script!",
            explanationBn = "অভিনন্দন! আপনি আরবি হরফের মৌলিক রূপভেদ সফলভাবে আয়ত্ত করেছেন!",
            explanationAr = "تهانينا! لقد أتقنت جميع أشكال الحروف العربية بنجاح!"
          )
        )
      ),

      // Level 11: Word Building & Tashdeed (Stage 4 Checkpoint)
      RoadmapLevel(
        id = 11,
        titleEn = "Level 11: Word Building & Tashdeed",
        titleBn = "লেভেল ১১: শব্দ গঠন ও তাসদীদ",
        titleAr = "المستوى ١١: تكوين الكلمات والتشديد",
        subtitleEn = "Connecting letters into words with double-stress Tashdeed (ّ)",
        subtitleBn = "হরফ যুক্ত করে শব্দ গঠন ও তাসদীদ (ّ) এর দ্বিত্ব উচ্চারণ",
        subtitleAr = "تركيب الحروف لتكوين الكلمات القرآنية مع علامة الشدة",
        iconEmoji = "🏗️",
        nodeColor = Color(0xFFEA580C), // Orange
        isCheckpoint = false,
        rewardStars = 50,
        rewardXp = 150,
        questions = listOf(
          ShapeQuizQuestion(
            id = "q11_1",
            type = QuizQuestionType.SHAPE_IDENTIFICATION,
            targetLetter = ArabicAlphabetRepository.letters[1], // Baa
            targetFormType = FormType.MEDIAL,
            promptEn = "Word Building: In the Quranic word 'رَبِّ' (Rabbi), which sign indicates double emphasis on Baa?",
            promptBn = "শব্দ গঠন: কুরআনিক শব্দ 'رَبِّ' (রব্বি)-তে 'বা' হরফটির দ্বিত্ব জোর নির্দেশ করে কোন চিহ্নটি?",
            promptAr = "تكوين الكلمات: في الكلمة القرآنية 'رَبِّ'، ما هي العلامة التي تدل على تشديد الباء؟",
            options = listOf(
              ShapeOption("ّ (Tashdeed)", FormType.ISOLATED, "Tashdeed", true),
              ShapeOption("ْ (Sukoon)", FormType.ISOLATED, "Sukoon", false),
              ShapeOption("ً (Tanween)", FormType.ISOLATED, "Tanween", false),
              ShapeOption("ٰ (Short Alif)", FormType.ISOLATED, "Short Alif", false)
            ),
            explanationEn = "Tashdeed (ّ) doubles the consonant sound with firm emphasis: Rab-bi!",
            explanationBn = "তাসদীদ (ّ) হরফটিকে দুইবার শক্তভাবে উচ্চারণে সহায়তা করে: রব-বি!",
            explanationAr = "الشدة (ّ) تضاعف الحرف بنطق مكرر قوي ومؤكد!"
          ),
          ShapeQuizQuestion(
            id = "q11_2",
            type = QuizQuestionType.MATCHING_PAIRS,
            targetLetter = ArabicAlphabetRepository.letters[0],
            targetFormType = FormType.INITIAL,
            promptEn = "Match isolated letters with their fused Quranic word form:",
            promptBn = "পৃথক হরফগুলোকে একত্রিত হয়ে গঠিত কুরআনিক শব্দের সাথে মেলান:",
            promptAr = "طابق الحروف المنفصلة مع الكلمة القرآنية المتصلة الناتجة:",
            pairs = listOf(
              MatchingPairItem("p11_1", "ك + ت + ب", "كَتَبَ", FormType.INITIAL, "Kataba"),
              MatchingPairItem("p11_2", "ق + ل", "قُلْ", FormType.INITIAL, "Qul"),
              MatchingPairItem("p11_3", "ح + م + د", "حَمْدٌ", FormType.INITIAL, "Hamdun")
            ),
            explanationEn = "Arabic letters connect smoothly on baseline to form authentic Quranic words!",
            explanationBn = "আরবি হরফগুলো একে অপরের সাথে বাহু প্রসারিত করে পূর্ণাঙ্গ কুরআনিক শব্দ গঠন করে।",
            explanationAr = "تتصل الحروف بانسيابية لتكوين الكلمات القرآنية العظيمة!"
          )
        )
      ),

      // Level 12: Full Quranic Verse Recitation (Stage 5 Grand Checkpoint)
      RoadmapLevel(
        id = 12,
        titleEn = "Level 12: Full Quranic Verse Reading",
        titleBn = "লেভেল ১২: সম্পূর্ণ আয়াত তিলাওয়াত",
        titleAr = "المستوى ١٢: تلاوة الآيات القرآنية الكاملة",
        subtitleEn = "Recite complete sacred Ayahs with Tajweed and fluent connection",
        subtitleBn = "তাজবীদ ও তারতীলের সাথে সম্পূর্ণ কুরআনিক আয়াত পাঠ",
        subtitleAr = "تلاوة آيات قرآنية كاملة مع مراعاة أحكام التجويد والوصل",
        iconEmoji = "📖",
        nodeColor = Color(0xFFD97706), // Royal Gold
        isCheckpoint = true,
        rewardStars = 150,
        rewardXp = 350,
        questions = listOf(
          ShapeQuizQuestion(
            id = "q12_1",
            type = QuizQuestionType.SHAPE_IDENTIFICATION,
            targetLetter = ArabicAlphabetRepository.letters[0],
            targetFormType = FormType.INITIAL,
            promptEn = "Quran Verse Reading: Identify the first letter of 'بِسْمِ ٱللَّهِ ٱلرَّحْمَـٰنِ ٱلرَّحِيمِ':",
            promptBn = "সম্পূর্ণ আয়াত পাঠ: 'بِسْمِ ٱللَّهِ ٱلرَّحْمَـٰنِ ٱلرَّحِيمِ' (বিসমিল্লাহ)-এর প্রথম হরফ কোনটি?",
            promptAr = "قراءة الآية: ما هو الحرف الأول في 'بِسْمِ ٱللَّهِ ٱلرَّحْمَـٰنِ ٱلرَّحِيمِ'؟",
            options = listOf(
              ShapeOption("بـ (Baa with Kasra)", FormType.INITIAL, "Bi", true),
              ShapeOption("سـ (Seen)", FormType.MEDIAL, "Seen", false),
              ShapeOption("م (Meem)", FormType.FINAL, "Meem", false),
              ShapeOption("ا (Alif)", FormType.ISOLATED, "Alif", false)
            ),
            explanationEn = "'بِسْمِ' begins with connected Baa with Kasra: 'Bi' joined to 'Seen'!",
            explanationBn = "'بِسْمِ' শুরু হয় যেরযুক্ত প্রারম্ভিক 'বা' হরফ দিয়ে, যা 'সীন'-এর সাথে যুক্ত থাকে!",
            explanationAr = "تبدأ البسملة بحرف الباء المكسورة المتصلة بحرف السين الساكنة: 'بِسْمِ'."
          ),
          ShapeQuizQuestion(
            id = "q12_2",
            type = QuizQuestionType.SHAPE_IDENTIFICATION,
            targetLetter = ArabicAlphabetRepository.letters[20], // Qaaf
            targetFormType = FormType.INITIAL,
            promptEn = "Surah Al-Ikhlas Verse 1: Which letter opens 'قُلْ هُوَ ٱللَّهُ أَحَدٌ'?",
            promptBn = "সূরা আল-ইখলাস আয়াত ১: 'قُلْ هُوَ ٱللَّهُ أَحَدٌ' কোন হরফ দিয়ে শুরু হয়েছে?",
            promptAr = "سورة الإخلاص: ما هو الحرف الذي تبدأ به الآية 'قُلْ هُوَ ٱللَّهُ أَحَدٌ'؟",
            options = listOf(
              ShapeOption("قـ (Qaaf with Damma)", FormType.INITIAL, "Qul", true),
              ShapeOption("ل (Laam with Sukoon)", FormType.FINAL, "Lam", false),
              ShapeOption("هـ (Haa)", FormType.INITIAL, "Huwa", false),
              ShapeOption("أ (Alif)", FormType.ISOLATED, "Ahad", false)
            ),
            explanationEn = "Qaaf with Damma connects to Laam with Sukoon to make 'Qul' (Say)!",
            explanationBn = "পেশযুক্ত 'ক্বাফ' সাকিনযুক্ত 'লাম'-এর সাথে যুক্ত হয়ে 'ক্বুল' (বলুন) উচ্চারিত হয়!",
            explanationAr = "القاف المضمومة تتصل باللام الساكنة لتنطق 'قُلْ'!"
          )
        )
      )
    )
  }

  val stages: List<QuranPedagogyStage> = listOf(
    QuranPedagogyStage(
      id = QuranStageId.STAGE_1_LETTERS_SHAPES,
      stageNumber = 1,
      titleEn = "Stage 1: Letters & Shapes",
      titleBn = "ধাপ ১: হরফ ও রূপসমূহ",
      titleAr = "المرحلة الأولى: الحروف والأشكال",
      subtitleEn = "Master all 29 letters and their 4 connecting forms",
      subtitleBn = "২৯টি আরবি হরফের একক ও ৪টি রূপ চেনা",
      subtitleAr = "إتقان الحروف الـ ٢٩ وأشكالها الأربعة في الكلمة",
      iconEmoji = "🔤",
      color = Color(0xFF0A5C36), // Emerald
      levelIds = listOf(1, 2, 3, 4)
    ),
    QuranPedagogyStage(
      id = QuranStageId.STAGE_2_HARAKAT_TANWEEN,
      stageNumber = 2,
      titleEn = "Stage 2: Harakat & Tanween",
      titleBn = "ধাপ ২: হরকত ও তানভীন",
      titleAr = "المرحلة الثانية: الحركات والتنوين",
      subtitleEn = "Short vowels (Fatha, Kasra, Damma) and Tanween sounds",
      subtitleBn = "যবর, যের, পেশ এবং দুই যবর, দুই যের, দুই পেশ (তানভীন)",
      subtitleAr = "الفتحة والكسرة والضمة والتنوين بأنواعه الثلاثة",
      iconEmoji = "🎵",
      color = Color(0xFF0284C7), // Sky Blue
      levelIds = listOf(5, 6)
    ),
    QuranPedagogyStage(
      id = QuranStageId.STAGE_3_SUKOON_MAD,
      stageNumber = 3,
      titleEn = "Stage 3: Sukoon & Mad",
      titleBn = "ধাপ ৩: সুকুন ও মাদ্দ",
      titleAr = "المرحلة الثالثة: السكون والمدود",
      subtitleEn = "Resting consonants (Jazm) and prolonged elongation (Madd)",
      subtitleBn = "সাকিন / জযম এবং মাদ্দের হরফে দীর্ঘ স্বর টানা",
      subtitleAr = "علامة السكون (الجزم) وحروف المد الثلاثة والإطالة الصوتية",
      iconEmoji = "🌊",
      color = Color(0xFF7C3AED), // Purple
      levelIds = listOf(7, 8)
    ),
    QuranPedagogyStage(
      id = QuranStageId.STAGE_4_WORD_BUILDING,
      stageNumber = 4,
      titleEn = "Stage 4: Word Building & Tashdeed",
      titleBn = "ধাপ ৪: শব্দ গঠন ও তাসদীদ",
      titleAr = "المرحلة الرابعة: تكوين الكلمات والتشديد",
      subtitleEn = "Combine letters into words with double consonants (Tashdeed)",
      subtitleBn = "হরফ যুক্ত করে পূর্ণ শব্দ গঠন এবং তাসদীদের দ্বিত্ব উচ্চারণ",
      subtitleAr = "دمج الحروف لتكوين الكلمات القرآنية وأحكام الشدة",
      iconEmoji = "🏗️",
      color = Color(0xFFEA580C), // Orange
      levelIds = listOf(9, 10, 11)
    ),
    QuranPedagogyStage(
      id = QuranStageId.STAGE_5_FULL_VERSE,
      stageNumber = 5,
      titleEn = "Stage 5: Full Verse Reading",
      titleBn = "ধাপ ৫: সম্পূর্ণ আয়াত তিলাওয়াত",
      titleAr = "المرحلة الخامسة: قراءة الآيات الكاملة",
      subtitleEn = "Recite complete Quranic verses with foundational Tajweed",
      subtitleBn = "সহীহ তিলাওয়াতে সম্পূর্ণ আয়াত ও সূরা পাঠ (তাজবীদসহ)",
      subtitleAr = "تلاوة آيات وسور قرآنية كاملة مع تطبيق أحكام التجويد",
      iconEmoji = "📖",
      color = Color(0xFFD97706), // Gold
      levelIds = listOf(12)
    )
  )
}
