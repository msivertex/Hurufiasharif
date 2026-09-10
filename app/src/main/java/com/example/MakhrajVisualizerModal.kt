package com.example

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.theme.RoyalEmerald
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * Interactive 3D Vision Makhraj (Articulation Point) Animation Modal.
 *
 * Provides:
 * 1. Semi-transparent 3D cross-section view of the human vocal tract (throat, tongue, lips, teeth, nasal cavity).
 * 2. Dynamic articulation movement for each letter (e.g. tongue elevating for 'ق', epiglottis constricting for 'ح', lips closing for 'ب').
 * 3. Glowing Emerald Green pulse effect at the exact Makhraj point of origin.
 * 4. Animated airflow arrows and breath stream particles (including nasal cavity diversion for Ghunnah).
 * 5. Complete user controls: Play, Pause, Slow-Motion (0.5x vs 1.0x), 3D Angle Rotation (-45° to +45°),
 *    Voice Gender switcher (Qari vs Qaria), and comprehensive Tajweed rules.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MakhrajVisualizerModal(
  initialLetter: ArabicLetter,
  selectedLang: String,
  soundManager: SoundManager,
  onDismiss: () -> Unit
) {
  var currentLetter by remember { mutableStateOf(initialLetter) }
  var isPlaying by remember { mutableStateOf(true) }
  var isSlowMotion by remember { mutableStateOf(false) }
  var rotation3DAngle by remember { mutableFloatStateOf(15f) } // -30f to +45f
  var activeVoiceGender by remember { mutableStateOf(soundManager.currentVoiceGender) }

  val makhrajProfile = remember(currentLetter.letter) {
    MakhrajRepository.getProfile(currentLetter.letter)
  }

  // Animation cycle: normal is ~1800ms, slow motion is ~3600ms
  val cycleDuration = if (isSlowMotion) 3600 else 1800
  val infiniteTransition = rememberInfiniteTransition(label = "makhraj_animation")
  val rawAnimationProgress by infiniteTransition.animateFloat(
    initialValue = 0f,
    targetValue = 1f,
    animationSpec = infiniteRepeatable(
      animation = tween(durationMillis = cycleDuration, easing = LinearEasing),
      repeatMode = RepeatMode.Restart
    ),
    label = "progress"
  )

  val animationProgress = if (isPlaying) rawAnimationProgress else 0.5f

  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(usePlatformDefaultWidth = false)
  ) {
    Surface(
      modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 8.dp, vertical = 12.dp)
        .testTag("makhraj_visualizer_dialog"),
      shape = RoundedCornerShape(24.dp),
      color = Color(0xFF0F172A) // Dark slate background for high-contrast medical 3D illumination
    ) {
      Column(
        modifier = Modifier
          .fillMaxSize()
          .padding(16.dp)
      ) {
        // 1. Top Header Bar
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(RoyalEmerald.copy(alpha = 0.2f))
                .border(1.5.dp, RoyalEmerald, CircleShape),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = currentLetter.letter,
                style = TextStyle(
                  fontSize = 24.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color.White,
                  fontFamily = FontFamily.Serif
                )
              )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text(
                text = when (selectedLang) {
                  "EN" -> "3D Makhraj Visualizer"
                  "AR" -> "المجسم ثلاثي الأبعاد لمخارج الحروف"
                  else -> "উচ্চারণস্থান (মাখরাজ) ৩ডি অ্যানিমেশন"
                },
                style = TextStyle(
                  fontSize = 15.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color.White
                )
              )
              Text(
                text = "${currentLetter.getName(selectedLang)} (${currentLetter.phonetic}) • ${makhrajProfile.mainRegion.getName(selectedLang)}",
                style = TextStyle(
                  fontSize = 11.sp,
                  color = Color(0xFF94A3B8)
                )
              )
            }
          }

          IconButton(
            onClick = onDismiss,
            modifier = Modifier.testTag("close_makhraj_modal")
          ) {
            Icon(
              imageVector = Icons.Default.Close,
              contentDescription = "Close",
              tint = Color(0xFFCBD5E1)
            )
          }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 2. Letters Quick Switcher Carousel
        LazyRow(
          modifier = Modifier
            .fillMaxWidth()
            .testTag("makhraj_letter_selector"),
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          items(ArabicAlphabetRepository.letters) { item ->
            val isSelected = item.letter == currentLetter.letter
            Surface(
              shape = RoundedCornerShape(10.dp),
              color = if (isSelected) RoyalEmerald else Color(0xFF1E293B),
              border = BorderStroke(
                1.dp,
                if (isSelected) Color(0xFF34D399) else Color(0xFF334155)
              ),
              modifier = Modifier
                .clickable {
                  currentLetter = item
                  soundManager.speakArabicLetter(item.letter, activeVoiceGender)
                }
                .testTag("makhraj_chip_${item.id}")
            ) {
              Text(
                text = item.letter,
                style = TextStyle(
                  fontSize = 18.sp,
                  fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                  color = if (isSelected) Color.White else Color(0xFF94A3B8),
                  fontFamily = FontFamily.Serif
                ),
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // 3. Scrollable Content Area: 3D Anatomical Canvas + Controls + Detailed Guide
        Column(
          modifier = Modifier
            .weight(1f)
            .verticalScroll(rememberScrollState()),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          // 3D Canvas Box
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .height(300.dp)
              .clip(RoundedCornerShape(18.dp))
              .background(
                Brush.radialGradient(
                  colors = listOf(Color(0xFF1E293B), Color(0xFF090D16))
                )
              )
              .border(1.5.dp, Color(0xFF334155), RoundedCornerShape(18.dp))
              .pointerInput(Unit) {
                detectDragGestures { _, dragAmount ->
                  rotation3DAngle = (rotation3DAngle + dragAmount.x * 0.35f).coerceIn(-35f, 45f)
                }
              }
              .testTag("makhraj_canvas_box")
          ) {
            Makhraj3DAnatomyCanvas(
              profile = makhrajProfile,
              progress = animationProgress,
              rotationAngle = rotation3DAngle,
              modifier = Modifier.fillMaxSize()
            )

            // Canvas Floating Overlay Badges
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.Top
            ) {
              // Makhraj Number Pill
              Surface(
                shape = RoundedCornerShape(8.dp),
                color = makhrajProfile.mainRegion.color.copy(alpha = 0.25f),
                border = BorderStroke(1.dp, makhrajProfile.mainRegion.color)
              ) {
                Text(
                  text = when (selectedLang) {
                    "EN" -> "Makhraj #${makhrajProfile.makhrajNumber}"
                    "AR" -> "مخرج رقم ${makhrajProfile.makhrajNumber}"
                    else -> "মাখরাজ নং ${makhrajProfile.makhrajNumber}"
                  },
                  style = TextStyle(
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                  ),
                  modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
              }

              // 3D Rotation readout
              Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color.Black.copy(alpha = 0.5f),
                border = BorderStroke(1.dp, Color(0xFF475569))
              ) {
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                ) {
                  Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = null,
                    tint = Color(0xFF38BDF8),
                    modifier = Modifier.size(13.dp)
                  )
                  Spacer(modifier = Modifier.width(4.dp))
                  Text(
                    text = "${rotation3DAngle.toInt()}° 3D Tilt",
                    style = TextStyle(fontSize = 10.sp, color = Color(0xFFE2E8F0))
                  )
                }
              }
            }

            // Bottom drag indicator hint
            Text(
              text = when (selectedLang) {
                "EN" -> "⇄ Drag to rotate 3D angle"
                "AR" -> "⇄ اسحب لتدوير الزاوية ثلاثية الأبعاد"
                else -> "⇄ আঙুল দিয়ে টেনে ৩ডি কোণ ঘোরান"
              },
              style = TextStyle(
                fontSize = 10.sp,
                color = Color(0xFF64748B)
              ),
              modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 6.dp)
            )
          }

          Spacer(modifier = Modifier.height(10.dp))

          // 4. Interactive Playback & 3D Controls
          Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
            border = BorderStroke(1.dp, Color(0xFF334155)),
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(modifier = Modifier.padding(12.dp)) {
              // Row 1: Play/Pause, Slow-Mo 0.5x, Audio Button
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                // Play / Pause Button
                Button(
                  onClick = { isPlaying = !isPlaying },
                  shape = RoundedCornerShape(10.dp),
                  colors = ButtonDefaults.buttonColors(
                    containerColor = if (isPlaying) Color(0xFF334155) else RoyalEmerald
                  ),
                  modifier = Modifier.testTag("toggle_play_animation")
                ) {
                  Icon(
                    imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = if (isPlaying) "Pause" else "Play",
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                  )
                  Spacer(modifier = Modifier.width(4.dp))
                  Text(
                    text = if (isPlaying) {
                      when (selectedLang) { "EN" -> "Pause" "AR" -> "إيقاف" else -> "স্থগিত" }
                    } else {
                      when (selectedLang) { "EN" -> "Play" "AR" -> "تشغيل" else -> "চালান" }
                    },
                    style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                  )
                }

                // Slow-Motion 0.5x Toggle
                Button(
                  onClick = { isSlowMotion = !isSlowMotion },
                  shape = RoundedCornerShape(10.dp),
                  colors = ButtonDefaults.buttonColors(
                    containerColor = if (isSlowMotion) Color(0xFFD97706) else Color(0xFF334155)
                  ),
                  modifier = Modifier.testTag("toggle_slow_motion")
                ) {
                  Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Slow Motion",
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                  )
                  Spacer(modifier = Modifier.width(4.dp))
                  Text(
                    text = if (isSlowMotion) "0.5x (Slow)" else "1.0x (Normal)",
                    style = TextStyle(fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                  )
                }

                // Audio Pronunciation Trigger
                Button(
                  onClick = {
                    soundManager.speakArabicLetter(currentLetter.letter, activeVoiceGender)
                  },
                  shape = RoundedCornerShape(10.dp),
                  colors = ButtonDefaults.buttonColors(containerColor = RoyalEmerald),
                  modifier = Modifier.testTag("play_makhraj_sound")
                ) {
                  Icon(
                    imageVector = Icons.Default.VolumeUp,
                    contentDescription = "Audio",
                    tint = Color.White,
                    modifier = Modifier.size(17.dp)
                  )
                  Spacer(modifier = Modifier.width(4.dp))
                  Text(
                    text = when (selectedLang) {
                      "EN" -> "Sound 🔊"
                      "AR" -> "صوت 🔊"
                      else -> "আওয়াজ 🔊"
                    },
                    style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                  )
                }
              }

              Spacer(modifier = Modifier.height(10.dp))

              // Row 2: 3D Angle Slider & Quick Angle Presets
              Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Icon(
                  imageVector = Icons.Default.Refresh,
                  contentDescription = null,
                  tint = Color(0xFF38BDF8),
                  modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                  text = when (selectedLang) {
                    "EN" -> "3D View Angle:"
                    "AR" -> "زاوية الرؤية ثلاثية الأبعاد:"
                    else -> "৩ডি ভিউ অ্যাঙ্গেল:"
                  },
                  style = TextStyle(fontSize = 11.sp, color = Color(0xFFCBD5E1))
                )
                Spacer(modifier = Modifier.width(6.dp))
                Slider(
                  value = rotation3DAngle,
                  onValueChange = { rotation3DAngle = it },
                  valueRange = -30f..45f,
                  modifier = Modifier
                    .weight(1f)
                    .testTag("rotation_slider"),
                  colors = SliderDefaults.colors(
                    thumbColor = Color(0xFF38BDF8),
                    activeTrackColor = Color(0xFF0284C7),
                    inactiveTrackColor = Color(0xFF475569)
                  )
                )
              }

              // Row 3: Angle Presets
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
              ) {
                listOf(
                  0f to (when (selectedLang) { "EN" -> "Sagittal 0°" "AR" -> "جانبي 0°" else -> "পাশ্বর্চ্ছেদ 0°" }),
                  20f to (when (selectedLang) { "EN" -> "3D Tilt 20°" "AR" -> "مائل 20°" else -> "৩ডি কোণ 20°" }),
                  40f to (when (selectedLang) { "EN" -> "Deep 3D 40°" "AR" -> "عميق 40°" else -> "গভীর ৩ডি 40°" })
                ).forEach { (angle, label) ->
                  Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (rotation3DAngle.toInt() == angle.toInt()) Color(0xFF0284C7) else Color(0xFF0F172A),
                    border = BorderStroke(1.dp, Color(0xFF334155)),
                    modifier = Modifier
                      .clickable { rotation3DAngle = angle }
                      .testTag("angle_preset_${angle.toInt()}")
                  ) {
                    Text(
                      text = label,
                      style = TextStyle(
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                      ),
                      modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                  }
                }
              }

              Spacer(modifier = Modifier.height(10.dp))

              // Row 4: Voice Gender Quick Selection inside Visualizer
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .clip(RoundedCornerShape(10.dp))
                  .background(Color(0xFF0F172A))
                  .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(
                  text = when (selectedLang) {
                    "EN" -> "Voice Engine:"
                    "AR" -> "الصوت القرآني:"
                    else -> "কণ্ঠস্বর (Voice):"
                  },
                  style = TextStyle(fontSize = 11.sp, color = Color(0xFF94A3B8), fontWeight = FontWeight.Bold)
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                  VoiceGender.entries.forEach { gender ->
                    val isSelected = activeVoiceGender == gender
                    Surface(
                      shape = RoundedCornerShape(8.dp),
                      color = if (isSelected) RoyalEmerald else Color(0xFF1E293B),
                      border = BorderStroke(1.dp, if (isSelected) Color(0xFF34D399) else Color(0xFF334155)),
                      modifier = Modifier
                        .clickable {
                          activeVoiceGender = gender
                          soundManager.setVoiceGender(gender)
                          soundManager.speakArabicLetter(currentLetter.letter, gender)
                        }
                        .testTag("visualizer_voice_${gender.id}")
                    ) {
                      Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                      ) {
                        Text(
                          text = if (gender == VoiceGender.MALE_QARI) "🎙️ Qari" else "🧕 Qaria",
                          style = TextStyle(
                            fontSize = 10.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) Color.White else Color(0xFFCBD5E1)
                          )
                        )
                      }
                    }
                  }
                }
              }
            }
          }

          Spacer(modifier = Modifier.height(12.dp))

          // 5. Tajweed & Anatomical Rules Card
          Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
            border = BorderStroke(1.dp, Color(0xFF334155)),
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(modifier = Modifier.padding(14.dp)) {
              // Tajweed attribute pills
              FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
              ) {
                // Region badge
                Surface(
                  shape = RoundedCornerShape(6.dp),
                  color = makhrajProfile.mainRegion.color.copy(alpha = 0.2f),
                  border = BorderStroke(1.dp, makhrajProfile.mainRegion.color)
                ) {
                  Text(
                    text = makhrajProfile.mainRegion.getName(selectedLang),
                    style = TextStyle(fontSize = 10.sp, fontWeight = FontWeight.Bold, color = makhrajProfile.mainRegion.color),
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                  )
                }

                if (makhrajProfile.hasQalqalah) {
                  Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Color(0xFFF59E0B).copy(alpha = 0.2f),
                    border = BorderStroke(1.dp, Color(0xFFF59E0B))
                  ) {
                    Text(
                      text = when (selectedLang) { "EN" -> "✨ Qalqalah (Echo)" "AR" -> "✨ القلقلة" else -> "✨ কলকলাহ (প্রতিধ্বনি)" },
                      style = TextStyle(fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFBBF24)),
                      modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                  }
                }

                if (makhrajProfile.hasGhunnah) {
                  Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Color(0xFFEC4899).copy(alpha = 0.2f),
                    border = BorderStroke(1.dp, Color(0xFFEC4899))
                  ) {
                    Text(
                      text = when (selectedLang) { "EN" -> "👃 Ghunnah (Nasal)" "AR" -> "👃 الغنة" else -> "👃 গুন্নাহ (নাসিক্য সুর)" },
                      style = TextStyle(fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFFF472B6)),
                      modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                  }
                }

                if (makhrajProfile.isHeavySound) {
                  Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Color(0xFF8B5CF6).copy(alpha = 0.2f),
                    border = BorderStroke(1.dp, Color(0xFF8B5CF6))
                  ) {
                    Text(
                      text = when (selectedLang) { "EN" -> "🔊 Mufakhkham (Heavy)" "AR" -> "🔊 مفخم (استعلاء)" else -> "🔊 পুর (ভারী হরফ)" },
                      style = TextStyle(fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFFA78BFA)),
                      modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                  }
                }
              }

              Spacer(modifier = Modifier.height(10.dp))

              Text(
                text = when (selectedLang) {
                  "EN" -> "Articulation Mechanism:"
                  "AR" -> "كيفية خروج الحرف وتشريح المخرج:"
                  else -> "উচ্চারণপদ্ধতি ও শারীরস্থান বিবরণ:"
                },
                style = TextStyle(
                  fontSize = 12.sp,
                  fontWeight = FontWeight.Bold,
                  color = RoyalEmerald
                )
              )

              Spacer(modifier = Modifier.height(4.dp))

              Text(
                text = when (selectedLang) {
                  "EN" -> makhrajProfile.descriptionEn
                  "AR" -> makhrajProfile.descriptionAr
                  else -> makhrajProfile.descriptionBn
                },
                style = TextStyle(
                  fontSize = 12.sp,
                  color = Color(0xFFE2E8F0),
                  lineHeight = 17.sp
                )
              )

              Spacer(modifier = Modifier.height(8.dp))

              Text(
                text = when (selectedLang) {
                  "EN" -> "Vocal Tract Interaction:"
                  "AR" -> "التفاعل الحركي لأعضاء النطق:"
                  else -> "মুখের অঙ্গসমূহের সক্রিয় সংস্পর্শ:"
                },
                style = TextStyle(
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color(0xFF38BDF8)
                )
              )

              Spacer(modifier = Modifier.height(3.dp))

              Text(
                text = when (selectedLang) {
                  "EN" -> makhrajProfile.anatomicalRuleEn
                  "AR" -> makhrajProfile.anatomicalRuleAr
                  else -> makhrajProfile.anatomicalRuleBn
                },
                style = TextStyle(
                  fontSize = 11.sp,
                  color = Color(0xFF94A3B8),
                  lineHeight = 16.sp
                )
              )
            }
          }
        }
      }
    }
  }
}

