package com.sfaxdroid.data.mappers

class LwpItem(
    var name: String,
    var desc: String,
    var type: String,
    url: String
) :
    BaseWallpaperView(url) {
}