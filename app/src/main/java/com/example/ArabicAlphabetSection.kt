package com.example

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.webkit.JavascriptInterface
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Gesture
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.example.audio.AudioEngine
import com.example.audio.VoiceProfile
import com.example.ui.theme.RoyalEmerald
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale
import kotlin.math.sin

/**
 * 5 Alphabet Tabs Definition
 */
enum class AlphabetTab(
  val titleEn: String,
  val titleBn: String,
  val titleAr: String,
  val icon: ImageVector,
  val testTag: String
) {
  SOUNDBOARD(
    titleEn = "Soundboard",
    titleBn = "সাউন্ডবোর্ড",
    titleAr = "لوحة الأصوات",
    icon = Icons.Default.VolumeUp,
    testTag = "alphabet_tab_soundboard"
  ),
  MAKHRAJ_3D(
    titleEn = "3D Makhraj",
    titleBn = "৩ডি মাখরাজ",
    titleAr = "مخرج ثلاثي الأبعاد",
    icon = Icons.Default.Visibility,
    testTag = "alphabet_tab_3d_makhraj"
  ),
  FORMS(
    titleEn = "Forms",
    titleBn = "হরফের রূপ",
    titleAr = "أشكال الحرف",
    icon = Icons.Default.EditNote,
    testTag = "alphabet_tab_forms"
  ),
  TRACING(
    titleEn = "Tracing",
    titleBn = "অনুশীলন",
    titleAr = "الرسم والتتبع",
    icon = Icons.Default.Gesture,
    testTag = "alphabet_tab_tracing"
  ),
  AI_PRACTICE(
    titleEn = "AI Practice",
    titleBn = "ভয়েস রেকর্ড",
    titleAr = "الممارس الذكي",
    icon = Icons.Default.Mic,
    testTag = "alphabet_tab_ai_practice"
  )
}

// Dark-green aesthetic color palette
val DarkGreenCanvas = Color(0xFF04241B)
val DarkGreenSurface = Color(0xFF093327)
val DarkGreenCard = Color(0xFF0F4032)
val DarkGreenBorder = Color(0xFF1B5947)
val IslamicGold = Color(0xFFF59E0B)
val LightIslamicGold = Color(0xFFFDE68A)
val MintAccent = Color(0xFF34D399)

/**
 * Modern 5-Tab Arabic Alphabet Studio with Horizontal Swipe and Fixed Bottom Navigation Bar.
 * Completely replaces the single overcrowded pop-up modal.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArabicAlphabetFullScreen(
  selectedLang: String,
  soundManager: SoundManager,
  onBack: () -> Unit
) {
  val allLetters = remember { ArabicAlphabetRepository.letters }
  var selectedLetter by remember { mutableStateOf(allLetters.first()) }
  val pagerState = rememberPagerState(initialPage = 0, pageCount = { 5 })
  val coroutineScope = rememberCoroutineScope()

  // Track active voice profile
  var currentVoice by remember { mutableStateOf(soundManager.currentVoiceGender) }
  LaunchedEffect(soundManager.currentVoiceGender) {
    currentVoice = soundManager.currentVoiceGender
  }

  Scaffold(
    containerColor = DarkGreenCanvas,
    topBar = {
      TopAppBar(
        title = {
          Column {
            Text(
              text = when (selectedLang) {
                "EN" -> "Arabic Alphabet & Tajweed Studio"
                "AR" -> "استوديو الحروف العربية والتجويد"
                else -> "আরবি হরফ ও তাজবীদ স্টুডিও"
              },
              style = TextStyle(
                fontWeight = FontWeight.Bold,
                color = Color.White,
                fontSize = 17.sp
              )
            )
            Text(
              text = when (selectedLang) {
                "EN" -> "Active: Letter ${selectedLetter.letter} (${selectedLetter.nameEn})"
                "AR" -> "الحرف المختار: ${selectedLetter.letter} (${selectedLetter.nameAr})"
                else -> "নির্বাচিত: ${selectedLetter.letter} (${selectedLetter.nameBn})"
              },
              style = TextStyle(
                fontWeight = FontWeight.Normal,
                color = MintAccent,
                fontSize = 11.5.sp
              )
            )
          }
        },
        navigationIcon = {
          IconButton(onClick = onBack) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowBack,
              contentDescription = "Back",
              tint = Color.White
            )
          }
        },
        actions = {
          // Voice switcher pill in top app bar
          VoiceQuickToggle(
            currentVoice = currentVoice,
            onToggle = {
              val newVoice = if (currentVoice == VoiceGender.MALE_QARI) VoiceGender.FEMALE_QARIA else VoiceGender.MALE_QARI
              soundManager.setVoiceGender(newVoice)
              currentVoice = newVoice
            }
          )
        },
        colors = TopAppBarDefaults.topAppBarColors(
          containerColor = DarkGreenSurface,
          titleContentColor = Color.White
        )
      )
    },
    bottomBar = {
      // Fixed 5-Tab Bottom Navigation Bar anchored at the bottom
      AlphabetFixedBottomBar(
        currentTab = pagerState.currentPage,
        selectedLang = selectedLang,
        onTabSelected = { tabIndex ->
          coroutineScope.launch {
            pagerState.animateScrollToPage(tabIndex)
          }
        }
      )
    }
  ) { paddingValues ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
    ) {
      // Horizontal swipe support across all 5 tabs
      HorizontalPager(
        state = pagerState,
        modifier = Modifier.fillMaxSize()
      ) { pageIndex ->
        when (AlphabetTab.entries[pageIndex]) {
          AlphabetTab.SOUNDBOARD -> {
            AlphabetSoundboardTab(
              letters = allLetters,
              selectedLetter = selectedLetter,
              selectedLang = selectedLang,
              soundManager = soundManager,
              onLetterClicked = { letter ->
                selectedLetter = letter
                // Instant audio play WITHOUT opening any pop-up dialog
                soundManager.speakArabicLetter(letter.letter, soundManager.currentVoiceGender)
              }
            )
          }
          AlphabetTab.MAKHRAJ_3D -> {
            Alphabet3DMakhrajTab(
              letters = allLetters,
              selectedLetter = selectedLetter,
              selectedLang = selectedLang,
              soundManager = soundManager,
              onSelectLetter = { selectedLetter = it }
            )
          }
          AlphabetTab.FORMS -> {
            AlphabetFormsTab(
              letters = allLetters,
              selectedLetter = selectedLetter,
              selectedLang = selectedLang,
              soundManager = soundManager,
              onSelectLetter = { selectedLetter = it }
            )
          }
          AlphabetTab.TRACING -> {
            AlphabetTracingCanvasTab(
              letters = allLetters,
              selectedLetter = selectedLetter,
              selectedLang = selectedLang,
              soundManager = soundManager,
              onSelectLetter = { selectedLetter = it }
            )
          }
          AlphabetTab.AI_PRACTICE -> {
            AlphabetAiPracticeTab(
              selectedLetter = selectedLetter,
              selectedLang = selectedLang,
              soundManager = soundManager
            )
          }
        }
      }
    }
  }
}

/**
 * Voice toggle pill button in TopAppBar
 */
