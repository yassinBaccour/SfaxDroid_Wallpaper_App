package com.sfaxdroid.mini.base

import android.annotation.SuppressLint
import android.app.Activity
import android.app.WallpaperManager
import android.content.res.Resources
import android.graphics.Point
import android.os.Bundle
import android.view.Display
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.IOException

abstract class BaseMiniAppActivity : AppCompatActivity() {

    lateinit var pref: PreferencesManager
    var width: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pref = PreferencesManager(this, BaseConstants.PREF_NAME)
        width = Resources.getSystem().displayMetrics.widthPixels
    }

    override fun onBackPressed() {
        if (backPressed + 2000 > System.currentTimeMillis()) super.onBackPressed() else {
            RateUs(this, packageName, width).appLaunched()
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

    override fun onResume() {
        super.onResume()
        nbRatingManager()
    }

    private fun nbRatingManager() {
        if (!pref["isRated", false]) {
            var nbRun = pref["nbRun", 0]
            if (nbRun == 2 || nbRun == 6 || nbRun == 10) {
                pref["isRated"] = true
                RateUs.startRateUsWithApi(this)
            }
            nbRun++
            pref["nbRun"] = nbRun
        }
    }

    companion object {
        private var backPressed: Long = 0
    }
}