/**
 * 3D-projected Anatomical Cross-Section Canvas.
 * Renders the human vocal tract (head silhouette, nasal passage, oral cavity, hard/soft palate,
 * tongue, teeth, lips, pharynx, epiglottis, larynx/vocal cords, trachea) with dynamic movement,
 * glowing emerald pulses, and animated airflow particles.
 */
@Composable
fun Makhraj3DAnatomyCanvas(
  profile: LetterMakhrajProfile,
  progress: Float,
  rotationAngle: Float,
  modifier: Modifier = Modifier
) {
  Canvas(modifier = modifier) {
    val width = size.width
    val height = size.height

    // Simulated 3D perspective projection factor based on rotationAngle
    val rad = (rotationAngle * PI / 180.0).toFloat()
    val cosAngle = cos(rad)
    val sinAngle = sin(rad)
    val depthShift = sinAngle * 28f

    // 1. Draw Subtle Depth Grid / Coordinate Rays
    drawDepthGrid(width, height, depthShift)

    // 2. Draw Translucent Head Silhouette Profile
    drawHeadSilhouette(width, height, depthShift)

    // 3. Draw Nasal Cavity (Al-Khayshum - الخيشوم)
    drawNasalCavity(width, height, depthShift, profile.hasGhunnah, progress)

    // 4. Draw Hard and Soft Palate (with Uvula)
    drawPalateAndUvula(width, height, depthShift, profile.articulationType, progress)

    // 5. Draw Upper and Lower Teeth & Gums
    drawTeeth(width, height, depthShift)

    // 6. Draw Pharynx, Throat, Epiglottis & Vocal Cords
    drawThroatAndEpiglottis(width, height, depthShift, profile.articulationType, progress)

    // 7. Draw Dynamic Articulated Tongue (Elevates/curves according to letter)
    drawArticulatedTongue(width, height, depthShift, profile.articulationType, progress)

    // 8. Draw Lips (Bilabial compression / rounding / resting)
    drawLips(width, height, depthShift, profile.articulationType, progress)

    // 9. Draw Dynamic Airflow Stream & Animated Particles
    drawAirflowStream(width, height, depthShift, profile, progress)

    // 10. Draw Pulsing Glowing Emerald Rings at the exact Makhraj origin coordinate
    drawMakhrajGlowingPulse(width, height, depthShift, profile.focusX, profile.focusY, progress)

    // 11. Draw Anatomical Pointer & Label
    drawMakhrajPointer(width, height, depthShift, profile)
  }
}

