package com.sfaxdroid.base.utils

import android.content.Context
import com.sfaxdroid.base.Constants
import com.sfaxdroid.base.WallpaperObject
import com.sfaxdroid.base.utils.Utils.Companion.getScreenHeightPixels
import com.sfaxdroid.base.utils.Utils.Companion.getScreenWidthPixels

class WallpaperUtils {

    companion object {
        fun getUrlFromWallpaper(
            wall: WallpaperObject,
            context: Context?
        ): String? {
            var url: String? = ""
            url =
                if (getScreenHeightPixels(context!!) < Constants.MIN_HEIGHT && getScreenWidthPixels(
                        context
                    ) < Constants.MIN_WIDHT
                ) wall.previewUrl.replace(
                    Constants.URL_BIG_WALLPAPER_FOLDER,
                    Constants.URL_SMALL_WALLPAPER_FOLDER
                ) else wall.previewUrl
            return url
        }

        fun getUrlFromWallpaper(
            wall: String,
            context: Context?
        ): String? {
            var url: String? = ""
            url =
                if (getScreenHeightPixels(context!!) < Constants.MIN_HEIGHT && getScreenWidthPixels(
                        context
                    ) < Constants.MIN_WIDHT
                ) wall.replace(
                    Constants.URL_BIG_WALLPAPER_FOLDER,
                    Constants.URL_SMALL_WALLPAPER_FOLDER
                ) else wall
            return url
        }
    }
}