package com.sfaxdroid.data.mappers

import com.sfaxdroid.data.entity.Wallpaper

class WallpaperToViewMapper : SfaxDroidMapper<Wallpaper, SimpleWallpaperView> {

    override fun map(from: Wallpaper?, isSmallScreen: Boolean): SimpleWallpaperView {
        return SimpleWallpaperView(
            getUrlByScreenSize(from?.url ?: "", isSmallScreen),
            getPreviewUrl(from?.url ?: "", isSmallScreen)
        )
    }

    private fun getPreviewUrl(urlToChange: String, isSmallScreen: Boolean): String {
        val urlByScreen = getUrlByScreenSize(urlToChange, isSmallScreen)
        return urlByScreen.replace("_preview", "")
    }
}
