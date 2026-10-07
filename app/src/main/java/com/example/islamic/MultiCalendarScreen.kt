package com.example.islamic

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.BanglaDateCalculator
import com.example.BanglaDateInfo
import com.example.ui.theme.RoyalEmerald
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/**
 * Bengali Number Formatter
 */
private fun toBnDigits(num: Int): String {
  val bnDigits = charArrayOf('০', '১', '২', '৩', '৪', '৫', '৬', '৭', '৮', '৯')
  return num.toString().map { if (it in '0'..'9') bnDigits[it - '0'] else it }.joinToString("")
}

private fun toBnDigits(num: Long): String {
  val bnDigits = charArrayOf('০', '১', '২', '৩', '৪', '৫', '৬', '৭', '৮', '৯')
  return num.toString().map { if (it in '0'..'9') bnDigits[it - '0'] else it }.joinToString("")
}

enum class CalendarType {
  BANGLA, HIJRI, GREGORIAN
}

/**
 * Data bundle for interactive monthly popup modal
 */
data class MonthModalState(
  val calendarType: CalendarType,
  val monthIndex: Int, // 0-based
  val monthName: String,
  val monthSubName: String,
  val year: Int,
  val yearText: String,
  val totalDays: Int,
  val startGregorianCal: Calendar,
  val isCurrentRunningMonth: Boolean,
  val todayDayNumber: Int // 1-based or -1 if not running month
)

/**
 * Multi-Calendar Screen providing direct tabs for:
 * 1. Bangla Calendar (বাংলা সন, বঙ্গাব্দ, ১২ মাস ও ষড়ঋতু)
 * 2. Hijri Calendar (হিজরি চান্দ্রবর্ষ, চাঁদ দর্শন সমন্বয়, ১২ মাস ও ইসলামিক দিবস)
 * 3. Gregorian Calendar (ইংরেজি বর্ষপঞ্জি, ১২ মাস ও ৩-ইন-১ সমন্বয়)
 *
 * Each calendar includes an interactive 12-Month grid with month click popup modals!
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MultiCalendarScreen(
  selectedLang: String,
  dayAdjustment: Int = 0,
  onDayAdjustmentChange: (Int) -> Unit = {}
) {
  var selectedCalendarTab by remember(selectedLang) {
    mutableIntStateOf(
      when (selectedLang) {
        "BN" -> 0
        "AR" -> 1
        else -> 2
      }
    )
  }

  // Current active modal data (if non-null, opens popup)
  var activeModalState by remember { mutableStateOf<MonthModalState?>(null) }

  val calendar = remember { Calendar.getInstance() }
  val banglaDate = remember(calendar) { BanglaDateCalculator.calculateBanglaDate(calendar) }
  val hijriDate = remember(calendar, dayAdjustment) {
    HijriCalendarCalculator.calculateHijri(calendar, dayAdjustment)
  }

  val gregFormatter = remember(selectedLang) {
    val locale = if (selectedLang == "BN") Locale("bn", "BD") else Locale.ENGLISH
    SimpleDateFormat("d MMMM yyyy, EEEE", locale)
  }
  val gregDateStr = remember(calendar) { gregFormatter.format(calendar.time) }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(Color(0xFFF8FAFC))
      .testTag("multi_calendar_screen")
  ) {
    // 1. Top 3-in-1 Secondary Tab Selector [বাংলা | হিজরি | ইংরেজি]
    SecondaryTabRow(
      selectedTabIndex = selectedCalendarTab,
      containerColor = Color.White,
      contentColor = RoyalEmerald,
      indicator = {
        TabRowDefaults.SecondaryIndicator(
          modifier = Modifier.tabIndicatorOffset(selectedCalendarTab),
          color = RoyalEmerald,
          height = 3.5.dp
        )
      },
      modifier = Modifier.fillMaxWidth()
    ) {
      Tab(
        selected = selectedCalendarTab == 0,
        onClick = { selectedCalendarTab = 0 },
        text = {
          Text(
            text = when (selectedLang) {
              "EN" -> "🇧🇩 Bangla"
              "AR" -> "🇧🇩 البنغالية"
              else -> "🇧🇩 বাংলা"
            },
            style = TextStyle(
              fontSize = 13.5.sp,
              fontWeight = if (selectedCalendarTab == 0) FontWeight.Bold else FontWeight.Medium,
              color = if (selectedCalendarTab == 0) RoyalEmerald else Color(0xFF64748B)
            )
          )
        },
        modifier = Modifier.testTag("tab_calendar_bangla")
      )

      Tab(
        selected = selectedCalendarTab == 1,
        onClick = { selectedCalendarTab = 1 },
        text = {
          Text(
            text = when (selectedLang) {
              "EN" -> "🌙 Hijri"
              "AR" -> "🌙 الهجرية"
              else -> "🌙 হিজরি"
            },
            style = TextStyle(
              fontSize = 13.5.sp,
              fontWeight = if (selectedCalendarTab == 1) FontWeight.Bold else FontWeight.Medium,
              color = if (selectedCalendarTab == 1) RoyalEmerald else Color(0xFF64748B)
            )
          )
        },
        modifier = Modifier.testTag("tab_calendar_hijri")
      )

      Tab(
        selected = selectedCalendarTab == 2,
        onClick = { selectedCalendarTab = 2 },
        text = {
          Text(
            text = when (selectedLang) {
              "EN" -> "📅 Gregorian"
              "AR" -> "📅 الميلادية"
              else -> "📅 ইংরেজি"
            },
            style = TextStyle(
              fontSize = 13.5.sp,
              fontWeight = if (selectedCalendarTab == 2) FontWeight.Bold else FontWeight.Medium,
              color = if (selectedCalendarTab == 2) RoyalEmerald else Color(0xFF64748B)
            )
          )
        },
        modifier = Modifier.testTag("tab_calendar_gregorian")
      )
    }

    // 2. Calendar Content based on selected tab with responsive slide & fade transitions (0.5s ease-in-out)
    AnimatedContent(
      targetState = selectedCalendarTab,
      transitionSpec = {
        val duration = 500
        val easing = FastOutSlowInEasing
        if (targetState > initialState) {
          (slideInHorizontally(tween(duration, easing = easing)) { width -> width / 4 } +
            fadeIn(tween(duration, easing = easing)))
            .togetherWith(
              slideOutHorizontally(tween(duration, easing = easing)) { width -> -width / 4 } +
                fadeOut(tween(duration, easing = easing))
            )
        } else {
          (slideInHorizontally(tween(duration, easing = easing)) { width -> -width / 4 } +
            fadeIn(tween(duration, easing = easing)))
            .togetherWith(
              slideOutHorizontally(tween(duration, easing = easing)) { width -> width / 4 } +
                fadeOut(tween(duration, easing = easing))
            )
        }
      },
      label = "calendar_tab_transition",
      modifier = Modifier.fillMaxSize()
    ) { tabIndex ->
      Column(
        modifier = Modifier
          .fillMaxSize()
          .verticalScroll(rememberScrollState())
          .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        when (tabIndex) {
          0 -> BanglaCalendarContent(
            banglaDate = banglaDate,
            calendar = calendar,
            selectedLang = selectedLang,
            onOpenMonthModal = { state -> activeModalState = state }
          )
          1 -> HijriCalendarContent(
            hijriDate = hijriDate,
            calendar = calendar,
            dayAdjustment = dayAdjustment,
            onDayAdjustmentChange = onDayAdjustmentChange,
            selectedLang = selectedLang,
            onOpenMonthModal = { state -> activeModalState = state }
          )
          2 -> GregorianCalendarContent(
            calendar = calendar,
            gregDateStr = gregDateStr,
            banglaDate = banglaDate,
            hijriDate = hijriDate,
            selectedLang = selectedLang,
            onOpenMonthModal = { state -> activeModalState = state }
          )
        }

        Spacer(modifier = Modifier.height(24.dp))
      }
    }
  }

  // 3. Interactive Monthly Calendar Modal Popup
  activeModalState?.let { modalState ->
    MonthCalendarDetailDialog(
      modalState = modalState,
      dayAdjustment = dayAdjustment,
      selectedLang = selectedLang,
      onDismiss = { activeModalState = null }
    )
  }
}

/**
 * 1. BANGLA CALENDAR TAB CONTENT
 */
