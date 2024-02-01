package com.sfaxdroid.bases

sealed class NavScreen(val route: String) {
    object Wallpaper : NavScreen("wallpaper")
    object LiveWallpaper : NavScreen("liveWallpaper")
    object Category : NavScreen("category")
    object Detail : NavScreen("detail")
    object Pixabay : NavScreen("pixabay")
}