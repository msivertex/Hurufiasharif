package com.example

import android.content.Context
import android.content.SharedPreferences

/**
 * Local Session Manager to handle persistent login & guest mode session.
 * Ensures auto-login behavior so subsequent app launches jump directly to Home Screen.
 */
object AuthSessionManager {
  private const val PREFS_NAME = "hurufia_auth_session_prefs"
  private const val KEY_IS_LOGGED_IN = "key_is_logged_in"
  private const val KEY_IS_GUEST_SESSION = "key_is_guest_session"
  private const val KEY_SAVED_USER_EMAIL = "key_saved_user_email"
  private const val KEY_SAVED_USER_UID = "key_saved_user_uid"
  private const val KEY_SELECTED_LANGUAGE = "key_selected_language"

  private fun getPrefs(context: Context): SharedPreferences {
    return context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
  }

  fun isGuestSession(context: Context): Boolean {
    return getPrefs(context).getBoolean(KEY_IS_GUEST_SESSION, false)
  }

  fun setGuestSession(context: Context, isGuest: Boolean) {
    getPrefs(context).edit()
      .putBoolean(KEY_IS_GUEST_SESSION, isGuest)
      .putBoolean(KEY_IS_LOGGED_IN, isGuest)
      .apply()
  }

  fun saveUserSession(context: Context, email: String?, uid: String?) {
    getPrefs(context).edit()
      .putBoolean(KEY_IS_LOGGED_IN, true)
      .putBoolean(KEY_IS_GUEST_SESSION, false)
      .putString(KEY_SAVED_USER_EMAIL, email ?: "")
      .putString(KEY_SAVED_USER_UID, uid ?: "")
      .apply()
  }

  fun getSavedUserEmail(context: Context): String {
    return getPrefs(context).getString(KEY_SAVED_USER_EMAIL, "") ?: ""
  }

  fun getSavedUserUid(context: Context): String {
    return getPrefs(context).getString(KEY_SAVED_USER_UID, "") ?: ""
  }

  fun hasActiveSession(context: Context): Boolean {
    return getPrefs(context).getBoolean(KEY_IS_LOGGED_IN, false)
  }

  fun getSelectedLanguage(context: Context, defaultLang: String = "BN"): String {
    return getPrefs(context).getString(KEY_SELECTED_LANGUAGE, defaultLang) ?: defaultLang
  }

  fun saveSelectedLanguage(context: Context, lang: String) {
    getPrefs(context).edit()
      .putString(KEY_SELECTED_LANGUAGE, lang)
      .apply()
  }

  fun clearSession(context: Context) {
    getPrefs(context).edit()
      .putBoolean(KEY_IS_LOGGED_IN, false)
      .putBoolean(KEY_IS_GUEST_SESSION, false)
      .remove(KEY_SAVED_USER_EMAIL)
      .remove(KEY_SAVED_USER_UID)
      .apply()
  }
}