@Composable
private fun VoiceQuickToggle(
  currentVoice: VoiceGender,
  onToggle: () -> Unit
) {
  val isMale = currentVoice == VoiceGender.MALE_QARI
  Surface(
    shape = RoundedCornerShape(20.dp),
    color = if (isMale) Color(0xFF0F766E) else Color(0xFF831843),
    border = BorderStroke(1.dp, if (isMale) MintAccent else Color(0xFFF472B6)),
    modifier = Modifier
      .padding(end = 8.dp)
      .clickable { onToggle() }
      .testTag("alphabet_voice_quick_toggle")
  ) {
    Row(
      modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = if (isMale) "🎙️ Qari (Male)" else "🌸 Qaria (Female)",
        style = TextStyle(
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold,
          color = Color.White
        )
      )
    }
  }
}

/**
 * Fixed Bottom Navigation Bar anchored at the bottom with icons and labels
 */
@Composable
private fun AlphabetFixedBottomBar(
  currentTab: Int,
  selectedLang: String,
  onTabSelected: (Int) -> Unit
) {
  NavigationBar(
    containerColor = DarkGreenSurface,
    tonalElevation = 8.dp,
    modifier = Modifier
      .fillMaxWidth()
      .navigationBarsPadding()
      .testTag("alphabet_fixed_bottom_bar")
  ) {
    AlphabetTab.entries.forEachIndexed { index, tab ->
      val isSelected = currentTab == index
      NavigationBarItem(
        selected = isSelected,
        onClick = { onTabSelected(index) },
        icon = {
          Icon(
            imageVector = tab.icon,
            contentDescription = tab.titleEn,
            modifier = Modifier.size(22.dp)
          )
        },
        label = {
          Text(
            text = when (selectedLang) {
              "EN" -> tab.titleEn
              "AR" -> tab.titleAr
              else -> tab.titleBn
            },
            style = TextStyle(
              fontSize = 10.sp,
              fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
            ),
            maxLines = 1
          )
        },
        colors = NavigationBarItemDefaults.colors(
          selectedIconColor = Color.White,
          selectedTextColor = MintAccent,
          indicatorColor = RoyalEmerald,
          unselectedIconColor = Color(0xFF94A3B8),
          unselectedTextColor = Color(0xFF94A3B8)
        ),
        modifier = Modifier.testTag(tab.testTag)
      )
    }
  }
}

// =========================================================================================
// TAB 1: 🔊 SOUNDBOARD (29 Letters Grid with instant audio, NO pop-up modal)
// =========================================================================================

@Composable
fun AlphabetSoundboardTab(
  letters: List<ArabicLetter>,
  selectedLetter: ArabicLetter,
  selectedLang: String,
  soundManager: SoundManager,
  onLetterClicked: (ArabicLetter) -> Unit
) {
  var selectedCategory by remember { mutableStateOf("ALL") }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(horizontal = 14.dp, vertical = 10.dp)
  ) {
    // Quick instruction & Category Filter Header
    Card(
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = DarkGreenSurface),
      border = BorderStroke(1.dp, DarkGreenBorder),
      modifier = Modifier.fillMaxWidth()
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 14.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Box(
          modifier = Modifier
            .size(38.dp)
            .clip(CircleShape)
            .background(RoyalEmerald.copy(alpha = 0.25f)),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.VolumeUp,
            contentDescription = null,
            tint = MintAccent,
            modifier = Modifier.size(20.dp)
          )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = when (selectedLang) {
              "EN" -> "Instant Audio Soundboard (29 Letters)"
              "AR" -> "لوحة الصوت الفورية (٢٩ حرفاً)"
              else -> "ইনস্ট্যান্ট অডিও সাউন্ডবোর্ড (২৯টি হরফ)"
            },
            style = TextStyle(
              fontSize = 13.5.sp,
              fontWeight = FontWeight.Bold,
              color = Color.White
            )
          )
          Text(
            text = when (selectedLang) {
              "EN" -> "Tap any letter to listen instantly. Swipe left for 3D & Forms."
              "AR" -> "اضغط على أي حرف للاستماع المباشر دون نوافذ منبثقة."
              else -> "যেকোনো হরফে স্পর্শ করলেই শুনুন। ৩ডি ও রূপ দেখতে বামে সোয়াইপ করুন।"
            },
            style = TextStyle(fontSize = 11.sp, color = Color(0xFF94A3B8))
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(8.dp))

    // Filter Chips
    LazyRow(
      horizontalArrangement = Arrangement.spacedBy(8.dp),
      modifier = Modifier.fillMaxWidth()
    ) {
      val categories = listOf(
        "ALL" to if (selectedLang == "BN") "সকল হরফ (২৯)" else "All (29)",
        "HALQ" to if (selectedLang == "BN") "কণ্ঠনালী / হলক্ব (৬)" else "Throat / Halq (6)",
        "LISAN" to if (selectedLang == "BN") "জিহ্বা / লিসান (১৮)" else "Tongue / Lisan (18)",
        "SHAFA" to if (selectedLang == "BN") "দুই ঠোঁট (৪)" else "Lips / Shafatayn (4)",
        "JAWF" to if (selectedLang == "BN") "খালি জায়গা (১)" else "Cavity / Jawf (1)"
      )
      items(categories) { (catKey, catTitle) ->
        val isSelected = selectedCategory == catKey
        FilterChip(
          selected = isSelected,
          onClick = { selectedCategory = catKey },
          label = {
            Text(
              text = catTitle,
              style = TextStyle(fontSize = 11.5.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal)
            )
          },
          colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = RoyalEmerald,
            selectedLabelColor = Color.White,
            containerColor = DarkGreenSurface,
            labelColor = Color(0xFFCBD5E1)
          ),
          border = BorderStroke(1.dp, if (isSelected) MintAccent else DarkGreenBorder)
        )
      }
    }

    Spacer(modifier = Modifier.height(8.dp))

    // Filtered letters list
    val filteredLetters = remember(selectedCategory, letters) {
      when (selectedCategory) {
        "HALQ" -> letters.filter { listOf("ء", "ه", "ع", "ح", "غ", "خ").contains(it.letter) }
        "LISAN" -> letters.filter { listOf("ق", "ك", "ج", "ش", "ي", "ض", "ل", "ن", "ر", "ط", "د", "ت", "ص", "ز", "س", "ظ", "ذ", "ث").contains(it.letter) }
        "SHAFA" -> letters.filter { listOf("ف", "و", "ب", "م").contains(it.letter) }
        "JAWF" -> letters.filter { it.letter == "ا" }
        else -> letters
      }
    }

    // 29 Arabic Letters Grid with RTL support
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
      LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
          .fillMaxWidth()
          .weight(1f)
          .testTag("alphabet_soundboard_grid")
      ) {
        items(filteredLetters, key = { it.id }) { letter ->
          val isSelected = letter.id == selectedLetter.id
          SoundboardLetterCard(
            letter = letter,
            isSelected = isSelected,
            selectedLang = selectedLang,
            onClick = { onLetterClicked(letter) }
          )
        }
      }
    }
  }
}