@Composable
private fun BanglaCalendarContent(
  banglaDate: BanglaDateInfo,
  calendar: Calendar,
  selectedLang: String,
  onOpenMonthModal: (MonthModalState) -> Unit
) {
  // 1. Current Date Hero Card with Dynamic Seasonal Canvas and 3D Flip Date Number
  Card(
    shape = RoundedCornerShape(22.dp),
    colors = CardDefaults.cardColors(containerColor = Color.Transparent),
    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
    modifier = Modifier
      .fillMaxWidth()
      .testTag("bangla_hero_card")
  ) {
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .background(
          Brush.linearGradient(
            listOf(Color(0xFF064E3B), Color(0xFF047857), Color(0xFF10B981))
          )
        )
    ) {
      // Automatic Bangla 6 Seasons Atmospheric Canvas
      BanglaSeasonalCanvas(
        seasonNameBn = banglaDate.seasonNameBn,
        modifier = Modifier.matchParentSize()
      )

      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
          .fillMaxWidth()
          .padding(22.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Surface(
            shape = RoundedCornerShape(10.dp),
            color = Color.White.copy(alpha = 0.22f)
          ) {
            Text(
              text = "🇧🇩 বাংলা সন",
              style = TextStyle(fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = Color.White),
              modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
            )
          }

          Surface(
            shape = RoundedCornerShape(10.dp),
            color = Color(0xFFFEF3C7)
          ) {
            Text(
              text = "ঋতু: ${banglaDate.seasonNameBn}",
              style = TextStyle(fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF92400E)),
              modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
            )
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Large prominent day number with 3D Page-Flip Animation
        FlipDateCardNumber(
          dateText = toBnDigits(banglaDate.day)
        )

        Text(
          text = "${banglaDate.monthNameBn}, ${toBnDigits(banglaDate.year)} বঙ্গাব্দ",
          style = TextStyle(
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFD1FAE5)
          )
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Sub-badge showing day and season
        Surface(
          shape = RoundedCornerShape(20.dp),
          color = Color.Black.copy(alpha = 0.22f)
        ) {
          Text(
            text = "আজ ${banglaDate.dayOfWeekBn} • ${banglaDate.seasonNameBn}",
            style = TextStyle(
              fontSize = 12.5.sp,
              fontWeight = FontWeight.Medium,
              color = Color.White.copy(alpha = 0.95f)
            ),
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 5.dp)
          )
        }
      }
    }
  }

  Spacer(modifier = Modifier.height(16.dp))

  // Pulsing animation for active Current Month card
  val banglaPulseAnim = rememberInfiniteTransition(label = "bangla_pulse")
  val banglaPulseAlpha by banglaPulseAnim.animateFloat(
    initialValue = 0.45f,
    targetValue = 1.0f,
    animationSpec = infiniteRepeatable(
      animation = tween(1200, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "pulse_alpha"
  )

  // 2. Bangla 12 Months Overview Grid (3 columns) with Month Click Interaction
  Card(
    shape = RoundedCornerShape(18.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    modifier = Modifier.fillMaxWidth()
  ) {
    Column(modifier = Modifier.padding(16.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.CalendarMonth,
            contentDescription = null,
            tint = RoyalEmerald,
            modifier = Modifier.size(20.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "বাংলা ১২ মাসের বর্ষপঞ্জি",
            style = TextStyle(fontSize = 14.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
          )
        }

        Surface(
          shape = RoundedCornerShape(8.dp),
          color = Color(0xFFECFDF5)
        ) {
          Text(
            text = "মাসে ট্যাপ করুন 👆",
            style = TextStyle(fontSize = 10.sp, fontWeight = FontWeight.SemiBold, color = RoyalEmerald),
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      val banglaMonths = listOf(
        Triple("বৈশাখ", "৩১ দিন", "গ্রীষ্মকাল"),
        Triple("জ্যৈষ্ঠ", "৩১ দিন", "গ্রীষ্মকাল"),
        Triple("আষাঢ়", "৩১ দিন", "বর্ষাকাল"),
        Triple("শ্রাবণ", "৩১ দিন", "বর্ষাকাল"),
        Triple("ভাদ্র", "৩১ দিন", "শরৎকাল"),
        Triple("আশ্বিন", "৩১ দিন", "শরৎকাল"),
        Triple("কার্তিক", "৩০ দিন", "হেমন্তকাল"),
        Triple("অগ্রহায়ণ", "৩০ দিন", "হেমন্তকাল"),
        Triple("পৌষ", "৩০ দিন", "শীতকাল"),
        Triple("মাঘ", "৩০ দিন", "শীতকাল"),
        Triple("ফাল্গুন", "২৯/৩০ দিন", "বসন্তকাল"),
        Triple("চৈত্র", "৩০ দিন", "বসন্তকাল")
      )

      banglaMonths.chunked(3).forEachIndexed { rowIndex, rowMonths ->
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.5.dp),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          rowMonths.forEachIndexed { colIndex, (month, days, season) ->
            val monthIndex = rowIndex * 3 + colIndex
            val isCurrentMonth = banglaDate.monthNameBn == month

            val totalDays = when (monthIndex) {
              in 0..5 -> 31
              10 -> 30 // Falgun (usually 30 in Bangladesh modern calendar)
              else -> 30
            }

            Surface(
              shape = RoundedCornerShape(12.dp),
              color = if (isCurrentMonth) Color(0xFFECFDF5) else Color(0xFFF8FAFC),
              border = BorderStroke(
                if (isCurrentMonth) 2.5.dp else 1.dp,
                if (isCurrentMonth) RoyalEmerald.copy(alpha = banglaPulseAlpha) else Color(0xFFE2E8F0)
              ),
              shadowElevation = if (isCurrentMonth) 4.dp else 0.dp,
              modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(12.dp))
                .clickable {
                  val startCal = getBanglaMonthStartGregorian(monthIndex, banglaDate.year)
                  onOpenMonthModal(
                    MonthModalState(
                      calendarType = CalendarType.BANGLA,
                      monthIndex = monthIndex,
                      monthName = month,
                      monthSubName = "$season • $days",
                      year = banglaDate.year,
                      yearText = "${toBnDigits(banglaDate.year)} বঙ্গাব্দ",
                      totalDays = totalDays,
                      startGregorianCal = startCal,
                      isCurrentRunningMonth = isCurrentMonth,
                      todayDayNumber = if (isCurrentMonth) banglaDate.day else -1
                    )
                  )
                }
                .testTag("bangla_month_card_$monthIndex")
            ) {
              Column(
                modifier = Modifier.padding(vertical = 10.dp, horizontal = 6.dp),
                horizontalAlignment = Alignment.CenterHorizontally
              ) {
                if (isCurrentMonth) {
                  Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = RoyalEmerald,
                    modifier = Modifier.padding(bottom = 4.dp)
                  ) {
                    Text(
                      text = "বর্তমান মাস",
                      style = TextStyle(fontSize = 8.5.sp, fontWeight = FontWeight.Bold, color = Color.White),
                      modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                    )
                  }
                }

                Text(
                  text = month,
                  style = TextStyle(
                    fontSize = 13.sp,
                    fontWeight = if (isCurrentMonth) FontWeight.ExtraBold else FontWeight.Bold,
                    color = if (isCurrentMonth) RoyalEmerald else Color(0xFF1E293B)
                  )
                )
                Text(
                  text = days,
                  style = TextStyle(
                    fontSize = 10.5.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (isCurrentMonth) RoyalEmerald.copy(alpha = 0.85f) else Color(0xFF64748B)
                  )
                )
                Text(
                  text = season,
                  style = TextStyle(fontSize = 9.sp, color = Color(0xFF94A3B8))
                )
              }
            }
          }
        }
      }
    }
  }

  Spacer(modifier = Modifier.height(16.dp))

  // 3. Collapsible Seasons Info Section (ষড়ঋতু)
  var isSeasonsExpanded by remember { mutableStateOf(true) }

  Card(
    shape = RoundedCornerShape(18.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    modifier = Modifier.fillMaxWidth()
  ) {
    Column(modifier = Modifier.padding(16.dp)) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(8.dp))
          .clickable { isSeasonsExpanded = !isSeasonsExpanded }
          .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(text = "🌾", fontSize = 18.sp)
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "বাংলার ষড়ঋতু (৬ ঋতু ও ১২ মাস)",
            style = TextStyle(fontSize = 14.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
          )
        }

        IconButton(
          onClick = { isSeasonsExpanded = !isSeasonsExpanded },
          modifier = Modifier.size(28.dp)
        ) {
          Icon(
            imageVector = if (isSeasonsExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
            contentDescription = "Toggle Seasons",
            tint = RoyalEmerald
          )
        }
      }

      AnimatedVisibility(
        visible = isSeasonsExpanded,
        enter = expandVertically(),
        exit = shrinkVertically()
      ) {
        Column(modifier = Modifier.padding(top = 10.dp)) {
          val seasons = listOf(
            Triple("☀️ গ্রীষ্মকাল", "বৈশাখ ও জ্যৈষ্ঠ মাস", "প্রচণ্ড রোদ ও সুস্বাদু ফলের মৌসুম"),
            Triple("🌧️ বর্ষাকাল", "আষাঢ় ও শ্রাবণ মাস", "অঝোর ধারায় বৃষ্টি ও সতেজ প্রকৃতি"),
            Triple("🌾 শরৎকাল", "ভাদ্র ও আশ্বিন মাস", "কাশফুল, নির্মল নীল আকাশ ও শুভ্র মেঘ"),
            Triple("🍂 হেমন্তকাল", "কার্তিক ও অগ্রহায়ণ মাস", "নবান্ন উৎসব ও নতুন ধানের ঘ্রাণ"),
            Triple("❄️ শীতকাল", "পৌষ ও মাঘ মাস", "হিমেল হাওয়া, খেজুরের রস ও পিঠাপুলি"),
            Triple("🌸 বসন্তকাল", "ফাল্গুন ও চৈত্র মাস", "ঋতুরাজ বসন্ত, পলাশ ও কোকিলের গান")
          )

          seasons.forEach { (season, months, desc) ->
            val isCurrentSeason = banglaDate.seasonNameBn.contains(season.substring(3, 7))
            Surface(
              shape = RoundedCornerShape(10.dp),
              color = if (isCurrentSeason) Color(0xFFECFDF5) else Color.Transparent,
              border = if (isCurrentSeason) BorderStroke(1.dp, Color(0xFFA7F3D0)) else null,
              modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 3.dp)
            ) {
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(horizontal = 8.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Column(modifier = Modifier.weight(1f)) {
                  Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                      text = season,
                      style = TextStyle(
                        fontSize = 12.5.sp,
                        fontWeight = if (isCurrentSeason) FontWeight.Bold else FontWeight.Medium,
                        color = if (isCurrentSeason) RoyalEmerald else Color(0xFF334155)
                      )
                    )
                    if (isCurrentSeason) {
                      Spacer(modifier = Modifier.width(6.dp))
                      Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = RoyalEmerald
                      ) {
                        Text(
                          text = "চলতি ঋতু",
                          fontSize = 8.5.sp,
                          color = Color.White,
                          fontWeight = FontWeight.Bold,
                          modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                        )
                      }
                    }
                  }
                  Text(
                    text = desc,
                    style = TextStyle(fontSize = 10.sp, color = Color(0xFF94A3B8))
                  )
                }

                Text(
                  text = months,
                  style = TextStyle(
                    fontSize = 11.sp,
                    fontWeight = if (isCurrentSeason) FontWeight.Bold else FontWeight.Normal,
                    color = if (isCurrentSeason) RoyalEmerald else Color(0xFF64748B)
                  )
                )
              }
            }
            HorizontalDivider(color = Color(0xFFF1F5F9))
          }
        }
      }
    }
  }
}

