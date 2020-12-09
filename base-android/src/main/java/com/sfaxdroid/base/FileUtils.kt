package com.sfaxdroid.base

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

class FileUtils {

    companion object {

        var mSavedPermanent = false
        const val SAVE_TEMPORARY = 0
        const val SAVE_PERMANENT = 1

        fun savePermanentFile(url: String?, context: Context, appName: String): Boolean {
            val savedFinal: Boolean
            val temp = getTemporaryFile(
                getFileName(url!!),
                context,
                appName
            )
            if (temp != null) {
                try {
                    copyFile(
                        temp,
                        getPermanentFile(
                            getFileName(url)!!,
                            context,
                            appName
                        )
                    )
                    mSavedPermanent = true
                } catch (ignored: IOException) {
                }
            } else {
                Glide.with(context).asBitmap().load(url)
                    .into(object : SimpleTarget<Bitmap?>() {
                        override fun onResourceReady(
                            resource: Bitmap,
                            transition: Transition<in Bitmap?>?
                        ) {
                            mSavedPermanent = saveBitmapToStorage(
                                resource,
                                getFileName(url),
                                SAVE_PERMANENT, context, appName
                            )
                        }
                    })
            }
            return mSavedPermanent
        }

        fun saveBitmapToStorage(
            bitmap: Bitmap, fileName: String?,
            saveOption: Int, context: Context, appName: String
        ): Boolean {
            val temporaryDir =
                getTemporaryDir(
                    context,
                    appName
                )
            val permanentDir =
                getPermanentDir(
                    context,
                    appName
                )
            val file: File
            file = when (saveOption) {
                SAVE_PERMANENT -> File(permanentDir, fileName)
                SAVE_TEMPORARY -> File(temporaryDir, fileName)
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
                if (saveOption == SAVE_PERMANENT) {
                    scanFile(file, context)
                }
                true
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }

        private fun scanFile(file: File, context: Context) {
            val intent =
                Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
            intent.data = Uri.fromFile(file)
            context.sendBroadcast(intent)
        }

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

        @Throws(IOException::class)
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