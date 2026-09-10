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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.RoyalEmerald

/**
 * App Settings Dialog containing Global Voice Gender Selection (Male Qari vs Female Qaria),
 * Language Preferences, Audio Preview, and Tajweed Engine controls.
 */
@Composable
fun AppSettingsDialog(
  selectedLang: String,
  onLanguageChange: (String) -> Unit,
  soundManager: SoundManager,
  onOpenMakhrajVisualizer: () -> Unit,
  onDismiss: () -> Unit
) {
  var selectedVoice by remember { mutableStateOf(soundManager.currentVoiceGender) }

  Dialog(onDismissRequest = onDismiss) {
    Card(
      shape = RoundedCornerShape(24.dp),
      colors = CardDefaults.cardColors(containerColor = Color.White),
      elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
      modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)
        .testTag("app_settings_dialog")
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(20.dp)
          .verticalScroll(rememberScrollState())
      ) {
        // Header
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
                .background(RoyalEmerald.copy(alpha = 0.1f)),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = null,
                tint = RoyalEmerald,
                modifier = Modifier.size(20.dp)
              )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Text(
              text = when (selectedLang) {
                "EN" -> "App Settings"
                "AR" -> "إعدادات التطبيق"
                else -> "অ্যাপ সেটিংস"
              },
              style = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F172A)
              )
            )
          }

          IconButton(
            onClick = onDismiss,
            modifier = Modifier.testTag("close_settings_dialog")
          ) {
            Icon(
              imageVector = Icons.Default.Close,
              contentDescription = "Close",
              tint = Color(0xFF64748B)
            )
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // SECTION 1: Voice Gender Selection (Male & Female Audio Support)
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.VolumeUp,
            contentDescription = null,
            tint = RoyalEmerald,
            modifier = Modifier.size(18.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = when (selectedLang) {
              "EN" -> "Voice Gender Selection (Audio Recitation)"
              "AR" -> "اختيار جنس الصوت (التلاوة الصوتية)"
              else -> "কণ্ঠস্বর নির্বাচন (পুরুষ ও মহিলা অডিও)"
            },
            style = TextStyle(
              fontSize = 13.sp,
              fontWeight = FontWeight.Bold,
              color = Color(0xFF1E293B)
            )
          )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
          text = when (selectedLang) {
            "EN" -> "Choose the voice profile used for Tajweed letter pronunciations and audio feedback:"
            "AR" -> "اختر نوع الصوت المعتمد لنطق الحروف وأحكام التجويد في التطبيق:"
            else -> "কুরআনি হরফ উচ্চারণ ও মাখরাজের জন্য আপনার পছন্দের কণ্ঠ নির্বাচন করুন:"
          },
          style = TextStyle(fontSize = 11.sp, color = Color(0xFF64748B), lineHeight = 15.sp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Option 1: Male Qari Voice
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

        Spacer(modifier = Modifier.height(8.dp))

        // Option 2: Female Qaria Voice
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

        Spacer(modifier = Modifier.height(18.dp))
        HorizontalDivider(color = Color(0xFFE2E8F0))
        Spacer(modifier = Modifier.height(14.dp))

        // SECTION 2: Global Quiz & Gameplay Hints (beginner_hints_enabled)
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.Lightbulb,
            contentDescription = null,
            tint = RoyalEmerald,
            modifier = Modifier.size(18.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = when (selectedLang) {
              "EN" -> "Quiz Gameplay Hints"
              "AR" -> "تلميحات الاختبار واللعب"
              else -> "কুইজ ও গেমপ্লে ইঙ্গিত"
            },
            style = TextStyle(
              fontSize = 13.sp,
              fontWeight = FontWeight.Bold,
              color = Color(0xFF1E293B)
            )
          )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Global toggle card for beginner_hints_enabled
        Card(
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(
            containerColor = if (soundManager.beginnerHintsEnabledState) Color(0xFFECFDF5) else Color(0xFFF8FAFC)
          ),
          border = BorderStroke(
            if (soundManager.beginnerHintsEnabledState) 1.5.dp else 1.dp,
            if (soundManager.beginnerHintsEnabledState) RoyalEmerald else Color(0xFFCBD5E1)
          ),
          modifier = Modifier
            .fillMaxWidth()
            .clickable {
              soundManager.setBeginnerHintsEnabled(!soundManager.beginnerHintsEnabledState)
            }
            .testTag("beginner_hints_enabled_card")
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Row(
              modifier = Modifier.weight(1f),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Box(
                modifier = Modifier
                  .size(36.dp)
                  .clip(CircleShape)
                  .background(
                    if (soundManager.beginnerHintsEnabledState) RoyalEmerald.copy(alpha = 0.15f)
                    else Color(0xFFE2E8F0)
                  ),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Default.Lightbulb,
                  contentDescription = null,
                  tint = if (soundManager.beginnerHintsEnabledState) RoyalEmerald else Color(0xFF64748B),
                  modifier = Modifier.size(20.dp)
                )
              }

              Spacer(modifier = Modifier.width(10.dp))

              Column {
                Text(
                  text = when (selectedLang) {
                    "EN" -> "Beginner Hints (Option Labels)"
                    "AR" -> "تلميحات المبتدئين (مواضع الخيارات)"
                    else -> "শিক্ষানবিস ইঙ্গিত (কার্ডের অবস্থান লেবেল)"
                  },
                  style = TextStyle(
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (soundManager.beginnerHintsEnabledState) RoyalEmerald else Color(0xFF0F172A)
                  )
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                  text = when (selectedLang) {
                    "EN" -> "Show positional text ('Beginning', 'Middle', 'End') on option cards"
                    "AR" -> "إظهار مسميات المواضع (في البداية، في الوسط، في النهاية) أسفل بطاقات الخيارات"
                    else -> "কুইজের অপশন কার্ডে হরফের অবস্থানগত সংক্ষিপ্ত রূপ (শুরুতে, মাঝে, শেষে) দেখান"
                  },
                  style = TextStyle(
                    fontSize = 10.sp,
                    color = Color(0xFF64748B),
                    lineHeight = 13.sp
                  )
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
              modifier = Modifier.testTag("beginner_hints_enabled")
            )
          }
        }

        Spacer(modifier = Modifier.height(18.dp))
        HorizontalDivider(color = Color(0xFFE2E8F0))
        Spacer(modifier = Modifier.height(14.dp))

        // SECTION 3: 3D Makhraj Visualizer Quick Launch
        Card(
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
          modifier = Modifier.fillMaxWidth()
        ) {
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
                  "EN" -> "3D Makhraj Visualizer"
                  "AR" -> "المجسم ثلاثي الأبعاد لمخارج الحروف"
                  else -> "উচ্চারণস্থান (মাখরাজ) ৩ডি অ্যানিমেশন"
                },
                style = TextStyle(
                  fontSize = 13.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color.White
                )
              )
              Spacer(modifier = Modifier.height(2.dp))
              Text(
                text = when (selectedLang) {
                  "EN" -> "Inspect tongue, throat, and vocal airflow in 3D."
                  "AR" -> "شاهد حركة اللسان والشفتين ومسار الهواء ثلاثي الأبعاد."
                  else -> "জিহ্বা, গলা ও বাতাসের প্রবাহ ৩ডি কোণে দেখুন।"
                },
                style = TextStyle(fontSize = 10.sp, color = Color(0xFF94A3B8))
              )
            }

            Button(
              onClick = {
                onDismiss()
                onOpenMakhrajVisualizer()
              },
              shape = RoundedCornerShape(10.dp),
              colors = ButtonDefaults.buttonColors(containerColor = RoyalEmerald),
              modifier = Modifier.testTag("settings_open_makhraj_button")
            ) {
              Text(
                text = when (selectedLang) { "EN" -> "Open 3D" "AR" -> "فتح" else -> "৩ডি দেখুন" },
                style = TextStyle(fontSize = 11.sp, fontWeight = FontWeight.Bold)
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(18.dp))
        HorizontalDivider(color = Color(0xFFE2E8F0))
        Spacer(modifier = Modifier.height(14.dp))

        // SECTION 3: Language Selection
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.Language,
            contentDescription = null,
            tint = RoyalEmerald,
            modifier = Modifier.size(18.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = when (selectedLang) {
              "EN" -> "Application Interface Language"
              "AR" -> "لغة واجهة التطبيق"
              else -> "অ্যাপের ভাষা নির্বাচন"
            },
            style = TextStyle(
              fontSize = 13.sp,
              fontWeight = FontWeight.Bold,
              color = Color(0xFF1E293B)
            )
          )
        }

        Spacer(modifier = Modifier.height(10.dp))

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
              shape = RoundedCornerShape(10.dp),
              color = if (isCurrent) RoyalEmerald else Color(0xFFF1F5F9),
              border = BorderStroke(1.dp, if (isCurrent) RoyalEmerald else Color(0xFFCBD5E1)),
              modifier = Modifier
                .weight(1f)
                .clickable { onLanguageChange(code) }
                .testTag("settings_lang_$code")
            ) {
              Text(
                text = label,
                style = TextStyle(
                  fontSize = 11.sp,
                  fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Medium,
                  color = if (isCurrent) Color.White else Color(0xFF334155),
                  textAlign = androidx.compose.ui.text.style.TextAlign.Center
                ),
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp)
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Done Button
        Button(
          onClick = onDismiss,
          shape = RoundedCornerShape(12.dp),
          colors = ButtonDefaults.buttonColors(containerColor = RoyalEmerald),
          modifier = Modifier
            .fillMaxWidth()
            .height(44.dp)
            .testTag("settings_done_button")
        ) {
          Text(
            text = when (selectedLang) { "EN" -> "Save & Close" "AR" -> "حفظ وإغلاق" else -> "সংরক্ষণ ও বন্ধ করুন" },
            style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
          )
        }
      }
    }
  }
}