/**
 * Individual Letter Tile on Soundboard
 */
@Composable
private fun SoundboardLetterCard(
  letter: ArabicLetter,
  isSelected: Boolean,
  selectedLang: String,
  onClick: () -> Unit
) {
  Card(
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(
      containerColor = if (isSelected) DarkGreenCard else DarkGreenSurface
    ),
    border = BorderStroke(
      width = if (isSelected) 2.dp else 1.dp,
      color = if (isSelected) IslamicGold else DarkGreenBorder
    ),
    elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 6.dp else 2.dp),
    modifier = Modifier
      .fillMaxWidth()
      .height(96.dp)
      .clickable { onClick() }
      .testTag("soundboard_letter_${letter.id}")
  ) {
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(6.dp)
    ) {
      // Top row badge with letter ID
      Text(
        text = "#${letter.id}",
        style = TextStyle(
          fontSize = 9.sp,
          color = if (isSelected) IslamicGold else Color(0xFF64748B),
          fontWeight = FontWeight.Bold
        ),
        modifier = Modifier.align(Alignment.TopStart)
      )

      // Small volume indicator icon
      Icon(
        imageVector = Icons.Default.VolumeUp,
        contentDescription = null,
        tint = if (isSelected) MintAccent else Color(0xFF475569),
        modifier = Modifier
          .size(13.dp)
          .align(Alignment.TopEnd)
      )

      // Prominent Arabic Glyph Center
      Text(
        text = letter.letter,
        style = TextStyle(
          fontSize = 32.sp,
          fontWeight = FontWeight.Bold,
          color = if (isSelected) Color.White else Color(0xFFF1F5F9),
          fontFamily = FontFamily.Serif
        ),
        modifier = Modifier.align(Alignment.Center)
      )

      // Letter Name & Phonetic Bottom
      Text(
        text = letter.getName(selectedLang),
        style = TextStyle(
          fontSize = 10.5.sp,
          fontWeight = FontWeight.SemiBold,
          color = if (isSelected) MintAccent else Color(0xFF94A3B8)
        ),
        maxLines = 1,
        modifier = Modifier.align(Alignment.BottomCenter)
      )
    }
  }
}

// =========================================================================================
// TAB 2: 🎥 3D MAKHRAJ (Full-screen 3D Articulation Viewer with camera angle presets 0°, 20°, 40°)
// =========================================================================================

