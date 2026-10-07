package com.example.firebase

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue

/**
 * User Profile Firestore entity stored at /users/{userId}
 */
data class UserProfile(
  val userId: String = "",
  val email: String = "",
  val displayName: String = "",
  val coins: Long = 0L,
  val level: Long = 1L,
  val streak: Long = 0L,
  val selectedLanguage: String = "BN",
  val selectedQariVoice: String = "MALE_QARI",
  val createdAt: Timestamp? = null,
  val lastActive: Timestamp? = null
) {
  fun toMap(useServerTimestamps: Boolean = true): Map<String, Any> {
    val map = mutableMapOf<String, Any>(
      "userId" to userId,
      "email" to email,
      "displayName" to displayName,
      "coins" to coins,
      "level" to level,
      "streak" to streak,
      "selectedLanguage" to selectedLanguage,
      "selectedQariVoice" to selectedQariVoice
    )
    if (useServerTimestamps) {
      if (createdAt == null) {
        map["createdAt"] = FieldValue.serverTimestamp()
      } else {
        map["createdAt"] = createdAt
      }
      map["lastActive"] = FieldValue.serverTimestamp()
    } else {
      createdAt?.let { map["createdAt"] = it }
      lastActive?.let { map["lastActive"] = it }
    }
    return map
  }
}

/**
 * Kaida chapter progress stored at /users/{userId}/progress/{chapterId}
 */
data class ChapterProgress(
  val userId: String = "",
  val chapterId: String = "",
  val unlocked: Boolean = false,
  val completed: Boolean = false,
  val completedPages: Long = 0L,
  val totalPages: Long = 0L,
  val stars: Long = 0L,
  val updatedAt: Timestamp? = null
) {
  fun toMap(useServerTimestamp: Boolean = true): Map<String, Any> {
    val map = mutableMapOf<String, Any>(
      "userId" to userId,
      "chapterId" to chapterId,
      "unlocked" to unlocked,
      "completed" to completed,
      "completedPages" to completedPages,
      "totalPages" to totalPages,
      "stars" to stars
    )
    if (useServerTimestamp) {
      map["updatedAt"] = FieldValue.serverTimestamp()
    } else {
      updatedAt?.let { map["updatedAt"] = it }
    }
    return map
  }
}

/**
 * Surah memorization progress stored at /users/{userId}/surah_progress/{surahNumber}
 */
data class SurahProgress(
  val userId: String = "",
  val surahNumber: Long = 1L,
  val surahName: String = "",
  val completedVerses: Long = 0L,
  val totalVerses: Long = 0L,
  val isMemorized: Boolean = false,
  val lastPracticed: Timestamp? = null
) {
  fun toMap(useServerTimestamp: Boolean = true): Map<String, Any> {
    val map = mutableMapOf<String, Any>(
      "userId" to userId,
      "surahNumber" to surahNumber,
      "surahName" to surahName,
      "completedVerses" to completedVerses,
      "totalVerses" to totalVerses,
      "isMemorized" to isMemorized
    )
    if (useServerTimestamp) {
      map["lastPracticed"] = FieldValue.serverTimestamp()
    } else {
      lastPracticed?.let { map["lastPracticed"] = it }
    }
    return map
  }
}

/**
 * Daily virtuous deed record stored at /users/{userId}/daily_deeds/{deedDate}
 */
data class DailyDeedRecord(
  val userId: String = "",
  val date: String = "",
  val completedDeeds: List<String> = emptyList(),
  val updatedAt: Timestamp? = null
) {
  fun toMap(useServerTimestamp: Boolean = true): Map<String, Any> {
    val map = mutableMapOf<String, Any>(
      "userId" to userId,
      "date" to date,
      "completedDeeds" to completedDeeds
    )
    if (useServerTimestamp) {
      map["updatedAt"] = FieldValue.serverTimestamp()
    } else {
      updatedAt?.let { map["updatedAt"] = it }
    }
    return map
  }
}
