package com.example

import android.content.Context
import android.content.Intent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.automirrored.filled.VolumeOff
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.DownloadDone
import androidx.compose.material.icons.filled.Feedback
import androidx.compose.material.icons.filled.Gamepad
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Mosque
import androidx.compose.material.icons.filled.NightsStay
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.NotificationsOff
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.RateReview
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.WbTwilight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.islamic.HijriCalendarCalculator
import com.example.islamic.IslamicOfflineRepository
import com.example.islamic.DailyPrayerSchedule
import com.example.islamic.PrayerEntry
import com.example.islamic.PrayerTimesCalculator
import com.example.ui.theme.RoyalEmerald
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * Bangla and Hijri Date Support and Digit Converters
 */
private fun toBnDigits(str: String): String {
  val bnDigits = charArrayOf('০', '১', '২', '৩', '৪', '৫', '৬', '৭', '৮', '৯')
  val sb = StringBuilder()
  for (c in str) {
    if (c in '0'..'9') {
      sb.append(bnDigits[c - '0'])
    } else {
      sb.append(c)
    }
  }
  return sb.toString()
}

private fun toBnDigits(number: Long): String = toBnDigits(number.toString())
private fun toBnDigits(number: Int): String = toBnDigits(number.toString())

private fun toArDigits(str: String): String {
  val arDigits = charArrayOf('٠', '١', '٢', '٣', '٤', '٥', '٦', '٧', '٨', '٩')
  val sb = StringBuilder()
  for (c in str) {
    if (c in '0'..'9') {
      sb.append(arDigits[c - '0'])
    } else {
      sb.append(c)
    }
  }
  return sb.toString()
}

private fun toArDigits(number: Long): String = toArDigits(number.toString())
private fun toArDigits(number: Int): String = toArDigits(number.toString())

private fun formatPrayerStartTime(timeStr: String, selectedLang: String): String {
  val isBn = selectedLang == "BN"
  val isAr = selectedLang == "AR"
  val isPm = timeStr.contains("PM", ignoreCase = true)
  val isAm = timeStr.contains("AM", ignoreCase = true)
  val clean = timeStr.replace("(?i)\\s*(AM|PM)".toRegex(), "").trim()
  val parts = clean.split(":")
  val timePart = if (parts.size == 2) {
    val h = parts[0].padStart(2, '0')
    val m = parts[1].padStart(2, '0')
    "$h:$m"
  } else {
    clean
  }
  return when {
    isBn -> {
      val suffix = if (isPm) " PM" else if (isAm) " AM" else ""
      toBnDigits(timePart) + suffix
    }
    isAr -> {
      val suffix = if (isPm) " م" else if (isAm) " ص" else ""
      toArDigits(timePart) + suffix
    }
    else -> {
      val suffix = if (isPm) " PM" else if (isAm) " AM" else ""
      timePart + suffix
    }
  }
}

private fun formatSunArcTime(timeStr: String, toBn: Boolean): String {
  val clean = timeStr.replace(" AM", "").replace(" PM", "").trim()
  val parts = clean.split(":")
  if (parts.size == 2) {
    val h = parts[0].toIntOrNull() ?: 0
    val m = parts[1]
    val res = "$h:$m"
    return if (toBn) toBnDigits(res) else res
  }
  return if (toBn) toBnDigits(clean) else clean
}

data class BanglaDateInfo(
  val dayOfWeekBn: String,
  val day: Int,
  val monthNameBn: String,
  val year: Int,
  val seasonNameBn: String
) {
  fun format(): String = "$dayOfWeekBn, ${toBnDigits(day)} $monthNameBn ${toBnDigits(year)} বঙ্গাব্দ, $seasonNameBn"
}

object BanglaDateCalculator {
  fun calculateBanglaDate(calendar: Calendar = Calendar.getInstance()): BanglaDateInfo {
    val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
    val dayOfWeekBn = when (dayOfWeek) {
      Calendar.SUNDAY -> "রবিবার"
      Calendar.MONDAY -> "সোমবার"
      Calendar.TUESDAY -> "মঙ্গলবার"
      Calendar.WEDNESDAY -> "বুধবার"
      Calendar.THURSDAY -> "বৃহস্পতিবার"
      Calendar.FRIDAY -> "শুক্রবার"
      Calendar.SATURDAY -> "শনিবার"
      else -> "সোমবার"
    }

    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH) // 0-indexed (Jan=0, Sep=8)
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    val isLeap = (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)

    val banglaYear = if (month > 3 || (month == 3 && day >= 14)) year - 593 else year - 594

    val (bMonth, bDay, season) = when {
      // Boishakh: Apr 14 - May 14 (31 days)
      (month == 3 && day >= 14) -> Triple("বৈশাখ", day - 13, "গ্রীষ্মকাল")
      (month == 4 && day <= 14) -> Triple("বৈশাখ", day + 17, "গ্রীষ্মকাল")
      // Jyoishtho: May 15 - Jun 14 (31 days)
      (month == 4 && day >= 15) -> Triple("জ্যৈষ্ঠ", day - 14, "গ্রীষ্মকাল")
      (month == 5 && day <= 14) -> Triple("জ্যৈষ্ঠ", day + 17, "গ্রীষ্মকাল")
      // Asharh: Jun 15 - Jul 15 (31 days)
      (month == 5 && day >= 15) -> Triple("আষাঢ়", day - 14, "বর্ষাকাল")
      (month == 6 && day <= 15) -> Triple("আষাঢ়", day + 16, "বর্ষাকাল")
      // Shrabon: Jul 16 - Aug 15 (31 days)
      (month == 6 && day >= 16) -> Triple("শ্রাবণ", day - 15, "বর্ষাকাল")
      (month == 7 && day <= 15) -> Triple("শ্রাবণ", day + 16, "বর্ষাকাল")
      // Bhadro: Aug 16 - Sep 15 (31 days)
      (month == 7 && day >= 16) -> Triple("ভাদ্র", day - 15, "শরৎকাল")
      (month == 8 && day <= 15) -> Triple("ভাদ্র", day + 16, "শরৎকাল")
      // Ashwin: Sep 16 - Oct 16 (31 days)
      (month == 8 && day >= 16) -> Triple("আশ্বিন", day - 15, "শরৎকাল")
      (month == 9 && day <= 16) -> Triple("আশ্বিন", day + 15, "শরৎকাল")
      // Kartik: Oct 17 - Nov 15 (30 days)
      (month == 9 && day >= 17) -> Triple("কার্তিক", day - 16, "হেমন্তকাল")
      (month == 10 && day <= 15) -> Triple("কার্তিক", day + 15, "হেমন্তকাল")
      // Agrahayan: Nov 16 - Dec 15 (30 days)
      (month == 10 && day >= 16) -> Triple("অগ্রহায়ণ", day - 15, "হেমন্তকাল")
      (month == 11 && day <= 15) -> Triple("অগ্রহায়ণ", day + 15, "হেমন্তকাল")
      // Poush: Dec 16 - Jan 14 (30 days)
      (month == 11 && day >= 16) -> Triple("পৌষ", day - 15, "শীতকাল")
      (month == 0 && day <= 14) -> Triple("পৌষ", day + 16, "শীতকাল")
      // Magh: Jan 15 - Feb 13 (30 days)
      (month == 0 && day >= 15) -> Triple("মাঘ", day - 14, "শীতকাল")
      (month == 1 && day <= 13) -> Triple("মাঘ", day + 17, "শীতকাল")
      // Falgun: Feb 14 - Mar 14 (29 or 30 days)
      (month == 1 && day >= 14) -> Triple("ফাল্গুন", day - 13, "বসন্তকাল")
      (month == 2 && day <= 14) -> Triple("ফাল্গুন", day + (if (isLeap) 16 else 15), "বসন্তকাল")
      // Chaitra: Mar 15 - Apr 13 (30 days)
      (month == 2 && day >= 15) -> Triple("চৈত্র", day - 14, "বসন্তকাল")
      else -> Triple("চৈত্র", day + 17, "বসন্তকাল")
    }

