package com.example

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Mosque
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.islamic.DailyPrayerSchedule
import com.example.ui.theme.BackgroundGray
import com.example.ui.theme.RoyalEmerald

/**
 * 1. SALAT RULES & GUIDE FULL-SCREEN VIEW
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SalatGuideScreen(
  selectedLang: String,
  onBack: () -> Unit
) {
  var selectedTab by remember { mutableIntStateOf(0) }
  val tabs = when (selectedLang) {
    "EN" -> listOf("Step-by-Step", "Rakats Table", "Essential Rules")
    "AR" -> listOf("خطوات الصلاة", "جدول الركعات", "أحكام وشروط")
    else -> listOf("নামাজের নিয়ম", "রাকাত তালিকা", "জরুরি হুকুম")
  }

  Scaffold(
    containerColor = BackgroundGray,
    topBar = {
      TopAppBar(
        title = {
          Text(
            text = when (selectedLang) {
              "EN" -> "Salat Guide & Rules"
              "AR" -> "دليل وأحكام الصلاة"
              else -> "নামাজের নিয়ম ও হুকুমাত"
            },
            style = TextStyle(
              fontSize = 19.sp,
              fontWeight = FontWeight.Bold,
              color = Color.White
            )
          )
        },
        navigationIcon = {
          IconButton(onClick = onBack) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowBack,
              contentDescription = "Back",
              tint = Color.White
            )
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = RoyalEmerald)
      )
    }
  ) { padding ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(padding)
        .navigationBarsPadding(),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      TabRow(
        selectedTabIndex = selectedTab,
        containerColor = Color.White,
        contentColor = RoyalEmerald,
        indicator = { tabPositions ->
          TabRowDefaults.SecondaryIndicator(
            modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
            color = RoyalEmerald,
            height = 3.dp
          )
        }
      ) {
        tabs.forEachIndexed { index, title ->
          Tab(
            selected = selectedTab == index,
            onClick = { selectedTab = index },
            text = {
              Text(
                text = title,
                style = TextStyle(
                  fontSize = 14.sp,
                  fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Medium,
                  color = if (selectedTab == index) RoyalEmerald else Color(0xFF64748B)
                )
              )
            }
          )
        }
      }

      Box(
        modifier = Modifier
          .fillMaxSize()
          .widthIn(max = 680.dp)
          .padding(horizontal = 16.dp, vertical = 12.dp)
      ) {
        when (selectedTab) {
          0 -> {
            LazyColumn(
              verticalArrangement = Arrangement.spacedBy(12.dp),
              modifier = Modifier.fillMaxSize()
            ) {
              item {
                Card(
                  shape = RoundedCornerShape(16.dp),
                  colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
                  border = BorderStroke(1.dp, RoyalEmerald.copy(alpha = 0.3f)),
                  modifier = Modifier.fillMaxWidth()
                ) {
                  Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    Text(text = "💡", fontSize = 24.sp)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                      text = when (selectedLang) {
                        "EN" -> "Step-by-step visual guidance for performing perfect Salah with Khushu."
                        "AR" -> "دليل عملي مصور لأداء صلاة صحيحة بخشوع وطمأنينة."
                        else -> "সহিহ নামাজ আদায়ের ধারাবাহিক ধাপসমূহ। প্রতিটি পর্বে তাসবীহ ও নির্দেশিকা দেওয়া হলো।"
                      },
                      style = TextStyle(fontSize = 13.sp, color = Color(0xFF1B5E20), lineHeight = 18.sp)
                    )
                  }
                }
              }

              items(SalatRepository.stepsList) { step ->
                Card(
                  shape = RoundedCornerShape(16.dp),
                  colors = CardDefaults.cardColors(containerColor = Color.White),
                  elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                  border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                  modifier = Modifier.fillMaxWidth()
                ) {
                  Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                      modifier = Modifier.fillMaxWidth(),
                      horizontalArrangement = Arrangement.SpaceBetween,
                      verticalAlignment = Alignment.CenterVertically
                    ) {
                      Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                          modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(RoyalEmerald),
                          contentAlignment = Alignment.Center
                        ) {
                          Text(
                            text = "${step.stepNumber}",
                            style = TextStyle(color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                          )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                          text = if (selectedLang == "BN") step.titleBn else step.titleEn,
                          style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
                        )
                      }
                      Text(text = step.iconEmoji, fontSize = 22.sp)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Arabic Phrase Box
                    Surface(
                      color = Color(0xFFF8FAFC),
                      shape = RoundedCornerShape(10.dp),
                      border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                      modifier = Modifier.fillMaxWidth()
                    ) {
                      Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                          text = step.arabicPhrase,
                          style = TextStyle(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = RoyalEmerald,
                            fontFamily = FontFamily.Serif,
                            textAlign = TextAlign.Center
                          ),
                          modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                          text = if (selectedLang == "BN") step.pronunciationBn else step.pronunciationEn,
                          style = TextStyle(fontSize = 12.sp, color = Color(0xFF475569), fontWeight = FontWeight.Medium)
                        )
                      }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                      text = if (selectedLang == "BN") "অর্থ: ${step.meaningBn}" else "Meaning: ${step.meaningEn}",
                      style = TextStyle(fontSize = 13.sp, color = Color(0xFF334155), lineHeight = 18.sp)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                      text = if (selectedLang == "BN") "পদ্ধতি: ${step.instructionBn}" else "Guide: ${step.instructionEn}",
                      style = TextStyle(fontSize = 12.sp, color = Color(0xFF64748B), lineHeight = 17.sp)
                    )
                  }
                }
              }

              item { Spacer(modifier = Modifier.height(24.dp)) }
            }
          }

          1 -> {
            LazyColumn(
              verticalArrangement = Arrangement.spacedBy(10.dp),
              modifier = Modifier.fillMaxSize()
            ) {
              item {
                Card(
                  shape = RoundedCornerShape(16.dp),
                  colors = CardDefaults.cardColors(containerColor = Color.White),
                  elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                  border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                  modifier = Modifier.fillMaxWidth()
                ) {
                  Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                      text = when (selectedLang) {
                        "EN" -> "Daily 5 Prayers Rakat Table"
                        "AR" -> "جدول ركعات الصلوات الخمس"
                        else -> "দৈনিক ৫ ওয়াক্তের পূর্ণাঙ্গ রাকাত তালিকা"
                      },
                      style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold, color = RoyalEmerald)
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    SalatRepository.rakatList.forEach { rakat ->
                      Row(
                        modifier = Modifier
                          .fillMaxWidth()
                          .clip(RoundedCornerShape(10.dp))
                          .background(Color(0xFFF8FAFC))
                          .padding(horizontal = 12.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                      ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                          Text(text = rakat.iconEmoji, fontSize = 20.sp)
                          Spacer(modifier = Modifier.width(10.dp))
                          Column {
                            Text(
                              text = if (selectedLang == "BN") rakat.prayerNameBn else rakat.prayerNameEn,
                              style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
                            )
                            val details = buildString {
                              if (rakat.sunnahMuakkadah > 0) append("সুন্নতে মুয়াক্কাদা: ${rakat.sunnahMuakkadah} | ")
                              if (rakat.farz > 0) append("ফরজ: ${rakat.farz} | ")
                              if (rakat.witr > 0) append("বিতর: ${rakat.witr} | ")
                              if (rakat.nafl > 0) append("নফল: ${rakat.nafl}")
                            }.removeSuffix(" | ")
                            Text(
                              text = details,
                              style = TextStyle(fontSize = 11.sp, color = Color(0xFF64748B))
                            )
                          }
                        }

                        Surface(
                          color = RoyalEmerald.copy(alpha = 0.12f),
                          shape = RoundedCornerShape(8.dp)
                        ) {
                          Text(
                            text = "${rakat.totalRakats} রাকাত",
                            style = TextStyle(
                              color = RoyalEmerald,
                              fontWeight = FontWeight.Bold,
                              fontSize = 13.sp
                            ),
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                          )
                        }
                      }
                      Spacer(modifier = Modifier.height(6.dp))
                    }
                  }
                }
              }
              item { Spacer(modifier = Modifier.height(24.dp)) }
            }
          }

          2 -> {
            LazyColumn(
              verticalArrangement = Arrangement.spacedBy(12.dp),
              modifier = Modifier.fillMaxSize()
            ) {
              item {
                Card(
                  shape = RoundedCornerShape(16.dp),
                  colors = CardDefaults.cardColors(containerColor = Color.White),
                  elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                  border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                  modifier = Modifier.fillMaxWidth()
                ) {
                  Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                      text = when (selectedLang) {
                        "EN" -> "Conditions of Valid Prayer (Shurut)"
                        "AR" -> "شروط صحة الصلاة"
                        else -> "নামাজের পূর্বশর্ত (আহকাম ও আরকান)"
                      },
                      style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold, color = RoyalEmerald)
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    val rules = listOf(
                      "১. শরীর পবিত্র হওয়া (ওজু বা গোসল থাকা)" to "Ensure spiritual purity via Wudu or Ghusl.",
                      "২. পরিধেয় কাপড় পবিত্র হওয়া" to "Clean and pure clothing free from Najasa.",
                      "৩. নামাজের স্থান পবিত্র হওয়া" to "Clean prayer spot/mat.",
                      "৪. সতর বা শরীর আবৃত রাখা" to "Covering awrah properly.",
                      "৫. কেবলামুখী হওয়া" to "Facing the Holy Qiblah.",
                      "৬. ওয়াক্ত অনুযায়ী নামাজ পড়া" to "Performing within valid prayer time.",
                      "৭. মনে মনে নিয়ত করা" to "Sincere intention in the heart."
                    )

                    rules.forEach { (bn, en) ->
                      Row(
                        modifier = Modifier
                          .fillMaxWidth()
                          .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                      ) {
                        Icon(
                          imageVector = Icons.Default.Check,
                          contentDescription = "Check",
                          tint = RoyalEmerald,
                          modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                          text = if (selectedLang == "BN") bn else en,
                          style = TextStyle(fontSize = 13.sp, color = Color(0xFF334155), lineHeight = 18.sp)
                        )
                      }
                    }
                  }
                }
              }

              item {
                Card(
                  shape = RoundedCornerShape(16.dp),
                  colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFBEB)),
                  border = BorderStroke(1.dp, Color(0xFFFDE68A)),
                  modifier = Modifier.fillMaxWidth()
                ) {
                  Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                      text = "সাহু সিজদার হুকুম (Sujud as-Sahw)",
                      style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color(0xFFB45309))
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                      text = when (selectedLang) {
                        "EN" -> "If a Wajib is missed inadvertently or delayed, perform Tashahhud in final sitting, turn salam to the right only, make two prostrations, and repeat Tashahhud, Durood, and Salam."
                        else -> "নামাজের কোনো ওয়াজিব ভুলবশত ছুটে গেলে বা বিলম্ব হলে শেষ বৈঠকে তাশাহহুদ পড়ে শুধু ডান দিকে সালাম ফিরিয়ে দুটি অতিরিক্ত সিজদা করতে হয়, এরপর পুনরায় তাশাহহুদ, দরূদ ও দোয়া মাছুরা পড়ে উভয় দিকে সালাম ফিরাতে হয়।"
                      },
                      style = TextStyle(fontSize = 13.sp, color = Color(0xFF78350F), lineHeight = 18.sp)
                    )
                  }
                }
              }

              item { Spacer(modifier = Modifier.height(24.dp)) }
            }
          }
        }
      }
    }
  }
}

/**
 * 2. CATEGORIZED HADITH COLLECTION FULL-SCREEN VIEW
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HadithCollectionScreen(
  selectedLang: String,
  onBack: () -> Unit
) {
  val context = LocalContext.current
  var selectedCategoryKey by remember { mutableStateOf("all") }
  var searchQuery by remember { mutableStateOf("") }

  val categories = listOf(
    "all" to ("সকল হাদিস" to "All Hadiths"),
    "niyyah" to ("ঈমান ও নিয়ত" to "Faith & Intention"),
    "quran" to ("কুরআন শিক্ষা" to "Quran Learning"),
    "salat" to ("সালাত ও তাহারাত" to "Prayer & Purity"),
    "akhlaq" to ("উত্তম চরিত্র" to "Noble Character"),
    "charity" to ("দান ও সদকা" to "Charity & Kindness"),
    "dua" to ("যিকির ও দোয়া" to "Remembrance & Dua")
  )

  val filteredHadiths = remember(selectedCategoryKey, searchQuery) {
    HadithRepository.hadithList.filter { hadith ->
      val matchesCat = selectedCategoryKey == "all" || hadith.categoryKey == selectedCategoryKey
      val matchesSearch = searchQuery.isBlank() ||
        hadith.titleBn.contains(searchQuery, ignoreCase = true) ||
        hadith.titleEn.contains(searchQuery, ignoreCase = true) ||
        hadith.translationBn.contains(searchQuery, ignoreCase = true)
      matchesCat && matchesSearch
    }
  }

  Scaffold(
    containerColor = BackgroundGray,
    topBar = {
      TopAppBar(
        title = {
          Text(
            text = when (selectedLang) {
              "EN" -> "Categorized Hadith Collection"
              "AR" -> "مجموعة الأحاديث النبوية"
              else -> "বিষয়ভিত্তিক সহিহ হাদিস সম্ভার"
            },
            style = TextStyle(
              fontSize = 19.sp,
              fontWeight = FontWeight.Bold,
              color = Color.White
            )
          )
        },
        navigationIcon = {
          IconButton(onClick = onBack) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowBack,
              contentDescription = "Back",
              tint = Color.White
            )
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = RoyalEmerald)
      )
    }
  ) { padding ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(padding)
        .navigationBarsPadding(),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      // Search Bar
      OutlinedTextField(
        value = searchQuery,
        onValueChange = { searchQuery = it },
        placeholder = {
          Text(
            text = if (selectedLang == "BN") "হাদিস বা বিষয় অনুসন্ধান করুন..." else "Search Hadith by title...",
            fontSize = 13.sp,
            color = Color(0xFF94A3B8)
          )
        },
        leadingIcon = {
          Icon(imageVector = Icons.Default.Search, contentDescription = "Search", tint = Color(0xFF64748B))
        },
        trailingIcon = {
          if (searchQuery.isNotEmpty()) {
            IconButton(onClick = { searchQuery = "" }) {
              Icon(imageVector = Icons.Default.Close, contentDescription = "Clear", tint = Color(0xFF64748B))
            }
          }
        },
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
          focusedContainerColor = Color.White,
          unfocusedContainerColor = Color.White,
          focusedBorderColor = RoyalEmerald,
          unfocusedBorderColor = Color(0xFFE2E8F0)
        ),
        modifier = Modifier
          .fillMaxWidth()
          .widthIn(max = 680.dp)
          .padding(horizontal = 16.dp, vertical = 10.dp)
      )

      // Category filter chips row
      LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        items(categories) { (key, labels) ->
          val isSelected = selectedCategoryKey == key
          val label = if (selectedLang == "BN") labels.first else labels.second
          Surface(
            shape = RoundedCornerShape(20.dp),
            color = if (isSelected) RoyalEmerald else Color.White,
            border = BorderStroke(1.dp, if (isSelected) RoyalEmerald else Color(0xFFCBD5E1)),
            modifier = Modifier.clickable { selectedCategoryKey = key }
          ) {
            Text(
              text = label,
              style = TextStyle(
                fontSize = 12.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) Color.White else Color(0xFF475569)
              ),
              modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp)
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        modifier = Modifier
          .fillMaxSize()
          .widthIn(max = 680.dp)
      ) {
        items(filteredHadiths) { hadith ->
          Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(modifier = Modifier.padding(16.dp)) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Surface(
                  color = RoyalEmerald.copy(alpha = 0.12f),
                  shape = RoundedCornerShape(8.dp)
                ) {
                  Text(
                    text = if (selectedLang == "BN") hadith.categoryNameBn else hadith.categoryNameEn,
                    style = TextStyle(
                      fontSize = 11.sp,
                      fontWeight = FontWeight.Bold,
                      color = RoyalEmerald
                    ),
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                  )
                }

                IconButton(
                  onClick = {
                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                      type = "text/plain"
                      putExtra(
                        Intent.EXTRA_TEXT,
                        """
                        📖 ${hadith.titleBn}
                        
                        ${hadith.arabicText}
                        
                        "${hadith.translationBn}"
                        
                        — বর্ণনাকারী: ${hadith.narrator}
                        — সূত্র: ${hadith.sourceReference}
                        
                        হরুফিয়া শরিফ অ্যাপ থেকে সংগৃহীত।
                        """.trimIndent()
                      )
                    }
                    context.startActivity(Intent.createChooser(shareIntent, "শেয়ার করুন"))
                  },
                  modifier = Modifier.size(32.dp)
                ) {
                  Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = "Share",
                    tint = Color(0xFF64748B),
                    modifier = Modifier.size(18.dp)
                  )
                }
              }

              Spacer(modifier = Modifier.height(8.dp))

              Text(
                text = if (selectedLang == "BN") hadith.titleBn else hadith.titleEn,
                style = TextStyle(
                  fontSize = 16.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color(0xFF0F172A)
                )
              )

              Spacer(modifier = Modifier.height(10.dp))

              // Arabic Text Card
              Surface(
                color = Color(0xFFF8FAFC),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                modifier = Modifier.fillMaxWidth()
              ) {
                Text(
                  text = hadith.arabicText,
                  style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = RoyalEmerald,
                    fontFamily = FontFamily.Serif,
                    lineHeight = 28.sp,
                    textAlign = TextAlign.Center
                  ),
                  modifier = Modifier.padding(14.dp)
                )
              }

              Spacer(modifier = Modifier.height(10.dp))

              Text(
                text = if (selectedLang == "BN") hadith.translationBn else hadith.translationEn,
                style = TextStyle(
                  fontSize = 14.sp,
                  color = Color(0xFF334155),
                  lineHeight = 20.sp
                )
              )

              Spacer(modifier = Modifier.height(8.dp))

              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(
                  text = "বর্ণনায়: ${hadith.narrator}",
                  style = TextStyle(fontSize = 12.sp, color = Color(0xFF64748B), fontWeight = FontWeight.Medium)
                )
                Text(
                  text = hadith.sourceReference,
                  style = TextStyle(fontSize = 11.sp, color = RoyalEmerald, fontWeight = FontWeight.Bold)
                )
              }
            }
          }
        }

        item { Spacer(modifier = Modifier.height(24.dp)) }
      }
    }
  }
}

/**
 * 3. DAILY DUA MODAL DIALOG
 */
