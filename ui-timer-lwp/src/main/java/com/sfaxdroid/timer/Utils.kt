package com.sfaxdroid.timer

import android.app.WallpaperManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.File
import java.io.IOException

class Utils {

    companion object {
        fun setWallpaperFromFile(
            context: Context,
            files: List<File>,
            screenWidthPixels: Int,
            screenHeightPixels: Int,
            wallpaperNum: Int? = null
        ) {
            files.getOrNull(wallpaperNum ?: 0)
                ?.takeIf { it.exists() }?.also { file ->
                    try {
                        BitmapFactory.decodeFile(
                            file.path,
                            BitmapFactory.Options().apply {
                                inPreferredConfig = Bitmap.Config.ARGB_8888
                            }
                        )?.also {
                            Bitmap.createScaledBitmap(
                                it,
                                screenWidthPixels,
                                screenHeightPixels,
                                true
                            )?.also { btm ->
                                WallpaperManager.getInstance(context)
                                    .setBitmap(btm)
                            }
                            it.recycle()
                        }
                    } catch (ignored: IOException) {
                    }
                }
        }

        fun openAddWallpaperWithKeyActivity(context: Context, key: String) {
            //todo https://developer.android.com/guide/navigation/navigation-deep-link
            try {
                context.startActivity(Intent(
                    context,
                    Class.forName("com.sami.rippel.feature.main.activity.GalleryActivity")
                ).apply {
                    putExtra(com.sfaxdroid.base.Constants.KEY_LWP_NAME, key)
                })
            } catch (ignored: ClassNotFoundException) {
            }
        }
    }
}