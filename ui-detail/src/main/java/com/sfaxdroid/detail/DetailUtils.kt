package com.sfaxdroid.detail

import android.app.WallpaperManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.sfaxdroid.base.Constants
import com.sfaxdroid.base.Utils
import java.io.File
import java.io.IOException
import kotlin.math.max

class DetailUtils {

    companion object {

        fun setWallpaper(wallpaper: Bitmap, context: Context): Boolean {
            return try {
                val wallpaperManager =
                    WallpaperManager.getInstance(context)
                val width = wallpaperManager.desiredMinimumWidth
                val height = wallpaperManager.desiredMinimumHeight
                if (width > wallpaper.width && height > wallpaper.height && Utils.getScreenWidthPixels(
                        context
                    ) < Constants.MIN_WIDHT
                ) {
                    val pixels =
                        IntArray(wallpaper.width * wallpaper.height)
                    wallpaper.getPixels(
                        pixels,
                        0,
                        wallpaper.width,
                        0,
                        0,
                        wallpaper.width,
                        wallpaper.height
                    )
                    wallpaperManager.setBitmap(Bitmap.createBitmap(
                        width,
                        height,
                        Bitmap.Config.ARGB_8888
                    ).apply {
                        setPixels(
                            pixels,
                            0,
                            wallpaper.width,
                            max(0, width - wallpaper.width) / 2,
                            max(0, height - wallpaper.height) / 2,
                            wallpaper.width,
                            wallpaper.height
                        )
                    })
                    true
                } else {
                    wallpaperManager.setBitmap(wallpaper)
                    true
                }
            } catch (e: IOException) {
                false
            }
        }


        fun decodeBitmapAndSetAsLiveWallpaper(file: File, context: Context): Boolean {
            if (file.exists()) {
                val wallpaperManager =
                    WallpaperManager.getInstance(context)
                try {
                    BitmapFactory.decodeFile(
                        file.path,
                        BitmapFactory.Options().apply {
                            inPreferredConfig = Bitmap.Config.ARGB_8888
                        }
                    )?.also {
                        val mBackground =
                            Bitmap.createScaledBitmap(
                                it,
                                Utils.getScreenWidthPixels(context),
                                Utils.getScreenHeightPixels(context),
                                true
                            )
                        if (mBackground != null) wallpaperManager.setBitmap(mBackground)
                        it.recycle()
                        return true
                    }
                    return false
                } catch (ignored: IOException) {
                    return false
                }
            } else {
                return false
            }
        }
    }
}