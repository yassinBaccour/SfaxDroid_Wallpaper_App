package com.sfaxdroid.wallpaper

sealed interface Destination {
    data object Wallpaper : Destination
    data class Detail(val url: String, val tag: List<String>, val source: String) : Destination
    data class Tag(val title: String, val tag: Pair<String, String>, val loadFromPartner: Boolean) : Destination
}