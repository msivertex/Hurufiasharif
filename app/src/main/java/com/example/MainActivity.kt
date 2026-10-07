package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BackgroundGray
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.RoyalEmerald

import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.firebase.FirebaseAuthManager
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    try {
      if (com.google.firebase.FirebaseApp.getApps(this).isEmpty()) {
        com.google.firebase.FirebaseApp.initializeApp(this)
      }
    } catch (e: Throwable) {
      android.util.Log.w("MainActivity", "FirebaseApp init warning", e)
    }
    enableEdgeToEdge()
    setContent {
      val context = androidx.compose.ui.platform.LocalContext.current
      val soundManager = remember { SoundManager.getInstance(context) }
      MyApplicationTheme(darkTheme = soundManager.darkModeEnabledState) {
        AuthScreen()
      }
    }
  }
}

/**
 * Android Jetpack Compose Auth Gate with Google Sign-In via Credential Manager & Firebase Auth.
 * Gates application UI behind authenticated user session or guest exploration mode.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen() {
  val context = androidx.compose.ui.platform.LocalContext.current
  val coroutineScope = androidx.compose.runtime.rememberCoroutineScope()
  val authManager = remember { FirebaseAuthManager.getInstance() }
  val currentUser by authManager.authStateFlow.collectAsStateWithLifecycle(initialValue = authManager.currentUser)

  var selectedLang by remember {
    mutableStateOf(AuthSessionManager.getSelectedLanguage(context, "BN"))
  }
  var showLanguageMenu by remember { mutableStateOf(false) }
  var isSigningIn by remember { mutableStateOf(false) }
  var isGuestMode by remember {
    mutableStateOf(AuthSessionManager.isGuestSession(context))
  }
  var errorMessage by remember { mutableStateOf<String?>(null) }

  // Automatically request Location Permission on app startup
  val locationPermissionLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.RequestMultiplePermissions()
  ) { _ -> }

  LaunchedEffect(Unit) {
    locationPermissionLauncher.launch(
      arrayOf(
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.ACCESS_COARSE_LOCATION
      )
    )
  }

  // Attempt silent sign-in on cold start if previously authorized
  LaunchedEffect(Unit) {
    if (authManager.currentUser == null && !isGuestMode) {
      authManager.trySilentSignIn(context)
    }
  }

  // If a real Firebase user logs in, persist session locally
  val user = currentUser
  LaunchedEffect(user) {
    if (user != null) {
      AuthSessionManager.saveUserSession(context, user.email, user.uid)
      isGuestMode = false
    }
  }

  val layoutDirection = if (selectedLang == "AR") LayoutDirection.Rtl else LayoutDirection.Ltr

  val hasPersistedSession = AuthSessionManager.hasActiveSession(context)
  val isSessionActive = user != null || isGuestMode || hasPersistedSession

  if (isSessionActive) {
    val activeEmail = user?.email?.ifBlank { null }
      ?: AuthSessionManager.getSavedUserEmail(context).ifBlank { null }
      ?: "guest@hurufia.com"
    val activeUid = user?.uid?.ifBlank { null }
      ?: AuthSessionManager.getSavedUserUid(context).ifBlank { null }
      ?: "guest_user"

    HomeDashboardScreen(
      selectedLang = selectedLang,
      onLanguageChange = {
        selectedLang = it
        AuthSessionManager.saveSelectedLanguage(context, it)
      },
      userEmail = activeEmail,
      currentUserId = activeUid,
      onSignOut = {
        authManager.signOut()
        AuthSessionManager.clearSession(context)
        isGuestMode = false
        errorMessage = null
      }
    )
  } else {
    CompositionLocalProvider(LocalLayoutDirection provides layoutDirection) {
      Scaffold(
        containerColor = BackgroundGray, // Color(0xFFF4F6F8)
        topBar = {
          TopAppBar(
            title = {
              Text(
                text = AppStrings.getAppName(selectedLang),
                style = TextStyle(
                  fontWeight = FontWeight.Bold,
                  color = Color.White,
                  fontSize = 20.sp
                )
              )
            },
            colors = TopAppBarDefaults.topAppBarColors(
              containerColor = RoyalEmerald // Color(0xFF0A5C36) - রয়াল এমারেল্ড গ্রিন
            ),
            actions = {
              // ভাষা বদলানোর ড্রপডাউন (DropdownButton<String>)
              Box {
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { showLanguageMenu = true }
                    .padding(horizontal = 8.dp, vertical = 6.dp)
                    .testTag("language_dropdown_trigger")
                ) {
                  Icon(
                    imageVector = Icons.Default.Language,
                    contentDescription = "Language",
                    tint = Color.White
                  )
                  Spacer(modifier = Modifier.width(6.dp))
                  Text(
                    text = selectedLang,
                    style = TextStyle(
                      color = Color.White,
                      fontWeight = FontWeight.Bold,
                      fontSize = 16.sp
                    )
                  )
                }

                DropdownMenu(
                  expanded = showLanguageMenu,
                  onDismissRequest = { showLanguageMenu = false },
                  modifier = Modifier
                    .background(RoyalEmerald)
                    .testTag("language_dropdown_menu")
                ) {
                  listOf("BN", "EN", "AR").forEach { lang ->
                    val label = when (lang) {
                      "BN" -> "BN (বাংলা)"
                      "AR" -> "AR (العربية)"
                      else -> "EN (English)"
                    }
                    DropdownMenuItem(
                      text = {
                        Row(
                          modifier = Modifier.fillMaxWidth(),
                          horizontalArrangement = Arrangement.SpaceBetween,
                          verticalAlignment = Alignment.CenterVertically
                        ) {
                          Text(
                            text = label,
                            style = TextStyle(
                              color = Color.White,
                              fontWeight = FontWeight.Bold,
                              fontSize = 15.sp
                            )
                          )
                          if (selectedLang == lang) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(
                              imageVector = Icons.Default.Check,
                              contentDescription = "Selected",
                              tint = Color.White,
                              modifier = Modifier.size(18.dp)
                            )
                          }
                        }
                      },
                      onClick = {
                        selectedLang = lang
                        showLanguageMenu = false
                      },
                      modifier = Modifier.testTag("lang_option_$lang")
                    )
                  }
                }
              }

              Spacer(modifier = Modifier.width(15.dp))
            }
          )
        }
      ) { innerPadding ->
        Box(
          modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding(),
          contentAlignment = Alignment.Center
        ) {
          Column(
            modifier = Modifier
              .widthIn(max = 480.dp)
              .fillMaxWidth()
              .verticalScroll(rememberScrollState())
              .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
          ) {
            // Hurufia Sharif Logo
            Box(
              modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .border(2.5.dp, RoyalEmerald, CircleShape)
                .shadow(6.dp, CircleShape),
              contentAlignment = Alignment.Center
            ) {
              Image(
                painter = painterResource(id = R.drawable.ic_hurufia_logo),
                contentDescription = "Hurufia Sharif Logo",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
              )
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Title & Description
            Text(
              text = AppStrings.getAppName(selectedLang),
              style = TextStyle(
                fontSize = 26.sp,
                fontWeight = FontWeight.ExtraBold,
                color = RoyalEmerald
              ),
              textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
              text = when (selectedLang) {
                "EN" -> "Authentic Arabic Learning & Tajweed with Cloud Sync"
                "AR" -> "تعلم الحروف والتجويد مع المزامنة السحابية"
                else -> "সহিহ কুরআন ও আরবি শিক্ষা • ক্লাউড প্রগ্রেস সিঙ্ক"
              },
              style = TextStyle(
                fontSize = 13.5.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF64748B)
              ),
              textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Firebase Cloud Features Info Card
            Surface(
              shape = RoundedCornerShape(16.dp),
              color = Color.White,
              border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
              shadowElevation = 2.dp,
              modifier = Modifier.fillMaxWidth()
            ) {
              Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Surface(
                    shape = CircleShape,
                    color = Color(0xFFFEF3C7),
                    modifier = Modifier.size(32.dp)
                  ) {
                    Box(contentAlignment = Alignment.Center) {
                      Text(text = "🔥", fontSize = 16.sp)
                    }
                  }
                  Spacer(modifier = Modifier.width(10.dp))
                  Column {
                    Text(
                      text = when (selectedLang) {
                        "EN" -> "Firebase Cloud Firestore"
                        "AR" -> "قاعدة بيانات فايربيس السحابية"
                        else -> "ফায়ারবেস ক্লাউড স্টোরেজ"
                      },
                      style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                    )
                    Text(
                      text = when (selectedLang) {
                        "EN" -> "Secure Google Sign-In & Live Data Sync"
                        "AR" -> "تسجيل دخول آمن ومزامنة تلقائية"
                        else -> "নিরাপদ গুগল সাইন-ইন ও স্বয়ংক্রিয় ক্লাউড ব্যাকআপ"
                      },
                      style = TextStyle(fontSize = 11.sp, color = Color(0xFF64748B))
                    )
                  }
                }
              }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Validation or sign-in error display
            AnimatedVisibility(
              visible = errorMessage != null,
              enter = fadeIn(),
              exit = fadeOut()
            ) {
              errorMessage?.let { msg ->
                Surface(
                  shape = RoundedCornerShape(12.dp),
                  color = Color(0xFFFEF2F2),
                  border = BorderStroke(1.dp, Color(0xFFFCA5A5)),
                  modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                ) {
                  Text(
                    text = msg,
                    color = Color(0xFFDC2626),
                    style = TextStyle(fontSize = 12.5.sp, fontWeight = FontWeight.Medium),
                    modifier = Modifier.padding(12.dp),
                    textAlign = TextAlign.Center
                  )
                }
              }
            }

            // Primary Google Sign-In Button (Mandatory Phase 1 Invariant)
            Button(
              onClick = {
                if (!isSigningIn) {
                  coroutineScope.launch {
                    isSigningIn = true
                    errorMessage = null
                    val result = authManager.signInWithGoogle(context)
                    isSigningIn = false
                    if (result.isFailure) {
                      val exception = result.exceptionOrNull()
                      if (exception !is androidx.credentials.exceptions.GetCredentialCancellationException) {
                        errorMessage = exception?.localizedMessage ?: "Google Sign-In failed"
                      }
                    }
                  }
                }
              },
              shape = RoundedCornerShape(14.dp),
              colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color(0xFF1F2937)
              ),
              border = BorderStroke(1.5.dp, Color(0xFFCBD5E1)),
              elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp, pressedElevation = 4.dp),
              modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .testTag("google_sign_in_button")
            ) {
              if (isSigningIn) {
                androidx.compose.material3.CircularProgressIndicator(
                  modifier = Modifier.size(22.dp),
                  color = RoyalEmerald,
                  strokeWidth = 2.5.dp
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                  text = when (selectedLang) {
                    "EN" -> "Signing in with Google..."
                    "AR" -> "جارٍ تسجيل الدخول..."
                    else -> "গুগল সাইন-ইন হচ্ছে..."
                  },
                  style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF374151))
                )
              } else {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  // Google 'G' Icon
                  androidx.compose.foundation.Canvas(modifier = Modifier.size(22.dp)) {
                    val w = size.width
                    val h = size.height
                    drawCircle(
                      color = Color(0xFF4285F4),
                      radius = w * 0.45f,
                      center = androidx.compose.ui.geometry.Offset(w / 2f, h / 2f)
                    )
                  }
                  Spacer(modifier = Modifier.width(12.dp))
                  Text(
                    text = when (selectedLang) {
                      "EN" -> "Sign in with Google"
                      "AR" -> "تسجيل الدخول عبر Google"
                      else -> "Google দিয়ে সাইন ইন করুন"
                    },
                    style = TextStyle(
                      fontSize = 15.sp,
                      fontWeight = FontWeight.Bold,
                      color = Color(0xFF1F2937)
                    )
                  )
                }
              }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Guest Mode / Instant Preview Button
            OutlinedButton(
              onClick = {
                AuthSessionManager.setGuestSession(context, true)
                isGuestMode = true
              },
              shape = RoundedCornerShape(14.dp),
              colors = ButtonDefaults.outlinedButtonColors(
                contentColor = RoyalEmerald
              ),
              border = BorderStroke(1.5.dp, RoyalEmerald),
              modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("guest_explore_button")
            ) {
              Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                modifier = Modifier.size(18.dp),
                tint = RoyalEmerald
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = when (selectedLang) {
                  "EN" -> "Continue as Guest"
                  "AR" -> "الدخول كضيف"
                  else -> "গেস্ট হিসেবে ব্যবহার করুন"
                },
                style = TextStyle(
                  fontSize = 14.5.sp,
                  fontWeight = FontWeight.Bold,
                  color = RoyalEmerald
                )
              )
            }
          }
        }
      }
    }
  }
}

/**
 * হোম পেজ (হোম স্ক্রিন)
 */
