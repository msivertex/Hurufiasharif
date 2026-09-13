package com.example.islamic

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CompassCalibration
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Mosque
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.RoyalEmerald
import java.util.Calendar
import java.util.Locale

/**
 * Islamic Utility Active View Screen enum
 */
enum class IslamicSuiteTab(val titleEn: String, val titleBn: String, val iconEmoji: String) {
  PRAYER_TIMES("Salah Times", "নামাজের সময়", "🕌"),
  QIBLA_COMPASS("Qibla Compass", "কিবলা কম্পাস", "🧭"),
  HIJRI_CALENDAR("Hijri Calendar", "হিজরি ক্যালেন্ডার", "📅"),
  ASMA_UL_HUSNA("99 Names", "আসমাউল হুসনা", "💎")
}

/**
 * Compact "Next Prayer" Widget for Home Dashboard
 */
@Composable
fun NextPrayerCompactWidget(
  selectedLang: String,
  onOpenSuite: (IslamicSuiteTab) -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val offlineRepo = remember { IslamicOfflineRepository.getInstance(context) }
  val locationService = remember { IslamicLocationService(context) }

  var latitude by remember { mutableDoubleStateOf(offlineRepo.latitude) }
  var longitude by remember { mutableDoubleStateOf(offlineRepo.longitude) }
  var timezoneHours by remember { mutableDoubleStateOf(offlineRepo.timezoneHours) }
  var locationName by remember { mutableStateOf(offlineRepo.locationName) }
  var method by remember { mutableStateOf(offlineRepo.calculationMethod) }
  var juristicMethod by remember { mutableStateOf(offlineRepo.juristicMethod) }

  // Auto-fetch GPS on initial composition if permission already granted
  LaunchedEffect(Unit) {
    if (locationService.hasLocationPermission()) {
      locationService.fetchCurrentLocation(
        onSuccess = { lat, lng, name, tz ->
          latitude = lat
          longitude = lng
          locationName = name
          timezoneHours = tz
          offlineRepo.saveLocation(lat, lng, name, tz)
        },
        onFailure = {}
      )
    }
  }

  val prayerSchedule = remember(latitude, longitude, timezoneHours, method, juristicMethod) {
    PrayerTimesCalculator.calculate(
      latitude = latitude,
      longitude = longitude,
      timezoneOffset = timezoneHours,
      locationName = locationName,
      method = method,
      juristicMethod = juristicMethod
    )
  }

  val hijriDate = remember {
    HijriCalendarCalculator.calculateHijri()
  }

  Card(
    shape = RoundedCornerShape(18.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
    border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
    modifier = modifier
      .fillMaxWidth()
      .shadow(4.dp, RoundedCornerShape(18.dp), spotColor = Color(0x18000000))
      .testTag("next_prayer_widget")
  ) {
    Column(modifier = Modifier.fillMaxWidth()) {
      // Top Emerald Header
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .background(
            Brush.horizontalGradient(
              colors = listOf(
                Color(0xFF063A22),
                Color(0xFF0A5C36),
                Color(0xFF15803D)
              )
            )
          )
          .padding(horizontal = 14.dp, vertical = 10.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "🌙", fontSize = 16.sp)
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = hijriDate.format(selectedLang),
              style = TextStyle(
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
              )
            )
          }

          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
              .clip(RoundedCornerShape(8.dp))
              .background(Color.White.copy(alpha = 0.15f))
              .padding(horizontal = 8.dp, vertical = 3.dp)
          ) {
            Icon(
              imageVector = Icons.Default.LocationOn,
              contentDescription = "Location",
              tint = Color(0xFFFDE047),
              modifier = Modifier.size(12.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = locationName.take(18),
              style = TextStyle(
                fontSize = 10.5.sp,
                color = Color.White,
                fontWeight = FontWeight.Medium
              ),
              maxLines = 1,
              overflow = TextOverflow.Ellipsis
            )
          }
        }
      }

      // Main Prayer Info Body
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = when (selectedLang) {
              "EN" -> "Next Prayer: ${prayerSchedule.nextPrayer.nameEn}"
              "AR" -> "الصلاة القادمة: ${prayerSchedule.nextPrayer.nameAr}"
              else -> "পরবর্তী সালাত: ${prayerSchedule.nextPrayer.nameBn}"
            },
            style = TextStyle(
              fontSize = 15.sp,
              fontWeight = FontWeight.Bold,
              color = Color(0xFF0F172A)
            )
          )

          Spacer(modifier = Modifier.height(2.dp))

          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = prayerSchedule.nextPrayer.timeFormatted,
              style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.Black,
                color = RoyalEmerald
              )
            )

            Spacer(modifier = Modifier.width(10.dp))

            Surface(
              shape = RoundedCornerShape(8.dp),
              color = Color(0xFFFEF3C7),
              border = BorderStroke(1.dp, Color(0xFFFDE68A))
            ) {
              Row(
                modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(text = "⏳", fontSize = 11.sp)
                Spacer(modifier = Modifier.width(4.dp))
                val hrs = prayerSchedule.timeUntilNextMinutes / 60
                val mins = prayerSchedule.timeUntilNextMinutes % 60
                val remainingText = when (selectedLang) {
                  "EN" -> if (hrs > 0) "${hrs}h ${mins}m left" else "${mins}m left"
                  "AR" -> if (hrs > 0) "بقي $hrs س $mins د" else "بقي $mins د"
                  else -> if (hrs > 0) "আর ${hrs} ঘণ্টা ${mins} মিনিট" else "আর ${mins} মিনিট"
                }
                Text(
                  text = remainingText,
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

        // Action button to full Salah Timings
        Button(
          onClick = { onOpenSuite(IslamicSuiteTab.PRAYER_TIMES) },
          colors = ButtonDefaults.buttonColors(containerColor = RoyalEmerald),
          shape = RoundedCornerShape(12.dp),
          contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
          modifier = Modifier.testTag("view_all_prayers_btn")
        ) {
          Text(
            text = when (selectedLang) {
              "EN" -> "Schedule"
              "AR" -> "المواقيت"
              else -> "সময়সূচি"
            },
            style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold)
          )
        }
      }

      // 4 Quick Islamic Utility Hub Buttons
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .background(Color(0xFFF8FAFC))
          .padding(horizontal = 10.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceAround
      ) {
        QuickUtilityItem(
          emoji = "🕌",
          title = when (selectedLang) { "EN" -> "Salah"; "AR" -> "الصلاة"; else -> "সালাত" },
          onClick = { onOpenSuite(IslamicSuiteTab.PRAYER_TIMES) },
          tag = "quick_salah_btn"
        )
        QuickUtilityItem(
          emoji = "🧭",
          title = when (selectedLang) { "EN" -> "Qibla"; "AR" -> "القبلة"; else -> "কিবলা" },
          onClick = { onOpenSuite(IslamicSuiteTab.QIBLA_COMPASS) },
          tag = "quick_qibla_btn"
        )
        QuickUtilityItem(
          emoji = "📅",
          title = when (selectedLang) { "EN" -> "Calendar"; "AR" -> "التقويم"; else -> "ক্যালেন্ডার" },
          onClick = { onOpenSuite(IslamicSuiteTab.HIJRI_CALENDAR) },
          tag = "quick_hijri_btn"
        )
        QuickUtilityItem(
          emoji = "💎",
          title = when (selectedLang) { "EN" -> "99 Names"; "AR" -> "الأسماء"; else -> "আসমাউল হুসনা" },
          onClick = { onOpenSuite(IslamicSuiteTab.ASMA_UL_HUSNA) },
          tag = "quick_asma_btn"
        )
      }
    }
  }
}

@Composable
private fun QuickUtilityItem(
  emoji: String,
  title: String,
  onClick: () -> Unit,
  tag: String
) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier
      .clip(RoundedCornerShape(10.dp))
      .clickable { onClick() }
      .padding(horizontal = 8.dp, vertical = 4.dp)
      .testTag(tag)
  ) {
    Box(
      modifier = Modifier
        .size(34.dp)
        .clip(RoundedCornerShape(10.dp))
        .background(Color.White)
        .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(10.dp)),
      contentAlignment = Alignment.Center
    ) {
      Text(text = emoji, fontSize = 16.sp)
    }
    Spacer(modifier = Modifier.height(3.dp))
    Text(
      text = title,
      style = TextStyle(
        fontSize = 10.5.sp,
        fontWeight = FontWeight.Medium,
        color = Color(0xFF334155)
      )
    )
  }
}