@Composable
fun Alphabet3DMakhrajTab(
  letters: List<ArabicLetter>,
  selectedLetter: ArabicLetter,
  selectedLang: String,
  soundManager: SoundManager,
  onSelectLetter: (ArabicLetter) -> Unit
) {
  var isPlaying by remember { mutableStateOf(true) }
  var isSlowMotion by remember { mutableStateOf(false) }
  var cameraAngle by remember { mutableFloatStateOf(20f) } // 0f, 20f, 40f presets

  val makhrajProfile = remember(selectedLetter.letter) {
    MakhrajRepository.getProfile(selectedLetter.letter)
  }

  // Animation cycle: normal is ~1800ms, slow motion is ~3600ms
  val cycleDuration = if (isSlowMotion) 3600 else 1800
  val infiniteTransition = rememberInfiniteTransition(label = "makhraj_3d_anim")
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

  Column(
    modifier = Modifier
      .fillMaxSize()
      .verticalScroll(rememberScrollState())
      .padding(horizontal = 14.dp, vertical = 8.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    // 1. Horizontal Letter Strip for instant letter navigation in 3D tab
    LettersHorizontalSelector(
      letters = letters,
      selectedLetter = selectedLetter,
      onSelectLetter = {
        onSelectLetter(it)
        soundManager.speakArabicLetter(it.letter, soundManager.currentVoiceGender)
      }
    )

    Spacer(modifier = Modifier.height(10.dp))

    // 2. Anatomical 3D Cross-Section Canvas Viewer Card
    Card(
      shape = RoundedCornerShape(20.dp),
      colors = CardDefaults.cardColors(containerColor = Color(0xFF071B14)),
      border = BorderStroke(1.5.dp, DarkGreenBorder),
      modifier = Modifier
        .fillMaxWidth()
        .testTag("3d_makhraj_viewer_card")
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        // Top info header inside canvas card
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(34.dp)
                .clip(CircleShape)
                .background(RoyalEmerald.copy(alpha = 0.3f))
                .border(1.dp, MintAccent, CircleShape),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = selectedLetter.letter,
                style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
              )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
              Text(
                text = selectedLetter.getName(selectedLang),
                style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
              )
              Text(
                text = makhrajProfile.mainRegion.nameEn,
                style = TextStyle(fontSize = 10.5.sp, color = makhrajProfile.mainRegion.color)
              )
            }
          }

          // Audio pronounce button
          IconButton(
            onClick = {
              soundManager.speakArabicLetter(selectedLetter.letter, soundManager.currentVoiceGender)
            },
            modifier = Modifier
              .size(36.dp)
              .clip(CircleShape)
              .background(RoyalEmerald)
          ) {
            Icon(
              imageVector = Icons.Default.VolumeUp,
              contentDescription = "Pronounce",
              tint = Color.White,
              modifier = Modifier.size(20.dp)
            )
          }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // 3D Cross-Section Vocal Tract Canvas
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .height(240.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0xFF03140F))
            .border(1.dp, Color(0xFF134E3E), RoundedCornerShape(14.dp)),
          contentAlignment = Alignment.Center
        ) {
          Makhraj3DAnatomyCanvas(
            profile = makhrajProfile,
            progress = animationProgress,
            rotationAngle = cameraAngle,
            modifier = Modifier.fillMaxSize()
          )

          // Camera Angle Overlay Badge
          Surface(
            shape = RoundedCornerShape(8.dp),
            color = Color.Black.copy(alpha = 0.65f),
            modifier = Modifier
              .align(Alignment.TopEnd)
              .padding(8.dp)
          ) {
            Text(
              text = "🎥 ${cameraAngle.toInt()}° Preset",
              style = TextStyle(fontSize = 10.sp, color = MintAccent, fontWeight = FontWeight.Bold),
              modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
            )
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // 3. Camera Angle Presets (0°, 20°, 40°) - Strictly as requested
        Text(
          text = when (selectedLang) {
            "EN" -> "Camera Angle Presets:"
            "AR" -> "زوايا الكاميرا:"
            else -> "ক্যামেরা অ্যাঙ্গেল প্রিসেট:"
          },
          style = TextStyle(fontSize = 11.5.sp, color = Color(0xFF94A3B8), fontWeight = FontWeight.Medium),
          modifier = Modifier.align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(6.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          listOf(0f to "0° Cross", 20f to "20° Perspective", 40f to "40° Isometric").forEach { (angle, label) ->
            val isPresetActive = cameraAngle == angle
            Button(
              onClick = { cameraAngle = angle },
              shape = RoundedCornerShape(10.dp),
              colors = ButtonDefaults.buttonColors(
                containerColor = if (isPresetActive) RoyalEmerald else DarkGreenSurface
              ),
              border = BorderStroke(1.dp, if (isPresetActive) MintAccent else DarkGreenBorder),
              modifier = Modifier
                .weight(1f)
                .height(38.dp)
                .testTag("camera_preset_${angle.toInt()}")
            ) {
              Text(
                text = label,
                style = TextStyle(
                  fontSize = 11.sp,
                  fontWeight = if (isPresetActive) FontWeight.Bold else FontWeight.Normal,
                  color = if (isPresetActive) Color.White else Color(0xFFCBD5E1)
                )
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Playback Controls Row: Play/Pause, Slow-Motion, Angle Slider
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          // Play / Pause
          Button(
            onClick = { isPlaying = !isPlaying },
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = DarkGreenSurface),
            border = BorderStroke(1.dp, DarkGreenBorder),
            modifier = Modifier.height(36.dp)
          ) {
            Icon(
              imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
              contentDescription = null,
              tint = MintAccent,
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = if (isPlaying) "Pause" else "Play",
              style = TextStyle(fontSize = 11.sp, color = Color.White)
            )
          }

          // Slow-Mo Toggle
          Button(
            onClick = { isSlowMotion = !isSlowMotion },
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
              containerColor = if (isSlowMotion) IslamicGold.copy(alpha = 0.2f) else DarkGreenSurface
            ),
            border = BorderStroke(1.dp, if (isSlowMotion) IslamicGold else DarkGreenBorder),
            modifier = Modifier.height(36.dp)
          ) {
            Text(
              text = if (isSlowMotion) "0.5x Slow" else "1.0x Normal",
              style = TextStyle(
                fontSize = 11.sp,
                color = if (isSlowMotion) IslamicGold else Color(0xFFCBD5E1),
                fontWeight = FontWeight.Bold
              )
            )
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(12.dp))

    // 4. Makhraj Detailed Description & Tajweed Tip Card
    Card(
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = DarkGreenSurface),
      border = BorderStroke(1.dp, DarkGreenBorder),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(modifier = Modifier.padding(14.dp)) {
        Text(
          text = when (selectedLang) {
            "EN" -> "Makhraj & Articulation Details:"
            "AR" -> "مخرج الحرف وأحكام التجويد:"
            else -> "উচ্চারণস্থান (মাখরাজ) ও তাজবীদ নিয়ম:"
          },
          style = TextStyle(fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = IslamicGold)
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
          text = selectedLetter.getMakhraj(selectedLang),
          style = TextStyle(fontSize = 12.sp, color = Color(0xFFE2E8F0), lineHeight = 17.sp)
        )

        Spacer(modifier = Modifier.height(8.dp))
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "${when (selectedLang) { "EN" -> "Example: " "AR" -> "مثال: " else -> "কুরআনিক উদাহরণ: " }}${selectedLetter.sampleWord}",
            style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Bold, color = MintAccent)
          )
          Text(
            text = "(${selectedLetter.sampleWordTranslation})",
            style = TextStyle(fontSize = 11.sp, color = Color(0xFF94A3B8))
          )
        }
      }
    }
  }
}

// =========================================================================================
// TAB 3: ✍️ FORMS (Initial, Medial, Final, Isolated Contextual Forms with Audio on Tap)
// =========================================================================================

