package com.sfaxdroid.base

import java.io.File

interface FileManager {
    fun getTemporaryDirWithFolder(
        folder: String
    ): File
}