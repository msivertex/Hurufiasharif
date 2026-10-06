package com.example

import kotlin.random.Random

/**
 * Algorithmic Question Generator for 20-Level Mini-Games.
 * Guarantees EXACTLY 20 questions per level with dynamic auto-shuffling
 * so content never repeats identically.
 *
 * Implements 3-Tier Progressive Difficulty:
 * - Levels 1-5: Basic single-letter recognition and simple visual cues.
 * - Levels 6-10: Connected letter forms (Initial, Medial, Final) and multi-nukta identification.
 * - Levels 11-20: Advanced connected words, time-attack rapid challenges, and phonetically similar letter pairs.
 */
object GameQuestionGenerator {

  // Phonetically similar confusing letter pairs
  private val phoneticPairs = listOf(
    Pair("ت", "ط"), // Taa vs Twaa
    Pair("ث", "س"), // Thaa vs Seen
    Pair("س", "ص"), // Seen vs Saad
    Pair("ذ", "ز"), // Dhal vs Zay
    Pair("ز", "ظ"), // Zay vs Zwaa
    Pair("ذ", "ظ"), // Dhal vs Zwaa
    Pair("ح", "ه"), // Haa vs Ha
    Pair("ق", "ك"), // Qaaf vs Kaaf
    Pair("د", "ض"), // Daal vs Dwaad
    Pair("ع", "ء"), // Ayn vs Hamza
    Pair("غ", "خ")  // Ghayn vs Khaa
  )

  // 3-Letter Quranic root words for Form Fuser and word challenges
  private data class WordChallenge(
    val word: String,
    val letters: List<String>,
    val meaningBn: String,
    val meaningEn: String,
    val meaningAr: String
  )

  private val wordChallenges = listOf(
    WordChallenge("كَتَبَ", listOf("ك", "ت", "ب"), "লিখল (He wrote)", "He wrote", "كَتَبَ"),
    WordChallenge("قَلَم", listOf("ق", "ل", "م"), "কলম (Pen)", "Pen", "قَلَم"),
    WordChallenge("نَصَرَ", listOf("ن", "ص", "ر"), "সাহায্য করল (He helped)", "He helped", "نَصَرَ"),
    WordChallenge("جَلَسَ", listOf("ج", "ل", "س"), "বসল (He sat)", "He sat", "جَلَسَ"),
    WordChallenge("صَبَرَ", listOf("ص", "ب", "ر"), "ধৈর্য ধরল (He was patient)", "He was patient", "صَبَرَ"),
    WordChallenge("عَبَدَ", listOf("ع", "ب", "د"), "ইবাদত করল (He worshipped)", "He worshipped", "عَبَدَ"),
    WordChallenge("ذَكَرَ", listOf("ذ", "ك", "ر"), "স্মরণ করল (He remembered)", "He remembered", "ذَكَرَ"),
    WordChallenge("خَلَقَ", listOf("خ", "ل", "ق"), "সৃষ্টি করল (He created)", "He created", "خَلَقَ"),
    WordChallenge("شَكَرَ", listOf("ش", "ك", "ر"), "কৃতজ্ঞতা জানাল (He thanked)", "He thanked", "شَكَرَ"),
    WordChallenge("غَفَرَ", listOf("غ", "ف", "ر"), "ক্ষমা করল (He forgave)", "He forgave", "غَفَرَ"),
    WordChallenge("رَحِمَ", listOf("ر", "ح", "م"), "দয়া করল (He had mercy)", "He had mercy", "رَحِمَ"),
    WordChallenge("سَمِعَ", listOf("س", "م", "ع"), "শুনল (He heard)", "He heard", "سَمِعَ"),
    WordChallenge("بَلَغَ", listOf("ب", "ل", "غ"), "পৌঁছাল (He reached)", "He reached", "بَلَغَ"),
    WordChallenge("وَهَبَ", listOf("و", "ه", "ب"), "দান করল (He granted)", "He granted", "وَهَبَ"),
    WordChallenge("حَمِدَ", listOf("ح", "م", "د"), "প্রশংসা করল (He praised)", "He praised", "حَمِدَ"),
    WordChallenge("صَدَقَ", listOf("ص", "د", "ق"), "সত্য বলল (He was truthful)", "He was truthful", "صَدَقَ"),
    WordChallenge("طَلَبَ", listOf("ط", "ل", "ب"), "চাইল (He sought)", "He sought", "طَلَبَ"),
    WordChallenge("فَتَحَ", listOf("ف", "ت", "ح"), "উন্মুক্ত করল (He opened)", "He opened", "فَتَحَ"),
    WordChallenge("مَلَكَ", listOf("م", "ل", "ك"), "মালিকানা পেল (He owned)", "He owned", "مَلَكَ"),
    WordChallenge("نَظَرَ", listOf("ن", "ظ", "ر"), "তাকিয়ে দেখল (He looked)", "He looked", "نَظَرَ"),
    WordChallenge("عَلِمَ", listOf("ع", "ل", "م"), "জানল (He knew)", "He knew", "عَلِمَ"),
    WordChallenge("رَسَمَ", listOf("ر", "س", "م"), "আঁকল (He drew)", "He drew", "رَسَمَ"),
    WordChallenge("نَبَتَ", listOf("ن", "ب", "ت"), "অঙ্কুরিত হল (It grew)", "It grew", "نَبَتَ")
  )

  /**
   * Generates EXACTLY 20 questions for the specified GameType and Level (1..20).
   * Fully auto-shuffled on every call so questions never repeat identically.
   */
  fun generateQuestionsForLevel(gameType: GameType, levelNumber: Int): List<GameQuestion> {
    val tier = GameDifficultyTier.fromLevel(levelNumber)
    val isTimeAttack = levelNumber >= 11

    return when (gameType) {
      GameType.DOT_MASTER -> generateDotMasterQuestions(levelNumber, tier, isTimeAttack)
      GameType.LETTER_LINK -> generateLetterLinkQuestions(levelNumber, tier, isTimeAttack)
      GameType.FORM_FUSER -> generateFormFuserQuestions(levelNumber, tier, isTimeAttack)
      GameType.MIX_AND_MATCH -> generateMixMatchQuestions(levelNumber, tier, isTimeAttack)
      GameType.SHAPE_MASTER_PATH -> generateShapeMasterQuestions(levelNumber, tier, isTimeAttack)
    }
  }

