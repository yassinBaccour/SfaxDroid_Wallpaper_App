package com.yassin.wallpaper

import android.app.Application
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp

/**
 * Created by yassin baccour on 15/05/2016.
 */
@HiltAndroidApp
class WallpaperApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}
