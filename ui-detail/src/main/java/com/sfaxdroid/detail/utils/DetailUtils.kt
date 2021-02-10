package com.sfaxdroid.detail.utils

import android.app.Activity
import android.app.WallpaperManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.sfaxdroid.base.Constants
import com.sfaxdroid.base.FileManager
import com.sfaxdroid.base.R
import com.sfaxdroid.base.extension.getFileName
import com.sfaxdroid.detail.ActionTypeEnum
import com.sfaxdroid.detail.IntentType
import java.io.File
import java.io.IOException

class DetailUtils {

    companion object {

        fun saveToFileToTempsDirAndChooseAction(
            url: String,
            action: ActionTypeEnum,
            context: Context,
            fileManager: FileManager,
            doActionAfterSave: (isSaved: Boolean, action: ActionTypeEnum) -> Unit
        ) {
            Glide.with(context).asBitmap().load(
                url
            )
                .into(object : SimpleTarget<Bitmap?>() {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap?>?
                    ) {
                        val isSaved =
                            fileManager.saveBitmapToStorage(
                                resource,
                                url.getFileName(),
                                Constants.SAVE_TEMPORARY,
                            )
                        doActionAfterSave(isSaved, action)
                        resource.recycle()
                    }
                })
        }

        private fun getIntentNameFromType(intentType: IntentType): String {
            return when (intentType) {
                is IntentType.FACEBOOK -> {
                    Constants.FB_PACKAGE
                }
                is IntentType.INSTAGRAM -> {
                    Constants.INSTAGRAM_PACKAGE
                }
                is IntentType.SNAP -> {
                    Constants.SNAP_PACKAGE
                }
            }
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

        fun shareFileWithIntentType(
            activity: Activity,
            file: File,
            intentType: IntentType
        ): Boolean {
            return if (appInstalledOrNot(getIntentNameFromType(intentType), activity)
            ) {
                activity.startActivity(Intent(Intent.ACTION_SEND).apply {
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET)
                    type = "ic_icon_image/*"
                    putExtra(
                        Intent.EXTRA_STREAM, FileProvider.getUriForFile(
                            activity,
                            activity.applicationContext.packageName + ".provider",
                            file
                        )
                    )
                    setPackage(
                        getIntentNameFromType(
                            intentType
                        )
                    )
                })
                true
            } else false
        }

        fun shareAllFile(activity: Activity, file: File?) {
            activity.startActivityForResult(
                Intent.createChooser(
                    Intent(Intent.ACTION_ATTACH_DATA).apply {
                        setDataAndType(
                            FileProvider.getUriForFile(
                                activity, activity.applicationContext.packageName
                                        + ".provider", file!!
                            ), "ic_icon_image/jpg"
                        )
                        putExtra("mimeType", "ic_icon_image/jpg")
                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    },
                    activity.getString(R.string.set_as_dialog_message)
                ), 200
            )
        }

        fun setWallpaper(wallpaper: Bitmap, context: Context, screenWidthPixels: Int): Boolean {
            return try {
                val wallpaperManager =
                    WallpaperManager.getInstance(context)
                val width = wallpaperManager.desiredMinimumWidth
                val height = wallpaperManager.desiredMinimumHeight
                if (width > wallpaper.width && height > wallpaper.height && screenWidthPixels < Constants.MIN_WIDHT
                ) {
                    val xPadding = Math.max(0, width - wallpaper.width) / 2
                    val yPadding = Math.max(0, height - wallpaper.height) / 2
                    val pixels =
                        IntArray(wallpaper.width * wallpaper.height)
                    wallpaper.getPixels(
                        pixels,
                        0,
                        wallpaper.width,
                        0,
                        0,
                        wallpaper.width,
                        wallpaper.height
                    )
                    wallpaperManager.setBitmap(Bitmap.createBitmap(
                        width,
                        height,
                        Bitmap.Config.ARGB_8888
                    ).apply {
                        setPixels(
                            pixels,
                            0,
                            wallpaper.width,
                            xPadding,
                            yPadding,
                            wallpaper.width,
                            wallpaper.height
                        )
                    })
                    true
                } else {
                    wallpaperManager.setBitmap(wallpaper)
                    true
                }
            } catch (e: IOException) {
                false
            }
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

        fun decodeBitmapAndSetAsLiveWallpaper(
            file: File,
            context: Context,
            screenWidthPixels: Int,
            screenHeightPixels: Int
        ): Boolean {
            if (file.exists()) {
                val wallpaperManager =
                    WallpaperManager.getInstance(context)
                try {
                    BitmapFactory.decodeFile(
                        file.path,
                        BitmapFactory.Options().apply {
                            inPreferredConfig = Bitmap.Config.ARGB_8888
                        }
                    )?.also {
                        val mBackground =
                            Bitmap.createScaledBitmap(
                                it,
                                screenWidthPixels,
                                screenHeightPixels,
                                true
                            )
                        if (mBackground != null) wallpaperManager.setBitmap(mBackground)
                        it.recycle()
                        return true
                    }
                    return false
                } catch (ignored: IOException) {
                    return false
                }
            } else {
                return false
            }
        }
    }
}