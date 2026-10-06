package com.example.islamic

import android.content.Context
import com.example.audio.AudioEngine
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Islamic Audio Player & Synthesizer for 99 Names of Allah & Adhan Chimes
 * Powered by unified dual-voice AudioEngine (Male Qari / Female Qaria).
 */
class IslamicAudioPlayer(private val context: Context) {

  val audioEngine: AudioEngine = AudioEngine.getInstance(context)

  private val _playingNameId = MutableStateFlow<Int?>(null)
  val playingNameId: StateFlow<Int?> = _playingNameId.asStateFlow()

  private val _isAutoPlay = MutableStateFlow(false)
  val isAutoPlay: StateFlow<Boolean> = _isAutoPlay.asStateFlow()

  private val scope = CoroutineScope(Dispatchers.Main)
  private var autoPlayJob: Job? = null

  fun playName(number: Int, arabicName: String) {
    _playingNameId.value = number

    audioEngine.playArabicText(
      arabicText = arabicName,
      voice = audioEngine.activeVoice,
      onComplete = {
        scope.launch {
          if (_isAutoPlay.value) {
            val current = _playingNameId.value ?: number
            if (current < 99) {
              delay(650)
              val nextItem = AsmaUlHusnaMaster.all99Names.getOrNull(current)
              if (nextItem != null && _isAutoPlay.value) {
                playName(nextItem.number, nextItem.nameAr)
              } else {
                stopPlayback()
              }
            } else {
              stopPlayback()
            }
          } else {
            _playingNameId.value = null
          }
        }
      }
    )
  }

  fun toggleAutoPlay(startFromId: Int = 1) {
    if (_isAutoPlay.value) {
      stopPlayback()
    } else {
      _isAutoPlay.value = true
      val item = AsmaUlHusnaMaster.all99Names.firstOrNull { it.number == startFromId }
        ?: AsmaUlHusnaMaster.all99Names.firstOrNull()
      if (item != null) {
        playName(item.number, item.nameAr)
      }
    }
  }

  fun stopPlayback() {
    autoPlayJob?.cancel()
    _isAutoPlay.value = false
    _playingNameId.value = null
    audioEngine.stop()
  }

  fun release() {
    stopPlayback()
  }
}

