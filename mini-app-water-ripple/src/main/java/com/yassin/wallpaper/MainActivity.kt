package com.yassin.wallpaper

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.TextView
import com.sfaxdroid.engine.Constants
import com.sfaxdroid.mini.base.BaseConstants
import com.sfaxdroid.mini.base.BaseMiniAppActivity
import com.sfaxdroid.mini.base.Utils

class MainActivity : BaseMiniAppActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btnSetWallpaper).setOnClickListener {
            clearCurrentWallpaper()
            Utils.openLiveWallpaper<WallpaperEngine>(this)
        }

        findViewById<ImageView>(R.id.imageViewPub).setOnClickListener {
            Utils.openPub(this)
        }

        initViewByApp()
    }

    private fun initViewByApp() {
        if (BuildConfig.FLAVOR == "green") {
            findViewById<ImageView>(R.id.imageViewLogo).visibility = View.GONE
        }
        if (BuildConfig.FLAVOR != "pink") {
            initRadioButton()
        } else {
            findViewById<RadioGroup>(R.id.radioChooseWallpaper).visibility = View.INVISIBLE
            findViewById<TextView>(R.id.textViewChoose).visibility = View.INVISIBLE
        }
    }

    private fun initRadioButton() {

        val sharedPref = getSharedPreferences(
            BaseConstants.PREF_NAME,
            Context.MODE_PRIVATE
        )

        val editor = sharedPref.edit()
        val radioChooseWallpaper = findViewById<RadioGroup>(R.id.radioChooseWallpaper)
        when (sharedPref.getString(Constants.CHANGE_IMAGE_KEY, "")) {
            Constants.CHANGE_IMAGE_VALUE_ONE -> {
                radioChooseWallpaper.check(R.id.wallp1)
            }
            Constants.CHANGE_IMAGE_VALUE_TWO -> {
                radioChooseWallpaper.check(R.id.wallp2)
            }
            Constants.CHANGE_IMAGE_VALUE_THREE -> {
                radioChooseWallpaper.check(R.id.wallp3)
            }
            else -> {
                radioChooseWallpaper.check(R.id.wallp1)
                editor.putString(
                    Constants.CHANGE_IMAGE_KEY,
                    Constants.CHANGE_IMAGE_VALUE_ONE
                )
                editor.apply()
            }
        }
        radioChooseWallpaper
            .setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    R.id.wallp1 -> {
                        editor.putString(
                            Constants.CHANGE_IMAGE_KEY,
                            Constants.CHANGE_IMAGE_VALUE_ONE
                        )
                        editor.apply()
                    }
                    R.id.wallp2 -> {
                        editor.putString(
                            Constants.CHANGE_IMAGE_KEY,
                            Constants.CHANGE_IMAGE_VALUE_TWO
                        )
                        editor.apply()
                    }
                    R.id.wallp3 -> {
                        editor.putString(
                            Constants.CHANGE_IMAGE_KEY,
                            Constants.CHANGE_IMAGE_VALUE_THREE
                        )
                        editor.apply()
                    }
                }
            }
    }
}
