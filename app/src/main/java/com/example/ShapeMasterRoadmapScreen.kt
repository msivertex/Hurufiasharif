package com.example

import androidx.compose.animation.core.FastOutSlowInEasing
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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.RoyalEmerald

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShapeMasterRoadmapScreen(
  selectedLang: String,
  onLanguageChange: (String) -> Unit,
  soundManager: SoundManager,
  onBackToDashboard: () -> Unit,
  onLevelStatsUpdated: (starsAdded: Int, xpAdded: Int) -> Unit
) {
  val layoutDirection = if (selectedLang == "AR") LayoutDirection.Rtl else LayoutDirection.Ltr
  var showLanguageMenu by remember { mutableStateOf(false) }

  // Level progress tracking: Level 1 is unlocked initially
  val unlockedLevels = remember {
    mutableStateMapOf<Int, Boolean>().apply {
      put(1, true)
    }
  }

  val levelStars = remember {
    mutableStateMapOf<Int, Int>()
  }

  var selectedLevelForGame by remember { mutableStateOf<RoadmapLevel?>(null) }
  var previewLevelDialog by remember { mutableStateOf<RoadmapLevel?>(null) }
  var totalStarsEarned by remember { mutableIntStateOf(0) }

  // If a game session is active, render ShapeMasterGameScreen
  if (selectedLevelForGame != null) {
    ShapeMasterGameScreen(
      level = selectedLevelForGame!!,
      selectedLang = selectedLang,
      soundManager = soundManager,
      onExit = { selectedLevelForGame = null },
      onLevelCompleted = { stars, xp, coins ->
        val currentLevelId = selectedLevelForGame!!.id
        levelStars[currentLevelId] = maxOf(levelStars[currentLevelId] ?: 0, stars)
        // Unlock next level!
        unlockedLevels[currentLevelId + 1] = true
        totalStarsEarned += coins
        onLevelStatsUpdated(coins, xp)
        selectedLevelForGame = null
      }
    )
    return
  }

  CompositionLocalProvider(LocalLayoutDirection provides layoutDirection) {
    Scaffold(
      containerColor = Color(0xFFF1F5F9), // Clean soft backdrop
      topBar = {
        TopAppBar(
          title = {
            Column {
              Text(
                text = when (selectedLang) {
                  "EN" -> "Shape Master Path"
                  "AR" -> "مسار إتقان الأشكال"
                  else -> "শেপ মাস্টার রোডম্যাপ"
                },
                style = TextStyle(
                  fontWeight = FontWeight.Bold,
                  color = Color.White,
                  fontSize = 18.sp
                )
              )
              Text(
                text = when (selectedLang) {
                  "EN" -> "Master Arabic letter forms step-by-step"
                  "AR" -> "أتقن أشكال الحروف خطوة بخطوة"
                  else -> "ধাপে ধাপে প্রতিটি রূপ আয়ত্ত করুন"
                },
                style = TextStyle(
                  color = Color.White.copy(alpha = 0.85f),
                  fontSize = 11.sp
                )
              )
            }
          },
          navigationIcon = {
            IconButton(
              onClick = onBackToDashboard,
              modifier = Modifier.testTag("roadmap_back_btn")
            ) {
              Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Color.White
              )
            }
          },
          colors = TopAppBarDefaults.topAppBarColors(
            containerColor = RoyalEmerald
          ),
          actions = {
            // Star Counter Pill
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFF064426))
                .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "⭐", fontSize = 12.sp)
                Spacer(modifier = Modifier.width(3.dp))
                val totalCollected = levelStars.values.sum()
                Text(
                  text = "$totalCollected / 30",
                  style = TextStyle(fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFFF6E05E))
                )
              }
            }

            Spacer(modifier = Modifier.width(6.dp))

            // Language Switcher Dropdown
            Box {
              IconButton(
                onClick = { showLanguageMenu = true },
                modifier = Modifier.testTag("roadmap_lang_btn")
              ) {
                Icon(
                  imageVector = Icons.Default.Language,
                  contentDescription = "Language",
                  tint = Color.White
                )
              }

              DropdownMenu(
                expanded = showLanguageMenu,
                onDismissRequest = { showLanguageMenu = false },
                modifier = Modifier.background(RoyalEmerald)
              ) {
                listOf("BN", "EN", "AR").forEach { lang ->
                  val label = when (lang) {
                    "BN" -> "BN (বাংলা)"
                    "AR" -> "AR (العربية)"
                    else -> "EN (English)"
                  }
                  DropdownMenuItem(
                    text = {
                      Text(
                        text = label,
                        color = Color.White,
                        fontWeight = if (selectedLang == lang) FontWeight.Bold else FontWeight.Normal
                      )
                    },
                    onClick = {
                      onLanguageChange(lang)
                      showLanguageMenu = false
                    }
                  )
                }
              }
            }
          }
        )
      }
    ) { innerPadding ->
      Column(
        modifier = Modifier
          .fillMaxSize()
          .padding(innerPadding)
          .statusBarsPadding()
          .navigationBarsPadding()
          .imePadding()
          .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .widthIn(max = 580.dp)
            .padding(horizontal = 16.dp, vertical = 14.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          // Top Roadmap Intro Card
          RoadmapHeaderCard(selectedLang = selectedLang)

          Spacer(modifier = Modifier.height(16.dp))

          // Duolingo-style Winding Stepping Stones Path
          DuolingoWindingPath(
            levels = ShapeMasterRoadmapRepository.levels,
            unlockedLevels = unlockedLevels,
            levelStars = levelStars,
            selectedLang = selectedLang,
            onNodeClick = { level ->
              val isUnlocked = unlockedLevels[level.id] == true
              if (isUnlocked) {
                soundManager.playSuccessChime()
                previewLevelDialog = level
              } else {
                soundManager.playErrorBuzzer()
              }
            }
          )

          Spacer(modifier = Modifier.height(20.dp))

          // Dev helper button to unlock all levels for rapid evaluation
          TextButton(
            onClick = {
              ShapeMasterRoadmapRepository.levels.forEach { lvl ->
                unlockedLevels[lvl.id] = true
              }
            },
            modifier = Modifier.testTag("unlock_all_levels_btn")
          ) {
            Text(
              text = when (selectedLang) {
                "EN" -> "🔓 Unlock All Levels for Testing"
                "AR" -> "🔓 فتح جميع المستويات للتجربة"
                else -> "🔓 পরীক্ষার জন্য সব লেভেল আনলক করুন"
              },
              fontSize = 12.sp,
              color = RoyalEmerald,
              fontWeight = FontWeight.SemiBold
            )
          }

          Spacer(modifier = Modifier.height(40.dp))
        }
      }
    }
  }

  // Level Preview / Launch Bottom Dialog
  previewLevelDialog?.let { level ->
    LevelLaunchDialog(
      level = level,
      stars = levelStars[level.id] ?: 0,
      selectedLang = selectedLang,
      onDismiss = { previewLevelDialog = null },
      onStartGame = {
        previewLevelDialog = null
        selectedLevelForGame = level
      }
    )
  }
}

