package com.sfaxdroid.timer

import android.app.Activity
import android.app.WallpaperManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.sfaxdroid.base.FileUtils.Companion.getPermanentDirListFiles
import com.sfaxdroid.base.Utils.Companion.getScreenHeightPixels
import com.sfaxdroid.base.Utils.Companion.getScreenWidthPixels
import java.io.IOException

class Utils {

    companion object {
        fun setWallpaperFromFile(context: Context, appName: String, wallpaperNum: Int? = null) {
            getPermanentDirListFiles(
                context,
                appName
            )?.getOrNull(wallpaperNum ?: 0)?.takeIf { it.exists() }?.also { file ->
                try {
                    BitmapFactory.decodeFile(
                        file.path,
                        BitmapFactory.Options().apply {
                            inPreferredConfig = Bitmap.Config.ARGB_8888
                        }
                    )?.also {
                        Bitmap.createScaledBitmap(
                            it,
                            getScreenWidthPixels(
                                context
                            ),
                            getScreenHeightPixels(
                                context
                            ),
                            true
                        )?.also { btm ->
                            WallpaperManager.getInstance(context)
                                .setBitmap(btm)
                        }
                        it.recycle()
                    }
                } catch (ignored: IOException) {
                }
            }
        }

        fun haveMinWallpaperInDir(context: Context, appName: String): Boolean {
            return getPermanentDirListFiles(
                context,
                appName
            )?.size ?: 0 > 3
        }

        fun openAddWallpaperWithKeyActivity(activity: Activity, key: String) {
            try {
                activity.startActivity(Intent(
                    activity,
                    Class.forName("com.sami.rippel.feature.main.activity.GalleryActivity")
                ).apply {
                    putExtra(com.sfaxdroid.base.Constants.KEY_LWP_NAME, key)
                })
            } catch (ignored: ClassNotFoundException) {
            }
        }
    }
}