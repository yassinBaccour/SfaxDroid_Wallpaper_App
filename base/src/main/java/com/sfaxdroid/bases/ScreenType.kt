package com.sfaxdroid.bases

sealed class ScreenType {
    object Lwp : ScreenType()
    object Wall : ScreenType()
    object Cat : ScreenType()
    object Lab : ScreenType()
    object TEXTURE : ScreenType()
    object CatWallpaper : ScreenType()
    object TIMER : ScreenType()
    object MIXED : ScreenType()
}