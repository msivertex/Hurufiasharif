package com.example.islamic

import android.graphics.PointF
import androidx.compose.animation.core.LinearEasing
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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import java.util.Locale
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * Atmospheric Day/Night theme categories for the dynamic live sky canvas
 */
enum class SkyAtmosphere {
  DAY_SUNSHINE,    // Ishraq, Dhuhr, Asr: Soft sky-blue gradient with slowly floating transparent clouds
  SUNSET_TWILIGHT, // Maghrib: Warm golden-purple twilight gradient
  NIGHT_STARRY     // Isha, Tahajjud, Fajr: Deep midnight blue canvas with twinkling stars & dome silhouette
}

/**
 * Helper to convert digits to Bengali / Arabic numbers
 */
private fun toBnDigits(str: String): String {
  val bnDigits = charArrayOf('০', '১', '২', '৩', '৪', '৫', '৬', '৭', '৮', '৯')
  val sb = StringBuilder()
  for (c in str) {
    if (c in '0'..'9') sb.append(bnDigits[c - '0']) else sb.append(c)
  }
  return sb.toString()
}

private fun toBnDigits(number: Long): String = toBnDigits(number.toString())
private fun toBnDigits(number: Int): String = toBnDigits(number.toString())

private fun toArDigits(str: String): String {
  val arDigits = charArrayOf('٠', '١', '٢', '٣', '٤', '٥', '٦', '٧', '٨', '٩')
  val sb = StringBuilder()
  for (c in str) {
    if (c in '0'..'9') sb.append(arDigits[c - '0']) else sb.append(c)
  }
  return sb.toString()
}

private fun toArDigits(number: Long): String = toArDigits(number.toString())
private fun toArDigits(number: Int): String = toArDigits(number.toString())

private fun formatStartTime(timeStr: String, lang: String): String {
  return when (lang) {
    "BN" -> toBnDigits(timeStr)
    "AR" -> toArDigits(timeStr)
    else -> timeStr
  }
}

/**
 * All-In-One Dynamic Glassmorphism Prayer Times Hero Card
 */