    return BanglaDateInfo(dayOfWeekBn, bDay, bMonth, banglaYear, season)
  }
}

private data class WaqtQuad(
  val current: PrayerEntry,
  val next: PrayerEntry,
  val startMillis: Long,
  val endMillis: Long
)

/**
 * 1. PRAYER TIME CARD (SALAT TIME) WITH SKY BLUE / CYAN GRADIENT & HIGHLIGHTED PROGRESS
 * Fully dynamic real-time device clock and GPS location engine.
 * Real-time per-second countdown, dynamic active Waqt evaluation, and calculated progress bar.
 */
@Composable
fun PrayerTimeCard(
  prayerSchedule: DailyPrayerSchedule,
  selectedLang: String,
  offlineRepo: IslamicOfflineRepository,
  onOpenFullTimetable: () -> Unit,
  onMuteToggle: (Boolean) -> Unit,
  onRefreshLocation: (() -> Unit)? = null,
  modifier: Modifier = Modifier
) {
  com.example.islamic.DynamicPrayerTimesHeroCard(
    prayerSchedule = prayerSchedule,
    selectedLang = selectedLang,
    offlineRepo = offlineRepo,
    onOpenFullTimetable = onOpenFullTimetable,
    onRefreshLocation = onRefreshLocation,
    modifier = modifier
  )
}

/**
 * 2. MAIN GRID MENU (2 COLUMNS, 6 FEATURE CARDS)
 */
data class MainGridItem(
  val id: String,
  val titleBn: String,
  val titleEn: String,
  val subtitleBn: String,
  val subtitleEn: String,
  val iconEmoji: String,
  val iconVector: ImageVector,
  val badgeTextBn: String,
  val accentColor: Color,
  val testTag: String
)

@Composable
fun MainGridMenu(
  selectedLang: String,
  onItemClick: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  val gridItems = listOf(
    MainGridItem(
      id = "arbi_horof",
      titleBn = "আরবি হরফ",
      titleEn = "Arbi Horof",
      subtitleBn = "২৯টি হরফ ও মাখরাজ",
      subtitleEn = "Alphabet & Articulation",
      iconEmoji = "أ",
      iconVector = Icons.Default.AutoAwesome,
      badgeTextBn = "২৯ হরফ",
      accentColor = Color(0xFF0A5C36),
      testTag = "grid_item_arbi_horof"
    ),
    MainGridItem(
      id = "game_mode",
      titleBn = "GAME",
      titleEn = "GAME",
      subtitleBn = "৫টি গেমিফাইড মোড",
      subtitleEn = "Interactive Modes",
      iconEmoji = "🎮",
      iconVector = Icons.Default.Gamepad,
      badgeTextBn = "৫ গেম",
      accentColor = Color(0xFFEAB308),
      testTag = "grid_item_game"
    ),
    MainGridItem(
      id = "kaida_education",
      titleBn = "কায়দা শিক্ষা",
      titleEn = "Kaida Education",
      subtitleBn = "৯ অধ্যায়ের কায়দা ও তাজবীদ",
      subtitleEn = "9 Chapters & Tajweed Book",
      iconEmoji = "📖",
      iconVector = Icons.AutoMirrored.Filled.MenuBook,
      badgeTextBn = "৯ অধ্যায়",
      accentColor = Color(0xFF059669),
      testTag = "grid_item_kaida"
    ),
    MainGridItem(
      id = "ampara_surahs",
      titleBn = "আমপাড়া ও ছোট সূরা",
      titleEn = "Ampara & Surahs",
      subtitleBn = "আয়াতভিত্তিক তিলাওয়াত ও হিফজ",
      subtitleEn = "Ayah Reader & Hifz",
      iconEmoji = "📜",
      iconVector = Icons.AutoMirrored.Filled.MenuBook,
      badgeTextBn = "আমপাড়া",
      accentColor = Color(0xFF0D9488),
      testTag = "grid_item_ampara"
    ),
    MainGridItem(
      id = "asmaul_husna",
      titleBn = "আসমাউল হুসনা",
      titleEn = "Asmaul Husna",
      subtitleBn = "আল্লাহর ৯৯টি গুণবাচক নাম",
      subtitleEn = "99 Divine Names",
      iconEmoji = "💎",
      iconVector = Icons.Default.Book,
      badgeTextBn = "৯৯ নাম",
      accentColor = Color(0xFF3B82F6),
      testTag = "grid_item_asmaul_husna"
    ),
    MainGridItem(
      id = "salat_guide",
      titleBn = "নামাজ শিক্ষা",
      titleEn = "Salat",
      subtitleBn = "নিয়ম, রাকাত ও হুকুমাত",
      subtitleEn = "Prayer Rules & Guide",
      iconEmoji = "🕌",
      iconVector = Icons.Default.Mosque,
      badgeTextBn = "পূর্ণ গাইড",
      accentColor = Color(0xFF059669),
      testTag = "grid_item_salat"
    ),
    MainGridItem(
      id = "hadith_collection",
      titleBn = "হাদিস সম্ভার",
      titleEn = "Hadith",
      subtitleBn = "বিষয়ভিত্তিক সহিহ হাদিস",
      subtitleEn = "Categorized Hadith",
      iconEmoji = "📜",
      iconVector = Icons.Default.Star,
      badgeTextBn = "সহিহ হাদিস",
      accentColor = Color(0xFF8B5CF6),
      testTag = "grid_item_hadith"
    )
  )

  Column(modifier = modifier.fillMaxWidth()) {
    // 2-Column layout using Rows
    for (i in gridItems.indices step 2) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        val item1 = gridItems[i]
        FeatureCard(
          item = item1,
          selectedLang = selectedLang,
          onClick = { onItemClick(item1.id) },
          modifier = Modifier.weight(1f)
        )

        if (i + 1 < gridItems.size) {
          val item2 = gridItems[i + 1]
          FeatureCard(
            item = item2,
            selectedLang = selectedLang,
            onClick = { onItemClick(item2.id) },
            modifier = Modifier.weight(1f)
          )
        } else {
          Spacer(modifier = Modifier.weight(1f))
        }
      }
      Spacer(modifier = Modifier.height(12.dp))
    }
  }
}

@Composable
fun FeatureCard(
  item: MainGridItem,
  selectedLang: String,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  val interactionSource = remember { MutableInteractionSource() }

  Card(
    shape = RoundedCornerShape(18.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
    border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
    modifier = modifier
      .height(112.dp)
      .clickable(
        interactionSource = interactionSource,
        indication = ripple(bounded = true, color = item.accentColor.copy(alpha = 0.2f)),
        onClick = onClick
      )
      .testTag(item.testTag)
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(12.dp),
      verticalArrangement = Arrangement.SpaceBetween
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        // Icon Circle
        Box(
          modifier = Modifier
            .size(38.dp)
            .clip(CircleShape)
            .background(item.accentColor.copy(alpha = 0.12f)),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = item.iconEmoji,
            style = TextStyle(
              fontSize = if (item.iconEmoji == "أ") 20.sp else 18.sp,
              fontWeight = FontWeight.Bold,
              color = item.accentColor,
              fontFamily = if (item.iconEmoji == "أ") FontFamily.Serif else FontFamily.Default
            )
          )
        }

        // Mini Badge
        Surface(
          shape = RoundedCornerShape(6.dp),
          color = item.accentColor.copy(alpha = 0.10f)
        ) {
          Text(
            text = item.badgeTextBn,
            style = TextStyle(
              fontSize = 9.sp,
              fontWeight = FontWeight.Bold,
              color = item.accentColor
            ),
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
          )
        }
      }

      Column {
        Text(
          text = if (selectedLang == "BN") item.titleBn else item.titleEn,
          style = TextStyle(
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1E293B)
          ),
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
          text = if (selectedLang == "BN") item.subtitleBn else item.subtitleEn,
          style = TextStyle(
            fontSize = 11.sp,
            color = Color(0xFF64748B)
          ),
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )
      }
    }
  }
}