/**
 * Coordinate mapping helper with 3D horizontal perspective shift.
 */
private fun project3D(normX: Float, normY: Float, width: Float, height: Float, depthShift: Float, zPlane: Float = 0f): Offset {
  val baseX = normX * width
  val baseY = normY * height
  val perspectiveX = baseX + depthShift * (1f - normX * 0.8f) + zPlane * depthShift
  return Offset(perspectiveX, baseY)
}

/**
 * Draw coordinate grid background.
 */
private fun DrawScope.drawDepthGrid(width: Float, height: Float, depthShift: Float) {
  val gridColor = Color(0xFF1E293B).copy(alpha = 0.5f)
  for (i in 1..4) {
    val y = height * (i * 0.2f)
    drawLine(gridColor, Offset(0f, y), Offset(width, y), strokeWidth = 1f)
  }
  for (i in 1..5) {
    val x = width * (i * 0.16f)
    drawLine(
      gridColor,
      Offset(x + depthShift * 0.5f, 0f),
      Offset(x - depthShift * 0.5f, height),
      strokeWidth = 1f
    )
  }
}

/**
 * Draw semi-transparent head silhouette.
 */
private fun DrawScope.drawHeadSilhouette(width: Float, height: Float, depthShift: Float) {
  val path = Path().apply {
    val p0 = project3D(0.35f, 0.08f, width, height, depthShift) // Forehead top
    val p1 = project3D(0.20f, 0.18f, width, height, depthShift) // Forehead front
    val p2 = project3D(0.18f, 0.26f, width, height, depthShift) // Nose bridge
    val p3 = project3D(0.10f, 0.35f, width, height, depthShift) // Nose tip
    val p4 = project3D(0.16f, 0.39f, width, height, depthShift) // Subnasal point
    val p5 = project3D(0.17f, 0.44f, width, height, depthShift) // Upper lip
    val p6 = project3D(0.19f, 0.48f, width, height, depthShift) // Mouth aperture
    val p7 = project3D(0.18f, 0.52f, width, height, depthShift) // Lower lip
    val p8 = project3D(0.17f, 0.58f, width, height, depthShift) // Chin
    val p9 = project3D(0.24f, 0.65f, width, height, depthShift) // Submental jaw
    val p10 = project3D(0.40f, 0.88f, width, height, depthShift) // Anterior neck
    val p11 = project3D(0.72f, 0.88f, width, height, depthShift) // Posterior neck
    val p12 = project3D(0.78f, 0.45f, width, height, depthShift) // Occiput
    val p13 = project3D(0.65f, 0.10f, width, height, depthShift) // Crown

    moveTo(p0.x, p0.y)
    cubicTo(p1.x, p1.y, p1.x, p1.y, p2.x, p2.y)
    lineTo(p3.x, p3.y)
    lineTo(p4.x, p4.y)
    lineTo(p5.x, p5.y)
    lineTo(p6.x, p6.y)
    lineTo(p7.x, p7.y)
    lineTo(p8.x, p8.y)
    cubicTo(p9.x, p9.y, p9.x, p9.y, p10.x, p10.y)
    lineTo(p11.x, p11.y)
    cubicTo(p12.x, p12.y, p12.x, p12.y, p13.x, p13.y)
    close()
  }

  // Soft medical cyan/slate glow
  drawPath(
    path = path,
    brush = Brush.radialGradient(
      colors = listOf(Color(0xFF38BDF8).copy(alpha = 0.05f), Color.Transparent),
      center = Offset(width * 0.4f, height * 0.4f),
      radius = width * 0.5f
    )
  )

  drawPath(
    path = path,
    color = Color(0xFF38BDF8).copy(alpha = 0.25f),
    style = Stroke(width = 1.5f, join = StrokeJoin.Round)
  )
}