/**
 * Main Islamic Utility Suite Container Screen
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IslamicSuiteScreen(
  initialTab: IslamicSuiteTab = IslamicSuiteTab.PRAYER_TIMES,
  selectedLang: String,
  showBackButton: Boolean = false,
  onBack: (() -> Unit)? = null
) {
  var currentTab by remember { mutableStateOf(initialTab) }
  val context = LocalContext.current
  val offlineRepo = remember { IslamicOfflineRepository.getInstance(context) }
  val locationService = remember { IslamicLocationService(context) }

  var latitude by remember { mutableDoubleStateOf(offlineRepo.latitude) }
  var longitude by remember { mutableDoubleStateOf(offlineRepo.longitude) }
  var timezoneHours by remember { mutableDoubleStateOf(offlineRepo.timezoneHours) }
  var locationName by remember { mutableStateOf(offlineRepo.locationName) }
  var calculationMethod by remember { mutableStateOf(offlineRepo.calculationMethod) }
  var juristicMethod by remember { mutableStateOf(offlineRepo.juristicMethod) }
  var dayAdjustment by remember { mutableIntStateOf(offlineRepo.hijriDayAdjustment) }
  var isGpsLoading by remember { mutableStateOf(false) }
  var showCityDialog by remember { mutableStateOf(false) }

  // Location Permission Launcher
  val locationPermissionLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.RequestMultiplePermissions()
  ) { permissions ->
    val granted = permissions[android.Manifest.permission.ACCESS_FINE_LOCATION] == true ||
      permissions[android.Manifest.permission.ACCESS_COARSE_LOCATION] == true
    if (granted) {
      isGpsLoading = true
      locationService.fetchCurrentLocation(
        onSuccess = { lat, lng, name, tz ->
          latitude = lat
          longitude = lng
          locationName = name
          timezoneHours = tz
          offlineRepo.saveLocation(lat, lng, name, tz)
          isGpsLoading = false
        },
        onFailure = { isGpsLoading = false }
      )
    }
  }

  fun requestGps() {
    if (locationService.hasLocationPermission()) {
      isGpsLoading = true
      locationService.fetchCurrentLocation(
        onSuccess = { lat, lng, name, tz ->
          latitude = lat
          longitude = lng
          locationName = name
          timezoneHours = tz
          offlineRepo.saveLocation(lat, lng, name, tz)
          isGpsLoading = false
        },
        onFailure = { isGpsLoading = false }
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

  Scaffold(
    containerColor = Color(0xFFF1F5F9),
    topBar = {
      TopAppBar(
        title = {
          Column {
            Text(
              text = when (selectedLang) {
                "EN" -> "Islamic Corner"
                "AR" -> "الركن الإسلامي"
                else -> "ইসলামিক কর্নার"
              },
              style = TextStyle(
                fontWeight = FontWeight.Bold,
                color = Color.White,
                fontSize = 18.sp
              )
            )
            Text(
              text = "📍 $locationName",
              style = TextStyle(color = Color.White.copy(alpha = 0.85f), fontSize = 11.sp),
              maxLines = 1,
              overflow = TextOverflow.Ellipsis
            )
          }
        },
        navigationIcon = {
          if (showBackButton && onBack != null) {
            IconButton(
              onClick = onBack,
              modifier = Modifier.testTag("islamic_suite_back_btn")
            ) {
              Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Color.White
              )
            }
          }
        },
        actions = {
          IconButton(
            onClick = { requestGps() },
            modifier = Modifier.testTag("suite_gps_btn")
          ) {
            Icon(
              imageVector = Icons.Default.MyLocation,
              contentDescription = "GPS Location",
              tint = if (isGpsLoading) Color(0xFFFDE047) else Color.White
            )
          }
          IconButton(
            onClick = { showCityDialog = true },
            modifier = Modifier.testTag("suite_city_picker_btn")
          ) {
            Icon(
              imageVector = Icons.Default.LocationOn,
              contentDescription = "Select City",
              tint = Color.White
            )
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = RoyalEmerald)
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
    ) {
      // 4-Tab Navigation Bar
      TabRow(
        selectedTabIndex = currentTab.ordinal,
        containerColor = Color.White,
        contentColor = RoyalEmerald,
        indicator = { tabPositions ->
          TabRowDefaults.SecondaryIndicator(
            modifier = Modifier.tabIndicatorOffset(tabPositions[currentTab.ordinal]),
            color = RoyalEmerald,
            height = 3.dp
          )
        }
      ) {
        IslamicSuiteTab.values().forEach { tab ->
          val isSelected = currentTab == tab
          Tab(
            selected = isSelected,
            onClick = { currentTab = tab },
            text = {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = tab.iconEmoji, fontSize = 13.sp)
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                  text = if (selectedLang == "BN") tab.titleBn else tab.titleEn,
                  style = TextStyle(
                    fontSize = 11.5.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    color = if (isSelected) RoyalEmerald else Color(0xFF64748B)
                  ),
                  maxLines = 1
                )
              }
            },
            modifier = Modifier.testTag("tab_${tab.name.lowercase()}")
          )
        }
      }

      // Screen Body
      when (currentTab) {
        IslamicSuiteTab.PRAYER_TIMES -> {
          SalahTimingsScreen(
            latitude = latitude,
            longitude = longitude,
            timezoneHours = timezoneHours,
            locationName = locationName,
            method = calculationMethod,
            juristicMethod = juristicMethod,
            selectedLang = selectedLang,
            onMethodChange = { calculationMethod = it },
            onJuristicChange = { juristicMethod = it },
            onRequestGps = { requestGps() },
            onOpenCityPicker = { showCityDialog = true }
          )
        }
        IslamicSuiteTab.QIBLA_COMPASS -> {
          QiblaCompassScreen(
            latitude = latitude,
            longitude = longitude,
            locationName = locationName,
            selectedLang = selectedLang,
            onRequestGps = { requestGps() }
          )
        }
        IslamicSuiteTab.HIJRI_CALENDAR -> {
          HijriCalendarScreen(
            selectedLang = selectedLang,
            dayAdjustment = dayAdjustment,
            onAdjustmentChange = { dayAdjustment = it }
          )
        }
        IslamicSuiteTab.ASMA_UL_HUSNA -> {
          AsmaUlHusnaScreen(selectedLang = selectedLang)
        }
      }
    }

    // City Selection Dialog
    if (showCityDialog) {
      CitySelectionDialog(
        selectedLang = selectedLang,
        onSelect = { city ->
          latitude = city.latitude
          longitude = city.longitude
          timezoneHours = city.timezoneHours
          val locName = if (selectedLang == "BN") city.nameBn else city.nameEn
          locationName = locName
          calculationMethod = city.defaultMethod
          juristicMethod = city.defaultJuristic
          offlineRepo.saveLocation(city.latitude, city.longitude, locName, city.timezoneHours)
          offlineRepo.calculationMethod = city.defaultMethod
          offlineRepo.juristicMethod = city.defaultJuristic
          showCityDialog = false
        },
        onDismiss = { showCityDialog = false }
      )
    }
  }
}

/**
 * 1. Salah Timings Screen
 */
