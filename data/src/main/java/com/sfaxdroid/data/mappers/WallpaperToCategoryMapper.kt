package com.sfaxdroid.data.mappers

import com.sfaxdroid.data.entity.Wallpaper

class WallpaperToCategoryMapper : SfaxDroidMapper<Wallpaper, CategoryItem> {

    override fun map(from: Wallpaper?): CategoryItem {
        return CategoryItem(
            from?.name ?: "",
            from?.desc ?: "",
            from?.color ?: "",
            getSimpleWallpaper(from?.subcategory?.wallpapers),
            from?.url ?: ""
        )
    }

    private fun getSimpleWallpaper(wallpapers: List<Wallpaper>?): List<SimpleWallpaperView> {
        return wallpapers?.map {
            SimpleWallpaperView(it.url)
        } ?: emptyList()
    }
}