@Composable
fun DynamicPrayerTimesHeroCard(
  prayerSchedule: DailyPrayerSchedule,
  selectedLang: String,
  offlineRepo: IslamicOfflineRepository,
  onOpenFullTimetable: () -> Unit,
  onRefreshLocation: (() -> Unit)? = null,
  modifier: Modifier = Modifier
) {
  var currentTimeMillis by remember { mutableLongStateOf(System.currentTimeMillis()) }

  // Sync with device clock every second
  LaunchedEffect(Unit) {
    while (true) {
      currentTimeMillis = System.currentTimeMillis()
      delay(1000L)
    }
  }

  // Live evaluated prayer schedule
  val liveSchedule = remember(
    currentTimeMillis / 1000L,
    prayerSchedule.latitude,
    prayerSchedule.longitude,
    prayerSchedule.locationName,
    prayerSchedule.timezoneOffsetHours,
    prayerSchedule.method,
    prayerSchedule.juristicMethod,
    prayerSchedule.isGpsLocated
  ) {
    PrayerTimesCalculator.calculate(
      latitude = prayerSchedule.latitude,
      longitude = prayerSchedule.longitude,
      timezoneOffset = prayerSchedule.timezoneOffsetHours,
      locationName = prayerSchedule.locationName,
      method = prayerSchedule.method,
      juristicMethod = prayerSchedule.juristicMethod,
      currentTimeMillis = currentTimeMillis,
      isGpsLocated = prayerSchedule.isGpsLocated
    )
  }

  val now = currentTimeMillis
  val currentPrayer = liveSchedule.currentPrayer
  val nextPrayer = liveSchedule.nextPrayer
  val currentWaqtStart = liveSchedule.currentPrayerStartMillis
  val currentWaqtEnd = liveSchedule.currentPrayerEndMillis
  val nextWaqtStart = liveSchedule.nextPrayerStartMillis

  // Countdown timer calculations
  val remainingWaqtSeconds = maxOf(0L, (nextWaqtStart - now) / 1000L)
  val waqtRemHours = remainingWaqtSeconds / 3600L
  val waqtRemMins = (remainingWaqtSeconds % 3600L) / 60L
  val waqtRemSecs = remainingWaqtSeconds % 60L

  // Elapsed prayer window fraction (0.0f - 1.0f)
  val totalPrayerWindow = maxOf(60000L, currentWaqtEnd - currentWaqtStart)
  val elapsedPrayerTime = (now - currentWaqtStart).coerceIn(0L, totalPrayerWindow)
  val prayerProgress = (elapsedPrayerTime.toFloat() / totalPrayerWindow.toFloat()).coerceIn(0.01f, 0.99f)

  // Hijri Date and Day of Month (1-30) for exact Lunar Phase calculations
  val hijriInfo = remember(offlineRepo.hijriDayAdjustment, currentTimeMillis / 86400000L) {
    HijriCalendarCalculator.calculateHijri(dayAdjustment = offlineRepo.hijriDayAdjustment)
  }
  val hijriDayOfMonth = hijriInfo.day.coerceIn(1, 30)

  // Determine active atmosphere based on current waqt
  val atmosphere = when (currentPrayer.id) {
    "sunrise", "dhuhr", "asr" -> SkyAtmosphere.DAY_SUNSHINE
    "maghrib" -> SkyAtmosphere.SUNSET_TWILIGHT
    else -> SkyAtmosphere.NIGHT_STARRY // "isha", "tahajjud", "fajr"
  }

  // Localized texts
  val hijriFormatted = when (selectedLang) {
    "BN" -> "${toBnDigits(hijriInfo.day)}ই ${hijriInfo.monthNameBn} ${toBnDigits(hijriInfo.year)} হিজরী"
    "AR" -> "${toArDigits(hijriInfo.day)} ${hijriInfo.monthNameAr} ${toArDigits(hijriInfo.year)} هـ"
    else -> "${hijriInfo.day} ${hijriInfo.monthNameEn} ${hijriInfo.year} AH"
  }

  val currentWaqtName = when (selectedLang) {
    "BN" -> currentPrayer.nameBn
    "AR" -> currentPrayer.nameAr
    else -> currentPrayer.nameEn
  }

  val nextWaqtName = when (selectedLang) {
    "BN" -> nextPrayer.nameBn
    "AR" -> nextPrayer.nameAr
    else -> nextPrayer.nameEn
  }

  val nextWaqtStartTime = formatStartTime(nextPrayer.timeFormatted, selectedLang)

  val countdownText = when (selectedLang) {
    "BN" -> {
      if (waqtRemHours > 0) {
        "বাকি: ${toBnDigits(waqtRemHours)} ঘণ্টা ${toBnDigits(waqtRemMins)} মি. ${toBnDigits(waqtRemSecs)} সে."
      } else {
        "বাকি: ${toBnDigits(waqtRemMins)} মি. ${toBnDigits(waqtRemSecs)} সে."
      }
    }
    "AR" -> {
      if (waqtRemHours > 0) {
        "متبقي: ${toArDigits(waqtRemHours)} س ${toArDigits(waqtRemMins)} د ${toArDigits(waqtRemSecs)} ث"
      } else {
        "متبقي: ${toArDigits(waqtRemMins)} د ${toArDigits(waqtRemSecs)} ث"
      }
    }
    else -> {
      if (waqtRemHours > 0) {
        "Left: ${waqtRemHours}h ${waqtRemMins}m ${waqtRemSecs}s"
      } else {
        "Left: ${waqtRemMins}m ${waqtRemSecs}s"
      }
    }
  }

  // Base Atmospheric Gradients (Rich realistic weather gradients)
  val skyGradientColors = when (atmosphere) {
    SkyAtmosphere.DAY_SUNSHINE -> listOf(
      Color(0xFF0284C7), // Deep Azure Sky
      Color(0xFF38BDF8), // Radiant Sky Blue
      Color(0xFF7DD3FC)  // Soft Horizon Mist
    )
    SkyAtmosphere.SUNSET_TWILIGHT -> listOf(
      Color(0xFF2E1065), // Deep Twilight Violet
      Color(0xFF7C2D12), // Deep Sunset Crimson
      Color(0xFFEA580C), // Orange Sunset
      Color(0xFFF59E0B)  // Warm Golden Horizon
    )
    SkyAtmosphere.NIGHT_STARRY -> listOf(
      Color(0xFF020617), // Pitch Dark Deep Cosmic Space
      Color(0xFF0B132B), // Deep Midnight Navy
      Color(0xFF0F172A)  // Slate Navy Atmosphere
    )
  }

  Card(
    shape = RoundedCornerShape(26.dp),
    colors = CardDefaults.cardColors(containerColor = Color.Transparent),
    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
    border = BorderStroke(
      width = 1.2.dp,
      color = Color.White.copy(alpha = if (atmosphere == SkyAtmosphere.NIGHT_STARRY) 0.18f else 0.35f)
    ),
    modifier = modifier
      .fillMaxWidth()
      .testTag("prayer_time_card")
  ) {
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .background(Brush.verticalGradient(skyGradientColors))
    ) {
      // 2. DYNAMIC SKY CANVAS & ATMOSPHERIC ANIMATIONS
      AtmosphericSkyCanvas(
        atmosphere = atmosphere,
        modifier = Modifier.matchParentSize()
      )

      // Foreground UI layered with Frosted Glass Panels
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 14.dp)
      ) {
        // 1. TOP HEADER SECTION (Location left, Hijri date right)
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          // Left: Location Pill with Map Pin
          Surface(
            shape = RoundedCornerShape(12.dp),
            color = Color.Black.copy(alpha = 0.28f),
            border = BorderStroke(0.7.dp, Color.White.copy(alpha = 0.25f)),
            modifier = Modifier
              .clickable { onRefreshLocation?.invoke() }
              .testTag("location_pill")
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier.padding(horizontal = 9.dp, vertical = 5.dp)
            ) {
              Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = "Location",
                tint = if (atmosphere == SkyAtmosphere.SUNSET_TWILIGHT) Color(0xFFFDE68A) else Color(0xFF7DD3FC),
                modifier = Modifier.size(15.dp)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = liveSchedule.locationName,
                style = TextStyle(
                  fontSize = 12.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color.White
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
              )
            }
          }

          Spacer(modifier = Modifier.width(8.dp))

          // Right: Current Hijri Date
          Surface(
            shape = RoundedCornerShape(12.dp),
            color = Color.Black.copy(alpha = 0.28f),
            border = BorderStroke(0.7.dp, Color.White.copy(alpha = 0.22f))
          ) {
            Text(
              text = hijriFormatted,
              style = TextStyle(
                fontSize = 11.5.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White.copy(alpha = 0.95f)
              ),
              modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
              maxLines = 1
            )
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // 3. HIJRI MOON PHASE ENGINE & GLOWING GLASS ARC
        GlowingCelestialArc(
          progress = prayerProgress,
          atmosphere = atmosphere,
          hijriDay = hijriDayOfMonth,
          selectedLang = selectedLang,
          startFormatted = formatStartTime(currentPrayer.timeFormatted, selectedLang),
          endFormatted = formatStartTime(nextPrayer.timeFormatted, selectedLang),
          midpointLabel = when {
            atmosphere == SkyAtmosphere.NIGHT_STARRY -> if (selectedLang == "BN") "মধ্যরাত / তাহাজ্জুদ" else if (selectedLang == "AR") "منتصف الليل / التهجد" else "Midnight / Tahajjud"
            atmosphere == SkyAtmosphere.SUNSET_TWILIGHT -> if (selectedLang == "BN") "গোধূলি" else if (selectedLang == "AR") "الشفق" else "Twilight"
            else -> if (selectedLang == "BN") "মধ্যাহ্ন" else if (selectedLang == "AR") "الظهيرة" else "Midday"
          },
          modifier = Modifier
            .fillMaxWidth()
            .height(132.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        // 4. FROSTED GLASS CONTAINER: CURRENT WAQT, TIMER & PROGRESS
        // Improved dark mode contrast with rgba(255,255,255,0.18) border and p-4 (16.dp) padding
        val innerCardBg = when (atmosphere) {
          SkyAtmosphere.DAY_SUNSHINE -> Color(0xFF034A75).copy(alpha = 0.45f)
          SkyAtmosphere.SUNSET_TWILIGHT -> Color(0xFF1E072E).copy(alpha = 0.50f)
          SkyAtmosphere.NIGHT_STARRY -> Color(0xFF020617).copy(alpha = 0.60f)
        }
        val innerCardBorder = Color.White.copy(alpha = 0.18f)

        Surface(
          shape = RoundedCornerShape(20.dp),
          color = innerCardBg,
          border = BorderStroke(1.dp, innerCardBorder),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            // Waqt Name & Live Countdown Timer (Flex-Row with White-Space: Nowrap & dynamic scaling)
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              // Left: Waqt Name + Active Status Badge
              Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f, fill = false)
              ) {
                Text(
                  text = currentWaqtName,
                  style = TextStyle(
                    fontSize = 21.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                  ),
                  maxLines = 1,
                  overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.width(8.dp))
                // Active status badge
                Surface(
                  shape = RoundedCornerShape(6.dp),
                  color = Color(0xFF10B981).copy(alpha = 0.35f),
                  border = BorderStroke(0.8.dp, Color(0xFF34D399))
                ) {
                  Text(
                    text = if (selectedLang == "BN") "চলমান" else if (selectedLang == "AR") "جارٍ" else "Active",
                    style = TextStyle(
                      fontSize = 10.sp,
                      fontWeight = FontWeight.Bold,
                      color = Color(0xFFE6FFFA)
                    ),
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                    maxLines = 1
                  )
                }
              }

              Spacer(modifier = Modifier.width(8.dp))

              // Right: Live Countdown Timer Box (Flex-Row, No-Wrap, Dynamic Font Scaling)
              Surface(
                shape = RoundedCornerShape(10.dp),
                color = Color.Black.copy(alpha = 0.42f),
                border = BorderStroke(0.9.dp, Color.White.copy(alpha = 0.24f))
              ) {
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.Center,
                  modifier = Modifier.padding(horizontal = 9.dp, vertical = 4.5.dp)
                ) {
                  val countdownFontSize = when {
                    countdownText.length > 25 -> 10.5.sp
                    countdownText.length > 18 -> 11.5.sp
                    else -> 12.5.sp
                  }
                  Text(
                    text = countdownText,
                    style = TextStyle(
                      fontSize = countdownFontSize,
                      fontWeight = FontWeight.Bold,
                      color = if (atmosphere == SkyAtmosphere.SUNSET_TWILIGHT) Color(0xFFFEF08A) else Color(0xFFFDE047)
                    ),
                    maxLines = 1,
                    softWrap = false,
                    overflow = TextOverflow.Ellipsis
                  )
                }
              }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Sleek progress bar
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(Color.White.copy(alpha = 0.20f))
                .testTag("salat_progress_bar")
            ) {
              Box(
                modifier = Modifier
                  .fillMaxWidth(prayerProgress)
                  .fillMaxHeight()
                  .clip(RoundedCornerShape(3.dp))
                  .background(
                    Brush.horizontalGradient(
                      if (atmosphere == SkyAtmosphere.SUNSET_TWILIGHT) listOf(Color(0xFFF59E0B), Color(0xFFFBBF24))
                      else listOf(Color(0xFF38BDF8), Color(0xFFFACC15))
                    )
                  )
              )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Next Waqt preview text
            Text(
              text = if (selectedLang == "BN") "পরবর্তী ওয়াক্ত: $nextWaqtName ($nextWaqtStartTime থেকে শুরু)"
              else if (selectedLang == "AR") "الوقت القادم: $nextWaqtName (يبدأ في $nextWaqtStartTime)"
              else "Next Waqt: $nextWaqtName (Starts at $nextWaqtStartTime)",
              style = TextStyle(
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White.copy(alpha = 0.92f)
              ),
              maxLines = 1,
              softWrap = false,
              overflow = TextOverflow.Ellipsis
            )
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 5. BOTTOM ACTION BUTTON (Frosted glass primary button "পূর্ণ সময়সূচি")
        Surface(
          shape = RoundedCornerShape(12.dp),
          color = Color.White.copy(alpha = 0.22f),
          border = BorderStroke(1.dp, Color.White.copy(alpha = 0.45f)),
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable { onOpenFullTimetable() }
            .testTag("btn_view_full_timetable")
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 10.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.Default.CalendarMonth,
              contentDescription = null,
              tint = Color.White,
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = if (selectedLang == "BN") "পূর্ণ সময়সূচি" else "Full Monthly Timetable",
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
}

/**
 * Data model for realistic celestial particle stars with astronomical properties
 */
private data class RealisticStar(
  val xRatio: Float,
  val yRatio: Float,
  val baseRadiusDp: Float,
  val color: Color,
  val phaseOffset: Float,
  val hasSpike: Boolean = false
)

private val REALISTIC_STARFIELD = listOf(
  RealisticStar(0.10f, 0.12f, 2.2f, Color(0xFFE0F2FE), 0.0f, hasSpike = true), // Sirius-like bright star
  RealisticStar(0.24f, 0.18f, 1.4f, Color(0xFFFFFFFF), 1.2f),
  RealisticStar(0.35f, 0.08f, 1.1f, Color(0xFFFEF3C7), 2.5f),
  RealisticStar(0.48f, 0.15f, 1.9f, Color(0xFFFFFFFF), 0.8f, hasSpike = true), // Vega-like bright star
  RealisticStar(0.62f, 0.11f, 1.3f, Color(0xFFE0F2FE), 3.1f),
  RealisticStar(0.78f, 0.14f, 2.0f, Color(0xFFFEF3C7), 1.7f, hasSpike = true), // Arcturus-like golden star
  RealisticStar(0.90f, 0.09f, 1.2f, Color(0xFFFFFFFF), 4.2f),
  RealisticStar(0.06f, 0.28f, 1.0f, Color(0xFFE0F2FE), 2.1f),
  RealisticStar(0.18f, 0.32f, 1.5f, Color(0xFFFFFFFF), 0.5f),
  RealisticStar(0.29f, 0.24f, 0.9f, Color(0xFFFEF3C7), 3.8f),
  RealisticStar(0.42f, 0.29f, 1.2f, Color(0xFFFFFFFF), 1.9f),
  RealisticStar(0.55f, 0.22f, 1.6f, Color(0xFFE0F2FE), 4.7f),
  RealisticStar(0.69f, 0.30f, 1.1f, Color(0xFFFFFFFF), 0.3f),
  RealisticStar(0.85f, 0.25f, 1.4f, Color(0xFFFEF3C7), 2.8f),
  RealisticStar(0.94f, 0.31f, 1.0f, Color(0xFFE0F2FE), 5.1f),
  RealisticStar(0.14f, 0.44f, 1.2f, Color(0xFFFFFFFF), 1.4f),
  RealisticStar(0.26f, 0.49f, 0.9f, Color(0xFFFEF3C7), 3.4f),
  RealisticStar(0.38f, 0.42f, 1.3f, Color(0xFFE0F2FE), 0.9f),
  RealisticStar(0.52f, 0.47f, 1.1f, Color(0xFFFFFFFF), 2.3f),
  RealisticStar(0.65f, 0.43f, 1.5f, Color(0xFFFEF3C7), 4.0f),
  RealisticStar(0.77f, 0.48f, 1.0f, Color(0xFFFFFFFF), 1.1f),
  RealisticStar(0.89f, 0.45f, 1.3f, Color(0xFFE0F2FE), 5.4f),
  RealisticStar(0.08f, 0.60f, 1.0f, Color(0xFFFFFFFF), 0.6f),
  RealisticStar(0.21f, 0.63f, 1.2f, Color(0xFFFEF3C7), 2.9f),
  RealisticStar(0.34f, 0.58f, 0.8f, Color(0xFFE0F2FE), 4.4f),
  RealisticStar(0.58f, 0.62f, 1.1f, Color(0xFFFFFFFF), 1.8f),
  RealisticStar(0.72f, 0.59f, 1.4f, Color(0xFFFEF3C7), 3.2f),
  RealisticStar(0.83f, 0.64f, 0.9f, Color(0xFFE0F2FE), 0.2f),
  RealisticStar(0.96f, 0.58f, 1.1f, Color(0xFFFFFFFF), 4.9f)
)

/**
 * 2. LIVE AMBIENT CANVAS BACKGROUND
 * Photorealistic Weather App Atmosphere:
 * - Daytime: Multi-layered volumetric drifting clouds with realistic lighting and soft atmospheric god rays.
 * - Sunset: Golden-purple twilight horizon with rose-gilded cloud silhouettes.
 * - Nighttime: Cosmic starry night with 30+ shimmering stars, Milky Way nebula dust, and occasional shooting star.
 */
@Composable
private fun AtmosphericSkyCanvas(
  atmosphere: SkyAtmosphere,
  modifier: Modifier = Modifier
) {
  val infiniteTransition = rememberInfiniteTransition(label = "sky_anim")

  // Multi-layered cloud horizontal parallax drifts
  val cloudOffsetBack by infiniteTransition.animateFloat(
    initialValue = 0f,
    targetValue = 1f,
    animationSpec = infiniteRepeatable(
      animation = tween(38000, easing = LinearEasing),
      repeatMode = RepeatMode.Restart
    ),
    label = "cloud_back"
  )

  val cloudOffsetMid by infiniteTransition.animateFloat(
    initialValue = 0f,
    targetValue = 1f,
    animationSpec = infiniteRepeatable(
      animation = tween(24000, easing = LinearEasing),
      repeatMode = RepeatMode.Restart
    ),
    label = "cloud_mid"
  )

  val cloudOffsetFore by infiniteTransition.animateFloat(
    initialValue = 0f,
    targetValue = 1f,
    animationSpec = infiniteRepeatable(
      animation = tween(16000, easing = LinearEasing),
      repeatMode = RepeatMode.Restart
    ),
    label = "cloud_fore"
  )

  // Atmospheric sun god ray gentle breathing shimmer
  val sunRayShimmer by infiniteTransition.animateFloat(
    initialValue = 0.10f,
    targetValue = 0.25f,
    animationSpec = infiniteRepeatable(
      animation = tween(4200, easing = LinearEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "sun_ray_shimmer"
  )

  // Star twinkling continuous phase driver (0 to 2*PI)
  val starTwinklePhase by infiniteTransition.animateFloat(
    initialValue = 0f,
    targetValue = (2f * PI).toFloat(),
    animationSpec = infiniteRepeatable(
      animation = tween(3200, easing = LinearEasing),
      repeatMode = RepeatMode.Restart
    ),
    label = "star_twinkle_phase"
  )

  // Shooting star / meteor cycle (streaks every 14s)
  val meteorProgress by infiniteTransition.animateFloat(
    initialValue = 0f,
    targetValue = 1f,
    animationSpec = infiniteRepeatable(
      animation = tween(14000, easing = LinearEasing),
      repeatMode = RepeatMode.Restart
    ),
    label = "meteor_cycle"
  )

  Canvas(modifier = modifier) {
    val w = size.width
    val h = size.height

    when (atmosphere) {
      SkyAtmosphere.DAY_SUNSHINE -> {
        // 1. Atmospheric God Rays (Sunbeams streaming down from upper sky)
        val beamPath1 = Path().apply {
          moveTo(w * 0.20f, 0f)
          lineTo(w * 0.40f, 0f)
          lineTo(w * 0.65f, h)
          lineTo(w * 0.35f, h)
          close()
        }
        drawPath(
          path = beamPath1,
          brush = Brush.verticalGradient(
            colors = listOf(
              Color(0xFFFFFBEB).copy(alpha = sunRayShimmer * 0.65f),
              Color(0xFFE0F2FE).copy(alpha = sunRayShimmer * 0.25f),
              Color.Transparent
            )
          )
        )

        val beamPath2 = Path().apply {
          moveTo(w * 0.55f, 0f)
          lineTo(w * 0.75f, 0f)
          lineTo(w * 0.95f, h)
          lineTo(w * 0.70f, h)
          close()
        }
        drawPath(
          path = beamPath2,
          brush = Brush.verticalGradient(
            colors = listOf(
              Color(0xFFFFFBEB).copy(alpha = sunRayShimmer * 0.50f),
              Color(0xFFBAE6FD).copy(alpha = sunRayShimmer * 0.15f),
              Color.Transparent
            )
          )
        )

        // 2. Layer 1: Background High-Altitude Stratus Clouds (Slow, Broad, Soft)
        val bgCloudX = ((cloudOffsetBack * (w + 260f)) % (w + 260f)) - 130f
        drawCircle(
          brush = Brush.radialGradient(
            colors = listOf(Color.White.copy(alpha = 0.16f), Color.Transparent),
            center = Offset(bgCloudX, h * 0.22f),
            radius = 70.dp.toPx()
          ),
          radius = 70.dp.toPx(),
          center = Offset(bgCloudX, h * 0.22f)
        )
        drawCircle(
          brush = Brush.radialGradient(
            colors = listOf(Color.White.copy(alpha = 0.14f), Color.Transparent),
            center = Offset(bgCloudX + 80.dp.toPx(), h * 0.26f),
            radius = 60.dp.toPx()
          ),
          radius = 60.dp.toPx(),
          center = Offset(bgCloudX + 80.dp.toPx(), h * 0.26f)
        )

        // 3. Layer 2: Midground Volumetric Cumulus Clouds (Detailed multi-puff with sunlight highlights)
        val midCloud1X = ((cloudOffsetMid * (w + 220f)) % (w + 220f)) - 110f
        val midCloud2X = (((cloudOffsetMid + 0.55f) * (w + 220f)) % (w + 220f)) - 110f

        // Cloud Cluster A (Billowy Cumulus Bank)
        val puffY = h * 0.32f
        // Base underside shadow
        drawCircle(
          brush = Brush.radialGradient(
            colors = listOf(Color(0xFF93C5FD).copy(alpha = 0.16f), Color.Transparent),
            center = Offset(midCloud1X, puffY + 12.dp.toPx()),
            radius = 48.dp.toPx()
          ),
          radius = 48.dp.toPx(),
          center = Offset(midCloud1X, puffY + 12.dp.toPx())
        )
        // Center white body
        drawCircle(
          brush = Brush.radialGradient(
            colors = listOf(Color.White.copy(alpha = 0.24f), Color.Transparent),
            center = Offset(midCloud1X, puffY),
            radius = 45.dp.toPx()
          ),
          radius = 45.dp.toPx(),
          center = Offset(midCloud1X, puffY)
        )
        // Sun-kissed top crest
        drawCircle(
          brush = Brush.radialGradient(
            colors = listOf(Color(0xFFFFFBEB).copy(alpha = 0.28f), Color.Transparent),
            center = Offset(midCloud1X - 10.dp.toPx(), puffY - 14.dp.toPx()),
            radius = 36.dp.toPx()
          ),
          radius = 36.dp.toPx(),
          center = Offset(midCloud1X - 10.dp.toPx(), puffY - 14.dp.toPx())
        )
        // Adjacent billow
        drawCircle(
          brush = Brush.radialGradient(
            colors = listOf(Color.White.copy(alpha = 0.22f), Color.Transparent),
            center = Offset(midCloud1X + 42.dp.toPx(), puffY - 6.dp.toPx()),
            radius = 38.dp.toPx()
          ),
          radius = 38.dp.toPx(),
          center = Offset(midCloud1X + 42.dp.toPx(), puffY - 6.dp.toPx())
        )
        drawCircle(
          brush = Brush.radialGradient(
            colors = listOf(Color.White.copy(alpha = 0.18f), Color.Transparent),
            center = Offset(midCloud1X - 38.dp.toPx(), puffY + 4.dp.toPx()),
            radius = 32.dp.toPx()
          ),
          radius = 32.dp.toPx(),
          center = Offset(midCloud1X - 38.dp.toPx(), puffY + 4.dp.toPx())
        )

        // Cloud Cluster B (Lower Floating Fluff)
        val puff2Y = h * 0.54f
        drawCircle(
          brush = Brush.radialGradient(
            colors = listOf(Color.White.copy(alpha = 0.20f), Color.Transparent),
            center = Offset(midCloud2X, puff2Y),
            radius = 40.dp.toPx()
          ),
          radius = 40.dp.toPx(),
          center = Offset(midCloud2X, puff2Y)
        )
        drawCircle(
          brush = Brush.radialGradient(
            colors = listOf(Color(0xFFFFFBEB).copy(alpha = 0.22f), Color.Transparent),
            center = Offset(midCloud2X + 32.dp.toPx(), puff2Y - 8.dp.toPx()),
            radius = 32.dp.toPx()
          ),
          radius = 32.dp.toPx(),
          center = Offset(midCloud2X + 32.dp.toPx(), puff2Y - 8.dp.toPx())
        )

        // 4. Layer 3: Foreground Atmospheric Wisps (Fast, semi-transparent)
        val foreCloudX = ((cloudOffsetFore * (w + 180f)) % (w + 180f)) - 90f
        drawCircle(
          brush = Brush.radialGradient(
            colors = listOf(Color.White.copy(alpha = 0.12f), Color.Transparent),
            center = Offset(foreCloudX, h * 0.70f),
            radius = 36.dp.toPx()
          ),
          radius = 36.dp.toPx(),
          center = Offset(foreCloudX, h * 0.70f)
        )
      }
      SkyAtmosphere.SUNSET_TWILIGHT -> {
        // Horizon Twilight Glow
        drawCircle(
          brush = Brush.radialGradient(
            colors = listOf(
              Color(0xFFFEF08A).copy(alpha = 0.40f),
              Color(0xFFF97316).copy(alpha = 0.25f),
              Color(0xFF7C3AED).copy(alpha = 0.15f),
              Color.Transparent
            ),
            center = Offset(w * 0.5f, h * 0.88f),
            radius = w * 0.75f
          ),
          center = Offset(w * 0.5f, h * 0.88f),
          radius = w * 0.75f
        )

        // Rose-gilded Twilight Cloud Silhouettes
        val sunsetCloudX = ((cloudOffsetMid * (w + 220f)) % (w + 220f)) - 110f
        drawCircle(
          brush = Brush.radialGradient(
            colors = listOf(Color(0xFFFDE68A).copy(alpha = 0.28f), Color.Transparent),
            center = Offset(sunsetCloudX, h * 0.40f),
            radius = 48.dp.toPx()
          ),
          radius = 48.dp.toPx(),
          center = Offset(sunsetCloudX, h * 0.40f)
        )
        drawCircle(
          brush = Brush.radialGradient(
            colors = listOf(Color(0xFFFB923C).copy(alpha = 0.22f), Color.Transparent),
            center = Offset(sunsetCloudX + 45.dp.toPx(), h * 0.38f),
            radius = 40.dp.toPx()
          ),
          radius = 40.dp.toPx(),
          center = Offset(sunsetCloudX + 45.dp.toPx(), h * 0.38f)
        )
      }
      SkyAtmosphere.NIGHT_STARRY -> {
        // 1. Cosmic Milky Way Dust Cloud (Celestial Nebula Mist)
        drawCircle(
          brush = Brush.radialGradient(
            colors = listOf(Color(0xFF38BDF8).copy(alpha = 0.08f), Color.Transparent),
            center = Offset(w * 0.32f, h * 0.28f),
            radius = w * 0.55f
          ),
          center = Offset(w * 0.32f, h * 0.28f),
          radius = w * 0.55f
        )
        drawCircle(
          brush = Brush.radialGradient(
            colors = listOf(Color(0xFF818CF8).copy(alpha = 0.07f), Color.Transparent),
            center = Offset(w * 0.70f, h * 0.22f),
            radius = w * 0.48f
          ),
          center = Offset(w * 0.70f, h * 0.22f),
          radius = w * 0.48f
        )

        // 2. Photorealistic Shimmering Particle Stars
        REALISTIC_STARFIELD.forEach { star ->
          val pos = Offset(w * star.xRatio, h * star.yRatio)
          // Individual natural sinusoidal twinkle phase
          val twinkle = (0.35f + 0.65f * (0.5f + 0.5f * sin(starTwinklePhase + star.phaseOffset))).coerceIn(0.12f, 1.0f)

          // Outer delicate starlight aura
          drawCircle(
            color = star.color.copy(alpha = twinkle * 0.40f),
            radius = star.baseRadiusDp.dp.toPx() * 2.2f,
            center = pos
          )

          // Core brilliant star pinprick
          drawCircle(
            color = star.color.copy(alpha = twinkle),
            radius = star.baseRadiusDp.dp.toPx(),
            center = pos
          )

          // 4-point cross diffraction spikes on bright navigational stars
          if (star.hasSpike && twinkle > 0.65f) {
            val spikeLen = 5.dp.toPx() * twinkle
            val spikeAlpha = (twinkle * 0.45f).coerceIn(0f, 1f)
            // Horizontal spike
            drawLine(
              color = star.color.copy(alpha = spikeAlpha),
              start = Offset(pos.x - spikeLen, pos.y),
              end = Offset(pos.x + spikeLen, pos.y),
              strokeWidth = 0.8.dp.toPx(),
              cap = StrokeCap.Round
            )
            // Vertical spike
            drawLine(
              color = star.color.copy(alpha = spikeAlpha),
              start = Offset(pos.x, pos.y - spikeLen),
              end = Offset(pos.x, pos.y + spikeLen),
              strokeWidth = 0.8.dp.toPx(),
              cap = StrokeCap.Round
            )
          }
        }

        // 3. Shooting Star / Meteor Streak Physics (Appears every 14s cycle)
        if (meteorProgress in 0.60f..0.72f) {
          val meteorSub = (meteorProgress - 0.60f) / 0.12f
          val startX = w * 0.85f
          val startY = h * 0.06f
          val endX = w * 0.20f
          val endY = h * 0.38f

          val curX = startX + (endX - startX) * meteorSub
          val curY = startY + (endY - startY) * meteorSub
          val tailLength = 48.dp.toPx()
          val dirX = (endX - startX) / (w * 0.8f)
          val dirY = (endY - startY) / (h * 0.4f)
          val tailX = curX - dirX * tailLength
          val tailY = curY - dirY * tailLength

          val meteorAlpha = (sin(meteorSub * PI.toFloat()) * 0.95f).coerceIn(0f, 1f)

          drawLine(
            brush = Brush.linearGradient(
              colors = listOf(Color.Transparent, Color(0xFF67E8F9).copy(alpha = meteorAlpha * 0.6f), Color.White.copy(alpha = meteorAlpha)),
              start = Offset(tailX, tailY),
              end = Offset(curX, curY)
            ),
            start = Offset(tailX, tailY),
            end = Offset(curX, curY),
            strokeWidth = 1.8.dp.toPx(),
            cap = StrokeCap.Round
          )
          drawCircle(
            color = Color.White.copy(alpha = meteorAlpha),
            radius = 2.2.dp.toPx(),
            center = Offset(curX, curY)
          )
        }

        // 4. Mosque Dome & Minarets Horizon Silhouette
        val domePath = Path().apply {
          moveTo(0f, h)
          lineTo(w * 0.24f, h)
          // Minaret 1
          lineTo(w * 0.24f, h - 36.dp.toPx())
          lineTo(w * 0.26f, h - 48.dp.toPx())
          lineTo(w * 0.28f, h - 36.dp.toPx())
          lineTo(w * 0.28f, h)
          // Mosque Dome
          lineTo(w * 0.37f, h)
          cubicTo(
            w * 0.41f, h - 42.dp.toPx(),
            w * 0.59f, h - 42.dp.toPx(),
            w * 0.63f, h
          )
          // Minaret 2
          lineTo(w * 0.72f, h)
          lineTo(w * 0.72f, h - 36.dp.toPx())
          lineTo(w * 0.74f, h - 48.dp.toPx())
          lineTo(w * 0.76f, h - 36.dp.toPx())
          lineTo(w * 0.76f, h)
          lineTo(w, h)
          close()
        }

        drawPath(
          path = domePath,
          color = Color(0xFF020617).copy(alpha = 0.40f),
          style = Fill
        )
      }
    }
  }
}

/**
 * 3. NEON GLOWING CELESTIAL ARC & HIJRI MOON PHASE ENGINE
 * Calculates moon phase from Hijri Day 1-30 and smoothly moves Sun/Moon on the celestial arc.
 * - Daytime: Photorealistic glowing Sun with radiating sunrays, corona, and optical lens flare.
 * - Nighttime: Accurate Hijri Moon with soft moonlight glow, lunar halo, and crater topography.
 */
@Composable
private fun GlowingCelestialArc(
  progress: Float,
  atmosphere: SkyAtmosphere,
  hijriDay: Int,
  selectedLang: String,
  startFormatted: String,
  endFormatted: String,
  midpointLabel: String,
  modifier: Modifier = Modifier
) {
  val infiniteTransition = rememberInfiniteTransition(label = "celestial_anim")

  // Sun corona rotational shimmer
  val sunCoronaRotation by infiniteTransition.animateFloat(
    initialValue = 0f,
    targetValue = (2f * PI).toFloat(),
    animationSpec = infiniteRepeatable(
      animation = tween(18000, easing = LinearEasing),
      repeatMode = RepeatMode.Restart
    ),
    label = "sun_rotation"
  )

  // Sun corona breathing pulse
  val sunPulse by infiniteTransition.animateFloat(
    initialValue = 0.88f,
    targetValue = 1.12f,
    animationSpec = infiniteRepeatable(
      animation = tween(3000, easing = LinearEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "sun_pulse"
  )

  Box(modifier = modifier) {
    Canvas(modifier = Modifier.fillMaxSize()) {
      val w = size.width
      val h = size.height

      // Semi-elliptical arc parameters:
      // RadiusX is bounded safely away from screen edges (0.36f of width)
      // RadiusY gives an elegant curve without clipping at the top
      // Arc center is placed at 56% height to leave 44% height at the bottom for labels
      val arcCenter = Offset(w * 0.5f, h * 0.56f)
      val radiusX = w * 0.36f
      val radiusY = h * 0.38f

      // 1. Background Neon Arc Track
      val arcPath = Path().apply {
        val steps = 40
        for (i in 0..steps) {
          val t = i.toFloat() / steps
          val angle = PI.toFloat() * (1f - t) // PI (left) to 0 (right)
          val x = arcCenter.x + radiusX * cos(angle)
          val y = arcCenter.y - radiusY * sin(angle)
          if (i == 0) moveTo(x, y) else lineTo(x, y)
        }
      }

      // Draw faint dashed background guide
      drawPath(
        path = arcPath,
        color = Color.White.copy(alpha = 0.28f),
        style = Stroke(
          width = 2.dp.toPx(),
          cap = StrokeCap.Round,
          pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 8f), 0f)
        )
      )

      // Draw endpoints anchors (subtle glowing rings)
      drawCircle(
        color = Color.White.copy(alpha = 0.6f),
        radius = 3.dp.toPx(),
        center = Offset(arcCenter.x - radiusX, arcCenter.y)
      )
      drawCircle(
        color = Color.White.copy(alpha = 0.6f),
        radius = 3.dp.toPx(),
        center = Offset(arcCenter.x + radiusX, arcCenter.y)
      )

      // 2. Glowing Active Trajectory Arc
      val clampedProgress = progress.coerceIn(0f, 1f)
      val activePath = Path().apply {
        val steps = (40 * clampedProgress).toInt().coerceAtLeast(1)
        for (i in 0..steps) {
          val t = (i.toFloat() / 40f).coerceAtMost(clampedProgress)
          val angle = PI.toFloat() * (1f - t)
          val x = arcCenter.x + radiusX * cos(angle)
          val y = arcCenter.y - radiusY * sin(angle)
          if (i == 0) moveTo(x, y) else lineTo(x, y)
        }
      }

      drawPath(
        path = activePath,
        brush = Brush.horizontalGradient(
          listOf(
            Color.White.copy(alpha = 0.5f),
            if (atmosphere == SkyAtmosphere.DAY_SUNSHINE) Color(0xFFFDE047) else Color(0xFF67E8F9),
            Color.White
          )
        ),
        style = Stroke(
          width = 3.dp.toPx(),
          cap = StrokeCap.Round
        )
      )

      // 3. Current Position of Celestial Body strictly locked on Arc trajectory
      val boundedProgress = clampedProgress.coerceIn(0.02f, 0.98f)
      val currentAngle = PI.toFloat() * (1f - boundedProgress)
      val celestialX = arcCenter.x + radiusX * cos(currentAngle)
      val celestialY = arcCenter.y - radiusY * sin(currentAngle)
      val celestialPos = Offset(celestialX, celestialY)

      // Draw Photorealistic Sun (Day) or Calculated Moon Phase (Night)
      if (atmosphere == SkyAtmosphere.DAY_SUNSHINE || atmosphere == SkyAtmosphere.SUNSET_TWILIGHT) {
        // --- REALISTIC GLOWING SUN WITH LENS FLARE & RAYS ---
        // 1. Broad outer atmospheric corona
        val outerRadius = 28.dp.toPx() * sunPulse
        drawCircle(
          brush = Brush.radialGradient(
            colors = listOf(
              Color(0xFFFDE047).copy(alpha = 0.35f),
              Color(0xFFF59E0B).copy(alpha = 0.15f),
              Color.Transparent
            ),
            center = celestialPos,
            radius = outerRadius
          ),
          radius = outerRadius,
          center = celestialPos
        )

        // 2. Chromatic lens flare halo ring
        val ringRadius = 21.dp.toPx() * sunPulse
        drawCircle(
          color = Color(0xFF67E8F9).copy(alpha = 0.22f),
          radius = ringRadius,
          center = celestialPos,
          style = Stroke(width = 1.dp.toPx())
        )

        // 3. Radiating Sunburst Rays (8 dynamic solar rays)
        val numRays = 8
        for (r in 0 until numRays) {
          val rayAngle = sunCoronaRotation + (r.toFloat() / numRays) * (2f * PI.toFloat())
          val innerDist = 10.dp.toPx()
          val outerDist = (15.5f + 2.5f * sin(rayAngle * 2f)).dp.toPx() * sunPulse
          val rayStart = Offset(celestialX + innerDist * cos(rayAngle), celestialY + innerDist * sin(rayAngle))
          val rayEnd = Offset(celestialX + outerDist * cos(rayAngle), celestialY + outerDist * sin(rayAngle))
          drawLine(
            color = Color(0xFFFEF9C3).copy(alpha = 0.55f),
            start = rayStart,
            end = rayEnd,
            strokeWidth = 1.6.dp.toPx(),
            cap = StrokeCap.Round
          )
        }

        // 4. Primary dense corona aura
        drawCircle(
          brush = Brush.radialGradient(
            colors = listOf(
              Color(0xFFFFFBEB).copy(alpha = 0.85f),
              Color(0xFFFBBF24).copy(alpha = 0.45f),
              Color.Transparent
            ),
            center = celestialPos,
            radius = 14.dp.toPx()
          ),
          radius = 14.dp.toPx(),
          center = celestialPos
        )

        // 5. Incandescent Core Sun Disc
        drawCircle(
          brush = Brush.radialGradient(
            colors = listOf(Color.White, Color(0xFFFFF7ED), Color(0xFFFDE047)),
            center = celestialPos,
            radius = 8.5.dp.toPx()
          ),
          radius = 8.5.dp.toPx(),
          center = celestialPos
        )

        // 6. Secondary optical lens flare ghost artifact disc
        val ghostX = celestialX + (arcCenter.x - celestialX) * 0.25f
        val ghostY = celestialY + (arcCenter.y - celestialY) * 0.25f
        drawCircle(
          color = Color(0xFFFEF08A).copy(alpha = 0.24f),
          radius = 3.5.dp.toPx(),
          center = Offset(ghostX, ghostY)
        )
      } else {
        // --- REALISTIC HIJRI MOON WITH SOFT MOONLIGHT GLOW ---
        // 1. Soft atmospheric moonlight halo
        drawCircle(
          brush = Brush.radialGradient(
            colors = listOf(
              Color(0xFFE0F2FE).copy(alpha = 0.32f),
              Color(0xFF38BDF8).copy(alpha = 0.10f),
              Color.Transparent
            ),
            center = celestialPos,
            radius = 26.dp.toPx()
          ),
          radius = 26.dp.toPx(),
          center = celestialPos
        )

        // 2. Secondary lunar corona
        drawCircle(
          brush = Brush.radialGradient(
            colors = listOf(Color(0xFFF8FAFC).copy(alpha = 0.45f), Color.Transparent),
            center = celestialPos,
            radius = 14.dp.toPx()
          ),
          radius = 14.dp.toPx(),
          center = celestialPos
        )

        // 3. Realistic Hijri Moon Phase with Earthshine and Crater Topography
        if (hijriDay !in 28..30) {
          drawMoonPhase(
            center = celestialPos,
            radius = 8.5.dp.toPx(),
            hijriDay = hijriDay
          )
        } else {
          // Amabashya (New Moon): Soft silhouetted outline with ambient starlight
          drawCircle(
            color = Color(0xFF0F172A).copy(alpha = 0.50f),
            radius = 8.5.dp.toPx(),
            center = celestialPos
          )
        }
      }
    }

    // Top Center Midpoint Badge (Tahajjud / Midday)
    Surface(
      shape = RoundedCornerShape(10.dp),
      color = Color.Black.copy(alpha = 0.35f),
      border = BorderStroke(0.7.dp, Color.White.copy(alpha = 0.22f)),
      modifier = Modifier
        .align(Alignment.TopCenter)
        .padding(top = 2.dp)
    ) {
      Text(
        text = midpointLabel,
        style = TextStyle(
          fontSize = 10.sp,
          color = Color.White.copy(alpha = 0.92f),
          fontWeight = FontWeight.SemiBold
        ),
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.5.dp)
      )
    }

    // Bottom Start Time Label: Positioned cleanly below left arc endpoint with generous padding
    Column(
      modifier = Modifier
        .align(Alignment.BottomStart)
        .padding(start = 18.dp, bottom = 4.dp)
    ) {
      Text(
        text = when (selectedLang) {
          "BN" -> "শুরু"
          "AR" -> "البداية"
          else -> "Start"
        },
        style = TextStyle(
          fontSize = 10.sp,
          color = Color.White.copy(alpha = 0.75f),
          fontWeight = FontWeight.Medium
        ),
        maxLines = 1
      )
      Text(
        text = startFormatted,
        style = TextStyle(
          fontSize = 12.sp,
          fontWeight = FontWeight.Bold,
          color = Color.White
        ),
        maxLines = 1,
        softWrap = false
      )
    }

    // Bottom End Time Label: Positioned cleanly below right arc endpoint with generous padding
    Column(
      horizontalAlignment = Alignment.End,
      modifier = Modifier
        .align(Alignment.BottomEnd)
        .padding(end = 18.dp, bottom = 4.dp)
    ) {
      Text(
        text = when (selectedLang) {
          "BN" -> "শেষ"
          "AR" -> "النهاية"
          else -> "End"
        },
        style = TextStyle(
          fontSize = 10.sp,
          color = Color.White.copy(alpha = 0.75f),
          fontWeight = FontWeight.Medium
        ),
        maxLines = 1
      )
      Text(
        text = endFormatted,
        style = TextStyle(
          fontSize = 12.sp,
          fontWeight = FontWeight.Bold,
          color = Color.White
        ),
        maxLines = 1,
        softWrap = false
      )
    }
  }
}

/**
 * Calculates and renders precise photorealistic Moon Phase graphics based on Hijri Day (1-30).
 * Features authentic Earthshine shading, craters (lunar Maria), and accurate terminator curve.
 */
private fun DrawScope.drawMoonPhase(
  center: Offset,
  radius: Float,
  hijriDay: Int
) {
  val moonColor = Color(0xFFF8FAFC)
  val earthshineColor = Color(0xFF1E293B).copy(alpha = 0.45f)
  val shadowColor = Color(0xFF0A1128).copy(alpha = 0.90f)

  // 1. Base Earthshine disc for unlit lunar limb
  drawCircle(color = earthshineColor, radius = radius, center = center)

  when (hijriDay) {
    in 1..3 -> {
      // Thin Waxing Crescent with glowing edge
      drawCircle(color = moonColor, radius = radius, center = center)
      drawCircle(
        color = shadowColor,
        radius = radius * 0.92f,
        center = Offset(center.x - radius * 0.55f, center.y)
      )
    }
    in 4..8 -> {
      // First Quarter Crescent
      drawCircle(color = moonColor, radius = radius, center = center)
      drawCircle(
        color = shadowColor,
        radius = radius * 0.95f,
        center = Offset(center.x - radius * 0.35f, center.y)
      )
      // Subtle Maria crater shading
      drawCircle(
        color = Color(0xFFCBD5E1).copy(alpha = 0.20f),
        radius = radius * 0.25f,
        center = Offset(center.x + radius * 0.30f, center.y - radius * 0.15f)
      )
    }
    in 9..12 -> {
      // Waxing Gibbous
      drawCircle(color = moonColor, radius = radius, center = center)
      drawCircle(
        color = shadowColor,
        radius = radius * 0.95f,
        center = Offset(center.x - radius * 0.75f, center.y)
      )
    }
    in 13..15 -> {
      // Radiant Full Moon (Badr) with bright aura and lunar Maria
      drawCircle(
        color = Color(0xFFFEF9C3).copy(alpha = 0.45f),
        radius = radius * 1.55f,
        center = center
      )
      drawCircle(
        brush = Brush.radialGradient(
          colors = listOf(Color(0xFFFFFFFF), Color(0xFFFEF3C7)),
          center = center,
          radius = radius
        ),
        radius = radius,
        center = center
      )
      // Realistic Lunar Maria (Crater plains)
      drawCircle(
        color = Color(0xFFCBD5E1).copy(alpha = 0.30f),
        radius = radius * 0.32f,
        center = Offset(center.x - radius * 0.20f, center.y - radius * 0.18f)
      )
      drawCircle(
        color = Color(0xFFCBD5E1).copy(alpha = 0.25f),
        radius = radius * 0.24f,
        center = Offset(center.x + radius * 0.25f, center.y + radius * 0.15f)
      )
    }
    in 16..22 -> {
      // Waning Gibbous
      drawCircle(color = moonColor, radius = radius, center = center)
      drawCircle(
        color = shadowColor,
        radius = radius * 0.95f,
        center = Offset(center.x + radius * 0.75f, center.y)
      )
    }
    in 23..27 -> {
      // Thin Waning Crescent
      drawCircle(color = moonColor, radius = radius, center = center)
      drawCircle(
        color = shadowColor,
        radius = radius * 0.92f,
        center = Offset(center.x + radius * 0.55f, center.y)
      )
    }
    else -> {
      // Normal balanced lunar disc
      drawCircle(color = moonColor, radius = radius, center = center)
    }
  }
}