@Composable
fun DailyDuaDialog(
  dua: DailyDuaItem,
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
            Text(text = "🤲", fontSize = 24.sp)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = if (selectedLang == "BN") dua.titleBn else dua.titleEn,
              style = TextStyle(fontSize = 17.sp, fontWeight = FontWeight.Bold, color = RoyalEmerald)
            )
          }
          IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
            Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = Color(0xFF64748B))
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Surface(
          color = Color(0xFFF1F5F9),
          shape = RoundedCornerShape(12.dp),
          border = BorderStroke(1.dp, RoyalEmerald.copy(alpha = 0.2f)),
          modifier = Modifier.fillMaxWidth()
        ) {
          Text(
            text = dua.arabicText,
            style = TextStyle(
              fontSize = 20.sp,
              fontWeight = FontWeight.Bold,
              color = RoyalEmerald,
              fontFamily = FontFamily.Serif,
              textAlign = TextAlign.Center,
              lineHeight = 30.sp
            ),
            modifier = Modifier.padding(16.dp)
          )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
          text = "উচ্চারণ: ${dua.pronunciationBn}",
          style = TextStyle(fontSize = 13.sp, color = Color(0xFF475569), fontWeight = FontWeight.Medium)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
          text = "অর্থ: ${dua.translationBn}",
          style = TextStyle(fontSize = 14.sp, color = Color(0xFF1E293B), lineHeight = 20.sp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        Surface(
          color = Color(0xFFECFDF5),
          shape = RoundedCornerShape(10.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Text(
            text = "ফজিলত: ${dua.virtue}",
            style = TextStyle(fontSize = 12.sp, color = Color(0xFF047857), lineHeight = 17.sp),
            modifier = Modifier.padding(10.dp)
          )
        }

        Spacer(modifier = Modifier.height(14.dp))

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

/**
 * 4. VIRTUOUS DEED MODAL DIALOG
 */
@Composable
fun VirtuousDeedDialog(
  deed: VirtuousDeedItem,
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
            Text(text = deed.iconEmoji, fontSize = 24.sp)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = if (selectedLang == "BN") deed.titleBn else deed.titleEn,
              style = TextStyle(fontSize = 17.sp, fontWeight = FontWeight.Bold, color = RoyalEmerald)
            )
          }
          IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
            Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = Color(0xFF64748B))
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
          text = deed.descriptionBn,
          style = TextStyle(fontSize = 14.sp, color = Color(0xFF1E293B), lineHeight = 20.sp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Surface(
          color = Color(0xFFF8FAFC),
          shape = RoundedCornerShape(10.dp),
          border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(12.dp)) {
            Text(
              text = "হাদিসের প্রমাণ:",
              style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold, color = RoyalEmerald)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = deed.hadithProofBn,
              style = TextStyle(fontSize = 13.sp, color = Color(0xFF475569), lineHeight = 18.sp)
            )
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Surface(
          color = Color(0xFFECFDF5),
          shape = RoundedCornerShape(10.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Text(
            text = "পুরস্কার: ${deed.rewardBn}",
            style = TextStyle(fontSize = 12.sp, color = Color(0xFF047857), lineHeight = 17.sp),
            modifier = Modifier.padding(10.dp)
          )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
          onClick = onDismiss,
          colors = ButtonDefaults.buttonColors(containerColor = RoyalEmerald),
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Text("আমল করার নিয়ত করলাম", fontWeight = FontWeight.Bold)
        }
      }
    }
  }
}

