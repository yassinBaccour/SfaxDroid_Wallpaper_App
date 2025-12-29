package com.sfaxdroid.detail.ui

import android.app.WallpaperManager
import android.content.Context
import android.graphics.Bitmap

internal object WallpaperUtils {

    internal fun setWallpaperWithChooser(context: Context, bitmap: Bitmap) {
        val wallpaperManager = WallpaperManager.getInstance(context)
        wallpaperManager.setBitmap(bitmap)
    }

}