/**
 * 3. INTERACTIVE CAROUSEL SLIDER (AD & CONTENT CORNER)
 */
sealed class CarouselSlide(
  val id: String,
  val tagEn: String,
  val tagBn: String,
  val titleBn: String,
  val titleEn: String,
  val descriptionBn: String,
  val descriptionEn: String,
  val ctaBn: String,
  val ctaEn: String,
  val bgGradient: List<Color>,
  val iconEmoji: String
) {
  object ShapeMaster : CarouselSlide(
    id = "slide_shape_master",
    tagEn = "FEATURE SPOTLIGHT",
    tagBn = "গেমিফাইড লার্নিং",
    titleBn = "শেপ মাস্টার অ্যাডভেঞ্চার",
    titleEn = "Shape Master Adventure",
    descriptionBn = "৩৫টি স্তরে হরফের বিচ্ছিন্ন ও সংযুক্ত রূপ শিখুন সম্পূর্ণ গেমিফাইড পদ্ধতিতে।",
    descriptionEn = "Master Arabic letter forms and positions across 35 levels.",
    ctaBn = "খেলুন এখনই ➔",
    ctaEn = "Play Now ➔",
    bgGradient = listOf(Color(0xFF0A5C36), Color(0xFF047857)),
    iconEmoji = "🎮"
  )

  object MakhrajVisualizer : CarouselSlide(
    id = "slide_makhraj",
    tagEn = "3D ARTICULATION",
    tagBn = "উচ্চারণ প্রশিক্ষণ",
    titleBn = "৩ডি মাখরাজ ভিজ্যুয়ালাইজার",
    titleEn = "3D Makhraj Visualizer",
    descriptionBn = "মুখের ১৭টি মাখরাজ ও কন্ঠনালির সঠিক অবস্থান দেখুন ৩ডি অ্যানিমেশনে।",
    descriptionEn = "Explore 17 Makhraj articulation points with interactive visuals.",
    ctaBn = "অনুশীলন করুন ➔",
    ctaEn = "Practice Now ➔",
    bgGradient = listOf(Color(0xFF0F172A), Color(0xFF1E293B)),
    iconEmoji = "🗣️"
  )

  object DailyDua : CarouselSlide(
    id = "slide_daily_dua",
    tagEn = "DAILY KNOWLEDGE",
    tagBn = "আজকের দোয়া",
    titleBn = "পিতা-মাতার জন্য সর্বোত্তম দোয়া",
    titleEn = "Dua for Loving Parents",
    descriptionBn = "رَّبِّ ارْحَمْهُمَا كَمَا رَبَّيَانِي صَغِيرًا — প্রতিদিন নিয়মিত পাঠের ফজিলত জানুন।",
    descriptionEn = "Supplication for parents from Surah Al-Isra with immense virtues.",
    ctaBn = "দোয়া পড়ুন ➔",
    ctaEn = "Read Dua ➔",
    bgGradient = listOf(Color(0xFF065F46), Color(0xFF0D9488)),
    iconEmoji = "🤲"
  )

  object VirtuousDeed : CarouselSlide(
    id = "slide_virtuous_deed",
    tagEn = "TODAY'S DEED",
    tagBn = "আজকের ফজিলতপূর্ণ আমল",
    titleBn = "ফরজ নামাজের পর আয়াতুল কুরসি",
    titleEn = "Ayatul Kursi After Prayer",
    descriptionBn = "নবীজি (সা.) বলেছেন: 'জান্নাতে প্রবেশের পথে একমাত্র মৃত্যু ছাড়া আর কোনো বাধা থাকে না।'",
    descriptionEn = "Tremendous reward of reciting Ayatul Kursi after each obligatory Salah.",
    ctaBn = "আমল দেখুন ➔",
    ctaEn = "View Deed ➔",
    bgGradient = listOf(Color(0xFF1E3A8A), Color(0xFF2563EB)),
    iconEmoji = "👑"
  )

  object ProPromotion : CarouselSlide(
    id = "slide_pro_promo",
    tagEn = "SPONSORED / PRO",
    tagBn = "হরুফিয়া প্রো লাইব্রেরি",
    titleBn = "বিজ্ঞাপনমুক্ত প্রিমিয়াম শিক্ষা",
    titleEn = "Hurufia Sharif Pro",
    descriptionBn = "অফলাইন অডিও প্যাক, বিজ্ঞাপনমুক্ত কুরআন কিতাব ও তাজবীদ সনদ পান।",
    descriptionEn = "Ad-free Quran learning, full offline audio packs and certificates.",
    ctaBn = "বিস্তারিত জানুন ➔",
    ctaEn = "Learn More ➔",
    bgGradient = listOf(Color(0xFF701A75), Color(0xFF9333EA)),
    iconEmoji = "✨"
  )
}

@Composable
fun InteractiveContentCarousel(
  selectedLang: String,
  onSlideClick: (CarouselSlide) -> Unit,
  modifier: Modifier = Modifier
) {
  val slides = remember {
    listOf(
      CarouselSlide.ShapeMaster,
      CarouselSlide.MakhrajVisualizer,
      CarouselSlide.DailyDua,
      CarouselSlide.VirtuousDeed,
      CarouselSlide.ProPromotion
    )
  }

  val pagerState = rememberPagerState(pageCount = { slides.size })

  // Auto-scroll every 4 seconds
  LaunchedEffect(pagerState) {
    while (true) {
      delay(4000L)
      val nextPage = (pagerState.currentPage + 1) % slides.size
      pagerState.animateScrollToPage(nextPage, animationSpec = tween(durationMillis = 600))
    }
  }

  Column(modifier = modifier.fillMaxWidth()) {
    HorizontalPager(
      state = pagerState,
      modifier = Modifier
        .fillMaxWidth()
        .height(160.dp)
        .testTag("home_content_carousel")
    ) { page ->
      val slide = slides[page]

      Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
          .fillMaxSize()
          .clickable { onSlideClick(slide) }
      ) {
        Box(
          modifier = Modifier
            .fillMaxSize()
            .background(Brush.horizontalGradient(slide.bgGradient))
            .padding(16.dp)
        ) {
          Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
          ) {
            // Header Row: Tag + Icon
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Surface(
                color = Color.White.copy(alpha = 0.22f),
                shape = RoundedCornerShape(6.dp)
              ) {
                Text(
                  text = if (selectedLang == "BN") slide.tagBn else slide.tagEn,
                  style = TextStyle(
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    letterSpacing = 0.8.sp
                  ),
                  modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                )
              }

              Text(text = slide.iconEmoji, fontSize = 22.sp)
            }

            // Title & Description
            Column {
              Text(
                text = if (selectedLang == "BN") slide.titleBn else slide.titleEn,
                style = TextStyle(
                  fontSize = 17.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color.White
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
              )
              Spacer(modifier = Modifier.height(3.dp))
              Text(
                text = if (selectedLang == "BN") slide.descriptionBn else slide.descriptionEn,
                style = TextStyle(
                  fontSize = 12.sp,
                  color = Color.White.copy(alpha = 0.9f),
                  lineHeight = 16.sp
                ),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
              )
            }

            // CTA Button / Chip
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.End
            ) {
              Surface(
                color = Color.White,
                shape = RoundedCornerShape(8.dp)
              ) {
                Text(
                  text = if (selectedLang == "BN") slide.ctaBn else slide.ctaEn,
                  style = TextStyle(
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = slide.bgGradient.first()
                  ),
                  modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                )
              }
            }
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(8.dp))

    // Dots Indicator
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.Center,
      verticalAlignment = Alignment.CenterVertically
    ) {
      repeat(slides.size) { index ->
        val isSelected = pagerState.currentPage == index
        Box(
          modifier = Modifier
            .padding(horizontal = 3.dp)
            .size(width = if (isSelected) 18.dp else 6.dp, height = 6.dp)
            .clip(CircleShape)
            .background(if (isSelected) RoyalEmerald else Color(0xFFCBD5E1))
        )
      }
    }
  }
}