  // =========================================================================
  // 1. DOT MASTER (নুকতা মাস্টার - ডট পজিশনিং)
  // =========================================================================
  private fun generateDotMasterQuestions(
    levelNumber: Int,
    tier: GameDifficultyTier,
    isTimeAttack: Boolean
  ): List<GameQuestion> {
    val allLetters = ArabicAlphabetRepository.letters
    val questions = mutableListOf<GameQuestion>()

    // Letter categorization by dot count and position
    val singleBelow = allLetters.filter { it.letter in listOf("ب", "ج") }
    val singleAboveTeeth = allLetters.filter { it.letter in listOf("ن", "ض", "ظ") }
    val singleAboveThroatLip = allLetters.filter { it.letter in listOf("خ", "غ", "ف") }
    val singleAboveIndependent = allLetters.filter { it.letter in listOf("ذ", "ز") }
    val allSingleAbove = singleAboveTeeth + singleAboveThroatLip + singleAboveIndependent
    val allSingleDots = singleBelow + allSingleAbove

    val doubleAbove = allLetters.filter { it.letter in listOf("ت", "ق") }
    val doubleBelow = allLetters.filter { it.letter in listOf("ي") }
    val allDoubleDots = doubleAbove + doubleBelow

    val tripleAbove = allLetters.filter { it.letter in listOf("ث", "ش") }

    val dotlessLetters = allLetters.filter {
      it.letter in listOf("ا", "ح", "د", "ر", "س", "ص", "ط", "ع", "ك", "ل", "م", "و", "ه")
    }

    val similarPairs = listOf(
      Pair("ح", "خ"),
      Pair("س", "ش"),
      Pair("ص", "ض"),
      Pair("ط", "ظ"),
      Pair("ع", "غ"),
      Pair("د", "ذ"),
      Pair("ر", "ز")
    )

    // Standard position option templates
    val optOneBelow = GameOption("pos_1_below", "• নিচে", "১টি নুকতা নিচে (বা পেটে)", "1 dot below (نقطة بالأسفل)", "📍")
    val optOneAbove = GameOption("pos_1_above", "• উপরে", "১টি নুকতা উপরে", "1 dot above (نقطة بالأعلى)", "📍")
    val optTwoAbove = GameOption("pos_2_above", "•• উপরে", "২টি নুকতা উপরে", "2 dots above (نقطتان بالأعلى)", "🎯")
    val optTwoBelow = GameOption("pos_2_below", "•• নিচে", "২টি নুকতা নিচে", "2 dots below (نقطتان بالأسفل)", "🎯")
    val optThreeAbove = GameOption("pos_3_above", "∴ উপরে", "৩টি নুকতা উপরে", "3 dots above (ثلاث نقاط بالأعلى)", "✨")
    val optZeroDots = GameOption("pos_0_dots", "০ নুকতাহীন", "কোনো নুকতা নেই (নুকতাহীন)", "No dots (بدون نقاط)", "⭕")

    fun getDotCategoryForLetter(letter: ArabicLetter): Pair<GameOption, String> = when (letter.letter) {
      in listOf("ب", "ج") -> Pair(optOneBelow, "১টি নুকতা নিচে")
      in listOf("خ", "ذ", "ز", "ض", "ظ", "غ", "ف", "ن") -> Pair(optOneAbove, "১টি নুকতা উপরে")
      in listOf("ت", "ق") -> Pair(optTwoAbove, "২টি নুকতা উপরে")
      "ي" -> Pair(optTwoBelow, "২টি নুকতা নিচে")
      in listOf("ث", "ش") -> Pair(optThreeAbove, "৩টি নুকতা উপরে")
      else -> Pair(optZeroDots, "কোনো নুকতা নেই (নুকতাহীন)")
    }

    val isLevelTimeAttack = isTimeAttack || levelNumber == 15 || levelNumber == 20
    val levelTimeLimit = if (levelNumber == 20) 10 else 12

    for (qNum in 1..20) {
      when (levelNumber) {
        // -------------------------------------------------------------------
        // LEVEL 1: ১ নুকতা: নিচে (বা ও জিম)
        // -------------------------------------------------------------------
        1 -> {
          val target = singleBelow[(qNum + levelNumber) % singleBelow.size]
          val isConnectedForm = qNum % 3 == 0
          val displayChar = if (isConnectedForm) target.initial else target.letter
          val formDesc = if (isConnectedForm) "শুরুর রূপ '$displayChar'" else "হরফ '$displayChar'"

          if (qNum % 2 == 1) {
            // Position question: where is the dot on Ba or Jeem?
            val distractors = listOf(optOneAbove, optTwoAbove, optZeroDots)
            val options = (listOf(optOneBelow) + distractors).shuffled()
            questions.add(
              GameQuestion(
                questionNumber = qNum,
                gameType = GameType.DOT_MASTER,
                levelNumber = levelNumber,
                promptBn = "$formDesc (${target.nameBn}) এ নুকতা কয়টি এবং কোথায়?",
                promptEn = "How many dots and where on $formDesc (${target.nameEn})?",
                promptAr = "كم عدد النقاط وموضعها في $formDesc (${target.nameAr})؟",
                displayArabic = displayChar,
                targetLetter = target,
                options = options,
                correctOptionId = optOneBelow.id,
                explanationBn = "হরফ '${target.nameBn} ($displayChar)' এর নিচে বা পেটে ১টি নুকতা থাকে।",
                explanationEn = "${target.nameEn} features exactly 1 dot below or inside."
              )
            )
          } else {
            // Letter picker: which letter has 1 dot below?
            val distractors = (allSingleAbove + dotlessLetters).shuffled().take(3)
            val options = (listOf(target) + distractors).shuffled().mapIndexed { idx, let ->
              val (catOpt, catDesc) = getDotCategoryForLetter(let)
              GameOption(
                id = "opt_l1_${idx}_${let.id}",
                arabicDisplay = let.letter,
                primaryText = "${let.nameBn} (${let.letter})",
                secondaryText = catDesc
              )
            }
            val correctId = options.first { it.arabicDisplay == target.letter }.id
            questions.add(
              GameQuestion(
                questionNumber = qNum,
                gameType = GameType.DOT_MASTER,
                levelNumber = levelNumber,
                promptBn = "নিচের কোন হরফটির নিচে বা পেটে ১টি নুকতা রয়েছে?",
                promptEn = "Which of the following letters has 1 dot below/inside?",
                promptAr = "أي من الحروف التالية تحته نقطة واحدة؟",
                displayArabic = "• নিচে ১ নুকতা",
                targetLetter = target,
                options = options,
                correctOptionId = correctId,
                explanationBn = "${target.nameBn} (${target.letter}) হরফে ১টি নুকতা নিচে থাকে।",
                explanationEn = "${target.nameEn} (${target.letter}) has one dot underneath."
              )
            )
          }
        }

        // -------------------------------------------------------------------
        // LEVEL 2: ১ নুকতা: উপরে (দাঁত ও বাটি - নুন, দোয়াদ, জোয়া)
        // -------------------------------------------------------------------
        2 -> {
          val target = singleAboveTeeth[(qNum * 2) % singleAboveTeeth.size]
          if (qNum % 2 == 1) {
            val distractors = listOf(optOneBelow, optTwoAbove, optZeroDots)
            val options = (listOf(optOneAbove) + distractors).shuffled()
            questions.add(
              GameQuestion(
                questionNumber = qNum,
                gameType = GameType.DOT_MASTER,
                levelNumber = levelNumber,
                promptBn = "হরফ '${target.nameBn} (${target.letter})' এ নুকতা কোথায় অবস্থিত?",
                promptEn = "Where is the dot located on '${target.nameEn} (${target.letter})'?",
                promptAr = "أين تقع النقطة في حرف '${target.nameAr}'؟",
                displayArabic = target.letter,
                targetLetter = target,
                options = options,
                correctOptionId = optOneAbove.id,
                explanationBn = "${target.nameBn} (${target.letter}) হরফের উপরে ১টি নুকতা থাকে।",
                explanationEn = "${target.nameEn} has 1 dot placed above."
              )
            )
          } else {
            val distractors = (singleBelow + dotlessLetters.filter { it.letter in listOf("ص", "ط", "ر") }).shuffled().take(3)
            val options = (listOf(target) + distractors).shuffled().mapIndexed { idx, let ->
              val (_, catDesc) = getDotCategoryForLetter(let)
              GameOption(
                id = "opt_l2_${idx}_${let.id}",
                arabicDisplay = let.letter,
                primaryText = "${let.nameBn} (${let.letter})",
                secondaryText = catDesc
              )
            }
            val correctId = options.first { it.arabicDisplay == target.letter }.id
            questions.add(
              GameQuestion(
                questionNumber = qNum,
                gameType = GameType.DOT_MASTER,
                levelNumber = levelNumber,
                promptBn = "কোন হরফটির উপরে ১টি নুকতা রয়েছে?",
                promptEn = "Which letter has 1 dot placed above?",
                promptAr = "أي حرف فوقه نقطة واحدة؟",
                displayArabic = "• উপরে ১ নুকতা",
                targetLetter = target,
                options = options,
                correctOptionId = correctId,
                explanationBn = "${target.nameBn} (${target.letter}) এর উপরে একটি নুকতা রয়েছে।",
                explanationEn = "${target.nameEn} has a single dot above."
              )
            )
          }
        }

        // -------------------------------------------------------------------
        // LEVEL 3: ১ নুকতা: উপরে (কণ্ঠ ও ওষ্ঠ - খা, গাইন, ফা)
        // -------------------------------------------------------------------
        3 -> {
          val target = singleAboveThroatLip[(qNum * 2) % singleAboveThroatLip.size]
          val contrastPartner = when (target.letter) {
            "خ" -> allLetters.first { it.letter == "ح" }
            "غ" -> allLetters.first { it.letter == "ع" }
            else -> allLetters.first { it.letter == "ق" }
          }

          if (qNum % 2 == 1) {
            val distractors = listOf(optOneBelow, optZeroDots, optTwoAbove)
            val options = (listOf(optOneAbove) + distractors).shuffled()
            questions.add(
              GameQuestion(
                questionNumber = qNum,
                gameType = GameType.DOT_MASTER,
                levelNumber = levelNumber,
                promptBn = "হরফ '${target.nameBn} (${target.letter})' এর নুকতার অবস্থান কোনটি?",
                promptEn = "What is the dot position of '${target.nameEn} (${target.letter})'?",
                promptAr = "ما هو موضع النقطة في حرف ${target.nameAr} (${target.letter})؟",
                displayArabic = target.letter,
                targetLetter = target,
                options = options,
                correctOptionId = optOneAbove.id,
                explanationBn = "${target.nameBn} (${target.letter}) এর মাথায়/উপরে ১টি নুকতা থাকে।",
                explanationEn = "${target.nameEn} features one dot on top."
              )
            )
          } else {
            val distractors = listOf(contrastPartner) + (dotlessLetters + singleBelow).filter { it.id != contrastPartner.id }.shuffled().take(2)
            val options = (listOf(target) + distractors).shuffled().mapIndexed { idx, let ->
              val (_, catDesc) = getDotCategoryForLetter(let)
              GameOption(
                id = "opt_l3_${idx}_${let.id}",
                arabicDisplay = let.letter,
                primaryText = "${let.nameBn} (${let.letter})",
                secondaryText = catDesc
              )
            }
            val correctId = options.first { it.arabicDisplay == target.letter }.id
            questions.add(
              GameQuestion(
                questionNumber = qNum,
                gameType = GameType.DOT_MASTER,
                levelNumber = levelNumber,
                promptBn = "সদৃশ হরফ '${contrastPartner.nameBn} (${contrastPartner.letter})' এর সাথে ১ নুকতাযুক্ত কোনটি?",
                promptEn = "Which letter paired with '${contrastPartner.nameEn}' has 1 dot above?",
                promptAr = "أي حرف شبيه بـ (${contrastPartner.letter}) وله نقطة بالأعلى؟",
                displayArabic = "${target.letter} বনাম ${contrastPartner.letter}",
                targetLetter = target,
                options = options,
                correctOptionId = correctId,
                explanationBn = "${target.nameBn} (${target.letter}) এর উপরে ১টি নুকতা রয়েছে, যেখানে ${contrastPartner.nameBn} নুকতাহীন বা ভিন্ন।",
                explanationEn = "${target.nameEn} has 1 dot on top distinguishing it from ${contrastPartner.nameEn}."
              )
            )
          }
        }

        // -------------------------------------------------------------------
        // LEVEL 4: ১ নুকতা: উপরে (স্বতন্ত্র - যাল ও যা)
        // -------------------------------------------------------------------
        4 -> {
          val target = singleAboveIndependent[qNum % singleAboveIndependent.size]
          val basePartner = if (target.letter == "ذ") allLetters.first { it.letter == "د" } else allLetters.first { it.letter == "ر" }

          if (qNum % 2 == 1) {
            val distractors = listOf(optZeroDots, optOneBelow, optTwoAbove)
            val options = (listOf(optOneAbove) + distractors).shuffled()
            questions.add(
              GameQuestion(
                questionNumber = qNum,
                gameType = GameType.DOT_MASTER,
                levelNumber = levelNumber,
                promptBn = "হরফ '${target.nameBn} (${target.letter})' এ কয়টি নুকতা এবং কোথায়?",
                promptEn = "How many dots and where on '${target.nameEn} (${target.letter})'?",
                promptAr = "كم عدد النقاط في حرف '${target.nameAr} (${target.letter})'؟",
                displayArabic = target.letter,
                targetLetter = target,
                options = options,
                correctOptionId = optOneAbove.id,
                explanationBn = "${target.nameBn} (${target.letter}) এর উপরে ১টি নুকতা থাকে। এর ভিত্তি হরফ ${basePartner.nameBn} (${basePartner.letter}) নুকতাহীন।",
                explanationEn = "${target.nameEn} has 1 dot above; its base partner ${basePartner.nameEn} is dotless."
              )
            )
          } else {
            val distractors = listOf(basePartner) + (dotlessLetters + singleBelow).filter { it.id != basePartner.id }.shuffled().take(2)
            val options = (listOf(target) + distractors).shuffled().mapIndexed { idx, let ->
              val (_, catDesc) = getDotCategoryForLetter(let)
              GameOption(
                id = "opt_l4_${idx}_${let.id}",
                arabicDisplay = let.letter,
                primaryText = "${let.nameBn} (${let.letter})",
                secondaryText = catDesc
              )
            }
            val correctId = options.first { it.arabicDisplay == target.letter }.id
            questions.add(
              GameQuestion(
                questionNumber = qNum,
                gameType = GameType.DOT_MASTER,
                levelNumber = levelNumber,
                promptBn = "স্বতন্ত্র হরফ '${basePartner.nameBn} (${basePartner.letter})' এর নুকতাযুক্ত সঙ্গী কোনটি?",
                promptEn = "Which letter is the dotted counterpart of '${basePartner.nameEn} (${basePartner.letter})'?",
                promptAr = "ما هو الحرف المنقوط المقابل لـ (${basePartner.letter})؟",
                displayArabic = "• উপরে ১ নুকতা",
                targetLetter = target,
                options = options,
                correctOptionId = correctId,
                explanationBn = "${basePartner.nameBn} (${basePartner.letter}) এর মাথায় ১ নুকতা দিলে তা হয় ${target.nameBn} (${target.letter})।",
                explanationEn = "Adding 1 dot on ${basePartner.nameEn} forms ${target.nameEn}."
              )
            )
          }
        }

        // -------------------------------------------------------------------
        // LEVEL 5: ১ নুকতা মাস্টার চ্যালেঞ্জ (১০টি ১-নুকতা হরফের সর্বাঙ্গীন পরীক্ষা)
        // -------------------------------------------------------------------
        5 -> {
          val target = allSingleDots[(qNum * 3 + levelNumber) % allSingleDots.size]
          val isBelow = target in singleBelow
          val (correctOpt, dotDesc) = getDotCategoryForLetter(target)

          if (qNum % 3 == 0) {
            // Connected shape dot recognition
            val form = if (qNum % 2 == 0) target.initial else target.medial
            val otherTeethOrCurv = (allSingleDots - target).shuffled().take(3)
            val options = (listOf(target) + otherTeethOrCurv).shuffled().mapIndexed { idx, let ->
              val letForm = if (qNum % 2 == 0) let.initial else let.medial
              GameOption(
                id = "opt_l5_c_${idx}_${let.id}",
                arabicDisplay = letForm,
                primaryText = "${let.nameBn} ($letForm)",
                secondaryText = "নুকতা: ${if (let in singleBelow) "নিচে ১টি" else "উপরে ১টি"}"
              )
            }
            val correctId = options.first { it.primaryText.startsWith(target.nameBn) }.id
            questions.add(
              GameQuestion(
                questionNumber = qNum,
                gameType = GameType.DOT_MASTER,
                levelNumber = levelNumber,
                promptBn = "সংযুক্ত রূপ '$form' এর নুকতা লক্ষ্য করে সঠিক হরফটি বাছুন:",
                promptEn = "Identify the letter from dot position on '$form':",
                promptAr = "ميز الحرف من موضع النقطة في '$form':",
                displayArabic = form,
                targetLetter = target,
                options = options,
                correctOptionId = correctId,
                explanationBn = "নুকতার অবস্থান দেখে নিশ্চিত হওয়া যায় এটি ${target.nameBn} ($form)।",
                explanationEn = "The single dot placement identifies ${target.nameEn}."
              )
            )
          } else {
            val distractors = listOf(
              if (isBelow) optOneAbove else optOneBelow,
              optTwoAbove,
              optZeroDots
            )
            val options = (listOf(correctOpt) + distractors).shuffled()
            questions.add(
              GameQuestion(
                questionNumber = qNum,
                gameType = GameType.DOT_MASTER,
                levelNumber = levelNumber,
                promptBn = "হরফ '${target.nameBn} (${target.letter})' এ নুকতার অবস্থান নিশ্চিত করুন:",
                promptEn = "Confirm dot placement for '${target.nameEn} (${target.letter})':",
                promptAr = "حدد موضع النقطة في حرف ${target.nameAr} (${target.letter}):",
                displayArabic = target.letter,
                targetLetter = target,
                options = options,
                correctOptionId = correctOpt.id,
                explanationBn = "${target.nameBn} (${target.letter}) এ $dotDesc রয়েছে।",
                explanationEn = "${target.nameEn} has $dotDesc."
              )
            )
          }
        }

        // -------------------------------------------------------------------
        // LEVEL 6: ২ নুকতা: উপরে (তা ও ক্বাফ)
        // -------------------------------------------------------------------
        6 -> {
          val target = doubleAbove[qNum % doubleAbove.size]
          if (qNum % 2 == 1) {
            val distractors = listOf(optTwoBelow, optOneAbove, optThreeAbove)
            val options = (listOf(optTwoAbove) + distractors).shuffled()
            questions.add(
              GameQuestion(
                questionNumber = qNum,
                gameType = GameType.DOT_MASTER,
                levelNumber = levelNumber,
                promptBn = "হরফ '${target.nameBn} (${target.letter})' এ নুকতা কয়টি এবং কোথায়?",
                promptEn = "How many dots and where on '${target.nameEn} (${target.letter})'?",
                promptAr = "كم نقطة وأين في حرف ${target.nameAr} (${target.letter})؟",
                displayArabic = target.letter,
                targetLetter = target,
                options = options,
                correctOptionId = optTwoAbove.id,
                explanationBn = "হরফ '${target.nameBn} (${target.letter})' এর উপরে ২টি নুকতা থাকে।",
                explanationEn = "${target.nameEn} (${target.letter}) has 2 dots above."
              )
            )
          } else {
            val distractors = (doubleBelow + allSingleAbove).shuffled().take(3)
            val options = (listOf(target) + distractors).shuffled().mapIndexed { idx, let ->
              val (_, catDesc) = getDotCategoryForLetter(let)
              GameOption(
                id = "opt_l6_${idx}_${let.id}",
                arabicDisplay = let.letter,
                primaryText = "${let.nameBn} (${let.letter})",
                secondaryText = catDesc
              )
            }
            val correctId = options.first { it.arabicDisplay == target.letter }.id
            questions.add(
              GameQuestion(
                questionNumber = qNum,
                gameType = GameType.DOT_MASTER,
                levelNumber = levelNumber,
                promptBn = "কোন হরফটির উপরে ২টি নুকতা বিদ্যমান?",
                promptEn = "Which letter features 2 dots placed above?",
                promptAr = "أي حرف يحمل نقطتين في أعلاه؟",
                displayArabic = "•• উপরে ২ নুকতা",
                targetLetter = target,
                options = options,
                correctOptionId = correctId,
                explanationBn = "${target.nameBn} (${target.letter}) এর উপরে ২টি নুকতা রয়েছে।",
                explanationEn = "${target.nameEn} contains two dots above."
              )
            )
          }
        }

        // -------------------------------------------------------------------
        // LEVEL 7: ২ নুকতা: নিচে (ইয়া - ي)
        // -------------------------------------------------------------------
        7 -> {
          val target = doubleBelow.first()
          val isConnected = qNum % 3 == 0
          val displayGlyph = if (isConnected) target.initial else target.letter
          val desc = if (isConnected) "শুরুর রূপ '$displayGlyph'" else "হরফ '$displayGlyph'"

          if (qNum % 2 == 1) {
            val distractors = listOf(optTwoAbove, optOneBelow, optZeroDots)
            val options = (listOf(optTwoBelow) + distractors).shuffled()
            questions.add(
              GameQuestion(
                questionNumber = qNum,
                gameType = GameType.DOT_MASTER,
                levelNumber = levelNumber,
                promptBn = "$desc (${target.nameBn}) এ নুকতার অবস্থান কোনটি?",
                promptEn = "What is the dot position on $desc (${target.nameEn})?",
                promptAr = "ما هو موضع النقطتين في $desc (${target.nameAr})؟",
                displayArabic = displayGlyph,
                targetLetter = target,
                options = options,
                correctOptionId = optTwoBelow.id,
                explanationBn = "আরবি বর্ণমালায় একমাত্র 'ইয়া (${target.letter})' হরফেই নিচে ২টি নুকতা থাকে।",
                explanationEn = "Yaa is the only Arabic letter with two dots below."
              )
            )
          } else {
            val distractors = (doubleAbove + singleBelow + tripleAbove).shuffled().take(3)
            val options = (listOf(target) + distractors).shuffled().mapIndexed { idx, let ->
              val (_, catDesc) = getDotCategoryForLetter(let)
              GameOption(
                id = "opt_l7_${idx}_${let.id}",
                arabicDisplay = let.letter,
                primaryText = "${let.nameBn} (${let.letter})",
                secondaryText = catDesc
              )
            }
            val correctId = options.first { it.arabicDisplay == target.letter }.id
            questions.add(
              GameQuestion(
                questionNumber = qNum,
                gameType = GameType.DOT_MASTER,
                levelNumber = levelNumber,
                promptBn = "কোন হরফটির নিচে ২টি নুকতা থাকে?",
                promptEn = "Which letter has 2 dots underneath?",
                promptAr = "أي حرف تحته نقطتان؟",
                displayArabic = "•• নিচে ২ নুকতা",
                targetLetter = target,
                options = options,
                correctOptionId = correctId,
                explanationBn = "ইয়া (${target.letter}) হরফের নিচে ২টি নুকতা থাকে।",
                explanationEn = "Yaa has 2 dots underneath."
              )
            )
          }
        }

        // -------------------------------------------------------------------
        // LEVEL 8: ২ নুকতার অবস্থান তুলনা (ت, ق বনাম ي এর পার্থক্য)
        // -------------------------------------------------------------------
        8 -> {
          val isTopTarget = qNum % 2 == 1
          val target = if (isTopTarget) doubleAbove.random() else doubleBelow.first()
          val other = if (isTopTarget) doubleBelow.first() else doubleAbove.random()

          val optTop = GameOption("opt_cmp_top", "•• উপরে", "উপরে ২টি নুকতা (তা/ক্বাফ)", "2 dots above (فوق الحرف)", "⬆️")
          val optBottom = GameOption("opt_cmp_bot", "•• নিচে", "নিচে ২টি নুকতা (ইয়া)", "2 dots below (تحت الحرف)", "⬇️")
          val optOne = GameOption("opt_cmp_one", "• একটি", "১টি নুকতা", "1 dot only", "1️⃣")
          val optNone = GameOption("opt_cmp_none", "০ নুকতাহীন", "কোনো নুকতা নেই", "No dots", "⭕")

          val correctOption = if (isTopTarget) optTop else optBottom
          val options = listOf(optTop, optBottom, optOne, optNone).shuffled()

          questions.add(
            GameQuestion(
              questionNumber = qNum,
              gameType = GameType.DOT_MASTER,
              levelNumber = levelNumber,
              promptBn = "হরফ '${target.nameBn} (${target.letter})' এ ২টি নুকতা কোথায় থাকে?",
              promptEn = "Where are the 2 dots placed on '${target.nameEn} (${target.letter})'?",
              promptAr = "أين تقع النقطتان في حرف '${target.nameAr} (${target.letter})'؟",
              displayArabic = "${target.letter} বনাম ${other.letter}",
              targetLetter = target,
              options = options,
              correctOptionId = correctOption.id,
              explanationBn = "${target.nameBn} (${target.letter}) এর ${if (isTopTarget) "উপরে" else "নিচে"} ২টি নুকতা থাকে, আর ${other.nameBn} (${other.letter}) এর ${if (isTopTarget) "নিচে" else "উপরে"} থাকে।",
              explanationEn = "${target.nameEn} has 2 dots ${if (isTopTarget) "above" else "below"}, contrasting with ${other.nameEn}."
            )
          )
        }

        // -------------------------------------------------------------------
        // LEVEL 9: যুক্ত রূপে ২ নুকতা (تـ, ـتـ বনাম يـ, ـيـ)
        // -------------------------------------------------------------------
        9 -> {
          val isTaa = qNum % 2 == 1
          val target = if (isTaa) allLetters.first { it.letter == "ت" } else allLetters.first { it.letter == "ي" }
          val form = if (qNum % 3 == 0) target.medial else target.initial
          val otherTeeth = allLetters.filter { it.letter in listOf("ب", "ت", "ث", "ن", "ي") && it.letter != target.letter }

          val options = (listOf(target) + otherTeeth.shuffled().take(3)).shuffled().mapIndexed { idx, let ->
            val letForm = if (qNum % 3 == 0) let.medial else let.initial
            GameOption(
              id = "opt_l9_${idx}_${let.id}",
              arabicDisplay = letForm,
              primaryText = "${let.nameBn} ($letForm)",
              secondaryText = "নুকতা: ${if (let.letter in listOf("ت", "ق")) "উপরে ২টি" else if (let.letter == "ي") "নিচে ২টি" else if (let.letter == "ث") "উপরে ৩টি" else if (let.letter == "ب") "নিচে ১টি" else "উপরে ১টি"}"
            )
          }
          val correctId = options.first { it.primaryText.startsWith(target.nameBn) }.id

          questions.add(
            GameQuestion(
              questionNumber = qNum,
              gameType = GameType.DOT_MASTER,
              levelNumber = levelNumber,
              promptBn = "দাঁতের উপর/নিচের ২টি নুকতা দেখে যুক্ত রূপ '$form' এর হরফটি চিনুন:",
              promptEn = "Identify the letter with 2 dots on tooth shape '$form':",
              promptAr = "ميز الحرف ذو النقطتين في الشكل المتصل '$form':",
              displayArabic = form,
              targetLetter = target,
              options = options,
              correctOptionId = correctId,
              explanationBn = "যুক্ত রূপে দাঁতের ${if (isTaa) "উপরে" else "নিচে"} ২টি নুকতা থাকলে তা ${target.nameBn} ($form)।",
              explanationEn = "Two dots ${if (isTaa) "above" else "below"} the tooth shape signifies ${target.nameEn}."
            )
          )
        }

        // -------------------------------------------------------------------
        // LEVEL 10: ২ নুকতা মাস্টার চ্যালেঞ্জ (সকল ২-নুকতা হরফের সমন্বিত রূপ)
        // -------------------------------------------------------------------
        10 -> {
          val target = allDoubleDots[(qNum * 2) % allDoubleDots.size]
          val isAbove = target in doubleAbove
          val correctOpt = if (isAbove) optTwoAbove else optTwoBelow
          val distractors = listOf(
            if (isAbove) optTwoBelow else optTwoAbove,
            optOneAbove,
            optThreeAbove
          )
          val options = (listOf(correctOpt) + distractors).shuffled()

          questions.add(
            GameQuestion(
              questionNumber = qNum,
              gameType = GameType.DOT_MASTER,
              levelNumber = levelNumber,
              promptBn = "হরফ '${target.nameBn} (${target.letter})' এ ২টি নুকতার সঠিক অবস্থান যাচাই করুন:",
              promptEn = "Verify 2-dot placement on '${target.nameEn} (${target.letter})':",
              promptAr = "تحقق من موضع النقطتين لحرف ${target.nameAr} (${target.letter}):",
              displayArabic = target.letter,
              targetLetter = target,
              options = options,
              correctOptionId = correctOpt.id,
              explanationBn = "${target.nameBn} (${target.letter}) এর ${if (isAbove) "উপরে" else "নিচে"} ২টি নুকতা বিদ্যমান।",
              explanationEn = "${target.nameEn} has 2 dots ${if (isAbove) "above" else "below"}."
            )
          )
        }

        // -------------------------------------------------------------------
        // LEVEL 11: ৩ নুকতা: সা (নৌকা হরফ - ث বনাম ب, ت, ن)
        // -------------------------------------------------------------------
        11 -> {
          val target = allLetters.first { it.letter == "ث" }
          val boatFamily = allLetters.filter { it.letter in listOf("ب", "ت", "ن") }

          if (qNum % 2 == 1) {
            val distractors = listOf(optTwoAbove, optOneBelow, optOneAbove)
            val options = (listOf(optThreeAbove) + distractors).shuffled()
            questions.add(
              GameQuestion(
                questionNumber = qNum,
                gameType = GameType.DOT_MASTER,
                levelNumber = levelNumber,
                promptBn = "হরফ '${target.nameBn} (${target.letter})' এ কয়টি নুকতা এবং কোথায়?",
                promptEn = "How many dots and where on '${target.nameEn} (${target.letter})'?",
                promptAr = "كم عدد النقاط في حرف ${target.nameAr} (${target.letter})؟",
                displayArabic = target.letter,
                targetLetter = target,
                options = options,
                correctOptionId = optThreeAbove.id,
                explanationBn = "${target.nameBn} (${target.letter}) এর উপরে ৩টি নুকতা থাকে।",
                explanationEn = "${target.nameEn} has three dots placed above."
              )
            )
          } else {
            val options = (listOf(target) + boatFamily).shuffled().mapIndexed { idx, let ->
              val (_, catDesc) = getDotCategoryForLetter(let)
              GameOption(
                id = "opt_l11_${idx}_${let.id}",
                arabicDisplay = let.letter,
                primaryText = "${let.nameBn} (${let.letter})",
                secondaryText = catDesc
              )
            }
            val correctId = options.first { it.arabicDisplay == target.letter }.id
            questions.add(
              GameQuestion(
                questionNumber = qNum,
                gameType = GameType.DOT_MASTER,
                levelNumber = levelNumber,
                promptBn = "নৌকা আকৃতির হরফগুলোর মধ্যে কোনটিতে ৩টি নুকতা থাকে?",
                promptEn = "Among boat letters, which one has 3 dots above?",
                promptAr = "أي من الحروف القاربية يحمل ثلاث نقاط بالأعلى؟",
                displayArabic = "∴ উপরে ৩ নুকতা",
                targetLetter = target,
                options = options,
                correctOptionId = correctId,
                explanationBn = "নৌকার মতো বাটিতে ৩টি নুকতা থাকলে তা ${target.nameBn} (${target.letter})।",
                explanationEn = "Three dots on the boat shape defines ${target.nameEn}."
              )
            )
          }
        }

        // -------------------------------------------------------------------
        // LEVEL 12: ৩ নুকতা: শিন (দাঁত হরফ - ش বনাম س)
        // -------------------------------------------------------------------
        12 -> {
          val target = allLetters.first { it.letter == "ش" }
          val seen = allLetters.first { it.letter == "س" }

          if (qNum % 2 == 1) {
            val distractors = listOf(optZeroDots, optOneAbove, optTwoAbove)
            val options = (listOf(optThreeAbove) + distractors).shuffled()
            questions.add(
              GameQuestion(
                questionNumber = qNum,
                gameType = GameType.DOT_MASTER,
                levelNumber = levelNumber,
                promptBn = "তিন দাঁতওয়ালা হরফ '${target.nameBn} (${target.letter})' এ নুকতা কয়টি?",
                promptEn = "How many dots on 3-teeth letter '${target.nameEn} (${target.letter})'?",
                promptAr = "كم نقطة على حرف الشين (${target.letter})؟",
                displayArabic = target.letter,
                targetLetter = target,
                options = options,
                correctOptionId = optThreeAbove.id,
                explanationBn = "${target.nameBn} (${target.letter}) এর উপরে ৩টি নুকতা থাকে। বিপরীতে ${seen.nameBn} (${seen.letter}) নুকতাহীন।",
                explanationEn = "${target.nameEn} has 3 dots on top; ${seen.nameEn} is dotless."
              )
            )
          } else {
            val distractors = listOf(seen) + allLetters.filter { it.letter in listOf("ص", "ض") }
            val options = (listOf(target) + distractors).shuffled().mapIndexed { idx, let ->
              val (_, catDesc) = getDotCategoryForLetter(let)
              GameOption(
                id = "opt_l12_${idx}_${let.id}",
                arabicDisplay = let.letter,
                primaryText = "${let.nameBn} (${let.letter})",
                secondaryText = catDesc
              )
            }
            val correctId = options.first { it.arabicDisplay == target.letter }.id
            questions.add(
              GameQuestion(
                questionNumber = qNum,
                gameType = GameType.DOT_MASTER,
                levelNumber = levelNumber,
                promptBn = "সদৃশ হরফ '${seen.nameBn} (${seen.letter})' এর ৩ নুকতাযুক্ত রূপ কোনটি?",
                promptEn = "Which letter is the 3-dot counterpart of '${seen.nameEn} (${seen.letter})'?",
                promptAr = "أي حرف يمثل الشكل ذو الثلاث نقاط المقابل للسين؟",
                displayArabic = "${target.letter} বনাম ${seen.letter}",
                targetLetter = target,
                options = options,
                correctOptionId = correctId,
                explanationBn = "${seen.nameBn} এর উপর ৩টি নুকতা দিলে তা ${target.nameBn} (${target.letter}) হয়।",
                explanationEn = "Placing 3 dots over Seen creates Sheen."
              )
            )
          }
        }

        // -------------------------------------------------------------------
        // LEVEL 13: ৩ নুকতা বনাম ১ ও ২ নুকতা (ث, ش বনাম ب, ت, س)
        // -------------------------------------------------------------------
        13 -> {
          val target = tripleAbove[qNum % tripleAbove.size]
          val countDesc = "৩টি নুকতা"
          val distractors = (allSingleDots + allDoubleDots + dotlessLetters).shuffled().take(3)
          val options = (listOf(target) + distractors).shuffled().mapIndexed { idx, let ->
            val (_, catDesc) = getDotCategoryForLetter(let)
            GameOption(
              id = "opt_l13_${idx}_${let.id}",
              arabicDisplay = let.letter,
              primaryText = "${let.nameBn} (${let.letter})",
              secondaryText = catDesc
            )
          }
          val correctId = options.first { it.arabicDisplay == target.letter }.id

          questions.add(
            GameQuestion(
              questionNumber = qNum,
              gameType = GameType.DOT_MASTER,
              levelNumber = levelNumber,
              promptBn = "নিচের কোন হরফটিতে $countDesc বিদ্যমান?",
              promptEn = "Which of these letters features 3 dots above?",
              promptAr = "أي من هذه الحروف يحمل ٣ نقاط بالأعلى؟",
              displayArabic = "∴ উপরে ৩ নুকতা",
              targetLetter = target,
              options = options,
              correctOptionId = correctId,
              explanationBn = "আরবি বর্ণমালায় কেবল '${target.nameBn} (${target.letter})' ও '${if (target.letter == "ث") "ش" else "ث"}' হরফে ৩টি নুকতা থাকে।",
              explanationEn = "Only Thaa and Sheen carry 3 dots in the Arabic alphabet."
            )
          )
        }

        // -------------------------------------------------------------------
        // LEVEL 14: সংযুক্ত রূপে ৩ নুকতা (ثـ, ـثـ, شـ, ـشـ)
        // -------------------------------------------------------------------
        14 -> {
          val target = tripleAbove[qNum % tripleAbove.size]
          val isInitial = qNum % 2 == 1
          val form = if (isInitial) target.initial else target.medial
          val distractors = if (target.letter == "ث") {
            allLetters.filter { it.letter in listOf("ب", "ت", "ن", "ي") }
          } else {
            allLetters.filter { it.letter in listOf("س", "ص", "ض") }
          }.shuffled().take(3)

          val options = (listOf(target) + distractors).shuffled().mapIndexed { idx, let ->
            val letForm = if (isInitial) let.initial else let.medial
            GameOption(
              id = "opt_l14_${idx}_${let.id}",
              arabicDisplay = letForm,
              primaryText = "${let.nameBn} ($letForm)",
              secondaryText = getDotCategoryForLetter(let).second
            )
          }
          val correctId = options.first { it.primaryText.startsWith(target.nameBn) }.id

          questions.add(
            GameQuestion(
              questionNumber = qNum,
              gameType = GameType.DOT_MASTER,
              levelNumber = levelNumber,
              promptBn = "সংযুক্ত রূপ '$form' এর উপরে ৩টি নুকতা দেখে হরফটি চিনুন:",
              promptEn = "Identify the letter from 3 dots above connected shape '$form':",
              promptAr = "ميز الحرف من النقاط الثلاث فوق الشكل المتصل '$form':",
              displayArabic = form,
              targetLetter = target,
              options = options,
              correctOptionId = correctId,
              explanationBn = "সংযুক্ত রূপ '$form' হলো ${target.nameBn} (${target.letter})।",
              explanationEn = "Connected form '$form' represents ${target.nameEn}."
            )
          )
        }

        // -------------------------------------------------------------------
        // LEVEL 15: ৩ নুকতা মাস্টার ⚡ (১২ সেকেন্ড দ্রুত পরীক্ষা)
        // -------------------------------------------------------------------
        15 -> {
          val target = (tripleAbove + allDoubleDots + allSingleDots).shuffled().first()
          val (correctOpt, catDesc) = getDotCategoryForLetter(target)
          val allOpts = listOf(optOneBelow, optOneAbove, optTwoAbove, optTwoBelow, optThreeAbove, optZeroDots)
          val distractors = (allOpts - correctOpt).shuffled().take(3)
          val options = (listOf(correctOpt) + distractors).shuffled()

          questions.add(
            GameQuestion(
              questionNumber = qNum,
              gameType = GameType.DOT_MASTER,
              levelNumber = levelNumber,
              promptBn = "দ্রুত উত্তর দিন! হরফ '${target.nameBn} (${target.letter})' এ নুকতা কয়টি ও কোথায়?",
              promptEn = "Fast! Dot count and placement for '${target.nameEn} (${target.letter})'?",
              promptAr = "بسرعة! ما هو عدد وموضع نقاط حرف ${target.nameAr} (${target.letter})؟",
              displayArabic = target.letter,
              targetLetter = target,
              options = options,
              correctOptionId = correctOpt.id,
              explanationBn = "${target.nameBn} (${target.letter}) এর $catDesc থাকে।",
              explanationEn = "${target.nameEn} has $catDesc.",
              isTimeAttack = true,
              timeLimitSeconds = 12
            )
          )
        }

        // -------------------------------------------------------------------
        // LEVEL 16: নুকতাহীন হরফ পরিচিতি (ح, س, ص, ط, ع)
        // -------------------------------------------------------------------
        16 -> {
          val target = dotlessLetters[(qNum * 2) % dotlessLetters.size]
          if (qNum % 2 == 1) {
            val distractors = listOf(optOneAbove, optOneBelow, optTwoAbove)
            val options = (listOf(optZeroDots) + distractors).shuffled()
            questions.add(
              GameQuestion(
                questionNumber = qNum,
                gameType = GameType.DOT_MASTER,
                levelNumber = levelNumber,
                promptBn = "হরফ '${target.nameBn} (${target.letter})' এ কয়টি নুকতা রয়েছে?",
                promptEn = "How many dots does letter '${target.nameEn} (${target.letter})' have?",
                promptAr = "كم نقطة في حرف ${target.nameAr} (${target.letter})؟",
                displayArabic = target.letter,
                targetLetter = target,
                options = options,
                correctOptionId = optZeroDots.id,
                explanationBn = "হরফ '${target.nameBn} (${target.letter})' একটি নুকতাহীন (مهملة) হরফ। এতে কোনো নুকতা নেই।",
                explanationEn = "${target.nameEn} (${target.letter}) is a dotless letter (without dots)."
              )
            )
          } else {
            val distractors = (allSingleDots + allDoubleDots).shuffled().take(3)
            val options = (listOf(target) + distractors).shuffled().mapIndexed { idx, let ->
              val (_, catDesc) = getDotCategoryForLetter(let)
              GameOption(
                id = "opt_l16_${idx}_${let.id}",
                arabicDisplay = let.letter,
                primaryText = "${let.nameBn} (${let.letter})",
                secondaryText = catDesc
              )
            }
            val correctId = options.first { it.arabicDisplay == target.letter }.id
            questions.add(
              GameQuestion(
                questionNumber = qNum,
                gameType = GameType.DOT_MASTER,
                levelNumber = levelNumber,
                promptBn = "নিচের কোন হরফটিতে কোনো নুকতা নেই (নুকতাহীন হরফ)?",
                promptEn = "Which of the following letters has NO dots (dotless)?",
                promptAr = "أي من الحروف التالية بدون نقاط (حرف مهمل)؟",
                displayArabic = "০ নুকতাহীন",
                targetLetter = target,
                options = options,
                correctOptionId = correctId,
                explanationBn = "${target.nameBn} (${target.letter}) নুকতাহীন হরফ।",
                explanationEn = "${target.nameEn} (${target.letter}) is completely dotless."
              )
            )
          }
        }

        // -------------------------------------------------------------------
        // LEVEL 17: নুকতাহীন বনাম ১ নুকতা (ح/خ, س/ش, ص/ض, ط/ظ, ع/غ)
        // -------------------------------------------------------------------
        17 -> {
          val pair = similarPairs[qNum % similarPairs.size]
          val dotlessLet = allLetters.first { it.letter == pair.first }
          val dottedLet = allLetters.first { it.letter == pair.second }
          val askForDotted = qNum % 2 == 1
          val target = if (askForDotted) dottedLet else dotlessLet
          val other = if (askForDotted) dotlessLet else dottedLet

          val distractors = (allLetters.filter { it.letter !in listOf(pair.first, pair.second) }).shuffled().take(2)
          val options = (listOf(target, other) + distractors).shuffled().mapIndexed { idx, let ->
            val (_, catDesc) = getDotCategoryForLetter(let)
            GameOption(
              id = "opt_l17_${idx}_${let.id}",
              arabicDisplay = let.letter,
              primaryText = "${let.nameBn} (${let.letter})",
              secondaryText = catDesc
            )
          }
          val correctId = options.first { it.arabicDisplay == target.letter }.id

          questions.add(
            GameQuestion(
              questionNumber = qNum,
              gameType = GameType.DOT_MASTER,
              levelNumber = levelNumber,
              promptBn = "সদৃশ জোড়া '${pair.first}' ও '${pair.second}' এর মধ্যে কোনটি ${if (askForDotted) "নুকতাযুক্ত" else "নুকতাহীন"}?",
              promptEn = "In pair '${pair.first}' and '${pair.second}', which one is ${if (askForDotted) "dotted" else "dotless"}?",
              promptAr = "بين '${pair.first}' و '${pair.second}'، أي حرف ${if (askForDotted) "منقوط" else "غير منقوط"}؟",
              displayArabic = "${pair.first} বনাম ${pair.second}",
              targetLetter = target,
              options = options,
              correctOptionId = correctId,
              explanationBn = "${target.nameBn} (${target.letter}) হলো ${if (askForDotted) "নুকতাযুক্ত হরফ" else "নুকতাহীন হরফ"}।",
              explanationEn = "${target.nameEn} (${target.letter}) is ${if (askForDotted) "dotted" else "dotless"}."
            )
          )
        }

        // -------------------------------------------------------------------
        // LEVEL 18: সদৃশ জোড়ায় নুকতা যাচাই (দাঁত, পেট ও গলার হরফ)
        // -------------------------------------------------------------------
        18 -> {
          val target = (allSingleDots + allDoubleDots + dotlessLetters).shuffled().first()
          val (correctOpt, catDesc) = getDotCategoryForLetter(target)
          val allOpts = listOf(optOneBelow, optOneAbove, optTwoAbove, optTwoBelow, optThreeAbove, optZeroDots)
          val distractors = (allOpts - correctOpt).shuffled().take(3)
          val options = (listOf(correctOpt) + distractors).shuffled()

          questions.add(
            GameQuestion(
              questionNumber = qNum,
              gameType = GameType.DOT_MASTER,
              levelNumber = levelNumber,
              promptBn = "হরফ '${target.nameBn} (${target.letter})' এর নুকতার অবস্থান ও সংখ্যা চিহ্নিত করুন:",
              promptEn = "Identify the dot position and count for '${target.nameEn} (${target.letter})':",
              promptAr = "حدد موضع وعدد نقاط حرف ${target.nameAr} (${target.letter}):",
              displayArabic = target.letter,
              targetLetter = target,
              options = options,
              correctOptionId = correctOpt.id,
              explanationBn = "${target.nameBn} (${target.letter}) হরফে $catDesc থাকে।",
              explanationEn = "${target.nameEn} (${target.letter}) has $catDesc."
            )
          )
        }

        // -------------------------------------------------------------------
        // LEVEL 19: নুকতা গণনা ও দ্রুত বাছাই (০, ১, ২, ৩ নুকতার সঠিক মিল)
        // -------------------------------------------------------------------
        19 -> {
          val targetDotCount = qNum % 4 // 0, 1, 2, or 3 dots
          val targetLettersList = when (targetDotCount) {
            0 -> dotlessLetters
            1 -> allSingleDots
            2 -> allDoubleDots
            else -> tripleAbove
          }
          val target = targetLettersList.random()
          val distractor1 = (if (targetDotCount != 0) dotlessLetters else allSingleDots).random()
          val distractor2 = (if (targetDotCount != 1) allSingleDots else allDoubleDots).random()
          val distractor3 = (if (targetDotCount != 3) tripleAbove else allDoubleDots).random()

          val candidateLetters = listOf(target, distractor1, distractor2, distractor3).distinctBy { it.id }
          val finalCandidates = if (candidateLetters.size < 4) {
            (candidateLetters + allLetters.filter { l -> candidateLetters.none { it.id == l.id } }).take(4)
          } else candidateLetters

          val options = finalCandidates.shuffled().mapIndexed { idx, let ->
            val (_, catDesc) = getDotCategoryForLetter(let)
            GameOption(
              id = "opt_l19_${idx}_${let.id}",
              arabicDisplay = let.letter,
              primaryText = "${let.nameBn} (${let.letter})",
              secondaryText = catDesc
            )
          }
          val correctId = options.first { it.arabicDisplay == target.letter }.id

          val countBn = when (targetDotCount) {
            0 -> "কোনো নুকতা নেই (০টি)"
            1 -> "১টি নুকতা"
            2 -> "২টি নুকতা"
            else -> "৩টি নুকতা"
          }

          questions.add(
            GameQuestion(
              questionNumber = qNum,
              gameType = GameType.DOT_MASTER,
              levelNumber = levelNumber,
              promptBn = "নিচের কোন হরফে $countBn রয়েছে?",
              promptEn = "Which letter has $targetDotCount dots?",
              promptAr = "أي حرف له $targetDotCount نقاط؟",
              displayArabic = if (targetDotCount == 0) "০ নুকতাহীন" else "$targetDotCount টি নুকতা",
              targetLetter = target,
              options = options,
              correctOptionId = correctId,
              explanationBn = "${target.nameBn} (${target.letter}) হরফে $countBn বিদ্যমান।",
              explanationEn = "${target.nameEn} (${target.letter}) features $targetDotCount dots."
            )
          )
        }

        // -------------------------------------------------------------------
        // LEVEL 20: গ্র্যান্ড নুকতা মাস্টার ⚡ (১০ সেকেন্ড টাইম-অ্যাটাক)
        // -------------------------------------------------------------------
        else -> {
          val target = allLetters[(qNum * 7 + levelNumber) % allLetters.size]
          val (correctOpt, catDesc) = getDotCategoryForLetter(target)
          val allOpts = listOf(optOneBelow, optOneAbove, optTwoAbove, optTwoBelow, optThreeAbove, optZeroDots)
          val distractors = (allOpts - correctOpt).shuffled().take(3)
          val options = (listOf(correctOpt) + distractors).shuffled()

          questions.add(
            GameQuestion(
              questionNumber = qNum,
              gameType = GameType.DOT_MASTER,
              levelNumber = levelNumber,
              promptBn = "⚡ গ্র্যান্ড স্পিড টেস্ট: হরফ '${target.nameBn} (${target.letter})' এ নুকতা কয়টি ও কোথায়?",
              promptEn = "⚡ Grand Speed Test: Dot count and placement for '${target.nameEn} (${target.letter})'?",
              promptAr = "⚡ اختبار السرعة الأكبر: نقاط حرف ${target.nameAr} (${target.letter})؟",
              displayArabic = target.letter,
              targetLetter = target,
              options = options,
              correctOptionId = correctOpt.id,
              explanationBn = "${target.nameBn} (${target.letter}) হরফে $catDesc থাকে।",
              explanationEn = "${target.nameEn} has $catDesc.",
              isTimeAttack = true,
              timeLimitSeconds = 10
            )
          )
        }
      }
    }

    return questions
  }

