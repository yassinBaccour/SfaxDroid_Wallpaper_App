package com.sfaxdroid.data.mappers

import com.sfaxdroid.data.entity.Wallpaper

class WallpaperToViewMapper : SfaxDroidMapper<Wallpaper, SimpleWallpaperView> {

    override fun map(from: Wallpaper?): SimpleWallpaperView {
        return SimpleWallpaperView(from?.url ?: "")
    }
}