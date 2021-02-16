package com.sfaxdroid.data.mappers

import com.sfaxdroid.data.entity.LiveWallpaper
import com.sfaxdroid.data.entity.Wallpaper

class WallpaperToLwpMapper : SfaxDroidMapper<Wallpaper, LwpItem> {

    override fun map(from: Wallpaper?, isSmallScreen: Boolean): LwpItem {
        return LwpItem(
            from?.name ?: "",
            from?.desc ?: "",
            getType(from?.type ?: ""),
            getUrlByScreenSize(from?.url ?: "", isSmallScreen)
        )
    }

    private fun getType(type: String): LiveWallpaper {
        return when (type) {
            "Img2D" -> LiveWallpaper.DouaLwp
            "SkyView" -> LiveWallpaper.SkyView
            "Timer" -> LiveWallpaper.TimerLwp
            "Text2D" -> LiveWallpaper.NameOfAllah2D
            else -> LiveWallpaper.None
        }
    }

}