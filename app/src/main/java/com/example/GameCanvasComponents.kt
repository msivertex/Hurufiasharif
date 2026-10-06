package com.example

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.testTag
import com.example.ui.theme.RoyalEmerald
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import kotlinx.coroutines.delay
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

// =============================================================================
// 1. PARTICLE CONFETTI ANIMATION CANVAS
// =============================================================================

private class ConfettiParticle(
  var x: Float,
  var y: Float,
  var vx: Float,
  var vy: Float,
  var rotation: Float,
  var rotationSpeed: Float,
  val width: Float,
  val height: Float,
  val color: Color
)

@Composable
fun ConfettiCanvas(
  triggerKey: Any?,
  modifier: Modifier = Modifier
) {
  val particles = remember { mutableStateListOf<ConfettiParticle>() }
  var isAnimating by remember { mutableStateOf(false) }

  val confettiColors = remember {
    listOf(
      Color(0xFFFFD700), // Gold
      Color(0xFF10B981), // Emerald
      Color(0xFF06B6D4), // Cyan
      Color(0xFFF43F5E), // Rose
      Color(0xFF8B5CF6), // Violet
      Color(0xFFF97316), // Orange
      Color(0xFF38BDF8)  // Sky
    )
  }

  LaunchedEffect(triggerKey) {
    if (triggerKey != null) {
      particles.clear()
      // Generate 55 burst particles
      for (i in 0 until 55) {
        val angle = Random.nextFloat() * 2f * Math.PI.toFloat()
        val speed = Random.nextFloat() * 600f + 250f
        particles.add(
          ConfettiParticle(
            x = 0.5f,
            y = 0.45f,
            vx = cos(angle) * speed,
            vy = (sin(angle) * speed) - 300f,
            rotation = Random.nextFloat() * 360f,
            rotationSpeed = (Random.nextFloat() - 0.5f) * 720f,
            width = Random.nextFloat() * 12f + 8f,
            height = Random.nextFloat() * 8f + 5f,
            color = confettiColors.random()
          )
        )
      }
      isAnimating = true
      delay(1600L)
      particles.clear()
      isAnimating = false
    }
  }

  if (isAnimating && particles.isNotEmpty()) {
    val progress = remember { Animatable(0f) }
    LaunchedEffect(triggerKey) {
      progress.snapTo(0f)
      progress.animateTo(1f, tween(1600, easing = LinearEasing))
    }

    Canvas(
      modifier = modifier
        .fillMaxSize()
        .testTag("confetti_canvas")
    ) {
      val canvasWidth = size.width
      val canvasHeight = size.height
      val dt = 0.016f

      particles.forEach { p ->
        // Physics update
        p.vy += 850f * dt // Gravity
        p.x += (p.vx * dt) / canvasWidth
        p.y += (p.vy * dt) / canvasHeight
        p.rotation += p.rotationSpeed * dt

        val currentAlpha = (1f - progress.value).coerceIn(0f, 1f)
        val px = p.x * canvasWidth
        val py = p.y * canvasHeight

        rotate(degrees = p.rotation, pivot = Offset(px, py)) {
          drawRect(
            color = p.color.copy(alpha = currentAlpha),
            topLeft = Offset(px - p.width / 2f, py - p.height / 2f),
            size = Size(p.width, p.height)
          )
        }
      }
    }
  }
}

// =============================================================================
// 2. STREAK COMBO BANNER (e.g., 'Combo x3! 🔥', 'Super Strike! ⚡')
// =============================================================================

@Composable
fun StreakOverlayBanner(
  streakCount: Int,
  modifier: Modifier = Modifier
) {
  AnimatedVisibility(
    visible = streakCount >= 2,
    enter = scaleIn(tween(250, easing = FastOutSlowInEasing)) + fadeIn(),
    exit = scaleOut(tween(200)) + fadeOut(),
    modifier = modifier
  ) {
    val (titleText, gradientColors, emoji) = when {
      streakCount >= 8 -> Triple("Unstoppable! x$streakCount", listOf(Color(0xFF7C3AED), Color(0xFFC026D3)), "🌟👑")
      streakCount >= 5 -> Triple("Super Strike! x$streakCount", listOf(Color(0xFFDC2626), Color(0xFFF97316)), "⚡🔥")
      streakCount >= 3 -> Triple("Combo x$streakCount! 🔥", listOf(Color(0xFFEA580C), Color(0xFFF59E0B)), "🔥")
      else -> Triple("Combo x$streakCount!", listOf(Color(0xFF059669), Color(0xFF10B981)), "🎯")
    }

    Surface(
      shape = RoundedCornerShape(16.dp),
      color = Color.Transparent,
      border = BorderStroke(1.5.dp, Color.White.copy(alpha = 0.8f)),
      shadowElevation = 8.dp,
      modifier = Modifier.testTag("streak_banner")
    ) {
      Box(
        modifier = Modifier
          .background(Brush.horizontalGradient(gradientColors))
          .padding(horizontal = 16.dp, vertical = 6.dp)
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(text = emoji, fontSize = 16.sp)
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = titleText,
            style = TextStyle(
              fontSize = 13.sp,
              fontWeight = FontWeight.ExtraBold,
              color = Color.White,
              letterSpacing = 0.5.sp
            )
          )
        }
      }
    }
  }
}