/**
 * 4. NAVIGATION DRAWER CONTENT
 * Top Header: App Info/About Header with App Logo, Title ('হরুফিয়া শরিফ'), Version info & purpose.
 * Menu Items: Language Settings, Azan & Notification, Audio Qari Selection, Offline Download Manager,
 * Share App, Rate Us, Feedback/Report Issue, and Privacy Policy.
 */
@Composable
fun HurufiaNavigationDrawerContent(
  selectedLang: String,
  currentVoiceGender: VoiceGender = VoiceGender.MALE_QARI,
  isQuizHintsEnabled: Boolean = true,
  onQuizHintsChange: (Boolean) -> Unit = {},
  isDarkModeEnabled: Boolean = false,
  onDarkModeChange: (Boolean) -> Unit = {},
  isGpsLocated: Boolean = false,
  locationName: String = "",
  onItemClick: (DrawerDestination) -> Unit
) {
  val isDark = isDarkModeEnabled
  ModalDrawerSheet(
    modifier = Modifier.width(315.dp),
    drawerContainerColor = if (isDark) Color(0xFF0F172A) else Color.White
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
    ) {
      // 1. App Info / About Header
      Surface(
        color = if (isDark) Color(0xFF064E3B) else Color(0xFF0A5C36),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 22.dp)
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(52.dp)
                .clip(CircleShape)
                .background(
                  Brush.radialGradient(
                    listOf(Color(0xFF10B981), Color(0xFF064E3B))
                  )
                )
                .border(2.dp, Color(0xFFFDE047), CircleShape),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = "ح",
                style = TextStyle(
                  color = Color.White,
                  fontSize = 28.sp,
                  fontWeight = FontWeight.Bold,
                  fontFamily = FontFamily.Serif
                )
              )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column {
              Text(
                text = "হরুফিয়া শরিফ",
                style = TextStyle(
                  color = Color.White,
                  fontSize = 18.sp,
                  fontWeight = FontWeight.ExtraBold,
                  letterSpacing = 0.5.sp
                )
              )
              Text(
                text = "v2.4.0 • বিশুদ্ধ কুরআন ও নামাজ সহায়িকা",
                style = TextStyle(
                  color = Color(0xFF86EFAC),
                  fontSize = 11.sp,
                  fontWeight = FontWeight.SemiBold
                )
              )
            }
          }

          Spacer(modifier = Modifier.height(12.dp))

          Text(
            text = if (selectedLang == "BN") {
              "সহজ ও বিশুদ্ধ কুরআন শিক্ষা, মাখরাজ উচ্চারণ, নামাজের সঠিক সময়সূচি ও অফলাইন ইসলামিক লাইব্রেরি।"
            } else if (selectedLang == "AR") {
              "تطبيق متكامل لتعليم تلاوة القرآن الكريم ومخارج الحروف ومواقيت الصلاة دون إنترنت."
            } else {
              "Authentic Quranic Arabic learning, Makhraj articulation, prayer times & offline Islamic guide."
            },
            style = TextStyle(
              color = Color.White.copy(alpha = 0.92f),
              fontSize = 11.5.sp,
              lineHeight = 16.sp,
              fontWeight = FontWeight.Normal
            )
          )
        }
      }

      Spacer(modifier = Modifier.height(8.dp))

      // Drawer Destination Items
      DrawerSectionHeader(
        text = if (selectedLang == "BN") "সেটিংস ও পছন্দ" else if (selectedLang == "AR") "الإعدادات والتفضيلات" else "Settings & Preferences"
      )

      // 1. Language Settings
      DrawerRowItem(
        icon = Icons.Default.Language,
        label = "Language / ভাষা / اللغة",
        badge = when (selectedLang) {
          "BN" -> "🇧🇩 বাংলা"
          "AR" -> "🇸🇦 العربية"
          else -> "🇬🇧 English"
        },
        isDark = isDark,
        onClick = { onItemClick(DrawerDestination.LANGUAGE) }
      )

      // 2. Location Settings (Automatic GPS & Asia Manual Search)
      DrawerRowItem(
        icon = Icons.Default.LocationOn,
        label = if (selectedLang == "BN") "লোকেশন / Location" else if (selectedLang == "AR") "الموقع / Location" else "Location / লোকেশন",
        badge = if (isGpsLocated) "📡 GPS" else "📍 Manual",
        isDark = isDark,
        onClick = { onItemClick(DrawerDestination.LOCATION) }
      )

      // 2. QUIZ & GAMEPLAY HINTS TOGGLE SWITCH (Visible directly inside drawer)
      DrawerToggleRowItem(
        icon = Icons.Default.AutoAwesome,
        title = if (selectedLang == "BN") "কুইজ ও গেমপ্লে ইঙ্গিত" else if (selectedLang == "AR") "تلميحات الاختبار والألعاب" else "Quiz & Gameplay Hints",
        subtitle = if (selectedLang == "BN") "শিক্ষানবিস ইঙ্গিত ও সহায়ক টিপস" else if (selectedLang == "AR") "إرشادات وتلميحات توضيحية" else "Beginner hints & gameplay tips",
        checked = isQuizHintsEnabled,
        onCheckedChange = onQuizHintsChange,
        testTag = "beginner_hints_enabled",
        isDark = isDark
      )

      // 3. THEME MODE TOGGLE SWITCH (Visible directly inside drawer)
      DrawerToggleRowItem(
        icon = if (isDarkModeEnabled) Icons.Default.NightsStay else Icons.Default.WbTwilight,
        title = if (selectedLang == "BN") "ডার্ক মোড / লাইট মোড" else if (selectedLang == "AR") "الوضع الداكن / الفاتح" else "Dark Mode / Light Mode",
        subtitle = if (isDarkModeEnabled) {
          if (selectedLang == "BN") "ডার্ক থিম সক্রিয় 🌙" else if (selectedLang == "AR") "الوضع الليلي مفعّل 🌙" else "Dark theme active 🌙"
        } else {
          if (selectedLang == "BN") "লাইট থিম সক্রিয় ☀️" else if (selectedLang == "AR") "الوضع النهاري مفعّل ☀️" else "Light theme active ☀️"
        },
        checked = isDarkModeEnabled,
        onCheckedChange = onDarkModeChange,
        testTag = "drawer_dark_mode_toggle",
        isDark = isDark
      )

      // 4. Azan & Notification Settings
      DrawerRowItem(
        icon = Icons.Default.NotificationsActive,
        label = if (selectedLang == "BN") "আজান ও নোটিফিকেশন" else if (selectedLang == "AR") "إعدادات الأذان والتنبيهات" else "Azan & Notification Settings",
        isDark = isDark,
        onClick = { onItemClick(DrawerDestination.NOTIFICATION_AZAN) }
      )

      // 5. Audio Qari Selection (Male/Female voice)
      DrawerRowItem(
        icon = Icons.Default.RecordVoiceOver,
        label = if (selectedLang == "BN") "ক্বারী ও অডিও কণ্ঠ নির্বাচন" else if (selectedLang == "AR") "اختيار القارئ والصوت (ذكر/أنثى)" else "Audio Qari Selection (Voice)",
        badge = if (currentVoiceGender == VoiceGender.MALE_QARI) "🎙️ Qari" else "🧕 Qaria",
        isDark = isDark,
        onClick = { onItemClick(DrawerDestination.AUDIO_QARI) }
      )

      // 6. Offline Download Manager
      DrawerRowItem(
        icon = Icons.Default.CloudDownload,
        label = if (selectedLang == "BN") "অফলাইন ডাউনলোড ম্যানেজার" else if (selectedLang == "AR") "مدير التحميل دون اتصال" else "Offline Download Manager",
        badge = "১০০% প্রস্তুত",
        isDark = isDark,
        onClick = { onItemClick(DrawerDestination.OFFLINE_MANAGER) }
      )

      HorizontalDivider(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
        color = if (isDark) Color(0xFF1E293B) else Color(0xFFF1F5F9)
      )

      DrawerSectionHeader(
        text = if (selectedLang == "BN") "তথ্য ও সাপোর্ট" else if (selectedLang == "AR") "معلومات وتواصل" else "App Info & Support"
      )

      // 7. Share App
      DrawerRowItem(
        icon = Icons.Default.Share,
        label = if (selectedLang == "BN") "অ্যাপটি শেয়ার করুন" else if (selectedLang == "AR") "مشاركة التطبيق" else "Share App",
        isDark = isDark,
        onClick = { onItemClick(DrawerDestination.SHARE_APP) }
      )

      // 8. Rate Us
      DrawerRowItem(
        icon = Icons.Default.Star,
        label = if (selectedLang == "BN") "রেটিং ও রিভিউ দিন" else if (selectedLang == "AR") "تقييم التطبيق" else "Rate Us",
        isDark = isDark,
        onClick = { onItemClick(DrawerDestination.RATE_US) }
      )

      // 9. Feedback / Report Issue
      DrawerRowItem(
        icon = Icons.Default.Feedback,
        label = if (selectedLang == "BN") "মতামত ও সমস্যা জানান" else if (selectedLang == "AR") "إرسال ملاحظة أو بلاغ" else "Feedback / Report Issue",
        isDark = isDark,
        onClick = { onItemClick(DrawerDestination.FEEDBACK) }
      )

      // 10. Privacy Policy
      DrawerRowItem(
        icon = Icons.Default.Security,
        label = if (selectedLang == "BN") "গোপনীয়তা নীতি (Privacy)" else if (selectedLang == "AR") "سياسة الخصوصية" else "Privacy Policy",
        isDark = isDark,
        onClick = { onItemClick(DrawerDestination.PRIVACY_POLICY) }
      )

      Spacer(modifier = Modifier.height(20.dp))

      // Footer version
      Text(
        text = "হরুফিয়া শরিফ v2.4.0 • ১০০% নিরাপদ ও অফলাইন",
        style = TextStyle(
          fontSize = 11.sp,
          color = if (isDark) Color(0xFF64748B) else Color(0xFF94A3B8),
          textAlign = TextAlign.Center
        ),
        modifier = Modifier
          .fillMaxWidth()
          .padding(16.dp)
      )
    }
  }
}

