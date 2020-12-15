package com.sfaxdoird.anim.img

import android.content.Context
import java.io.File

object Utils {
    @JvmStatic
    fun getTemporaryDir(
        context: Context,
        folder: String,
        appName: String
    ): File {
        val zipDestination =
            File(getTemporaryDir(context, appName), folder)
        if (!zipDestination.exists()) {
            zipDestination.mkdirs()
        }
        return zipDestination
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