/**
 * Draw Nasal Cavity (Khayshum - الخيشوم).
 */
private fun DrawScope.drawNasalCavity(width: Float, height: Float, depthShift: Float, hasGhunnah: Boolean, progress: Float) {
  val nasalPath = Path().apply {
    val n0 = project3D(0.18f, 0.36f, width, height, depthShift) // Nostril entry
    val n1 = project3D(0.28f, 0.28f, width, height, depthShift) // Nasal cavity front
    val n2 = project3D(0.48f, 0.27f, width, height, depthShift) // Nasal cavity roof
    val n3 = project3D(0.58f, 0.35f, width, height, depthShift) // Nasopharynx entry
    val n4 = project3D(0.50f, 0.38f, width, height, depthShift) // Hard palate top
    val n5 = project3D(0.26f, 0.38f, width, height, depthShift) // Maxilla bone

    moveTo(n0.x, n0.y)
    cubicTo(n1.x, n1.y, n2.x, n2.y, n3.x, n3.y)
    cubicTo(n4.x, n4.y, n5.x, n5.y, n0.x, n0.y)
    close()
  }

  val fillColor = if (hasGhunnah) {
    val glowAlpha = 0.2f + 0.15f * sin(progress * 2 * PI).toFloat().coerceIn(0f, 1f)
    Color(0xFFEC4899).copy(alpha = glowAlpha) // Vibrant pink/rose glow for Ghunnah resonance
  } else {
    Color(0xFF0284C7).copy(alpha = 0.08f)
  }

  drawPath(path = nasalPath, color = fillColor, style = Fill)
  drawPath(
    path = nasalPath,
    color = if (hasGhunnah) Color(0xFFF472B6) else Color(0xFF0284C7).copy(alpha = 0.4f),
    style = Stroke(width = 1.5f)
  )
}