  // =========================================================================
  // 2. LETTER LINK (লেটার লিংক - ম্যাচ গ্রিড)
  // =========================================================================
  private fun generateLetterLinkQuestions(
    levelNumber: Int,
    tier: GameDifficultyTier,
    isTimeAttack: Boolean
  ): List<GameQuestion> {
    val allLetters = ArabicAlphabetRepository.letters
    val questions = mutableListOf<GameQuestion>()

    val earlyLetters = allLetters.subList(0, 7)    // ا, ب, ت, ث, ج, ح, خ
    val midLetters = allLetters.subList(7, 14)     // د, ذ, ر, ز, س, ش, ص
    val heavyLetters = allLetters.subList(14, 21)  // ض, ط, ظ, ع, غ, ف, ق
    val finalLetters = allLetters.subList(21, 28)  // ك, ل, م, ن, و, ه, ي

    // Phonetic pair map
    val phoneticMap = mapOf(
      "ت" to "ط", "ط" to "ت",
      "ث" to "س", "س" to "ص", "ص" to "س",
      "ذ" to "ظ", "ظ" to "ذ", "ز" to "ظ",
      "ح" to "ه", "ه" to "ح",
      "ق" to "ك", "ك" to "ق",
      "د" to "ض", "ض" to "د",
      "ع" to "ء", "ء" to "ع",
      "غ" to "خ", "خ" to "غ"
    )

    for (qNum in 1..20) {
      when (levelNumber) {
        // ===================================================================
        // TIER 1: LEVELS 1-5 (2x2 Basic Matching Grid - 4 Cards)
        // ===================================================================
        1 -> {
          // Level 1: Early Letters Link (ا, ب, ت, ث, ج, ح, خ)
          val target = earlyLetters[(qNum * 2 + 1) % earlyLetters.size]
          val distractors = earlyLetters.filter { it.id != target.id }.shuffled().take(3)
          val options = (listOf(target) + distractors).shuffled().mapIndexed { idx, let ->
            GameOption(
              id = "opt_l1_${qNum}_${idx}_${let.id}",
              arabicDisplay = let.letter,
              primaryText = "${let.nameBn} (${let.letter})",
              secondaryText = let.phonetic,
              iconEmoji = "💎"
            )
          }
          val correctId = options.first { it.arabicDisplay == target.letter }.id
          questions.add(
            GameQuestion(
              questionNumber = qNum,
              gameType = GameType.LETTER_LINK,
              levelNumber = levelNumber,
              promptBn = "হরফ '${target.nameBn} (${target.letter})' এর সাথে ২x২ গ্রিডে নিখুঁত জোড়া মিলিয়ে লিঙ্ক করুন:",
              promptEn = "Match letter '${target.nameEn} (${target.letter})' with its identical link on the 2x2 grid:",
              promptAr = "طابق الحرف '${target.nameAr} (${target.letter})' في شبكة المطابقة ٢×٢:",
              displayArabic = target.letter,
              targetLetter = target,
              options = options,
              correctOptionId = correctId,
              explanationBn = "হরফ '${target.letter}' এর নাম ${target.nameBn} এবং সঠিক উচ্চারণ '${target.phonetic}'।",
              explanationEn = "Letter '${target.letter}' is ${target.nameEn} (${target.phonetic})."
            )
          )
        }

        2 -> {
          // Level 2: Mid Letters Link (د, ذ, ر, ز, س, ش, ص)
          val target = midLetters[(qNum * 2 + 3) % midLetters.size]
          val distractors = midLetters.filter { it.id != target.id }.shuffled().take(3)
          val options = (listOf(target) + distractors).shuffled().mapIndexed { idx, let ->
            GameOption(
              id = "opt_l2_${qNum}_${idx}_${let.id}",
              arabicDisplay = let.letter,
              primaryText = "${let.nameBn} (${let.letter})",
              secondaryText = let.phonetic,
              iconEmoji = "💎"
            )
          }
          val correctId = options.first { it.arabicDisplay == target.letter }.id
          questions.add(
            GameQuestion(
              questionNumber = qNum,
              gameType = GameType.LETTER_LINK,
              levelNumber = levelNumber,
              promptBn = "হরফ '${target.nameBn} (${target.letter})' এর সাথে ২x২ গ্রিডে সঠিক জোড়া লিঙ্ক করুন:",
              promptEn = "Link letter '${target.nameEn} (${target.letter})' to its identical partner card:",
              promptAr = "صل الحرف '${target.nameAr} (${target.letter})' ببطاقته المطابقة:",
              displayArabic = target.letter,
              targetLetter = target,
              options = options,
              correctOptionId = correctId,
              explanationBn = "হরফ '${target.letter}' এর নাম ${target.nameBn} (${target.phonetic})।",
              explanationEn = "Letter '${target.letter}' is ${target.nameEn}."
            )
          )
        }

        3 -> {
          // Level 3: Heavy Letters Link (ض, ط, ظ, ع, غ, ف, ق)
          val target = heavyLetters[(qNum * 2 + 5) % heavyLetters.size]
          val distractors = heavyLetters.filter { it.id != target.id }.shuffled().take(3)
          val options = (listOf(target) + distractors).shuffled().mapIndexed { idx, let ->
            GameOption(
              id = "opt_l3_${qNum}_${idx}_${let.id}",
              arabicDisplay = let.letter,
              primaryText = "${let.nameBn} (${let.letter})",
              secondaryText = let.phonetic,
              iconEmoji = "💎"
            )
          }
          val correctId = options.first { it.arabicDisplay == target.letter }.id
          questions.add(
            GameQuestion(
              questionNumber = qNum,
              gameType = GameType.LETTER_LINK,
              levelNumber = levelNumber,
              promptBn = "ভারী হরফ '${target.nameBn} (${target.letter})' এর সাথে ২x২ গ্রিডে সঠিক ম্যাচ লিঙ্ক করুন:",
              promptEn = "Match heavy letter '${target.nameEn} (${target.letter})' on the 2x2 grid:",
              promptAr = "طابق الحرف المفخم '${target.nameAr} (${target.letter})' في الشبكة:",
              displayArabic = target.letter,
              targetLetter = target,
              options = options,
              correctOptionId = correctId,
              explanationBn = "হরফ '${target.letter}' (${target.nameBn}) একটি গভীর মাখরাজের বিশিষ্ট হরফ।",
              explanationEn = "Letter '${target.letter}' (${target.nameEn}) is an emphatic letter."
            )
          )
        }

        4 -> {
          // Level 4: Final Letters Link (ك, ل, م, ن, و, ه, ي)
          val target = finalLetters[(qNum * 2 + 1) % finalLetters.size]
          val distractors = finalLetters.filter { it.id != target.id }.shuffled().take(3)
          val options = (listOf(target) + distractors).shuffled().mapIndexed { idx, let ->
            GameOption(
              id = "opt_l4_${qNum}_${idx}_${let.id}",
              arabicDisplay = let.letter,
              primaryText = "${let.nameBn} (${let.letter})",
              secondaryText = let.phonetic,
              iconEmoji = "💎"
            )
          }
          val correctId = options.first { it.arabicDisplay == target.letter }.id
          questions.add(
            GameQuestion(
              questionNumber = qNum,
              gameType = GameType.LETTER_LINK,
              levelNumber = levelNumber,
              promptBn = "হরফ '${target.nameBn} (${target.letter})' এর সাথে ২x২ গ্রিডে সঠিক জোড়া লিঙ্ক করুন:",
              promptEn = "Link letter '${target.nameEn} (${target.letter})' on the 2x2 match grid:",
              promptAr = "اربط الحرف '${target.nameAr} (${target.letter})' في شبكة المطابقة:",
              displayArabic = target.letter,
              targetLetter = target,
              options = options,
              correctOptionId = correctId,
              explanationBn = "হরফ '${target.letter}' (${target.nameBn}) এর মাখরাজ: ${target.makhrajBn}।",
              explanationEn = "Letter '${target.letter}' (${target.nameEn}) Makhraj: ${target.makhrajEn}."
            )
          )
        }

        5 -> {
          // Level 5: 2x2 Master Match Challenge (All 28 Letters)
          val target = allLetters[(qNum * 3 + levelNumber) % allLetters.size]
          val distractors = allLetters.filter { it.id != target.id }.shuffled().take(3)
          val options = (listOf(target) + distractors).shuffled().mapIndexed { idx, let ->
            GameOption(
              id = "opt_l5_${qNum}_${idx}_${let.id}",
              arabicDisplay = let.letter,
              primaryText = "${let.nameBn} (${let.letter})",
              secondaryText = let.phonetic,
              iconEmoji = "👑"
            )
          }
          val correctId = options.first { it.arabicDisplay == target.letter }.id
          questions.add(
            GameQuestion(
              questionNumber = qNum,
              gameType = GameType.LETTER_LINK,
              levelNumber = levelNumber,
              promptBn = "২x২ মাস্টার গ্রিড: হরফ '${target.nameBn} (${target.letter})' এর সঠিক জোড়া লিঙ্ক করুন:",
              promptEn = "2x2 Master Grid: Connect letter '${target.nameEn} (${target.letter})' to its matching card:",
              promptAr = "تحدي شبكة ٢×٢ الشامل: طابق الحرف '${target.nameAr} (${target.letter})':",
              displayArabic = target.letter,
              targetLetter = target,
              options = options,
              correctOptionId = correctId,
              explanationBn = "অভিনন্দন! হরফ '${target.letter}' এর সাথে ${target.nameBn} কার্ডের নিখুঁত সংযোগ হয়েছে।",
              explanationEn = "Perfect link for letter '${target.letter}' (${target.nameEn})."
            )
          )
        }

        // ===================================================================
        // TIER 2: LEVELS 6-10 (3x3 Shape-Variant Grid - 6 Cards in 3 Columns)
        // ===================================================================
        6 -> {
          // Level 6: Initial Forms (শুরুর রূপ بـ, تـ, ثـ, نـ, يـ etc.)
          val pool = allLetters.filter { it.letter in listOf("ب", "ت", "ث", "ن", "ي", "ف", "ق", "ك", "ل", "م") }
          val target = pool[(qNum * 2) % pool.size]
          val distractors = pool.filter { it.id != target.id }.shuffled().take(5)
          val options = (listOf(target) + distractors).shuffled().mapIndexed { idx, let ->
            GameOption(
              id = "opt_l6_${qNum}_${idx}_${let.id}",
              arabicDisplay = let.initial,
              primaryText = "${let.nameBn} (শুরু)",
              secondaryText = let.initial
            )
          }
          val correctId = options.first { it.primaryText.startsWith(target.nameBn) }.id
          questions.add(
            GameQuestion(
              questionNumber = qNum,
              gameType = GameType.LETTER_LINK,
              levelNumber = levelNumber,
              promptBn = "মূল হরফ '${target.nameBn} (${target.letter})' এর সাথে এর প্রারম্ভিক রূপ (Initial Form) লিঙ্ক করুন:",
              promptEn = "Match isolated letter '${target.nameEn} (${target.letter})' to its initial form:",
              promptAr = "اربط الحرف '${target.nameAr} (${target.letter})' بشكله في بداية الكلمة:",
              displayArabic = target.letter,
              targetLetter = target,
              options = options,
              correctOptionId = correctId,
              explanationBn = "হরফ '${target.letter}' শব্দের শুরুতে '${target.initial}' রূপ ধারণ করে।",
              explanationEn = "Letter '${target.letter}' appears as '${target.initial}' at the beginning of words."
            )
          )
        }

        7 -> {
          // Level 7: Curved & Throat Letters (جـ, حـ, خـ, عـ, غـ)
          val pool = allLetters.filter { it.letter in listOf("ج", "ح", "خ", "ع", "غ") }
          val target = pool[(qNum * 2 + 1) % pool.size]
          val distractors = (pool.filter { it.id != target.id } + allLetters.filter { it.letter in listOf("ف", "ق", "ص") }).shuffled().take(5)
          val isMedial = qNum % 2 == 1
          val options = (listOf(target) + distractors).shuffled().mapIndexed { idx, let ->
            val form = if (isMedial) let.medial else let.initial
            GameOption(
              id = "opt_l7_${qNum}_${idx}_${let.id}",
              arabicDisplay = form,
              primaryText = "${let.nameBn} (${if (isMedial) "মধ্য" else "শুরু"})",
              secondaryText = form
            )
          }
          val correctId = options.first { it.primaryText.startsWith(target.nameBn) }.id
          val formLabel = if (isMedial) "মধ্য রূপ (Medial)" else "প্রারম্ভিক রূপ (Initial)"
          questions.add(
            GameQuestion(
              questionNumber = qNum,
              gameType = GameType.LETTER_LINK,
              levelNumber = levelNumber,
              promptBn = "হরফ '${target.nameBn} (${target.letter})' এর $formLabel ৩x৩ গ্রিডে লিঙ্ক করুন:",
              promptEn = "Link letter '${target.nameEn} (${target.letter})' to its $formLabel form:",
              promptAr = "طابق الحرف '${target.nameAr} (${target.letter})' بشكله المتصل:",
              displayArabic = target.letter,
              targetLetter = target,
              options = options,
              correctOptionId = correctId,
              explanationBn = "হরফ '${target.letter}' পেট ও গলার হরফ, যুক্ত অবস্থায় এর রূপ '${if (isMedial) target.medial else target.initial}'।",
              explanationEn = "Letter '${target.letter}' takes the form '${if (isMedial) target.medial else target.initial}'."
            )
          )
        }

        8 -> {
          // Level 8: Teeth & Tall Letters (سـ, شـ, صـ, ضـ, كـ, لـ)
          val pool = allLetters.filter { it.letter in listOf("س", "ش", "ص", "ض", "ك", "ل", "ط", "ظ") }
          val target = pool[(qNum * 2 + 3) % pool.size]
          val distractors = pool.filter { it.id != target.id }.shuffled().take(5)
          val options = (listOf(target) + distractors).shuffled().mapIndexed { idx, let ->
            GameOption(
              id = "opt_l8_${qNum}_${idx}_${let.id}",
              arabicDisplay = let.initial,
              primaryText = "${let.nameBn} (শুরু)",
              secondaryText = let.initial
            )
          }
          val correctId = options.first { it.primaryText.startsWith(target.nameBn) }.id
          questions.add(
            GameQuestion(
              questionNumber = qNum,
              gameType = GameType.LETTER_LINK,
              levelNumber = levelNumber,
              promptBn = "দাঁত ও লম্বা হরফ '${target.nameBn} (${target.letter})' এর সংযুক্ত রূপ লিঙ্ক করুন:",
              promptEn = "Link teeth/tall letter '${target.nameEn} (${target.letter})' to its connected form:",
              promptAr = "اربط الحرف '${target.nameAr} (${target.letter})' بشكله في بداية الكلمة:",
              displayArabic = target.letter,
              targetLetter = target,
              options = options,
              correctOptionId = correctId,
              explanationBn = "হরফ '${target.letter}' এর প্রারম্ভিক সংযুক্ত রূপ হল '${target.initial}'।",
              explanationEn = "Letter '${target.letter}' connects as '${target.initial}'."
            )
          )
        }

        9 -> {
          // Level 9: Medial & Final Mix (ـبـ, ـجـ, ـعـ, ـم, ـي)
          val pool = allLetters.filter { it.letter in listOf("ب", "ج", "ع", "م", "ي", "ت", "ن", "ف", "س") }
          val target = pool[(qNum * 3) % pool.size]
          val isFinal = qNum % 2 == 1
          val distractors = pool.filter { it.id != target.id }.shuffled().take(5)
          val options = (listOf(target) + distractors).shuffled().mapIndexed { idx, let ->
            val form = if (isFinal) let.finalForm else let.medial
            GameOption(
              id = "opt_l9_${qNum}_${idx}_${let.id}",
              arabicDisplay = form,
              primaryText = "${let.nameBn} (${if (isFinal) "শেষ" else "মধ্য"})",
              secondaryText = form
            )
          }
          val correctId = options.first { it.primaryText.startsWith(target.nameBn) }.id
          val formLabel = if (isFinal) "সমাপ্তি রূপ (Final Form)" else "মধ্যবর্তী রূপ (Medial Form)"
          questions.add(
            GameQuestion(
              questionNumber = qNum,
              gameType = GameType.LETTER_LINK,
              levelNumber = levelNumber,
              promptBn = "হরফ '${target.nameBn} (${target.letter})' এর $formLabel ৩x৩ গ্রিডে লিঙ্ক করুন:",
              promptEn = "Link root letter '${target.nameEn} (${target.letter})' to its $formLabel:",
              promptAr = "طابق الحرف '${target.nameAr} (${target.letter})' بشكله في الكلمة:",
              displayArabic = target.letter,
              targetLetter = target,
              options = options,
              correctOptionId = correctId,
              explanationBn = "হরফ '${target.letter}' এর রূপ: '${if (isFinal) target.finalForm else target.medial}'।",
              explanationEn = "Letter '${target.letter}' connects as '${if (isFinal) target.finalForm else target.medial}'."
            )
          )
        }

        10 -> {
          // Level 10: 3x3 Shape-Variant Master Challenge
          val target = allLetters[(qNum * 5 + levelNumber) % allLetters.size]
          val formType = when (qNum % 3) {
            0 -> "initial"
            1 -> "medial"
            else -> "final"
          }
          val targetForm = when (formType) {
            "initial" -> target.initial
            "medial" -> target.medial
            else -> target.finalForm
          }
          val distractors = allLetters.filter { it.id != target.id }.shuffled().take(5)
          val options = (listOf(target) + distractors).shuffled().mapIndexed { idx, let ->
            val form = when (formType) {
              "initial" -> let.initial
              "medial" -> let.medial
              else -> let.finalForm
            }
            GameOption(
              id = "opt_l10_${qNum}_${idx}_${let.id}",
              arabicDisplay = form,
              primaryText = "${let.nameBn} (${form})",
              secondaryText = let.nameEn
            )
          }
          val correctId = options.first { it.primaryText.startsWith(target.nameBn) }.id
          val formNameBn = when (formType) {
            "initial" -> "প্রারম্ভিক রূপ"
            "medial" -> "মধ্য রূপ"
            else -> "শেষ রূপ"
          }
          questions.add(
            GameQuestion(
              questionNumber = qNum,
              gameType = GameType.LETTER_LINK,
              levelNumber = levelNumber,
              promptBn = "৩x৩ মাস্টার গ্রিড: '${target.nameBn} (${target.letter})' এর $formNameBn লিঙ্ক করুন:",
              promptEn = "3x3 Master Grid: Match '${target.nameEn} (${target.letter})' to its connected form:",
              promptAr = "تحدي شبكة الأشكال ٣×٣: اربط الحرف '${target.nameAr}' بشكله المناسب:",
              displayArabic = target.letter,
              targetLetter = target,
              options = options,
              correctOptionId = correctId,
              explanationBn = "হরফ '${target.letter}' এর সঠিক রূপ হল '$targetForm' (${target.nameBn})।",
              explanationEn = "Letter '${target.letter}' matches '$targetForm'."
            )
          )
        }

        // ===================================================================
        // TIER 3: LEVELS 11-15 (4x4 Phonetic Pair Grid - 8 Cards in 4 Columns)
        // ===================================================================
        11 -> {
          // Level 11: Taa vs Twaa (ت / ط)
          val isTwaa = qNum % 2 == 1
          val targetLetterStr = if (isTwaa) "ط" else "ت"
          val partnerLetterStr = if (isTwaa) "ت" else "ط"
          val target = allLetters.first { it.letter == targetLetterStr }
          val partner = allLetters.first { it.letter == partnerLetterStr }
          val distractors = allLetters.filter { it.letter in listOf("د", "ض", "ظ", "ث", "س", "ص") }.shuffled().take(6)
          val options = (listOf(partner, target) + distractors).distinctBy { it.id }.take(8).shuffled().mapIndexed { idx, let ->
            GameOption(
              id = "opt_l11_${qNum}_${idx}_${let.id}",
              arabicDisplay = let.letter,
              primaryText = let.nameBn,
              secondaryText = if (let.letter in listOf("ط", "ض", "ظ", "ص")) "ভারী (মুফাখ্খাম)" else "নরম (তারক্বীক্ব)",
              iconEmoji = if (let.letter in listOf("ط", "ض", "ظ", "ص")) "🔊" else "🎵"
            )
          }
          val correctId = options.first { it.arabicDisplay == partner.letter }.id
          questions.add(
            GameQuestion(
              questionNumber = qNum,
              gameType = GameType.LETTER_LINK,
              levelNumber = levelNumber,
              promptBn = "হরফ '${target.nameBn} (${target.letter})' এর সাথে এর ফনেটিক পেয়ার '${partner.nameBn} (${partner.letter})' লিঙ্ক করুন:",
              promptEn = "Link letter '${target.nameEn} (${target.letter})' to its phonetic partner '${partner.nameEn} (${partner.letter})':",
              promptAr = "اربط الحرف '${target.nameAr} (${target.letter})' بزوجِه الصوتي المقابل:",
              displayArabic = target.letter,
              targetLetter = target,
              options = options,
              correctOptionId = correctId,
              explanationBn = "তা (ت) নরম ও তারক্বীক্ব, আর ত্বা (ط) ভারী ও ইস্তিলা মাখরাজ সম্পন্ন হরফ।",
              explanationEn = "Taa (ت) is soft while Twaa (ط) is heavy and emphatic."
            )
          )
        }

        12 -> {
          // Level 12: Z-Sound Triad (ذ / ز / ظ)
          val triad = listOf("ذ", "ز", "ظ")
          val targetLetterStr = triad[qNum % 3]
          val partnerLetterStr = triad[(qNum + 1) % 3]
          val target = allLetters.first { it.letter == targetLetterStr }
          val partner = allLetters.first { it.letter == partnerLetterStr }
          val distractors = allLetters.filter { it.letter in listOf("ث", "س", "ص", "د", "ض", "ت") }.shuffled().take(6)
          val options = (listOf(partner, target) + distractors).distinctBy { it.id }.take(8).shuffled().mapIndexed { idx, let ->
            GameOption(
              id = "opt_l12_${qNum}_${idx}_${let.id}",
              arabicDisplay = let.letter,
              primaryText = let.nameBn,
              secondaryText = let.phonetic,
              iconEmoji = "🎧"
            )
          }
          val correctId = options.first { it.arabicDisplay == partner.letter }.id
          questions.add(
            GameQuestion(
              questionNumber = qNum,
              gameType = GameType.LETTER_LINK,
              levelNumber = levelNumber,
              promptBn = "ঝ-ধ্বনির ত্রয়ী: হরফ '${target.nameBn} (${target.letter})' এর ফনেটিক সহচর '${partner.nameBn} (${partner.letter})' লিঙ্ক করুন:",
              promptEn = "Z-Sound Triad: Link '${target.nameEn} (${target.letter})' with its partner '${partner.nameEn} (${partner.letter})':",
              promptAr = "اربط الحرف '${target.nameAr} (${target.letter})' بالصوت المتقارب له:",
              displayArabic = target.letter,
              targetLetter = target,
              options = options,
              correctOptionId = correctId,
              explanationBn = "যাল (ذ) নরম জিহ্বার অগ্রভাগ, যা (ز) শিস ধ্বনি, জোয়া (ظ) ভারী ও মুখ ভরে উচ্চারিত হয়।",
              explanationEn = "Dhal (ذ) is soft, Zay (ز) is whistling, and Zwaa (ظ) is emphatic."
            )
          )
        }

        13 -> {
          // Level 13: S & Th Sounds (س / ص / ث)
          val sGroup = listOf("س", "ص", "ث")
          val targetLetterStr = sGroup[qNum % 3]
          val partnerLetterStr = sGroup[(qNum + 2) % 3]
          val target = allLetters.first { it.letter == targetLetterStr }
          val partner = allLetters.first { it.letter == partnerLetterStr }
          val distractors = allLetters.filter { it.letter in listOf("ش", "ز", "ذ", "ظ", "ت", "ط") }.shuffled().take(6)
          val options = (listOf(partner, target) + distractors).distinctBy { it.id }.take(8).shuffled().mapIndexed { idx, let ->
            GameOption(
              id = "opt_l13_${qNum}_${idx}_${let.id}",
              arabicDisplay = let.letter,
              primaryText = let.nameBn,
              secondaryText = let.phonetic,
              iconEmoji = "🎧"
            )
          }
          val correctId = options.first { it.arabicDisplay == partner.letter }.id
          questions.add(
            GameQuestion(
              questionNumber = qNum,
              gameType = GameType.LETTER_LINK,
              levelNumber = levelNumber,
              promptBn = "হরফ '${target.nameBn} (${target.letter})' এর সাথে এর নিকটবর্তী ধ্বনি '${partner.nameBn} (${partner.letter})' লিঙ্ক করুন:",
              promptEn = "Link letter '${target.nameEn} (${target.letter})' to similar sound '${partner.nameEn} (${partner.letter})':",
              promptAr = "طابق الحرف '${target.nameAr} (${target.letter})' مع نظيره الصوتي:",
              displayArabic = target.letter,
              targetLetter = target,
              options = options,
              correctOptionId = correctId,
              explanationBn = "সিন (س) শিসযুক্ত নরম, সদ (ص) ভারী শিসযুক্ত এবং সা (ث) নরম বাতাসযুক্ত।",
              explanationEn = "Seen is soft whistle, Saad is heavy whistle, and Thaa is soft lisp."
            )
          )
        }

        14 -> {
          // Level 14: Throat H & Ayn (ح / هـ / ع)
          val throatGroup = listOf("ح", "ه", "ع", "غ")
          val targetLetterStr = throatGroup[qNum % 4]
          val partnerLetterStr = throatGroup[(qNum + 1) % 4]
          val target = allLetters.first { it.letter == targetLetterStr }
          val partner = allLetters.first { it.letter == partnerLetterStr }
          val distractors = allLetters.filter { it.letter in listOf("ء", "خ", "ق", "ك", "ف", "ج") }.shuffled().take(6)
          val options = (listOf(partner, target) + distractors).distinctBy { it.id }.take(8).shuffled().mapIndexed { idx, let ->
            GameOption(
              id = "opt_l14_${qNum}_${idx}_${let.id}",
              arabicDisplay = let.letter,
              primaryText = let.nameBn,
              secondaryText = "মাখরাজ: ${let.makhrajBn.take(15)}...",
              iconEmoji = "🔊"
            )
          }
          val correctId = options.first { it.arabicDisplay == partner.letter }.id
          questions.add(
            GameQuestion(
              questionNumber = qNum,
              gameType = GameType.LETTER_LINK,
              levelNumber = levelNumber,
              promptBn = "কণ্ঠনালীর হরফ '${target.nameBn} (${target.letter})' এর সাথে এর সহচর '${partner.nameBn} (${partner.letter})' লিঙ্ক করুন:",
              promptEn = "Link throat letter '${target.nameEn} (${target.letter})' to its partner '${partner.nameEn} (${partner.letter})':",
              promptAr = "اربط حرف الحلق '${target.nameAr} (${target.letter})' برفيقه الحلقي:",
              displayArabic = target.letter,
              targetLetter = target,
              options = options,
              correctOptionId = correctId,
              explanationBn = "কণ্ঠনালীর মধ্যভাগ থেকে হা (ح) ও আইন (ع), এবং নিম্নভাগ থেকে হা (ه) উচ্চারিত হয়।",
              explanationEn = "Haa (ح) and Ayn (ع) articulate from mid-throat, while Ha (ه) from chest/lower throat."
            )
          )
        }

        15 -> {
          // Level 15: 4x4 Phonetic Master ⚡ (12 seconds)
          val pair = phoneticPairs[qNum % phoneticPairs.size]
          val letA = allLetters.first { it.letter == pair.first }
          val letB = allLetters.first { it.letter == pair.second }
          val target = if (qNum % 2 == 0) letA else letB
          val partner = if (qNum % 2 == 0) letB else letA
          val distractors = allLetters.filter { it.id != target.id && it.id != partner.id }.shuffled().take(6)
          val options = (listOf(partner, target) + distractors).distinctBy { it.id }.take(8).shuffled().mapIndexed { idx, let ->
            GameOption(
              id = "opt_l15_${qNum}_${idx}_${let.id}",
              arabicDisplay = let.letter,
              primaryText = let.nameBn,
              secondaryText = let.phonetic,
              iconEmoji = "⚡"
            )
          }
          val correctId = options.first { it.arabicDisplay == partner.letter }.id
          questions.add(
            GameQuestion(
              questionNumber = qNum,
              gameType = GameType.LETTER_LINK,
              levelNumber = levelNumber,
              promptBn = "⚡ ফনেটিক স্পিড টেস্ট: '${target.nameBn} (${target.letter})' এর সঠিক জোড়া '${partner.nameBn} (${partner.letter})' লিঙ্ক করুন:",
              promptEn = "⚡ Phonetic Speed Test: Link '${target.nameEn} (${target.letter})' to '${partner.nameEn} (${partner.letter})':",
              promptAr = "⚡ اختبار الأزواج الصوتية السريع: اربط الحرف '${target.nameAr}' بزوجه:",
              displayArabic = target.letter,
              targetLetter = target,
              options = options,
              correctOptionId = correctId,
              explanationBn = "হরফ '${target.letter}' এবং '${partner.letter}' একে অপরের ফনেটিক ও মাখরাজ পেয়ার।",
              explanationEn = "Letters '${target.letter}' and '${partner.letter}' form a phonetic pair.",
              isTimeAttack = true,
              timeLimitSeconds = 12
            )
          )
        }

        // ===================================================================
        // TIER 4: LEVELS 16-20 (Rapid Time-Attack Grid - 8 Cards in 4 Columns)
        // ===================================================================
        16 -> {
          // Level 16: Rapid Memory Recall I ⚡ (12s)
          val target = allLetters[(qNum * 4 + 7) % allLetters.size]
          val distractors = allLetters.filter { it.id != target.id }.shuffled().take(7)
          val options = (listOf(target) + distractors).shuffled().mapIndexed { idx, let ->
            GameOption(
              id = "opt_l16_${qNum}_${idx}_${let.id}",
              arabicDisplay = let.letter,
              primaryText = let.nameBn,
              secondaryText = let.phonetic,
              iconEmoji = "⚡"
            )
          }
          val correctId = options.first { it.arabicDisplay == target.letter }.id
          questions.add(
            GameQuestion(
              questionNumber = qNum,
              gameType = GameType.LETTER_LINK,
              levelNumber = levelNumber,
              promptBn = "⚡ ১২ সেকেন্ড মেমোরি রিকল: হরফ '${target.nameBn} (${target.letter})' এর নিখুঁত কার্ডে লিঙ্ক করুন!",
              promptEn = "⚡ 12s Memory Recall: Quick-link letter '${target.nameEn} (${target.letter})'!",
              promptAr = "⚡ استرجاع الذاكرة السريع (١٢ ثانية): طابق الحرف '${target.nameAr}'!",
              displayArabic = target.letter,
              targetLetter = target,
              options = options,
              correctOptionId = correctId,
              explanationBn = "দ্রুত ও নিখুঁত! '${target.letter}' হল ${target.nameBn}।",
              explanationEn = "Fast and accurate! '${target.letter}' is ${target.nameEn}.",
              isTimeAttack = true,
              timeLimitSeconds = 12
            )
          )
        }

        17 -> {
          // Level 17: Rapid Memory Recall II ⚡ (11s)
          val target = allLetters[(qNum * 6 + 11) % allLetters.size]
          val distractors = allLetters.filter { it.id != target.id }.shuffled().take(7)
          val options = (listOf(target) + distractors).shuffled().mapIndexed { idx, let ->
            GameOption(
              id = "opt_l17_${qNum}_${idx}_${let.id}",
              arabicDisplay = let.initial,
              primaryText = "${let.nameBn} (শুরু)",
              secondaryText = let.initial,
              iconEmoji = "⚡"
            )
          }
          val correctId = options.first { it.primaryText.startsWith(target.nameBn) }.id
          questions.add(
            GameQuestion(
              questionNumber = qNum,
              gameType = GameType.LETTER_LINK,
              levelNumber = levelNumber,
              promptBn = "⚡ ১১ সেকেন্ড রূপ রিকল: '${target.nameBn} (${target.letter})' এর সংযুক্ত রূপ লিঙ্ক করুন!",
              promptEn = "⚡ 11s Form Recall: Link '${target.nameEn} (${target.letter})' to its connected form!",
              promptAr = "⚡ استرجاع الشكل (١١ ثانية): اربط الحرف '${target.nameAr}' بشكله المتصل!",
              displayArabic = target.letter,
              targetLetter = target,
              options = options,
              correctOptionId = correctId,
              explanationBn = "হরফ '${target.letter}' এর প্রারম্ভিক রূপ '${target.initial}'।",
              explanationEn = "Initial form of '${target.letter}' is '${target.initial}'.",
              isTimeAttack = true,
              timeLimitSeconds = 11
            )
          )
        }

        18 -> {
          // Level 18: High-Speed Form & Sound Link ⚡ (10s)
          val target = allLetters[(qNum * 7 + 13) % allLetters.size]
          val isMedial = qNum % 2 == 1
          val distractors = allLetters.filter { it.id != target.id }.shuffled().take(7)
          val options = (listOf(target) + distractors).shuffled().mapIndexed { idx, let ->
            val form = if (isMedial) let.medial else let.finalForm
            GameOption(
              id = "opt_l18_${qNum}_${idx}_${let.id}",
              arabicDisplay = form,
              primaryText = "${let.nameBn} (${if (isMedial) "মধ্য" else "শেষ"})",
              secondaryText = let.phonetic,
              iconEmoji = "⚡"
            )
          }
          val correctId = options.first { it.primaryText.startsWith(target.nameBn) }.id
          val formLabel = if (isMedial) "মধ্য রূপ" else "শেষ রূপ"
          questions.add(
            GameQuestion(
              questionNumber = qNum,
              gameType = GameType.LETTER_LINK,
              levelNumber = levelNumber,
              promptBn = "⚡ ১০ সেকেন্ড স্পিড লিংক: '${target.nameBn} (${target.letter})' এর $formLabel লিঙ্ক করুন!",
              promptEn = "⚡ 10s Speed Link: Match '${target.nameEn} (${target.letter})' to its connected form!",
              promptAr = "⚡ مطابقة السرعة (١٠ ثوان): طابق الحرف '${target.nameAr}'!",
              displayArabic = target.letter,
              targetLetter = target,
              options = options,
              correctOptionId = correctId,
              explanationBn = "হরফ '${target.letter}' এর রূপ: '${if (isMedial) target.medial else target.finalForm}'।",
              explanationEn = "Connected form of '${target.letter}' is '${if (isMedial) target.medial else target.finalForm}'.",
              isTimeAttack = true,
              timeLimitSeconds = 10
            )
          )
        }

        19 -> {
          // Level 19: 4x4 Speed Blitz ⚡ (9s)
          val target = allLetters[(qNum * 9 + 17) % allLetters.size]
          val distractors = allLetters.filter { it.id != target.id }.shuffled().take(7)
          val options = (listOf(target) + distractors).shuffled().mapIndexed { idx, let ->
            GameOption(
              id = "opt_l19_${qNum}_${idx}_${let.id}",
              arabicDisplay = let.letter,
              primaryText = let.nameBn,
              secondaryText = let.phonetic,
              iconEmoji = "⚡"
            )
          }
          val correctId = options.first { it.arabicDisplay == target.letter }.id
          questions.add(
            GameQuestion(
              questionNumber = qNum,
              gameType = GameType.LETTER_LINK,
              levelNumber = levelNumber,
              promptBn = "⚡ ৯ সেকেন্ড স্পিড ব্লিৎজ: হরফ '${target.nameBn} (${target.letter})' দ্রুত শনাক্ত করে লিঙ্ক করুন!",
              promptEn = "⚡ 9s Speed Blitz: Instantly link letter '${target.nameEn} (${target.letter})'!",
              promptAr = "⚡ السرعة الخاطفة (٩ ثوان): اربط الحرف '${target.nameAr}' فوراً!",
              displayArabic = target.letter,
              targetLetter = target,
              options = options,
              correctOptionId = correctId,
              explanationBn = "চমৎকার দ্রুততা! '${target.letter}' হল ${target.nameBn}।",
              explanationEn = "Excellent speed! '${target.letter}' is ${target.nameEn}.",
              isTimeAttack = true,
              timeLimitSeconds = 9
            )
          )
        }

        else -> {
          // Level 20: Grand Letter Link Champion ⚡ (8s Time-Attack)
          val target = allLetters[(qNum * 11 + 19) % allLetters.size]
          val distractors = allLetters.filter { it.id != target.id }.shuffled().take(7)
          val options = (listOf(target) + distractors).shuffled().mapIndexed { idx, let ->
            GameOption(
              id = "opt_l20_${qNum}_${idx}_${let.id}",
              arabicDisplay = let.letter,
              primaryText = "${let.nameBn} (${let.letter})",
              secondaryText = let.phonetic,
              iconEmoji = "👑"
            )
          }
          val correctId = options.first { it.arabicDisplay == target.letter }.id
          questions.add(
            GameQuestion(
              questionNumber = qNum,
              gameType = GameType.LETTER_LINK,
              levelNumber = levelNumber,
              promptBn = "⚡ ৮ সেকেন্ড গ্র্যান্ড ফাইনাল: '${target.nameBn} (${target.letter})' এর চ্যাম্পিয়ন লিঙ্ক সম্পন্ন করুন!",
              promptEn = "⚡ 8s Grand Final: Complete the championship link for '${target.nameEn} (${target.letter})'!",
              promptAr = "⚡ النهائي الأكبر (٨ ثوان): حقق رابط البطولة للحرف '${target.nameAr}'!",
              displayArabic = target.letter,
              targetLetter = target,
              options = options,
              correctOptionId = correctId,
              explanationBn = "আলহামদুলিল্লাহ! হরফ '${target.letter}' (${target.nameBn}) সফলভাবে লিংক করে আপনি গ্র্যান্ড চ্যাম্পিয়ন হলেন।",
              explanationEn = "Alhamdulillah! Letter '${target.letter}' (${target.nameEn}) mastered in Grand Champion speed.",
              isTimeAttack = true,
              timeLimitSeconds = 8
            )
          )
        }
      }
    }

    return questions
  }

