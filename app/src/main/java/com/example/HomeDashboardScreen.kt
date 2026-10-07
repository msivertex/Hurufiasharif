package com.example

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Menu
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
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.compose.ui.graphics.drawscope.Stroke
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import com.example.islamic.AsmaUlHusnaScreen
import com.example.islamic.IslamicLocationService
import com.example.islamic.IslamicOfflineRepository
import com.example.islamic.IslamicSuiteScreen
import com.example.islamic.IslamicSuiteTab
import com.example.islamic.MultiCalendarScreen
import com.example.islamic.LocationSettingsDialog
import com.example.islamic.NextPrayerCompactWidget
import com.example.islamic.PrayerTimesCalculator
import com.example.islamic.QiblaCompassScreen
import com.example.ui.theme.BackgroundGray
import com.example.ui.theme.RoyalEmerald
import kotlinx.coroutines.launch

enum class HomeSubScreen {
  ARABIC_ALPHABET,
  GAME_SELECTOR,
  KAIDA_EDUCATION,
  AMPARA_SURAHS,
  ASMA_UL_HUSNA,
  SALAT_GUIDE,
  HADITH_COLLECTION
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeDashboardScreen(
  selectedLang: String,
  onLanguageChange: (String) -> Unit,
  userEmail: String,
  currentUserId: String = "",
  onSignOut: () -> Unit
) {
  val context = LocalContext.current
  val soundManager = remember { SoundManager.getInstance(context) }
  DisposableEffect(Unit) {
    onDispose { soundManager.release() }
  }

  val firebaseUserRepo = remember {
    try {
      com.example.firebase.FirebaseUserRepository(context)
    } catch (e: Exception) {
      null
    }
  }

  val layoutDirection = if (selectedLang == "AR") LayoutDirection.Rtl else LayoutDirection.Ltr
  val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
  val scope = rememberCoroutineScope()

  // Interactive user stats backed by OfflineProgressRepository & Islamic repository
  val progressRepo = remember { OfflineProgressRepository.getInstance(context) }
  val islamicOfflineRepo = remember { IslamicOfflineRepository.getInstance(context) }

  val userCoins = progressRepo.userCoins
  val userLevel = progressRepo.userLevel
  val userStreak = progressRepo.userStreak

  // Real-time Cloud Sync with Firestore
  LaunchedEffect(currentUserId, userCoins, userLevel, userStreak, selectedLang) {
    if (currentUserId.isNotBlank() && currentUserId != "guest_user" && firebaseUserRepo != null) {
      try {
        firebaseUserRepo.saveUserProfile(
          com.example.firebase.UserProfile(
            userId = currentUserId,
            email = userEmail,
            displayName = userEmail.substringBefore("@"),
            coins = userCoins.toLong(),
            level = userLevel.toLong(),
            streak = userStreak.toLong(),
            selectedLanguage = selectedLang,
            selectedQariVoice = soundManager.currentVoiceGender.name
          )
        )
      } catch (e: Exception) {
        android.util.Log.w("HomeDashboard", "Firestore sync skipped or error", e)
      }
    }
  }

  var selectedLetterForDetail by remember { mutableStateOf<ArabicLetter?>(null) }
  var letterForMakhrajVisualizer by remember { mutableStateOf<ArabicLetter?>(null) }
  var showSettingsDialog by remember { mutableStateOf(false) }
  var activeGameModal by remember { mutableStateOf<GameMode?>(null) }
  var isLetterGridView by remember { mutableStateOf(true) }
  var currentBottomTab by remember { mutableStateOf(MainAppTab.HOME) }

  var activeSubScreen by remember { mutableStateOf<HomeSubScreen?>(null) }
  var showFullTimetableDialog by remember { mutableStateOf(false) }
  var selectedDuaForDialog by remember { mutableStateOf<DailyDuaItem?>(null) }
  var selectedDeedForDialog by remember { mutableStateOf<VirtuousDeedItem?>(null) }
  var showLearningProgressDialog by remember { mutableStateOf(false) }
  var showAzanSettingsDialog by remember { mutableStateOf(false) }
  var showOfflineManagerDialog by remember { mutableStateOf(false) }
  var showPrivacyPolicyDialog by remember { mutableStateOf(false) }
  var showLanguageDialog by remember { mutableStateOf(false) }
  var showLocationSettingsDialog by remember { mutableStateOf(false) }
  var showAudioQariDialog by remember { mutableStateOf(false) }
  var showFeedbackDialog by remember { mutableStateOf(false) }
  var showDeveloperDialog by remember { mutableStateOf(false) }
  var isAzanMuted by remember { mutableStateOf(islamicOfflineRepo.isAzanMuted) }

  val locationService = remember { IslamicLocationService(context) }
  var userLocationTrigger by remember { mutableIntStateOf(0) }

  val locationPermissionLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.RequestMultiplePermissions()
  ) { permissions ->
    val granted = permissions[android.Manifest.permission.ACCESS_FINE_LOCATION] == true ||
      permissions[android.Manifest.permission.ACCESS_COARSE_LOCATION] == true
    if (granted) {
      locationService.fetchCurrentLocation(
        onSuccess = { lat, lng, name, tz, isGps, autoMethod, autoJuristic ->
          islamicOfflineRepo.saveLocation(lat, lng, name, tz, isGps, autoMethod, autoJuristic)
          userLocationTrigger++
        },
        onFailure = {}
      )
    }
  }

