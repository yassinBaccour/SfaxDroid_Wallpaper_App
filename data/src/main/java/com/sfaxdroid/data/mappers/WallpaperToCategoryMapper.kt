package com.sfaxdroid.data.mappers

import com.sfaxdroid.data.entity.Wallpaper

class WallpaperToCategoryMapper : SfaxDroidMapper<Wallpaper, CategoryItem> {

    override fun map(from: Wallpaper?, isSmallScreen: Boolean): CategoryItem {
        return CategoryItem(
            from?.name ?: "",
            from?.desc ?: "",
            from?.color ?: "",
            from?.file ?: "",
            getUrlByScreenSize(from?.url ?: "", isSmallScreen)
        )
    }
}
