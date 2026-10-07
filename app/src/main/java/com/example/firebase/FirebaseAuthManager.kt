package com.example.firebase

import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import com.example.R
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FirebaseAuthManager(
  private val auth: FirebaseAuth? = try {
    Firebase.auth
  } catch (e: Throwable) {
    Log.w(TAG, "FirebaseAuth not immediately initialized", e)
    null
  }
) {
  val currentUser: FirebaseUser?
    get() = try {
      auth?.currentUser ?: Firebase.auth.currentUser
    } catch (e: Throwable) {
      null
    }

  val authStateFlow: Flow<FirebaseUser?> = callbackFlow {
    val activeAuth = try {
      auth ?: Firebase.auth
    } catch (e: Throwable) {
      null
    }

    if (activeAuth == null) {
      trySend(null)
      awaitClose { }
      return@callbackFlow
    }

    val listener = FirebaseAuth.AuthStateListener { firebaseAuth ->
      trySend(firebaseAuth.currentUser)
    }
    activeAuth.addAuthStateListener(listener)
    awaitClose { activeAuth.removeAuthStateListener(listener) }
  }

  suspend fun signInWithGoogle(context: Context): Result<FirebaseUser> {
    val webClientId = try {
      context.getString(R.string.default_web_client_id)
    } catch (e: Exception) {
      Log.e(TAG, "Missing default_web_client_id resource", e)
      return Result.failure(IllegalStateException("Google Client ID configuration missing"))
    }

    val credentialManager = CredentialManager.create(context)
    val signInOption = GetSignInWithGoogleOption.Builder(webClientId)
      .build()

    val request = GetCredentialRequest.Builder()
      .addCredentialOption(signInOption)
      .build()

    return try {
      val response = credentialManager.getCredential(
        context = context,
        request = request
      )

      val credential = response.credential
      if (credential is CustomCredential &&
          credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
      ) {
        val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
        val authCredential = GoogleAuthProvider.getCredential(googleIdTokenCredential.idToken, null)
        val activeAuth = auth ?: Firebase.auth
        val authResult = activeAuth.signInWithCredential(authCredential).await()
        val user = authResult.user ?: error("Firebase sign-in returned null user")
        Result.success(user)
      } else {
        Result.failure(IllegalStateException("Unexpected credential type returned"))
      }
    } catch (e: GetCredentialCancellationException) {
      Log.w(TAG, "Google Sign-In was cancelled by user: ${e.message}")
      Result.failure(e)
    } catch (e: Exception) {
      Log.e(TAG, "Google Sign-In failed", e)
      Result.failure(e)
    }
  }

  suspend fun trySilentSignIn(context: Context): Result<FirebaseUser>? {
    val webClientId = try {
      context.getString(R.string.default_web_client_id)
    } catch (e: Exception) {
      return null
    }

    val credentialManager = CredentialManager.create(context)
    val googleIdOption = GetGoogleIdOption.Builder()
      .setFilterByAuthorizedAccounts(true)
      .setServerClientId(webClientId)
      .setAutoSelectEnabled(true)
      .build()

    val request = GetCredentialRequest.Builder()
      .addCredentialOption(googleIdOption)
      .build()

    return try {
      val response = credentialManager.getCredential(
        context = context,
        request = request
      )
      val credential = response.credential
      if (credential is CustomCredential &&
          credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
      ) {
        val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
        val authCredential = GoogleAuthProvider.getCredential(googleIdTokenCredential.idToken, null)
        val activeAuth = auth ?: Firebase.auth
        val authResult = activeAuth.signInWithCredential(authCredential).await()
        val user = authResult.user
        if (user != null) Result.success(user) else null
      } else {
        null
      }
    } catch (e: Exception) {
      // Silent sign-in may fail if no active authorized account; fall back to interactive sign-in
      null
    }
  }

  fun signOut() {
    try {
      (auth ?: Firebase.auth).signOut()
    } catch (e: Throwable) {
      Log.w(TAG, "Sign out error", e)
    }
  }

  companion object {
    private const val TAG = "FirebaseAuthManager"

    @Volatile
    private var instance: FirebaseAuthManager? = null

    fun getInstance(): FirebaseAuthManager {
      return instance ?: synchronized(this) {
        instance ?: FirebaseAuthManager().also { instance = it }
      }
    }
  }
}
