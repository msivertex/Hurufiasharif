package com.example

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.setValue
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Offline Data Caching Repository for Game & Learning Progression.
 * Fully persists unlocked roadmap levels, star ratings, user coins, XP, level, and streaks.
 * Operates 100% offline with instant SharedPreferences backing.
 */
class OfflineProgressRepository private constructor(context: Context) {
  private val prefs: SharedPreferences =
    context.applicationContext.getSharedPreferences("hurufia_game_progress", Context.MODE_PRIVATE)

  // Unlocked levels state map (levelId -> isUnlocked)
  val unlockedLevels = mutableStateMapOf<Int, Boolean>()

  // Level star ratings state map (levelId -> starCount: 1..3)
  val levelStars = mutableStateMapOf<Int, Int>()

  var userCoins by mutableIntStateOf(250)
    private set
  var userLevel by mutableIntStateOf(1)
    private set
  var userStreak by mutableIntStateOf(3)
    private set
  var userXp by mutableIntStateOf(120)
    private set

  init {
    loadProgress()
  }

  fun loadProgress() {
    // Default level 1 is always unlocked
    unlockedLevels.clear()
    unlockedLevels[1] = true

    // Load unlocked levels (format: comma-separated "1,2,3")
    val unlockedString = prefs.getString("unlocked_levels", "1") ?: "1"
    unlockedString.split(",").forEach { idStr ->
      idStr.trim().toIntOrNull()?.let { id ->
        unlockedLevels[id] = true
      }
    }

    // Load stars (keys prefixed with "level_stars_")
    levelStars.clear()
    for (i in 1..35) {
      val stars = prefs.getInt("level_stars_$i", 0)
      if (stars > 0) {
        levelStars[i] = stars
      }
    }

    userCoins = prefs.getInt("user_coins", 250)
    userLevel = prefs.getInt("user_level", 1)
    userXp = prefs.getInt("user_xp", 120)

    checkAndMaintainStreak()
  }

  fun completeLevel(levelId: Int, starsEarned: Int, xpEarned: Int, coinsEarned: Int) {
    // Record stars (keep best score)
    val existingStars = levelStars[levelId] ?: 0
    val newStars = maxOf(existingStars, starsEarned)
    levelStars[levelId] = newStars
    prefs.edit().putInt("level_stars_$levelId", newStars).apply()

    // Unlock next level
    val nextLevelId = levelId + 1
    unlockedLevels[nextLevelId] = true
    saveUnlockedLevels()

    // Award rewards
    userCoins += coinsEarned
    userXp += xpEarned
    userLevel = 1 + (userCoins / 300)

    prefs.edit()
      .putInt("user_coins", userCoins)
      .putInt("user_xp", userXp)
      .putInt("user_level", userLevel)
      .apply()

    recordActivityToday()
  }

  fun unlockAllLevelsForTesting() {
    for (i in 1..35) {
      unlockedLevels[i] = true
    }
    saveUnlockedLevels()
  }

  fun addCoins(amount: Int) {
    userCoins += amount
    userLevel = 1 + (userCoins / 300)
    prefs.edit()
      .putInt("user_coins", userCoins)
      .putInt("user_level", userLevel)
      .apply()
    recordActivityToday()
  }

  private fun saveUnlockedLevels() {
    val unlockedString = unlockedLevels.filter { it.value }.keys.sorted().joinToString(",")
    prefs.edit().putString("unlocked_levels", unlockedString).apply()
  }

  private fun recordActivityToday() {
    val today = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
    val lastDate = prefs.getString("last_active_date", "")

    if (lastDate != today) {
      userStreak += 1
      prefs.edit()
        .putString("last_active_date", today)
        .putInt("user_streak", userStreak)
        .apply()
    }
  }

  private fun checkAndMaintainStreak() {
    userStreak = prefs.getInt("user_streak", 3)
    val lastDateStr = prefs.getString("last_active_date", null) ?: return
    try {
      val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
      val lastDate = sdf.parse(lastDateStr) ?: return
      val now = Date()
      val diffDays = ((now.time - lastDate.time) / (1000 * 60 * 60 * 24)).toInt()
      if (diffDays > 1) {
        userStreak = 1
        prefs.edit().putInt("user_streak", userStreak).apply()
      }
    } catch (_: Exception) {
    }
  }

  companion object {
    @Volatile
    private var INSTANCE: OfflineProgressRepository? = null

    fun getInstance(context: Context): OfflineProgressRepository {
      return INSTANCE ?: synchronized(this) {
        INSTANCE ?: OfflineProgressRepository(context).also { INSTANCE = it }
      }
    }
  }
}
