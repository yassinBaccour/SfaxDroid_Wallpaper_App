package com.sfaxdroid.data.entity

sealed class LiveWallpaper {
    object DouaLwp : LiveWallpaper()
    object SkyView : LiveWallpaper()
    object TimerLwp : LiveWallpaper()
    object NameOfAllah2D : LiveWallpaper()
    object none : LiveWallpaper()
}