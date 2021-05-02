package com.sfaxdroid.data.entity

sealed class LiveWallpaper {
    object WordImg : LiveWallpaper()
    object SkyView : LiveWallpaper()
    object TimerLwp : LiveWallpaper()
    object Word2d : LiveWallpaper()
    object None : LiveWallpaper()
}