package com.example

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.RoyalEmerald

@Composable
fun ShapeMasterGameScreen(
  level: RoadmapLevel,
  selectedLang: String,
  soundManager: SoundManager,
  onExit: () -> Unit,
  onLevelCompleted: (stars: Int, xpEarned: Int, coinsEarned: Int) -> Unit
) {
  val layoutDirection = if (selectedLang == "AR") LayoutDirection.Rtl else LayoutDirection.Ltr

  var currentQuestionIndex by remember { mutableIntStateOf(0) }
  var lives by remember { mutableIntStateOf(3) }
  var scorePoints by remember { mutableIntStateOf(0) }
  var isLevelFinished by remember { mutableStateOf(false) }
  var showExitConfirmDialog by remember { mutableStateOf(false) }
  var showGameOverDialog by remember { mutableStateOf(false) }

  // State for Type A
  var selectedOptionIndex by remember { mutableIntStateOf(-1) }

  // State for Type B
  var selectedLeftPairId by remember { mutableStateOf<String?>(null) }
  val matchedPairIds = remember { mutableStateListOf<String>() }

  // Global Beginner Hints setting observed directly from SoundManager / App Settings
  val showHints = soundManager.beginnerHintsEnabledState

  // Feedback Bottom Sheet State
  var feedbackState by remember { mutableStateOf<FeedbackType?>(null) }
  var isCheckingAnswer by remember { mutableStateOf(false) }

  val questions = level.questions
  val currentQuestion = questions.getOrNull(currentQuestionIndex)

  // Progress ratio
  val animatedProgress by animateFloatAsState(
    targetValue = if (questions.isEmpty()) 0f else (currentQuestionIndex.toFloat() / questions.size.toFloat()),
    label = "progress_anim"
  )

  // Auto-play audio when question changes
  LaunchedEffect(currentQuestionIndex) {
    currentQuestion?.let { q ->
      soundManager.speakArabicOrBeep(q.targetLetter.letter)
    }
    selectedOptionIndex = -1
    selectedLeftPairId = null
    matchedPairIds.clear()
    feedbackState = null
    isCheckingAnswer = false
  }

  CompositionLocalProvider(LocalLayoutDirection provides layoutDirection) {
    Scaffold(
      containerColor = Color(0xFFF8FAFC),
      topBar = {
        // Game Header: Quit (X), Progress Bar, Lives (❤️), Score
        GameTopHeader(
          progress = animatedProgress,
          currentQuestionNumber = currentQuestionIndex + 1,
          totalQuestions = questions.size,
          lives = lives,
          score = scorePoints,
          selectedLang = selectedLang,
          onQuitClick = { showExitConfirmDialog = true }
        )
      },
      bottomBar = {
        // Bottom Action Area / Feedback Bottom Banner
        GameBottomActionBar(
          currentQuestion = currentQuestion,
          selectedOptionIndex = selectedOptionIndex,
          matchedPairsCount = matchedPairIds.size,
          feedbackState = feedbackState,
          selectedLang = selectedLang,
          onCheckAnswer = {
            if (currentQuestion == null) return@GameBottomActionBar
            isCheckingAnswer = true

            if (currentQuestion.type == QuizQuestionType.SHAPE_IDENTIFICATION) {
              val selectedOption = currentQuestion.options.getOrNull(selectedOptionIndex)
              if (selectedOption != null && selectedOption.isCorrect) {
                soundManager.playSuccessChime()
                scorePoints += 30
                feedbackState = FeedbackType.CORRECT
              } else {
                soundManager.playErrorBuzzer()
                lives = (lives - 1).coerceAtLeast(0)
                feedbackState = FeedbackType.INCORRECT
              }
            } else {
              // Type B (Matching Pairs)
              if (matchedPairIds.size == currentQuestion.pairs.size) {
                soundManager.playSuccessChime()
                scorePoints += 40
                feedbackState = FeedbackType.CORRECT
              }
            }
          },
          onNextQuestion = {
            if (lives <= 0) {
              showGameOverDialog = true
            } else if (currentQuestionIndex + 1 < questions.size) {
              currentQuestionIndex += 1
            } else {
              // Level Completed!
              isLevelFinished = true
            }
          }
        )
      }
    ) { innerPadding ->
      Box(
        modifier = Modifier
          .fillMaxSize()
          .padding(innerPadding)
          .statusBarsPadding()
          .navigationBarsPadding()
          .imePadding()
      ) {
        if (currentQuestion != null && !isLevelFinished) {
          Column(
            modifier = Modifier
              .fillMaxSize()
              .verticalScroll(rememberScrollState())
              .padding(horizontal = 20.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Column(
              modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 600.dp),
              horizontalAlignment = Alignment.CenterHorizontally
            ) {
              // Question Type A (Shape Identification)
              if (currentQuestion.type == QuizQuestionType.SHAPE_IDENTIFICATION) {
                ShapeIdentificationView(
                  question = currentQuestion,
                  selectedOptionIndex = selectedOptionIndex,
                  selectedLang = selectedLang,
                  showHints = showHints,
                  feedbackState = feedbackState,
                  soundManager = soundManager,
                  onSelectOption = { index ->
                    if (feedbackState == null) {
                      selectedOptionIndex = index
                      soundManager.playTone()
                    }
                  }
                )
              } else {
                // Question Type B (Matching Pairs)
                MatchingPairsView(
                  question = currentQuestion,
                  selectedLeftPairId = selectedLeftPairId,
                  matchedPairIds = matchedPairIds,
                  selectedLang = selectedLang,
                  showHints = showHints,
                  soundManager = soundManager,
                  onSelectLeft = { pairId ->
                    if (feedbackState == null && !matchedPairIds.contains(pairId)) {
                      selectedLeftPairId = pairId
                      soundManager.playTone()
                    }
                  },
                  onSelectRight = { rightPairId ->
                    if (feedbackState == null && selectedLeftPairId != null) {
                      if (selectedLeftPairId == rightPairId) {
                        // Match found!
                        matchedPairIds.add(rightPairId)
                        selectedLeftPairId = null
                        soundManager.playSuccessChime()
                        // Auto complete if all pairs matched
                        if (matchedPairIds.size == currentQuestion.pairs.size) {
                          scorePoints += 40
                          feedbackState = FeedbackType.CORRECT
                        }
                      } else {
                        // Mismatch
                        soundManager.playErrorBuzzer()
                        lives = (lives - 1).coerceAtLeast(0)
                        selectedLeftPairId = null
                        if (lives <= 0) {
                          showGameOverDialog = true
                        }
                      }
                    }
                  }
                )
              }

              Spacer(modifier = Modifier.height(100.dp))
            }
          }
        }
      }
    }
  }

  // End of Level Summary Celebration Modal
  if (isLevelFinished) {
    val starsEarned = when (lives) {
      3 -> 3
      2 -> 2
      else -> 1
    }
    LevelSummaryModal(
      level = level,
      starsEarned = starsEarned,
      xpEarned = level.rewardXp,
      coinsEarned = level.rewardStars,
      selectedLang = selectedLang,
      onContinue = {
        onLevelCompleted(starsEarned, level.rewardXp, level.rewardStars)
      }
    )
  }

  // Confirm Exit Dialog
  if (showExitConfirmDialog) {
    Dialog(onDismissRequest = { showExitConfirmDialog = false }) {
      Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier.padding(16.dp)
      ) {
        Column(
          modifier = Modifier.padding(20.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Text(text = "⚠️", fontSize = 36.sp)
          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = when (selectedLang) {
              "EN" -> "Leave Session?"
              "AR" -> "هل ترغب في مغادرة الجلسة؟"
              else -> "সেশন ছেড়ে যেতে চান?"
            },
            style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
          )
          Spacer(modifier = Modifier.height(6.dp))
          Text(
            text = when (selectedLang) {
              "EN" -> "Your progress for this level will not be saved."
              "AR" -> "لن يتم حفظ تقدمك في هذا المستوى."
              else -> "এই লেভেলের অগ্রগতি সংরক্ষিত হবে না।"
            },
            style = TextStyle(fontSize = 12.sp, color = Color(0xFF64748B), textAlign = TextAlign.Center)
          )
          Spacer(modifier = Modifier.height(16.dp))
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            OutlinedButton(
              onClick = { showExitConfirmDialog = false },
              modifier = Modifier.weight(1f)
            ) {
              Text(
                text = when (selectedLang) { "EN" -> "Stay" "AR" -> "البقاء" else -> "থাকুন" }
              )
            }
            Button(
              onClick = {
                showExitConfirmDialog = false
                onExit()
              },
              colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444)),
              modifier = Modifier.weight(1f)
            ) {
              Text(
                text = when (selectedLang) { "EN" -> "Leave" "AR" -> "مغادرة" else -> "বের হন" }
              )
            }
          }
        }
      }
    }
  }

  // Game Over (No Lives Left) Dialog
  if (showGameOverDialog) {
    Dialog(onDismissRequest = {}) {
      Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier.padding(16.dp)
      ) {
        Column(
          modifier = Modifier.padding(20.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Text(text = "💔", fontSize = 42.sp)
          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = when (selectedLang) {
              "EN" -> "Out of Lives!"
              "AR" -> "نفدت القلوب!"
              else -> "সবগুলো জীবন শেষ!"
            },
            style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFFDC2626))
          )
          Spacer(modifier = Modifier.height(6.dp))
          Text(
            text = when (selectedLang) {
              "EN" -> "Don't give up! Learning Arabic letter forms takes practice."
              "AR" -> "لا تستسلم! تعلم أشكال الحروف يحتاج إلى تدريب."
              else -> "হাল ছাড়বেন না! অনুশীলনের মাধ্যমে আপনি অবশ্যই সফল হবেন।"
            },
            style = TextStyle(fontSize = 12.sp, color = Color(0xFF64748B), textAlign = TextAlign.Center)
          )
          Spacer(modifier = Modifier.height(18.dp))
          Button(
            onClick = {
              lives = 3
              currentQuestionIndex = 0
              showGameOverDialog = false
            },
            colors = ButtonDefaults.buttonColors(containerColor = RoyalEmerald),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            Text(
              text = when (selectedLang) {
                "EN" -> "Try Again with 3 Lives ❤️"
                "AR" -> "حاول مجدداً مع ٣ قلوب ❤️"
                else -> "পুনরায় চেষ্টা করুন (৩টি জীবন) ❤️"
              },
              fontWeight = FontWeight.Bold
            )
          }
          Spacer(modifier = Modifier.height(8.dp))
          OutlinedButton(
            onClick = {
              showGameOverDialog = false
              onExit()
            },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            Text(
              text = when (selectedLang) {
                "EN" -> "Back to Roadmap"
                "AR" -> "العودة إلى المسار"
                else -> "রোডম্যাপে ফিরে যান"
              }
            )
          }
        }
      }
    }
  }
}

