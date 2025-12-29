package com.sfaxdroid.wallpaper

sealed interface Destination {
    data object Wallpaper : Destination
    data class Detail(val url: String) : Destination
}