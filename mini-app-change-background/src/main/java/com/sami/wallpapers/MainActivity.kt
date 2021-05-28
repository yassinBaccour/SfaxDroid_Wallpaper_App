package com.sami.wallpapers

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.RadioGroup
import com.sfaxdroid.mini.base.BaseMiniAppActivity
import com.sfaxdroid.mini.base.RateUs
import com.sfaxdroid.mini.base.Utils

class MainActivity : BaseMiniAppActivity() {

    private var sharedPrefs: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPrefs =
            getSharedPreferences(
                com.sfaxdroid.mini.base.BaseConstants.PREF_NAME,
                Context.MODE_PRIVATE
            )
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.button_set_wallpaper).setOnClickListener {
            var nbRun = pref["nbRun", 0]
            nbRun++
            pref["nbRun"] = nbRun
            Utils.openLiveWallpaper<WallpaperAppService>(this)
        }

        findViewById<Button>(R.id.button_open_gallery).setOnClickListener {
            startActivity(
                Intent(
                    applicationContext,
                    ViewWallpaperActivity::class.java
                )
            )
        }

        findViewById<Button>(R.id.button_privacy).setOnClickListener {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse(Constants.PRIVACY)
            )
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            }
        }

        findViewById<Button>(R.id.btn_rating).setOnClickListener {
            RateUs.startRateUsWithApi(this)
        }

        val radioAnimation = findViewById<RadioGroup>(R.id.radio_animation)
        when (sharedPrefs?.getString(Constants.PREF_KEY_SPEED, "none")) {
            Constants.PREF_VALUE_SPEED_1 -> {
                radioAnimation.check(R.id.speed1)
            }
            Constants.PREF_VALUE_SPEED_2 -> {
                radioAnimation.check(R.id.speed2)
            }
            Constants.PREF_VALUE_SPEED_3 -> {
                radioAnimation.check(R.id.speed3)
            }
            Constants.PREF_VALUE_SPEED_4 -> {
                radioAnimation.check(R.id.speed4)
            }
            else -> {
                radioAnimation.check(R.id.speed3)
                saveSpeed(Constants.PREF_VALUE_SPEED_3)
            }
        }

        radioAnimation.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.speed1 -> {
                    saveSpeed(Constants.PREF_VALUE_SPEED_1)
                }
                R.id.speed2 -> {
                    saveSpeed(Constants.PREF_VALUE_SPEED_2)
                }
                R.id.speed3 -> {
                    saveSpeed(Constants.PREF_VALUE_SPEED_3)
                }
                R.id.speed4 -> {
                    saveSpeed(Constants.PREF_VALUE_SPEED_4)
                }
            }
        }

        val radioQuality = findViewById<RadioGroup>(R.id.radio_quality)

        when (sharedPrefs?.getString(Constants.PREF_KEY_QUALITY, "none")) {
            Constants.PREF_VALUE_QUALITY_1 -> {
                radioQuality.check(R.id.quality1)
            }
            Constants.PREF_VALUE_QUALITY_2 -> {
                radioQuality.check(R.id.quality2)
            }
            else -> {
                radioQuality.check(R.id.quality1)
                saveQuality(Constants.PREF_VALUE_QUALITY_1)
            }
        }

        radioQuality.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.quality1 -> {
                    saveQuality(Constants.PREF_VALUE_QUALITY_1)
                }
                R.id.quality2 -> {
                    saveQuality(Constants.PREF_VALUE_QUALITY_2)
                }
            }
        }
    }

    private fun saveQuality(quality: String) {
        sharedPrefs?.edit()?.apply {
            putString(Constants.PREF_KEY_QUALITY, quality)
            apply()
        }
    }

    private fun saveSpeed(speed: String) {
        sharedPrefs?.edit()?.apply {
            putString(Constants.PREF_KEY_SPEED, speed)
            apply()
        }
    }
}
