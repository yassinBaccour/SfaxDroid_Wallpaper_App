package com.sfaxdroid.domain.entity

data class WallpaperGroup(
    val title: String,
    val wallpapers: List<Wallpaper>,
    val theme: WallpaperTheme
)