/**
 * Top Roadmap Intro Card
 */
@Composable
fun RoadmapHeaderCard(selectedLang: String) {
  Card(
    shape = RoundedCornerShape(18.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
    border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
    modifier = Modifier.fillMaxWidth()
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(14.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Box(
        modifier = Modifier
          .size(48.dp)
          .clip(CircleShape)
          .background(RoyalEmerald.copy(alpha = 0.12f)),
        contentAlignment = Alignment.Center
      ) {
        Text(text = "🗺️", fontSize = 24.sp)
      }
      Spacer(modifier = Modifier.width(12.dp))
      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = when (selectedLang) {
            "EN" -> "Journey Through Letter Shapes"
            "AR" -> "رحلة استكشاف أشكال الحروف"
            else -> "হরফের রূপভেদ আবিষ্কারের যাত্রা"
          },
          style = TextStyle(
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = RoyalEmerald
          )
        )
        Text(
          text = when (selectedLang) {
            "EN" -> "Tap each stepping stone to unlock Arabic mastery"
            "AR" -> "انقر على كل حجر خطوة لفتح الإتقان العربي"
            else -> "প্রতিটি ধাপে ট্যাপ করে নতুন নতুন রূপভেদ শিখুন"
          },
          style = TextStyle(fontSize = 11.sp, color = Color(0xFF64748B))
        )
      }
    }
  }
}

/**
 * Duolingo-style Winding Stepping Stones Path
 * Generates an alternating S-curve road with stepping stones, glowing pulse, and checkpoints
 */
