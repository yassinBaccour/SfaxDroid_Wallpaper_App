package com.sfaxdroid.mini.base

import android.app.WallpaperManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast

class Utils {
    companion object {

        fun openPub(context: Context) {
            try {
                context.startActivity(
                    Intent(Intent.ACTION_VIEW).apply {
                        data = Uri.parse(BaseConstants.ADS_URL)
                    }
                )
            } catch (exception: Exception) {
            }
        }

        fun ratingApplication(context: Context) {
            context.startActivity(
                Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse("market://details?id=" + context.packageName)
                }
            )
        }

        inline fun <reified T : Any> openLiveWallpaper(context: Context) {
            try {
                context.startActivity(
                    Intent(
                        WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER
                    ).apply {
                        putExtra(
                            WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
                            ComponentName(
                                context,
                                T::class.java
                            )
                        )
                    }
                )
            } catch (exception: Exception) {
                context.startActivity(
                    Intent().apply {
                        action = WallpaperManager.ACTION_LIVE_WALLPAPER_CHOOSER
                    }
                )
                Toast.makeText(
                    context, R.string.set_wallpaper_failure,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
