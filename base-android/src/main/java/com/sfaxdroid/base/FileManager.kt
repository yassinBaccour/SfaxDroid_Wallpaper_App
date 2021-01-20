package com.sfaxdroid.base

import android.graphics.Bitmap
import java.io.File

interface FileManager {

    fun getTemporaryDirWithFolder(
        folder: String
    ): File

    fun getTemporaryDirWithFile(
        fileName: String
    ): File

    fun getExternalFilesDir(): File

    fun getTemporaryDir(): File

    fun getPermanentDir(): File

    fun savePermanentFile(url: String): Boolean

    fun saveBitmapToStorage(
        bitmap: Bitmap,
        fileName: String,
        saveOption: Int
    ): Boolean

    fun getPermanentDirListFiles(): List<File>
}