  // Auto-sync GPS location on start if permission granted, or prompt for permission
  LaunchedEffect(Unit) {
    if (locationService.hasLocationPermission()) {
      locationService.fetchCurrentLocation(
        onSuccess = { lat, lng, name, tz, isGps, autoMethod, autoJuristic ->
          islamicOfflineRepo.saveLocation(lat, lng, name, tz, isGps, autoMethod, autoJuristic)
          userLocationTrigger++
        },
        onFailure = {}
      )
    } else {
      locationPermissionLauncher.launch(
        arrayOf(
          android.Manifest.permission.ACCESS_FINE_LOCATION,
          android.Manifest.permission.ACCESS_COARSE_LOCATION
        )
      )
    }
  }

  val prayerSchedule = remember(
    userLocationTrigger,
    islamicOfflineRepo.latitude,
    islamicOfflineRepo.longitude,
    islamicOfflineRepo.timezoneHours,
    islamicOfflineRepo.calculationMethod,
    islamicOfflineRepo.juristicMethod,
    islamicOfflineRepo.locationName,
    islamicOfflineRepo.isGpsLocated
  ) {
    PrayerTimesCalculator.calculate(
      latitude = islamicOfflineRepo.latitude,
      longitude = islamicOfflineRepo.longitude,
      timezoneOffset = islamicOfflineRepo.timezoneHours,
      locationName = islamicOfflineRepo.locationName,
      method = islamicOfflineRepo.calculationMethod,
      juristicMethod = islamicOfflineRepo.juristicMethod,
      isGpsLocated = islamicOfflineRepo.isGpsLocated
    )
  }

  // When any game mode is launched, display the immersive Full-Screen 20-Level Selection Roadmap
  activeGameModal?.let { game ->
    FullScreenGameRoadmap(
      gameMode = game,
      selectedLang = selectedLang,
      soundManager = soundManager,
      onExit = { activeGameModal = null }
    )
    return
  }

