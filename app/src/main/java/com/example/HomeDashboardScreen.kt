package com.example

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ViewCarousel
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.BackgroundGray
import com.example.ui.theme.RoyalEmerald

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeDashboardScreen(
  selectedLang: String,
  onLanguageChange: (String) -> Unit,
  userEmail: String,
  onSignOut: () -> Unit
) {
  val context = LocalContext.current
  val soundManager = remember { SoundManager(context) }
  DisposableEffect(Unit) {
    onDispose { soundManager.release() }
  }

  val layoutDirection = if (selectedLang == "AR") LayoutDirection.Rtl else LayoutDirection.Ltr
  var showLanguageMenu by remember { mutableStateOf(false) }

  // Interactive user stats
  var userCoins by remember { mutableIntStateOf(250) }
  var userLevel by remember { mutableIntStateOf(1) }
  var userStreak by remember { mutableIntStateOf(3) }
  var selectedLetterForDetail by remember { mutableStateOf<ArabicLetter?>(null) }
  var letterForMakhrajVisualizer by remember { mutableStateOf<ArabicLetter?>(null) }
  var showSettingsDialog by remember { mutableStateOf(false) }
  var activeGameModal by remember { mutableStateOf<GameMode?>(null) }
  var isLetterGridView by remember { mutableStateOf(false) }
  var isShapeMasterRoadmapOpen by remember { mutableStateOf(false) }

  if (isShapeMasterRoadmapOpen) {
    ShapeMasterRoadmapScreen(
      selectedLang = selectedLang,
      onLanguageChange = onLanguageChange,
      soundManager = soundManager,
      onBackToDashboard = { isShapeMasterRoadmapOpen = false },
      onLevelStatsUpdated = { starsAdded, xpAdded ->
        userCoins += starsAdded
        userStreak += 1
        userLevel = 1 + (userCoins / 300)
      }
    )
    return
  }

  CompositionLocalProvider(LocalLayoutDirection provides layoutDirection) {
    Scaffold(
      containerColor = BackgroundGray,
      topBar = {
        TopAppBar(
          title = {
            Text(
              text = AppStrings.getAppName(selectedLang),
              style = TextStyle(
                fontWeight = FontWeight.Bold,
                color = Color.White,
                fontSize = 20.sp,
                letterSpacing = if (selectedLang == "EN") 1.2.sp else 0.sp
              )
            )
          },
          colors = TopAppBarDefaults.topAppBarColors(
            containerColor = RoyalEmerald
          ),
          actions = {
            // Language selector dropdown on top right
            Box {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                  .clip(RoundedCornerShape(8.dp))
                  .clickable { showLanguageMenu = true }
                  .padding(horizontal = 8.dp, vertical = 6.dp)
                  .testTag("home_language_dropdown")
              ) {
                Icon(
                  imageVector = Icons.Default.Language,
                  contentDescription = "Language",
                  tint = Color.White
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                  text = selectedLang,
                  style = TextStyle(
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                  )
                )
              }

              DropdownMenu(
                expanded = showLanguageMenu,
                onDismissRequest = { showLanguageMenu = false },
                modifier = Modifier
                  .background(RoyalEmerald)
                  .testTag("home_lang_menu")
              ) {
                listOf("BN", "EN", "AR").forEach { lang ->
                  val label = when (lang) {
                    "BN" -> "BN (বাংলা)"
                    "AR" -> "AR (العربية)"
                    else -> "EN (English)"
                  }
                  DropdownMenuItem(
                    text = {
                      Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                      ) {
                        Text(
                          text = label,
                          style = TextStyle(
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                          )
                        )
                        if (selectedLang == lang) {
                          Spacer(modifier = Modifier.width(8.dp))
                          Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Selected",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                          )
                        }
                      }
                    },
                    onClick = {
                      onLanguageChange(lang)
                      showLanguageMenu = false
                    }
                  )
                }
              }
            }

            IconButton(
              onClick = { showSettingsDialog = true },
              modifier = Modifier.testTag("home_settings_button")
            ) {
              Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = "Settings",
                tint = Color.White
              )
            }

            IconButton(
              onClick = onSignOut,
              modifier = Modifier.testTag("home_logout_button")
            ) {
              Icon(
                imageVector = Icons.AutoMirrored.Filled.Logout,
                contentDescription = "Sign Out",
                tint = Color.White.copy(alpha = 0.9f)
              )
            }

            Spacer(modifier = Modifier.width(8.dp))
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
            .widthIn(max = 680.dp)
            .padding(horizontal = 16.dp, vertical = 12.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          // 2. User Stats Bar Card (Level 1, 250 ⭐, 3 Days 🔥)
          UserStatsBarCard(
            level = userLevel,
            coins = userCoins,
            streak = userStreak,
            selectedLang = selectedLang,
            modifier = Modifier.fillMaxWidth()
          )

          Spacer(modifier = Modifier.height(14.dp))

          // Hero Graphic Banner
          DashboardHeroBanner(
            selectedLang = selectedLang
          )

          Spacer(modifier = Modifier.height(20.dp))

          // 3. Game Selector Menu Section Header
          SectionHeader(
            title = when (selectedLang) {
              "EN" -> "Select Game Mode"
              "AR" -> "اختر نمط اللعبة"
              else -> "গেম মোড নির্বাচন করুন"
            },
            subtitle = when (selectedLang) {
              "EN" -> "5 Interactive 3D learning adventures"
              "AR" -> "٥ أنماط تفاعلية لتعلم الحروف"
              else -> "৫টি আকর্ষণীয় ৩ডি গেমিফাইড অ্যাডভেঞ্চার"
            }
          )

          Spacer(modifier = Modifier.height(12.dp))

          // 3. Game Selector Cards
          GameRepository.gameModes.forEach { gameMode ->
            GameModeCard3D(
              gameMode = gameMode,
              selectedLang = selectedLang,
              onPlayClick = {
                soundManager.playSuccessChime()
                if (gameMode.type == GameType.SHAPE_MASTER_PATH) {
                  isShapeMasterRoadmapOpen = true
                } else {
                  activeGameModal = gameMode
                }
              },
              modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))
          }

          Spacer(modifier = Modifier.height(14.dp))

          // 3D Makhraj Visualizer Feature Banner
          Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            border = BorderStroke(1.5.dp, Color(0xFF10B981).copy(alpha = 0.5f)),
            modifier = Modifier
              .fillMaxWidth()
              .clickable {
                letterForMakhrajVisualizer = ArabicAlphabetRepository.letters.first()
              }
              .testTag("makhraj_visualizer_banner")
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Box(
                  modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(
                      Brush.radialGradient(
                        colors = listOf(Color(0xFF10B981).copy(alpha = 0.3f), Color.Transparent)
                      )
                    )
                    .border(2.dp, Color(0xFF10B981), CircleShape),
                  contentAlignment = Alignment.Center
                ) {
                  Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = null,
                    tint = Color(0xFF34D399),
                    modifier = Modifier.size(26.dp)
                  )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                  Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                      text = when (selectedLang) {
                        "EN" -> "3D Makhraj Visualizer"
                        "AR" -> "المجسم ثلاثي الأبعاد للمخارج"
                        else -> "মাখরাজ ৩ডি ভিজ্যুয়ালাইজার"
                      },
                      style = TextStyle(
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                      )
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Surface(
                      shape = RoundedCornerShape(4.dp),
                      color = Color(0xFF10B981).copy(alpha = 0.2f),
                      border = BorderStroke(0.5.dp, Color(0xFF10B981))
                    ) {
                      Text(
                        text = "3D",
                        style = TextStyle(
                          fontSize = 9.sp,
                          fontWeight = FontWeight.ExtraBold,
                          color = Color(0xFF34D399)
                        ),
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                      )
                    }
                  }

                  Spacer(modifier = Modifier.height(2.dp))

                  Text(
                    text = when (selectedLang) {
                      "EN" -> "Explore vocal tract, tongue motion & airflow in 3D"
                      "AR" -> "استكشف حركة اللسان والشفتين ومسار تدفق الهواء"
                      else -> "কণ্ঠনালী, জিহ্বার স্পর্শ ও বায়ুপ্রবাহ ৩ডি কোণে দেখুন"
                    },
                    style = TextStyle(
                      fontSize = 11.sp,
                      color = Color(0xFF94A3B8),
                      lineHeight = 14.sp
                    )
                  )
                }
              }

              Button(
                onClick = {
                  letterForMakhrajVisualizer = ArabicAlphabetRepository.letters.first()
                },
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = RoyalEmerald),
                modifier = Modifier
                  .padding(start = 6.dp)
                  .testTag("open_makhraj_banner_btn")
              ) {
                Text(
                  text = when (selectedLang) { "EN" -> "View" "AR" -> "عرض" else -> "দেখুন" },
                  style = TextStyle(fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(18.dp))

          // 4. Interactive Letter Chart Quick-Access Section Header
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = when (selectedLang) {
                  "EN" -> "Interactive Letter Chart (29 Letters)"
                  "AR" -> "جدول الحروف التفاعلي (٢٩ حرفاً)"
                  else -> "ইন্টারেক্টিভ হরফ চার্ট (২৯টি হরফ)"
                },
                style = TextStyle(
                  fontSize = 18.sp,
                  fontWeight = FontWeight.Bold,
                  color = RoyalEmerald
                )
              )
              Text(
                text = when (selectedLang) {
                  "EN" -> "Tap any letter to view its 4 positional forms"
                  "AR" -> "اضغط على أي حرف لعرض أشكاله الأربعة"
                  else -> "যেকোনো হরফে ট্যাপ করে ৪টি রূপ ও মাখরাজ দেখুন"
                },
                style = TextStyle(
                  fontSize = 12.sp,
                  color = Color(0xFF64748B)
                )
              )
            }

            // View toggle (Horizontal Carousel vs Grid)
            IconButton(
              onClick = { isLetterGridView = !isLetterGridView },
              modifier = Modifier
                .clip(CircleShape)
                .background(Color.White)
                .border(1.dp, Color(0xFFE2E8F0), CircleShape)
            ) {
              Icon(
                imageVector = if (isLetterGridView) Icons.Default.ViewCarousel else Icons.Default.GridView,
                contentDescription = "Toggle View",
                tint = RoyalEmerald
              )
            }
          }

          Spacer(modifier = Modifier.height(12.dp))

          // 4. Interactive Letter Chart View
          if (!isLetterGridView) {
            // Horizontal Carousel
            LazyRow(
              contentPadding = PaddingValues(horizontal = 4.dp, vertical = 6.dp),
              horizontalArrangement = Arrangement.spacedBy(10.dp),
              modifier = Modifier
                .fillMaxWidth()
                .testTag("letters_carousel")
            ) {
              items(ArabicAlphabetRepository.letters) { letter ->
                LetterCardItem(
                  letter = letter,
                  selectedLang = selectedLang,
                  onClick = {
                    soundManager.speakArabicOrBeep(letter.letter)
                    selectedLetterForDetail = letter
                  }
                )
              }
            }
          } else {
            // Grid View
            @OptIn(ExperimentalLayoutApi::class)
            FlowRow(
              maxItemsInEachRow = 5,
              horizontalArrangement = Arrangement.spacedBy(8.dp),
              verticalArrangement = Arrangement.spacedBy(8.dp),
              modifier = Modifier
                .fillMaxWidth()
                .testTag("letters_grid")
            ) {
              ArabicAlphabetRepository.letters.forEach { letter ->
                LetterGridItem(
                  letter = letter,
                  selectedLang = selectedLang,
                  onClick = {
                    soundManager.speakArabicOrBeep(letter.letter)
                    selectedLetterForDetail = letter
                  }
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(28.dp))
        }
      }
    }
  }

  // 4. Detail Pop-up showing 4 forms of selected letter
  selectedLetterForDetail?.let { letter ->
    LetterDetailDialog(
      letter = letter,
      selectedLang = selectedLang,
      soundManager = soundManager,
      onOpenMakhraj = { makhrajLetter ->
        letterForMakhrajVisualizer = makhrajLetter
      },
      onDismiss = { selectedLetterForDetail = null }
    )
  }

  // 5. 3D Makhraj Visualizer Modal
  letterForMakhrajVisualizer?.let { letter ->
    MakhrajVisualizerModal(
      initialLetter = letter,
      selectedLang = selectedLang,
      soundManager = soundManager,
      onDismiss = { letterForMakhrajVisualizer = null }
    )
  }

  // 6. Global App Settings Modal (Voice Gender Selection & Language)
  if (showSettingsDialog) {
    AppSettingsDialog(
      selectedLang = selectedLang,
      onLanguageChange = onLanguageChange,
      soundManager = soundManager,
      onOpenMakhrajVisualizer = {
        letterForMakhrajVisualizer = ArabicAlphabetRepository.letters.first()
      },
      onDismiss = { showSettingsDialog = false }
    )
  }

  // Playable interactive modal for each Game Mode
  activeGameModal?.let { game ->
    GameInteractiveModal(
      game = game,
      selectedLang = selectedLang,
      soundManager = soundManager,
      onCoinsEarned = { earned ->
        userCoins += earned
        userStreak += 1
      },
      onDismiss = { activeGameModal = null }
    )
  }
}

