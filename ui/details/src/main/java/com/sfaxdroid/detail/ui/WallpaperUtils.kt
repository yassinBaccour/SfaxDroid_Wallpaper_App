package com.sfaxdroid.detail.ui

import android.Manifest
import android.app.WallpaperManager
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import androidx.annotation.RequiresPermission
import kotlin.math.max
import androidx.core.graphics.scale

internal object WallpaperUtils {

    @RequiresPermission(Manifest.permission.SET_WALLPAPER)
    internal fun setWallpaperWithChooser(context: Context, bitmap: Bitmap) {

        val displayMetrics = Resources.getSystem().displayMetrics
        val screenWidth = displayMetrics.widthPixels
        val screenHeight = displayMetrics.heightPixels

        val croppedBitmap = cropBitmapToScreen(bitmap, screenWidth, screenHeight)

        val wallpaperManager = WallpaperManager.getInstance(context)
        wallpaperManager.setBitmap(croppedBitmap)
    }

    private fun cropBitmapToScreen(bitmap: Bitmap, screenWidth: Int, screenHeight: Int): Bitmap {

        val scale = max(
            screenWidth.toFloat() / bitmap.width,
            screenHeight.toFloat() / bitmap.height
        )

        val scaledWidth = (bitmap.width * scale).toInt()
        val scaledHeight = (bitmap.height * scale).toInt()

        val scaledBitmap = bitmap.scale(scaledWidth, scaledHeight)

        val x = (scaledWidth - screenWidth) / 2
        val y = (scaledHeight - screenHeight) / 2

        return Bitmap.createBitmap(
            scaledBitmap,
            x,
            y,
            screenWidth,
            screenHeight
        )
    }

}