  CompositionLocalProvider(LocalLayoutDirection provides layoutDirection) {
    ModalNavigationDrawer(
      drawerState = drawerState,
      drawerContent = {
        HurufiaNavigationDrawerContent(
          selectedLang = selectedLang,
          currentVoiceGender = soundManager.currentVoiceGender,
          isQuizHintsEnabled = soundManager.beginnerHintsEnabledState,
          onQuizHintsChange = { enabled ->
            soundManager.setBeginnerHintsEnabled(enabled)
          },
          isDarkModeEnabled = soundManager.darkModeEnabledState,
          onDarkModeChange = { enabled ->
            soundManager.setDarkModeEnabled(enabled)
          },
          isGpsLocated = islamicOfflineRepo.isGpsLocated,
          locationName = islamicOfflineRepo.locationName,
          onItemClick = { dest ->
            scope.launch { drawerState.close() }
            when (dest) {
              DrawerDestination.LANGUAGE -> showLanguageDialog = true
              DrawerDestination.LOCATION -> showLocationSettingsDialog = true
              DrawerDestination.NOTIFICATION_AZAN -> showAzanSettingsDialog = true
              DrawerDestination.AUDIO_QARI -> showAudioQariDialog = true
              DrawerDestination.OFFLINE_MANAGER -> showOfflineManagerDialog = true
              DrawerDestination.SHARE_APP -> {
                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                  type = "text/plain"
                  putExtra(
                    Intent.EXTRA_TEXT,
                    "হরুফিয়া শরিফ (HURUFIA SHARIF) - আরবি হরফ ও কুরআন শিক্ষা অ্যাপ! অফলাইনে নামাজ সময় ও তাজবীদ শিখুন।"
                  )
                }
                context.startActivity(Intent.createChooser(shareIntent, "শেয়ার করুন"))
              }
              DrawerDestination.RATE_US -> {
                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                  type = "text/plain"
                  putExtra(Intent.EXTRA_TEXT, "হরুফিয়া শরিফ অ্যাপটি অত্যন্ত উপকারী ও সমৃদ্ধ একটি ইসলামিক প্ল্যাটফর্ম!")
                }
                context.startActivity(Intent.createChooser(shareIntent, "রেটিং ও রিভিউ"))
              }
              DrawerDestination.FEEDBACK -> showFeedbackDialog = true
              DrawerDestination.PRIVACY_POLICY -> showPrivacyPolicyDialog = true
              DrawerDestination.ABOUT_DEVELOPER -> showDeveloperDialog = true
            }
          }
        )
      }
    ) {
      Scaffold(
        containerColor = if (soundManager.darkModeEnabledState) Color(0xFF0B1320) else BackgroundGray,
        topBar = {
          if (activeSubScreen == null) {
            TopAppBar(
              title = {
                Text(
                  text = AppStrings.getAppName(selectedLang),
                  style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 20.sp,
                    letterSpacing = if (selectedLang == "EN") 1.2.sp else 0.sp
                  ),
                  modifier = Modifier.testTag("app_title")
                )
              },
              colors = TopAppBarDefaults.topAppBarColors(
                containerColor = RoyalEmerald
              ),
              actions = {
                IconButton(
                  onClick = {
                    scope.launch { drawerState.open() }
                  },
                  modifier = Modifier.testTag("home_hamburger_menu_button")
                ) {
                  Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Navigation Drawer Menu",
                    tint = Color.White,
                    modifier = Modifier.size(26.dp)
                  )
                }
              }
            )
          }
        },
        bottomBar = {
          if (activeSubScreen == null) {
            HurufiaBottomNavigationBar(
              currentTab = currentBottomTab,
              selectedLang = selectedLang,
              onTabSelected = { tab ->
                soundManager.playSuccessChime()
                currentBottomTab = tab
              }
            )
          }
        }
      ) { persistentScaffoldPadding ->
        Box(
          modifier = Modifier
            .fillMaxSize()
            .padding(
              top = if (activeSubScreen == null) persistentScaffoldPadding.calculateTopPadding() else 0.dp,
              bottom = if (activeSubScreen == null) persistentScaffoldPadding.calculateBottomPadding() else 0.dp
            )
        ) {
          Crossfade(
            targetState = currentBottomTab,
            animationSpec = tween(durationMillis = 180),
            label = "main_tabs_crossfade"
          ) { tab ->
            when (tab) {
              MainAppTab.HOME -> {
                when (activeSubScreen) {
                  HomeSubScreen.ARABIC_ALPHABET -> {
                    ArabicAlphabetFullScreen(
                      selectedLang = selectedLang,
                      soundManager = soundManager,
                      onBack = { activeSubScreen = null }
                    )
                  }
                  HomeSubScreen.GAME_SELECTOR -> {
                    GameSelectorFullScreen(
                      selectedLang = selectedLang,
                      soundManager = soundManager,
                      onBack = { activeSubScreen = null },
                      onSelectGame = { gameMode ->
                        activeGameModal = gameMode
                      }
                    )
                  }
                  HomeSubScreen.KAIDA_EDUCATION -> {
                    KaidaEducationScreen(
                      selectedLang = selectedLang,
                      onLanguageChange = onLanguageChange,
                      soundManager = soundManager,
                      onBackToDashboard = { activeSubScreen = null }
                    )
                  }
                  HomeSubScreen.AMPARA_SURAHS -> {
                    AmparaSurahScreen(
                      selectedLang = selectedLang,
                      onLanguageChange = onLanguageChange,
                      soundManager = soundManager,
                      onBackToDashboard = { activeSubScreen = null }
                    )
                  }
                  HomeSubScreen.ASMA_UL_HUSNA -> {
                    AsmaUlHusnaFullScreen(
                      selectedLang = selectedLang,
                      onBack = { activeSubScreen = null }
                    )
                  }
                  HomeSubScreen.SALAT_GUIDE -> {
                    SalatGuideScreen(
                      selectedLang = selectedLang,
                      onBack = { activeSubScreen = null }
                    )
                  }
                  HomeSubScreen.HADITH_COLLECTION -> {
                    HadithCollectionScreen(
                      selectedLang = selectedLang,
                      onBack = { activeSubScreen = null }
                    )
                  }
                  null -> {
                    Column(
                      modifier = Modifier
                        .fillMaxSize()
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
                          // 1. PRAYER TIME CARD (SALAT TIME) WITH ACTIVE LIVE COUNTDOWN & AZAN TOGGLE
                          PrayerTimeCard(
                            prayerSchedule = prayerSchedule,
                            selectedLang = selectedLang,
                            offlineRepo = islamicOfflineRepo,
                            onOpenFullTimetable = { showFullTimetableDialog = true },
                            onMuteToggle = { muted ->
                              isAzanMuted = muted
                              islamicOfflineRepo.isAzanMuted = muted
                              if (muted) soundManager.playTone(android.media.ToneGenerator.TONE_PROP_NACK)
                              else soundManager.playSuccessChime()
                            },
                            onRefreshLocation = {
                              showLocationSettingsDialog = true
                            },
                            modifier = Modifier.fillMaxWidth()
                          )

                          Spacer(modifier = Modifier.height(14.dp))

                          // 2. INTERACTIVE CAROUSEL SLIDER (AD & CONTENT CORNER)
                          InteractiveContentCarousel(
                            selectedLang = selectedLang,
                            onSlideClick = { slide ->
                              when (slide) {
                                is CarouselSlide.ShapeMaster -> {
                                  soundManager.playSuccessChime()
                                  activeSubScreen = HomeSubScreen.KAIDA_EDUCATION
                                }
                                is CarouselSlide.MakhrajVisualizer -> {
                                  soundManager.playSuccessChime()
                                  letterForMakhrajVisualizer = ArabicAlphabetRepository.letters.first()
                                }
                                is CarouselSlide.DailyDua -> {
                                  soundManager.playSuccessChime()
                                  selectedDuaForDialog = HadithRepository.dailyDuas.first()
                                }
                                is CarouselSlide.VirtuousDeed -> {
                                  soundManager.playSuccessChime()
                                  selectedDeedForDialog = HadithRepository.dailyDeeds.first()
                                }
                                is CarouselSlide.ProPromotion -> {
                                  soundManager.playSuccessChime()
                                  showOfflineManagerDialog = true
                                }
                              }
                            },
                            modifier = Modifier.fillMaxWidth()
                          )

                          Spacer(modifier = Modifier.height(18.dp))

                          // 3. MAIN GRID MENU HEADER
                          Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                          ) {
                            Text(
                              text = if (selectedLang == "BN") "প্রধান ফিচারসমূহ" else "Main Features",
                              style = TextStyle(
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1E293B)
                              )
                            )
                            Text(
                              text = if (selectedLang == "BN") "৬টি বিভাগ" else "6 Categories",
                              style = TextStyle(
                                fontSize = 12.sp,
                                color = Color(0xFF64748B),
                                fontWeight = FontWeight.Medium
                              )
                            )
                          }

                          Spacer(modifier = Modifier.height(10.dp))

                          // 4. MAIN GRID MENU (2 COLUMNS, 6 FEATURE CARDS)
                          MainGridMenu(
                            selectedLang = selectedLang,
                            onItemClick = { itemId ->
                              soundManager.playSuccessChime()
                              when (itemId) {
                                "arbi_horof" -> activeSubScreen = HomeSubScreen.ARABIC_ALPHABET
                                "game_mode" -> activeSubScreen = HomeSubScreen.GAME_SELECTOR
                                "kaida_education", "quran_learning" -> activeSubScreen = HomeSubScreen.KAIDA_EDUCATION
                                "ampara_surahs" -> activeSubScreen = HomeSubScreen.AMPARA_SURAHS
                                "asmaul_husna" -> activeSubScreen = HomeSubScreen.ASMA_UL_HUSNA
                                "salat_guide" -> activeSubScreen = HomeSubScreen.SALAT_GUIDE
                                "hadith_collection" -> activeSubScreen = HomeSubScreen.HADITH_COLLECTION
                              }
                            },
                            modifier = Modifier.fillMaxWidth()
                          )

                          Spacer(modifier = Modifier.height(28.dp))
                        }
                      }
                    }
                  }
                }
              MainAppTab.CALENDAR -> {
                MultiCalendarScreen(
                  selectedLang = selectedLang,
                  dayAdjustment = islamicOfflineRepo.hijriDayAdjustment,
                  onDayAdjustmentChange = { adj ->
                    islamicOfflineRepo.hijriDayAdjustment = adj
                  }
                )
              }
              MainAppTab.QIBLA -> {
                QiblaCompassScreen(
                  latitude = islamicOfflineRepo.latitude,
                  longitude = islamicOfflineRepo.longitude,
                  locationName = islamicOfflineRepo.locationName,
                  selectedLang = selectedLang,
                  onRequestGps = {
                    if (locationService.hasLocationPermission()) {
                      locationService.fetchCurrentLocation(
                        onSuccess = { lat, lng, name, tz, isGps ->
                          islamicOfflineRepo.saveLocation(lat, lng, name, tz, isGps)
                          userLocationTrigger++
                          soundManager.playSuccessChime()
                        },
                        onFailure = {}
                      )
                    } else {
                      locationPermissionLauncher.launch(
                        arrayOf(
                          android.Manifest.permission.ACCESS_FINE_LOCATION,
                          android.Manifest.permission.ACCESS_COARSE_LOCATION
                        )
                      )
                    }
                  }
                )
              }
              MainAppTab.PROFILE -> {
                SettingsTabScreen(
                  selectedLang = selectedLang,
                  onLanguageChange = onLanguageChange,
                  userEmail = userEmail,
                  userLevel = userLevel,
                  userCoins = userCoins,
                  userStreak = userStreak,
                  soundManager = soundManager,
                  onOpenMakhrajVisualizer = {
                    letterForMakhrajVisualizer = ArabicAlphabetRepository.letters.first()
                  },
                  onSignOut = onSignOut
                )
              }
            }
          }
        }
      }
    }
  }

  // Detail Pop-up modal removed in favor of 5-Tab Arabic Alphabet Studio

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

  // Islamic & Utility Dialogs
  if (showFullTimetableDialog) {
    FullTimetableDialog(
      prayerSchedule = prayerSchedule,
      selectedLang = selectedLang,
      onDismiss = { showFullTimetableDialog = false }
    )
  }

  selectedDuaForDialog?.let { dua ->
    DailyDuaDialog(
      dua = dua,
      selectedLang = selectedLang,
      onDismiss = { selectedDuaForDialog = null }
    )
  }

  selectedDeedForDialog?.let { deed ->
    VirtuousDeedDialog(
      deed = deed,
      selectedLang = selectedLang,
      onDismiss = { selectedDeedForDialog = null }
    )
  }

  if (showLearningProgressDialog) {
    LearningProgressDialog(
      progressRepo = progressRepo,
      selectedLang = selectedLang,
      onDismiss = { showLearningProgressDialog = false }
    )
  }

  if (showAzanSettingsDialog) {
    AzanNotificationSettingsDialog(
      offlineRepo = islamicOfflineRepo,
      selectedLang = selectedLang,
      onDismiss = { showAzanSettingsDialog = false }
    )
  }

  if (showOfflineManagerDialog) {
    OfflineManagerDialog(
      selectedLang = selectedLang,
      onDismiss = { showOfflineManagerDialog = false }
    )
  }

  if (showPrivacyPolicyDialog) {
    PrivacyPolicyDialog(
      selectedLang = selectedLang,
      onDismiss = { showPrivacyPolicyDialog = false }
    )
  }

  if (showLanguageDialog) {
    LanguageSelectionDialog(
      selectedLang = selectedLang,
      onLanguageSelected = { newLang ->
        onLanguageChange(newLang)
      },
      onDismiss = { showLanguageDialog = false }
    )
  }

  if (showLocationSettingsDialog) {
    LocationSettingsDialog(
      selectedLang = selectedLang,
      offlineRepo = islamicOfflineRepo,
      locationService = locationService,
      soundManager = soundManager,
      onLocationUpdated = {
        userLocationTrigger++
      },
      onDismiss = { showLocationSettingsDialog = false }
    )
  }

  if (showAudioQariDialog) {
    AudioQariSelectionDialog(
      soundManager = soundManager,
      selectedLang = selectedLang,
      onDismiss = { showAudioQariDialog = false }
    )
  }

  if (showFeedbackDialog) {
    FeedbackReportDialog(
      selectedLang = selectedLang,
      onDismiss = { showFeedbackDialog = false }
    )
  }

  if (showDeveloperDialog) {
    Dialog(onDismissRequest = { showDeveloperDialog = false }) {
      DeveloperProfileCard(
        selectedLang = selectedLang,
        modifier = Modifier
          .fillMaxWidth()
          .widthIn(max = 460.dp)
      )
    }
  }
}