  // =========================================================================
  // 3. FORM FUSER (ফর্ম ফিউজার / হরফ জোড়া ও শব্দ গঠন)
  // =========================================================================
  private data class FormFuserWordItem(
    val word: String,
    val letters: List<String>,
    val meaningBn: String,
    val meaningEn: String,
    val phonetic: String
  )

  private val formFuserTwoLetterWords = listOf(
    FormFuserWordItem("مِنْ", listOf("م", "ن"), "হতে / থেকে", "From", "Min"),
    FormFuserWordItem("عَنْ", listOf("ع", "ن"), "সম্পর্কে / হতে", "About / From", "'An"),
    FormFuserWordItem("قُلْ", listOf("ق", "ل"), "বলুন", "Say", "Qul"),
    FormFuserWordItem("هَلْ", listOf("ه", "ل"), "কি?", "Is/Are?", "Hal"),
    FormFuserWordItem("بَلْ", listOf("ب", "ل"), "বরং", "Rather", "Bal"),
    FormFuserWordItem("كُنْ", listOf("ك", "ن"), "হও", "Be", "Kun"),
    FormFuserWordItem("قُمْ", listOf("ق", "م"), "দাঁড়াও", "Stand up", "Qum"),
    FormFuserWordItem("لَمْ", listOf("ل", "م"), "না / করেনি", "Did not", "Lam"),
    FormFuserWordItem("لَنْ", listOf("ل", "ن"), "কখনোই না", "Never", "Lan"),
    FormFuserWordItem("فِي", listOf("ف", "ي"), "মধ্যে / ভিতরে", "In", "Fee"),
    FormFuserWordItem("قَدْ", listOf("ق", "د"), "নিশ্চয়ই / ইতিমধ্যে", "Indeed / Already", "Qad"),
    FormFuserWordItem("لَوْ", listOf("ل", "و"), "যদি", "If", "Law"),
    FormFuserWordItem("خُذْ", listOf("خ", "ذ"), "গ্রহণ করো", "Take", "Khudh"),
    FormFuserWordItem("ذُو", listOf("ذ", "و"), "অধিকারী", "Possessor", "Dhoo"),
    FormFuserWordItem("سِرْ", listOf("س", "ر"), "চলো / ভ্রমণ করো", "Walk / Travel", "Sir"),
    FormFuserWordItem("كَيْ", listOf("ك", "ي"), "যাতে করে", "So that", "Kay"),
    FormFuserWordItem("أَوْ", listOf("ا", "و"), "অথবা", "Or", "Aw"),
    FormFuserWordItem("أَمْ", listOf("ا", "م"), "নাকি / অথবা", "Or", "Am"),
    FormFuserWordItem("إِذْ", listOf("ا", "ذ"), "যখন", "When", "Idh"),
    FormFuserWordItem("إِنْ", listOf("ا", "ن"), "যদি", "If", "In"),
    FormFuserWordItem("بِي", listOf("ب", "ي"), "আমার সাথে", "With me", "Biy"),
    FormFuserWordItem("لِي", listOf("ل", "ي"), "আমার জন্য", "For me", "Liy"),
    FormFuserWordItem("صُمْ", listOf("ص", "م"), "রোজা রাখো", "Fast", "Sum"),
    FormFuserWordItem("عُدْ", listOf("ع", "د"), "ফিরে এসো", "Return", "'Ud"),
    FormFuserWordItem("ذُقْ", listOf("ذ", "ق"), "স্বাদ নাও", "Taste", "Dhuq"),
    FormFuserWordItem("دَعْ", listOf("د", "ع"), "ছেড়ে দাও", "Leave", "Da'"),
    FormFuserWordItem("مَا", listOf("م", "ا"), "যা / কি", "What / Not", "Maa"),
    FormFuserWordItem("لَا", listOf("ل", "ا"), "না", "No", "Laa")
  )