/**
 * 2. User Stats Bar Card:
 * Shows Current Level (Level 1), Coins/Stars earned (250 ⭐), Daily Streak (3 Days 🔥)
 */
@Composable
fun UserStatsBarCard(
  level: Int,
  coins: Int,
  streak: Int,
  selectedLang: String,
  modifier: Modifier = Modifier
) {
  Card(
    shape = RoundedCornerShape(18.dp),
    colors = CardDefaults.cardColors(
      containerColor = Color.White
    ),
    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
    border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
    modifier = modifier.testTag("user_stats_bar")
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(14.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        // Level Pill
        StatChip(
          iconEmoji = "🏆",
          label = when (selectedLang) {
            "EN" -> "Level $level"
            "AR" -> "المستوى $level"
            else -> "লেভেল $level"
          },
          subtitle = when (selectedLang) {
            "EN" -> "Novice"
            "AR" -> "مبتدئ"
            else -> "শিক্ষানবিস"
          },
          backgroundColor = Color(0xFFFEF3C7),
          textColor = Color(0xFF92400E)
        )

        // Coins / Stars
        StatChip(
          iconEmoji = "⭐",
          label = "$coins",
          subtitle = when (selectedLang) {
            "EN" -> "Stars"
            "AR" -> "نجوم"
            else -> "তারকা"
          },
          backgroundColor = Color(0xFFFEF9C3),
          textColor = Color(0xFF854D0E)
        )

        // Streak
        StatChip(
          iconEmoji = "🔥",
          label = when (selectedLang) {
            "EN" -> "$streak Days"
            "AR" -> "$streak أيام"
            else -> "$streak দিন"
          },
          subtitle = when (selectedLang) {
            "EN" -> "Streak"
            "AR" -> "حماس"
            else -> "ধারাবাহিক"
          },
          backgroundColor = Color(0xFFFFEDD5),
          textColor = Color(0xFF9A3412)
        )
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Level XP Progress Bar
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = when (selectedLang) {
            "EN" -> "Level Progress"
            "AR" -> "تقدم المستوى"
            else -> "লেভেল অগ্রগতি"
          },
          style = TextStyle(
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF64748B)
          )
        )
        Text(
          text = "320 / 500 XP (64%)",
          style = TextStyle(
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = RoyalEmerald
          )
        )
      }

      Spacer(modifier = Modifier.height(4.dp))

      LinearProgressIndicator(
        progress = { 0.64f },
        modifier = Modifier
          .fillMaxWidth()
          .height(6.dp)
          .clip(RoundedCornerShape(3.dp)),
        color = RoyalEmerald,
        trackColor = Color(0xFFE2E8F0)
      )
    }
  }
}

