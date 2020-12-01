package com.sami.rippel.lailahailaahhah

import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import com.sfaxdroid.engine.BaseWallpaperEngine

class WallpaperEngine : BaseWallpaperEngine(),
    OnSharedPreferenceChangeListener {

    override fun getWallpaperOne(): Int {
        return R.drawable.ic_wallpaper_one
    }

    override fun getWallpaperTwo(): Int {
        return R.drawable.ic_wallpaper_two
    }

    override fun getWallpaperThree(): Int {
        return R.drawable.ic_wallpaper_three
    }

}