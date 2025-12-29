package com.sfaxdroid.wallpaper

sealed interface Destination {
    data object Wallpaper : Destination
    data class Detail(val url: String, val tag: List<String>, val source: String) : Destination
    data class Tag(val tag: String) : Destination
}