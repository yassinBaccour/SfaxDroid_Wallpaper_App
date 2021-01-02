package com.sfaxdroid.data.mappers

import com.sfaxdroid.data.entity.Wallpaper

class WallpaperToCategoryMapper : SfaxDroidMapper<Wallpaper, CategoryItem> {

    override fun map(from: Wallpaper?): CategoryItem {
        return CategoryItem(
            from?.name ?: "",
            from?.desc ?: "",
            from?.color ?: "",
            from?.file ?: "",
            from?.url ?: ""
        )
    }
}