@Composable
fun HomeScreen(
  selectedLang: String,
  userEmail: String,
  onSignOut: () -> Unit
) {
  Card(
    shape = RoundedCornerShape(20.dp),
    colors = CardDefaults.cardColors(
      containerColor = Color.White
    ),
    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    modifier = Modifier
      .padding(24.dp)
      .widthIn(max = 480.dp)
      .fillMaxWidth()
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(24.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Icon(
        imageVector = Icons.Default.CheckCircle,
        contentDescription = "Success",
        tint = RoyalEmerald,
        modifier = Modifier.size(56.dp)
      )

      Spacer(modifier = Modifier.height(16.dp))

      Text(
        text = AppStrings.getAppName(selectedLang),
        style = TextStyle(
          fontSize = 22.sp,
          fontWeight = FontWeight.Bold,
          color = RoyalEmerald
        ),
        textAlign = TextAlign.Center
      )

      Spacer(modifier = Modifier.height(8.dp))

      Text(
        text = when (selectedLang) {
          "EN" -> "Welcome to the Home Screen!"
          "AR" -> "مرحباً بك في الشاشة الرئيسية!"
          else -> "হোম স্ক্রিনে আপনাকে স্বাগতম!"
        },
        style = TextStyle(
          fontSize = 18.sp,
          fontWeight = FontWeight.Medium,
          color = Color(0xFF1E293B)
        ),
        textAlign = TextAlign.Center
      )

      Spacer(modifier = Modifier.height(4.dp))

      Text(
        text = userEmail,
        style = TextStyle(
          fontSize = 14.sp,
          color = Color(0xFF64748B)
        )
      )

      Spacer(modifier = Modifier.height(24.dp))

      Button(
        onClick = onSignOut,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
          containerColor = RoyalEmerald
        ),
        modifier = Modifier
          .fillMaxWidth()
          .height(48.dp)
      ) {
        Icon(
          imageVector = Icons.AutoMirrored.Filled.Logout,
          contentDescription = "Sign Out",
          tint = Color.White,
          modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = when (selectedLang) {
            "EN" -> "Sign Out"
            "AR" -> "تسجيل الخروج"
            else -> "লগআউট"
          },
          style = TextStyle(
            fontWeight = FontWeight.Bold,
            color = Color.White
          )
        )
      }
    }
  }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
  Text(text = "Hello $name!", modifier = modifier)
}

@Preview(showBackground = true)
@Composable
fun AuthScreenPreview() {
  MyApplicationTheme {
    AuthScreen()
  }
}


