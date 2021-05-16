package com.sfaxdroid.data.mappers

import com.sfaxdroid.data.entity.LiveWallpaper

class LwpItem(
    val name: String,
    val desc: String,
    val type: LiveWallpaper,
    thumbnailUrl: String
) :
    BaseWallpaperView(thumbnailUrl)