enum class FeedbackType {
  CORRECT,
  INCORRECT
}

/**
 * Top Header: Exit, Progress Bar, Lives (❤️), Score
 */
@Composable
fun GameTopHeader(
  progress: Float,
  currentQuestionNumber: Int,
  totalQuestions: Int,
  lives: Int,
  score: Int,
  selectedLang: String = "BN",
  onQuitClick: () -> Unit
) {
  Surface(
    color = Color.White,
    shadowElevation = 2.dp,
    modifier = Modifier.fillMaxWidth()
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        // Quit Button (X)
        IconButton(
          onClick = onQuitClick,
          modifier = Modifier
            .size(36.dp)
            .testTag("game_quit_btn")
        ) {
          Icon(
            imageVector = Icons.Default.Close,
            contentDescription = "Quit",
            tint = Color(0xFF64748B)
          )
        }

        // Progress Bar
        Box(
          modifier = Modifier
            .weight(1f)
            .padding(horizontal = 10.dp)
        ) {
          LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
              .fillMaxWidth()
              .height(10.dp)
              .clip(RoundedCornerShape(5.dp)),
            color = RoyalEmerald,
            trackColor = Color(0xFFE2E8F0)
          )
        }

        // Lives Indicator (❤️❤️❤️) and Score
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier.testTag("game_lives_row")
        ) {
          repeat(3) { index ->
            val isAlive = index < lives
            Icon(
              imageVector = if (isAlive) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
              contentDescription = "Heart",
              tint = if (isAlive) Color(0xFFEF4444) else Color(0xFFCBD5E1),
              modifier = Modifier
                .size(18.dp)
                .padding(horizontal = 1.dp)
            )
          }

          Spacer(modifier = Modifier.width(6.dp))

          // Score pill
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(10.dp))
              .background(Color(0xFFFEF3C7))
              .padding(horizontal = 8.dp, vertical = 4.dp)
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(text = "⭐", fontSize = 11.sp)
              Spacer(modifier = Modifier.width(2.dp))
              Text(
                text = "$score",
                style = TextStyle(
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color(0xFF92400E)
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
 * Question Type A: Shape Identification
 */
@Composable
fun ShapeIdentificationView(
  question: ShapeQuizQuestion,
  selectedOptionIndex: Int,
  selectedLang: String,
  showHints: Boolean,
  feedbackState: FeedbackType?,
  soundManager: SoundManager,
  onSelectOption: (Int) -> Unit
) {
  Column(
    modifier = Modifier.fillMaxWidth(),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Spacer(modifier = Modifier.height(10.dp))

    // Audio Speaker Hero Button with pulse aesthetic
    Card(
      shape = RoundedCornerShape(20.dp),
      colors = CardDefaults.cardColors(containerColor = Color.White),
      elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
      border = BorderStroke(1.5.dp, RoyalEmerald.copy(alpha = 0.25f)),
      modifier = Modifier
        .clickable {
          soundManager.speakArabicOrBeep(question.targetLetter.letter)
        }
        .testTag("audio_pronunciation_hero")
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(horizontal = 18.dp, vertical = 12.dp)
      ) {
        Box(
          modifier = Modifier
            .size(44.dp)
            .clip(CircleShape)
            .background(RoyalEmerald.copy(alpha = 0.12f)),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.VolumeUp,
            contentDescription = "Play Letter Audio",
            tint = RoyalEmerald,
            modifier = Modifier.size(24.dp)
          )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
          Text(
            text = "${question.targetLetter.letter}  •  ${question.targetLetter.getName(selectedLang)}",
            style = TextStyle(
              fontSize = 18.sp,
              fontWeight = FontWeight.Bold,
              color = RoyalEmerald,
              fontFamily = FontFamily.Serif
            )
          )
          Text(
            text = when (selectedLang) {
              "EN" -> "Tap to hear pronunciation 🔊"
              "AR" -> "اضغط للاستماع إلى النطق 🔊"
              else -> "উচ্চারণ শুনতে এখানে ট্যাপ করুন 🔊"
            },
            style = TextStyle(fontSize = 11.sp, color = Color(0xFF64748B))
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(14.dp))

    // Form Type Badge Indicator: Explicit and clearly visible
    Box(
      modifier = Modifier
        .clip(RoundedCornerShape(12.dp))
        .background(question.targetFormType.getTagColor().copy(alpha = 0.15f))
        .border(1.dp, question.targetFormType.getTagColor().copy(alpha = 0.4f), RoundedCornerShape(12.dp))
        .padding(horizontal = 12.dp, vertical = 6.dp)
        .testTag("target_form_badge")
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
          text = "${question.targetFormType.getTitle(selectedLang)} ${question.targetFormType.getPositionalHint(selectedLang)}",
          style = TextStyle(
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = question.targetFormType.getTagColor()
          )
        )
        Spacer(modifier = Modifier.width(6.dp))
        Surface(
          shape = RoundedCornerShape(6.dp),
          color = question.targetFormType.getTagColor().copy(alpha = 0.2f)
        ) {
          Text(
            text = question.targetFormType.getVisualSchematic(),
            style = TextStyle(
              fontSize = 14.sp,
              fontWeight = FontWeight.Black,
              color = question.targetFormType.getTagColor(),
              letterSpacing = 1.sp
            ),
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(12.dp))

    // Main Question Title (ALWAYS CLEARLY VISIBLE and states explicit target form)
    Text(
      text = question.getPrompt(selectedLang),
      style = TextStyle(
        fontSize = 17.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF0F172A),
        textAlign = TextAlign.Center,
        lineHeight = 23.sp
      ),
      modifier = Modifier
        .padding(horizontal = 8.dp)
        .testTag("question_prompt_text")
    )

    Spacer(modifier = Modifier.height(16.dp))

    // 4 Options (2x2 Grid)
    @OptIn(ExperimentalLayoutApi::class)
    FlowRow(
      maxItemsInEachRow = 2,
      horizontalArrangement = Arrangement.spacedBy(14.dp),
      verticalArrangement = Arrangement.spacedBy(14.dp),
      modifier = Modifier.fillMaxWidth()
    ) {
      question.options.forEachIndexed { index, option ->
        val isSelected = selectedOptionIndex == index

        // Color calculation based on feedback
        val borderColor by animateColorAsState(
          targetValue = when {
            feedbackState == FeedbackType.CORRECT && option.isCorrect -> RoyalEmerald
            feedbackState == FeedbackType.INCORRECT && isSelected -> Color(0xFFEF4444)
            isSelected -> RoyalEmerald
            else -> Color(0xFFCBD5E1)
          },
          label = "border_color"
        )

        val cardBgColor by animateColorAsState(
          targetValue = when {
            feedbackState == FeedbackType.CORRECT && option.isCorrect -> Color(0xFFECFDF5)
            feedbackState == FeedbackType.INCORRECT && isSelected -> Color(0xFFFEF2F2)
            isSelected -> RoyalEmerald.copy(alpha = 0.08f)
            else -> Color.White
          },
          label = "bg_color"
        )

        Card(
          shape = RoundedCornerShape(20.dp),
          colors = CardDefaults.cardColors(containerColor = cardBgColor),
          elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 6.dp else 2.dp,
            pressedElevation = 8.dp
          ),
          border = BorderStroke(if (isSelected) 2.5.dp else 1.5.dp, borderColor),
          modifier = Modifier
            .weight(1f)
            .height(115.dp)
            .clip(RoundedCornerShape(20.dp))
            .clickable(enabled = feedbackState == null) {
              onSelectOption(index)
            }
            .testTag("shape_option_$index")
        ) {
          Column(
            modifier = Modifier
              .fillMaxSize()
              .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
          ) {
            Text(
              text = option.glyph,
              style = TextStyle(
                fontSize = 38.sp,
                fontWeight = FontWeight.Bold,
                color = if (isSelected) RoyalEmerald else Color(0xFF1E293B),
                fontFamily = FontFamily.Serif
              )
            )
            // Positional Hint Label: Displayed when showHints is ON, Completely hidden when OFF
            if (showHints) {
              Spacer(modifier = Modifier.height(3.dp))
              Text(
                text = option.formType.getShortPositionalLabel(selectedLang),
                style = TextStyle(
                  fontSize = 11.sp,
                  fontWeight = FontWeight.SemiBold,
                  color = Color(0xFF64748B)
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.testTag("option_hint_label_$index")
              )
            }
          }
        }
      }
    }
  }
}

/**
 * Question Type B: Matching Pairs
 */
@Composable
fun MatchingPairsView(
  question: ShapeQuizQuestion,
  selectedLeftPairId: String?,
  matchedPairIds: List<String>,
  selectedLang: String,
  showHints: Boolean,
  soundManager: SoundManager,
  onSelectLeft: (String) -> Unit,
  onSelectRight: (String) -> Unit
) {
  Column(
    modifier = Modifier.fillMaxWidth(),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Spacer(modifier = Modifier.height(10.dp))

    // Instruction Banner
    Card(
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = Color(0xFFEFF6FF)),
      border = BorderStroke(1.dp, Color(0xFFBFDBFE)),
      modifier = Modifier.fillMaxWidth()
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(14.dp)
      ) {
        Icon(
          imageVector = Icons.Default.Lightbulb,
          contentDescription = "Hint",
          tint = Color(0xFF2563EB),
          modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
          text = question.getPrompt(selectedLang),
          style = TextStyle(
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1E3A8A)
          )
        )
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Pairs Matching Columns
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      // Left Column: Isolated Root Letters
      Column(
        modifier = Modifier.weight(1f),
        verticalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        Text(
          text = when (selectedLang) { "EN" -> "Isolated Root" "AR" -> "الأصل المنفصل" else -> "মূল বিচ্ছিন্ন হরফ" },
          style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF64748B)),
          textAlign = TextAlign.Center,
          modifier = Modifier.fillMaxWidth()
        )

        question.pairs.forEach { pair ->
          val isMatched = matchedPairIds.contains(pair.id)
          val isSelected = selectedLeftPairId == pair.id

          Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
              containerColor = when {
                isMatched -> Color(0xFFDCFCE7)
                isSelected -> RoyalEmerald.copy(alpha = 0.12f)
                else -> Color.White
              }
            ),
            border = BorderStroke(
              width = if (isSelected || isMatched) 2.dp else 1.dp,
              color = when {
                isMatched -> RoyalEmerald
                isSelected -> RoyalEmerald
                else -> Color(0xFFCBD5E1)
              }
            ),
            modifier = Modifier
              .fillMaxWidth()
              .height(72.dp)
              .clickable(enabled = !isMatched) {
                onSelectLeft(pair.id)
              }
              .testTag("pair_left_${pair.id}")
          ) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                  text = pair.isolatedLetter,
                  fontSize = 28.sp,
                  fontWeight = FontWeight.Bold,
                  color = if (isMatched) RoyalEmerald else Color(0xFF0F172A),
                  fontFamily = FontFamily.Serif
                )
                if (isMatched) {
                  Spacer(modifier = Modifier.width(6.dp))
                  Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Matched",
                    tint = RoyalEmerald,
                    modifier = Modifier.size(18.dp)
                  )
                }
              }
            }
          }
        }
      }

      // Right Column: Connected Shapes (Scrambled for interactivity)
      Column(
        modifier = Modifier.weight(1f),
        verticalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        Text(
          text = when (selectedLang) { "EN" -> "Connecting Shape" "AR" -> "الشكل المتصل" else -> "সংযুক্ত রূপ" },
          style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF64748B)),
          textAlign = TextAlign.Center,
          modifier = Modifier.fillMaxWidth()
        )

        // Reversed list so it's a puzzle matching
        val rightPairs = remember(question) { question.pairs.reversed() }
        rightPairs.forEach { pair ->
          val isMatched = matchedPairIds.contains(pair.id)

          Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
              containerColor = if (isMatched) Color(0xFFDCFCE7) else Color.White
            ),
            border = BorderStroke(
              width = if (isMatched) 2.dp else 1.dp,
              color = if (isMatched) RoyalEmerald else Color(0xFFCBD5E1)
            ),
            modifier = Modifier
              .fillMaxWidth()
              .height(72.dp)
              .clickable(enabled = !isMatched) {
                onSelectRight(pair.id)
              }
              .testTag("pair_right_${pair.id}")
          ) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
              Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                  text = pair.connectedGlyph,
                  fontSize = 28.sp,
                  fontWeight = FontWeight.Bold,
                  color = if (isMatched) RoyalEmerald else Color(0xFF0F172A),
                  fontFamily = FontFamily.Serif
                )
                // When showHints is true, display positional hint label. When false (Challenge Mode), completely hidden!
                if (showHints) {
                  Text(
                    text = "${pair.formType.getTitle(selectedLang)} ${pair.formType.getPositionalHint(selectedLang)}",
                    style = TextStyle(fontSize = 10.sp, color = Color(0xFF64748B)),
                    textAlign = TextAlign.Center
                  )
                }
              }
            }
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    Text(
      text = when (selectedLang) {
        "EN" -> "Matched: ${matchedPairIds.size} of ${question.pairs.size}"
        "AR" -> "تمت المطابقة: ${matchedPairIds.size} من ${question.pairs.size}"
        else -> "মিলিত হয়েছে: ${matchedPairIds.size} / ${question.pairs.size}"
      },
      style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Bold, color = RoyalEmerald)
    )
  }
}

