package com.sfaxdroid.engine

import android.content.Context
import android.content.SharedPreferences
import android.service.wallpaper.WallpaperService
import com.sfaxdroid.mini.base.BaseConstants
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
            BaseConstants.PREF_NAME,
            Context.MODE_PRIVATE
        )

        sharedPreferences?.registerOnSharedPreferenceChangeListener(this)
        sharedPreferences?.registerOnSharedPreferenceChangeListener(this)

        setRippleSound(sharedPreferences)
        setRippleSize(sharedPreferences)
        setRippleSpeedKey(sharedPreferences)
        initBackground(sharedPreferences)

        return WallpaperEngine(
            getSharedPreferences(
                BaseConstants.PREF_NAME,
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

    private fun initBackground(pref: SharedPreferences?) {
        when (pref?.getString(Constants.CHANGE_IMAGE_KEY, "")) {
            Constants.CHANGE_IMAGE_VALUE_ONE -> wallpaperRenderer?.initBackground(
                getWallpaperOne()
            )
            Constants.CHANGE_IMAGE_VALUE_TWO -> wallpaperRenderer?.initBackground(
                getWallpaperTwo()
            )
            Constants.CHANGE_IMAGE_VALUE_THREE -> wallpaperRenderer?.initBackground(
                getWallpaperThree()
            )
        }
    }

    override fun onSharedPreferenceChanged(pref: SharedPreferences?, key: String?) {
        if (wallpaperRenderer != null) {
            when (key) {
                Constants.CHANGE_IMAGE_KEY -> initBackground(pref)
                Constants.SOUND_KEY -> setRippleSound(pref)
                Constants.RIPPLE_SIZE_KEY -> setRippleSize(pref)
                Constants.RIPPLE_SPEED_KEY -> setRippleSpeedKey(pref)
            }
        }
    }


    private fun setRippleSpeedKey(pref: SharedPreferences?) {
        when (pref?.getString(Constants.RIPPLE_SPEED_KEY, "")) {
            "slow" -> wallpaperRenderer?.setRippleSpeed(4f)
            "medium" -> wallpaperRenderer?.setRippleSpeed(5.5f)
            "fast" -> wallpaperRenderer?.setRippleSpeed(8f)
            else -> wallpaperRenderer?.setRippleSpeed(6f)
        }
    }

    private fun setRippleSize(pref: SharedPreferences?) {
        when (pref?.getString(Constants.RIPPLE_SIZE_KEY, "")) {
            "small" -> wallpaperRenderer?.setRippleSize(96f)
            "medium" -> wallpaperRenderer?.setRippleSize(72f)
            "large" -> wallpaperRenderer?.setRippleSize(52f)
        }
    }

    private fun setRippleSound(pref: SharedPreferences?) {
        wallpaperRenderer?.setSound(pref!!.getBoolean(Constants.SOUND_KEY, false))
    }
}