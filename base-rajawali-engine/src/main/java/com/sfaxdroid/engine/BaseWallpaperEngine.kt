package com.sfaxdroid.engine

import android.content.Context
import android.content.SharedPreferences
import android.service.wallpaper.WallpaperService
import rajawali.wallpaper.Wallpaper

abstract class BaseWallpaperEngine : Wallpaper(),
    SharedPreferences.OnSharedPreferenceChangeListener {

    private var wallpaperRenderer: WallpaperRenderer? = null
    private var sharedPreferences: SharedPreferences? = null

    abstract fun getWallpaperOne(): Int
    abstract fun getWallpaperTwo(): Int
    abstract fun getWallpaperThree(): Int

    override fun onCreateEngine(): WallpaperService.Engine {
        wallpaperRenderer = WallpaperRenderer(
            this,
            R.raw.water_drop,
            getWallpaperOne()
        )
        sharedPreferences = getSharedPreferences(
            Constants.PREFERENCE_NAME,
            Context.MODE_PRIVATE
        )

        sharedPreferences?.registerOnSharedPreferenceChangeListener(this)
        sharedPreferences?.registerOnSharedPreferenceChangeListener(this)

        setRippleSound(sharedPreferences, Constants.SOUND_KEY)
        setRippleSize(sharedPreferences, Constants.RIPPLE_SIZE_KEY)
        setRippleSpeedKey(sharedPreferences, Constants.RIPPLE_SPEED_KEY)
        initBackground(sharedPreferences, Constants.CHANGE_IMAGE_KEY)

        return WallpaperEngine(
            getSharedPreferences(
                Constants.PREFERENCE_NAME,
                Context.MODE_PRIVATE
            ),
            baseContext,
            wallpaperRenderer,
            false
        )
    }

    override fun onDestroy() {
        sharedPreferences?.unregisterOnSharedPreferenceChangeListener(this)
        super.onDestroy()
    }

    private fun initBackground(pref: SharedPreferences?, key: String) {
        when (pref?.getString(key, "")) {
            Constants.CHANGE_IMAGE_VALUE_ONE -> wallpaperRenderer!!.initBackground(
                getWallpaperOne()
            )
            Constants.CHANGE_IMAGE_VALUE_TWO -> wallpaperRenderer!!.initBackground(
                getWallpaperTwo()
            )
            Constants.CHANGE_IMAGE_VALUE_THREE -> wallpaperRenderer!!.initBackground(
                getWallpaperThree()
            )
        }
    }

    override fun onSharedPreferenceChanged(pref: SharedPreferences?, key: String?) {
        if (wallpaperRenderer != null) {
            when (key) {
                Constants.CHANGE_IMAGE_KEY -> initBackground(
                    pref,
                    Constants.CHANGE_IMAGE_KEY
                )
                Constants.SOUND_KEY -> setRippleSound(pref, key)
                Constants.RIPPLE_SIZE_KEY -> setRippleSize(pref, key)
                Constants.RIPPLE_SPEED_KEY -> setRippleSpeedKey(pref, key)
            }
        }
    }


    private fun setRippleSpeedKey(pref: SharedPreferences?, key: String?) {
        when (pref?.getString(key, "")) {
            "slow" -> wallpaperRenderer?.setRippleSpeed(4f)
            "medium" -> wallpaperRenderer?.setRippleSpeed(5.5f)
            "fast" -> wallpaperRenderer?.setRippleSpeed(8f)
            else -> wallpaperRenderer?.setRippleSpeed(6f)
        }
    }

    private fun setRippleSize(pref: SharedPreferences?, key: String?) {
        when (pref?.getString(key, "")) {
            "small" -> wallpaperRenderer?.setRippleSize(96f)
            "medium" -> wallpaperRenderer?.setRippleSize(72f)
            "large" -> wallpaperRenderer?.setRippleSize(52f)
        }
    }

    private fun setRippleSound(pref: SharedPreferences?, key: String?) {
        wallpaperRenderer?.setSound(pref!!.getBoolean(key, false))
    }
}