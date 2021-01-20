package com.sfaxdroid.base.utils

import android.app.WallpaperManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.snackbar.Snackbar

class Utils {
    companion object {

        fun showSnackMessage(
            mRootLayout: CoordinatorLayout,
            message: String
        ) {
            val snackBar = Snackbar
                .make(mRootLayout, message, Snackbar.LENGTH_LONG)
            snackBar.show()
        }

        fun getBytesDownloaded(progress: Int, totalBytes: Long): String {
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
            }
        }

    }

}