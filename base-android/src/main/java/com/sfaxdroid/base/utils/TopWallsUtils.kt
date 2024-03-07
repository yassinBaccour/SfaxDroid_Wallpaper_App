package com.sfaxdroid.base.utils

class TopWallsUtils {
    fun getCatchyString(downloads: Int, likes: Int, views: Int) = listOf(
        "$downloads Downloads",
        "$likes Likes",
        "$likes Likes",
        "$views Views",
    ).random()
}