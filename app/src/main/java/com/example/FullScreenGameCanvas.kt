package com.example

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.platform.testTag
import com.example.ui.theme.RoyalEmerald
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Immersive Full-Screen Gameplay Canvas:
 * - Top Bar UI: Back Button, Dynamic Progress Bar & Text (e.g. 'প্রশ্ন ১৫ / ২০'), Level Badge, Live Star/Score Counter
 * - Main Canvas Area: Maximized interactive screen area for high-resolution graphics, animated cards, responsive touch
 * - Algorithmic 20 questions auto-shuffled dynamically
 * - Progressive Arabic Cultural Audio Praise Engine (Marhaba, BarakAllah Fik, MashaAllah, JazakAllah Khair, Muntazun Jiddan)
 * - Confetti celebration, letter Makhraj audio, green/red feedback glow, streak combos
 * - Level Completion Summary Modal with Arabic Cultural Titles & Makhraj Review
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FullScreenGameCanvas(
  gameMode: GameMode,
  initialLevelNumber: Int,
  selectedLang: String,
  soundManager: SoundManager,
  onExit: () -> Unit
) {
  val context = LocalContext.current
  val hapticFeedback = LocalHapticFeedback.current
  val progressRepo = remember { OfflineProgressRepository.getInstance(context) }
  val coroutineScope = rememberCoroutineScope()

  var currentLevelNumber by remember { mutableIntStateOf(initialLevelNumber) }
  var questions by remember(currentLevelNumber) {
    mutableStateOf(GameQuestionGenerator.generateQuestionsForLevel(gameMode.type, currentLevelNumber))
  }
  var currentQuestionIndex by remember(currentLevelNumber) { mutableIntStateOf(0) }
  var selectedOptionId by remember { mutableStateOf<String?>(null) }
  var isAnswerCorrect by remember { mutableStateOf<Boolean?>(null) }
  var isAnsweringLocked by remember { mutableStateOf(false) }

  var currentStreak by remember { mutableIntStateOf(0) }
  var correctAnswersCount by remember { mutableIntStateOf(0) }
  var liveScore by remember { mutableIntStateOf(0) }

  var activePraise by remember { mutableStateOf<ArabicCulturalPraise?>(null) }
  var showPraiseBanner by remember { mutableStateOf(false) }
  var confettiTriggerKey by remember { mutableStateOf<Any?>(null) }

  val encounteredLetters = remember(currentLevelNumber) { mutableListOf<ArabicLetter>() }
  var levelStartTimeMillis by remember(currentLevelNumber) { mutableLongStateOf(System.currentTimeMillis()) }
  var levelCompletionSummary by remember { mutableStateOf<GameLevelSummary?>(null) }

  // Current Question
  val currentQuestion = questions.getOrNull(currentQuestionIndex) ?: return

  // Placed tiles for Form Fuser Word Assembly
  val placedFormFuserOptions = remember(currentQuestionIndex) { mutableStateListOf<GameOption>() }

  // Mix & Match (মেমোরি কার্ড ফ্লিপ) Card State Management
  val matchedPairIds = remember(currentQuestionIndex) { mutableStateListOf<String>() }
  val flippedCardIds = remember(currentQuestionIndex) { mutableStateListOf<String>() }
  val mismatchedCardIds = remember(currentQuestionIndex) { mutableStateListOf<String>() }
  var isCardInteractionLocked by remember(currentQuestionIndex) { mutableStateOf(false) }
  var attemptsInRound by remember(currentQuestionIndex) { mutableIntStateOf(0) }

  // Preview Reveal Memory Phase (অক্ষরগুলো ৩ সেকেন্ড উন্মুক্ত রাখা)
  var isPreviewMemoryPhase by remember(currentQuestionIndex) {
    mutableStateOf(gameMode.type == GameType.MIX_AND_MATCH)
  }
  var previewCountdownSeconds by remember(currentQuestionIndex) {
    mutableIntStateOf(3)
  }

  LaunchedEffect(currentQuestionIndex, gameMode.type) {
    if (gameMode.type == GameType.MIX_AND_MATCH) {
      isPreviewMemoryPhase = true
      isCardInteractionLocked = true
      previewCountdownSeconds = 3
      soundManager.playOptionSelect()
      while (previewCountdownSeconds > 0) {
        delay(1000L)
        previewCountdownSeconds -= 1
      }
      // After 3 seconds, flip all cards face-down simultaneously and activate interactive tapping
      isPreviewMemoryPhase = false
      isCardInteractionLocked = false
      soundManager.playOptionSelect()
    }
  }

  // Keep track of letters encountered
  LaunchedEffect(currentQuestion) {
    if (!encounteredLetters.any { it.id == currentQuestion.targetLetter.id }) {
      encounteredLetters.add(currentQuestion.targetLetter)
    }
  }

  // Time Attack Timer (Levels 11..20)
  var remainingTimeSeconds by remember(currentQuestionIndex, currentLevelNumber) {
    mutableIntStateOf(currentQuestion.timeLimitSeconds)
  }

  LaunchedEffect(currentQuestionIndex, currentLevelNumber, isAnsweringLocked, isPreviewMemoryPhase) {
    if (currentQuestion.isTimeAttack && !isAnsweringLocked && !isPreviewMemoryPhase) {
      remainingTimeSeconds = currentQuestion.timeLimitSeconds
      while (remainingTimeSeconds > 0 && !isAnsweringLocked && !isPreviewMemoryPhase) {
        delay(1000L)
        remainingTimeSeconds -= 1
      }
      if (remainingTimeSeconds <= 0 && !isAnsweringLocked && !isPreviewMemoryPhase) {
        // Time out! Treat as incorrect
        isAnsweringLocked = true
        isAnswerCorrect = false
        currentStreak = 0
        hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
        soundManager.playErrorBuzzer()

        delay(1300L)
        if (currentQuestionIndex < 19) {
          currentQuestionIndex += 1
          selectedOptionId = null
          isAnswerCorrect = null
          isAnsweringLocked = false
        } else {
          // Level Completed
          val durationSec = (System.currentTimeMillis() - levelStartTimeMillis) / 1000L
          val stars = ArabicCulturalTitle.calculateStars(correctAnswersCount, 20)
          progressRepo.completeGameLevel(gameMode.type, currentLevelNumber, stars, liveScore)
          levelCompletionSummary = GameLevelSummary(
            gameType = gameMode.type,
            levelNumber = currentLevelNumber,
            correctAnswersCount = correctAnswersCount,
            totalQuestions = 20,
            pointsEarned = liveScore,
            durationSeconds = durationSec,
            encounteredLetters = encounteredLetters.toList()
          )
        }
      }
    }
  }

  val tier = GameDifficultyTier.fromLevel(currentLevelNumber)

  val shakeOffsets = remember(currentQuestionIndex) {
    List(16) { Animatable(0f) }
  }

  Scaffold(
    containerColor = Color(0xFFF8FAFC),
    topBar = {
      TopAppBar(
        title = {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
          ) {
            // Level Badge
            Surface(
              shape = RoundedCornerShape(10.dp),
              color = Color.White.copy(alpha = 0.25f),
              border = BorderStroke(1.dp, Color.White.copy(alpha = 0.4f))
            ) {
              Text(
                text = when (selectedLang) {
                  "BN" -> "লেভেল ${toBnDigits(currentLevelNumber)}"
                  "AR" -> "مستوى ${toArDigits(currentLevelNumber)}"
                  else -> "Level $currentLevelNumber"
                },
                style = TextStyle(
                  fontSize = 12.sp,
                  fontWeight = FontWeight.ExtraBold,
                  color = Color.White
                ),
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
              )
            }

            Spacer(modifier = Modifier.width(10.dp))

            // Dynamic Question Progress Counter: 'প্রশ্ন ১৫ / ২০'
            Text(
              text = when (selectedLang) {
                "BN" -> "প্রশ্ন ${toBnDigits(currentQuestionIndex + 1)} / ২০"
                "AR" -> "السؤال ${toArDigits(currentQuestionIndex + 1)} / ٢٠"
                else -> "Question ${currentQuestionIndex + 1} / 20"
              },
              style = TextStyle(
                fontSize = 14.5.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
              ),
              modifier = Modifier.weight(1f)
            )

            // Live Score / Stars Pill
            Surface(
              shape = RoundedCornerShape(12.dp),
              color = Color(0xFF0F172A).copy(alpha = 0.35f),
              border = BorderStroke(1.dp, Color(0xFFFDE68A).copy(alpha = 0.5f))
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
              ) {
                Text(text = "🪙", fontSize = 12.sp)
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                  text = when (selectedLang) {
                    "BN" -> toBnDigits(liveScore)
                    "AR" -> toArDigits(liveScore)
                    else -> "$liveScore"
                  },
                  style = TextStyle(
                    fontSize = 13.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFFFDE68A)
                  )
                )
              }
            }
          }
        },
        navigationIcon = {
          IconButton(onClick = onExit) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowBack,
              contentDescription = "Back",
              tint = Color.White
            )
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = gameMode.primaryColor)
      )
    }
  ) { padding ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(padding)
    ) {
      Column(
        modifier = Modifier
          .fillMaxSize()
          .verticalScroll(rememberScrollState())
          .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        // 1. DYNAMIC PROGRESS BAR (20 Questions)
        val progressFraction = (currentQuestionIndex + 1).toFloat() / 20f
        val animatedProgress by animateFloatAsState(
          targetValue = progressFraction,
          animationSpec = tween(300, easing = FastOutSlowInEasing),
          label = "game_progress"
        )

        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically
        ) {
          LinearProgressIndicator(
            progress = { animatedProgress },
            color = gameMode.secondaryColor,
            trackColor = Color(0xFFE2E8F0),
            modifier = Modifier
              .weight(1f)
              .height(8.dp)
              .clip(RoundedCornerShape(4.dp))
              .testTag("game_progress_bar")
          )

          if (currentQuestion.isTimeAttack) {
            Spacer(modifier = Modifier.width(10.dp))
            Surface(
              shape = RoundedCornerShape(8.dp),
              color = if (remainingTimeSeconds <= 4) Color(0xFFFEE2E2) else Color(0xFFFEF3C7),
              border = BorderStroke(1.dp, if (remainingTimeSeconds <= 4) Color(0xFFEF4444) else Color(0xFFF59E0B))
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
              ) {
                Icon(
                  imageVector = Icons.Default.Timer,
                  contentDescription = "Timer",
                  tint = if (remainingTimeSeconds <= 4) Color(0xFFDC2626) else Color(0xFFD97706),
                  modifier = Modifier.size(13.dp)
                )
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                  text = "${remainingTimeSeconds}s",
                  style = TextStyle(
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (remainingTimeSeconds <= 4) Color(0xFFDC2626) else Color(0xFFD97706)
                  )
                )
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Streak Overlay Banner & Praise Feedback
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .height(44.dp),
          contentAlignment = Alignment.Center
        ) {
          StreakOverlayBanner(streakCount = currentStreak)
          ArabicPraiseFeedbackBanner(
            praise = activePraise,
            isVisible = showPraiseBanner,
            selectedLang = selectedLang
          )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 2. MAIN INTERACTIVE CANVAS CARD
        Card(
          shape = RoundedCornerShape(24.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
          border = BorderStroke(
            1.5.dp,
            when (isAnswerCorrect) {
              true -> Color(0xFF10B981)
              false -> Color(0xFFEF4444)
              null -> Color(0xFFE2E8F0)
            }
          ),
          modifier = Modifier
            .fillMaxWidth()
            .shadow(
              elevation = if (isAnswerCorrect == true) 12.dp else 2.dp,
              shape = RoundedCornerShape(24.dp),
              spotColor = if (isAnswerCorrect == true) Color(0xFF10B981) else Color.Transparent
            )
            .testTag("question_canvas_card")
        ) {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            // Tier / Category Pill
            val (tierPillText, pillColor) = if (gameMode.type == GameType.SHAPE_MASTER_PATH) {
              val smTier = ShapeMasterCurriculum.getTierForLevel(currentLevelNumber)
              val smInfo = ShapeMasterCurriculum.getLevelInfo(currentLevelNumber)
              val text = when (selectedLang) {
                "EN" -> "${smTier.iconEmoji} ${smTier.titleEn} • ${smInfo.nameEn}"
                "AR" -> "${smTier.iconEmoji} ${smTier.titleAr} • ${smInfo.nameAr}"
                else -> "${smTier.iconEmoji} ${smTier.titleBn} • ${smInfo.nameBn}"
              }
              Pair(text, smTier.badgeColor)
            } else if (gameMode.type == GameType.DOT_MASTER) {
              val nmTier = NuktaMasterCurriculum.getTierForLevel(currentLevelNumber)
              val nmInfo = NuktaMasterCurriculum.getLevelInfo(currentLevelNumber)
              val text = when (selectedLang) {
                "EN" -> "${nmTier.iconEmoji} ${nmTier.titleEn} • ${nmInfo.nameEn}"
                "AR" -> "${nmTier.iconEmoji} ${nmTier.titleAr} • ${nmInfo.nameAr}"
                else -> "${nmTier.iconEmoji} ${nmTier.titleBn} • ${nmInfo.nameBn}"
              }
              Pair(text, nmTier.badgeColor)
            } else if (gameMode.type == GameType.LETTER_LINK) {
              val llTier = LetterLinkCurriculum.getTierForLevel(currentLevelNumber)
              val llInfo = LetterLinkCurriculum.getLevelInfo(currentLevelNumber)
              val text = when (selectedLang) {
                "EN" -> "${llTier.iconEmoji} ${llTier.titleEn} • ${llInfo.nameEn}"
                "AR" -> "${llTier.iconEmoji} ${llTier.titleAr} • ${llInfo.nameAr}"
                else -> "${llTier.iconEmoji} ${llTier.titleBn} • ${llInfo.nameBn}"
              }
              Pair(text, llTier.badgeColor)
            } else if (gameMode.type == GameType.FORM_FUSER) {
              val ffTier = FormFuserCurriculum.getTierForLevel(currentLevelNumber)
              val ffInfo = FormFuserCurriculum.getLevelInfo(currentLevelNumber)
              val text = when (selectedLang) {
                "EN" -> "${ffTier.iconEmoji} ${ffTier.titleEn} • ${ffInfo.nameEn}"
                "AR" -> "${ffTier.iconEmoji} ${ffTier.titleAr} • ${ffInfo.nameAr}"
                else -> "${ffTier.iconEmoji} ${ffTier.titleBn} • ${ffInfo.nameBn}"
              }
              Pair(text, ffTier.badgeColor)
            } else if (gameMode.type == GameType.MIX_AND_MATCH) {
              val mmTier = MixAndMatchCurriculum.getTierForLevel(currentLevelNumber)
              val mmInfo = MixAndMatchCurriculum.getLevelInfo(currentLevelNumber)
              val text = when (selectedLang) {
                "EN" -> "${mmTier.iconEmoji} ${mmTier.titleEn} • ${mmInfo.nameEn}"
                "AR" -> "${mmTier.iconEmoji} ${mmTier.titleAr} • ${mmInfo.nameAr}"
                else -> "${mmTier.iconEmoji} ${mmTier.titleBn} • ${mmInfo.nameBn}"
              }
              Pair(text, mmTier.badgeColor)
            } else {
              val text = "${tier.iconEmoji} " + when (selectedLang) {
                "EN" -> tier.titleEn
                "AR" -> tier.titleAr
                else -> tier.titleBn
              }
              Pair(text, tier.badgeColor)
            }

            Surface(
              shape = RoundedCornerShape(8.dp),
              color = pillColor.copy(alpha = 0.1f),
              border = BorderStroke(0.8.dp, pillColor.copy(alpha = 0.3f))
            ) {
              Text(
                text = tierPillText,
                style = TextStyle(
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  color = pillColor
                ),
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
              )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Big High-Resolution Arabic Glyph / Display
            if (gameMode.type == GameType.FORM_FUSER) {
              // Form Fuser Target Challenge Display
              Surface(
                shape = RoundedCornerShape(20.dp),
                color = if (isAnswerCorrect == true) Color(0xFFECFDF5) else Color(0xFFF1F5F9),
                border = BorderStroke(
                  if (isAnswerCorrect == true) 2.dp else 1.dp,
                  if (isAnswerCorrect == true) Color(0xFF10B981) else Color(0xFFCBD5E1)
                ),
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(horizontal = 8.dp)
                  .clickable {
                    soundManager.playArabicText(currentQuestion.displayArabic)
                  }
                  .testTag("form_fuser_target_card")
              ) {
                Box(
                  modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 14.dp, horizontal = 16.dp),
                  contentAlignment = Alignment.Center
                ) {
                  if (isAnswerCorrect == true) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                      Text(
                        text = currentQuestion.displayArabic,
                        style = TextStyle(
                          fontSize = 44.sp,
                          fontWeight = FontWeight.Bold,
                          color = Color(0xFF047857),
                          textAlign = TextAlign.Center
                        )
                      )
                      Spacer(modifier = Modifier.height(2.dp))
                      Text(
                        text = "${currentQuestion.targetWordMeaningBn} • ${currentQuestion.targetWordPhonetic}",
                        style = TextStyle(
                          fontSize = 13.sp,
                          fontWeight = FontWeight.SemiBold,
                          color = Color(0xFF065F46)
                        )
                      )
                    }
                  } else {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                      Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                      ) {
                        Text(
                          text = "🧩 লক্ষ্য শব্দার্থ: ",
                          style = TextStyle(
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF64748B)
                          )
                        )
                        Text(
                          text = currentQuestion.targetWordMeaningBn,
                          style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A)
                          )
                        )
                      }
                      Spacer(modifier = Modifier.height(4.dp))
                      Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                      ) {
                        Surface(
                          shape = RoundedCornerShape(6.dp),
                          color = Color(0xFFE2E8F0)
                        ) {
                          Text(
                            text = "[ ${currentQuestion.targetWordPhonetic} ]",
                            style = TextStyle(
                              fontSize = 12.sp,
                              fontWeight = FontWeight.SemiBold,
                              color = Color(0xFF334155)
                            ),
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                          )
                        }
                        Surface(
                          shape = RoundedCornerShape(6.dp),
                          color = Color(0xFFEFF6FF)
                        ) {
                          Text(
                            text = "${currentQuestion.puzzleLetters.size}টি হরফ",
                            style = TextStyle(
                              fontSize = 11.5.sp,
                              fontWeight = FontWeight.Bold,
                              color = Color(0xFF2563EB)
                            ),
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                          )
                        }
                      }
                    }
                  }

                  // Speaker Icon overlay
                  Icon(
                    imageVector = Icons.Default.VolumeUp,
                    contentDescription = "Play Pronunciation",
                    tint = if (isAnswerCorrect == true) Color(0xFF047857) else RoyalEmerald,
                    modifier = Modifier
                      .align(Alignment.CenterEnd)
                      .size(24.dp)
                  )
                }
              }
            } else if (gameMode.type == GameType.MIX_AND_MATCH) {
              // Mix & Match Interactive Status Banner
              Surface(
                shape = RoundedCornerShape(20.dp),
                color = if (isAnswerCorrect == true) Color(0xFFECFDF5) else Color(0xFFFAF5FF),
                border = BorderStroke(
                  if (isAnswerCorrect == true) 2.dp else 1.dp,
                  if (isAnswerCorrect == true) Color(0xFF10B981) else Color(0xFFDDD6FE)
                ),
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(horizontal = 6.dp)
                  .testTag("mix_match_status_card")
              ) {
                Row(
                  modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp, horizontal = 16.dp),
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.SpaceBetween
                ) {
                  Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                  ) {
                    Surface(
                      shape = CircleShape,
                      color = if (isAnswerCorrect == true) Color(0xFF10B981) else Color(0xFF8B5CF6)
                    ) {
                      Text(
                        text = if (isAnswerCorrect == true) "✓" else "🃏",
                        style = TextStyle(
                          fontSize = 18.sp,
                          color = Color.White
                        ),
                        modifier = Modifier.padding(8.dp)
                      )
                    }
                    Column {
                      Text(
                        text = if (isAnswerCorrect == true) "মাশাল্লাহ! সম্পূর্ণ সফল!" else "মেমোরি কার্ড রিকল",
                        style = TextStyle(
                          fontSize = 14.5.sp,
                          fontWeight = FontWeight.Bold,
                          color = if (isAnswerCorrect == true) Color(0xFF047857) else Color(0xFF4C1D95)
                        )
                      )
                      Text(
                        text = "জোড়া মেলানো: ${matchedPairIds.size} / ${currentQuestion.requiredPairsCount}",
                        style = TextStyle(
                          fontSize = 12.sp,
                          fontWeight = FontWeight.SemiBold,
                          color = if (isAnswerCorrect == true) Color(0xFF065F46) else Color(0xFF6B21A8)
                        )
                      )
                    }
                  }

                  // Attempts Counter & Audio Play
                  Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                  ) {
                    Surface(
                      shape = RoundedCornerShape(8.dp),
                      color = Color(0xFFF3E8FF),
                      border = BorderStroke(1.dp, Color(0xFFC084FC).copy(alpha = 0.4f))
                    ) {
                      Text(
                        text = "চেষ্টা: ${attemptsInRound}",
                        style = TextStyle(
                          fontSize = 11.5.sp,
                          fontWeight = FontWeight.Bold,
                          color = Color(0xFF6B21A8)
                        ),
                        modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
                      )
                    }

                    IconButton(
                      onClick = {
                        soundManager.speakArabicLetter(currentQuestion.targetLetter.letter)
                      },
                      modifier = Modifier.size(32.dp)
                    ) {
                      Icon(
                        imageVector = Icons.Default.VolumeUp,
                        contentDescription = "Play Pronunciation",
                        tint = Color(0xFF8B5CF6),
                        modifier = Modifier.size(20.dp)
                      )
                    }
                  }
                }
              }
            } else {
              Surface(
                shape = RoundedCornerShape(20.dp),
                color = Color(0xFFF1F5F9),
                border = BorderStroke(1.dp, Color(0xFFCBD5E1)),
                modifier = Modifier
                  .size(120.dp)
                  .clickable {
                    if (currentQuestion.displayArabic.length > 2) {
                      soundManager.playArabicText(currentQuestion.displayArabic)
                    } else {
                      soundManager.speakArabicLetter(currentQuestion.targetLetter.letter)
                    }
                  }
                  .testTag("arabic_glyph_canvas")
              ) {
                Box(contentAlignment = Alignment.Center) {
                  Text(
                    text = currentQuestion.displayArabic,
                    style = TextStyle(
                      fontSize = if (currentQuestion.displayArabic.length > 2) 36.sp else 54.sp,
                      fontWeight = FontWeight.Bold,
                      color = Color(0xFF0F172A),
                      textAlign = TextAlign.Center
                    )
                  )

                  // Speaker Icon overlay
                  Icon(
                    imageVector = Icons.Default.VolumeUp,
                    contentDescription = "Play Pronunciation",
                    tint = RoyalEmerald,
                    modifier = Modifier
                      .align(Alignment.BottomEnd)
                      .padding(8.dp)
                      .size(20.dp)
                  )
                }
              }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Question Prompt in Selected Language
            Text(
              text = currentQuestion.getPrompt(selectedLang),
              style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F172A),
                textAlign = TextAlign.Center
              ),
              modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
            )

            // Explanation (shown after answering)
            if (isAnswerCorrect != null) {
              Spacer(modifier = Modifier.height(8.dp))
              Text(
                text = when (selectedLang) {
                  "EN" -> currentQuestion.explanationEn
                  else -> currentQuestion.explanationBn
                },
                style = TextStyle(
                  fontSize = 12.sp,
                  fontWeight = FontWeight.Medium,
                  color = if (isAnswerCorrect == true) Color(0xFF047857) else Color(0xFFB91C1C),
                  textAlign = TextAlign.Center
                ),
                modifier = Modifier
                  .fillMaxWidth()
                  .background(
                    if (isAnswerCorrect == true) Color(0xFFECFDF5) else Color(0xFFFEF2F2),
                    RoundedCornerShape(8.dp)
                  )
                  .padding(8.dp)
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // 3. RESPONSIVE TOUCH MULTIPLE CHOICE OPTIONS (Interactive Grid or Column)
        if (gameMode.type == GameType.LETTER_LINK) {
          // INTERACTIVE MATCH GRID FOR LETTER LINK (2x2 for lv1-5, 3x3 for lv6-10, 4x4 for lv11-20)
          val columns = when (currentLevelNumber) {
            in 1..5 -> 2
            in 6..10 -> 3
            else -> 4
          }
          val optionRows = currentQuestion.options.chunked(columns)

          Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            optionRows.forEachIndexed { rowIndex, rowOptions ->
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
              ) {
                rowOptions.forEach { option ->
                  val globalIndex = currentQuestion.options.indexOfFirst { it.id == option.id }
                  val isSelected = selectedOptionId == option.id
                  val isCorrectOption = option.id == currentQuestion.correctOptionId

                  val cardBorderColor by animateColorAsState(
                    targetValue = when {
                      isSelected && isAnswerCorrect == true -> Color(0xFF10B981)
                      isSelected && isAnswerCorrect == false -> Color(0xFFEF4444)
                      isAnswerCorrect != null && isCorrectOption -> Color(0xFF10B981)
                      isSelected -> gameMode.primaryColor
                      else -> Color(0xFFE2E8F0)
                    },
                    label = "grid_card_border"
                  )

                  val cardContainerColor by animateColorAsState(
                    targetValue = when {
                      isSelected && isAnswerCorrect == true -> Color(0xFFECFDF5)
                      isSelected && isAnswerCorrect == false -> Color(0xFFFEF2F2)
                      isAnswerCorrect != null && isCorrectOption -> Color(0xFFF0FDF4)
                      isSelected -> Color(0xFFF8FAFC)
                      else -> Color.White
                    },
                    label = "grid_card_bg"
                  )

                  val cardScale by animateFloatAsState(
                    targetValue = if (isSelected && isAnswerCorrect == true) 1.05f else 1f,
                    label = "grid_card_scale"
                  )

                  Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = cardContainerColor),
                    elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 6.dp else 2.dp),
                    border = BorderStroke(if (isSelected || (isAnswerCorrect != null && isCorrectOption)) 2.dp else 1.2.dp, cardBorderColor),
                    modifier = Modifier
                      .weight(1f)
                      .offset(x = if (globalIndex in shakeOffsets.indices) shakeOffsets[globalIndex].value.dp else 0.dp)
                      .scale(cardScale)
                      .clickable(enabled = !isAnsweringLocked) {
                        if (isAnsweringLocked) return@clickable
                        selectedOptionId = option.id
                        isAnsweringLocked = true

                        val correct = (option.id == currentQuestion.correctOptionId)
                        isAnswerCorrect = correct

                        if (!correct && globalIndex in shakeOffsets.indices) {
                          coroutineScope.launch {
                            shakeOffsets[globalIndex].animateTo(
                              targetValue = 0f,
                              animationSpec = keyframes {
                                durationMillis = 350
                                -12f at 40
                                12f at 80
                                -9f at 130
                                9f at 180
                                -6f at 230
                                6f at 280
                                0f at 350
                              }
                            )
                          }
                        }

                        coroutineScope.launch {
                          if (correct) {
                            correctAnswersCount += 1
                            currentStreak += 1
                            liveScore += 15 + (currentStreak * 5)
                            confettiTriggerKey = currentQuestionIndex

                            val praise = ArabicPraiseEngine.getPraiseForQuestion(currentQuestionIndex + 1)
                            activePraise = praise
                            showPraiseBanner = true

                            soundManager.playSuccessChime()
                            delay(300L)
                            if (currentQuestion.displayArabic.length > 2) {
                              soundManager.playArabicText(currentQuestion.displayArabic)
                            } else {
                              soundManager.speakArabicLetter(currentQuestion.targetLetter.letter)
                            }
                            delay(600L)
                            soundManager.playArabicPraiseForQuestion(currentQuestionIndex + 1)
                          } else {
                            currentStreak = 0
                            hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                            soundManager.playErrorBuzzer()
                          }

                          delay(1600L)
                          showPraiseBanner = false

                          if (currentQuestionIndex < 19) {
                            currentQuestionIndex += 1
                            selectedOptionId = null
                            isAnswerCorrect = null
                            isAnsweringLocked = false
                          } else {
                            val durationSec = (System.currentTimeMillis() - levelStartTimeMillis) / 1000L
                            val stars = ArabicCulturalTitle.calculateStars(correctAnswersCount, 20)
                            progressRepo.completeGameLevel(gameMode.type, currentLevelNumber, stars, liveScore)
                            levelCompletionSummary = GameLevelSummary(
                              gameType = gameMode.type,
                              levelNumber = currentLevelNumber,
                              correctAnswersCount = correctAnswersCount,
                              totalQuestions = 20,
                              pointsEarned = liveScore,
                              durationSeconds = durationSec,
                              encounteredLetters = encounteredLetters.toList()
                            )
                          }
                        }
                      }
                      .testTag("match_grid_card_${option.id}")
                  ) {
                    Column(
                      modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                          vertical = if (columns == 4) 10.dp else if (columns == 3) 12.dp else 16.dp,
                          horizontal = 4.dp
                        ),
                      horizontalAlignment = Alignment.CenterHorizontally,
                      verticalArrangement = Arrangement.Center
                    ) {
                      if (option.arabicDisplay.isNotBlank()) {
                        Text(
                          text = option.arabicDisplay,
                          style = TextStyle(
                            fontSize = if (columns == 4) 28.sp else if (columns == 3) 32.sp else 38.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected && isAnswerCorrect == true) Color(0xFF047857) else Color(0xFF0F172A),
                            textAlign = TextAlign.Center
                          )
                        )
                        Spacer(modifier = Modifier.height(3.dp))
                      }
                      Text(
                        text = option.primaryText,
                        style = TextStyle(
                          fontSize = if (columns == 4) 10.5.sp else 12.sp,
                          fontWeight = FontWeight.Bold,
                          color = if (isSelected && isAnswerCorrect == true) Color(0xFF047857) else Color(0xFF334155),
                          textAlign = TextAlign.Center
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                      )
                      if (option.secondaryText.isNotBlank() && columns <= 3) {
                        Text(
                          text = option.secondaryText,
                          style = TextStyle(
                            fontSize = 10.sp,
                            color = Color(0xFF64748B),
                            textAlign = TextAlign.Center
                          ),
                          maxLines = 1,
                          overflow = TextOverflow.Ellipsis
                        )
                      }
                    }
                  }
                }
              }
            }
          }
        } else if (gameMode.type == GameType.FORM_FUSER) {
          // -------------------------------------------------------------
          // FORM FUSER: WORD ASSEMBLY CANVAS (ডান থেকে বামে শব্দ গঠন)
          // -------------------------------------------------------------
          val targetWordLetters = currentQuestion.puzzleLetters
          val slotCount = targetWordLetters.size

          Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(14.dp)
          ) {
            // Success Connected Fusion Banner (appears when word is fused correctly!)
            AnimatedVisibility(
              visible = isAnswerCorrect == true,
              enter = fadeIn() + scaleIn(),
              exit = fadeOut() + scaleOut()
            ) {
              Surface(
                shape = RoundedCornerShape(18.dp),
                color = Color(0xFFECFDF5),
                border = BorderStroke(2.dp, Color(0xFF10B981)),
                modifier = Modifier
                  .fillMaxWidth()
                  .testTag("form_fuser_success_banner")
              ) {
                Column(
                  modifier = Modifier.padding(14.dp),
                  horizontalAlignment = Alignment.CenterHorizontally
                ) {
                  Text(
                    text = "✨ মারহাবা! হরফগুলো সফলভাবে যুক্ত হয়েছে ✨",
                    style = TextStyle(
                      fontSize = 12.5.sp,
                      fontWeight = FontWeight.Bold,
                      color = Color(0xFF047857)
                    )
                  )
                  Spacer(modifier = Modifier.height(4.dp))
                  Text(
                    text = currentQuestion.displayArabic,
                    style = TextStyle(
                      fontSize = 42.sp,
                      fontWeight = FontWeight.Bold,
                      color = Color(0xFF047857),
                      textAlign = TextAlign.Center
                    )
                  )
                  Text(
                    text = "${currentQuestion.targetWordMeaningBn} (${currentQuestion.targetWordPhonetic})",
                    style = TextStyle(
                      fontSize = 13.5.sp,
                      fontWeight = FontWeight.SemiBold,
                      color = Color(0xFF065F46)
                    )
                  )
                }
              }
            }

            // Word Assembly Slots Header
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "১. শব্দ গঠনের স্লট (ডান ➔ বাম):",
                style = TextStyle(
                  fontSize = 13.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color(0xFF334155)
                )
              )
              Surface(
                shape = RoundedCornerShape(6.dp),
                color = Color(0xFFEFF6FF)
              ) {
                Text(
                  text = "${placedFormFuserOptions.size} / $slotCount হরফ যুক্ত",
                  style = TextStyle(
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2563EB)
                  ),
                  modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp)
                )
              }
            }

            // RTL Word Assembly Drop Slots
            // In Arabic script, slot 0 is on the far right
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .background(Color(0xFFF1F5F9), RoundedCornerShape(16.dp))
                  .padding(vertical = 12.dp, horizontal = 8.dp)
                  .testTag("form_fuser_slots_row"),
                horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically
              ) {
                for (slotIndex in 0 until slotCount) {
                  val isFilled = slotIndex < placedFormFuserOptions.size
                  val placedOption = placedFormFuserOptions.getOrNull(slotIndex)

                  val slotBorderColor by animateColorAsState(
                    targetValue = when {
                      isAnswerCorrect == true -> Color(0xFF10B981)
                      isAnswerCorrect == false -> Color(0xFFEF4444)
                      isFilled -> Color(0xFF3B82F6)
                      else -> Color(0xFFCBD5E1)
                    },
                    label = "ff_slot_border"
                  )

                  val slotBgColor by animateColorAsState(
                    targetValue = when {
                      isAnswerCorrect == true -> Color(0xFFECFDF5)
                      isAnswerCorrect == false -> Color(0xFFFEF2F2)
                      isFilled -> Color(0xFFEFF6FF)
                      else -> Color.White
                    },
                    label = "ff_slot_bg"
                  )

                  Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = slotBgColor,
                    border = BorderStroke(if (isFilled) 2.dp else 1.5.dp, slotBorderColor),
                    shadowElevation = if (isFilled) 3.dp else 0.dp,
                    modifier = Modifier
                      .size(if (slotCount >= 4) 64.dp else 74.dp)
                      .clickable(enabled = isFilled && !isAnsweringLocked) {
                        if (isFilled && !isAnsweringLocked) {
                          placedFormFuserOptions.removeAt(slotIndex)
                          soundManager.playOptionSelect()
                          hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        }
                      }
                      .testTag("ff_slot_$slotIndex")
                  ) {
                    Box(
                      contentAlignment = Alignment.Center,
                      modifier = Modifier.fillMaxSize()
                    ) {
                      if (isFilled && placedOption != null) {
                        Column(
                          horizontalAlignment = Alignment.CenterHorizontally,
                          verticalArrangement = Arrangement.Center
                        ) {
                          Text(
                            text = placedOption.arabicDisplay,
                            style = TextStyle(
                              fontSize = if (slotCount >= 4) 28.sp else 34.sp,
                              fontWeight = FontWeight.Bold,
                              color = if (isAnswerCorrect == true) Color(0xFF047857) else Color(0xFF1E293B)
                            )
                          )
                          Text(
                            text = placedOption.primaryText,
                            style = TextStyle(
                              fontSize = 9.5.sp,
                              fontWeight = FontWeight.Medium,
                              color = Color(0xFF64748B)
                            ),
                            maxLines = 1
                          )
                        }
                      } else {
                        // Empty slot indicator
                        Column(
                          horizontalAlignment = Alignment.CenterHorizontally,
                          verticalArrangement = Arrangement.Center
                        ) {
                          Text(
                            text = "${slotIndex + 1}",
                            style = TextStyle(
                              fontSize = 15.sp,
                              fontWeight = FontWeight.Bold,
                              color = Color(0xFF94A3B8)
                            )
                          )
                          Text(
                            text = "হরফ",
                            style = TextStyle(
                              fontSize = 8.5.sp,
                              color = Color(0xFFCBD5E1)
                            )
                          )
                        }
                      }
                    }
                  }
                }
              }
            }

            // Controls Row (Reset Slots & Listen Target Word)
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              // Reset Placed Letters Button
              Surface(
                shape = RoundedCornerShape(10.dp),
                color = if (placedFormFuserOptions.isNotEmpty() && !isAnsweringLocked) Color(0xFFFEE2E2) else Color(0xFFF1F5F9),
                border = BorderStroke(
                  1.dp,
                  if (placedFormFuserOptions.isNotEmpty() && !isAnsweringLocked) Color(0xFFFCA5A5) else Color(0xFFE2E8F0)
                ),
                modifier = Modifier
                  .clickable(enabled = placedFormFuserOptions.isNotEmpty() && !isAnsweringLocked) {
                    placedFormFuserOptions.clear()
                    soundManager.playOptionSelect()
                  }
                  .testTag("ff_reset_slots_btn")
              ) {
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                  Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Reset Slots",
                    tint = if (placedFormFuserOptions.isNotEmpty() && !isAnsweringLocked) Color(0xFFDC2626) else Color(0xFF94A3B8),
                    modifier = Modifier.size(15.dp)
                  )
                  Spacer(modifier = Modifier.width(4.dp))
                  Text(
                    text = "মুছে ফেলুন (Clear)",
                    style = TextStyle(
                      fontSize = 11.5.sp,
                      fontWeight = FontWeight.SemiBold,
                      color = if (placedFormFuserOptions.isNotEmpty() && !isAnsweringLocked) Color(0xFFDC2626) else Color(0xFF94A3B8)
                    )
                  )
                }
              }

              // Listen Target Word Button
              Surface(
                shape = RoundedCornerShape(10.dp),
                color = Color(0xFFEFF6FF),
                border = BorderStroke(1.dp, Color(0xFFBFDBFE)),
                modifier = Modifier
                  .clickable {
                    soundManager.playArabicText(currentQuestion.displayArabic)
                  }
                  .testTag("ff_listen_word_btn")
              ) {
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                  Icon(
                    imageVector = Icons.Default.VolumeUp,
                    contentDescription = "Listen Word",
                    tint = Color(0xFF2563EB),
                    modifier = Modifier.size(15.dp)
                  )
                  Spacer(modifier = Modifier.width(4.dp))
                  Text(
                    text = "শব্দ শুনুন (Pronounce)",
                    style = TextStyle(
                      fontSize = 11.5.sp,
                      fontWeight = FontWeight.SemiBold,
                      color = Color(0xFF2563EB)
                    )
                  )
                }
              }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // 2. LETTER BANK (হরফ ভাণ্ডার / নিচের হরফ স্পর্শ করে স্লটে বসান)
            Text(
              text = "২. নিচের হরফ নির্বাচন করে সঠিক ক্রমানুসারে বসান:",
              style = TextStyle(
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E293B)
              ),
              modifier = Modifier.fillMaxWidth()
            )

            // Letter Options Grid
            val bankColumns = if (currentQuestion.options.size <= 4) currentQuestion.options.size else 4
            val bankRows = currentQuestion.options.chunked(bankColumns)

            Column(
              modifier = Modifier.fillMaxWidth(),
              verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
              bankRows.forEach { rowOptions ->
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                  rowOptions.forEach { option ->
                    val isPlaced = placedFormFuserOptions.any { it.id == option.id }
                    val canPlace = !isPlaced && !isAnsweringLocked && placedFormFuserOptions.size < slotCount

                    Surface(
                      shape = RoundedCornerShape(14.dp),
                      color = if (isPlaced) Color(0xFFF1F5F9) else Color.White,
                      border = BorderStroke(
                        1.5.dp,
                        if (isPlaced) Color(0xFFCBD5E1) else gameMode.secondaryColor.copy(alpha = 0.5f)
                      ),
                      shadowElevation = if (canPlace) 3.dp else 0.dp,
                      modifier = Modifier
                        .weight(1f)
                        .clickable(enabled = canPlace) {
                          placedFormFuserOptions.add(option)
                          soundManager.playOptionSelect()
                          hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)

                          // Check if all slots are now filled
                          if (placedFormFuserOptions.size == slotCount) {
                            isAnsweringLocked = true
                            val placedLetters = placedFormFuserOptions.map { it.arabicDisplay }
                            val isCorrect = (placedLetters == currentQuestion.puzzleLetters)
                            isAnswerCorrect = isCorrect

                            coroutineScope.launch {
                              if (isCorrect) {
                                correctAnswersCount += 1
                                currentStreak += 1
                                liveScore += 15 + (currentStreak * 5)
                                confettiTriggerKey = currentQuestionIndex

                                val praise = ArabicPraiseEngine.getPraiseForQuestion(currentQuestionIndex + 1)
                                activePraise = praise
                                showPraiseBanner = true

                                soundManager.playSuccessChime()
                                delay(250L)
                                soundManager.playArabicText(currentQuestion.displayArabic)
                                delay(600L)
                                soundManager.playArabicPraiseForQuestion(currentQuestionIndex + 1)

                                delay(1700L)
                                showPraiseBanner = false

                                if (currentQuestionIndex < 19) {
                                  currentQuestionIndex += 1
                                  selectedOptionId = null
                                  isAnswerCorrect = null
                                  isAnsweringLocked = false
                                } else {
                                  // Complete Level
                                  val durationSec = (System.currentTimeMillis() - levelStartTimeMillis) / 1000L
                                  val stars = ArabicCulturalTitle.calculateStars(correctAnswersCount, 20)
                                  progressRepo.completeGameLevel(gameMode.type, currentLevelNumber, stars, liveScore)
                                  levelCompletionSummary = GameLevelSummary(
                                    gameType = gameMode.type,
                                    levelNumber = currentLevelNumber,
                                    correctAnswersCount = correctAnswersCount,
                                    totalQuestions = 20,
                                    pointsEarned = liveScore,
                                    durationSeconds = durationSec,
                                    encounteredLetters = encounteredLetters.toList()
                                  )
                                }
                              } else {
                                currentStreak = 0
                                hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                                soundManager.playErrorBuzzer()

                                delay(950L)
                                placedFormFuserOptions.clear()
                                isAnswerCorrect = null
                                isAnsweringLocked = false
                              }
                            }
                          }
                        }
                        .testTag("ff_bank_tile_${option.id}")
                    ) {
                      Column(
                        modifier = Modifier
                          .fillMaxWidth()
                          .padding(vertical = 10.dp, horizontal = 4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                      ) {
                        Text(
                          text = option.arabicDisplay,
                          style = TextStyle(
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isPlaced) Color(0xFF94A3B8) else Color(0xFF0F172A),
                            textAlign = TextAlign.Center
                          )
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                          text = option.primaryText,
                          style = TextStyle(
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isPlaced) Color(0xFF94A3B8) else Color(0xFF475569),
                            textAlign = TextAlign.Center
                          ),
                          maxLines = 1,
                          overflow = TextOverflow.Ellipsis
                        )
                      }
                    }
                  }
                }
              }
            }
          }
        } else if (gameMode.type == GameType.MIX_AND_MATCH) {
          // ===================================================================
          // 5. MIX & MATCH: DEDICATED MEMORY CARD FLIP CANVAS (মিক্স অ্যান্ড ম্যাচ)
          // ===================================================================
          val memoryCards = currentQuestion.memoryCards
          val totalCards = memoryCards.size
          val columnsCount = when {
            totalCards <= 4 -> 2
            totalCards == 6 -> 3 // 2x3 Grid (3 columns x 2 rows)
            totalCards == 8 -> 4 // 2x4 Grid (4 columns x 2 rows)
            else -> 4 // 3x4 Grid (4 columns x 3 rows for 10-12 cards)
          }

          val maxAttemptsForRound = when (currentQuestion.requiredPairsCount) {
            3 -> 6
            4 -> 8
            5 -> 10
            6 -> 12
            else -> (currentQuestion.requiredPairsCount * 2).coerceAtLeast(6)
          }
          val remainingAttempts = (maxAttemptsForRound - attemptsInRound).coerceAtLeast(0)

          Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            // 1. Preview Reveal Memory Phase (অক্ষরগুলো ৩ সেকেন্ড উন্মুক্ত রাখা ও কাউন্টডাউন)
            AnimatedVisibility(
              visible = isPreviewMemoryPhase,
              enter = fadeIn() + scaleIn(),
              exit = fadeOut() + scaleOut()
            ) {
              Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color(0xFF4C1D95),
                border = BorderStroke(2.dp, Color(0xFFA78BFA)),
                shadowElevation = 6.dp,
                modifier = Modifier
                  .fillMaxWidth()
                  .testTag("preview_memory_banner")
              ) {
                Row(
                  modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.SpaceBetween
                ) {
                  Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                  ) {
                    Surface(
                      shape = CircleShape,
                      color = Color(0xFF8B5CF6).copy(alpha = 0.4f),
                      modifier = Modifier.size(36.dp)
                    ) {
                      Box(contentAlignment = Alignment.Center) {
                        Text(text = "🧠", fontSize = 18.sp)
                      }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                      Text(
                        text = when (selectedLang) {
                          "BN" -> "মেমোরি প্রিভিউ ফেজ"
                          "AR" -> "مرحلة المعاينة والحفظ"
                          else -> "Memory Preview Phase"
                        },
                        style = TextStyle(
                          fontSize = 13.sp,
                          fontWeight = FontWeight.Bold,
                          color = Color(0xFFE9D5FF)
                        )
                      )
                      Text(
                        text = when (selectedLang) {
                          "BN" -> "অক্ষরগুলো মনে রাখুন... ${toBnDigits(previewCountdownSeconds)}"
                          "AR" -> "احفظ مواضع الحروف... ${toArDigits(previewCountdownSeconds)}"
                          else -> "Memorize card positions... $previewCountdownSeconds"
                        },
                        style = TextStyle(
                          fontSize = 11.5.sp,
                          fontWeight = FontWeight.Medium,
                          color = Color.White.copy(alpha = 0.9f)
                        )
                      )
                    }
                  }

                  // Glowing Countdown Badge
                  Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFF59E0B),
                    border = BorderStroke(1.5.dp, Color(0xFFFDE68A)),
                    shadowElevation = 4.dp
                  ) {
                    Row(
                      modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                      verticalAlignment = Alignment.CenterVertically
                    ) {
                      Icon(
                        imageVector = Icons.Default.Timer,
                        contentDescription = null,
                        tint = Color(0xFF78350F),
                        modifier = Modifier.size(15.dp)
                      )
                      Spacer(modifier = Modifier.width(4.dp))
                      Text(
                        text = when (selectedLang) {
                          "BN" -> "${toBnDigits(previewCountdownSeconds)} সে."
                          "AR" -> "${toArDigits(previewCountdownSeconds)} ث"
                          else -> "${previewCountdownSeconds}s"
                        },
                        style = TextStyle(
                          fontSize = 14.sp,
                          fontWeight = FontWeight.ExtraBold,
                          color = Color(0xFF78350F)
                        )
                      )
                    }
                  }
                }
              }
            }

            // 2. Interactive Phase Status Bar (Pairs Matched & Remaining Attempt Counter)
            AnimatedVisibility(
              visible = !isPreviewMemoryPhase && isAnswerCorrect == null,
              enter = fadeIn(),
              exit = fadeOut()
            ) {
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(horizontal = 2.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                // Matched Pairs Badge
                Surface(
                  shape = RoundedCornerShape(10.dp),
                  color = Color(0xFFECFDF5),
                  border = BorderStroke(1.dp, Color(0xFF10B981).copy(alpha = 0.5f))
                ) {
                  Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    Text(text = "🧩", fontSize = 12.sp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                      text = when (selectedLang) {
                        "BN" -> "মেলানো জোড়া: ${toBnDigits(matchedPairIds.size)} / ${toBnDigits(currentQuestion.requiredPairsCount)}"
                        "AR" -> "الأزواج: ${toArDigits(matchedPairIds.size)} / ${toArDigits(currentQuestion.requiredPairsCount)}"
                        else -> "Pairs: ${matchedPairIds.size} / ${currentQuestion.requiredPairsCount}"
                      },
                      style = TextStyle(
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF047857)
                      )
                    )
                  }
                }

                // Attempt Limit Counter Badge
                val isLowAttempts = remainingAttempts <= 2
                Surface(
                  shape = RoundedCornerShape(10.dp),
                  color = if (isLowAttempts) Color(0xFFFEF2F2) else Color(0xFFF3E8FF),
                  border = BorderStroke(1.dp, if (isLowAttempts) Color(0xFFEF4444) else Color(0xFF8B5CF6).copy(alpha = 0.5f))
                ) {
                  Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    Text(text = if (isLowAttempts) "⚠️" else "🎯", fontSize = 12.sp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                      text = when (selectedLang) {
                        "BN" -> "অবশিষ্ট চেষ্টা: ${toBnDigits(remainingAttempts)} / ${toBnDigits(maxAttemptsForRound)}"
                        "AR" -> "المحاولات: ${toArDigits(remainingAttempts)} / ${toArDigits(maxAttemptsForRound)}"
                        else -> "Attempts: $remainingAttempts / $maxAttemptsForRound"
                      },
                      style = TextStyle(
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isLowAttempts) Color(0xFFDC2626) else Color(0xFF6B21A8)
                      )
                    )
                  }
                }
              }
            }

            // 3. Out of Attempts Failure Banner
            AnimatedVisibility(
              visible = isAnswerCorrect == false,
              enter = fadeIn() + scaleIn(),
              exit = fadeOut() + scaleOut()
            ) {
              Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color(0xFFFEF2F2),
                border = BorderStroke(2.dp, Color(0xFFEF4444)),
                modifier = Modifier
                  .fillMaxWidth()
                  .testTag("mix_match_failure_banner")
              ) {
                Column(
                  modifier = Modifier.padding(12.dp),
                  horizontalAlignment = Alignment.CenterHorizontally
                ) {
                  Text(
                    text = "⚠️ حَاوِلْ مَرَّةً أُخْرَى",
                    style = TextStyle(
                      fontSize = 18.sp,
                      fontWeight = FontWeight.Bold,
                      color = Color(0xFFDC2626),
                      textAlign = TextAlign.Center
                    )
                  )
                  Spacer(modifier = Modifier.height(3.dp))
                  Text(
                    text = when (selectedLang) {
                      "BN" -> "চেষ্টা শেষ হয়েছে! পরবর্তী রাউন্ডে কার্ডগুলোর অবস্থান আরও মনোযোগ দিয়ে মনে রাখুন।"
                      "AR" -> "نفدت المحاولات! تذكر مواضع الحروف جيداً في الجولة التالية."
                      else -> "Attempts exhausted! Memorize card positions carefully next round."
                    },
                    style = TextStyle(
                      fontSize = 12.sp,
                      fontWeight = FontWeight.SemiBold,
                      color = Color(0xFFB91C1C),
                      textAlign = TextAlign.Center
                    )
                  )
                }
              }
            }

            // Round Completion Celebratory Banner
            AnimatedVisibility(
              visible = isAnswerCorrect == true,
              enter = fadeIn() + scaleIn(),
              exit = fadeOut() + scaleOut()
            ) {
              Surface(
                shape = RoundedCornerShape(18.dp),
                color = Color(0xFFECFDF5),
                border = BorderStroke(2.dp, Color(0xFF10B981)),
                modifier = Modifier
                  .fillMaxWidth()
                  .testTag("mix_match_success_banner")
              ) {
                Column(
                  modifier = Modifier.padding(14.dp),
                  horizontalAlignment = Alignment.CenterHorizontally
                ) {
                  Text(
                    text = "✨ مَرْحَبًا! جميع الأزواج متطابقة ✨",
                    style = TextStyle(
                      fontSize = 20.sp,
                      fontWeight = FontWeight.Bold,
                      color = Color(0xFF047857),
                      textAlign = TextAlign.Center
                    )
                  )
                  Spacer(modifier = Modifier.height(3.dp))
                  Text(
                    text = "চমৎকার মেমোরি রিকল! সফলভাবে সকল জোড়া মিলিয়েছেন! (+২০ পয়েন্ট)",
                    style = TextStyle(
                      fontSize = 13.sp,
                      fontWeight = FontWeight.SemiBold,
                      color = Color(0xFF065F46),
                      textAlign = TextAlign.Center
                    )
                  )
                }
              }
            }

            // Grid of Memory Cards with Responsive Dimensions
            val cardRows = memoryCards.chunked(columnsCount)
            val spacing = if (totalCards >= 10) 6.dp else 8.dp

            // Card Height responsive to grid density & rows
            val cardHeight = when {
              totalCards <= 4 -> 112.dp
              totalCards == 6 -> 100.dp
              totalCards == 8 -> 88.dp
              totalCards == 10 -> 82.dp
              else -> 74.dp
            }

            Column(
              modifier = Modifier.fillMaxWidth(),
              horizontalAlignment = Alignment.CenterHorizontally,
              verticalArrangement = Arrangement.spacedBy(spacing)
            ) {
              cardRows.forEachIndexed { rowIndex, rowCards ->
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.spacedBy(spacing)
                ) {
                  // For rows with fewer cards than columnsCount (e.g., 2 cards in last row of 10-card grid)
                  // Place spacer on left and right so cards maintain the exact uniform width
                  val missingSlots = columnsCount - rowCards.size
                  val leftPaddingSlots = missingSlots / 2
                  val rightPaddingSlots = missingSlots - leftPaddingSlots

                  repeat(leftPaddingSlots) {
                    Spacer(modifier = Modifier.weight(1f))
                  }

                  rowCards.forEach { card ->
                    val isMatched = card.pairId in matchedPairIds
                    val isFlipped = isPreviewMemoryPhase || card.id in flippedCardIds || isMatched
                    val isMismatched = card.id in mismatchedCardIds

                    // 3D Flip Animation Specs
                    val rotationY by animateFloatAsState(
                      targetValue = if (isFlipped) 180f else 0f,
                      animationSpec = tween(durationMillis = 350, easing = FastOutSlowInEasing),
                      label = "card_flip_${card.id}"
                    )

                    Box(
                      modifier = Modifier
                        .weight(1f)
                        .height(cardHeight)
                        .graphicsLayer {
                          this.rotationY = rotationY
                          cameraDistance = 12f * density
                        }
                          .clickable(
                            enabled = !isPreviewMemoryPhase && !isCardInteractionLocked && !isFlipped && !isMatched && isAnswerCorrect == null
                          ) {
                            flippedCardIds.add(card.id)
                            soundManager.playOptionSelect()
                            if (card.audioLetter.isNotEmpty()) {
                              soundManager.speakArabicLetter(card.audioLetter)
                            }

                            if (flippedCardIds.size == 2) {
                              isCardInteractionLocked = true
                              attemptsInRound += 1
                              val card1 = memoryCards.find { it.id == flippedCardIds[0] }
                              val card2 = memoryCards.find { it.id == flippedCardIds[1] }

                              if (card1 != null && card2 != null && card1.pairId == card2.pairId) {
                                // MATCH!
                                matchedPairIds.add(card1.pairId)
                                hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                soundManager.playSuccessChime()
                                coroutineScope.launch {
                                  delay(250)
                                  flippedCardIds.clear()
                                  isCardInteractionLocked = false

                                  // Check if all required pairs in this round are matched
                                  if (matchedPairIds.size >= currentQuestion.requiredPairsCount) {
                                    isAnswerCorrect = true
                                    isAnsweringLocked = true
                                    correctAnswersCount += 1
                                    currentStreak += 1
                                    val attemptBonus = (maxAttemptsForRound - attemptsInRound).coerceAtLeast(0) * 5
                                    liveScore += 20 + (currentStreak * 5) + attemptBonus
                                    confettiTriggerKey = currentQuestionIndex

                                    val praise = ArabicPraiseEngine.getPraiseForQuestion(currentQuestionIndex + 1)
                                    activePraise = praise
                                    showPraiseBanner = true
                                    soundManager.playArabicPraiseForQuestion(currentQuestionIndex + 1)

                                    delay(1600L)
                                    showPraiseBanner = false

                                    if (currentQuestionIndex < 19) {
                                      currentQuestionIndex += 1
                                      isAnswerCorrect = null
                                      isAnsweringLocked = false
                                    } else {
                                      val durationSec = (System.currentTimeMillis() - levelStartTimeMillis) / 1000L
                                      val stars = ArabicCulturalTitle.calculateStars(correctAnswersCount, 20)
                                      progressRepo.completeGameLevel(gameMode.type, currentLevelNumber, stars, liveScore)
                                      levelCompletionSummary = GameLevelSummary(
                                        gameType = gameMode.type,
                                        levelNumber = currentLevelNumber,
                                        correctAnswersCount = correctAnswersCount,
                                        totalQuestions = 20,
                                        pointsEarned = liveScore,
                                        durationSeconds = durationSec,
                                        encounteredLetters = encounteredLetters.toList()
                                      )
                                    }
                                  }
                                }
                              } else {
                                // MISMATCH!
                                coroutineScope.launch {
                                  mismatchedCardIds.addAll(flippedCardIds)
                                  hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                                  soundManager.playErrorBuzzer()
                                  delay(900L)
                                  mismatchedCardIds.clear()
                                  flippedCardIds.clear()

                                  // Check if Attempt Limit has been reached
                                  if (attemptsInRound >= maxAttemptsForRound) {
                                    isAnswerCorrect = false
                                    isAnsweringLocked = true
                                    currentStreak = 0

                                    delay(1500L)
                                    if (currentQuestionIndex < 19) {
                                      currentQuestionIndex += 1
                                      isAnswerCorrect = null
                                      isAnsweringLocked = false
                                    } else {
                                      val durationSec = (System.currentTimeMillis() - levelStartTimeMillis) / 1000L
                                      val stars = ArabicCulturalTitle.calculateStars(correctAnswersCount, 20)
                                      progressRepo.completeGameLevel(gameMode.type, currentLevelNumber, stars, liveScore)
                                      levelCompletionSummary = GameLevelSummary(
                                        gameType = gameMode.type,
                                        levelNumber = currentLevelNumber,
                                        correctAnswersCount = correctAnswersCount,
                                        totalQuestions = 20,
                                        pointsEarned = liveScore,
                                        durationSeconds = durationSec,
                                        encounteredLetters = encounteredLetters.toList()
                                      )
                                    }
                                  } else {
                                    isCardInteractionLocked = false
                                  }
                                }
                              }
                            }
                          }
                          .testTag("memory_card_${card.id}")
                      ) {
                        if (rotationY <= 90f) {
                          // CARD BACK (Face Down)
                          Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFF6B46C1),
                            border = BorderStroke(1.5.dp, Color(0xFFD6BCFA).copy(alpha = 0.6f)),
                            shadowElevation = 3.dp,
                            modifier = Modifier.fillMaxSize()
                          ) {
                            Box(
                              contentAlignment = Alignment.Center,
                              modifier = Modifier.fillMaxSize()
                            ) {
                              Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                              ) {
                                Text(
                                  text = "؟",
                                  style = TextStyle(
                                    fontSize = if (totalCards >= 10) 20.sp else if (totalCards >= 8) 22.sp else 28.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color(0xFFF3E8FF).copy(alpha = 0.9f)
                                  )
                                )
                                if (totalCards <= 6) {
                                  Spacer(modifier = Modifier.height(2.dp))
                                  Text(
                                    text = "ফ্লিপ করুন",
                                    style = TextStyle(
                                      fontSize = 9.sp,
                                      fontWeight = FontWeight.Medium,
                                      color = Color(0xFFE9D5FF)
                                    )
                                  )
                                }
                              }
                            }
                          }
                        } else {
                          // CARD FRONT (Face Up)
                          val cardBorderColor = when {
                            isPreviewMemoryPhase -> Color(0xFFA78BFA)
                            isMatched -> Color(0xFF10B981)
                            isMismatched -> Color(0xFFEF4444)
                            else -> Color(0xFF8B5CF6)
                          }
                          val cardBgColor = when {
                            isMatched -> Color(0xFFECFDF5)
                            isMismatched -> Color(0xFFFEF2F2)
                            isPreviewMemoryPhase -> Color(0xFFFAF5FF)
                            else -> Color(0xFFFAF5FF)
                          }

                          Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = cardBgColor,
                            border = BorderStroke(if (isMatched || isMismatched) 2.dp else 1.5.dp, cardBorderColor),
                            shadowElevation = if (isMatched) 4.dp else 2.dp,
                            modifier = Modifier
                              .fillMaxSize()
                              .graphicsLayer { this.rotationY = 180f }
                          ) {
                            Box(
                              modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 3.dp, vertical = 4.dp),
                              contentAlignment = Alignment.Center
                            ) {
                              if (isMatched) {
                                Icon(
                                  imageVector = Icons.Default.CheckCircle,
                                  contentDescription = "Matched",
                                  tint = Color(0xFF10B981),
                                  modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(1.dp)
                                    .size(if (totalCards >= 10) 11.dp else 13.dp)
                                )
                              } else if (isMismatched) {
                                Icon(
                                  imageVector = Icons.Default.Close,
                                  contentDescription = "Mismatch",
                                  tint = Color(0xFFEF4444),
                                  modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(1.dp)
                                    .size(if (totalCards >= 10) 11.dp else 13.dp)
                                )
                              }

                              Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                              ) {
                                if (card.cardTypeLabel.isNotEmpty()) {
                                  Surface(
                                    shape = RoundedCornerShape(3.dp),
                                    color = cardBorderColor.copy(alpha = 0.12f)
                                  ) {
                                    Text(
                                      text = card.cardTypeLabel,
                                      style = TextStyle(
                                        fontSize = if (totalCards >= 10) 7.sp else if (totalCards >= 8) 7.5.sp else 8.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = cardBorderColor
                                      ),
                                      modifier = Modifier.padding(horizontal = 3.dp, vertical = 0.5.dp)
                                    )
                                  }
                                  Spacer(modifier = Modifier.height(1.5.dp))
                                }

                                val textSize = when {
                                  card.displayArabic.length > 3 -> if (totalCards >= 10) 12.sp else if (totalCards >= 8) 14.sp else 18.sp
                                  card.displayArabic.length > 1 -> if (totalCards >= 10) 15.sp else if (totalCards >= 8) 18.sp else 23.sp
                                  else -> if (totalCards >= 10) 20.sp else if (totalCards >= 8) 25.sp else 32.sp
                                }

                                Text(
                                  text = card.displayArabic,
                                  style = TextStyle(
                                    fontSize = textSize,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isMatched) Color(0xFF047857) else if (isMismatched) Color(0xFFDC2626) else Color(0xFF1E1B4B),
                                    textAlign = TextAlign.Center
                                  ),
                                  maxLines = 1
                                )

                                if (card.subtitle.isNotEmpty()) {
                                  Spacer(modifier = Modifier.height(1.5.dp))
                                  Text(
                                    text = card.subtitle,
                                    style = TextStyle(
                                      fontSize = if (totalCards >= 10) 7.5.sp else if (totalCards >= 8) 8.5.sp else 10.sp,
                                      fontWeight = FontWeight.Medium,
                                      color = if (isMatched) Color(0xFF065F46) else Color(0xFF6B7280),
                                      textAlign = TextAlign.Center
                                    ),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                  )
                                }
                              }
                            }
                          }
                        }
                      }
                    }

                  repeat(rightPaddingSlots) {
                    Spacer(modifier = Modifier.weight(1f))
                  }
                }
              }
            }
          }
        } else {
        // Standard Multiple Choice List for Other Game Modes
        Column(
          modifier = Modifier.fillMaxWidth(),
          verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          currentQuestion.options.forEachIndexed { index, option ->
            val isSelected = selectedOptionId == option.id
            val isCorrectOption = option.id == currentQuestion.correctOptionId

            val cardBorderColor by animateColorAsState(
              targetValue = when {
                isSelected && isAnswerCorrect == true -> Color(0xFF10B981)
                isSelected && isAnswerCorrect == false -> Color(0xFFEF4444)
                isAnswerCorrect != null && isCorrectOption -> Color(0xFF10B981) // reveal correct
                isSelected -> gameMode.primaryColor
                else -> Color(0xFFE2E8F0)
              },
              label = "card_border"
            )

            val cardContainerColor by animateColorAsState(
              targetValue = when {
                isSelected && isAnswerCorrect == true -> Color(0xFFECFDF5)
                isSelected && isAnswerCorrect == false -> Color(0xFFFEF2F2)
                isAnswerCorrect != null && isCorrectOption -> Color(0xFFF0FDF4)
                isSelected -> Color(0xFFF8FAFC)
                else -> Color.White
              },
              label = "card_bg"
            )

            Card(
              shape = RoundedCornerShape(16.dp),
              colors = CardDefaults.cardColors(containerColor = cardContainerColor),
              elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 4.dp else 1.dp),
              border = BorderStroke(if (isSelected || (isAnswerCorrect != null && isCorrectOption)) 2.dp else 1.dp, cardBorderColor),
              modifier = Modifier
                .fillMaxWidth()
                .clickable(enabled = !isAnsweringLocked) {
                  if (isAnsweringLocked) return@clickable
                  selectedOptionId = option.id
                  isAnsweringLocked = true

                  val correct = (option.id == currentQuestion.correctOptionId)
                  isAnswerCorrect = correct

                  coroutineScope.launch {
                    if (correct) {
                      // Correct Answer Trigger:
                      correctAnswersCount += 1
                      currentStreak += 1
                      liveScore += 15 + (currentStreak * 5)
                      confettiTriggerKey = currentQuestionIndex

                      val praise = ArabicPraiseEngine.getPraiseForQuestion(currentQuestionIndex + 1)
                      activePraise = praise
                      showPraiseBanner = true

                      // Precise Letter Makhraj audio or Word recitation + Assigned Progressive Arabic Praise audio
                      soundManager.playSuccessChime()
                      delay(300L)
                      if (currentQuestion.displayArabic.length > 2) {
                        soundManager.playArabicText(currentQuestion.displayArabic)
                      } else {
                        soundManager.speakArabicLetter(currentQuestion.targetLetter.letter)
                      }
                      delay(600L)
                      soundManager.playArabicPraiseForQuestion(currentQuestionIndex + 1)
                    } else {
                      // Incorrect Answer Trigger:
                      currentStreak = 0
                      hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                      soundManager.playErrorBuzzer()
                    }

                    delay(1600L)
                    showPraiseBanner = false

                    if (currentQuestionIndex < 19) {
                      currentQuestionIndex += 1
                      selectedOptionId = null
                      isAnswerCorrect = null
                      isAnsweringLocked = false
                    } else {
                      // Level Completed! Show Level Completion Summary
                      val durationSec = (System.currentTimeMillis() - levelStartTimeMillis) / 1000L
                      val stars = ArabicCulturalTitle.calculateStars(correctAnswersCount, 20)
                      progressRepo.completeGameLevel(gameMode.type, currentLevelNumber, stars, liveScore)
                      levelCompletionSummary = GameLevelSummary(
                        gameType = gameMode.type,
                        levelNumber = currentLevelNumber,
                        correctAnswersCount = correctAnswersCount,
                        totalQuestions = 20,
                        pointsEarned = liveScore,
                        durationSeconds = durationSec,
                        encounteredLetters = encounteredLetters.toList()
                      )
                    }
                  }
                }
                .testTag("game_option_$index")
            ) {
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(horizontal = 16.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  modifier = Modifier.weight(1f)
                ) {
                  // Option Arabic glyph badge
                  if (option.arabicDisplay.isNotBlank()) {
                    Surface(
                      shape = RoundedCornerShape(10.dp),
                      color = Color(0xFFF1F5F9),
                      border = BorderStroke(0.8.dp, Color(0xFFCBD5E1)),
                      modifier = Modifier.size(42.dp)
                    ) {
                      Box(contentAlignment = Alignment.Center) {
                        Text(
                          text = option.arabicDisplay,
                          style = TextStyle(
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A)
                          )
                        )
                      }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                  } else if (option.iconEmoji.isNotBlank()) {
                    Text(text = option.iconEmoji, fontSize = 24.sp)
                    Spacer(modifier = Modifier.width(12.dp))
                  }

                  Column {
                    Text(
                      text = option.primaryText,
                      style = TextStyle(
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                      )
                    )
                    if (option.secondaryText.isNotBlank()) {
                      Text(
                        text = option.secondaryText,
                        style = TextStyle(
                          fontSize = 11.5.sp,
                          color = Color(0xFF64748B)
                        )
                      )
                    }
                  }
                }

                // Status Icon indicator
                when {
                  isSelected && isAnswerCorrect == true -> {
                    Icon(
                      imageVector = Icons.Default.CheckCircle,
                      contentDescription = "Correct",
                      tint = Color(0xFF10B981),
                      modifier = Modifier.size(24.dp)
                    )
                  }
                  isSelected && isAnswerCorrect == false -> {
                    Icon(
                      imageVector = Icons.Default.Close,
                      contentDescription = "Incorrect",
                      tint = Color(0xFFEF4444),
                      modifier = Modifier.size(24.dp)
                    )
                  }
                  isAnswerCorrect != null && isCorrectOption -> {
                    Icon(
                      imageVector = Icons.Default.CheckCircle,
                      contentDescription = "Correct Answer",
                      tint = Color(0xFF10B981),
                      modifier = Modifier.size(24.dp)
                    )
                  }
                }
              }
            }
          }
        }
        }

        Spacer(modifier = Modifier.height(20.dp))
      }

      // 4. Confetti overlay
      ConfettiCanvas(triggerKey = confettiTriggerKey)
    }
  }

  // 5. Level Completion Summary Modal with Arabic Cultural Titles
  levelCompletionSummary?.let { summary ->
    LevelCompletionDialog(
      summary = summary,
      selectedLang = selectedLang,
      soundManager = soundManager,
      onNextLevel = {
        levelCompletionSummary = null
        if (currentLevelNumber < 20) {
          currentLevelNumber += 1
          currentQuestionIndex = 0
          correctAnswersCount = 0
          currentStreak = 0
          liveScore = 0
          selectedOptionId = null
          isAnswerCorrect = null
          isAnsweringLocked = false
          encounteredLetters.clear()
          levelStartTimeMillis = System.currentTimeMillis()
          questions = GameQuestionGenerator.generateQuestionsForLevel(gameMode.type, currentLevelNumber)
        } else {
          onExit()
        }
      },
      onReplay = {
        levelCompletionSummary = null
        currentQuestionIndex = 0
        correctAnswersCount = 0
        currentStreak = 0
        liveScore = 0
        selectedOptionId = null
        isAnswerCorrect = null
        isAnsweringLocked = false
        encounteredLetters.clear()
        levelStartTimeMillis = System.currentTimeMillis()
        questions = GameQuestionGenerator.generateQuestionsForLevel(gameMode.type, currentLevelNumber)
      },
      onBackToRoadmap = onExit
    )
  }
}
