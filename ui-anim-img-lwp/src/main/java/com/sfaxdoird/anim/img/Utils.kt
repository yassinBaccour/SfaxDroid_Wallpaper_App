package com.sfaxdoird.anim.img

import android.content.Context
import java.io.File

object Utils {
    @JvmStatic
    fun getTemporaryDirWithFolder(
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
}