/**
 * 2. HIJRI CALENDAR TAB CONTENT
 */
@Composable
private fun HijriCalendarContent(
  hijriDate: HijriDate,
  calendar: Calendar,
  dayAdjustment: Int,
  onDayAdjustmentChange: (Int) -> Unit,
  selectedLang: String,
  onOpenMonthModal: (MonthModalState) -> Unit
) {
  // 1. Hero Hijri Date Card with Deep Emerald/Midnight Canvas and 3D Flip Date Number
  Card(
    shape = RoundedCornerShape(22.dp),
    colors = CardDefaults.cardColors(containerColor = Color.Transparent),
    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
    border = BorderStroke(1.2.dp, Color(0xFF6EE7B7).copy(alpha = 0.28f)),
    modifier = Modifier
      .fillMaxWidth()
      .testTag("hijri_hero_card")
  ) {
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .background(
          Brush.verticalGradient(
            listOf(
              Color(0xFF021B14), // Deep Obsidian Emerald
              Color(0xFF042B20), // Deep Islamic Forest Emerald
              Color(0xFF063A2B), // Rich Emerald Atmosphere
              Color(0xFF082232), // Midnight Teal Transition
              Color(0xFF020617)  // Deep Midnight Obsidian
            )
          )
        )
    ) {
      // Dynamic Hijri Atmospheric Canvas (Starry night particles, Ramadan Fanous, Dhul Hijjah Aura)
      HijriMonthAtmosphericCanvas(
        hijriMonth = hijriDate.month,
        hijriDay = hijriDate.day,
        modifier = Modifier.matchParentSize()
      )

      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 20.dp, vertical = 22.dp)
      ) {
        // Top Header Row
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Surface(
            shape = RoundedCornerShape(10.dp),
            color = Color.Black.copy(alpha = 0.35f),
            border = BorderStroke(0.8.dp, Color(0xFF6EE7B7).copy(alpha = 0.30f))
          ) {
            Text(
              text = if (selectedLang == "BN") "হিজরি চান্দ্রবর্ষ" else if (selectedLang == "AR") "التقويم الهجري" else "Hijri Calendar",
              style = TextStyle(
                fontSize = 11.5.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFD1FAE5)
              ),
              modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
            )
          }

          if (hijriDate.isWhiteDay) {
            Surface(
              shape = RoundedCornerShape(10.dp),
              color = Color(0xFFFEF3C7).copy(alpha = 0.92f),
              border = BorderStroke(0.8.dp, Color(0xFFF59E0B))
            ) {
              Text(
                text = "✨ আইয়ামে বিজ (রোজা)",
                style = TextStyle(fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF92400E)),
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // 1. SINGLE CENTRAL REALISTIC DYNAMIC MOON GRAPHIC (Based on current Hijri date - 23rd Rabi al-Thani)
        DynamicCalendarMoonPhaseView(
          hijriDay = hijriDate.day,
          modifier = Modifier.size(54.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Moon Phase Status Badge directly below central Moon
        Surface(
          shape = RoundedCornerShape(12.dp),
          color = Color.Black.copy(alpha = 0.38f),
          border = BorderStroke(1.dp, Color(0xFF6EE7B7).copy(alpha = 0.35f))
        ) {
          val moonPhaseName = when (hijriDate.day) {
            1 -> if (selectedLang == "BN") "নতুন চাঁদ (হিলাল শুরু)" else "New Moon (Hilal)"
            in 2..5 -> if (selectedLang == "BN") "হিলাল (অর্ধচন্দ্র)" else "Waxing Crescent"
            in 6..8 -> if (selectedLang == "BN") "প্রথম চতুর্থাংশ" else "First Quarter"
            in 9..12 -> if (selectedLang == "BN") "কূর্মাভ চাঁদ" else "Waxing Gibbous"
            in 13..15 -> if (selectedLang == "BN") "বদর (পূর্ণিমা / আইয়ামে বিজ)" else "Full Moon (Badr)"
            in 16..21 -> if (selectedLang == "BN") "হ্রাসমান কূর্মাভ চাঁদ" else "Waning Gibbous"
            in 22..25 -> if (selectedLang == "BN") "ক্ষয়িষ্ণু অর্ধচন্দ্র (২৩ই রবিউস সানি)" else "Waning Crescent (23rd Rabi al-Thani)"
            in 26..28 -> if (selectedLang == "BN") "ক্ষয়িষ্ণু অর্ধচন্দ্র" else "Waning Crescent"
            else -> if (selectedLang == "BN") "অমাবস্যা" else "Dark Moon"
          }
          Text(
            text = moonPhaseName,
            style = TextStyle(
              fontSize = 11.5.sp,
              fontWeight = FontWeight.SemiBold,
              color = Color(0xFFD1FAE5),
              letterSpacing = 0.3.sp
            ),
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp)
          )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // 3D Page-Flip Entrance Animated Date Number ("২৩")
        FlipDateCardNumber(
          dateText = toBnDigits(hijriDate.day),
          textStyle = TextStyle(
            fontSize = 66.sp,
            fontWeight = FontWeight.Black,
            color = Color(0xFFFEF9C3),
            letterSpacing = (-1.5).sp
          )
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Frosted Glass Container with High Contrast & Polished Typography for Month & Year
        Surface(
          shape = RoundedCornerShape(16.dp),
          color = Color(0xFF021B14).copy(alpha = 0.58f),
          border = BorderStroke(1.dp, Color(0xFF6EE7B7).copy(alpha = 0.32f)),
          shadowElevation = 2.dp,
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 11.dp)
          ) {
            Text(
              text = "${hijriDate.monthNameBn}, ${toBnDigits(hijriDate.year)} হিজরি",
              style = TextStyle(
                fontSize = 19.5.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF6EE7B7),
                letterSpacing = 0.5.sp
              ),
              textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
              text = "${hijriDate.monthNameAr} ${toBnDigits(hijriDate.year)} هـ",
              style = TextStyle(
                fontSize = 14.5.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White.copy(alpha = 0.90f),
                letterSpacing = 0.4.sp
              ),
              textAlign = TextAlign.Center
            )
          }
        }
      }
    }
  }

  Spacer(modifier = Modifier.height(14.dp))

  // 2. Moon Sighting Fine Adjustment Card (±1, ±2 days)
  Card(
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    modifier = Modifier.fillMaxWidth()
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(14.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
        Box(
          modifier = Modifier
            .size(34.dp)
            .clip(CircleShape)
            .background(RoyalEmerald.copy(alpha = 0.1f)),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.Tune,
            contentDescription = null,
            tint = RoyalEmerald,
            modifier = Modifier.size(18.dp)
          )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column {
          Text(
            text = "চাঁদ দেখার সমন্বয়",
            style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
          )
          Text(
            text = "স্থানীয় দর্শন অনুযায়ী সমন্বয়: (${if (dayAdjustment >= 0) "+$dayAdjustment" else "$dayAdjustment"} দিন)",
            style = TextStyle(fontSize = 10.5.sp, color = Color(0xFF64748B))
          )
        }
      }

      Row(verticalAlignment = Alignment.CenterVertically) {
        IconButton(
          onClick = { if (dayAdjustment > -2) onDayAdjustmentChange(dayAdjustment - 1) },
          modifier = Modifier.size(32.dp)
        ) {
          Icon(Icons.Default.ChevronLeft, contentDescription = "Decrease", tint = RoyalEmerald)
        }

        Text(
          text = "${if (dayAdjustment >= 0) "+$dayAdjustment" else "$dayAdjustment"}",
          style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Bold, color = RoyalEmerald),
          modifier = Modifier.padding(horizontal = 4.dp)
        )

        IconButton(
          onClick = { if (dayAdjustment < 2) onDayAdjustmentChange(dayAdjustment + 1) },
          modifier = Modifier.size(32.dp)
        ) {
          Icon(Icons.Default.ChevronRight, contentDescription = "Increase", tint = RoyalEmerald)
        }
      }
    }
  }

  Spacer(modifier = Modifier.height(16.dp))

  // Pulsing animation for active Current Month card
  val hijriPulseAnim = rememberInfiniteTransition(label = "hijri_pulse")
  val hijriPulseAlpha by hijriPulseAnim.animateFloat(
    initialValue = 0.45f,
    targetValue = 1.0f,
    animationSpec = infiniteRepeatable(
      animation = tween(1200, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "pulse_alpha"
  )

  // 3. Hijri 12 Months Grid (3 columns) with interactive modal click
  Card(
    shape = RoundedCornerShape(18.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    modifier = Modifier.fillMaxWidth()
  ) {
    Column(modifier = Modifier.padding(16.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.CalendarMonth,
            contentDescription = null,
            tint = RoyalEmerald,
            modifier = Modifier.size(20.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "হিজরি ১২ মাসের বর্ষপঞ্জি",
            style = TextStyle(fontSize = 14.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
          )
        }

        Surface(
          shape = RoundedCornerShape(8.dp),
          color = Color(0xFFECFDF5)
        ) {
          Text(
            text = "মাসে ট্যাপ করুন 👆",
            style = TextStyle(fontSize = 10.sp, fontWeight = FontWeight.SemiBold, color = RoyalEmerald),
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      val hijriMonths = listOf(
        Triple("মুহররম", "المحرّم", "৩০ দিন"),
        Triple("সফর", "صفر", "২৯ দিন"),
        Triple("রবিউল আউয়াল", "ربيع الأوّل", "৩০ দিন"),
        Triple("রবিউস সানি", "ربيع الثاني", "২৯ দিন"),
        Triple("জমাদিউল আউয়াল", "جمادى الأولى", "৩০ দিন"),
        Triple("জমাদিউস সানি", "جمادى الآخرة", "২৯ দিন"),
        Triple("রজব", "رجب", "৩০ দিন"),
        Triple("শাবান", "شعبان", "২৯ দিন"),
        Triple("রমজান", "রমضان", "৩০ দিন"),
        Triple("শাওয়াল", "شوّال", "২৯ দিন"),
        Triple("জিলকদ", "ذو القعدة", "৩০ দিন"),
        Triple("জিলহজ", "ذو الحجة", "২৯/৩০ দিন")
      )

      hijriMonths.chunked(3).forEachIndexed { rowIndex, rowMonths ->
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.5.dp),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          rowMonths.forEachIndexed { colIndex, (nameBn, nameAr, days) ->
            val monthNum = rowIndex * 3 + colIndex + 1 // 1..12
            val isCurrentMonth = hijriDate.month == monthNum
            val totalDays = if (monthNum % 2 == 1) 30 else 29

            Surface(
              shape = RoundedCornerShape(12.dp),
              color = if (isCurrentMonth) Color(0xFFECFDF5) else Color(0xFFF8FAFC),
              border = BorderStroke(
                if (isCurrentMonth) 2.5.dp else 1.dp,
                if (isCurrentMonth) RoyalEmerald.copy(alpha = hijriPulseAlpha) else Color(0xFFE2E8F0)
              ),
              shadowElevation = if (isCurrentMonth) 4.dp else 0.dp,
              modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(12.dp))
                .clickable {
                  val startCal = getHijriMonthStartGregorian(monthNum, hijriDate.year, dayAdjustment)
                  onOpenMonthModal(
                    MonthModalState(
                      calendarType = CalendarType.HIJRI,
                      monthIndex = monthNum - 1,
                      monthName = nameBn,
                      monthSubName = "$nameAr • $days",
                      year = hijriDate.year,
                      yearText = "${toBnDigits(hijriDate.year)} হিজরি",
                      totalDays = totalDays,
                      startGregorianCal = startCal,
                      isCurrentRunningMonth = isCurrentMonth,
                      todayDayNumber = if (isCurrentMonth) hijriDate.day else -1
                    )
                  )
                }
                .testTag("hijri_month_card_$monthNum")
            ) {
              Column(
                modifier = Modifier.padding(vertical = 10.dp, horizontal = 4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
              ) {
                if (isCurrentMonth) {
                  Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = RoyalEmerald,
                    modifier = Modifier.padding(bottom = 4.dp)
                  ) {
                    Text(
                      text = "চলতি মাস",
                      style = TextStyle(fontSize = 8.5.sp, fontWeight = FontWeight.Bold, color = Color.White),
                      modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                    )
                  }
                }

                Text(
                  text = nameBn,
                  style = TextStyle(
                    fontSize = 12.5.sp,
                    fontWeight = if (isCurrentMonth) FontWeight.ExtraBold else FontWeight.Bold,
                    color = if (isCurrentMonth) RoyalEmerald else Color(0xFF1E293B)
                  )
                )
                Text(
                  text = nameAr,
                  style = TextStyle(fontSize = 10.5.sp, color = if (isCurrentMonth) RoyalEmerald else Color(0xFF64748B))
                )
                Text(
                  text = days,
                  style = TextStyle(fontSize = 9.sp, color = Color(0xFF94A3B8))
                )
              }
            }
          }
        }
      }
    }
  }

  Spacer(modifier = Modifier.height(16.dp))

  // 4. Upcoming Islamic Events List
  Card(
    shape = RoundedCornerShape(18.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    modifier = Modifier.fillMaxWidth()
  ) {
    Column(modifier = Modifier.padding(16.dp)) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
          imageVector = Icons.Default.Event,
          contentDescription = null,
          tint = RoyalEmerald,
          modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = "ইসলামিক গুরুত্বপূর্ণ দিবস ও উৎসব",
          style = TextStyle(fontSize = 14.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
        )
      }

      Spacer(modifier = Modifier.height(12.dp))

      IslamicEventsMaster.allEvents.forEach { event ->
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(text = event.iconEmoji, fontSize = 22.sp)
          Spacer(modifier = Modifier.width(12.dp))
          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = event.titleBn,
              style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
            )
            Text(
              text = "${toBnDigits(event.hijriDay)}ই ${HijriCalendarCalculator.hijriMonthsBn[event.hijriMonth - 1]} • ${event.descriptionBn}",
              style = TextStyle(fontSize = 11.sp, color = Color(0xFF64748B), lineHeight = 15.sp)
            )
          }
        }
        HorizontalDivider(color = Color(0xFFF1F5F9))
      }
    }
  }
}

