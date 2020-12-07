package com.sfaxdroid.base

import android.content.Context
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class FileUtils {

    companion object {

        fun getPermanentFile(fileName: String, context: Context, appName: String): File? {
            return File(getPermanentDir(context, appName), fileName)
        }

        fun getFileName(path: String): String? {
            return path.substring(path.lastIndexOf('/') + 1, path.length)
        }

        fun createImageFile(context: Context, appName: String): File? {
            val timeStamp =
                SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US)
                    .format(Date())
            val imageFileName = "IMAGE_" + timeStamp + "_"
            return File.createTempFile(imageFileName, ".jpg", getTemporaryDir(context, appName))
        }

        fun getPermanentDir(context: Context, appName: String): File? {
            val permanentDir = File(
                context.filesDir,
                "$appName/MyWallpaper"
            )
            if (!permanentDir.exists()) {
                permanentDir.mkdirs()
            }
            return permanentDir
        }

        fun getPermanentDirListFiles(context: Context, appName: String): List<File?>? {
            return getListFiles(getPermanentDir(context, appName))
        }

        fun getBasmalaStickersFileList(context: Context, appName: String): List<File?>? {
            return getListFiles(getBasmalaFileDirDir(context, appName))
        }

        fun getTemporaryDir(context: Context, appName: String): File? {
            val temporaryDir = File(
                context.filesDir,
                "$appName/temp"
            )
            if (!temporaryDir.exists()) {
                temporaryDir.mkdirs()
            }
            return temporaryDir
        }

        fun getTemporaryFile(fileName: String?, context: Context, appName: String): File? {
            return File(getTemporaryDir(context, appName), fileName)
        }

        fun isSavedToStorage(fileName: String?, context: Context, appName: String): Boolean {
            val temporaryFile = getTemporaryFile(fileName, context, appName)
            return temporaryFile!!.exists()
        }

        fun deleteFile(url: String?): Boolean {
            val file = File(url)
            return file.delete()
        }

        private fun getBasmalaFileDirDir(context: Context, appName: String): File? {
            val zipDestination = File(
                getTemporaryDir(context, appName),
                Constants.KEY_BASMALA_FOLDER_CONTAINER
            )
            if (!zipDestination.exists()) {
                zipDestination.mkdirs()
            }
            return zipDestination
        }

        private fun getListFiles(parentDir: File?): List<File>? {
            val inFiles = ArrayList<File>()
            val files = parentDir?.listFiles()
            if (files != null) {
                for (file in files) {
                    if (file.isDirectory) {
                        inFiles.addAll(getListFiles(file)!!)
                    } else {
                        inFiles.add(file)
                    }
                }
            }
            return inFiles
        }
    }
}