/**
 * Draw Hard Palate, Soft Palate & Uvula.
 */
private fun DrawScope.drawPalateAndUvula(
  width: Float,
  height: Float,
  depthShift: Float,
  articulationType: ArticulationType,
  progress: Float
) {
  val isQaf = articulationType == ArticulationType.TONGUE_ROOT_SOFT_PALATE
  val isGhunnah = articulationType == ArticulationType.BOTH_LIPS_COMPRESSED ||
    articulationType == ArticulationType.TONGUE_TIP_UPPER_GUMS

  // Soft palate and uvula dynamic position: drops for Ghunnah, seals for Qaf
  val uvulaDrop = if (isGhunnah) 0.025f * sin(progress * PI).toFloat() else 0f
  val uvulaLift = if (isQaf) -0.02f * sin(progress * PI).toFloat() else 0f

  val palatePath = Path().apply {
    val p0 = project3D(0.24f, 0.42f, width, height, depthShift) // Behind upper incisors
    val p1 = project3D(0.36f, 0.40f, width, height, depthShift) // Hard palate curve
    val p2 = project3D(0.48f, 0.42f, width, height, depthShift) // Soft palate start
    val p3 = project3D(0.53f, 0.47f + uvulaDrop + uvulaLift, width, height, depthShift) // Uvula tip

    moveTo(p0.x, p0.y)
    cubicTo(p1.x, p1.y, p2.x, p2.y, p3.x, p3.y)
  }

  drawPath(
    path = palatePath,
    color = Color(0xFFCBD5E1),
    style = Stroke(width = 3.5f, cap = StrokeCap.Round)
  )

  // Uvula (اللهاة) spherical flesh node
  val uvulaPos = project3D(0.53f, 0.47f + uvulaDrop + uvulaLift, width, height, depthShift)
  drawCircle(
    color = if (isQaf) Color(0xFF34D399) else Color(0xFFE2E8F0),
    radius = 5.dp.toPx(),
    center = uvulaPos
  )
}