@Composable
fun SalahTimingsScreen(
  latitude: Double,
  longitude: Double,
  timezoneHours: Double,
  locationName: String,
  method: CalculationMethod,
  juristicMethod: JuristicMethod,
  selectedLang: String,
  onMethodChange: (CalculationMethod) -> Unit,
  onJuristicChange: (JuristicMethod) -> Unit,
  onRequestGps: () -> Unit,
  onOpenCityPicker: () -> Unit
) {
  val prayerSchedule = remember(latitude, longitude, timezoneHours, method, juristicMethod) {
    PrayerTimesCalculator.calculate(
      latitude = latitude,
      longitude = longitude,
      timezoneOffset = timezoneHours,
      locationName = locationName,
      method = method,
      juristicMethod = juristicMethod
    )
  }

  var showSettingsDialog by remember { mutableStateOf(false) }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .verticalScroll(rememberScrollState())
      .padding(16.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    // Top Active Prayer Card
    Card(
      shape = RoundedCornerShape(20.dp),
      elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
      colors = CardDefaults.cardColors(containerColor = Color.White),
      modifier = Modifier
        .fillMaxWidth()
        .shadow(4.dp, RoundedCornerShape(20.dp), spotColor = RoyalEmerald.copy(alpha = 0.2f))
    ) {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .background(
            Brush.verticalGradient(
              colors = listOf(
                Color(0xFF063A22),
                Color(0xFF0A5C36)
              )
            )
          )
          .padding(18.dp)
      ) {
        Column(
          modifier = Modifier.fillMaxWidth(),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Surface(
              shape = RoundedCornerShape(8.dp),
              color = Color.White.copy(alpha = 0.18f)
            ) {
              Text(
                text = "📍 $locationName",
                style = TextStyle(fontSize = 11.sp, color = Color.White),
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                maxLines = 1
              )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
              Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color.White.copy(alpha = 0.2f)
              ) {
                val tzStr = if (timezoneHours >= 0) "+$timezoneHours" else "$timezoneHours"
                Text(
                  text = "GMT$tzStr",
                  style = TextStyle(fontSize = 10.sp, color = Color.White, fontWeight = FontWeight.Bold),
                  modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
              }

              Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color(0xFFFDE047).copy(alpha = 0.25f),
                border = BorderStroke(1.dp, Color(0xFFFDE047))
              ) {
                Text(
                  text = method.name,
                  style = TextStyle(fontSize = 10.sp, color = Color(0xFFFEF08A), fontWeight = FontWeight.Bold),
                  modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(14.dp))

          Text(
            text = when (selectedLang) {
              "EN" -> "UPCOMING PRAYER"
              "AR" -> "الصلاة القادمة"
              else -> "পরবর্তী সালাত"
            },
            style = TextStyle(
              fontSize = 11.sp,
              fontWeight = FontWeight.SemiBold,
              color = Color.White.copy(alpha = 0.75f),
              letterSpacing = 1.sp
            )
          )

          Spacer(modifier = Modifier.height(4.dp))

          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = prayerSchedule.nextPrayer.iconEmoji,
              fontSize = 28.sp
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = when (selectedLang) {
                "EN" -> prayerSchedule.nextPrayer.nameEn
                "AR" -> prayerSchedule.nextPrayer.nameAr
                else -> prayerSchedule.nextPrayer.nameBn
              },
              style = TextStyle(
                fontSize = 26.sp,
                fontWeight = FontWeight.Black,
                color = Color.White
              )
            )
          }

          Text(
            text = prayerSchedule.nextPrayer.timeFormatted,
            style = TextStyle(
              fontSize = 28.sp,
              fontWeight = FontWeight.ExtraBold,
              color = Color(0xFFFDE047)
            )
          )

          Spacer(modifier = Modifier.height(8.dp))

          val hrs = prayerSchedule.timeUntilNextMinutes / 60
          val mins = prayerSchedule.timeUntilNextMinutes % 60
          val timeRemainingStr = when (selectedLang) {
            "EN" -> if (hrs > 0) "${hrs}h ${mins}m until ${prayerSchedule.nextPrayer.nameEn}" else "${mins}m remaining"
            "AR" -> if (hrs > 0) "متبقي $hrs ساعة و $mins دقيقة" else "متبقي $mins دقيقة"
            else -> if (hrs > 0) "আর ${hrs} ঘণ্টা ${mins} মিনিট বাকি" else "আর ${mins} মিনিট বাকি"
          }

          Surface(
            shape = RoundedCornerShape(12.dp),
            color = Color.White.copy(alpha = 0.18f)
          ) {
            Text(
              text = "⏳ $timeRemainingStr",
              style = TextStyle(
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White
              ),
              modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
            )
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(12.dp))

    // Quick Action Bar: City Picker, GPS, Juristic Toggle & Settings
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      // Manual City Selector
      OutlinedButton(
        onClick = onOpenCityPicker,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFCBD5E1)),
        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
        modifier = Modifier
          .weight(1f)
          .testTag("open_city_picker_btn")
      ) {
        Text(text = "📍", fontSize = 13.sp)
        Spacer(modifier = Modifier.width(4.dp))
        Text(
          text = when (selectedLang) { "EN" -> "Select City"; "AR" -> "المدينة"; else -> "শহর/জেলা" },
          style = TextStyle(fontSize = 11.5.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF1E293B)),
          maxLines = 1
        )
      }

      // Auto GPS
      OutlinedButton(
        onClick = onRequestGps,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFCBD5E1)),
        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
        modifier = Modifier
          .weight(1f)
          .testTag("request_gps_btn")
      ) {
        Text(text = "🛰️", fontSize = 13.sp)
        Spacer(modifier = Modifier.width(4.dp))
        Text(
          text = when (selectedLang) { "EN" -> "Auto GPS"; "AR" -> "تحديد الموقع"; else -> "অটো জিপিএস" },
          style = TextStyle(fontSize = 11.5.sp, fontWeight = FontWeight.SemiBold, color = RoyalEmerald),
          maxLines = 1
        )
      }

      // Asr Juristic Toggle
      OutlinedButton(
        onClick = {
          val nextJuristic = if (juristicMethod == JuristicMethod.HANAFI) JuristicMethod.SHAFI else JuristicMethod.HANAFI
          onJuristicChange(nextJuristic)
        },
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFCBD5E1)),
        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
        modifier = Modifier
          .weight(1f)
          .testTag("toggle_juristic_btn")
      ) {
        Text(text = "⚖️", fontSize = 13.sp)
        Spacer(modifier = Modifier.width(4.dp))
        Text(
          text = if (juristicMethod == JuristicMethod.HANAFI) "হানাফি" else "শাফেয়ী",
          style = TextStyle(fontSize = 11.5.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF0284C7)),
          maxLines = 1
        )
      }
    }

    Spacer(modifier = Modifier.height(14.dp))

    // Timetable Header
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column {
        Text(
          text = when (selectedLang) {
            "EN" -> "Daily Prayer Schedule"
            "AR" -> "جدول مواقيت الصلاة اليومي"
            else -> "আজকের পাঁচ ওয়াক্ত নামাজের সময়সূচি"
          },
          style = TextStyle(
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0F172A)
          )
        )
        Text(
          text = "${method.titleBn.take(30)} • ${juristicMethod.name}",
          style = TextStyle(fontSize = 10.5.sp, color = Color(0xFF64748B))
        )
      }

      OutlinedButton(
        onClick = { showSettingsDialog = true },
        shape = RoundedCornerShape(10.dp),
        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
        modifier = Modifier.testTag("prayer_settings_btn")
      ) {
        Icon(
          imageVector = Icons.Default.Settings,
          contentDescription = "Settings",
          modifier = Modifier.size(14.dp),
          tint = RoyalEmerald
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
          text = when (selectedLang) { "EN" -> "Settings"; "AR" -> "إعدادات"; else -> "পদ্ধতি" },
          style = TextStyle(fontSize = 11.sp, color = RoyalEmerald, fontWeight = FontWeight.Bold)
        )
      }
    }

    Spacer(modifier = Modifier.height(10.dp))

    // List of 7 Prayer entries (Fajr, Sunrise, Dhuhr, Asr, Maghrib, Isha, Tahajjud)
    prayerSchedule.allPrayers().forEach { prayer ->
      val isNext = prayer.id == prayerSchedule.nextPrayer.id
      val isCurrent = prayer.id == prayerSchedule.currentPrayer.id

      Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
          containerColor = if (isNext) Color(0xFFEFFDF5) else Color.White
        ),
        border = BorderStroke(
          width = if (isNext) 1.5.dp else 1.dp,
          color = if (isNext) RoyalEmerald else Color(0xFFE2E8F0)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isNext) 2.dp else 1.dp),
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 4.dp)
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 12.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(if (isNext) RoyalEmerald else Color(0xFFF1F5F9)),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = prayer.iconEmoji,
                fontSize = 18.sp
              )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                  text = when (selectedLang) {
                    "EN" -> prayer.nameEn
                    "AR" -> prayer.nameAr
                    else -> prayer.nameBn
                  },
                  style = TextStyle(
                    fontSize = 14.5.sp,
                    fontWeight = if (isNext) FontWeight.Bold else FontWeight.SemiBold,
                    color = if (isNext) RoyalEmerald else Color(0xFF0F172A)
                  )
                )

                if (prayer.isOptional) {
                  Spacer(modifier = Modifier.width(6.dp))
                  Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = Color(0xFFE2E8F0)
                  ) {
                    Text(
                      text = when (selectedLang) { "EN" -> "Sunnah"; "AR" -> "سنة"; else -> "নফল/সুন্নাহ" },
                      style = TextStyle(fontSize = 9.sp, color = Color(0xFF475569)),
                      modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                    )
                  }
                }
              }

              Text(
                text = prayer.nameAr,
                style = TextStyle(fontSize = 11.sp, color = Color(0xFF94A3B8))
              )
            }
          }

          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = prayer.timeFormatted,
              style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = if (isNext) RoyalEmerald else Color(0xFF1E293B)
              )
            )

            if (isNext) {
              Spacer(modifier = Modifier.width(8.dp))
              Surface(
                shape = CircleShape,
                color = RoyalEmerald
              ) {
                Box(modifier = Modifier.size(8.dp))
              }
            }
          }
        }
      }
    }
  }

  if (showSettingsDialog) {
    PrayerSettingsDialog(
      currentMethod = method,
      currentJuristic = juristicMethod,
      selectedLang = selectedLang,
      onSave = { newMethod, newJuristic ->
        onMethodChange(newMethod)
        onJuristicChange(newJuristic)
        showSettingsDialog = false
      },
      onDismiss = { showSettingsDialog = false }
    )
  }
}

