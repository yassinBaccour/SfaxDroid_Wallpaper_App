package com.sfaxdroid.detail.ui

import android.Manifest
import android.app.WallpaperManager
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Color
import androidx.annotation.RequiresPermission
import androidx.core.graphics.ColorUtils.calculateLuminance
import kotlin.math.max
import androidx.core.graphics.scale
import androidx.palette.graphics.Palette

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

    internal fun isColorDark(bitmap: Bitmap): Boolean {
        val color = extractTopStartColor(bitmap)
        return calculateLuminance(color) < 0.2
    }

    private fun extractTopStartColor(bitmap: Bitmap, size: Int = 48): Int {
        val width = minOf(size, bitmap.width)
        val height = minOf(size, bitmap.height)

        val cropped = Bitmap.createBitmap(bitmap, 0, 0, width, height)

        val palette = Palette.from(cropped)
            .maximumColorCount(8)
            .generate()

        return palette.getDominantColor(Color.BLACK)
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