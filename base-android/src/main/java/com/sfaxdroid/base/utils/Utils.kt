package com.sfaxdroid.base.utils

import android.Manifest
import android.app.Activity
import android.app.WallpaperManager
import android.content.ComponentName
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.util.DisplayMetrics
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.snackbar.Snackbar
import com.sfaxdroid.base.Constants
import com.sfaxdroid.base.R
import com.sfaxdroid.bases.*
import java.io.File

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

        private fun getDisplayMetrics(context: Context): DisplayMetrics? {
            val mWindowManager =
                context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val displayMetrics = DisplayMetrics()
            return if (mWindowManager != null) {
                mWindowManager.defaultDisplay.getMetrics(displayMetrics)
                displayMetrics
            } else null
        }

        fun getUrlByScreen(
            url: String,
            screenHeightPixels: Int,
            screenWidthPixels: Int
        ): String {
            return if (screenHeightPixels < 820 && screenWidthPixels < 500) url.replace(
                "/islamicimages/",
                "/islamicimagesmini/"
            ) else url
        }

        fun getScreenHeightPixels(context: Context): Int {
            return getDisplayMetrics(context)!!.heightPixels
        }

        fun getScreenWidthPixels(context: Context): Int {
            return getDisplayMetrics(context)!!.widthPixels
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

        fun getUrlByScreenSize(urlToChange: String, context: Context): String {
            var url: String? = ""
            url = if (getScreenHeightPixels(context) < Constants.MIN_HEIGHT && getScreenWidthPixels(
                    context
                ) < Constants.MIN_WIDHT
            ) urlToChange.replace(
                Constants.URL_BIG_WALLPAPER_FOLDER,
                Constants.URL_SMALL_WALLPAPER_FOLDER
            ) else urlToChange
            return url
        }

        fun checkPermission(
            activity: Activity?,
            permissionName: String?,
            onRequestPermissions: () -> Unit
        ) {
            if (ContextCompat.checkSelfPermission(
                    activity!!,
                    permissionName!!
                )
                != PackageManager.PERMISSION_GRANTED
            ) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        activity,
                        permissionName
                    )
                ) {
                    showMessageOKCancel(
                        activity.getString(R.string.permission_storage_message),
                        { _: DialogInterface?, _: Int -> onRequestPermissions() },
                        activity
                    )
                } else {
                    onRequestPermissions()
                }
            }
        }

        private fun showMessageOKCancel(
            message: String,
            okListener: DialogInterface.OnClickListener,
            ac: Activity
        ) {
            AlertDialog.Builder(ac)
                .setMessage(message)
                .setPositiveButton(
                    ac.getString(R.string.permission_accept_click_button),
                    okListener
                )
                .setNegativeButton(ac.getString(R.string.permission_deny_click_button), null)
                .create()
                .show()
        }

        fun appInstalledOrNot(uri: String, context: Context): Boolean {
            val mPackageManager: PackageManager = context.packageManager
            val appInstalled: Boolean
            appInstalled = try {
                mPackageManager.getPackageInfo(
                    uri,
                    PackageManager.GET_ACTIVITIES
                )
                true
            } catch (e: PackageManager.NameNotFoundException) {
                false
            }
            return appInstalled
        }


        fun isSmallScreen(context: Context): Boolean {
            return getScreenHeightPixels(context) < 820
                    && getScreenWidthPixels(context) < 500
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