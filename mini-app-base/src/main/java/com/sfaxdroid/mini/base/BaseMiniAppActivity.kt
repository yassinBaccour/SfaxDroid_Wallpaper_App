package com.sfaxdroid.mini.base

import android.annotation.SuppressLint
import android.app.Activity
import android.app.WallpaperManager
import android.widget.Toast
import java.io.IOException

abstract class BaseMiniAppActivity : Activity() {
    override fun onBackPressed() {
        if (back_pressed + 2000 > System.currentTimeMillis()) super.onBackPressed() else {
            RateUs(this, packageName).appLaunched()
            Toast.makeText(
                baseContext, R.string.txtrate6,
                Toast.LENGTH_SHORT
            ).show()
        }
        back_pressed = System.currentTimeMillis()
    }

    @SuppressLint("MissingPermission")
    fun ClearCurrentWallpaper() {
        val myWallpaperManager = WallpaperManager
            .getInstance(this)
        try {
            myWallpaperManager.clear()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    companion object {
        private var back_pressed: Long = 0
    }
}