/**
 * Draw Upper and Lower Teeth.
 */
private fun DrawScope.drawTeeth(width: Float, height: Float, depthShift: Float) {
  // Upper Incisors (الثنايا العليا)
  val upperTeeth = project3D(0.22f, 0.44f, width, height, depthShift)
  drawRoundRect(
    color = Color.White,
    topLeft = Offset(upperTeeth.x - 3.dp.toPx(), upperTeeth.y),
    size = androidx.compose.ui.geometry.Size(6.dp.toPx(), 11.dp.toPx()),
    cornerRadius = androidx.compose.ui.geometry.CornerRadius(2.dp.toPx(), 2.dp.toPx())
  )

  // Lower Incisors (الثنايا السفلى)
  val lowerTeeth = project3D(0.22f, 0.50f, width, height, depthShift)
  drawRoundRect(
    color = Color.White,
    topLeft = Offset(lowerTeeth.x - 3.dp.toPx(), lowerTeeth.y - 11.dp.toPx()),
    size = androidx.compose.ui.geometry.Size(6.dp.toPx(), 11.dp.toPx()),
    cornerRadius = androidx.compose.ui.geometry.CornerRadius(2.dp.toPx(), 2.dp.toPx())
  )
}

/**
 * Draw Throat, Pharynx, Epiglottis and Vocal Cords.
 */
private fun DrawScope.drawThroatAndEpiglottis(
  width: Float,
  height: Float,
  depthShift: Float,
  articulationType: ArticulationType,
  progress: Float
) {
  val isMidThroat = articulationType == ArticulationType.THROAT_MID_EPIGLOTTIS // 'Ayn, Haa
  val isDeepThroat = articulationType == ArticulationType.THROAT_DEEP_VOCAL_CORDS // Hamza, Ha

  // Epiglottis constriction toward posterior pharyngeal wall
  val epiglottisConstriction = if (isMidThroat) 0.035f * sin(progress * PI).toFloat() else 0f
  val vocalCordVibe = if (isDeepThroat) 0.015f * sin(progress * 6 * PI).toFloat() else 0f

  // Posterior Pharyngeal Wall (جدار البلعوم الخلفي)
  val pharynxWall = Path().apply {
    val w0 = project3D(0.64f, 0.35f, width, height, depthShift)
    val w1 = project3D(0.66f, 0.55f, width, height, depthShift)
    val w2 = project3D(0.66f, 0.85f, width, height, depthShift) // Trachea back
    moveTo(w0.x, w0.y)
    cubicTo(w1.x, w1.y, w1.x, w1.y, w2.x, w2.y)
  }
  drawPath(pharynxWall, color = Color(0xFF64748B), style = Stroke(width = 2.5f, cap = StrokeCap.Round))

  // Epiglottis (لسان المزمار)
  val epiBase = project3D(0.57f, 0.62f, width, height, depthShift)
  val epiTip = project3D(0.62f + epiglottisConstriction, 0.67f, width, height, depthShift)
  drawLine(
    color = if (isMidThroat) Color(0xFFF59E0B) else Color(0xFF94A3B8),
    start = epiBase,
    end = epiTip,
    strokeWidth = 3.5f,
    cap = StrokeCap.Round
  )

  // Vocal Cords / Larynx (الحنجرة والأوتار الصوتية)
  val cordLeft = project3D(0.61f - vocalCordVibe, 0.74f, width, height, depthShift)
  val cordRight = project3D(0.65f + vocalCordVibe, 0.74f, width, height, depthShift)
  drawLine(
    color = if (isDeepThroat) Color(0xFFEF4444) else Color(0xFFE2E8F0),
    start = cordLeft,
    end = cordRight,
    strokeWidth = 4f,
    cap = StrokeCap.Round
  )
}

/**
 * Draw Dynamic Tongue (اللسان).
 * Actively morphs its shape according to the specific letter's articulation requirements.
 */