/**
 * 3. GREGORIAN CALENDAR TAB CONTENT
 */
@Composable
private fun GregorianCalendarContent(
  calendar: Calendar,
  gregDateStr: String,
  banglaDate: BanglaDateInfo,
  hijriDate: HijriDate,
  selectedLang: String,
  onOpenMonthModal: (MonthModalState) -> Unit
) {
  val day = calendar.get(Calendar.DAY_OF_MONTH)
  val month = calendar.get(Calendar.MONTH) // 0..11
  val year = calendar.get(Calendar.YEAR)
  val dayOfYear = calendar.get(Calendar.DAY_OF_YEAR)
  val weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR)

  // 1. Hero Gregorian Date Card with Dynamic Seasonal Canvas and 3D Flip Date Number
  Card(
    shape = RoundedCornerShape(22.dp),
    colors = CardDefaults.cardColors(containerColor = Color.Transparent),
    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
    modifier = Modifier
      .fillMaxWidth()
      .testTag("gregorian_hero_card")
  ) {
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .background(
          Brush.linearGradient(
            listOf(Color(0xFF1E3A8A), Color(0xFF2563EB), Color(0xFF3B82F6))
          )
        )
    ) {
      // Dynamic Gregorian Seasonal Canvas (Snowfall, Sunburst, Breeze, Autumn leaves)
      GregorianSeasonalCanvas(
        monthIndex = month,
        modifier = Modifier.matchParentSize()
      )

      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
          .fillMaxWidth()
          .padding(22.dp)
      ) {
        Surface(
          shape = RoundedCornerShape(10.dp),
          color = Color.White.copy(alpha = 0.2f)
        ) {
          Text(
            text = "📅 ইংরেজি / গ্রেগোরিয়ান ক্যালেন্ডার",
            style = TextStyle(fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = Color.White),
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
          )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Large prominent day number with 3D Page-Flip Animation
        FlipDateCardNumber(
          dateText = toBnDigits(day)
        )

        Text(
          text = gregDateStr,
          style = TextStyle(
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFDBEAFE)
          )
        )

        Spacer(modifier = Modifier.height(8.dp))

        Surface(
          shape = RoundedCornerShape(20.dp),
          color = Color.Black.copy(alpha = 0.22f)
        ) {
          Text(
            text = "বছরের ${toBnDigits(dayOfYear)} তম দিন • সপ্তাহ নম্বর ${toBnDigits(weekOfYear)}",
            style = TextStyle(fontSize = 12.sp, color = Color.White.copy(alpha = 0.92f)),
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 5.dp)
          )
        }
      }
    }
  }

  Spacer(modifier = Modifier.height(16.dp))

  // Pulsing animation for active Current Month card
  val gregPulseAnim = rememberInfiniteTransition(label = "greg_pulse")
  val gregPulseAlpha by gregPulseAnim.animateFloat(
    initialValue = 0.45f,
    targetValue = 1.0f,
    animationSpec = infiniteRepeatable(
      animation = tween(1200, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "pulse_alpha"
  )

  // 2. Gregorian 12 Months Grid (3 columns) with interactive modal click
  Card(
    shape = RoundedCornerShape(18.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    modifier = Modifier.fillMaxWidth()
  ) {
    Column(modifier = Modifier.padding(16.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.CalendarMonth,
            contentDescription = null,
            tint = Color(0xFF2563EB),
            modifier = Modifier.size(20.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "ইংরেজি ১২ মাসের ক্যালেন্ডার",
            style = TextStyle(fontSize = 14.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
          )
        }

        Surface(
          shape = RoundedCornerShape(8.dp),
          color = Color(0xFFEFF6FF)
        ) {
          Text(
            text = "মাসে ট্যাপ করুন 👆",
            style = TextStyle(fontSize = 10.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF2563EB)),
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      val isLeap = (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)
      val gregMonths = listOf(
        Triple("জানুয়ারি", "January", 31),
        Triple("ফেব্রুয়ারি", "February", if (isLeap) 29 else 28),
        Triple("মার্চ", "March", 31),
        Triple("এপ্রিল", "April", 30),
        Triple("মে", "May", 31),
        Triple("জুন", "June", 30),
        Triple("জুলাই", "July", 31),
        Triple("আগস্ট", "August", 31),
        Triple("সেপ্টেম্বর", "September", 30),
        Triple("অক্টোবর", "October", 31),
        Triple("নভেম্বর", "November", 30),
        Triple("ডিসেম্বর", "December", 31)
      )

      gregMonths.chunked(3).forEachIndexed { rowIndex, rowMonths ->
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.5.dp),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          rowMonths.forEachIndexed { colIndex, (nameBn, nameEn, totalDays) ->
            val monthIdx = rowIndex * 3 + colIndex
            val isCurrentMonth = month == monthIdx

            Surface(
              shape = RoundedCornerShape(12.dp),
              color = if (isCurrentMonth) Color(0xFFEFF6FF) else Color(0xFFF8FAFC),
              border = BorderStroke(
                if (isCurrentMonth) 2.5.dp else 1.dp,
                if (isCurrentMonth) Color(0xFF2563EB).copy(alpha = gregPulseAlpha) else Color(0xFFE2E8F0)
              ),
              shadowElevation = if (isCurrentMonth) 4.dp else 0.dp,
              modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(12.dp))
                .clickable {
                  val startCal = Calendar.getInstance().apply {
                    set(year, monthIdx, 1, 12, 0, 0)
                  }
                  onOpenMonthModal(
                    MonthModalState(
                      calendarType = CalendarType.GREGORIAN,
                      monthIndex = monthIdx,
                      monthName = nameBn,
                      monthSubName = "$nameEn • ${toBnDigits(totalDays)} দিন",
                      year = year,
                      yearText = "${toBnDigits(year)} সন",
                      totalDays = totalDays,
                      startGregorianCal = startCal,
                      isCurrentRunningMonth = isCurrentMonth,
                      todayDayNumber = if (isCurrentMonth) day else -1
                    )
                  )
                }
                .testTag("greg_month_card_$monthIdx")
            ) {
              Column(
                modifier = Modifier.padding(vertical = 10.dp, horizontal = 4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
              ) {
                if (isCurrentMonth) {
                  Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Color(0xFF2563EB),
                    modifier = Modifier.padding(bottom = 4.dp)
                  ) {
                    Text(
                      text = "চলতি মাস",
                      style = TextStyle(fontSize = 8.5.sp, fontWeight = FontWeight.Bold, color = Color.White),
                      modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                    )
                  }
                }

                Text(
                  text = nameBn,
                  style = TextStyle(
                    fontSize = 12.5.sp,
                    fontWeight = if (isCurrentMonth) FontWeight.ExtraBold else FontWeight.Bold,
                    color = if (isCurrentMonth) Color(0xFF1D4ED8) else Color(0xFF1E293B)
                  )
                )
                Text(
                  text = nameEn,
                  style = TextStyle(fontSize = 10.sp, color = Color(0xFF64748B))
                )
                Text(
                  text = "${toBnDigits(totalDays)} দিন",
                  style = TextStyle(fontSize = 9.sp, color = Color(0xFF94A3B8))
                )
              }
            }
          }
        }
      }
    }
  }

  Spacer(modifier = Modifier.height(16.dp))

  // 3. 3-in-1 Synchronized Date Summary Card
  Card(
    shape = RoundedCornerShape(18.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    modifier = Modifier.fillMaxWidth()
  ) {
    Column(modifier = Modifier.padding(16.dp)) {
      Text(
        text = "সমন্বিত ৩টি ক্যালেন্ডার সারসংক্ষেপ",
        style = TextStyle(fontSize = 14.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
      )
      Spacer(modifier = Modifier.height(12.dp))

      // Row 1: Bangla
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Text(text = "🇧🇩 বাংলা সন:", style = TextStyle(fontSize = 12.sp, color = Color(0xFF64748B)))
        Text(
          text = "${toBnDigits(banglaDate.day)} ${banglaDate.monthNameBn}, ${toBnDigits(banglaDate.year)} বঙ্গাব্দ (${banglaDate.seasonNameBn})",
          style = TextStyle(fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = RoyalEmerald)
        )
      }

      HorizontalDivider(color = Color(0xFFF1F5F9))

      // Row 2: Hijri
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Text(text = "🌙 হিজরি সন:", style = TextStyle(fontSize = 12.sp, color = Color(0xFF64748B)))
        Text(
          text = "${toBnDigits(hijriDate.day)} ${hijriDate.monthNameBn}, ${toBnDigits(hijriDate.year)} হিজরি",
          style = TextStyle(fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF047857))
        )
      }

      HorizontalDivider(color = Color(0xFFF1F5F9))

      // Row 3: Gregorian
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Text(text = "📅 ইংরেজি সন:", style = TextStyle(fontSize = 12.sp, color = Color(0xFF64748B)))
        Text(
          text = "$day/${month + 1}/$year ($gregDateStr)",
          style = TextStyle(fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2563EB))
        )
      }
    }
  }
}

