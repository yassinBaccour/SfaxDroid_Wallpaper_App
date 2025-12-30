package com.sfaxdroid.detail.ui

import android.Manifest
import android.app.WallpaperManager
import android.content.Context
import android.graphics.Bitmap
import androidx.annotation.RequiresPermission

internal object WallpaperUtils {

    @RequiresPermission(Manifest.permission.SET_WALLPAPER)
    internal fun setWallpaperWithChooser(context: Context, bitmap: Bitmap) {
        val wallpaperManager = WallpaperManager.getInstance(context)
        wallpaperManager.setBitmap(bitmap)
    }

}