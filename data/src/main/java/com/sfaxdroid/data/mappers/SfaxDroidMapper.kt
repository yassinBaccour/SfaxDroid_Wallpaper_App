package com.sfaxdroid.data.mappers

interface SfaxDroidMapper<F, T> {
    fun map(from: F?, isSmallScreen: Boolean): T

    fun getUrlByScreenSize(urlToChange: String, isSmallScreen: Boolean): String {
        return if (isSmallScreen)
            urlToChange.replace(
                "/islamicimages/",
                "/islamicimagesmini/", true
            )
        else urlToChange
    }
}