private fun DrawScope.drawArticulatedTongue(
  width: Float,
  height: Float,
  depthShift: Float,
  articulationType: ArticulationType,
  progress: Float
) {
  val cycleWeight = sin(progress * PI).toFloat()

  // Dynamic displacement vectors
  var backTongueY = 0f
  var midTongueY = 0f
  var tipTongueX = 0f
  var tipTongueY = 0f

  when (articulationType) {
    ArticulationType.TONGUE_ROOT_SOFT_PALATE -> { // Qaf ('ق')
      backTongueY = -0.06f * cycleWeight // Elevate back tongue firmly against soft palate
    }
    ArticulationType.TONGUE_BACK_HARD_PALATE -> { // Kaf ('ك')
      backTongueY = -0.045f * cycleWeight
      midTongueY = -0.02f * cycleWeight
    }
    ArticulationType.TONGUE_CENTER_ROOF -> { // Jim ('ج'), Sheen ('ش'), Yaa ('ي')
      midTongueY = -0.055f * cycleWeight // Arch middle of tongue to palate roof
    }
    ArticulationType.TONGUE_TIP_INCISORS_ROOT -> { // Ta ('ط'), Dal ('د'), Taa ('ت')
      tipTongueX = -0.015f * cycleWeight
      tipTongueY = -0.05f * cycleWeight // Strike base of upper incisors
    }
    ArticulationType.TONGUE_TIP_INCISORS_EDGE -> { // Tha ('ث'), Dhal ('ذ'), Zaa ('ظ')
      tipTongueX = -0.035f * cycleWeight // Slip between tooth edges
      tipTongueY = -0.025f * cycleWeight
    }
    ArticulationType.TONGUE_TIP_LOWER_INCISORS -> { // Seen ('س'), Zay ('ز'), Saad ('ص')
      tipTongueY = 0.015f * cycleWeight // Stay behind lower teeth
    }
    ArticulationType.TONGUE_TIP_UPPER_GUMS,
    ArticulationType.TONGUE_EDGE_GUMS -> { // Noon ('ن'), Raa ('ر'), Lam ('ل')
      tipTongueY = -0.04f * cycleWeight // Press against upper gums
    }
    ArticulationType.TONGUE_SIDE_MOLAR -> { // Dad ('ض')
      midTongueY = -0.03f * cycleWeight
      backTongueY = -0.03f * cycleWeight
    }
    else -> {}
  }

  val tonguePath = Path().apply {
    val root = project3D(0.55f, 0.65f, width, height, depthShift)
    val back = project3D(0.48f, 0.52f + backTongueY, width, height, depthShift)
    val center = project3D(0.38f, 0.49f + midTongueY, width, height, depthShift)
    val blade = project3D(0.30f, 0.49f + tipTongueY * 0.7f, width, height, depthShift)
    val tip = project3D(0.24f + tipTongueX, 0.49f + tipTongueY, width, height, depthShift)
    val sublingual = project3D(0.26f, 0.55f, width, height, depthShift)
    val tongueBase = project3D(0.42f, 0.62f, width, height, depthShift)

    moveTo(root.x, root.y)
    cubicTo(back.x, back.y, center.x, center.y, blade.x, blade.y)
    lineTo(tip.x, tip.y)
    cubicTo(sublingual.x, sublingual.y, tongueBase.x, tongueBase.y, root.x, root.y)
    close()
  }

  // Beautiful medical tongue gradient (muscular pink to warm coral)
  drawPath(
    path = tonguePath,
    brush = Brush.linearGradient(
      colors = listOf(Color(0xFFF43F5E).copy(alpha = 0.55f), Color(0xFFFB7185).copy(alpha = 0.4f)),
      start = project3D(0.55f, 0.65f, width, height, depthShift),
      end = project3D(0.24f, 0.49f, width, height, depthShift)
    )
  )

  drawPath(
    path = tonguePath,
    color = Color(0xFFFB7185),
    style = Stroke(width = 2.5f, join = StrokeJoin.Round)
  )
}

/**
 * Draw Lips (الشفتان).
 * Simulates bilabial compression for Baa ('ب') & Meem ('م'), rounding for Waw ('و'),
 * and lower lip to upper teeth for Faa ('ف').
 */
private fun DrawScope.drawLips(
  width: Float,
  height: Float,
  depthShift: Float,
  articulationType: ArticulationType,
  progress: Float
) {
  val cycleWeight = sin(progress * PI).toFloat()

  var upperLipY = 0f
  var lowerLipY = 0f
  var lipProtrusion = 0f

  when (articulationType) {
    ArticulationType.BOTH_LIPS_COMPRESSED -> { // Baa ('ب'), Meem ('م')
      upperLipY = 0.015f * cycleWeight // Descends to seal
      lowerLipY = -0.015f * cycleWeight // Ascends to compress
    }
    ArticulationType.BOTH_LIPS_ROUNDED -> { // Waw ('و')
      lipProtrusion = -0.025f * cycleWeight // Project forward
    }
    ArticulationType.LOWER_LIP_UPPER_TEETH -> { // Faa ('ف')
      lowerLipY = -0.025f * cycleWeight // Meets upper incisors
    }
    else -> {}
  }

  val lipColor = Color(0xFFE11D48)

  // Upper Lip
  val upperLipPath = Path().apply {
    val u0 = project3D(0.18f + lipProtrusion, 0.43f, width, height, depthShift)
    val u1 = project3D(0.21f + lipProtrusion, 0.47f + upperLipY, width, height, depthShift)
    moveTo(u0.x, u0.y)
    lineTo(u1.x, u1.y)
  }
  drawPath(upperLipPath, color = lipColor, style = Stroke(width = 4.5f, cap = StrokeCap.Round))

  // Lower Lip
  val lowerLipPath = Path().apply {
    val l0 = project3D(0.21f + lipProtrusion, 0.49f + lowerLipY, width, height, depthShift)
    val l1 = project3D(0.18f + lipProtrusion, 0.54f, width, height, depthShift)
    moveTo(l0.x, l0.y)
    lineTo(l1.x, l1.y)
  }
  drawPath(lowerLipPath, color = lipColor, style = Stroke(width = 4.5f, cap = StrokeCap.Round))
}