/**
 * Full screen for Game Mode selector when tapped from Home Grid
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameSelectorFullScreen(
  selectedLang: String,
  soundManager: SoundManager,
  onBack: () -> Unit,
  onSelectGame: (GameMode) -> Unit
) {
  Scaffold(
    containerColor = BackgroundGray,
    topBar = {
      TopAppBar(
        title = {
          Text(
            text = when (selectedLang) {
              "EN" -> "Select Game Mode"
              "AR" -> "أنماط الألعاب التعليمية"
              else -> "গেম মোড সমূহ"
            },
            style = TextStyle(
              fontWeight = FontWeight.Bold,
              color = Color.White,
              fontSize = 18.sp
            )
          )
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
        colors = TopAppBarDefaults.topAppBarColors(containerColor = RoyalEmerald)
      )
    }
  ) { padding ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(padding)
        .verticalScroll(rememberScrollState())
        .padding(16.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Text(
        text = when (selectedLang) {
          "EN" -> "5 Interactive Gamified Learning Modes"
          "AR" -> "٥ أنماط تفاعلية لتعلم الحروف والتجويد"
          else -> "৫টি আকর্ষণীয় শিক্ষামূলক গেম ও কুইজ"
        },
        style = TextStyle(
          fontSize = 16.sp,
          fontWeight = FontWeight.Bold,
          color = RoyalEmerald
        ),
        modifier = Modifier.fillMaxWidth()
      )

      Spacer(modifier = Modifier.height(4.dp))

      Text(
        text = when (selectedLang) {
          "EN" -> "Play, earn stars, and master Arabic letters effortlessly!"
          "AR" -> "العب واجمع النجوم وأتقن الحروف العربية بسهولة!"
          else -> "খেলুন, পয়েন্ট অর্জন করুন এবং সহজে আরবি হরফ শিখুন!"
        },
        style = TextStyle(fontSize = 13.sp, color = Color(0xFF64748B)),
        modifier = Modifier.fillMaxWidth()
      )

      Spacer(modifier = Modifier.height(16.dp))

      GameRepository.gameModes.forEach { gameMode ->
        GameModeCard3D(
          gameMode = gameMode,
          selectedLang = selectedLang,
          onPlayClick = {
            soundManager.playSuccessChime()
            onSelectGame(gameMode)
          },
          modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(14.dp))
      }

      Spacer(modifier = Modifier.height(20.dp))
    }
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
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(
      containerColor = Color.White
    ),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
    modifier = modifier
      .shadow(2.dp, RoundedCornerShape(16.dp), spotColor = Color(0x14000000))
      .testTag("user_stats_bar")
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
            "EN" -> "Daily Goal & Level Progress"
            "AR" -> "الهدف اليومي وتقدم المستوى"
            else -> "দৈনিক লক্ষ্য ও লেভেল অগ্রগতি"
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

      Spacer(modifier = Modifier.height(5.dp))

      LinearProgressIndicator(
        progress = { 0.64f },
        modifier = Modifier
          .fillMaxWidth()
          .height(7.dp)
          .clip(RoundedCornerShape(4.dp)),
        color = RoyalEmerald,
        trackColor = Color(0xFFF1F5F9)
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
 * Ultra-crisp Islamic Geometric Art vector pattern overlay for banners
 */
