package com.sami.wallpapers

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

data class WallpaperPref(var speed: String, var quality: String)


class CheckedViewModel : ViewModel() {

    val counterLiveDate: LiveData<WallpaperPref>
        get() = data
    private val data = MutableLiveData<WallpaperPref>()

    init {
        data.value = WallpaperPref(Constants.PREF_VALUE_SPEED_4, Constants.PREF_VALUE_QUALITY_2)
    }

    fun setQuality(quality: String) {
        val wallpaperPref = data.value
        val speed = wallpaperPref?.speed
        val wallpaperPref1 = WallpaperPref(speed = speed!!, quality = quality)
        data.value = wallpaperPref1
    }

    fun setSpeed(speed: String) {
        val wallpaperPref = data.value
        val quality = wallpaperPref?.quality
        val wallpaperPref1 = WallpaperPref(speed = speed, quality = quality!!)
        data.value = wallpaperPref1
    }
}