@Composable
fun StatChip(
  iconEmoji: String,
  label: String,
  subtitle: String,
  backgroundColor: Color,
  textColor: Color
) {
  Row(
    verticalAlignment = Alignment.CenterVertically,
    modifier = Modifier
      .clip(RoundedCornerShape(12.dp))
      .background(backgroundColor)
      .padding(horizontal = 10.dp, vertical = 6.dp)
  ) {
    Text(
      text = iconEmoji,
      fontSize = 18.sp
    )
    Spacer(modifier = Modifier.width(6.dp))
    Column {
      Text(
        text = label,
        style = TextStyle(
          fontSize = 13.sp,
          fontWeight = FontWeight.Bold,
          color = textColor
        )
      )
      Text(
        text = subtitle,
        style = TextStyle(
          fontSize = 9.sp,
          color = textColor.copy(alpha = 0.8f)
        )
      )
    }
  }
}

/**
 * Hero Banner with high quality graphic aesthetic
 */
@Composable
fun DashboardHeroBanner(
  selectedLang: String
) {
  Card(
    shape = RoundedCornerShape(20.dp),
    colors = CardDefaults.cardColors(containerColor = RoyalEmerald),
    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    modifier = Modifier.fillMaxWidth()
  ) {
    Box(modifier = Modifier.fillMaxWidth()) {
      // Background Hero Graphic
      Image(
        painter = painterResource(id = R.drawable.img_dashboard_hero),
        contentDescription = "Dashboard Banner",
        modifier = Modifier
          .fillMaxWidth()
          .height(130.dp),
        contentScale = ContentScale.Crop,
        alpha = 0.42f
      )

      // Gradient overlay for readability
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(130.dp)
          .background(
            Brush.horizontalGradient(
              colors = listOf(
                RoyalEmerald.copy(alpha = 0.95f),
                RoyalEmerald.copy(alpha = 0.65f)
              )
            )
          )
      )

      // Banner Text Content
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(16.dp)
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(text = "✨", fontSize = 16.sp)
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = when (selectedLang) {
              "EN" -> "Welcome, Learner!"
              "AR" -> "أهلاً بك يا بطل!"
              else -> "স্বাগতম, প্রিয় শিক্ষার্থী!"
            },
            style = TextStyle(
              fontSize = 13.sp,
              fontWeight = FontWeight.SemiBold,
              color = Color(0xFFF6E05E)
            )
          )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
          text = when (selectedLang) {
            "EN" -> "Master Arabic Letter Forms with Play"
            "AR" -> "أتقن أشكال الحروف العربية باللعب والتشويق"
            else -> "খেলার ছলে শিখুন আরবি হরফের রূপভেদ"
          },
          style = TextStyle(
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
          )
        )

        Spacer(modifier = Modifier.height(2.dp))

        Text(
          text = when (selectedLang) {
            "EN" -> "Choose a 3D game below or tap a letter to explore."
            "AR" -> "اختر نمط لعب أدناه أو اضغط على أي حرف للاستكشاف."
            else -> "নিচের যেকোনো গেম খেলুন অথবা হরফে ট্যাপ করে জানুন।"
          },
          style = TextStyle(
            fontSize = 11.sp,
            color = Color.White.copy(alpha = 0.9f)
          )
        )
      }
    }
  }
}

/**
 * 3. High-quality 3D-styled clickable Game Mode Card
 */