/**
 * 2. Qibla Compass Screen
 */
@Composable
fun QiblaCompassScreen(
  latitude: Double,
  longitude: Double,
  locationName: String,
  selectedLang: String,
  onRequestGps: () -> Unit
) {
  val context = LocalContext.current
  val compassManager = remember { QiblaCompassManager(context) }

  var qiblaInfo by remember {
    mutableStateOf(
      QiblaInfo(
        bearingDegrees = QiblaCompassManager.calculateQiblaBearing(latitude, longitude),
        distanceKm = QiblaCompassManager.calculateDistanceToKaabaKm(latitude, longitude),
        deviceAzimuth = 0f,
        needleAngle = QiblaCompassManager.calculateQiblaBearing(latitude, longitude),
        isFacingKaaba = false
      )
    )
  }

  DisposableEffect(latitude, longitude) {
    compassManager.onCompassUpdated = { info ->
      qiblaInfo = info
    }
    compassManager.startListening(latitude, longitude)
    onDispose {
      compassManager.stopListening()
    }
  }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .verticalScroll(rememberScrollState())
      .padding(16.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    // Header Info Card
    Card(
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = Color.White),
      elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
      border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
      modifier = Modifier.fillMaxWidth()
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Column {
          Text(
            text = when (selectedLang) {
              "EN" -> "Qibla Direction (Makkah)"
              "AR" -> "اتجاه القبلة الشريفة"
              else -> "পবিত্র কাবা শরিফের দিক (কিবলা)"
            },
            style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold, color = RoyalEmerald)
          )
          Text(
            text = "📍 $locationName",
            style = TextStyle(fontSize = 11.sp, color = Color(0xFF64748B))
          )
        }

        Surface(
          shape = RoundedCornerShape(10.dp),
          color = if (qiblaInfo.isFacingKaaba) Color(0xFFDCFCE7) else Color(0xFFF1F5F9),
          border = BorderStroke(1.dp, if (qiblaInfo.isFacingKaaba) RoyalEmerald else Color(0xFFE2E8F0))
        ) {
          Text(
            text = "${qiblaInfo.bearingDegrees.toInt()}°",
            style = TextStyle(
              fontSize = 16.sp,
              fontWeight = FontWeight.Black,
              color = if (qiblaInfo.isFacingKaaba) RoyalEmerald else Color(0xFF0F172A)
            ),
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(20.dp))

    // Facing Kaaba Status Indicator Badge
    Surface(
      shape = RoundedCornerShape(30.dp),
      color = if (qiblaInfo.isFacingKaaba) Color(0xFF15803D) else Color(0xFF334155),
      modifier = Modifier
        .shadow(4.dp, RoundedCornerShape(30.dp))
        .testTag("qibla_facing_badge")
    ) {
      Row(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = if (qiblaInfo.isFacingKaaba) "🕋 ✨" else "🧭",
          fontSize = 16.sp
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = if (qiblaInfo.isFacingKaaba) {
            when (selectedLang) {
              "EN" -> "Facing Holy Kaaba Precisely!"
              "AR" -> "أنت تواجه الكعبة المشرفة تماماً!"
              else -> "আপনি সরাসরি কাবার মুখোমুখি আছেন!"
            }
          } else {
            when (selectedLang) {
              "EN" -> "Rotate device towards ${qiblaInfo.bearingDegrees.toInt()}°"
              "AR" -> "قم بتدوير الجهاز نحو ${qiblaInfo.bearingDegrees.toInt()}°"
              else -> "ফোনটি ঘুরিয়ে ${qiblaInfo.bearingDegrees.toInt()}° কোণে রাখুন"
            }
          },
          style = TextStyle(
            fontSize = 12.5.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
          )
        )
      }
    }

    Spacer(modifier = Modifier.height(24.dp))

    // Custom 3D Islamic Compass Dial Canvas
    Box(
      modifier = Modifier
        .size(280.dp)
        .clip(CircleShape)
        .background(
          Brush.radialGradient(
            colors = listOf(
              Color.White,
              if (qiblaInfo.isFacingKaaba) Color(0xFFDCFCE7) else Color(0xFFF8FAFC),
              if (qiblaInfo.isFacingKaaba) Color(0xFFBBF7D0) else Color(0xFFE2E8F0)
            )
          )
        )
        .border(
          width = 4.dp,
          color = if (qiblaInfo.isFacingKaaba) RoyalEmerald else Color(0xFFCBD5E1),
          shape = CircleShape
        )
        .shadow(8.dp, CircleShape),
      contentAlignment = Alignment.Center
    ) {
      // Rotating Compass Dial
      Canvas(
        modifier = Modifier
          .fillMaxSize()
          .rotate(-qiblaInfo.deviceAzimuth)
      ) {
        val center = Offset(size.width / 2, size.height / 2)
        val radius = size.width / 2 - 20.dp.toPx()

        // Cardinal markers (N, E, S, W)
        for (i in 0 until 360 step 30) {
          val isMajor = i % 90 == 0
          val lineLen = if (isMajor) 14.dp.toPx() else 8.dp.toPx()
          val strokeWidth = if (isMajor) 3.dp.toPx() else 1.5.dp.toPx()
          val color = if (i == 0) Color(0xFFDC2626) else Color(0xFF94A3B8)

          rotate(i.toFloat(), center) {
            drawLine(
              color = color,
              start = Offset(center.x, center.y - radius),
              end = Offset(center.x, center.y - radius + lineLen),
              strokeWidth = strokeWidth
            )
          }
        }
      }

      // Rotating Qibla Pointer Needle
      Box(
        modifier = Modifier
          .fillMaxSize()
          .rotate(qiblaInfo.needleAngle),
        contentAlignment = Alignment.TopCenter
      ) {
        Column(
          horizontalAlignment = Alignment.CenterHorizontally,
          modifier = Modifier.padding(top = 18.dp)
        ) {
          // Kaaba Icon at the tip of the needle
          Box(
            modifier = Modifier
              .size(42.dp)
              .clip(CircleShape)
              .background(if (qiblaInfo.isFacingKaaba) RoyalEmerald else Color(0xFF0F172A))
              .border(2.dp, Color(0xFFFDE047), CircleShape)
              .shadow(4.dp, CircleShape),
            contentAlignment = Alignment.Center
          ) {
            Text(text = "🕋", fontSize = 20.sp)
          }

          // Needle arrow
          Box(
            modifier = Modifier
              .width(6.dp)
              .height(60.dp)
              .clip(RoundedCornerShape(3.dp))
              .background(
                Brush.verticalGradient(
                  colors = listOf(
                    if (qiblaInfo.isFacingKaaba) RoyalEmerald else Color(0xFFDC2626),
                    Color.Transparent
                  )
                )
              )
          )
        }
      }

      // Compass Center Pivot
      Box(
        modifier = Modifier
          .size(24.dp)
          .clip(CircleShape)
          .background(RoyalEmerald)
          .border(2.dp, Color.White, CircleShape)
      )
    }

    Spacer(modifier = Modifier.height(24.dp))

    // Distance and Azimuth stats card
    Card(
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = Color.White),
      border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
      modifier = Modifier.fillMaxWidth()
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Text(
            text = when (selectedLang) { "EN" -> "Distance to Kaaba"; "AR" -> "المسافة إلى الكعبة"; else -> "কাবার দূরত্ব" },
            style = TextStyle(fontSize = 11.sp, color = Color(0xFF64748B))
          )
          Text(
            text = "${qiblaInfo.distanceKm.toInt()} km",
            style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold, color = RoyalEmerald)
          )
        }

        Box(
          modifier = Modifier
            .width(1.dp)
            .height(30.dp)
            .background(Color(0xFFE2E8F0))
        )

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Text(
            text = when (selectedLang) { "EN" -> "Phone Heading"; "AR" -> "اتجاه الهاتف"; else -> "ফোনের বর্তমান কোণ" },
            style = TextStyle(fontSize = 11.sp, color = Color(0xFF64748B))
          )
          Text(
            text = "${qiblaInfo.deviceAzimuth.toInt()}°",
            style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
          )
        }
      }
    }
  }
}

