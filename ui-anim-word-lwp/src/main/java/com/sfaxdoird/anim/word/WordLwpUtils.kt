package com.sfaxdoird.anim.word

import android.content.Context
import android.graphics.Typeface
import java.io.File

internal class WordLwpUtils {
    companion object {
        fun getTemporaryDir(
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

        fun getTypeFace(context: Context, mTypefaceNum: Int): Typeface? {
            return try {
                Typeface.createFromAsset(
                    context.assets,
                    "arabicfont$mTypefaceNum.ttf"
                )
            } catch (e: Exception) {
                return try {
                    Typeface.createFromAsset(
                        context.assets,
                        "arabicfont$mTypefaceNum.otf"
                    )
                } catch (e: Exception) {
                    Typeface.DEFAULT
                }
            }
        }
    }
}