/**
 * Bottom Action Bar with Animated Feedback Sheet
 */
@Composable
fun GameBottomActionBar(
  currentQuestion: ShapeQuizQuestion?,
  selectedOptionIndex: Int,
  matchedPairsCount: Int,
  feedbackState: FeedbackType?,
  selectedLang: String,
  onCheckAnswer: () -> Unit,
  onNextQuestion: () -> Unit
) {
  val isCheckEnabled = when {
    currentQuestion == null -> false
    currentQuestion.type == QuizQuestionType.SHAPE_IDENTIFICATION -> selectedOptionIndex >= 0
    else -> matchedPairsCount == currentQuestion.pairs.size
  }

  Column(
    modifier = Modifier
      .fillMaxWidth()
      .background(Color.White)
      .shadow(8.dp)
  ) {
    // Animated Immediate Feedback Drawer/Sheet
    AnimatedVisibility(
      visible = feedbackState != null,
      enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
      exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
    ) {
      val isCorrect = feedbackState == FeedbackType.CORRECT
      val bannerBg = if (isCorrect) Color(0xFFDCFCE7) else Color(0xFFFEE2E2)
      val textColor = if (isCorrect) Color(0xFF065F46) else Color(0xFF991B1B)
      val icon = if (isCorrect) "🎉" else "❌"

      Column(
        modifier = Modifier
          .fillMaxWidth()
          .background(bannerBg)
          .padding(horizontal = 20.dp, vertical = 14.dp)
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(text = icon, fontSize = 22.sp)
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = if (isCorrect) {
              when (selectedLang) {
                "EN" -> "Excellent! Perfect Answer"
                "AR" -> "أحسنت! إجابة ممتازة"
                else -> "চমৎকার! সঠিক উত্তর"
              }
            } else {
              when (selectedLang) {
                "EN" -> "Not quite right!"
                "AR" -> "للأسف، إجابة غير دقيقة!"
                else -> "উত্তরটি সঠিক হয়নি!"
              }
            },
            style = TextStyle(
              fontSize = 16.sp,
              fontWeight = FontWeight.Bold,
              color = textColor
            )
          )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
          text = currentQuestion?.getExplanation(selectedLang) ?: "",
          style = TextStyle(
            fontSize = 12.sp,
            color = textColor.copy(alpha = 0.9f),
            lineHeight = 17.sp
          )
        )
      }
    }

    // Action Button Area
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp, vertical = 14.dp)
    ) {
      if (feedbackState == null) {
        Button(
          onClick = onCheckAnswer,
          enabled = isCheckEnabled,
          colors = ButtonDefaults.buttonColors(
            containerColor = RoyalEmerald,
            disabledContainerColor = Color(0xFFE2E8F0)
          ),
          shape = RoundedCornerShape(14.dp),
          modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .testTag("check_answer_btn")
        ) {
          Text(
            text = when (selectedLang) {
              "EN" -> "CHECK ANSWER"
              "AR" -> "تحقق من الإجابة"
              else -> "যাচাই করুন"
            },
            style = TextStyle(
              fontSize = 15.sp,
              fontWeight = FontWeight.Bold,
              color = if (isCheckEnabled) Color.White else Color(0xFF94A3B8),
              letterSpacing = 1.sp
            )
          )
        }
      } else {
        Button(
          onClick = onNextQuestion,
          colors = ButtonDefaults.buttonColors(
            containerColor = if (feedbackState == FeedbackType.CORRECT) RoyalEmerald else Color(0xFFEF4444)
          ),
          shape = RoundedCornerShape(14.dp),
          modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .testTag("continue_next_btn")
        ) {
          Text(
            text = when (selectedLang) {
              "EN" -> "CONTINUE ➔"
              "AR" -> "متابعة ➔"
              else -> "চালিয়ে যান ➔"
            },
            style = TextStyle(
              fontSize = 15.sp,
              fontWeight = FontWeight.Bold,
              color = Color.White,
              letterSpacing = 1.sp
            )
          )
        }
      }
    }
  }
}

