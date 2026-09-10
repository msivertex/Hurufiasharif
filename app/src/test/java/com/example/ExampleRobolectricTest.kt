package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("HURUFIA SHARIF", appName)
  }

  @Test
  fun `verify app strings in all three languages`() {
    // English
    assertEquals("HURUFIA SHARIF", AppStrings.getAppName("EN"))
    assertEquals("Welcome Back", AppStrings.getLoginTitle("EN"))
    assertEquals("Enter Email", AppStrings.getEmailHint("EN"))
    assertEquals("Enter Password", AppStrings.getPasswordHint("EN"))
    assertEquals("Login / Register", AppStrings.getSubmitBtn("EN"))

    // Arabic
    assertEquals("حروفية شريف", AppStrings.getAppName("AR"))
    assertEquals("أهلاً بك", AppStrings.getLoginTitle("AR"))
    assertEquals("أدخل البريد الإلكتروني", AppStrings.getEmailHint("AR"))
    assertEquals("أدخل كلمة المرور", AppStrings.getPasswordHint("AR"))
    assertEquals("تسجيل الدخول", AppStrings.getSubmitBtn("AR"))

    // Bengali (Default)
    assertEquals("হরুফিয়া শরিফ", AppStrings.getAppName("BN"))
    assertEquals("স্বাগতম", AppStrings.getLoginTitle("BN"))
    assertEquals("ইমেইল লিখুন", AppStrings.getEmailHint("BN"))
    assertEquals("পাসওয়ার্ড লিখুন", AppStrings.getPasswordHint("BN"))
    assertEquals("লগইন / রেজিস্ট্রেশন", AppStrings.getSubmitBtn("BN"))

    // Fallback default to Bengali
    assertEquals("হরুফিয়া শরিফ", AppStrings.getAppName("UNKNOWN"))
  }

  @Test
  fun `verify arabic alphabet repository contains 29 letters with 4 forms`() {
    val letters = ArabicAlphabetRepository.letters
    assertEquals(29, letters.size)

    val first = letters.first()
    assertEquals(1, first.id)
    assertEquals("ا", first.letter)
    assertEquals("Alif", first.nameEn)
    assertEquals("আলিফ", first.nameBn)
    assertEquals("ألف", first.nameAr)

    // Verify all 4 game modes exist
    assertEquals(4, GameRepository.gameModes.size)
  }

  @Test
  fun `verify shape master roadmap levels and questions`() {
    val levels = ShapeMasterRoadmapRepository.levels
    assertEquals(10, levels.size)

    // Level 1: Alif & Baa
    val lvl1 = levels[0]
    assertEquals(1, lvl1.id)
    assertEquals(3, lvl1.questions.size)

    val q1 = lvl1.questions[0]
    assertEquals(QuizQuestionType.SHAPE_IDENTIFICATION, q1.type)
    assertEquals(4, q1.options.size)
    assertEquals(true, q1.options.any { it.isCorrect })

    val q3 = lvl1.questions[2]
    assertEquals(QuizQuestionType.MATCHING_PAIRS, q3.type)
    assertEquals(3, q3.pairs.size)

    // Level 3 Checkpoint
    val lvl3 = levels[2]
    assertEquals(true, lvl3.isCheckpoint)
    assertEquals(50, lvl3.rewardStars)

    // Level 10 Final Crown
    val lvl10 = levels[9]
    assertEquals(true, lvl10.isCheckpoint)
    assertEquals(100, lvl10.rewardStars)
  }

  @Test
  fun `verify beginner and challenge hint mode behavior`() {
    val q = ShapeMasterRoadmapRepository.levels[0].questions[0]
    
    // Beginner Mode (Hints ON): includes positional title in prompt
    val beginnerPromptBn = q.getPrompt("BN", showHints = true)
    val beginnerPromptEn = q.getPrompt("EN", showHints = true)
    assert(beginnerPromptBn.contains("রূপ") || beginnerPromptBn.contains("চিহ্নিত"))
    assert(beginnerPromptEn.contains("Form") || beginnerPromptEn.contains("form"))

    // Challenge Mode (Hints OFF): positional text is replaced with generic shape identification prompt
    val challengePromptBn = q.getPrompt("BN", showHints = false)
    val challengePromptEn = q.getPrompt("EN", showHints = false)
    assertEquals("'${q.targetLetter.nameBn}' (${q.targetLetter.letter}) হরফের জন্য লক্ষ্য আকৃতিটি নির্বাচন করুন", challengePromptBn)
    assertEquals("Identify the target Arabic shape for '${q.targetLetter.nameEn}' (${q.targetLetter.letter})", challengePromptEn)

    // FormType positional hints
    assertEquals("(শুরুতে)", FormType.INITIAL.getPositionalHint("BN"))
    assertEquals("(মাঝে)", FormType.MEDIAL.getPositionalHint("BN"))
    assertEquals("(শেষে)", FormType.FINAL.getPositionalHint("BN"))
    assertEquals("(বিচ্ছিন্ন)", FormType.ISOLATED.getPositionalHint("BN"))

    // Visual schematics for Challenge Mode
    assertEquals("●―", FormType.INITIAL.getVisualSchematic())
    assertEquals("―●―", FormType.MEDIAL.getVisualSchematic())
    assertEquals("―●", FormType.FINAL.getVisualSchematic())
    assertEquals("○", FormType.ISOLATED.getVisualSchematic())
  }
}