enum class DrawerDestination {
  LANGUAGE,
  LOCATION,
  NOTIFICATION_AZAN,
  AUDIO_QARI,
  OFFLINE_MANAGER,
  SHARE_APP,
  RATE_US,
  FEEDBACK,
  PRIVACY_POLICY
}

@Composable
fun DrawerSectionHeader(text: String) {
  Text(
    text = text,
    style = TextStyle(
      fontSize = 11.sp,
      fontWeight = FontWeight.Bold,
      color = Color(0xFF94A3B8),
      letterSpacing = 0.6.sp
    ),
    modifier = Modifier.padding(horizontal = 18.dp, vertical = 6.dp)
  )
}

@Composable
fun DrawerRowItem(
  icon: ImageVector,
  label: String,
  badge: String? = null,
  isDark: Boolean = false,
  onClick: () -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clickable(onClick = onClick)
      .padding(horizontal = 18.dp, vertical = 11.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    Row(verticalAlignment = Alignment.CenterVertically) {
      Box(
        modifier = Modifier
          .size(36.dp)
          .clip(RoundedCornerShape(10.dp))
          .background(if (isDark) Color(0xFF1E293B) else Color(0xFFF1F5F9)),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = icon,
          contentDescription = label,
          tint = if (isDark) Color(0xFF34D399) else Color(0xFF0A5C36),
          modifier = Modifier.size(19.dp)
        )
      }
      Spacer(modifier = Modifier.width(13.dp))
      Text(
        text = label,
        style = TextStyle(
          fontSize = 13.5.sp,
          fontWeight = FontWeight.Medium,
          color = if (isDark) Color(0xFFF1F5F9) else Color(0xFF334155)
        )
      )
    }

    if (badge != null) {
      Surface(
        shape = RoundedCornerShape(6.dp),
        color = if (isDark) Color(0xFF064E3B) else Color(0xFFECFDF5)
      ) {
        Text(
          text = badge,
          style = TextStyle(
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = if (isDark) Color(0xFF6EE7B7) else Color(0xFF059669)
          ),
          modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
        )
      }
    }
  }
}

@Composable
fun DrawerToggleRowItem(
  icon: ImageVector,
  title: String,
  subtitle: String? = null,
  checked: Boolean,
  onCheckedChange: (Boolean) -> Unit,
  testTag: String,
  isDark: Boolean = false
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clickable { onCheckedChange(!checked) }
      .padding(horizontal = 18.dp, vertical = 9.dp)
      .testTag("${testTag}_row"),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier
        .weight(1f)
        .padding(end = 10.dp)
    ) {
      Box(
        modifier = Modifier
          .size(36.dp)
          .clip(RoundedCornerShape(10.dp))
          .background(
            if (checked) RoyalEmerald.copy(alpha = if (isDark) 0.25f else 0.12f)
            else if (isDark) Color(0xFF1E293B) else Color(0xFFF1F5F9)
          ),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = icon,
          contentDescription = title,
          tint = if (checked) RoyalEmerald else if (isDark) Color(0xFF94A3B8) else Color(0xFF64748B),
          modifier = Modifier.size(19.dp)
        )
      }
      Spacer(modifier = Modifier.width(13.dp))
      Column {
        Text(
          text = title,
          style = TextStyle(
            fontSize = 13.5.sp,
            fontWeight = FontWeight.SemiBold,
            color = if (isDark) Color(0xFFF1F5F9) else Color(0xFF1E293B)
          )
        )
        if (subtitle != null) {
          Text(
            text = subtitle,
            style = TextStyle(
              fontSize = 11.sp,
              color = if (isDark) Color(0xFF94A3B8) else Color(0xFF64748B),
              lineHeight = 14.sp
            )
          )
        }
      }
    }

    Switch(
      checked = checked,
      onCheckedChange = onCheckedChange,
      colors = SwitchDefaults.colors(
        checkedThumbColor = Color.White,
        checkedTrackColor = RoyalEmerald,
        uncheckedThumbColor = Color.White,
        uncheckedTrackColor = if (isDark) Color(0xFF475569) else Color(0xFFCBD5E1)
      ),
      modifier = Modifier.testTag(testTag)
    )
  }
}

/**
 * 5. DRAWER DIALOGS (Progress, Azan Settings, Offline Manager, Privacy)
 */
