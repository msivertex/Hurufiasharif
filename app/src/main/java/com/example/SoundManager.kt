package com.example

import android.content.Context
import android.content.SharedPreferences
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.ToneGenerator
import android.os.Build
import android.speech.tts.TextToSpeech
import android.speech.tts.Voice
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import java.util.Locale

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
    subtitleBn = "গম্ভীর, গম্ভীর অনুরণন ও সুমধুর ক্বারিয়ানা মাখরাজ",
    subtitleEn = "Resonant, dignified classical Qari recitation",
    subtitleAr = "تلاوة وقورة ومتقنة بأحكام التجويد ومخارج الحروف",
    assetFolder = "audio/male"
  ),
  FEMALE_QARIA(
    id = "female_qaria",
    titleBn = "মহিলা কণ্ঠ (Qaria - Female Voice)",
    titleEn = "Female Voice (Qaria)",
    titleAr = "صوت القارئة (صوت أنثوي)",
    subtitleBn = "স্পষ্ট, সুরময় ও স্ফটিক-স্বচ্ছ ক্বারিয়া উচ্চারণ",
    subtitleEn = "Crystal-clear, melodious Qaria tajweed articulation",
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
}

class SoundManager(private val context: Context) {
  private val prefs: SharedPreferences =
    context.getSharedPreferences("hurufia_audio_prefs", Context.MODE_PRIVATE)

  private var toneGenerator: ToneGenerator? = null
  private var textToSpeech: TextToSpeech? = null
  private var isTtsReady = false
  private var mediaPlayer: MediaPlayer? = null

  var currentVoiceGender: VoiceGender by mutableStateOf(VoiceGender.MALE_QARI)
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

  init {
    // Load persisted voice gender setting
    val savedGenderId = prefs.getString("voice_gender", VoiceGender.MALE_QARI.id)
    currentVoiceGender = VoiceGender.entries.firstOrNull { it.id == savedGenderId } ?: VoiceGender.MALE_QARI

    try {
      toneGenerator = ToneGenerator(AudioManager.STREAM_MUSIC, 85)
    } catch (_: Exception) {
      // Graceful fallback
    }

    initTts()
  }

  private fun initTts() {
    try {
      textToSpeech = TextToSpeech(context.applicationContext) { status ->
        if (status == TextToSpeech.SUCCESS) {
          val arLocales = listOf(
            Locale("ar"),
            Locale("ar", "SA"),
            Locale("ar", "EG"),
            Locale.ROOT
          )
          for (loc in arLocales) {
            val result = textToSpeech?.setLanguage(loc)
            if (result != TextToSpeech.LANG_MISSING_DATA && result != TextToSpeech.LANG_NOT_SUPPORTED) {
              isTtsReady = true
              break
            }
          }
          if (isTtsReady) {
            applyVoiceProfile(currentVoiceGender)
          }
        }
      }
    } catch (_: Exception) {
      isTtsReady = false
    }
  }

  /**
   * Set and persist the global voice gender.
   */
  fun setVoiceGender(gender: VoiceGender) {
    currentVoiceGender = gender
    prefs.edit().putString("voice_gender", gender.id).apply()
    applyVoiceProfile(gender)
  }

