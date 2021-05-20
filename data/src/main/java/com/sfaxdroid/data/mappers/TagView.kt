package com.sfaxdroid.data.mappers

class TagView(val name: String, val fileName: String, val type: TagType)

sealed class TagType {
    object Category : TagType()
    object CategorySquare : TagType()
    object Wallpaper : TagType()
    object Texture : TagType()
}
