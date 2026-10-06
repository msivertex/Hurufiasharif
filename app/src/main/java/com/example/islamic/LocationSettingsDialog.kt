package com.example.islamic

import android.Manifest
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.SoundManager
import kotlinx.coroutines.launch

private val RoyalEmerald = Color(0xFF0A5C36)
private val LightEmeraldBg = Color(0xFFF0FDF4)
private val EmeraldBorder = Color(0xFFA7F3D0)

@Composable
fun LocationSettingsDialog(
  selectedLang: String,
  offlineRepo: IslamicOfflineRepository,
  locationService: IslamicLocationService,
  soundManager: SoundManager? = null,
  onLocationUpdated: () -> Unit,
  onDismiss: () -> Unit
) {
  val context = LocalContext.current
  val coroutineScope = rememberCoroutineScope()

  var isGpsMode by remember { mutableStateOf(offlineRepo.isGpsLocated) }
  var isGpsLoading by remember { mutableStateOf(false) }
  var gpsStatusMessage by remember { mutableStateOf<String?>(null) }

  var searchQuery by remember { mutableStateOf("") }
  var selectedRegion by remember { mutableStateOf("ALL") }
  var isCustomSearching by remember { mutableStateOf(false) }

  // Location Permission Launcher
  val permissionLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.RequestMultiplePermissions()
  ) { permissions ->
    val granted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
      permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
    if (granted) {
      isGpsLoading = true
      locationService.fetchCurrentLocation(
        onSuccess = { lat, lng, name, tz, isGps, autoMethod, autoJuristic ->
          isGpsLoading = false
          isGpsMode = true
          offlineRepo.saveLocation(lat, lng, name, tz, isGps = true, autoMethod, autoJuristic)
          gpsStatusMessage = when (selectedLang) {
            "BN" -> "জিপিএস লোকেশন সনাক্ত হয়েছে: $name"
            "AR" -> "تم تحديد موقع GPS: $name"
            else -> "GPS Location resolved: $name"
          }
          soundManager?.playSuccessChime()
          onLocationUpdated()
        },
        onFailure = {
          isGpsLoading = false
          Toast.makeText(
            context,
            if (selectedLang == "BN") "জিপিএস লোকেশন পাওয়া যায়নি, ডিফল্ট লোকেশন ব্যবহার করা হচ্ছে"
            else "GPS could not be resolved",
            Toast.LENGTH_SHORT
          ).show()
        }
      )
    } else {
      isGpsMode = false
      Toast.makeText(
        context,
        if (selectedLang == "BN") "লোকেশন পারমিশন প্রয়োজন" else "Location permission is required",
        Toast.LENGTH_SHORT
      ).show()
    }
  }

  // Trigger GPS detection function
  fun activateGpsMode() {
    if (!locationService.hasLocationPermission()) {
      permissionLauncher.launch(
        arrayOf(
          Manifest.permission.ACCESS_FINE_LOCATION,
          Manifest.permission.ACCESS_COARSE_LOCATION
        )
      )
    } else {
      isGpsLoading = true
      locationService.fetchCurrentLocation(
        onSuccess = { lat, lng, name, tz, isGps, autoMethod, autoJuristic ->
          isGpsLoading = false
          isGpsMode = true
          offlineRepo.saveLocation(lat, lng, name, tz, isGps = true, autoMethod, autoJuristic)
          gpsStatusMessage = when (selectedLang) {
            "BN" -> "জিপিএস লোকেশন সনাক্ত হয়েছে: $name"
            "AR" -> "تم تحديد موقع GPS: $name"
            else -> "GPS Location resolved: $name"
          }
          soundManager?.playSuccessChime()
          onLocationUpdated()
        },
        onFailure = {
          isGpsLoading = false
          Toast.makeText(
            context,
            if (selectedLang == "BN") "জিপিএস লোকেশন সিঙ্ক ব্যর্থ হয়েছে" else "GPS sync failed",
            Toast.LENGTH_SHORT
          ).show()
        }
      )
    }
  }

  // Filter preset cities
  val filteredCities = remember(searchQuery, selectedRegion) {
    LocationPresets.defaultCities.filter { city ->
      val matchesRegion = when (selectedRegion) {
        "ALL" -> true
        "BD" -> city.regionCategory == "BD"
        "GULF" -> city.regionCategory == "GULF"
        "SOUTH_ASIA" -> city.regionCategory == "SOUTH_ASIA"
        "SOUTHEAST_ASIA" -> city.regionCategory == "SOUTHEAST_ASIA"
        "EAST_ASIA" -> city.regionCategory == "EAST_ASIA"
        "CENTRAL_WEST_ASIA" -> city.regionCategory == "CENTRAL_WEST_ASIA"
        else -> true
      }
      val matchesQuery = if (searchQuery.isBlank()) {
        true
      } else {
        val q = searchQuery.trim().lowercase()
        city.nameEn.lowercase().contains(q) ||
          city.nameBn.lowercase().contains(q) ||
          city.countryEn.lowercase().contains(q) ||
          city.countryBn.lowercase().contains(q) ||
          city.stateOrDivision.lowercase().contains(q)
      }
      matchesRegion && matchesQuery
    }
  }

  val regions = listOf(
    "ALL" to if (selectedLang == "BN") "সব এশিয়া" else if (selectedLang == "AR") "كل آسيا" else "All Asia",
    "BD" to if (selectedLang == "BN") "🇧🇩 বাংলাদেশ (৬৪ জেলা)" else "🇧🇩 Bangladesh",
    "GULF" to if (selectedLang == "BN") "🇸🇦 মধ্যপ্রাচ্য ও উপসাগর" else "🇸🇦 Gulf / Mideast",
    "SOUTH_ASIA" to if (selectedLang == "BN") "🇮🇳 দক্ষিণ এশিয়া" else "🇮🇳 South Asia",
    "SOUTHEAST_ASIA" to if (selectedLang == "BN") "🇲🇾 দক্ষিণ-পূর্ব এশিয়া" else "🇲🇾 Southeast Asia",
    "EAST_ASIA" to if (selectedLang == "BN") "🇯🇵 পূর্ব এশিয়া" else "🇯🇵 East Asia",
    "CENTRAL_WEST_ASIA" to if (selectedLang == "BN") "🇹🇷 মধ্য ও পশ্চিম এশিয়া" else "🇹🇷 Central/West Asia"
  )

  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(usePlatformDefaultWidth = false)
  ) {
    Card(
      shape = RoundedCornerShape(24.dp),
      colors = CardDefaults.cardColors(containerColor = Color.White),
      elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
      modifier = Modifier
        .fillMaxWidth(0.94f)
        .padding(vertical = 16.dp)
        .testTag("location_settings_dialog")
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(20.dp)
      ) {
        // ==========================================
        // HEADER
        // ==========================================
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
          ) {
            Box(
              modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(
                  Brush.linearGradient(listOf(Color(0xFF10B981), RoyalEmerald))
                ),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(24.dp)
              )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
              Text(
                text = when (selectedLang) {
                  "BN" -> "লোকেশন সেটিংস"
                  "AR" -> "إعدادات الموقع"
                  else -> "Location Settings"
                },
                style = TextStyle(
                  fontSize = 18.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color(0xFF0F172A)
                )
              )
              Text(
                text = when (selectedLang) {
                  "BN" -> "স্বয়ংক্রিয় জিপিএস ও এশিয়া মহাদেশীয় সার্চ"
                  "AR" -> "تحديد تلقائي عبر GPS وبحث مدن آسيا"
                  else -> "Automatic GPS & Asia Manual Search"
                },
                style = TextStyle(
                  fontSize = 11.5.sp,
                  color = Color(0xFF64748B)
                )
              )
            }
          }

          IconButton(
            onClick = onDismiss,
            modifier = Modifier.testTag("close_location_dialog")
          ) {
            Icon(
              imageVector = Icons.Default.Close,
              contentDescription = "Close",
              tint = Color(0xFF64748B)
            )
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ==========================================
        // MODE A: AUTOMATIC GPS (RECOMMENDED)
        // ==========================================
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(
            containerColor = if (isGpsMode) LightEmeraldBg else Color(0xFFF8FAFC)
          ),
          border = BorderStroke(
            1.5.dp,
            if (isGpsMode) RoyalEmerald else Color(0xFFE2E8F0)
          ),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("gps_mode_card")
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
              Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
              ) {
                Box(
                  modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(
                      if (isGpsMode) RoyalEmerald.copy(alpha = 0.15f) else Color(0xFFE2E8F0)
                    ),
                  contentAlignment = Alignment.Center
                ) {
                  Icon(
                    imageVector = Icons.Default.MyLocation,
                    contentDescription = null,
                    tint = if (isGpsMode) RoyalEmerald else Color(0xFF64748B),
                    modifier = Modifier.size(20.dp)
                  )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                  Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                      text = when (selectedLang) {
                        "BN" -> "অটোমেটিক জিপিএস"
                        "AR" -> "نظام تحديد المواقع (GPS)"
                        else -> "Automatic GPS"
                      },
                      style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                      )
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Surface(
                      shape = RoundedCornerShape(6.dp),
                      color = Color(0xFFDCFCE7)
                    ) {
                      Text(
                        text = if (selectedLang == "BN") "সুপারিশকৃত" else "Recommended",
                        style = TextStyle(
                          fontSize = 9.5.sp,
                          fontWeight = FontWeight.Bold,
                          color = RoyalEmerald
                        ),
                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                      )
                    }
                  }

                  Text(
                    text = when (selectedLang) {
                      "BN" -> "ডিভাইসের স্যাটেলাইট ও নেটওয়ার্ক লোকেশন"
                      "AR" -> "تحديد الموقع الدقيق عبر أجهزة الاستشعار"
                      else -> "Real-time device coordinates & timezone"
                    },
                    style = TextStyle(fontSize = 11.sp, color = Color(0xFF64748B))
                  )
                }
              }

              Switch(
                checked = isGpsMode,
                onCheckedChange = { checked ->
                  if (checked) {
                    activateGpsMode()
                  } else {
                    isGpsMode = false
                    offlineRepo.isGpsLocated = false
                    onLocationUpdated()
                  }
                },
                colors = SwitchDefaults.colors(
                  checkedThumbColor = Color.White,
                  checkedTrackColor = RoyalEmerald,
                  uncheckedThumbColor = Color.White,
                  uncheckedTrackColor = Color(0xFFCBD5E1)
                ),
                modifier = Modifier.testTag("gps_toggle_switch")
              )
            }

            // GPS Active status indicator & Refresh button
            if (isGpsMode) {
              Spacer(modifier = Modifier.height(10.dp))
              HorizontalDivider(color = EmeraldBorder, thickness = 0.8.dp)
              Spacer(modifier = Modifier.height(10.dp))

              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Column(modifier = Modifier.weight(1f)) {
                  Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                      modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF10B981))
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                      text = if (selectedLang == "BN") "🟢 জিপিএস সক্রিয়" else "🟢 GPS Active",
                      style = TextStyle(
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = RoyalEmerald
                      )
                    )
                  }
                  Text(
                    text = offlineRepo.locationName.ifBlank { "Auto Coords" },
                    style = TextStyle(
                      fontSize = 12.sp,
                      fontWeight = FontWeight.Medium,
                      color = Color(0xFF1E293B)
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                  )
                  Text(
                    text = String.format(
                      java.util.Locale.US,
                      "%.4f° N, %.4f° E (GMT %+.1f)",
                      offlineRepo.latitude,
                      offlineRepo.longitude,
                      offlineRepo.timezoneHours
                    ),
                    style = TextStyle(fontSize = 10.5.sp, color = Color(0xFF64748B))
                  )
                }

                ElevatedButton(
                  onClick = { activateGpsMode() },
                  enabled = !isGpsLoading,
                  shape = RoundedCornerShape(10.dp),
                  colors = ButtonDefaults.elevatedButtonColors(
                    containerColor = RoyalEmerald,
                    contentColor = Color.White
                  ),
                  contentPadding = androidx.compose.foundation.layout.PaddingValues(
                    horizontal = 10.dp,
                    vertical = 6.dp
                  ),
                  modifier = Modifier.testTag("refresh_gps_btn")
                ) {
                  if (isGpsLoading) {
                    CircularProgressIndicator(
                      modifier = Modifier.size(16.dp),
                      strokeWidth = 2.dp,
                      color = Color.White
                    )
                  } else {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                      Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp)
                      )
                      Spacer(modifier = Modifier.width(4.dp))
                      Text(
                        text = if (selectedLang == "BN") "রিফ্রেশ" else "Refresh",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                      )
                    }
                  }
                }
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // ==========================================
        // SECTION DIVIDER / OR MANUAL SEARCH
        // ==========================================
        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically
        ) {
          HorizontalDivider(modifier = Modifier.weight(1f), color = Color(0xFFE2E8F0))
          Text(
            text = when (selectedLang) {
              "BN" -> "অথবা ম্যানুয়াল সার্চ (এশিয়া)"
              "AR" -> "أو بحث يدوي (قارة آسيا)"
              else -> "OR MANUAL SEARCH (ASIA)"
            },
            style = TextStyle(
              fontSize = 10.5.sp,
              fontWeight = FontWeight.SemiBold,
              color = Color(0xFF94A3B8)
            ),
            modifier = Modifier.padding(horizontal = 8.dp)
          )
          HorizontalDivider(modifier = Modifier.weight(1f), color = Color(0xFFE2E8F0))
        }

        Spacer(modifier = Modifier.height(10.dp))

        // ==========================================
        // MODE B: MANUAL SEARCH INPUT (ASIA COVERAGE)
        // ==========================================
        OutlinedTextField(
          value = searchQuery,
          onValueChange = { searchQuery = it },
          placeholder = {
            Text(
              text = when (selectedLang) {
                "BN" -> "শহর, জেলা বা দেশ খুঁজুন (যেমন: ঢাকা, সিলেট, Tokyo, Dubai...)"
                "AR" -> "ابحث عن المدينة (مثل: مكة، دبي، طوكيو...)"
                else -> "Search city, district, country (e.g. Dhaka, Tokyo)..."
              },
              style = TextStyle(fontSize = 12.sp, color = Color(0xFF94A3B8)),
              maxLines = 1,
              overflow = TextOverflow.Ellipsis
            )
          },
          leadingIcon = {
            Icon(
              imageVector = Icons.Default.Search,
              contentDescription = "Search",
              tint = RoyalEmerald,
              modifier = Modifier.size(20.dp)
            )
          },
          trailingIcon = {
            if (searchQuery.isNotEmpty()) {
              IconButton(onClick = { searchQuery = "" }) {
                Icon(
                  imageVector = Icons.Default.Close,
                  contentDescription = "Clear",
                  tint = Color(0xFF94A3B8),
                  modifier = Modifier.size(16.dp)
                )
              }
            }
          },
          shape = RoundedCornerShape(12.dp),
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = RoyalEmerald,
            unfocusedBorderColor = Color(0xFFCBD5E1),
            focusedContainerColor = Color(0xFFF8FAFC),
            unfocusedContainerColor = Color(0xFFF8FAFC)
          ),
          singleLine = true,
          modifier = Modifier
            .fillMaxWidth()
            .testTag("manual_city_search_input")
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Region Filter Chips (Horizontally Scrollable)
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          regions.forEach { (code, label) ->
            val isSelected = selectedRegion == code
            Surface(
              shape = RoundedCornerShape(16.dp),
              color = if (isSelected) RoyalEmerald else Color(0xFFF1F5F9),
              border = BorderStroke(1.dp, if (isSelected) RoyalEmerald else Color(0xFFE2E8F0)),
              modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .clickable { selectedRegion = code }
            ) {
              Text(
                text = label,
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

        // ==========================================
        // CITIES & DISTRICTS LIST
        // ==========================================
        LazyColumn(
          modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 240.dp)
            .testTag("asian_cities_list")
        ) {
          if (filteredCities.isEmpty()) {
            item {
              Column(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
              ) {
                Text(
                  text = when (selectedLang) {
                    "BN" -> "তালিকায় পাওয়া যায়নি"
                    "AR" -> "لم يتم العثور على المدينة"
                    else -> "No preset city matched"
                  },
                  style = TextStyle(fontSize = 12.5.sp, color = Color(0xFF94A3B8))
                )

                if (searchQuery.isNotBlank()) {
                  Spacer(modifier = Modifier.height(8.dp))
                  OutlinedButton(
                    onClick = {
                      isCustomSearching = true
                      locationService.searchCustomLocation(searchQuery) { resolvedPreset ->
                        isCustomSearching = false
                        if (resolvedPreset != null) {
                          isGpsMode = false
                          offlineRepo.saveLocation(
                            lat = resolvedPreset.latitude,
                            lng = resolvedPreset.longitude,
                            name = "${resolvedPreset.nameBn}, ${resolvedPreset.countryBn}",
                            tz = resolvedPreset.timezoneHours,
                            isGps = false,
                            autoDetectedMethod = resolvedPreset.defaultMethod,
                            autoDetectedJuristic = resolvedPreset.defaultJuristic,
                            cityName = resolvedPreset.nameEn
                          )
                          soundManager?.playSuccessChime()
                          onLocationUpdated()
                          onDismiss()
                        } else {
                          Toast.makeText(
                            context,
                            if (selectedLang == "BN") "'$searchQuery' খুঁজে পাওয়া যায়নি"
                            else "Location not found for '$searchQuery'",
                            Toast.LENGTH_SHORT
                          ).show()
                        }
                      }
                    },
                    enabled = !isCustomSearching,
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, RoyalEmerald)
                  ) {
                    if (isCustomSearching) {
                      CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                    } else {
                      Text(
                        text = if (selectedLang == "BN") "অনলাইনে খুঁজুন: \"$searchQuery\""
                        else "Search online: \"$searchQuery\"",
                        fontSize = 11.5.sp,
                        color = RoyalEmerald,
                        fontWeight = FontWeight.Bold
                      )
                    }
                  }
                }
              }
            }
          } else {
            items(filteredCities) { city ->
              val isCurrentSelected = !isGpsMode &&
                (offlineRepo.selectedCityName.equals(city.nameEn, ignoreCase = true) ||
                  offlineRepo.locationName.contains(city.nameBn) ||
                  offlineRepo.locationName.contains(city.nameEn))

              Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                  containerColor = if (isCurrentSelected) LightEmeraldBg else Color(0xFFF8FAFC)
                ),
                border = BorderStroke(
                  1.dp,
                  if (isCurrentSelected) RoyalEmerald else Color(0xFFE2E8F0)
                ),
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(vertical = 3.dp)
                  .clip(RoundedCornerShape(12.dp))
                  .clickable {
                    isGpsMode = false
                    offlineRepo.saveLocation(
                      lat = city.latitude,
                      lng = city.longitude,
                      name = "${city.nameBn}, ${city.countryBn}",
                      tz = city.timezoneHours,
                      isGps = false,
                      autoDetectedMethod = city.defaultMethod,
                      autoDetectedJuristic = city.defaultJuristic,
                      cityName = city.nameEn
                    )
                    soundManager?.playSuccessChime()
                    onLocationUpdated()
                    onDismiss()
                  }
                  .testTag("city_item_${city.nameEn.lowercase().replace(" ", "_")}")
              ) {
                Row(
                  modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 9.dp),
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.SpaceBetween
                ) {
                  Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                  ) {
                    Box(
                      modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(
                          if (isCurrentSelected) RoyalEmerald else Color(0xFFE2E8F0)
                        ),
                      contentAlignment = Alignment.Center
                    ) {
                      Icon(
                        imageVector = if (isCurrentSelected) Icons.Default.Check else Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = if (isCurrentSelected) Color.White else Color(0xFF64748B),
                        modifier = Modifier.size(16.dp)
                      )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                      Text(
                        text = if (selectedLang == "BN") "${city.nameBn} (${city.nameEn})" else city.nameEn,
                        style = TextStyle(
                          fontSize = 13.sp,
                          fontWeight = if (isCurrentSelected) FontWeight.Bold else FontWeight.Medium,
                          color = if (isCurrentSelected) RoyalEmerald else Color(0xFF0F172A)
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                      )
                      Text(
                        text = "${if (selectedLang == "BN") city.countryBn else city.countryEn}${if (city.stateOrDivision.isNotBlank()) " • ${city.stateOrDivision}" else ""}",
                        style = TextStyle(fontSize = 11.sp, color = Color(0xFF64748B)),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                      )
                    }
                  }

                  // Badges: Timezone & Calculation Method
                  Column(horizontalAlignment = Alignment.End) {
                    Surface(
                      shape = RoundedCornerShape(6.dp),
                      color = Color(0xFFE2E8F0)
                    ) {
                      Text(
                        text = String.format(java.util.Locale.US, "GMT%+.0f", city.timezoneHours),
                        style = TextStyle(
                          fontSize = 9.5.sp,
                          fontWeight = FontWeight.Bold,
                          color = Color(0xFF334155)
                        ),
                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                      )
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                      text = city.defaultMethod.name,
                      style = TextStyle(fontSize = 9.sp, color = Color(0xFF94A3B8))
                    )
                  }
                }
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Close / Done Button
        ElevatedButton(
          onClick = onDismiss,
          shape = RoundedCornerShape(12.dp),
          colors = ButtonDefaults.elevatedButtonColors(
            containerColor = RoyalEmerald,
            contentColor = Color.White
          ),
          modifier = Modifier
            .fillMaxWidth()
            .height(44.dp)
            .testTag("location_dialog_done_btn")
        ) {
          Text(
            text = when (selectedLang) {
              "BN" -> "ঠিক আছে (সম্পন্ন)"
              "AR" -> "تم الإغلاق"
              else -> "Done"
            },
            style = TextStyle(fontSize = 13.5.sp, fontWeight = FontWeight.Bold)
          )
        }
      }
    }
  }
}