  private val formFuserThreeLetterWords = listOf(
    FormFuserWordItem("كَتَبَ", listOf("ك", "ت", "ب"), "লিখল", "He wrote", "Kataba"),
    FormFuserWordItem("قَرَأَ", listOf("ق", "ر", "أ"), "পড়ল", "He read", "Qara'a"),
    FormFuserWordItem("دَرَسَ", listOf("د", "ر", "س"), "অধ্যয়ন করল", "He studied", "Darasa"),
    FormFuserWordItem("سَمِعَ", listOf("س", "م", "ع"), "শুনল", "He heard", "Sami'a"),
    FormFuserWordItem("خَلَقَ", listOf("خ", "ل", "ق"), "সৃষ্টি করল", "He created", "Khalaqa"),
    FormFuserWordItem("عَلِمَ", listOf("ع", "ل", "م"), "জানল", "He knew", "'Alima"),
    FormFuserWordItem("نَصَرَ", listOf("ن", "ص", "ر"), "সাহায্য করল", "He helped", "Nasara"),
    FormFuserWordItem("صَبَرَ", listOf("ص", "ب", "ر"), "ধৈর্য ধরল", "He was patient", "Sabara"),
    FormFuserWordItem("جَلَسَ", listOf("ج", "ل", "س"), "বসল", "He sat", "Jalasa"),
    FormFuserWordItem("ذَكَرَ", listOf("ذ", "ك", "ر"), "স্মরণ করল", "He remembered", "Dhakara"),
    FormFuserWordItem("شَكَرَ", listOf("ش", "ك", "ر"), "শুকরিয়া জানাল", "He thanked", "Shakara"),
    FormFuserWordItem("غَفَرَ", listOf("غ", "ف", "ر"), "ক্ষমা করল", "He forgave", "Ghafara"),
    FormFuserWordItem("رَحِمَ", listOf("ر", "ح", "م"), "দয়া করল", "He had mercy", "Rahima"),
    FormFuserWordItem("حَمِدَ", listOf("ح", "م", "د"), "প্রশংসা করল", "He praised", "Hamida"),
    FormFuserWordItem("صَدَقَ", listOf("ص", "د", "ق"), "সত্য বলল", "He was truthful", "Sadaqa"),
    FormFuserWordItem("طَلَبَ", listOf("ط", "ل", "ب"), "চাইল", "He sought", "Talaba"),
    FormFuserWordItem("فَتَحَ", listOf("ف", "ت", "ح"), "উন্মুক্ত করল", "He opened", "Fataha"),
    FormFuserWordItem("مَلَكَ", listOf("م", "ل", "ك"), "মালিকানা পেল", "He owned", "Malaka"),
    FormFuserWordItem("نَظَرَ", listOf("ن", "ظ", "ر"), "তাকিয়ে দেখল", "He looked", "Nazara"),
    FormFuserWordItem("بَلَغَ", listOf("ب", "ل", "غ"), "পৌঁছাল", "He reached", "Balagha"),
    FormFuserWordItem("وَهَبَ", listOf("و", "ه", "ب"), "দান করল", "He granted", "Wahaba"),
    FormFuserWordItem("رَسَمَ", listOf("ر", "س", "م"), "আঁকল", "He drew", "Rasama"),
    FormFuserWordItem("نَبَتَ", listOf("ن", "ب", "ت"), "অঙ্কুরিত হল", "It grew", "Nabata"),
    FormFuserWordItem("رَزَقَ", listOf("ر", "ز", "ق"), "রিযিক দিল", "He provided", "Razaqa"),
    FormFuserWordItem("ذَهَبَ", listOf("ذ", "ه", "ب"), "গেল", "He went", "Dhahaba"),
    FormFuserWordItem("سَجَدَ", listOf("س", "ج", "د"), "সিজদা করল", "He prostrated", "Sajada"),
    FormFuserWordItem("رَكَعَ", listOf("ر", "ك", "ع"), "রুকু করল", "He bowed", "Raka'a"),
    FormFuserWordItem("حَفِظَ", listOf("ح", "ف", "ظ"), "সংরক্ষণ করল", "He preserved", "Hafiza"),
    FormFuserWordItem("بَعَثَ", listOf("ب", "ع", "ث"), "প্রেরণ করল", "He sent", "Ba'atha"),
    FormFuserWordItem("جَمَعَ", listOf("ج", "م", "ع"), "একত্র করল", "He gathered", "Jama'a"),
    FormFuserWordItem("رَفَعَ", listOf("ر", "ف", "ع"), "উত্তোলন করল", "He raised", "Rafa'a"),
    FormFuserWordItem("قَلَم", listOf("ق", "ل", "م"), "কলম", "Pen", "Qalam"),
    FormFuserWordItem("بَيْت", listOf("ب", "ي", "ت"), "বাড়ি / ঘর", "House", "Bayt")
  )

  private val formFuserFourLetterWords = listOf(
    FormFuserWordItem("مَسْجِد", listOf("م", "س", "ج", "د"), "মসজিদ", "Mosque", "Masjid"),
    FormFuserWordItem("مَكْتَب", listOf("م", "ك", "ت", "ب"), "অফিস / টেবিল", "Desk / Office", "Maktab"),
    FormFuserWordItem("مُصْحَف", listOf("م", "ص", "ح", "ف"), "কুরআন গ্রন্থ (মুসহাফ)", "Mushaf", "Mushaf"),
    FormFuserWordItem("كِتَاب", listOf("ك", "ت", "ا", "ب"), "বই / কিতাব", "Book", "Kitaab"),
    FormFuserWordItem("دَفْتَر", listOf("د", "ف", "ت", "ر"), "খাতা / নোটবুক", "Notebook", "Daftar"),
    FormFuserWordItem("قُرْآن", listOf("ق", "ر", "ا", "ن"), "পবিত্র কুরআন", "Holy Quran", "Qur'an"),
    FormFuserWordItem("حِسَاب", listOf("ح", "س", "ا", "ب"), "হিসাব", "Account", "Hisaab"),
    FormFuserWordItem("سَلَام", listOf("س", "ل", "ا", "م"), "শান্তি / সালাম", "Peace", "Salaam"),
    FormFuserWordItem("صَلَاة", listOf("ص", "ل", "ا", "ة"), "নামাজ / সালাত", "Prayer", "Salah"),
    FormFuserWordItem("زَكَاة", listOf("ز", "ك", "ا", "ة"), "যাকাত", "Zakah", "Zakah"),
    FormFuserWordItem("مَطْبَخ", listOf("م", "ط", "ب", "خ"), "রান্নাঘর", "Kitchen", "Matbakh"),
    FormFuserWordItem("مَلْعَب", listOf("م", "ل", "ع", "ب"), "খেলার মাঠ", "Playground", "Mal'ab"),
    FormFuserWordItem("مَنْزِل", listOf("م", "ن", "ز", "ل"), "বাসস্থান / গৃহ", "Home", "Manzil"),
    FormFuserWordItem("كَوْكَب", listOf("ك", "و", "ك", "ب"), "গ্রহ / তারা", "Planet / Star", "Kawkab"),
    FormFuserWordItem("مَشْرِق", listOf("م", "ش", "ر", "ق"), "পূর্বদিক", "East", "Mashriq"),
    FormFuserWordItem("مَغْرِب", listOf("م", "غ", "ر", "ب"), "পশ্চিমদিক / মাগরিব", "West", "Maghrib"),
    FormFuserWordItem("رَحْمَن", listOf("ر", "ح", "م", "ن"), "পরম দয়ালু", "Most Merciful", "Rahman"),
    FormFuserWordItem("سُبْحَان", listOf("س", "ب", "ح", "ا", "ن"), "মহাপবিত্র", "Glorified", "Subhan"),
    FormFuserWordItem("إِسْلَام", listOf("ا", "س", "ل", "ا", "م"), "ইসলাম", "Islam", "Islam"),
    FormFuserWordItem("سُلْطَان", listOf("س", "ل", "ط", "ا", "ن"), "কর্তৃত্ব / প্রমাণ", "Authority", "Sultan"),
    FormFuserWordItem("مِيزَان", listOf("م", "ي", "ز", "ا", "ن"), "দাঁড়িপাল্লা", "Scale", "Meezan"),
    FormFuserWordItem("بُرْهَان", listOf("ب", "ر", "ه", "ا", "ن"), "সুস্পষ্ট প্রমাণ", "Clear Proof", "Burhan"),
    FormFuserWordItem("كَوْثَر", listOf("ك", "و", "ث", "ر"), "প্রচুর কল্যাণ / কাউসার", "Abundance", "Kawthar"),
    FormFuserWordItem("مَرْكَز", listOf("م", "ر", "ك", "ز"), "কেন্দ্র", "Center", "Markaz"),
    FormFuserWordItem("مَجْلِس", listOf("م", "ج", "ل", "س"), "বৈঠক / সভা", "Council", "Majlis"),
    FormFuserWordItem("مَدْخَل", listOf("م", "د", "خ", "ل"), "প্রবেশদ্বার", "Entrance", "Madkhal"),
    FormFuserWordItem("مَخْرَج", listOf("م", "خ", "ر", "ج"), "মাখরাজ / নির্গমন স্থান", "Makhraj", "Makhraj")
  )

  private val formFuserComplexWords = listOf(
    FormFuserWordItem("زَهْرَة", listOf("ز", "ه", "ر", "ة"), "ফুল", "Flower", "Zahrah"),
    FormFuserWordItem("وَرْدَة", listOf("و", "ر", "د", "ة"), "গোলাপ", "Rose", "Wardah"),
    FormFuserWordItem("دَرْس", listOf("د", "ر", "س"), "পাঠ / শিক্ষা", "Lesson", "Dars"),
    FormFuserWordItem("رِزْق", listOf("ر", "ز", "ق"), "জীবিকা / রিযিক", "Provision", "Rizq"),
    FormFuserWordItem("ذِكْر", listOf("ذ", "ك", "ر"), "স্মরণ / জিকির", "Remembrance", "Dhikr"),
    FormFuserWordItem("وَرَق", listOf("و", "ر", "ق"), "কাগজ / পাতা", "Paper / Leaves", "Waraq"),
    FormFuserWordItem("بَاب", listOf("ب", "ا", "ب"), "দরজা", "Door", "Baab"),
    FormFuserWordItem("نُور", listOf("ن", "و", "ر"), "আলো", "Light", "Noor"),
    FormFuserWordItem("زَيْت", listOf("ز", "ي", "ت"), "তেল / জয়তুন", "Oil", "Zayt"),
    FormFuserWordItem("دَار", listOf("د", "ا", "ر"), "গৃহ / নিবাস", "Abode", "Daar"),
    FormFuserWordItem("رَأْس", listOf("ر", "ا", "س"), "মাথা", "Head", "Ra's"),
    FormFuserWordItem("وَجْه", listOf("و", "ج", "ه"), "চেহারা", "Face", "Wajh"),
    FormFuserWordItem("زَمَان", listOf("ز", "م", "ا", "ن"), "কাল / যুগ", "Time / Era", "Zaman"),
    FormFuserWordItem("رَسُول", listOf("ر", "س", "و", "ل"), "রাসূল / বার্তাবাহক", "Messenger", "Rasool"),
    FormFuserWordItem("دُعَاء", listOf("د", "ع", "ا", "ء"), "দোয়া / প্রার্থনা", "Supplication", "Du'aa"),
    FormFuserWordItem("وَلَد", listOf("و", "ل", "د"), "সন্তান / ছেলে", "Child / Boy", "Walad"),
    FormFuserWordItem("إِذْن", listOf("ا", "ذ", "ن"), "অনুমতি", "Permission", "Idhn"),
    FormFuserWordItem("ذَهَب", listOf("ذ", "ه", "ب"), "স্বর্ণ / সোনা", "Gold", "Dhahab"),
    FormFuserWordItem("وَاحِد", listOf("و", "ا", "ح", "د"), "এক / একক", "One", "Waahid"),
    FormFuserWordItem("وَعْد", listOf("و", "ع", "د"), "প্রতিশ্রুতি", "Promise", "Wa'd"),
    FormFuserWordItem("أَرْض", listOf("ا", "ر", "ض"), "পৃথিবী / জমিন", "Earth", "Ard"),
    FormFuserWordItem("رُوح", listOf("ر", "و", "ح"), "আত্মা / রুহ", "Soul", "Rooh"),
    FormFuserWordItem("نَار", listOf("ن", "ا", "ر"), "আগুন", "Fire", "Naar"),
    FormFuserWordItem("فِرْدَوْس", listOf("ف", "ر", "د", "و", "س"), "জান্নাতুল ফিরদাউস", "Paradise", "Firdaws"),
    FormFuserWordItem("رَحْمَة", listOf("ر", "ح", "م", "ة"), "রহমত / করুণা", "Mercy", "Rahmah")
  )