@Composable
fun LearningProgressDialog(
  progressRepo: OfflineProgressRepository,
  selectedLang: String,
  onDismiss: () -> Unit
) {
  Dialog(onDismissRequest = onDismiss) {
    Surface(
      shape = RoundedCornerShape(20.dp),
      color = Color.White,
      shadowElevation = 12.dp,
      modifier = Modifier
        .fillMaxWidth()
        .widthIn(max = 480.dp)
    ) {
      Column(
        modifier = Modifier
          .padding(20.dp)
          .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "🏆", fontSize = 24.sp)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = if (selectedLang == "BN") "আমার শিখন অগ্রগতি" else "Learning Progress",
              style = TextStyle(fontSize = 17.sp, fontWeight = FontWeight.Bold, color = RoyalEmerald)
            )
          }
          IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
            Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = Color(0xFF64748B))
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Big Level Card
        Surface(
          shape = RoundedCornerShape(14.dp),
          color = Color(0xFFF0FDF4),
          border = BorderStroke(1.dp, Color(0xFFBBF7D0)),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Text(
              text = "বর্তমান স্তর: লেভেল ${progressRepo.userLevel}",
              style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold, color = RoyalEmerald)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = "কুরআন ও আরবি হরফ শিক্ষায় সক্রিয় শিক্ষার্থী",
              style = TextStyle(fontSize = 12.sp, color = Color(0xFF15803D))
            )
            Spacer(modifier = Modifier.height(10.dp))
            LinearProgressIndicator(
              progress = { ((progressRepo.unlockedLevels.size) / 35f).coerceIn(0.1f, 1.0f) },
              modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
              color = RoyalEmerald,
              trackColor = Color(0xFFDCFCE7)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
              text = "৩৫টি রোড়ম্যাপ স্তরের মধ্যে ${progressRepo.unlockedLevels.size}টি সম্পন্ন",
              style = TextStyle(fontSize = 11.sp, color = Color(0xFF475569))
            )
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Grid Stats
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Surface(
            color = Color(0xFFFFFBEB),
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, Color(0xFFFDE68A)),
            modifier = Modifier.weight(1f)
          ) {
            Column(
              modifier = Modifier.padding(12.dp),
              horizontalAlignment = Alignment.CenterHorizontally
            ) {
              Text(text = "⭐ ${progressRepo.userCoins}", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFFB45309))
              Text(text = "অর্জিত স্টার", fontSize = 11.sp, color = Color(0xFF78350F))
            }
          }

          Surface(
            color = Color(0xFFEFF6FF),
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, Color(0xFFBFDBFE)),
            modifier = Modifier.weight(1f)
          ) {
            Column(
              modifier = Modifier.padding(12.dp),
              horizontalAlignment = Alignment.CenterHorizontally
            ) {
              Text(text = "🔥 ${progressRepo.userStreak} দিন", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1D4ED8))
              Text(text = "ধারাবাহিকতা", fontSize = 11.sp, color = Color(0xFF1E40AF))
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
          Text("চালিয়ে যান", fontWeight = FontWeight.Bold)
        }
      }
    }
  }
}

@Composable
fun AzanNotificationSettingsDialog(
  offlineRepo: IslamicOfflineRepository,
  selectedLang: String,
  onDismiss: () -> Unit
) {
  var masterAzan by remember { mutableStateOf(!offlineRepo.isAzanMuted) }
  var fajrAlert by remember { mutableStateOf(true) }
  var dhuhrAlert by remember { mutableStateOf(true) }
  var asrAlert by remember { mutableStateOf(true) }
  var maghribAlert by remember { mutableStateOf(true) }
  var ishaAlert by remember { mutableStateOf(true) }

  Dialog(onDismissRequest = onDismiss) {
    Surface(
      shape = RoundedCornerShape(20.dp),
      color = Color.White,
      shadowElevation = 12.dp,
      modifier = Modifier
        .fillMaxWidth()
        .widthIn(max = 480.dp)
    ) {
      Column(
        modifier = Modifier
          .padding(20.dp)
          .verticalScroll(rememberScrollState())
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "🔔", fontSize = 24.sp)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = if (selectedLang == "BN") "আজান ও নোটিফিকেশন" else "Azan & Notifications",
              style = TextStyle(fontSize = 17.sp, fontWeight = FontWeight.Bold, color = RoyalEmerald)
            )
          }
          IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
            Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = Color(0xFF64748B))
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Master Switch
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFF1F5F9))
            .padding(12.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(
              text = "আজান অ্যালার্ট সক্রিয়",
              style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
            )
            Text(
              text = "ওয়াক্ত অনুযায়ী স্বয়ংক্রিয় ধ্বনি বাজবে",
              style = TextStyle(fontSize = 11.sp, color = Color(0xFF64748B))
            )
          }
          Switch(
            checked = masterAzan,
            onCheckedChange = {
              masterAzan = it
              offlineRepo.isAzanMuted = !it
            },
            colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = RoyalEmerald)
          )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Per-prayer items
        val prayers = listOf(
          "ফজর (Fajr)" to fajrAlert,
          "যোহর (Dhuhr)" to dhuhrAlert,
          "আসর (Asr)" to asrAlert,
          "মাগরিব (Maghrib)" to maghribAlert,
          "ইশা (Isha)" to ishaAlert
        )

        prayers.forEachIndexed { index, (name, isChecked) ->
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(text = name, style = TextStyle(fontSize = 13.sp, color = Color(0xFF334155), fontWeight = FontWeight.Medium))
            Switch(
              checked = masterAzan && isChecked,
              enabled = masterAzan,
              onCheckedChange = {
                when (index) {
                  0 -> fajrAlert = it
                  1 -> dhuhrAlert = it
                  2 -> asrAlert = it
                  3 -> maghribAlert = it
                  4 -> ishaAlert = it
                }
              },
              colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = RoyalEmerald)
            )
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
          onClick = onDismiss,
          colors = ButtonDefaults.buttonColors(containerColor = RoyalEmerald),
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Text("সংরক্ষণ করুন", fontWeight = FontWeight.Bold)
        }
      }
    }
  }
}

@Composable
fun OfflineManagerDialog(
  selectedLang: String,
  onDismiss: () -> Unit
) {
  Dialog(onDismissRequest = onDismiss) {
    Surface(
      shape = RoundedCornerShape(20.dp),
      color = Color.White,
      shadowElevation = 12.dp,
      modifier = Modifier
        .fillMaxWidth()
        .widthIn(max = 480.dp)
    ) {
      Column(
        modifier = Modifier
          .padding(20.dp)
          .verticalScroll(rememberScrollState())
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "💾", fontSize = 24.sp)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = if (selectedLang == "BN") "অফলাইন ডাউনলোড ম্যানেজার" else "Offline Manager",
              style = TextStyle(fontSize = 17.sp, fontWeight = FontWeight.Bold, color = RoyalEmerald)
            )
          }
          IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
            Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = Color(0xFF64748B))
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Surface(
          shape = RoundedCornerShape(12.dp),
          color = Color(0xFFECFDF5),
          border = BorderStroke(1.dp, Color(0xFFA7F3D0)),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(imageVector = Icons.Default.DownloadDone, contentDescription = "Ready", tint = RoyalEmerald)
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text(
                text = "১০০% অফলাইন প্রস্তুত (Offline Ready)",
                style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Bold, color = RoyalEmerald)
              )
              Text(
                text = "ইন্টারনেট ছাড়াও অ্যাপের সকল ফিচার নির্বিঘ্নে চলবে।",
                style = TextStyle(fontSize = 11.sp, color = Color(0xFF047857))
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        val items = listOf(
          "২৯টি হরফের অডিও প্যাক (ক্বারী ও ক্বারিয়া)" to "সংরক্ষিত (৬.২ MB)",
          "নামাজের সময়সূচি ও জ্যোতির্বিজ্ঞান ইঞ্জিন" to "সংরক্ষিত (১.৮ MB)",
          "হিজরি ক্যালেন্ডার ও ইসলামিক ঘটনাবলি" to "সংরক্ষিত (১.৫ MB)",
          "বিষয়ভিত্তিক সহিহ হাদিস ও নামাজ গাইড" to "সংরক্ষিত (২.৯ MB)"
        )

        items.forEach { (title, status) ->
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = title,
              style = TextStyle(fontSize = 12.sp, color = Color(0xFF334155), fontWeight = FontWeight.Medium),
              modifier = Modifier.weight(1f)
            )
            Surface(
              shape = RoundedCornerShape(6.dp),
              color = Color(0xFFF1F5F9)
            ) {
              Text(
                text = status,
                style = TextStyle(fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F766E)),
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
              )
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
          Text("ঠিক আছে", fontWeight = FontWeight.Bold)
        }
      }
    }
  }
}

