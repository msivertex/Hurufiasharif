package com.example

import android.app.Application
import android.util.Log
import com.google.firebase.FirebaseApp

class HurufiaApplication : Application() {
  override fun onCreate() {
    super.onCreate()
    try {
      if (FirebaseApp.getApps(this).isEmpty()) {
        FirebaseApp.initializeApp(this)
      }
    } catch (e: Exception) {
      Log.e(TAG, "FirebaseApp.initializeApp error", e)
    }
  }

  companion object {
    private const val TAG = "HurufiaApplication"
  }
}
