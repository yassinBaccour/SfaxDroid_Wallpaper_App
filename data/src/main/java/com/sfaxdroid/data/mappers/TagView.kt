package com.sfaxdroid.data.mappers


class TagView(var name: String, var fileName: String, var type: TagType)

sealed class TagType {
    object Category : TagType()
    object Wallpaper : TagType()
    object Texture : TagType()
}