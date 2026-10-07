package com.example

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.RoyalEmerald

/**
 * Dedicated "About Developer / ডেভেলপার সম্পর্কিত তথ্য" Profile Card Component.
 *
 * Developer Profile:
 * - Developer Name: MD SHARIFUL ISLAM (মোঃ শরিফুল ইসলাম)
 * - Organization: MSI VERTEX
 * - Profession: Radiologic Technologist
 * - Service/Affiliation: Bangladesh Army Medical Corps (AMC)
 * - Location: Paikgacha, Khulna, Bangladesh
 * - Contact Email: mdsharifbdarmy@gmail.com
 */
@Composable
fun DeveloperProfileCard(
  selectedLang: String = "BN",
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val developerEmail = "mdsharifbdarmy@gmail.com"
  val developerLocation = "Paikgacha, Khulna, Bangladesh"

  Card(
    shape = RoundedCornerShape(22.dp),
    colors = CardDefaults.cardColors(
      containerColor = Color.White.copy(alpha = 0.95f)
    ),
    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    border = BorderStroke(1.dp, Color(0xFF10B981).copy(alpha = 0.35f)),
    modifier = modifier
      .fillMaxWidth()
      .testTag("developer_profile_card")
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .background(
          Brush.verticalGradient(
            colors = listOf(
              Color(0xFFF0FDF4),
              Color(0xFFFFFFFF),
              Color(0xFFF8FAFC)
            )
          )
        )
        .padding(18.dp)
    ) {
      // Top Title & Badge
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(38.dp)
              .clip(CircleShape)
              .background(
                Brush.linearGradient(
                  listOf(Color(0xFF047857), Color(0xFF065F46))
                )
              )
              .border(1.5.dp, Color(0xFF34D399), CircleShape),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Person,
              contentDescription = null,
              tint = Color.White,
              modifier = Modifier.size(22.dp)
            )
          }

          Spacer(modifier = Modifier.width(10.dp))

          Column {
            Text(
              text = when (selectedLang) {
                "EN" -> "About Developer"
                "AR" -> "معلومات المطور"
                else -> "ডেভেলপার সম্পর্কিত তথ্য"
              },
              style = TextStyle(
                fontSize = 15.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF0F172A)
              )
            )
            Text(
              text = when (selectedLang) {
                "EN" -> "Lead Engineer & Research"
                "AR" -> "المطور والمشرف التقني"
                else -> "প্রধান ডেভেলপার ও ইসলামিক প্রযুক্তি গবেষক"
              },
              style = TextStyle(fontSize = 11.sp, color = Color(0xFF64748B))
            )
          }
        }

        // Verified Army / Medical Corps Badge
        Surface(
          shape = RoundedCornerShape(12.dp),
          color = Color(0xFFECFDF5),
          border = BorderStroke(1.dp, Color(0xFFA7F3D0))
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.Default.MilitaryTech,
              contentDescription = null,
              tint = Color(0xFF059669),
              modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = "AMC",
              style = TextStyle(
                fontSize = 10.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF047857)
              )
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Main Developer Name Banner
      Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
        shadowElevation = 1.dp,
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(
          modifier = Modifier.padding(14.dp)
        ) {
          Text(
            text = "MD SHARIFUL ISLAM",
            style = TextStyle(
              fontSize = 16.sp,
              fontWeight = FontWeight.ExtraBold,
              color = Color(0xFF0F172A),
              letterSpacing = 0.5.sp
            )
          )
          Spacer(modifier = Modifier.height(2.dp))
          Text(
            text = "মোঃ শরিফুল ইসলাম",
            style = TextStyle(
              fontSize = 14.sp,
              fontWeight = FontWeight.Bold,
              color = RoyalEmerald
            )
          )
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Professional Metadata List
      Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        // 1. Organization
        DeveloperInfoRow(
          icon = Icons.Default.Business,
          iconBgColor = Color(0xFFEFF6FF),
          iconTint = Color(0xFF2563EB),
          label = if (selectedLang == "BN") "প্রতিষ্ঠান / Organization" else "Organization",
          value = "MSI VERTEX"
        )

        // 2. Profession
        DeveloperInfoRow(
          icon = Icons.Default.LocalHospital,
          iconBgColor = Color(0xFFECFDF5),
          iconTint = Color(0xFF059669),
          label = if (selectedLang == "BN") "পেশা / Profession" else "Profession",
          value = "Radiologic Technologist"
        )

        // 3. Service / Affiliation
        DeveloperInfoRow(
          icon = Icons.Default.MilitaryTech,
          iconBgColor = Color(0xFFFEF3C7),
          iconTint = Color(0xFFD97706),
          label = if (selectedLang == "BN") "সার্ভিস ও অধিভুক্তি" else "Service / Affiliation",
          value = "Bangladesh Army Medical Corps (AMC)"
        )

        // 4. Location with Map Pin Indicator
        DeveloperInfoRow(
          icon = Icons.Default.LocationOn,
          iconBgColor = Color(0xFFFEE2E2),
          iconTint = Color(0xFFDC2626),
          label = if (selectedLang == "BN") "অবস্থান / Location" else "Location",
          value = developerLocation,
          isInteractive = true,
          onClick = {
            openLocationInMaps(context, developerLocation)
          }
        )

        // 5. Contact Email
        DeveloperInfoRow(
          icon = Icons.Default.Email,
          iconBgColor = Color(0xFFF3E8FF),
          iconTint = Color(0xFF7C3AED),
          label = if (selectedLang == "BN") "ইমেইল / Contact Email" else "Contact Email",
          value = developerEmail,
          isInteractive = true,
          onClick = {
            sendEmailToDeveloper(context, developerEmail)
          }
        )
      }

      Spacer(modifier = Modifier.height(16.dp))
      HorizontalDivider(color = Color(0xFFE2E8F0))
      Spacer(modifier = Modifier.height(14.dp))

      // Interactive Contact Button: Launches device email client
      Button(
        onClick = {
          sendEmailToDeveloper(context, developerEmail)
        },
        colors = ButtonDefaults.buttonColors(
          containerColor = RoyalEmerald
        ),
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier
          .fillMaxWidth()
          .height(48.dp)
          .testTag("contact_developer_email_btn")
      ) {
        Icon(
          imageVector = Icons.Default.Send,
          contentDescription = null,
          tint = Color.White,
          modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = when (selectedLang) {
            "EN" -> "Contact Developer ($developerEmail)"
            "AR" -> "مراسلة المطور عبر البريد"
            else -> "ইমেইল করুন (Contact Developer)"
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

@Composable
private fun DeveloperInfoRow(
  icon: ImageVector,
  iconBgColor: Color,
  iconTint: Color,
  label: String,
  value: String,
  isInteractive: Boolean = false,
  onClick: (() -> Unit)? = null
) {
  Surface(
    shape = RoundedCornerShape(12.dp),
    color = Color.White,
    border = BorderStroke(1.dp, Color(0xFFF1F5F9)),
    modifier = Modifier
      .fillMaxWidth()
      .then(if (isInteractive && onClick != null) Modifier.clickable { onClick() } else Modifier)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 12.dp, vertical = 10.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Box(
        modifier = Modifier
          .size(32.dp)
          .clip(CircleShape)
          .background(iconBgColor),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = icon,
          contentDescription = null,
          tint = iconTint,
          modifier = Modifier.size(17.dp)
        )
      }

      Spacer(modifier = Modifier.width(10.dp))

      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = label,
          style = TextStyle(
            fontSize = 10.5.sp,
            color = Color(0xFF64748B),
            fontWeight = FontWeight.Medium
          )
        )
        Text(
          text = value,
          style = TextStyle(
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF0F172A)
          )
        )
      }

      if (isInteractive) {
        Surface(
          shape = RoundedCornerShape(8.dp),
          color = Color(0xFFF8FAFC),
          border = BorderStroke(0.5.dp, Color(0xFFE2E8F0))
        ) {
          Text(
            text = "ট্যাপ",
            style = TextStyle(fontSize = 9.5.sp, color = Color(0xFF64748B)),
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
          )
        }
      }
    }
  }
}

