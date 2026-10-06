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
  fun `verify MainActivity launches and initializes without crash`() {
    val controller = org.robolectric.Robolectric.buildActivity(MainActivity::class.java).setup()
    val activity = controller.get()
    assert(activity != null)
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

    // Verify all 5 game modes exist
    assertEquals(5, GameRepository.gameModes.size)
  }

  @Test
  fun `verify shape master roadmap levels and questions`() {
    val levels = ShapeMasterRoadmapRepository.levels
    assertEquals(12, levels.size)

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

    // Level 12 Final Grand Checkpoint
    val lvl12 = levels.last()
    assertEquals(true, lvl12.isCheckpoint)
    assertEquals(150, lvl12.rewardStars)
  }

  @Test
  fun `verify beginner and challenge hint mode behavior`() {
    val q = ShapeMasterRoadmapRepository.levels[0].questions[0]
    
    val promptBn = q.getPrompt("BN", showHints = true)
    val promptEn = q.getPrompt("EN", showHints = true)
    assert(promptBn.contains("রূপ") || promptBn.contains("বা"))
    assert(promptEn.contains("Initial") || promptEn.contains("form") || promptEn.contains("Baa"))

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

  @Test
  fun `verify kaida education repository contains 9 foundational chapters with digital book pages`() {
    val chapters = KaidaEducationRepository.chapters
    assertEquals(9, chapters.size)

    // Chapter 1: Harakat
    val ch1 = chapters[0]
    assertEquals(1, ch1.id)
    assertEquals("হরকত (যবর, যের, পেশ)", ch1.titleBn)
    assert(ch1.pages.isNotEmpty())

    // Chapter 5: Madd Rules
    val ch5 = chapters[4]
    assertEquals(5, ch5.id)
    assertEquals("মাদ্দ এর নিয়ম (টেনে পড়া)", ch5.titleBn)
    assertEquals(TajweedType.MADD, ch5.primaryTajweedRule)

    // Chapter 6: Ghunnah & Ikhfa
    val ch6 = chapters[5]
    assertEquals(6, ch6.id)
    assertEquals(TajweedType.GHUNNAH_IKHFA, ch6.primaryTajweedRule)

    // Chapter 7: Qalqalah
    val ch7 = chapters[6]
    assertEquals(7, ch7.id)
    assertEquals(TajweedType.QALQALAH, ch7.primaryTajweedRule)

    // Chapter 9: Short Surahs
    val ch9 = chapters[8]
    assertEquals(9, ch9.id)
    assert(ch9.pages.size >= 2)
  }

  @Test
  fun `verify ampara surahs repository contains surahs with ayahs and tajweed word tokens`() {
    val surahs = AmparaSurahRepository.surahs
    assert(surahs.isNotEmpty())

    // Surah Al-Fatihah
    val fatihah = surahs.first { it.number == 1 }
    assertEquals("Al-Fatihah", fatihah.nameEnglish)
    assertEquals(7, fatihah.totalVerses)
    assertEquals(7, fatihah.ayahs.size)
    assertEquals(RevelationType.MAKKI, fatihah.revelationType)
    assert(fatihah.ayahs.first().words.isNotEmpty())

    // Surah Al-Ikhlas
    val ikhlas = surahs.first { it.number == 112 }
    assertEquals("Al-Ikhlas", ikhlas.nameEnglish)
    assertEquals(4, ikhlas.totalVerses)
    assert(ikhlas.ayahs.first().words.any { it.tajweedType == TajweedType.QALQALAH })

    // Surah An-Nas
    val nas = surahs.first { it.number == 114 }
    assertEquals(6, nas.totalVerses)
    assert(nas.ayahs.first().words.any { it.tajweedType == TajweedType.GHUNNAH_IKHFA })
  }

  @Test
  fun `verify bangla date calculation produces valid year month and season`() {
    val cal = java.util.Calendar.getInstance()
    val banglaDate = BanglaDateCalculator.calculateBanglaDate(cal)
    assert(banglaDate.day in 1..31)
    assert(banglaDate.year >= 1430)
    assert(banglaDate.monthNameBn.isNotEmpty())
    assert(banglaDate.seasonNameBn.isNotEmpty())
  }

  @Test
  fun `verify hijri calendar calculation produces valid date and moon phase`() {
    val cal = java.util.Calendar.getInstance()
    val hijriDate = com.example.islamic.HijriCalendarCalculator.calculateHijri(cal, dayAdjustment = 0)
    assert(hijriDate.day in 1..30)
    assert(hijriDate.month in 1..12)
    assert(hijriDate.year >= 1445)
    assert(hijriDate.monthNameBn.isNotEmpty())
    assert(hijriDate.monthNameAr.isNotEmpty())
    assert(hijriDate.moonPhaseEmoji.isNotEmpty())
  }
}