@Composable
fun VoiceGenderCard(
  gender: VoiceGender,
  isSelected: Boolean,
  selectedLang: String,
  onSelect: () -> Unit,
  onPreviewAudio: () -> Unit
) {
  Card(
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(
      containerColor = if (isSelected) Color(0xFFECFDF5) else Color(0xFFF8FAFC)
    ),
    border = BorderStroke(
      if (isSelected) 2.dp else 1.dp,
      if (isSelected) RoyalEmerald else Color(0xFFE2E8F0)
    ),
    modifier = Modifier
      .fillMaxWidth()
      .clickable { onSelect() }
      .testTag("voice_card_${gender.id}")
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(12.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Row(
        modifier = Modifier.weight(1f),
        verticalAlignment = Alignment.CenterVertically
      ) {
        RadioButton(
          selected = isSelected,
          onClick = onSelect,
          colors = RadioButtonDefaults.colors(selectedColor = RoyalEmerald)
        )

        Spacer(modifier = Modifier.width(4.dp))

        Column {
          Text(
            text = gender.getTitle(selectedLang),
            style = TextStyle(
              fontSize = 13.sp,
              fontWeight = FontWeight.Bold,
              color = if (isSelected) RoyalEmerald else Color(0xFF0F172A)
            )
          )
          Spacer(modifier = Modifier.height(2.dp))
          Text(
            text = gender.getSubtitle(selectedLang),
            style = TextStyle(
              fontSize = 10.sp,
              color = Color(0xFF64748B),
              lineHeight = 13.sp
            )
          )
        }
      }

      // Preview Audio Button
      OutlinedButton(
        onClick = onPreviewAudio,
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, RoyalEmerald),
        modifier = Modifier
          .padding(start = 6.dp)
          .testTag("preview_voice_${gender.id}")
      ) {
        Icon(
          imageVector = Icons.Default.VolumeUp,
          contentDescription = "Preview",
          tint = RoyalEmerald,
          modifier = Modifier.size(15.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
          text = when (selectedLang) { "EN" -> "Sample" "AR" -> "عينة" else -> "নমুনা" },
          style = TextStyle(fontSize = 10.sp, fontWeight = FontWeight.Bold, color = RoyalEmerald)
        )
      }
    }
  }
}
