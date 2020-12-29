package com.sfaxdroid.data.mappers

class CategoryItem(
    var name: String,
    var desc: String,
    var color: String,
    var list: List<SimpleWallpaperView>,
    url: String
) :
    BaseWallpaperView(url) {
}