/**
 * 5. FULL PRAYER TIMETABLE MODAL DIALOG
 */
@Composable
fun FullTimetableDialog(
  prayerSchedule: DailyPrayerSchedule,
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
            Text(text = "🕌", fontSize = 24.sp)
            Spacer(modifier = Modifier.width(8.dp))
            Column {
              Text(
                text = if (selectedLang == "BN") "পূর্ণাঙ্গ নামাজের সময়সূচি" else "Full Prayer Timetable",
                style = TextStyle(fontSize = 17.sp, fontWeight = FontWeight.Bold, color = RoyalEmerald)
              )
              Text(
                text = prayerSchedule.locationName,
                style = TextStyle(fontSize = 12.sp, color = Color(0xFF64748B))
              )
            }
          }
          IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
            Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = Color(0xFF64748B))
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Sahri and Iftar Highlight Cards
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Surface(
            color = Color(0xFFEFF6FF),
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, Color(0xFFBFDBFE)),
            modifier = Modifier.weight(1f)
          ) {
            Column(
              modifier = Modifier.padding(10.dp),
              horizontalAlignment = Alignment.CenterHorizontally
            ) {
              Text(text = "🌙 শেষ সেহরি", fontSize = 12.sp, color = Color(0xFF1E40AF), fontWeight = FontWeight.Bold)
              Spacer(modifier = Modifier.height(3.dp))
              Text(
                text = prayerSchedule.sahriEndFormatted,
                style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E3A8A))
              )
            }
          }

          Surface(
            color = Color(0xFFFFF7ED),
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, Color(0xFFFED7AA)),
            modifier = Modifier.weight(1f)
          ) {
            Column(
              modifier = Modifier.padding(10.dp),
              horizontalAlignment = Alignment.CenterHorizontally
            ) {
              Text(text = "🌇 ইফতারের সময়", fontSize = 12.sp, color = Color(0xFFC2410C), fontWeight = FontWeight.Bold)
              Spacer(modifier = Modifier.height(3.dp))
              Text(
                text = prayerSchedule.iftarFormatted,
                style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color(0xFF9A3412))
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // All 5 waqts + Sunrise + Tahajjud
        for (entry in prayerSchedule.allPrayers()) {
          val isCurrent = entry.id == prayerSchedule.currentPrayer.id
          val isNext = entry.id == prayerSchedule.nextPrayer.id

          Row(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(10.dp))
              .background(
                if (isCurrent) RoyalEmerald.copy(alpha = 0.12f)
                else if (isNext) Color(0xFFF1F5F9)
                else Color.White
              )
              .border(
                1.dp,
                if (isCurrent) RoyalEmerald else Color(0xFFE2E8F0),
                RoundedCornerShape(10.dp)
              )
              .padding(horizontal = 12.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(text = entry.iconEmoji, fontSize = 20.sp)
              Spacer(modifier = Modifier.width(10.dp))
              Column {
                Text(
                  text = if (selectedLang == "BN") entry.nameBn else entry.nameEn,
                  style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = if (isCurrent || isNext) FontWeight.Bold else FontWeight.Medium,
                    color = if (isCurrent) RoyalEmerald else Color(0xFF1E293B)
                  )
                )
                if (isCurrent) {
                  Text(
                    text = if (selectedLang == "BN") "চলমান ওয়াক্ত" else "Current Waqt",
                    style = TextStyle(fontSize = 10.sp, color = RoyalEmerald, fontWeight = FontWeight.Bold)
                  )
                } else if (isNext) {
                  Text(
                    text = if (selectedLang == "BN") "পরবর্তী ওয়াক্ত" else "Next Prayer",
                    style = TextStyle(fontSize = 10.sp, color = Color(0xFF2563EB), fontWeight = FontWeight.Bold)
                  )
                }
              }
            }

            Text(
              text = entry.timeFormatted,
              style = TextStyle(
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = if (isCurrent) RoyalEmerald else Color(0xFF1E293B)
              )
            )
          }
          Spacer(modifier = Modifier.height(6.dp))
        }

        Spacer(modifier = Modifier.height(14.dp))

        Button(
          onClick = onDismiss,
          colors = ButtonDefaults.buttonColors(containerColor = RoyalEmerald),
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Text("বন্ধ করুন", fontWeight = FontWeight.Bold)
        }
      }
    }
  }
}