@Composable
fun GameModeCard3D(
  gameMode: GameMode,
  selectedLang: String,
  onPlayClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Card(
    shape = RoundedCornerShape(20.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    elevation = CardDefaults.cardElevation(
      defaultElevation = 4.dp,
      pressedElevation = 8.dp
    ),
    border = BorderStroke(1.5.dp, gameMode.primaryColor.copy(alpha = 0.25f)),
    modifier = modifier
      .shadow(6.dp, RoundedCornerShape(20.dp), spotColor = gameMode.primaryColor.copy(alpha = 0.35f))
      .clickable { onPlayClick() }
      .testTag("game_card_${gameMode.type.name}")
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .background(
          Brush.linearGradient(
            colors = listOf(
              gameMode.primaryColor.copy(alpha = 0.08f),
              Color.White,
              gameMode.secondaryColor.copy(alpha = 0.04f)
            )
          )
        )
        .padding(16.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
      ) {
        // 3D Styled Icon container
        Box(
          modifier = Modifier
            .size(56.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(
              Brush.linearGradient(
                colors = listOf(gameMode.primaryColor, gameMode.secondaryColor)
              )
            )
            .border(2.dp, gameMode.accentColor.copy(alpha = 0.6f), RoundedCornerShape(16.dp))
            .shadow(4.dp, RoundedCornerShape(16.dp)),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = gameMode.iconEmoji,
            fontSize = 28.sp
          )
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            // Badge tag
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(gameMode.primaryColor)
                .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
              Text(
                text = gameMode.getBadge(selectedLang),
                style = TextStyle(
                  fontSize = 9.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color.White,
                  letterSpacing = 0.5.sp
                )
              )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Star rating indicator
            Row {
              repeat(gameMode.starsCount) {
                Icon(
                  imageVector = Icons.Default.Star,
                  contentDescription = "Star",
                  tint = Color(0xFFF59E0B),
                  modifier = Modifier.size(13.dp)
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(3.dp))

          Text(
            text = gameMode.getTitle(selectedLang),
            style = TextStyle(
              fontSize = 17.sp,
              fontWeight = FontWeight.Bold,
              color = Color(0xFF0F172A)
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
          )

          Text(
            text = gameMode.getSubtitle(selectedLang),
            style = TextStyle(
              fontSize = 11.sp,
              color = Color(0xFF475569),
              lineHeight = 15.sp
            ),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
          )
        }

        Spacer(modifier = Modifier.width(8.dp))

        // 3D-styled Action button
        Button(
          onClick = onPlayClick,
          shape = RoundedCornerShape(12.dp),
          colors = ButtonDefaults.buttonColors(
            containerColor = gameMode.primaryColor
          ),
          contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp),
          modifier = Modifier
            .shadow(3.dp, RoundedCornerShape(12.dp), spotColor = gameMode.primaryColor)
            .testTag("play_btn_${gameMode.type.name}")
        ) {
          Icon(
            imageVector = Icons.Default.PlayArrow,
            contentDescription = "Play",
            tint = Color.White,
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text(
            text = when (selectedLang) {
              "EN" -> "Play"
              "AR" -> "العب"
              else -> "খেলুন"
            },
            style = TextStyle(
              fontSize = 13.sp,
              fontWeight = FontWeight.Bold,
              color = Color.White
            )
          )
        }
      }
    }
  }
}

/**
 * 4. Letter Item for the Horizontal Carousel
 */
@Composable
fun LetterCardItem(
  letter: ArabicLetter,
  selectedLang: String,
  onClick: () -> Unit
) {
  Card(
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
    border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
    modifier = Modifier
      .width(84.dp)
      .clickable { onClick() }
      .testTag("letter_card_${letter.id}")
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Text(
        text = "#${letter.id}",
        style = TextStyle(
          fontSize = 10.sp,
          fontWeight = FontWeight.SemiBold,
          color = Color(0xFF94A3B8)
        )
      )

      Spacer(modifier = Modifier.height(2.dp))

      Text(
        text = letter.letter,
        style = TextStyle(
          fontSize = 32.sp,
          fontWeight = FontWeight.Bold,
          color = RoyalEmerald,
          fontFamily = FontFamily.Serif
        )
      )

      Spacer(modifier = Modifier.height(2.dp))

      Text(
        text = letter.getName(selectedLang),
        style = TextStyle(
          fontSize = 11.sp,
          fontWeight = FontWeight.Medium,
          color = Color(0xFF1E293B)
        ),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
      )

      Text(
        text = letter.phonetic,
        style = TextStyle(
          fontSize = 10.sp,
          color = Color(0xFF64748B)
        )
      )
    }
  }
}

/**
 * 4. Letter Grid Item for the Quick-Access Grid
 */
@Composable
fun LetterGridItem(
  letter: ArabicLetter,
  selectedLang: String,
  onClick: () -> Unit
) {
  Card(
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
    modifier = Modifier
      .size(64.dp)
      .clickable { onClick() }
      .testTag("letter_grid_${letter.id}")
  ) {
    Column(
      modifier = Modifier.fillMaxSize(),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center
    ) {
      Text(
        text = letter.letter,
        style = TextStyle(
          fontSize = 24.sp,
          fontWeight = FontWeight.Bold,
          color = RoyalEmerald,
          fontFamily = FontFamily.Serif
        )
      )
      Text(
        text = letter.getName(selectedLang),
        style = TextStyle(
          fontSize = 9.sp,
          fontWeight = FontWeight.Medium,
          color = Color(0xFF475569)
        ),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
      )
    }
  }
}

/**
 * 4. Pop-up Dialog showing the 4 forms of the letter:
 * Isolated, Initial, Medial, Final with Audio Feedback Placeholder
 */
@Composable
fun LetterDetailDialog(
  letter: ArabicLetter,
  selectedLang: String,
  soundManager: SoundManager,
  onOpenMakhraj: (ArabicLetter) -> Unit,
  onDismiss: () -> Unit
) {
  Dialog(onDismissRequest = onDismiss) {
    Card(
      shape = RoundedCornerShape(24.dp),
      colors = CardDefaults.cardColors(containerColor = Color.White),
      elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
      modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)
        .testTag("letter_detail_dialog")
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        // Header with close button
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = when (selectedLang) {
              "EN" -> "Letter #${letter.id} • ${letter.nameEn}"
              "AR" -> "الحرف رقم ${letter.id} • ${letter.nameAr}"
              else -> "হরফ নং ${letter.id} • ${letter.nameBn}"
            },
            style = TextStyle(
              fontSize = 15.sp,
              fontWeight = FontWeight.Bold,
              color = RoyalEmerald
            )
          )

          IconButton(
            onClick = onDismiss,
            modifier = Modifier.size(28.dp)
          ) {
            Icon(
              imageVector = Icons.Default.Close,
              contentDescription = "Close",
              tint = Color(0xFF64748B)
            )
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Prominent Letter Display
        Box(
          modifier = Modifier
            .size(86.dp)
            .clip(CircleShape)
            .background(
              Brush.radialGradient(
                colors = listOf(RoyalEmerald.copy(alpha = 0.15f), RoyalEmerald.copy(alpha = 0.04f))
              )
            )
            .border(2.dp, RoyalEmerald.copy(alpha = 0.4f), CircleShape),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = letter.letter,
            style = TextStyle(
              fontSize = 44.sp,
              fontWeight = FontWeight.Bold,
              color = RoyalEmerald,
              fontFamily = FontFamily.Serif
            )
          )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // 4 Forms Section (Isolated, Initial, Medial, Final)
        Text(
          text = when (selectedLang) {
            "EN" -> "4 Contextual Forms of the Letter"
            "AR" -> "الأشكال الأربعة للحرف في الكلمة"
            else -> "শব্দে হরফের ৪টি ভিন্ন রূপ"
          },
          style = TextStyle(
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1E293B)
          )
        )

        Spacer(modifier = Modifier.height(10.dp))

        // 4 Forms Row
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceEvenly
        ) {
          FormBox(
            title = when (selectedLang) {
              "EN" -> "Isolated"
              "AR" -> "مستقل"
              else -> "বিচ্ছিন্ন"
            },
            glyph = letter.isolated,
            subLabel = "مستقل"
          )

          FormBox(
            title = when (selectedLang) {
              "EN" -> "Initial"
              "AR" -> "بداية"
              else -> "শুরুতে"
            },
            glyph = letter.initial,
            subLabel = "أول الكلمة"
          )

          FormBox(
            title = when (selectedLang) {
              "EN" -> "Medial"
              "AR" -> "وسط"
              else -> "মাঝে"
            },
            glyph = letter.medial,
            subLabel = "وسط الكلمة"
          )

          FormBox(
            title = when (selectedLang) {
              "EN" -> "Final"
              "AR" -> "نهاية"
              else -> "শেষে"
            },
            glyph = letter.finalForm,
            subLabel = "آخر الكلمة"
          )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Makhraj (Articulation Point)
        Card(
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
          border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(12.dp)) {
            Text(
              text = when (selectedLang) {
                "EN" -> "Articulation Point (Makhraj):"
                "AR" -> "مخرج الحرف:"
                else -> "উচ্চারণস্থান (মাখরাজ):"
              },
              style = TextStyle(
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = RoyalEmerald
              )
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
              text = letter.getMakhraj(selectedLang),
              style = TextStyle(
                fontSize = 11.sp,
                color = Color(0xFF334155),
                lineHeight = 15.sp
              )
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "${when (selectedLang) { "EN" -> "Example:" "AR" -> "مثال:" else -> "উদাহরণ:" }} ${letter.sampleWord}",
                style = TextStyle(
                  fontSize = 12.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color(0xFF0F172A)
                )
              )
              Text(
                text = "(${letter.sampleWordTranslation})",
                style = TextStyle(
                  fontSize = 11.sp,
                  color = Color(0xFF64748B)
                )
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // 3D Makhraj Visualizer Button
        Button(
          onClick = {
            onDismiss()
            onOpenMakhraj(letter)
          },
          shape = RoundedCornerShape(14.dp),
          colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F172A)),
          border = BorderStroke(1.5.dp, RoyalEmerald),
          modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .testTag("open_makhraj_modal_from_detail")
        ) {
          Icon(
            imageVector = Icons.Default.Refresh,
            contentDescription = "3D Makhraj",
            tint = Color(0xFF34D399),
            modifier = Modifier.size(20.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = when (selectedLang) {
              "EN" -> "3D Articulation Point (Makhraj) View 👁️"
              "AR" -> "عرض مخرج الحرف ثلاثي الأبعاد 👁️"
              else -> "উচ্চারণ স্থান ও ৩ডি মাখরাজ ভিউ 👁️"
            },
            style = TextStyle(
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold,
              color = Color.White
            )
          )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Audio Feedback Button with Voice Gender indicator
        Button(
          onClick = {
            soundManager.speakArabicLetter(letter.letter, soundManager.currentVoiceGender)
          },
          shape = RoundedCornerShape(14.dp),
          colors = ButtonDefaults.buttonColors(containerColor = RoyalEmerald),
          modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .testTag("play_letter_audio")
        ) {
          Icon(
            imageVector = Icons.Default.VolumeUp,
            contentDescription = "Audio",
            tint = Color.White,
            modifier = Modifier.size(20.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = when (selectedLang) {
              "EN" -> "Play Audio (${if (soundManager.currentVoiceGender == VoiceGender.MALE_QARI) "Qari" else "Qaria"}) 🔊"
              "AR" -> "استمع للنطق (${if (soundManager.currentVoiceGender == VoiceGender.MALE_QARI) "قارئ" else "قارئة"}) 🔊"
              else -> "সঠিক উচ্চারণ শুনুন (${if (soundManager.currentVoiceGender == VoiceGender.MALE_QARI) "ক্বারী" else "ক্বারিয়া"}) 🔊"
            },
            style = TextStyle(
              fontSize = 13.sp,
              fontWeight = FontWeight.Bold,
              color = Color.White
            )
          )
        }
      }
    }
  }
}

@Composable
fun FormBox(
  title: String,
  glyph: String,
  subLabel: String
) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier.padding(horizontal = 4.dp)
  ) {
    Text(
      text = title,
      style = TextStyle(
        fontSize = 10.sp,
        fontWeight = FontWeight.SemiBold,
        color = Color(0xFF64748B)
      )
    )

    Spacer(modifier = Modifier.height(4.dp))

    Box(
      modifier = Modifier
        .size(54.dp)
        .clip(RoundedCornerShape(12.dp))
        .background(Color(0xFFF1F5F9))
        .border(1.5.dp, RoyalEmerald.copy(alpha = 0.3f), RoundedCornerShape(12.dp)),
      contentAlignment = Alignment.Center
    ) {
      Text(
        text = glyph,
        style = TextStyle(
          fontSize = 24.sp,
          fontWeight = FontWeight.Bold,
          color = RoyalEmerald,
          fontFamily = FontFamily.Serif
        )
      )
    }

    Spacer(modifier = Modifier.height(2.dp))

    Text(
      text = subLabel,
      style = TextStyle(
        fontSize = 9.sp,
        color = Color(0xFF94A3B8)
      )
    )
  }
}