/**
 * Launch device mail application addressed directly to the developer
 */
fun sendEmailToDeveloper(context: Context, emailAddress: String) {
  try {
    val intent = Intent(Intent.ACTION_SENDTO).apply {
      data = Uri.parse("mailto:$emailAddress")
      putExtra(Intent.EXTRA_SUBJECT, "Hurufia Sharif App - Feedback & Query")
      putExtra(Intent.EXTRA_TEXT, "আসসালামু আলাইকুম,\n\nহুরুফিয়া শরিফ অ্যাপ সম্পর্কিত বার্তা:\n\n")
    }
    context.startActivity(Intent.createChooser(intent, "ইমেইল ক্লায়েন্ট নির্বাচন করুন"))
  } catch (e: Exception) {
    Toast.makeText(context, "ইমেইল অ্যাপ খুঁজে পাওয়া যায়নি: $emailAddress", Toast.LENGTH_SHORT).show()
  }
}

/**
 * Open map location in maps application
 */
fun openLocationInMaps(context: Context, locationQuery: String) {
  try {
    val gmmIntentUri = Uri.parse("geo:0,0?q=${Uri.encode(locationQuery)}")
    val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
    context.startActivity(Intent.createChooser(mapIntent, "মানচিত্রে দেখুন"))
  } catch (e: Exception) {
    Toast.makeText(context, locationQuery, Toast.LENGTH_SHORT).show()
  }
}
