package com.sfaxdroid.app

import com.sfaxdroid.app.downloadsystem.DecompressZip
import java.io.File

class ZipUtils {
    companion object {
        fun unzipFile(zipFile: File, destination: File) {
            val decompressZip = DecompressZip(
                zipFile.path,
                destination.path + File.separator
            )
            decompressZip.unzip()
        }
    }
}