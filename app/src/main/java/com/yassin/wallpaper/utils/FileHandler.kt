package com.yassin.wallpaper.utils

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import com.sfaxdoird.anim.img.R
import com.sfaxdroid.base.Constants
import com.sfaxdroid.base.FileManager
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FileHandler @Inject constructor(@ApplicationContext private val context: Context) :
    FileManager {

    override fun getTemporaryDirWithFolder(folder: String): File {
        return provideTemporaryDirWithFolder(
            context,
            folder,
            context.getString(R.string.app_namenospace)
        )
    }

    override fun getTemporaryDirWithFile(fileName: String): File {
        return File(getTemporaryDir(), fileName)
    }

    override fun getExternalFilesDir(): File {
        return getTemporaryDir(context, context.getString(R.string.app_namenospace))
    }

    override fun getTemporaryDir(): File {
        return getTemporaryDir(context, context.getString(R.string.app_namenospace))
    }

    override fun getPermanentDir(): File {
        val appName = context.getString(R.string.app_namenospace)
        val permanentDir = File(
            context.filesDir,
            "$appName/MyWallpaper"
        )
        if (!permanentDir.exists()) {
            permanentDir.mkdirs()
        }
        return permanentDir
    }

    override fun savePermanentFile(url: String): Boolean {
        val temp = getTemporaryDirWithFile(
            getFileName(url),
        )
        return try {
            copyFile(
                temp, File(getPermanentDir(), getFileName(url))
            )
            true
        } catch (ignored: IOException) {
            false
        }
    }

    override fun saveBitmapToStorage(
        bitmap: Bitmap,
        fileName: String,
        saveOption: Int
    ): Boolean {
        val temporaryDir =
            getTemporaryDir()
        val permanentDir =
            getPermanentDir()
        val file: File = when (saveOption) {
            Constants.SAVE_PERMANENT -> File(permanentDir, fileName)
            Constants.SAVE_TEMPORARY -> File(temporaryDir, fileName)
            else -> File(temporaryDir, fileName)
        }
        if (file.exists()) {
            file.delete()
        }
        return try {
            val out = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
            out.flush()
            out.close()
            if (saveOption == Constants.SAVE_PERMANENT) {
                scanFile(file)
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    override fun getPermanentDirListFiles(): List<File> {
        return getListFiles(getPermanentDir())
    }

    private fun scanFile(file: File) {
        context.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).apply {
            data = Uri.fromFile(file)
        })
    }

    private fun getListFiles(parentDir: File): List<File> {
        val inFiles = arrayListOf<File>()
        val files = parentDir.listFiles()
        if (!files.isNullOrEmpty()) {
            for (file in files) {
                if (file.isDirectory) {
                    inFiles.addAll(getListFiles(file))
                } else {
                    inFiles.add(file)
                }
            }
        }
        return inFiles
    }

    private fun provideTemporaryDirWithFolder(
        context: Context,
        folder: String,
        appName: String
    ): File {
        val tempFolder =
            File(getTemporaryDir(context, appName), folder)
        if (!tempFolder.exists()) {
            tempFolder.mkdirs()
        }
        return tempFolder
    }

    private fun getTemporaryDir(
        context: Context,
        appName: String
    ): File {
        val temporaryDir = File(
            context.filesDir,
            "$appName/temp"
        )
        if (!temporaryDir.exists()) {
            temporaryDir.mkdirs()
        }
        return temporaryDir
    }


    private fun copyFile(
        sourceLocation: File,
        targetLocation: File?
    ) {
        if (sourceLocation.isDirectory) {
            if (targetLocation?.exists() == false) {
                targetLocation.mkdir()
            }
            val children = sourceLocation.list()
            for (i in sourceLocation.listFiles().indices) {
                copyFile(
                    File(sourceLocation, children[i]),
                    File(targetLocation, children[i])
                )
            }
        } else {
            val `in`: InputStream = FileInputStream(sourceLocation)
            val out: OutputStream = FileOutputStream(targetLocation)
            val buf = ByteArray(1024)
            var len: Int
            while (`in`.read(buf).also { len = it } > 0) {
                out.write(buf, 0, len)
            }
            `in`.close()
            out.close()
        }
    }

    private fun getFileName(path: String): String {
        return path.substring(path.lastIndexOf('/') + 1, path.length)
    }
}