package com.example

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.RoyalEmerald

/**
 * Dedicated User Profile Screen for Bottom Navigation 'Profile' tab.
 * Strictly focused on User Profile stats:
 * 1. User Name & Avatar
 * 2. Level / XP Progression & Badges
 * 3. Learning Streaks & Consistency
 * 4. Progress Analytics & Learning Milestones
 *
 * All global settings (Language, Voice/Qari, Hints, Dark Mode) are removed
 * and consolidated strictly into the Navigation Drawer's App Settings dialog.
 */
@Composable
fun SettingsTabScreen(
  selectedLang: String,
  onLanguageChange: (String) -> Unit = {},
  userEmail: String,
  userLevel: Int,
  userCoins: Int,
  userStreak: Int,
  soundManager: SoundManager,
  onOpenMakhrajVisualizer: () -> Unit = {},
  onSignOut: () -> Unit
) {
  val layoutDirection = if (selectedLang == "AR") LayoutDirection.Rtl else LayoutDirection.Ltr

  CompositionLocalProvider(LocalLayoutDirection provides layoutDirection) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .background(Color(0xFFF8FAFC))
        .verticalScroll(rememberScrollState())
        .testTag("profile_screen"),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .widthIn(max = 680.dp)
          .padding(horizontal = 16.dp, vertical = 14.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        // 1. User Profile Header Card
        Card(
          shape = RoundedCornerShape(22.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
          border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("profile_header_card")
        ) {
          Column(modifier = Modifier.padding(18.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
              ) {
                // User Avatar with Emerald Gradient & Arabic Symbol
                Box(
                  modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(
                      Brush.radialGradient(
                        listOf(RoyalEmerald, Color(0xFF042F1A))
                      )
                    )
                    .border(2.dp, Color(0xFF34D399), CircleShape),
                  contentAlignment = Alignment.Center
                ) {
                  Text(
                    text = "ح",
                    style = TextStyle(
                      fontSize = 28.sp,
                      fontWeight = FontWeight.Bold,
                      color = Color.White
                    )
                  )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column {
                  Text(
                    text = if (userEmail.contains("@")) userEmail.substringBefore("@") else userEmail,
                    style = TextStyle(
                      fontSize = 17.sp,
                      fontWeight = FontWeight.Bold,
                      color = Color(0xFF0F172A)
                    )
                  )
                  Text(
                    text = userEmail,
                    style = TextStyle(
                      fontSize = 11.5.sp,
                      color = Color(0xFF64748B)
                    )
                  )
                  Spacer(modifier = Modifier.height(4.dp))
                  Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = RoyalEmerald.copy(alpha = 0.12f),
                    border = BorderStroke(0.5.dp, RoyalEmerald)
                  ) {
                    Text(
                      text = when (selectedLang) {
                        "EN" -> "🌟 Dedicated Quranic Student"
                        "AR" -> "🌟 طالب قرآني مواظب"
                        else -> "🌟 নিবেদিত কুরআনি শিক্ষার্থী"
                      },
                      style = TextStyle(
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = RoyalEmerald
                      ),
                      modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp)
                    )
                  }
                }
              }

              // Sign Out Action Button
              IconButton(
                onClick = onSignOut,
                modifier = Modifier
                  .clip(CircleShape)
                  .background(Color(0xFFFEE2E2))
                  .testTag("profile_sign_out_btn")
              ) {
                Icon(
                  imageVector = Icons.AutoMirrored.Filled.Logout,
                  contentDescription = "Sign Out",
                  tint = Color(0xFFDC2626),
                  modifier = Modifier.size(20.dp)
                )
              }
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = Color(0xFFF1F5F9))
            Spacer(modifier = Modifier.height(14.dp))

            // User High-Level Metric Pills
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceAround,
              verticalAlignment = Alignment.CenterVertically
            ) {
              ProfileStatPill(
                icon = "🏅",
                value = "$userLevel",
                label = when (selectedLang) { "EN" -> "Level" "AR" -> "المستوى" else -> "লেভেল" }
              )

              ProfileStatPill(
                icon = "⭐",
                value = "$userCoins",
                label = when (selectedLang) { "EN" -> "Stars" "AR" -> "النجوم" else -> "নক্ষত্র" }
              )

              ProfileStatPill(
                icon = "🔥",
                value = "$userStreak",
                label = when (selectedLang) { "EN" -> "Day Streak" "AR" -> "أيام متتالية" else -> "দিনের ধারা" }
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 2. Level & XP Progression Card
        Card(
          shape = RoundedCornerShape(20.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
          border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("profile_xp_progression_card")
        ) {
          Column(modifier = Modifier.padding(18.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                  modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(RoyalEmerald.copy(alpha = 0.12f)),
                  contentAlignment = Alignment.Center
                ) {
                  Icon(
                    imageVector = Icons.Default.WorkspacePremium,
                    contentDescription = null,
                    tint = RoyalEmerald,
                    modifier = Modifier.size(20.dp)
                  )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                  Text(
                    text = when (selectedLang) {
                      "EN" -> "Level $userLevel • Tajweed Explorer"
                      "AR" -> "المستوى $userLevel • مستكشف التجويد"
                      else -> "লেভেল $userLevel • তাজবিদ সাধক"
                    },
                    style = TextStyle(
                      fontSize = 15.sp,
                      fontWeight = FontWeight.Bold,
                      color = Color(0xFF0F172A)
                    )
                  )
                  Text(
                    text = when (selectedLang) {
                      "EN" -> "Earn XP through quizzes and pronunciation practice"
                      "AR" -> "اكتسب نقاط الخبرة من خلال الاختبارات وتدريبات النطق"
                      else -> "কুইজ ও মাখরাজ অনুশীলনের মাধ্যমে XP অর্জন করুন"
                    },
                    style = TextStyle(fontSize = 11.sp, color = Color(0xFF64748B))
                  )
                }
              }

              Text(
                text = "750 / 1000 XP",
                style = TextStyle(
                  fontSize = 12.sp,
                  fontWeight = FontWeight.Bold,
                  color = RoyalEmerald
                )
              )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // XP Progress Bar
            LinearProgressIndicator(
              progress = { 0.75f },
              modifier = Modifier
                .fillMaxWidth()
                .height(10.dp)
                .clip(RoundedCornerShape(5.dp)),
              color = RoyalEmerald,
              trackColor = Color(0xFFE2E8F0),
              drawStopIndicator = {}
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
              text = when (selectedLang) {
                "EN" -> "✨ 250 XP remaining to unlock Level ${userLevel + 1} 'Tajweed Hafiz' badge"
                "AR" -> "✨ تبقى ٢٥٠ نقطة خبرة للوصول إلى وسام 'حافظ التجويد' في المستوى ${userLevel + 1}"
                else -> "✨ লেভেল ${userLevel + 1} 'তাজবিদ হাফিজ' ব্যাজ আনলক করতে আর ২৫০ XP প্রয়োজন"
              },
              style = TextStyle(fontSize = 11.sp, color = Color(0xFF059669), fontWeight = FontWeight.Medium)
            )
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 3. Learning Streaks & Weekly Consistency Card
        Card(
          shape = RoundedCornerShape(20.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
          border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("profile_streak_consistency_card")
        ) {
          Column(modifier = Modifier.padding(18.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                  modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFFFF7ED)),
                  contentAlignment = Alignment.Center
                ) {
                  Icon(
                    imageVector = Icons.Default.LocalFireDepartment,
                    contentDescription = null,
                    tint = Color(0xFFEA580C),
                    modifier = Modifier.size(22.dp)
                  )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                  Text(
                    text = when (selectedLang) {
                      "EN" -> "Daily Learning Streak"
                      "AR" -> "تتابع التعلم اليومي"
                      else -> "দৈনিক শিখন ধারাবাহিকতা"
                    },
                    style = TextStyle(
                      fontSize = 15.sp,
                      fontWeight = FontWeight.Bold,
                      color = Color(0xFF0F172A)
                    )
                  )
                  Text(
                    text = when (selectedLang) {
                      "EN" -> "Practice every day to keep the flame alive"
                      "AR" -> "تدرب كل يوم للحفاظ على توهج التتابع"
                      else -> "প্রতিদিন অন্তত ১টি সেশন সম্পন্ন করে শিখা প্রজ্বলিত রাখুন"
                    },
                    style = TextStyle(fontSize = 11.sp, color = Color(0xFF64748B))
                  )
                }
              }

              Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFFFFF7ED),
                border = BorderStroke(1.dp, Color(0xFFFED7AA))
              ) {
                Text(
                  text = "🔥 $userStreak " + when (selectedLang) { "EN" -> "Days" "AR" -> "أيام" else -> "দিন" },
                  style = TextStyle(
                    fontSize = 12.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFEA580C)
                  ),
                  modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                )
              }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 7-Day Consistency Week Rings
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              val daysBn = listOf("রবি", "সোম", "মঙ্গল", "বুধ", "বৃহঃ", "শুক্র", "শনি")
              val daysEn = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
              val isCompletedList = listOf(true, true, true, true, true, false, false)

              daysBn.indices.forEach { index ->
                val dayLabel = if (selectedLang == "EN") daysEn[index] else daysBn[index]
                val done = isCompletedList[index]

                Column(
                  horizontalAlignment = Alignment.CenterHorizontally,
                  modifier = Modifier.weight(1f)
                ) {
                  Box(
                    modifier = Modifier
                      .size(34.dp)
                      .clip(CircleShape)
                      .background(if (done) RoyalEmerald else Color(0xFFF1F5F9))
                      .border(
                        1.dp,
                        if (done) RoyalEmerald else Color(0xFFCBD5E1),
                        CircleShape
                      ),
                    contentAlignment = Alignment.Center
                  ) {
                    if (done) {
                      Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Completed",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                      )
                    } else {
                      Text(
                        text = "•",
                        color = Color(0xFF94A3B8),
                        fontSize = 18.sp
                      )
                    }
                  }

                  Spacer(modifier = Modifier.height(6.dp))

                  Text(
                    text = dayLabel,
                    style = TextStyle(
                      fontSize = 10.5.sp,
                      fontWeight = if (done) FontWeight.Bold else FontWeight.Normal,
                      color = if (done) RoyalEmerald else Color(0xFF64748B)
                    )
                  )
                }
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 4. Achievement Badges Grid Card
        Card(
          shape = RoundedCornerShape(20.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
          border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("profile_badges_card")
        ) {
          Column(modifier = Modifier.padding(18.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Box(
                modifier = Modifier
                  .size(36.dp)
                  .clip(CircleShape)
                  .background(Color(0xFFFEF3C7)),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Default.EmojiEvents,
                  contentDescription = null,
                  tint = Color(0xFFD97706),
                  modifier = Modifier.size(20.dp)
                )
              }
              Spacer(modifier = Modifier.width(10.dp))
              Column {
                Text(
                  text = when (selectedLang) {
                    "EN" -> "Earned Badges & Honors"
                    "AR" -> "الأوسمة والإنجازات المكتسبة"
                    else -> "অর্জিত ব্যাজ ও সম্মাননা"
                  },
                  style = TextStyle(
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A)
                  )
                )
                Text(
                  text = when (selectedLang) {
                    "EN" -> "Milestones reached across Arabic & Tajweed"
                    "AR" -> "المعالم المنجزة في الحروف العربية والتجويد"
                    else -> "হরুফ শিক্ষা ও তাজবিদ অনুশীলনের মাইলফলকসমূহ"
                  },
                  style = TextStyle(fontSize = 11.sp, color = Color(0xFF64748B))
                )
              }
            }

            Spacer(modifier = Modifier.height(14.dp))

            val badges = listOf(
              Triple("🏅", "হরফ ওস্তাদ", "২৮/২৮ হরফ সমাপ্ত"),
              Triple("⭐", "নক্ষত্র সংগ্রাহক", "$userCoins স্টারস সংগৃহীত"),
              Triple("🎯", "নির্ভুল কুইজ", "৯২% নির্ভুলতা স্কোর"),
              Triple("💎", "মাখরাজ সাধক", "৩ডি ভিজ্যুয়ালাইজার")
            )

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              badges.take(2).forEach { (emoji, title, desc) ->
                BadgeItemCard(emoji = emoji, title = title, subtitle = desc, modifier = Modifier.weight(1f))
              }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              badges.drop(2).forEach { (emoji, title, desc) ->
                BadgeItemCard(emoji = emoji, title = title, subtitle = desc, modifier = Modifier.weight(1f))
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 5. Progress Analytics Card
        Card(
          shape = RoundedCornerShape(20.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
          border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("profile_analytics_card")
        ) {
          Column(modifier = Modifier.padding(18.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Box(
                modifier = Modifier
                  .size(36.dp)
                  .clip(CircleShape)
                  .background(RoyalEmerald.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Default.Insights,
                  contentDescription = null,
                  tint = RoyalEmerald,
                  modifier = Modifier.size(20.dp)
                )
              }
              Spacer(modifier = Modifier.width(10.dp))
              Column {
                Text(
                  text = when (selectedLang) {
                    "EN" -> "Learning Progress Analytics"
                    "AR" -> "تحليلات التقدم التعليمي"
                    else -> "শিখন অগ্রগতি পরিসংখ্যান"
                  },
                  style = TextStyle(
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A)
                  )
                )
                Text(
                  text = when (selectedLang) {
                    "EN" -> "Detailed tracking of Quranic alphabet mastery"
                    "AR" -> "متابعة مفصلة لإتقان الأبجدية القرآنية"
                    else -> "আরবি বর্ণমালা ও সঠিক উচ্চারণ আয়ত্তকরণের হিসাব"
                  },
                  style = TextStyle(fontSize = 11.sp, color = Color(0xFF64748B))
                )
              }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Analytics Metric Rows
            AnalyticsBarRow(
              title = "২৮টি আরবি হরফ শিক্ষা",
              value = "২৮ / ২৮ (১০০%)",
              progress = 1.0f,
              color = RoyalEmerald
            )

            Spacer(modifier = Modifier.height(10.dp))

            AnalyticsBarRow(
              title = "তাজবিদ নিয়মাবলি (নূন সাকিন, মাদ)",
              value = "১২ / ১৬ (৭৫%)",
              progress = 0.75f,
              color = Color(0xFF0284C7)
            )

            Spacer(modifier = Modifier.height(10.dp))

            AnalyticsBarRow(
              title = "কুইজ নির্ভুলতার হার",
              value = "৯২% নির্ভুল",
              progress = 0.92f,
              color = Color(0xFFD97706)
            )

            Spacer(modifier = Modifier.height(10.dp))

            AnalyticsBarRow(
              title = "মোট শিখন সেশন সম্পন্ন",
              value = "২৪টি সেশন",
              progress = 0.85f,
              color = Color(0xFF7C3AED)
            )
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 6. 3D Makhraj Visualizer Quick Launch Card
        Card(
          shape = RoundedCornerShape(20.dp),
          colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
          elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
          border = BorderStroke(1.dp, Color(0xFF10B981).copy(alpha = 0.4f)),
          modifier = Modifier
            .fillMaxWidth()
            .clickable { onOpenMakhrajVisualizer() }
            .testTag("profile_makhraj_banner")
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Column(modifier = Modifier.weight(1f)) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                  text = "মাখরাজ ৩ডি ভিজ্যুয়ালাইজার",
                  style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Surface(
                  shape = RoundedCornerShape(4.dp),
                  color = Color(0xFF10B981).copy(alpha = 0.2f),
                  border = BorderStroke(0.5.dp, Color(0xFF10B981))
                ) {
                  Text(
                    text = "3D",
                    style = TextStyle(fontSize = 9.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF34D399)),
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                  )
                }
              }
              Spacer(modifier = Modifier.height(3.dp))
              Text(
                text = "জিহ্বার স্পর্শ, কণ্ঠনালী ও বাতাস ৩ডি কোণে দেখুন",
                style = TextStyle(fontSize = 11.sp, color = Color(0xFF94A3B8))
              )
            }

            Button(
              onClick = onOpenMakhrajVisualizer,
              shape = RoundedCornerShape(10.dp),
              colors = ButtonDefaults.buttonColors(containerColor = RoyalEmerald),
              modifier = Modifier.testTag("profile_open_makhraj_btn")
            ) {
              Text(
                text = "৩ডি দেখুন",
                style = TextStyle(fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 7. App Info Card
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(
            modifier = Modifier.padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Text(
              text = "হুরুফিয়া শরিফ • HURUFIA SHARIF",
              style = TextStyle(
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = RoyalEmerald,
                letterSpacing = 1.sp
              )
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
              text = "সংস্করণ ১.২.০ • কুরআনি তাজবিদ ও ইসলামিক স্যুট",
              style = TextStyle(fontSize = 11.sp, color = Color(0xFF64748B))
            )
          }
        }

        Spacer(modifier = Modifier.height(28.dp))
      }
    }
  }
}

@Composable
private fun ProfileStatPill(
  icon: String,
  value: String,
  label: String
) {
  Column(horizontalAlignment = Alignment.CenterHorizontally) {
    Text(text = icon, fontSize = 22.sp)
    Spacer(modifier = Modifier.height(2.dp))
    Text(
      text = value,
      style = TextStyle(
        fontSize = 15.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF0F172A)
      )
    )
    Text(
      text = label,
      style = TextStyle(
        fontSize = 10.5.sp,
        color = Color(0xFF64748B)
      )
    )
  }
}

@Composable
private fun BadgeItemCard(
  emoji: String,
  title: String,
  subtitle: String,
  modifier: Modifier = Modifier
) {
  Surface(
    shape = RoundedCornerShape(12.dp),
    color = Color(0xFFF8FAFC),
    border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
    modifier = modifier
  ) {
    Row(
      modifier = Modifier.padding(10.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(text = emoji, fontSize = 20.sp)
      Spacer(modifier = Modifier.width(8.dp))
      Column {
        Text(
          text = title,
          style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
        )
        Text(
          text = subtitle,
          style = TextStyle(fontSize = 10.sp, color = Color(0xFF64748B))
        )
      }
    }
  }
}

@Composable
private fun AnalyticsBarRow(
  title: String,
  value: String,
  progress: Float,
  color: Color
) {
  Column(modifier = Modifier.fillMaxWidth()) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = title,
        style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Medium, color = Color(0xFF334155))
      )
      Text(
        text = value,
        style = TextStyle(fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = color)
      )
    }

    Spacer(modifier = Modifier.height(4.dp))

    LinearProgressIndicator(
      progress = { progress },
      modifier = Modifier
        .fillMaxWidth()
        .height(6.dp)
        .clip(RoundedCornerShape(3.dp)),
      color = color,
      trackColor = Color(0xFFE2E8F0),
      drawStopIndicator = {}
    )
  }
}
