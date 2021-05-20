package com.sfaxdroid.data.mappers

import com.sfaxdroid.data.entity.Tag

class TagToTagViewMap : SfaxDroidMapper<Tag, TagView> {
    override fun map(from: Tag?, isSmallScreen: Boolean): TagView {
        return TagView(from?.title ?: "", from?.fileName ?: "", getType(from?.type ?: ""))
    }

    private fun getType(type: String): TagType {
        return when (type) {
            "category" -> TagType.Category
            "categorySquare" -> TagType.CategorySquare
            "texture" -> TagType.Texture
            else -> TagType.Wallpaper
        }
    }
}
