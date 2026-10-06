package com.example

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.RoyalEmerald

/**
 * 4 Primary Destinations for persistent bottom navigation:
 * Home, Calendar (Hijri/Gregorian), Qibla (Compass), Profile
 */
enum class MainAppTab(
  val id: String,
  val icon: ImageVector,
  val testTag: String
) {
  HOME(
    id = "home",
    icon = Icons.Default.Home,
    testTag = "bottom_tab_home"
  ),
  CALENDAR(
    id = "calendar",
    icon = Icons.Default.DateRange,
    testTag = "bottom_tab_calendar"
  ),
  QIBLA(
    id = "qibla",
    icon = Icons.Default.Explore,
    testTag = "bottom_tab_qibla"
  ),
  PROFILE(
    id = "profile",
    icon = Icons.Default.Person,
    testTag = "bottom_tab_profile"
  );

  fun getTitle(lang: String): String {
    return when (this) {
      HOME -> when (lang) {
        "EN" -> "Home"
        "AR" -> "الرئيسية"
        else -> "হোম"
      }
      CALENDAR -> when (lang) {
        "EN" -> "Calendar"
        "AR" -> "التقويم"
        else -> "ক্যালেন্ডার"
      }
      QIBLA -> when (lang) {
        "EN" -> "Qibla"
        "AR" -> "القبلة"
        else -> "কিবলা"
      }
      PROFILE -> when (lang) {
        "EN" -> "Profile"
        "AR" -> "الملف الشخصي"
        else -> "প্রোফাইল"
      }
    }
  }
}

/**
 * Persistent 4-Tab Bottom Navigation Bar
 * Features:
 * - Primary Emerald Green (#0A5C36) styling
 * - Smooth active tab capsule indicator with scale and color transitions
 * - Touch targets >= 48dp
 * - Full multi-language support (BN, EN, AR)
 */
@Composable
fun HurufiaBottomNavigationBar(
  currentTab: MainAppTab,
  selectedLang: String,
  onTabSelected: (MainAppTab) -> Unit,
  modifier: Modifier = Modifier
) {
  Surface(
    color = Color.White,
    shadowElevation = 16.dp,
    border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
    modifier = modifier
      .fillMaxWidth()
      .testTag("main_bottom_nav_bar")
  ) {
    Column(modifier = Modifier.fillMaxWidth()) {
      // Subtle top emerald decorative hairline
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(2.dp)
          .background(
            Brush.horizontalGradient(
              colors = listOf(
                RoyalEmerald.copy(alpha = 0.2f),
                RoyalEmerald,
                RoyalEmerald.copy(alpha = 0.2f)
              )
            )
          )
      )

      Row(
        modifier = Modifier
          .fillMaxWidth()
          .navigationBarsPadding()
          .padding(horizontal = 8.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
      ) {
        MainAppTab.entries.forEach { tab ->
          val isSelected = currentTab == tab

          // Smooth animated values
          val containerColor by animateColorAsState(
            targetValue = if (isSelected) RoyalEmerald else Color.Transparent,
            animationSpec = tween(durationMillis = 250, easing = FastOutSlowInEasing),
            label = "tab_container_color"
          )
          val contentColor by animateColorAsState(
            targetValue = if (isSelected) Color.White else Color(0xFF64748B),
            animationSpec = tween(durationMillis = 250),
            label = "tab_content_color"
          )
          val iconScale by animateFloatAsState(
            targetValue = if (isSelected) 1.12f else 1.0f,
            animationSpec = tween(durationMillis = 200, easing = FastOutSlowInEasing),
            label = "tab_icon_scale"
          )

          val interactionSource = remember { MutableInteractionSource() }

          Box(
            modifier = Modifier
              .weight(1f)
              .height(56.dp)
              .clip(RoundedCornerShape(14.dp))
              .background(containerColor)
              .clickable(
                interactionSource = interactionSource,
                indication = ripple(bounded = true, color = RoyalEmerald.copy(alpha = 0.2f))
              ) {
                if (!isSelected) {
                  onTabSelected(tab)
                }
              }
              .padding(horizontal = 2.dp, vertical = 4.dp)
              .testTag(tab.testTag),
            contentAlignment = Alignment.Center
          ) {
            Column(
              horizontalAlignment = Alignment.CenterHorizontally,
              verticalArrangement = Arrangement.Center
            ) {
              Icon(
                imageVector = tab.icon,
                contentDescription = tab.getTitle(selectedLang),
                tint = contentColor,
                modifier = Modifier
                  .size(22.dp)
                  .scale(iconScale)
              )
              Spacer(modifier = Modifier.height(3.dp))
              Text(
                text = tab.getTitle(selectedLang),
                style = TextStyle(
                  fontSize = 10.sp,
                  fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                  color = contentColor,
                  textAlign = TextAlign.Center
                ),
                maxLines = 1,
                softWrap = false,
                overflow = TextOverflow.Ellipsis
              )
            }
          }
        }
      }
    }
  }
}