@Composable
fun DuolingoWindingPath(
  levels: List<RoadmapLevel>,
  unlockedLevels: Map<Int, Boolean>,
  levelStars: Map<Int, Int>,
  selectedLang: String,
  onNodeClick: (RoadmapLevel) -> Unit
) {
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 8.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    levels.forEachIndexed { index, level ->
      val isUnlocked = unlockedLevels[level.id] == true
      val stars = levelStars[level.id] ?: 0
      val isCurrentActive = isUnlocked && (levelStars[level.id] == null || levelStars[level.id] == 0)

      // Duolingo wavy offset: Center, Left, Center, Right, Center...
      val horizontalOffsetDp = when (index % 4) {
        0 -> 0.dp
        1 -> (-60).dp
        2 -> 0.dp
        else -> 60.dp
      }

      // Connecting Path Segment (between nodes)
      if (index > 0) {
        ConnectingTrail(
          isUnlocked = isUnlocked,
          fromOffset = when ((index - 1) % 4) {
            0 -> 0.dp
            1 -> (-60).dp
            2 -> 0.dp
            else -> 60.dp
          },
          toOffset = horizontalOffsetDp
        )
      }

      // The 3D Stepping Stone Node
      RoadmapNodeButton(
        level = level,
        isUnlocked = isUnlocked,
        stars = stars,
        isCurrentActive = isCurrentActive,
        selectedLang = selectedLang,
        horizontalOffset = horizontalOffsetDp,
        onClick = { onNodeClick(level) }
      )
    }
  }
}

/**
 * Connecting Trail dashed line between nodes
 */
@Composable
fun ConnectingTrail(
  isUnlocked: Boolean,
  fromOffset: Dp,
  toOffset: Dp
) {
  Box(
    modifier = Modifier
      .fillMaxWidth()
      .height(44.dp),
    contentAlignment = Alignment.Center
  ) {
    Canvas(modifier = Modifier.fillMaxSize()) {
      val startX = (size.width / 2) + fromOffset.toPx()
      val endX = (size.width / 2) + toOffset.toPx()
      val path = Path().apply {
        moveTo(startX, 0f)
        cubicTo(
          startX, size.height * 0.5f,
          endX, size.height * 0.5f,
          endX, size.height
        )
      }

      drawPath(
        path = path,
        color = if (isUnlocked) RoyalEmerald.copy(alpha = 0.6f) else Color(0xFFCBD5E1),
        style = Stroke(
          width = 6.dp.toPx(),
          pathEffect = PathEffect.dashPathEffect(floatArrayOf(14f, 14f), 0f)
        )
      )
    }
  }
}

/**
 * 3D Stepping Stone Node Button
 */