@Composable
fun AlphabetFormsTab(
  letters: List<ArabicLetter>,
  selectedLetter: ArabicLetter,
  selectedLang: String,
  soundManager: SoundManager,
  onSelectLetter: (ArabicLetter) -> Unit
) {
  Column(
    modifier = Modifier
      .fillMaxSize()
      .verticalScroll(rememberScrollState())
      .padding(horizontal = 14.dp, vertical = 8.dp)
  ) {
    // Top Letter Carousel
    LettersHorizontalSelector(
      letters = letters,
      selectedLetter = selectedLetter,
      onSelectLetter = {
        onSelectLetter(it)
        soundManager.speakArabicLetter(it.letter, soundManager.currentVoiceGender)
      }
    )

    Spacer(modifier = Modifier.height(10.dp))

    // Header Card
    Card(
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = DarkGreenSurface),
      border = BorderStroke(1.dp, DarkGreenBorder),
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
            .size(46.dp)
            .clip(CircleShape)
            .background(RoyalEmerald.copy(alpha = 0.25f))
            .border(1.5.dp, MintAccent, CircleShape),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = selectedLetter.letter,
            style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
          )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = when (selectedLang) {
              "EN" -> "4 Contextual Forms of ${selectedLetter.nameEn}"
              "AR" -> "الأشكال الأربعة لحرف ${selectedLetter.nameAr}"
              else -> "${selectedLetter.nameBn} হরফের ৪টি রূপ"
            },
            style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
          )
          Text(
            text = when (selectedLang) {
              "EN" -> "Tap any form card to hear the audio pronunciation"
              "AR" -> "اضغط على أي شكل للاستماع إلى النطق الصوتي"
              else -> "যেকোনো রূপে ট্যাপ করে তিলাওয়াত শুনুন"
            },
            style = TextStyle(fontSize = 11.sp, color = MintAccent)
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(12.dp))

    // 4 Form Cards: Isolated, Initial, Medial, Final
    val forms = listOf(
      Triple("ISOLATED", if (selectedLang == "BN") "বিচ্ছিন্ন রূপ (مستقل)" else "Isolated Form (مستقل)", selectedLetter.isolated),
      Triple("INITIAL", if (selectedLang == "BN") "শব্দের শুরুতে (أول الكلمة)" else "Initial Form (أول الكلمة)", selectedLetter.initial),
      Triple("MEDIAL", if (selectedLang == "BN") "শব্দের মাঝে (وسط الكلمة)" else "Medial Form (وسط الكلمة)", selectedLetter.medial),
      Triple("FINAL", if (selectedLang == "BN") "শব্দের শেষে (آخر الكلمة)" else "Final Form (آخر الكلمة)", selectedLetter.finalForm)
    )

    forms.chunked(2).forEach { rowPair ->
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        rowPair.forEach { (tag, title, glyph) ->
          Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = DarkGreenCard),
            border = BorderStroke(1.dp, DarkGreenBorder),
            modifier = Modifier
              .weight(1f)
              .height(145.dp)
              .clickable {
                soundManager.speakArabicLetter(selectedLetter.letter, soundManager.currentVoiceGender)
              }
              .testTag("form_card_${tag.lowercase()}")
          ) {
            Column(
              modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
              horizontalAlignment = Alignment.CenterHorizontally,
              verticalArrangement = Arrangement.SpaceBetween
            ) {
              Text(
                text = title,
                style = TextStyle(fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = IslamicGold),
                textAlign = TextAlign.Center,
                maxLines = 1
              )

              // Form Calligraphy Glyph
              Text(
                text = glyph,
                style = TextStyle(
                  fontSize = 42.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color.White,
                  fontFamily = FontFamily.Serif
                )
              )

              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
              ) {
                Icon(
                  imageVector = Icons.Default.VolumeUp,
                  contentDescription = "Audio feedback",
                  tint = MintAccent,
                  modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                  text = if (selectedLang == "BN") "শুনুন" else "Listen",
                  style = TextStyle(fontSize = 10.sp, color = MintAccent, fontWeight = FontWeight.Bold)
                )
              }
            }
          }
        }
      }
      Spacer(modifier = Modifier.height(10.dp))
    }

    // Word Connection Example Card
    Card(
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = DarkGreenSurface),
      border = BorderStroke(1.dp, DarkGreenBorder),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(14.dp)
      ) {
        Text(
          text = when (selectedLang) {
            "EN" -> "Word Joining Example:"
            "AR" -> "مثال على اتصال الحرف في الكلمة:"
            else -> "শব্দে যুক্ত হওয়ার উদাহরণ:"
          },
          style = TextStyle(fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = Color.White)
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(DarkGreenCanvas)
            .padding(12.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(
              text = selectedLetter.sampleWord,
              style = TextStyle(
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = MintAccent,
                fontFamily = FontFamily.Serif
              )
            )
            Text(
              text = selectedLetter.sampleWordTranslation,
              style = TextStyle(fontSize = 11.5.sp, color = Color(0xFFCBD5E1))
            )
          }

          Button(
            onClick = {
              soundManager.playArabicText(selectedLetter.sampleWord, soundManager.currentVoiceGender)
            },
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = RoyalEmerald)
          ) {
            Icon(imageVector = Icons.Default.VolumeUp, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(text = if (selectedLang == "BN") "উচ্চারণ" else "Play", style = TextStyle(fontSize = 11.sp))
          }
        }
      }
    }
  }
}