// =============================================================================
// 3. PROGRESSIVE ARABIC CULTURAL PRAISE BANNER
// =============================================================================

@Composable
fun ArabicPraiseFeedbackBanner(
  praise: ArabicCulturalPraise?,
  isVisible: Boolean,
  selectedLang: String,
  modifier: Modifier = Modifier
) {
  AnimatedVisibility(
    visible = isVisible && praise != null,
    enter = slideInVertically(initialOffsetY = { -it / 2 }) + fadeIn() + scaleIn(initialScale = 0.8f),
    exit = slideOutVertically(targetOffsetY = { -it / 2 }) + fadeOut(),
    modifier = modifier
  ) {
    if (praise == null) return@AnimatedVisibility

    val meaning = when (selectedLang) {
      "EN" -> praise.meaningEn
      "AR" -> praise.meaningBn
      else -> praise.meaningBn
    }

    Surface(
      shape = RoundedCornerShape(20.dp),
      color = Color.White,
      border = BorderStroke(2.dp, Color(0xFF10B981)),
      shadowElevation = 12.dp,
      modifier = Modifier.testTag("praise_feedback_banner")
    ) {
      Box(
        modifier = Modifier
          .background(
            Brush.horizontalGradient(
              listOf(
                Color(0xFFECFDF5),
                Color(0xFFD1FAE5),
                Color(0xFFE0F2FE)
              )
            )
          )
          .padding(horizontal = 20.dp, vertical = 10.dp)
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.Center
        ) {
          Text(text = praise.iconEmoji, fontSize = 26.sp)
          Spacer(modifier = Modifier.width(10.dp))
          Column(horizontalAlignment = Alignment.Start) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = praise.arabicText,
                style = TextStyle(
                  fontSize = 20.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color(0xFF047857)
                )
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "• ${praise.bengaliText}",
                style = TextStyle(
                  fontSize = 14.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color(0xFF0F172A)
                )
              )
            }
            Text(
              text = meaning,
              style = TextStyle(
                fontSize = 11.5.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF334155)
              )
            )
          }
        }
      }
    }
  }
}

// =============================================================================
// 4. LEVEL COMPLETION SCREEN WITH ARABIC CULTURAL TITLES & MAKHRAJ REVIEW
// =============================================================================

data class GameLevelSummary(
  val gameType: GameType,
  val levelNumber: Int,
  val correctAnswersCount: Int, // out of 20
  val totalQuestions: Int = 20,
  val pointsEarned: Int,
  val durationSeconds: Long,
  val encounteredLetters: List<ArabicLetter>
)

