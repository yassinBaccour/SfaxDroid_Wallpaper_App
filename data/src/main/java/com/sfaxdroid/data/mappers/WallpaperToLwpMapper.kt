package com.sfaxdroid.data.mappers

import com.sfaxdroid.data.entity.AppName
import com.sfaxdroid.data.entity.LiveWallpaper
import com.sfaxdroid.data.entity.Wallpaper
import javax.inject.Inject
import javax.inject.Named

class WallpaperToLwpMapper @Inject constructor(@Named("app-name") var appName: AppName) :
    SfaxDroidMapper<Wallpaper, LwpItem> {

    override fun map(from: Wallpaper?, isSmallScreen: Boolean): LwpItem {
        val url = getUrlByScreenSize(from?.url ?: "", isSmallScreen)
        val urlByApp = if (appName == AppName.AccountTwo) {
            url.replace(".jpg", "_land.jpg")
        } else {
            url
        }
        return LwpItem(
            from?.name ?: "",
            from?.desc ?: "",
            getType(from?.type ?: ""),
            urlByApp
        )
    }

    private fun getType(type: String): LiveWallpaper {
        return when (type) {
            "Img2D" -> LiveWallpaper.WordImg
            "SkyView" -> LiveWallpaper.SkyView
            "Timer" -> LiveWallpaper.TimerLwp
            "Text2D" -> LiveWallpaper.Word2d
            else -> LiveWallpaper.None
        }
    }
}