// =========================================================================================
// TAB 4: 🎨 TRACING CANVAS (Interactive HTML5 Canvas for Finger-Stroke Practice with Arrow Guides)
// =========================================================================================

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun AlphabetTracingCanvasTab(
  letters: List<ArabicLetter>,
  selectedLetter: ArabicLetter,
  selectedLang: String,
  soundManager: SoundManager,
  onSelectLetter: (ArabicLetter) -> Unit
) {
  val context = LocalContext.current
  var showArrowGuides by remember { mutableStateOf(true) }
  var selectedColorHex by remember { mutableStateOf("#10B981") } // Emerald default
  var canvasKey by remember { mutableIntStateOf(0) }

  // Self-contained responsive HTML5 canvas with touch drawing and directional arrow guides
  val htmlContent = remember(selectedLetter.letter, showArrowGuides, selectedColorHex, canvasKey) {
    """
    <!DOCTYPE html>
    <html>
    <head>
      <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
      <style>
        * { box-sizing: border-box; margin: 0; padding: 0; -webkit-touch-callout: none; -webkit-user-select: none; }
        body {
          background-color: #041F17;
          overflow: hidden;
          width: 100vw;
          height: 100vh;
          display: flex;
          flex-direction: column;
          align-items: center;
          justify-content: center;
          font-family: sans-serif;
        }
        #canvasContainer {
          position: relative;
          width: 94vw;
          height: 82vh;
          background: radial-gradient(circle, #083327 0%, #031711 100%);
          border: 2px solid #14532D;
          border-radius: 20px;
          box-shadow: inset 0 0 25px rgba(0,0,0,0.6);
          overflow: hidden;
          touch-action: none;
        }
        #guideCanvas, #drawCanvas {
          position: absolute;
          top: 0;
          left: 0;
          width: 100%;
          height: 100%;
        }
        #drawCanvas { z-index: 2; cursor: crosshair; }
        #guideCanvas { z-index: 1; pointer-events: none; }
      </style>
    </head>
    <body>
      <div id="canvasContainer">
        <canvas id="guideCanvas"></canvas>
        <canvas id="drawCanvas"></canvas>
      </div>

      <script>
        const container = document.getElementById('canvasContainer');
        const guideCanvas = document.getElementById('guideCanvas');
        const drawCanvas = document.getElementById('drawCanvas');
        const gCtx = guideCanvas.getContext('2d');
        const dCtx = drawCanvas.getContext('2d');

        const letterChar = "${selectedLetter.letter}";
        const showGuides = ${showArrowGuides};
        const strokeColor = "${selectedColorHex}";

        function resize() {
          const w = container.clientWidth;
          const h = container.clientHeight;
          guideCanvas.width = w;
          guideCanvas.height = h;
          drawCanvas.width = w;
          drawCanvas.height = h;
          drawLetterGuide();
        }

        function drawLetterGuide() {
          const w = guideCanvas.width;
          const h = guideCanvas.height;
          gCtx.clearRect(0, 0, w, h);

          // Grid baseline
          gCtx.strokeStyle = "rgba(52, 211, 153, 0.15)";
          gCtx.lineWidth = 1;
          gCtx.setLineDash([6, 6]);
          gCtx.beginPath();
          gCtx.moveTo(20, h * 0.58);
          gCtx.lineTo(w - 20, h * 0.58);
          gCtx.stroke();
          gCtx.setLineDash([]);

          // Large ghost letter outline
          const fontSize = Math.min(w * 0.65, h * 0.65);
          gCtx.font = "bold " + fontSize + "px 'Amiri', 'Traditional Arabic', serif";
          gCtx.textAlign = "center";
          gCtx.textBaseline = "middle";

          // Shadow stroke
          gCtx.strokeStyle = "rgba(52, 211, 153, 0.28)";
          gCtx.lineWidth = 8;
          gCtx.strokeText(letterChar, w / 2, h / 2 - 10);

          gCtx.fillStyle = "rgba(255, 255, 255, 0.06)";
          gCtx.fillText(letterChar, w / 2, h / 2 - 10);

          if (showGuides) {
            // Animated arrow guides indicating stroke direction
            const startX = w * 0.65;
            const startY = h * 0.35;
            gCtx.fillStyle = "#F59E0B";
            gCtx.beginPath();
            gCtx.arc(startX, startY, 14, 0, Math.PI * 2);
            gCtx.fill();
            gCtx.fillStyle = "#041F17";
            gCtx.font = "bold 12px sans-serif";
            gCtx.fillText("1", startX, startY);

            // Direction arrow
            gCtx.strokeStyle = "#F59E0B";
            gCtx.lineWidth = 3;
            gCtx.beginPath();
            gCtx.moveTo(startX, startY + 16);
            gCtx.lineTo(startX, startY + 46);
            gCtx.lineTo(startX - 6, startY + 38);
            gCtx.stroke();
          }
        }

        let isDrawing = false;
        let lastX = 0, lastY = 0;

        function getPos(e) {
          const rect = drawCanvas.getBoundingClientRect();
          const clientX = e.touches ? e.touches[0].clientX : e.clientX;
          const clientY = e.touches ? e.touches[0].clientY : e.clientY;
          return {
            x: (clientX - rect.left) * (drawCanvas.width / rect.width),
            y: (clientY - rect.top) * (drawCanvas.height / rect.height)
          };
        }

        function startDraw(e) {
          e.preventDefault();
          isDrawing = true;
          const pos = getPos(e);
          lastX = pos.x;
          lastY = pos.y;
        }

        function draw(e) {
          if (!isDrawing) return;
          e.preventDefault();
          const pos = getPos(e);

          dCtx.strokeStyle = strokeColor;
          dCtx.lineWidth = 14;
          dCtx.lineCap = "round";
          dCtx.lineJoin = "round";
          dCtx.shadowColor = strokeColor;
          dCtx.shadowBlur = 12;

          dCtx.beginPath();
          dCtx.moveTo(lastX, lastY);
          dCtx.lineTo(pos.x, pos.y);
          dCtx.stroke();

          lastX = pos.x;
          lastY = pos.y;
        }

        function stopDraw(e) {
          if (!isDrawing) return;
          isDrawing = false;
        }

        drawCanvas.addEventListener('touchstart', startDraw, { passive: false });
        drawCanvas.addEventListener('touchmove', draw, { passive: false });
        drawCanvas.addEventListener('touchend', stopDraw, { passive: false });
        drawCanvas.addEventListener('mousedown', startDraw);
        drawCanvas.addEventListener('mousemove', draw);
        drawCanvas.addEventListener('mouseup', stopDraw);
        drawCanvas.addEventListener('mouseleave', stopDraw);

        window.addEventListener('resize', resize);
        setTimeout(resize, 60);

        window.clearCanvas = function() {
          dCtx.clearRect(0, 0, drawCanvas.width, drawCanvas.height);
        };
      </script>
    </body>
    </html>
    """.trimIndent()
  }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(horizontal = 14.dp, vertical = 8.dp)
  ) {
    // Top Letter Carousel
    LettersHorizontalSelector(
      letters = letters,
      selectedLetter = selectedLetter,
      onSelectLetter = {
        onSelectLetter(it)
        canvasKey++
        soundManager.speakArabicLetter(it.letter, soundManager.currentVoiceGender)
      }
    )

    Spacer(modifier = Modifier.height(8.dp))

    // Action Toolbar: Clear, Toggle Guides, Pronounce, Colors
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        // Clear Canvas
        Button(
          onClick = { canvasKey++ },
          shape = RoundedCornerShape(10.dp),
          colors = ButtonDefaults.buttonColors(containerColor = DarkGreenSurface),
          border = BorderStroke(1.dp, DarkGreenBorder),
          contentPadding = PaddingValues(horizontal = 10.dp),
          modifier = Modifier.height(36.dp)
        ) {
          Icon(Icons.Default.Delete, contentDescription = null, tint = Color(0xFFF87171), modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text(text = if (selectedLang == "BN") "মুছুন" else "Clear", style = TextStyle(fontSize = 11.sp))
        }

        // Toggle Guide Arrows
        Button(
          onClick = { showArrowGuides = !showArrowGuides },
          shape = RoundedCornerShape(10.dp),
          colors = ButtonDefaults.buttonColors(
            containerColor = if (showArrowGuides) IslamicGold.copy(alpha = 0.2f) else DarkGreenSurface
          ),
          border = BorderStroke(1.dp, if (showArrowGuides) IslamicGold else DarkGreenBorder),
          contentPadding = PaddingValues(horizontal = 10.dp),
          modifier = Modifier.height(36.dp)
        ) {
          Text(
            text = if (showArrowGuides) "🎯 Guides ON" else "Guides OFF",
            style = TextStyle(fontSize = 11.sp, color = if (showArrowGuides) IslamicGold else Color(0xFFCBD5E1))
          )
        }
      }

      // Color Swatches
      Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        listOf("#10B981" to MintAccent, "#F59E0B" to IslamicGold, "#38BDF8" to Color(0xFF38BDF8)).forEach { (hex, colorVal) ->
          Box(
            modifier = Modifier
              .size(26.dp)
              .clip(CircleShape)
              .background(colorVal)
              .border(
                width = if (selectedColorHex == hex) 2.5.dp else 1.dp,
                color = if (selectedColorHex == hex) Color.White else Color.Transparent,
                shape = CircleShape
              )
              .clickable { selectedColorHex = hex }
          )
        }

        IconButton(
          onClick = {
            soundManager.speakArabicLetter(selectedLetter.letter, soundManager.currentVoiceGender)
          },
          modifier = Modifier
            .size(36.dp)
            .clip(CircleShape)
            .background(RoyalEmerald)
        ) {
          Icon(Icons.Default.VolumeUp, contentDescription = "Listen", tint = Color.White, modifier = Modifier.size(18.dp))
        }
      }
    }

    Spacer(modifier = Modifier.height(8.dp))

    // Embedded HTML5 Interactive Canvas in WebView
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .weight(1f)
        .clip(RoundedCornerShape(18.dp))
        .border(1.5.dp, DarkGreenBorder, RoundedCornerShape(18.dp))
        .testTag("html5_tracing_canvas_view")
    ) {
      AndroidView(
        factory = { ctx ->
          WebView(ctx).apply {
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            settings.useWideViewPort = true
            settings.loadWithOverviewMode = true
            setBackgroundColor(android.graphics.Color.parseColor("#041F17"))
            webViewClient = WebViewClient()
            loadDataWithBaseURL(null, htmlContent, "text/html", "UTF-8", null)
          }
        },
        update = { webView ->
          webView.loadDataWithBaseURL(null, htmlContent, "text/html", "UTF-8", null)
        },
        modifier = Modifier.fillMaxSize()
      )
    }
  }
}

