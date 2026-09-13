package com.example

import androidx.compose.ui.graphics.Color

enum class FormType {
  ISOLATED,
  INITIAL,
  MEDIAL,
  FINAL;

  fun getTitle(lang: String): String = when (lang) {
    "EN" -> when (this) {
      ISOLATED -> "Isolated"
      INITIAL -> "Initial (Start)"
      MEDIAL -> "Medial (Middle)"
      FINAL -> "Final (End)"
    }
    "AR" -> when (this) {
      ISOLATED -> "مستقل"
      INITIAL -> "بداية الكلمة"
      MEDIAL -> "وسط الكلمة"
      FINAL -> "نهاية الكلمة"
    }
    else -> when (this) {
      ISOLATED -> "বিচ্ছিন্ন"
      INITIAL -> "শুরুতে (প্রারম্ভিক)"
      MEDIAL -> "মাঝে (মধ্যবর্তী)"
      FINAL -> "শেষে (প্রান্তিক)"
    }
  }

  fun getDirectionHint(): String = when (this) {
    ISOLATED -> "• Standalone"
    INITIAL -> "← Connects Left"
    MEDIAL -> "↔ Connects Both"
    FINAL -> "→ Connects Right"
  }

  fun getShortPositionalLabel(lang: String): String = when (lang) {
    "EN" -> when (this) {
      ISOLATED -> "Isolated"
      INITIAL -> "Beginning"
      MEDIAL -> "Middle"
      FINAL -> "End"
    }
    "AR" -> when (this) {
      ISOLATED -> "منفصل"
      INITIAL -> "في البداية"
      MEDIAL -> "في الوسط"
      FINAL -> "في النهاية"
    }
    else -> when (this) {
      ISOLATED -> "বিচ্ছিন্ন"
      INITIAL -> "শুরুতে"
      MEDIAL -> "মাঝে"
      FINAL -> "শেষে"
    }
  }

  fun getPositionalHint(lang: String): String = when (lang) {
    "EN" -> when (this) {
      ISOLATED -> "(Standalone)"
      INITIAL -> "(Beginning)"
      MEDIAL -> "(Middle)"
      FINAL -> "(End)"
    }
    "AR" -> when (this) {
      ISOLATED -> "(منفصل)"
      INITIAL -> "(في البداية)"
      MEDIAL -> "(في الوسط)"
      FINAL -> "(في النهاية)"
    }
    else -> when (this) {
      ISOLATED -> "(বিচ্ছিন্ন)"
      INITIAL -> "(শুরুতে)"
      MEDIAL -> "(মাঝে)"
      FINAL -> "(শেষে)"
    }
  }

  fun getVisualSchematic(): String = when (this) {
    ISOLATED -> "○"
    INITIAL -> "●―"
    MEDIAL -> "―●―"
    FINAL -> "―●"
  }

  fun getTagColor(): Color = when (this) {
    ISOLATED -> Color(0xFF3B82F6) // Blue
    INITIAL -> Color(0xFF10B981)  // Emerald
    MEDIAL -> Color(0xFFF59E0B)   // Amber
    FINAL -> Color(0xFF8B5CF6)    // Purple
  }
}

enum class QuizQuestionType {
  SHAPE_IDENTIFICATION, // Question Type A
  MATCHING_PAIRS        // Question Type B
}

data class ShapeOption(
  val glyph: String,
  val formType: FormType,
  val letterName: String,
  val isCorrect: Boolean
)

data class MatchingPairItem(
  val id: String,
  val isolatedLetter: String,
  val connectedGlyph: String,
  val formType: FormType,
  val letterName: String
)

data class ShapeQuizQuestion(
  val id: String,
  val type: QuizQuestionType,
  val targetLetter: ArabicLetter,
  val targetFormType: FormType,
  val promptEn: String,
  val promptBn: String,
  val promptAr: String,
  val options: List<ShapeOption> = emptyList(),
  val pairs: List<MatchingPairItem> = emptyList(),
  val explanationEn: String,
  val explanationBn: String,
  val explanationAr: String
) {
  fun getPrompt(lang: String, showHints: Boolean = true): String {
    // The main question title MUST ALWAYS clearly explicitly state which form to find,
    // regardless of whether the hint setting is ON or OFF.
    return when (lang) {
      "EN" -> promptEn
      "AR" -> promptAr
      else -> promptBn
    }
  }

  fun getExplanation(lang: String): String = when (lang) {
    "EN" -> explanationEn
    "AR" -> explanationAr
    else -> explanationBn
  }
}

data class RoadmapLevel(
  val id: Int,
  val titleEn: String,
  val titleBn: String,
  val titleAr: String,
  val subtitleEn: String,
  val subtitleBn: String,
  val subtitleAr: String,
  val iconEmoji: String,
  val nodeColor: Color,
  val isCheckpoint: Boolean = false,
  val rewardStars: Int = 30,
  val rewardXp: Int = 75,
  val questions: List<ShapeQuizQuestion>
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
}

data class LevelProgress(
  val levelId: Int,
  val isUnlocked: Boolean,
  val stars: Int, // 0 to 3
  val highScore: Int
)

enum class QuranStageId {
  STAGE_1_LETTERS_SHAPES,
  STAGE_2_HARAKAT_TANWEEN,
  STAGE_3_SUKOON_MAD,
  STAGE_4_WORD_BUILDING,
  STAGE_5_FULL_VERSE
}

data class QuranPedagogyStage(
  val id: QuranStageId,
  val stageNumber: Int,
  val titleEn: String,
  val titleBn: String,
  val titleAr: String,
  val subtitleEn: String,
  val subtitleBn: String,
  val subtitleAr: String,
  val iconEmoji: String,
  val color: Color,
  val levelIds: List<Int>
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
}