@Composable
fun PrivacyPolicyDialog(
  selectedLang: String,
  onDismiss: () -> Unit
) {
  Dialog(onDismissRequest = onDismiss) {
    Surface(
      shape = RoundedCornerShape(20.dp),
      color = Color.White,
      shadowElevation = 12.dp,
      modifier = Modifier
        .fillMaxWidth()
        .widthIn(max = 480.dp)
    ) {
      Column(
        modifier = Modifier
          .padding(20.dp)
          .verticalScroll(rememberScrollState())
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "🛡️", fontSize = 24.sp)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = if (selectedLang == "BN") "গোপনীয়তা নীতি" else "Privacy Policy",
              style = TextStyle(fontSize = 17.sp, fontWeight = FontWeight.Bold, color = RoyalEmerald)
            )
          }
          IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
            Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = Color(0xFF64748B))
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
          text = """
          হরুফিয়া শরিফ (HURUFIA SHARIF) ব্যবহারকারীর ব্যক্তিগত তথ্যের সর্বোচ্চ সুরক্ষায় অঙ্গীকারবদ্ধ।
          
          ১. অফলাইন অগ্রাধিকার:
          আপনার নামাজের সময়, শিক্ষাগত লেভেল ও অগ্রগতি সম্পূর্ণ স্থানীয়ভাবে ডিভাইসে সংরক্ষিত থাকে। কোনো তথ্য বাইরের সার্ভারে বিক্রি বা আদান-প্রদান করা হয় না।
          
          ২. জিপিএস লোকেশন:
          নামাজের সঠিক ওয়াক্ত নির্ধারণের জন্য ডিভাইসের লোকেশন কেবল গণনায় ব্যবহৃত হয়। কোনো ট্র্যাকিং পরিচালিত হয় না।
          
          ৩. ইসলামিক বিশুদ্ধতা:
          হাদিস, কায়দা ও নামাজ নির্দেশিকা নির্ভরযোগ্য ও বিশুদ্ধ ইসলামিক সূত্র থেকে প্রণীত।
          """.trimIndent(),
          style = TextStyle(fontSize = 13.sp, color = Color(0xFF334155), lineHeight = 19.sp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
          onClick = onDismiss,
          colors = ButtonDefaults.buttonColors(containerColor = RoyalEmerald),
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Text("সম্মত আছি", fontWeight = FontWeight.Bold)
        }
      }
    }
  }
}

@Composable
fun LanguageSelectionDialog(
  selectedLang: String,
  onLanguageSelected: (String) -> Unit,
  onDismiss: () -> Unit
) {
  val languages = listOf(
    Triple("BN", "বাংলা (বাংলা ফন্ট ও উচ্চারণ)", "🇧🇩"),
    Triple("EN", "English (Global standard)", "🇬🇧"),
    Triple("AR", "العربية (النصوص القرآنية)", "🇸🇦")
  )

  Dialog(onDismissRequest = onDismiss) {
    Surface(
      shape = RoundedCornerShape(20.dp),
      color = Color.White,
      shadowElevation = 12.dp,
      modifier = Modifier
        .fillMaxWidth()
        .widthIn(max = 440.dp)
        .testTag("language_selection_dialog")
    ) {
      Column(
        modifier = Modifier
          .padding(20.dp)
          .fillMaxWidth()
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.Language,
              contentDescription = null,
              tint = RoyalEmerald,
              modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "Language / ভাষা / اللغة",
              style = TextStyle(fontSize = 17.sp, fontWeight = FontWeight.Bold, color = RoyalEmerald)
            )
          }
          IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
            Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = Color(0xFF64748B))
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        languages.forEach { (code, name, flag) ->
          val isSelected = selectedLang == code
          Surface(
            shape = RoundedCornerShape(14.dp),
            color = if (isSelected) Color(0xFFECFDF5) else Color(0xFFF8FAFC),
            border = BorderStroke(1.5.dp, if (isSelected) RoyalEmerald else Color(0xFFE2E8F0)),
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 5.dp)
              .clip(RoundedCornerShape(14.dp))
              .clickable {
                onLanguageSelected(code)
                onDismiss()
              }
              .testTag("lang_option_$code")
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = flag, fontSize = 24.sp)
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                  Text(
                    text = name,
                    style = TextStyle(
                      fontSize = 14.5.sp,
                      fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                      color = if (isSelected) RoyalEmerald else Color(0xFF1E293B)
                    )
                  )
                  Text(
                    text = "Code: $code",
                    style = TextStyle(fontSize = 11.sp, color = Color(0xFF64748B))
                  )
                }
              }

              if (isSelected) {
                Icon(
                  imageVector = Icons.Default.Check,
                  contentDescription = "Selected",
                  tint = RoyalEmerald,
                  modifier = Modifier.size(20.dp)
                )
              }
            }
          }
        }
      }
    }
  }
}