/**
 * 3. Hijri Calendar & Key Days Screen
 */
@Composable
fun HijriCalendarScreen(
  selectedLang: String,
  dayAdjustment: Int,
  onAdjustmentChange: (Int) -> Unit
) {
  val currentHijri = remember(dayAdjustment) {
    HijriCalendarCalculator.calculateHijri(dayAdjustment = dayAdjustment)
  }

  val upcomingEvents = remember(currentHijri) {
    HijriCalendarCalculator.getUpcomingEvents(currentHijri)
  }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .verticalScroll(rememberScrollState())
      .padding(16.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    // Current Hijri Card
    Card(
      shape = RoundedCornerShape(20.dp),
      colors = CardDefaults.cardColors(containerColor = Color.White),
      elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
      border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
      modifier = Modifier.fillMaxWidth()
    ) {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .background(
            Brush.verticalGradient(
              colors = listOf(
                Color(0xFF063A22),
                Color(0xFF0A5C36)
              )
            )
          )
          .padding(18.dp)
      ) {
        Column(
          modifier = Modifier.fillMaxWidth(),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Surface(
              shape = RoundedCornerShape(8.dp),
              color = Color.White.copy(alpha = 0.15f)
            ) {
              Text(
                text = "আজকের হিজরি তারিখ",
                style = TextStyle(fontSize = 11.sp, color = Color.White),
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
              )
            }

            Text(
              text = currentHijri.moonPhaseEmoji,
              fontSize = 24.sp
            )
          }

          Spacer(modifier = Modifier.height(10.dp))

          Text(
            text = "${currentHijri.day}",
            style = TextStyle(
              fontSize = 44.sp,
              fontWeight = FontWeight.Black,
              color = Color(0xFFFDE047)
            )
          )

          Text(
            text = when (selectedLang) {
              "EN" -> "${currentHijri.monthNameEn}, ${currentHijri.year} AH"
              "AR" -> "${currentHijri.monthNameAr} ${currentHijri.year} هـ"
              else -> "${currentHijri.monthNameBn}, ${currentHijri.year} হিজরি"
            },
            style = TextStyle(
              fontSize = 18.sp,
              fontWeight = FontWeight.Bold,
              color = Color.White
            )
          )

          if (currentHijri.isWhiteDay) {
            Spacer(modifier = Modifier.height(6.dp))
            Surface(
              shape = RoundedCornerShape(10.dp),
              color = Color(0xFFFDE047).copy(alpha = 0.25f),
              border = BorderStroke(1.dp, Color(0xFFFDE047))
            ) {
              Text(
                text = "✨ আইয়ামে বিজ (সুন্নাত রোজা রাখার দিন)",
                style = TextStyle(fontSize = 11.sp, color = Color(0xFFFEF08A), fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp)
              )
            }
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(14.dp))

    // Moon Sighting Fine Adjustment Control
    Card(
      shape = RoundedCornerShape(14.dp),
      colors = CardDefaults.cardColors(containerColor = Color.White),
      border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
      modifier = Modifier.fillMaxWidth()
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 14.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = when (selectedLang) {
              "EN" -> "Moon Sighting Adjustment"
              "AR" -> "تعديل رؤية الهلال"
              else -> "চাঁদ দেখার সমন্বয়"
            },
            style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
          )
          Text(
            text = when (selectedLang) {
              "EN" -> "Adjust ±1-2 days based on local sighting"
              "AR" -> "تعديل يوم أو يومين حسب الرؤية المحلية"
              else -> "স্থানীয় চাঁদ দেখার ভিত্তিতে ১-২ দিন সমন্বয় করুন"
            },
            style = TextStyle(fontSize = 10.sp, color = Color(0xFF64748B))
          )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
          IconButton(
            onClick = { onAdjustmentChange(dayAdjustment - 1) },
            enabled = dayAdjustment > -2
          ) {
            Text(text = "◀", fontSize = 14.sp, color = RoyalEmerald)
          }

          Text(
            text = if (dayAdjustment > 0) "+$dayAdjustment" else "$dayAdjustment",
            style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold, color = RoyalEmerald)
          )

          IconButton(
            onClick = { onAdjustmentChange(dayAdjustment + 1) },
            enabled = dayAdjustment < 2
          ) {
            Text(text = "▶", fontSize = 14.sp, color = RoyalEmerald)
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(18.dp))

    // Key Islamic Days Section Header
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = when (selectedLang) {
          "EN" -> "Significant Islamic Days & Events"
          "AR" -> "المناسبات والأيام الإسلامية المهمة"
          else -> "গুরুত্বপূর্ণ ইসলামিক দিবস ও রজনী"
        },
        style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
      )
    }

    Spacer(modifier = Modifier.height(10.dp))

    // Events List
    upcomingEvents.forEach { (event, daysRemaining) ->
      Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 4.dp)
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(14.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Box(
            modifier = Modifier
              .size(44.dp)
              .clip(RoundedCornerShape(12.dp))
              .background(Color(event.badgeColorHex).copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
          ) {
            Text(text = event.iconEmoji, fontSize = 20.sp)
          }

          Spacer(modifier = Modifier.width(12.dp))

          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = if (selectedLang == "BN") event.titleBn else event.titleEn,
              style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
            )
            Text(
              text = if (selectedLang == "BN") event.descriptionBn else event.descriptionEn,
              style = TextStyle(fontSize = 11.sp, color = Color(0xFF64748B)),
              maxLines = 2,
              overflow = TextOverflow.Ellipsis
            )
          }

          Spacer(modifier = Modifier.width(8.dp))

          Surface(
            shape = RoundedCornerShape(8.dp),
            color = if (daysRemaining == 0) Color(0xFFDCFCE7) else Color(0xFFF1F5F9),
            border = BorderStroke(1.dp, if (daysRemaining == 0) RoyalEmerald else Color(0xFFE2E8F0))
          ) {
            Text(
              text = if (daysRemaining == 0) {
                when (selectedLang) { "EN" -> "Today!"; "AR" -> "اليوم!"; else -> "আজ!" }
              } else {
                when (selectedLang) { "EN" -> "${daysRemaining}d left"; "AR" -> "بقي $daysRemaining ي"; else -> "আর $daysRemaining দিন" }
              },
              style = TextStyle(
                fontSize = 10.5.sp,
                fontWeight = FontWeight.Bold,
                color = if (daysRemaining == 0) RoyalEmerald else Color(0xFF334155)
              ),
              modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
            )
          }
        }
      }
    }
  }
}

/**
 * 4. Asma-ul-Husna (99 Names of Allah) Screen
 */