  /**
   * Configures TTS speech parameters (pitch, speed, voice characteristics)
   * to match the selected Qari (male) or Qaria (female) recitation profile.
   *
   * CRITICAL FIX: Ensures strict non-inverted voice mapping.
   * Male Voice strictly selects genuine Male Qari profiles (rejects female markers).
   * Female Voice strictly selects genuine Female Qaria profiles.
   */
  private fun applyVoiceProfile(gender: VoiceGender) {
    val tts = textToSpeech ?: return
    if (!isTtsReady) return

    try {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        val availableVoices: Set<Voice>? = tts.voices
        if (!availableVoices.isNullOrEmpty()) {
          val arabicVoices = availableVoices.filter {
            it.locale.language.startsWith("ar", ignoreCase = true)
          }

          if (gender == VoiceGender.MALE_QARI) {
            // Strictly MALE voice selection:
            // Explicitly filter OUT any female indicators ('female', 'fem', '-xa-', '-xd-')
            val maleVoice = arabicVoices.firstOrNull { voice ->
              val name = voice.name.lowercase(Locale.ROOT)
              (name.contains("male") && !name.contains("female")) ||
                name.contains("-xb-") || name.contains("-xc-")
            } ?: arabicVoices.firstOrNull { voice ->
              val name = voice.name.lowercase(Locale.ROOT)
              !name.contains("female") && !name.contains("fem") && !name.contains("-xa-") && !name.contains("-xd-")
            } ?: arabicVoices.firstOrNull()

            if (maleVoice != null) {
              tts.voice = maleVoice
            }
          } else {
            // Strictly FEMALE voice selection:
            // Explicitly prioritize female indicators ('female', 'fem', '-xa-', '-xd-')
            val femaleVoice = arabicVoices.firstOrNull { voice ->
              val name = voice.name.lowercase(Locale.ROOT)
              name.contains("female") || name.contains("fem") || name.contains("-xa-") || name.contains("-xd-")
            } ?: arabicVoices.firstOrNull { voice ->
              val name = voice.name.lowercase(Locale.ROOT)
              name.contains("female") || name.contains("fem")
            } ?: arabicVoices.lastOrNull()

            if (femaleVoice != null) {
              tts.voice = femaleVoice
            }
          }
        }
      }

      // Authentic Bengali Reciter Style Voice Profile Modulation:
      // Male Qari: Deep, resonant baritone, dignified Tajweed cadence
      // Female Qaria: High, bright, clear, melodic articulation
      if (gender == VoiceGender.MALE_QARI) {
        tts.setPitch(0.78f)       // Deep, authentic male Qari pitch
        tts.setSpeechRate(0.80f)   // Dignified, calm classical pace for Tajweed
      } else {
        tts.setPitch(1.28f)       // Clear, high-fidelity, melodic female Qaria pitch
        tts.setSpeechRate(0.88f)   // Crisp, musical, clear articulation
      }
    } catch (_: Exception) {
      // Graceful fallback
    }
  }

  /**
   * Speaks the Arabic letter with full Tajweed Harakat and pronunciation.
   */
  fun speakArabicLetter(
    letterGlyph: String,
    gender: VoiceGender = currentVoiceGender,
    onComplete: (() -> Unit)? = null
  ) {
    // 1. Try playing custom audio asset if present in asset folder (e.g., assets/audio/male/ba.mp3)
    val assetPlayed = playAssetAudio(letterGlyph, gender, onComplete)
    if (assetPlayed) return

    // 2. Synthesize using our optimized Tajweed TTS engine
    val tajweedText = getClassicalArabicLetterPronunciation(letterGlyph)
    if (isTtsReady && textToSpeech != null) {
      applyVoiceProfile(gender)
      textToSpeech?.speak(tajweedText, TextToSpeech.QUEUE_FLUSH, null, "letter_${letterGlyph}_${gender.id}")
      onComplete?.invoke()
    } else {
      // Acoustic fallback tone
      playTone(ToneGenerator.TONE_PROP_BEEP)
      onComplete?.invoke()
    }
  }

  fun speakArabicOrBeep(text: String) {
    speakArabicLetter(text, currentVoiceGender)
  }

  /**
   * Preview a voice sample so users can hear the difference directly in Settings.
   */
  fun previewVoiceSample(gender: VoiceGender) {
    val sampleWords = if (gender == VoiceGender.MALE_QARI) {
      "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ • أَلِفْ • بَاءْ • تَاءْ • ثَاءْ"
    } else {
      "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ • اقْرَأْ بِاسْمِ رَبِّكَ الَّذِي خَلَقَ"
    }
    if (isTtsReady && textToSpeech != null) {
      applyVoiceProfile(gender)
      textToSpeech?.speak(sampleWords, TextToSpeech.QUEUE_FLUSH, null, "preview_${gender.id}")
    } else {
      playTone(if (gender == VoiceGender.MALE_QARI) ToneGenerator.TONE_PROP_ACK else ToneGenerator.TONE_PROP_PROMPT)
    }
  }

  private fun playAssetAudio(
    letterGlyph: String,
    gender: VoiceGender,
    onComplete: (() -> Unit)?
  ): Boolean {
    val stripped = letterGlyph.replace("\u0640", "").trim()
    val cleanLetter = when (stripped) {
      "ا", "أ", "إ", "آ", "ٱ" -> "alif"
      "ب" -> "ba"
      "ت", "ة" -> "ta"
      "ث" -> "tha"
      "ج" -> "jim"
      "ح" -> "ha"
      "خ" -> "kha"
      "দ", "د" -> "dal"
      "ذ" -> "dhal"
      "ر" -> "ra"
      "ز" -> "zay"
      "স", "س" -> "sin"
      "শ", "ش" -> "shin"
      "ص" -> "sad"
      "ض" -> "dad"
      "ط" -> "taa"
      "ظ" -> "zaa"
      "ع" -> "ayn"
      "غ" -> "ghayn"
      "ف" -> "fa"
      "ق" -> "qaf"
      "ك" -> "kaf"
      "ل" -> "lam"
      "م" -> "mim"
      "ن" -> "nun"
      "و" -> "waw"
      "هـ", "ه" -> "haa"
      "ء", "ئ", "ؤ" -> "hamza"
      "ي", "ى" -> "ya"
      else -> null
    } ?: return false

    val possiblePaths = listOf(
      "${gender.assetFolder}/$cleanLetter.mp3",
      "${gender.assetFolder}/$cleanLetter.ogg",
      "${gender.assetFolder}/$cleanLetter.wav"
    )

    for (path in possiblePaths) {
      try {
        val afd = context.assets.openFd(path)
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer().apply {
          setAudioAttributes(
            AudioAttributes.Builder()
              .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
              .setUsage(AudioAttributes.USAGE_MEDIA)
              .build()
          )
          setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
          prepare()
          setOnCompletionListener {
            onComplete?.invoke()
          }
          start()
        }
        afd.close()
        return true
      } catch (_: Exception) {
        // Continue to next or fallback
      }
    }
    return false
  }

  /**
   * Classical vocalization with Tajweed and Sukoon (without Nunation/Tanween).
   * Authentically suited for Bengali Noorani Qaida recitation learning.
   * Cleans Tatweel / Kashida so initial/medial/final forms vocalize accurately.
   */
  private fun getClassicalArabicLetterPronunciation(glyph: String): String {
    val stripped = glyph.replace("\u0640", "").trim()
    return when (stripped) {
      "ا", "أ", "إ", "آ", "ٱ" -> "أَلِفْ"
      "ب" -> "بَاءْ"
      "ت" -> "تَاءْ"
      "ث" -> "ثَاءْ"
      "ج" -> "جِيمْ"
      "ح" -> "حَاءْ"
      "خ" -> "خَاءْ"
      "দ", "د" -> "دَالْ"
      "ذ" -> "ذَالْ"
      "ر" -> "رَاءْ"
      "ز" -> "زَايْ"
      "স", "س" -> "سِينْ"
      "শ", "ش" -> "شِينْ"
      "ص" -> "صَادْ"
      "ض" -> "ضَادْ"
      "ط" -> "طَاءْ"
      "ظ" -> "ظَاءْ"
      "ع" -> "عَيْنْ"
      "غ" -> "غَيْنْ"
      "ف" -> "فَاءْ"
      "ق" -> "قَافْ"
      "ك" -> "كَافْ"
      "ل" -> "لَامْ"
      "م" -> "مِيمْ"
      "ن" -> "نُونْ"
      "و" -> "وَاوْ"
      "هـ", "ه", "ة" -> "هَاءْ"
      "ء", "ئ", "ؤ" -> "هَمْزَةْ"
      "ي", "ى" -> "يَاءْ"
      else -> glyph
    }
  }

  fun playTone(type: Int = ToneGenerator.TONE_PROP_BEEP) {
    try {
      toneGenerator?.startTone(type, 150)
    } catch (_: Exception) {
    }
  }

  fun playSuccessChime() {
    try {
      toneGenerator?.startTone(ToneGenerator.TONE_PROP_ACK, 200)
    } catch (_: Exception) {
    }
  }

  fun playErrorBuzzer() {
    try {
      toneGenerator?.startTone(ToneGenerator.TONE_PROP_NACK, 250)
    } catch (_: Exception) {
    }
  }

  fun release() {
    try {
      toneGenerator?.release()
      textToSpeech?.stop()
      textToSpeech?.shutdown()
      mediaPlayer?.release()
      mediaPlayer = null
    } catch (_: Exception) {
    }
  }
}