/**
 * Playable Interactive Modal for each Game Mode
 */
@Composable
fun GameInteractiveModal(
  game: GameMode,
  selectedLang: String,
  soundManager: SoundManager,
  onCoinsEarned: (Int) -> Unit,
  onDismiss: () -> Unit
) {
  var gameScore by remember { mutableIntStateOf(0) }

  Dialog(onDismissRequest = onDismiss) {
    Card(
      shape = RoundedCornerShape(24.dp),
      colors = CardDefaults.cardColors(containerColor = Color.White),
      elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
      modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)
        .testTag("game_interactive_dialog")
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        // Modal Header
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = game.iconEmoji, fontSize = 24.sp)
            Spacer(modifier = Modifier.width(8.dp))
            Column {
              Text(
                text = game.getTitle(selectedLang),
                style = TextStyle(
                  fontSize = 16.sp,
                  fontWeight = FontWeight.Bold,
                  color = game.primaryColor
                )
              )
              Text(
                text = game.getBadge(selectedLang),
                style = TextStyle(
                  fontSize = 10.sp,
                  fontWeight = FontWeight.SemiBold,
                  color = Color(0xFF64748B)
                )
              )
            }
          }

          IconButton(onClick = onDismiss) {
            Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        when (game.type) {
          GameType.SHAPE_MASTER_PATH -> {
            DuolingoRoadmapGameView(
              selectedLang = selectedLang,
              soundManager = soundManager,
              onComplete = {
                soundManager.playSuccessChime()
                onCoinsEarned(50)
              }
            )
          }
          GameType.LETTER_LINK -> {
            MatchThreeLetterGameView(
              selectedLang = selectedLang,
              soundManager = soundManager,
              onScoreAdded = { pts ->
                gameScore += pts
                onCoinsEarned(pts)
              }
            )
          }
          GameType.FORM_FUSER -> {
            FormFuserPuzzleGameView(
              selectedLang = selectedLang,
              soundManager = soundManager,
              onCompleteWord = {
                soundManager.playSuccessChime()
                onCoinsEarned(35)
              }
            )
          }
          GameType.DOT_MASTER -> {
            DotMasterPreviewGameView(
              selectedLang = selectedLang,
              soundManager = soundManager,
              onScoreAdded = { pts ->
                gameScore += pts
                onCoinsEarned(pts)
              }
            )
          }
          GameType.MIX_AND_MATCH -> {
            MixAndMatchCardGameView(
              selectedLang = selectedLang,
              soundManager = soundManager,
              onMatchFound = {
                soundManager.playSuccessChime()
                onCoinsEarned(30)
              }
            )
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
          onClick = onDismiss,
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Text(
            text = when (selectedLang) {
              "EN" -> "Return to Dashboard"
              "AR" -> "العودة إلى لوحة التحكم"
              else -> "ড্যাশবোর্ডে ফিরে যান"
            },
            color = RoyalEmerald
          )
        }
      }
    }
  }
}