@Composable
fun IslamicGeometricOverlay(
  modifier: Modifier = Modifier,
  patternColor: Color = Color(0xFFF6E05E).copy(alpha = 0.16f)
) {
  Canvas(modifier = modifier) {
    val step = 44.dp.toPx()
    val starRadius = 14.dp.toPx()
    val numCols = (size.width / step).toInt() + 2
    val numRows = (size.height / step).toInt() + 2

    for (c in 0..numCols) {
      for (r in 0..numRows) {
        val cx = c * step + (if (r % 2 == 1) step / 2f else 0f)
        val cy = r * step

        // Square 1
        drawRect(
          color = patternColor,
          topLeft = Offset(cx - starRadius / 2f, cy - starRadius / 2f),
          size = androidx.compose.ui.geometry.Size(starRadius, starRadius),
          style = Stroke(width = 1.2.dp.toPx())
        )
        // Square 2 (rotated 45 degrees)
        val halfD = (starRadius / 2f) * 1.414f
        val pathRotated = Path().apply {
          moveTo(cx, cy - halfD)
          lineTo(cx + halfD, cy)
          lineTo(cx, cy + halfD)
          lineTo(cx - halfD, cy)
          close()
        }
        drawPath(
          path = pathRotated,
          color = patternColor,
          style = Stroke(width = 1.2.dp.toPx())
        )
        // Center focal star dot
        drawCircle(
          color = patternColor.copy(alpha = patternColor.alpha * 0.8f),
          radius = 1.8.dp.toPx(),
          center = Offset(cx, cy)
        )
      }
    }
  }
}