/**
 * 4. INTERACTIVE MONTHLY CALENDAR DETAIL MODAL DIALOG
 * - Displays 7-day header (রবি, সোম, মঙ্গল, বুধ, বৃহস্পতি, শুক্র, শনি)
 * - Complete day grid mapped correctly to weekdays
 * - Weekend highlighting (Friday/Saturday)
 * - Government & Islamic holidays marked with distinct dots/badges
 * - Interactive date clicking with Gregorian, Hijri, and Bangla converted equivalents
 */
@Composable
fun MonthCalendarDetailDialog(
  modalState: MonthModalState,
  dayAdjustment: Int,
  selectedLang: String,
  onDismiss: () -> Unit
) {
  // Day of week of the 1st day of this month
  // Calendar.SUNDAY = 1, MONDAY = 2, ... SATURDAY = 7
  val startDayOfWeek = modalState.startGregorianCal.get(Calendar.DAY_OF_WEEK)
  // Index from 0 (Sunday/রবি) to 6 (Saturday/শনি)
  val leadingEmptyCells = startDayOfWeek - 1

  // Currently selected date inside the modal
  var selectedDayNumber by remember {
    mutableIntStateOf(if (modalState.todayDayNumber > 0) modalState.todayDayNumber else 1)
  }

  // Calculate day details for the selected day
  val selectedDayCal = remember(selectedDayNumber) {
    (modalState.startGregorianCal.clone() as Calendar).apply {
      add(Calendar.DAY_OF_YEAR, selectedDayNumber - 1)
    }
  }

  val selectedBangla = remember(selectedDayCal) {
    BanglaDateCalculator.calculateBanglaDate(selectedDayCal)
  }
  val selectedHijri = remember(selectedDayCal, dayAdjustment) {
    HijriCalendarCalculator.calculateHijri(selectedDayCal, dayAdjustment)
  }

  val gregFormat = remember { SimpleDateFormat("d MMMM yyyy", Locale.ENGLISH) }
  val selectedGregStr = remember(selectedDayCal) { gregFormat.format(selectedDayCal.time) }

  val dayOfWeekNameBn = when (selectedDayCal.get(Calendar.DAY_OF_WEEK)) {
    Calendar.SUNDAY -> "রবিবার"
    Calendar.MONDAY -> "সোমবার"
    Calendar.TUESDAY -> "মঙ্গলবার"
    Calendar.WEDNESDAY -> "বুধবার"
    Calendar.THURSDAY -> "বৃহস্পতিবার"
    Calendar.FRIDAY -> "শুক্রবার"
    Calendar.SATURDAY -> "শনিবার"
    else -> "দিন"
  }

  val isFriday = selectedDayCal.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY
  val isSaturday = selectedDayCal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY

  // Check holiday or special event
  val holidayNote = remember(modalState, selectedDayNumber, selectedBangla, selectedHijri) {
    getHolidayNote(modalState.calendarType, selectedDayNumber, selectedBangla, selectedHijri)
  }

  // Spring-scale popup animation
  val modalScale = remember { Animatable(0.82f) }
  val modalAlpha = remember { Animatable(0f) }
  LaunchedEffect(Unit) {
    modalScale.animateTo(
      targetValue = 1f,
      animationSpec = spring(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessLow
      )
    )
  }
  LaunchedEffect(Unit) {
    modalAlpha.animateTo(
      targetValue = 1f,
      animationSpec = tween(durationMillis = 280, easing = FastOutSlowInEasing)
    )
  }

  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(usePlatformDefaultWidth = false)
  ) {
    Surface(
      shape = RoundedCornerShape(24.dp),
      color = Color.White,
      shadowElevation = 16.dp,
      modifier = Modifier
        .fillMaxWidth(0.95f)
        .widthIn(max = 480.dp)
        .graphicsLayer {
          scaleX = modalScale.value
          scaleY = modalScale.value
          alpha = modalAlpha.value
        }
        .testTag("month_calendar_modal")
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .verticalScroll(rememberScrollState())
          .padding(20.dp)
      ) {
        // Modal Header
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            val iconEmoji = when (modalState.calendarType) {
              CalendarType.BANGLA -> "🇧🇩"
              CalendarType.HIJRI -> "🌙"
              CalendarType.GREGORIAN -> "📅"
            }
            Text(text = iconEmoji, fontSize = 24.sp)
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text(
                text = "${modalState.monthName} ${modalState.yearText}",
                style = TextStyle(
                  fontSize = 18.sp,
                  fontWeight = FontWeight.ExtraBold,
                  color = RoyalEmerald
                )
              )
              Text(
                text = modalState.monthSubName,
                style = TextStyle(fontSize = 11.5.sp, color = Color(0xFF64748B))
              )
            }
          }

          IconButton(
            onClick = onDismiss,
            modifier = Modifier
              .size(34.dp)
              .clip(CircleShape)
              .background(Color(0xFFF1F5F9))
              .testTag("modal_close_button")
          ) {
            Icon(
              imageVector = Icons.Default.Close,
              contentDescription = "Close Modal",
              tint = Color(0xFF475569),
              modifier = Modifier.size(18.dp)
            )
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 7-day Weekdays Header (রবি, সোম, মঙ্গল, বুধ, বৃহস্পতি, শুক্র, শনি)
        val weekdayHeaders = listOf(
          Pair("রবি", false),
          Pair("সোম", false),
          Pair("মঙ্গল", false),
          Pair("বুধ", false),
          Pair("বৃহস্পতি", false),
          Pair("শুক্র", true),  // Weekend
          Pair("শনি", true)   // Weekend
        )

        Row(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFFF8FAFC))
            .padding(vertical = 8.dp),
          horizontalArrangement = Arrangement.SpaceAround
        ) {
          weekdayHeaders.forEach { (name, isWeekend) ->
            Box(
              modifier = Modifier.weight(1f),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = name,
                style = TextStyle(
                  fontSize = 12.sp,
                  fontWeight = FontWeight.Bold,
                  color = if (isWeekend) Color(0xFFDC2626) else Color(0xFF475569)
                )
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Monthly Day Grid (Calculates accurate weekdays)
        val totalCells = leadingEmptyCells + modalState.totalDays
        val rows = (totalCells + 6) / 7

        for (r in 0 until rows) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 3.dp),
            horizontalArrangement = Arrangement.SpaceAround
          ) {
            for (c in 0 until 7) {
              val cellIndex = r * 7 + c
              val dayNum = cellIndex - leadingEmptyCells + 1

              if (dayNum in 1..modalState.totalDays) {
                val isToday = modalState.isCurrentRunningMonth && dayNum == modalState.todayDayNumber
                val isSelected = dayNum == selectedDayNumber
                val isWeekendCol = c == 5 || c == 6 // Friday or Saturday

                // Check if this date has a holiday
                val cellCal = (modalState.startGregorianCal.clone() as Calendar).apply {
                  add(Calendar.DAY_OF_YEAR, dayNum - 1)
                }
                val cellBangla = BanglaDateCalculator.calculateBanglaDate(cellCal)
                val cellHijri = HijriCalendarCalculator.calculateHijri(cellCal, dayAdjustment)
                val cellHoliday = getHolidayNote(modalState.calendarType, dayNum, cellBangla, cellHijri)

                Box(
                  modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f)
                    .padding(2.5.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                      when {
                        isToday -> RoyalEmerald
                        isSelected -> Color(0xFFECFDF5)
                        isWeekendCol -> Color(0xFFFEF2F2).copy(alpha = 0.6f)
                        else -> Color(0xFFF8FAFC)
                      }
                    )
                    .border(
                      width = when {
                        isSelected -> 2.dp
                        isToday -> 0.dp
                        else -> 0.5.dp
                      },
                      color = when {
                        isSelected -> RoyalEmerald
                        else -> Color(0xFFE2E8F0)
                      },
                      shape = RoundedCornerShape(10.dp)
                    )
                    .clickable { selectedDayNumber = dayNum },
                  contentAlignment = Alignment.Center
                ) {
                  Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                  ) {
                    Text(
                      text = toBnDigits(dayNum),
                      style = TextStyle(
                        fontSize = 13.5.sp,
                        fontWeight = if (isToday || isSelected) FontWeight.ExtraBold else FontWeight.Medium,
                        color = when {
                          isToday -> Color.White
                          isSelected -> RoyalEmerald
                          isWeekendCol -> Color(0xFFDC2626)
                          else -> Color(0xFF1E293B)
                        }
                      )
                    )

                    // Holiday / special event dot
                    if (cellHoliday != null) {
                      Box(
                        modifier = Modifier
                          .size(5.dp)
                          .clip(CircleShape)
                          .background(if (isToday) Color(0xFFFDE047) else Color(0xFFDC2626))
                      )
                    }
                  }
                }
              } else {
                // Empty blank space before day 1 or after last day
                Spacer(modifier = Modifier.weight(1f))
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Selected Day Details Card (Displays equivalents in all 3 calendars)
        Surface(
          shape = RoundedCornerShape(16.dp),
          color = Color(0xFFF8FAFC),
          border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "তারিখের বিস্তারিত বিবরণ",
                style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF64748B))
              )

              if (isFriday) {
                Surface(
                  shape = RoundedCornerShape(6.dp),
                  color = Color(0xFFECFDF5)
                ) {
                  Text(
                    text = "🕌 জুমু'আহ মুবারক",
                    style = TextStyle(fontSize = 10.sp, fontWeight = FontWeight.Bold, color = RoyalEmerald),
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                  )
                }
              } else if (isSaturday) {
                Surface(
                  shape = RoundedCornerShape(6.dp),
                  color = Color(0xFFFEF2F2)
                ) {
                  Text(
                    text = "সাপ্তাহিক ছুটি",
                    style = TextStyle(fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFFDC2626)),
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                  )
                }
              }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Prominent main selected date
            Text(
              text = when (modalState.calendarType) {
                CalendarType.BANGLA -> "${toBnDigits(selectedDayNumber)}ই ${modalState.monthName}, ${modalState.yearText} ($dayOfWeekNameBn)"
                CalendarType.HIJRI -> "${toBnDigits(selectedDayNumber)}ই ${modalState.monthName}, ${modalState.yearText} ($dayOfWeekNameBn)"
                CalendarType.GREGORIAN -> "${toBnDigits(selectedDayNumber)} ${modalState.monthName} ${modalState.yearText} ($dayOfWeekNameBn)"
              },
              style = TextStyle(
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = RoyalEmerald
              )
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Converted equivalents grid
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              // 1. Bangla
              Surface(
                shape = RoundedCornerShape(10.dp),
                color = Color.White,
                border = BorderStroke(0.5.dp, Color(0xFFCBD5E1)),
                modifier = Modifier.weight(1f)
              ) {
                Column(modifier = Modifier.padding(8.dp)) {
                  Text(text = "🇧🇩 বাংলা", fontSize = 10.sp, color = Color(0xFF64748B), fontWeight = FontWeight.Medium)
                  Text(
                    text = "${toBnDigits(selectedBangla.day)} ${selectedBangla.monthNameBn}",
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = RoyalEmerald
                  )
                  Text(text = "${toBnDigits(selectedBangla.year)} বঙ্গাব্দ", fontSize = 9.sp, color = Color(0xFF94A3B8))
                }
              }

              // 2. Hijri
              Surface(
                shape = RoundedCornerShape(10.dp),
                color = Color.White,
                border = BorderStroke(0.5.dp, Color(0xFFCBD5E1)),
                modifier = Modifier.weight(1f)
              ) {
                Column(modifier = Modifier.padding(8.dp)) {
                  Text(text = "🌙 হিজরি", fontSize = 10.sp, color = Color(0xFF64748B), fontWeight = FontWeight.Medium)
                  Text(
                    text = "${toBnDigits(selectedHijri.day)} ${selectedHijri.monthNameBn}",
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF047857)
                  )
                  Text(text = "${toBnDigits(selectedHijri.year)} হিজরি", fontSize = 9.sp, color = Color(0xFF94A3B8))
                }
              }

              // 3. Gregorian
              Surface(
                shape = RoundedCornerShape(10.dp),
                color = Color.White,
                border = BorderStroke(0.5.dp, Color(0xFFCBD5E1)),
                modifier = Modifier.weight(1f)
              ) {
                Column(modifier = Modifier.padding(8.dp)) {
                  Text(text = "📅 ইংরেজি", fontSize = 10.sp, color = Color(0xFF64748B), fontWeight = FontWeight.Medium)
                  Text(
                    text = "${selectedDayCal.get(Calendar.DAY_OF_MONTH)} ${getEnglishMonthShort(selectedDayCal.get(Calendar.MONTH))}",
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2563EB)
                  )
                  Text(text = "${selectedDayCal.get(Calendar.YEAR)}", fontSize = 9.sp, color = Color(0xFF94A3B8))
                }
              }
            }

            // Holiday notice if exists
            if (holidayNote != null) {
              Spacer(modifier = Modifier.height(10.dp))
              Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color(0xFFFEF2F2),
                border = BorderStroke(1.dp, Color(0xFFFECACA)),
                modifier = Modifier.fillMaxWidth()
              ) {
                Row(
                  modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Text(text = "🎉", fontSize = 14.sp)
                  Spacer(modifier = Modifier.width(6.dp))
                  Text(
                    text = holidayNote,
                    style = TextStyle(fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFFB91C1C))
                  )
                }
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
          onClick = onDismiss,
          colors = ButtonDefaults.buttonColors(containerColor = RoyalEmerald),
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Text(text = "ঠিক আছে (বন্ধ করুন)", fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }
      }
    }
  }
}