@Composable
fun AsmaUlHusnaScreen(selectedLang: String) {
  val context = LocalContext.current
  val audioPlayer = remember { IslamicAudioPlayer(context) }
  DisposableEffect(Unit) {
    onDispose { audioPlayer.release() }
  }

  val playingId by audioPlayer.playingNameId.collectAsState()
  val isAutoPlay by audioPlayer.isAutoPlay.collectAsState()

  var searchQuery by remember { mutableStateOf("") }
  var selectedCategory by remember { mutableStateOf(NameCategory.ALL) }
  var selectedNameDetail by remember { mutableStateOf<AllahName?>(null) }

  val filteredNames = remember(searchQuery, selectedCategory) {
    AsmaUlHusnaMaster.all99Names.filter { name ->
      val matchesCat = selectedCategory == NameCategory.ALL || name.category == selectedCategory
      val q = searchQuery.trim().lowercase()
      val matchesQuery = q.isEmpty() ||
        name.nameAr.contains(q) ||
        name.nameBn.lowercase().contains(q) ||
        name.nameEn.lowercase().contains(q) ||
        name.meaningBn.lowercase().contains(q) ||
        name.meaningEn.lowercase().contains(q) ||
        name.number.toString() == q
      matchesCat && matchesQuery
    }
  }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(horizontal = 14.dp, vertical = 8.dp)
  ) {
    // Search Bar
    OutlinedTextField(
      value = searchQuery,
      onValueChange = { searchQuery = it },
      placeholder = {
        Text(
          text = when (selectedLang) {
            "EN" -> "Search by Arabic, English, Bengali or number..."
            "AR" -> "ابحث بالاسم أو المعنى أو الرقم..."
            else -> "আরবি নাম, বাংলা অর্থ বা নম্বর দিয়ে খুঁজুন..."
          },
          style = TextStyle(fontSize = 12.sp)
        )
      },
      leadingIcon = {
        Icon(Icons.Default.Search, contentDescription = "Search", tint = RoyalEmerald)
      },
      shape = RoundedCornerShape(12.dp),
      singleLine = true,
      modifier = Modifier
        .fillMaxWidth()
        .testTag("asma_search_input")
    )

    Spacer(modifier = Modifier.height(8.dp))

    // Auto-play / Continuous recitation bar
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(12.dp))
        .background(RoyalEmerald.copy(alpha = 0.08f))
        .padding(horizontal = 12.dp, vertical = 6.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Text(text = "🎧", fontSize = 16.sp)
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = if (isAutoPlay) {
            when (selectedLang) { "EN" -> "Auto-Playing 99 Names..."; "AR" -> "تشغيل تلقائي..."; else -> "ধারাবাহিক তিলাওয়াত চলছে..." }
          } else {
            when (selectedLang) { "EN" -> "Audio Recitation (99 Names)"; "AR" -> "تلاوة أسماء الله الحسنى"; else -> "অডিও তিলাওয়াত শুনুন" }
          },
          style = TextStyle(fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = RoyalEmerald)
        )
      }

      Button(
        onClick = { audioPlayer.toggleAutoPlay(playingId ?: 1) },
        colors = ButtonDefaults.buttonColors(
          containerColor = if (isAutoPlay) Color(0xFFDC2626) else RoyalEmerald
        ),
        shape = RoundedCornerShape(8.dp),
        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
        modifier = Modifier.testTag("asma_autoplay_btn")
      ) {
        Text(
          text = if (isAutoPlay) {
            when (selectedLang) { "EN" -> "Stop"; "AR" -> "إيقاف"; else -> "থামান" }
          } else {
            when (selectedLang) { "EN" -> "Play All"; "AR" -> "تشغيل الكل"; else -> "সব শুনুন" }
          },
          style = TextStyle(fontSize = 11.sp, fontWeight = FontWeight.Bold)
        )
      }
    }

    Spacer(modifier = Modifier.height(8.dp))

    // Category Filter Chips
    LazyRow(
      horizontalArrangement = Arrangement.spacedBy(6.dp),
      contentPadding = PaddingValues(horizontal = 2.dp)
    ) {
      items(NameCategory.values()) { category ->
        val isSelected = selectedCategory == category
        Surface(
          shape = RoundedCornerShape(10.dp),
          color = if (isSelected) RoyalEmerald else Color.White,
          border = BorderStroke(1.dp, if (isSelected) RoyalEmerald else Color(0xFFE2E8F0)),
          modifier = Modifier
            .clickable { selectedCategory = category }
            .testTag("asma_cat_${category.name.lowercase()}")
        ) {
          Text(
            text = if (selectedLang == "BN") category.titleBn else category.titleEn,
            style = TextStyle(
              fontSize = 11.sp,
              fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
              color = if (isSelected) Color.White else Color(0xFF334155)
            ),
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(10.dp))

    // 99 Names Grid
    LazyVerticalGrid(
      columns = GridCells.Fixed(2),
      horizontalArrangement = Arrangement.spacedBy(8.dp),
      verticalArrangement = Arrangement.spacedBy(8.dp),
      modifier = Modifier.fillMaxSize()
    ) {
      items(filteredNames) { item ->
        val isItemPlaying = playingId == item.number
        Card(
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(
            containerColor = if (isItemPlaying) Color(0xFFECFDF5) else Color.White
          ),
          border = BorderStroke(
            width = if (isItemPlaying) 1.5.dp else 1.dp,
            color = if (isItemPlaying) RoyalEmerald else Color(0xFFE2E8F0)
          ),
          elevation = CardDefaults.cardElevation(defaultElevation = if (isItemPlaying) 3.dp else 1.dp),
          modifier = Modifier
            .fillMaxWidth()
            .clickable { selectedNameDetail = item }
            .testTag("asma_item_${item.number}")
        ) {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Surface(
                shape = CircleShape,
                color = if (isItemPlaying) RoyalEmerald else Color(0xFFF1F5F9)
              ) {
                Text(
                  text = "${item.number}",
                  style = TextStyle(
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isItemPlaying) Color.White else Color(0xFF64748B)
                  ),
                  modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                )
              }

              IconButton(
                onClick = { audioPlayer.playName(item.number, item.nameAr) },
                modifier = Modifier.size(24.dp)
              ) {
                Icon(
                  imageVector = if (isItemPlaying) Icons.Default.VolumeUp else Icons.Default.PlayArrow,
                  contentDescription = "Play audio",
                  tint = if (isItemPlaying) RoyalEmerald else Color(0xFF94A3B8),
                  modifier = Modifier.size(16.dp)
                )
              }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Arabic Script
            Text(
              text = item.nameAr,
              style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = if (isItemPlaying) RoyalEmerald else Color(0xFF0F172A),
                fontFamily = FontFamily.Serif
              )
            )

            Spacer(modifier = Modifier.height(2.dp))

            // Transliteration
            Text(
              text = if (selectedLang == "BN") item.nameBn else item.nameEn,
              style = TextStyle(
                fontSize = 11.5.sp,
                fontWeight = FontWeight.SemiBold,
                color = RoyalEmerald
              )
            )

            // Meaning
            Text(
              text = if (selectedLang == "BN") item.meaningBn else item.meaningEn,
              style = TextStyle(
                fontSize = 10.5.sp,
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

  // Details Dialog for Selected Name
  selectedNameDetail?.let { item ->
    Dialog(onDismissRequest = { selectedNameDetail = null }) {
      Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
          .fillMaxWidth()
          .padding(16.dp)
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(18.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Surface(
              shape = RoundedCornerShape(6.dp),
              color = RoyalEmerald.copy(alpha = 0.12f)
            ) {
              Text(
                text = "#${item.number}",
                style = TextStyle(fontSize = 11.sp, fontWeight = FontWeight.Bold, color = RoyalEmerald),
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
              )
            }

            IconButton(
              onClick = { audioPlayer.playName(item.number, item.nameAr) }
            ) {
              Icon(Icons.Default.VolumeUp, contentDescription = "Play", tint = RoyalEmerald)
            }
          }

          Spacer(modifier = Modifier.height(10.dp))

          Text(
            text = item.nameAr,
            style = TextStyle(
              fontSize = 36.sp,
              fontWeight = FontWeight.Bold,
              color = RoyalEmerald,
              fontFamily = FontFamily.Serif
            )
          )

          Spacer(modifier = Modifier.height(4.dp))

          Text(
            text = "${item.nameBn} (${item.nameEn})",
            style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
          )

          Text(
            text = item.meaningBn,
            style = TextStyle(fontSize = 13.sp, color = Color(0xFF15803D), fontWeight = FontWeight.SemiBold)
          )

          Spacer(modifier = Modifier.height(12.dp))

          Text(
            text = item.explanationBn,
            style = TextStyle(fontSize = 12.sp, color = Color(0xFF334155), lineHeight = 18.sp),
            textAlign = TextAlign.Center
          )

          Spacer(modifier = Modifier.height(10.dp))

          Surface(
            shape = RoundedCornerShape(8.dp),
            color = Color(0xFFF1F5F9)
          ) {
            Text(
              text = "📖 ${item.quranVerse}",
              style = TextStyle(fontSize = 11.sp, color = Color(0xFF475569)),
              modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
            )
          }

          Spacer(modifier = Modifier.height(16.dp))

          Button(
            onClick = { selectedNameDetail = null },
            colors = ButtonDefaults.buttonColors(containerColor = RoyalEmerald),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            Text(
              text = when (selectedLang) { "EN" -> "Close"; "AR" -> "إغلاق"; else -> "বন্ধ করুন" },
              style = TextStyle(fontWeight = FontWeight.Bold)
            )
          }
        }
      }
    }
  }
}

/**
 * City Preset Picker Dialog with Search and Region Filtering
 */
@Composable
fun CitySelectionDialog(
  selectedLang: String,
  onSelect: (CityPreset) -> Unit,
  onDismiss: () -> Unit
) {
  var searchQuery by remember { mutableStateOf("") }
  var selectedRegion by remember { mutableStateOf("ALL") }

  val regions = listOf(
    "ALL" to if (selectedLang == "BN") "সকল শহর" else "All Cities",
    "BD" to if (selectedLang == "BN") "বাংলাদেশ (জেলাসমূহ)" else "Bangladesh (Districts)",
    "GULF" to if (selectedLang == "BN") "সৌদি ও উপসাগর" else "Saudi & Gulf",
    "PK" to if (selectedLang == "BN") "পাকিস্তান" else "Pakistan",
    "IN" to if (selectedLang == "BN") "ভারত" else "India",
    "MY_SG" to if (selectedLang == "BN") "মালয়েশিয়া ও সিঙ্গাপুর" else "Malaysia & SG",
    "ID" to if (selectedLang == "BN") "ইন্দোনেশিয়া" else "Indonesia",
    "ME_TR" to if (selectedLang == "BN") "তুরস্ক ও মধ্যপ্রাচ্য" else "Turkey & Middle East",
    "ASIA" to if (selectedLang == "BN") "মধ্য ও পূর্ব এশিয়া" else "Central & East Asia",
    "WEST" to if (selectedLang == "BN") "অন্যান্য" else "Western & Other"
  )

  val filteredCities = remember(searchQuery, selectedRegion) {
    LocationPresets.defaultCities.filter { city ->
      val matchesRegion = when (selectedRegion) {
        "ALL" -> true
        "BD" -> city.countryEn == "Bangladesh"
        "GULF" -> city.countryEn in listOf("Saudi Arabia", "United Arab Emirates", "Qatar", "Kuwait", "Bahrain", "Oman")
        "PK" -> city.countryEn == "Pakistan"
        "IN" -> city.countryEn == "India"
        "MY_SG" -> city.countryEn in listOf("Malaysia", "Singapore")
        "ID" -> city.countryEn == "Indonesia"
        "ME_TR" -> city.countryEn in listOf("Turkey", "Egypt", "Jordan", "Lebanon", "Iraq", "Iran")
        "ASIA" -> city.countryEn in listOf("Uzbekistan", "Kazakhstan", "Kyrgyzstan", "Tajikistan", "Afghanistan", "Maldives", "Sri Lanka", "Thailand", "Japan", "South Korea", "China")
        "WEST" -> city.countryEn in listOf("United Kingdom", "United States", "Canada", "Australia")
        else -> true
      }

      val q = searchQuery.trim().lowercase()
      val matchesQuery = if (q.isEmpty()) true else {
        city.nameEn.lowercase().contains(q) ||
          city.nameBn.contains(q) ||
          city.countryEn.lowercase().contains(q) ||
          city.countryBn.contains(q)
      }

      matchesRegion && matchesQuery
    }
  }

  Dialog(onDismissRequest = onDismiss) {
    Card(
      shape = RoundedCornerShape(20.dp),
      colors = CardDefaults.cardColors(containerColor = Color.White),
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 12.dp)
    ) {
      Column(modifier = Modifier.padding(16.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(
              text = when (selectedLang) {
                "EN" -> "Select Location"
                "AR" -> "اختر المدينة"
                else -> "শহর / জেলা নির্বাচন করুন"
              },
              style = TextStyle(fontSize = 17.sp, fontWeight = FontWeight.Bold, color = RoyalEmerald)
            )
            Text(
              text = when (selectedLang) {
                "EN" -> "Accurate timezone & calculation presets"
                "AR" -> "مواقيت دقيقة حسب المنطقة"
                else -> "সঠিক টাইমজোন ও ওয়াক্ত ক্যালকুলেশন"
              },
              style = TextStyle(fontSize = 11.sp, color = Color(0xFF64748B))
            )
          }

          IconButton(onClick = onDismiss) {
            Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = Color(0xFF64748B))
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Search Box
        OutlinedTextField(
          value = searchQuery,
          onValueChange = { searchQuery = it },
          placeholder = {
            Text(
              text = when (selectedLang) {
                "EN" -> "Search city, district, country..."
                "AR" -> "ابحث عن المدينة..."
                else -> "শহর, জেলা বা দেশের নাম লিখুন..."
              },
              style = TextStyle(fontSize = 12.5.sp, color = Color(0xFF94A3B8))
            )
          },
          leadingIcon = {
            Icon(imageVector = Icons.Default.Search, contentDescription = "Search", tint = RoyalEmerald, modifier = Modifier.size(18.dp))
          },
          trailingIcon = {
            if (searchQuery.isNotEmpty()) {
              IconButton(onClick = { searchQuery = "" }) {
                Icon(imageVector = Icons.Default.Close, contentDescription = "Clear", tint = Color(0xFF94A3B8), modifier = Modifier.size(16.dp))
              }
            }
          },
          shape = RoundedCornerShape(12.dp),
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = RoyalEmerald,
            unfocusedBorderColor = Color(0xFFE2E8F0),
            focusedContainerColor = Color(0xFFF8FAFC),
            unfocusedContainerColor = Color(0xFFF8FAFC)
          ),
          singleLine = true,
          modifier = Modifier
            .fillMaxWidth()
            .testTag("city_search_input")
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Region Filter Chips
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          regions.forEach { (code, label) ->
            val isSelected = selectedRegion == code
            Surface(
              shape = RoundedCornerShape(20.dp),
              color = if (isSelected) RoyalEmerald else Color(0xFFF1F5F9),
              border = BorderStroke(1.dp, if (isSelected) RoyalEmerald else Color(0xFFE2E8F0)),
              modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .clickable { selectedRegion = code }
            ) {
              Text(
                text = label,
                style = TextStyle(
                  fontSize = 11.5.sp,
                  fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                  color = if (isSelected) Color.White else Color(0xFF334155)
                ),
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // City List
        LazyColumn(
          modifier = Modifier
            .height(300.dp)
            .fillMaxWidth()
        ) {
          if (filteredCities.isEmpty()) {
            item {
              Box(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(32.dp),
                contentAlignment = Alignment.Center
              ) {
                Text(
                  text = when (selectedLang) {
                    "EN" -> "No matching cities found"
                    "AR" -> "لم يتم العثور على نتائج"
                    else -> "কোন মিল পাওয়া যায়নি"
                  },
                  style = TextStyle(fontSize = 13.sp, color = Color(0xFF94A3B8))
                )
              }
            }
          } else {
            items(filteredCities) { city ->
              Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(vertical = 4.dp)
                  .clip(RoundedCornerShape(12.dp))
                  .clickable { onSelect(city) }
                  .testTag("city_item_${city.nameEn.lowercase().replace(" ", "_")}")
              ) {
                Row(
                  modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.SpaceBetween
                ) {
                  Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                  ) {
                    Box(
                      modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(RoyalEmerald.copy(alpha = 0.1f)),
                      contentAlignment = Alignment.Center
                    ) {
                      Text(text = "📍", fontSize = 16.sp)
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                      Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                          text = if (selectedLang == "BN") city.nameBn else city.nameEn,
                          style = TextStyle(fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                        )
                        if (selectedLang != "BN" || city.nameBn != city.nameEn) {
                          Spacer(modifier = Modifier.width(6.dp))
                          Text(
                            text = if (selectedLang == "BN") "(${city.nameEn})" else "(${city.nameBn})",
                            style = TextStyle(fontSize = 11.sp, color = Color(0xFF64748B))
                          )
                        }
                      }

                      Text(
                        text = if (selectedLang == "BN") city.countryBn else city.countryEn,
                        style = TextStyle(fontSize = 11.sp, color = Color(0xFF64748B))
                      )
                    }
                  }

                  // Badges: Timezone & Method
                  Column(horizontalAlignment = Alignment.End) {
                    Surface(
                      shape = RoundedCornerShape(6.dp),
                      color = Color(0xFFE0F2FE),
                      border = BorderStroke(0.5.dp, Color(0xFFBAE6FD))
                    ) {
                      val tzText = if (city.timezoneHours >= 0) "GMT+${city.timezoneHours}" else "GMT${city.timezoneHours}"
                      Text(
                        text = tzText,
                        style = TextStyle(fontSize = 9.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0369A1)),
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 1.5.dp)
                      )
                    }

                    Spacer(modifier = Modifier.height(3.dp))

                    Surface(
                      shape = RoundedCornerShape(6.dp),
                      color = Color(0xFFDCFCE7),
                      border = BorderStroke(0.5.dp, Color(0xFFBBF7D0))
                    ) {
                      Text(
                        text = city.defaultMethod.name,
                        style = TextStyle(fontSize = 9.5.sp, fontWeight = FontWeight.Bold, color = RoyalEmerald),
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 1.5.dp)
                      )
                    }
                  }
                }
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        TextButton(
          onClick = onDismiss,
          modifier = Modifier.align(Alignment.End)
        ) {
          Text(
            text = when (selectedLang) { "EN" -> "Close"; "AR" -> "إغلاق"; else -> "বন্ধ করুন" },
            color = Color(0xFF64748B),
            fontWeight = FontWeight.SemiBold
          )
        }
      }
    }
  }
}

/**
 * Calculation Method and Juristic Settings Dialog
 */
@Composable
fun PrayerSettingsDialog(
  currentMethod: CalculationMethod,
  currentJuristic: JuristicMethod,
  selectedLang: String,
  onSave: (CalculationMethod, JuristicMethod) -> Unit,
  onDismiss: () -> Unit
) {
  var selectedMethod by remember { mutableStateOf(currentMethod) }
  var selectedJuristic by remember { mutableStateOf(currentJuristic) }

  Dialog(onDismissRequest = onDismiss) {
    Card(
      shape = RoundedCornerShape(20.dp),
      colors = CardDefaults.cardColors(containerColor = Color.White),
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 12.dp)
    ) {
      Column(
        modifier = Modifier
          .padding(16.dp)
          .verticalScroll(rememberScrollState())
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = when (selectedLang) {
              "EN" -> "Prayer Calculation Settings"
              "AR" -> "إعدادات حساب الصلاة"
              else -> "নামাজের গণনা পদ্ধতি ও মাযহাব"
            },
            style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold, color = RoyalEmerald)
          )

          IconButton(onClick = onDismiss) {
            Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = Color(0xFF64748B))
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
          text = when (selectedLang) {
            "EN" -> "Calculation Method (Asian & Global)"
            "AR" -> "طريقة الحساب (الآسيوية والعالمية)"
            else -> "গণনা পদ্ধতি (এশিয়ান ও বৈশ্বিক)"
          },
          style = TextStyle(fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF334155))
        )

        Spacer(modifier = Modifier.height(6.dp))

        CalculationMethod.values().forEach { method ->
          val isSelected = selectedMethod == method
          Surface(
            shape = RoundedCornerShape(10.dp),
            color = if (isSelected) Color(0xFFEFFDF5) else Color(0xFFF8FAFC),
            border = BorderStroke(1.dp, if (isSelected) RoyalEmerald else Color(0xFFE2E8F0)),
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 3.dp)
              .clip(RoundedCornerShape(10.dp))
              .clickable { selectedMethod = method }
              .testTag("method_option_${method.name.lowercase()}")
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 8.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(
                imageVector = if (isSelected) Icons.Default.CheckCircle else Icons.Default.Check,
                contentDescription = null,
                tint = if (isSelected) RoyalEmerald else Color(0xFFCBD5E1),
                modifier = Modifier.size(18.dp)
              )
              Spacer(modifier = Modifier.width(8.dp))
              Column {
                Text(
                  text = if (selectedLang == "BN") method.titleBn else method.titleEn,
                  style = TextStyle(
                    fontSize = 12.5.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                    color = if (isSelected) RoyalEmerald else Color(0xFF1E293B)
                  )
                )
                val paramDesc = when (method) {
                  CalculationMethod.KARACHI -> "Fajr 18° • Isha 18° (South Asia / Bangladesh)"
                  CalculationMethod.MAKKAH -> "Fajr 18.5° • Isha +90m (Saudi & Gulf)"
                  CalculationMethod.MWL -> "Fajr 18° • Isha 17° (Muslim World League)"
                  CalculationMethod.EGYPT -> "Fajr 19.5° • Isha 17.5° (Middle East & Africa)"
                  CalculationMethod.MUIS -> "Fajr 20° • Isha 18° (Singapore)"
                  CalculationMethod.JAKIM -> "Fajr 20° • Isha 18° (Malaysia)"
                  CalculationMethod.KEMENAG -> "Fajr 20° • Isha 18° (Indonesia)"
                  CalculationMethod.DUBAI -> "Fajr 18.2° • Isha 18.2° (UAE)"
                  CalculationMethod.ISNA -> "Fajr 15° • Isha 15° (North America)"
                }
                Text(
                  text = paramDesc,
                  style = TextStyle(fontSize = 10.sp, color = Color(0xFF64748B))
                )
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
          text = when (selectedLang) {
            "EN" -> "Asr Juristic Method"
            "AR" -> "مذهب صلاة العصر"
            else -> "আসর নামাজের মাযহাব (ছায়ার হিসাব)"
          },
          style = TextStyle(fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF334155))
        )

        Spacer(modifier = Modifier.height(6.dp))

        JuristicMethod.values().forEach { juristic ->
          val isSelected = selectedJuristic == juristic
          Surface(
            shape = RoundedCornerShape(10.dp),
            color = if (isSelected) Color(0xFFEFFDF5) else Color(0xFFF8FAFC),
            border = BorderStroke(1.dp, if (isSelected) RoyalEmerald else Color(0xFFE2E8F0)),
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 3.dp)
              .clip(RoundedCornerShape(10.dp))
              .clickable { selectedJuristic = juristic }
              .testTag("juristic_option_${juristic.name.lowercase()}")
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 8.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(
                imageVector = if (isSelected) Icons.Default.CheckCircle else Icons.Default.Check,
                contentDescription = null,
                tint = if (isSelected) RoyalEmerald else Color(0xFFCBD5E1),
                modifier = Modifier.size(18.dp)
              )
              Spacer(modifier = Modifier.width(8.dp))
              Column {
                Text(
                  text = if (selectedLang == "BN") juristic.titleBn else juristic.titleEn,
                  style = TextStyle(
                    fontSize = 12.5.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                    color = if (isSelected) RoyalEmerald else Color(0xFF1E293B)
                  )
                )
                Text(
                  text = if (juristic == JuristicMethod.HANAFI) "বস্তুর ছায়া দ্বিগুণ হওয়া (Shadow Multiplier = 2)" else "বস্তুর ছায়া একগুণ হওয়া (Shadow Multiplier = 1)",
                  style = TextStyle(fontSize = 10.sp, color = Color(0xFF64748B))
                )
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.End
        ) {
          TextButton(onClick = onDismiss) {
            Text(
              text = when (selectedLang) { "EN" -> "Cancel"; "AR" -> "إلغاء"; else -> "বাতিল" },
              color = Color(0xFF64748B)
            )
          }

          Spacer(modifier = Modifier.width(8.dp))

          Button(
            onClick = { onSave(selectedMethod, selectedJuristic) },
            colors = ButtonDefaults.buttonColors(containerColor = RoyalEmerald),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.testTag("save_prayer_settings_btn")
          ) {
            Text(
              text = when (selectedLang) { "EN" -> "Apply Settings"; "AR" -> "حفظ الإعدادات"; else -> "সংরক্ষণ করুন" },
              fontWeight = FontWeight.Bold
            )
          }
        }
      }
    }
  }
}
