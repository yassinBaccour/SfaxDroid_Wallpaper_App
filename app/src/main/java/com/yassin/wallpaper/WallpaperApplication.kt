package com.yassin.wallpaper

import android.app.Application
import com.yassin.wallpaper.appInitializers.AppInitializers
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

/**
 * Created by yassin baccour on 15/05/2016.
 */
@HiltAndroidApp
class WallpaperApplication : Application() {

    @Inject
    lateinit var initializers: AppInitializers

    override fun onCreate() {
        super.onCreate()
        initializers.init(this)
    }
}