/**
 * Hero Banner with glossy gradient, subtle Islamic geometric art & CTA button
 */
@Composable
fun DashboardHeroBanner(
  selectedLang: String,
  onPracticeClick: () -> Unit
) {
  Card(
    shape = RoundedCornerShape(20.dp),
    colors = CardDefaults.cardColors(containerColor = RoyalEmerald),
    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    border = BorderStroke(1.dp, Color(0xFFF6E05E).copy(alpha = 0.4f)),
    modifier = Modifier
      .fillMaxWidth()
      .shadow(6.dp, RoundedCornerShape(20.dp), spotColor = Color(0x350A5C36))
  ) {
    Box(modifier = Modifier.fillMaxWidth()) {
      // Glossy Gradient Background
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .background(
            Brush.linearGradient(
              colors = listOf(
                Color(0xFF063A22),
                Color(0xFF0A5C36),
                Color(0xFF0F7645)
              )
            )
          )
      )

      // Background Hero Graphic
      Image(
        painter = painterResource(id = R.drawable.img_dashboard_hero),
        contentDescription = "Dashboard Banner",
        modifier = Modifier
          .fillMaxWidth()
          .height(180.dp),
        contentScale = ContentScale.Crop,
        alpha = 0.22f
      )

      // Subtle Islamic geometric art pattern overlay
      IslamicGeometricOverlay(
        modifier = Modifier
          .fillMaxWidth()
          .height(180.dp),
        patternColor = Color(0xFFF6E05E).copy(alpha = 0.16f)
      )

      // Glossy Light Sheen
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(180.dp)
          .background(
            Brush.verticalGradient(
              colors = listOf(
                Color.White.copy(alpha = 0.14f),
                Color.Transparent,
                Color.Black.copy(alpha = 0.22f)
              )
            )
          )
      )

      // Banner Text & Prominent CTA Content
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(18.dp)
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween,
          modifier = Modifier.fillMaxWidth()
        ) {
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(8.dp))
              .background(Color(0xFFF6E05E).copy(alpha = 0.22f))
              .padding(horizontal = 8.dp, vertical = 3.dp)
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(text = "✨", fontSize = 12.sp)
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = when (selectedLang) {
                  "EN" -> "Daily Arabic Mastery"
                  "AR" -> "إتقان العربية اليومي"
                  else -> "দৈনিক আরবি শিক্ষা"
                },
                style = TextStyle(
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color(0xFFF6E05E)
                )
              )
            }
          }

          Text(
            text = "📖 29 حروف",
            style = TextStyle(
              fontSize = 11.sp,
              fontWeight = FontWeight.SemiBold,
              color = Color.White.copy(alpha = 0.88f)
            )
          )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
          text = when (selectedLang) {
            "EN" -> "Master Arabic Letter Forms with Play"
            "AR" -> "أتقن أشكال الحروف العربية باللعب والتشويق"
            else -> "খেলার ছলে শিখুন আরবি হরফের রূপভেদ"
          },
          style = TextStyle(
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            letterSpacing = 0.3.sp
          )
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
          text = when (selectedLang) {
            "EN" -> "Step-by-step Quran reading journey from letters to full Ayahs."
            "AR" -> "رحلة متدرجة لقراءة القرآن الكريم من الحروف حتى الآيات الكاملة."
            else -> "ধাপে ধাপে হরফ থেকে শুরু করে পূর্ণাঙ্গ আয়াত তিলাওয়াত পর্যন্ত যাত্রা।"
          },
          style = TextStyle(
            fontSize = 11.5.sp,
            color = Color.White.copy(alpha = 0.92f)
          ),
          maxLines = 2
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Prominent Call-to-action Button: "আজকের হরফ প্র্যাকটিস করুন"
        Button(
          onClick = onPracticeClick,
          colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFF6E05E),
            contentColor = Color(0xFF063A22)
          ),
          shape = RoundedCornerShape(12.dp),
          elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp, pressedElevation = 8.dp),
          modifier = Modifier
            .testTag("hero_practice_letters_cta")
            .shadow(4.dp, RoundedCornerShape(12.dp), spotColor = Color(0x60F6E05E))
        ) {
          Icon(
            imageVector = Icons.Default.PlayArrow,
            contentDescription = null,
            tint = Color(0xFF063A22),
            modifier = Modifier.size(18.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = when (selectedLang) {
              "EN" -> "Practice Today's Letters ➔"
              "AR" -> "تدرّب على حروف اليوم ➔"
              else -> "আজকের হরফ প্র্যাকটিস করুন ➔"
            },
            style = TextStyle(
              fontSize = 13.sp,
              fontWeight = FontWeight.Bold,
              color = Color(0xFF063A22)
            )
          )
        }
      }
    }
  }
}

