package com.sami.rippel.utils

import android.content.Context
import com.sfaxdoird.anim.img.R
import com.sfaxdroid.base.FileManager
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
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

    override fun getExternalFilesDir() {
        context.getExternalFilesDir("")
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
}