  private fun generateFormFuserQuestions(
    levelNumber: Int,
    tier: GameDifficultyTier,
    isTimeAttack: Boolean
  ): List<GameQuestion> {
    val allLetters = ArabicAlphabetRepository.letters
    val questions = mutableListOf<GameQuestion>()

    val isLevelTimeAttack = when (levelNumber) {
      15 -> true
      18 -> true
      19 -> true
      20 -> true
      else -> false
    }

    val timeLimit = when (levelNumber) {
      15 -> 12
      18 -> 11
      19 -> 10
      20 -> 8
      else -> 12
    }

    for (qNum in 1..20) {
      val target: FormFuserWordItem = when (levelNumber) {
        // TIER 1: Levels 1-5 (2-Letter Simple Words / ২-বর্ণের সহজ শব্দ)
        1 -> {
          // Level 1: Fundamental particles (مِنْ, عَنْ, فِي, إِذْ, إِنْ, بِي, لِي)
          val pool = formFuserTwoLetterWords.filter { it.word in listOf("مِنْ", "عَنْ", "فِي", "إِذْ", "إِنْ", "بِي", "لِي") }
          pool[(qNum - 1) % pool.size]
        }
        2 -> {
          // Level 2: Question & particle words (قُلْ, هَلْ, بَلْ, مَا, لَا)
          val pool = formFuserTwoLetterWords.filter { it.word in listOf("قُلْ", "هَلْ", "بَلْ", "مَا", "لَا", "أَوْ", "أَمْ") }
          pool[(qNum - 1) % pool.size]
        }
        3 -> {
          // Level 3: Short imperative verbs (كُنْ, قُمْ, صُمْ, خُذْ, سِرْ, عُدْ)
          val pool = formFuserTwoLetterWords.filter { it.word in listOf("كُنْ", "قُمْ", "صُمْ", "خُذْ", "سِرْ", "عُدْ", "ذُقْ", "دَعْ") }
          pool[(qNum - 1) % pool.size]
        }
        4 -> {
          // Level 4: Negation & certainty (لَمْ, لَنْ, قَدْ, كَيْ, لَوْ, ذُو)
          val pool = formFuserTwoLetterWords.filter { it.word in listOf("لَمْ", "لَنْ", "قَدْ", "كَيْ", "لَوْ", "ذُو") }
          pool[(qNum - 1) % pool.size]
        }
        5 -> {
          // Level 5: 2-Letter Master Fusion (All 2-Letter Words Shuffled)
          formFuserTwoLetterWords[(qNum * 3 + levelNumber) % formFuserTwoLetterWords.size]
        }

        // TIER 2: Levels 6-10 (3-Letter Basic Words / ৩-বর্ণের মৌলিক শব্দ)
        6 -> {
          // Level 6: 3-Letter Verbs I (كَتَبَ, قَرَأَ, دَرَسَ, جَلَسَ)
          val pool = formFuserThreeLetterWords.filter { it.word in listOf("كَتَبَ", "قَرَأَ", "دَرَسَ", "جَلَسَ", "رَسَمَ", "نَبَتَ") }
          pool[(qNum - 1) % pool.size]
        }
        7 -> {
          // Level 7: 3-Letter Verbs II (سَمِعَ, عَلِمَ, حَمِدَ, رَحِمَ, شَكَرَ)
          val pool = formFuserThreeLetterWords.filter { it.word in listOf("سَمِعَ", "عَلِمَ", "حَمِدَ", "رَحِمَ", "شَكَرَ", "حَفِظَ") }
          pool[(qNum - 1) % pool.size]
        }
        8 -> {
          // Level 8: Quranic Verbs III (خَلَقَ, نَصَرَ, عَبَدَ, ذَكَرَ, غَفَرَ)
          val pool = formFuserThreeLetterWords.filter { it.word in listOf("خَلَقَ", "نَصَرَ", "ذَكَرَ", "غَفَرَ", "رَزَقَ", "بَعَثَ") }
          pool[(qNum - 1) % pool.size]
        }
        9 -> {
          // Level 9: Praise, Worship & Action (صَبَرَ, صَدَقَ, طَلَبَ, فَتَحَ, مَلَكَ, سَجَدَ)
          val pool = formFuserThreeLetterWords.filter { it.word in listOf("صَبَرَ", "صَدَقَ", "طَلَبَ", "فَتَحَ", "مَلَكَ", "سَجَدَ", "رَكَعَ", "رَفَعَ") }
          pool[(qNum - 1) % pool.size]
        }
        10 -> {
          // Level 10: 3-Letter Master Word Builder (All 3-Letter Words)
          formFuserThreeLetterWords[(qNum * 5 + levelNumber) % formFuserThreeLetterWords.size]
        }

        // TIER 3: Levels 11-15 (4-Letter Compound Words / ৪-বর্ণের যুক্তশব্দ)
        11 -> {
          // Level 11: 4-Letter Places & Objects (مَسْجِد, مَكْتَب, مَطْبَخ, مَلْعَب)
          val pool = formFuserFourLetterWords.filter { it.word in listOf("مَسْجِد", "مَكْتَب", "مَطْبَخ", "مَلْعَب", "مَدْخَل", "مَخْرَج") }
          pool[(qNum - 1) % pool.size]
        }
        12 -> {
          // Level 12: 4-Letter Learning & Household (مُصْحَف, دَفْتَر, مَنْزِل, كَوْكَب, مَرْكَز)
          val pool = formFuserFourLetterWords.filter { it.word in listOf("مُصْحَف", "دَفْتَر", "مَنْزِل", "كَوْكَب", "مَرْكَز", "مَجْلِس") }
          pool[(qNum - 1) % pool.size]
        }
        13 -> {
          // Level 13: 4-Letter Quranic Nouns (كِتَاب, قُرْآن, حِسَاب, سَلَام, صَلَاة, زَكَاة)
          val pool = formFuserFourLetterWords.filter { it.word in listOf("كِتَاب", "قُرْآن", "حِسَاب", "سَلَام", "صَلَاة", "زَكَاة") }
          pool[(qNum - 1) % pool.size]
        }
        14 -> {
          // Level 14: 4-Letter Sacred Words (رَحْمَن, سُبْحَان, إِسْلَام, سُلْطَان, مِيزَان, بُرْهَان, كَوْثَر)
          val pool = formFuserFourLetterWords.filter { it.word in listOf("رَحْمَن", "سُبْحَان", "إِسْلَام", "سُلْطَان", "مِيزَان", "بُرْهَان", "كَوْثَر") }
          pool[(qNum - 1) % pool.size]
        }
        15 -> {
          // Level 15: 4-Letter Speed Fusion ⚡ (12s Time Attack)
          formFuserFourLetterWords[(qNum * 7 + levelNumber) % formFuserFourLetterWords.size]
        }

        // TIER 4: Levels 16-20 (Complex Word Assembly / জটিল শব্দ গঠন)
        16 -> {
          // Level 16: Non-connecting letters (د, ذ, ر, ز) (دَرْس, ذِكْر, رِزْق, دَار, زَيْت, ذَهَب)
          val pool = formFuserComplexWords.filter { it.word in listOf("دَرْس", "ذِكْر", "رِزْق", "دَار", "زَيْت", "ذَهَب", "رَأْس") }
          pool[(qNum - 1) % pool.size]
        }
        17 -> {
          // Level 17: Non-connecting letters (ا, و) (نُور, بَاب, وَرَق, وَجْه, وَلَد, وَاحِد, وَعْد, رُوح, نَار)
          val pool = formFuserComplexWords.filter { it.word in listOf("نُور", "بَاب", "وَرَق", "وَجْه", "وَلَد", "وَاحِد", "وَعْد", "رُوح", "نَار") }
          pool[(qNum - 1) % pool.size]
        }
        18 -> {
          // Level 18: Mixed Connectivity Challenge ⚡ (11s Time Attack)
          val pool = formFuserComplexWords.filter { it.word in listOf("زَهْرَة", "وَرْدَة", "زَمَان", "رَسُول", "دُعَاء", "أَرْض", "إِذْن", "رَحْمَة") }
          pool[(qNum - 1) % pool.size]
        }
        19 -> {
          // Level 19: Advanced Multi-Letter Speed Fusion ⚡ (10s Time Attack)
          val pool = (formFuserFourLetterWords + formFuserComplexWords).filter { it.letters.size >= 4 }
          pool[(qNum * 3 + levelNumber) % pool.size]
        }
        else -> {
          // Level 20: Grand Form Fuser Champion ⚡ (8s Time Attack)
          val combinedMaster = formFuserTwoLetterWords + formFuserThreeLetterWords + formFuserFourLetterWords + formFuserComplexWords
          combinedMaster[(qNum * 13 + levelNumber) % combinedMaster.size]
        }
      }

      val targetLetters = target.letters
      val targetLetter = allLetters.firstOrNull { it.letter == targetLetters.first() } ?: allLetters.first()

      // Select 1 to 2 distractor letters from alphabet that do NOT belong to this word
      val distractorCount = if (targetLetters.size >= 4) 1 else 2
      val distractorLetters = allLetters
        .filter { it.letter !in targetLetters }
        .shuffled()
        .take(distractorCount)

      // Create shuffled option tiles
      val options = (targetLetters.mapIndexed { idx, letStr ->
        val letObj = allLetters.firstOrNull { it.letter == letStr }
        GameOption(
          id = "opt_ff_${levelNumber}_${qNum}_t_${idx}_$letStr",
          arabicDisplay = letStr,
          primaryText = letObj?.nameBn ?: letStr,
          secondaryText = letObj?.phonetic ?: ""
        )
      } + distractorLetters.mapIndexed { idx, letObj ->
        GameOption(
          id = "opt_ff_${levelNumber}_${qNum}_d_${idx}_${letObj.letter}",
          arabicDisplay = letObj.letter,
          primaryText = letObj.nameBn,
          secondaryText = letObj.phonetic
        )
      }).shuffled()

      val promptBn = when (levelNumber) {
        in 1..5 -> "নিচের হরফগুলো ডান থেকে বামে সাজিয়ে '${target.meaningBn}' (${target.phonetic}) শব্দটি গঠন করুন:"
        in 6..10 -> "৩টি হরফ ক্রমানুসারে জোড়া দিয়ে '${target.meaningBn}' (${target.phonetic}) ক্রিয়াপদটি গঠন করুন:"
        in 11..15 -> if (isLevelTimeAttack) "⚡ ১২ সেকেন্ড স্পিড ফিউশন: '${target.meaningBn}' (${target.phonetic}) শব্দটি দ্রুত গঠন করুন!" else "৪টি হরফের সঠিক রূপান্তর মিলিয়ে '${target.meaningBn}' (${target.phonetic}) শব্দটি ফিউজ করুন:"
        else -> "⚡ $timeLimit সেকেন্ড স্পিড পাজল: হরফ সাজিয়ে '${target.meaningBn}' (${target.phonetic}) শব্দটি তৈরি করুন!"
      }

      val promptEn = if (isLevelTimeAttack) {
        "⚡ ${timeLimit}s Speed Puzzle: Assemble '${target.meaningEn}' (${target.phonetic})!"
      } else {
        "Assemble the letters right-to-left to form '${target.meaningEn}' (${target.phonetic}):"
      }

      val promptAr = "رتب الحروف من اليمين إلى اليسار لتكوين الكلمة '${target.word}':"

      questions.add(
        GameQuestion(
          questionNumber = qNum,
          gameType = GameType.FORM_FUSER,
          levelNumber = levelNumber,
          promptBn = promptBn,
          promptEn = promptEn,
          promptAr = promptAr,
          displayArabic = target.word,
          targetLetter = targetLetter,
          options = options,
          correctOptionId = targetLetters.joinToString(","),
          explanationBn = "${targetLetters.joinToString(" + ")} ক্রমানুসারে জোড়া লেগে গঠিত হয় '${target.word}'। অর্থ: ${target.meaningBn}।",
          explanationEn = "Connecting ${targetLetters.joinToString(" + ")} forms '${target.word}' (${target.meaningEn}).",
          isTimeAttack = isLevelTimeAttack,
          timeLimitSeconds = timeLimit,
          puzzleLetters = targetLetters,
          targetWordMeaningBn = target.meaningBn,
          targetWordMeaningEn = target.meaningEn,
          targetWordPhonetic = target.phonetic
        )
      )
    }

    return questions
  }

