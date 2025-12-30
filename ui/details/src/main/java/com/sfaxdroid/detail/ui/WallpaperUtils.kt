package com.sfaxdroid.detail.ui

import android.Manifest
import android.app.WallpaperManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream

internal object WallpaperUtils {

    fun saveBitmapToCache(context: Context, bitmap: Bitmap): Uri {
        val wallpapersDir = File(context.cacheDir, "wallpapers")
        if (!wallpapersDir.exists()) {
            wallpapersDir.mkdirs()
        }
        val tempFile = File.createTempFile("wallpaper", ".jpg", wallpapersDir)
        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            tempFile)
    }

    @RequiresPermission(Manifest.permission.SET_WALLPAPER)
    internal fun setWallpaperWithChooser(context: Context, bitmap: Bitmap) {
        val uri = saveBitmapToCache(context, bitmap)

        val intent = WallpaperManager
            .getInstance(context)
            .getCropAndSetWallpaperIntent(uri)

        intent.addFlags(
            Intent.FLAG_GRANT_READ_URI_PERMISSION or
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION
        )

        val contentResolver = context.contentResolver
        val mimeType = contentResolver.getType(uri)
        Log.d("MIME_TYPE", "Type: $mimeType")

        context.startActivity(intent)
    }

}