/**
 * Calendar Helper Functions
 */
private fun getBanglaMonthStartGregorian(monthIndex: Int, banglaYear: Int): Calendar {
  val cal = Calendar.getInstance()
  val gregYear = if (monthIndex >= 9) banglaYear + 594 else banglaYear + 593
  when (monthIndex) {
    0 -> cal.set(gregYear, Calendar.APRIL, 14, 12, 0, 0)
    1 -> cal.set(gregYear, Calendar.MAY, 15, 12, 0, 0)
    2 -> cal.set(gregYear, Calendar.JUNE, 15, 12, 0, 0)
    3 -> cal.set(gregYear, Calendar.JULY, 16, 12, 0, 0)
    4 -> cal.set(gregYear, Calendar.AUGUST, 16, 12, 0, 0)
    5 -> cal.set(gregYear, Calendar.SEPTEMBER, 16, 12, 0, 0)
    6 -> cal.set(gregYear, Calendar.OCTOBER, 17, 12, 0, 0)
    7 -> cal.set(gregYear, Calendar.NOVEMBER, 16, 12, 0, 0)
    8 -> cal.set(gregYear, Calendar.DECEMBER, 16, 12, 0, 0)
    9 -> cal.set(gregYear, Calendar.JANUARY, 15, 12, 0, 0)
    10 -> cal.set(gregYear, Calendar.FEBRUARY, 14, 12, 0, 0)
    11 -> cal.set(gregYear, Calendar.MARCH, 15, 12, 0, 0)
  }
  cal.set(Calendar.MILLISECOND, 0)
  return cal
}