  // =========================================================================
  // 4. MIX & MATCH (মিক্স অ্যান্ড ম্যাচ - মেমোরি কার্ড ফ্লিপ)
  // 20-Level Progressive Memory Curriculum Engine:
  // - Levels 1-5: Basic 4-Card Memory Layout / 2 Pairs (একই হরফ মেলান)
  // - Levels 6-10: Medium 6-Card Layout / 3 Pairs (হরফ ও আরবি নাম মেলান)
  // - Levels 11-15: Advanced 8-Card Layout / 4 Pairs (বিচ্ছিন্ন ও সংযুক্ত রূপ মেলান)
  // - Levels 16-20: Speed Flip Challenge 10-12 Cards / Multi Pairs ⚡ (টাইম-অ্যাটাক ও মাল্টি-পেয়ার্স)
  // =========================================================================
  private fun generateMixMatchQuestions(
    levelNumber: Int,
    tier: GameDifficultyTier,
    isTimeAttack: Boolean
  ): List<GameQuestion> {
    val allLetters = ArabicAlphabetRepository.letters
    val questions = mutableListOf<GameQuestion>()

    val isLevelTimeAttack = when (levelNumber) {
      15 -> true
      16 -> true
      18 -> true
      19 -> true
      20 -> true
      else -> false
    }

    val timeLimit = when (levelNumber) {
      15 -> 15
      16 -> 12
      18 -> 14
      19 -> 12
      20 -> 10
      else -> 20
    }

    for (qNum in 1..20) {
      val memoryCards = mutableListOf<MemoryFlipCard>()
      val requiredPairs: Int
      val promptBn: String
      val promptEn: String
      val promptAr: String
      val explanationBn: String
      val explanationEn: String

      when (levelNumber) {
        // ---------------------------------------------------------------------
        // STAGE 1: LEVELS 1-5 (INTRO 6-CARD GRID / 3 PAIRS / 2x3 LAYOUT)
        // ---------------------------------------------------------------------
        in 1..5 -> {
          requiredPairs = 3
          val pool = when (levelNumber) {
            1 -> allLetters.filter { it.letter in listOf("ا", "ب", "ت", "ث", "ج") }
            2 -> allLetters.filter { it.letter in listOf("ح", "خ", "د", "ذ", "ر", "ز") }
            3 -> allLetters.filter { it.letter in listOf("س", "ش", "ص", "ض", "ط", "ظ") }
            4 -> allLetters.filter { it.letter in listOf("ع", "غ", "ف", "ق", "ك", "ل") }
            else -> allLetters
          }
          val shuffledPool = pool.shuffled(Random(levelNumber * 100 + qNum * 13))
          val selectedThree = shuffledPool.take(3).let {
            if (it.size < 3) allLetters.shuffled().take(3) else it
          }

          selectedThree.forEachIndexed { idx, let ->
            val pId = "p_l${levelNumber}_q${qNum}_${let.id}"
            memoryCards.add(
              MemoryFlipCard(
                id = "c_${pId}_1",
                pairId = pId,
                displayArabic = let.letter,
                subtitle = "${let.nameBn} (${let.nameEn})",
                audioLetter = let.letter,
                cardTypeLabel = "হরফ"
              )
            )
            memoryCards.add(
              MemoryFlipCard(
                id = "c_${pId}_2",
                pairId = pId,
                displayArabic = let.letter,
                subtitle = "${let.nameBn} (${let.phonetic})",
                audioLetter = let.letter,
                cardTypeLabel = "হরফ"
              )
            )
          }

          val pairNames = selectedThree.joinToString(" • ") { it.nameBn }
          promptBn = "৬-কার্ড গ্রিড: '${pairNames}' হরফগুলোর ৩টি জোড়া মিলিয়ে নিন:"
          promptEn = "Flip 6 cards in a 2x3 grid to match 3 pairs for $pairNames:"
          promptAr = "اقلب ٦ بطاقات لمطابقة ٣ أزواج من الحروف المتطابقة:"
          explanationBn = "সফলভাবে ৩ জোড়া (${pairNames}) হরফের মিল উদ্ধার করেছেন।"
          explanationEn = "Successfully matched 3 pairs for $pairNames."
        }

        // ---------------------------------------------------------------------
        // STAGE 2: LEVELS 6-12 (INTERMEDIATE 8-CARD GRID / 4 PAIRS / 2x4 LAYOUT)
        // ---------------------------------------------------------------------
        in 6..12 -> {
          requiredPairs = 4
          when (levelNumber) {
            in 6..9 -> {
              val pool = when (levelNumber) {
                6 -> allLetters.filter { it.letter in listOf("ا", "ب", "ت", "ث", "ج") }
                7 -> allLetters.filter { it.letter in listOf("ح", "خ", "د", "ذ", "ر", "ز") }
                8 -> allLetters.filter { it.letter in listOf("س", "ش", "ص", "ض", "ط", "ظ") }
                else -> allLetters.filter { it.letter in listOf("ع", "غ", "ف", "ق", "ك", "ل", "م", "ن", "هـ", "و", "ي") }
              }
              val shuffledPool = pool.shuffled(Random(levelNumber * 100 + qNum * 17))
              val selectedFour = shuffledPool.take(4).let {
                if (it.size < 4) allLetters.shuffled().take(4) else it
              }

              selectedFour.forEachIndexed { idx, let ->
                val pId = "p_l${levelNumber}_q${qNum}_${let.id}"
                // Card A: Single Letter
                memoryCards.add(
                  MemoryFlipCard(
                    id = "c_${pId}_let",
                    pairId = pId,
                    displayArabic = let.letter,
                    subtitle = "হরফ (Letter)",
                    audioLetter = let.letter,
                    cardTypeLabel = "হরফ"
                  )
                )
                // Card B: Full Arabic Name
                memoryCards.add(
                  MemoryFlipCard(
                    id = "c_${pId}_name",
                    pairId = pId,
                    displayArabic = let.nameAr,
                    subtitle = "${let.nameBn} (${let.nameEn})",
                    audioLetter = let.letter,
                    cardTypeLabel = "নাম"
                  )
                )
              }

              promptBn = "৮-কার্ড গ্রিড: ৪টি হরফ ও তাদের পূর্ণ আরবি নামের জোড়া মেলান:"
              promptEn = "Flip 8 cards (2x4) to match 4 pairs of letters and Arabic names:"
              promptAr = "طابق ٤ أزواج بين الحرف واسمه العربي في شبكة ٨ بطاقات:"
              explanationBn = "৮-কার্ড গ্রিডে ৪ জোড়া হরফ ও আরবি নামের মিল সফল হয়েছে।"
              explanationEn = "Successfully matched 4 pairs of letters and names in 8-card grid."
            }
            10 -> {
              // 4 Pairs Initial Connected Forms
              val pool = allLetters.filter { it.initial.isNotEmpty() && it.initial != it.isolated }
              val selectedFour = pool.shuffled(Random(levelNumber * 100 + qNum * 19)).take(4).let {
                if (it.size < 4) allLetters.take(4) else it
              }
              selectedFour.forEachIndexed { idx, let ->
                val pId = "p_l${levelNumber}_q${qNum}_init_$idx"
                memoryCards.add(
                  MemoryFlipCard(
                    id = "c_${pId}_iso",
                    pairId = pId,
                    displayArabic = let.letter,
                    subtitle = "${let.nameBn} (বিচ্ছিন্ন)",
                    audioLetter = let.letter,
                    cardTypeLabel = "বিচ্ছিন্ন"
                  )
                )
                memoryCards.add(
                  MemoryFlipCard(
                    id = "c_${pId}_conn",
                    pairId = pId,
                    displayArabic = let.initial,
                    subtitle = "${let.nameBn} (প্রারম্ভিক রূপ)",
                    audioLetter = let.letter,
                    cardTypeLabel = "প্রারম্ভিক"
                  )
                )
              }
              promptBn = "৮-কার্ড গ্রিড: ৪টি বিচ্ছিন্ন হরফের সাথে তাদের প্রারম্ভিক রূপের মিল করুন:"
              promptEn = "Match 4 pairs of isolated letters with their initial forms:"
              promptAr = "طابق ٤ أزواج من الحروف المفردة بأشكالها الأولية:"
              explanationBn = "৪ জোড়া বিচ্ছিন্ন ও প্রারম্ভিক রূপ সফলভাবে মেলানো হয়েছে।"
              explanationEn = "4 pairs of isolated and initial forms matched."
            }
            11 -> {
              // 4 Pairs Medial Connected Forms
              val pool = allLetters.filter { it.medial.isNotEmpty() && it.medial != it.isolated }
              val selectedFour = pool.shuffled(Random(levelNumber * 100 + qNum * 21)).take(4).let {
                if (it.size < 4) allLetters.take(4) else it
              }
              selectedFour.forEachIndexed { idx, let ->
                val pId = "p_l${levelNumber}_q${qNum}_med_$idx"
                memoryCards.add(
                  MemoryFlipCard(
                    id = "c_${pId}_iso",
                    pairId = pId,
                    displayArabic = let.letter,
                    subtitle = "${let.nameBn} (বিচ্ছিন্ন)",
                    audioLetter = let.letter,
                    cardTypeLabel = "বিচ্ছিন্ন"
                  )
                )
                memoryCards.add(
                  MemoryFlipCard(
                    id = "c_${pId}_conn",
                    pairId = pId,
                    displayArabic = let.medial,
                    subtitle = "${let.nameBn} (মধ্যবর্তী রূপ)",
                    audioLetter = let.letter,
                    cardTypeLabel = "মধ্যবর্তী"
                  )
                )
              }
              promptBn = "৮-কার্ড গ্রিড: ৪টি বিচ্ছিন্ন হরফের সাথে তাদের মধ্যবর্তী রূপের মিল করুন:"
              promptEn = "Match 4 pairs of isolated letters with their medial forms:"
              promptAr = "طابق ٤ أزواج من الحروف بأشكالها المتوسطة:"
              explanationBn = "৪ জোড়া মধ্যবর্তী রূপভেদ সঠিকভাবে মেলানো হয়েছে।"
              explanationEn = "4 pairs of medial forms matched."
            }
            else -> {
              // Level 12: 4 Pairs Special Forms & Sounds
              val pool = allLetters.filter { it.letter in listOf("ع", "غ", "ف", "ق", "ك", "ل", "م", "هـ") }
              val selectedFour = pool.shuffled(Random(levelNumber * 100 + qNum * 23)).take(4).let {
                if (it.size < 4) allLetters.take(4) else it
              }
              selectedFour.forEachIndexed { idx, let ->
                val pId = "p_l${levelNumber}_q${qNum}_spec_$idx"
                memoryCards.add(
                  MemoryFlipCard(
                    id = "c_${pId}_A",
                    pairId = pId,
                    displayArabic = let.letter,
                    subtitle = "${let.nameBn} (${let.phonetic})",
                    audioLetter = let.letter,
                    cardTypeLabel = "হরফ"
                  )
                )
                memoryCards.add(
                  MemoryFlipCard(
                    id = "c_${pId}_B",
                    pairId = pId,
                    displayArabic = if (let.finalForm.isNotEmpty()) let.finalForm else let.medial,
                    subtitle = "${let.nameAr} (যুক্তরূপ)",
                    audioLetter = let.letter,
                    cardTypeLabel = "যুক্তরূপ"
                  )
                )
              }
              promptBn = "৮-কার্ড গ্রিড: ৪ জোড়া বিশিষ্ট রূপভেদ ও ধ্বনি মিল করুন:"
              promptEn = "Match 4 pairs of distinctive forms and phonetic values in 8-card grid:"
              promptAr = "طابق ٤ أزواج من الأشكال والأصوات الخاصة:"
              explanationBn = "বিশিষ্ট রূপভেদের ৪টি জোড়াই সফলভাবে উদ্ধার করেছেন।"
              explanationEn = "Successfully matched all 4 pairs."
            }
          }
        }

        // ---------------------------------------------------------------------
        // STAGE 3: LEVELS 13-20 (MASTER LEVELS / 10-12 CARD GRID / 5-6 PAIRS)
        // ---------------------------------------------------------------------
        else -> {
          val pairCount = if (levelNumber >= 17) 6 else 5
          requiredPairs = pairCount

          when (levelNumber) {
            13 -> {
              // 10 Cards (5 Pairs): Phonetically similar / distinct articulation pairs
              val phonetics = listOf(
                Pair("ت", "ত়া (নরম ধ্বনি)"),
                Pair("ط", "ত্বা (ভারী ধ্বনি)"),
                Pair("ث", "ছা (নরম জিহ্বাগ্র)"),
                Pair("س", "সিন (শিস ধ্বনি)"),
                Pair("ص", "সদ (ভারী শিস ধ্বনি)"),
                Pair("ذ", "যাল (নরম য-ধ্বনি)"),
                Pair("ز", "যা (তীক্ষ্ণ ঝিঁঝিঁ ধ্বনি)"),
                Pair("ظ", "জোয়া (ভারী নরম ধ্বনি)"),
                Pair("ح", "হা (মধ্য কণ্ঠনালী)"),
                Pair("هـ", "হা (নিম্ন কণ্ঠনালী)")
              ).shuffled(Random(levelNumber * 100 + qNum * 23)).take(5)

              phonetics.forEachIndexed { idx, pair ->
                val pId = "p_l${levelNumber}_q${qNum}_ph_$idx"
                val letterObj = allLetters.find { it.letter == pair.first } ?: allLetters.first()
                memoryCards.add(
                  MemoryFlipCard(
                    id = "c_${pId}_let",
                    pairId = pId,
                    displayArabic = pair.first,
                    subtitle = "${letterObj.nameBn} (হরফ)",
                    audioLetter = pair.first,
                    cardTypeLabel = "হরফ"
                  )
                )
                memoryCards.add(
                  MemoryFlipCard(
                    id = "c_${pId}_desc",
                    pairId = pId,
                    displayArabic = pair.first,
                    subtitle = pair.second,
                    audioLetter = pair.first,
                    cardTypeLabel = "মাখরাজ"
                  )
                )
              }
              promptBn = "১০-কার্ড গ্রিড: ধ্বনি ও মাখরাজ বৈশিষ্ট্যের ৫ জোড়া হরফ মেমোরি ফ্লিপ করে মেলান:"
              promptEn = "Flip 10 cards (3x4 grid) to match 5 pairs of phonetic articulation traits:"
              promptAr = "طابق ٥ أزواج من الحروف حسب مخارجها الصوتية في شبكة ١٠ بطاقات:"
              explanationBn = "মাখরাজ ও ধ্বনি বৈশিষ্ট্যের ৫টি জোড়াই নির্ভুলভাবে মিলিয়েছেন।"
              explanationEn = "All 5 phonetic pairs matched accurately."
            }
            14 -> {
              // 10 Cards (5 Pairs): Harakat Sound Pairs (Fatha, Kasra, Damma)
              val harakatBaseLetters = allLetters.shuffled(Random(levelNumber * 100 + qNum * 29)).take(5)
              val harakatTypes = listOf(
                Triple("َ", "যবর (Fatha)", "a"),
                Triple("ِ", "যের (Kasra)", "i"),
                Triple("ُ", "পেশ (Damma)", "u")
              )

              harakatBaseLetters.forEachIndexed { idx, let ->
                val pId = "p_l${levelNumber}_q${qNum}_hk_$idx"
                val hk = harakatTypes[idx % harakatTypes.size]
                val arabicWithHarakat = "${let.letter}${hk.first}"
                val soundLabel = "${let.phonetic}${hk.third}"

                memoryCards.add(
                  MemoryFlipCard(
                    id = "c_${pId}_ar",
                    pairId = pId,
                    displayArabic = arabicWithHarakat,
                    subtitle = "${let.nameBn} ${hk.second}",
                    audioLetter = let.letter,
                    cardTypeLabel = "হরকত"
                  )
                )
                memoryCards.add(
                  MemoryFlipCard(
                    id = "c_${pId}_sound",
                    pairId = pId,
                    displayArabic = arabicWithHarakat,
                    subtitle = "ধ্বনি: [$soundLabel]",
                    audioLetter = let.letter,
                    cardTypeLabel = "উচ্চারণ"
                  )
                )
              }
              promptBn = "১০-কার্ড গ্রিড: হরকতযুক্ত হরফ ও ধ্বনি উচ্চারণের ৫ জোড়া মেমোরি কার্ড মেলান:"
              promptEn = "Flip 10 cards to match 5 pairs of short vowels (Harakat) and sound values:"
              promptAr = "طابق ٥ أزواج من الحروف بالحركات القصيرة مع أصواتها:"
              explanationBn = "হরকতসহ সংক্ষিপ্ত স্বরধ্বনির ৫ জোড়া মেমোরি ফ্লিপ সফল হয়েছে।"
              explanationEn = "5 Harakat sound pairs successfully matched."
            }
            15 -> {
              // 10 Cards (5 Pairs): Mixed Letters & Forms Speed Flip ⚡ (15s)
              val selectedFive = allLetters.shuffled(Random(levelNumber * 100 + qNum * 31)).take(5)
              selectedFive.forEachIndexed { idx, let ->
                val pId = "p_l${levelNumber}_q${qNum}_sp5_$idx"
                memoryCards.add(
                  MemoryFlipCard(
                    id = "c_${pId}_A",
                    pairId = pId,
                    displayArabic = let.letter,
                    subtitle = let.nameBn,
                    audioLetter = let.letter,
                    cardTypeLabel = "বিচ্ছিন্ন"
                  )
                )
                memoryCards.add(
                  MemoryFlipCard(
                    id = "c_${pId}_B",
                    pairId = pId,
                    displayArabic = if (let.initial.isNotEmpty()) let.initial else let.letter,
                    subtitle = "${let.nameBn} (সংযুক্ত)",
                    audioLetter = let.letter,
                    cardTypeLabel = "সংযুক্ত"
                  )
                )
              }
              promptBn = "⚡ স্পিড ফ্লিপ ১০-কার্ড: ১৫ সেকেন্ডে ৫ জোড়া মিশ্র হরফ ও যুক্তরূপ মেলান!"
              promptEn = "⚡ Speed Flip 10-Card: Quickly match 5 pairs of mixed Arabic letters in 15s!"
              promptAr = "⚡ سباق السرعة: طابق ٥ أزواج من الحروف والأشكال في ١٥ ثانية!"
              explanationBn = "চমৎকার দ্রুততায় ১০টি কার্ডের ৫ জোড়াই সফলভাবে উদ্ধার করেছেন।"
              explanationEn = "Rapidly matched 5 pairs under 15s timer."
            }
            16 -> {
              // 10 Cards (5 Pairs): Rapid Time Attack ⚡ (12s)
              val selectedFive = allLetters.shuffled(Random(levelNumber * 100 + qNum * 33)).take(5)
              selectedFive.forEachIndexed { idx, let ->
                val pId = "p_l${levelNumber}_q${qNum}_ta5_$idx"
                memoryCards.add(
                  MemoryFlipCard(
                    id = "c_${pId}_A",
                    pairId = pId,
                    displayArabic = let.letter,
                    subtitle = let.nameBn,
                    audioLetter = let.letter,
                    cardTypeLabel = "হরফ"
                  )
                )
                memoryCards.add(
                  MemoryFlipCard(
                    id = "c_${pId}_B",
                    pairId = pId,
                    displayArabic = let.nameAr,
                    subtitle = let.phonetic,
                    audioLetter = let.letter,
                    cardTypeLabel = "নাম"
                  )
                )
              }
              promptBn = "⚡ টাইম-অ্যাটাক ১০-কার্ড: ১২ সেকেন্ডে ৫ জোড়া হরফ ও নামের মিল করুন!"
              promptEn = "⚡ Time-Attack 10-Card: Match 5 pairs of letters & names within 12 seconds!"
              promptAr = "⚡ هجوم الوقت: طابق ٥ أزواج من الحروف والأسماء في ١٢ ثانية!"
              explanationBn = "১২ সেকেন্ডের মধ্যে ৫ জোড়াই নির্ভুলভাবে ফ্লিপ করে মিলিয়েছেন।"
              explanationEn = "5 pairs matched in 12 seconds."
            }
            17 -> {
              // 12 Cards (6 Pairs): Full Alphabet Memory Recall
              val selectedSix = allLetters.shuffled(Random(levelNumber * 100 + qNum * 35)).take(6)
              selectedSix.forEachIndexed { idx, let ->
                val pId = "p_l${levelNumber}_q${qNum}_rec6_$idx"
                memoryCards.add(
                  MemoryFlipCard(
                    id = "c_${pId}_A",
                    pairId = pId,
                    displayArabic = let.letter,
                    subtitle = let.nameBn,
                    audioLetter = let.letter,
                    cardTypeLabel = "হরফ"
                  )
                )
                memoryCards.add(
                  MemoryFlipCard(
                    id = "c_${pId}_B",
                    pairId = pId,
                    displayArabic = let.nameAr,
                    subtitle = "${let.nameBn} (${let.nameEn})",
                    audioLetter = let.letter,
                    cardTypeLabel = "নাম"
                  )
                )
              }
              promptBn = "১২-কার্ড গ্রিড (৩x৪): পূর্ণ বর্ণমালার ৬ জোড়া হরফ ও আরবি নাম উন্মোচন করুন:"
              promptEn = "Flip 12 cards (3x4 grid) to match 6 pairs of letters and Arabic names:"
              promptAr = "طابق ٦ أزواج من الحروف والأسماء في شبكة ١٢ بطاقة (٣×٤):"
              explanationBn = "১২টি কার্ডের ৬ জোড়াই সফলভাবে উদ্ধার করেছেন।"
              explanationEn = "6 pairs matched in 12-card grid."
            }
            18 -> {
              // 12 Cards (6 Pairs): Form & Makhraj Champion ⚡ (14s)
              val selectedSix = allLetters.shuffled(Random(levelNumber * 100 + qNum * 37)).take(6)
              selectedSix.forEachIndexed { idx, let ->
                val pId = "p_l${levelNumber}_q${qNum}_champ6_$idx"
                memoryCards.add(
                  MemoryFlipCard(
                    id = "c_${pId}_A",
                    pairId = pId,
                    displayArabic = let.letter,
                    subtitle = let.nameBn,
                    audioLetter = let.letter,
                    cardTypeLabel = "বিচ্ছিন্ন"
                  )
                )
                memoryCards.add(
                  MemoryFlipCard(
                    id = "c_${pId}_B",
                    pairId = pId,
                    displayArabic = if (let.initial.isNotEmpty()) let.initial else let.letter,
                    subtitle = "${let.nameBn} (সংযুক্ত)",
                    audioLetter = let.letter,
                    cardTypeLabel = "সংযুক্ত"
                  )
                )
              }
              promptBn = "⚡ চ্যাম্পিয়ন ১২-কার্ড: ১৪ সেকেন্ডে ৬ জোড়া হরফ ও যুক্তরূপের দ্রুত মিল করুন!"
              promptEn = "⚡ Champion 12-Card: Match 6 pairs of letters and connected forms in 14s!"
              promptAr = "⚡ بطل الحروف والأشكال: طابق ٦ أزواج في غضون ١٤ ثانية!"
              explanationBn = "১৪ সেকেন্ডের মধ্যে ১২টি কার্ডের ৬ জোড়া রূপভেদ উদ্ধার সম্পন্ন!"
              explanationEn = "6 pairs of forms matched under 14s timer."
            }
            19 -> {
              // 12 Cards (6 Pairs): Super Memory Blitz ⚡ (12s)
              val selectedSix = allLetters.shuffled(Random(levelNumber * 100 + qNum * 39)).take(6)
              selectedSix.forEachIndexed { idx, let ->
                val pId = "p_l${levelNumber}_q${qNum}_blitz6_$idx"
                memoryCards.add(
                  MemoryFlipCard(
                    id = "c_${pId}_A",
                    pairId = pId,
                    displayArabic = let.letter,
                    subtitle = let.nameBn,
                    audioLetter = let.letter,
                    cardTypeLabel = "হরফ"
                  )
                )
                memoryCards.add(
                  MemoryFlipCard(
                    id = "c_${pId}_B",
                    pairId = pId,
                    displayArabic = let.nameAr,
                    subtitle = let.phonetic,
                    audioLetter = let.letter,
                    cardTypeLabel = "নাম"
                  )
                )
              }
              promptBn = "⚡ সুপার মেমোরি ১২-কার্ড: ১২ সেকেন্ডে ৬ জোড়া হরফ ও নামের মিল করুন!"
              promptEn = "⚡ Super Memory 12-Card: Match 6 pairs of letters & names within 12 seconds!"
              promptAr = "⚡ الذاكرة الفائقة ١٢ بطاقة: طابق ٦ أزواج في غضون ١٢ ثانية!"
              explanationBn = "১২ সেকেন্ডের মধ্যে ১২টি কার্ডের ৬ জোড়াই সফলভাবে উদ্ধার করেছেন!"
              explanationEn = "Super memory achieved: 6 pairs matched within 12 seconds!"
            }
            else -> {
              // Level 20: 12 Cards (6 Pairs): Grand Memory Champion ⚡ (10s)
              val selectedSix = allLetters.shuffled(Random(levelNumber * 100 + qNum * 41)).take(6)
              selectedSix.forEachIndexed { idx, let ->
                val pId = "p_l${levelNumber}_q${qNum}_grand6_$idx"
                memoryCards.add(
                  MemoryFlipCard(
                    id = "c_${pId}_A",
                    pairId = pId,
                    displayArabic = let.letter,
                    subtitle = let.nameBn,
                    audioLetter = let.letter,
                    cardTypeLabel = "হরফ"
                  )
                )
                memoryCards.add(
                  MemoryFlipCard(
                    id = "c_${pId}_B",
                    pairId = pId,
                    displayArabic = if (let.initial.isNotEmpty()) let.initial else let.letter,
                    subtitle = let.nameAr,
                    audioLetter = let.letter,
                    cardTypeLabel = "রূপ"
                  )
                )
              }
              promptBn = "⚡ গ্র্যান্ড মেমোরি চ্যাম্পিয়ন: ১০ সেকেন্ডে ১২টি কার্ডের ৬ জোড়া উন্মোচন করুন!"
              promptEn = "⚡ Grand Memory Champion: Reveal 6 pairs from 12 cards in 10 seconds!"
              promptAr = "⚡ بطل الذاكرة الأكبر: اكشف ٦ أزواج من ١٢ بطاقة في ١٠ ثوانٍ!"
              explanationBn = "অসাধারণ মেমোরি রিকল! গ্র্যান্ড চ্যাম্পিয়নের মতো ৬টি জোড়াই উদ্ধার করেছেন।"
              explanationEn = "Grand Champion Memory! Mastered all 6 pairs in record time."
            }
          }
        }
      }

      // Dynamic Shuffling of cards so layout never repeats predictably!
      val shuffledCards = memoryCards.shuffled(Random(levelNumber * 1000 + qNum * 47 + System.currentTimeMillis() % 100))
      val firstLetter = allLetters.find { it.letter == shuffledCards.firstOrNull()?.audioLetter } ?: allLetters.first()

      questions.add(
        GameQuestion(
          questionNumber = qNum,
          gameType = GameType.MIX_AND_MATCH,
          levelNumber = levelNumber,
          promptBn = promptBn,
          promptEn = promptEn,
          promptAr = promptAr,
          displayArabic = "🃏",
          targetLetter = firstLetter,
          options = emptyList(),
          correctOptionId = "",
          explanationBn = explanationBn,
          explanationEn = explanationEn,
          isTimeAttack = isLevelTimeAttack,
          timeLimitSeconds = timeLimit,
          memoryCards = shuffledCards,
          requiredPairsCount = requiredPairs
        )
      )
    }

    return questions
  }

  // =========================================================================
  // 5. SHAPE MASTER PATH (শেপ মাস্টার পাথ - রোডম্যাপ গেম)
  // 20-Level Progressive Curriculum Engine:
  // - Levels 1-5: Initial Forms (প্রাথমিক রূপ / بِدَايَة)
  // - Levels 6-10: Medial Forms (মধ্য রূপ / وَسَط)
  // - Levels 11-15: Final Forms (শেষ রূপ / نِهَايَة)
  // - Levels 16-20: Full Word Analysis (মিশ্র রূপ ও যুক্তবর্ণ / تحليل الكلمات)
  // =========================================================================
  private data class WordShapeEntry(
    val word: String,
    val meaningBn: String,
    val meaningEn: String,
    val meaningAr: String,
    val initGlyph: String,
    val initShape: String,
    val medGlyph: String,
    val medShape: String,
    val finGlyph: String,
    val finShape: String
  )

  private val wordShapeEntries = listOf(
    WordShapeEntry("كَتَبَ", "লিখল (He wrote)", "He wrote", "كَتَبَ", "ك", "كـ", "ت", "ـتـ", "ب", "ـب"),
    WordShapeEntry("قَلَم", "কলম (Pen)", "Pen", "قَلَم", "ق", "قـ", "ل", "ـلـ", "م", "ـم"),
    WordShapeEntry("نَصَرَ", "সাহায্য করল (He helped)", "He helped", "نَصَرَ", "ن", "نـ", "ص", "ـصـ", "ر", "ـر"),
    WordShapeEntry("سَمِعَ", "শুনল (He heard)", "He heard", "سَمِعَ", "س", "سـ", "م", "ـمـ", "ع", "ـع"),
    WordShapeEntry("خَلَقَ", "সৃষ্টি করল (He created)", "He created", "خَلَقَ", "خ", "خـ", "ل", "ـلـ", "ق", "ـق"),
    WordShapeEntry("غَفَرَ", "ক্ষমা করল (He forgave)", "He forgave", "غَفَرَ", "غ", "غـ", "ف", "ـفـ", "ر", "ـر"),
    WordShapeEntry("صَبَرَ", "ধৈর্য ধরল (He was patient)", "He was patient", "صَبَرَ", "ص", "صـ", "ب", "ـبـ", "ر", "ـر"),
    WordShapeEntry("عَبَدَ", "ইবাদত করল (He worshipped)", "He worshipped", "عَبَدَ", "ع", "عـ", "ب", "ـبـ", "د", "ـد"),
    WordShapeEntry("شَكَرَ", "কৃতজ্ঞতা জানাল (He thanked)", "He thanked", "شَكَرَ", "ش", "شـ", "ك", "ـكـ", "ر", "ـر"),
    WordShapeEntry("ذَكَرَ", "স্মরণ করল (He remembered)", "He remembered", "ذَكَرَ", "ذ", "ذ", "ك", "ـكـ", "ر", "ـر"),
    WordShapeEntry("جَلَسَ", "বসল (He sat)", "He sat", "جَلَسَ", "ج", "جـ", "ل", "ـلـ", "س", "ـس"),
    WordShapeEntry("رَحِمَ", "দয়া করল (He had mercy)", "He had mercy", "رَحِمَ", "ر", "ر", "ح", "حـ", "م", "ـم"),
    WordShapeEntry("فَتَحَ", "উন্মুক্ত করল (He opened)", "He opened", "فَتَحَ", "ف", "فـ", "ت", "ـتـ", "ح", "ـح"),
    WordShapeEntry("طَلَبَ", "সন্ধান করল (He sought)", "He sought", "طَلَبَ", "ط", "طـ", "ل", "ـلـ", "ب", "ـب"),
    WordShapeEntry("مَلَكَ", "মালিকানা পেল (He owned)", "He owned", "مَلَكَ", "م", "مـ", "ل", "ـلـ", "ك", "ـك"),
    WordShapeEntry("نَظَرَ", "তাকিয়ে দেখল (He looked)", "He looked", "نَظَرَ", "ن", "نـ", "ظ", "ـظـ", "ر", "ـر"),
    WordShapeEntry("عَلِمَ", "জানল (He knew)", "He knew", "عَلِمَ", "ع", "عـ", "ل", "ـلـ", "م", "ـم"),
    WordShapeEntry("بَلَغَ", "পৌঁছাল (He reached)", "He reached", "بَلَغَ", "ب", "بـ", "ل", "ـلـ", "غ", "ـغ"),
    WordShapeEntry("وَهَبَ", "দান করল (He granted)", "He granted", "وَهَبَ", "و", "و", "هـ", "ـهـ", "ب", "ـب"),
    WordShapeEntry("حَمِدَ", "প্রশংসা করল (He praised)", "He praised", "حَمِدَ", "ح", "حـ", "م", "ـمـ", "د", "ـد"),
    WordShapeEntry("صَدَقَ", "সত্য বলল (He was truthful)", "He was truthful", "صَدَقَ", "ص", "صـ", "د", "ـد", "ق", "ـق"),
    WordShapeEntry("نَبَتَ", "অঙ্কুরিত হল (It grew)", "It grew", "نَبَتَ", "ن", "نـ", "ب", "ـبـ", "ت", "ـت"),
    WordShapeEntry("رَسَمَ", "আঁকল (He drew)", "He drew", "رَسَمَ", "ر", "ر", "س", "ـسـ", "م", "ـم"),
    WordShapeEntry("زَرَعَ", "বপন করল (He planted)", "He planted", "زَرَعَ", "ز", "ز", "ر", "ـر", "ع", "ـع"),
    WordShapeEntry("بَعَثَ", "প্রেরণ করল (He sent)", "He sent", "بَعَثَ", "ب", "بـ", "ع", "ـعـ", "ث", "ـث")
  )

  private fun findArabicLetter(glyph: String): ArabicLetter {
    val clean = glyph.trim()
    return ArabicAlphabetRepository.letters.firstOrNull { it.letter == clean }
      ?: ArabicAlphabetRepository.letters.firstOrNull { it.isolated == clean }
      ?: ArabicAlphabetRepository.letters.firstOrNull { it.letter.startsWith(clean.take(1)) }
      ?: ArabicAlphabetRepository.letters.first()
  }