/**
 * Draw Dynamic Airflow Stream & Particle Arrows.
 */
private fun DrawScope.drawAirflowStream(
  width: Float,
  height: Float,
  depthShift: Float,
  profile: LetterMakhrajProfile,
  progress: Float
) {
  val isGhunnah = profile.hasGhunnah

  // Breath flow path from lungs upward
  val airwayPoints = mutableListOf<Offset>()
  airwayPoints.add(project3D(0.63f, 0.86f, width, height, depthShift)) // Trachea
  airwayPoints.add(project3D(0.63f, 0.74f, width, height, depthShift)) // Vocal cords
  airwayPoints.add(project3D(0.60f, 0.65f, width, height, depthShift)) // Pharynx
  airwayPoints.add(project3D(0.50f, 0.50f, width, height, depthShift)) // Oral entry

  if (isGhunnah) {
    // Branch air into nasal cavity
    airwayPoints.add(project3D(0.54f, 0.36f, width, height, depthShift))
    airwayPoints.add(project3D(0.40f, 0.32f, width, height, depthShift))
    airwayPoints.add(project3D(0.22f, 0.35f, width, height, depthShift)) // Out nostrils
  } else {
    // Air flows out of mouth
    airwayPoints.add(project3D(0.35f, 0.47f, width, height, depthShift))
    airwayPoints.add(project3D(0.22f, 0.48f, width, height, depthShift))
    airwayPoints.add(project3D(0.10f, 0.48f, width, height, depthShift)) // Out lips
  }

  // Draw smooth dashed stream
  val flowPath = Path().apply {
    moveTo(airwayPoints[0].x, airwayPoints[0].y)
    for (i in 1 until airwayPoints.size) {
      val prev = airwayPoints[i - 1]
      val cur = airwayPoints[i]
      quadraticTo(prev.x, prev.y, (prev.x + cur.x) / 2f, (prev.y + cur.y) / 2f)
    }
  }

  val streamColor = if (isGhunnah) Color(0xFFF472B6) else Color(0xFF38BDF8)
  drawPath(
    path = flowPath,
    color = streamColor.copy(alpha = 0.45f),
    style = Stroke(
      width = 2.5f,
      pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 8f), phase = progress * 40f)
    )
  )

  // Animated pulse particle traveling along the stream
  val particleIndex = ((progress * (airwayPoints.size - 1)).toInt()).coerceIn(0, airwayPoints.size - 2)
  val segmentProgress = (progress * (airwayPoints.size - 1)) - particleIndex
  val pA = airwayPoints[particleIndex]
  val pB = airwayPoints[particleIndex + 1]
  val particlePos = Offset(
    pA.x + (pB.x - pA.x) * segmentProgress,
    pA.y + (pB.y - pA.y) * segmentProgress
  )

  drawCircle(
    color = if (isGhunnah) Color(0xFFEC4899) else Color(0xFF00E5FF),
    radius = 4.dp.toPx(),
    center = particlePos
  )
}

/**
 * Draw Glowing Emerald Green / Royal Pulse at the exact Makhraj Contact Point.
 */
private fun DrawScope.drawMakhrajGlowingPulse(
  width: Float,
  height: Float,
  depthShift: Float,
  focusX: Float,
  focusY: Float,
  progress: Float
) {
  val center = project3D(focusX, focusY, width, height, depthShift)
  val pulseWeight = sin(progress * 2 * PI).toFloat().coerceIn(0f, 1f)

  // Outer expanding glowing ripple ring
  val ringRadius1 = 14.dp.toPx() + 18.dp.toPx() * pulseWeight
  drawCircle(
    color = Color(0xFF10B981).copy(alpha = (0.4f * (1f - pulseWeight * 0.7f)).coerceIn(0f, 1f)),
    radius = ringRadius1,
    center = center,
    style = Stroke(width = 2.5f)
  )

  // Inner vibrant pulsing ring
  val ringRadius2 = 8.dp.toPx() + 6.dp.toPx() * pulseWeight
  drawCircle(
    color = Color(0xFF34D399).copy(alpha = 0.7f),
    radius = ringRadius2,
    center = center,
    style = Stroke(width = 2f)
  )

  // Solid bright emerald core
  drawCircle(
    color = Color(0xFF10B981),
    radius = 5.dp.toPx(),
    center = center
  )
  drawCircle(
    color = Color.White,
    radius = 2.dp.toPx(),
    center = center
  )
}

/**
 * Draw Callout Line & Anatomical Pointer Pin.
 */
private fun DrawScope.drawMakhrajPointer(
  width: Float,
  height: Float,
  depthShift: Float,
  profile: LetterMakhrajProfile
) {
  val target = project3D(profile.focusX, profile.focusY, width, height, depthShift)
  val pointerOrigin = Offset(target.x - 32.dp.toPx(), target.y - 36.dp.toPx())

  drawLine(
    color = Color(0xFF10B981),
    start = pointerOrigin,
    end = target,
    strokeWidth = 1.5f,
    pathEffect = PathEffect.dashPathEffect(floatArrayOf(4f, 4f))
  )

  drawCircle(
    color = Color(0xFF10B981),
    radius = 3.dp.toPx(),
    center = pointerOrigin
  )
}

private fun MakhrajMainRegion.getName(lang: String): String = when (lang) {
  "EN" -> nameEn
  "AR" -> nameAr
  else -> nameBn
}
