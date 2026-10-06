package com.example

import android.content.Context
import android.content.SharedPreferences
import android.media.ToneGenerator
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.audio.AudioEngine
import com.example.audio.VoiceProfile

/**
 * Global Voice Gender for recitation audio.
 */
enum class VoiceGender(
  val id: String,
  val titleBn: String,
  val titleEn: String,
  val titleAr: String,
  val subtitleBn: String,
  val subtitleEn: String,
  val subtitleAr: String,
  val assetFolder: String
) {
  MALE_QARI(
    id = "male_qari",
    titleBn = "পুরুষ কণ্ঠ (Qari - Male Voice)",
    titleEn = "Male Voice (Qari)",
    titleAr = "صوت القارئ (صوت ذكوري)",
    subtitleBn = "গম্ভীর ও সুললিত ক্বারিয়ানা তিলাওয়াত, গভীর অনুরণন ও নিখুঁত তাজবীদ",
    subtitleEn = "Native TTS: ar-EG-ShakirNeural / Clean Baritone Qari",
    subtitleAr = "تلاوة وقورة نقية عميقة بأحكام التجويد ومخارج الحروف",
    assetFolder = "audio/male"
  ),
  FEMALE_QARIA(
    id = "female_qaria",
    titleBn = "মহিলা কণ্ঠ (Qaria - Female Voice)",
    titleEn = "Female Voice (Qaria)",
    titleAr = "صوت القارئة (صوت أنثوي)",
    subtitleBn = "কোমল, সুমধুর, স্ফটিক-স্বচ্ছ ক্বারিয়া উচ্চারণ (শিশুদের জন্য উপযোগী)",
    subtitleEn = "Soft, sweet, clear recitation style suitable for kids and smooth learning",
    subtitleAr = "نطق نقي عذب ومخارج واضحة ومخارج دقيقة",
    assetFolder = "audio/female"
  );

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

  fun toVoiceProfile(): VoiceProfile = when (this) {
    MALE_QARI -> VoiceProfile.MALE_QARI
    FEMALE_QARIA -> VoiceProfile.FEMALE_QARIA
  }

  companion object {
    fun fromVoiceProfile(profile: VoiceProfile): VoiceGender = when (profile) {
      VoiceProfile.MALE_QARI -> MALE_QARI
      VoiceProfile.FEMALE_QARIA -> FEMALE_QARIA
    }
  }
}

class SoundManager(private val context: Context) {
  companion object {
    @Volatile
    private var instance: SoundManager? = null

    fun getInstance(context: Context): SoundManager {
      return instance ?: synchronized(this) {
        instance ?: SoundManager(context.applicationContext).also { instance = it }
      }
    }
  }

  val audioEngine: AudioEngine = AudioEngine.getInstance(context)

  private val prefs: SharedPreferences =
    context.getSharedPreferences("hurufia_audio_prefs", Context.MODE_PRIVATE)

  var currentVoiceGender: VoiceGender by mutableStateOf(VoiceGender.fromVoiceProfile(audioEngine.activeVoice))
    private set

  // Global Beginner Hints setting for quizzes/games (persisted with key 'beginner_hints_enabled')
  var beginnerHintsEnabledState: Boolean by mutableStateOf(
    prefs.getBoolean("beginner_hints_enabled", true)
  )
    private set

  fun setBeginnerHintsEnabled(enabled: Boolean) {
    beginnerHintsEnabledState = enabled
    prefs.edit().putBoolean("beginner_hints_enabled", enabled).apply()
  }

  // Global Dark Mode theme setting (persisted with key 'dark_mode_enabled')
  var darkModeEnabledState: Boolean by mutableStateOf(
    prefs.getBoolean("dark_mode_enabled", false)
  )
    private set

  fun setDarkModeEnabled(enabled: Boolean) {
    darkModeEnabledState = enabled
    prefs.edit().putBoolean("dark_mode_enabled", enabled).apply()
  }

  init {
    // Load persisted voice gender setting
    val savedGenderId = prefs.getString("voice_gender", VoiceGender.MALE_QARI.id)
    val initialGender = VoiceGender.entries.firstOrNull { it.id == savedGenderId } ?: VoiceGender.MALE_QARI
    currentVoiceGender = initialGender
    audioEngine.setVoice(initialGender.toVoiceProfile())
  }

  /**
   * Set and persist the global voice gender.
   */
  fun setVoiceGender(gender: VoiceGender) {
    currentVoiceGender = gender
    prefs.edit().putString("voice_gender", gender.id).commit()
    audioEngine.setVoice(gender.toVoiceProfile())
  }

  /**
   * Speaks the Arabic letter with full Tajweed Harakat and pronunciation
   * using the dual-voice audio engine.
   */
  fun speakArabicLetter(
    letterGlyph: String,
    gender: VoiceGender = currentVoiceGender,
    onComplete: (() -> Unit)? = null
  ) {
    audioEngine.playArabicLetter(letterGlyph, gender.toVoiceProfile(), onComplete)
  }

  fun speakArabicOrBeep(text: String) {
    speakArabicLetter(text, currentVoiceGender)
  }

  /**
   * Plays dynamic progressive Arabic cultural praise voiceover across the 20 questions:
   * Q1-4: Marhaba (মারহাবা)
   * Q5-8: BarakAllah Fik (বারাকাল্লাহু ফিক)
   * Q9-12: MashaAllah (মাশাল্লাহ)
   * Q13-16: JazakAllah Khair (জাযাকাল্লাহু খাইরান)
   * Q17-20: Muntazun Jiddan (মুন্তাজুন জিদ্দান)
   */
  fun playArabicPraiseForQuestion(
    questionNumber: Int,
    gender: VoiceGender = currentVoiceGender,
    onComplete: (() -> Unit)? = null
  ) {
    val praiseArabic = when (questionNumber) {
      in 1..4 -> "مَرْحَبًا"
      in 5..8 -> "بَارَكَ اللَّهُ فِيكَ"
      in 9..12 -> "مَا شَاءَ اللَّهُ"
      in 13..16 -> "جَزَاكَ اللَّهُ خَيْرًا"
      else -> "مُمْتَازٌ جِدًّا"
    }
    audioEngine.playArabicText(praiseArabic, gender.toVoiceProfile(), onComplete)
  }

  fun playArabicText(
    arabicText: String,
    gender: VoiceGender = currentVoiceGender,
    onComplete: (() -> Unit)? = null
  ) {
    audioEngine.playArabicText(arabicText, gender.toVoiceProfile(), onComplete)
  }

  /**
   * Preview a voice sample so users can hear the difference directly in Settings.
   */
  fun previewVoiceSample(gender: VoiceGender) {
    audioEngine.previewVoice(gender.toVoiceProfile())
  }

  fun playTone(type: Int = ToneGenerator.TONE_PROP_BEEP) {
    audioEngine.playTone(type)
  }

  fun playSuccessChime() {
    audioEngine.playSuccessChime()
  }

  fun playErrorBuzzer() {
    audioEngine.playErrorBuzzer()
  }

  fun playOptionSelect() {
    audioEngine.playTone(ToneGenerator.TONE_PROP_BEEP)
  }

  fun stopAudio() {
    audioEngine.stop()
  }

  fun release() {
    audioEngine.release()
  }
}