private fun getHijriMonthStartGregorian(targetMonth: Int, targetYear: Int, dayAdjustment: Int): Calendar {
  val cal = Calendar.getInstance()
  cal.set(Calendar.HOUR_OF_DAY, 12)
  cal.set(Calendar.MINUTE, 0)
  cal.set(Calendar.SECOND, 0)
  cal.set(Calendar.MILLISECOND, 0)

  val todayH = HijriCalendarCalculator.calculateHijri(cal, dayAdjustment)
  val monthDiff = (targetYear - todayH.year) * 12 + (targetMonth - todayH.month)
  val approxDaysDiff = (monthDiff * 29.53).toInt() - (todayH.day - 1)
  cal.add(Calendar.DAY_OF_YEAR, approxDaysDiff)

  var currentH = HijriCalendarCalculator.calculateHijri(cal, dayAdjustment)
  var loopGuard = 0
  while (currentH.day > 1 && loopGuard < 10) {
    cal.add(Calendar.DAY_OF_YEAR, -(currentH.day - 1))
    currentH = HijriCalendarCalculator.calculateHijri(cal, dayAdjustment)
    loopGuard++
  }
  return cal
}

private fun getEnglishMonthShort(monthIdx: Int): String = when (monthIdx) {
  0 -> "Jan"
  1 -> "Feb"
  2 -> "Mar"
  3 -> "Apr"
  4 -> "May"
  5 -> "Jun"
  6 -> "Jul"
  7 -> "Aug"
  8 -> "Sep"
  9 -> "Oct"
  10 -> "Nov"
  11 -> "Dec"
  else -> ""
}