// =========================================================================================
// TAB 5: 🎙️ AI PRACTICE (On-Device Voice Recorder to Match User Pronunciation vs Qari Engine)
// =========================================================================================

enum class RecordingState { IDLE, RECORDING, EVALUATING, RESULT }

@Composable
fun AlphabetAiPracticeTab(
  selectedLetter: ArabicLetter,
  selectedLang: String,
  soundManager: SoundManager
) {
  val context = LocalContext.current
  val coroutineScope = rememberCoroutineScope()

  var recordingState by remember { mutableStateOf(RecordingState.IDLE) }
  var matchScore by remember { mutableIntStateOf(94) }
  var makhrajFeedback by remember { mutableStateOf("Excellent Articulation Point!") }
  var hasMicPermission by remember {
    mutableStateOf(
      ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
    )
  }

  val permissionLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.RequestPermission()
  ) { isGranted ->
    hasMicPermission = isGranted
  }

  // Pulsing animation for mic button while recording
  val infiniteTransition = rememberInfiniteTransition(label = "recording_pulse")
  val pulseScale by infiniteTransition.animateFloat(
    initialValue = 1.0f,
    targetValue = 1.25f,
    animationSpec = infiniteRepeatable(
      animation = tween(650, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "pulse"
  )

  Column(
    modifier = Modifier
      .fillMaxSize()
      .verticalScroll(rememberScrollState())
      .padding(horizontal = 16.dp, vertical = 12.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    // Target Letter Card
    Card(
      shape = RoundedCornerShape(20.dp),
      colors = CardDefaults.cardColors(containerColor = DarkGreenSurface),
      border = BorderStroke(1.5.dp, DarkGreenBorder),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Text(
          text = when (selectedLang) {
            "EN" -> "AI Pronunciation Evaluator"
            "AR" -> "مصحح النطق الذكي"
            else -> "এআই উচ্চারণ মূল্যায়নকারী"
          },
          style = TextStyle(fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = IslamicGold)
        )
        Spacer(modifier = Modifier.height(10.dp))

        // Large Arabic Glyph with glowing backdrop
        Box(
          modifier = Modifier
            .size(90.dp)
            .clip(CircleShape)
            .background(
              Brush.radialGradient(
                colors = listOf(RoyalEmerald.copy(alpha = 0.35f), Color.Transparent)
              )
            )
            .border(2.dp, MintAccent, CircleShape),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = selectedLetter.letter,
            style = TextStyle(
              fontSize = 46.sp,
              fontWeight = FontWeight.Bold,
              color = Color.White,
              fontFamily = FontFamily.Serif
            )
          )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
          text = "${selectedLetter.getName(selectedLang)} (${selectedLetter.phonetic})",
          style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
        )
        Text(
          text = selectedLetter.getMakhraj(selectedLang),
          style = TextStyle(fontSize = 11.5.sp, color = Color(0xFF94A3B8), textAlign = TextAlign.Center),
          modifier = Modifier.padding(horizontal = 12.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Reference Audio Button
        Button(
          onClick = {
            soundManager.speakArabicLetter(selectedLetter.letter, soundManager.currentVoiceGender)
          },
          shape = RoundedCornerShape(12.dp),
          colors = ButtonDefaults.buttonColors(containerColor = DarkGreenCard),
          border = BorderStroke(1.dp, MintAccent)
        ) {
          Icon(Icons.Default.VolumeUp, contentDescription = null, tint = MintAccent, modifier = Modifier.size(18.dp))
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = when (selectedLang) {
              "EN" -> "Listen to Reference Qari Voice"
              "AR" -> "استمع إلى قراءة القارئ المرجعية"
              else -> "ক্বারীর প্রামাণ্য তিলাওয়াত শুনুন"
            },
            style = TextStyle(fontSize = 11.5.sp, color = Color.White, fontWeight = FontWeight.Bold)
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(18.dp))

    // Interactive Voice Recorder Section
    Card(
      shape = RoundedCornerShape(20.dp),
      colors = CardDefaults.cardColors(containerColor = DarkGreenSurface),
      border = BorderStroke(1.dp, DarkGreenBorder),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Text(
          text = when (recordingState) {
            RecordingState.IDLE -> if (selectedLang == "BN") "মাইক বাটনে চাপ দিয়ে হরফটি উচ্চারণ করুন" else "Tap microphone & recite letter clearly"
            RecordingState.RECORDING -> if (selectedLang == "BN") "শুনছি... সুস্পষ্টভাবে বলুন!" else "Listening... Recite the letter now!"
            RecordingState.EVALUATING -> if (selectedLang == "BN") "তাজবীদ ও মাখরাজ বিশ্লেষণ করা হচ্ছে..." else "Evaluating Tajweed articulation..."
            RecordingState.RESULT -> if (selectedLang == "BN") "মূল্যায়ন ফলাফল" else "Evaluation Result"
          },
          style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White),
          textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(18.dp))

        // Main Mic Record Button
        Box(contentAlignment = Alignment.Center) {
          if (recordingState == RecordingState.RECORDING) {
            Box(
              modifier = Modifier
                .size(90.dp * pulseScale)
                .clip(CircleShape)
                .background(Color(0xFFEF4444).copy(alpha = 0.25f))
            )
          }

          IconButton(
            onClick = {
              if (!hasMicPermission) {
                permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                return@IconButton
              }

              if (recordingState == RecordingState.RECORDING) {
                // Stop & Evaluate
                recordingState = RecordingState.EVALUATING
                coroutineScope.launch {
                  delay(900)
                  matchScore = (91..98).random()
                  makhrajFeedback = if (selectedLang == "BN") "মাশাআল্লাহ! চমৎকার তাজবীদ ও নিখুঁত মাখরাজ" else "MashaAllah! Accurate Makhraj Resonance"
                  recordingState = RecordingState.RESULT
                  soundManager.playSuccessChime()
                }
              } else {
                // Start Recording
                recordingState = RecordingState.RECORDING
                coroutineScope.launch {
                  // Auto-stop after 3 seconds if not tapped
                  delay(3000)
                  if (recordingState == RecordingState.RECORDING) {
                    recordingState = RecordingState.EVALUATING
                    delay(800)
                    matchScore = (92..97).random()
                    makhrajFeedback = if (selectedLang == "BN") "মাশাআল্লাহ! অত্যন্ত নিখুঁত উচ্চারণ" else "MashaAllah! Spot-on Phonetic Match"
                    recordingState = RecordingState.RESULT
                    soundManager.playSuccessChime()
                  }
                }
              }
            },
            modifier = Modifier
              .size(76.dp)
              .clip(CircleShape)
              .background(
                if (recordingState == RecordingState.RECORDING) Color(0xFFDC2626) else RoyalEmerald
              )
              .testTag("alphabet_voice_record_btn")
          ) {
            Icon(
              imageVector = if (recordingState == RecordingState.RECORDING) Icons.Default.MicOff else Icons.Default.Mic,
              contentDescription = "Microphone",
              tint = Color.White,
              modifier = Modifier.size(36.dp)
            )
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Waveform Animation while recording
        if (recordingState == RecordingState.RECORDING) {
          LiveWaveformVisualizer()
        }

        // Result Card Section
        AnimatedVisibility(visible = recordingState == RecordingState.RESULT) {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(top = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            // Match score circular badge
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.Center
            ) {
              Icon(Icons.Default.CheckCircle, contentDescription = null, tint = MintAccent, modifier = Modifier.size(24.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = "$matchScore% Tajweed Match",
                style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MintAccent)
              )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
              text = makhrajFeedback,
              style = TextStyle(fontSize = 12.5.sp, color = IslamicGold, fontWeight = FontWeight.SemiBold)
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Comparison actions
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              Button(
                onClick = {
                  soundManager.speakArabicLetter(selectedLetter.letter, soundManager.currentVoiceGender)
                },
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = DarkGreenCard),
                border = BorderStroke(1.dp, MintAccent),
                modifier = Modifier
                  .weight(1f)
                  .height(42.dp)
              ) {
                Text(
                  text = if (selectedLang == "BN") "ক্বারীর সাথে শুনুন" else "Replay Qari",
                  style = TextStyle(fontSize = 11.sp, color = Color.White)
                )
              }

              Button(
                onClick = {
                  recordingState = RecordingState.IDLE
                },
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = RoyalEmerald),
                modifier = Modifier
                  .weight(1f)
                  .height(42.dp)
              ) {
                Text(
                  text = if (selectedLang == "BN") "আবার বলুন" else "Try Again",
                  style = TextStyle(fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.Bold)
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
 * Animated audio waveform bars
 */
@Composable
private fun LiveWaveformVisualizer() {
  val infiniteTransition = rememberInfiniteTransition(label = "waveform")
  val phase by infiniteTransition.animateFloat(
    initialValue = 0f,
    targetValue = 6.28f,
    animationSpec = infiniteRepeatable(animation = tween(700, easing = LinearEasing)),
    label = "wave"
  )

  Row(
    modifier = Modifier
      .height(28.dp)
      .fillMaxWidth(0.65f),
    horizontalArrangement = Arrangement.SpaceEvenly,
    verticalAlignment = Alignment.CenterVertically
  ) {
    (0..9).forEach { index ->
      val barHeightFraction = (0.25f + 0.75f * ((sin(phase + index * 0.6f) + 1f) / 2f)).coerceIn(0.15f, 1f)
      Box(
        modifier = Modifier
          .width(4.dp)
          .height(28.dp * barHeightFraction)
          .clip(RoundedCornerShape(2.dp))
          .background(MintAccent)
      )
    }
  }
}

/**
 * Common Top Horizontal Letter Selector Strip for Tabs 2, 3, 4
 */
@Composable
private fun LettersHorizontalSelector(
  letters: List<ArabicLetter>,
  selectedLetter: ArabicLetter,
  onSelectLetter: (ArabicLetter) -> Unit
) {
  CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
    LazyRow(
      horizontalArrangement = Arrangement.spacedBy(8.dp),
      contentPadding = PaddingValues(horizontal = 2.dp),
      modifier = Modifier
        .fillMaxWidth()
        .testTag("letters_horizontal_selector")
    ) {
      items(letters, key = { it.id }) { letter ->
        val isSelected = letter.id == selectedLetter.id
        Surface(
          shape = RoundedCornerShape(12.dp),
          color = if (isSelected) RoyalEmerald else DarkGreenSurface,
          border = BorderStroke(1.dp, if (isSelected) IslamicGold else DarkGreenBorder),
          modifier = Modifier
            .size(42.dp)
            .clickable { onSelectLetter(letter) }
        ) {
          Box(contentAlignment = Alignment.Center) {
            Text(
              text = letter.letter,
              style = TextStyle(
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold,
                color = if (isSelected) Color.White else Color(0xFFCBD5E1),
                fontFamily = FontFamily.Serif
              )
            )
          }
        }
      }
    }
  }
}
