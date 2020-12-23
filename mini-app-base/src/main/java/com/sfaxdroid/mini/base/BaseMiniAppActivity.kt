package com.sfaxdroid.mini.base

import android.annotation.SuppressLint
import android.app.Activity
import android.app.WallpaperManager
import android.widget.Toast
import java.io.IOException

abstract class BaseMiniAppActivity : Activity() {
    override fun onBackPressed() {
        if (backPressed + 2000 > System.currentTimeMillis()) super.onBackPressed() else {
            RateUs(this, packageName).appLaunched()
            Toast.makeText(
                baseContext, R.string.back_press_message_info,
                Toast.LENGTH_SHORT
            ).show()
        }
        backPressed = System.currentTimeMillis()
    }

    @SuppressLint("MissingPermission")
    fun clearCurrentWallpaper() {
        val myWallpaperManager = WallpaperManager
            .getInstance(this)
        try {
            myWallpaperManager.clear()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    companion object {
        private var backPressed: Long = 0
    }
}