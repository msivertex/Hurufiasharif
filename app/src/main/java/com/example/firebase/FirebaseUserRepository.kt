package com.example.firebase

import android.content.Context
import android.util.Log
import com.example.R
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

class FirebaseUserRepository(
  private val db: FirebaseFirestore,
  private val auth: FirebaseAuth? = null
) {
  // Convenience constructor resolving the named database ID from string resources
  constructor(context: Context) : this(
    FirebaseFirestore.getInstance(
      context.applicationContext.getString(R.string.firestore_database_id)
    )
  )

  private fun requireUserId(): String {
    val activeAuth = auth ?: try { Firebase.auth } catch (e: Exception) { null }
    return activeAuth?.currentUser?.uid ?: error("Operation requires authenticated user")
  }

  // 1. User Profile Operations
  fun observeUserProfile(userId: String): Flow<UserProfile?> {
    return db.collection("users").document(userId)
      .snapshots()
      .map { snapshot ->
        if (snapshot.exists()) {
          snapshot.toObject(UserProfile::class.java, DocumentSnapshot.ServerTimestampBehavior.ESTIMATE)
        } else {
          null
        }
      }
  }

  suspend fun saveUserProfile(profile: UserProfile) {
    val uid = requireUserId()
    val userDoc = db.collection("users").document(uid)
    val map = profile.toMap(useServerTimestamps = true)
    userDoc.set(map, SetOptions.merge()).await()
  }

  suspend fun updateStats(coins: Long, level: Long, streak: Long) {
    val uid = requireUserId()
    db.collection("users").document(uid).update(
      mapOf(
        "coins" to coins,
        "level" to level,
        "streak" to streak
      )
    ).await()
  }

  // 2. Chapter Progress Operations
  fun observeChapterProgress(userId: String): Flow<List<ChapterProgress>> {
    return db.collection("users").document(userId).collection("progress")
      .snapshots()
      .map { snapshot ->
        snapshot.toObjects(ChapterProgress::class.java, DocumentSnapshot.ServerTimestampBehavior.ESTIMATE)
      }
  }

  suspend fun saveChapterProgress(progress: ChapterProgress) {
    val uid = requireUserId()
    val docRef = db.collection("users").document(uid)
      .collection("progress").document(progress.chapterId)
    docRef.set(progress.toMap(useServerTimestamp = true), SetOptions.merge()).await()
  }

  // 3. Surah Memorization Progress Operations
  fun observeSurahProgress(userId: String): Flow<List<SurahProgress>> {
    return db.collection("users").document(userId).collection("surah_progress")
      .snapshots()
      .map { snapshot ->
        snapshot.toObjects(SurahProgress::class.java, DocumentSnapshot.ServerTimestampBehavior.ESTIMATE)
      }
  }

  suspend fun saveSurahProgress(progress: SurahProgress) {
    val uid = requireUserId()
    val docRef = db.collection("users").document(uid)
      .collection("surah_progress").document(progress.surahNumber.toString())
    docRef.set(progress.toMap(useServerTimestamp = true), SetOptions.merge()).await()
  }

  // 4. Daily Deeds Operations
  fun observeDailyDeeds(userId: String, date: String): Flow<DailyDeedRecord?> {
    return db.collection("users").document(userId).collection("daily_deeds").document(date)
      .snapshots()
      .map { snapshot ->
        if (snapshot.exists()) {
          snapshot.toObject(DailyDeedRecord::class.java, DocumentSnapshot.ServerTimestampBehavior.ESTIMATE)
        } else {
          null
        }
      }
  }

  suspend fun saveDailyDeeds(record: DailyDeedRecord) {
    val uid = requireUserId()
    val docRef = db.collection("users").document(uid)
      .collection("daily_deeds").document(record.date)
    docRef.set(record.toMap(useServerTimestamp = true), SetOptions.merge()).await()
  }

  companion object {
    private const val TAG = "FirebaseUserRepo"
  }
}