@Composable
fun AudioQariSelectionDialog(
  soundManager: SoundManager,
  selectedLang: String,
  onDismiss: () -> Unit
) {
  var selectedVoice by remember { mutableStateOf(soundManager.currentVoiceGender) }
  LaunchedEffect(soundManager.currentVoiceGender) {
    selectedVoice = soundManager.currentVoiceGender
  }

  Dialog(onDismissRequest = onDismiss) {
    Surface(
      shape = RoundedCornerShape(20.dp),
      color = Color.White,
      shadowElevation = 12.dp,
      modifier = Modifier
        .fillMaxWidth()
        .widthIn(max = 450.dp)
        .testTag("audio_qari_dialog")
    ) {
      Column(
        modifier = Modifier
          .padding(20.dp)
          .fillMaxWidth()
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.RecordVoiceOver,
              contentDescription = null,
              tint = RoyalEmerald,
              modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = if (selectedLang == "BN") "ক্বারী ও কণ্ঠ নির্বাচন" else if (selectedLang == "AR") "اختيار القارئ والصوت" else "Audio Qari Selection",
              style = TextStyle(fontSize = 17.sp, fontWeight = FontWeight.Bold, color = RoyalEmerald)
            )
          }
          IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
            Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = Color(0xFF64748B))
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Male Qari Option
        val isMale = selectedVoice == VoiceGender.MALE_QARI
        Surface(
          shape = RoundedCornerShape(14.dp),
          color = if (isMale) Color(0xFFECFDF5) else Color(0xFFF8FAFC),
          border = BorderStroke(1.5.dp, if (isMale) RoyalEmerald else Color(0xFFE2E8F0)),
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
            .clip(RoundedCornerShape(14.dp))
            .clickable {
              selectedVoice = VoiceGender.MALE_QARI
              soundManager.setVoiceGender(VoiceGender.MALE_QARI)
              soundManager.previewVoiceSample(VoiceGender.MALE_QARI)
            }
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(text = "👳‍♂️", fontSize = 24.sp)
              Spacer(modifier = Modifier.width(12.dp))
              Column {
                Text(
                  text = if (selectedLang == "BN") "পুরুষ কণ্ঠ (Qari - Male Voice)" else "Male Voice (Qari)",
                  style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold, color = if (isMale) RoyalEmerald else Color(0xFF1E293B))
                )
                Text(
                  text = if (selectedLang == "BN") "গম্ভীর ও সুললিত ক্বারিয়ানা তিলাওয়াত, গভীর অনুরণন ও নিখুঁত তাজবীদ" else "Deep, resonant Qari recitation (Clean Baritone, 0.88x Tajweed pacing)",
                  style = TextStyle(fontSize = 11.sp, color = Color(0xFF64748B))
                )
                Text(
                  text = "Native TTS: ar-EG-ShakirNeural / Clean Baritone Qari",
                  style = TextStyle(fontSize = 9.5.sp, color = Color(0xFF0D9488), fontWeight = FontWeight.SemiBold)
                )
              }
            }
            if (isMale) {
              Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = RoyalEmerald)
            }
          }
        }

        // Female Qaria Option
        val isFemale = selectedVoice == VoiceGender.FEMALE_QARIA
        Surface(
          shape = RoundedCornerShape(14.dp),
          color = if (isFemale) Color(0xFFECFDF5) else Color(0xFFF8FAFC),
          border = BorderStroke(1.5.dp, if (isFemale) RoyalEmerald else Color(0xFFE2E8F0)),
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
            .clip(RoundedCornerShape(14.dp))
            .clickable {
              selectedVoice = VoiceGender.FEMALE_QARIA
              soundManager.setVoiceGender(VoiceGender.FEMALE_QARIA)
              soundManager.previewVoiceSample(VoiceGender.FEMALE_QARIA)
            }
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(text = "🧕", fontSize = 24.sp)
              Spacer(modifier = Modifier.width(12.dp))
              Column {
                Text(
                  text = if (selectedLang == "BN") "মহিলা কণ্ঠ (Qaria - Female Voice)" else "Female Voice (Qaria)",
                  style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold, color = if (isFemale) RoyalEmerald else Color(0xFF1E293B))
                )
                Text(
                  text = if (selectedLang == "BN") "কোমল, সুমধুর, স্ফটিক-স্বচ্ছ ক্বারিয়া উচ্চারণ (শিশু ও শিক্ষার্থীদের উপযোগী)" else "Soft, sweet, clear recitation style suitable for kids and smooth learning",
                  style = TextStyle(fontSize = 11.sp, color = Color(0xFF64748B))
                )
                Text(
                  text = "Native TTS: ar-SA-ZariyahNeural / Local Audio",
                  style = TextStyle(fontSize = 9.5.sp, color = Color(0xFF0D9488), fontWeight = FontWeight.SemiBold)
                )
              }
            }
            if (isFemale) {
              Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = RoyalEmerald)
            }
          }
        }

        Spacer(modifier = Modifier.height(6.dp))

        Surface(
          shape = RoundedCornerShape(8.dp),
          color = Color(0xFFF0FDF4),
          border = BorderStroke(0.5.dp, Color(0xFFBBF7D0)),
          modifier = Modifier.fillMaxWidth()
        ) {
          Text(
            text = if (selectedLang == "BN")
              "✓ কোনো এক্সটার্নাল এপিআই কী ছাড়া অফলাইন ও বিল্ট-ইন নেটিভ আরবি অডিও ইঞ্জিনে কার্যকর।"
            else
              "✓ Operates 100% offline via native Arabic neural voice synthesis & local audio (Zero external API keys required).",
            style = TextStyle(fontSize = 10.sp, color = Color(0xFF15803D), fontWeight = FontWeight.Medium),
            modifier = Modifier.padding(8.dp)
          )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
          onClick = {
            soundManager.setVoiceGender(selectedVoice)
            soundManager.previewVoiceSample(selectedVoice)
            onDismiss()
          },
          colors = ButtonDefaults.buttonColors(containerColor = RoyalEmerald),
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("save_voice_button")
        ) {
          Text(
            text = if (selectedLang == "BN") "সংরক্ষণ করুন" else "Save & Apply",
            fontWeight = FontWeight.Bold
          )
        }
      }
    }
  }
}

@Composable
fun FeedbackReportDialog(
  selectedLang: String,
  onDismiss: () -> Unit
) {
  var feedbackText by remember { mutableStateOf("") }
  var isSubmitted by remember { mutableStateOf(false) }

  Dialog(onDismissRequest = onDismiss) {
    Surface(
      shape = RoundedCornerShape(20.dp),
      color = Color.White,
      shadowElevation = 12.dp,
      modifier = Modifier
        .fillMaxWidth()
        .widthIn(max = 440.dp)
        .testTag("feedback_report_dialog")
    ) {
      Column(
        modifier = Modifier
          .padding(20.dp)
          .fillMaxWidth()
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.Feedback,
              contentDescription = null,
              tint = RoyalEmerald,
              modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = if (selectedLang == "BN") "মতামত ও সমস্যা জানান" else "Feedback & Support",
              style = TextStyle(fontSize = 17.sp, fontWeight = FontWeight.Bold, color = RoyalEmerald)
            )
          }
          IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
            Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = Color(0xFF64748B))
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        if (isSubmitted) {
          Surface(
            shape = RoundedCornerShape(12.dp),
            color = Color(0xFFECFDF5),
            border = BorderStroke(1.dp, Color(0xFFA7F3D0)),
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(
              modifier = Modifier.padding(16.dp),
              horizontalAlignment = Alignment.CenterHorizontally
            ) {
              Text(text = "✅", fontSize = 28.sp)
              Spacer(modifier = Modifier.height(6.dp))
              Text(
                text = if (selectedLang == "BN") "ধন্যবাদ! আপনার মূল্যবান মতামত সফলভাবে গ্রহণ করা হয়েছে।" else "Thank you! Your feedback has been received.",
                style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Bold, color = RoyalEmerald, textAlign = TextAlign.Center)
              )
            }
          }
          Spacer(modifier = Modifier.height(16.dp))
          Button(
            onClick = onDismiss,
            colors = ButtonDefaults.buttonColors(containerColor = RoyalEmerald),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            Text("ঠিক আছে", fontWeight = FontWeight.Bold)
          }
        } else {
          Text(
            text = if (selectedLang == "BN") "অ্যাপ নিয়ে কোনো পরামর্শ বা সমস্যা থাকলে নির্দ্বিধায় লিখুন:" else "Please write any suggestions or report any bugs:",
            style = TextStyle(fontSize = 12.5.sp, color = Color(0xFF475569))
          )

          Spacer(modifier = Modifier.height(8.dp))

          androidx.compose.material3.OutlinedTextField(
            value = feedbackText,
            onValueChange = { feedbackText = it },
            placeholder = { Text("আপনার মতামত লিখুন...", fontSize = 13.sp) },
            minLines = 3,
            maxLines = 5,
            modifier = Modifier.fillMaxWidth()
          )

          Spacer(modifier = Modifier.height(16.dp))

          Button(
            onClick = {
              if (feedbackText.isNotBlank()) {
                isSubmitted = true
              }
            },
            enabled = feedbackText.isNotBlank(),
            colors = ButtonDefaults.buttonColors(containerColor = RoyalEmerald),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            Text(if (selectedLang == "BN") "জমা দিন" else "Submit", fontWeight = FontWeight.Bold)
          }
        }
      }
    }
  }
}