/**
 * 1. Shape Master Path Game View (Duolingo Style Roadmap)
 */
@Composable
fun DuolingoRoadmapGameView(
  selectedLang: String,
  soundManager: SoundManager,
  onComplete: () -> Unit
) {
  var isAnswerCorrect by remember { mutableStateOf<Boolean?>(null) }
  var showHints by remember { mutableStateOf(true) }

  Column(
    modifier = Modifier.fillMaxWidth(),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    // Hint Toggle Switch
    Card(
      shape = RoundedCornerShape(12.dp),
      colors = CardDefaults.cardColors(
        containerColor = if (showHints) Color(0xFFF0FDF4) else Color(0xFFFFFBEB)
      ),
      border = BorderStroke(
        1.dp,
        if (showHints) RoyalEmerald.copy(alpha = 0.35f) else Color(0xFFF59E0B).copy(alpha = 0.4f)
      ),
      modifier = Modifier
        .fillMaxWidth()
        .clickable { showHints = !showHints }
        .testTag("modal_hint_toggle_card")
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 10.dp, vertical = 5.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.Lightbulb,
            contentDescription = null,
            tint = if (showHints) RoyalEmerald else Color(0xFFD97706),
            modifier = Modifier.size(15.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = if (showHints) {
              when (selectedLang) {
                "EN" -> "Hints: ON (Beginner Mode)"
                "AR" -> "التلميحات: مفعلة"
                else -> "ইঙ্গিত / Hints: চালু"
              }
            } else {
              when (selectedLang) {
                "EN" -> "Hints: OFF (Challenge Mode)"
                "AR" -> "التلميحات: معطلة"
                else -> "ইঙ্গিত / Hints: বন্ধ"
              }
            },
            style = TextStyle(
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = if (showHints) RoyalEmerald else Color(0xFFB45309)
            )
          )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(
            text = if (showHints) {
              when (selectedLang) { "EN" -> "ON" "AR" -> "مفعل" else -> "চালু" }
            } else {
              when (selectedLang) { "EN" -> "OFF" "AR" -> "معطل" else -> "বন্ধ" }
            },
            style = TextStyle(
              fontSize = 10.sp,
              fontWeight = FontWeight.Bold,
              color = if (showHints) RoyalEmerald else Color(0xFF64748B)
            )
          )
          Spacer(modifier = Modifier.width(4.dp))
          Switch(
            checked = showHints,
            onCheckedChange = { showHints = it },
            colors = SwitchDefaults.colors(
              checkedThumbColor = Color.White,
              checkedTrackColor = RoyalEmerald,
              uncheckedThumbColor = Color.White,
              uncheckedTrackColor = Color(0xFFCBD5E1)
            ),
            modifier = Modifier
              .scale(0.7f)
              .testTag("modal_hint_switch")
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(10.dp))

    Text(
      text = if (showHints) {
        when (selectedLang) {
          "EN" -> "Step 1: Identify the Initial Form of 'Ba' (ب)"
          "AR" -> "المرحلة الأولى: حدد شكل حرف الباء في البداية"
          else -> "ধাপ ১: 'বা' (ب) হরফের প্রারম্ভিক রূপ কোনটি?"
        }
      } else {
        when (selectedLang) {
          "EN" -> "Step 1: Identify the Target Form for 'Ba' (ب)"
          "AR" -> "المرحلة الأولى: حدد شكل حرف الباء المستهدف"
          else -> "ধাপ ১: 'বা' (ب) হরফের লক্ষ্য রূপ কোনটি?"
        }
      },
      style = TextStyle(
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF0F172A)
      ),
      textAlign = TextAlign.Center
    )

    Spacer(modifier = Modifier.height(12.dp))

    // Interactive choices
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceEvenly
    ) {
      data class ChoiceData(val glyph: String, val hintBn: String, val hintEn: String, val hintAr: String)
      val choices = listOf(
        ChoiceData("ـبـ", "(মাঝে)", "(Medial)", "(وسط)"),
        ChoiceData("بـ", "(শুরুতে)", "(Initial)", "(بداية)"),
        ChoiceData("ـب", "(শেষে)", "(Final)", "(نهاية)"),
        ChoiceData("ب", "(বিচ্ছিন্ন)", "(Isolated)", "(منفصل)")
      )

      choices.forEach { item ->
        val choice = item.glyph
        Card(
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(
            containerColor = if (choice == "بـ" && isAnswerCorrect == true) Color(0xFFDCFCE7) else Color(0xFFF8FAFC)
          ),
          border = BorderStroke(
            1.5.dp,
            if (choice == "بـ" && isAnswerCorrect == true) RoyalEmerald else Color(0xFFCBD5E1)
          ),
          modifier = Modifier
            .width(62.dp)
            .height(if (showHints) 70.dp else 56.dp)
            .clickable {
              if (choice == "بـ") {
                isAnswerCorrect = true
                soundManager.playSuccessChime()
                onComplete()
              } else {
                isAnswerCorrect = false
                soundManager.playTone()
              }
            }
            .testTag("duo_choice_$choice")
        ) {
          Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
          ) {
            Text(
              text = choice,
              fontSize = 22.sp,
              fontWeight = FontWeight.Bold,
              color = RoyalEmerald,
              fontFamily = FontFamily.Serif
            )
            // Positional hint label only visible when showHints is true!
            if (showHints) {
              Text(
                text = when (selectedLang) {
                  "EN" -> item.hintEn
                  "AR" -> item.hintAr
                  else -> item.hintBn
                },
                style = TextStyle(fontSize = 9.sp, color = Color(0xFF64748B))
              )
            }
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(12.dp))

    isAnswerCorrect?.let { correct ->
      Text(
        text = if (correct) {
          when (selectedLang) {
            "EN" -> "🎉 Perfect! 'بـ' joins from the left side! (+50 ⭐)"
            "AR" -> "🎉 ممتاز! 'بـ' يتصل بما بعده! (+٥٠ ⭐)"
            else -> "🎉 চমৎকার! 'بـ' হরফটি বাম দিকে যুক্ত হয়! (+৫০ ⭐)"
          }
        } else {
          when (selectedLang) {
            "EN" -> "Try again! Look for the joining tail to the left."
            "AR" -> "حاول مجدداً! ابحث عن الوصلة لليسار."
            else -> "আবার চেষ্টা করুন! বাম পাশের সংযোগ লক্ষ্য করুন।"
          }
        },
        style = TextStyle(
          fontSize = 12.sp,
          fontWeight = FontWeight.Bold,
          color = if (correct) RoyalEmerald else MaterialTheme.colorScheme.error
        ),
        textAlign = TextAlign.Center
      )
    }
  }
}

/**
 * 2. Letter Link Match-3 Game View
 */
@Composable
fun MatchThreeLetterGameView(
  selectedLang: String,
  soundManager: SoundManager,
  onScoreAdded: (Int) -> Unit
) {
  val letters = remember { mutableListOf("ب", "ت", "ث", "ج", "ب", "ب", "ت", "ث", "ج") }
  var comboCount by remember { mutableIntStateOf(0) }

  Column(
    modifier = Modifier.fillMaxWidth(),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Text(
      text = when (selectedLang) {
        "EN" -> "Tap matching letters to pop & link (+20 ⭐ each)"
        "AR" -> "اضغط على الحروف المتشابهة للمطابقة (+٢٠ ⭐)"
        else -> "একই হরফে ট্যাপ করে ম্যাচ করুন (+২০ ⭐ প্রতিবার)"
      },
      style = TextStyle(fontSize = 12.sp, color = Color(0xFF64748B)),
      textAlign = TextAlign.Center
    )

    Spacer(modifier = Modifier.height(10.dp))

    // 3x3 Match-3 grid
    Column(
      verticalArrangement = Arrangement.spacedBy(8.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      for (row in 0..2) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          for (col in 0..2) {
            val index = row * 3 + col
            val letter = letters[index]
            Card(
              shape = RoundedCornerShape(12.dp),
              colors = CardDefaults.cardColors(containerColor = Color(0xFFFCE7F3)),
              border = BorderStroke(1.5.dp, Color(0xFFDB2777)),
              modifier = Modifier
                .size(54.dp)
                .clickable {
                  soundManager.playSuccessChime()
                  comboCount += 1
                  onScoreAdded(20)
                }
            ) {
              Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                  text = letter,
                  fontSize = 24.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color(0xFF9D174D)
                )
              }
            }
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(10.dp))

    Text(
      text = "Score Combo: $comboCount 🔥",
      style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFFBE185D))
    )
  }
}

