package com.sfaxdroid.base

import android.app.WallpaperManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.DisplayMetrics
import android.view.WindowManager
import android.widget.Toast

class Utils {
    companion object {

        private fun getDisplayMetrics(context: Context): DisplayMetrics? {
            val mWindowManager =
                context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val displayMetrics = DisplayMetrics()
            return if (mWindowManager != null) {
                mWindowManager.defaultDisplay.getMetrics(displayMetrics)
                displayMetrics
            } else null
        }

        fun getScreenHeightPixels(context: Context): Int {
            return getDisplayMetrics(context)!!.heightPixels
        }

        fun getScreenWidthPixels(context: Context): Int {
            return getDisplayMetrics(context)!!.widthPixels
        }

        fun openPub(context: Context) {
            context.startActivity(Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("market://details?id=com.sami.rippel.allah")
            })
        }

        fun ratingApplication(context: Context) {
            context.startActivity(Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("market://details?id=" + context.packageName)
            })
        }

        fun getBytesDownloaded(progress: Int, totalBytes: Long): String? {
            val bytesCompleted = progress * totalBytes / 100
            if (totalBytes >= 1000000) {
                return "" + String.format(
                    "%.1f",
                    bytesCompleted.toFloat() / 1000000
                ) + "/" + String.format(
                    "%.1f",
                    totalBytes.toFloat() / 1000000
                ) + "MB"
            }
            return if (totalBytes >= 1000) {
                "" + String.format(
                    "%.1f",
                    bytesCompleted.toFloat() / 1000
                ) + "/" + String.format("%.1f", totalBytes.toFloat() / 1000) + "Kb"
            } else {
                "$bytesCompleted/$totalBytes"
            }
        }

        inline fun <reified T : Any> openLiveWallpaper(context: Context) {
            try {
                context.startActivity(Intent(
                    WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER
                ).apply {
                    putExtra(
                        WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
                        ComponentName(
                            context,
                            T::class.java
                        )
                    )
                })
            } catch (exception: Exception) {
                context.startActivity(Intent().apply {
                    action = WallpaperManager.ACTION_LIVE_WALLPAPER_CHOOSER
                })
                Toast.makeText(
                    context, R.string.txt1,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

}