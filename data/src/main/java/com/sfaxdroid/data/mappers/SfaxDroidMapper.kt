package com.sfaxdroid.data.mappers

import com.sfaxdroid.data.Constants

interface SfaxDroidMapper<F, T> {
    fun map(from: F?, isSmallScreen: Boolean): T

    fun getUrlByScreenSize(urlToChange: String, isSmallScreen: Boolean): String {
        return if (isSmallScreen)
            urlToChange.replace(
                Constants.URL_BIG_WALLPAPER_FOLDER,
                Constants.URL_SMALL_WALLPAPER_FOLDER, true
            )
        else urlToChange
    }
}