@Composable
fun LevelCompletionDialog(
  summary: GameLevelSummary,
  selectedLang: String,
  soundManager: SoundManager,
  onNextLevel: () -> Unit,
  onReplay: () -> Unit,
  onBackToRoadmap: () -> Unit
) {
  val title = ArabicCulturalTitle.fromScore(summary.correctAnswersCount, summary.totalQuestions)
  val stars = ArabicCulturalTitle.calculateStars(summary.correctAnswersCount, summary.totalQuestions)
  val hasPassed = stars >= 1

  val minutes = summary.durationSeconds / 60
  val seconds = summary.durationSeconds % 60
  val timeSpentFormatted = when (selectedLang) {
    "BN" -> "${toBnDigits(minutes)} মি. ${toBnDigits(seconds)} সে."
    "AR" -> "${toArDigits(minutes)} د ${toArDigits(seconds)} ث"
    else -> "${minutes}m ${seconds}s"
  }

  // Trigger celebration audio on appear
  LaunchedEffect(Unit) {
    if (hasPassed) {
      soundManager.playSuccessChime()
      delay(400)
      soundManager.playArabicText(title.titleAr)
    } else {
      soundManager.playErrorBuzzer()
    }
  }

  Dialog(
    onDismissRequest = onBackToRoadmap,
    properties = DialogProperties(usePlatformDefaultWidth = false)
  ) {
    Box(
      modifier = Modifier
        .fillMaxSize()
        .background(Color.Black.copy(alpha = 0.65f))
        .padding(16.dp),
      contentAlignment = Alignment.Center
    ) {
      // Confetti burst for passing score
      if (hasPassed) {
        ConfettiCanvas(triggerKey = summary.levelNumber)
      }

      Card(
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 16.dp),
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 24.dp)
          .testTag("level_completion_card")
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          // Top Header & Close
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Surface(
              shape = RoundedCornerShape(10.dp),
              color = title.primaryColor.copy(alpha = 0.12f),
              border = BorderStroke(1.dp, title.primaryColor.copy(alpha = 0.4f))
            ) {
              Text(
                text = when (selectedLang) {
                  "BN" -> "লেভেল ${toBnDigits(summary.levelNumber)} সমাপ্ত"
                  "AR" -> "اكتمل المستوى ${toArDigits(summary.levelNumber)}"
                  else -> "Level ${summary.levelNumber} Completed"
                },
                style = TextStyle(
                  fontSize = 12.sp,
                  fontWeight = FontWeight.Bold,
                  color = title.secondaryColor
                ),
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
              )
            }

            IconButton(onClick = onBackToRoadmap) {
              Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = Color(0xFF64748B))
            }
          }

          Spacer(modifier = Modifier.height(10.dp))

          // 3-Star Rating Animation
          Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
          ) {
            for (i in 1..3) {
              val isEarned = i <= stars
              Icon(
                imageVector = Icons.Default.Star,
                contentDescription = "Star $i",
                tint = if (isEarned) Color(0xFFF59E0B) else Color(0xFFE2E8F0),
                modifier = Modifier
                  .size(if (i == 2) 48.dp else 40.dp)
                  .padding(horizontal = 4.dp)
              )
            }
          }

          Spacer(modifier = Modifier.height(12.dp))

          // Dynamic Arabic Cultural Badge / Title
          Surface(
            shape = RoundedCornerShape(18.dp),
            color = title.primaryColor.copy(alpha = 0.08f),
            border = BorderStroke(1.5.dp, title.primaryColor),
            modifier = Modifier.fillMaxWidth().testTag("cultural_title_badge")
          ) {
            Column(
              modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
              horizontalAlignment = Alignment.CenterHorizontally
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = title.emoji, fontSize = 24.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                  Text(
                    text = title.titleAr,
                    style = TextStyle(
                      fontSize = 20.sp,
                      fontWeight = FontWeight.Bold,
                      color = title.secondaryColor
                    )
                  )
                  Text(
                    text = title.getTitle(selectedLang),
                    style = TextStyle(
                      fontSize = 15.sp,
                      fontWeight = FontWeight.ExtraBold,
                      color = title.primaryColor
                    )
                  )
                }
              }

              Spacer(modifier = Modifier.height(6.dp))

              Text(
                text = title.getSubtitle(selectedLang),
                style = TextStyle(
                  fontSize = 12.sp,
                  color = Color(0xFF334155),
                  textAlign = TextAlign.Center
                ),
                modifier = Modifier.padding(horizontal = 8.dp)
              )
            }
          }

          Spacer(modifier = Modifier.height(14.dp))

          // Stats Metric Grid: Correct / Points / Time
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
          ) {
            StatMetricPill(
              label = when (selectedLang) { "EN" -> "Score" "AR" -> "النتيجة" else -> "সঠিক উত্তর" },
              value = when (selectedLang) {
                "BN" -> "${toBnDigits(summary.correctAnswersCount)} / ২০"
                "AR" -> "${toArDigits(summary.correctAnswersCount)} / ٢٠"
                else -> "${summary.correctAnswersCount} / 20"
              },
              iconEmoji = "🎯",
              color = Color(0xFF0284C7)
            )

            StatMetricPill(
              label = when (selectedLang) { "EN" -> "Coins" "AR" -> "النقاط" else -> "পয়েন্ট" },
              value = when (selectedLang) {
                "BN" -> "+${toBnDigits(summary.pointsEarned)}"
                "AR" -> "+${toArDigits(summary.pointsEarned)}"
                else -> "+${summary.pointsEarned}"
              },
              iconEmoji = "🪙",
              color = Color(0xFFD97706)
            )

            StatMetricPill(
              label = when (selectedLang) { "EN" -> "Time" "AR" -> "الوقت" else -> "সময়" },
              value = timeSpentFormatted,
              iconEmoji = "⏱️",
              color = Color(0xFF7C3AED)
            )
          }

          Spacer(modifier = Modifier.height(14.dp))

          // Makhraj Review Section
          Text(
            text = when (selectedLang) {
              "EN" -> "Makhraj & Letter Review (Tap to listen)"
              "AR" -> "مراجعة مخارج الحروف (اضغط للاستماع)"
              else -> "মাখরাজ ও হরফ পর্যালোচনা (শুনতে ট্যাপ করুন)"
            },
            style = TextStyle(
              fontSize = 12.5.sp,
              fontWeight = FontWeight.Bold,
              color = Color(0xFF0F172A)
            ),
            modifier = Modifier.fillMaxWidth()
          )

          Spacer(modifier = Modifier.height(6.dp))

          val lettersList = summary.encounteredLetters.distinctBy { it.id }.take(6)
          Surface(
            shape = RoundedCornerShape(14.dp),
            color = Color(0xFFF8FAFC),
            border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
            modifier = Modifier
              .fillMaxWidth()
              .height(if (lettersList.size > 3) 120.dp else 75.dp)
          ) {
            LazyColumn(
              contentPadding = PaddingValues(8.dp),
              verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
              items(lettersList) { letter ->
                Row(
                  modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.White)
                    .border(0.5.dp, Color(0xFFCBD5E1), RoundedCornerShape(10.dp))
                    .clickable { soundManager.speakArabicLetter(letter.letter) }
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.SpaceBetween
                ) {
                  Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                      text = letter.letter,
                      style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = RoyalEmerald
                      )
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                      Text(
                        text = "${letter.nameBn} (${letter.nameEn})",
                        style = TextStyle(
                          fontSize = 12.sp,
                          fontWeight = FontWeight.Bold,
                          color = Color(0xFF0F172A)
                        )
                      )
                      Text(
                        text = letter.getMakhraj(selectedLang),
                        style = TextStyle(
                          fontSize = 10.5.sp,
                          color = Color(0xFF64748B)
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                      )
                    }
                  }

                  Icon(
                    imageVector = Icons.Default.VolumeUp,
                    contentDescription = "Pronounce",
                    tint = RoyalEmerald,
                    modifier = Modifier.size(18.dp)
                  )
                }
              }
            }
          }

          Spacer(modifier = Modifier.height(18.dp))

          // Action Buttons: Next Level & Replay
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            OutlinedButton(
              onClick = onReplay,
              shape = RoundedCornerShape(12.dp),
              modifier = Modifier
                .weight(1f)
                .testTag("btn_replay_level")
            ) {
              Icon(imageVector = Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = when (selectedLang) {
                  "EN" -> "Replay"
                  "AR" -> "إعادة"
                  else -> "পুনরায় খেলুন"
                },
                fontSize = 12.5.sp
              )
            }

            if (hasPassed && summary.levelNumber < 20) {
              Button(
                onClick = onNextLevel,
                colors = ButtonDefaults.buttonColors(containerColor = RoyalEmerald),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                  .weight(1.3f)
                  .testTag("btn_next_level")
              ) {
                Text(
                  text = when (selectedLang) {
                    "EN" -> "Next Level"
                    "AR" -> "المستوى التالي"
                    else -> "পরবর্তী লেভেল"
                  },
                  fontSize = 13.sp,
                  fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(6.dp))
                Icon(
                  imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                  contentDescription = null,
                  modifier = Modifier.size(16.dp)
                )
              }
            } else {
              Button(
                onClick = onBackToRoadmap,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0284C7)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                  .weight(1.3f)
                  .testTag("btn_back_to_roadmap")
              ) {
                Text(
                  text = when (selectedLang) {
                    "EN" -> "Back to Roadmap"
                    "AR" -> "العودة للمسار"
                    else -> "রোডম্যাপে ফিরুন"
                  },
                  fontSize = 13.sp,
                  fontWeight = FontWeight.Bold
                )
              }
            }
          }
        }
      }
    }
  }
}

@Composable
private fun StatMetricPill(
  label: String,
  value: String,
  iconEmoji: String,
  color: Color
) {
  Surface(
    shape = RoundedCornerShape(12.dp),
    color = color.copy(alpha = 0.08f),
    border = BorderStroke(0.8.dp, color.copy(alpha = 0.3f)),
    modifier = Modifier.width(96.dp)
  ) {
    Column(
      modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Text(text = iconEmoji, fontSize = 12.sp)
        Spacer(modifier = Modifier.width(3.dp))
        Text(
          text = label,
          style = TextStyle(
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF64748B)
          ),
          maxLines = 1
        )
      }
      Spacer(modifier = Modifier.height(2.dp))
      Text(
        text = value,
        style = TextStyle(
          fontSize = 13.sp,
          fontWeight = FontWeight.ExtraBold,
          color = color
        ),
        maxLines = 1
      )
    }
  }
}
