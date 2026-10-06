package com.example

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import com.example.ui.theme.RoyalEmerald
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Full-Screen Level Selection Roadmap View:
 * - Levels 1 through 20 arranged in an interactive roadmap/grid
 * - Progressive Unlock Logic: Level 1 is unlocked by default. Subsequent levels unlock sequentially upon earning >= 1 star in previous level.
 * - Level Card Components: Level number, locked/unlocked indicator, best star rating earned (0 to 3 Stars)
 * - Transitions to FullScreenGameCanvas when an unlocked level is tapped
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FullScreenGameRoadmap(
  gameMode: GameMode,
  selectedLang: String,
  soundManager: SoundManager,
  onExit: () -> Unit
) {
  val context = LocalContext.current
  val progressRepo = remember { OfflineProgressRepository.getInstance(context) }
  var selectedLevelForPlay by remember { mutableStateOf<Int?>(null) }

  // If a level is active, display the FullScreenGameCanvas
  selectedLevelForPlay?.let { levelNum ->
    FullScreenGameCanvas(
      gameMode = gameMode,
      initialLevelNumber = levelNum,
      selectedLang = selectedLang,
      soundManager = soundManager,
      onExit = { selectedLevelForPlay = null }
    )
    return
  }

  val totalStars = progressRepo.getGameTotalStars(gameMode.type)
  val userCoins = progressRepo.userCoins

  Scaffold(
    containerColor = Color(0xFFF8FAFC),
    topBar = {
      TopAppBar(
        title = {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(modifier = Modifier.weight(1f)) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = gameMode.iconEmoji, fontSize = 20.sp)
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = gameMode.getTitle(selectedLang),
                  style = TextStyle(
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                  ),
                  maxLines = 1,
                  overflow = TextOverflow.Ellipsis
                )
              }
              Text(
                text = when (selectedLang) {
                  "BN" -> "২০টি লেভেলের চ্যালেঞ্জ রোডম্যাপ"
                  "AR" -> "مسار التحديات عبر ٢٠ مستوى"
                  else -> "20-Level Challenge Roadmap"
                },
                style = TextStyle(
                  fontSize = 11.5.sp,
                  color = Color.White.copy(alpha = 0.85f)
                )
              )
            }

            // Total Stars & Coins Counter
            Surface(
              shape = RoundedCornerShape(12.dp),
              color = Color.Black.copy(alpha = 0.25f),
              border = BorderStroke(1.dp, Color.White.copy(alpha = 0.35f))
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
              ) {
                Icon(
                  imageVector = Icons.Default.Star,
                  contentDescription = "Total Stars",
                  tint = Color(0xFFFDE68A),
                  modifier = Modifier.size(15.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                  text = when (selectedLang) {
                    "BN" -> "${toBnDigits(totalStars)} / ৬০"
                    "AR" -> "${toArDigits(totalStars)} / ٦٠"
                    else -> "$totalStars / 60"
                  },
                  style = TextStyle(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
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
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(padding)
        .testTag("game_roadmap_screen"),
      contentPadding = PaddingValues(16.dp),
      verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
      // Hero Header Banner
      item {
        Card(
          shape = RoundedCornerShape(20.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
          border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .background(
                Brush.horizontalGradient(
                  listOf(
                    gameMode.primaryColor.copy(alpha = 0.08f),
                    gameMode.secondaryColor.copy(alpha = 0.05f)
                  )
                )
              )
              .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(text = gameMode.iconEmoji, fontSize = 42.sp)
            Spacer(modifier = Modifier.width(14.dp))
            Column {
              Text(
                text = gameMode.getTitle(selectedLang),
                style = TextStyle(
                  fontSize = 18.sp,
                  fontWeight = FontWeight.Bold,
                  color = gameMode.secondaryColor
                )
              )
              Spacer(modifier = Modifier.height(2.dp))
              Text(
                text = gameMode.getSubtitle(selectedLang),
                style = TextStyle(
                  fontSize = 12.5.sp,
                  color = Color(0xFF475569)
                )
              )
            }
          }
        }
      }

      if (gameMode.type == GameType.SHAPE_MASTER_PATH) {
        // Render 4-tier dedicated curriculum for Shape Master Path
        ShapeMasterCurriculum.allTiers.forEach { smTier ->
          item(key = "sm_tier_${smTier.tierNumber}") {
            TierRoadmapSection(
              title = when (selectedLang) {
                "EN" -> smTier.titleEn
                "AR" -> smTier.titleAr
                else -> smTier.titleBn
              },
              description = when (selectedLang) {
                "EN" -> smTier.descriptionEn
                "AR" -> smTier.descriptionAr
                else -> smTier.descriptionBn
              },
              badgeColor = smTier.badgeColor,
              iconEmoji = smTier.iconEmoji,
              levels = smTier.levelRange.toList(),
              gameMode = gameMode,
              selectedLang = selectedLang,
              progressRepo = progressRepo,
              onSelectLevel = { selectedLevelForPlay = it }
            )
          }
        }
      } else if (gameMode.type == GameType.DOT_MASTER) {
        // Render 4-tier dedicated curriculum for Nukta Master
        NuktaMasterCurriculum.allTiers.forEach { nmTier ->
          item(key = "nm_tier_${nmTier.tierNumber}") {
            TierRoadmapSection(
              title = when (selectedLang) {
                "EN" -> nmTier.titleEn
                "AR" -> nmTier.titleAr
                else -> nmTier.titleBn
              },
              description = when (selectedLang) {
                "EN" -> nmTier.descriptionEn
                "AR" -> nmTier.descriptionAr
                else -> nmTier.descriptionBn
              },
              badgeColor = nmTier.badgeColor,
              iconEmoji = nmTier.iconEmoji,
              levels = nmTier.levelRange.toList(),
              gameMode = gameMode,
              selectedLang = selectedLang,
              progressRepo = progressRepo,
              onSelectLevel = { selectedLevelForPlay = it }
            )
          }
        }
      } else if (gameMode.type == GameType.LETTER_LINK) {
        // Render 4-tier dedicated curriculum for Letter Link (Match Grid)
        LetterLinkCurriculum.allTiers.forEach { llTier ->
          item(key = "ll_tier_${llTier.tierNumber}") {
            TierRoadmapSection(
              title = when (selectedLang) {
                "EN" -> llTier.titleEn
                "AR" -> llTier.titleAr
                else -> llTier.titleBn
              },
              description = when (selectedLang) {
                "EN" -> llTier.descriptionEn
                "AR" -> llTier.descriptionAr
                else -> llTier.descriptionBn
              },
              badgeColor = llTier.badgeColor,
              iconEmoji = llTier.iconEmoji,
              levels = llTier.levelRange.toList(),
              gameMode = gameMode,
              selectedLang = selectedLang,
              progressRepo = progressRepo,
              onSelectLevel = { selectedLevelForPlay = it }
            )
          }
        }
      } else if (gameMode.type == GameType.FORM_FUSER) {
        // Render 4-tier dedicated curriculum for Form Fuser (শব্দ গঠনের পাজল)
        FormFuserCurriculum.allTiers.forEach { ffTier ->
          item(key = "ff_tier_${ffTier.tierNumber}") {
            TierRoadmapSection(
              title = when (selectedLang) {
                "EN" -> ffTier.titleEn
                "AR" -> ffTier.titleAr
                else -> ffTier.titleBn
              },
              description = when (selectedLang) {
                "EN" -> ffTier.descriptionEn
                "AR" -> ffTier.descriptionAr
                else -> ffTier.descriptionBn
              },
              badgeColor = ffTier.badgeColor,
              iconEmoji = ffTier.iconEmoji,
              levels = ffTier.levelRange.toList(),
              gameMode = gameMode,
              selectedLang = selectedLang,
              progressRepo = progressRepo,
              onSelectLevel = { selectedLevelForPlay = it }
            )
          }
        }
      } else if (gameMode.type == GameType.MIX_AND_MATCH) {
        // Render 4-tier dedicated curriculum for Mix & Match (মিক্স অ্যান্ড ম্যাচ - মেমোরি কার্ড ফ্লিপ)
        MixAndMatchCurriculum.allTiers.forEach { mmTier ->
          item(key = "mm_tier_${mmTier.tierNumber}") {
            TierRoadmapSection(
              title = when (selectedLang) {
                "EN" -> mmTier.titleEn
                "AR" -> mmTier.titleAr
                else -> mmTier.titleBn
              },
              description = when (selectedLang) {
                "EN" -> mmTier.descriptionEn
                "AR" -> mmTier.descriptionAr
                else -> mmTier.descriptionBn
              },
              badgeColor = mmTier.badgeColor,
              iconEmoji = mmTier.iconEmoji,
              levels = mmTier.levelRange.toList(),
              gameMode = gameMode,
              selectedLang = selectedLang,
              progressRepo = progressRepo,
              onSelectLevel = { selectedLevelForPlay = it }
            )
          }
        }
      } else {
        // TIER 1: Levels 1..5 (Foundation)
        item {
          TierRoadmapSection(
            title = when (selectedLang) { "EN" -> GameDifficultyTier.FOUNDATION.titleEn; "AR" -> GameDifficultyTier.FOUNDATION.titleAr; else -> GameDifficultyTier.FOUNDATION.titleBn },
            description = when (selectedLang) { "EN" -> GameDifficultyTier.FOUNDATION.descriptionEn; "AR" -> GameDifficultyTier.FOUNDATION.descriptionAr; else -> GameDifficultyTier.FOUNDATION.descriptionBn },
            badgeColor = GameDifficultyTier.FOUNDATION.badgeColor,
            iconEmoji = GameDifficultyTier.FOUNDATION.iconEmoji,
            levels = (1..5).toList(),
            gameMode = gameMode,
            selectedLang = selectedLang,
            progressRepo = progressRepo,
            onSelectLevel = { selectedLevelForPlay = it }
          )
        }

        // TIER 2: Levels 6..10 (Connected Forms & Dots)
        item {
          TierRoadmapSection(
            title = when (selectedLang) { "EN" -> GameDifficultyTier.CONNECTED_FORMS.titleEn; "AR" -> GameDifficultyTier.CONNECTED_FORMS.titleAr; else -> GameDifficultyTier.CONNECTED_FORMS.titleBn },
            description = when (selectedLang) { "EN" -> GameDifficultyTier.CONNECTED_FORMS.descriptionEn; "AR" -> GameDifficultyTier.CONNECTED_FORMS.descriptionAr; else -> GameDifficultyTier.CONNECTED_FORMS.descriptionBn },
            badgeColor = GameDifficultyTier.CONNECTED_FORMS.badgeColor,
            iconEmoji = GameDifficultyTier.CONNECTED_FORMS.iconEmoji,
            levels = (6..10).toList(),
            gameMode = gameMode,
            selectedLang = selectedLang,
            progressRepo = progressRepo,
            onSelectLevel = { selectedLevelForPlay = it }
          )
        }

        // TIER 3: Levels 11..20 (Advanced Mastery & Speed)
        item {
          TierRoadmapSection(
            title = when (selectedLang) { "EN" -> GameDifficultyTier.ADVANCED_MASTERY.titleEn; "AR" -> GameDifficultyTier.ADVANCED_MASTERY.titleAr; else -> GameDifficultyTier.ADVANCED_MASTERY.titleBn },
            description = when (selectedLang) { "EN" -> GameDifficultyTier.ADVANCED_MASTERY.descriptionEn; "AR" -> GameDifficultyTier.ADVANCED_MASTERY.descriptionAr; else -> GameDifficultyTier.ADVANCED_MASTERY.descriptionBn },
            badgeColor = GameDifficultyTier.ADVANCED_MASTERY.badgeColor,
            iconEmoji = GameDifficultyTier.ADVANCED_MASTERY.iconEmoji,
            levels = (11..20).toList(),
            gameMode = gameMode,
            selectedLang = selectedLang,
            progressRepo = progressRepo,
            onSelectLevel = { selectedLevelForPlay = it }
          )
        }
      }
    }
  }
}

@Composable
private fun TierRoadmapSection(
  title: String,
  description: String,
  badgeColor: Color,
  iconEmoji: String,
  levels: List<Int>,
  gameMode: GameMode,
  selectedLang: String,
  progressRepo: OfflineProgressRepository,
  onSelectLevel: (Int) -> Unit
) {
  Column(
    modifier = Modifier.fillMaxWidth(),
    verticalArrangement = Arrangement.spacedBy(10.dp)
  ) {
    // Tier Header Banner
    Surface(
      shape = RoundedCornerShape(14.dp),
      color = badgeColor.copy(alpha = 0.1f),
      border = BorderStroke(1.dp, badgeColor.copy(alpha = 0.3f)),
      modifier = Modifier.fillMaxWidth()
    ) {
      Row(
        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(text = iconEmoji, fontSize = 20.sp)
        Spacer(modifier = Modifier.width(8.dp))
        Column {
          Text(
            text = title,
            style = TextStyle(
              fontSize = 13.5.sp,
              fontWeight = FontWeight.Bold,
              color = badgeColor
            )
          )
          Text(
            text = description,
            style = TextStyle(
              fontSize = 11.sp,
              color = Color(0xFF64748B)
            )
          )
        }
      }
    }

    // Grid of Levels in this tier
    val chunkedLevels = levels.chunked(2)
    chunkedLevels.forEach { rowLevels ->
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        rowLevels.forEach { levelNum ->
          val isUnlocked = progressRepo.isGameLevelUnlocked(gameMode.type, levelNum)
          val starsEarned = progressRepo.getGameLevelStars(gameMode.type, levelNum)

          LevelCard(
            levelNumber = levelNum,
            isUnlocked = isUnlocked,
            starsEarned = starsEarned,
            tierColor = badgeColor,
            gameType = gameMode.type,
            selectedLang = selectedLang,
            onClick = {
              if (isUnlocked) {
                onSelectLevel(levelNum)
              }
            },
            modifier = Modifier.weight(1f)
          )
        }
        if (rowLevels.size == 1) {
          Spacer(modifier = Modifier.weight(1f))
        }
      }
    }
  }
}

@Composable
private fun LevelCard(
  levelNumber: Int,
  isUnlocked: Boolean,
  starsEarned: Int,
  tierColor: Color,
  gameType: GameType,
  selectedLang: String,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  val cardBg = if (isUnlocked) Color.White else Color(0xFFF1F5F9)
  val borderColor = if (isUnlocked) {
    if (starsEarned >= 1) tierColor.copy(alpha = 0.6f) else Color(0xFFCBD5E1)
  } else {
    Color(0xFFE2E8F0)
  }

  Card(
    shape = RoundedCornerShape(18.dp),
    colors = CardDefaults.cardColors(containerColor = cardBg),
    elevation = CardDefaults.cardElevation(defaultElevation = if (isUnlocked) 3.dp else 0.dp),
    border = BorderStroke(1.2.dp, borderColor),
    modifier = modifier
      .clickable(enabled = isUnlocked, onClick = onClick)
      .testTag("level_card_$levelNumber")
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(14.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        // Level number badge
        Surface(
          shape = RoundedCornerShape(8.dp),
          color = if (isUnlocked) tierColor.copy(alpha = 0.15f) else Color(0xFFE2E8F0),
          border = BorderStroke(0.8.dp, if (isUnlocked) tierColor.copy(alpha = 0.4f) else Color(0xFFCBD5E1))
        ) {
          Text(
            text = when (selectedLang) {
              "BN" -> "লেভেল ${toBnDigits(levelNumber)}"
              "AR" -> "مستوى ${toArDigits(levelNumber)}"
              else -> "Level $levelNumber"
            },
            style = TextStyle(
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = if (isUnlocked) tierColor else Color(0xFF94A3B8)
            ),
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
          )
        }

        // Lock / Play indicator
        if (isUnlocked) {
          Surface(
            shape = CircleShape,
            color = RoyalEmerald.copy(alpha = 0.15f),
            modifier = Modifier.size(26.dp)
          ) {
            Box(contentAlignment = Alignment.Center) {
              Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = "Play",
                tint = RoyalEmerald,
                modifier = Modifier.size(16.dp)
              )
            }
          }
        } else {
          Icon(
            imageVector = Icons.Default.Lock,
            contentDescription = "Locked",
            tint = Color(0xFF94A3B8),
            modifier = Modifier.size(18.dp)
          )
        }
      }

      if (gameType == GameType.SHAPE_MASTER_PATH) {
        val smLevelInfo = ShapeMasterCurriculum.getLevelInfo(levelNumber)
        Spacer(modifier = Modifier.height(6.dp))
        Text(
          text = when (selectedLang) {
            "EN" -> smLevelInfo.nameEn
            "AR" -> smLevelInfo.nameAr
            else -> smLevelInfo.nameBn
          },
          style = TextStyle(
            fontSize = 12.5.sp,
            fontWeight = FontWeight.Bold,
            color = if (isUnlocked) Color(0xFF1E293B) else Color(0xFF94A3B8),
            textAlign = TextAlign.Center
          ),
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )
        Text(
          text = smLevelInfo.lettersSubtitle,
          style = TextStyle(
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = if (isUnlocked) tierColor else Color(0xFF94A3B8),
            textAlign = TextAlign.Center
          ),
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )
      } else if (gameType == GameType.DOT_MASTER) {
        val nmLevelInfo = NuktaMasterCurriculum.getLevelInfo(levelNumber)
        Spacer(modifier = Modifier.height(6.dp))
        Text(
          text = when (selectedLang) {
            "EN" -> nmLevelInfo.nameEn
            "AR" -> nmLevelInfo.nameAr
            else -> nmLevelInfo.nameBn
          },
          style = TextStyle(
            fontSize = 12.5.sp,
            fontWeight = FontWeight.Bold,
            color = if (isUnlocked) Color(0xFF1E293B) else Color(0xFF94A3B8),
            textAlign = TextAlign.Center
          ),
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )
        Text(
          text = nmLevelInfo.lettersSubtitle,
          style = TextStyle(
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = if (isUnlocked) tierColor else Color(0xFF94A3B8),
            textAlign = TextAlign.Center
          ),
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )
      } else if (gameType == GameType.LETTER_LINK) {
        val llLevelInfo = LetterLinkCurriculum.getLevelInfo(levelNumber)
        Spacer(modifier = Modifier.height(6.dp))
        Text(
          text = when (selectedLang) {
            "EN" -> llLevelInfo.nameEn
            "AR" -> llLevelInfo.nameAr
            else -> llLevelInfo.nameBn
          },
          style = TextStyle(
            fontSize = 12.5.sp,
            fontWeight = FontWeight.Bold,
            color = if (isUnlocked) Color(0xFF1E293B) else Color(0xFF94A3B8),
            textAlign = TextAlign.Center
          ),
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )
        Text(
          text = llLevelInfo.lettersSubtitle,
          style = TextStyle(
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = if (isUnlocked) tierColor else Color(0xFF94A3B8),
            textAlign = TextAlign.Center
          ),
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )
      } else if (gameType == GameType.FORM_FUSER) {
        val ffLevelInfo = FormFuserCurriculum.getLevelInfo(levelNumber)
        Spacer(modifier = Modifier.height(6.dp))
        Text(
          text = when (selectedLang) {
            "EN" -> ffLevelInfo.nameEn
            "AR" -> ffLevelInfo.nameAr
            else -> ffLevelInfo.nameBn
          },
          style = TextStyle(
            fontSize = 12.5.sp,
            fontWeight = FontWeight.Bold,
            color = if (isUnlocked) Color(0xFF1E293B) else Color(0xFF94A3B8),
            textAlign = TextAlign.Center
          ),
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )
        Text(
          text = ffLevelInfo.lettersSubtitle,
          style = TextStyle(
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = if (isUnlocked) tierColor else Color(0xFF94A3B8),
            textAlign = TextAlign.Center
          ),
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )
      } else if (gameType == GameType.MIX_AND_MATCH) {
        val mmLevelInfo = MixAndMatchCurriculum.getLevelInfo(levelNumber)
        Spacer(modifier = Modifier.height(6.dp))
        Text(
          text = when (selectedLang) {
            "EN" -> mmLevelInfo.nameEn
            "AR" -> mmLevelInfo.nameAr
            else -> mmLevelInfo.nameBn
          },
          style = TextStyle(
            fontSize = 12.5.sp,
            fontWeight = FontWeight.Bold,
            color = if (isUnlocked) Color(0xFF1E293B) else Color(0xFF94A3B8),
            textAlign = TextAlign.Center
          ),
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )
        Text(
          text = mmLevelInfo.cardsSubtitle,
          style = TextStyle(
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = if (isUnlocked) tierColor else Color(0xFF94A3B8),
            textAlign = TextAlign.Center
          ),
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )
      }

      Spacer(modifier = Modifier.height(8.dp))

      // 3-Star Rating Display
      Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
      ) {
        for (i in 1..3) {
          val isFilled = i <= starsEarned
          Icon(
            imageVector = Icons.Default.Star,
            contentDescription = null,
            tint = if (isFilled) Color(0xFFF59E0B) else Color(0xFFCBD5E1),
            modifier = Modifier.size(20.dp).padding(horizontal = 1.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(6.dp))

      // Status text
      Text(
        text = if (isUnlocked) {
          if (starsEarned >= 1) {
            when (selectedLang) {
              "BN" -> "${toBnDigits(starsEarned)} তারকা অর্জিত"
              "AR" -> "${toArDigits(starsEarned)} نجوم محققة"
              else -> "$starsEarned Stars Earned"
            }
          } else {
            when (selectedLang) {
              "BN" -> "খেলতে ট্যাপ করুন"
              "AR" -> "اضغط للبدء"
              else -> "Tap to Play"
            }
          }
        } else {
          when (selectedLang) {
            "BN" -> "১ তারকা পেয়ে আনলক করুন"
            "AR" -> "يلزم نجمة للفتح"
            else -> "Earn 1★ to unlock"
          }
        },
        style = TextStyle(
          fontSize = 10.5.sp,
          fontWeight = if (isUnlocked) FontWeight.SemiBold else FontWeight.Normal,
          color = if (isUnlocked) (if (starsEarned >= 1) Color(0xFFD97706) else RoyalEmerald) else Color(0xFF94A3B8),
          textAlign = TextAlign.Center
        ),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
      )
    }
  }
}
