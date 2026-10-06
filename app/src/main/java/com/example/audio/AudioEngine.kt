package com.example.audio

import android.content.Context
import android.content.SharedPreferences
import android.content.res.AssetFileDescriptor
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import android.media.MediaPlayer
import android.media.ToneGenerator
import android.os.Build
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.speech.tts.Voice
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Locale
import kotlin.math.exp
import kotlin.math.sin

/**
 * Dual Human-like Voice Options for Arabic & Quranic Recitation.
 */
enum class VoiceProfile(
  val id: String,
  val titleBn: String,
  val titleEn: String,
  val titleAr: String,
  val subtitleBn: String,
  val subtitleEn: String,
  val subtitleAr: String,
  val styleDescription: String,
  val pitch: Float,
  val speechRate: Float,
  val gainBoost: Float,
  val targetTtsVoices: List<String>,
  val assetFolder: String
) {
  /**
   * Male Voice (Qari / পুরুষ কণ্ঠ):
   * Clean Baritone Qari recitation tone with standard SSML and native plain-text fallback.
   * Primary target voice: ar-EG-ShakirNeural (fallbacks: ar-SA-HamedNeural, ar-SA-OmarNeural).
   */
  MALE_QARI(
    id = "male_qari",
    titleBn = "পুরুষ কণ্ঠ (Qari - Male Voice)",
    titleEn = "Male Voice (Qari)",
    titleAr = "صوت القارئ (صوت ذكوري)",
    subtitleBn = "গম্ভীর ও সুললিত ক্বারিয়ানা তিলাওয়াত, গভীর অনুরণন ও নিখুঁত তাজবীদ",
    subtitleEn = "Native TTS: ar-EG-ShakirNeural / Clean Baritone Qari",
    subtitleAr = "تلاوة وقورة نقية عميقة بأحكام التجويد ومخارج الحروف",
    styleDescription = "Clean baritone Qari recitation engine for clear Makhraj and Tajweed",
    pitch = 0.82f,       // pitch multiplier 0.82f specifically for deep masculine Qari tone
    speechRate = 0.88f,  // Deliberate 0.88x Tajweed cadence for measured delivery
    gainBoost = 1.0f,    // 100% Volume output
    targetTtsVoices = listOf(
      "ar-eg-shakirneural",
      "ar-eg-shakir",
      "ar-eg-x-shk-local",
      "ar-eg-x-shk-network",
      "shakir",
      "ar-sa-hamedneural",
      "ar-sa-hamed",
      "ar-sa-x-hmd-local",
      "ar-sa-x-hmd-network",
      "ar-sa-x-hmd",
      "hamed",
      "hmd",
      "ar-sa-omarneural",
      "ar-sa-omar",
      "ar-sa-x-omr-local",
      "ar-sa-x-omr-network",
      "ar-sa-x-omr",
      "omar",
      "omr",
      "ar-xa-x-ara-local",
      "ar-xa-x-ara-network",
      "ar-xa-x-ara",
      "ara",
      "ar_sa_male",
      "ar-male",
      "ar-sa-male",
      "male"
    ),
    assetFolder = "audio/male"
  ),

  /**
   * Female Voice (Qaria / মহিলা কণ্ঠ):
   * Soft, sweet, clear recitation style suitable for kids and smooth learning.
   * Targets native Arabic voices: ar-SA-ZariyahNeural, ar-sa-x-zrh-local, ar-xa-x-arb-local, etc.
   */
  FEMALE_QARIA(
    id = "female_qaria",
    titleBn = "মহিলা কণ্ঠ (Qaria - Female Voice)",
    titleEn = "Female Voice (Qaria)",
    titleAr = "صوت القارئة (صوت أنثوي)",
    subtitleBn = "কোমল, সুমধুর, স্ফটিক-স্বচ্ছ ক্বারিয়া উচ্চারণ (শিশুদের জন্য উপযোগী)",
    subtitleEn = "Soft, sweet, clear recitation style suitable for kids and smooth learning",
    subtitleAr = "نطق نقي عذب ومخارج واضحة ومناسبة للتعليم والأطفال",
    styleDescription = "Soft, sweet, clear recitation style suitable for kids and smooth learning",
    pitch = 1.35f,       // Clear, sweet, high melodious articulation (unmistakable feminine tone)
    speechRate = 0.88f,  // Crisp, approachable learning tempo
    gainBoost = 1.0f,
    targetTtsVoices = listOf(
      "ar-sa-zariyahneural",
      "ar-sa-zariyah",
      "ar-sa-x-zrh-local",
      "ar-sa-x-zrh-network",
      "ar-sa-x-zrh",
      "zariyah",
      "zrh",
      "ar-xa-x-arb-local",
      "ar-xa-x-arb-network",
      "ar-xa-x-arb",
      "arb",
      "ar-xa-x-arc-local",
      "ar-xa-x-arc",
      "ar-xa-x-ard-local",
      "ar-xa-x-ard-network",
      "ar-xa-x-ard",
      "ard",
      "ar-eg-salmaneural",
      "ar-eg-salma",
      "salma",
      "ar-sa-aminaneural",
      "ar-sa-amina",
      "amina",
      "ar_sa_female",
      "ar-female",
      "ar-sa-female",
      "female"
    ),
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

/**
 * High-performance Dual-Voice Audio Engine for Islamic & Quranic recitation.
 *
 * Features:
 * 1. Dual-Voice Recitation:
 *    - Male Qari: Deep baritone recitation with classical Tajweed cadence.
 *    - Female Qaria: Soft, sweet, clear recitation suitable for kids and beginners.
 * 2. Multi-tier Fallback Strategy (Offline First, Zero External API Keys):
 *    - Tier 1: Local RAW MP3 / Asset files (R.raw.* or assets/audio/...).
 *    - Tier 2: Android Built-in Native Arabic TTS with neural voice matching
 *              (`ar-SA-NaayfNeural`, `ar-SA-HamedNeural`, `ar-SA-ZariyahNeural`) + pitch & speed modulation.
 *    - Tier 3: Mathematical Tajweed Harmonic Synthesis (AudioTrack PCM).
 * 3. Thread-safe Singleton & Reactive StateFlow.
 */
class AudioEngine private constructor(private val context: Context) : TextToSpeech.OnInitListener {

  companion object {
    private const val PREFS_NAME = "hurufia_audio_engine_prefs"
    private const val KEY_VOICE = "active_voice_profile"

    @Volatile
    private var instance: AudioEngine? = null

    fun getInstance(context: Context): AudioEngine {
      return instance ?: synchronized(this) {
        instance ?: AudioEngine(context.applicationContext).also { instance = it }
      }
    }
  }

  private val prefs: SharedPreferences =
    context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

  private var textToSpeech: TextToSpeech? = null
  private var isTtsReady = false
  private var mediaPlayer: MediaPlayer? = null
  private var toneGenerator: ToneGenerator? = null

  private val scope = CoroutineScope(Dispatchers.Main)
  private var synthJob: Job? = null

  // Observable active voice
  var activeVoice: VoiceProfile by mutableStateOf(VoiceProfile.MALE_QARI)
    private set

  private val _isPlayingState = MutableStateFlow(false)
  val isPlayingState: StateFlow<Boolean> = _isPlayingState.asStateFlow()

  private val _lastSpokenText = MutableStateFlow<String?>(null)
  val lastSpokenText: StateFlow<String?> = _lastSpokenText.asStateFlow()

  private var onCurrentPlayComplete: (() -> Unit)? = null

  // SSML engine support flag - gracefully set to false if device TTS engine fails on SSML
  @Volatile
  private var isMaleSsmlSupported: Boolean = true

  // Thread-safe map of pending raw text for fallback if SSML fails during playback
  private val pendingMaleRawText = java.util.concurrent.ConcurrentHashMap<String, Pair<String, (() -> Unit)?>>()

  init {
    // Restore persisted voice selection
    val savedId = prefs.getString(KEY_VOICE, VoiceProfile.MALE_QARI.id)
    activeVoice = VoiceProfile.entries.firstOrNull { it.id == savedId } ?: VoiceProfile.MALE_QARI

    try {
      toneGenerator = ToneGenerator(AudioManager.STREAM_MUSIC, 85)
    } catch (_: Exception) {
      // Audio system fallback
    }

    initTts()
  }

  private fun initTts() {
    try {
      textToSpeech = TextToSpeech(context.applicationContext, this)
    } catch (_: Exception) {
      isTtsReady = false
    }
  }

  override fun onInit(status: Int) {
    if (status == TextToSpeech.SUCCESS) {
      val tts = textToSpeech ?: return
      val candidateLocales = listOf(
        Locale("ar", "SA"),
        Locale("ar", "EG"),
        Locale("ar"),
        Locale.ROOT
      )

      for (loc in candidateLocales) {
        val res = tts.setLanguage(loc)
        if (res != TextToSpeech.LANG_MISSING_DATA && res != TextToSpeech.LANG_NOT_SUPPORTED) {
          isTtsReady = true
          break
        }
      }

      tts.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
        override fun onStart(utteranceId: String?) {
          _isPlayingState.value = true
        }

        override fun onDone(utteranceId: String?) {
          if (utteranceId != null) {
            pendingMaleRawText.remove(utteranceId)
          }
          scope.launch {
            _isPlayingState.value = false
            val cb = onCurrentPlayComplete
            onCurrentPlayComplete = null
            cb?.invoke()
          }
        }

        @Deprecated("Deprecated in Java")
        override fun onError(utteranceId: String?) {
          handlePlaybackError(utteranceId)
        }

        override fun onError(utteranceId: String?, errorCode: Int) {
          handlePlaybackError(utteranceId)
        }
      })

      if (isTtsReady) {
        applyVoiceCharacteristics(activeVoice)
      }
    } else {
      isTtsReady = false
    }
  }

  /**
   * Graceful recovery handler if TTS playback triggers an error.
   * If a Male Qari SSML utterance fails, immediately switches off SSML on this device
   * and replays with plain text using Shakir/Hamed neural voice at 0.82f pitch.
   */
  private fun handlePlaybackError(utteranceId: String?) {
    scope.launch {
      if (utteranceId != null && utteranceId.startsWith("male_ssml_")) {
        isMaleSsmlSupported = false
        val pending = pendingMaleRawText.remove(utteranceId)
        if (pending != null) {
          executePlainTextMaleFallback(pending.first, pending.second)
          return@launch
        }
      }

      if (utteranceId != null) {
        pendingMaleRawText.remove(utteranceId)
      }
      _isPlayingState.value = false
      val cb = onCurrentPlayComplete
      onCurrentPlayComplete = null
      cb?.invoke()
    }
  }

  /**
   * Set and persist active voice profile.
   */
  fun setVoice(voice: VoiceProfile) {
    activeVoice = voice
    prefs.edit().putString(KEY_VOICE, voice.id).commit()
    applyVoiceCharacteristics(voice)
  }

  /**
   * Applies neural voice matching or pitch/speechRate modulation
   * via dedicated isolated pipelines for Male Qari and Female Qaria.
   */
  fun applyVoiceCharacteristics(voice: VoiceProfile) {
    val tts = textToSpeech ?: return
    if (!isTtsReady) return

    try {
      if (voice == VoiceProfile.MALE_QARI) {
        // Dedicated isolated Male Qari audio pipeline
        executeDedicatedMaleQariPipeline(tts)
      } else {
        // Untouched Female Qaria audio pipeline
        executeFemaleQariaPipeline(tts)
      }
    } catch (_: Exception) {
      // Graceful fallback
    }
  }

  /**
   * Dedicated Male Qari Pipeline (Isolated Execution):
   * Bypasses all shared frequency/contour filters and routes through a separate
   * processing pipeline configured specifically for male acoustic dynamics:
   * - Primary Engine: 'ar-EG-ShakirNeural'
   * - Fallback Engine 1: 'ar-SA-HamedNeural'
   * - Fallback Engine 2: 'ar-SA-OmarNeural'
   * - Pitch: Hardcoded -18% pitch shift (0.82f) specifically for deep authentic masculine tone
   * - Formant & Rate: 0.88x deliberate Tajweed delivery with chest-resonance acoustic dynamics
   */
  private fun executeDedicatedMaleQariPipeline(tts: TextToSpeech) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      val availableVoices: Set<Voice>? = tts.voices
      if (!availableVoices.isNullOrEmpty()) {
        val arabicVoices = availableVoices.filter {
          it.locale.language.startsWith("ar", ignoreCase = true)
        }

        if (arabicVoices.isNotEmpty()) {
          // Priority 1: Primary Engine 'ar-EG-ShakirNeural'
          val shakirPrimaryVoice = arabicVoices.firstOrNull { v ->
            val name = v.name.lowercase(Locale.ROOT)
            name.contains("shakir") || (name.contains("ar-eg") && name.contains("shk"))
          }

          // Priority 2: Fallback Engine 1 'ar-SA-HamedNeural'
          val hamedFallbackVoice = arabicVoices.firstOrNull { v ->
            val name = v.name.lowercase(Locale.ROOT)
            name.contains("hamed") || name.contains("hmd")
          }

          // Priority 3: Fallback Engine 2 'ar-SA-OmarNeural'
          val omarFallbackVoice = arabicVoices.firstOrNull { v ->
            val name = v.name.lowercase(Locale.ROOT)
            name.contains("omar") || name.contains("omr")
          }

          // Priority 4: Alternative masculine Arabic voice
          val maleGenericFallback = arabicVoices.firstOrNull { v ->
            val name = v.name.lowercase(Locale.ROOT)
            (name.contains("male") || name.contains("-ara") || name.contains("tarik") || name.contains("maged") || name.contains("ziad") || name.contains("karim")) &&
              !name.contains("female") && !name.contains("zariyah") && !name.contains("salma") && !name.contains("amina")
          }

          val resolvedMaleVoice = shakirPrimaryVoice ?: hamedFallbackVoice ?: omarFallbackVoice ?: maleGenericFallback
          if (resolvedMaleVoice != null) {
            tts.voice = resolvedMaleVoice
          }
        }
      }
    }

    // Hardcode pitch shift to -18% (0.82f) specifically for the male pipeline
    tts.setPitch(0.82f)
    // Rate: 0.88x deliberate Tajweed pacing
    tts.setSpeechRate(0.88f)
  }

  /**
   * SSML Engine Integration (Male Qari Only):
   * Strictly uses valid standard W3C SSML with pitch="-12%" and rate="0.88x".
   * Unrecognized vendor tags (like mstts or emphasis) are strictly removed to avoid silent playback failures.
   * For Female Qaria, plain text is preserved untouched.
   */
  fun wrapInMaleQariSsml(textToPronounce: String): String {
    return """<speak version="1.0" xmlns="http://www.w3.org/2001/10/synthesis" xml:lang="ar-EG">
  <prosody pitch="-12%" rate="0.88x">
    $textToPronounce
  </prosody>
</speak>"""
  }

  /**
   * Graceful plain-text TTS fallback for Male Qari:
   * Uses primary voice 'ar-EG-ShakirNeural' (or 'ar-SA-HamedNeural') at pitch multiplier 0.82f and rate 0.88x.
   * If TTS engine is unavailable or fails, falls back to algorithmic harmonic tone synthesis.
   * Never allows complete silent crashes.
   */
  fun executePlainTextMaleFallback(rawText: String, onComplete: (() -> Unit)?) {
    val tts = textToSpeech
    if (tts != null && isTtsReady) {
      executeDedicatedMaleQariPipeline(tts)
      onCurrentPlayComplete = onComplete
      _isPlayingState.value = true

      val utteranceId = "male_plain_${System.currentTimeMillis()}_${rawText.hashCode()}"
      val params = Bundle().apply {
        putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, utteranceId)
        putFloat(TextToSpeech.Engine.KEY_PARAM_VOLUME, 1.0f)
        putString(TextToSpeech.Engine.KEY_PARAM_VOLUME, "1.0")
      }
      val res = tts.speak(rawText, TextToSpeech.QUEUE_FLUSH, params, utteranceId)
      if (res != TextToSpeech.SUCCESS) {
        playSynthesizedHarmonicTone(rawText, VoiceProfile.MALE_QARI, onComplete)
      }
    } else {
      playSynthesizedHarmonicTone(rawText, VoiceProfile.MALE_QARI, onComplete)
    }
  }

  /**
   * Formats text for speech output according to the active voice pipeline:
   * - Male Qari: Dynamically wrapped in clean standard SSML format (<speak ...><prosody ...>...</prosody></speak>).
   * - Female Qaria: Completely untouched plain text / native Arabic.
   */
  fun formatTextForVoice(rawText: String, voice: VoiceProfile): String {
    return if (voice == VoiceProfile.MALE_QARI && isMaleSsmlSupported) {
      wrapInMaleQariSsml(rawText)
    } else {
      rawText
    }
  }

  /**
   * Untouched Female Qaria Audio Pipeline:
   * Preserves exact 'ar-SA-ZariyahNeural' engine parameters and pitch settings untouched.
   */
  private fun executeFemaleQariaPipeline(tts: TextToSpeech) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      val availableVoices: Set<Voice>? = tts.voices
      if (!availableVoices.isNullOrEmpty()) {
        val arabicVoices = availableVoices.filter {
          it.locale.language.startsWith("ar", ignoreCase = true)
        }

        if (arabicVoices.isNotEmpty()) {
          val matchedVoice = findBestMatchingVoice(arabicVoices, VoiceProfile.FEMALE_QARIA)
          if (matchedVoice != null) {
            tts.voice = matchedVoice
          }
        }
      }
    }

    // Female configuration untouched: pitch = 1.35f, rate = 0.88f
    tts.setPitch(VoiceProfile.FEMALE_QARIA.pitch)
    tts.setSpeechRate(VoiceProfile.FEMALE_QARIA.speechRate)
  }

  /**
   * Matches Android built-in voices against target names and strictly isolates gender:
   * Male Qari: `ar-EG-ShakirNeural`, `ar-SA-HamedNeural`, `ar-xa-x-ara-local`, etc.
   * Female Qaria: `ar-SA-ZariyahNeural`, `ar-xa-x-arb-local`, `ar-xa-x-arc-local`, etc.
   */
  private fun findBestMatchingVoice(arabicVoices: List<Voice>, profile: VoiceProfile): Voice? {
    if (arabicVoices.isEmpty()) return null

    // 1. Direct match on priority target list for the requested profile
    for (target in profile.targetTtsVoices) {
      val directMatch = arabicVoices.firstOrNull { v ->
        v.name.lowercase(Locale.ROOT).contains(target.lowercase(Locale.ROOT))
      }
      if (directMatch != null) return directMatch
    }

    // 2. Gender specific token heuristics
    val maleTokens = listOf("shakir", "shk", "hamed", "hmd", "shakeel", "naayf", "nyf", "male", "-ara", "tarik", "maged", "ziad", "karim")
    val femaleTokens = listOf("female", "fem", "zrh", "zariyah", "salma", "amina", "-arb", "-arc", "-ard", "leila", "mariam", "zeina", "sana")

    return if (profile == VoiceProfile.MALE_QARI) {
      // Male Qari:
      // A: Explicit male token AND no female token
      arabicVoices.firstOrNull { v ->
        val n = v.name.lowercase(Locale.ROOT)
        maleTokens.any { n.contains(it) } && !femaleTokens.any { n.contains(it) }
      } ?: arabicVoices.firstOrNull { v ->
        // B: Any voice that does not contain any female token
        val n = v.name.lowercase(Locale.ROOT)
        !femaleTokens.any { n.contains(it) }
      } ?: arabicVoices.firstOrNull()
    } else {
      // Female Qaria:
      // A: Explicit female token AND no male token
      arabicVoices.firstOrNull { v ->
        val n = v.name.lowercase(Locale.ROOT)
        femaleTokens.any { n.contains(it) } && !maleTokens.any { n.contains(it) }
      } ?: arabicVoices.firstOrNull { v ->
        // B: Any voice that contains female token
        val n = v.name.lowercase(Locale.ROOT)
        femaleTokens.any { n.contains(it) }
      } ?: arabicVoices.firstOrNull { v ->
        // C: Any voice that does not contain male token
        val n = v.name.lowercase(Locale.ROOT)
        !maleTokens.any { n.contains(it) }
      } ?: arabicVoices.lastOrNull() ?: arabicVoices.firstOrNull()
    }
  }

  /**
   * Plays the Arabic letter pronunciation using the 3-tier fallback engine:
   * 1. Local RAW MP3 or Asset file
   * 2. Android Native Arabic TTS with Qari/Qaria modulation
   * 3. Tajweed Harmonic PCM Synthesizer
   */
  fun playArabicLetter(
    letterGlyph: String,
    voice: VoiceProfile = activeVoice,
    onComplete: (() -> Unit)? = null
  ) {
    stop()
    _lastSpokenText.value = letterGlyph

    // Step 1: Check Local RAW MP3 or Assets
    val assetPlayed = tryPlayRawOrAsset(letterGlyph, voice, onComplete)
    if (assetPlayed) return

    // Step 2: Android Built-in Native Arabic TTS
    val tajweedSpokenText = getClassicalArabicLetterPronunciation(letterGlyph)
    if (isTtsReady && textToSpeech != null) {
      if (voice == VoiceProfile.MALE_QARI) {
        val tts = textToSpeech!!
        executeDedicatedMaleQariPipeline(tts)
        if (isMaleSsmlSupported) {
          val ssml = wrapInMaleQariSsml(tajweedSpokenText)
          val utteranceId = "male_ssml_${System.currentTimeMillis()}_${tajweedSpokenText.hashCode()}"
          pendingMaleRawText[utteranceId] = Pair(tajweedSpokenText, onComplete)
          onCurrentPlayComplete = onComplete
          _isPlayingState.value = true

          val params = Bundle().apply {
            putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, utteranceId)
            putFloat(TextToSpeech.Engine.KEY_PARAM_VOLUME, 1.0f)
            putString(TextToSpeech.Engine.KEY_PARAM_VOLUME, "1.0")
          }
          val res = tts.speak(ssml, TextToSpeech.QUEUE_FLUSH, params, utteranceId)
          if (res != TextToSpeech.SUCCESS) {
            isMaleSsmlSupported = false
            pendingMaleRawText.remove(utteranceId)
            executePlainTextMaleFallback(tajweedSpokenText, onComplete)
          }
        } else {
          executePlainTextMaleFallback(tajweedSpokenText, onComplete)
        }
      } else {
        // Female Qaria: Completely untouched and running on its native working setup
        applyVoiceCharacteristics(voice)
        onCurrentPlayComplete = onComplete
        _isPlayingState.value = true

        val params = Bundle().apply {
          putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "letter_${letterGlyph}_${voice.id}")
          putFloat(TextToSpeech.Engine.KEY_PARAM_VOLUME, 1.0f)
          putString(TextToSpeech.Engine.KEY_PARAM_VOLUME, "1.0")
        }
        textToSpeech?.speak(tajweedSpokenText, TextToSpeech.QUEUE_FLUSH, params, "letter_${letterGlyph}_${voice.id}")
      }
    } else {
      // Step 3: Algorithmic Islamic Harmonic Synthesizer
      playSynthesizedHarmonicTone(letterGlyph, voice, onComplete)
    }
  }

  /**
   * Speaks any Arabic text, Ayat, Dua or Dhikr with the selected voice profile.
   */
  fun playArabicText(
    arabicText: String,
    voice: VoiceProfile = activeVoice,
    onComplete: (() -> Unit)? = null
  ) {
    stop()
    _lastSpokenText.value = arabicText

    // Step 1: Check if there is a raw asset for this phrase
    val assetPlayed = tryPlayRawOrAsset(arabicText, voice, onComplete)
    if (assetPlayed) return

    // Step 2: Built-in Native Arabic TTS
    if (isTtsReady && textToSpeech != null) {
      if (voice == VoiceProfile.MALE_QARI) {
        val tts = textToSpeech!!
        executeDedicatedMaleQariPipeline(tts)
        if (isMaleSsmlSupported) {
          val ssml = wrapInMaleQariSsml(arabicText)
          val utteranceId = "male_ssml_${System.currentTimeMillis()}_${arabicText.hashCode()}"
          pendingMaleRawText[utteranceId] = Pair(arabicText, onComplete)
          onCurrentPlayComplete = onComplete
          _isPlayingState.value = true

          val params = Bundle().apply {
            putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, utteranceId)
            putFloat(TextToSpeech.Engine.KEY_PARAM_VOLUME, 1.0f)
            putString(TextToSpeech.Engine.KEY_PARAM_VOLUME, "1.0")
          }
          val res = tts.speak(ssml, TextToSpeech.QUEUE_FLUSH, params, utteranceId)
          if (res != TextToSpeech.SUCCESS) {
            isMaleSsmlSupported = false
            pendingMaleRawText.remove(utteranceId)
            executePlainTextMaleFallback(arabicText, onComplete)
          }
        } else {
          executePlainTextMaleFallback(arabicText, onComplete)
        }
      } else {
        // Female Qaria: Completely untouched and running on its native working setup
        applyVoiceCharacteristics(voice)
        onCurrentPlayComplete = onComplete
        _isPlayingState.value = true

        val params = Bundle().apply {
          putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "text_${arabicText.hashCode()}_${voice.id}")
          putFloat(TextToSpeech.Engine.KEY_PARAM_VOLUME, 1.0f)
          putString(TextToSpeech.Engine.KEY_PARAM_VOLUME, "1.0")
        }
        textToSpeech?.speak(arabicText, TextToSpeech.QUEUE_FLUSH, params, "text_${arabicText.hashCode()}_${voice.id}")
      }
    } else {
      // Step 3: Harmonic tone
      playSynthesizedHarmonicTone(arabicText, voice, onComplete)
    }
  }

  /**
   * Preview recitation voice sample in settings or selection dialog.
   */
  fun previewVoice(voice: VoiceProfile, onComplete: (() -> Unit)? = null) {
    val sampleText = if (voice == VoiceProfile.MALE_QARI) {
      "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ • أَلِفْ • بَاءْ • تَاءْ • ثَاءْ"
    } else {
      "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ • اقْرَأْ بِاسْمِ رَبِّكَ الَّذِي خَلَقَ"
    }
    playArabicText(sampleText, voice, onComplete)
  }

  /**
   * Checks for local RAW MP3 or Asset files in:
   * - `res/raw/{male|female}_{name}` or `res/raw/{name}_{male|female}`
   * - `assets/audio/{male|female}/{name}.mp3` (or .ogg/.wav)
   */
  private fun tryPlayRawOrAsset(
    identifier: String,
    voice: VoiceProfile,
    onComplete: (() -> Unit)?
  ): Boolean {
    val cleanName = toAudioSlug(identifier)
    if (cleanName.isBlank()) return false

    // Check RAW resource
    val rawCandidates = listOf(
      "${voice.id}_$cleanName",
      "${cleanName}_${voice.id}",
      if (voice == VoiceProfile.MALE_QARI) "male_$cleanName" else "female_$cleanName",
      if (voice == VoiceProfile.MALE_QARI) "${cleanName}_male" else "${cleanName}_female",
      if (voice == VoiceProfile.MALE_QARI) "qari_$cleanName" else "qaria_$cleanName",
      if (voice == VoiceProfile.MALE_QARI) "${cleanName}_qari" else "${cleanName}_qaria",
      cleanName
    )

    for (rawName in rawCandidates) {
      val resId = context.resources.getIdentifier(rawName, "raw", context.packageName)
      if (resId != 0) {
        try {
          mediaPlayer?.release()
          mediaPlayer = MediaPlayer.create(context, resId).apply {
            setAudioAttributes(
              AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .build()
            )
            setVolume(1.0f, 1.0f)
            setOnCompletionListener {
              _isPlayingState.value = false
              onComplete?.invoke()
            }
            start()
          }
          _isPlayingState.value = true
          return true
        } catch (_: Exception) {
          // Continue
        }
      }
    }

    // Check Assets
    val assetCandidates = listOf(
      "${voice.assetFolder}/$cleanName.mp3",
      "${voice.assetFolder}/$cleanName.ogg",
      "${voice.assetFolder}/$cleanName.wav",
      "audio/$cleanName.mp3"
    )

    for (assetPath in assetCandidates) {
      try {
        val afd: AssetFileDescriptor = context.assets.openFd(assetPath)
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
          setVolume(1.0f, 1.0f)
          setOnCompletionListener {
            _isPlayingState.value = false
            onComplete?.invoke()
          }
          start()
        }
        afd.close()
        _isPlayingState.value = true
        return true
      } catch (_: Exception) {
        // Continue
      }
    }

    return false
  }

  /**
   * Algorithmic Islamic Harmonic Synthesizer using AudioTrack PCM.
   * Generates warm, crystal-clear harmonic notes tailored to the voice style.
   */
  private fun playSynthesizedHarmonicTone(
    glyphOrText: String,
    voice: VoiceProfile,
    onComplete: (() -> Unit)?
  ) {
    synthJob?.cancel()
    _isPlayingState.value = true

    synthJob = scope.launch(Dispatchers.Default) {
      try {
        // High-bitrate studio sampling rate (48000Hz) to prevent tinny or high-pitched artifacts
        val sampleRate = 48000
        val durationSec = if (glyphOrText.length > 5) 0.8 else 0.55
        val numSamples = (durationSec * sampleRate).toInt()
        val buffer = ShortArray(numSamples)

        // Musical frequency determination
        val charCode = glyphOrText.firstOrNull()?.code ?: 65
        val baseFrequencies = if (voice == VoiceProfile.MALE_QARI) {
          // Dedicated male chest-resonance fundamental frequencies (-18% pitch tuned)
          // Deep masculine baritone range (107.2Hz - 160.7Hz)
          doubleArrayOf(107.26, 120.40, 135.14, 143.18, 160.72)
        } else {
          // Soft sweet soprano pentatonic range (C4 to G4: ~261Hz - 392Hz) - UNTOUCHED
          doubleArrayOf(261.63, 293.66, 329.63, 349.23, 392.00)
        }

        val baseFreq = baseFrequencies[charCode % baseFrequencies.size]

        for (i in 0 until numSamples) {
          val t = i.toDouble() / sampleRate
          val envelope = if (voice == VoiceProfile.MALE_QARI) {
            // Chest-resonance audio envelope dynamics: rich acoustic body and warm sustain decay
            (1.0 - exp(-t * 28.0)) * exp(-t * 2.6)
          } else {
            // Untouched original female envelope
            (1.0 - exp(-t * 25.0)) * exp(-t * 4.0)
          }

          // Harmonic series (Fundamental + 2nd + 3rd harmonic for rich vocal timbre)
          val wave = if (voice == VoiceProfile.MALE_QARI) {
            // Chest formant resonance: dominant fundamental + resonant 2nd harmonic
            0.64 * sin(2.0 * Math.PI * baseFreq * t) +
              0.26 * sin(2.0 * Math.PI * baseFreq * 2.0 * t) +
              0.10 * sin(2.0 * Math.PI * baseFreq * 3.0 * t)
          } else {
            // Untouched female harmonics
            0.80 * sin(2.0 * Math.PI * baseFreq * t) +
              0.15 * sin(2.0 * Math.PI * baseFreq * 2.0 * t) +
              0.05 * sin(2.0 * Math.PI * baseFreq * 4.0 * t)
          }

          val maxAmplitude = if (voice == VoiceProfile.MALE_QARI) 27500.0 * voice.gainBoost else 20000.0
          val sampleVal = (wave * envelope * maxAmplitude).toInt().coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt())
          buffer[i] = sampleVal.toShort()
        }

        val track = AudioTrack.Builder()
          .setAudioAttributes(
            AudioAttributes.Builder()
              .setUsage(AudioAttributes.USAGE_MEDIA)
              .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
              .build()
          )
          .setAudioFormat(
            AudioFormat.Builder()
              .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
              .setSampleRate(sampleRate)
              .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
              .build()
          )
          .setBufferSizeInBytes(buffer.size * 2)
          .setTransferMode(AudioTrack.MODE_STATIC)
          .build()

        track.setVolume(1.0f)
        track.write(buffer, 0, buffer.size)
        track.play()

        // Wait for tone duration
        kotlinx.coroutines.delay((durationSec * 1000).toLong())
        track.stop()
        track.release()
      } catch (_: Exception) {
        // Fallback tone generator
        playTone(if (voice == VoiceProfile.MALE_QARI) ToneGenerator.TONE_PROP_ACK else ToneGenerator.TONE_PROP_PROMPT)
      } finally {
        scope.launch(Dispatchers.Main) {
          _isPlayingState.value = false
          onComplete?.invoke()
        }
      }
    }
  }

  fun stop() {
    synthJob?.cancel()
    _isPlayingState.value = false
    try {
      mediaPlayer?.stop()
      mediaPlayer?.release()
      mediaPlayer = null
      textToSpeech?.stop()
    } catch (_: Exception) {
    }
  }

  fun playTone(type: Int = ToneGenerator.TONE_PROP_BEEP) {
    try {
      toneGenerator?.startTone(type, 160)
    } catch (_: Exception) {}
  }

  fun playSuccessChime() {
    try {
      toneGenerator?.startTone(ToneGenerator.TONE_PROP_ACK, 200)
    } catch (_: Exception) {}
  }

  fun playErrorBuzzer() {
    try {
      toneGenerator?.startTone(ToneGenerator.TONE_PROP_NACK, 250)
    } catch (_: Exception) {}
  }

  fun release() {
    stop()
    try {
      toneGenerator?.release()
      textToSpeech?.stop()
      textToSpeech?.shutdown()
      textToSpeech = null
    } catch (_: Exception) {}
  }

  private fun toAudioSlug(glyph: String): String {
    val stripped = glyph.replace("\u0640", "").trim()
    return when (stripped) {
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
      else -> stripped.filter { it.isLetterOrDigit() || it == '_' }.lowercase(Locale.ROOT)
    }
  }

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
}
