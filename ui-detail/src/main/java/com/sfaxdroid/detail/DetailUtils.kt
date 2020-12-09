package com.sfaxdroid.detail

import android.app.WallpaperManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.sfaxdroid.base.Constants
import com.sfaxdroid.base.Utils
import java.io.File
import java.io.IOException

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
                    val xPadding =
                        Math.max(0, width - wallpaper.width) / 2
                    val yPadding =
                        Math.max(0, height - wallpaper.height) / 2
                    val paddedWallpaper =
                        Bitmap.createBitmap(
                            width,
                            height,
                            Bitmap.Config.ARGB_8888
                        )
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
                    paddedWallpaper.setPixels(
                        pixels,
                        0,
                        wallpaper.width,
                        xPadding,
                        yPadding,
                        wallpaper.width,
                        wallpaper.height
                    )
                    wallpaperManager.setBitmap(paddedWallpaper)
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
            val mOptions =
                BitmapFactory.Options()
            mOptions.inPreferredConfig = Bitmap.Config.ARGB_8888
            return if (file.exists()) {
                val mBitmap = BitmapFactory.decodeFile(
                    file.path,
                    mOptions
                )
                val wallpaperManager =
                    WallpaperManager.getInstance(context)
                try {
                    if (mBitmap != null) {
                        val mBackground =
                            Bitmap.createScaledBitmap(
                                mBitmap,
                                Utils.getScreenWidthPixels(context),
                                Utils.getScreenHeightPixels(context),
                                true
                            )
                        if (mBackground != null) wallpaperManager.setBitmap(mBackground)
                        mBitmap.recycle()
                        true
                    } else false
                } catch (ignored: IOException) {
                    false
                }
            } else {
                false
            }
        }
    }
}