/**
 * End of Level Summary Celebration Modal
 */
@Composable
fun LevelSummaryModal(
  level: RoadmapLevel,
  starsEarned: Int,
  xpEarned: Int,
  coinsEarned: Int,
  selectedLang: String,
  onContinue: () -> Unit
) {
  Dialog(onDismissRequest = onContinue) {
    Card(
      shape = RoundedCornerShape(26.dp),
      colors = CardDefaults.cardColors(containerColor = Color.White),
      elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
      modifier = Modifier
        .fillMaxWidth()
        .padding(12.dp)
        .testTag("level_summary_modal")
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .background(
            Brush.verticalGradient(
              colors = listOf(
                RoyalEmerald.copy(alpha = 0.08f),
                Color.White
              )
            )
          )
          .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        // Trophy Emoji
        Text(text = "🏆", fontSize = 48.sp)

        Spacer(modifier = Modifier.height(10.dp))

        Text(
          text = when (selectedLang) {
            "EN" -> "Level Complete!"
            "AR" -> "اكتمل المستوى بنجاح!"
            else -> "লেভেল সম্পন্ন হয়েছে!"
          },
          style = TextStyle(
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = RoyalEmerald
          )
        )

        Text(
          text = level.getTitle(selectedLang),
          style = TextStyle(
            fontSize = 14.sp,
            color = Color(0xFF64748B)
          )
        )

        Spacer(modifier = Modifier.height(18.dp))

        // Stars display with glowing effect
        Row(
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          repeat(3) { index ->
            val hasStar = index < starsEarned
            Box(
              modifier = Modifier
                .size(46.dp)
                .clip(CircleShape)
                .background(if (hasStar) Color(0xFFFEF3C7) else Color(0xFFF1F5F9)),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.Star,
                contentDescription = "Star",
                tint = if (hasStar) Color(0xFFF59E0B) else Color(0xFFCBD5E1),
                modifier = Modifier.size(28.dp)
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Rewards Breakdown Card
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
          border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
          ) {
            // XP Reward
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              Text(text = "⚡", fontSize = 20.sp)
              Text(
                text = "+$xpEarned XP",
                style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF4F46E5))
              )
              Text(
                text = when (selectedLang) { "EN" -> "Experience" "AR" -> "خبرة" else -> "অভিজ্ঞতা" },
                style = TextStyle(fontSize = 10.sp, color = Color(0xFF64748B))
              )
            }

            Box(
              modifier = Modifier
                .width(1.dp)
                .height(36.dp)
                .background(Color(0xFFE2E8F0))
            )

            // Coins / Stars Reward
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              Text(text = "⭐", fontSize = 20.sp)
              Text(
                text = "+$coinsEarned",
                style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD97706))
              )
              Text(
                text = when (selectedLang) { "EN" -> "Stars" "AR" -> "نجوم" else -> "তারকা" },
                style = TextStyle(fontSize = 10.sp, color = Color(0xFF64748B))
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Continue to Roadmap Button
        Button(
          onClick = onContinue,
          colors = ButtonDefaults.buttonColors(containerColor = RoyalEmerald),
          shape = RoundedCornerShape(14.dp),
          modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .testTag("summary_continue_btn")
        ) {
          Text(
            text = when (selectedLang) {
              "EN" -> "CONTINUE TO ROADMAP ➔"
              "AR" -> "متابعة إلى المسار ➔"
              else -> "রোডম্যাপে এগিয়ে যান ➔"
            },
            style = TextStyle(
              fontSize = 14.sp,
              fontWeight = FontWeight.Bold,
              color = Color.White
            )
          )
        }
      }
    }
  }
}