private fun getHolidayNote(
  type: CalendarType,
  dayNum: Int,
  bangla: BanglaDateInfo,
  hijri: HijriDate
): String? {
  // Check known government & national holidays
  if (bangla.monthNameBn == "বৈশাখ" && bangla.day == 1) return "পহেলা বৈশাখ (বাংলা নববর্ষ) 🎊"
  if (bangla.monthNameBn == "জ্যৈষ্ঠ" && bangla.day == 18) return "আন্তর্জাতিক শ্রমিক দিবস (মে দিবস)"
  if (bangla.monthNameBn == "পৌষ" && bangla.day == 1) return "মহান বিজয় দিবস 🇧🇩"
  if (bangla.monthNameBn == "পৌষ" && bangla.day == 10) return "শুভ বড়দিন (ক্রিসমাস) 🎄"
  if (bangla.monthNameBn == "ফাল্গুন" && bangla.day == 9) return "আন্তর্জাতিক মাতৃভাষা ও শহীদ দিবস 🌺"
  if (bangla.monthNameBn == "চৈত্র" && bangla.day == 12) return "মহান স্বাধীনতা ও জাতীয় দিবস 🇧🇩"

  // Check known Islamic holidays & observances
  if (hijri.month == 1 && hijri.day == 1) return "পবিত্র হিজরি নববর্ষ 🌙"
  if (hijri.month == 1 && hijri.day == 10) return "পবিত্র আশুরা (রোজা) 🕌"
  if (hijri.month == 3 && hijri.day == 12) return "পবিত্র ঈদে মিলাদুন্নবী (সাঃ) ✨"
  if (hijri.month == 7 && hijri.day == 27) return "পবিত্র শবে মেরাজ 🌌"
  if (hijri.month == 8 && hijri.day == 15) return "পবিত্র শবে বরাত (লাইলাতুল বরাত) 🌟"
  if (hijri.month == 9 && hijri.day == 1) return "পবিত্র মাহে রমজান শুরু 🌙"
  if (hijri.month == 9 && hijri.day == 27) return "পবিত্র শবে কদর (লাইলাতুল কদর) 👑"
  if (hijri.month == 10 && hijri.day in 1..3) return "পবিত্র ঈদুল ফিতর 🎉"
  if (hijri.month == 12 && hijri.day == 9) return "পবিত্র ইয়াওমে আরাফাহ (হজ) ⛰️"
  if (hijri.month == 12 && hijri.day in 10..12) return "পবিত্র ঈদুল আজহা (কোরবানি) 🐑"
  if (hijri.isWhiteDay) return "আইয়ামে বিজ (নফল রোজা) ✨"

  return null
}