/**
 * 3. Form Fuser Puzzle Game View
 */
@Composable
fun FormFuserPuzzleGameView(
  selectedLang: String,
  soundManager: SoundManager,
  onCompleteWord: () -> Unit
) {
  var isFused by remember { mutableStateOf(false) }

  Column(
    modifier = Modifier.fillMaxWidth(),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Text(
      text = when (selectedLang) {
        "EN" -> "Fuse parts together: بـ + ا + ب = ?"
        "AR" -> "ادمج أجزاء الكلمة: بـ + ا + ب = ؟"
        else -> "রূপগুলো জোড়া লাগান: بـ + ا + ب = ?"
      },
      style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B)),
      textAlign = TextAlign.Center
    )

    Spacer(modifier = Modifier.height(12.dp))

    Row(
      horizontalArrangement = Arrangement.spacedBy(8.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      PuzzlePiece("بـ")
      Text("+", fontSize = 18.sp, fontWeight = FontWeight.Bold)
      PuzzlePiece("ا")
      Text("+", fontSize = 18.sp, fontWeight = FontWeight.Bold)
      PuzzlePiece("ب")
    }

    Spacer(modifier = Modifier.height(14.dp))

    if (!isFused) {
      Button(
        onClick = {
          isFused = true
          soundManager.playSuccessChime()
          onCompleteWord()
        },
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2B6CB0))
      ) {
        Text(
          text = when (selectedLang) {
            "EN" -> "Fuse Into Word 🧩"
            "AR" -> "دمج لتكوين الكلمة 🧩"
            else -> "শব্দ তৈরি করুন 🧩"
          }
        )
      }
    } else {
      Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFEBF8FF)),
        border = BorderStroke(2.dp, Color(0xFF3182CE)),
        modifier = Modifier.padding(8.dp)
      ) {
        Column(
          modifier = Modifier.padding(14.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Text(
            text = "بَابٌ",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF2B6CB0)
          )
          Text(
            text = "Meaning: Door / দরজা (+35 ⭐)",
            style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold, color = RoyalEmerald)
          )
        }
      }
    }
  }
}

@Composable
fun PuzzlePiece(letter: String) {
  Card(
    shape = RoundedCornerShape(10.dp),
    colors = CardDefaults.cardColors(containerColor = Color(0xFFE2E8F0)),
    border = BorderStroke(1.dp, Color(0xFF94A3B8)),
    modifier = Modifier.size(48.dp)
  ) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
      Text(text = letter, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
    }
  }
}

/**
 * 4. Mix & Match Memory Card Game View
 */
@Composable
fun MixAndMatchCardGameView(
  selectedLang: String,
  soundManager: SoundManager,
  onMatchFound: () -> Unit
) {
  val cardLetters = listOf("ج", "ح", "خ", "ج", "ح", "خ")
  var revealedIndex by remember { mutableIntStateOf(-1) }
  var matchesFound by remember { mutableIntStateOf(0) }

  Column(
    modifier = Modifier.fillMaxWidth(),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Text(
      text = when (selectedLang) {
        "EN" -> "Memory Flip: Find matching letter pairs!"
        "AR" -> "اقلب البطاقات واعثر على الحروف المتطابقة!"
        else -> "স্মৃতিশক্তি পরীক্ষা: দুটি একই হরফ খুঁজে বের করুন!"
      },
      style = TextStyle(fontSize = 12.sp, color = Color(0xFF64748B)),
      textAlign = TextAlign.Center
    )

    Spacer(modifier = Modifier.height(10.dp))

    // 2 rows of 3 cards
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
      for (r in 0..1) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          for (c in 0..2) {
            val idx = r * 3 + c
            val isRevealed = revealedIndex == idx || matchesFound >= 1
            Card(
              shape = RoundedCornerShape(12.dp),
              colors = CardDefaults.cardColors(
                containerColor = if (isRevealed) Color(0xFFEDE9FE) else Color(0xFF6B46C1)
              ),
              modifier = Modifier
                .size(56.dp)
                .clickable {
                  soundManager.playTone()
                  revealedIndex = idx
                  matchesFound += 1
                  onMatchFound()
                }
            ) {
              Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                  text = if (isRevealed) cardLetters[idx] else "❓",
                  fontSize = 22.sp,
                  fontWeight = FontWeight.Bold,
                  color = if (isRevealed) Color(0xFF5B21B6) else Color.White
                )
              }
            }
          }
        }
      }
    }
  }
}

/**
 * Interactive Dot Master (নুকতা মাস্টার / ডট পজিশনিং) preview mini-game.
 */
