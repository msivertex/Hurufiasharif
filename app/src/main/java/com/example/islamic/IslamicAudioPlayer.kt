package com.example.islamic

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Locale
import kotlin.math.sin

/**
 * Islamic Audio Player & Synthesizer for 99 Names of Allah & Adhan Chimes
 */
class IslamicAudioPlayer(private val context: Context) : TextToSpeech.OnInitListener {

  private var tts: TextToSpeech? = null
  private var isTtsInitialized = false
  private var hasArabicVoice = false

  private val _playingNameId = MutableStateFlow<Int?>(null)
  val playingNameId: StateFlow<Int?> = _playingNameId.asStateFlow()

  private val _isAutoPlay = MutableStateFlow(false)
  val isAutoPlay: StateFlow<Boolean> = _isAutoPlay.asStateFlow()

  private val scope = CoroutineScope(Dispatchers.Main)
  private var autoPlayJob: Job? = null

  init {
    tts = TextToSpeech(context.applicationContext, this)
  }

  override fun onInit(status: Int) {
    if (status == TextToSpeech.SUCCESS) {
      val arLocale = Locale("ar")
      val avail = tts?.isLanguageAvailable(arLocale)
      if (avail != TextToSpeech.LANG_MISSING_DATA && avail != TextToSpeech.LANG_NOT_SUPPORTED) {
        tts?.language = arLocale
        hasArabicVoice = true
      }
      isTtsInitialized = true

      tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
        override fun onStart(utteranceId: String?) {}

        override fun onDone(utteranceId: String?) {
          scope.launch {
            if (_isAutoPlay.value) {
              val current = _playingNameId.value ?: 1
              if (current < 99) {
                delay(800)
                playName(current + 1, AsmaUlHusnaMaster.all99Names.getOrNull(current)?.nameAr ?: "")
              } else {
                stopPlayback()
              }
            } else {
              _playingNameId.value = null
            }
          }
        }

        @Deprecated("Deprecated in Java")
        override fun onError(utteranceId: String?) {
          scope.launch { _playingNameId.value = null }
        }
      })
    }
  }

  fun playName(number: Int, arabicName: String) {
    _playingNameId.value = number

    if (isTtsInitialized && hasArabicVoice) {
      val params = Bundle()
      params.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "name_$number")
      tts?.speak(arabicName, TextToSpeech.QUEUE_FLUSH, params, "name_$number")
    } else {
      // High-quality Islamic Chime audio synth fallback
      scope.launch(Dispatchers.Default) {
        synthesizeIslamicChime(number)
        delay(1200)
        scope.launch(Dispatchers.Main) {
          if (_isAutoPlay.value) {
            if (number < 99) {
              playName(number + 1, AsmaUlHusnaMaster.all99Names.getOrNull(number)?.nameAr ?: "")
            } else {
              stopPlayback()
            }
          } else {
            _playingNameId.value = null
          }
        }
      }
    }
  }

  fun toggleAutoPlay(startFromId: Int = 1) {
    if (_isAutoPlay.value) {
      stopPlayback()
    } else {
      _isAutoPlay.value = true
      val item = AsmaUlHusnaMaster.all99Names.firstOrNull { it.number == startFromId }
      if (item != null) {
        playName(item.number, item.nameAr)
      }
    }
  }

  fun stopPlayback() {
    autoPlayJob?.cancel()
    _isAutoPlay.value = false
    _playingNameId.value = null
    tts?.stop()
  }

  fun release() {
    stopPlayback()
    tts?.shutdown()
    tts = null
  }

  /**
   * Generates a warm Islamic prayer bell / chime harmonic tone
   */
  private fun synthesizeIslamicChime(seed: Int) {
    try {
      val sampleRate = 44100
      val durationSec = 1.0
      val numSamples = (durationSec * sampleRate).toInt()
      val buffer = ShortArray(numSamples)

      // Pentatonic pitch frequencies inspired by Maqam Bayati
      val frequencies = doubleArrayOf(392.0, 440.0, 493.88, 587.33, 659.25, 783.99)
      val baseFreq = frequencies[(seed - 1) % frequencies.size]

      for (i in 0 until numSamples) {
        val t = i.toDouble() / sampleRate
        // Envelope: smooth attack & exponential decay
        val envelope = (1.0 - kotlin.math.exp(-t * 20.0)) * kotlin.math.exp(-t * 3.5)
        // Harmonics
        val sample = 0.6 * sin(2.0 * Math.PI * baseFreq * t) +
          0.3 * sin(2.0 * Math.PI * baseFreq * 2.0 * t) +
          0.1 * sin(2.0 * Math.PI * baseFreq * 3.0 * t)
        val value = (sample * envelope * 24000.0).toInt().coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt())
        buffer[i] = value.toShort()
      }

      val track = AudioTrack.Builder()
        .setAudioAttributes(
          AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
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

      track.write(buffer, 0, buffer.size)
      track.play()
    } catch (_: Exception) {}
  }
}