@Composable
fun RoadmapNodeButton(
  level: RoadmapLevel,
  isUnlocked: Boolean,
  stars: Int,
  isCurrentActive: Boolean,
  selectedLang: String,
  horizontalOffset: Dp,
  onClick: () -> Unit
) {
  // Pulsating animation for the current active node
  val infiniteTransition = rememberInfiniteTransition(label = "pulse_trans")
  val pulseScale by infiniteTransition.animateFloat(
    initialValue = 1f,
    targetValue = 1.12f,
    animationSpec = infiniteRepeatable(
      animation = tween(900, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "pulse_scale"
  )

  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier.offset(x = horizontalOffset)
  ) {
    Box(
      contentAlignment = Alignment.Center,
      modifier = Modifier.size(88.dp)
    ) {
      // Glowing halo for current active level
      if (isCurrentActive) {
        Box(
          modifier = Modifier
            .size(86.dp)
            .scale(pulseScale)
            .clip(CircleShape)
            .background(level.nodeColor.copy(alpha = 0.25f))
        )
      }

      // 3D Push Button Stone
      val buttonColor = if (isUnlocked) level.nodeColor else Color(0xFF94A3B8)
      val shadowColor = if (isUnlocked) level.nodeColor.copy(alpha = 0.8f) else Color(0xFF64748B)

      // Bottom shadow layer (Duolingo 3D button effect)
      Box(
        modifier = Modifier
          .size(72.dp)
          .offset(y = 5.dp)
          .clip(CircleShape)
          .background(shadowColor)
      )

      // Top Button Surface
      Box(
        modifier = Modifier
          .size(72.dp)
          .clip(CircleShape)
          .background(
            if (isUnlocked) {
              Brush.verticalGradient(
                colors = listOf(buttonColor, buttonColor.copy(alpha = 0.9f))
              )
            } else {
              Brush.verticalGradient(
                colors = listOf(Color(0xFFCBD5E1), Color(0xFF94A3B8))
              )
            }
          )
          .border(2.5.dp, Color.White.copy(alpha = 0.4f), CircleShape)
          .clickable { onClick() }
          .testTag("roadmap_node_${level.id}"),
        contentAlignment = Alignment.Center
      ) {
        if (!isUnlocked) {
          Icon(
            imageVector = Icons.Default.Lock,
            contentDescription = "Locked",
            tint = Color.White,
            modifier = Modifier.size(28.dp)
          )
        } else if (level.isCheckpoint) {
          Text(
            text = level.iconEmoji,
            fontSize = 32.sp
          )
        } else {
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
              text = level.iconEmoji,
              fontSize = 20.sp
            )
            Text(
              text = "${level.id}",
              style = TextStyle(
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
              )
            )
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(4.dp))

    // Level Title Tag
    Text(
      text = level.getTitle(selectedLang),
      style = TextStyle(
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        color = if (isUnlocked) Color(0xFF0F172A) else Color(0xFF94A3B8)
      ),
      maxLines = 1,
      overflow = TextOverflow.Ellipsis
    )

    // Star Rating under node
    if (isUnlocked) {
      Row(
        modifier = Modifier.padding(top = 2.dp),
        horizontalArrangement = Arrangement.Center
      ) {
        repeat(3) { index ->
          val isEarned = index < stars
          Icon(
            imageVector = Icons.Default.Star,
            contentDescription = "Star",
            tint = if (isEarned) Color(0xFFF59E0B) else Color(0xFFCBD5E1),
            modifier = Modifier.size(13.dp)
          )
        }
      }
    }
  }
}

/**
 * Level Preview Launch Dialog
 */
@Composable
fun LevelLaunchDialog(
  level: RoadmapLevel,
  stars: Int,
  selectedLang: String,
  onDismiss: () -> Unit,
  onStartGame: () -> Unit
) {
  Dialog(onDismissRequest = onDismiss) {
    Card(
      shape = RoundedCornerShape(24.dp),
      colors = CardDefaults.cardColors(containerColor = Color.White),
      elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
      modifier = Modifier
        .fillMaxWidth()
        .padding(12.dp)
        .testTag("level_launch_dialog")
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        // Node Icon Badge
        Box(
          modifier = Modifier
            .size(64.dp)
            .clip(CircleShape)
            .background(level.nodeColor.copy(alpha = 0.15f))
            .border(2.dp, level.nodeColor, CircleShape),
          contentAlignment = Alignment.Center
        ) {
          Text(text = level.iconEmoji, fontSize = 32.sp)
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
          text = level.getTitle(selectedLang),
          style = TextStyle(
            fontSize = 19.sp,
            fontWeight = FontWeight.Bold,
            color = level.nodeColor
          )
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
          text = level.getSubtitle(selectedLang),
          style = TextStyle(
            fontSize = 12.sp,
            color = Color(0xFF64748B),
            textAlign = TextAlign.Center
          ),
          modifier = Modifier.padding(horizontal = 8.dp)
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Level Details Box
        Card(
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
          border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              Text(text = "❓", fontSize = 16.sp)
              Text(
                text = "${level.questions.size} Questions",
                style = TextStyle(fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
              )
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              Text(text = "⚡", fontSize = 16.sp)
              Text(
                text = "+${level.rewardXp} XP",
                style = TextStyle(fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF4F46E5))
              )
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              Text(text = "⭐", fontSize = 16.sp)
              Text(
                text = "+${level.rewardStars} Stars",
                style = TextStyle(fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD97706))
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Start Game Button
        Button(
          onClick = onStartGame,
          colors = ButtonDefaults.buttonColors(containerColor = level.nodeColor),
          shape = RoundedCornerShape(14.dp),
          modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .testTag("start_level_btn")
        ) {
          Icon(
            imageVector = Icons.Default.PlayArrow,
            contentDescription = "Start",
            tint = Color.White,
            modifier = Modifier.size(20.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = when (selectedLang) {
              "EN" -> "START LEVEL"
              "AR" -> "ابدأ المستوى"
              else -> "লেভেল শুরু করুন"
            },
            style = TextStyle(
              fontSize = 14.sp,
              fontWeight = FontWeight.Bold,
              color = Color.White,
              letterSpacing = 1.sp
            )
          )
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(
          onClick = onDismiss,
          shape = RoundedCornerShape(14.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Text(
            text = when (selectedLang) {
              "EN" -> "Back"
              "AR" -> "رجوع"
              else -> "ফিরে যান"
            },
            color = Color(0xFF64748B)
          )
        }
      }
    }
  }
}
