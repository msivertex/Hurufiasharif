package com.example

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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.ui.theme.BackgroundGray
import com.example.ui.theme.RoyalEmerald

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsTabScreen(
  selectedLang: String,
  onLanguageChange: (String) -> Unit,
  userEmail: String,
  userLevel: Int,
  userCoins: Int,
  userStreak: Int,
  soundManager: SoundManager,
  onOpenMakhrajVisualizer: () -> Unit,
  onSignOut: () -> Unit
) {
  val layoutDirection = if (selectedLang == "AR") LayoutDirection.Rtl else LayoutDirection.Ltr
  var selectedVoice by remember { mutableStateOf(soundManager.currentVoiceGender) }

  CompositionLocalProvider(LocalLayoutDirection provides layoutDirection) {
    Scaffold(
      containerColor = BackgroundGray,
      topBar = {
        TopAppBar(
          title = {
            Column {
              Text(
                text = when (selectedLang) {
                  "EN" -> "Settings & Profile"
                  "AR" -> "الإعدادات والملف الشخصي"
                  else -> "সেটিংস ও প্রোফাইল"
                },
                style = TextStyle(
                  fontWeight = FontWeight.Bold,
                  color = Color.White,
                  fontSize = 18.sp
                )
              )
              Text(
                text = when (selectedLang) {
                  "EN" -> "Audio, Voice Gender, Gameplay Hints & Account"
                  "AR" -> "الصوت، جنس الصوت، تلميحات اللعب والحساب"
                  else -> "ভয়েস জেন্ডার, গেমপ্লে ইঙ্গিত ও অ্যাকাউন্ট"
                },
                style = TextStyle(color = Color.White.copy(alpha = 0.85f), fontSize = 11.sp)
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
          .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .widthIn(max = 680.dp)
            .padding(horizontal = 16.dp, vertical = 14.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          // 1. User Profile Card
          Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("settings_profile_card")
          ) {
            Column(modifier = Modifier.padding(16.dp)) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Row(
                  modifier = Modifier.weight(1f),
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  // User Avatar
                  Box(
                    modifier = Modifier
                      .size(54.dp)
                      .clip(CircleShape)
                      .background(
                        Brush.radialGradient(
                          listOf(RoyalEmerald, Color(0xFF042F1A))
                        )
                      )
                      .border(2.dp, Color(0xFF34D399), CircleShape),
                    contentAlignment = Alignment.Center
                  ) {
                    Icon(
                      imageVector = Icons.Default.Person,
                      contentDescription = "User Avatar",
                      tint = Color.White,
                      modifier = Modifier.size(32.dp)
                    )
                  }

                  Spacer(modifier = Modifier.width(12.dp))

                  Column {
                    Text(
                      text = userEmail,
                      style = TextStyle(
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                      )
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Surface(
                      shape = RoundedCornerShape(6.dp),
                      color = RoyalEmerald.copy(alpha = 0.12f),
                      border = BorderStroke(0.5.dp, RoyalEmerald)
                    ) {
                      Text(
                        text = when (selectedLang) {
                          "EN" -> "🌟 Dedicated Learner"
                          "AR" -> "🌟 متعلم مواظب"
                          else -> "🌟 নিবেদিত শিক্ষার্থী"
                        },
                        style = TextStyle(
                          fontSize = 10.sp,
                          fontWeight = FontWeight.Bold,
                          color = RoyalEmerald
                        ),
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                      )
                    }
                  }
                }

                // Sign Out Button
                IconButton(
                  onClick = onSignOut,
                  modifier = Modifier
                    .clip(CircleShape)
                    .background(Color(0xFFFEE2E2))
                    .testTag("settings_sign_out_btn")
                ) {
                  Icon(
                    imageVector = Icons.AutoMirrored.Filled.Logout,
                    contentDescription = "Sign Out",
                    tint = Color(0xFFDC2626),
                    modifier = Modifier.size(20.dp)
                  )
                }
              }

              Spacer(modifier = Modifier.height(14.dp))
              HorizontalDivider(color = Color(0xFFF1F5F9))
              Spacer(modifier = Modifier.height(12.dp))

              // User Stats Row
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
              ) {
                // Level
                StatPill(
                  emoji = "🏅",
                  label = when (selectedLang) { "EN" -> "Level" "AR" -> "المستوى" else -> "লেভেল" },
                  value = "$userLevel"
                )

                // Stars/Coins
                StatPill(
                  emoji = "⭐",
                  label = when (selectedLang) { "EN" -> "Stars" "AR" -> "النجوم" else -> "স্টারস" },
                  value = "$userCoins"
                )

                // Streak
                StatPill(
                  emoji = "🔥",
                  label = when (selectedLang) { "EN" -> "Streak" "AR" -> "التتابع" else -> "ধারাবাহিকতা" },
                  value = "$userStreak " + when (selectedLang) { "EN" -> "Days" "AR" -> "أيام" else -> "দিন" }
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(16.dp))

          // 2. Voice Gender Selection (Male Qari & Female Qaria)
          Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("settings_voice_gender_card")
          ) {
            Column(modifier = Modifier.padding(16.dp)) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                  modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(RoyalEmerald.copy(alpha = 0.12f)),
                  contentAlignment = Alignment.Center
                ) {
                  Icon(
                    imageVector = Icons.Default.VolumeUp,
                    contentDescription = null,
                    tint = RoyalEmerald,
                    modifier = Modifier.size(18.dp)
                  )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                  Text(
                    text = when (selectedLang) {
                      "EN" -> "Voice Gender Selection (Recitation)"
                      "AR" -> "اختيار جنس الصوت (التلاوة الصوتية)"
                      else -> "কণ্ঠস্বর নির্বাচন (পুরুষ ও মহিলা অডিও)"
                    },
                    style = TextStyle(
                      fontSize = 14.sp,
                      fontWeight = FontWeight.Bold,
                      color = Color(0xFF0F172A)
                    )
                  )
                  Text(
                    text = when (selectedLang) {
                      "EN" -> "Select preferred Tajweed recitation voice"
                      "AR" -> "اختر صوت التلاوة المفضل لتعلم الحروف"
                      else -> "কুরআনি হরফ উচ্চারণ ও মাখরাজের জন্য কণ্ঠ নির্বাচন করুন"
                    },
                    style = TextStyle(fontSize = 11.sp, color = Color(0xFF64748B))
                  )
                }
              }

              Spacer(modifier = Modifier.height(14.dp))

              // Male Voice Option
              VoiceGenderCard(
                gender = VoiceGender.MALE_QARI,
                isSelected = selectedVoice == VoiceGender.MALE_QARI,
                selectedLang = selectedLang,
                onSelect = {
                  selectedVoice = VoiceGender.MALE_QARI
                  soundManager.setVoiceGender(VoiceGender.MALE_QARI)
                  soundManager.previewVoiceSample(VoiceGender.MALE_QARI)
                },
                onPreviewAudio = {
                  soundManager.previewVoiceSample(VoiceGender.MALE_QARI)
                }
              )

              Spacer(modifier = Modifier.height(10.dp))

              // Female Voice Option
              VoiceGenderCard(
                gender = VoiceGender.FEMALE_QARIA,
                isSelected = selectedVoice == VoiceGender.FEMALE_QARIA,
                selectedLang = selectedLang,
                onSelect = {
                  selectedVoice = VoiceGender.FEMALE_QARIA
                  soundManager.setVoiceGender(VoiceGender.FEMALE_QARIA)
                  soundManager.previewVoiceSample(VoiceGender.FEMALE_QARIA)
                },
                onPreviewAudio = {
                  soundManager.previewVoiceSample(VoiceGender.FEMALE_QARIA)
                }
              )
            }
          }

          Spacer(modifier = Modifier.height(16.dp))

          // 3. Quiz Gameplay Hints (beginner_hints_enabled)
          Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("settings_hints_card")
          ) {
            Column(modifier = Modifier.padding(16.dp)) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Row(
                  modifier = Modifier.weight(1f),
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Box(
                    modifier = Modifier
                      .size(34.dp)
                      .clip(CircleShape)
                      .background(
                        if (soundManager.beginnerHintsEnabledState) RoyalEmerald.copy(alpha = 0.12f)
                        else Color(0xFFF1F5F9)
                      ),
                    contentAlignment = Alignment.Center
                  ) {
                    Icon(
                      imageVector = Icons.Default.Lightbulb,
                      contentDescription = null,
                      tint = if (soundManager.beginnerHintsEnabledState) RoyalEmerald else Color(0xFF64748B),
                      modifier = Modifier.size(18.dp)
                    )
                  }

                  Spacer(modifier = Modifier.width(10.dp))

                  Column {
                    Text(
                      text = when (selectedLang) {
                        "EN" -> "Quiz Gameplay Hints"
                        "AR" -> "تلميحات الاختبار واللعب"
                        else -> "কুইজ ও গেমপ্লে ইঙ্গিত"
                      },
                      style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                      )
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                      text = when (selectedLang) {
                        "EN" -> "Show positional text ('Beginning', 'Middle', 'End') on quiz cards"
                        "AR" -> "إظهار مسميات المواضع (في البداية، في الوسط، في النهاية) أسفل بطاقات الخيارات"
                        else -> "কুইজের অপশন কার্ডে হরফের অবস্থানগত সংক্ষিপ্ত রূপ দেখান"
                      },
                      style = TextStyle(fontSize = 11.sp, color = Color(0xFF64748B), lineHeight = 14.sp)
                    )
                  }
                }

                Spacer(modifier = Modifier.width(8.dp))

                Switch(
                  checked = soundManager.beginnerHintsEnabledState,
                  onCheckedChange = { isChecked ->
                    soundManager.setBeginnerHintsEnabled(isChecked)
                  },
                  colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = RoyalEmerald,
                    uncheckedThumbColor = Color.White,
                    uncheckedTrackColor = Color(0xFFCBD5E1)
                  ),
                  modifier = Modifier.testTag("settings_beginner_hints_switch")
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(16.dp))

          // 4. 3D Makhraj Visualizer Quick Launch
          Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            border = BorderStroke(1.5.dp, Color(0xFF10B981).copy(alpha = 0.5f)),
            modifier = Modifier
              .fillMaxWidth()
              .clickable { onOpenMakhrajVisualizer() }
              .testTag("settings_open_makhraj_banner")
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
                    text = when (selectedLang) {
                      "EN" -> "3D Makhraj Visualizer"
                      "AR" -> "المجسم ثلاثي الأبعاد لمخارج الحروف"
                      else -> "মাখরাজ ৩ডি ভিজ্যুয়ালাইজার"
                    },
                    style = TextStyle(
                      fontSize = 14.sp,
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
                    else -> "জিহ্বার স্পর্শ, কণ্ঠনালী ও বায়ুপ্রবাহ ৩ডি কোণে দেখুন"
                  },
                  style = TextStyle(fontSize = 11.sp, color = Color(0xFF94A3B8))
                )
              }

              Button(
                onClick = onOpenMakhrajVisualizer,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = RoyalEmerald),
                modifier = Modifier.testTag("settings_open_makhraj_btn")
              ) {
                Text(
                  text = when (selectedLang) { "EN" -> "Open 3D" "AR" -> "عرض" else -> "৩ডি দেখুন" },
                  style = TextStyle(fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(16.dp))

          // 5. Language Selection
          Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("settings_language_card")
          ) {
            Column(modifier = Modifier.padding(16.dp)) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                  modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(RoyalEmerald.copy(alpha = 0.12f)),
                  contentAlignment = Alignment.Center
                ) {
                  Icon(
                    imageVector = Icons.Default.Language,
                    contentDescription = null,
                    tint = RoyalEmerald,
                    modifier = Modifier.size(18.dp)
                  )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                  text = when (selectedLang) {
                    "EN" -> "Application Interface Language"
                    "AR" -> "لغة واجهة التطبيق"
                    else -> "অ্যাপের ভাষা পরিবর্তন"
                  },
                  style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A)
                  )
                )
              }

              Spacer(modifier = Modifier.height(12.dp))

              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
              ) {
                listOf(
                  "BN" to "বাংলা (BN)",
                  "EN" to "English (EN)",
                  "AR" to "العربية (AR)"
                ).forEach { (code, label) ->
                  val isCurrent = selectedLang == code
                  Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (isCurrent) RoyalEmerald else Color(0xFFF1F5F9),
                    border = BorderStroke(1.dp, if (isCurrent) RoyalEmerald else Color(0xFFCBD5E1)),
                    modifier = Modifier
                      .weight(1f)
                      .clickable { onLanguageChange(code) }
                      .testTag("settings_lang_btn_$code")
                  ) {
                    Text(
                      text = label,
                      style = TextStyle(
                        fontSize = 11.5.sp,
                        fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Medium,
                        color = if (isCurrent) Color.White else Color(0xFF334155),
                        textAlign = TextAlign.Center
                      ),
                      modifier = Modifier.padding(vertical = 10.dp, horizontal = 4.dp)
                    )
                  }
                }
              }
            }
          }

          Spacer(modifier = Modifier.height(16.dp))

          // 6. About App Card
          Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(
              modifier = Modifier.padding(16.dp),
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
              Spacer(modifier = Modifier.height(3.dp))
              Text(
                text = when (selectedLang) {
                  "EN" -> "Version 1.2.0 • Interactive Quranic Tajweed & Islamic Suite"
                  "AR" -> "الإصدار ١.٢.٠ • منصة التجويد القرآني والحقيبة الإسلامية"
                  else -> "সংস্করণ ১.২.০ • কুরআনি তাজবিদ ও ইসলামিক স্যুট"
                },
                style = TextStyle(fontSize = 11.sp, color = Color(0xFF64748B))
              )
            }
          }

          Spacer(modifier = Modifier.height(24.dp))
        }
      }
    }
  }
}

@Composable
private fun StatPill(
  emoji: String,
  label: String,
  value: String
) {
  Column(horizontalAlignment = Alignment.CenterHorizontally) {
    Text(text = emoji, fontSize = 20.sp)
    Spacer(modifier = Modifier.height(3.dp))
    Text(
      text = value,
      style = TextStyle(
        fontSize = 13.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF0F172A)
      )
    )
    Text(
      text = label,
      style = TextStyle(
        fontSize = 10.sp,
        color = Color(0xFF64748B)
      )
    )
  }
}