@Composable
fun DotMasterPreviewGameView(
  selectedLang: String,
  soundManager: SoundManager,
  onScoreAdded: (Int) -> Unit
) {
  data class DotQuiz(
    val promptBn: String,
    val promptEn: String,
    val promptAr: String,
    val letterGlyph: String,
    val optionsBn: List<String>,
    val optionsEn: List<String>,
    val optionsAr: List<String>,
    val correctIndex: Int
  )

  val quizzes = remember {
    listOf(
      DotQuiz(
        promptBn = "'বা' (ب) হরফের নুকতার সংখ্যা ও অবস্থান কোনটি?",
        promptEn = "How many dots and position for letter 'Ba' (ب)?",
        promptAr = "كم عدد وموضع نقاط حرف 'الباء' (ب)؟",
        letterGlyph = "ب",
        optionsBn = listOf("নিচে ১টি নুকতা", "উপরে ২টি নুকতা", "উপরে ৩টি নুকতা", "উপরে ১টি নুকতা"),
        optionsEn = listOf("1 dot below", "2 dots above", "3 dots above", "1 dot above"),
        optionsAr = listOf("نقطة واحدة بالأسفل", "نقطتان بالأعلى", "٣ نقاط بالأعلى", "نقطة واحدة بالأعلى"),
        correctIndex = 0
      ),
      DotQuiz(
        promptBn = "'তা' (ت) হরফের নুকতার সংখ্যা ও অবস্থান কোনটি?",
        promptEn = "How many dots and position for letter 'Ta' (ت)?",
        promptAr = "كم عدد وموضع نقاط حرف 'التاء' (ت)؟",
        letterGlyph = "ت",
        optionsBn = listOf("নিচে ১টি নুকতা", "উপরে ২টি নুকতা", "উপরে ৩টি নুকতা", "পেটে ১টি নুকতা"),
        optionsEn = listOf("1 dot below", "2 dots above", "3 dots above", "1 dot inside"),
        optionsAr = listOf("نقطة واحدة بالأسفل", "نقطتان بالأعلى", "٣ نقاط بالأعلى", "نقطة واحدة في الوسط"),
        correctIndex = 1
      ),
      DotQuiz(
        promptBn = "'ছা' (ث) হরফের নুকতার সংখ্যা ও অবস্থান কোনটি?",
        promptEn = "How many dots and position for letter 'Tha' (ث)?",
        promptAr = "كم عدد وموضع نقاط حرف 'الثاء' (ث)؟",
        letterGlyph = "ث",
        optionsBn = listOf("উপরে ১টি নুকতা", "উপরে ২টি নুকতা", "উপরে ৩টি নুকতা", "নিচে ২টি নুকতা"),
        optionsEn = listOf("1 dot above", "2 dots above", "3 dots above", "2 dots below"),
        optionsAr = listOf("نقطة واحدة بالأعلى", "نقطتان بالأعلى", "٣ نقاط بالأعلى", "نقطتان بالأسفل"),
        correctIndex = 2
      ),
      DotQuiz(
        promptBn = "'নূন' (ن) হরফের নুকতা কোথায় থাকে?",
        promptEn = "Where is the dot placed in 'Nun' (ن)?",
        promptAr = "أين تقع نقطة حرف 'النون' (ن)؟",
        letterGlyph = "ن",
        optionsBn = listOf("উপরে ১টি নুকতা", "নিচে ১টি নুকতা", "পেটে ২টি নুকতা", "কোনো নুকতা নেই"),
        optionsEn = listOf("1 dot above", "1 dot below", "2 dots inside", "No dots"),
        optionsAr = listOf("نقطة واحدة بالأعلى", "نقطة واحدة بالأسفل", "نقطتان في الوسط", "بدون نقاط"),
        correctIndex = 0
      )
    )
  }

  var currentQuizIdx by remember { mutableIntStateOf(0) }
  var selectedChoice by remember { mutableIntStateOf(-1) }
  var feedbackCorrect by remember { mutableStateOf<Boolean?>(null) }
  val activeQuiz = quizzes[currentQuizIdx % quizzes.size]

  Card(
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF7ED)),
    border = BorderStroke(1.5.dp, Color(0xFFEA580C).copy(alpha = 0.4f)),
    modifier = Modifier.fillMaxWidth().testTag("dot_master_mini_game")
  ) {
    Column(
      modifier = Modifier.fillMaxWidth().padding(16.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Text(
        text = when (selectedLang) {
          "EN" -> "Dot Master Mini Quiz 🎯"
          "AR" -> "تحدي إتقان النقاط 🎯"
          else -> "নুকতা মাস্টার কুইজ 🎯"
        },
        style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color(0xFFC2410C))
      )
      Spacer(modifier = Modifier.height(8.dp))

      // Arabic letter display
      Surface(
        shape = CircleShape,
        color = Color.White,
        shadowElevation = 3.dp,
        border = BorderStroke(1.dp, Color(0xFFFDBA74)),
        modifier = Modifier.size(72.dp)
      ) {
        Box(contentAlignment = Alignment.Center) {
          Text(
            text = activeQuiz.letterGlyph,
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFEA580C),
            fontFamily = FontFamily.Serif
          )
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      Text(
        text = when (selectedLang) {
          "EN" -> activeQuiz.promptEn
          "AR" -> activeQuiz.promptAr
          else -> activeQuiz.promptBn
        },
        style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF7C2D12)),
        textAlign = TextAlign.Center
      )

      Spacer(modifier = Modifier.height(12.dp))

      val options = when (selectedLang) {
        "EN" -> activeQuiz.optionsEn
        "AR" -> activeQuiz.optionsAr
        else -> activeQuiz.optionsBn
      }

      options.forEachIndexed { index, optionText ->
        val isSelected = selectedChoice == index
        val isCorrect = activeQuiz.correctIndex == index
        val buttonBg = when {
          isSelected && feedbackCorrect == true -> Color(0xFFDCFCE7)
          isSelected && feedbackCorrect == false -> Color(0xFFFEE2E2)
          else -> Color.White
        }
        val borderCol = when {
          isSelected && feedbackCorrect == true -> Color(0xFF16A34A)
          isSelected && feedbackCorrect == false -> Color(0xFFDC2626)
          else -> Color(0xFFFED7AA)
        }

        Card(
          shape = RoundedCornerShape(10.dp),
          colors = CardDefaults.cardColors(containerColor = buttonBg),
          border = BorderStroke(1.dp, borderCol),
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable {
              selectedChoice = index
              if (index == activeQuiz.correctIndex) {
                feedbackCorrect = true
                soundManager.playSuccessChime()
                onScoreAdded(25)
              } else {
                feedbackCorrect = false
                soundManager.playErrorBuzzer()
              }
            }
        ) {
          Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "${index + 1}.",
              style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFFEA580C))
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = optionText,
              style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Medium, color = Color(0xFF431407))
            )
          }
        }
      }

      if (feedbackCorrect != null) {
        Spacer(modifier = Modifier.height(10.dp))
        Button(
          onClick = {
            currentQuizIdx = (currentQuizIdx + 1) % quizzes.size
            selectedChoice = -1
            feedbackCorrect = null
          },
          colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEA580C)),
          shape = RoundedCornerShape(10.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Text(
            text = when (selectedLang) {
              "EN" -> "Next Question ➔"
              "AR" -> "السؤال التالي ➔"
              else -> "পরবর্তী প্রশ্ন ➔"
            },
            color = Color.White,
            fontWeight = FontWeight.Bold
          )
        }
      }
    }
  }
}

@Composable
fun SectionHeader(
  title: String,
  subtitle: String
) {
  Column(modifier = Modifier.fillMaxWidth()) {
    Text(
      text = title,
      style = TextStyle(
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        color = RoyalEmerald
      )
    )
    Text(
      text = subtitle,
      style = TextStyle(
        fontSize = 12.sp,
        color = Color(0xFF64748B)
      )
    )
  }
}
