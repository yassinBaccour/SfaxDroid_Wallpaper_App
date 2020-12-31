package com.sfaxdroid.data.mappers

import com.sfaxdroid.data.entity.LiveWallpaper

class LwpItem(
    var name: String,
    var desc: String,
    var type: LiveWallpaper,
    url: String
) :
    BaseWallpaperView(url) {
}