  private fun generateShapeMasterQuestions(
    levelNumber: Int,
    tier: GameDifficultyTier,
    isTimeAttack: Boolean
  ): List<GameQuestion> {
    val allLetters = ArabicAlphabetRepository.letters
    val questions = mutableListOf<GameQuestion>()

    // Pedagogical letter groups
    val boatLetters = allLetters.filter { it.letter in listOf("ب", "ت", "ث", "ن", "ي") }
    val curveLetters = allLetters.filter { it.letter in listOf("ج", "ح", "خ", "ع", "غ") }
    val toothLetters = allLetters.filter { it.letter in listOf("س", "ش", "ص", "ض", "ط", "ظ") }
    val tallSpecialLetters = allLetters.filter { it.letter in listOf("ف", "ق", "ك", "ل", "م", "هـ") }
    val nonConnectingLetters = allLetters.filter { it.letter in listOf("ا", "د", "ذ", "ر", "ز", "و") }

    when (levelNumber) {
      // -----------------------------------------------------------------------
      // STAGE 1: LEVELS 1-5 (INITIAL FORMS / প্রাথমিক রূপ / بِدَايَة)
      // -----------------------------------------------------------------------
      in 1..5 -> {
        val levelPool = when (levelNumber) {
          1 -> boatLetters
          2 -> curveLetters
          3 -> toothLetters
          4 -> tallSpecialLetters + nonConnectingLetters
          else -> allLetters
        }

        for (qNum in 1..20) {
          val target = levelPool[(qNum * 3 + levelNumber * 7) % levelPool.size]
          val isShapeToLetter = (qNum % 2 == 0)

          if (!isShapeToLetter) {
            // Type A: Given Letter -> Pick Initial Form
            val correctShape = target.initial
            val optCorrect = GameOption(
              id = "opt_l${levelNumber}_q${qNum}_init",
              arabicDisplay = correctShape,
              primaryText = "${target.nameBn} (শুরুর রূপ)",
              secondaryText = "Initial Form (بداية)"
            )
            val optMedial = GameOption(
              id = "opt_l${levelNumber}_q${qNum}_med",
              arabicDisplay = target.medial,
              primaryText = "${target.nameBn} (মধ্য রূপ)",
              secondaryText = "Medial Form (وسط)"
            )
            val optFinal = GameOption(
              id = "opt_l${levelNumber}_q${qNum}_fin",
              arabicDisplay = target.finalForm,
              primaryText = "${target.nameBn} (শেষ রূপ)",
              secondaryText = "Final Form (نهاية)"
            )
            val distractorLetter = allLetters.filter { it.id != target.id }.shuffled().first()
            val optDistractor = GameOption(
              id = "opt_l${levelNumber}_q${qNum}_dist",
              arabicDisplay = distractorLetter.initial,
              primaryText = "${distractorLetter.nameBn} (শুরুর রূপ)",
              secondaryText = "Initial Form (بداية)"
            )

            val options = listOf(optCorrect, optMedial, optFinal, optDistractor).shuffled()
            questions.add(
              GameQuestion(
                questionNumber = qNum,
                gameType = GameType.SHAPE_MASTER_PATH,
                levelNumber = levelNumber,
                promptBn = "আরবি হরফ '${target.nameBn} (${target.letter})' শব্দের শুরুতে (প্রারম্ভিক রূপ) কেমন দেখায়?",
                promptEn = "Which shape represents '${target.nameEn} (${target.letter})' at the beginning of a word (Initial Form)?",
                promptAr = "ما هو شكل حرف '${target.nameAr}' في بداية الكلمة (البداية)؟",
                displayArabic = target.letter,
                targetLetter = target,
                options = options,
                correctOptionId = optCorrect.id,
                explanationBn = "${target.nameBn} শব্দের শুরুতে পরবর্তী হরফের সাথে যুক্ত হতে '${target.initial}' রূপ ধারণ করে।",
                explanationEn = "${target.nameEn} connects to the next letter as '${target.initial}' at the start of a word."
              )
            )
          } else {
            // Type B: Given Initial Shape -> Pick Letter Name
            val targetInitial = target.initial
            val distractorLetters = allLetters.filter { it.id != target.id }.shuffled().take(3)
            val optCorrect = GameOption(
              id = "opt_l${levelNumber}_q${qNum}_corr_let",
              arabicDisplay = target.letter,
              primaryText = "${target.nameBn} (${target.letter})",
              secondaryText = target.nameEn
            )
            val otherOptions = distractorLetters.mapIndexed { idx, let ->
              GameOption(
                id = "opt_l${levelNumber}_q${qNum}_dist_$idx",
                arabicDisplay = let.letter,
                primaryText = "${let.nameBn} (${let.letter})",
                secondaryText = let.nameEn
              )
            }
            val options = (listOf(optCorrect) + otherOptions).shuffled()
            questions.add(
              GameQuestion(
                questionNumber = qNum,
                gameType = GameType.SHAPE_MASTER_PATH,
                levelNumber = levelNumber,
                promptBn = "প্রারম্ভিক রূপ '${targetInitial}' কোন আরবি হরফটির শুরুর রূপ?",
                promptEn = "The initial shape '${targetInitial}' belongs to which Arabic letter?",
                promptAr = "الشكل الأولي '${targetInitial}' يمثل بداية أي حرف؟",
                displayArabic = targetInitial,
                targetLetter = target,
                options = options,
                correctOptionId = optCorrect.id,
                explanationBn = "'$targetInitial' হলো '${target.nameBn} (${target.letter})' এর প্রারম্ভিক সংযুক্ত রূপ।",
                explanationEn = "'$targetInitial' is the initial connected form of '${target.nameEn}'."
              )
            )
          }
        }
      }

      // -----------------------------------------------------------------------
      // STAGE 2: LEVELS 6-10 (MEDIAL FORMS / মধ্য রূপ / وَسَط)
      // -----------------------------------------------------------------------
      in 6..10 -> {
        val levelPool = when (levelNumber) {
          6 -> boatLetters
          7 -> curveLetters
          8 -> toothLetters
          9 -> tallSpecialLetters
          else -> allLetters.filter { it.medial != it.isolated } // All connecting letters
        }

        for (qNum in 1..20) {
          val target = levelPool[(qNum * 3 + levelNumber * 5) % levelPool.size]
          val isShapeToLetter = (qNum % 3 == 0)

          if (!isShapeToLetter) {
            // Pick Medial Form among Initial, Final, Isolated
            val correctMedial = target.medial
            val optCorrect = GameOption(
              id = "opt_l${levelNumber}_q${qNum}_med_corr",
              arabicDisplay = correctMedial,
              primaryText = "${target.nameBn} (মধ্য রূপ)",
              secondaryText = "Medial Form (وسط)"
            )
            val optInitial = GameOption(
              id = "opt_l${levelNumber}_q${qNum}_init",
              arabicDisplay = target.initial,
              primaryText = "${target.nameBn} (শুরুর রূপ)",
              secondaryText = "Initial Form (بداية)"
            )
            val optFinal = GameOption(
              id = "opt_l${levelNumber}_q${qNum}_fin",
              arabicDisplay = target.finalForm,
              primaryText = "${target.nameBn} (শেষ রূপ)",
              secondaryText = "Final Form (نهاية)"
            )
            val optIsolated = GameOption(
              id = "opt_l${levelNumber}_q${qNum}_iso",
              arabicDisplay = target.isolated,
              primaryText = "${target.nameBn} (বিচ্ছিন্ন রূপ)",
              secondaryText = "Isolated Form (منفصل)"
            )

            val options = listOf(optCorrect, optInitial, optFinal, optIsolated).shuffled()
            questions.add(
              GameQuestion(
                questionNumber = qNum,
                gameType = GameType.SHAPE_MASTER_PATH,
                levelNumber = levelNumber,
                promptBn = "হরফ '${target.nameBn} (${target.letter})' যখন শব্দের মাঝে উভয় পাশে যুক্ত হয় (মধ্য রূপ), তখন রূপটি কেমন?",
                promptEn = "Which shape represents '${target.nameEn}' in the middle of a word connected on both sides (Medial Form)?",
                promptAr = "ما هو شكل حرف '${target.nameAr}' في وسط الكلمة متصلاً من الجانبين؟",
                displayArabic = target.letter,
                targetLetter = target,
                options = options,
                correctOptionId = optCorrect.id,
                explanationBn = "${target.nameBn} শব্দের মাঝে দুই পাশের হরফের সাথে যুক্ত হয়ে '${target.medial}' রূপ ধারণ করে।",
                explanationEn = "${target.nameEn} transforms into '${target.medial}' when connecting in the middle of a word."
              )
            )
          } else {
            // Given Medial Shape -> Pick Letter Name
            val targetMedial = target.medial
            val distractorLetters = allLetters.filter { it.id != target.id }.shuffled().take(3)
            val optCorrect = GameOption(
              id = "opt_l${levelNumber}_q${qNum}_corr_med_let",
              arabicDisplay = target.letter,
              primaryText = "${target.nameBn} (${target.letter})",
              secondaryText = target.nameEn
            )
            val otherOptions = distractorLetters.mapIndexed { idx, let ->
              GameOption(
                id = "opt_l${levelNumber}_q${qNum}_dist_med_$idx",
                arabicDisplay = let.letter,
                primaryText = "${let.nameBn} (${let.letter})",
                secondaryText = let.nameEn
              )
            }
            val options = (listOf(optCorrect) + otherOptions).shuffled()
            questions.add(
              GameQuestion(
                questionNumber = qNum,
                gameType = GameType.SHAPE_MASTER_PATH,
                levelNumber = levelNumber,
                promptBn = "মধ্যবর্তী যুক্ত রূপ '${targetMedial}' কোন আরবি হরফটির রূপ?",
                promptEn = "The medial shape '${targetMedial}' belongs to which Arabic letter?",
                promptAr = "الشكل المتوسط '${targetMedial}' يمثل وسط أي حرف؟",
                displayArabic = targetMedial,
                targetLetter = target,
                options = options,
                correctOptionId = optCorrect.id,
                explanationBn = "'$targetMedial' হলো '${target.nameBn} (${target.letter})' এর মধ্যবর্তী রূপ।",
                explanationEn = "'$targetMedial' is the medial connected shape of '${target.nameEn}'."
              )
            )
          }
        }
      }

      // -----------------------------------------------------------------------
      // STAGE 3: LEVELS 11-15 (FINAL FORMS / শেষ রূপ / نِهَايَة)
      // -----------------------------------------------------------------------
      in 11..15 -> {
        val levelPool = when (levelNumber) {
          11 -> boatLetters
          12 -> curveLetters
          13 -> toothLetters
          14 -> tallSpecialLetters
          else -> allLetters
        }

        for (qNum in 1..20) {
          val target = levelPool[(qNum * 3 + levelNumber * 7) % levelPool.size]
          val isShapeToLetter = (qNum % 3 == 0)

          if (!isShapeToLetter) {
            // Pick Final Connected Form vs Isolated vs Medial vs Initial
            val correctFinal = target.finalForm
            val optCorrect = GameOption(
              id = "opt_l${levelNumber}_q${qNum}_fin_corr",
              arabicDisplay = correctFinal,
              primaryText = "${target.nameBn} (শেষ যুক্ত রূপ)",
              secondaryText = "Final Connected (نهاية)"
            )
            val optIsolated = GameOption(
              id = "opt_l${levelNumber}_q${qNum}_iso",
              arabicDisplay = target.isolated,
              primaryText = "${target.nameBn} (বিচ্ছিন্ন রূপ)",
              secondaryText = "Isolated (منفصل)"
            )
            val optMedial = GameOption(
              id = "opt_l${levelNumber}_q${qNum}_med",
              arabicDisplay = target.medial,
              primaryText = "${target.nameBn} (মধ্য রূপ)",
              secondaryText = "Medial (وسط)"
            )
            val optInitial = GameOption(
              id = "opt_l${levelNumber}_q${qNum}_init",
              arabicDisplay = target.initial,
              primaryText = "${target.nameBn} (শুরুর রূপ)",
              secondaryText = "Initial (بداية)"
            )

            val options = listOf(optCorrect, optIsolated, optMedial, optInitial).shuffled()
            questions.add(
              GameQuestion(
                questionNumber = qNum,
                gameType = GameType.SHAPE_MASTER_PATH,
                levelNumber = levelNumber,
                promptBn = "হরফ '${target.nameBn} (${target.letter})' যখন শব্দের শেষে পূর্বে যুক্ত হয় (সমাপ্তি/শেষ রূপ), তখন এর সঠিক রূপ কোনটি?",
                promptEn = "Which shape is the Final connected form of '${target.nameEn}' at the end of a word?",
                promptAr = "ما هو شكل حرف '${target.nameAr}' المتصل في نهاية الكلمة؟",
                displayArabic = target.letter,
                targetLetter = target,
                options = options,
                correctOptionId = optCorrect.id,
                explanationBn = "${target.nameBn} শব্দের শেষে এসে পূর্বের হরফের সাথে যুক্ত হতে '${target.finalForm}' রূপ ধারণ করে।",
                explanationEn = "${target.nameEn} connects to the preceding letter as '${target.finalForm}' at word end.",
                isTimeAttack = (levelNumber == 15),
                timeLimitSeconds = if (levelNumber == 15) 12 else 0
              )
            )
          } else {
            // Given Final Connected Shape -> Pick Letter Name
            val targetFinal = target.finalForm
            val distractorLetters = allLetters.filter { it.id != target.id }.shuffled().take(3)
            val optCorrect = GameOption(
              id = "opt_l${levelNumber}_q${qNum}_corr_fin_let",
              arabicDisplay = target.letter,
              primaryText = "${target.nameBn} (${target.letter})",
              secondaryText = target.nameEn
            )
            val otherOptions = distractorLetters.mapIndexed { idx, let ->
              GameOption(
                id = "opt_l${levelNumber}_q${qNum}_dist_fin_$idx",
                arabicDisplay = let.letter,
                primaryText = "${let.nameBn} (${let.letter})",
                secondaryText = let.nameEn
              )
            }
            val options = (listOf(optCorrect) + otherOptions).shuffled()
            questions.add(
              GameQuestion(
                questionNumber = qNum,
                gameType = GameType.SHAPE_MASTER_PATH,
                levelNumber = levelNumber,
                promptBn = "যুক্ত সমাপ্তি রূপ '${targetFinal}' কোন আরবি হরফটির শেষ রূপ?",
                promptEn = "The final connected shape '${targetFinal}' belongs to which Arabic letter?",
                promptAr = "الشكل النهائي '${targetFinal}' يمثل نهاية أي حرف؟",
                displayArabic = targetFinal,
                targetLetter = target,
                options = options,
                correctOptionId = optCorrect.id,
                explanationBn = "'$targetFinal' হলো '${target.nameBn} (${target.letter})' এর শেষ সমাপ্তি রূপ।",
                explanationEn = "'$targetFinal' is the connected ending form of '${target.nameEn}'.",
                isTimeAttack = (levelNumber == 15),
                timeLimitSeconds = if (levelNumber == 15) 12 else 0
              )
            )
          }
        }
      }

      // -----------------------------------------------------------------------
      // STAGE 4: LEVELS 16-20 (FULL WORD ANALYSIS / মিশ্র রূপ ও যুক্তবর্ণ)
      // -----------------------------------------------------------------------
      else -> {
        for (qNum in 1..20) {
          val entry = wordShapeEntries[(qNum * 2 + levelNumber * 3) % wordShapeEntries.size]
          val targetPosition = when (levelNumber) {
            16 -> 0 // Initial Letter
            17 -> 1 // Medial Letter
            18 -> 2 // Final Letter
            else -> qNum % 3 // Levels 19 and 20: Mixed Positions
          }

          when (targetPosition) {
            0 -> {
              // Word Analysis: Initial Letter Isolation
              val targetLetter = findArabicLetter(entry.initGlyph)
              val correctShape = entry.initShape
              val optCorrect = GameOption(
                id = "opt_l${levelNumber}_q${qNum}_word_init_corr",
                arabicDisplay = correctShape,
                primaryText = "${targetLetter.nameBn} (শুরুর রূপ)",
                secondaryText = "Initial Shape (بداية)"
              )
              val optMedial = GameOption(
                id = "opt_l${levelNumber}_q${qNum}_word_init_med",
                arabicDisplay = targetLetter.medial,
                primaryText = "${targetLetter.nameBn} (মধ্য রূপ)",
                secondaryText = "Medial Shape (وسط)"
              )
              val optFinal = GameOption(
                id = "opt_l${levelNumber}_q${qNum}_word_init_fin",
                arabicDisplay = targetLetter.finalForm,
                primaryText = "${targetLetter.nameBn} (শেষ রূপ)",
                secondaryText = "Final Shape (نهاية)"
              )
              val optWordMedialLetter = GameOption(
                id = "opt_l${levelNumber}_q${qNum}_word_other",
                arabicDisplay = entry.medShape,
                primaryText = "অন্য হরফ: ${entry.medGlyph}",
                secondaryText = "Other letter in word"
              )

              val options = listOf(optCorrect, optMedial, optFinal, optWordMedialLetter).shuffled()
              questions.add(
                GameQuestion(
                  questionNumber = qNum,
                  gameType = GameType.SHAPE_MASTER_PATH,
                  levelNumber = levelNumber,
                  promptBn = "'${entry.word}' (${entry.meaningBn}) শব্দটিতে শুরুর হরফ '${targetLetter.nameBn} (${targetLetter.letter})' এর সঠিক প্রারম্ভিক রূপ কোনটি?",
                  promptEn = "In '${entry.word}' (${entry.meaningEn}), which shape is the initial '${targetLetter.nameEn}'?",
                  promptAr = "في كلمة '${entry.word}'، ما هو شكل الحرف الأول '${targetLetter.nameAr}'؟",
                  displayArabic = entry.word,
                  targetLetter = targetLetter,
                  options = options,
                  correctOptionId = optCorrect.id,
                  explanationBn = "'${entry.word}' শব্দটিতে শুরুর হরফ হলো '${targetLetter.nameBn}', যার প্রারম্ভিক রূপ হলো '$correctShape' যা পরবর্তী হরফের সাথে যুক্ত।",
                  explanationEn = "In '${entry.word}', the first letter is '${targetLetter.nameEn}' connecting as '$correctShape'.",
                  isTimeAttack = (levelNumber == 20),
                  timeLimitSeconds = if (levelNumber == 20) 10 else 0
                )
              )
            }
            1 -> {
              // Word Analysis: Medial Letter Isolation
              val targetLetter = findArabicLetter(entry.medGlyph)
              val correctShape = entry.medShape
              val optCorrect = GameOption(
                id = "opt_l${levelNumber}_q${qNum}_word_med_corr",
                arabicDisplay = correctShape,
                primaryText = "${targetLetter.nameBn} (মধ্য রূপ)",
                secondaryText = "Medial Shape (وسط)"
              )
              val optInitial = GameOption(
                id = "opt_l${levelNumber}_q${qNum}_word_med_init",
                arabicDisplay = targetLetter.initial,
                primaryText = "${targetLetter.nameBn} (শুরুর রূপ)",
                secondaryText = "Initial Shape (بداية)"
              )
              val optFinal = GameOption(
                id = "opt_l${levelNumber}_q${qNum}_word_med_fin",
                arabicDisplay = targetLetter.finalForm,
                primaryText = "${targetLetter.nameBn} (শেষ রূপ)",
                secondaryText = "Final Shape (نهاية)"
              )
              val optWordInitLetter = GameOption(
                id = "opt_l${levelNumber}_q${qNum}_word_init_other",
                arabicDisplay = entry.initShape,
                primaryText = "শুরুর হরফ: ${entry.initGlyph}",
                secondaryText = "First letter in word"
              )

              val options = listOf(optCorrect, optInitial, optFinal, optWordInitLetter).shuffled()
              questions.add(
                GameQuestion(
                  questionNumber = qNum,
                  gameType = GameType.SHAPE_MASTER_PATH,
                  levelNumber = levelNumber,
                  promptBn = "'${entry.word}' (${entry.meaningBn}) শব্দটিতে মধ্যবর্তী হরফ '${targetLetter.nameBn} (${targetLetter.letter})' এর সঠিক মধ্য রূপ কোনটি?",
                  promptEn = "In '${entry.word}' (${entry.meaningEn}), which shape is the medial '${targetLetter.nameEn}'?",
                  promptAr = "في كلمة '${entry.word}'، ما هو شكل الحرف الأوسط '${targetLetter.nameAr}'؟",
                  displayArabic = entry.word,
                  targetLetter = targetLetter,
                  options = options,
                  correctOptionId = optCorrect.id,
                  explanationBn = "'${entry.word}' শব্দটিতে মধ্যবর্তী হরফ '${targetLetter.nameBn}' দুই পাশের হরফের মাঝে যুক্ত হয়ে '$correctShape' রূপ ধারণ করেছে।",
                  explanationEn = "In '${entry.word}', the middle letter '${targetLetter.nameEn}' connects from both sides as '$correctShape'.",
                  isTimeAttack = (levelNumber == 20),
                  timeLimitSeconds = if (levelNumber == 20) 10 else 0
                )
              )
            }
            else -> {
              // Word Analysis: Final Letter Isolation
              val targetLetter = findArabicLetter(entry.finGlyph)
              val correctShape = entry.finShape
              val optCorrect = GameOption(
                id = "opt_l${levelNumber}_q${qNum}_word_fin_corr",
                arabicDisplay = correctShape,
                primaryText = "${targetLetter.nameBn} (শেষ সমাপ্তি রূপ)",
                secondaryText = "Final Shape (نهاية)"
              )
              val optMedial = GameOption(
                id = "opt_l${levelNumber}_q${qNum}_word_fin_med",
                arabicDisplay = targetLetter.medial,
                primaryText = "${targetLetter.nameBn} (মধ্য রূপ)",
                secondaryText = "Medial Shape (وسط)"
              )
              val optInitial = GameOption(
                id = "opt_l${levelNumber}_q${qNum}_word_fin_init",
                arabicDisplay = targetLetter.initial,
                primaryText = "${targetLetter.nameBn} (শুরুর রূপ)",
                secondaryText = "Initial Shape (بداية)"
              )
              val optIsolated = GameOption(
                id = "opt_l${levelNumber}_q${qNum}_word_fin_iso",
                arabicDisplay = targetLetter.isolated,
                primaryText = "${targetLetter.nameBn} (বিচ্ছিন্ন রূপ)",
                secondaryText = "Isolated (منفصل)"
              )

              val options = listOf(optCorrect, optMedial, optInitial, optIsolated).shuffled()
              questions.add(
                GameQuestion(
                  questionNumber = qNum,
                  gameType = GameType.SHAPE_MASTER_PATH,
                  levelNumber = levelNumber,
                  promptBn = "'${entry.word}' (${entry.meaningBn}) শব্দটিতে সমাপ্তি হরফ '${targetLetter.nameBn} (${targetLetter.letter})' এর সঠিক শেষ রূপ কোনটি?",
                  promptEn = "In '${entry.word}' (${entry.meaningEn}), which shape is the final '${targetLetter.nameEn}'?",
                  promptAr = "في كلمة '${entry.word}'، ما هو شكل الحرف الأخير '${targetLetter.nameAr}'؟",
                  displayArabic = entry.word,
                  targetLetter = targetLetter,
                  options = options,
                  correctOptionId = optCorrect.id,
                  explanationBn = "'${entry.word}' শব্দটিতে শেষ হরফ '${targetLetter.nameBn}' পূর্বের অক্ষরের সাথে সমাপ্তিতে '$correctShape' রূপে মিলিত হয়েছে।",
                  explanationEn = "In '${entry.word}', the final letter '${targetLetter.nameEn}' ends the word as '$correctShape'.",
                  isTimeAttack = (levelNumber == 20),
                  timeLimitSeconds = if (levelNumber == 20) 10 else 0
                )
              )
            }
          }
        }
      }
    }

    return questions
  }
}