/**
 * Modern Gamified Mode Card with rich vibrant gradients, clean micro-shadows & distinct icon containers
 */
@Composable
fun GameModeCard3D(
  gameMode: GameMode,
  selectedLang: String,
  onPlayClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Card(
    shape = RoundedCornerShape(18.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    elevation = CardDefaults.cardElevation(
      defaultElevation = 2.dp,
      pressedElevation = 6.dp
    ),
    border = BorderStroke(1.dp, gameMode.primaryColor.copy(alpha = 0.22f)),
    modifier = modifier
      .shadow(3.dp, RoundedCornerShape(18.dp), spotColor = gameMode.primaryColor.copy(alpha = 0.28f))
      .clickable { onPlayClick() }
      .testTag("game_card_${gameMode.type.name}")
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .background(
          Brush.linearGradient(
            colors = listOf(
              gameMode.primaryColor.copy(alpha = 0.07f),
              Color.White,
              gameMode.secondaryColor.copy(alpha = 0.03f)
            )
          )
        )
        .padding(15.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
      ) {
        // Distinct High-Contrast 3D Icon Container
        Box(
          modifier = Modifier
            .size(56.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(
              Brush.linearGradient(
                colors = listOf(gameMode.primaryColor, gameMode.secondaryColor)
              )
            )
            .border(2.dp, Color.White.copy(alpha = 0.65f), RoundedCornerShape(16.dp))
            .shadow(4.dp, RoundedCornerShape(16.dp), spotColor = gameMode.primaryColor.copy(alpha = 0.4f)),
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
            // Badge capsule
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(gameMode.primaryColor)
                .padding(horizontal = 7.dp, vertical = 2.dp)
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

            // Star Rating
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
              fontSize = 16.sp,
              fontWeight = FontWeight.Bold,
              color = Color(0xFF0F172A)
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
          )

          Text(
            text = gameMode.getSubtitle(selectedLang),
            style = TextStyle(
              fontSize = 11.5.sp,
              color = Color(0xFF64748B),
              lineHeight = 15.sp
            ),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
          )
        }

        Spacer(modifier = Modifier.width(10.dp))

        // Vibrant Play Button Pill
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(
              Brush.horizontalGradient(
                listOf(gameMode.primaryColor, gameMode.secondaryColor)
              )
            )
            .clickable { onPlayClick() }
            .padding(horizontal = 11.dp, vertical = 8.dp),
          contentAlignment = Alignment.Center
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = when (selectedLang) {
                "EN" -> "PLAY"
                "AR" -> "العب"
                else -> "খেলুন"
              },
              style = TextStyle(
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                letterSpacing = 0.5.sp
              )
            )
            Spacer(modifier = Modifier.width(3.dp))
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowForward,
              contentDescription = null,
              tint = Color.White,
              modifier = Modifier.size(12.dp)
            )
          }
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
        ),
        textAlign = TextAlign.Center
      )

      Spacer(modifier = Modifier.height(2.dp))

      CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
        Text(
          text = letter.getName(selectedLang),
          style = TextStyle(
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF1E293B)
          ),
          textAlign = TextAlign.Center,
          modifier = Modifier.fillMaxWidth(),
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )

        Text(
          text = letter.phonetic,
          style = TextStyle(
            fontSize = 10.sp,
            color = Color(0xFF64748B)
          ),
          textAlign = TextAlign.Center,
          modifier = Modifier.fillMaxWidth()
        )
      }
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
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    elevation = CardDefaults.cardElevation(
      defaultElevation = 2.5.dp,
      pressedElevation = 6.dp
    ),
    border = BorderStroke(1.2.dp, Color(0xFFE2E8F0)),
    modifier = Modifier
      .size(64.dp)
      .shadow(2.dp, RoundedCornerShape(16.dp), spotColor = RoyalEmerald.copy(alpha = 0.18f))
      .clickable { onClick() }
      .testTag("letter_grid_${letter.id}")
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 2.dp, vertical = 3.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center
    ) {
      Text(
        text = letter.letter,
        style = TextStyle(
          fontSize = 25.sp,
          fontWeight = FontWeight.Bold,
          color = RoyalEmerald,
          fontFamily = FontFamily.Serif
        ),
        textAlign = TextAlign.Center
      )
      Spacer(modifier = Modifier.height(1.dp))
      CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
        Text(
          text = letter.getName(selectedLang),
          style = TextStyle(
            fontSize = 9.5.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF334155)
          ),
          textAlign = TextAlign.Center,
          modifier = Modifier.fillMaxWidth(),
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )
      }
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AsmaUlHusnaFullScreen(
  selectedLang: String,
  onBack: () -> Unit
) {
  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Text(
            text = if (selectedLang == "BN") "আল্লাহর ৯৯টি গুণবাচক নাম" else "99 Names of Allah",
            style = TextStyle(color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
          )
        },
        navigationIcon = {
          IconButton(onClick = onBack) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = RoyalEmerald)
      )
    }
  ) { padding ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(padding)
    ) {
      AsmaUlHusnaScreen(selectedLang = selectedLang)
    }
  }
}
