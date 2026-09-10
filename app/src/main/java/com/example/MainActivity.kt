package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        AuthScreen()
      }
    }
  }
}

/**
 * Android Jetpack Compose implementation of AuthScreen based on the Flutter specification.
 * - Background: Color(0xFFF4F6F8)
 * - AppBar: Color(0xFF0A5C36) (Royal Emerald Green)
 * - Language Dropdown: ['BN', 'EN', 'AR'] (Default 'BN')
 * - Title: AppStrings.getLoginTitle(selectedLang) with Color(0xFF0A5C36)
 * - Email Input: AppStrings.getEmailHint(selectedLang) with 12.dp radius & email icon
 * - Password Input: AppStrings.getPasswordHint(selectedLang) with 12.dp radius & lock icon
 * - Submit Button: AppStrings.getSubmitBtn(selectedLang) with 50.dp height & Color(0xFF0A5C36)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen() {
  // ১. ডিফল্ট ভাষা বাংলা ('BN')
  var selectedLang by remember { mutableStateOf("BN") }
  var email by remember { mutableStateOf("user@hurufia.com") }
  var password by remember { mutableStateOf("") }
  var passwordVisible by remember { mutableStateOf(false) }
  var showLanguageMenu by remember { mutableStateOf(false) }
  var isLoggedIn by remember { mutableStateOf(true) }
  var errorMessage by remember { mutableStateOf<String?>(null) }

  val focusManager = LocalFocusManager.current
  val layoutDirection = if (selectedLang == "AR") LayoutDirection.Rtl else LayoutDirection.Ltr

  if (isLoggedIn) {
    HomeDashboardScreen(
      selectedLang = selectedLang,
      onLanguageChange = { selectedLang = it },
      userEmail = email.ifBlank { "user@hurufia.com" },
      onSignOut = {
        isLoggedIn = false
        password = ""
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
          // Auth Screen Body: Padding(all: 24.0) -> Center -> SingleChildScrollView -> Column
          Column(
            modifier = Modifier
              .widthIn(max = 480.dp)
              .fillMaxWidth()
              .verticalScroll(rememberScrollState())
              .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
          ) {
            // Optional logo emblem representing Hurufia Sharif
            Box(
              modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .border(2.dp, RoyalEmerald, CircleShape)
                .shadow(4.dp, CircleShape),
              contentAlignment = Alignment.Center
            ) {
              Image(
                painter = painterResource(id = R.drawable.ic_hurufia_logo),
                contentDescription = "Hurufia Sharif Logo",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
              )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // টাইটেল: AppStrings.getLoginTitle(selectedLang)
            Text(
              text = AppStrings.getLoginTitle(selectedLang),
              style = TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = RoyalEmerald // const Color(0xFF0A5C36)
              ),
              textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(30.dp))

            // ইমেইল ইনপুট বক্স
            OutlinedTextField(
              value = email,
              onValueChange = {
                email = it
                errorMessage = null
              },
              label = { Text(AppStrings.getEmailHint(selectedLang)) },
              placeholder = { Text(AppStrings.getEmailHint(selectedLang)) },
              shape = RoundedCornerShape(12.dp),
              leadingIcon = {
                Icon(
                  imageVector = Icons.Default.Email,
                  contentDescription = "Email",
                  tint = RoyalEmerald // const Color(0xFF0A5C36)
                )
              },
              colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = RoyalEmerald,
                unfocusedBorderColor = Color(0xFFCBD5E1),
                focusedLabelColor = RoyalEmerald,
                cursorColor = RoyalEmerald
              ),
              singleLine = true,
              keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
              ),
              modifier = Modifier
                .fillMaxWidth()
                .testTag("email_input")
            )

            Spacer(modifier = Modifier.height(15.dp))

            // পাসওয়ার্ড ইনপুট বক্স
            OutlinedTextField(
              value = password,
              onValueChange = {
                password = it
                errorMessage = null
              },
              label = { Text(AppStrings.getPasswordHint(selectedLang)) },
              placeholder = { Text(AppStrings.getPasswordHint(selectedLang)) },
              shape = RoundedCornerShape(12.dp),
              leadingIcon = {
                Icon(
                  imageVector = Icons.Default.Lock,
                  contentDescription = "Password",
                  tint = RoyalEmerald // const Color(0xFF0A5C36)
                )
              },
              trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                  Icon(
                    imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                    contentDescription = if (passwordVisible) "Hide Password" else "Show Password",
                    tint = RoyalEmerald.copy(alpha = 0.7f)
                  )
                }
              },
              visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
              colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = RoyalEmerald,
                unfocusedBorderColor = Color(0xFFCBD5E1),
                focusedLabelColor = RoyalEmerald,
                cursorColor = RoyalEmerald
              ),
              singleLine = true,
              keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
              ),
              keyboardActions = KeyboardActions(
                onDone = { focusManager.clearFocus() }
              ),
              modifier = Modifier
                .fillMaxWidth()
                .testTag("password_input")
            )

            // Validation message if any
            AnimatedVisibility(
              visible = errorMessage != null,
              enter = fadeIn(),
              exit = fadeOut()
            ) {
              errorMessage?.let { msg ->
                Text(
                  text = msg,
                  color = MaterialTheme.colorScheme.error,
                  style = MaterialTheme.typography.bodySmall,
                  modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, start = 4.dp)
                )
              }
            }

            Spacer(modifier = Modifier.height(25.dp))

            // প্রবেশ বাটন: SizedBox(width: double.infinity, height: 50)
            Button(
              onClick = {
                focusManager.clearFocus()
                if (email.isBlank()) {
                  errorMessage = when (selectedLang) {
                    "EN" -> "Please enter your email"
                    "AR" -> "يرجى إدخال البريد الإلكتروني"
                    else -> "দয়া করে আপনার ইমেইল প্রদান করুন"
                  }
                  return@Button
                }
                // এখানে ক্লিক করলে হোম পেজে চলে যাবে
                isLoggedIn = true
              },
              shape = RoundedCornerShape(12.dp),
              colors = ButtonDefaults.buttonColors(
                containerColor = RoyalEmerald // const Color(0xFF0A5C36)
              ),
              modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .testTag("submit_button")
            ) {
              Text(
                text = AppStrings.getSubmitBtn(selectedLang),
                style = TextStyle(
                  fontSize = 18.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color.White
                )
              )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Demo helper button for rapid testing
            TextButton(
              onClick = {
                email = "user@hurufia.com"
                password = "Password123"
                errorMessage = null
              },
              modifier = Modifier.testTag("demo_button")
            ) {
              Text(
                text = when (selectedLang) {
                  "EN" -> "Quick Fill (Demo Account)"
                  "AR" -> "ملء تلقائي (حساب تجريبي)"
                  else -> "ডেমো তথ্য দিয়ে চেষ্টা করুন"
                },
                color = RoyalEmerald,
                fontWeight = FontWeight.SemiBold
              )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Direct exploration shortcut button: 'পরবর্তী' / 'Next'
            OutlinedButton(
              onClick = {
                email = "user@hurufia.com"
                isLoggedIn = true
              },
              shape = RoundedCornerShape(12.dp),
              border = BorderStroke(1.dp, RoyalEmerald.copy(alpha = 0.5f)),
              modifier = Modifier
                .fillMaxWidth()
                .testTag("skip_to_home_button")
            ) {
              Text(
                text = when (selectedLang) {
                  "EN" -> "Next: Explore Home Dashboard ➔"
                  "AR" -> "التالي: استكشف لوحة التحكم ➔"
                  else -> "পরবর্তী: হোম ড্যাশবোর্ড দেখুন ➔"
                },
                color = RoyalEmerald,
                fontWeight = FontWeight.Bold
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


